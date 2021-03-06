-- ========================================================================================
-- PREINSTALL FROM 18.06
-- ========================================================================================
-- Consideraciones importantes:
--	1) NO hacer cambios en el archivo, realizar siempre APPENDs al final del mismo 
-- 	2) Recordar realizar las adiciones con un comentario con formato YYYYMMDD-HHMM
-- ========================================================================================

--20180814-1125 Nueva columna para habilitar/inhabilitar entidades comerciales para operar en el sistema
update ad_system set dummy = (SELECT addcolumnifnotexists('c_bpartner','trxenabled','character(1) NOT NULL DEFAULT ''Y''::bpchar'));

update ad_client
set modelvalidationclasses = modelvalidationclasses || ';' || 'org.openXpertya.model.BusinessPartnerValidator'
where position('org.openXpertya.model.BusinessPartnerValidator' in modelvalidationclasses) = 0;

update ad_client
set modelvalidationclasses = replace(modelvalidationclasses,';;',';');

--20180818-2045 El informe de Declaración de Valores o Cierre de Caja para tarjetas debe mostrar el total del mismo ya que es un cupón devuelto por el posnet 
CREATE OR REPLACE FUNCTION c_posjournal_c_payment_v_filtered(posjournalids anyarray)
  RETURNS SETOF c_posjournal_c_payment_v_type AS
$BODY$
declare
	consulta varchar;
	wherePosJournalIDs varchar;
	existsPosJournalIDs boolean;
	adocument c_posjournal_c_payment_v_type;
BEGIN
	-- Armado de condiciones de filtro parámetro
	existsPosJournalIDs = posjournalIDs is not null and array_length(posjournalIDs,1) > 0 and posjournalIDs[1] <> -1;

	-- Filtro de cajas diarias
	wherePosJournalIDs = '';
	if existsPosJournalIDs then
		wherePosJournalIDs = ' AND p.c_posjournal_id IN ('|| array_to_string(posjournalIDs, ',') || ')';
	end if;
	
	consulta =  
	'SELECT al.c_allocationhdr_id, al.c_allocationline_id, al.ad_client_id, al.ad_org_id, al.isactive, al.created, al.createdby, al.updated, al.updatedby, al.c_invoice_id, al.c_payment_id, al.c_cashline_id, al.c_invoice_credit_id, p.tendertype::character varying AS tendertype, p.documentno, p.description, ((p.documentno::text || ''_''::text) || to_char(p.datetrx, ''DD/MM/YYYY''::text))::character varying AS info, COALESCE(currencyconvert((case when ah.allocationtype in (''OPA'',''RCA'') or p.tendertype = ''C'' then p.payamt else al.amount + al.discountamt + al.writeoffamt end), (case when ah.allocationtype in (''OPA'',''RCA'') then p.c_currency_id else ah.c_currency_id end), ah.c_currency_id, ah.dateacct, null::integer, ah.ad_client_id, ah.ad_org_id), 0::numeric)::numeric(20,2) AS amount, NULL::integer AS c_cash_id, NULL::numeric(18,0) AS line, NULL::integer AS c_doctype_id, p.checkno, p.a_bank, p.checkno AS transferno, p.creditcardtype, p.m_entidadfinancieraplan_id, ep.m_entidadfinanciera_id, p.couponnumber, date_trunc(''day''::text, ah.datetrx) AS allocationdate, p.docstatus, p.dateacct::date AS dateacct, (case when ah.allocationtype in (''OPA'',''RCA'') then ''Anticipo'' else i.documentno end) AS invoice_documentno, i.grandtotal AS invoice_grandtotal, ef.value AS entidadfinanciera_value, ef.name AS entidadfinanciera_name, bp.value AS bp_entidadfinanciera_value, bp.name AS bp_entidadfinanciera_name, p.couponnumber AS cupon, p.creditcardnumber AS creditcard, NULL::bpchar AS isfiscaldocument, NULL::bpchar AS isfiscal, NULL::bpchar AS fiscalalreadyprinted, al.changeamt, ah.c_currency_id
           FROM c_allocationline al
      JOIN c_allocationhdr ah ON al.c_allocationhdr_id = ah.c_allocationhdr_id
   LEFT JOIN c_invoice i ON al.c_invoice_id = i.c_invoice_id
   JOIN c_payment p ON al.c_payment_id = p.c_payment_id
   LEFT JOIN m_entidadfinancieraplan ep ON p.m_entidadfinancieraplan_id = ep.m_entidadfinancieraplan_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = ep.m_entidadfinanciera_id
   LEFT JOIN c_bpartner bp ON bp.c_bpartner_id = ef.c_bpartner_id
   WHERE 1=1 ' || wherePosJournalIDs || ' AND NOT EXISTS (SELECT ala.c_payment_id FROM c_allocationline as ala INNER JOIN c_allocationhdr as aha on aha.c_allocationhdr_id = ala.c_allocationhdr_id WHERE ala.c_payment_id = p.c_payment_id AND aha.allocationtype in (''OPA'',''RCA'') AND aha.c_allocationhdr_id <> ah.c_allocationhdr_id) ' || 
   ' UNION ALL 
        ( SELECT NULL::integer AS c_allocationhdr_id, NULL::integer AS c_allocationline_id, p.ad_client_id, p.ad_org_id, p.isactive, p.created, p.createdby, p.updated, p.updatedby, NULL::integer AS c_invoice_id, p.c_payment_id, NULL::integer AS c_cashline_id, NULL::integer AS c_invoice_credit_id, p.tendertype::character varying(2) AS tendertype, p.documentno, p.description, (((p.documentno::text || ''_''::text) || to_char(p.datetrx, ''DD/MM/YYYY''::text)))::character varying(255) AS info, p.payamt AS amount, NULL::integer AS c_cash_id, NULL::numeric(18,0) AS line, NULL::integer AS c_doctype_id, p.checkno, p.a_bank, p.checkno AS transferno, p.creditcardtype, p.m_entidadfinancieraplan_id, ep.m_entidadfinanciera_id, p.couponnumber, date_trunc(''day''::text, p.datetrx) AS allocationdate, p.docstatus, p.dateacct::date AS dateacct, NULL::character varying(30) AS invoice_documentno, NULL::numeric(20,2) AS invoice_grandtotal, ef.value AS entidadfinanciera_value, ef.name AS entidadfinanciera_name, bp.value AS bp_entidadfinanciera_value, bp.name AS bp_entidadfinanciera_name, p.couponnumber AS cupon, p.creditcardnumber AS creditcard, NULL::bpchar AS isfiscaldocument, NULL::bpchar AS isfiscal, NULL::bpchar AS fiscalalreadyprinted, 0 AS changeamt, p.c_currency_id
           FROM c_payment p
      LEFT JOIN m_entidadfinancieraplan ep ON p.m_entidadfinancieraplan_id = ep.m_entidadfinancieraplan_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = ep.m_entidadfinanciera_id
   LEFT JOIN c_bpartner bp ON bp.c_bpartner_id = ef.c_bpartner_id
  WHERE 1=1 ' || wherePosJournalIDs || 
  ' AND NOT (EXISTS ( SELECT al.c_allocationline_id
    FROM c_allocationline al
   WHERE al.c_payment_id = p.c_payment_id))
  ORDER BY p.tendertype::character varying(2), p.documentno);';

	--raise notice '%', consulta;
	FOR adocument IN EXECUTE consulta LOOP
		return next adocument;
	END LOOP;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION c_posjournal_c_payment_v_filtered(anyarray)
  OWNER TO libertya;
  
--20180824-1900 Fixes a las funciones de pendientes de entrega
CREATE OR REPLACE FUNCTION getqtyreserved(
    clientid integer,
    orgid integer,
    locatorid integer,
    productid integer,
    dateto date)
  RETURNS numeric AS
$BODY$
/***********
Obtiene la cantidad reservada a fecha de corte. Si no hay fecha de corte, entonces se devuelven los pendientes actuales.
Por lo pronto no se utiliza el pendiente a fecha de corte ya que primero deberíamos analizar e implementar 
una forma en la que se determine cuando un pedido fue completo, anulado, etc.
*/
DECLARE
reserved numeric;
BEGIN
reserved := 0;
--Si no hay fecha de corte o es mayor o igual a la fecha actual, entonces se suman las cantidades reservadas de los pedidos
--if ( dateTo is null OR dateTo >= current_date ) THEN
SELECT INTO reserved coalesce(sum(ol.qtyreserved),0)
from c_orderline ol
inner join c_order o on o.c_order_id = ol.c_order_id
inner join c_doctype dto on dto.c_doctype_id = o.c_doctypetarget_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid 
and ol.qtyreserved <> 0
and o.docstatus in ('CO','CL')
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and (dateto is null or o.dateordered::date <= dateto::date)
and o.issotrx = 'Y'
and dto.doctypekey <> 'SOSOT';
/*ELSE
SELECT INTO reserved coalesce(sum(qty),0)
from (
-- Cantidad pedida a fecha de corte
select coalesce(sum(ol.qtyordered),0) as qty
from c_orderline ol
inner join c_order o on o.c_order_id = ol.c_order_id
inner join c_doctype dt on dt.c_doctype_id = o.c_doctypetarget_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid
and o.processed = 'Y' 
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and o.issotrx = 'Y'
and dt.doctypekey NOT IN ('SOSOT')
and o.dateordered::date <= dateTo::date
and o.dateordered::date <= current_date
union all
-- Notas de crédito con (o sin) el check Actualizar Cantidades de Pedido
select coalesce(sum(il.qtyinvoiced),0) as qty
from c_invoiceline il
inner join c_invoice i on i.c_invoice_id = il.c_invoice_id
inner join c_doctype dt on dt.c_doctype_id = i.c_doctypetarget_id
inner join c_orderline ol on ol.c_orderline_id = il.c_orderline_id
inner join c_order o on o.c_order_id = ol.c_order_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid
and o.processed = 'Y' 
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and i.issotrx = 'Y'
and il.m_inoutline_id is null
and dt.signo_issotrx = '-1'
and o.dateordered::date <= dateTo::date
and i.dateinvoiced::date > dateTo::date
and i.dateinvoiced::date <= current_date
union all
--En transaction las salidas son negativas y las entradas positivas
select coalesce(sum(t.movementqty),0) as qty
from m_transaction t
inner join m_inoutline iol on iol.m_inoutline_id = t.m_inoutline_id
inner join m_inout io on io.m_inout_id = iol.m_inout_id
inner join c_doctype dt on dt.c_doctype_id = io.c_doctype_id
inner join c_orderline ol on ol.c_orderline_id = iol.c_orderline_id
inner join c_order o on o.c_order_id = ol.c_order_id
where t.ad_client_id = clientid
and t.ad_org_id = orgid
and t.m_product_id = productid
and t.m_locator_id = locatorid
and dt.reservestockmanagment = 'Y'
and o.dateordered::date <= dateTo::date
and t.movementdate::date <= dateTo::date
and t.movementdate::date <= current_date
union all
--Cantidades transferidas
select coalesce(sum(ol.qtyordered * -1),0) as qty
from c_orderline ol
inner join c_order o on o.c_order_id = ol.c_order_id
inner join c_orderline rl on rl.c_orderline_id = ol.ref_orderline_id
inner join c_order r on r.c_order_id = rl.c_order_id
inner join c_doctype dt on dt.c_doctype_id = o.c_doctype_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid
and o.processed = 'Y' 
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and o.issotrx = 'Y'
and dt.doctypekey IN ('SOSOT')
and r.dateordered::date <= dateTo::date
and o.dateordered::date <= dateTo::date
and o.dateordered::date <= current_date
) todo;
END IF;*/

return reserved;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getqtyreserved(integer, integer, integer, integer, date)
  OWNER TO libertya;
  
 --Update reserved dateto
CREATE OR REPLACE FUNCTION update_reserved(
    clientid integer,
    orgid integer,
    productid integer,
    dateto date)
  RETURNS void AS
$BODY$
/***********
Actualiza la cantidad reservada de los depósitos de la compañía, organización y artículo parametro, 
siempre y cuando existan los regitros en m_storage 
y sólo sobre locators marcados como default ya que asi se realiza al procesar pedidos.
Las cantidades reservadas se obtienen de pedidos procesados. 
IMPORTANTE: No funciona para artículos que no son ITEMS (Stockeables)
*/
BEGIN
	update m_storage s
	set qtyreserved = getqtyreserved(s.ad_client_id, s.ad_org_id, s.m_locator_id, s.m_product_id, dateto)
	where ad_client_id = clientid
		and (orgid = 0 or ad_org_id = orgid)
		and (productid = 0 or m_product_id = productid);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_reserved(integer, integer, integer, date)
  OWNER TO libertya;
  
-- Update reserved 
CREATE OR REPLACE FUNCTION update_reserved(
    clientid integer,
    orgid integer,
    productid integer)
  RETURNS void AS
$BODY$
/***********
Actualiza la cantidad reservada de los depósitos de la compañía, organización y artículo parametro, 
siempre y cuando existan los regitros en m_storage 
y sólo sobre locators marcados como default ya que asi se realiza al procesar pedidos.
Las cantidades reservadas se obtienen de pedidos procesados. 
IMPORTANTE: No funciona para artículos que no son ITEMS (Stockeables)
*/
BEGIN
	perform update_reserved(clientid, orgid, productid, null::date);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_reserved(integer, integer, integer)
  OWNER TO libertya;
  
--20180905-1015 Mejoras a las funciones de pendientes de pago y factura para que tome el signo del importe original
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
  v_PayAmt NUMERIC := 0;
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
v_Currency_ID, v_PayAmt, v_IsReceipt,
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
v_Amt := currencyConvert(r.Amount, r.C_Currency_ID, v_Currency_ID,v_DateAcct, v_ConversionType_ID, r.AD_Client_ID, r.AD_Org_ID);
v_allocatedAmt := v_allocatedAmt + v_Amt;
END LOOP;

-- esto supone que las alocaciones son siempre no negativas; si esto no pasa, se van a retornar valores que no van a tener sentido
v_AvailableAmt := ABS(v_PayAmt) - abs(v_allocatedAmt);
-- v_AvailableAmt aca DEBE ser NO Negativo si admeas, las suma de las alocaciones nunca superan el monto del pago
-- de cualquiera manera, por "seguridad", si el valor es negativo, se corrige a cero
IF (v_AvailableAmt < 0) THEN
RAISE NOTICE 'Payment Available negative, correcting to zero - %',v_AvailableAmt ;
v_AvailableAmt := 0;
END IF;

--  el resultado debe ser 0 o de lo contrario tener el mismo signo que el payment
IF (v_PayAmt < 0) THEN
	v_AvailableAmt := v_AvailableAmt * -1::numeric;
END IF;

v_AvailableAmt := currencyRound(v_AvailableAmt,v_Currency_ID,NULL);
RETURN v_AvailableAmt;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION paymentavailable(integer, timestamp without time zone)
  OWNER TO libertya;

CREATE OR REPLACE FUNCTION invoiceopen(
    p_c_invoice_id integer,
    p_c_invoicepayschedule_id integer,
    p_c_currency_id integer,
    p_c_conversiontype_id integer,
    p_dateto timestamp without time zone)
  RETURNS numeric AS
$BODY$ /*************************************************************************  * The contents of this file are subject to the Compiere License.  You may  * obtain a copy of the License at    http://www.compiere.org/license.html  * Software is on an  "AS IS" basis,  WITHOUT WARRANTY OF ANY KIND, either  * express or implied. See the License for details. Code: Compiere ERP+CRM  * Copyright (C) 1999-2001 Jorg Janke, ComPiere, Inc. All Rights Reserved.  *  * converted to postgreSQL by Karsten Thiemann (Schaeffer AG),   * kthiemann@adempiere.org  *************************************************************************  ***  * Title:	Calculate Open Item Amount in Invoice Currency  * Description:  *	Add up total amount open for C_Invoice_ID if no split payment.  *  Grand Total minus Sum of Allocations in Invoice Currency  *  *  For Split Payments:  *  Allocate Payments starting from first schedule.  *  Cannot be used for IsPaid as mutating  *  * Test:  * 	SELECT C_InvoicePaySchedule_ID, DueAmt FROM C_InvoicePaySchedule WHERE C_Invoice_ID=109 ORDER BY DueDate;  * 	SELECT invoiceOpen (109, null) FROM AD_System; - converted to default client currency  * 	SELECT invoiceOpen (109, 11) FROM AD_System; - converted to default client currency  * 	SELECT invoiceOpen (109, 102) FROM AD_System;  * 	SELECT invoiceOpen (109, 103) FROM AD_System;  ***  * Pasado a Libertya a partir de Adempiere 360LTS  * - ids son de tipo integer, no numeric  * - TODO : tema de las zonas en los timestamp  * - Excepciones en SELECT INTO requieren modificador STRICT bajo PostGreSQL o usar  * NOT FOUND  * - Por ahora, el "ignore rounding" se hace como en libertya (-0.01,0.01),  * en vez de usar la precisión de la moneda  * - Se toma el tipo de conversion de la factura, auqneu esto es dudosamente correcto  * ya que otras funciones , en particular currencyBase nunca tiene en cuenta  * este valor  * - Como en Libertya se tiene en cuenta tambien C_Invoice_Credit_ID para calcular  * la cantidad alocada a una factura (aunque esto es medio dudoso....)  * - No se soporta la fecha como 3er parametro (en realidad, tampoco se esta  * usando actualmente, y se deberia poder resolver de otra manera)  * - Libertya parece tener un bug al filtrar por C_InvoicePaySchedule_ID al calcular  * el granTotal (el granTotal SIEMPRE es el total de la factura, tomada directamente  * de C_Invoice.GranTotal o a partir de la suma de los DueAmt en C_InvoicePaySchedule);  * se usa la sentencia como esta en Adempeire (esto es, solo se filtra por C_Invoice_ID)  * - Nuevo enfoque: NO se usa ni la vista C_Invoice_V ni multiplicadores  * se asume todo positivo...  * - El resultado SIEMPRE deberia ser positivo y en el intervalo [0..GrandTotal]  * - 03 julio: se pasa a usar getAllocatedAmt para hacer esta funcion consistente  * con invoicePaid  * - 03 julio: se pasa de usar STRICT a NOT FOUND; es mas eficiente  ************************************************************************/ 
DECLARE 	
v_Currency_ID		INTEGER := p_c_currency_id; 	
v_GrandTotal	  	NUMERIC := 0; 	
v_TotalOpenAmt  	NUMERIC := 0; 	
v_PaidAmt  	        NUMERIC := 0; 	
v_Remaining	        NUMERIC := 0;    	
v_Precision            	NUMERIC := 0;    	
v_Min            	NUMERIC := 0.01;     	
s			RECORD; 	
v_ConversionType_ID INTEGER := p_c_conversiontype_id;  	
v_Date timestamp with time zone := ('now'::text)::timestamp(6);                

BEGIN 	 	

SELECT	currencyConvert(GrandTotal, I.c_currency_id, v_Currency_ID, v_Date, v_ConversionType_ID, I.AD_Client_ID, I.AD_Org_ID) as GrandTotal, 	
	(SELECT StdPrecision FROM C_Currency C WHERE C.C_Currency_ID = I.C_Currency_ID) AS StdPrecision  	
	INTO v_TotalOpenAmt, v_Precision 	
FROM	C_Invoice I 
WHERE	I.C_Invoice_ID = p_C_Invoice_ID; 	

IF NOT FOUND THEN  
	RAISE NOTICE 'Invoice no econtrada - %', p_C_Invoice_ID; 		
	RETURN NULL; 	
END IF; 	      	 	 	 	

v_GrandTotal := v_TotalOpenAmt;
v_PaidAmt := getAllocatedAmt(p_C_Invoice_ID,v_Currency_ID,v_ConversionType_ID,1,p_dateto); 

IF (p_C_InvoicePaySchedule_ID > 0) THEN 
	v_Remaining := abs(v_PaidAmt);         
	FOR s IN  SELECT  ips.C_InvoicePaySchedule_ID, currencyConvert(ips.DueAmt, i.c_currency_id, v_Currency_ID, v_Date, v_ConversionType_ID, i.AD_Client_ID, i.AD_Org_ID) as DueAmt 	        
		FROM    C_InvoicePaySchedule ips 	        
		INNER JOIN C_Invoice i on (ips.C_Invoice_ID = i.C_Invoice_ID) 		
		WHERE	ips.C_Invoice_ID = p_C_Invoice_ID AND   ips.IsValid='Y'         	
		ORDER BY ips.DueDate         
	LOOP             

		IF (s.C_InvoicePaySchedule_ID = p_C_InvoicePaySchedule_ID) THEN                 
			v_TotalOpenAmt := abs(s.DueAmt) - abs(v_Remaining);
			IF (v_TotalOpenAmt < 0) THEN                     
				v_TotalOpenAmt := 0;                  
			END IF; 				
			EXIT;              
		ELSE                  
			v_Remaining := abs(v_Remaining) - abs(s.DueAmt);     
			IF (v_Remaining < 0) THEN         
				v_Remaining := 0;                 
			END IF;             
		END IF;         
	END LOOP;     
ELSE         
	v_TotalOpenAmt := abs(v_TotalOpenAmt) - abs(v_PaidAmt);     
END IF; 	 	

IF (v_TotalOpenAmt >= -v_Min AND v_TotalOpenAmt <= v_Min) THEN 		
	v_TotalOpenAmt := 0; 	
END IF; 	 	

--  el resultado debe ser 0 o de lo contrario tener el mismo signo que el comprobante 
IF (v_GrandTotal < 0) THEN
	v_TotalOpenAmt := v_TotalOpenAmt * -1::numeric;
END IF;

v_TotalOpenAmt := ROUND(COALESCE(v_TotalOpenAmt,0), v_Precision);

RETURN	v_TotalOpenAmt; 

END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION invoiceopen(integer, integer, integer, integer, timestamp without time zone)
  OWNER TO libertya;

--20180912-1355 Mejoras a las funciones de actualización y obtención de cantidades reservadas
CREATE OR REPLACE FUNCTION getqtyreserved(
    clientid integer,
    orgid integer,
    locatorid integer,
    productid integer,
    dateto date)
  RETURNS numeric AS
