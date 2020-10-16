CREATE TABLE IF NOT EXISTS certificate_requests(id serial PRIMARY KEY, subjectDN VARCHAR(50), serial_number VARCHAR(20), certificate VARCHAR, entity INT, CONSTRAINT fk_entity FOREIGN KEY("entity") REFERENCES entities (id));

CREATE TABLE IF NOT EXISTS certificates(id serial PRIMARY KEY, subjectDN VARCHAR(50), serial_number VARCHAR(20), certificate VARCHAR, entity INT, CONSTRAINT fk_entity FOREIGN KEY("entity") REFERENCES entities (id));
