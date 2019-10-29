package org.openXpertya.process.customImport.centralPos.jobs;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openXpertya.model.MCommissionConcepts;
import org.openXpertya.model.MCreditCardSettlement;
import org.openXpertya.model.MEntidadFinanciera;
import org.openXpertya.model.MExpenseConcepts;
import org.openXpertya.model.MIVASettlements;
import org.openXpertya.model.MPerceptionsSettlement;
import org.openXpertya.model.MRetencionSchema;
import org.openXpertya.model.MWithholdingSettlement;
import org.openXpertya.model.X_C_ExternalServiceAttributes;
import org.openXpertya.model.X_I_VisaPayments;
import org.openXpertya.process.customImport.centralPos.exceptions.SaveFromAPIException;
import org.openXpertya.process.customImport.centralPos.http.Get;
import org.openXpertya.process.customImport.centralPos.mapping.VisaPayments;
import org.openXpertya.process.customImport.centralPos.pojos.visa.pago.Datum;
import org.openXpertya.process.customImport.centralPos.pojos.visa.pago.VisaPagos;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.Env;

/**
 * Proceso de importación. Visa.
 * @author Kevin Feuerschvenger - Sur Software S.H.
 * @version 1.0
 */
public class ImportVisa extends Import {

	public ImportVisa(Properties ctx, String trxName) throws Exception {
		super(EXTERNAL_SERVICE_VISA, ctx, trxName);
	}

	@Override
	public String excecute() throws SaveFromAPIException, Exception {
		VisaPagos response; // Respuesta.
		int currentPage = 1; // Pagina actual.
		int lastPage = 2; // Ultima pagina.
		int areadyExists = 0; // Elementos omitidos.
		int processed = 0; // Elementos procesados.
		Get get; // Método get.

		// Mientras resten páginas a importar
		while (currentPage <= lastPage) {
			get = makeGetter(); // Metodo get para obtener pagos de visa.
			get.addQueryParam("paginate", resultsPerPage); // Parametro de elem. por pagina.
			get.addQueryParam("page", currentPage); // Parametro de pagina a consultar.

			// Si hay parámetros extra, los agrego.
			if (!extraParams.isEmpty()) {
				get.addQueryParams(extraParams);
			}

			StringBuffer fields = new StringBuffer();
			for (String field : VisaPayments.filteredFields) {
				fields.append(field + ",");
			}
			if (fields.length() > 0) {
				fields.deleteCharAt(fields.length() - 1);
				get.addQueryParam("_fields", fields); // Campos a recuperar.
			}
			response = (VisaPagos) get.execute(VisaPagos.class); // Ejecuto la consulta.

			currentPage = response.getPagos().getCurrentPage();
			lastPage = response.getPagos().getLastPage();

			// Por cada resultado, inserto en la tabla de importación.
			List<Datum> data = response.getPagos().getData();

			for (Datum datum: data) {
				VisaPayments payment = new VisaPayments(datum);
				int no = payment.save(ctx, trxName);
				if (no > 0) {
					processed += no;
				} else if (no < 0) {
					areadyExists += (no * -1);
				}
			}
			log.info("Procesados = " + processed + ", Preexistentes = " + areadyExists + ", Pagina = " + currentPage + "/" + lastPage);
			currentPage++;
		}
		return msg(new Object[] { processed, areadyExists });
	}

	@Override
	public void setDateFromParam(Timestamp date) {
		if (date != null) {
			addParam("fpag-min", Env.getDateFormatted(date));
		}
	}

	@Override
	public void setDateToParam(Timestamp date) {
		if (date != null) {
			addParam("fpag-max", Env.getDateFormatted(date));
		}
	}

	@Override
	public void validate(Properties ctx, ResultSet rs, Map<String, X_C_ExternalServiceAttributes> attributes,
			String trxName) throws Exception {
		int id = getC_BPartner_ID(ctx, rs.getString("num_est"), trxName);
		if (id <= 0) {
			throw new Exception("Ignorado: no se encontró Nro. de comercio en E.Financieras");
		}
		
		//IIBB
		id = getRetencionSchemaByNroEst(ctx, rs.getString("num_est"), trxName);
		if (id <= 0) {
			throw new Exception("No existe retención de IIBB para la región configurada en la E.Financiera");
		} 
		
		String name = "Ret IVA";
		id = getRetencionSchemaIDByValue(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto Ret IVA");
		}
		
		name = "Ret Ganancias";
		id = getRetencionSchemaIDByValue(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto Ret Ganancias");
		}
		
		name = "Dto por ventas de campañas";
		id = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto Dto por ventas de campañas");
		}

