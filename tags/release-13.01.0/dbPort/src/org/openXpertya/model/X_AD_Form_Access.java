/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por AD_Form_Access
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2010-05-19 11:07:30.02 */
public class X_AD_Form_Access extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_AD_Form_Access (Properties ctx, int AD_Form_Access_ID, String trxName)
{
super (ctx, AD_Form_Access_ID, trxName);
/** if (AD_Form_Access_ID == 0)
{
setAD_Form_ID (0);
setAD_Role_ID (0);
setIsReadWrite (false);
}
 */
}
/** Load Constructor */
public X_AD_Form_Access (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("AD_Form_Access");

/** TableName=AD_Form_Access */
public static final String Table_Name="AD_Form_Access";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"AD_Form_Access");
protected static BigDecimal AccessLevel = new BigDecimal(6);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_AD_Form_Access[").append(getID()).append("]");
return sb.toString();
}
/** Set AD_ComponentObjectUID */
public void setAD_ComponentObjectUID (String AD_ComponentObjectUID)
{
if (AD_ComponentObjectUID != null && AD_ComponentObjectUID.length() > 100)
{
log.warning("Length > 100 - truncated");
AD_ComponentObjectUID = AD_ComponentObjectUID.substring(0,100);
}
set_Value ("AD_ComponentObjectUID", AD_ComponentObjectUID);
}
/** Get AD_ComponentObjectUID */
public String getAD_ComponentObjectUID() 
{
return (String)get_Value("AD_ComponentObjectUID");
}
/** Set Component Version Identifier */
public void setAD_ComponentVersion_ID (int AD_ComponentVersion_ID)
{
if (AD_ComponentVersion_ID <= 0) set_Value ("AD_ComponentVersion_ID", null);
 else 
set_Value ("AD_ComponentVersion_ID", new Integer(AD_ComponentVersion_ID));
}
/** Get Component Version Identifier */
public int getAD_ComponentVersion_ID() 
{
Integer ii = (Integer)get_Value("AD_ComponentVersion_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Special Form.
Special Form */
public void setAD_Form_ID (int AD_Form_ID)
{
set_ValueNoCheck ("AD_Form_ID", new Integer(AD_Form_ID));
}
/** Get Special Form.
Special Form */
public int getAD_Form_ID() 
{
Integer ii = (Integer)get_Value("AD_Form_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Role.
Responsibility Role */
public void setAD_Role_ID (int AD_Role_ID)
{
set_ValueNoCheck ("AD_Role_ID", new Integer(AD_Role_ID));
}
/** Get Role.
Responsibility Role */
public int getAD_Role_ID() 
{
Integer ii = (Integer)get_Value("AD_Role_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Read Write.
Field is read / write */
public void setIsReadWrite (boolean IsReadWrite)
{
set_Value ("IsReadWrite", new Boolean(IsReadWrite));
}
/** Get Read Write.
Field is read / write */
public boolean isReadWrite() 
{
Object oo = get_Value("IsReadWrite");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
