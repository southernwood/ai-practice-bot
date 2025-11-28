import { DislikeOutlined, LikeOutlined } from "@ant-design/icons";
import { Tooltip } from "antd";
import { useState } from "react";
import { sendFeedback } from "../../services/ajaxService";

export interface ReviewButtonsProps {
  userId: string;
  question: string;
  answer: string;
  embeddingIds: number[];
}

export const ReviewButtons: React.FC<ReviewButtonsProps> = ({
  userId,
  question,
  answer,
  embeddingIds,
}) => {
  const [feedback, setFeedback] = useState<"up" | "down" | null>(null);

  const handleFeedback = async (isHelpful: boolean) => {
    if (feedback !== null) return;

    try {
      await sendFeedback({
        userId,
        question,
        answer,
        embeddingIds,
        isHelpFul: isHelpful,
      });

      setFeedback(isHelpful ? "up" : "down");
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div style={{ display: "flex", gap: 14, marginTop: 6 }}>
      <Tooltip title="Helpful">
        <LikeOutlined
          onClick={() => handleFeedback(true)}
          style={{
            fontSize: 18,
            cursor: feedback === null ? "pointer" : "default",
            color: feedback === "up" ? "#4f9aff" : "#999",
            opacity: feedback === null ? 1 : 0.5,
          }}
        />
      </Tooltip>

      <Tooltip title="Not helpful">
        <DislikeOutlined
          onClick={() => handleFeedback(false)}
          style={{
            fontSize: 18,
            cursor: feedback === null ? "pointer" : "default",
            color: feedback === "down" ? "#ff7675" : "#999",
            opacity: feedback === null ? 1 : 0.5,
          }}
        />
      </Tooltip>
    </div>
  );
};