		name = "Costo plan acelerado cuotas";
		id = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto Costo plan acelerado cuotas");
		}
		
		name = "Cargo adic por planes cuotas";
		id = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto Cargo adic por planes cuotas");
		}
		
		name = "Importe Arancel";
		id = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto Importe Arancel");
		}
		
		name = "IVA 10.5";
		id = getTaxIDByName(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto IVA 10.5");
		}
		
		name = "IVA 21";
		id = getTaxIDByName(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto IVA 21");
		}

		name = "IVA";
		id = getTaxIDByName(ctx, attributes.get(name).getName(), trxName);
		if (id <= 0) {
			throw new Exception("No se encontró el concepto IVA");
		}
	}

	@Override
	public boolean create(Properties ctx, ResultSet rs, Map<String, X_C_ExternalServiceAttributes> attributes,
			String trxName) throws Exception {
		int C_BPartner_ID = getC_BPartner_ID(ctx, rs.getString("num_est"), trxName);
		if (C_BPartner_ID <= 0) {
			throw new Exception("Número de comercio \"" + rs.getString("num_est") + "\" ignorado.");
		}
		
		int M_EntidadFinanciera_ID = getM_EntidadFinanciera_ID(ctx, rs.getString("num_est"), trxName);
		if (M_EntidadFinanciera_ID <= 0) {
			throw new Exception("Ignorado: no se encontró Nro. de comercio en E.Financieras");
		}
		
		MEntidadFinanciera ef = new MEntidadFinanciera(ctx, M_EntidadFinanciera_ID, trxName);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse(rs.getString("fpag"));
		
		int C_CreditCardSettlement_ID = getSettlementIdFromNroAndBPartner(ctx, rs.getString("nroliq"), C_BPartner_ID,
				new Timestamp(date.getTime()), trxName);
		MCreditCardSettlement settlement = null;
		if (C_CreditCardSettlement_ID > 0) {
			settlement = new MCreditCardSettlement(ctx, C_CreditCardSettlement_ID, trxName);
			if (!settlement.getDocStatus().equals(MCreditCardSettlement.DOCSTATUS_Drafted)) {
				// Se marca importado si ya existe para que luego no se
				// levante como un registro pendiente de importación 
				return false;
			}
		}

		BigDecimal netAmt = safeMultiply(rs.getString("impneto"), rs.getString("signo_3"));
		BigDecimal amt = safeMultiply(rs.getString("impbruto"), rs.getString("signo_1"));

		//Acumuladores para totales de impuestos, tasas, etc.
		BigDecimal withholdingAmt = new BigDecimal(0);
		BigDecimal perceptionAmt = new BigDecimal(0);
		BigDecimal expensesAmt = new BigDecimal(0);
		BigDecimal ivaAmt = new BigDecimal(0);
		BigDecimal commissionAmt = new BigDecimal(0);
		
		if(settlement == null) {
			settlement = new MCreditCardSettlement(ctx, 0, trxName);
		}
		settlement.setGenerateChildrens(false);
		settlement.setAD_Org_ID(ef.getAD_Org_ID());
		settlement.setCreditCardType(MCreditCardSettlement.CREDITCARDTYPE_VISA);
		settlement.setC_BPartner_ID(C_BPartner_ID);
		settlement.setPaymentDate(new Timestamp(date.getTime()));
		settlement.setAmount(amt);
		settlement.setNetAmount(netAmt);
		settlement.setC_Currency_ID(Env.getC_Currency_ID(ctx));
		settlement.setSettlementNo(rs.getString("nroliq"));

		if (!settlement.save()) {
			throw new Exception(CLogger.retrieveErrorAsString());
		} 
		
		try {
			int C_RetencionSchema_ID = getRetencionSchemaByNroEst(ctx, rs.getString("num_est"), trxName);
			BigDecimal withholding = safeMultiply(rs.getString("ret_ingbru"), rs.getString("signo_32"));
			if (withholding.compareTo(new BigDecimal(0)) != 0 && C_RetencionSchema_ID > 0) {
				MRetencionSchema retSchema = new MRetencionSchema(ctx, C_RetencionSchema_ID, trxName);
				MWithholdingSettlement ws = new MWithholdingSettlement(ctx, 0, trxName);
				ws.setC_RetencionSchema_ID(C_RetencionSchema_ID);
				ws.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ws.setAD_Org_ID(settlement.getAD_Org_ID());
				ws.setC_Region_ID(retSchema.getC_Region_ID());
				ws.setAmount(withholding);
				if(!ws.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				withholdingAmt = withholdingAmt.add(withholding);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "Ret IVA";
			int C_RetencionSchema_ID = getRetencionSchemaIDByValue(ctx, attributes.get(name).getName(), trxName);
			BigDecimal withholding = safeMultiply(rs.getString("ret_iva"), "+");
			if (withholding.compareTo(new BigDecimal(0)) != 0) {
				MWithholdingSettlement ws = new MWithholdingSettlement(ctx, 0, trxName);
				ws.setC_RetencionSchema_ID(C_RetencionSchema_ID);
				ws.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ws.setAD_Org_ID(settlement.getAD_Org_ID());
				ws.setAmount(withholding);
				if(!ws.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				withholdingAmt = withholdingAmt.add(withholding);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "Ret Ganancias";
			int C_RetencionSchema_ID = getRetencionSchemaIDByValue(ctx, attributes.get(name).getName(), trxName);
			BigDecimal withholding = safeMultiply(rs.getString("ret_gcias"), rs.getString("signo_31"));
			if (withholding.compareTo(new BigDecimal(0)) != 0) {
				MWithholdingSettlement ws = new MWithholdingSettlement(ctx, 0, trxName);
				ws.setC_RetencionSchema_ID(C_RetencionSchema_ID);
				ws.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ws.setAD_Org_ID(settlement.getAD_Org_ID());
				ws.setAmount(withholding);
				if(!ws.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				withholdingAmt = withholdingAmt.add(withholding);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		/* -- -- -- */
		try {
			String name = "Dto por ventas de campañas";
			int C_CardSettlementConcept_ID = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
			BigDecimal expense = safeMultiply(rs.getString("dto_campania"), rs.getString("signo_04_3"));
			if (expense.compareTo(new BigDecimal(0)) != 0) {
				MExpenseConcepts ec = new MExpenseConcepts(ctx, 0, trxName);
				ec.setC_Cardsettlementconcepts_ID(C_CardSettlementConcept_ID);
				ec.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ec.setAD_Org_ID(settlement.getAD_Org_ID());
				ec.setAmount(expense);
				if(!ec.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				expensesAmt = expensesAmt.add(expense);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "Costo plan acelerado cuotas";
			int C_CardSettlementConcept_ID = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
			BigDecimal expense = safeMultiply(rs.getString("costo_cuoemi"), rs.getString("signo_12"));
			if (expense.compareTo(new BigDecimal(0)) != 0) {
				MExpenseConcepts ec = new MExpenseConcepts(ctx, 0, trxName);
				ec.setC_Cardsettlementconcepts_ID(C_CardSettlementConcept_ID);
				ec.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ec.setAD_Org_ID(settlement.getAD_Org_ID());
				ec.setAmount(expense);
				if(!ec.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				expensesAmt = expensesAmt.add(expense);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "Cargo adic por planes cuotas";
			int C_CardSettlementConcept_ID = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
			BigDecimal expense = safeMultiply(rs.getString("adic_plancuo"), rs.getString("signo_04_15"));
			if (expense.compareTo(new BigDecimal(0)) != 0) {
				MExpenseConcepts ec = new MExpenseConcepts(ctx, 0, trxName);
				ec.setC_Cardsettlementconcepts_ID(C_CardSettlementConcept_ID);
				ec.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ec.setAD_Org_ID(settlement.getAD_Org_ID());
				ec.setAmount(expense);
				if(!ec.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				expensesAmt = expensesAmt.add(expense);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "Cargo adic por op internacionales";
			int C_CardSettlementConcept_ID = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
			BigDecimal expense = safeMultiply(rs.getString("adic_opinter"), rs.getString("signo_04_17"));
			if (expense.compareTo(new BigDecimal(0)) != 0) {
				MExpenseConcepts ec = new MExpenseConcepts(ctx, 0, trxName);
				ec.setC_Cardsettlementconcepts_ID(C_CardSettlementConcept_ID);
				ec.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ec.setAD_Org_ID(settlement.getAD_Org_ID());
				ec.setAmount(expense);
				if(!ec.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				expensesAmt = expensesAmt.add(expense);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "Importe Arancel";
			int C_CardSettlementConcepts_ID = getCardSettlementConceptIDByValue(ctx, attributes.get(name).getName(), trxName);
			BigDecimal commission = safeMultiply(rs.getString("impret"), rs.getString("signo_2"));
			if (commission.compareTo(new BigDecimal(0)) != 0) {
				MCommissionConcepts cc = new MCommissionConcepts(ctx, 0, trxName);
				cc.setC_CardSettlementConcepts_ID(C_CardSettlementConcepts_ID);
				cc.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				cc.setAD_Org_ID(settlement.getAD_Org_ID());
				cc.setAmount(commission);
				if(!cc.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				commissionAmt = commissionAmt.add(commission);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "IVA 10.5";
			int C_Tax_ID = getTaxIDByName(ctx, attributes.get(name).getName(), trxName);
			BigDecimal iva = safeMultiply(rs.getString("retiva_cuo1"), rs.getString("signo_13"));
			if (iva.compareTo(new BigDecimal(0)) != 0) {
				MIVASettlements iv = new MIVASettlements(ctx, 0, trxName); 
				iv.setC_Tax_ID(C_Tax_ID);
				iv.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				iv.setAD_Org_ID(settlement.getAD_Org_ID());
				iv.setAmount(iva);
				if(!iv.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				ivaAmt = ivaAmt.add(iva);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "IVA 21";
			int C_Tax_ID = getTaxIDByName(ctx, attributes.get(name).getName(), trxName);
			BigDecimal iva = safeMultiply(rs.getString("retiva_d1"), rs.getString("signo_7")).add(safeMultiply(rs.getString("iva1_ad_plancuo"), rs.getString("signo_04_16")))
					.add(safeMultiply(rs.getString("iva1_ad_opinter"), rs.getString("signo_04_18")));
			if (iva.compareTo(new BigDecimal(0)) != 0) {
				MIVASettlements iv = new MIVASettlements(ctx, 0, trxName); 
				iv.setC_Tax_ID(C_Tax_ID);
				iv.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				iv.setAD_Org_ID(settlement.getAD_Org_ID());
				iv.setAmount(iva);
				if(!iv.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				ivaAmt = ivaAmt.add(iva);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		try {
			String name = "IVA";
			int C_Tax_ID = getTaxIDByName(ctx, attributes.get(name).getName(), trxName);
			BigDecimal perception = safeMultiply(rs.getString("retiva_esp"), rs.getString("signo_5"));
			if (perception.compareTo(new BigDecimal(0)) != 0) {
				MPerceptionsSettlement ps = new MPerceptionsSettlement(ctx, 0, trxName);
				ps.setC_Tax_ID(C_Tax_ID);
				ps.setC_CreditCardSettlement_ID(settlement.getC_CreditCardSettlement_ID());
				ps.setAD_Org_ID(settlement.getAD_Org_ID());
				ps.setAmount(perception);
				if(!ps.save()){
					throw new Exception(CLogger.retrieveErrorAsString());
				}
				perceptionAmt = perceptionAmt.add(perception);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		settlement.setWithholding(withholdingAmt);
		settlement.setPerception(perceptionAmt);
		settlement.setExpenses(expensesAmt);
		settlement.setIVAAmount(ivaAmt);
		settlement.setCommissionAmount(commissionAmt);
		if (!settlement.save()) {
			throw new Exception(CLogger.retrieveErrorAsString());
		} 
		return true;
	}

	@Override
	public String getTableName() {
		return X_I_VisaPayments.Table_Name;
	}

	@Override
	public String[] getFilteredFields() {
		return VisaPayments.filteredFields;
	}
}
