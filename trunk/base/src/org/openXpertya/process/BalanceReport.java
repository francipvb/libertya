package org.openXpertya.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.openXpertya.cc.CurrentAccountQuery;
import org.openXpertya.model.MPayment;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Util;

public class BalanceReport extends SvrProcess {

	/** Organización de los comprobantes a consultar */
	private int    p_AD_Org_ID;
	/** Visualizar solo clientes con facturas impagas o todos (AL (All), OO (Open invoices Only) */
	private String     p_Scope; 
	/** Criterio de ordenamiento (BP (BPartner), BL (Balance), OI (Oldest open Invoice) */
	private String     p_Sort_Criteria;
	/** Grupo de entidad comercial */
	private int    p_C_BP_Group_ID;
	/** Fecha hasta de la transacción */
	private Timestamp  p_DateTrx_To;
	/** Tipo de Cuenta del Reporte: Cliente o Proveedor */
	private String     p_AccountType;
	/** Signo de documentos que son débitos (depende de p_AccountType) */
	private int debit_signo_issotrx;
	/** Signo de documentos que son créditos (depende de p_AccountType) */
	private int credit_signo_issotrx;
	/** Es transacción de ventas? (depende de p_AccountType) */
	private String isSOtrx = "Y";
	/** Moneda en la que trabaja la compañía */
	private int client_Currency_ID;
	/** Clave de búsqueda desde */
	private String valueFrom;
	/** Clave de búsqueda hasta */
	private String valueTo;
	/** Sólo mostrar con crédito activado */
	private boolean onlyCurentAccounts;
	/** Condición de los comprobantes: Efectivo, Cuenta Corriente, Todos */
	private String condition;
	/** Consulta de cuenta corriente centralizada */
	private CurrentAccountQuery currentAccountQuery;
	
