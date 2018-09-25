CREATE TABLE users (
  id         UUID PRIMARY KEY,
  username      VARCHAR   NOT NULL,
  email      VARCHAR   NOT NULL,
  password    VARCHAR   NOT NULL DEFAULT md5(random() :: TEXT),
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);