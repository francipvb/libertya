package org.openXpertya.process.release;

import org.openXpertya.JasperReport.MJasperReport;
import org.openXpertya.model.MProcess;
import org.openXpertya.process.PluginPostInstallProcess;
import org.openXpertya.utils.JarHelper;

public class PostInstallUpgradeFrom1503 extends PluginPostInstallProcess {
	
	/** UID del Informe de Ventas por Region */
	protected final static String VENTAS_POR_REGION_REPORT_UID = "CORE-AD_Process-1010399";
	protected final static String VENTAS_POR_REGION_REPORT_FILENAME = "VentasPorRegion.jrxml";
	
	/** UID del Informe de Compras por Region */
	protected final static String COMPRAS_POR_REGION_REPORT_UID = "CORE-AD_Process-1010400";
	protected final static String COMPRAS_POR_REGION_REPORT_FILENAME = "ComprasPorRegion.jrxml";
	
	/** UID del Informe de Cobranzas y Pagos */
	protected final static String INFORME_DE_COBRANZAS_Y_PAGOS_REPORT_UID = "CORE-AD_Process-1010401";
	protected final static String INFORME_DE_COBRANZAS_Y_PAGOS_REPORT_FILENAME = "InformeDeCobranzasYPagos.jrxml";
	
	/** UID del informe de Seguimiento de Folletos */
	protected final static String BROCHURE_REPORT_UID = "CORE-AD_JasperReport-1010129";
	protected final static String BROCHURE_REPORT_FILENAME = "BrochureReport.jasper";
	
	/** UID del Reporte de Correcciones de Comprobantes */
	protected final static String CHANGE_INVOICE_DATA_REPORT_UID = "CORE-AD_Process-1010414";
	protected final static String CHANGE_INVOICE_DATA_REPORT_FILENAME = "ChangeInvoiceDataAudit.jrxml";
	
	/** UID de la impresión de Cierre de Almacén */
	protected final static String WAREHOUSE_CLOSE_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010045";
	protected final static String WAREHOUSE_CLOSE_JASPER_REPORT_FILENAME = "WarehouseClose.jasper";
	
	/** UID del Informe de Comprobante de Retención */
	protected final static String RPT_COMPROBANTE_RETENCION_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010081";
	protected final static String RPT_COMPROBANTE_RETENCION_JASPER_REPORT_FILENAME = "rpt_Comprobante_Retencion.jasper";
	
	/** UID del reporte del Orden de Pago*/
	protected final static String ORDEN_PAGO_JASPER_REPORT_UID = "CORE-AD_JasperReport-1000012";
	protected final static String ORDEN_PAGO_JASPER_REPORT_FILENAME = "OrdenPago.jasper";
	
	/** UID del Reporte de Cheques Emitidos por Banco */
	protected final static String CHECKS_ISSUED_BY_BANK_REPORT_UID = "CORE-AD_Process-1010415";
	protected final static String CHECKS_ISSUED_BY_BANK_REPORT_FILENAME = "ChecksIssuedByBank.jrxml";
	
	/** UID del Informe de Declaración de Valores */
	protected final static String DECLARACION_VALORES_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010047";
	protected final static String DECLARACION_VALORES_JASPER_REPORT_FILENAME = "DeclaracionDeValores.jasper";
	
	/** UID del Subreporte de Valores del Informe de Declaración de Valores */
	protected final static String DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010049";
	protected final static String DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_FILENAME = "DeclaracionDeValores_Subreport_Valores.jasper";
	
	/** UID del Reporte de Facturas Autorizadas al Pago */
	protected final static String AUTHORIZED_INVOICE_TO_PAY_REPORT_UID = "CORE-AD_Process-1010421";
	protected final static String AUTHORIZED_INVOICE_TO_PAY_REPORT_FILENAME = "ReportAuthorizedInvoiceToPay.jrxml";
	
	/** UID del Reporte de Remitos */
	protected final static String IN_OUT_REPORT_UID = "CORE-AD_Process-1010422";
	protected final static String IN_OUT_REPORT_FILENAME = "InOutReport.jrxml";
	
	/** UID del Informe Libro IVA */
	protected final static String INFORME_LIBRO_IVA_REPORT_UID = "LIVA2CORE-AD_JasperReport-1010047-20121031201418";
	protected final static String INFORME_LIBRO_IVA_REPORT_FILENAME = "InformeLibroIVA.jasper";
	
