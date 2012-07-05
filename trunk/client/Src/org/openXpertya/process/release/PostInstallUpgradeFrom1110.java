package org.openXpertya.process.release;

import org.openXpertya.JasperReport.MJasperReport;
import org.openXpertya.process.PluginPostInstallProcess;
import org.openXpertya.util.DB;
import org.openXpertya.utils.JarHelper;

public class PostInstallUpgradeFrom1110 extends PluginPostInstallProcess {

	/** UID de la impresión de Transferencia de Mercadería */
	protected final static String MATERIAL_TRANSFER_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010044";
	protected final static String MATERIAL_TRANSFER_JASPER_REPORT_FILENAME = "MaterialTransfer.jasper";
	
	/** UID de la impresión de Cierre de Almacén */
	protected final static String WAREHOUSE_CLOSE_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010045";
	protected final static String WAREHOUSE_CLOSE_JASPER_REPORT_FILENAME = "WarehouseClose.jasper";
	
	/** UID del Informe de inventario de bienes de uso */
	protected final static String INVENTORY_ASSETS_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010046";
	protected final static String INVENTORY_ASSETS_JASPER_REPORT_FILENAME = "InventoryAssetsReport.jasper";
	
	/** UID del Informe de Declaración de Valores */
	protected final static String DECLARACION_VALORES_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010047";
	protected final static String DECLARACION_VALORES_JASPER_REPORT_FILENAME = "DeclaracionDeValores.jasper";
	
	/** UID del Subreporte de transacciones del Informe de Declaración de Valores */
	protected final static String DECLARACION_VALORES_SUBREPORT_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010048";
	protected final static String DECLARACION_VALORES_SUBREPORT_JASPER_REPORT_FILENAME = "DeclaracionDeValores_Subreport.jasper";
	
	/** UID del Subreporte de valores del Informe de Declaración de Valores */
	protected final static String DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010049";
	protected final static String DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_FILENAME = "DeclaracionDeValores_Subreport_Valores.jasper";
	
	/** UID del Reporte de Cambio de Artículos */
	protected final static String CAMBIO_JASPER_REPORT_UID = "R182CORE-AD_JasperReport-1010046-20120329191043";
	protected final static String CAMBIO_JASPER_REPORT_FILENAME = "rpt_Cambio.jasper";
	
	/** UID del Reporte de Retiro por Depósito */
	protected final static String WAREHOUSE_DELIVER_DOCUMENT_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010068";
	protected final static String WAREHOUSE_DELIVER_DOCUMENT_JASPER_REPORT_FILENAME = "WarehouseDeliverDocument.jasper";
	
	/** UID de la impresión de Entradas/Simples */
	protected final static String INVENTORY_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010069";
	protected final static String INVENTORY_JASPER_REPORT_FILENAME = "Inventory.jasper";
	
	/** UID de la impresión de documento de cuenta corriente */
	protected final static String CURRENT_ACCOUNT_DOCUMENT_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010070";
	protected final static String CURRENT_ACCOUNT_DOCUMENT_JASPER_REPORT_FILENAME = "CurrentAccountDocument.jasper";
	
	/** UID del reporte de facturas en cuenta corriente para cajas diarias */
	protected final static String POS_JOURNAL_CA_INVOICES_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010071";
	protected final static String POS_JOURNAL_CA_INVOICES_JASPER_REPORT_FILENAME = "POSJournalCurrentAccountInvoices.jasper";
	
	/** UID del subreporte de facturas y sus medios de cobro para cajas diarias */
	protected final static String DECLARACION_VALORES_SUBREPORT_SALES_RECEIPT_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010072";
	protected final static String DECLARACION_VALORES_SUBREPORT_SALES_RECEIPT_JASPER_REPORT_FILENAME = "DeclaracionDeValores_Subreport_VentasReceipt.jasper";
	
	/** UID del reporte de facturas y sus medios de cobro para cajas diarias */
	protected final static String POS_JOURNAL_INVOICES_RECEIPTS_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010073";
	protected final static String POS_JOURNAL_INVOICES_RECEIPTS_JASPER_REPORT_FILENAME = "POSJournalInvoicesReceipts.jasper";
	
	/** UID del reporte de Resumen de Ventas */
	protected final static String SALES_SUMMARY_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010074";
	protected final static String SALES_SUMMARY_JASPER_REPORT_FILENAME = "ResumenVentas.jasper";
	
	/** UID del subreporte de Resumen de Ventas */
	protected final static String SALES_SUMMARY_SUBREPORT_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010075";
	protected final static String SALES_SUMMARY_SUBREPORT_JASPER_REPORT_FILENAME = "ResumenVentas-Subreport.jasper";
	
	/** UID del reporte de Auditoría de Valores */
	protected final static String AUDITORIA_VALORES_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010076";
	protected final static String AUDITORIA_VALORES_JASPER_REPORT_FILENAME = "AuditoriaDeValores.jasper";
	
