import { Button, Input } from "antd";
import React, { useState } from "react";

const { TextArea } = Input;

interface ChatInputProps {
  onSend: (message: string) => void;
  isSending: boolean;
}

export const ChatInput: React.FC<ChatInputProps> = ({ onSend, isSending }) => {
  const [input, setInput] = useState("");

  const handleSend = () => {
    if (!input.trim() || isSending) return;
    onSend(input.trim());
    setInput("");
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter") {
      if (e.shiftKey) {
        return;
      } else {
        e.preventDefault();
        handleSend();
      }
    }
  };

  return (
    <div
      style={{
        display: "flex",
        gap: 8,
        padding: 12,
        borderTop: "1px solid #333",
        backgroundColor: "#1f1f1f",
      }}
    >
      <TextArea
        value={input}
        onChange={(e) => setInput(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Type a message..."
        rows={3}
        style={{
          flex: 1,
          borderRadius: 20,
          backgroundColor: "#2b2b2b",
          color: "#f5f5f5",
          border: "1px solid #444",
          boxShadow: "0 2px 6px rgba(0,0,0,0.3)",
          transition: "all 0.3s ease",
          resize: "none",
        }}
      />
      <Button
        type="primary"
        onClick={handleSend}
        style={{
          borderRadius: 20,
          background: "linear-gradient(45deg, #4f9aff, #1f6fff)",
          border: "none",
          color: "#fff",
          fontWeight: 600,
          boxShadow: "0 2px 8px rgba(79,154,255,0.5)",
          transition: "all 0.2s ease",
        }}
        disabled={isSending}
      >
        Send
      </Button>
    </div>
  );
};
