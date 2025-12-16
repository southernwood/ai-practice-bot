import { AnimatePresence, motion } from "framer-motion";
import { ChevronDown, ChevronUp } from "lucide-react";
import { useState } from "react";
import { Badge } from "../ui/badge";
import type { TimelineItemType } from "./Timeline";

interface TimelineItemProps {
  item: TimelineItemType;
  linePosition: "left" | "right";
  isLast?: boolean;
}

export const TimelineItem: React.FC<TimelineItemProps> = ({
  item,
  linePosition,
  isLast,
}) => {
  const [open, setOpen] = useState(false);
  const colors = {
    work: {
      dot: "#A855F7",
      border: "#C084FC",
      title: "text-purple-300",
      summary: "text-purple-100",
      details: "text-purple-200",
      badgeFrom: "from-purple-500/40",
      badgeTo: "to-blue-500/40",
    },
    education: {
      dot: "#9DECF9",
      border: "#C8F6FF",
      title: "text-[#B3F8FF]",
      summary: "text-[#E8FCFF]",
      details: "text-[#C9F8FF]",
      badgeFrom: "from-[#9DECF9]/50",
      badgeTo: "to-[#67E3F0]/50",
    },
  };

  const palette = colors[item.type || "work"];

  return (
    <div className={`relative ${linePosition === "right" ? "pr-12" : "pl-12"}`}>
      {/* Dot */}
      <motion.div
        className={`absolute top-1 ${linePosition === "right" ? "right-3" : "left-3"} w-4 h-4 rounded-full border-2 shadow-[0_0_10px_rgba(168,85,247,0.6)]`}
        style={{
          backgroundColor: palette.dot,
          borderColor: palette.border,
        }}
        animate={{ scale: open ? 1.3 : 1 }}
      />

      {/* Connecting line */}
      {!isLast && (
        <div
          className={`absolute ${linePosition === "right" ? "right-4" : "left-4"} top-8`}
          style={{
            height: "calc(100%)",
            borderLeft: "5px dotted",
            borderColor: palette.dot,
            filter: "blur(2px)",
            opacity: 0.5,
          }}
        />
      )}

      <div className="bg-white/5 backdrop-blur-sm border border-white/10 rounded-xl p-5 shadow-md hover:shadow-xl transition-all mt-5">
        {/* Header */}
        <div
          className="flex justify-between items-center cursor-pointer"
          onClick={() => setOpen(!open)}
        >
          <div>
            <h3 className={`text-xl font-semibold ${palette.title}`}>
              {item.title}
            </h3>
            <p className="text-sm opacity-70">{item.time}</p>
          </div>
          {open ? (
            <ChevronUp className="w-5 h-5 opacity-70" />
          ) : (
            <ChevronDown className="w-5 h-5 opacity-70" />
          )}
        </div>

        {/* Summary */}
        <p className={`mt-2 font-medium text-base ${palette.summary}`}>
          {item.summary[0]}
        </p>

        {/* Expandable details */}
        <AnimatePresence>
          {open && (
            <motion.div
              initial={{ opacity: 0, height: 0 }}
              animate={{ opacity: 1, height: "auto" }}
              exit={{ opacity: 0, height: 0 }}
              transition={{ duration: 0.3 }}
            >
              <div className="mt-4 border-t border-white/10 pt-4">
                {item.details && (
                  <>
                    <h4 className={`font-medium ${palette.details} mb-2`}>
                      Details
                    </h4>
                    <ul className="list-disc ml-5 text-sm opacity-90 space-y-1">
                      {item.details.map((line, idx) => (
                        <li key={idx}>{line}</li>
                      ))}
                    </ul>
                  </>
                )}

                {item.skills && (
                  <>
                    <h4 className={`font-medium ${palette.details} mt-4 mb-2`}>
                      Skills
                    </h4>
                    <div className="flex flex-wrap gap-2">
                      {item.skills.map((skill, idx) => (
                        <Badge
                          key={idx}
                          className={`bg-gradient-to-r ${palette.badgeFrom} ${palette.badgeTo} border border-white/10 text-white shadow`}
                        >
                          {skill}
                        </Badge>
                      ))}
                    </div>
                  </>
                )}
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </div>
  );
};
