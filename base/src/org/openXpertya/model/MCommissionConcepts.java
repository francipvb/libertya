package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;

/**
 * @author Kevin Feuerschvenger - Sur Software S.H.
 */
public class MCommissionConcepts extends X_C_CommissionConcepts {
	private static final long serialVersionUID = 1L;

	public MCommissionConcepts(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MCommissionConcepts(Properties ctx, int C_CommissionConcepts_ID, String trxName) {
		super(ctx, C_CommissionConcepts_ID, trxName);
	}

	@Override
	public boolean doAfterSave(boolean newRecord, boolean success) {
		return recalculate();
	}

	@Override
	protected boolean afterDelete(boolean success) {
		return recalculate();
	}

	private boolean recalculate() {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT ");
		sql.append("	amount ");
		sql.append("FROM ");
		sql.append("	" + Table_Name + " ");
		sql.append("WHERE ");
		sql.append("	C_CreditCardSettlement_ID = ?");

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = DB.prepareStatement(sql.toString());
			ps.setInt(1, getC_CreditCardSettlement_ID());
			rs = ps.executeQuery();

			BigDecimal amt = BigDecimal.ZERO;

			while (rs.next()) {
				amt = amt.add(rs.getBigDecimal("amount"));
			}

			MCreditCardSettlement settlement = new MCreditCardSettlement(getCtx(), getC_CreditCardSettlement_ID(), get_TrxName());
			settlement.setCommissionAmount(amt);
			if (!settlement.save()) {
				throw new Exception(CLogger.retrieveErrorAsString());
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "recalculate", e);
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Cannot close statement or resultset");
			}
		}
		return true;
	}

}
