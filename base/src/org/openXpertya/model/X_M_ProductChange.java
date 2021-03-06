/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por M_ProductChange
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2019-11-29 20:51:52.449 */
public class X_M_ProductChange extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_M_ProductChange (Properties ctx, int M_ProductChange_ID, String trxName)
{
super (ctx, M_ProductChange_ID, trxName);
/** if (M_ProductChange_ID == 0)
{
setCost (Env.ZERO);
setCostTo (Env.ZERO);
setDateTrx (new Timestamp(System.currentTimeMillis()));	// @#Date@
setDocAction (null);	// CO
setDocStatus (null);	// DR
setDocumentNo (null);
setMaxPriceVariationPerc (Env.ZERO);
setM_Locator_ID (0);	// @SQL=SELECT m_locator_id FROM m_locator where m_warehouse_id = @M_Warehouse_ID@ order by isdefault desc limit 1
setM_Locator_To_ID (0);	// @SQL=SELECT m_locator_id FROM m_locator where m_warehouse_id = @M_Warehouse_ID@ order by isdefault desc limit 1
setM_ProductChange_ID (0);
setM_Product_ID (0);
setM_Product_To_ID (0);
setM_Warehouse_ID (0);
setProcessed (false);
setProductPrice (Env.ZERO);
setProductQty (Env.ZERO);
setProductToPrice (Env.ZERO);
}
 */
}
/** Load Constructor */
public X_M_ProductChange (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("M_ProductChange");

/** TableName=M_ProductChange */
public static final String Table_Name="M_ProductChange";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"M_ProductChange");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_ProductChange[").append(getID()).append("]");
return sb.toString();
}
/** Set Cost.
Cost information */
public void setCost (BigDecimal Cost)
{
if (Cost == null) throw new IllegalArgumentException ("Cost is mandatory");
set_Value ("Cost", Cost);
}
/** Get Cost.
Cost information */
public BigDecimal getCost() 
{
BigDecimal bd = (BigDecimal)get_Value("Cost");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Cost To */
public void setCostTo (BigDecimal CostTo)
{
if (CostTo == null) throw new IllegalArgumentException ("CostTo is mandatory");
set_Value ("CostTo", CostTo);
}
/** Get Cost To */
public BigDecimal getCostTo() 
{
BigDecimal bd = (BigDecimal)get_Value("CostTo");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Transaction Date.
Transaction Date */
public void setDateTrx (Timestamp DateTrx)
{
if (DateTrx == null) throw new IllegalArgumentException ("DateTrx is mandatory");
set_Value ("DateTrx", DateTrx);
}
/** Get Transaction Date.
Transaction Date */
public Timestamp getDateTrx() 
{
return (Timestamp)get_Value("DateTrx");
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,255);
}
set_Value ("Description", Description);
}
/** Get Description.
Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
public static final int DOCACTION_AD_Reference_ID = MReference.getReferenceID("_Document Action");
/** Approve = AP */
public static final String DOCACTION_Approve = "AP";
/** Close = CL */
public static final String DOCACTION_Close = "CL";
/** Prepare = PR */
public static final String DOCACTION_Prepare = "PR";
/** Invalidate = IN */
public static final String DOCACTION_Invalidate = "IN";
/** Complete = CO */
public static final String DOCACTION_Complete = "CO";
/** <None> = -- */
public static final String DOCACTION_None = "--";
/** Reverse - Correct = RC */
public static final String DOCACTION_Reverse_Correct = "RC";
/** Reject = RJ */
public static final String DOCACTION_Reject = "RJ";
/** Reverse - Accrual = RA */
public static final String DOCACTION_Reverse_Accrual = "RA";
/** Wait Complete = WC */
public static final String DOCACTION_WaitComplete = "WC";
/** Unlock = XL */
public static final String DOCACTION_Unlock = "XL";
/** Re-activate = RE */
public static final String DOCACTION_Re_Activate = "RE";
/** Post = PO */
public static final String DOCACTION_Post = "PO";
/** Void = VO */
public static final String DOCACTION_Void = "VO";
/** Set Document Action.
The targeted status of the document */
public void setDocAction (String DocAction)
{
if (DocAction.equals("AP") || DocAction.equals("CL") || DocAction.equals("PR") || DocAction.equals("IN") || DocAction.equals("CO") || DocAction.equals("--") || DocAction.equals("RC") || DocAction.equals("RJ") || DocAction.equals("RA") || DocAction.equals("WC") || DocAction.equals("XL") || DocAction.equals("RE") || DocAction.equals("PO") || DocAction.equals("VO") || ( refContainsValue("CORE-AD_Reference-135", DocAction) ) );
 else throw new IllegalArgumentException ("DocAction Invalid value: " + DocAction + ".  Valid: " +  refValidOptions("CORE-AD_Reference-135") );
if (DocAction == null) throw new IllegalArgumentException ("DocAction is mandatory");
if (DocAction.length() > 2)
{
log.warning("Length > 2 - truncated");
DocAction = DocAction.substring(0,2);
}
set_Value ("DocAction", DocAction);
}
/** Get Document Action.
The targeted status of the document */
public String getDocAction() 
{
return (String)get_Value("DocAction");
}
public static final int DOCSTATUS_AD_Reference_ID = MReference.getReferenceID("_Document Status");
/** Voided = VO */
public static final String DOCSTATUS_Voided = "VO";
/** Not Approved = NA */
public static final String DOCSTATUS_NotApproved = "NA";
/** In Progress = IP */
public static final String DOCSTATUS_InProgress = "IP";
/** Completed = CO */
public static final String DOCSTATUS_Completed = "CO";
/** Approved = AP */
public static final String DOCSTATUS_Approved = "AP";
/** Closed = CL */
public static final String DOCSTATUS_Closed = "CL";
/** Waiting Confirmation = WC */
public static final String DOCSTATUS_WaitingConfirmation = "WC";
/** Waiting Payment = WP */
public static final String DOCSTATUS_WaitingPayment = "WP";
/** Unknown = ?? */
public static final String DOCSTATUS_Unknown = "??";
/** Drafted = DR */
public static final String DOCSTATUS_Drafted = "DR";
/** Invalid = IN */
public static final String DOCSTATUS_Invalid = "IN";
/** Reversed = RE */
public static final String DOCSTATUS_Reversed = "RE";
/** Set Document Status.
The current status of the document */
public void setDocStatus (String DocStatus)
{
if (DocStatus.equals("VO") || DocStatus.equals("NA") || DocStatus.equals("IP") || DocStatus.equals("CO") || DocStatus.equals("AP") || DocStatus.equals("CL") || DocStatus.equals("WC") || DocStatus.equals("WP") || DocStatus.equals("??") || DocStatus.equals("DR") || DocStatus.equals("IN") || DocStatus.equals("RE") || ( refContainsValue("CORE-AD_Reference-131", DocStatus) ) );
 else throw new IllegalArgumentException ("DocStatus Invalid value: " + DocStatus + ".  Valid: " +  refValidOptions("CORE-AD_Reference-131") );
if (DocStatus == null) throw new IllegalArgumentException ("DocStatus is mandatory");
if (DocStatus.length() > 2)
{
log.warning("Length > 2 - truncated");
DocStatus = DocStatus.substring(0,2);
}
set_Value ("DocStatus", DocStatus);
}
/** Get Document Status.
The current status of the document */
public String getDocStatus() 
{
return (String)get_Value("DocStatus");
}
/** Set Document No.
Document sequence NUMERIC of the document */
public void setDocumentNo (String DocumentNo)
{
if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory");
if (DocumentNo.length() > 30)
{
log.warning("Length > 30 - truncated");
DocumentNo = DocumentNo.substring(0,30);
}
set_Value ("DocumentNo", DocumentNo);
}
/** Get Document No.
Document sequence NUMERIC of the document */
public String getDocumentNo() 
{
return (String)get_Value("DocumentNo");
}
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(getID(), getDocumentNo());
}
/** Set Attribute Set Instance.
Product Attribute Set Instance */
public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
{
if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
 else 
set_Value ("M_AttributeSetInstance_ID", new Integer(M_AttributeSetInstance_ID));
}
/** Get Attribute Set Instance.
Product Attribute Set Instance */
public int getM_AttributeSetInstance_ID() 
{
Integer ii = (Integer)get_Value("M_AttributeSetInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Attribute Set Instance To.
Target Product Attribute Set Instance */
public void setM_AttributeSetInstanceTo_ID (int M_AttributeSetInstanceTo_ID)
{
if (M_AttributeSetInstanceTo_ID <= 0) set_Value ("M_AttributeSetInstanceTo_ID", null);
 else 
set_Value ("M_AttributeSetInstanceTo_ID", new Integer(M_AttributeSetInstanceTo_ID));
}
/** Get Attribute Set Instance To.
Target Product Attribute Set Instance */
public int getM_AttributeSetInstanceTo_ID() 
{
Integer ii = (Integer)get_Value("M_AttributeSetInstanceTo_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Max Price Variation Percentage */
public void setMaxPriceVariationPerc (BigDecimal MaxPriceVariationPerc)
{
if (MaxPriceVariationPerc == null) throw new IllegalArgumentException ("MaxPriceVariationPerc is mandatory");
set_Value ("MaxPriceVariationPerc", MaxPriceVariationPerc);
}
/** Get Max Price Variation Percentage */
public BigDecimal getMaxPriceVariationPerc() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxPriceVariationPerc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Phys.Inventory.
Parameters for a Physical Inventory */
public void setM_Inventory_ID (int M_Inventory_ID)
{
if (M_Inventory_ID <= 0) set_Value ("M_Inventory_ID", null);
 else 
set_Value ("M_Inventory_ID", new Integer(M_Inventory_ID));
}
/** Get Phys.Inventory.
Parameters for a Physical Inventory */
public int getM_Inventory_ID() 
{
Integer ii = (Integer)get_Value("M_Inventory_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Locator.
Warehouse Locator */
public void setM_Locator_ID (int M_Locator_ID)
{
set_Value ("M_Locator_ID", new Integer(M_Locator_ID));
}
/** Get Locator.
Warehouse Locator */
public int getM_Locator_ID() 
{
Integer ii = (Integer)get_Value("M_Locator_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Target Locator.
Target Locator */
public void setM_Locator_To_ID (int M_Locator_To_ID)
{
set_Value ("M_Locator_To_ID", new Integer(M_Locator_To_ID));
}
/** Get Target Locator.
Target Locator */
public int getM_Locator_To_ID() 
{
Integer ii = (Integer)get_Value("M_Locator_To_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product Change.
Product Change */
public void setM_ProductChange_ID (int M_ProductChange_ID)
{
set_ValueNoCheck ("M_ProductChange_ID", new Integer(M_ProductChange_ID));
}
/** Get Product Change.
Product Change */
public int getM_ProductChange_ID() 
{
Integer ii = (Integer)get_Value("M_ProductChange_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product.
Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
set_Value ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int M_PRODUCT_TO_ID_AD_Reference_ID = MReference.getReferenceID("C_Product");
/** Set To Product.
Product to be converted to (must have UOM Conversion defined to From Product) */
public void setM_Product_To_ID (int M_Product_To_ID)
{
set_Value ("M_Product_To_ID", new Integer(M_Product_To_ID));
}
/** Get To Product.
Product to be converted to (must have UOM Conversion defined to From Product) */
public int getM_Product_To_ID() 
{
Integer ii = (Integer)get_Value("M_Product_To_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Warehouse.
Storage Warehouse and Service Point */
public void setM_Warehouse_ID (int M_Warehouse_ID)
{
set_Value ("M_Warehouse_ID", new Integer(M_Warehouse_ID));
}
/** Get Warehouse.
Storage Warehouse and Service Point */
public int getM_Warehouse_ID() 
{
Integer ii = (Integer)get_Value("M_Warehouse_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Processed.
The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value ("Processed", new Boolean(Processed));
}
/** Get Processed.
The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Product Price.
Source ProductPrice */
public void setProductPrice (BigDecimal ProductPrice)
{
if (ProductPrice == null) throw new IllegalArgumentException ("ProductPrice is mandatory");
set_Value ("ProductPrice", ProductPrice);
}
/** Get Product Price.
Source ProductPrice */
public BigDecimal getProductPrice() 
{
BigDecimal bd = (BigDecimal)get_Value("ProductPrice");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Product Quantity.
Product Quantity */
public void setProductQty (BigDecimal ProductQty)
{
if (ProductQty == null) throw new IllegalArgumentException ("ProductQty is mandatory");
set_Value ("ProductQty", ProductQty);
}
/** Get Product Quantity.
Product Quantity */
public BigDecimal getProductQty() 
{
BigDecimal bd = (BigDecimal)get_Value("ProductQty");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Product To Price.
Destiny Product Price */
public void setProductToPrice (BigDecimal ProductToPrice)
{
if (ProductToPrice == null) throw new IllegalArgumentException ("ProductToPrice is mandatory");
set_Value ("ProductToPrice", ProductToPrice);
}
/** Get Product To Price.
Destiny Product Price */
public BigDecimal getProductToPrice() 
{
BigDecimal bd = (BigDecimal)get_Value("ProductToPrice");
if (bd == null) return Env.ZERO;
return bd;
}
public static final int VOID_INVENTORY_ID_AD_Reference_ID = MReference.getReferenceID("M_Inventory");
/** Set Void Inventory.
Void Inventory */
public void setVoid_Inventory_ID (int Void_Inventory_ID)
{
if (Void_Inventory_ID <= 0) set_Value ("Void_Inventory_ID", null);
 else 
set_Value ("Void_Inventory_ID", new Integer(Void_Inventory_ID));
}
/** Get Void Inventory.
Void Inventory */
public int getVoid_Inventory_ID() 
{
Integer ii = (Integer)get_Value("Void_Inventory_ID");
if (ii == null) return 0;
return ii.intValue();
}

public boolean insertDirect() 
{
 
try 
{
 
 		 String sql = " INSERT INTO M_ProductChange(IsActive,Created,CreatedBy,Updated,UpdatedBy,Description,ProductQty,Processed,DocStatus,DocAction,AD_Client_ID,AD_Org_ID,M_ProductChange_ID,DocumentNo,DateTrx,M_Locator_To_ID,M_Warehouse_ID,M_Inventory_ID,Void_Inventory_ID,M_Locator_ID,M_AttributeSetInstanceTo_ID,M_AttributeSetInstance_ID,M_Product_ID,M_Product_To_ID,MaxPriceVariationPerc,ProductPrice,ProductToPrice,CostTo,Cost," + getAdditionalParamNames() + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + getAdditionalParamMarks() + ") ";

		 if (getCreated() == null) sql = sql.replaceFirst("Created,","").replaceFirst("\\?,", "");
 		 if (getUpdated() == null) sql = sql.replaceFirst("Updated,","").replaceFirst("\\?,", "");
 		 if (getDescription() == null) sql = sql.replaceFirst("Description,","").replaceFirst("\\?,", "");
 		 if (getProductQty() == null) sql = sql.replaceFirst("ProductQty,","").replaceFirst("\\?,", "");
 		 if (getDocStatus() == null) sql = sql.replaceFirst("DocStatus,","").replaceFirst("\\?,", "");
 		 if (getDocAction() == null) sql = sql.replaceFirst("DocAction,","").replaceFirst("\\?,", "");
 		 if (getDocumentNo() == null) sql = sql.replaceFirst("DocumentNo,","").replaceFirst("\\?,", "");
 		 if (getDateTrx() == null) sql = sql.replaceFirst("DateTrx,","").replaceFirst("\\?,", "");
 		 if (getM_Inventory_ID() == 0) sql = sql.replaceFirst("M_Inventory_ID,","").replaceFirst("\\?,", "");
 		 if (getVoid_Inventory_ID() == 0) sql = sql.replaceFirst("Void_Inventory_ID,","").replaceFirst("\\?,", "");
 		 if (getM_AttributeSetInstanceTo_ID() == 0) sql = sql.replaceFirst("M_AttributeSetInstanceTo_ID,","").replaceFirst("\\?,", "");
 		 if (getM_AttributeSetInstance_ID() == 0) sql = sql.replaceFirst("M_AttributeSetInstance_ID,","").replaceFirst("\\?,", "");
 		 if (getMaxPriceVariationPerc() == null) sql = sql.replaceFirst("MaxPriceVariationPerc,","").replaceFirst("\\?,", "");
 		 if (getProductPrice() == null) sql = sql.replaceFirst("ProductPrice,","").replaceFirst("\\?,", "");
 		 if (getProductToPrice() == null) sql = sql.replaceFirst("ProductToPrice,","").replaceFirst("\\?,", "");
 		 if (getCostTo() == null) sql = sql.replaceFirst("CostTo,","").replaceFirst("\\?,", "");
 		 if (getCost() == null) sql = sql.replaceFirst("Cost,","").replaceFirst("\\?,", "");
 		 skipAdditionalNullValues(sql);
 

 		 sql = sql.replace(",)", ")");
 
		 sql = sql.replace(",,)", ",");
 
		 int col = 1;
 
		 CPreparedStatement pstmt = new CPreparedStatement( ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, sql, get_TrxName(), true);
 
		 pstmt.setString(col++, isActive()?"Y":"N");
		 if (getCreated() != null) pstmt.setTimestamp(col++, getCreated());
		 pstmt.setInt(col++, getCreatedBy());
		 if (getUpdated() != null) pstmt.setTimestamp(col++, getUpdated());
		 pstmt.setInt(col++, getUpdatedBy());
		 if (getDescription() != null) pstmt.setString(col++, getDescription());
		 if (getProductQty() != null) pstmt.setBigDecimal(col++, getProductQty());
		 pstmt.setString(col++, isProcessed()?"Y":"N");
		 if (getDocStatus() != null) pstmt.setString(col++, getDocStatus());
		 if (getDocAction() != null) pstmt.setString(col++, getDocAction());
		 pstmt.setInt(col++, getAD_Client_ID());
		 pstmt.setInt(col++, getAD_Org_ID());
		 pstmt.setInt(col++, getM_ProductChange_ID());
		 if (getDocumentNo() != null) pstmt.setString(col++, getDocumentNo());
		 if (getDateTrx() != null) pstmt.setTimestamp(col++, getDateTrx());
		 pstmt.setInt(col++, getM_Locator_To_ID());
		 pstmt.setInt(col++, getM_Warehouse_ID());
		 if (getM_Inventory_ID() != 0) pstmt.setInt(col++, getM_Inventory_ID());
		 if (getVoid_Inventory_ID() != 0) pstmt.setInt(col++, getVoid_Inventory_ID());
		 pstmt.setInt(col++, getM_Locator_ID());
		 if (getM_AttributeSetInstanceTo_ID() != 0) pstmt.setInt(col++, getM_AttributeSetInstanceTo_ID());
		 if (getM_AttributeSetInstance_ID() != 0) pstmt.setInt(col++, getM_AttributeSetInstance_ID());
		 pstmt.setInt(col++, getM_Product_ID());
		 pstmt.setInt(col++, getM_Product_To_ID());
		 if (getMaxPriceVariationPerc() != null) pstmt.setBigDecimal(col++, getMaxPriceVariationPerc());
		 if (getProductPrice() != null) pstmt.setBigDecimal(col++, getProductPrice());
		 if (getProductToPrice() != null) pstmt.setBigDecimal(col++, getProductToPrice());
		 if (getCostTo() != null) pstmt.setBigDecimal(col++, getCostTo());
		 if (getCost() != null) pstmt.setBigDecimal(col++, getCost());
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
