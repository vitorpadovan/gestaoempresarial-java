delete from cliente;
ALTER SEQUENCE cliente_seq RESTART WITH 1;

-- Auto-generated SQL script #202303132047
INSERT INTO public.cliente (codcliente,cpfcnpj,nomecliente,tipoDeDocumento)
	VALUES (nextval('cliente_seq'),'37794695880','Vitor',0),
(nextval('cliente_seq'),'04450600851','Julia',0),
(nextval('cliente_seq'),'38888899912','Julia',0);

SELECT * FROM Cliente;