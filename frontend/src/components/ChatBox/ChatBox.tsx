import axios from "axios";
import React, { useState } from "react";
import { ChatInput } from "./ChatInput";
import { MessageList } from "./MessageList";

export type Message  = {
  content: string;
  sender: "user" | "bot";
  timestamp?: string;
  embeddingIds?: number[];
  question?: string;
}

export const ChatBox: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [isSending, setIsSending] = useState(false);

  const sendMessage = async (content: string) => {
    const userMessage: Message = {
      content,
      sender: "user",
      timestamp: new Date().toLocaleTimeString(),
      question: content
    };
    setMessages((prev) => [...prev, userMessage]);
    setIsSending(true);
    try {
      const res = await axios.post(
        "http://localhost:8080/api/chat?userId=Lisa",
        { message: content, userId: 'testing'},
      );
      setIsSending(false);
      const botMessage: Message = {
        content: res.data.answer,
        sender: "bot",
        embeddingIds: res.data.embeddingIds,
        timestamp: new Date().toLocaleTimeString(),
        question: res.data.question
      };
      setMessages((prev) => [...prev, botMessage]);
    } catch (err) {
      console.error(err);
      const errorMsg: Message = {
        content: "Error: could not get response",
        sender: "bot",
        timestamp: new Date().toLocaleTimeString(),
      };
      setIsSending(false);
      setMessages((prev) => [...prev, errorMsg]);
    }
  };

  return (
    <div
      style={{
        width: 400,
        height: 600,
        backgroundColor: "#1f1f1f",
        borderRadius: 12,
        display: "flex",
        flexDirection: "column",
        overflow: "hidden",
        boxShadow: "0 4px 12px rgba(0,0,0,0.3)",
      }}
    >
      <MessageList messages={messages} isSending={isSending} />
      <ChatInput onSend={sendMessage} isSending={isSending} />
    </div>
  );
};
