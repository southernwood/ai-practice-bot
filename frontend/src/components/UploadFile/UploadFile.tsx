import { UploadOutlined } from "@ant-design/icons";
import { Button, message, Upload } from "antd";
import type { UploadFile } from "antd/es/upload/interface";
import axios from "axios";
import React, { useState } from "react";

export const UploadFileButton: React.FC = () => {
  const [fileList, setFileList] = useState<UploadFile[]>([]);

  const handleUpload = async () => {
    if (fileList.length === 0) {
      message.warning("Please select a file first.");
      return;
    }

    const formData = new FormData();
    formData.append("file", fileList[0].originFileObj as File);

    try {
      const response = await axios.post(
        "/api/embeddings/upload-file",
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
        },
      );
      message.success(response.data);
      setFileList([]);
    } catch (error) {
      console.error(error);
      message.error("Failed to upload file.");
    }
  };

  return (
    <div style={{ maxWidth: 600, margin: "50px auto" }}>
      <h2>Upload a Text File for Embedding</h2>
      <Upload
        beforeUpload={(file) => {
          setFileList([
            {
              uid: file.uid,
              name: file.name,
              status: "done",
              originFileObj: file,
            },
          ]);
          return false;
        }}
        fileList={fileList}
        onRemove={() => setFileList([])}
      >
        <Button icon={<UploadOutlined />}>Select File</Button>
      </Upload>
      <Button type="primary" onClick={handleUpload} style={{ marginTop: 10 }}>
        Upload File
      </Button>
    </div>
  );
};
