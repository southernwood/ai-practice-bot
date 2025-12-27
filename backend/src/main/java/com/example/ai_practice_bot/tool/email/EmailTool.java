package com.example.ai_practice_bot.tool.email;

import com.example.ai_practice_bot.dto.SendEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class EmailTool {

    private static final Logger log = LoggerFactory.getLogger(EmailTool.class);

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api-key:}")
    private String brevoApiKey;

    @Value("${brevo.sender.email:}")
    private String senderEmail;

    @Value("${brevo.sender.name:}")
    private String senderName;


    @Tool(
            name = "send_email",
            description = "Send an email to the site owner on behalf of a visitor"
    )
    public String sendEmail(SendEmailRequest request) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            Map<String, Object> payload = Map.of(
                    "sender", Map.of(
                            "email", senderEmail,
                            "name", senderName
                    ),
                    "to", List.of(
                            Map.of(
                                    "email", senderEmail,
                                    "name", senderName
                            )
                    ),
                    "subject", request.subject(),
                    "htmlContent", buildHtmlContent(request)
            );

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(payload, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(BREVO_API_URL, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully via Brevo.");
                return "✅ Email sent successfully.";
            }

            log.error("Brevo error response: {}", response.getBody());
            return "❌ Failed to send email via Brevo.";

        } catch (Exception e) {
            log.error("Failed to send email via Brevo", e);
            return "❌ Failed to send email: " + e.getMessage();
        }
    }

    private String buildHtmlContent(SendEmailRequest request) {
        return """
            <p><strong>From:</strong> %s (%s)</p>
            <p><strong>Company:</strong> %s</p>
            <hr/>
            <p>%s</p>
            """.formatted(
                request.fromName(),
                request.fromEmail(),
                request.fromCompany(),
                request.body().replace("\n", "<br/>")
        );
    }
}
