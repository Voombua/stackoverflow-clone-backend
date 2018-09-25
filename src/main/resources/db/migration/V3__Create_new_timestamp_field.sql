CREATE TABLE users (
  id         UUID PRIMARY KEY DEFAULT uuid_generate_v1(),
  username      VARCHAR   NOT NULL,
  email      VARCHAR   NOT NULL,
  password    VARCHAR   NOT NULL DEFAULT md5(random() :: TEXT),
  created_at TIMESTAMP NOT NULL DEFAULT,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);