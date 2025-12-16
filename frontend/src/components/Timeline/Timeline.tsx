import { TimelineItem } from "./TimelineItem";

export type TimelineItemType = {
  title: string;
  time: string;
  summary: string[];
  details?: string[];
  skills?: string[];
  type?: "work" | "education";
};

export type TimelineGroupType = {
  title: string;
  items: TimelineItemType[];
  linePosition?: "left" | "right";
  dotColor?: string;
  dotBorderColor?: string;
};

interface TimelineProps {
  groups: TimelineGroupType[];
}

export const Timeline: React.FC<TimelineProps> = ({ groups }) => {
  return (
    <div className="relative flex flex-col gap-16 w-full max-w-[1600px] mx-auto">
      {groups.map((group, idx) => (
        <div key={idx} className="relative">
          <h2
            className="text-3xl font-semibold mb-8"
            style={{
              color: group.linePosition === "right" ? "#8FF8FF" : "#C084FC",
            }}
          >
            {group.title}
          </h2>

          <div className="flex flex-col gap-10 relative">
            {group.items.map((item, index) => (
              <TimelineItem
                key={index}
                item={item}
                linePosition={group.linePosition || "left"}
                isLast={index === group.items.length - 1}
              />
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};
