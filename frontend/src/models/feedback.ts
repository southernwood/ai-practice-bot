export type FeedbackPayload = {
  userId: string;
  question: string;
  answer: string;
  embeddingIds: number[];
  isHelpFul: boolean;
};
