DELIMITER $$
DROP PROCEDURE IF EXISTS funcTransaction;
CREATE
    /*[DEFINER = { user | CURRENT_USER }]*/
    PROCEDURE boom.funcTransaction(IN inText TEXT CHARSET utf8mb4, OUT outResult TINYINT)
    /*LANGUAGE SQL
    | [NOT] DETERMINISTIC
    | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
    | SQL SECURITY { DEFINER | INVOKER }
    | COMMENT 'string'*/
    BEGIN
		DECLARE txtQueries TEXT;
		DECLARE strQuery VARCHAR(2048);
		DECLARE nCurrentPos INT DEFAULT 1;
		
		DECLARE nError INT DEFAULT 0;
		
		DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET nError = -1;
		
		SET txtQueries = inText;
		START TRANSACTION;
		WHILE CHAR_LENGTH(txtQueries) > 0 AND nCurrentPos > 0 AND nError = 0 DO
			SET nCurrentPos = INSTR(txtQueries, ';\\;');
			IF nCurrentPos = 0 THEN
				SET strQuery = txtQueries;
			ELSE 
				SET strQuery = LEFT(txtQueries, nCurrentPos - 1);
			END IF;
			
			IF TRIM(strQuery) != '' THEN
				SET @s := strQuery;
				PREPARE STMT FROM @s;
				EXECUTE STMT;
				
				IF ROW_COUNT() = 0 THEN
					SET nError = -2;
				END IF;
			END IF;
			
			SET txtQueries = SUBSTRING(txtQueries, nCurrentPos + 3);
		END WHILE;
		
		SET outResult = nError;
		IF nError = 0 THEN
			COMMIT;
		ELSE
			ROLLBACK;
		END IF;
    END$$

DELIMITER ;