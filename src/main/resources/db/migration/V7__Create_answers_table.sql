CREATE TABLE "answers" (
  "id"      VARCHAR PRIMARY KEY,
  "question_id" VARCHAR NOT NULL,
  "user_id" VARCHAR NOT NULL,
  "content" TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);

ALTER TABLE "answers" ADD CONSTRAINT "answers_user_fk" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE CASCADE ;
ALTER TABLE "answers" ADD CONSTRAINT "answers_question_fk" FOREIGN KEY ("question_id") REFERENCES "questions" ("id") ON UPDATE NO ACTION ON DELETE CASCADE ;