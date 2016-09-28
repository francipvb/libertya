package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.openXpertya.util.DB;
import org.openXpertya.util.Msg;

/**
 * Línea de Transferencia de Mercadería
 * @author Franco Bonafine - Disytel
 */
public class MTransferLine extends X_M_TransferLine {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase.
	 * @param ctx
	 * @param transferLine_ID
	 * @param trxName
	 */
	public MTransferLine(Properties ctx, int transferLine_ID, String trxName) {
		super(ctx, transferLine_ID, trxName);

		if (transferLine_ID == 0) {
			setConfirmedQty(BigDecimal.ZERO);
			setQty(BigDecimal.ZERO);
		}
	}

	/**
	 * Constructor de la clase.
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MTransferLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * Constructor de la clase.
	 * @param transfer Encabezado al que pertenecerá la línea.
	 */
	public MTransferLine(MTransfer transfer) {
		this(transfer.getCtx(), 0, transfer.get_TrxName());
		if (transfer.getM_Transfer_ID() == 0) {
			throw new IllegalArgumentException("Header not saved");
		}
		setClientOrg(transfer);
		setM_Transfer_ID(transfer.getM_Transfer_ID());
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		// La cantidad del artículo debe ser mayor que cero
		if (getQty().compareTo(BigDecimal.ZERO) <= 0) {
			log.saveError("SaveError", Msg.getMsg(getCtx(), "ValueMustBeGreatherThanZero", new Object[] { Msg.translate(getCtx(), "Quantity"), "0" }));
			return false;
		}

		/* COMENTADO: Flexibilizar la gestión de líneas de transferencia (por
		 * ejemplo: números de serie requieren una unidad por línea) */
		/* // No es posible ingresar dos o mas líneas con el mismo artículo y ubicación origen.
		 * if (!validateDuplicateLine()) {
		 * log.saveError("SaveError", Msg.translate(getCtx(), "DuplicateMaterialTransferLine"));
		 * return false;
		 * } */

		// Obtiene el número de línea si no ha sido asignado aún
		if (getLine() == 0) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT COALESCE(MAX(Line), 0)+10 ");
			sql.append("FROM M_TransferLine ");
			sql.append("WHERE M_Transfer_ID = ?");

			int lineNo = DB.getSQLValue(get_TrxName(), sql.toString(), getM_Transfer_ID());

			setLine(lineNo);
		}
		return true;
	}

	/**
	 * @return Devuelve el artículo de la línea.
	 */
	public MProduct getProduct() {
		return MProduct.get(getCtx(), getM_Product_ID());
	}

	@Override
	public void setClientOrg(int AD_Client_ID, int AD_Org_ID) {
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}

	/**
	 * Descripción de Método
	 * @param oLine
	 * @param M_Locator_ID
	 * @param Qty
	 */
	public void setOrderLine(MOrderLine oLine, int M_Locator_Origin_ID, int M_Locator_Destination_ID, BigDecimal Qty) {
		setLine(oLine.getLine());

		MProduct product = oLine.getProduct();
		if (product == null) {
			set_ValueNoCheck("M_Product_ID", null);
			set_ValueNoCheck("M_AttributeSetInstance_ID", null);
			set_ValueNoCheck("M_Locator_ID", null);
		} else {
			setM_Product_ID(oLine.getM_Product_ID());
			setM_Locator_ID(M_Locator_Origin_ID);
			setM_Locator_To_ID(M_Locator_Destination_ID);
		}
	}

}
