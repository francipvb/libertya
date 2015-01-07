package org.openXpertya.process.release;

import java.sql.PreparedStatement;

import org.openXpertya.JasperReport.MJasperReport;
import org.openXpertya.process.PluginPostInstallProcess;
import org.openXpertya.util.DB;
import org.openXpertya.utils.JarHelper;

public class PostInstallUpgradeFrom1105 extends PluginPostInstallProcess {

	/** UID del reporte de Libro de IVA */
	protected final static String LIBRO_IVA_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010024";
	protected final static String LIBRO_IVA_JASPER_REPORT_FILENAME = "LibroDeIVA.jasper";
	
	/** UID del reporte del Informe de Vencimientos */
	protected final static String VENCIMIENTOS_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010043";
	protected final static String VENCIMIENTOS_JASPER_REPORT_FILENAME = "ListadoVencimientos.jasper";
	
	protected String doIt() throws Exception {
		super.doIt();
		
		// Inserts hards de traducciones para la localización Paraguay en el
		// período de tiempo 11.05 y activación del idioma
		insertTrlPY();
		
		// Actualización del libro de iva por modificaciones
		MJasperReport.updateBinaryData(get_TrxName(), getCtx(),
				LIBRO_IVA_JASPER_REPORT_UID, JarHelper.readBinaryFromJar(
						jarFileURL,
						getBinaryFileURL(LIBRO_IVA_JASPER_REPORT_FILENAME)));

		// Informe de vencimientos
		MJasperReport.updateBinaryData(get_TrxName(), getCtx(),
				VENCIMIENTOS_JASPER_REPORT_UID, JarHelper.readBinaryFromJar(
						jarFileURL,
						getBinaryFileURL(VENCIMIENTOS_JASPER_REPORT_FILENAME)));
		
		return " ";
	}
	
	
	protected void insertTrlPY() throws Exception{
		String sql = 
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015142'),'es_PY',0,0,'Y',now(),104,now(),104,'Display Logic','N','CORE-AD_Column_Trl-1015142-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023')); " +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015143'),'es_PY',0,0,'Y',now(),101,now(),101,'CreateReplicationTrigger','N','CORE-AD_Column_Trl-1015143-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023')); " +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015144'),'es_PY',0,0,'Y',now(),101,now(),101,'generatedirectmethods','N','CORE-AD_Column_Trl-1015144-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023')); " +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015145'),'es_PY',0,0,'Y',now(),104,now(),104,'shownoprice','N','CORE-AD_Column_Trl-1015145-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023')); " +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015146'),'es_PY',0,0,'Y',now(),104,now(),104,'Organization','N','CORE-AD_Column_Trl-1015146-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023')); " +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015147'),'es_PY',0,0,'Y',now(),104,now(),104,'m_product_gamas_id','N','CORE-AD_Column_Trl-1015147-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015148'),'es_PY',0,0,'Y',now(),104,now(),104,'M_Product_Lines_ID','N','CORE-AD_Column_Trl-1015148-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015149'),'es_PY',0,0,'Y',now(),104,now(),104,'Sales Transaction','N','CORE-AD_Column_Trl-1015149-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015150'),'es_PY',0,0,'Y',now(),104,now(),104,'Client','N','CORE-AD_Column_Trl-1015150-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015151'),'es_PY',0,0,'Y',now(),104,now(),104,'MovementDateFrom','N','CORE-AD_Column_Trl-1015151-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015152'),'es_PY',0,0,'Y',now(),104,now(),104,'movementdateto','N','CORE-AD_Column_Trl-1015152-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015153'),'es_PY',0,0,'Y',now(),104,now(),104,'Product','N','CORE-AD_Column_Trl-1015153-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015154'),'es_PY',0,0,'Y',now(),104,now(),104,'Product Category','N','CORE-AD_Column_Trl-1015154-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015155'),'es_PY',0,0,'Y',now(),104,now(),104,'Shipment/Receipt','N','CORE-AD_Column_Trl-1015155-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015156'),'es_PY',0,0,'Y',now(),104,now(),104,'Movement Type','N','CORE-AD_Column_Trl-1015156-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015157'),'es_PY',0,0,'Y',now(),104,now(),104,'Shipment/Receipt Line','N','CORE-AD_Column_Trl-1015157-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015158'),'es_PY',0,0,'Y',now(),104,now(),104,'movementout','N','CORE-AD_Column_Trl-1015158-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015159'),'es_PY',0,0,'Y',now(),104,now(),104,'movementin','N','CORE-AD_Column_Trl-1015159-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Column_Trl (ad_column_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Column_ID FROM ad_column WHERE ad_componentobjectuid = 'CORE-AD_Column-1015160'),'es_PY',0,0,'Y',now(),104,now(),104,'Business Partner Group','N','CORE-AD_Column_Trl-1015160-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +

		"INSERT INTO AD_Element_Trl (ad_element_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,printname,description,help,po_name,po_printname,po_description,po_help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Element_ID FROM ad_element WHERE ad_componentobjectuid = 'CORE-AD_Element-1011054'),'es_PY',0,0,'Y',now(),101,now(),101,'generatedirectmethods','generatedirectmethods',null,null,null,null,null,null,'N','CORE-AD_Element_Trl-1011054-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Element_Trl SET Description='Utilizado por el GenerateModel para saber que clases X deberan generarse con el metodo insertDirect()',IsTranslated='Y',Name='Generar metodos directos',PrintName='Generar metodos directos' WHERE ad_componentobjectuid = 'CORE-AD_Element_Trl-1011054-es_PY';" +
		"INSERT INTO AD_Element_Trl (ad_element_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,printname,description,help,po_name,po_printname,po_description,po_help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Element_ID FROM ad_element WHERE ad_componentobjectuid = 'CORE-AD_Element-1011055'),'es_PY',0,0,'Y',now(),104,now(),104,'shownoprice','shownoprice',null,null,null,null,null,null,'N','CORE-AD_Element_Trl-1011055-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Element_Trl SET Name='Fecha hasta de Movimiento',PrintName='Fecha hasta de Movimiento' WHERE ad_componentobjectuid = 'CORE-AD_Element_Trl-1000608-es_PY';" +
		"UPDATE AD_Element_Trl SET Name='M_Product_Gamas_ID',PrintName='M_Product_Gamas_ID' WHERE ad_componentobjectuid = 'CORE-AD_Element_Trl-1000299-es_PY';" +

		"INSERT INTO AD_Field_Trl (ad_field_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Field_ID FROM ad_field WHERE ad_componentobjectuid = 'CORE-AD_Field-1016459'),'es_PY',0,0,'Y',now(),104,now(),104,'Display Logic','If the Field is displayed, the result determines if the field is actually displayed','format := {expression} [{logic} {expression}]<br> expression := @{context}@{operand}{value} or @{context}@{operand}{value}<br>" + 
		"logic := {|}|{&}<br>" +
		"context := any global or window context <br>" +
		"value := strings or numbers<br>" +
		"logic operators := AND or OR with the previous result from left to right <br>" +
		"operand := eq{=}, gt{&gt;}, le{&lt;}, not{~^!} <br>" +
		"Examples: <br>" +
		"@AD_Table_ID@=14 | @Language@!GERGER <br>" +
		"@PriceLimit@>10 | @PriceList@>@PriceActual@<br>" +
		"@Name@>J<br>" +
		"Strings may be in single quotes (optional)','N','CORE-AD_Field_Trl-1016459-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Field_Trl SET Description='Si el campo es desplegado; el resultado determina si el campo es efectivamente desplegado',Help='Si el campo es desplegado; el resultado determina si el campo es efectivamente desplegado',Name='Despliegue Lógico' WHERE ad_componentobjectuid = 'CORE-AD_Field_Trl-1016459-es_PY';" +
		"INSERT INTO AD_Field_Trl (ad_field_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Field_ID FROM ad_field WHERE ad_componentobjectuid = 'CORE-AD_Field-1016460'),'es_PY',0,0,'Y',now(),101,now(),101,'GenerateDirectMethods',null,null,'N','CORE-AD_Field_Trl-1016460-es_PY',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Field_Trl SET Description='Utilizado por el GenerateModel para saber que clases X deberan generarse con el metodo insertDirect()',Name='Generar metodos directos' WHERE ad_componentobjectuid = 'CORE-AD_Field_Trl-1016460-es_PY';" +

		"INSERT INTO AD_Message_Trl (ad_message_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,msgtext,msgtip,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Message_ID FROM ad_message WHERE ad_componentobjectuid = 'CORE-AD_Message-1010733'),'es_PY',0,0,'Y',now(),104,now(),104,'No es posible guardar un combo en estado Publicado sin líneas',null,'N','CORE-AD_Message_Trl-es_PY-1010733',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Message_Trl (ad_message_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,msgtext,msgtip,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Message_ID FROM ad_message WHERE ad_componentobjectuid = 'CORE-AD_Message-1010734'),'es_PY',0,0,'Y',now(),104,now(),104,'No es posible guardar un combo nuevo en estado Publicado',null,'N','CORE-AD_Message_Trl-es_PY-1010734',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_Message_Trl (ad_message_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,msgtext,msgtip,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Message_ID FROM ad_message WHERE ad_componentobjectuid = 'CORE-AD_Message-1010735'),'es_PY',0,0,'Y',now(),104,now(),104,'Descuento de EC',null,'N','CORE-AD_Message_Trl-es_PY-1010735',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +

		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Incluir pedidos sin facturar' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-1024671-es_PY';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Stock a P. Tarifa' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-1024660-es_PY';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Stock a P. Límite' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-1024658-es_PY';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038024'),'es_PY',0,0,'Y',now(),104,now(),104,'Product Family','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038024',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='m_product_family_id' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038024';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038025'),'es_PY',0,0,'Y',now(),104,now(),104,'Product Category','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038025',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Familia' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038025';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='SubFamilia' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038025';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Marca' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038024';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038026'),'es_PY',0,0,'Y',now(),104,now(),104,'Client','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038026',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Compañía' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038026';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038027'),'es_PY',0,0,'Y',now(),104,now(),104,'MovementDateFrom','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038027',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='movementdatefrom' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038027';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038028'),'es_PY',0,0,'Y',now(),104,now(),104,'Movement Date To','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038028',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Fecha hasta de Movimiento' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038028';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038029'),'es_PY',0,0,'Y',now(),104,now(),104,'Movement In','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038029',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Movimiento de Entrada' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038029';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038030'),'es_PY',0,0,'Y',now(),104,now(),104,'Movement Out','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038030',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Movimiento de Salida' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038030';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038031'),'es_PY',0,0,'Y',now(),104,now(),104,'Movement Type','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038031',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Tipo de Movimiento' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038031';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038032'),'es_PY',0,0,'Y',now(),104,now(),104,'M_Product_Gamas_ID','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038032',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038033'),'es_PY',0,0,'Y',now(),104,now(),104,'M_Product_Lines_ID','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038033',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='m_product_lines_id' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038033';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038034'),'es_PY',0,0,'Y',now(),104,now(),104,'Organization','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038034',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Organización' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038034';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038035'),'es_PY',0,0,'Y',now(),104,now(),104,'Product','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038035',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Artículo' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038035';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038036'),'es_PY',0,0,'Y',now(),104,now(),104,'Product Category','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038036',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Familia' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038036';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038037'),'es_PY',0,0,'Y',now(),104,now(),104,'Sales Transaction','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038037',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Transacción de Ventas' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038037';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038038'),'es_PY',0,0,'Y',now(),104,now(),104,'Shipment/Receipt','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038038',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Entrega / Recibo' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038038';" +
		"INSERT INTO AD_PrintFormatItem_Trl (ad_printformatitem_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,printname,istranslated,printnamesuffix,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_printformatitem_ID FROM ad_printformatitem WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem-1038039'),'es_PY',0,0,'Y',now(),104,now(),104,'Shipment/Receipt Line','N',null,'CORE-AD_PrintFormatItem_Trl-es_PY-1038039',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Línea Entrega / Recibo' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038039';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Línea de Artículo' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038033';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Familia' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038032';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='SubFamilia' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038036';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Entrada' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038029';" +
		"UPDATE AD_PrintFormatItem_Trl SET PrintName='Salida' WHERE ad_componentobjectuid = 'CORE-AD_PrintFormatItem_Trl-es_PY-1038030';" +

		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010488'),'es_PY',0,0,'Y',now(),104,now(),104,'Do Import',null,null,'N','CORE-AD_Process_Para_Trl-es_PY-1010488',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Crear Extracto',Description='Realiza la importación de las líneas de extracto',Help='Realiza la importación de las líneas de extracto si está tildado este parámetro. Si no está tildado, solamente setea valores de los registros a importar.' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010488';" +
		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010489'),'es_PY',0,0,'Y',now(),104,now(),104,'Cash Type','Cash Type',null,'N','CORE-AD_Process_Para_Trl-es_PY-1010489',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Tipo de Efectivo',Description='Tipo de Efectivo',Help='El Tipo de Efectivo indica la fuente para esta Línea del Diario de Efectivo' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010489';" +
		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010490'),'es_PY',0,0,'Y',now(),104,now(),104,'Charge',null,null,'N','CORE-AD_Process_Para_Trl-es_PY-1010490',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Cargo',Description='Cargo',Help='Cargos adicionales del documento' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010490';" +
		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010491'),'es_PY',0,0,'Y',now(),104,now(),104,'Show Products without Price',null,null,'N','CORE-AD_Process_Para_Trl-es_PY-1010491',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Mostrar Artículos sin Precio',Description='Mostrar Artículos sin Precio en la versión de tarifa seleccionada' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010491';" +
		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010492'),'es_PY',0,0,'Y',now(),104,now(),104,'Product Line',null,null,'N','CORE-AD_Process_Para_Trl-es_PY-1010492',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Línea de Artículo' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010492';" +
		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010493'),'es_PY',0,0,'Y',now(),104,now(),104,'Product Gamas',null,null,'N','CORE-AD_Process_Para_Trl-es_PY-1010493',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Familia' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010493';" +
		"UPDATE AD_Process_Para_Trl SET Name='SubFamilia' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-1000271-es_PY';" +
		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010494'),'es_PY',0,0,'Y',now(),104,now(),104,'Business Partner Group',null,null,'N','CORE-AD_Process_Para_Trl-es_PY-1010494',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Grupo de Entidad Comercial',Description='Grupo de Entidad Comercial' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010494';" +
		"INSERT INTO AD_Process_Para_Trl (ad_process_para_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,help,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Process_Para_ID FROM ad_process_para WHERE ad_componentobjectuid = 'CORE-AD_Process_Para-1010495'),'es_PY',0,0,'Y',now(),101,now(),101,'IncludeOpenOrders',null,null,'N','CORE-AD_Process_Para_Trl-es_PY-1010495',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));" +
		"UPDATE AD_Process_Para_Trl SET Name='Incluir pedidos sin facturar',Description='Incluye en el reporte los pedidos no facturados, presentando el monto pendiente a facturar',IsTranslated='Y' WHERE ad_componentobjectuid = 'CORE-AD_Process_Para_Trl-es_PY-1010495';" +

		"INSERT INTO AD_Table_Trl (ad_table_id,ad_language,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,istranslated,ad_componentobjectuid,ad_componentversion_id) VALUES  ((SELECT AD_Table_ID FROM ad_table WHERE ad_componentobjectuid = 'CORE-AD_Table-1010254'),'es_PY',0,0,'Y',now(),104,now(),104,'RV_RotationReport_V2','N','CORE-AD_Table_Trl-es_PY-1010254',(select ad_componentversion_id FROM ad_componentversion WHERE ad_componentobjectuid = 'CORE-AD_ComponentVersion-1010023'));";
		
		PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
		ps.executeUpdate();
	}
	
}
