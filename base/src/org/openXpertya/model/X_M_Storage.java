/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por M_Storage
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2019-11-29 20:53:09.244 */
public class X_M_Storage extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_M_Storage (Properties ctx, int M_Storage_ID, String trxName)
{
super (ctx, M_Storage_ID, trxName);
/** if (M_Storage_ID == 0)
{
setM_AttributeSetInstance_ID (0);
setM_Locator_ID (0);
setM_Product_ID (0);
setQtyOnHand (Env.ZERO);
setQtyOrdered (Env.ZERO);
setQtyReserved (Env.ZERO);
}
 */
}
/** Load Constructor */
public X_M_Storage (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("M_Storage");

/** TableName=M_Storage */
public static final String Table_Name="M_Storage";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"M_Storage");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_Storage[").append(getID()).append("]");
return sb.toString();
}
/** Set Date last inventory count.
Date of Last Inventory Count */
public void setDateLastInventory (Timestamp DateLastInventory)
{
set_Value ("DateLastInventory", DateLastInventory);
}
/** Get Date last inventory count.
Date of Last Inventory Count */
public Timestamp getDateLastInventory() 
{
return (Timestamp)get_Value("DateLastInventory");
}
/** Set Attribute Set Instance.
Product Attribute Set Instance */
public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
{
set_ValueNoCheck ("M_AttributeSetInstance_ID", new Integer(M_AttributeSetInstance_ID));
}
/** Get Attribute Set Instance.
Product Attribute Set Instance */
public int getM_AttributeSetInstance_ID() 
{
Integer ii = (Integer)get_Value("M_AttributeSetInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Locator.
Warehouse Locator */
public void setM_Locator_ID (int M_Locator_ID)
{
set_ValueNoCheck ("M_Locator_ID", new Integer(M_Locator_ID));
}
/** Get Locator.
Warehouse Locator */
public int getM_Locator_ID() 
{
Integer ii = (Integer)get_Value("M_Locator_ID");
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
/** Set On Hand Quantity.
On Hand Quantity */
public void setQtyOnHand (BigDecimal QtyOnHand)
{
if (QtyOnHand == null) throw new IllegalArgumentException ("QtyOnHand is mandatory");
set_ValueNoCheck ("QtyOnHand", QtyOnHand);
}
/** Get On Hand Quantity.
On Hand Quantity */
public BigDecimal getQtyOnHand() 
{
BigDecimal bd = (BigDecimal)get_Value("QtyOnHand");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Ordered Quantity.
Ordered Quantity */
public void setQtyOrdered (BigDecimal QtyOrdered)
{
if (QtyOrdered == null) throw new IllegalArgumentException ("QtyOrdered is mandatory");
set_ValueNoCheck ("QtyOrdered", QtyOrdered);
}
/** Get Ordered Quantity.
Ordered Quantity */
public BigDecimal getQtyOrdered() 
{
BigDecimal bd = (BigDecimal)get_Value("QtyOrdered");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Reserved Quantity.
Reserved Quantity */
public void setQtyReserved (BigDecimal QtyReserved)
{
if (QtyReserved == null) throw new IllegalArgumentException ("QtyReserved is mandatory");
set_ValueNoCheck ("QtyReserved", QtyReserved);
}
/** Get Reserved Quantity.
Reserved Quantity */
public BigDecimal getQtyReserved() 
{
BigDecimal bd = (BigDecimal)get_Value("QtyReserved");
if (bd == null) return Env.ZERO;
return bd;
}

public boolean insertDirect() 
{
 
try 
{
 
 		 String sql = " INSERT INTO M_Storage(AD_Client_ID,IsActive,Created,CreatedBy,Updated,AD_Org_ID,UpdatedBy,QtyReserved,QtyOrdered,DateLastInventory,QtyOnHand,M_AttributeSetInstance_ID,M_Locator_ID,M_Product_ID," + getAdditionalParamNames() + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?," + getAdditionalParamMarks() + ") ";

		 if (getCreated() == null) sql = sql.replaceFirst("Created,","").replaceFirst("\\?,", "");
 		 if (getUpdated() == null) sql = sql.replaceFirst("Updated,","").replaceFirst("\\?,", "");
 		 if (getQtyReserved() == null) sql = sql.replaceFirst("QtyReserved,","").replaceFirst("\\?,", "");
 		 if (getQtyOrdered() == null) sql = sql.replaceFirst("QtyOrdered,","").replaceFirst("\\?,", "");
 		 if (getDateLastInventory() == null) sql = sql.replaceFirst("DateLastInventory,","").replaceFirst("\\?,", "");
 		 if (getQtyOnHand() == null) sql = sql.replaceFirst("QtyOnHand,","").replaceFirst("\\?,", "");
 		 skipAdditionalNullValues(sql);
 

 		 sql = sql.replace(",)", ")");
 
		 sql = sql.replace(",,)", ",");
 
		 int col = 1;
 
		 CPreparedStatement pstmt = new CPreparedStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, sql, get_TrxName(), true);
 
		 pstmt.setInt(col++, getAD_Client_ID());
		 pstmt.setString(col++, isActive()?"Y":"N");
		 if (getCreated() != null) pstmt.setTimestamp(col++, getCreated());
		 pstmt.setInt(col++, getCreatedBy());
		 if (getUpdated() != null) pstmt.setTimestamp(col++, getUpdated());
		 pstmt.setInt(col++, getAD_Org_ID());
		 pstmt.setInt(col++, getUpdatedBy());
		 if (getQtyReserved() != null) pstmt.setBigDecimal(col++, getQtyReserved());
		 if (getQtyOrdered() != null) pstmt.setBigDecimal(col++, getQtyOrdered());
		 if (getDateLastInventory() != null) pstmt.setTimestamp(col++, getDateLastInventory());
		 if (getQtyOnHand() != null) pstmt.setBigDecimal(col++, getQtyOnHand());
		 pstmt.setInt(col++, getM_AttributeSetInstance_ID());
		 pstmt.setInt(col++, getM_Locator_ID());
		 pstmt.setInt(col++, getM_Product_ID());
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
