-- ========================================================================================
-- PREINSTALL FROM 17.05
-- ========================================================================================
-- Consideraciones importantes:
--	1) NO hacer cambios en el archivo, realizar siempre APPENDs al final del mismo 
-- 	2) Recordar realizar las adiciones con un comentario con formato YYYYMMDD-HHMM
-- ========================================================================================

--20170529-1030 Nueva columna que permite indicar si un pago es manual, en ese caso se genera un allocation unilateral al completar 
update ad_system set dummy = (SELECT addcolumnifnotexists('C_Payment','ismanual','character(1) NOT NULL DEFAULT ''N''::bpchar'));

--20170606-1445 El resultado del pendiente debe ser positivo ya que para payments con importe negativo no estaba retornando correctamente el valor
CREATE OR REPLACE FUNCTION paymentavailable(
  p_c_payment_id integer,
  dateto timestamp without time zone)
  RETURNS numeric AS
$BODY$
DECLARE
v_Currency_ID INTEGER;
v_AvailableAmt NUMERIC := 0;
  v_IsReceipt CHARACTER(1);
  v_Amt NUMERIC := 0;
  r RECORD;
v_Charge_ID INTEGER;
v_ConversionType_ID INTEGER;
v_allocatedAmt NUMERIC; -- candida alocada total convertida a la moneda de la linea
v_DateAcct timestamp without time zone;
BEGIN
BEGIN

SELECT C_Currency_ID, PayAmt, IsReceipt,
C_Charge_ID,C_ConversionType_ID, DateAcct
INTO STRICT
v_Currency_ID, v_AvailableAmt, v_IsReceipt,
v_Charge_ID,v_ConversionType_ID, v_DateAcct
FROM C_Payment
WHERE C_Payment_ID = p_C_Payment_ID;
EXCEPTION
WHEN OTHERS THEN
  RAISE NOTICE 'PaymentAvailable - %', SQLERRM;
RETURN NULL;
END;

IF (v_Charge_ID > 0 ) THEN
RETURN 0;
END IF;

v_allocatedAmt := 0;
FOR r IN
SELECT a.AD_Client_ID, a.AD_Org_ID, al.Amount, a.C_Currency_ID, a.DateTrx
FROM C_AllocationLine al
INNER JOIN C_AllocationHdr a ON (al.C_AllocationHdr_ID=a.C_AllocationHdr_ID)
WHERE al.C_Payment_ID = p_C_Payment_ID
  AND a.IsActive='Y'
  AND (dateTo IS NULL OR a.dateacct::date <= dateTo::date)
LOOP
v_Amt := currencyConvert(r.Amount, r.C_Currency_ID, v_Currency_ID,
v_DateAcct, v_ConversionType_ID, r.AD_Client_ID, r.AD_Org_ID);
v_allocatedAmt := v_allocatedAmt + v_Amt;
END LOOP;

-- esto supone que las alocaciones son siempre no negativas; si esto no pasa, se van a retornar valores que no van a tener sentido
v_AvailableAmt := ABS(v_AvailableAmt) - v_allocatedAmt;
-- v_AvailableAmt aca DEBE ser NO Negativo si admeas, las suma de las alocaciones nunca superan el monto del pago
-- de cualquiera manera, por "seguridad", si el valor es negativo, se corrige a cero
IF (v_AvailableAmt < 0) THEN
RAISE NOTICE 'Payment Available negative, correcting to zero - %',v_AvailableAmt ;
v_AvailableAmt := 0;
END IF;

v_AvailableAmt := currencyRound(v_AvailableAmt,v_Currency_ID,NULL);
RETURN v_AvailableAmt;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION paymentavailable(integer, timestamp without time zone)
  OWNER TO libertya;

--20170612-1200 Nueva columna para configurar la cuenta bancaria utilizada en el payment generado por liquidaciones
update ad_system set dummy = (SELECT addcolumnifnotexists('m_entidadfinanciera','C_BankAccount_Settlement_ID','integer'));

--20170613-1519 Creacion de tabla de bajo nivel para seguimiento de eventuales queries/conexiones relacioandas con informes canditadas a ser canceladas en caso de quedar "huerfanas" del cliente o servidor LY 
create table ad_keepalive (ad_session_id int, pid int, created timestamp, updated timestamp);

--20170626-2030 Creación de tabla que permite configurar datos necesarios para procesos de Corrección de Cobranzas
CREATE TABLE c_payment_recovery_config
(
  c_payment_recovery_config_id integer NOT NULL,
  ad_client_id integer NOT NULL,
  ad_org_id integer NOT NULL,
  isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
  created timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  createdby integer NOT NULL,
  updated timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  updatedby integer NOT NULL,
  c_doctype_recovery_id integer NOT NULL,
  c_doctype_credit_recovery_id integer NOT NULL,
  m_product_recovery_id integer NOT NULL,
  c_doctype_rejected_id integer NOT NULL,
  c_doctype_credit_rejected_id integer NOT NULL,
  m_product_rejected_id integer NOT NULL,
  CONSTRAINT c_payment_recovery_config_key PRIMARY KEY (c_payment_recovery_config_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE c_payment_recovery_config OWNER TO libertya;

--20170628-1400 Merge de revisión 2047
ALTER TABLE c_perceptionssettlement ALTER COLUMN internalno DROP NOT NULL;