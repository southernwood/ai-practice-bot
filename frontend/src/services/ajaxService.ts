import axios, { type AxiosResponse } from "axios";
import type { FeedbackPayload } from "../models/Feedback";
import type { MessagePayload } from "../models/chat";

const BASE_URL = `${import.meta.env.VITE_API_URL}/api`;

console.log("base url", BASE_URL);

export async function sendFeedback(payload: FeedbackPayload) {
  try {
    const res = await axios.post(`${BASE_URL}/feedback`, payload, {
      headers: { "Content-Type": "application/json" },
    });

    return res.data; 
  } catch (error: any) {
    console.error("Feedback API failed:", error.response || error.message);
    return error;
  }
}

export const sendChat = (payload : MessagePayload) : Promise<AxiosResponse<any>> => {
  return axios.post(`${BASE_URL}/chat`, payload, {
    headers: { "Content-Type": "application/json" },
  });
}

export const uploadFile = (formData: FormData) => {
  return axios.post(
    `${BASE_URL}/embeddings/upload-file`,
    formData,
    {
      headers: { "Content-Type": "multipart/form-data" },
    },
  )
}