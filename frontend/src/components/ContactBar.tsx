import { Github, Linkedin, Mail } from "lucide-react";

export const ContactBar = () => {
  return (
    <div className="flex flex-wrap items-center gap-6 pt-4 text-sm">
      {/* GitHub */}
      <a
        href="https://github.com/southernwood"
        target="_blank"
        rel="noopener noreferrer"
        className="group flex items-center gap-2 text-[#a9b0bb] hover:text-[#e6eef8] transition-colors"
      >
        <Github className="w-5 h-5 text-[#9ca3af] group-hover:text-[#e6eef8] transition-colors" />
        <span>GitHub</span>
      </a>

      {/* LinkedIn */}
      <a
        href="https://www.linkedin.com/in/weiqing-wang-40a253115/"
        target="_blank"
        rel="noopener noreferrer"
        className="group flex items-center gap-2 text-[#a9b0bb] hover:text-[#e6eef8] transition-colors"
      >
        <Linkedin className="w-5 h-5 text-[#9ca3af] group-hover:text-[#38bdf8] transition-colors" />
        <span>LinkedIn</span>
      </a>

      {/* Email */}
      <a
        href="mailto:southernwood.wang@gmail.com"
        className="group flex items-center gap-2 text-[#a9b0bb] hover:text-[#e6eef8] transition-colors"
      >
        <Mail className="w-5 h-5 text-[#9ca3af] group-hover:text-[#a855f7] transition-colors" />
        <span>southernwood.wang@gmail.com</span>
      </a>
    </div>
  );
};
