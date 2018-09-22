-- keyspace: horus
-- description: creates member schema
-- authoredAt: 1537646473
-- up:

-- stage: 1
CREATE TABLE if not exists stackoverflowclone.members(
  member_id UUID,
  username text,
  email text,
  password text,
  PRIMARY KEY (member_id));