	/** UID del Subreporte de Totales del Informe Libro IVA */
	protected final static String INFORME_LIBRO_TOTAL_IVA_REPORT_UID = "CORE-AD_JasperReport-1010131";
	protected final static String INFORME_LIBRO_IVA_TOTAL_REPORT_FILENAME = "InformeLibreIVA_Total.jasper";
	
	/** UID del Reporte de cheques sin Conciliar */
	protected final static String CHEQUES_SIN_CONCILIAR_REPORT_UID = "CORE-AD_Process-1010424";
	protected final static String CHEQUES_SIN_CONCILIAR_REPORT_FILENAME = "UnreconciledCheksReport.jrxml";
	
	/** Listado de OC que Recibieron Mercadería y no Poseen Factura Cargada */
	protected final static String PURCHASE_ORDER_WITH_INOUT_WITH_OUT_INVOICE_REPORT_UID = "CORE-AD_Process-1010426";
	protected final static String PURCHASE_ORDER_WITH_INOUT_WITH_OUT_INVOICE_REPORT_FILENAME = "PurchaseOrderWithInOutWithOutInvoice.jrxml";
	
	/** UID del Informe de Libro Diario */
	protected final static String REPORT_JOURNAL_BOOK_REPORT_UID = "CORE-AD_Process-1010379";
	protected final static String REPORT_JOURNAL_BOOK_REPORT_FILENAME = "LibroDiario.jrxml";
	
	/** UID del Informe de Asientos Manuales */
	protected final static String PRINT_FORMAT_FOR_JOURNAL_MANUALS_REPORT_UID = "CORE-AD_Process-1010427";
	protected final static String PRINT_FORMAT_FOR_JOURNAL_MANUALS_REPORT_FILENAME = "PrintFormatforJornalManuals.jrxml";
	
	/** UID del Informe de Libro Diario Resumido */
	protected final static String LIBRO_DIARIO_RESUMIDO_REPORT_UID = "CORE-AD_Process-1010428";
	protected final static String LIBRO_DIARIO_RESUMIDO_REPORT_FILENAME = "LibroDiarioResumido.jrxml";
	
	/** UID del Informe de OPA */
	protected final static String INFORME_OPA_REPORT_UID = "CORE-AD_Process-1010429";
	protected final static String INFORME_OPA_REPORT_FILENAME = "OPAnticipadas.jrxml";
	
	/** UID del Informe de Relacion OC-Remito-Factura */
	protected final static String TRAZABILIDAD_DE_DOCUMENTOS_REPORT_UID = "CORE-AD_Process-1010430";
	protected final static String TRAZABILIDAD_DE_DOCUMENTOS_REPORT_FILENAME = "TrazabilidadDeDocumentos.jrxml";
	
	/** UID del Reporte de Deudas de Cuentas Corrientes */
	protected final static String CURRENT_ACCOUNT_DEBTS_REPORT_UID = "CORE-AD_Process-1010394";
	protected final static String CURRENT_ACCOUNT_DEBTS_REPORT_FILENAME = "CurrentAccountDebts.jrxml";
	
	/** UID del Informe de Ranking de Ventas */
	protected final static String SALES_RANKING_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010099";
	protected final static String SALES_RANKING_JASPER_REPORT_FILENAME = "SalesRanking.jasper";
	
	/** UID del Informe de Cambio de Precios */
	protected final static String PRICE_CHANGING_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010121";
	protected final static String PRICE_CHANGING_JASPER_REPORT_FILENAME = "PriceChanging.jasper";
	
	/** Impresión de la OC */
	protected final static String ORDEN_DE_COMPRA_JASPER_REPORT_UID = "CORE-AD_JasperReport-1010023";
	protected final static String ORDEN_DE_COMPRA_JASPER_REPORT_FILENAME = "rpt_OrdenCompra.jasper";
	
	/** Reporte de Recepciones de Proveedor */
	protected final static String RECEPTIONS_VENDOR_REPORT_UID = "CORE-AD_Process-1010431";
	protected final static String RECEPTIONS_VENDOR_REPORT_FILENAME = "ReceptionsVendor.jrxml";
	
	/** Listado de OC Vencidas o Sin Novedades */
	protected final static String PURCHASE_ORDER_DUE_REPORT_UID = "CORE-AD_Process-1010432";
	protected final static String PURCHASE_ORDER_DUE_REPORT_FILENAME = "ListOfPurchaseOrdersDue.jrxml";
	
