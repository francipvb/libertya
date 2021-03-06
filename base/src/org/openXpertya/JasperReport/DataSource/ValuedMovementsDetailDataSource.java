package org.openXpertya.JasperReport.DataSource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openXpertya.util.Env;
import org.openXpertya.util.Util;

public class ValuedMovementsDetailDataSource extends ValuedMovementsDataSource {

	/** Línea de Artículo */
	private Integer productLinesID;
	
	/** Tipo de precio */
	private String priceType;
	
	public ValuedMovementsDetailDataSource(String trxName) {
		super(trxName);
		// TODO Auto-generated constructor stub
	}

	public ValuedMovementsDetailDataSource(Properties ctx, Integer orgID,
			Timestamp dateFrom, Timestamp dateTo, String trxName) {
		super(ctx, orgID, dateFrom, dateTo, trxName);
		// TODO Auto-generated constructor stub
	}

	public ValuedMovementsDetailDataSource(Properties ctx, Integer orgID,
			Timestamp dateFrom, Timestamp dateTo, Integer warehouseID,
			Integer priceListVersionID, Integer chargeID, Integer productLinesID, 
			String priceType, String trxName) {
		super(ctx, orgID, dateFrom, dateTo, warehouseID, priceListVersionID,
				chargeID, trxName);
		setProductLinesID(productLinesID);
		setPriceType(priceType);
	}

	@Override
	protected String getQuery() {
		StringBuffer sql = new StringBuffer("SELECT tablename, ad_org_id, orgvalue, orgname, doc_id, documentno, datetrx, description, c_charge_id, chargename, m_warehouse_id, warehousevalue, warehousename, m_warehouseto_id, warehousetovalue, warehousetoname, m_product_id, productvalue, productname, productlinesvalue, productlinesname, pricestd, sum(qty) as qty, sum(amt) as amt " +
											" FROM (SELECT tablename, m.ad_org_id, orgvalue, orgname, doc_id, documentno, datetrx, description, c_charge_id, chargename, m_warehouse_id, warehousevalue, warehousename, m_warehouseto_id, warehousetovalue, warehousetoname, m.m_product_id, productvalue, productname, productlinesvalue, productlinesname, qty, coalesce("+getPriceColumnName()+",0) as pricestd, coalesce("+getPriceColumnName()+",0) * m.qty as amt " +
											" FROM (SELECT tablename, ad_org_id, orgvalue, orgname, doc_id, documentno, datetrx, description, c_charge_id, chargename, m_warehouse_id, warehousevalue, warehousename, m_warehouseto_id, warehousetovalue, warehousetoname, m_product_id, productvalue, productname, productlinesvalue, productlinesname, qty, coalesce(costprice,0) as costprice " +
											"		FROM v_product_movements_filtered( " + (getOrgID() != null ? getOrgID() : "-1") + ", "
																						 + (getDateFrom() != null ? "'" + getDateFrom() + "'" : "null") + "::timestamp, "
																						 + (getDateTo() != null ? "'" + getDateTo() + "'" : "null") + "::timestamp, "
																						 + (getChargeID() != null ? getChargeID() : "-1") + ", "
																						 + (getProductLinesID() != null ? getProductLinesID() : "-1") +") " +
											"		WHERE docstatus IN ('CL','CO') " +
											"				AND ad_client_id = ? " +
											"				AND tablename <> 'M_InOut' " +
											"				AND qty <> 0 ");
		if(getWarehouseID() != null){
			sql.append(" AND ((tablename = 'M_Transfer' AND type = 'T' AND aditionaltype = 'I' AND m_warehouseto_id = ?) OR m_warehouse_id = ?) ");
		}
		sql.append(") as m ");
		if(!Util.isEmpty(getPriceListVersionID())){
			sql.append(" LEFT JOIN m_productprice as pp on pp.m_product_id = m.m_product_id " +
						" WHERE pp.m_pricelist_version_id = ? ");
		}
		sql.append(" ) as a ");
		sql.append(" GROUP BY tablename, ad_org_id, orgvalue, orgname, doc_id, documentno, datetrx, description, c_charge_id, chargename, m_warehouse_id, warehousevalue, warehousename, m_warehouseto_id, warehousetovalue, warehousetoname, m_product_id, productvalue, productname, productlinesvalue, productlinesname, pricestd ");
		sql.append(" ORDER BY orgvalue, datetrx, tablename, documentno ");
		return sql.toString();
	}
	
	@Override
	protected Object[] getParameters() {
		List<Object> params = new ArrayList<Object>();
		params.add(Env.getAD_Client_ID(getCtx()));
		if(getWarehouseID() != null){
			params.add(getWarehouseID());
			params.add(getWarehouseID());
		}
		if(!Util.isEmpty(getPriceListVersionID())){
			params.add(getPriceListVersionID());
		}
		return params.toArray();
	}

	protected Integer getProductLinesID() {
		return productLinesID;
	}

	protected void setProductLinesID(Integer productLinesID) {
		this.productLinesID = productLinesID;
	}
	
	@Override
	protected String getDescriptionByTable(){
		return (String)getCurrentRecord().get("DESCRIPTION");
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	
	/**
	 * @return columna de precio a utilizar, precio de tarifa o histórico
	 */
	public String getPriceColumnName(){
		return getPriceType().equals("P")?"pricestd":"costprice";
	}
}
