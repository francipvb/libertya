package org.openXpertya.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

public class AuthorizationChainManager {
	
	private Authorization authorizationChainDoc;
	private MAuthorizationChain authorizationChain;
	private String trxName;
	private Properties ctx;

	public AuthorizationChainManager(Authorization authorization, Properties ctx, String trxName) {
		this.setAuthorization(authorization);
		this.setCtx(ctx);
		this.setTrxName(trxName);
	}

	public void setAuthorization(Authorization authorization) {
		authorizationChainDoc = authorization;
	}

	public Authorization getAuthorization() {
		return authorizationChainDoc;
	}
	
	public void setCtx(Properties ctx){
		this.ctx = ctx;
	}

	public Properties getCtx(){
		return this.ctx;
	}
	
	public void setTrxName(String trxName){
		this.trxName = trxName;
	}

	public String getTrxName(){
		return this.trxName;
	}
	
	public String loadAuthorizationChain(Boolean reactivateDocument) throws Exception {
		boolean authorizationPending = false;
		authorizationChain = new MAuthorizationChain(
				this.getCtx(), authorizationChainDoc.getAuthorizationID(),
				this.getTrxName());
		
		// Si el documento se reactiva se tiene que volver a autorizar todos los eslabónes
		if (reactivateDocument) {
			StringBuffer sqlDeleteAll = new StringBuffer(
					" DELETE FROM M_AuthorizationChainDocument " + " WHERE "
							+ authorizationChainDoc.get_TableName() + "_ID ="
							+ authorizationChainDoc.getID());
			DB.executeUpdate(sqlDeleteAll.toString(), getTrxName());
			authorizationChainDoc.setOldGrandTotal(authorizationChainDoc.getGrandTotal());
		}
		
		// Borro todos los eslabones que no pertenezcan a la cadena de autorizacion para el pedido/fc:
		StringBuffer sql = new StringBuffer(
				" DELETE FROM M_AuthorizationChainDocument "
				+ " WHERE M_AuthorizationChainLink_ID IN "
					+ " ( SELECT M_AuthorizationChainLink_ID "
					+ " FROM M_AuthorizationChainLink acl "
					+ " WHERE acl.M_AuthorizationChain_ID = " + authorizationChain.getM_AuthorizationChain_ID()
					+ " AND (case when acl.ValidateDocumentAmount = 'Y' then (acl.MinimumAmount > " + getAmount() + ") else true end) "
					+ " AND acl.AD_Org_ID = " + authorizationChain.getAD_Org_ID()
					+ " AND acl.AD_Client_ID = " + authorizationChain.getAD_Client_ID() 
					+ ")"
				+ " AND " + authorizationChainDoc.get_TableName() + "_ID =" + authorizationChainDoc.getID()
				+ " AND Status ='" + X_M_AuthorizationChainDocument.STATUS_Pending + "'"
				);
		DB.executeUpdate(sql.toString(), getTrxName());
		
		//Veo si existen eslabones de la cadena que no estén en los eslabones del documento
		StringBuffer sqlAuthorizationChainLink_ID = new StringBuffer(
				"SELECT M_AuthorizationChainLink_ID FROM M_AuthorizationChainLink acl WHERE M_AuthorizationChain_ID = ? "
				+ " AND ? > acl.MinimumAmount "
				+ " AND acl.AD_Org_ID = ?"
				+ " AND acl.AD_Client_ID = ?"
						+ " EXCEPT "
						+ " SELECT M_AuthorizationChainLink_ID FROM M_AuthorizationChainDocument WHERE "
						+ authorizationChainDoc.get_TableName() + "_ID = ? ");

		PreparedStatement pstmt = null;

		pstmt = DB.prepareStatement(sqlAuthorizationChainLink_ID.toString(),
				getTrxName());
		pstmt.setInt(1, authorizationChainDoc.getAuthorizationID());
		pstmt.setBigDecimal(2, getAmount());
		pstmt.setInt(3, authorizationChain.getAD_Org_ID());
		pstmt.setInt(4, authorizationChain.getAD_Client_ID());
		pstmt.setInt(5, authorizationChainDoc.getID());
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			X_M_AuthorizationChainDocument authDocument = new X_M_AuthorizationChainDocument(
					authorizationChain.getCtx(), 0,
					authorizationChain.get_TrxName());
			authDocument.setM_AuthorizationChainLink_ID(rs.getInt("M_AuthorizationChainLink_ID"));
			authDocument.setStatus(X_M_AuthorizationChainDocument.STATUS_Pending);
			authorizationChainDoc.setDocumentID(authDocument);
			if (!authDocument.save()) {
				throw new Exception(CLogger.retrieveErrorAsString());
			}
			authorizationPending = true;
		}

		// Seteo a Pendiente si hay autorizaciones pendientes nuevas
		authorizationChainDoc.setAuthorizationChainStatus(authorizationPending
				? X_M_AuthorizationChainDocument.STATUS_Pending : authorizationChainDoc.getAuthorizationChainStatus());
		
		// Obtengo el estado de no autorización a setear y lo devuelvo
		String docStatus = null;
		// Obtener el estado de no autorización del tipo de documento y cadena
		MAuthorizationChainDocumentType acdt = MAuthorizationChainDocumentType.get(getCtx(),
				authorizationChain.getAD_Org_ID(), authorizationChainDoc.getDocTypeID(), authorizationChain.getID(),
				getTrxName());
		if (X_M_AuthorizationChainDocument.STATUS_Pending.equals(authorizationChainDoc.getAuthorizationChainStatus())
				&& acdt != null) {
			docStatus = acdt.getNotAuthorizationStatus();
		}
		
		return docStatus;
	}
	
	private BigDecimal getAmount(){
		return MCurrency.currencyConvert(authorizationChainDoc.getGrandTotal(), authorizationChainDoc.getC_Currency_ID(), authorizationChain.getC_Currency_ID(), Env.getDate(), authorizationChain.getAD_Org_ID(), getCtx());
	}

}