	/** Listado de OC */
	protected final static String PURCHASE_ORDER_REPORT_UID = "CORE-AD_Process-1010433";
	protected final static String PURCHASE_ORDER_REPORT_FILENAME = "PurchaseOrderReport.jrxml";
	
	@Override
	protected String doIt() throws Exception {
		super.doIt();
		
		/*
		 * Actualizacion de binarios
		 * """"""""""""""""""""""""" 
		 * Utilizar SIEMPRE los métodos MJasperReport.updateBinaryData() y MProcess.addAttachment() 
		 * para la carga de informes tipo Jasper, el primero para la carga en AD_JasperReport y el 
		 * segundo en reportes dinámicos, los cuales van adjuntos en el informe/proceso correspondiente.
		 */
		
		// Informe de Ventas por Region
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				VENTAS_POR_REGION_REPORT_UID,
				VENTAS_POR_REGION_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(VENTAS_POR_REGION_REPORT_FILENAME)));
		
		// Informe de Compras por Region
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				COMPRAS_POR_REGION_REPORT_UID,
				COMPRAS_POR_REGION_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(COMPRAS_POR_REGION_REPORT_FILENAME)));
		
		// Informe de Cobranzas y Pagos
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				INFORME_DE_COBRANZAS_Y_PAGOS_REPORT_UID,
				INFORME_DE_COBRANZAS_Y_PAGOS_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(INFORME_DE_COBRANZAS_Y_PAGOS_REPORT_FILENAME)));
		
		// Seguimiento de Folletos
		MJasperReport
				.updateBinaryData(
						get_TrxName(),
						getCtx(),
						BROCHURE_REPORT_UID,
						JarHelper
								.readBinaryFromJar(
										jarFileURL,
										getBinaryFileURL(BROCHURE_REPORT_FILENAME)));
		
		// Reporte de Correcciones de Comprobantes
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				CHANGE_INVOICE_DATA_REPORT_UID,
				CHANGE_INVOICE_DATA_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(CHANGE_INVOICE_DATA_REPORT_FILENAME)));
		
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
		
		
		
		// Informe de Orden de Pago
		MJasperReport.updateBinaryData(get_TrxName(), getCtx(),
				ORDEN_PAGO_JASPER_REPORT_UID, JarHelper.readBinaryFromJar(
						jarFileURL,
						getBinaryFileURL(ORDEN_PAGO_JASPER_REPORT_FILENAME)));
		
		// Informe de Comprobante de Retención
		MJasperReport
			.updateBinaryData(
					get_TrxName(),
					getCtx(),
					RPT_COMPROBANTE_RETENCION_JASPER_REPORT_UID,
					JarHelper
							.readBinaryFromJar(
									jarFileURL,
									getBinaryFileURL(RPT_COMPROBANTE_RETENCION_JASPER_REPORT_FILENAME)));
		
		// Reporte de Cheques Emitidos por Banco
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				CHECKS_ISSUED_BY_BANK_REPORT_UID,
				CHECKS_ISSUED_BY_BANK_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(CHECKS_ISSUED_BY_BANK_REPORT_FILENAME)));
		
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
		
		// Subreporte de Valores del Informe de Declaración de Valores
		MJasperReport
			.updateBinaryData(
					get_TrxName(),
					getCtx(),
					DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_UID,
					JarHelper
							.readBinaryFromJar(
									jarFileURL,
									getBinaryFileURL(DECLARACION_VALORES_SUBREPORT_VALORES_JASPER_REPORT_FILENAME)));
		
		// Reporte de Facturas Autorizadas al Pago
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				AUTHORIZED_INVOICE_TO_PAY_REPORT_UID,
				AUTHORIZED_INVOICE_TO_PAY_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(AUTHORIZED_INVOICE_TO_PAY_REPORT_FILENAME)));
		
		// Reporte de Remitos
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				IN_OUT_REPORT_UID,
				IN_OUT_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(IN_OUT_REPORT_FILENAME)));
		
		// Informe Libro IVA
		MJasperReport
			.updateBinaryData(
					get_TrxName(),
					getCtx(),
					INFORME_LIBRO_IVA_REPORT_UID,
					JarHelper
							.readBinaryFromJar(
									jarFileURL,
									getBinaryFileURL(INFORME_LIBRO_IVA_REPORT_FILENAME)));

		// Subreporte Totales - Informe Libro IVA
		MJasperReport
			.updateBinaryData(
					get_TrxName(),
					getCtx(),
					INFORME_LIBRO_TOTAL_IVA_REPORT_UID,
					JarHelper
							.readBinaryFromJar(
									jarFileURL,
									getBinaryFileURL(INFORME_LIBRO_IVA_TOTAL_REPORT_FILENAME)));
		
		// Reporte de cheques sin Conciliar
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				CHEQUES_SIN_CONCILIAR_REPORT_UID,
				CHEQUES_SIN_CONCILIAR_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(CHEQUES_SIN_CONCILIAR_REPORT_FILENAME)));
				
		// Listado de OC que Recibieron Mercadería y no Poseen Factura Cargada
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				PURCHASE_ORDER_WITH_INOUT_WITH_OUT_INVOICE_REPORT_UID,
				PURCHASE_ORDER_WITH_INOUT_WITH_OUT_INVOICE_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(PURCHASE_ORDER_WITH_INOUT_WITH_OUT_INVOICE_REPORT_FILENAME)));
		
		// Informe de Libro Diario
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				REPORT_JOURNAL_BOOK_REPORT_UID,
				REPORT_JOURNAL_BOOK_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(REPORT_JOURNAL_BOOK_REPORT_FILENAME)));
		
		// Informe de Asientos Manuales 
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				PRINT_FORMAT_FOR_JOURNAL_MANUALS_REPORT_UID,
				PRINT_FORMAT_FOR_JOURNAL_MANUALS_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(PRINT_FORMAT_FOR_JOURNAL_MANUALS_REPORT_FILENAME)));
		
		// Informe de Libro diario resumido
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				LIBRO_DIARIO_RESUMIDO_REPORT_UID,
				LIBRO_DIARIO_RESUMIDO_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(LIBRO_DIARIO_RESUMIDO_REPORT_FILENAME)));
		
		// Informe de OPA
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				INFORME_OPA_REPORT_UID,
				INFORME_OPA_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(INFORME_OPA_REPORT_FILENAME)));
		
		// Informe de Relacion OC-Remito-Factura
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				TRAZABILIDAD_DE_DOCUMENTOS_REPORT_UID,
				TRAZABILIDAD_DE_DOCUMENTOS_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(TRAZABILIDAD_DE_DOCUMENTOS_REPORT_FILENAME)));
		
		// Reporte de Deudas de Cuenta Corriente
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				CURRENT_ACCOUNT_DEBTS_REPORT_UID,
				CURRENT_ACCOUNT_DEBTS_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(CURRENT_ACCOUNT_DEBTS_REPORT_FILENAME)));
		
		// Informe de Ranking de Ventas
		MJasperReport
			.updateBinaryData(
					get_TrxName(),
					getCtx(),
					SALES_RANKING_JASPER_REPORT_UID,
					JarHelper
							.readBinaryFromJar(
									jarFileURL,
									getBinaryFileURL(SALES_RANKING_JASPER_REPORT_FILENAME)));
		
		// Informe de Cambio de Precios
		MJasperReport
			.updateBinaryData(
					get_TrxName(),
					getCtx(),
					PRICE_CHANGING_JASPER_REPORT_UID,
					JarHelper
							.readBinaryFromJar(
									jarFileURL,
									getBinaryFileURL(PRICE_CHANGING_JASPER_REPORT_FILENAME)));
		
		// Impresión de la OC
		MJasperReport
			.updateBinaryData(
					get_TrxName(),
					getCtx(),
					ORDEN_DE_COMPRA_JASPER_REPORT_UID,
					JarHelper
							.readBinaryFromJar(
									jarFileURL,
									getBinaryFileURL(ORDEN_DE_COMPRA_JASPER_REPORT_FILENAME)));
		
		// Reporte de Recepciones de Proveedor
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				RECEPTIONS_VENDOR_REPORT_UID,
				RECEPTIONS_VENDOR_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(RECEPTIONS_VENDOR_REPORT_FILENAME)));
		
		// Listado de OC Vencidas o Sin Novedades
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				PURCHASE_ORDER_DUE_REPORT_UID,
				PURCHASE_ORDER_DUE_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(PURCHASE_ORDER_DUE_REPORT_FILENAME)));
		
		// Listado de OC
		MProcess.addAttachment(
				get_TrxName(),
				getCtx(),
				PURCHASE_ORDER_REPORT_UID,
				PURCHASE_ORDER_REPORT_FILENAME,
				JarHelper
						.readBinaryFromJar(
								jarFileURL,
								getBinaryFileURL(PURCHASE_ORDER_REPORT_FILENAME)));
		
		return " ";
	}
	
}
