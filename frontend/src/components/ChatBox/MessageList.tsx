import React, { useEffect, useRef } from "react";
import { BotTyping } from "./BotTyping";
import type { Message } from "./ChatBox";
import { MessageItem } from "./MessageItem";

interface MessageListProps {
  messages: Message[];
  isSending: boolean;
}

export const MessageList: React.FC<MessageListProps> = ({
  messages,
  isSending,
}) => {
  const listRef = useRef<HTMLDivElement>(null);
  console.log('message list', messages)
  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTo({
        top: listRef.current.scrollHeight,
        behavior: "smooth",
      });
    }
  }, [messages]);

  return (
    <div
      ref={listRef}
      style={{
        flex: 1,
        overflowY: "auto",
        padding: "16px",
        display: "flex",
        flexDirection: "column",
        gap: 8,
        background: "linear-gradient(to bottom, #1f1f1f, #121212)", // dark gradient background
        borderRadius: 16,
        boxShadow: "0 4px 12px rgba(0,0,0,0.2)",
        scrollbarWidth: "thin",
        scrollbarColor: "#555 #1f1f1f",
      }}
    >
      {messages.map((msg, index) => (
        <MessageItem key={index} message={msg} />
      ))}
      {isSending && <BotTyping />}

      <style>
        {`
          /* Firefox */
          div::-moz-scrollbar {
            width: 8px;
          }
          div::-moz-scrollbar-track {
            background: #1f1f1f;
            border-radius: 4px;
          }
          div::-moz-scrollbar-thumb {
            background-color: #555;
            border-radius: 4px;
          }

          /* Chrome, Edge, Safari */
          div::-webkit-scrollbar {
            width: 8px;
          }
          div::-webkit-scrollbar-track {
            background: #1f1f1f;
            border-radius: 4px;
          }
          div::-webkit-scrollbar-thumb {
            background-color: #555;
            border-radius: 4px;
          }
        `}
      </style>
    </div>
  );
};
