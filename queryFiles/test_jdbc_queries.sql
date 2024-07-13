INSERT INTO l (id, Titolo, Autore) VALUES
(1, 'Transistor', 'Coppelli'),
(2, 'Compilatore', 'Ranieri'),
(3, 'Diodi', 'Stortoni'),
(4, 'Algoritmi', 'Sedge'),
(5, 'Pascal', 'Wirth');

INSERT INTO p (id, Inizio, Fine, id_U, id_L) VALUES
(1, '2005-02-10', '2005-06-15', 6, 3),
(2, '2015-12-10', '2015-12-15', 6, 1),
(3, '2002-06-25', '2010-07-01', 5, 2),
(4, '2014-09-14', '2014-09-15', 4, 5),
(5, '2021-04-27', '2021-07-01', 4, 4),
(6, '2020-05-21', '2020-05-30', 3, 4),
(7, '2022-06-11', null, 1, 4);