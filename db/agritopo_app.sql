CREATE TABLE classe (
 classeid INT NOT NULL PRIMARY KEY,
 nome VARCHAR(150)
);


CREATE TABLE configuracao (
 configuracaoid VARCHAR(50) NOT NULL PRIMARY KEY,
 nome VARCHAR(50),
 tipo VARCHAR(50),
 valor VARCHAR(250)
);


CREATE TABLE geradorid (
 tabela VARCHAR(80) NOT NULL PRIMARY KEY,
 id_atual INT
);


CREATE TABLE imagem (
 imagemid INT NOT NULL PRIMARY KEY,
 arquivo VARCHAR(1000)
);


CREATE TABLE imovel (
 imovelid INT NOT NULL PRIMARY KEY,
 descricao VARCHAR(200)
);


CREATE TABLE mapa (
 mapaid INT NOT NULL PRIMARY KEY,
 descricao VARCHAR(200),
 local VARCHAR(200),
 sincronizado CHAR(10),
 tipo VARCHAR(50)
);


CREATE TABLE ponto (
 pontoid INT NOT NULL PRIMARY KEY,
 altitude NUMERIC(18,6),
 latitude NUMERIC(18,6),
 longitude NUMERIC(18,6)
);


CREATE TABLE pontoimagem (
 pontoid INT NOT NULL,
 imagemid INT NOT NULL,

 PRIMARY KEY (pontoid,imagemid),

 FOREIGN KEY (pontoid) REFERENCES ponto (pontoid),
 FOREIGN KEY (imagemid) REFERENCES imagem (imagemid)
);


CREATE TABLE tipoelemento (
 tipoelementoid INT NOT NULL PRIMARY KEY,
 nome VARCHAR(50)
);


CREATE TABLE usuario (
 usuarioid INT NOT NULL PRIMARY KEY,
 email VARCHAR(300),
 senha VARCHAR(30),
 estado INT
);


CREATE TABLE campodinamico (
 campodinamicoid INT NOT NULL,
 tipoelementoid INT NOT NULL,
 nome VARCHAR(200),
 tipocomponente VARCHAR(100),

 PRIMARY KEY (campodinamicoid,tipoelementoid),

 FOREIGN KEY (tipoelementoid) REFERENCES tipoelemento (tipoelementoid)
);


CREATE TABLE campodinamicovalores (
 campodinamicoid INT NOT NULL,
 tipoelementoid INT NOT NULL,
 valorpermitido VARCHAR(200),

 PRIMARY KEY (campodinamicoid,tipoelementoid),

 FOREIGN KEY (campodinamicoid,tipoelementoid) REFERENCES campodinamico (campodinamicoid,tipoelementoid)
);


CREATE TABLE elemento (
 elementoid INT NOT NULL PRIMARY KEY,
 classeid INT NOT NULL,
 tipoelementoid INT NOT NULL,
 titulo VARCHAR(200),
 descricao VARCHAR(500),
 created_at TIMESTAMP(10),
 modified_at TIMESTAMP(10),

 FOREIGN KEY (classeid) REFERENCES classe (classeid),
 FOREIGN KEY (tipoelementoid) REFERENCES tipoelemento (tipoelementoid)
);


CREATE TABLE elementocampodinamico (
 campodinamicoid INT NOT NULL,
 tipoelementoid INT NOT NULL,
 elementoid INT NOT NULL,
 valor VARCHAR(200),

 PRIMARY KEY (campodinamicoid,tipoelementoid,elementoid),

 FOREIGN KEY (campodinamicoid,tipoelementoid) REFERENCES campodinamico (campodinamicoid,tipoelementoid),
 FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)
);


CREATE TABLE elementoponto (
 pontoid INT NOT NULL,
 elementoid INT NOT NULL,

 PRIMARY KEY (pontoid,elementoid),

 FOREIGN KEY (pontoid) REFERENCES ponto (pontoid),
 FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)
);


CREATE TABLE imovelelemento (
 imovelid INT NOT NULL,
 elementoid INT NOT NULL,

 PRIMARY KEY (imovelid,elementoid),

 FOREIGN KEY (imovelid) REFERENCES imovel (imovelid),
 FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)
);


CREATE TABLE imovelmapa (
 mapaid INT NOT NULL,
 imovelid INT NOT NULL,

 PRIMARY KEY (mapaid,imovelid),

 FOREIGN KEY (mapaid) REFERENCES mapa (mapaid),
 FOREIGN KEY (imovelid) REFERENCES imovel (imovelid)
);


CREATE TABLE imovelusuario (
 usuarioid INT NOT NULL,
 imovelid INT NOT NULL,
 permissao INT,

 PRIMARY KEY (usuarioid,imovelid),

 FOREIGN KEY (usuarioid) REFERENCES usuario (usuarioid),
 FOREIGN KEY (imovelid) REFERENCES imovel (imovelid)
);