	/** UID del subreporte de Auditoría de Valores */
	protected final static String AUDITORIA_VALORES_SUBREPORT_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010077";
	protected final static String AUDITORIA_VALORES_SUBREPORT_JASPER_REPORT_FILENAME = "AuditoriaDeValores_Subreport.jasper";
	
	/** UID del reporte de Precios Diferidos */
	protected final static String DEFERRED_PRICES_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010078";
	protected final static String DEFERRED_PRICES_JASPER_REPORT_FILENAME = "DeferredPrices.jasper";
	
	/** UID del reporte de Movimientos por Artículo */
	protected final static String PRODUCT_MOVEMENTS_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010079";
	protected final static String PRODUCT_MOVEMENTS_JASPER_REPORT_FILENAME = "ProductMovements.jasper";
	
	protected String doIt() throws Exception {
		super.doIt();
		
		// Impresión de Transferencia de Mercadería
		MJasperReport
				.updateBinaryData(
						get_TrxName(),
						getCtx(),
						MATERIAL_TRANSFER_JASPER_REPORT_UID,
						JarHelper
								.readBinaryFromJar(
										jarFileURL,
										getBinaryFileURL(MATERIAL_TRANSFER_JASPER_REPORT_FILENAME)));
		
		// Impresión de Cierre de Almacén
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				WAREHOUSE_CLOSE_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(WAREHOUSE_CLOSE_JASPER_REPORT_FILENAME)));
		
		// Informe de inventario de bienes de uso
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				INVENTORY_ASSETS_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(INVENTORY_ASSETS_JASPER_REPORT_FILENAME)));
		
		// Informe de Declaración de Valores
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				DECLARACION_VALORES_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(DECLARACION_VALORES_JASPER_REPORT_FILENAME)));

		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				DECLARACION_VALORES_SUBREPORT_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(DECLARACION_VALORES_SUBREPORT_JASPER_REPORT_FILENAME)));

		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_FILENAME)));
		
		// Informe de Cambio
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				CAMBIO_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(CAMBIO_JASPER_REPORT_FILENAME)));

		// Informe de Retiro por Depósito
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				WAREHOUSE_DELIVER_DOCUMENT_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(WAREHOUSE_DELIVER_DOCUMENT_JASPER_REPORT_FILENAME)));
		
		// Impresión de Entradas/Salidas Simples
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				INVENTORY_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(INVENTORY_JASPER_REPORT_FILENAME)));
		
		// Incorporar la clase validator nueva
		DB.executeUpdate(
				"UPDATE ad_client SET modelvalidationclasses = (CASE WHEN modelvalidationclasses is null THEN 'org.openXpertya.model.DocMaxLinesValidator;' ELSE 'org.openXpertya.model.DocMaxLinesValidator;' || modelvalidationclasses END) WHERE ad_client_id = 1010016",
				get_TrxName());
		
		// Impresión de Documento de Cuenta Corriente
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				CURRENT_ACCOUNT_DOCUMENT_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(CURRENT_ACCOUNT_DOCUMENT_JASPER_REPORT_FILENAME)));
		
		// Reporte de Facturas en Cuenta Corriente para Cajas Diarias
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				POS_JOURNAL_CA_INVOICES_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(POS_JOURNAL_CA_INVOICES_JASPER_REPORT_FILENAME)));
		
		// SubReporte de Facturas y sus medios de cobro para Cajas Diarias
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				DECLARACION_VALORES_SUBREPORT_SALES_RECEIPT_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(DECLARACION_VALORES_SUBREPORT_SALES_RECEIPT_JASPER_REPORT_FILENAME)));
		
		// Reporte de Facturas y sus medios de cobro para Cajas Diarias
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				POS_JOURNAL_INVOICES_RECEIPTS_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(POS_JOURNAL_INVOICES_RECEIPTS_JASPER_REPORT_FILENAME)));
		
		// Reporte de Resumen de Ventas
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				SALES_SUMMARY_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(SALES_SUMMARY_JASPER_REPORT_FILENAME)));

		// Subreporte de Resumen de Ventas
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				SALES_SUMMARY_SUBREPORT_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(SALES_SUMMARY_SUBREPORT_JASPER_REPORT_FILENAME)));
		
		// Reporte de Auditoría de Valores
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				AUDITORIA_VALORES_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(AUDITORIA_VALORES_JASPER_REPORT_FILENAME)));

		// Subreporte de Auditoría de Valores
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				AUDITORIA_VALORES_SUBREPORT_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(AUDITORIA_VALORES_SUBREPORT_JASPER_REPORT_FILENAME)));
		
		// Reporte de Precios Diferidos
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				DEFERRED_PRICES_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(DEFERRED_PRICES_JASPER_REPORT_FILENAME)));
		
		// Reporte de Movimiento por Artículos
		MJasperReport
		.updateBinaryData(
				get_TrxName(),
				getCtx(),
				PRODUCT_MOVEMENTS_JASPER_REPORT_UID,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(PRODUCT_MOVEMENTS_JASPER_REPORT_FILENAME)));
		
		return " ";
	}
	
}
