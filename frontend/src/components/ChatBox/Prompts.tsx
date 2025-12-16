import React from "react";

type PromptsProps = {
  prompts?: string[];
  onClickPrompt: (prompt: string) => void;
};

export const Prompts: React.FC<PromptsProps> = ({ prompts, onClickPrompt }) => {
  const defaultPrompts = [
    "Tell me about yourself",
    "What is the project you are most proud of?",
    "What role do you usually play in a team?",
  ];

  const displayPrompts = prompts || defaultPrompts;

  return (
    <div
      className="flex flex-wrap gap-2 px-3 py-2 fixed "
      style={{ bottom: "110px" }}
    >
      {displayPrompts.map((prompt, idx) => (
        <button
          key={idx}
          onClick={() => onClickPrompt(prompt)}
          className="bg-gradient-to-r from-[#392467] via-[#5D3587] to-[#5D3587] text-gray-100
                     px-4 py-2 rounded-full shadow-md 
                     hover:shadow-lg hover:scale-105 transition-all duration-200"
        >
          {prompt}
        </button>
      ))}
    </div>
  );
};
