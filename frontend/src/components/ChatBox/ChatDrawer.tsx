import { CloseOutlined } from "@ant-design/icons";
import { Drawer } from "antd";
import { ChatBox } from "./ChatBox";

type ChatDrawerProps = {
  open: boolean;
  onClose: () => void;
};

export const ChatDrawer: React.FC<ChatDrawerProps> = ({ open, onClose }) => {
  return (
    <Drawer
      open={open}
      onClose={onClose}
      placement="right"
      width={450}
      closable={false}
      title={
        <div className="flex items-center  w-full">
          <span className="text-gray-200 font-medium">Lisa Bot</span>
          <span className="text-xs text-green-400 ml-4">● Online</span>
        </div>
      }
      extra={
        <CloseOutlined
          onClick={onClose}
          className="cursor-pointer text-gray-400 hover:text-gray-200"
        />
      }
      styles={{
        header: {
          background: "#1f1f1f",
          borderBottom: "1px solid #2a2a2a",
        },
        body: {
          padding: 0,
          background: "#1f1f1f",
        },
      }}
    >
      <div className="h-full">
        <ChatBox />
      </div>
    </Drawer>
  );
};
