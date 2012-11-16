package org.openXpertya.JasperReport.DataSource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

public class ResumenVentasCategoriaIVADataSource extends
		ResumenVentasDataSource {

	public ResumenVentasCategoriaIVADataSource(String trxName, Properties ctx,
			Integer orgID, Timestamp dateFrom, Timestamp dateTo) {
		super(trxName, ctx, orgID, dateFrom, dateTo);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getDSWhereClause() {
		return " AND trxtype IN ('CAI','P') ";
	}

	@Override
	protected List<Object> getDSWhereClauseParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getGroupFields() {
		return "c_categoria_iva_id, categorianame";
	}
	
	@Override
	protected String getLineDescription() {
		return "Total "+(String)getCurrentRecord().get("CATEGORIANAME");
	}

}
