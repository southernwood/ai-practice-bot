import axios from "axios";
import type { FeedbackPayload } from "../models/Feedback";

const BASE_URL = "http://localhost:8080/api";

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