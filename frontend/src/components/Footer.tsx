export const Footer = () => {
  return (
    <footer className="w-full border-t border-[#1f2933] mt-24 py-8 px-6 text-sm text-[#8b95a5]">
      <div className="max-w-[1600px] mx-auto flex flex-col md:flex-row items-center justify-between gap-4">
        <span>© 2025 Weiqing Wang</span>
        <span className="text-[#6b7280]">
          Built with React · TypeScript · Tailwind · Node.js · OpenAI · n8n ·
          PostgreSQL
        </span>
      </div>
    </footer>
  );
};
