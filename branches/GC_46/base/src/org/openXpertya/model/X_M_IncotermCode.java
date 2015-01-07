/** Modelo Generado - NO CAMBIAR MANUALMENTE - Copyright (C) 2006 FUNDESLE */
package org.openXpertya.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por M_IncotermCode
 *  @author Comunidad de Desarrollo openXpertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2008-01-03 10:26:38.187 */
public class X_M_IncotermCode extends PO
{
/** Constructor estándar */
public X_M_IncotermCode (Properties ctx, int M_IncotermCode_ID, String trxName)
{
super (ctx, M_IncotermCode_ID, trxName);
/** if (M_IncotermCode_ID == 0)
{
setm_incotermcode_id (0);
}
 */
}
/** Load Constructor */
public X_M_IncotermCode (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=1000100 */
public static final int Table_ID=1000100;

/** TableName=M_IncotermCode */
public static final String Table_Name="M_IncotermCode";

protected static KeyNamePair Model = new KeyNamePair(1000100,"M_IncotermCode");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_IncotermCode[").append(getID()).append("]");
return sb.toString();
}
/** Set Validation code.
Validation Code */
public void setCode (BigDecimal Code)
{
set_Value ("Code", Code);
}
/** Get Validation code.
Validation Code */
public BigDecimal getCode() 
{
BigDecimal bd = (BigDecimal)get_Value("Code");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set m_incotermcode_id */
public void setm_incotermcode_id (int m_incotermcode_id)
{
set_ValueNoCheck ("m_incotermcode_id", new Integer(m_incotermcode_id));
}
/** Get m_incotermcode_id */
public int getm_incotermcode_id() 
{
Integer ii = (Integer)get_Value("m_incotermcode_id");
if (ii == null) return 0;
return ii.intValue();
}
}