$BODY$
/***********
Obtiene la cantidad reservada a fecha de corte. Si no hay fecha de corte, entonces se devuelven los pendientes actuales.
Por lo pronto no se utiliza el pendiente a fecha de corte ya que primero deberíamos analizar e implementar 
una forma en la que se determine cuando un pedido fue completo, anulado, etc.
*/
DECLARE
reserved numeric;
BEGIN
reserved := 0;
--Si no hay fecha de corte o es mayor o igual a la fecha actual, entonces se suman las cantidades reservadas de los pedidos
--if ( dateTo is null OR dateTo >= current_date ) THEN
SELECT INTO reserved coalesce(sum(ol.qtyreserved),0)
from c_orderline ol
inner join c_order o on o.c_order_id = ol.c_order_id
inner join c_doctype dto on dto.c_doctype_id = o.c_doctypetarget_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid 
and ol.qtyreserved > 0
and o.docstatus in ('CO','CL')
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and (dateto is null or o.dateordered::date <= dateto::date)
and o.issotrx = 'Y'
and dto.doctypekey <> 'SOSOT';
/*ELSE
SELECT INTO reserved coalesce(sum(qty),0)
from (
-- Cantidad pedida a fecha de corte
select coalesce(sum(ol.qtyordered),0) as qty
from c_orderline ol
inner join c_order o on o.c_order_id = ol.c_order_id
inner join c_doctype dt on dt.c_doctype_id = o.c_doctypetarget_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid
and o.processed = 'Y' 
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and o.issotrx = 'Y'
and dt.doctypekey NOT IN ('SOSOT')
and o.dateordered::date <= dateTo::date
and o.dateordered::date <= current_date
union all
-- Notas de crédito con (o sin) el check Actualizar Cantidades de Pedido
select coalesce(sum(il.qtyinvoiced),0) as qty
from c_invoiceline il
inner join c_invoice i on i.c_invoice_id = il.c_invoice_id
inner join c_doctype dt on dt.c_doctype_id = i.c_doctypetarget_id
inner join c_orderline ol on ol.c_orderline_id = il.c_orderline_id
inner join c_order o on o.c_order_id = ol.c_order_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid
and o.processed = 'Y' 
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and i.issotrx = 'Y'
and il.m_inoutline_id is null
and dt.signo_issotrx = '-1'
and o.dateordered::date <= dateTo::date
and i.dateinvoiced::date > dateTo::date
and i.dateinvoiced::date <= current_date
union all
--En transaction las salidas son negativas y las entradas positivas
select coalesce(sum(t.movementqty),0) as qty
from m_transaction t
inner join m_inoutline iol on iol.m_inoutline_id = t.m_inoutline_id
inner join m_inout io on io.m_inout_id = iol.m_inout_id
inner join c_doctype dt on dt.c_doctype_id = io.c_doctype_id
inner join c_orderline ol on ol.c_orderline_id = iol.c_orderline_id
inner join c_order o on o.c_order_id = ol.c_order_id
where t.ad_client_id = clientid
and t.ad_org_id = orgid
and t.m_product_id = productid
and t.m_locator_id = locatorid
and dt.reservestockmanagment = 'Y'
and o.dateordered::date <= dateTo::date
and t.movementdate::date <= dateTo::date
and t.movementdate::date <= current_date
union all
--Cantidades transferidas
select coalesce(sum(ol.qtyordered * -1),0) as qty
from c_orderline ol
inner join c_order o on o.c_order_id = ol.c_order_id
inner join c_orderline rl on rl.c_orderline_id = ol.ref_orderline_id
inner join c_order r on r.c_order_id = rl.c_order_id
inner join c_doctype dt on dt.c_doctype_id = o.c_doctype_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid
and o.processed = 'Y' 
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and o.issotrx = 'Y'
and dt.doctypekey IN ('SOSOT')
and r.dateordered::date <= dateTo::date
and o.dateordered::date <= dateTo::date
and o.dateordered::date <= current_date
) todo;
END IF;*/

return reserved;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getqtyreserved(integer, integer, integer, integer, date)
  OWNER TO libertya;

CREATE OR REPLACE FUNCTION update_reserved(
    clientid integer,
    orgid integer,
    productid integer,
    dateto date)
  RETURNS void AS
$BODY$
/***********
Actualiza la cantidad reservada de los depósitos de la compañía, organización y artículo parametro, 
siempre y cuando existan los regitros en m_storage 
y sólo sobre locators marcados como default ya que asi se realiza al procesar pedidos.
Las cantidades reservadas se obtienen de pedidos procesados. 
IMPORTANTE: No funciona para artículos que no son ITEMS (Stockeables)
*/
BEGIN
	--Seteamos a 0 todo
	update m_storage s
	set qtyreserved = 0
	where ad_client_id = clientid
		and (orgid = 0 or ad_org_id = orgid)
		and (productid = 0 or m_product_id = productid);
		
	--Actualizamos el reservado
	update m_storage s
	set qtyreserved = getqtyreserved(clientid, s.ad_org_id, s.m_locator_id, s.m_product_id, dateto)
	where ad_client_id = clientid
		and (orgid = 0 or ad_org_id = orgid)
		and (productid = 0 or m_product_id = productid)
		and exists (select ol.c_orderline_id
				from c_orderline ol
				join c_order o on o.c_order_id = ol.c_order_id
				join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
				join m_locator l on l.m_warehouse_id = w.m_warehouse_id
				where l.m_locator_id = s.m_locator_id 
					and ol.m_product_id = s.m_product_id 
					and ol.qtyreserved > 0);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_reserved(integer, integer, integer, date)
  OWNER TO libertya;
  
--20180928-1810 Cuando tenemos extracash en los allocations, se duplican los importes para tarjetas
CREATE OR REPLACE FUNCTION c_posjournal_c_payment_v_filtered(posjournalids anyarray)
  RETURNS SETOF c_posjournal_c_payment_v_type AS
$BODY$
declare
	consulta varchar;
	wherePosJournalIDs varchar;
	existsPosJournalIDs boolean;
	adocument c_posjournal_c_payment_v_type;
BEGIN
	-- Armado de condiciones de filtro parámetro
	existsPosJournalIDs = posjournalIDs is not null and array_length(posjournalIDs,1) > 0 and posjournalIDs[1] <> -1;

	-- Filtro de cajas diarias
	wherePosJournalIDs = '';
	if existsPosJournalIDs then
		wherePosJournalIDs = ' AND p.c_posjournal_id IN ('|| array_to_string(posjournalIDs, ',') || ')';
	end if;
	
	consulta =  
	'SELECT al.c_allocationhdr_id, al.c_allocationline_id, al.ad_client_id, al.ad_org_id, al.isactive, al.created, al.createdby, al.updated, al.updatedby, al.c_invoice_id, al.c_payment_id, al.c_cashline_id, al.c_invoice_credit_id, p.tendertype::character varying AS tendertype, p.documentno, p.description, ((p.documentno::text || ''_''::text) || to_char(p.datetrx, ''DD/MM/YYYY''::text))::character varying AS info, COALESCE(currencyconvert((case when ah.allocationtype in (''OPA'',''RCA'') then p.payamt else al.amount + al.discountamt + al.writeoffamt end), (case when ah.allocationtype in (''OPA'',''RCA'') then p.c_currency_id else ah.c_currency_id end), ah.c_currency_id, ah.dateacct, null::integer, ah.ad_client_id, ah.ad_org_id), 0::numeric)::numeric(20,2) AS amount, NULL::integer AS c_cash_id, NULL::numeric(18,0) AS line, NULL::integer AS c_doctype_id, p.checkno, p.a_bank, p.checkno AS transferno, p.creditcardtype, p.m_entidadfinancieraplan_id, ep.m_entidadfinanciera_id, p.couponnumber, date_trunc(''day''::text, ah.datetrx) AS allocationdate, p.docstatus, p.dateacct::date AS dateacct, (case when ah.allocationtype in (''OPA'',''RCA'') then ''Anticipo'' else i.documentno end) AS invoice_documentno, i.grandtotal AS invoice_grandtotal, ef.value AS entidadfinanciera_value, ef.name AS entidadfinanciera_name, bp.value AS bp_entidadfinanciera_value, bp.name AS bp_entidadfinanciera_name, p.couponnumber AS cupon, p.creditcardnumber AS creditcard, NULL::bpchar AS isfiscaldocument, NULL::bpchar AS isfiscal, NULL::bpchar AS fiscalalreadyprinted, al.changeamt, ah.c_currency_id
           FROM c_allocationline al
      JOIN c_allocationhdr ah ON al.c_allocationhdr_id = ah.c_allocationhdr_id
   LEFT JOIN c_invoice i ON al.c_invoice_id = i.c_invoice_id
   JOIN c_payment p ON al.c_payment_id = p.c_payment_id
   LEFT JOIN m_entidadfinancieraplan ep ON p.m_entidadfinancieraplan_id = ep.m_entidadfinancieraplan_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = ep.m_entidadfinanciera_id
   LEFT JOIN c_bpartner bp ON bp.c_bpartner_id = ef.c_bpartner_id
   WHERE 1=1 ' || wherePosJournalIDs || ' AND NOT EXISTS (SELECT ala.c_payment_id FROM c_allocationline as ala INNER JOIN c_allocationhdr as aha on aha.c_allocationhdr_id = ala.c_allocationhdr_id WHERE ala.c_payment_id = p.c_payment_id AND aha.allocationtype in (''OPA'',''RCA'') AND aha.c_allocationhdr_id <> ah.c_allocationhdr_id) ' || 
   ' UNION ALL 
        ( SELECT NULL::integer AS c_allocationhdr_id, NULL::integer AS c_allocationline_id, p.ad_client_id, p.ad_org_id, p.isactive, p.created, p.createdby, p.updated, p.updatedby, NULL::integer AS c_invoice_id, p.c_payment_id, NULL::integer AS c_cashline_id, NULL::integer AS c_invoice_credit_id, p.tendertype::character varying(2) AS tendertype, p.documentno, p.description, (((p.documentno::text || ''_''::text) || to_char(p.datetrx, ''DD/MM/YYYY''::text)))::character varying(255) AS info, p.payamt AS amount, NULL::integer AS c_cash_id, NULL::numeric(18,0) AS line, NULL::integer AS c_doctype_id, p.checkno, p.a_bank, p.checkno AS transferno, p.creditcardtype, p.m_entidadfinancieraplan_id, ep.m_entidadfinanciera_id, p.couponnumber, date_trunc(''day''::text, p.datetrx) AS allocationdate, p.docstatus, p.dateacct::date AS dateacct, NULL::character varying(30) AS invoice_documentno, NULL::numeric(20,2) AS invoice_grandtotal, ef.value AS entidadfinanciera_value, ef.name AS entidadfinanciera_name, bp.value AS bp_entidadfinanciera_value, bp.name AS bp_entidadfinanciera_name, p.couponnumber AS cupon, p.creditcardnumber AS creditcard, NULL::bpchar AS isfiscaldocument, NULL::bpchar AS isfiscal, NULL::bpchar AS fiscalalreadyprinted, 0 AS changeamt, p.c_currency_id
           FROM c_payment p
      LEFT JOIN m_entidadfinancieraplan ep ON p.m_entidadfinancieraplan_id = ep.m_entidadfinancieraplan_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = ep.m_entidadfinanciera_id
   LEFT JOIN c_bpartner bp ON bp.c_bpartner_id = ef.c_bpartner_id
  WHERE 1=1 ' || wherePosJournalIDs || 
  ' AND NOT (EXISTS ( SELECT al.c_allocationline_id
    FROM c_allocationline al
   WHERE al.c_payment_id = p.c_payment_id))
  ORDER BY p.tendertype::character varying(2), p.documentno);';

	--raise notice '%', consulta;
	FOR adocument IN EXECUTE consulta LOOP
		return next adocument;
	END LOOP;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION c_posjournal_c_payment_v_filtered(anyarray)
  OWNER TO libertya;
  
--20181003-1245 Códigos o Cupones Promocionales 
update ad_system set dummy = (SELECT addcolumnifnotexists('c_promotion','promotiontype','character(1)'));

UPDATE c_promotion
set promotiontype = 'G'
where ad_client_id = 1010016;

ALTER TABLE c_promotion ALTER COLUMN promotiontype SET NOT NULL;
update ad_system set dummy = (SELECT addcolumnifnotexists('c_promotion','maxpromotionalcodes','integer NOT NULL DEFAULT 0'));

update ad_system set dummy = (SELECT addcolumnifnotexists('m_discountconfig','maxpromotionalcoupons','integer NOT NULL DEFAULT 0'));

