// src/App.tsx
import { ConfigProvider } from "antd";
import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import { ChatBox } from "./components/ChatBox/ChatBox";
import { UploadFileButton } from "./components/UploadFile/UploadFile";
import "./styles/global.css";
import theme from "./theme";

const App: React.FC = () => {
  return (
    <ConfigProvider theme={theme}>
      <Router>
        <div className="app-shell">
          {/* Routes */}
          <Routes>
            <Route
              path="/chat"
              element={
                <div className="chat-frame">
                  <div className="chat-panel">
                    <ChatBox />
                  </div>
                </div>
              }
            />
            <Route path="/upload-file" element={<UploadFileButton />} />
          </Routes>
        </div>
      </Router>
    </ConfigProvider>
  );
};

export default App;
