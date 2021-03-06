-- If you change these method names or signatures, update the docs in `docs/_posts/2017-12-07-admin-guide.md`
-- StartNoTest
DELIMITER //

DROP PROCEDURE IF EXISTS addBoxSize//
CREATE PROCEDURE addBoxSize(
  iRows int,
  iColumns int,
  iScannable tinyint(1)
) BEGIN
  IF NOT EXISTS (SELECT 1 FROM BoxSize WHERE `boxSizeRows` = iRows AND `boxSizeColumns` = iColumns AND scannable = iScannable)
  THEN
    INSERT INTO BoxSize(`boxSizeRows`, `boxSizeColumns`, scannable)
    VALUES (iRows, iColumns, iScannable);
  END IF;
END//

DROP PROCEDURE IF EXISTS addBoxUse//
CREATE PROCEDURE addBoxUse(
  iAlias varchar(255)
) BEGIN
  IF NOT EXISTS (SELECT 1 FROM BoxUse WHERE alias = iAlias)
  THEN
    INSERT INTO BoxUse(alias) VALUES (iAlias);
  END IF;
END//

DELIMITER ;
-- EndNoTest
