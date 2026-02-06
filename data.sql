-- Insérer les races
INSERT INTO race (nom, prix_vente, prix_achat, poids_max, augmentation_poids) VALUES
('Tilapia', 5000.00, 1000.00, 1500.00, 10.00), 
('Carpe', 6000.00, 1200.00, 2000.00, 12.00);

-- Tilapia
INSERT INTO besoin (id_race, nom, quantite) VALUES
(1, 'Proteine', 4.00),
(1, 'Glucide', 2.00);
-- (1, 'Lipide', 1.50);

-- Carpe
INSERT INTO besoin (id_race, nom, quantite) VALUES
(2, 'Proteine', 10),
(2, 'Glucide', 5);
-- (2, 'Lipide', 2.00);


INSERT INTO aliment (nom, prix_achat) VALUES
('Ovy', 2000.00),
('Carotte', 1000.00);

--  Ovy
INSERT INTO apport (id_aliment, nom, quantite) VALUES
(1, 'Proteine', 2.00),   -- 2%
(1, 'Glucide', 1.00);    -- 1%
-- (1, 'Lipide', 2.00);     -- 2%

-- Carotte
INSERT INTO apport (id_aliment, nom, quantite) VALUES
(2, 'Proteine', 10.00),   -- 10% de protéine
(2, 'Glucide', 10.00);   -- 10% de glucide
-- (2, 'Lipide', 0.50);     -- 0.5% de lipide

INSERT INTO sakafo (id_aliment, quantite) VALUES
(1, 5000),  -- 5kg de ovy
(2, 5000),  -- 5kg de Carottes
(1, 10000);   -- 10kg de ovy

-- Créer un dobo
INSERT INTO dobo (nom, date_heure) VALUES
('Dobo Principal', CURRENT_TIMESTAMP);

-- Insérer quelques trondros
INSERT INTO trondro (nom, id_race, poids_initial) VALUES
('Tilapia 1', 1, 5.00),
('Tilapia 2', 1, 5.50),
('Carpe 1', 2, 6.00),
('Carpe 2', 2, 6.50);

-- Lier les trondros au dobo (table dobo_trondro)
INSERT INTO dobo_trondro (id_dobo, id_trondro, date_heure) VALUES
(1, 1, CURRENT_TIMESTAMP),
(1, 2, CURRENT_TIMESTAMP),
(1, 3, CURRENT_TIMESTAMP),
(1, 4, CURRENT_TIMESTAMP);


INSERT INTO fisakafona (id_sakafo, id_dobo, date_heure, est_lany) VALUES
(3, 1, CURRENT_TIMESTAMP, false),  -- 5000g de Ovy
(2, 1, CURRENT_TIMESTAMP, false);  -- 5000g de Carottes




















-- Pour tester, créer un autre dobo avec fisakafona déjà expirée
INSERT INTO dobo (nom, date_heure) VALUES
('Dobo Secondaire', CURRENT_TIMESTAMP);

INSERT INTO fisakafona (id_sakafo, id_dobo, date_heure, est_lany) VALUES
(1, 2, CURRENT_TIMESTAMP, true);  -- Déjà expirée

-- Ajouter quelques trondros au dobo secondaire
INSERT INTO trondro (nom, id_race, poids_initial) VALUES
('Tilapia 3', 1, 4.80),
('Carpe 3', 2, 5.80);

INSERT INTO dobo_trondro (id_dobo, id_trondro, date_heure) VALUES
(2, 5, CURRENT_TIMESTAMP),
(2, 6, CURRENT_TIMESTAMP);

-- Vérification des données
SELECT '=== Races ===' as "";
SELECT * FROM race;

SELECT '=== Besoins ===' as "";
SELECT b.*, r.nom as race_nom FROM besoin b JOIN race r ON b.id_race = r.id;

SELECT '=== Aliments ===' as "";
SELECT * FROM aliment;

SELECT '=== Apports ===' as "";
SELECT a.*, al.nom as aliment_nom FROM apport a JOIN aliment al ON a.id_aliment = al.id;

SELECT '=== Sakafos ===' as "";
SELECT s.*, al.nom as aliment_nom FROM sakafo s JOIN aliment al ON s.id_aliment = al.id;

SELECT '=== Dobos ===' as "";
SELECT * FROM dobo;

SELECT '=== Trondros ===' as "";
SELECT t.*, r.nom as race_nom FROM trondro t JOIN race r ON t.id_race = r.id;

SELECT '=== Dobo-Trondro (liaisons) ===' as "";
SELECT dt.*, d.nom as dobo_nom, t.nom as trondro_nom 
FROM dobo_trondro dt 
JOIN dobo d ON dt.id_dobo = d.id 
JOIN trondro t ON dt.id_trondro = t.id;

SELECT '=== Fisakafona (alimentations) ===' as "";
SELECT f.*, 
       d.nom as dobo_nom, 
       s.quantite as sakafo_quantite,
       al.nom as aliment_nom,
       CASE WHEN f.est_lany THEN 'OUI' ELSE 'NON' END as est_expiree
FROM fisakafona f 
JOIN dobo d ON f.id_dobo = d.id 
JOIN sakafo s ON f.id_sakafo = s.id
JOIN aliment al ON s.id_aliment = al.id;