	@Override
	protected void prepare() {

        ProcessInfoParameter[] para = getParameter();
        for( int i = 0;i < para.length;i++ ) {
            String name = para[ i ].getParameterName();

            if( para[ i ].getParameter() == null ) ;
            else if( name.equalsIgnoreCase( "Scope" )) {
            	p_Scope = (String)para[ i ].getParameter();
        	} else if( name.equalsIgnoreCase( "SortCriteria" )) {
            	p_Sort_Criteria = (String)para[ i ].getParameter();
        	} else if( name.equalsIgnoreCase( "AD_Org_ID" )) {
        		BigDecimal tmp = ( BigDecimal )para[ i ].getParameter();
        		p_AD_Org_ID = tmp == null ? null : tmp.intValue();
        	} else if( name.equalsIgnoreCase( "C_BP_Group_ID" )) {
        		p_C_BP_Group_ID = para[ i ].getParameterAsInt();
        	} else if( name.equalsIgnoreCase( "TrueDateTrx" )) {
        		p_DateTrx_To = ( Timestamp )para[ i ].getParameter();
        	} else if( name.equalsIgnoreCase( "AccountType" )) {
        		p_AccountType = ( String )para[ i ].getParameter();
        	} else if( name.equalsIgnoreCase( "OnlyCurrentAccounts" )) {
        		onlyCurentAccounts = ((String)para[ i ].getParameter()).equals("Y");
        	} else if( name.equalsIgnoreCase( "ValueFrom" )) {
        		valueFrom = (String)para[ i ].getParameter();
        	} else if( name.equalsIgnoreCase( "ValueTo" )) {
        		valueTo = (String)para[ i ].getParameter();
        	} else if (name.equalsIgnoreCase("Condition")) {
				setCondition((String) para[i].getParameter());
			}
        }
        
        // Reporte de Cta Corriente de Cliente o Proveedor.
 		// +-------------------------+--------------------------+
 		// |  Cta Corriente Cliente  |  Cta Corriente Proveedor |
 		// +-------------------------+--------------------------+
 		// |   Debitos  = Signo 1    |   Debitos  = Signo -1    |
 		// |   Créditos = Signo -1   |   Créditos = Signo 1     |
 		// +-------------------------+--------------------------+
 		debit_signo_issotrx = p_AccountType.equalsIgnoreCase("C") ? 1 : -1;
 		credit_signo_issotrx = p_AccountType.equalsIgnoreCase("C") ? -1 : 1;
        isSOtrx = p_AccountType.equalsIgnoreCase("C")?"'Y'":"'N'";
     // Moneda de la compañía utilizada para conversión de montos de documentos.
        client_Currency_ID = Env.getContextAsInt(getCtx(), "$C_Currency_ID");
		setCurrentAccountQuery(
				new CurrentAccountQuery(getCtx(), p_AD_Org_ID, null, true, null, p_DateTrx_To, getCondition(), null));
	}

	
	@Override
	protected String doIt() throws Exception {

		// delete all rows older than a week
		DB.executeUpdate("DELETE FROM T_BALANCEREPORT WHERE DATECREATED < ('now'::text)::timestamp(6) - interval '7 days'");		
		// delete all rows in table with the given ad_pinstance_id
		DB.executeUpdate("DELETE FROM T_BALANCEREPORT WHERE AD_PInstance_ID = " + getAD_PInstance_ID());

		// calcular el estado de cuenta de cada E.C.
		StringBuffer sqlDoc = new StringBuffer();
		sqlDoc.append(" SELECT T.C_BPARTNER_ID, T.NAME, C_BP_Group_ID, COALESCE(T.so_description,'') AS so_description, coalesce(T.totalopenbalance,0.00) as actualbalance, COALESCE(SUM(t.Credit),0.0) AS Credit, COALESCE(SUM(t.Debit),0.0) AS Debit, COALESCE(SUM(t.Debit),0.0) - COALESCE(SUM(t.Credit),0.0) AS balance,  ");
		sqlDoc.append(" 	( SELECT dateacct FROM C_INVOICE WHERE issotrx = ")
				.append(isSOtrx)
				.append(" AND invoiceopen(c_invoice_id, 0, " + getCurrentAccountQuery().getDateToInlineQuery() + ") > 0	AND C_BPartner_id = t.c_bpartner_id ORDER BY DATEACCT asc LIMIT 1 ) as fecha_fact_antigua, ");
		sqlDoc.append(" 	( SELECT dateacct FROM C_INVOICE WHERE issotrx = ")
				.append(isSOtrx);
		if (p_AD_Org_ID > 0){
			sqlDoc.append(" AND AD_Org_ID = ").append(p_AD_Org_ID);
		}
		sqlDoc.append(" AND invoiceopen(c_invoice_id, 0, " + getCurrentAccountQuery().getDateToInlineQuery() + ") > 0	AND C_BPartner_id = t.c_bpartner_id ORDER BY DATEACCT desc LIMIT 1 ) as fecha_fact_reciente, ");
		sqlDoc.append(" (select coalesce(sum(currencyconvert(invoiceopen(c_invoice_id, c_invoicepayschedule_id, " + getCurrentAccountQuery().getDateToInlineQuery() + "), c_currency_id, ").append(client_Currency_ID).append(", " + getCurrentAccountQuery().getDateToInlineQuery() + ", COALESCE(c_conversiontype_id,0), ad_client_id, ad_org_id)),0.00) as duedebt " +
						"from c_invoice_v as i " +
						"where i.duedate::date <= ?::date " +
						"		and i.c_bpartner_id = T.c_bpartner_id " +
						"		and i.docstatus not in ('DR','IN')");
		if (p_AD_Org_ID > 0){
			sqlDoc.append(" AND AD_Org_ID = ").append(p_AD_Org_ID);
		}
		sqlDoc.append(" AND AD_Client_ID = ").append(Env.getAD_Client_ID(getCtx()));
		sqlDoc.append(") as duedebt, ");
		sqlDoc.append(" (select coalesce(sum(currencyconvert(paymentavailable(c_payment_id), c_currency_id, ").append(client_Currency_ID).append(", " + getCurrentAccountQuery().getDateToInlineQuery() + ", COALESCE(c_conversiontype_id,0), ad_client_id, ad_org_id)),0.00) as duedebt " +
						"from c_payment " +
						"where duedate::date <= ?::date " +
						"		and c_bpartner_id = T.c_bpartner_id " +
						"		and docstatus not in ('DR','IN') " +
						"		and tendertype = '"+MPayment.TENDERTYPE_Check+"'");
		if (p_AD_Org_ID > 0){
			sqlDoc.append(" AND AD_Org_ID = ").append(p_AD_Org_ID);
		}
		sqlDoc.append(" AND AD_Client_ID = ").append(Env.getAD_Client_ID(getCtx()));
		sqlDoc.append(") as chequesencartera ");
		sqlDoc.append(" FROM ");
		if(!Util.isEmpty(valueFrom, true)){
			sqlDoc.append(" (SELECT min(value) as minvalue " +
						" FROM c_bpartner " +
						" WHERE ad_client_id = " + Env.getAD_Client_ID(getCtx()) +
						"		and (('"+valueFrom+"' is null OR length(trim('"+valueFrom+"'::character varying)) = 0) " +
						"				OR (CASE WHEN position('%' in '"+valueFrom+"'::character varying) > 0	" +
						"						THEN value ilike trim('"+valueFrom+"'::character varying) " +
						"						ELSE upper(value) >= upper(trim('"+valueFrom+"'::character varying)) END))) min, ");
		}
		if(!Util.isEmpty(valueTo, true)){
			sqlDoc.append(" (SELECT max(value) as maxvalue " +
						" FROM c_bpartner " +
						" WHERE ad_client_id = " + Env.getAD_Client_ID(getCtx()) +
						"		AND (('"+valueTo+"' is null OR length(trim('"+valueTo+"'::character varying)) = 0) " +
								"		OR (CASE WHEN position('%' in '"+valueTo+"'::character varying) > 0 " +
								"				THEN value ilike trim('"+valueTo+"'::character varying) " +
								"				ELSE upper(value) <= upper(trim('"+valueTo+"'::character varying)) END))) max, ");
		}
		sqlDoc.append(" ( ");
		sqlDoc.append(" 	SELECT " ); 
		sqlDoc.append(" 		d.c_bpartner_id, ");
		sqlDoc.append(" 		bp.value, ");
		sqlDoc.append(" 		bp.name, ");
		sqlDoc.append(" 		bp.C_BP_Group_ID, ");
		sqlDoc.append(" 		bp.so_description, "); 
		sqlDoc.append(" 		bp.totalopenbalance, ");
		sqlDoc.append(" 		d.debit, ");
		sqlDoc.append(" 		d.credit ");
		sqlDoc.append(" 	FROM ( ");
		sqlDoc.append(getCurrentAccountQuery().getQuery());
		sqlDoc.append(" 	) d ");
		sqlDoc.append(" 	INNER JOIN c_bpartner bp on d.c_bpartner_id = bp.c_bpartner_id ");
		sqlDoc.append(" 	WHERE bp.isactive = 'Y' ");
		if(p_C_BP_Group_ID > 0){
			sqlDoc.append(" AND bp.C_BP_Group_ID = ").append(p_C_BP_Group_ID);
		}
		if ("OO".equals(p_Scope))		// filtrar E.C.: solo las que adeudan, o listar todas
		{
			sqlDoc.append(" AND bp.C_BPartner_ID IN (");
			sqlDoc.append(" 	SELECT DISTINCT c_bpartner_id FROM c_invoice ");
			sqlDoc.append(
					" 	WHERE invoiceopen(c_invoice_id, 0, " + getCurrentAccountQuery().getDateToInlineQuery() + ") > 0 AND issotrx = ")
					.append(isSOtrx).append(" AND AD_Client_ID = ")
					.append(getAD_Client_ID()).append(")");
		}
		sqlDoc.append(p_AccountType.equalsIgnoreCase("C") ? " AND bp.iscustomer = 'Y' "
				: " AND bp.isvendor = 'Y' ");
		sqlDoc.append(onlyCurentAccounts?" AND d.socreditstatus <> 'X' ":"");
		sqlDoc.append(" ) AS T ");
		sqlDoc.append(" WHERE (1=1) ");
		if(!Util.isEmpty(valueFrom, true)){
			sqlDoc.append(" AND T.value >= minvalue ");
		}
		if(!Util.isEmpty(valueTo, true)){
			sqlDoc.append(" AND T.value <= maxvalue ");
		}
		sqlDoc.append(" GROUP BY T.c_bpartner_id, T.name, T.C_BP_Group_ID, T.so_description, T.totalopenbalance ");
		sqlDoc.append(" ORDER BY ");
		if ("BP".equals(p_Sort_Criteria))
			sqlDoc.append("T.name");
		if ("BL".equals(p_Sort_Criteria))
			sqlDoc.append("balance");		
		if ("OI".equals(p_Sort_Criteria))
			sqlDoc.append("fecha_fact_antigua");		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			// ejecutar la consulta y cargar la tabla temporal
			pstmt = DB.prepareStatement(sqlDoc.toString(), get_TrxName(), true);
			int i = 1;
			// Parámetros de sqlDoc
			pstmt.setTimestamp(i++,
					p_DateTrx_To != null ? p_DateTrx_To : Env.getDate());
			pstmt.setTimestamp(i++,
					p_DateTrx_To != null ? p_DateTrx_To : Env.getDate());
			pstmt.setInt(i++, debit_signo_issotrx);
			pstmt.setInt(i++, client_Currency_ID);
			pstmt.setInt(i++, credit_signo_issotrx);
			pstmt.setInt(i++, client_Currency_ID);
			pstmt.setInt(i++, getAD_Client_ID());
			i = pstmtSetParam(i, p_AD_Org_ID, pstmt);
			// Parámetros para el filtro de fechas
			i = pstmtSetParam(i, p_DateTrx_To, pstmt);

			rs = pstmt.executeQuery();
			int subindice=0;
			StringBuffer usql = new StringBuffer();
			while (rs.next())
			{
				subindice++;
				usql.append(" INSERT INTO T_BALANCEREPORT (ad_pinstance_id, ad_client_id, ad_org_id, subindice, c_bpartner_id, observaciones, ");
				usql.append("								credit, debit, balance, date_oldest_open_invoice, date_newest_open_invoice, sortcriteria, scope, c_bp_group_id, truedatetrx, accounttype, ");
				usql.append("								onlycurrentaccounts, valuefrom, valueto, duedebt, actualbalance, chequesencartera, generalbalance, Condition ) ");
				usql.append(" VALUES ( ")	.append(getAD_PInstance_ID()).append(",")
											.append(getAD_Client_ID()).append(",")
											.append(p_AD_Org_ID).append(",")
											.append(subindice).append(",")
											.append(rs.getInt("C_BPartner_ID")).append(", '")
											.append(rs.getString("SO_DESCRIPTION")).append("', ")
											.append(rs.getBigDecimal("Credit")).append(",")
											.append(rs.getBigDecimal("Debit")).append(",")
											.append(rs.getBigDecimal("Balance")).append(", ");
				if (rs.getTimestamp("fecha_fact_antigua")!=null)
					usql.append(" '").append(rs.getTimestamp("fecha_fact_antigua")).append("', ");
				else
					usql.append("null, ");
				if (rs.getTimestamp("fecha_fact_reciente")!=null)
					usql.append(" '").append(rs.getTimestamp("fecha_fact_reciente")).append("', ");
				else
					usql.append("null, ");
				usql.append(" '").append(p_Sort_Criteria).append("', ")
					.append(" '").append(p_Scope).append("', ")
					.append(rs.getInt("C_BP_Group_ID")).append(", ");
				if (p_DateTrx_To!=null)
					usql.append(" '").append(p_DateTrx_To).append("'::timestamp, ");
				else
					usql.append("null, ");
				usql.append("'").append(p_AccountType).append("'");
				usql.append(" , ");
				usql.append(onlyCurentAccounts?"'Y'":"'N'");
				usql.append(" , ");
				usql.append("'"+valueFrom+"'");
				usql.append(" , ");
				usql.append("'"+valueTo+"'");
				usql.append(" , ");
				usql.append(rs.getBigDecimal("duedebt"));
				usql.append(" , ");
				usql.append(rs.getBigDecimal("actualbalance"));
				usql.append(" , ");
				usql.append(rs.getBigDecimal("chequesencartera"));
				usql.append(" , ");
				usql.append(rs.getBigDecimal("actualbalance").add(
						rs.getBigDecimal("chequesencartera")));
				usql.append(" , ");
				usql.append("'"+getCondition()+"'");
				usql.append(" ); ");
			}
			
			// si no hubo entradas directamente no se ejecuta sentencia de insercion alguna
			if (subindice > 0){
				int no = DB.executeUpdate(usql.toString(), get_TrxName());
				if(no == 0){
					throw new Exception("Error insertando datos en la tabla temporal");
				}
			}
		} catch(Exception e){
			throw e;
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rs != null) rs.close();
			} catch (Exception e2) {
				throw e2;
			}
		}		
		
		return "";
		
	}

	public String getCondition() {
		return condition;
	}


	public void setCondition(String condition) {
		this.condition = condition;
	}

	protected String getDateToInlineQuery(){
		return " ('"+ ((p_DateTrx_To != null) ? p_DateTrx_To + "'" : "now'::text") + ")::timestamp(6) without time zone ";
	}


	protected CurrentAccountQuery getCurrentAccountQuery() {
		return currentAccountQuery;
	}


	protected void setCurrentAccountQuery(CurrentAccountQuery currentAccountQuery) {
		this.currentAccountQuery = currentAccountQuery;
	}
	
	private int pstmtSetParam(int index, Object value, PreparedStatement pstmt)
			throws SQLException {
		int i = index;
		if (value != null)
			pstmt.setObject(i++, value);
		return i;
	}
}