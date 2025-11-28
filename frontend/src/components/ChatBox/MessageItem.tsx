import { Avatar } from "antd";
import { motion } from "framer-motion";
import type { Message } from "./ChatBox";
import { ReviewButtons } from "./ReviewButtons";
interface MessageItemProps {
  timestamp?: string;
  message: Message;
}

export const MessageItem: React.FC<MessageItemProps> = ({
  timestamp,
  message,
}) => {
  console.log("message", message)
  const { sender, content, question, embeddingIds } = message;
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.3 }}
      style={{
        display: "flex",
        justifyContent: sender === "user" ? "flex-end" : "flex-start",
        marginBottom: 12,
      }}
    >
      {sender === "bot" && (
        <Avatar
          size="small"
          style={{ marginRight: 8, backgroundColor: "#1f6fff", color: "#fff" }}
        >
          B
        </Avatar>
      )}
      <div
        style={{
          padding: "10px 16px",
          borderRadius: 16,
          backgroundColor: sender === "user" ? "#4f9aff" : "#2b2b2b",
          color: sender === "user" ? "#fff" : "#f5f5f5",
          maxWidth: "70%",
          wordBreak: "break-word",
          boxShadow:
            sender === "user"
              ? "0 2px 8px rgba(79,154,255,0.5)"
              : "0 2px 8px rgba(0,0,0,0.3)",
          transition: "all 0.2s ease",
        }}
      >
        {message.content}
        {timestamp && (
          <div
            style={{
              fontSize: 10,
              textAlign: "right",
              marginTop: 4,
              color: sender === "user" ? "#cce4ff" : "#999",
            }}
          >
            {timestamp}
          </div>
        )}
      </div>
      {sender === "user" && (
        <Avatar
          size="small"
          style={{ marginLeft: 8, backgroundColor: "#36D1DC", color: "#fff" }}
        >
          U
        </Avatar>
      )}
      {sender === "bot" && (
        <div style={{ paddingLeft: 36 }}>
          <ReviewButtons
            userId={"testing"}
            question={question!}
            answer={content}
            embeddingIds={embeddingIds!}
          />
        </div>
      )}
    </motion.div>
  );
};
