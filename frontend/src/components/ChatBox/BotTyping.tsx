import { theme } from "antd";
import { motion } from "framer-motion";
import React from "react";

export const BotTyping: React.FC = () => {
  const { token } = theme.useToken();
  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        padding: "8px 12px",
        borderRadius: 12,
        backgroundColor: "transparent",
        maxWidth: "70%",
        marginBottom: 8,
      }}
    >
      <motion.div
        style={{
          display: "flex",
          gap: 4,
        }}
      >
        {[0, 1, 2].map((i) => (
          <motion.div
            key={i}
            style={{
              width: 8,
              height: 8,
              borderRadius: "50%",
              background: `linear-gradient(135deg, ${token.colorPrimary}, ${token.colorPrimaryHover})`,

              boxShadow: `0 0 4px ${token.colorPrimary}66`,
            }}
            animate={{ y: [0, -6, 0] }}
            transition={{
              duration: 0.6,
              repeat: Infinity,
              delay: i * 0.2,
            }}
          />
        ))}
      </motion.div>
    </div>
  );
};
