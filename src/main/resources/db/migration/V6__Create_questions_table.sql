CREATE TABLE "questions" (
  "id"      VARCHAR PRIMARY KEY,
  "user_id" VARCHAR NOT NULL,
  "title" VARCHAR(254) NOT NULL,
  "content" TEXT NOT NULL,
  "tags"     VARCHAR,
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);

ALTER TABLE "questions" ADD CONSTRAINT "user_fk" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE CASCADE;