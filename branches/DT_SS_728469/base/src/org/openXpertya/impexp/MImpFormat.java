/*
 *    El contenido de este fichero está sujeto a la  Licencia Pública openXpertya versión 1.1 (LPO)
 * en tanto en cuanto forme parte íntegra del total del producto denominado:  openXpertya, solución 
 * empresarial global , y siempre según los términos de dicha licencia LPO.
 *    Una copia  íntegra de dicha  licencia está incluida con todas  las fuentes del producto.
 *    Partes del código son CopyRight (c) 2002-2007 de Ingeniería Informática Integrada S.L., otras 
 * partes son  CopyRight (c) 2002-2007 de  Consultoría y  Soporte en  Redes y  Tecnologías  de  la
 * Información S.L.,  otras partes son  adaptadas, ampliadas,  traducidas, revisadas  y/o mejoradas
 * a partir de código original de  terceros, recogidos en el  ADDENDUM  A, sección 3 (A.3) de dicha
 * licencia  LPO,  y si dicho código es extraido como parte del total del producto, estará sujeto a
 * su respectiva licencia original.  
 *     Más información en http://www.openxpertya.org/ayuda/Licencia.html
 */

package org.openXpertya.impexp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.openXpertya.model.X_AD_ImpFormat;
import org.openXpertya.util.DB;

/**
 * Descripción de Clase
 * @version 2.2, 12.10.07
 * @author Equipo de Desarrollo de openXpertya
 */
public class MImpFormat extends X_AD_ImpFormat {

	/**
	 * Constructor de la clase.
	 * @param ctx
	 * @param AD_ImpFormat_ID
	 * @param trxName
	 */
	public MImpFormat(Properties ctx, int AD_ImpFormat_ID, String trxName) {
		super(ctx, AD_ImpFormat_ID, trxName);
	} // MImpFormat

	/**
	 * Constructor de la clase.
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MImpFormat(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	} // MImpFormat

	/** @return Las filas que definen al formato. */
	public MImpFormatRow[] getRows() {
		ArrayList<MImpFormatRow> list = new ArrayList<MImpFormatRow>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM AD_ImpFormat_Row ");
		sql.append("WHERE AD_ImpFormat_ID=? ");
		sql.append("ORDER BY SeqNo");

		PreparedStatement pstmt = null;

		try {
			pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, getAD_ImpFormat_ID());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new MImpFormatRow(getCtx(), rs, get_TrxName()));
			}

			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "getRows", e);
		}

		try {
			if (pstmt != null) {
				pstmt.close();
			}

			pstmt = null;
		} catch (Exception e) {
			pstmt = null;
		}

		MImpFormatRow[] retValue = new MImpFormatRow[list.size()];

		list.toArray(retValue);

		return retValue;
	} // getRows

	/** @return El formato padre. */
	public MImpFormat getParent() {
		return new MImpFormat(getCtx(), getAD_Impformat_Parent_ID(), get_TrxName());
	}

	/** @return Todas las cabeceras de formato que tengan a este como hijo. */
	public List<MImpFormat> getChilds() {
		List<MImpFormat> list = new ArrayList<MImpFormat>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM AD_ImpFormat ");
		sql.append("WHERE ad_impformat_parent_id = ? ");
		sql.append("ORDER BY SeqNo");

		PreparedStatement pstmt = null;

		try {
			pstmt = DB.prepareStatement(sql.toString());
			pstmt.setInt(1, getAD_ImpFormat_ID());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new MImpFormat(getCtx(), rs, get_TrxName()));
			}

			rs.close();
			pstmt.close();
			pstmt = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "getChilds", e);
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			pstmt = null;
		} catch (Exception e) {
			pstmt = null;
		}
		return list;
	}

	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (getAD_Impformat_Parent_ID() > 0) {
			if (getAD_Table_ID() != getParent().getAD_Table_ID()) {
				log.saveError("", "La tabla del formato padre y del actual deben coincidir");
				return false;
			}
		}
		return super.beforeSave(newRecord);
	}

} // MImpFormat

/* @(#)MImpFormat.java 02.07.07
 * 
 * Fin del fichero MImpFormat.java
 * 
 * Versión 2.2 */
