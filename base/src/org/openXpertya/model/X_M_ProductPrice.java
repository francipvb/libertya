/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por M_ProductPrice
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2019-11-29 20:52:22.831 */
public class X_M_ProductPrice extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_M_ProductPrice (Properties ctx, int M_ProductPrice_ID, String trxName)
{
super (ctx, M_ProductPrice_ID, trxName);
/** if (M_ProductPrice_ID == 0)
{
setM_PriceList_Version_ID (0);
setM_Product_ID (0);
setPriceLimit (Env.ZERO);
setPriceList (Env.ZERO);
setPriceStd (Env.ZERO);
}
 */
}
/** Load Constructor */
public X_M_ProductPrice (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("M_ProductPrice");

/** TableName=M_ProductPrice */
public static final String Table_Name="M_ProductPrice";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"M_ProductPrice");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_ProductPrice[").append(getID()).append("]");
return sb.toString();
}
/** Set Price List Version.
Identifies a unique instance of a Price List */
public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
{
set_ValueNoCheck ("M_PriceList_Version_ID", new Integer(M_PriceList_Version_ID));
}
/** Get Price List Version.
Identifies a unique instance of a Price List */
public int getM_PriceList_Version_ID() 
{
Integer ii = (Integer)get_Value("M_PriceList_Version_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product.
Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
set_ValueNoCheck ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Limit Price.
Lowest price for a product */
public void setPriceLimit (BigDecimal PriceLimit)
{
if (PriceLimit == null) throw new IllegalArgumentException ("PriceLimit is mandatory");
set_Value ("PriceLimit", PriceLimit);
}
/** Get Limit Price.
Lowest price for a product */
public BigDecimal getPriceLimit() 
{
BigDecimal bd = (BigDecimal)get_Value("PriceLimit");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set List Price.
List Price */
public void setPriceList (BigDecimal PriceList)
{
if (PriceList == null) throw new IllegalArgumentException ("PriceList is mandatory");
set_Value ("PriceList", PriceList);
}
/** Get List Price.
List Price */
public BigDecimal getPriceList() 
{
BigDecimal bd = (BigDecimal)get_Value("PriceList");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Standard Price.
Standard Price */
public void setPriceStd (BigDecimal PriceStd)
{
if (PriceStd == null) throw new IllegalArgumentException ("PriceStd is mandatory");
set_Value ("PriceStd", PriceStd);
}
/** Get Standard Price.
Standard Price */
public BigDecimal getPriceStd() 
{
BigDecimal bd = (BigDecimal)get_Value("PriceStd");
if (bd == null) return Env.ZERO;
return bd;
}

public boolean insertDirect() 
{
 
try 
{
 
 		 String sql = " INSERT INTO M_ProductPrice(IsActive,Updated,AD_Client_ID,CreatedBy,AD_Org_ID,Created,M_PriceList_Version_ID,PriceList,UpdatedBy,M_Product_ID,PriceLimit,PriceStd," + getAdditionalParamNames() + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?," + getAdditionalParamMarks() + ") ";

		 if (getUpdated() == null) sql = sql.replaceFirst("Updated,","").replaceFirst("\\?,", "");
 		 if (getCreated() == null) sql = sql.replaceFirst("Created,","").replaceFirst("\\?,", "");
 		 if (getPriceList() == null) sql = sql.replaceFirst("PriceList,","").replaceFirst("\\?,", "");
 		 if (getPriceLimit() == null) sql = sql.replaceFirst("PriceLimit,","").replaceFirst("\\?,", "");
 		 if (getPriceStd() == null) sql = sql.replaceFirst("PriceStd,","").replaceFirst("\\?,", "");
 		 skipAdditionalNullValues(sql);
 

 		 sql = sql.replace(",)", ")");
 
		 sql = sql.replace(",,)", ",");
 
		 int col = 1;
 
		 CPreparedStatement pstmt = new CPreparedStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, sql, get_TrxName(), true);
 
		 pstmt.setString(col++, isActive()?"Y":"N");
		 if (getUpdated() != null) pstmt.setTimestamp(col++, getUpdated());
		 pstmt.setInt(col++, getAD_Client_ID());
		 pstmt.setInt(col++, getCreatedBy());
		 pstmt.setInt(col++, getAD_Org_ID());
		 if (getCreated() != null) pstmt.setTimestamp(col++, getCreated());
		 pstmt.setInt(col++, getM_PriceList_Version_ID());
		 if (getPriceList() != null) pstmt.setBigDecimal(col++, getPriceList());
		 pstmt.setInt(col++, getUpdatedBy());
		 pstmt.setInt(col++, getM_Product_ID());
		 if (getPriceLimit() != null) pstmt.setBigDecimal(col++, getPriceLimit());
		 if (getPriceStd() != null) pstmt.setBigDecimal(col++, getPriceStd());
		 col = setAdditionalInsertValues(col, pstmt);
 

		pstmt.executeUpdate();

		return true;

	}
catch (SQLException e) 
{
	log.log(Level.SEVERE, "insertDirect", e);
	log.saveError("Error", DB.getErrorMsg(e) + " - " + e);
	return false;
	}
catch (Exception e2) 
{
	log.log(Level.SEVERE, "insertDirect", e2);
	return false;
}

}

protected String getAdditionalParamNames() 
{
 return "";
 }
 
protected String getAdditionalParamMarks() 
{
 return "";
 }
 
protected void skipAdditionalNullValues(String sql) 
{
  }
 
protected int setAdditionalInsertValues(int col, PreparedStatement pstmt) throws Exception 
{
 return col;
 }
 
}
