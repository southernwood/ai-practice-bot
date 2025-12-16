import { Tooltip } from "antd";
import "antd/dist/reset.css";
import React from "react";

export interface ChatBubbleProps {
  onClick: () => void;
}
export const ChatBubble: React.FC<ChatBubbleProps> = ({ onClick }) => {
  return (
    <div className="fixed bottom-8">
      <Tooltip title="Chat with me" placement="top" mouseEnterDelay={0.3}>
        <div
          className="w-16 h-16 md:w-20 md:h-20 rounded-full
            bg-gradient-to-br from-[#101216] to-[#26292f]
            shadow-lg shadow-purple-900/20
            flex items-center justify-center
            cursor-pointer
            animate-bounce-slow
            hover:scale-110
            hover:animate-none"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="w-8 h-8 text-purple-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={2}
            onClick={onClick}
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M8 10h.01M12 10h.01M16 10h.01M21 12c0 4.418-4.03 8-9 8s-9-3.582-9-8 4.03-8 9-8 9 3.582 9 8z"
            />
          </svg>
        </div>
      </Tooltip>
    </div>
  );
};
