package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.openXpertya.util.DB;
import org.openXpertya.util.Msg;

public class MPaymentBatchPOInvoices extends X_C_PaymentBatchPOInvoices {
	
	private BigDecimal oldPaymentAmount = null; 
	
	public MPaymentBatchPOInvoices(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MPaymentBatchPOInvoices(Properties ctx, int C_PaymentBatchPOInvoices_ID, String trxName) {
		super(ctx, C_PaymentBatchPOInvoices_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1377325076495272116L;
	
	protected boolean beforeSave(boolean newRecord) { 
		if (!newRecord)
			oldPaymentAmount = (BigDecimal)get_ValueOld("PaymentAmount");
		return true;
	}

	protected boolean afterSave(boolean newRecord, boolean success) {
		if( !success ) {
            return success;
        }
		
		if( newRecord || is_ValueChanged( "PaymentAmount" )) {
			updateDetail(newRecord);
		}
		return true;
	}
	
	protected boolean afterDelete( boolean success ) {
        if( !success ) {
            return success;
        }

        updateDetail(false);
        return true;
    }
	
	private void updateDetail(boolean newRecord) {
		MPaymentBatchPODetail detail = new MPaymentBatchPODetail(getCtx(), this.getC_PaymentBatchpoDetail_ID(), get_TrxName());
		
		//Si es una actualización solo recalculo el importe total
		if (oldPaymentAmount != null) {
			if (!oldPaymentAmount.equals(this.getPaymentAmount())) {
				detail.setPaymentAmount(detail.getPaymentAmount().subtract(oldPaymentAmount).add(this.getPaymentAmount()));
			}
		} else {
			//Si es nuevo, sumo el importe y actualizo fechas
			if (newRecord) {
				detail.setPaymentAmount(detail.getPaymentAmount() != null ? detail.getPaymentAmount().add(this.getPaymentAmount()) : this.getPaymentAmount());
				if (detail.getFirstDueDate() == null || detail.getFirstDueDate().compareTo(this.getDueDate()) > 0) //Si la primer fecha es mayor
					detail.setFirstDueDate(this.getDueDate());
				if (detail.getLastDueDate() == null || detail.getLastDueDate().compareTo(this.getDueDate()) < 0) //Si la última fecha es menor
					detail.setLastDueDate(this.getDueDate());
			} else {
				//Sino resto el importe y actualizo fechas
				detail.setPaymentAmount(detail.getPaymentAmount().subtract(this.getPaymentAmount()));
				updateDatesOnDelete(detail);
			}
		}
		if (!detail.save()) {
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), "PaymentBatchPODetailGenerationError") + ": " + detail.getProcessMsg());
		}
	}
	
	private void updateDatesOnDelete(MPaymentBatchPODetail detail) {
		//Construyo la query
		String sql = "SELECT " +
					 "min(duedate) AS firstDueDate, " +
					 "max(duedate) AS lastDueDate " +
					 "FROM " +
					    "c_paymentbatchpoinvoices " +
					 "WHERE " +
					 	"c_paymentbatchpodetail_id = ? " + 
					    "AND c_paymentbatchpoinvoices_id != ?";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, get_TrxName());
			
			//Parámetros
			ps.setInt(1, this.getC_PaymentBatchpoDetail_ID());
			ps.setInt(2, this.getID());
			rs = ps.executeQuery();
			while (rs.next()) {
				detail.setFirstDueDate(rs.getTimestamp("firstDueDate"));
				detail.setLastDueDate(rs.getTimestamp("lastDueDate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
