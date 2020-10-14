CREATE TABLE IF NOT EXISTS certificate_requests(id serial PRIMARY KEY, subjectDN VARCHAR(50), serialNumber VARCHAR(20), certificate VARCHAR, entity INT REFERENCES entities(id));

CREATE TABLE IF NOT EXISTS certificates(id serial PRIMARY KEY, subjectDN VARCHAR(50), serialNumber VARCHAR(20), certificate VARCHAR, entity INT REFERENCES entities(id));
