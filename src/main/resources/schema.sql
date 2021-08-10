DROP TABLE IF EXISTS suburb;
DROP SEQUENCE IF EXISTS suburb_seq;

CREATE SEQUENCE suburb_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE suburb (
  id INT  default suburb_seq.nextval NOT NULL PRIMARY KEY,
  name VARCHAR(1024) UNIQUE NOT NULL,
  post_code INT NOT NULL,
  create_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP()
);

