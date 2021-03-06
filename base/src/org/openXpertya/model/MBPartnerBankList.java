package org.openXpertya.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.openXpertya.util.DB;
import org.openXpertya.util.Util;

public class MBPartnerBankList extends X_C_BPartner_BankList {

	public MBPartnerBankList(Properties ctx, int C_BPartner_BankList_ID, String trxName) {
		super(ctx, C_BPartner_BankList_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MBPartnerBankList(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		// CBU de 22 caracteres obligatoriamente para configuraciones de listas
		// de transferencias electrónicas
		int isTransfer = DB.getSQLValue(get_TrxName(),
				"select count(*) "
				+ "from c_electronicpaymentbranch epb "
				+ "join c_banklist_config bc on bc.c_bank_id = epb.c_bank_id "
				+ "join c_doctype dt on dt.c_doctype_id = bc.c_doctype_id "
				+ "where c_electronicpaymentbranch_id = ? and bc.paymenttype = '"
				+ X_C_BankList_Config.PAYMENTTYPE_ElectronicTransfer + "'",
				getC_ElectronicPaymentBranch_ID());
		if (isTransfer > 0) {
			// CBU Obligatorio
			// TODO Pasar a ad_message 
			String msg = "";
			if(Util.isEmpty(getCBU(), true) || getCBU().length() != 22 || !getCBU().matches("[\\d]*")){
				msg = (Util.isEmpty(msg, true)?"":". ")+"El CBU es obligatorio para configuración de transferencias electronicas y debe tener una longitud exacta de 22 numeros";
			}
			// Tipo de Cuenta obligatorio
			if(Util.isEmpty(getTransferBankAccountType(), true)){
				msg = (Util.isEmpty(msg, true)?"":". ")+"Tipo de Cuenta obligatorio";
			}
			// Concepto de transferencia obligatorio
			if(Util.isEmpty(getTransferConcept(), true)){
				msg = (Util.isEmpty(msg, true)?"":". ")+"Concepto de Transferencia obligatorio";
			}
			if(!Util.isEmpty(msg, true)){
				log.saveError("SaveError", msg);
				return false;
			}			
		}
		return true;
	}

}