CREATE TABLE c_promotion_code_batch
(
c_promotion_code_batch_id integer NOT NULL,
ad_client_id integer NOT NULL,
ad_org_id integer NOT NULL,
isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
created timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
createdby integer NOT NULL,
updated timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
updatedby integer NOT NULL,
datetrx timestamp without time zone NOT NULL,
description character varying(255),
voidbatch character(1) NOT NULL DEFAULT 'N'::bpchar,
processed character(1) NOT NULL DEFAULT 'N'::bpchar,
documentno character varying(30) NOT NULL,
CONSTRAINT c_promotion_code_batch_key PRIMARY KEY (c_promotion_code_batch_id),
CONSTRAINT client_promotion_code_batch FOREIGN KEY (ad_client_id)
REFERENCES ad_client (ad_client_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT org_promotion_code_batch FOREIGN KEY (ad_org_id)
REFERENCES ad_org (ad_org_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE c_promotion_code_batch
OWNER TO libertya;

CREATE TABLE c_promotion_code
(
c_promotion_code_id integer NOT NULL,
ad_client_id integer NOT NULL,
ad_org_id integer NOT NULL,
isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
created timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
createdby integer NOT NULL,
updated timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
updatedby integer NOT NULL,
c_promotion_id integer NOT NULL,
code character varying(255) NOT NULL,
validfrom timestamp without time zone NOT NULL,
validto timestamp without time zone,
c_promotion_code_batch_id integer NOT NULL,
used character(1) NOT NULL DEFAULT 'N'::bpchar,
processed character(1) NOT NULL DEFAULT 'N'::bpchar,
c_invoice_id integer,
CONSTRAINT c_promotion_code_key PRIMARY KEY (c_promotion_code_id),
CONSTRAINT c_promotion_code_invoice FOREIGN KEY (c_invoice_id)
REFERENCES c_invoice (c_invoice_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT client_promotion_code FOREIGN KEY (ad_client_id)
REFERENCES ad_client (ad_client_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT org_promotion_code FOREIGN KEY (ad_org_id)
REFERENCES ad_org (ad_org_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT promotion_promotion_code FOREIGN KEY (c_promotion_id)
REFERENCES c_promotion (c_promotion_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT promotion_promotion_code_batch FOREIGN KEY (c_promotion_code_batch_id)
REFERENCES c_promotion_code_batch (c_promotion_code_batch_id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
OIDS=FALSE
);
ALTER TABLE c_promotion_code
OWNER TO libertya;

--20181105-1630 Agregar o no validación de acceso a organizaciones para Crear Desde de Facturas
update ad_system set dummy = (SELECT addcolumnifnotexists('ad_role','addsecurityvalidation_createfrominvoice','character(1) NOT NULL DEFAULT ''Y''::bpchar'));

--20181105-1725 Agregar filtro de tipos de documento electrónicos en Resumen de Ventas
CREATE OR REPLACE FUNCTION v_dailysales_v2_filtered(
    orgid integer,
    posid integer,
    userid integer,
    datefrom date,
    dateto date,
    invoicedatefrom date,
    invoicedateto date,
    addinvoicedate boolean)
  RETURNS SETOF v_dailysales_type AS
$BODY$
declare
	consulta varchar;
	whereDateInvoices varchar;
	whereDatePayments varchar;
	whereInvoiceDate varchar;
	wherePOSInvoices varchar;
	wherePOSPayments varchar;
	whereUserInvoices varchar;
	whereUserPayments varchar;
	whereOrg varchar;
	whereClauseStd varchar;
	posJournalPaymentsFrom varchar;
	dateFromPOSJournalPayments varchar;
	dateToPOSJournalPayments varchar;
	orgIDPOSJournalPayments integer;
	adocument v_dailysales_type;
BEGIN
	-- Armado de las condiciones en base a los parámetros
	-- Organización
	whereOrg = '';
	if orgID is not null AND orgID > 0 THEN
		whereOrg = ' AND i.ad_org_id = ' || orgID;
	END IF;
	
	-- Fecha de factura
	whereInvoiceDate = '';
	if addInvoiceDate then
		if invoiceDateFrom is not null then
			whereInvoiceDate = ' AND date_trunc(''day'', i.dateacct) >= date_trunc(''day'', '''|| invoiceDateFrom || '''::date)';
		end if;
		if invoiceDateTo is not null then
			whereInvoiceDate = whereInvoiceDate || ' AND date_trunc(''day'', i.dateacct) <= date_trunc(''day'', ''' || invoiceDateTo || '''::date) ';
		end if;
	end if;

	-- Fechas para allocations y facturas
	whereDatePayments = '';
	whereDateInvoices = '';
	if dateFrom is not null then
		whereDatePayments = ' AND date_trunc(''day'', pjp.allocationdate) >= date_trunc(''day'', ''' || dateFrom || '''::date)';
		whereDateInvoices = ' AND date_trunc(''day''::text, i.dateinvoiced) >= date_trunc(''day'', ''' || dateFrom || '''::date)';
	end if;

	if dateTo is not null then
		whereDatePayments = whereDatePayments || ' AND date_trunc(''day'', pjp.allocationdate) <= date_trunc(''day'', ''' || dateTo || '''::date) ';
		whereDateInvoices = whereDateInvoices || ' AND date_trunc(''day''::text, i.dateinvoiced) <= date_trunc(''day'', ''' || dateTo || '''::date) ';
	end if;
	
	-- TPV
	wherePOSPayments = ' AND (' || posID || ' = -1 OR COALESCE(pjh.c_pos_id, pj.c_pos_id) = ' || posID || ')';
	wherePOSInvoices = ' AND (' || posID || ' = -1 OR pj.c_pos_id = ' || posID || ')';

	-- Usuario
	whereUserPayments = ' AND (' || userID || ' = -1 OR COALESCE(pjh.ad_user_id, pj.ad_user_id) = ' || userID || ')';
	whereUserInvoices = ' AND (' || userID || ' = -1 OR pj.ad_user_id = ' || userID || ')';

	-- Condiciones básicas del reporte
	whereClauseStd = ' ( i.issotrx = ''Y'' ' ||
			 whereOrg || 
			 ' AND (i.docstatus = ''CO'' or i.docstatus = ''CL'' or i.docstatus = ''RE'' or i.docstatus = ''VO'' OR i.docstatus = ''??'') ' ||
			 ' AND dtc.isfiscaldocument = ''Y'' ' || 
			 ' AND (dtc.isfiscal is null OR dtc.isfiscal = ''N'' OR (dtc.isfiscal = ''Y'' AND i.fiscalalreadyprinted = ''Y'')) ' ||
			 ' AND (dtc.iselectronic is null OR dtc.iselectronic = ''N'' OR (dtc.iselectronic = ''Y'' AND i.cae is not null)) ' ||
			 ' AND dtc.doctypekey not in (''RTR'', ''RTI'', ''RCR'', ''RCI'') ' || 
			 ' AND (dtc.transactiontypefrontliva is null OR dtc.transactiontypefrontliva = ''S'') ' || 
			 ' ) ';

	-- Agregar las condiciones anteriores
	whereClauseStd = whereClauseStd || whereInvoiceDate;

	-- Armado del llamado a la función que ejecuta la vista filtrada c_posjournalpayments_v
	dateFromPOSJournalPayments = (CASE WHEN dateFrom is null THEN 'null::date' ELSE '''' || dateFrom || '''::date' END);
	dateToPOSJournalPayments = (CASE WHEN dateTo is null THEN 'null::date' ELSE '''' || dateTo || '''::date' END);
	orgIDPOSJournalPayments = (CASE WHEN orgID is null THEN -1 ELSE orgID END);
	posJournalPaymentsFrom = 'c_posjournalpayments_v_filtered(' || orgIDPOSJournalPayments || ', ' || dateFromPOSJournalPayments || ', ' || dateToPOSJournalPayments || ')';

	-- Armar la consulta
	consulta = '(        (         SELECT ''P''::character varying AS trxtype, pjp.ad_client_id, pjp.ad_org_id, 
                                CASE
                                    WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN pjp.c_invoice_credit_id
                                    ELSE i.c_invoice_id
                                END AS c_invoice_id, pjp.allocationdate AS datetrx, pjp.c_payment_id, pjp.c_cashline_id, 
                                CASE
                                    WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN i.c_invoice_id
                                    ELSE pjp.c_invoice_credit_id
                                END AS c_invoice_credit_id, pjp.tendertype, pjp.documentno, pjp.description, pjp.info, currencybase(pjp.amount, i.c_currency_id, pjp.dateacct, pjp.ad_client_id, pjp.ad_org_id)::numeric(20,2) as amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, 
                                CASE
                                    WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dtc.c_doctype_id
                                    WHEN (dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dt.c_doctype_id
                                    WHEN pjp.c_cashline_id IS NOT NULL THEN ppmc.c_pospaymentmedium_id
                                    ELSE p.c_pospaymentmedium_id
                                END AS c_pospaymentmedium_id, 
                                CASE
                                    WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dtc.docbasetype::character varying
                                    WHEN (dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dt.docbasetype::character varying
                                    WHEN pjp.c_cashline_id IS NOT NULL THEN ppmc.name
                                    ELSE ppm.name
                                END AS pospaymentmediumname, pjp.m_entidadfinanciera_id, ef.name AS entidadfinancieraname, ef.value AS entidadfinancieravalue, pjp.m_entidadfinancieraplan_id, efp.name AS planname, pjp.docstatus, i.issotrx, pjp.dateacct, i.dateacct::date AS invoicedateacct, COALESCE(pjh.c_posjournal_id, pj.c_posjournal_id) AS c_posjournal_id, COALESCE(pjh.ad_user_id, pj.ad_user_id) AS ad_user_id, COALESCE(pjh.c_pos_id, pj.c_pos_id) AS c_pos_id, dtc.isfiscal, i.fiscalalreadyprinted
                           FROM ' || posJournalPaymentsFrom || ' pjp
                      JOIN c_invoice i ON i.c_invoice_id = pjp.c_invoice_id
                 LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
            JOIN c_doctype dtc ON i.c_doctypetarget_id = dtc.c_doctype_id
       JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   JOIN c_allocationhdr hdr ON hdr.c_allocationhdr_id = pjp.c_allocationhdr_id
   LEFT JOIN c_posjournal pjh ON pjh.c_posjournal_id = hdr.c_posjournal_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
   LEFT JOIN c_payment p ON p.c_payment_id = pjp.c_payment_id
   LEFT JOIN c_pospaymentmedium ppm ON ppm.c_pospaymentmedium_id = p.c_pospaymentmedium_id
   LEFT JOIN c_cashline c ON c.c_cashline_id = pjp.c_cashline_id
   LEFT JOIN c_pospaymentmedium ppmc ON ppmc.c_pospaymentmedium_id = c.c_pospaymentmedium_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = pjp.m_entidadfinanciera_id
   LEFT JOIN m_entidadfinancieraplan efp ON efp.m_entidadfinancieraplan_id = pjp.m_entidadfinancieraplan_id
   LEFT JOIN c_invoice cc ON cc.c_invoice_id = pjp.c_invoice_credit_id
   LEFT JOIN c_doctype dt ON cc.c_doctypetarget_id = dt.c_doctype_id
  WHERE ' || whereClauseStd || whereDatePayments || whereUserPayments || wherePOSPayments ||
  ' AND (date_trunc(''day''::text, i.dateacct) = date_trunc(''day''::text, pjp.dateacct::timestamp with time zone) OR i.initialcurrentaccountamt = 0::numeric) AND ((dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) OR (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL) AND (pjp.c_invoice_credit_id IS NULL OR pjp.c_invoice_credit_id IS NOT NULL AND (cc.docstatus = ANY (ARRAY[''CO''::bpchar, ''CL''::bpchar]))) AND NOT (EXISTS ( SELECT c2.c_payment_id
   FROM c_cashline c2
  WHERE c2.c_payment_id = pjp.c_payment_id AND i.isvoidable = ''Y''::bpchar))
                UNION ALL 
                         SELECT ''NCC''::character varying AS trxtype, i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc(''day''::text, i.dateinvoiced) AS datetrx, NULL::unknown AS c_payment_id, NULL::unknown AS c_cashline_id, NULL::unknown AS c_invoice_credit_id, 
                                CASE
                                    WHEN i.paymentrule::text = ''T''::text OR i.paymentrule::text = ''Tr''::text THEN ''A''::character varying
                                    WHEN i.paymentrule::text = ''B''::text THEN ''CA''::character varying
                                    WHEN i.paymentrule::text = ''K''::text THEN ''C''::character varying
                                    WHEN i.paymentrule::text = ''P''::text THEN ''CC''::character varying
                                    WHEN i.paymentrule::text = ''S''::text THEN ''K''::character varying
                                    ELSE i.paymentrule
                                END AS tendertype, i.documentno, i.description, NULL::unknown AS info, currencybase(i.grandtotal, i.c_currency_id, i.dateacct, i.ad_client_id, i.ad_org_id)::numeric(20,2) * dtc.signo_issotrx::numeric AS amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, 
                                CASE
                                    WHEN i.paymentrule::text = ''P''::text THEN NULL::integer
                                    ELSE ( SELECT ad_ref_list.ad_ref_list_id
                                       FROM ad_ref_list
                                      WHERE ad_ref_list.ad_reference_id = 195 AND ad_ref_list.value::text = i.paymentrule::text
                                     LIMIT 1)
                                END AS c_pospaymentmedium_id, 
                                CASE
                                    WHEN i.paymentrule::text = ''T''::text OR i.paymentrule::text = ''Tr''::text THEN ''A''::character varying
                                    WHEN i.paymentrule::text = ''B''::text THEN ''CA''::character varying
                                    WHEN i.paymentrule::text = ''K''::text THEN ''C''::character varying
                                    WHEN i.paymentrule::text = ''P''::text THEN NULL::character varying
                                    WHEN i.paymentrule::text = ''S''::text THEN ''K''::character varying
                                    ELSE i.paymentrule
                                END AS pospaymentmediumname, NULL::unknown AS m_entidadfinanciera_id, NULL::unknown AS entidadfinancieraname, NULL::unknown AS entidadfinancieravalue, NULL::unknown AS m_entidadfinancieraplan_id, NULL::unknown AS planname, i.docstatus, i.issotrx, i.dateacct::date AS dateacct, i.dateacct::date AS invoicedateacct, pj.c_posjournal_id, pj.ad_user_id, pj.c_pos_id, dtc.isfiscal, i.fiscalalreadyprinted
                           FROM c_invoice i
                      LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
                 JOIN c_doctype dtc ON i.c_doctypetarget_id = dtc.c_doctype_id
            JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
       JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
  WHERE ' || whereClauseStd || whereDateInvoices || whereUserInvoices || wherePOSInvoices ||
  ' AND dtc.docbasetype = ''ARC''::bpchar AND ((i.docstatus = ANY (ARRAY[''CO''::bpchar, ''CL''::bpchar])) OR (i.docstatus = ANY (ARRAY[''VO''::bpchar, ''RE''::bpchar])) AND (EXISTS ( SELECT al.c_allocationline_id
          FROM c_allocationline al
         WHERE al.c_invoice_id = i.c_invoice_id))) AND NOT (EXISTS ( SELECT al.c_allocationline_id
          FROM c_allocationline al
         WHERE al.c_invoice_credit_id = i.c_invoice_id)))
        UNION ALL 
                 SELECT ''PA''::character varying AS trxtype, pjp.ad_client_id, pjp.ad_org_id, 
                        CASE
                            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN pjp.c_invoice_credit_id
                            ELSE i.c_invoice_id
                        END AS c_invoice_id, pjp.allocationdate AS datetrx, pjp.c_payment_id, pjp.c_cashline_id, 
                        CASE
                            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN i.c_invoice_id
                            ELSE pjp.c_invoice_credit_id
                        END AS c_invoice_credit_id, pjp.tendertype, pjp.documentno, pjp.description, pjp.info, currencybase(pjp.amount, i.c_currency_id, pjp.dateacct, pjp.ad_client_id, pjp.ad_org_id)::numeric(20,2) * (-1)::numeric AS amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, 
                        CASE
                            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dtc.c_doctype_id
                            WHEN (dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dt.c_doctype_id
                            WHEN pjp.c_cashline_id IS NOT NULL THEN ppmc.c_pospaymentmedium_id
                            ELSE p.c_pospaymentmedium_id
                        END AS c_pospaymentmedium_id, 
                        CASE
                            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dtc.docbasetype::character varying
                            WHEN (dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dt.docbasetype::character varying
                            WHEN pjp.c_cashline_id IS NOT NULL THEN ppmc.name
                            ELSE ppm.name
                        END AS pospaymentmediumname, pjp.m_entidadfinanciera_id, ef.name AS entidadfinancieraname, ef.value AS entidadfinancieravalue, pjp.m_entidadfinancieraplan_id, efp.name AS planname, pjp.docstatus, i.issotrx, pjp.dateacct, i.dateacct::date AS invoicedateacct, COALESCE(pjh.c_posjournal_id, pj.c_posjournal_id) AS c_posjournal_id, COALESCE(pjh.ad_user_id, pj.ad_user_id) AS ad_user_id, COALESCE(pjh.c_pos_id, pj.c_pos_id) AS c_pos_id, dtc.isfiscal, i.fiscalalreadyprinted
                   FROM ' || posJournalPaymentsFrom || ' pjp
              JOIN c_invoice i ON i.c_invoice_id = pjp.c_invoice_id
         LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
    JOIN c_doctype dtc ON i.c_doctypetarget_id = dtc.c_doctype_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   JOIN c_allocationhdr hdr ON hdr.c_allocationhdr_id = pjp.c_allocationhdr_id
   LEFT JOIN c_posjournal pjh ON pjh.c_posjournal_id = hdr.c_posjournal_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
   LEFT JOIN c_payment p ON p.c_payment_id = pjp.c_payment_id
   LEFT JOIN c_pospaymentmedium ppm ON ppm.c_pospaymentmedium_id = p.c_pospaymentmedium_id
   LEFT JOIN c_cashline c ON c.c_cashline_id = pjp.c_cashline_id
   LEFT JOIN c_pospaymentmedium ppmc ON ppmc.c_pospaymentmedium_id = c.c_pospaymentmedium_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = pjp.m_entidadfinanciera_id
   LEFT JOIN m_entidadfinancieraplan efp ON efp.m_entidadfinancieraplan_id = pjp.m_entidadfinancieraplan_id
   LEFT JOIN c_invoice cc ON cc.c_invoice_id = pjp.c_invoice_credit_id
   LEFT JOIN c_doctype dt ON cc.c_doctypetarget_id = dt.c_doctype_id
  WHERE ' || whereClauseStd || whereDatePayments || whereUserPayments || wherePOSPayments ||
  ' AND (date_trunc(''day''::text, i.dateacct) = date_trunc(''day''::text, pjp.dateacct::timestamp with time zone) OR i.initialcurrentaccountamt = 0::numeric) AND hdr.isactive = ''N''::bpchar AND NOT (EXISTS ( SELECT c2.c_payment_id
   FROM c_cashline c2
  WHERE c2.c_payment_id = pjp.c_payment_id AND i.isvoidable = ''Y''::bpchar)))
UNION ALL 
         SELECT ''ND''::character varying AS trxtype, i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc(''day''::text, i.dateinvoiced) AS datetrx, NULL::unknown AS c_payment_id, NULL::unknown AS c_cashline_id, NULL::unknown AS c_invoice_credit_id, 
                CASE
                    WHEN i.paymentrule::text = ''T''::text OR i.paymentrule::text = ''Tr''::text THEN ''A''::character varying
                    WHEN i.paymentrule::text = ''B''::text THEN ''CA''::character varying
                    WHEN i.paymentrule::text = ''K''::text THEN ''C''::character varying
                    WHEN i.paymentrule::text = ''P''::text THEN ''CC''::character varying
                    WHEN i.paymentrule::text = ''S''::text THEN ''K''::character varying
                    ELSE i.paymentrule
                END AS tendertype, i.documentno, i.description, NULL::unknown AS info, currencybase(i.grandtotal, i.c_currency_id, i.dateacct, i.ad_client_id, i.ad_org_id)::numeric(20,2) * dtc.signo_issotrx::numeric AS amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, ( SELECT ad_ref_list.ad_ref_list_id
                   FROM ad_ref_list
                  WHERE ad_ref_list.ad_reference_id = 195 AND ad_ref_list.value::text = i.paymentrule::text
                 LIMIT 1) AS c_pospaymentmedium_id, 
                CASE
                    WHEN i.paymentrule::text = ''T''::text OR i.paymentrule::text = ''Tr''::text THEN ''A''::character varying
                    WHEN i.paymentrule::text = ''B''::text THEN ''CA''::character varying
                    WHEN i.paymentrule::text = ''K''::text THEN ''C''::character varying
                    WHEN i.paymentrule::text = ''P''::text THEN ''CC''::character varying
                    WHEN i.paymentrule::text = ''S''::text THEN ''K''::character varying
                    ELSE i.paymentrule
                END AS pospaymentmediumname, NULL::unknown AS m_entidadfinanciera_id, NULL::unknown AS entidadfinancieraname, NULL::unknown AS entidadfinancieravalue, NULL::unknown AS m_entidadfinancieraplan_id, NULL::unknown AS planname, i.docstatus, i.issotrx, i.dateacct::date AS dateacct, i.dateacct::date AS invoicedateacct, pj.c_posjournal_id, pj.ad_user_id, pj.c_pos_id, dtc.isfiscal, i.fiscalalreadyprinted
           FROM c_invoice i
      LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
   JOIN c_doctype dtc ON i.c_doctypetarget_id = dtc.c_doctype_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
  WHERE ' || whereClauseStd || whereDateInvoices || whereUserInvoices || wherePOSInvoices ||
  ' AND "position"(dtc.doctypekey::text, ''CDN''::text) = 1 AND ((i.docstatus = ANY (ARRAY[''CO''::bpchar, ''CL''::bpchar])) OR (i.docstatus = ANY (ARRAY[''VO''::bpchar, ''RE''::bpchar])) AND (EXISTS ( SELECT al.c_allocationline_id
   FROM c_allocationline al
  WHERE al.c_invoice_credit_id = i.c_invoice_id))) AND NOT (EXISTS ( SELECT al.c_allocationline_id
   FROM c_allocationline al
  WHERE al.c_invoice_id = i.c_invoice_id));';

--raise notice '%', consulta;
FOR adocument IN EXECUTE consulta LOOP
	return next adocument;
END LOOP;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION v_dailysales_v2_filtered(integer, integer, integer, date, date, date, date, boolean)
  OWNER TO libertya;

--Invoices
CREATE OR REPLACE FUNCTION v_dailysales_invoices_filtered(
    orgid integer,
    posid integer,
    userid integer,
    datefrom date,
    dateto date,
    invoicedatefrom date,
    invoicedateto date,
    addinvoicedate boolean)
  RETURNS SETOF v_dailysales_type AS
$BODY$
declare
	consulta varchar;
	whereDateInvoices varchar;
	whereInvoiceDate varchar;
	wherePOSInvoices varchar;
	whereUserInvoices varchar;
	whereOrg varchar;
	whereClauseStd varchar;
	adocument v_dailysales_type;
BEGIN
	-- Armado de las condiciones en base a los parámetros
	-- Organización
	whereOrg = '';
	if orgID is not null AND orgID > 0 THEN
		whereOrg = ' AND i.ad_org_id = ' || orgID;
	END IF;
	
	-- Fecha de factura
	whereInvoiceDate = '';
	if addInvoiceDate then
		if invoiceDateFrom is not null then
			whereInvoiceDate = ' AND date_trunc(''day'', i.dateacct) >= date_trunc(''day'', '''|| invoiceDateFrom || '''::date)';
		end if;
		if invoiceDateTo is not null then
			whereInvoiceDate = whereInvoiceDate || ' AND date_trunc(''day'', i.dateacct) <= date_trunc(''day'', ''' || invoiceDateTo || '''::date) ';
		end if;
	end if;

	-- Fechas para allocations y facturas
	whereDateInvoices = '';
	if dateFrom is not null then
		whereDateInvoices = ' AND date_trunc(''day''::text, i.dateinvoiced) >= date_trunc(''day'', ''' || dateFrom || '''::date)';
	end if;

	if dateTo is not null then
		whereDateInvoices = whereDateInvoices || ' AND date_trunc(''day''::text, i.dateinvoiced) <= date_trunc(''day'', ''' || dateTo || '''::date) ';
	end if;
	
	-- TPV
	wherePOSInvoices = ' AND (' || posID || ' = -1 OR pj.c_pos_id = ' || posID || ')';

	-- Usuario
	whereUserInvoices = ' AND (' || userID || ' = -1 OR pj.ad_user_id = ' || userID || ')';

	-- Condiciones básicas del reporte
	whereClauseStd = ' ( i.issotrx = ''Y'' ' ||
			 whereOrg || 
			 ' AND (i.docstatus = ''CO'' or i.docstatus = ''CL'' or i.docstatus = ''RE'' or i.docstatus = ''VO'' OR i.docstatus = ''??'') ' ||
			 ' AND dtc.isfiscaldocument = ''Y'' ' || 
			 ' AND (dtc.isfiscal is null OR dtc.isfiscal = ''N'' OR (dtc.isfiscal = ''Y'' AND i.fiscalalreadyprinted = ''Y'')) ' ||
			 ' AND (dtc.iselectronic is null OR dtc.iselectronic = ''N'' OR (dtc.iselectronic = ''Y'' AND i.cae is not null)) ' ||
			 ' AND dtc.doctypekey not in (''RTR'', ''RTI'', ''RCR'', ''RCI'') ' || 
			 ' AND (dtc.transactiontypefrontliva is null OR dtc.transactiontypefrontliva = ''S'') ' || 
			 ' ) ';

	-- Agregar las condiciones anteriores
	whereClauseStd = whereClauseStd || whereInvoiceDate || whereDateInvoices || wherePOSInvoices || whereUserInvoices;

	-- Armar la consulta
	consulta = 'SELECT ''I''::character varying AS trxtype, i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc(''day''::text, i.dateinvoiced) AS datetrx, NULL::integer AS c_payment_id, NULL::integer AS c_cashline_id, NULL::integer AS c_invoice_credit_id, dtc.docbasetype AS tendertype, i.documentno, i.description, NULL::unknown AS info, currencybase(i.grandtotal, i.c_currency_id, i.dateacct, i.ad_client_id, i.ad_org_id)::numeric(20,2) * dtc.signo_issotrx::numeric AS amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, dtc.c_doctype_id AS c_pospaymentmedium_id, dtc.name AS pospaymentmediumname, NULL::integer AS m_entidadfinanciera_id, NULL::unknown AS entidadfinancieraname, NULL::unknown AS entidadfinancieravalue, NULL::integer AS m_entidadfinancieraplan_id, NULL::unknown AS planname, i.docstatus, i.issotrx, i.dateacct::date AS dateacct, i.dateacct::date AS invoicedateacct, pj.c_posjournal_id, pj.ad_user_id, pj.c_pos_id, dtc.isfiscal, i.fiscalalreadyprinted
   FROM c_invoice i
   LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
   JOIN c_doctype dtc ON i.c_doctypetarget_id = dtc.c_doctype_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
  WHERE ' || whereClauseStd ||
  ' AND NOT (
  		EXISTS (
			SELECT * FROM (
				SELECT *
				FROM c_allocationline al
				WHERE i.c_invoice_id = al.c_invoice_id AND i.isvoidable = ''Y''::bpchar 
			) as FOO
			JOIN c_payment p ON p.c_payment_id = foo.c_payment_id
			JOIN c_cashline cl ON cl.c_payment_id = p.c_payment_id
		)
	);';

-- raise notice '%', consulta;
FOR adocument IN EXECUTE consulta LOOP
	return next adocument;
END LOOP;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION v_dailysales_invoices_filtered(integer, integer, integer, date, date, date, date, boolean)
  OWNER TO libertya;

--Current account filtered
CREATE OR REPLACE FUNCTION v_dailysales_current_account_filtered(
    orgid integer,
    posid integer,
    userid integer,
    datefrom date,
    dateto date,
    invoicedatefrom date,
    invoicedateto date,
    addinvoicedate boolean)
  RETURNS SETOF v_dailysales_type AS
$BODY$
declare
	consulta varchar;
	whereDateInvoices varchar;
	whereInvoiceDate varchar;
	wherePOSInvoices varchar;
	whereUserInvoices varchar;
	whereOrg varchar;
	whereClauseStd varchar;
	posJournalPaymentsFrom varchar;
	dateFromPOSJournalPayments varchar;
	dateToPOSJournalPayments varchar;
	orgIDPOSJournalPayments integer;
	adocument v_dailysales_type;
BEGIN
	-- Armado de las condiciones en base a los parámetros
	-- Organización
	whereOrg = '';
	if orgID is not null AND orgID > 0 THEN
		whereOrg = ' AND i.ad_org_id = ' || orgID;
	END IF;
	
	-- Fecha de factura
	whereInvoiceDate = '';
	if addInvoiceDate then
		if invoiceDateFrom is not null then
			whereInvoiceDate = ' AND date_trunc(''day'', i.dateacct) >= date_trunc(''day'', '''|| invoiceDateFrom || '''::date)';
		end if;
		if invoiceDateTo is not null then
			whereInvoiceDate = whereInvoiceDate || ' AND date_trunc(''day'', i.dateacct) <= date_trunc(''day'', ''' || invoiceDateTo || '''::date) ';
		end if;
	end if;

	-- Fechas para allocations y facturas
	whereDateInvoices = '';
	if dateFrom is not null then
		whereDateInvoices = ' AND date_trunc(''day''::text, i.dateinvoiced) >= date_trunc(''day'', ''' || dateFrom || '''::date)';
	end if;

	if dateTo is not null then
		whereDateInvoices = whereDateInvoices || ' AND date_trunc(''day''::text, i.dateinvoiced) <= date_trunc(''day'', ''' || dateTo || '''::date) ';
	end if;
	
	-- TPV
	wherePOSInvoices = ' AND (' || posID || ' = -1 OR pj.c_pos_id = ' || posID || ')';

	-- Usuario
	whereUserInvoices = ' AND (' || userID || ' = -1 OR pj.ad_user_id = ' || userID || ')';

	-- Condiciones básicas del reporte
	whereClauseStd = ' ( i.issotrx = ''Y'' ' ||
			 whereOrg || 
			 ' AND (i.docstatus = ''CO'' or i.docstatus = ''CL'' or i.docstatus = ''RE'' or i.docstatus = ''VO'' OR i.docstatus = ''??'') ' ||
			 ' AND dt.isfiscaldocument = ''Y'' ' || 
			 ' AND (dt.isfiscal is null OR dt.isfiscal = ''N'' OR (dt.isfiscal = ''Y'' AND i.fiscalalreadyprinted = ''Y'')) ' ||
			 ' AND (dt.iselectronic is null OR dt.iselectronic = ''N'' OR (dt.iselectronic = ''Y'' AND i.cae is not null)) ' ||
			 ' AND dt.doctypekey not in (''RTR'', ''RTI'', ''RCR'', ''RCI'') ' || 
			 ' AND (dt.transactiontypefrontliva is null OR dt.transactiontypefrontliva = ''S'') ' || 
			 ' ) ';

	-- Agregar las condiciones anteriores
	whereClauseStd = whereClauseStd || whereInvoiceDate;

	-- Armado del llamado a la función que ejecuta la vista filtrada c_posjournalpayments_v
	dateFromPOSJournalPayments = (CASE WHEN dateFrom is null THEN 'null::date' ELSE '''' || dateFrom || '''::date' END);
	dateToPOSJournalPayments = (CASE WHEN dateTo is null THEN 'null::date' ELSE '''' || dateTo || '''::date' END);
	orgIDPOSJournalPayments = (CASE WHEN orgID is null THEN -1 ELSE orgID END);
	posJournalPaymentsFrom = 'c_posjournalpayments_v_filtered(' || orgIDPOSJournalPayments || ', ' || dateFromPOSJournalPayments || ', ' || dateToPOSJournalPayments || ')';

	-- Armar la consulta
	consulta = 'SELECT ''CAI''::character varying AS trxtype, i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc(''day''::text, i.dateinvoiced) AS datetrx, NULL::integer AS c_payment_id, NULL::integer AS c_cashline_id, NULL::integer AS c_invoice_credit_id, ''CC'' AS tendertype, i.documentno, i.description, NULL::unknown AS info, (currencybase(i.grandtotal, i.c_currency_id, i.dateacct, i.ad_client_id, i.ad_org_id)::numeric(20,2) - COALESCE(cobros.amount, 0::numeric))::numeric(20,2) AS amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, NULL::unknown AS c_pospaymentmedium_id, NULL::unknown AS pospaymentmediumname, NULL::integer AS m_entidadfinanciera_id, NULL::unknown AS entidadfinancieraname, NULL::unknown AS entidadfinancieravalue, NULL::integer AS m_entidadfinancieraplan_id, NULL::unknown AS planname, i.docstatus, i.issotrx, i.dateacct::date AS dateacct, i.dateacct::date AS invoicedateacct, pj.c_posjournal_id, pj.ad_user_id, pj.c_pos_id, dt.isfiscal, i.fiscalalreadyprinted
           FROM c_invoice i
      LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
   JOIN c_doctype dt ON i.c_doctypetarget_id = dt.c_doctype_id
   LEFT JOIN ( SELECT c.c_invoice_id, sum(c.amount) AS amount
         FROM ( SELECT 
                      CASE
                          WHEN (dt.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN pjp.c_invoice_credit_id
                          ELSE i.c_invoice_id
                      END AS c_invoice_id, currencybase(pjp.amount, i.c_currency_id, pjp.dateacct, pjp.ad_client_id, pjp.ad_org_id)::numeric(20,2) as amount
                 FROM ' || posJournalPaymentsFrom || ' pjp
            JOIN c_invoice i ON i.c_invoice_id = pjp.c_invoice_id
       JOIN c_doctype dt ON i.c_doctypetarget_id = dt.c_doctype_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   JOIN c_allocationhdr hdr ON hdr.c_allocationhdr_id = pjp.c_allocationhdr_id
   LEFT JOIN c_posjournal as pj on pj.c_posjournal_id = i.c_posjournal_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
   LEFT JOIN c_payment p ON p.c_payment_id = pjp.c_payment_id
   LEFT JOIN c_pospaymentmedium ppm ON ppm.c_pospaymentmedium_id = p.c_pospaymentmedium_id
   LEFT JOIN c_cashline c ON c.c_cashline_id = pjp.c_cashline_id
   LEFT JOIN c_pospaymentmedium ppmc ON ppmc.c_pospaymentmedium_id = c.c_pospaymentmedium_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = pjp.m_entidadfinanciera_id
   LEFT JOIN m_entidadfinancieraplan efp ON efp.m_entidadfinancieraplan_id = pjp.m_entidadfinancieraplan_id
   LEFT JOIN c_invoice cc ON cc.c_invoice_id = pjp.c_invoice_credit_id
  WHERE ' || whereClauseStd || whereDateInvoices || whereUserInvoices || wherePOSInvoices ||
  ' AND date_trunc(''day''::text, i.dateacct) = date_trunc(''day''::text, pjp.dateacct::timestamp with time zone) AND ((i.docstatus = ANY (ARRAY[''CO''::bpchar, ''CL''::bpchar])) AND hdr.isactive = ''Y''::bpchar OR (i.docstatus = ANY (ARRAY[''VO''::bpchar, ''RE''::bpchar])) AND hdr.isactive = ''N''::bpchar)) c
        GROUP BY c.c_invoice_id) cobros ON cobros.c_invoice_id = i.c_invoice_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
  WHERE ' || whereClauseStd || whereDateInvoices || whereUserInvoices || wherePOSInvoices ||
  ' AND (cobros.amount IS NULL OR i.grandtotal <> cobros.amount) AND i.initialcurrentaccountamt > 0::numeric AND (dt.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND "position"(dt.doctypekey::text, ''CDN''::text) < 1
UNION ALL 
         SELECT ''CAIA''::character varying AS trxtype, i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc(''day''::text, i.dateinvoiced) AS datetrx, NULL::integer AS c_payment_id, NULL::integer AS c_cashline_id, NULL::integer AS c_invoice_credit_id, ''CC'' AS tendertype, i.documentno, i.description, NULL::unknown AS info, (currencybase(i.grandtotal, i.c_currency_id, i.dateacct, i.ad_client_id, i.ad_org_id)::numeric(20,2) - COALESCE(cobros.amount, 0::numeric))::numeric(20,2) * (-1)::numeric AS amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, NULL::integer AS c_pospaymentmedium_id, NULL::unknown AS pospaymentmediumname, NULL::integer AS m_entidadfinanciera_id, NULL::unknown AS entidadfinancieraname, NULL::unknown AS entidadfinancieravalue, NULL::integer AS m_entidadfinancieraplan_id, NULL::unknown AS planname, i.docstatus, i.issotrx, i.dateacct::date AS dateacct, i.dateacct::date AS invoicedateacct, pj.c_posjournal_id, pj.ad_user_id, pj.c_pos_id, dt.isfiscal, i.fiscalalreadyprinted
           FROM c_invoice i
      LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
   JOIN c_doctype dt ON i.c_doctypetarget_id = dt.c_doctype_id
   LEFT JOIN ( SELECT c.c_invoice_id, sum(c.amount) AS amount
         FROM ( SELECT 
                      CASE
                          WHEN (dt.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN pjp.c_invoice_credit_id
                          ELSE i.c_invoice_id
                      END AS c_invoice_id, currencybase(pjp.amount, i.c_currency_id, pjp.dateacct, pjp.ad_client_id, pjp.ad_org_id)::numeric(20,2) as amount
                 FROM ' || posJournalPaymentsFrom || ' pjp
            JOIN c_invoice i ON i.c_invoice_id = pjp.c_invoice_id
       JOIN c_doctype dt ON i.c_doctypetarget_id = dt.c_doctype_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   JOIN c_allocationhdr hdr ON hdr.c_allocationhdr_id = pjp.c_allocationhdr_id
   LEFT JOIN c_posjournal as pj on pj.c_posjournal_id = i.c_posjournal_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
   LEFT JOIN c_payment p ON p.c_payment_id = pjp.c_payment_id
   LEFT JOIN c_pospaymentmedium ppm ON ppm.c_pospaymentmedium_id = p.c_pospaymentmedium_id
   LEFT JOIN c_cashline c ON c.c_cashline_id = pjp.c_cashline_id
   LEFT JOIN c_pospaymentmedium ppmc ON ppmc.c_pospaymentmedium_id = c.c_pospaymentmedium_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = pjp.m_entidadfinanciera_id
   LEFT JOIN m_entidadfinancieraplan efp ON efp.m_entidadfinancieraplan_id = pjp.m_entidadfinancieraplan_id
   LEFT JOIN c_invoice cc ON cc.c_invoice_id = pjp.c_invoice_credit_id
  WHERE ' || whereClauseStd || whereDateInvoices || whereUserInvoices || wherePOSInvoices ||
  ' AND date_trunc(''day''::text, i.dateacct) = date_trunc(''day''::text, pjp.dateacct::timestamp with time zone) AND ((i.docstatus = ANY (ARRAY[''CO''::bpchar, ''CL''::bpchar])) AND hdr.isactive = ''Y''::bpchar OR (i.docstatus = ANY (ARRAY[''VO''::bpchar, ''RE''::bpchar])) AND hdr.isactive = ''N''::bpchar)) c
        GROUP BY c.c_invoice_id) cobros ON cobros.c_invoice_id = i.c_invoice_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
  WHERE ' || whereClauseStd || whereDateInvoices || whereUserInvoices || wherePOSInvoices ||
  ' AND (cobros.amount IS NULL OR i.grandtotal <> cobros.amount) AND i.initialcurrentaccountamt > 0::numeric AND (dt.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND "position"(dt.doctypekey::text, ''CDN''::text) < 1 AND (i.docstatus = ANY (ARRAY[''VO''::bpchar, ''RE''::bpchar]));';

-- raise notice '%', consulta;
FOR adocument IN EXECUTE consulta LOOP
	return next adocument;
END LOOP;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION v_dailysales_current_account_filtered(integer, integer, integer, date, date, date, date, boolean)
  OWNER TO libertya;

--Current accounts payment
CREATE OR REPLACE FUNCTION v_dailysales_current_account_payments_filtered(
    orgid integer,
    posid integer,
    userid integer,
    datefrom date,
    dateto date)
  RETURNS SETOF v_dailysales_type AS
$BODY$
declare
	consulta varchar;
	whereDatePayments varchar;
	wherePOSPayments varchar;
	whereUserPayments varchar;
	whereOrg varchar;
	whereClauseStd varchar;
	posJournalPaymentsFrom varchar;
	dateFromPOSJournalPayments varchar;
	dateToPOSJournalPayments varchar;
	orgIDPOSJournalPayments integer;
	adocument v_dailysales_type;
BEGIN
	-- Armado de las condiciones en base a los parámetros
	-- Organización
	whereOrg = '';
	if orgID is not null AND orgID > 0 THEN
		whereOrg = ' AND i.ad_org_id = ' || orgID;
	END IF;

	-- Fechas para allocations y facturas
	whereDatePayments = '';
	if dateFrom is not null then
		whereDatePayments = ' AND date_trunc(''day'', pjp.allocationdate) >= date_trunc(''day'', ''' || dateFrom || '''::date)';
	end if;

	if dateTo is not null then
		whereDatePayments = whereDatePayments || ' AND date_trunc(''day'', pjp.allocationdate) <= date_trunc(''day'', ''' || dateTo || '''::date) ';
	end if;
	
	-- TPV
	wherePOSPayments = ' AND (' || posID || ' = -1 OR COALESCE(pjh.c_pos_id, pj.c_pos_id) = ' || posID || ')';

	-- Usuario
	whereUserPayments = ' AND (' || userID || ' = -1 OR COALESCE(pjh.ad_user_id, pj.ad_user_id) = ' || userID || ')';

	-- Condiciones básicas del reporte
	whereClauseStd = ' ( i.issotrx = ''Y'' ' ||
			 whereOrg || 
			 ' AND (i.docstatus = ''CO'' or i.docstatus = ''CL'' or i.docstatus = ''RE'' or i.docstatus = ''VO'' OR i.docstatus = ''??'') ' ||
			 ' AND dtc.isfiscaldocument = ''Y'' ' || 
			 ' AND (dtc.isfiscal is null OR dtc.isfiscal = ''N'' OR (dtc.isfiscal = ''Y'' AND i.fiscalalreadyprinted = ''Y'')) ' ||
			 ' AND (dtc.iselectronic is null OR dtc.iselectronic = ''N'' OR (dtc.iselectronic = ''Y'' AND i.cae is not null)) ' ||
			 ' AND dtc.doctypekey not in (''RTR'', ''RTI'', ''RCR'', ''RCI'') ' || 
			 ' AND (dtc.transactiontypefrontliva is null OR dtc.transactiontypefrontliva = ''S'') ' || 
			 ' ) ';

	-- Armado del llamado a la función que ejecuta la vista filtrada c_posjournalpayments_v
	dateFromPOSJournalPayments = (CASE WHEN dateFrom is null THEN 'null::date' ELSE '''' || dateFrom || '''::date' END);
	dateToPOSJournalPayments = (CASE WHEN dateTo is null THEN 'null::date' ELSE '''' || dateTo || '''::date' END);
	orgIDPOSJournalPayments = (CASE WHEN orgID is null THEN -1 ELSE orgID END);
	posJournalPaymentsFrom = 'c_posjournalpayments_v_filtered(' || orgIDPOSJournalPayments || ', ' || dateFromPOSJournalPayments || ', ' || dateToPOSJournalPayments || ')';

	-- Armar la consulta
	consulta = 'SELECT ''PCA''::character varying AS trxtype, pjp.ad_client_id, pjp.ad_org_id, 
        CASE
            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN pjp.c_invoice_credit_id
            ELSE i.c_invoice_id
        END AS c_invoice_id, pjp.allocationdate AS datetrx, pjp.c_payment_id, pjp.c_cashline_id, 
        CASE
            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN i.c_invoice_id
            ELSE pjp.c_invoice_credit_id
        END AS c_invoice_credit_id, pjp.tendertype, pjp.documentno, pjp.description, pjp.info, currencybase(pjp.amount, i.c_currency_id, pjp.dateacct, pjp.ad_client_id, pjp.ad_org_id)::numeric(20,2) as amount, bp.c_bpartner_id, bp.name, bp.c_bp_group_id, bpg.name AS groupname, bp.c_categoria_iva_id, ci.name AS categorianame, 
        CASE
            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dtc.c_doctype_id
            WHEN (dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dt.c_doctype_id
            WHEN pjp.c_cashline_id IS NOT NULL THEN ppmc.c_pospaymentmedium_id
            ELSE p.c_pospaymentmedium_id
        END AS c_pospaymentmedium_id, 
        CASE
            WHEN (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dtc.docbasetype::character varying
            WHEN (dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL THEN dt.docbasetype::character varying
            WHEN pjp.c_cashline_id IS NOT NULL THEN ppmc.name
            ELSE ppm.name
        END AS pospaymentmediumname, pjp.m_entidadfinanciera_id, ef.name AS entidadfinancieraname, ef.value AS entidadfinancieravalue, pjp.m_entidadfinancieraplan_id, efp.name AS planname, pjp.docstatus, i.issotrx, pjp.dateacct, i.dateacct::date AS invoicedateacct, COALESCE(pjh.c_posjournal_id, pj.c_posjournal_id) AS c_posjournal_id, COALESCE(pjh.ad_user_id, pj.ad_user_id) AS ad_user_id, COALESCE(pjh.c_pos_id, pj.c_pos_id) AS c_pos_id, dtc.isfiscal, i.fiscalalreadyprinted
   FROM ' || posJournalPaymentsFrom || ' pjp
   JOIN c_invoice i ON i.c_invoice_id = pjp.c_invoice_id
   LEFT JOIN c_posjournal pj ON pj.c_posjournal_id = i.c_posjournal_id
   JOIN c_doctype dtc ON i.c_doctypetarget_id = dtc.c_doctype_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_bp_group bpg ON bpg.c_bp_group_id = bp.c_bp_group_id
   JOIN c_allocationhdr hdr ON hdr.c_allocationhdr_id = pjp.c_allocationhdr_id
   LEFT JOIN c_posjournal pjh ON pjh.c_posjournal_id = hdr.c_posjournal_id
   LEFT JOIN c_categoria_iva ci ON ci.c_categoria_iva_id = bp.c_categoria_iva_id
   LEFT JOIN c_payment p ON p.c_payment_id = pjp.c_payment_id
   LEFT JOIN c_pospaymentmedium ppm ON ppm.c_pospaymentmedium_id = p.c_pospaymentmedium_id
   LEFT JOIN c_cashline c ON c.c_cashline_id = pjp.c_cashline_id
   LEFT JOIN c_pospaymentmedium ppmc ON ppmc.c_pospaymentmedium_id = c.c_pospaymentmedium_id
   LEFT JOIN m_entidadfinanciera ef ON ef.m_entidadfinanciera_id = pjp.m_entidadfinanciera_id
   LEFT JOIN m_entidadfinancieraplan efp ON efp.m_entidadfinancieraplan_id = pjp.m_entidadfinancieraplan_id
   LEFT JOIN c_invoice cc ON cc.c_invoice_id = pjp.c_invoice_credit_id
   LEFT JOIN c_doctype dt ON cc.c_doctypetarget_id = dt.c_doctype_id
  WHERE ' || whereClauseStd || whereDatePayments || whereUserPayments || wherePOSPayments ||
  ' AND date_trunc(''day''::text, i.dateacct) <> date_trunc(''day''::text, pjp.allocationdateacct::timestamp with time zone) AND i.initialcurrentaccountamt > 0::numeric AND hdr.isactive = ''Y''::bpchar AND ((dtc.docbasetype <> ALL (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) OR (dtc.docbasetype = ANY (ARRAY[''ARC''::bpchar, ''APC''::bpchar])) AND pjp.c_invoice_credit_id IS NOT NULL);';

-- raise notice '%', consulta;
FOR adocument IN EXECUTE consulta LOOP
	return next adocument;
END LOOP;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION v_dailysales_current_account_payments_filtered(integer, integer, integer, date, date)
  OWNER TO libertya;
  
--20181115-1545 Nueva función para determinar la cantidad facturada en devoluciones
CREATE OR REPLACE FUNCTION getInvoicedQtyReturned(orderlineID integer)
  RETURNS numeric AS
$BODY$
DECLARE
	qty numeric;
BEGIN
	select into qty sum(il.qtyinvoiced)
	from c_orderline as ol 
	inner join m_inoutline as iol on iol.c_orderline_id = ol.c_orderline_id 
	inner join m_inout as io on io.m_inout_id = iol.m_inout_id 
	inner join c_doctype as dt on dt.c_doctype_id = io.c_doctype_id 
	inner join c_invoiceline as il on il.m_inoutline_id = iol.m_inoutline_id 
	inner join c_invoice as i on i.c_invoice_id = il.c_invoice_id 
	where ol.c_orderline_id = orderlineID AND dt.doctypekey = 'DC' and io.docstatus IN ('CL','CO') and i.docstatus IN ('CL','CO');

	if (qty is null) then qty = 0; end if;
	
	return qty;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getInvoicedQtyReturned(integer)
  OWNER TO libertya;
  
--20181128-1325 Incorporación de permisos por perfil para visualizar las pestañas Pendente de Recibir y de Entrega en ventana Historial, accesible desde los Info Product e Info BPartner
update ad_system set dummy = (SELECT addcolumnifnotexists('ad_role','allow_info_product_reserved_tab','character(1) NOT NULL DEFAULT ''Y''::bpchar'));
update ad_system set dummy = (SELECT addcolumnifnotexists('ad_role','allow_info_product_ordered_tab','character(1) NOT NULL DEFAULT ''Y''::bpchar'));

--20181128-1355 Incorporación de permisos por perfil para visualizar las opciones de Creación y Actualización de Entidades Comerciales en el campo
update ad_system set dummy = (SELECT addcolumnifnotexists('ad_role','lookup_allow_bpartner_create_menu','character(1) NOT NULL DEFAULT ''Y''::bpchar'));

--20181129-1730 Funview correspondiente a la view v_product_movements_detailed. Mejoras de performance
--TYPE v_product_movements_detailed_type
CREATE TYPE v_product_movements_detailed_type AS (movement_table text, ad_client_id integer, ad_org_id integer, 
		m_locator_id integer, m_warehouse_id integer, warehouse_value varchar(40), warehouse_name varchar(60), 
		receiptvalue text, movementdate timestamp, doctypename varchar(60), documentno varchar(60), 
		docstatus character(2), m_product_id integer, product_value varchar(40), product_name varchar(60), 
		qty numeric(22,4), c_order_id integer, created timestamp, updated timestamp);
	
--FUNCTION v_product_movements_detailed_filtered(integer)
CREATE OR REPLACE FUNCTION v_product_movements_detailed_filtered(productID integer)
  RETURNS SETOF v_product_movements_detailed_type AS
$BODY$
declare
	consulta varchar;
	productCondition varchar;
	adocument v_product_movements_detailed_type;
BEGIN
	-- Armar la condición por el artículo
	productCondition = '(' || $1 || ' <= 0 or m_product_id = ' || $1 || ')';
	-- Armar la consulta
	consulta = ' SELECT t.movement_table, t.ad_client_id, t.ad_org_id, t.m_locator_id, w.m_warehouse_id, w.value AS warehouse_value, w.name AS warehouse_name, t.receiptvalue, t.movementdate, t.doctypename, t.documentno, t.docstatus, t.m_product_id, p.value as product_value, p.name as product_name, t.qty, t.c_order_id, t.created, t.updated
from (

SELECT t.ad_client_id, t.ad_org_id, t.m_locator_id, 
	t.movementdate, t.m_product_id, io.c_order_id, 
	CASE 
	WHEN t.movementqty > 0 THEN ''Y''::text
	ELSE ''N''::text
	END AS receiptvalue,
	abs(t.movementqty) AS qty, 
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN ''M_InOut'' 
	WHEN ml.m_movementline_id IS NOT NULL THEN ''M_Movement'' 
	WHEN tr.m_transfer_id IS NOT NULL THEN ''M_Transfer''
	WHEN (sp.m_splitting_id IS NOT NULL OR spv.m_splitting_id IS NOT NULL) THEN ''M_Splitting''
	WHEN (pc.m_productchange_id IS NOT NULL OR pcv.m_productchange_id IS NOT NULL) THEN ''M_ProductChange''
	ELSE ''M_Inventory''
	END AS movement_table, 
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN dtio.name
	WHEN ml.m_movementline_id IS NOT NULL THEN dtm.name
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.transfertype
	WHEN (sp.m_splitting_id IS NOT NULL OR spv.m_splitting_id IS NOT NULL) THEN ''M_Splitting_ID''
	WHEN (pc.m_productchange_id IS NOT NULL OR pcv.m_productchange_id IS NOT NULL) THEN ''M_ProductChange_ID''
	ELSE dti.name
	END AS doctypename,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.documentno
	WHEN ml.m_movementline_id IS NOT NULL THEN m.documentno
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.documentno
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.documentno
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.documentno
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.documentno
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.documentno
	ELSE i.documentno
	END AS documentno,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.docstatus
	WHEN ml.m_movementline_id IS NOT NULL THEN m.docstatus
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.docstatus
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.docstatus
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.docstatus
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.docstatus
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.docstatus
	ELSE i.docstatus
	END AS docstatus,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.created 
	WHEN ml.m_movementline_id IS NOT NULL THEN m.created 
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.created
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.created
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.created
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.created 
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.created
	ELSE i.created
	END AS created,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.updated 
	WHEN ml.m_movementline_id IS NOT NULL THEN m.updated 
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.updated
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.updated
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.updated
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.updated
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.updated
	ELSE i.updated
	END AS updated
FROM (SELECT *
	FROM m_transaction
	WHERE ' || productCondition || ') t
LEFT JOIN m_inoutline iol ON iol.m_inoutline_id = t.m_inoutline_id
LEFT JOIN m_inout io ON io.m_inout_id = iol.m_inout_id
LEFT JOIN c_doctype dtio ON dtio.c_doctype_id = io.c_doctype_id

LEFT JOIN m_movementline ml ON ml.m_movementline_id = t.m_movementline_id
LEFT JOIN m_movement m ON m.m_movement_id = ml.m_movement_id
LEFT JOIN c_doctype dtm ON dtm.c_doctype_id = m.c_doctype_id

LEFT JOIN m_inventoryline il ON il.m_inventoryline_id = t.m_inventoryline_id
LEFT JOIN m_inventory i ON i.m_inventory_id = il.m_inventory_id
LEFT JOIN c_doctype dti ON dti.c_doctype_id = i.c_doctype_id

LEFT JOIN m_transfer tr ON tr.m_inventory_id = i.m_inventory_id
LEFT JOIN m_splitting sp ON sp.m_inventory_id = i.m_inventory_id
LEFT JOIN m_splitting spv ON spv.void_inventory_id = i.m_inventory_id
LEFT JOIN m_productchange pc ON pc.m_inventory_id = i.m_inventory_id
LEFT JOIN m_productchange pcv ON pcv.void_inventory_id = i.m_inventory_id

UNION ALL

SELECT i.ad_client_id, i.ad_org_id, il.m_locator_id, 
	i.movementdate, il.m_product_id, NULL::integer AS c_order_id,
	CASE
	WHEN (il.qtycount - il.qtybook) >= 0::numeric THEN ''Y''::text
	ELSE ''N''::text
	END AS receiptvalue,
	abs(il.qtycount - il.qtybook) AS qty,
	''M_Inventory'' AS movement_table, 
	dt.name AS doctypename, i.documentno, i.docstatus, i.created, i.updated
FROM m_inventory i
JOIN m_inventoryline il ON i.m_inventory_id = il.m_inventory_id
JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
WHERE ' || productCondition || ' 
	and NOT EXISTS ( SELECT t.m_transaction_id
			  FROM m_transaction t
			  WHERE il.m_inventoryline_id = t.m_inventoryline_id)
) as t
JOIN m_product p on p.m_product_id = t.m_product_id
JOIN m_locator l ON l.m_locator_id = t.m_locator_id
JOIN m_warehouse w ON w.m_warehouse_id = l.m_warehouse_id; ';

FOR adocument IN EXECUTE consulta LOOP
	return next adocument;
END LOOP;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION v_product_movements_detailed_filtered(integer)
  OWNER TO libertya;

--VIEW v_product_movements_detailed
DROP VIEW v_product_movements_detailed;

CREATE OR REPLACE VIEW v_product_movements_detailed AS 
 select *
 from v_product_movements_detailed_filtered(-1);

ALTER TABLE v_product_movements_detailed
  OWNER TO libertya;
  
--20181204-1305 Nueva columna para permitir que las unidades de medida sean seleccionables para artículos
update ad_system set dummy = (SELECT addcolumnifnotexists('c_uom','productselectable','character(1) NOT NULL DEFAULT ''Y''::bpchar'));

--201812041339 Nueva función para redireccionar un UOM a otro junto con todos sus artículos y lineas de pedido, remito y factura
CREATE OR REPLACE FUNCTION redirect_um(clientID integer, orgID integer, x12de355_from varchar, x12de355_to varchar, postproductselectable boolean)
RETURNS void AS
$BODY$
/**
* Redireccionar desde la UM from a la UM to:
* - Los artículos 
* - Las líneas de remitos 
* - Las líneas de facturas
* - Las líneas de pedidos
* Luego se deja activo o desactivo la UM from dependiendo el parámetro postinactive.
*/
DECLARE
	productID integer;
	uomfromid integer;
	uomtoid integer;
	inoutlines integer;
	invoicelines integer;
	orderlines integer;
	products integer;
	uomactive varchar; 
BEGIN
	-- Obtener el ID del UOM desde en base al símbolo parámetro
	SELECT c_uom_id INTO uomfromid
	FROM c_uom 
	WHERE (ad_client_id = clientID OR ad_client_id = 0) and x12de355 = x12de355_from
	order by ad_client_id desc limit 1;

	IF uomfromid IS NULL OR uomfromid = 0 THEN 
		RAISE NOTICE 'Imposible determinar Unidad de Medida, x12de355 %', x12de355_from;
		RETURN;
	END IF;

	-- Obtener el ID del UOM hasta en base al símbolo parámetro
	SELECT c_uom_id INTO uomtoid 
	FROM c_uom 
	WHERE (ad_client_id = clientID OR ad_client_id = 0) and x12de355 = x12de355_to
	order by ad_client_id desc limit 1;
	
	IF uomtoid IS NULL OR uomtoid = 0 THEN 
		RAISE NOTICE 'Imposible determinar Unidad de Medida, x12de355 %', x12de355_to;
		RETURN;
	END IF;
	
	-- Se modifican las líneas de remitos, facturas y pedidos 
	-- para los artículos de la UOM desde que poseen esa UOM en la línea
	FOR productID IN 
		SELECT m_product_id 
		FROM m_product 
		WHERE ad_client_id = clientID and c_uom_id = uomfromid
		ORDER BY m_product_id
	LOOP
		RAISE NOTICE 'Articulo %', productID;
		
		-- Líneas de remito
		UPDATE m_inoutline il 
		SET c_uom_id = uomtoid
		WHERE m_product_id = productID AND c_uom_id = uomfromid AND ad_org_id = orgID 
			AND EXISTS (SELECT m_inout_id 
					FROM m_inout i 
					WHERE i.m_inout_id = il.m_inout_id and i.docstatus = 'CO');

		GET DIAGNOSTICS inoutlines = ROW_COUNT;
		RAISE NOTICE 'Lineas de remito actualizadas %', inoutlines;
		
		-- Líneas de factura
		UPDATE c_invoiceline il
		SET c_uom_id = uomtoid
		WHERE m_product_id = productID AND c_uom_id = uomfromid AND ad_org_id = orgID 
			AND EXISTS (SELECT c_invoice_id 
					FROM c_invoice i 
					WHERE i.c_invoice_id = il.c_invoice_id and i.docstatus = 'CO');

		GET DIAGNOSTICS invoicelines = ROW_COUNT;
		RAISE NOTICE 'Lineas de factura actualizadas %', invoicelines;
		
		-- Líneas de pedido
		UPDATE c_orderline il 
		SET c_uom_id = uomtoid
		WHERE m_product_id = productID AND c_uom_id = uomfromid AND ad_org_id = orgID 
			AND EXISTS (SELECT c_order_id 
					FROM c_order i 
					WHERE i.c_order_id = il.c_order_id and i.docstatus = 'CO');

		GET DIAGNOSTICS orderlines = ROW_COUNT;
		RAISE NOTICE 'Lineas de pedido actualizadas %', orderlines;
	END LOOP;

	-- Actualizar los artículos
	UPDATE m_product
	SET c_uom_id = uomtoid
	WHERE c_uom_id = uomfromid;

	-- Articulos actualizados
	GET DIAGNOSTICS products = ROW_COUNT;
	RAISE NOTICE 'Articulos actualizados %', products;

	-- Activar/Desactivar UM
	uomactive = 'N';
	IF postproductselectable THEN uomactive = 'Y'; END IF;

	UPDATE c_uom
	SET productselectable = uomactive
	WHERE c_uom_id = uomfromid;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION redirect_um(integer, integer, varchar, varchar, boolean)
  OWNER TO libertya;
  
-- 20181207-1153 Indice para bpartners en allocations
update ad_system set dummy = (SELECT addindexifnotexists('c_allocationhdr_bpartner','c_allocationhdr','c_bpartner_id'));

--20181221-1050 La unicidad de liquidaciones debe incluir la fecha de pago
ALTER TABLE c_creditcardsettlement DROP CONSTRAINT uniquecreditcardsettlement;
ALTER TABLE c_creditcardsettlement ADD CONSTRAINT uniquecreditcardsettlement UNIQUE (settlementno, c_bpartner_id, paymentdate);

-- 20181213-1730 Nueva columna para permitir indicar el dato No A La Orden en exportación de pagos electrónicos Patagonia
update ad_system set dummy = (SELECT addcolumnifnotexists('c_bpartner_banklist','nottoorder','character(1) NOT NULL DEFAULT ''N''::bpchar'));

--20181221-1715 Nueva tabla temporal para el informe de Declaración de Valores para mejorar performance
CREATE TABLE t_pos_declaracionvalores ( 
  t_pos_declaracionvalores_id integer NOT NULL,
  ad_pinstance_id integer NOT NULL,
  ad_client_id integer NOT NULL,
  ad_org_id integer NOT NULL,
  isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
  created timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  createdby integer NOT NULL,
  updated timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  updatedby integer NOT NULL,
  c_posjournal_id integer, 
  ad_user_id integer, 
  c_currency_id integer, 
  datetrx date, 
  docstatus character(2), 
  category varchar, 
  tendertype character(3), 
  description text, 
  c_charge_id integer, 
  chargename character varying(60), 
  doc_id integer, 
  ingreso numeric(22,2), 
  egreso numeric(22,2), 
  c_invoice_id integer, 
  invoice_documentno character varying(30), 
  invoice_grandtotal numeric(22,2), 
  entidadfinanciera_value varchar, 
  entidadfinanciera_name varchar, 
  bp_entidadfinanciera_value varchar, 
  bp_entidadfinanciera_name varchar, 
  cupon varchar, 
  creditcard varchar, 
  generated_invoice_documentno varchar, 
  allocation_active character(1), 
  c_pos_id integer, 
  posname character varying(60),
  CONSTRAINT t_pos_declaracionvalores_key PRIMARY KEY (t_pos_declaracionvalores_id)
 );
ALTER TABLE t_pos_declaracionvalores 
  OWNER TO libertya;
  
--20190114-1720 Nuevas funciones de obtención y actualización de cantidad por recibir
CREATE OR REPLACE FUNCTION getqtyordered(
    clientid integer,
    orgid integer,
    locatorid integer,
    productid integer,
    dateto date)
  RETURNS numeric AS
$BODY$
/***********
Obtiene la cantidad a recibir a fecha de corte. Si no hay fecha de corte, entonces se devuelven los pedidos actuales.
Por lo pronto no se utiliza el pendiente a fecha de corte ya que primero deberíamos analizar e implementar 
una forma en la que se determine cuando un pedido fue completo, anulado, etc.
*/
DECLARE
reserved numeric;
BEGIN
reserved := 0;

SELECT INTO reserved coalesce(sum(ol.qtyreserved),0)
from c_orderline ol
inner join c_order o on o.c_order_id = ol.c_order_id
inner join c_doctype dto on dto.c_doctype_id = o.c_doctypetarget_id
inner join m_warehouse w on w.m_warehouse_id = o.m_warehouse_id
inner join m_locator l on l.m_warehouse_id = w.m_warehouse_id
where o.ad_client_id = clientid
and o.ad_org_id = orgid 
and ol.qtyreserved > 0
and o.docstatus in ('CO','CL')
and ol.m_product_id = productid
and l.m_locator_id = locatorid
and (dateto is null or o.dateordered::date <= dateto::date)
and o.issotrx = 'N';

return reserved;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getqtyordered(integer, integer, integer, integer, date)
  OWNER TO libertya;

CREATE OR REPLACE FUNCTION update_ordered(
    clientid integer,
    orgid integer,
    productid integer,
    dateto date)
  RETURNS void AS
$BODY$
/***********
Actualiza la cantidad a recibir de los depósitos de la compañía, organización y artículo parametro, 
siempre y cuando existan los regitros en m_storage 
y sólo sobre locators marcados como default ya que asi se realiza al procesar pedidos.
Las cantidades pedidas se obtienen de pedidos procesados. 
IMPORTANTE: No funciona para artículos que no son ITEMS (Stockeables)
*/
BEGIN
	--Seteamos a 0 todo
	update m_storage s
	set qtyordered = 0
	where ad_client_id = clientid
		and (orgid = 0 or ad_org_id = orgid)
		and (productid = 0 or m_product_id = productid);
		
	--Actualizamos el reservado
	update m_storage s
	set qtyordered = getqtyordered(clientid, s.ad_org_id, s.m_locator_id, s.m_product_id, dateto)
	where ad_client_id = clientid
		and (orgid = 0 or ad_org_id = orgid)
		and (productid = 0 or m_product_id = productid)
		and exists (select ol.c_orderline_id
				from c_orderline ol
				join m_warehouse w on w.m_warehouse_id = ol.m_warehouse_id
				join m_locator l on l.m_warehouse_id = w.m_warehouse_id
				where l.m_locator_id = s.m_locator_id 
					and ol.m_product_id = s.m_product_id 
					and ol.qtyreserved > 0);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_ordered(integer, integer, integer, date)
  OWNER TO libertya;
  
--20190204-1820 Incorporación de nuevas columnas para registro de datos para importación de novedades
update ad_system set dummy = (SELECT addcolumnifnotexists('c_payment','banklist_registerno','character varying(60)'));
update ad_system set dummy = (SELECT addcolumnifnotexists('c_payment','bank_payment_msg_description','character varying(255)'));
update ad_system set dummy = (SELECT addcolumnifnotexists('i_paymentbanknews','payment_amount','numeric(20,2)'));
update ad_system set dummy = (SELECT addcolumnifnotexists('i_paymentbanknews','payment_status_msg_description','character varying(255)'));

--20190322-1303 Nueva columna para referenciar el ticket originante del cupon (o codigo) promocional
update ad_system set dummy = (SELECT addcolumnifnotexists('C_Promotion_Code','C_Invoice_Orig_ID','integer'));

--20190401-1645 Nueva tabla para registrar la información de los cierres fiscales Z y X
CREATE TABLE c_controlador_fiscal_closing_info
(
  c_controlador_fiscal_closing_info_id integer NOT NULL,
  ad_client_id integer NOT NULL,
  ad_org_id integer NOT NULL,
  isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
  created timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  createdby integer NOT NULL,
  updated timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  updatedby integer NOT NULL,
  c_controlador_fiscal_id integer NOT NULL,
  puntodeventa integer,
  fiscalclosingtype character(1) NOT NULL,
  fiscalclosingno integer NOT NULL DEFAULT 0,
  fiscalclosingdate timestamp without time zone NOT NULL,
  qtycanceledfiscaldocument integer NOT NULL DEFAULT 0,
  qtycanceledcreditnote integer NOT NULL DEFAULT 0,
  qtynofiscalhomologated integer NOT NULL DEFAULT 0,
  qtynofiscaldocument integer NOT NULL DEFAULT 0,
  qtyfiscaldocument integer NOT NULL DEFAULT 0,
  qtyfiscaldocumentbc integer NOT NULL DEFAULT 0,
  qtyfiscaldocumenta integer NOT NULL DEFAULT 0,
  qtycreditnote integer NOT NULL DEFAULT 0,
  qtycreditnotebc integer NOT NULL DEFAULT 0,
  qtycreditnotea integer NOT NULL DEFAULT 0,
  fiscaldocument_bc_lastemitted integer NOT NULL DEFAULT 0,
  fiscaldocument_a_lastemitted integer NOT NULL DEFAULT 0,
  fiscaldocumentamt numeric(22,4) NOT NULL DEFAULT 0,
  fiscaldocumentgravadoamt numeric(22,4) NOT NULL DEFAULT 0,
  fiscaldocumentnogravadoamt numeric(22,4) NOT NULL DEFAULT 0,
  fiscaldocumentexemptamt numeric(22,4) NOT NULL DEFAULT 0,
  fiscaldocumenttaxamt numeric(22,4) NOT NULL DEFAULT 0,
  fiscaldocumentinternaltaxamt numeric(22,4) NOT NULL DEFAULT 0,
  fiscaldocumentperceptionamt numeric(22,4) NOT NULL DEFAULT 0,
  fiscaldocumentnotregisteredtaxamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnote_bc_lastemitted integer NOT NULL DEFAULT 0,
  creditnote_a_lastemitted integer NOT NULL DEFAULT 0,
  creditnoteamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnotegravadoamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnotenogravadoamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnoteexemptamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnotetaxamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnoteinternaltaxamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnoteperceptionamt numeric(22,4) NOT NULL DEFAULT 0,
  creditnotenotregisteredtaxamt numeric(22,4) NOT NULL DEFAULT 0,
  nofiscalhomologatedamt numeric(22,4) NOT NULL DEFAULT 0,
  CONSTRAINT c_controlador_fiscal_closing_info_key PRIMARY KEY (c_controlador_fiscal_closing_info_id),
  CONSTRAINT adclient_c_controlador_fiscal_closing_info FOREIGN KEY (ad_client_id)
      REFERENCES ad_client (ad_client_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT adorg_c_controlador_fiscal_closing_info FOREIGN KEY (ad_org_id)
      REFERENCES ad_org (ad_org_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE c_controlador_fiscal_closing_info
  OWNER TO libertya;
  
--20190404-1425 Nueva view de retenciones
CREATE OR REPLACE VIEW c_invoice_retenciones_v AS 
SELECT distinct i.ad_client_id, i.ad_org_id, i.c_invoice_id, i.documentno, date_trunc('day'::text, i.dateinvoiced) AS dateinvoiced, 
	date_trunc('day'::text, i.dateacct) AS dateacct, date_trunc('day'::text, i.dateinvoiced) AS date, 
	lc.letra, i.puntodeventa, i.numerocomprobante, translate(i.documentno::text, lc.letra::text, ''::text)::character varying(30) AS documentno_without_letter, 
	i.grandtotal, i.netamount, bp.c_bpartner_id, bp.value AS bpartner_value, bp.name AS bpartner_name, 
	replace(bp.taxid::text, '-'::text, ''::text) AS taxid, bp.iibb, bp.isconveniomultilateral,
	((("substring"(replace(bp.taxid::text, '-'::text, ''::text), 1, 2) || '-'::text) 
		|| "substring"(replace(bp.taxid::text, '-'::text, ''::text), 3, 8)) || '-'::text) 
		|| "substring"(replace(bp.taxid::text, '-'::text, ''::text), 11, 1) AS taxid_with_script,
	r.jurisdictioncode,
	ri.c_retencionschema_id,
	ri.amt_retenc,
	ri.pagos_ant_acumulados_amt,
	ri.retenciones_ant_acumuladas_amt,
	ri.pago_actual_amt, 
	ri.retencion_percent,
	ri.importe_no_imponible_amt,
	ri.baseimponible_amt,
	ri.importe_determinado_amt
FROM c_allocationhdr ah
JOIN m_retencion_invoice ri ON ah.c_allocationhdr_id = ri.c_allocationhdr_id
JOIN c_invoice i ON i.c_invoice_id = ri.c_invoice_id
JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
JOIN c_retencionschema rs ON rs.c_retencionschema_id = ri.c_retencionschema_id
LEFT JOIN c_letra_comprobante lc ON lc.c_letra_comprobante_id = i.c_letra_comprobante_id
LEFT JOIN c_region r ON r.c_region_id = rs.c_region_id
WHERE rs.retencionapplication = 'E' and i.docstatus in ('CO','CL');

ALTER TABLE c_invoice_retenciones_v
  OWNER TO libertya;
  
--20190408-1016 Fix de tamaño de columna product_name en funview v_product_movements_detailed_filtered 
DROP VIEW v_product_movements_detailed;
DROP FUNCTION v_product_movements_detailed_filtered(productID integer);
DROP TYPE v_product_movements_detailed_type ;
CREATE TYPE v_product_movements_detailed_type AS (movement_table text, ad_client_id integer, ad_org_id integer, 
		m_locator_id integer, m_warehouse_id integer, warehouse_value varchar(40), warehouse_name varchar(60), 
		receiptvalue text, movementdate timestamp, doctypename varchar(60), documentno varchar(60), 
		docstatus character(2), m_product_id integer, product_value varchar(40), product_name varchar(255), 
		qty numeric(22,4), c_order_id integer, created timestamp, updated timestamp);

CREATE OR REPLACE FUNCTION v_product_movements_detailed_filtered(productID integer)
  RETURNS SETOF v_product_movements_detailed_type AS
$BODY$
declare
	consulta varchar;
	productCondition varchar;
	adocument v_product_movements_detailed_type;
BEGIN
	-- Armar la condición por el artículo
	productCondition = '(' || $1 || ' <= 0 or m_product_id = ' || $1 || ')';
	-- Armar la consulta
	consulta = ' SELECT t.movement_table, t.ad_client_id, t.ad_org_id, t.m_locator_id, w.m_warehouse_id, w.value AS warehouse_value, w.name AS warehouse_name, t.receiptvalue, t.movementdate, t.doctypename, t.documentno, t.docstatus, t.m_product_id, p.value as product_value, p.name as product_name, t.qty, t.c_order_id, t.created, t.updated
from (

SELECT t.ad_client_id, t.ad_org_id, t.m_locator_id, 
	t.movementdate, t.m_product_id, io.c_order_id, 
	CASE 
	WHEN t.movementqty > 0 THEN ''Y''::text
	ELSE ''N''::text
	END AS receiptvalue,
	abs(t.movementqty) AS qty, 
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN ''M_InOut'' 
	WHEN ml.m_movementline_id IS NOT NULL THEN ''M_Movement'' 
	WHEN tr.m_transfer_id IS NOT NULL THEN ''M_Transfer''
	WHEN (sp.m_splitting_id IS NOT NULL OR spv.m_splitting_id IS NOT NULL) THEN ''M_Splitting''
	WHEN (pc.m_productchange_id IS NOT NULL OR pcv.m_productchange_id IS NOT NULL) THEN ''M_ProductChange''
	ELSE ''M_Inventory''
	END AS movement_table, 
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN dtio.name
	WHEN ml.m_movementline_id IS NOT NULL THEN dtm.name
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.transfertype
	WHEN (sp.m_splitting_id IS NOT NULL OR spv.m_splitting_id IS NOT NULL) THEN ''M_Splitting_ID''
	WHEN (pc.m_productchange_id IS NOT NULL OR pcv.m_productchange_id IS NOT NULL) THEN ''M_ProductChange_ID''
	ELSE dti.name
	END AS doctypename,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.documentno
	WHEN ml.m_movementline_id IS NOT NULL THEN m.documentno
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.documentno
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.documentno
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.documentno
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.documentno
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.documentno
	ELSE i.documentno
	END AS documentno,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.docstatus
	WHEN ml.m_movementline_id IS NOT NULL THEN m.docstatus
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.docstatus
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.docstatus
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.docstatus
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.docstatus
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.docstatus
	ELSE i.docstatus
	END AS docstatus,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.created 
	WHEN ml.m_movementline_id IS NOT NULL THEN m.created 
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.created
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.created
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.created
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.created 
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.created
	ELSE i.created
	END AS created,
	CASE 
	WHEN iol.m_inoutline_id IS NOT NULL THEN io.updated 
	WHEN ml.m_movementline_id IS NOT NULL THEN m.updated 
	WHEN tr.m_transfer_id IS NOT NULL THEN tr.updated
	WHEN sp.m_splitting_id IS NOT NULL THEN sp.updated
	WHEN spv.m_splitting_id IS NOT NULL THEN spv.updated
	WHEN pc.m_productchange_id IS NOT NULL THEN pc.updated
	WHEN pcv.m_productchange_id IS NOT NULL THEN pcv.updated
	ELSE i.updated
	END AS updated
FROM (SELECT *
	FROM m_transaction
	WHERE ' || productCondition || ') t
LEFT JOIN m_inoutline iol ON iol.m_inoutline_id = t.m_inoutline_id
LEFT JOIN m_inout io ON io.m_inout_id = iol.m_inout_id
LEFT JOIN c_doctype dtio ON dtio.c_doctype_id = io.c_doctype_id

LEFT JOIN m_movementline ml ON ml.m_movementline_id = t.m_movementline_id
LEFT JOIN m_movement m ON m.m_movement_id = ml.m_movement_id
LEFT JOIN c_doctype dtm ON dtm.c_doctype_id = m.c_doctype_id

LEFT JOIN m_inventoryline il ON il.m_inventoryline_id = t.m_inventoryline_id
LEFT JOIN m_inventory i ON i.m_inventory_id = il.m_inventory_id
LEFT JOIN c_doctype dti ON dti.c_doctype_id = i.c_doctype_id

LEFT JOIN m_transfer tr ON tr.m_inventory_id = i.m_inventory_id
LEFT JOIN m_splitting sp ON sp.m_inventory_id = i.m_inventory_id
LEFT JOIN m_splitting spv ON spv.void_inventory_id = i.m_inventory_id
LEFT JOIN m_productchange pc ON pc.m_inventory_id = i.m_inventory_id
LEFT JOIN m_productchange pcv ON pcv.void_inventory_id = i.m_inventory_id

UNION ALL

SELECT i.ad_client_id, i.ad_org_id, il.m_locator_id, 
	i.movementdate, il.m_product_id, NULL::integer AS c_order_id,
	CASE
	WHEN (il.qtycount - il.qtybook) >= 0::numeric THEN ''Y''::text
	ELSE ''N''::text
	END AS receiptvalue,
	abs(il.qtycount - il.qtybook) AS qty,
	''M_Inventory'' AS movement_table, 
	dt.name AS doctypename, i.documentno, i.docstatus, i.created, i.updated
FROM m_inventory i
JOIN m_inventoryline il ON i.m_inventory_id = il.m_inventory_id
JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
WHERE ' || productCondition || ' 
	and NOT EXISTS ( SELECT t.m_transaction_id
			  FROM m_transaction t
			  WHERE il.m_inventoryline_id = t.m_inventoryline_id)
) as t
JOIN m_product p on p.m_product_id = t.m_product_id
JOIN m_locator l ON l.m_locator_id = t.m_locator_id
JOIN m_warehouse w ON w.m_warehouse_id = l.m_warehouse_id; ';

FOR adocument IN EXECUTE consulta LOOP
	return next adocument;
END LOOP;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION v_product_movements_detailed_filtered(integer)
  OWNER TO libertya;

CREATE OR REPLACE VIEW v_product_movements_detailed AS 
 select *
 from v_product_movements_detailed_filtered(-1);

ALTER TABLE v_product_movements_detailed
  OWNER TO libertya;  
  
--20190415-1414 Cambio en trigger de replicacion. Se adecua la logica de replicacion para no marcar un registro creado e inmediatamente modificado (el cual esta bajo estado '1' (insercion)) como  'a' (re-enviar luego de ack)  
CREATE OR REPLACE FUNCTION replication_event()
  RETURNS trigger AS
$BODY$
DECLARE 
	found integer; 
	replicationPos integer;
	v_newRepArray varchar; 
	aKeyColumn varchar;
	repSeq bigint;
	shouldReplicate varchar;
	recordColumns RECORD;
	columnValue varchar;
	isValid integer;
	v_valueDetail varchar;
	v_nameDetail varchar;
	v_columnname varchar;
	v_tableid int; 
	v_tablename varchar;
	v_referenceStr varchar;
	v_referencedTableStr varchar;
	v_referencedTableID int;
	v_existsValueField int;
	v_existsNameField int;
	shouldcheckreferences boolean;
	checkreferencestableconf character;
BEGIN 
	-- se deberan verificar referencias a registros fuera del esquema de replicacion? (inicialmente no)
	shouldcheckreferences := false;

	-- estamos en una accion de eliminacion?
	IF (TG_OP = 'DELETE') THEN

		-- Checkear switch maestro de replicacion
		SELECT INTO shouldReplicate VALUE FROM AD_PREFERENCE WHERE ATTRIBUTE = 'ReplicationEventsActive';
		IF (shouldReplicate <> 'Y') THEN
			RETURN OLD;
		END IF;

		-- Se repArray es nulo o vacio, no hay mas que hacer dado que es un registro fuera de replicacion
		IF (OLD.repArray IS NULL OR OLD.repArray = '') THEN
			RETURN OLD;
		END IF;

		-- El registro fue replicado? Si no lo fue puede ser eliminado, pero en caso contrario hay que registrar su eliminacion
		IF replication_is_record_replicated(OLD.repArray) = 1 THEN

			-- Recuperar el repArray de la tabla en cuestion
			SELECT INTO v_newRepArray replicationArray 
			FROM ad_tablereplication 
			WHERE ad_table_ID = TG_ARGV[0]::int;

			-- Cambiar 3 (replicacion bidireccional) por 1 (enviar); y 2 (recibir) por 0 (sin accion)
			v_newRepArray := replace(v_newRepArray, '3', '1');
			v_newRepArray := replace(v_newRepArray, '2', '0');

			-- Insertar en la tabla de eliminaciones
			IF v_newRepArray IS NOT NULL AND v_newRepArray <> '' THEN
				INSERT INTO ad_changelog_replication (AD_Changelog_Replication_ID, AD_Client_ID, AD_Org_ID, isActive, Created, CreatedBy, Updated, UpdatedBy, AD_Table_ID, retrieveUID, operationtype, binaryvalue, reparray, columnvalues, includeInReplication)
				SELECT nextval('seq_ad_changelog_replication'),OLD.AD_Client_ID,OLD.AD_Org_ID,'Y',now(),OLD.CreatedBy,now(),OLD.UpdatedBy,TG_ARGV[0]::int,OLD.retrieveUID,'I',null,v_newRepArray,null,'Y';
			END IF;
		END IF;	
		
		RETURN OLD;
	END IF;
	-- estamos en una accion de insercion o actualizacion?
	IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN

		-- Checkear switch maestro de replicacion		
		SELECT INTO shouldReplicate VALUE FROM AD_PREFERENCE WHERE ATTRIBUTE = 'ReplicationEventsActive';
		IF (shouldReplicate <> 'Y') THEN
			RETURN NEW;
		END IF;

		-- El uso de SKIP se supone para omitir acciones posteriores en eliminacion (dado que setea repArray en NULL)
		IF (NEW.repArray = 'SKIP') THEN
			NEW.repArray := NULL;
			return NEW;
		END IF;

		-- Verificar si hay que generar el retrieveUID. Ejemplo: h1_291
		IF NEW.retrieveUID IS NULL OR NEW.retrieveUID = '' THEN 
			-- Primeramente intentar utilizar el AD_ComponentObjectUID (registro perteneciente a un componente)
			BEGIN
				IF NEW.AD_ComponentObjectUID IS NOT NULL AND NEW.AD_ComponentObjectUID <> '' THEN
					NEW.retrieveUID = NEW.AD_ComponentObjectUID;
				END IF;
			EXCEPTION
				WHEN OTHERS THEN
					-- Do nothing
			END;
			-- Si el registro no pertenece a un componente, generar el retrieveUID
		        IF NEW.retrieveUID IS NULL OR NEW.retrieveUID = '' THEN 
				-- Obtener posicion de host
				SELECT INTO replicationPos replicationArrayPos FROM AD_ReplicationHost WHERE thisHost = 'Y'; 
				IF replicationPos IS NULL THEN RAISE EXCEPTION 'Configuracion de Hosts incompleta: Ninguna sucursal tiene marca de Este Host'; END IF; 
				-- Obtener siguiente valor para la tabla dada
				SELECT INTO repseq nextVal('repseq_' || TG_ARGV[1]);
				IF repseq IS NULL THEN RAISE EXCEPTION 'No hay definida una secuencia de replicacion para la tabla %', TG_ARGV[1]; END IF;
					NEW.retrieveUID := 'h'::varchar || replicationPos::varchar || '_' || repseq || '_' || lower(TG_ARGV[1]);
				END IF;		
			END IF;

		-- Si estamos insertando...
		IF (TG_OP = 'INSERT') THEN

			-- Si se indico el repArray con SET, entonces se esta configurando el registro manualmente.  No hacer nada mas.
			IF (substr(NEW.repArray, 1, 3) = 'SET') THEN
				NEW.repArray := substr(NEW.repArray, 4, length(NEW.repArray)-3);
			ELSE
				-- Recuperar el repArray
				SELECT INTO v_newRepArray replicationArray 
				FROM ad_tablereplication 
				WHERE ad_table_ID = TG_ARGV[0]::int;

				-- Si es nulo o vacio no hacer nada mas
				IF v_newRepArray IS NULL OR v_newRepArray = '' THEN
					RETURN NEW;
				END IF;

				-- Cambiar 3 (replicacion bidireccional) por 1 (enviar); y 2 (recibir) por 0 (sin accion)
				NEW.repArray := replace(v_newRepArray, '3', '1');
				NEW.repArray := replace(NEW.repArray, '2', '0');
				-- Si el registro deberá replicar hacia otros hosts (hay al menos un 1)
				-- entonces debe incluirse en replicacion y hay que check referencias
				IF (position('1' in NEW.repArray) > 0) THEN
					NEW.includeInReplication = 'Y';
					shouldcheckreferences := true;
				ELSE
					NEW.repArray := NULL;
				END IF;
			END IF;
			
		-- Si estamos actualizando...
		ELSEIF (TG_OP = 'UPDATE') THEN 

			-- Si se indico el repArray con SET, entonces se esta configurando el registro manualmente.  No hacer nada mas.		
			IF (substr(NEW.repArray, 1, 3) = 'SET') THEN
				NEW.repArray := substr(NEW.repArray, 4, length(NEW.repArray)-3);
			ELSE

				-- Recuperar el repArray
				SELECT INTO v_newRepArray replicationArray 
				FROM ad_tablereplication 
				WHERE ad_table_ID = TG_ARGV[0]::int;

				-- El repArray no esta seteado todavia? (Caso: modificacion de un registro preexistente)
				IF (OLD.repArray IS NULL OR OLD.repArray = '0') THEN

					-- Cambiar 2 (recibir) por 0 (sin accion)
					NEW.repArray := replace(v_newRepArray, '2', '0');
					-- Cambiar 1 (enviar) y 3 (bidireccional) por 2 (replicado)
					NEW.repArray := replace(NEW.repArray, '1', '2');
					NEW.repArray := replace(NEW.repArray, '3', '2');
				ELSE
					-- El repArray ya fue seteado anteriormente, hay que verificar si la configuracion de repArray fue ampliada
					-- El v_newRepArray tiene una longitud MAYOR que el reparray existente actualmente? Completar con la configuracion
					IF (length(v_newRepArray) > length(NEW.repArray)) THEN
						NEW.repArray := rpad(NEW.repArray, length(v_newRepArray), substr(replace(replace(v_newRepArray, '3', '1'), '2', '0'), length(NEW.repArray)+1));
					END IF;
				END IF;

				-- Cambiar los 2 (replicado) por 3 (modificado).
				-- Adicionalmente para JMS: 4 (espera ack) por 5 (cambios luego de ack)
				NEW.repArray := replace(NEW.repArray, '2', '3');
				NEW.repArray := replace(NEW.repArray, '4', '5');
				-- Ademas: Cambiar 1 (replicar) por a (re-replicar luego de insertar o modificar segun existencia en destino).  
				--	   Puede darse el caso que se realiza una modificacion a un registro replicado en ciertos hosts pero no en otros.
				--	   Este cambio debería garantizar el reenvio del registro en caso de que un ack omita sin querer la modificacion.
				--	   Esta modificacion se realizara unicamente si el intervalo de tiempo entre un cambio y otro es mayor a un intervalo 
				--         de tiempo razonable (considerando que los registros creados recientemente no son tenidos en cuenta para replicacion
				--	   (o si la tabla no contiene las columnas de creacion/actualizacion)
				BEGIN
					IF age(now(), OLD.created) > '1 minute' THEN
						NEW.repArray := replace(NEW.repArray, '1', 'a');
					END IF;
				EXCEPTION
					WHEN OTHERS THEN
						NEW.repArray := replace(NEW.repArray, '1', 'a');
				END;
			
				-- Si el registro deberá replicar hacia otros hosts (hay al menos un 3 o una A/a)
				-- entonces debe incluirse en replicacion y hay que check referencias
				IF (position('3' in NEW.repArray) > 0 OR position('A' in NEW.repArray) > 0 OR position('a' in NEW.repArray) > 0) THEN
					NEW.includeInReplication = 'Y';
					shouldcheckreferences := true;
				END IF;
			END IF;
		END IF;
	END IF;

	IF (shouldcheckreferences = true) THEN

		-- Verificar si la tabla tiene configurado que hay que chequear referencias
		SELECT into checkreferencestableconf CheckReferences FROM ad_tablereplication WHERE AD_table_id = TG_ARGV[0]::int;
		IF (checkreferencestableconf = 'N') THEN
			return NEW;
		END IF;

		-- Validar referencias iterando todas las columnas de la tabla
		FOR recordColumns IN
			SELECT isc.column_name, isc.data_type, c.ad_column_id, t.tablename, t.ad_table_id
			FROM information_schema.columns isc
			INNER JOIN ad_table t ON lower(isc.table_name) = lower(t.tablename)
			INNER JOIN ad_column c ON lower(isc.column_name) = lower(c.columnname) AND t.ad_table_id = c.ad_table_id
			WHERE table_name = quote_ident(TG_TABLE_NAME)
			AND isc.data_type = 'integer'
			AND isc.column_name not in ('retrieveuid', 'reparray', 'datelastsentjms', 'includeinreplication')
		LOOP
			-- Obtener el value de la columna y verificar si es referencia valida.  En caso de no serlo, presentar error correspondiente
			EXECUTE 'SELECT (' || quote_literal(NEW) || '::' || TG_RELID::regclass || ').' || quote_ident(recordColumns.column_name) INTO columnValue;
			SELECT INTO isValid replication_is_valid_reference(recordColumns.ad_column_id, columnvalue);
			IF isValid = 0 THEN

				-- valores por defecto
				v_valueDetail := '';
				v_nameDetail := '';
				v_referenceStr := '';
				
				-- recuperar el nombre de la columna y tabla para brindar un mensaje mas intuitivo al usuario
				v_columnname := recordColumns.column_name;
				v_tablename := recordColumns.tablename;

				BEGIN 
					-- recuperar el nombre o value del registro referenciado a fin de mejorar la legibilidad del mensaje de error
					SELECT INTO v_referencedTableStr replication_get_referenced_table(recordColumns.ad_column_id);
					SELECT INTO v_referencedTableID AD_Table_ID FROM AD_Table WHERE tablename ilike v_referencedTableStr;

					-- ver si existe las columnas value y name
					SELECT INTO v_existsValueField Count(1) FROM AD_Column WHERE columnname ilike 'Value' AND AD_Table_ID = v_referencedTableID;
					SELECT INTO v_existsNameField Count(1) FROM AD_Column WHERE columnname ilike 'Name' AND AD_Table_ID = v_referencedTableID;

					-- cargar value y name
					IF v_existsValueField = 1 THEN
						EXECUTE 'SELECT value FROM ' || v_referencedTableStr || ' WHERE ' || v_referencedTableStr || '_ID = ' || columnvalue || '::int' INTO v_valueDetail;
						v_referenceStr := v_valueDetail;
					END IF;
					IF v_existsNameField = 1 THEN
						EXECUTE 'SELECT name FROM ' || v_referencedTableStr || ' WHERE ' || v_referencedTableStr || '_ID = ' || columnvalue || '::int' INTO v_nameDetail;
						v_referenceStr := v_referenceStr || ' ' || v_nameDetail;
					END IF;
				EXCEPTION
					WHEN OTHERS THEN
						-- do nothing
				END;
				
				-- concatenar acordemente para mensaje de retorno
				RAISE EXCEPTION 'Validacion de replicación - La columna: % (%) de la tabla: % (%) referencia al registro: % (%), fuera del sistema de replicacion.', v_columnname, recordColumns.ad_column_id, v_tablename, recordColumns.ad_table_id, v_referenceStr, columnvalue;
			END IF;
		END LOOP;

	END IF;
	RETURN NEW;
END; 
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
ALTER FUNCTION replication_event() OWNER TO libertya;

--20190502-1030 Incorporación de nuevas columnas para registro de datos para importación de novedades
update ad_system set dummy = (SELECT addcolumnifnotexists('i_gljournal','importonlyjournal','character(1)'));

--20190502-1030 Nueva configuración de proveedor para transferencias electrónicas
update ad_system set dummy = (SELECT addcolumnifnotexists('C_BPartner_BankList','cbu','character varying(30)'));
update ad_system set dummy = (SELECT addcolumnifnotexists('C_BPartner_BankList','transferbankaccounttype','character(2)'));
update ad_system set dummy = (SELECT addcolumnifnotexists('C_BPartner_BankList','transferconcept','character(3)'));

--20190502-1030 Incorporación del dato del cobro que genera el descuento/recargo en el TPV
update ad_system set dummy = (SELECT addcolumnifnotexists('c_documentdiscount','c_payment_id','integer'));

--20190509-0945 Fecha de última exportación en los formatos de exportación
update ad_system set dummy = (SELECT addcolumnifnotexists('ad_expformat','lastexporteddate','timestamp without time zone'));

--20190509-1020 FKs de los asientos generados desde importaciones no deben ser bloqueantes ante la eliminación del asiento importado 
ALTER TABLE i_gljournal DROP CONSTRAINT gljourbelline_igljournal;
ALTER TABLE i_gljournal DROP CONSTRAINT gljournal_igljournal;
ALTER TABLE i_gljournal DROP CONSTRAINT gljournalbatch_igljournal;

ALTER TABLE i_gljournal ADD CONSTRAINT gljourbelline_igljournal FOREIGN KEY (gl_journalline_id)
      REFERENCES gl_journalline (gl_journalline_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE i_gljournal ADD CONSTRAINT gljournal_igljournal FOREIGN KEY (gl_journal_id)
      REFERENCES gl_journal (gl_journal_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE i_gljournal ADD CONSTRAINT gljournalbatch_igljournal FOREIGN KEY (gl_journalbatch_id)
      REFERENCES gl_journalbatch (gl_journalbatch_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
--20190510-1130 Columnas de tipo TableDir que deberian ser Busqueda dado que referencian tablas potencialmente voluminosas
update ad_column set ad_reference_id = 30, updated=now() where ad_componentobjectuid = 'CORE-AD_Column-1014641';
update ad_column set ad_reference_id = 30, updated=now() where ad_componentobjectuid = 'CORE-AD_Column-1014838';

-- 20190514-110000.  Estado de la sincronizacion.  P: Pending - S: Sincronizado	- E: Error
alter table c_promotion_code add column suitesyncstatus character(1) not null default 'P';

--20190603-1350 Cuentas contables ajustables por índice de inflación
update ad_system set dummy = (SELECT addcolumnifnotexists('c_elementvalue','isadjustable','character(1) NOT NULL DEFAULT ''N''::bpchar'));

--20190603-1350 Tabla para el registro de índices de inflación
CREATE TABLE c_inflation_index
(
  c_inflation_index_id integer NOT NULL,
  ad_client_id integer NOT NULL,
  ad_org_id integer NOT NULL,
  isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
  created timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  createdby integer NOT NULL,
  updated timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  updatedby integer NOT NULL,
  c_period_id integer NOT NULL,
  description character varying(255),
  inflationindex numeric(8,4) NOT NULL DEFAULT 0,
  CONSTRAINT c_inflation_index_key PRIMARY KEY (c_inflation_index_id),
  CONSTRAINT adclient_inflation_index FOREIGN KEY (ad_client_id)
      REFERENCES ad_client (ad_client_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT adorg_inflation_index FOREIGN KEY (ad_org_id)
      REFERENCES ad_org (ad_org_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT period_inflation_index FOREIGN KEY (c_period_id)
      REFERENCES c_period (c_period_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=TRUE
);
ALTER TABLE c_inflation_index
  OWNER TO libertya;

--20190603-1350 Nuevas columnas para extensión de informe de balance contable para aplicar índices de inflación
update ad_system set dummy = (SELECT addcolumnifnotexists('t_acct_balance','isadjustable','character(1) NOT NULL DEFAULT ''N''::bpchar'));
update ad_system set dummy = (SELECT addcolumnifnotexists('t_acct_balance','balanceadjusted','numeric(12,2) DEFAULT 0'));
update ad_system set dummy = (SELECT addcolumnifnotexists('t_acct_balance','ApplyInflationIndex','character(1) NOT NULL DEFAULT ''N''::bpchar'));

--20190603-1616 Nueva columna en boletas de depósito para registrar cargos contables y trasladarlos al pago generado
update ad_system set dummy = (SELECT addcolumnifnotexists('m_boletadeposito','accounting_c_charge_id','integer'));

--20190605-1655 Fixes a la exportación CITI Ventas y Compras
DROP VIEW reginfo_compras_cbte_v;
DROP VIEW reginfo_compras_alicuotas_v;
DROP VIEW reginfo_ventas_alicuotas_v;
DROP VIEW reginfo_ventas_cbte_v;
DROP FUNCTION getcantidadalicuotasiva(integer);
DROP FUNCTION getcodigooperacion(integer);

CREATE OR REPLACE FUNCTION getcantidadalicuotasiva(p_c_invoice_id integer)
  RETURNS numeric AS
$BODY$ DECLARE     	v_Cant        	NUMERIC; BEGIN     SELECT COUNT(*)     INTO v_Cant     FROM C_Invoicetax it     INNER JOIN C_Tax t ON (t.C_Tax_ID = it.C_Tax_ID)     WHERE (C_Invoice_ID = p_c_invoice_id) AND (isPercepcion = 'N') AND ((t.rate > 0::numeric AND it.taxamt > 0::numeric) OR (t.rate = 0::numeric AND it.taxamt = 0::numeric));     RETURN v_Cant; END; $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getcantidadalicuotasiva(integer)
  OWNER TO libertya;

CREATE OR REPLACE FUNCTION getcodigooperacion(p_c_invoice_id integer)
  RETURNS character AS
$BODY$   DECLARE     	v_CodigoOperacion        	character(1);   BEGIN       

   SELECT CASE WHEN (COUNT(*) >= 1) THEN t.codigooperacion ELSE NULL END     INTO v_CodigoOperacion       FROM (SELECT CASE WHEN it.taxamt = 0 AND t.rate <> 0 AND te.c_tax_id > 0 THEN te.codigooperacion ELSE t.codigooperacion END as codigooperacion 	 FROM C_Invoicetax it      	 INNER JOIN C_Tax t ON (t.C_Tax_ID = it.C_Tax_ID)  	 LEFT JOIN (select * from c_tax where rate = 0 and isactive = 'Y' AND ispercepcion = 'N') as te on te.ad_client_id = it.ad_client_id     	 WHERE (C_Invoice_ID = p_c_invoice_id) 
AND (t.rate = 0 OR (t.rate <> 0 AND it.taxamt = 0))
AND (t.rate > 0 and it.taxamt > 0 or t.rate = 0 and it.taxamt = 0)
) as t  
GROUP BY t.codigooperacion;       

   RETURN v_CodigoOperacion;   END; $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getcodigooperacion(integer)
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_compras_cbte_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra)::character varying(15) AS tipodecomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.puntodeventa
        END AS puntodeventa, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.numerocomprobante
        END AS nrocomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN i.importclearance
            ELSE NULL::character varying
        END::character varying(30) AS despachoimportacion, bp.taxidtype AS codigodocvendedor, bp.taxid AS nroidentificacionvendedor, bp.name AS nombrevendedor, currencyconvert(getgrandtotal(i.c_invoice_id, true), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imptotal, 0::numeric(20,2) AS impconceptosnoneto, 0::numeric AS impopeexentas, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosvaloragregado, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'N'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosdeimpunac, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'B'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepiibb, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'M'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepimpumuni, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impimpuinternos, cu.wsfecode AS codmoneda, currencyrate(i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(10,6) AS tipodecambio, 
        CASE
            WHEN (l.letra = ANY (ARRAY['B'::bpchar, 'C'::bpchar])) AND gettipodecomprobante(dt.doctypekey, l.letra)::text <> '66'::text THEN 0::numeric
            ELSE getcantidadalicuotasiva(i.c_invoice_id)
        END AS cantalicuotasiva, getcodigooperacion(i.c_invoice_id)::character varying(1) AS codigooperacion, currencyconvert(getcreditofiscalcomputable(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impcreditofiscalcomputable, currencyconvert(getimporteotrostributos(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impotrostributos, NULL::character varying(20) AS cuitemisorcorredor, NULL::character varying(60) AS denominacionemisorcorredor, 0::numeric(20,2) AS ivacomision
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_currency cu ON cu.c_currency_id = i.c_currency_id
  WHERE (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar])) AND i.issotrx = 'N'::bpchar AND i.isactive = 'Y'::bpchar AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) AND dt.isfiscaldocument = 'Y'::bpchar AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar);

ALTER TABLE reginfo_compras_cbte_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_compras_alicuotas_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra)::character varying(15) AS tipodecomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.puntodeventa
        END AS puntodeventa, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.numerocomprobante
        END AS nrocomprobante, bp.taxidtype AS codigodocvendedor, bp.taxid AS nroidentificacionvendedor, currencyconvert(it.taxbaseamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impnetogravado, t.wsfecode AS alicuotaiva, currencyconvert(it.taxamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impuestoliquidado
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_invoicetax it ON i.c_invoice_id = it.c_invoice_id
   JOIN c_tax t ON t.c_tax_id = it.c_tax_id
  WHERE t.ispercepcion = 'N'::bpchar AND (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar])) AND i.issotrx = 'N'::bpchar AND i.isactive = 'Y'::bpchar AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) AND dt.isfiscaldocument = 'Y'::bpchar AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar) AND (l.letra <> ALL (ARRAY['B'::bpchar, 'C'::bpchar])) AND (t.rate > 0::numeric AND it.taxamt > 0::numeric OR t.rate = 0::numeric AND it.taxamt = 0::numeric);

ALTER TABLE reginfo_compras_alicuotas_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_ventas_alicuotas_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateinvoiced) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, ei.codigo AS tipodecomprobante, i.puntodeventa, i.numerocomprobante AS nrocomprobante, currencyconvert(it.taxbaseamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impnetogravado, t.wsfecode AS alicuotaiva, currencyconvert(it.taxamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impuestoliquidado
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN e_electronicinvoiceref ei ON dt.doctypekey::text ~~* (ei.clave_busqueda::text || '%'::text) AND ei.tabla_ref::text = 'TCOM'::text
   JOIN c_invoicetax it ON i.c_invoice_id = it.c_invoice_id
   JOIN c_tax t ON t.c_tax_id = it.c_tax_id
  WHERE t.ispercepcion = 'N'::bpchar AND (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar, 'VO'::bpchar, 'RE'::bpchar])) AND i.issotrx = 'Y'::bpchar AND i.isactive = 'Y'::bpchar AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) AND dt.isfiscaldocument = 'Y'::bpchar AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar) AND (getimporteoperacionexentas(i.c_invoice_id) <> 0::numeric AND t.rate <> 0::numeric OR getimporteoperacionexentas(i.c_invoice_id) = 0::numeric);

ALTER TABLE reginfo_ventas_alicuotas_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_ventas_cbte_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateacct) AS fechadecomprobante, ei.codigo AS tipodecomprobante, i.puntodeventa, i.numerocomprobante AS nrocomprobante, i.numerocomprobante AS nrocomprobantehasta, 
        CASE
            WHEN bp.taxidtype = '99'::bpchar AND i.grandtotal > 1000::numeric THEN '96'::bpchar
            ELSE bp.taxidtype
        END::character(2) AS codigodoccomprador, gettaxid(bp.taxid, bp.taxidtype, bp.c_categoria_iva_id, i.nroidentificcliente, i.grandtotal)::character varying(20) AS nroidentificacioncomprador, bp.name AS nombrecomprador, currencyconvert(getgrandtotal(i.c_invoice_id, true), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imptotal, 0::numeric(20,2) AS impconceptosnoneto, 0::numeric(20,2) AS imppercepnocategorizados, currencyconvert(getimporteoperacionexentas(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impopeexentas, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'N'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosdeimpunac, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'B'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepiibb, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'M'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepimpumuni, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impimpuinternos, cu.wsfecode AS codmoneda, currencyrate(i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(10,6) AS tipodecambio, 
        CASE
            WHEN getimporteoperacionexentas(i.c_invoice_id) <> 0::numeric THEN getcantidadalicuotasiva(i.c_invoice_id) - 1::numeric
            ELSE getcantidadalicuotasiva(i.c_invoice_id)
        END AS cantalicuotasiva, getcodigooperacion(i.c_invoice_id)::character varying(1) AS codigooperacion, currencyconvert(getimporteotrostributos(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impotrostributos, NULL::timestamp without time zone AS fechavencimientopago
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN e_electronicinvoiceref ei ON dt.doctypekey::text ~~* (ei.clave_busqueda::text || '%'::text) AND ei.tabla_ref::text = 'TCOM'::text
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_currency cu ON cu.c_currency_id = i.c_currency_id
  WHERE (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar, 'VO'::bpchar, 'RE'::bpchar])) AND i.issotrx = 'Y'::bpchar AND i.isactive = 'Y'::bpchar AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) AND dt.isfiscaldocument = 'Y'::bpchar AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar);

ALTER TABLE reginfo_ventas_cbte_v
  OWNER TO libertya;

--20190607-1400 Incorporación del nombre de la config del tpv relacionada al punto de venta del cierre fiscal
update ad_system set dummy = (SELECT addcolumnifnotexists('c_controlador_fiscal_closing_info','posname','character varying(100)'));

update c_controlador_fiscal_closing_info  ci
set posname = (SELECT name
		FROM c_pos 
		WHERE ad_client_id = 1010016 
			and posnumber = ci.puntodeventa
			and isactive = 'Y' 
		ORDER BY created desc 
		LIMIT 1);
		
--20190614-1350 Incorporación de estado de auditoría y estado de pago electrónico
DROP VIEW c_allocation_detail_credits_v;

CREATE OR REPLACE VIEW c_allocation_detail_credits_v AS 
 SELECT c.c_allocation_detail_credits_v_id, c.c_allocationhdr_id, c.ad_client_id, c.ad_org_id, c.isactive, c.created, c.createdby, c.updated, c.updatedby, c.fecha, c.c_currency_id, c.tipo, c.cash, c.payamt, c.payment_medium_name, c.pay_currency_id, c.c_bankaccount_id, c.c_invoice_credit_id, c.credit_doctypekey, c.credit_doctypename, c.credit_numerocomprobante, c.credit_puntodeventa, c.credit_letra_comprobante_id, c.credit_netamount, c.c_cashline_id, c.cashname, c.c_payment_id, c.accountno, c.checkno, c.a_name, c.a_bank, c.a_cuit, c.duedate, c.dateemissioncheck, c.checkstatus, c.creditcardnumber, c.couponbatchnumber, c.couponnumber, c.m_entidadfinancieraplan_id, c.m_entidadfinanciera_id, c.posnet, c.micr, c.isreconciled, c.creditdate, c.creditdocumentno, auditstatus, C_Bankpaymentstatus_ID, sum(COALESCE(currencyconvert(c.amount + c.discountamt + c.writeoffamt, c.c_currency_id, c.pay_currency_id, NULL::timestamp with time zone, NULL::integer, c.ad_client_id, c.ad_org_id), 0::numeric(20,2))) AS montosaldado
   FROM ( SELECT ah.c_allocationhdr_id AS c_allocation_detail_credits_v_id, ah.c_allocationhdr_id, ah.ad_client_id, ah.ad_org_id, ah.isactive, ah.created, ah.createdby, ah.updated, ah.updatedby, ah.datetrx AS fecha, ah.c_currency_id, 
                CASE
                    WHEN al.c_invoice_credit_id IS NOT NULL THEN 'N'::bpchar
                    WHEN p.tendertype IS NOT NULL THEN p.tendertype
                    WHEN p.tendertype IS NULL THEN 'CA'::bpchar
                    ELSE NULL::bpchar
                END AS tipo, 
                CASE
                    WHEN cl.c_cashline_id IS NOT NULL THEN 'Y'::text
                    WHEN cl.c_cashline_id IS NULL THEN 'N'::text
                    ELSE NULL::text
                END AS cash, abs(COALESCE(p.payamt, cl.amount, credit.grandtotal, 0::numeric(20,2))) AS payamt, 
                CASE
                    WHEN al.c_payment_id IS NOT NULL THEN pppm.name
                    WHEN al.c_cashline_id IS NOT NULL THEN cppm.name
                    WHEN al.c_invoice_credit_id IS NOT NULL THEN dt.name
                    ELSE NULL::character varying
                END AS payment_medium_name, COALESCE(p.c_currency_id, cl.c_currency_id, credit.c_currency_id) AS pay_currency_id, p.c_bankaccount_id, al.c_invoice_credit_id, dt.doctypekey AS credit_doctypekey, dt.name AS credit_doctypename, credit.numerocomprobante AS credit_numerocomprobante, credit.puntodeventa AS credit_puntodeventa, credit.c_letra_comprobante_id AS credit_letra_comprobante_id, credit.netamount AS credit_netamount, al.c_cashline_id, c.name AS cashname, al.c_payment_id, p.accountno, p.checkno, p.a_name, p.a_bank, p.a_cuit, p.duedate, p.dateemissioncheck, p.checkstatus, p.creditcardnumber, p.couponbatchnumber, p.couponnumber, p.m_entidadfinancieraplan_id, efp.m_entidadfinanciera_id, p.posnet, p.micr, p.isreconciled, 
                CASE
                    WHEN al.c_payment_id IS NOT NULL THEN p.datetrx
                    WHEN al.c_cashline_id IS NOT NULL THEN c.statementdate
                    WHEN al.c_invoice_credit_id IS NOT NULL THEN credit.dateinvoiced
                    ELSE NULL::timestamp without time zone
                END AS creditdate, 
                CASE
                    WHEN al.c_payment_id IS NOT NULL THEN p.documentno
                    WHEN al.c_cashline_id IS NOT NULL THEN ('# '::text || cl.line)::character varying
                    WHEN al.c_invoice_credit_id IS NOT NULL THEN credit.documentno
                    ELSE NULL::character varying
                END AS creditdocumentno, al.amount, al.discountamt, al.writeoffamt, p.auditstatus, p.C_Bankpaymentstatus_ID
           FROM c_allocationhdr ah
      JOIN c_allocationline al ON ah.c_allocationhdr_id = al.c_allocationhdr_id
   LEFT JOIN c_payment p ON al.c_payment_id = p.c_payment_id
   LEFT JOIN m_entidadfinancieraplan efp ON efp.m_entidadfinancieraplan_id = p.m_entidadfinancieraplan_id
   LEFT JOIN c_cashline cl ON al.c_cashline_id = cl.c_cashline_id
   LEFT JOIN c_cash c ON c.c_cash_id = cl.c_cash_id
   LEFT JOIN c_invoice credit ON al.c_invoice_credit_id = credit.c_invoice_id
   LEFT JOIN c_doctype dt ON credit.c_doctypetarget_id = dt.c_doctype_id
   LEFT JOIN c_pospaymentmedium cppm ON cppm.c_pospaymentmedium_id = cl.c_pospaymentmedium_id
   LEFT JOIN c_pospaymentmedium pppm ON pppm.c_pospaymentmedium_id = p.c_pospaymentmedium_id) c
  GROUP BY c.c_allocation_detail_credits_v_id, c.c_allocationhdr_id, c.ad_client_id, c.ad_org_id, c.isactive, c.created, c.createdby, c.updated, c.updatedby, c.fecha, c.c_currency_id, c.tipo, c.cash, c.payamt, c.payment_medium_name, c.pay_currency_id, c.c_bankaccount_id, c.c_invoice_credit_id, c.credit_doctypekey, c.credit_doctypename, c.credit_numerocomprobante, c.credit_puntodeventa, c.credit_letra_comprobante_id, c.credit_netamount, c.c_cashline_id, c.cashname, c.c_payment_id, c.accountno, c.checkno, c.a_name, c.a_bank, c.a_cuit, c.duedate, c.dateemissioncheck, c.checkstatus, c.creditcardnumber, c.couponbatchnumber, c.couponnumber, c.m_entidadfinancieraplan_id, c.m_entidadfinanciera_id, c.posnet, c.micr, c.isreconciled, c.creditdate, c.creditdocumentno, auditstatus, C_Bankpaymentstatus_ID
  ORDER BY c.c_allocationhdr_id, c.c_payment_id, c.c_cashline_id, c.c_invoice_credit_id;

ALTER TABLE c_allocation_detail_credits_v
  OWNER TO libertya;
  
--20190704-1800 Contabilidad Agrupada
CREATE TABLE fact_acct_balance_config
(
  fact_acct_balance_config_id integer NOT NULL,
  ad_client_id integer NOT NULL,
  ad_org_id integer NOT NULL,
  c_acctschema_id integer NOT NULL,
  createdby integer NOT NULL DEFAULT 0,
  created timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  updatedby integer NOT NULL DEFAULT 0,
  updated timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp(6) with time zone,
  isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
  seqno numeric(18,0) NOT NULL,
  ad_column_id integer NOT NULL,
  ad_componentversion_id integer,
  ad_componentobjectuid character varying(100),
  CONSTRAINT fact_acct_balance_config_key PRIMARY KEY (fact_acct_balance_config_id),
  CONSTRAINT adclient_factacctbalconf FOREIGN KEY (ad_client_id)
      REFERENCES ad_client (ad_client_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT adorg_factacctbalconf FOREIGN KEY (ad_org_id)
      REFERENCES ad_org (ad_org_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT cacctschema_factacctbalconf FOREIGN KEY (c_acctschema_id)
      REFERENCES c_acctschema (c_acctschema_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT column_factacctbalconf FOREIGN KEY (ad_column_id)
      REFERENCES ad_column (ad_column_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=TRUE
);
ALTER TABLE fact_acct_balance_config
  OWNER TO libertya;

update ad_system set dummy = (SELECT addcolumnifnotexists('C_AcctSchema','factacctbalanceactive','character(1) NOT NULL DEFAULT ''N''::bpchar'));
update ad_system set dummy = (SELECT addcolumnifnotexists('Fact_Acct','isfactalreadybalanced','character(1) NOT NULL DEFAULT ''N''::bpchar'));
update ad_system set dummy = (SELECT addcolumnifnotexists('Fact_Acct_Balance','description','character varying(255)'));
update ad_system set dummy = (SELECT addcolumnifnotexists('Fact_Acct_Balance','journalno','integer'));
update ad_system set dummy = (SELECT addcolumnifnotexists('t_sumsandbalance','FactAcctTable','character varying(20)'));
update ad_system set dummy = (SELECT addcolumnifnotexists('T_Acct_Balance','FactAcctTable','character varying(20)'));

CREATE OR REPLACE VIEW v_diariomayor_balance AS 
 SELECT fa.ad_client_id, fa.ad_org_id, fa.created, fa.createdby, fa.updated, fa.updatedby, ev.c_elementvalue_id, 
    fa.dateacct, fa.journalno, ev.value, (ev.value::text || ' '::text) || ev.name::text AS name, 
        coalesce(fa.description,ev.name) as description, fa.amtacctdr AS debe, fa.amtacctcr AS haber, 
        fa.amtacctdr - fa.amtacctcr AS saldo,
        fa.c_bpartner_id, 
        fa.dateacct as datedoc
   FROM fact_acct_balance  fa
   JOIN c_elementvalue ev ON ev.c_elementvalue_id = fa.account_id 
  ORDER BY ev.name, fa.dateacct;

ALTER TABLE v_diariomayor_balance
  OWNER TO libertya;

  
CREATE OR REPLACE FUNCTION update_fact_acct_balance(
    fa text,
    fact_acct_class regclass,
    sign integer)
  RETURNS void AS
$BODY$
DECLARE
	balanceconfigs record;
	r record;
	
	clientid integer;
	acctschemaid integer;
	factacctid integer;
	factalreadybalanced char;
	dobalance boolean;
	
	theinsert character varying;
	theupdate character varying;
	insertvalues character varying;
	whereclause character varying;

	qty numeric;
	amtdr numeric;
	amtcr numeric;
	
	valuei integer;
	valuen numeric;
	valuec character varying;
	valuet timestamp;
	
	ur integer;
BEGIN
	EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident('ad_client_id') INTO clientid;
	EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident('c_acctschema_id') INTO acctschemaid;
	EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident('isfactalreadybalanced') INTO factalreadybalanced;

	-- Si está activo el balance contable entonces comenzar con las actividades
	select into r c_acctschema_id
	from c_acctschema 
	where isactive = 'Y' and factacctbalanceactive = 'Y' and c_acctschema_id = acctschemaid;

	-- Se debe balancear este registro cuando se hay que incrementar o el registro fue balanceado previamente 
	-- Si al decrementar el registro del balance, el fuente fact_acct no fue balanceado previamente significa que 
	-- estamos bajo un fact no balanceado el cual sus importes no fueron balanceados previamente
	dobalance := sign = 1 OR (sign = -1 AND factalreadybalanced = 'Y');

	IF ( r IS NOT NULL AND dobalance ) THEN
		-- Importes
		EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident('qty') INTO qty;
		EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident('amtacctdr') INTO amtdr;
		EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident('amtacctcr') INTO amtcr;

		qty = qty * sign;
		amtdr = amtdr * sign;
		amtcr = amtcr * sign;

		theinsert := 'INSERT INTO Fact_Acct_Balance (AD_Client_ID, C_AcctSchema_ID, Qty, AmtAcctDr, AmtAcctCr';
		insertvalues := ' VALUES ('||clientid||','||acctschemaid||','||qty||','||amtdr||','||amtcr;
		theupdate := 'UPDATE Fact_Acct_Balance SET qty = qty + '||qty||', amtacctdr = amtacctdr + '||amtdr||', amtacctcr = amtacctcr + '||amtcr;
		whereclause := ' WHERE c_acctschema_id = '||acctschemaid;
		
		-- Obtener las columnas de la configuración del balance contable 
		-- Si no existe ninguna entonces se debe a que no está habilitado 
		-- el balance contable para el esquema contable del registro (viejo y nuevo)
		FOR balanceconfigs IN select col.ad_column_id, col.columnname, c.seqno, col.ad_reference_id
					from Fact_Acct_Balance_Config c 
					join ad_column col on col.ad_column_id = c.ad_column_id
					where c.isactive = 'Y' and c.c_acctschema_id = acctschemaid
					order by c.seqno
		LOOP
			-- Armar update e insert si es que es necesario
			theinsert = theinsert ||','||balanceconfigs.columnname;
			whereclause = whereclause || ' AND ';
			-- Verificar tipos de datos para obtener los valores reales
			--Fecha
			IF (balanceconfigs.ad_reference_id = 15 
				OR balanceconfigs.ad_reference_id = 16 
				OR balanceconfigs.ad_reference_id = 24) THEN 
				EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident(lower(balanceconfigs.columnname)) INTO valuet;
				whereclause = whereclause || balanceconfigs.columnname || '::date = '||quote_literal(valuet)||'::date';
				insertvalues = insertvalues || ', '||quote_literal(valuet)||'::date ';
			--Numero
			ELSIF (balanceconfigs.ad_reference_id = 12 
				OR balanceconfigs.ad_reference_id = 22 
				OR balanceconfigs.ad_reference_id = 37
				OR balanceconfigs.ad_reference_id = 29) THEN 
				EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident(lower(balanceconfigs.columnname)) INTO valuen;
				whereclause = whereclause || ' coalesce(' || balanceconfigs.columnname || ',0) = coalesce('||valuen||',0) ';
				insertvalues = insertvalues || ', coalesce('||valuen||',0) ';
			--Integer
			ELSIF (balanceconfigs.ad_reference_id = 11
				OR balanceconfigs.ad_reference_id = 13
				OR balanceconfigs.ad_reference_id = 28
				OR balanceconfigs.ad_reference_id = 19
				OR balanceconfigs.ad_reference_id = 30
				OR balanceconfigs.ad_reference_id = 21
				OR balanceconfigs.ad_reference_id = 31
				OR balanceconfigs.ad_reference_id = 25
				OR balanceconfigs.ad_reference_id = 33
				OR balanceconfigs.ad_reference_id = 35) THEN 
				EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident(lower(balanceconfigs.columnname)) INTO valuei;
				whereclause = whereclause || ' coalesce(' || balanceconfigs.columnname || ',0) = coalesce('||valuei||',0) ';
				insertvalues = insertvalues || ', coalesce('||valuei||',0) ';
			--Otro se supone Carater
			ELSE 
				EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident(lower(balanceconfigs.columnname)) INTO valuec;
				whereclause = whereclause || balanceconfigs.columnname || ' = '||quote_literal(valuec);
				insertvalues = insertvalues || ', '||quote_literal(valuec);
			END IF;
		END LOOP;
		
		-- Insert y Update Final
		theinsert = theinsert||') '||insertvalues||') ;';
		theupdate = theupdate||whereclause;

		--RAISE NOTICE 'Insert %s',theinsert;

		-- Actualizamos, sino insertamos
		EXECUTE theupdate;
		GET DIAGNOSTICS ur = row_count;
		IF (ur < 1 AND sign > 0) THEN
			EXECUTE theinsert;
			GET DIAGNOSTICS ur = row_count;
		END IF;

		-- Actualizar el fact_acct asignando que ya fue balanceado
		IF ( factalreadybalanced = 'N' ) THEN
			EXECUTE 'SELECT (' || fa || '::' || fact_acct_class::regclass || ').' || quote_ident(lower('fact_acct_id')) INTO factacctid;

			UPDATE fact_acct
			SET isfactalreadybalanced = 'Y'
			WHERE fact_acct_id = factacctid;
		END IF;
		
	END IF;
	
	RETURN;
END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_fact_acct_balance(text, regclass, integer)
  OWNER TO libertya;


CREATE OR REPLACE FUNCTION update_fact_acct_balance()
  RETURNS trigger AS
$BODY$
DECLARE
	fa record;
BEGIN
	-- Si es delete, sólo se decrementan los datos y no se inserta
	IF (TG_OP = 'DELETE') THEN
		PERFORM update_fact_acct_balance(quote_literal(OLD), TG_RELID::regclass, -1);
		fa = OLD;
	-- Si es insert, se actualiza y sino se inserta
	ELSIF (TG_OP = 'INSERT') THEN
		PERFORM update_fact_acct_balance(quote_literal(NEW), TG_RELID::regclass, 1);
		fa = NEW;
	-- Si es update, se decrementa y luego se incrementa/inserta
	/*ELSIF (TG_OP = 'UPDATE') THEN
		PERFORM update_fact_acct_balance(quote_literal(OLD), TG_RELID::regclass, -1);
		PERFORM update_fact_acct_balance(quote_literal(NEW), TG_RELID::regclass, 1);
		fa = NEW;*/
	END IF;

	RETURN fa;
END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_fact_acct_balance()
  OWNER TO libertya;

CREATE TRIGGER update_fact_acct_balance
  AFTER INSERT OR DELETE
  ON fact_acct
  FOR EACH ROW
  EXECUTE PROCEDURE update_fact_acct_balance();
  
--20190704-1820 Mejoras a la view de saldos bancarios para que aparezcan todos los movimientos
DROP VIEW v_bankbalances;

CREATE OR REPLACE VIEW v_bankbalances AS 
         SELECT bsl.ad_client_id, bsl.ad_org_id, bsl.isactive, bs.c_bankaccount_id, '@StatementLine@'::character varying AS documenttype, bsl.description::text::character varying AS documentno, bs.statementdate AS datetrx, bsl.dateacct AS duedate, bs.docstatus, '' AS ischequesencartera, 
                CASE
                    WHEN bsl.stmtamt < 0.0 THEN abs(bsl.stmtamt)
                    ELSE 0.0
                END AS debit, 
                CASE
                    WHEN bsl.stmtamt >= 0.0 THEN abs(bsl.stmtamt)
                    ELSE 0.0
                END AS credit, bsl.isreconciled, NULL::unknown AS tendertype, bsl.description
           FROM c_bankstatementline bsl
      JOIN c_bankstatement bs ON bsl.c_bankstatement_id = bs.c_bankstatement_id
      WHERE bsl.isactive <> 'X'
UNION ALL
         SELECT p.ad_client_id, p.ad_org_id, p.isactive, p.c_bankaccount_id, dt.name AS documenttype, COALESCE(
                CASE
                    WHEN p.couponnumber IS NOT NULL AND btrim(p.couponnumber::text) <> ''::text THEN p.couponnumber
                    WHEN p.checkno IS NOT NULL AND btrim(p.checkno::text) <> ''::text THEN p.checkno
                    ELSE p.documentno
                END, bp.name) AS documentno, p.datetrx, COALESCE(p.duedate, p.dateacct) AS duedate, p.docstatus, ba.ischequesencartera, 
                CASE
                    WHEN dt.signo_issotrx = 1 AND p.payamt >= 0::numeric OR dt.signo_issotrx = (-1) AND p.payamt < 0::numeric THEN abs(p.payamt)
                    ELSE 0.0
                END AS debit, 
                CASE
                    WHEN dt.signo_issotrx = 1 AND p.payamt < 0::numeric OR dt.signo_issotrx = (-1) AND p.payamt >= 0::numeric THEN abs(p.payamt)
                    ELSE 0.0
                END AS credit, p.isreconciled, p.tendertype, COALESCE(p.a_name, bp.name)::text || COALESCE(
                CASE
                    WHEN p.description IS NOT NULL AND btrim(p.description::text) <> ''::text THEN ' - '::text || p.description::text
                    ELSE NULL::text
                END, ''::text) AS description
           FROM c_payment p
      JOIN c_doctype dt ON p.c_doctype_id = dt.c_doctype_id
   JOIN c_bpartner bp ON p.c_bpartner_id = bp.c_bpartner_id
   JOIN c_bankaccount ba ON p.c_bankaccount_id = ba.c_bankaccount_id
  WHERE NOT (EXISTS ( SELECT bsl.c_bankstatementline_id, bsl.ad_client_id, bsl.ad_org_id, bsl.isactive, bsl.created, bsl.createdby, bsl.updated, bsl.updatedby, bsl.c_bankstatement_id, bsl.line, bsl.description, bsl.isreversal, bsl.c_payment_id, bsl.valutadate, bsl.dateacct, bsl.c_currency_id, bsl.trxamt, bsl.stmtamt, bsl.c_charge_id, bsl.chargeamt, bsl.interestamt, bsl.memo, bsl.referenceno, bsl.ismanual, bsl.efttrxid, bsl.efttrxtype, bsl.eftmemo, bsl.eftpayee, bsl.eftpayeeaccount, bsl.createpayment, bsl.statementlinedate, bsl.eftstatementlinedate, bsl.eftvalutadate, bsl.eftreference, bsl.eftcurrency, bsl.eftamt, bsl.eftcheckno, bsl.matchstatement, bsl.c_bpartner_id, bsl.c_invoice_id, bsl.processed, bsl.m_boletadeposito_id, bsl.isreconciled, bs.c_bankstatement_id, bs.ad_client_id, bs.ad_org_id, bs.isactive, bs.created, bs.createdby, bs.updated, bs.updatedby, bs.c_bankaccount_id, bs.name, bs.description, bs.ismanual, bs.statementdate, bs.beginningbalance, bs.endingbalance, bs.statementdifference, bs.createfrom, bs.processing, bs.processed, bs.posted, bs.eftstatementreference, bs.eftstatementdate, bs.matchstatement, bs.isapproved, bs.docstatus, bs.docaction, bsr.c_bankstatline_reconcil_id, bsr.ad_client_id, bsr.ad_org_id, bsr.isactive, bsr.created, bsr.createdby, bsr.updated, bsr.updatedby, bsr.c_bankstatementline_id, bsr.c_payment_id, bsr.m_boletadeposito_id, bsr.c_currency_id, bsr.trxamt, bsr.referenceno, bsr.ismanual, bsr.processed, bsr.isreconciled, bsr.processing, bsr.docstatus, bsr.docaction
    FROM c_bankstatementline bsl
   JOIN c_bankstatement bs ON bsl.c_bankstatement_id = bs.c_bankstatement_id
   LEFT JOIN c_bankstatline_reconcil bsr ON bsl.c_bankstatementline_id::numeric = bsr.c_bankstatementline_id
  WHERE (bs.docstatus = ANY (ARRAY['CO'::bpchar, 'CL'::bpchar])) AND (bsl.c_payment_id = p.c_payment_id OR bsr.c_payment_id = p.c_payment_id::numeric)));

ALTER TABLE v_bankbalances
  OWNER TO libertya;
  
--20190718-1122 Mejoras a las views de exportación de CITI por comprobantes de cruzados
DROP VIEW reginfo_compras_cbte_v;
DROP VIEW reginfo_compras_alicuotas_v;
DROP VIEW reginfo_ventas_alicuotas_v;
DROP VIEW reginfo_ventas_cbte_v;

CREATE OR REPLACE VIEW reginfo_compras_cbte_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra)::character varying(15) AS tipodecomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.puntodeventa
        END AS puntodeventa, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.numerocomprobante
        END AS nrocomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN i.importclearance
            ELSE NULL::character varying
        END::character varying(30) AS despachoimportacion, bp.taxidtype AS codigodocvendedor, bp.taxid AS nroidentificacionvendedor, bp.name AS nombrevendedor, currencyconvert(getgrandtotal(i.c_invoice_id, true), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imptotal, 0::numeric(20,2) AS impconceptosnoneto, 0::numeric AS impopeexentas, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosvaloragregado, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'N'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosdeimpunac, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'B'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepiibb, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'M'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepimpumuni, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impimpuinternos, cu.wsfecode AS codmoneda, currencyrate(i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(10,6) AS tipodecambio, 
        CASE
            WHEN (l.letra = ANY (ARRAY['B'::bpchar, 'C'::bpchar])) AND gettipodecomprobante(dt.doctypekey, l.letra)::text <> '66'::text THEN 0::numeric
            ELSE getcantidadalicuotasiva(i.c_invoice_id)
        END AS cantalicuotasiva, getcodigooperacion(i.c_invoice_id)::character varying(1) AS codigooperacion, currencyconvert(getcreditofiscalcomputable(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impcreditofiscalcomputable, currencyconvert(getimporteotrostributos(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impotrostributos, NULL::character varying(20) AS cuitemisorcorredor, NULL::character varying(60) AS denominacionemisorcorredor, 0::numeric(20,2) AS ivacomision
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_currency cu ON cu.c_currency_id = i.c_currency_id
  WHERE (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar])) 
	AND ((i.issotrx = 'N' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'P')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar 
	AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar);

ALTER TABLE reginfo_compras_cbte_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_compras_alicuotas_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra)::character varying(15) AS tipodecomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.puntodeventa
        END AS puntodeventa, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra)::text = '66'::text THEN 0
            ELSE i.numerocomprobante
        END AS nrocomprobante, bp.taxidtype AS codigodocvendedor, bp.taxid AS nroidentificacionvendedor, currencyconvert(it.taxbaseamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impnetogravado, t.wsfecode AS alicuotaiva, currencyconvert(it.taxamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impuestoliquidado
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_invoicetax it ON i.c_invoice_id = it.c_invoice_id
   JOIN c_tax t ON t.c_tax_id = it.c_tax_id
  WHERE t.ispercepcion = 'N'::bpchar AND (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar])) 
	AND ((i.issotrx = 'N' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'P')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar 
	AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar) 
	AND (l.letra <> ALL (ARRAY['B'::bpchar, 'C'::bpchar])) 
	AND (t.rate > 0::numeric AND it.taxamt > 0::numeric OR t.rate = 0::numeric AND it.taxamt = 0::numeric);

ALTER TABLE reginfo_compras_alicuotas_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_ventas_alicuotas_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateinvoiced) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, ei.codigo AS tipodecomprobante, i.puntodeventa, i.numerocomprobante AS nrocomprobante, currencyconvert(it.taxbaseamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impnetogravado, t.wsfecode AS alicuotaiva, currencyconvert(it.taxamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impuestoliquidado
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN e_electronicinvoiceref ei ON dt.doctypekey::text ~~* (ei.clave_busqueda::text || '%'::text) AND ei.tabla_ref::text = 'TCOM'::text
   JOIN c_invoicetax it ON i.c_invoice_id = it.c_invoice_id
   JOIN c_tax t ON t.c_tax_id = it.c_tax_id
  WHERE t.ispercepcion = 'N'::bpchar 
	AND (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar, 'VO'::bpchar, 'RE'::bpchar])) 
	AND ((i.issotrx = 'Y' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'S')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar) 
	AND (getimporteoperacionexentas(i.c_invoice_id) <> 0::numeric AND t.rate <> 0::numeric OR getimporteoperacionexentas(i.c_invoice_id) = 0::numeric);

ALTER TABLE reginfo_ventas_alicuotas_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_ventas_cbte_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateacct) AS fechadecomprobante, ei.codigo AS tipodecomprobante, i.puntodeventa, i.numerocomprobante AS nrocomprobante, i.numerocomprobante AS nrocomprobantehasta, 
        CASE
            WHEN bp.taxidtype = '99'::bpchar AND i.grandtotal > 1000::numeric THEN '96'::bpchar
            ELSE bp.taxidtype
        END::character(2) AS codigodoccomprador, gettaxid(bp.taxid, bp.taxidtype, bp.c_categoria_iva_id, i.nroidentificcliente, i.grandtotal)::character varying(20) AS nroidentificacioncomprador, bp.name AS nombrecomprador, currencyconvert(getgrandtotal(i.c_invoice_id, true), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imptotal, 0::numeric(20,2) AS impconceptosnoneto, 0::numeric(20,2) AS imppercepnocategorizados, currencyconvert(getimporteoperacionexentas(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impopeexentas, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'N'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosdeimpunac, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'B'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepiibb, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'M'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepimpumuni, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impimpuinternos, cu.wsfecode AS codmoneda, currencyrate(i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(10,6) AS tipodecambio, 
        CASE
            WHEN getimporteoperacionexentas(i.c_invoice_id) <> 0::numeric THEN getcantidadalicuotasiva(i.c_invoice_id) - 1::numeric
            ELSE getcantidadalicuotasiva(i.c_invoice_id)
        END AS cantalicuotasiva, getcodigooperacion(i.c_invoice_id)::character varying(1) AS codigooperacion, currencyconvert(getimporteotrostributos(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impotrostributos, NULL::timestamp without time zone AS fechavencimientopago
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN e_electronicinvoiceref ei ON dt.doctypekey::text ~~* (ei.clave_busqueda::text || '%'::text) AND ei.tabla_ref::text = 'TCOM'::text
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_currency cu ON cu.c_currency_id = i.c_currency_id
  WHERE (i.docstatus = ANY (ARRAY['CL'::bpchar, 'CO'::bpchar, 'VO'::bpchar, 'RE'::bpchar])) 
	AND ((i.issotrx = 'Y' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'S')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar 
	AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar);

ALTER TABLE reginfo_ventas_cbte_v
  OWNER TO libertya;
  
--20190718-1130 Incremento de tamaño de columnas de importes para el informe de cuenta corriente
ALTER TABLE t_cuentacorriente ALTER COLUMN debe TYPE numeric(22,2);
ALTER TABLE t_cuentacorriente ALTER COLUMN haber TYPE numeric(22,2);
ALTER TABLE t_cuentacorriente ALTER COLUMN saldo TYPE numeric(22,2);
ALTER TABLE t_cuentacorriente ALTER COLUMN amount TYPE numeric(22,2);

--20190724-1750 Mejoras al procedimiento CITI en cuanto a comprobantes inversos (ej comprobante de ventas que debe actuar como compras y viceversa)
update ad_system set dummy = (SELECT addcolumnifnotexists('e_electronicinvoiceref','codigo_inverso','character varying(15)'));

DROP VIEW reginfo_compras_cbte_v;
DROP VIEW reginfo_compras_alicuotas_v;
DROP VIEW reginfo_ventas_alicuotas_v;
DROP VIEW reginfo_ventas_cbte_v;
DROP FUNCTION gettipodecomprobante(character varying, character);

CREATE OR REPLACE FUNCTION gettipodecomprobante(
    p_doctypekey character varying,
    p_letra character,
    issotrx character,
    transactiontypefrontliva character)
  RETURNS character varying AS
$BODY$ 
DECLARE     	
r record; 
codigo_result character varying;
BEGIN     

SELECT ei.codigo, ei.codigo_inverso INTO r
FROM e_electronicinvoiceref ei      
WHERE (p_doctypekey || p_letra) ~~* (ei.clave_busqueda::text || '%'::text) AND ei.tabla_ref::text = 'TCOM'::text      
LIMIT 1;

-- Tipo de Transacción y configuración frente al Libro IVA
IF (issotrx = 'Y' and transactiontypefrontliva IS NOT NULL and transactiontypefrontliva = 'P') THEN
	codigo_result = r.codigo_inverso;
ELSIF (issotrx = 'N' and transactiontypefrontliva IS NOT NULL and transactiontypefrontliva = 'S') THEN
	codigo_result = r.codigo_inverso;
ELSE
	codigo_result = r.codigo;
END IF;

RETURN codigo_result; 

END; $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION gettipodecomprobante(character varying, character, character, character)
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_compras_cbte_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::character varying(15) AS tipodecomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::text = '66'::text THEN 0
            ELSE i.puntodeventa
        END AS puntodeventa, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::text = '66'::text THEN 0
            ELSE i.numerocomprobante
        END AS nrocomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::text = '66'::text THEN i.importclearance
            ELSE NULL::character varying
        END::character varying(30) AS despachoimportacion, bp.taxidtype AS codigodocvendedor, bp.taxid AS nroidentificacionvendedor, bp.name AS nombrevendedor, currencyconvert(getgrandtotal(i.c_invoice_id, true), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imptotal, 0::numeric(20,2) AS impconceptosnoneto, 0::numeric AS impopeexentas, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosvaloragregado, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'N'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosdeimpunac, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'B'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepiibb, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'M'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepimpumuni, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impimpuinternos, cu.wsfecode AS codmoneda, currencyrate(i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(10,6) AS tipodecambio, 
        CASE
            WHEN (l.letra = ANY (ARRAY['B'::bpchar, 'C'::bpchar])) AND gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::text <> '66'::text THEN 0::numeric
            ELSE getcantidadalicuotasiva(i.c_invoice_id)
        END AS cantalicuotasiva, getcodigooperacion(i.c_invoice_id)::character varying(1) AS codigooperacion, currencyconvert(getcreditofiscalcomputable(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impcreditofiscalcomputable, currencyconvert(getimporteotrostributos(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impotrostributos, NULL::character varying(20) AS cuitemisorcorredor, NULL::character varying(60) AS denominacionemisorcorredor, 0::numeric(20,2) AS ivacomision
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_currency cu ON cu.c_currency_id = i.c_currency_id
  WHERE (CASE WHEN i.issotrx = 'N' THEN i.docstatus IN ('CO','CL') ELSE i.docstatus IN ('CO','CL','VO','RE','??') END)
	AND ((i.issotrx = 'N' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'P')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar 
	AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar);

ALTER TABLE reginfo_compras_cbte_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_compras_alicuotas_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::character varying(15) AS tipodecomprobante, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::text = '66'::text THEN 0
            ELSE i.puntodeventa
        END AS puntodeventa, 
        CASE
            WHEN gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva)::text = '66'::text THEN 0
            ELSE i.numerocomprobante
        END AS nrocomprobante, bp.taxidtype AS codigodocvendedor, bp.taxid AS nroidentificacionvendedor, currencyconvert(it.taxbaseamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impnetogravado, t.wsfecode AS alicuotaiva, currencyconvert(it.taxamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impuestoliquidado
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_invoicetax it ON i.c_invoice_id = it.c_invoice_id
   JOIN c_tax t ON t.c_tax_id = it.c_tax_id
  WHERE t.ispercepcion = 'N'::bpchar 
	AND (CASE WHEN i.issotrx = 'N' THEN i.docstatus IN ('CO','CL') ELSE i.docstatus IN ('CO','CL','VO','RE','??') END)
	AND ((i.issotrx = 'N' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'P')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar 
	AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar) 
	AND (l.letra <> ALL (ARRAY['B'::bpchar, 'C'::bpchar])) 
	AND (t.rate > 0::numeric AND it.taxamt > 0::numeric OR t.rate = 0::numeric AND it.taxamt = 0::numeric);

ALTER TABLE reginfo_compras_alicuotas_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_ventas_alicuotas_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateinvoiced) AS date, date_trunc('day'::text, i.dateinvoiced) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva) AS tipodecomprobante, i.puntodeventa, i.numerocomprobante AS nrocomprobante, currencyconvert(it.taxbaseamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impnetogravado, t.wsfecode AS alicuotaiva, currencyconvert(it.taxamt, i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impuestoliquidado
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
   JOIN c_invoicetax it ON i.c_invoice_id = it.c_invoice_id
   JOIN c_tax t ON t.c_tax_id = it.c_tax_id
  WHERE t.ispercepcion = 'N'::bpchar 
	AND (CASE WHEN i.issotrx = 'N' THEN i.docstatus IN ('CO','CL') ELSE i.docstatus IN ('CO','CL','VO','RE','??') END)
	AND ((i.issotrx = 'Y' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'S')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar) 
	AND (getimporteoperacionexentas(i.c_invoice_id) <> 0::numeric AND t.rate <> 0::numeric OR getimporteoperacionexentas(i.c_invoice_id) = 0::numeric);

ALTER TABLE reginfo_ventas_alicuotas_v
  OWNER TO libertya;

CREATE OR REPLACE VIEW reginfo_ventas_cbte_v AS 
 SELECT i.ad_client_id, i.ad_org_id, i.c_invoice_id, date_trunc('day'::text, i.dateacct) AS date, date_trunc('day'::text, i.dateacct) AS fechadecomprobante, gettipodecomprobante(dt.doctypekey, l.letra, i.issotrx, dt.transactiontypefrontliva) AS tipodecomprobante, i.puntodeventa, i.numerocomprobante AS nrocomprobante, i.numerocomprobante AS nrocomprobantehasta, 
        CASE
            WHEN bp.taxidtype = '99'::bpchar AND i.grandtotal > 1000::numeric THEN '96'::bpchar
            ELSE bp.taxidtype
        END::character(2) AS codigodoccomprador, gettaxid(bp.taxid, bp.taxidtype, bp.c_categoria_iva_id, i.nroidentificcliente, i.grandtotal)::character varying(20) AS nroidentificacioncomprador, bp.name AS nombrecomprador, currencyconvert(getgrandtotal(i.c_invoice_id, true), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imptotal, 0::numeric(20,2) AS impconceptosnoneto, 0::numeric(20,2) AS imppercepnocategorizados, currencyconvert(getimporteoperacionexentas(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impopeexentas, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'N'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepopagosdeimpunac, currencyconvert(gettaxamountbyperceptiontype(i.c_invoice_id, 'B'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepiibb, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'M'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS imppercepimpumuni, currencyconvert(gettaxamountbyareatype(i.c_invoice_id, 'I'::bpchar), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impimpuinternos, cu.wsfecode AS codmoneda, currencyrate(i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(10,6) AS tipodecambio, 
        CASE
            WHEN getimporteoperacionexentas(i.c_invoice_id) <> 0::numeric THEN getcantidadalicuotasiva(i.c_invoice_id) - 1::numeric
            ELSE getcantidadalicuotasiva(i.c_invoice_id)
        END AS cantalicuotasiva, getcodigooperacion(i.c_invoice_id)::character varying(1) AS codigooperacion, currencyconvert(getimporteotrostributos(i.c_invoice_id), i.c_currency_id, 118, i.dateacct::timestamp with time zone, NULL::integer, i.ad_client_id, i.ad_org_id)::numeric(20,2) AS impotrostributos, NULL::timestamp without time zone AS fechavencimientopago
   FROM c_invoice i
   JOIN c_doctype dt ON dt.c_doctype_id = i.c_doctype_id
   JOIN c_bpartner bp ON bp.c_bpartner_id = i.c_bpartner_id
   JOIN c_currency cu ON cu.c_currency_id = i.c_currency_id
   LEFT JOIN c_letra_comprobante l ON l.c_letra_comprobante_id = i.c_letra_comprobante_id
  WHERE (CASE WHEN i.issotrx = 'N' THEN i.docstatus IN ('CO','CL') ELSE i.docstatus IN ('CO','CL','VO','RE','??') END)
	AND ((i.issotrx = 'Y' AND dt.transactiontypefrontliva is null) OR dt.transactiontypefrontliva = 'S')
	AND i.isactive = 'Y'::bpchar 
	AND (dt.doctypekey::text <> ALL (ARRAY['RTR'::character varying::text, 'RTI'::character varying::text, 'RCR'::character varying::text, 'RCI'::character varying::text])) 
	AND dt.isfiscaldocument = 'Y'::bpchar 
	AND (dt.isfiscal IS NULL OR dt.isfiscal = 'N'::bpchar OR dt.isfiscal = 'Y'::bpchar AND i.fiscalalreadyprinted = 'Y'::bpchar);

ALTER TABLE reginfo_ventas_cbte_v
  OWNER TO libertya;
  
--20190725-1037 Versionado de BBDD para release
UPDATE ad_system SET version = '31-07-2019' WHERE ad_system_id = 0;