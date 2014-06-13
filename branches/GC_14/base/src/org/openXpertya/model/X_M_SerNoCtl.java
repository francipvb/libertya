/** Modelo Generado - NO CAMBIAR MANUALMENTE - Copyright (C) 2006 FUNDESLE */
package org.openXpertya.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por M_SerNoCtl
 *  @author Comunidad de Desarrollo openXpertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2008-01-03 10:26:39.765 */
public class X_M_SerNoCtl extends PO
{
/** Constructor estándar */
public X_M_SerNoCtl (Properties ctx, int M_SerNoCtl_ID, String trxName)
{
super (ctx, M_SerNoCtl_ID, trxName);
/** if (M_SerNoCtl_ID == 0)
{
setCurrentNext (0);	// 100
setIncrementNo (0);	// 1
setM_SerNoCtl_ID (0);
setName (null);
setStartNo (0);	// 100
}
 */
}
/** Load Constructor */
public X_M_SerNoCtl (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=555 */
public static final int Table_ID=555;

/** TableName=M_SerNoCtl */
public static final String Table_Name="M_SerNoCtl";

protected static KeyNamePair Model = new KeyNamePair(555,"M_SerNoCtl");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_SerNoCtl[").append(getID()).append("]");
return sb.toString();
}
/** Set Current Next.
The next number to be used */
public void setCurrentNext (int CurrentNext)
{
set_Value ("CurrentNext", new Integer(CurrentNext));
}
/** Get Current Next.
The next number to be used */
public int getCurrentNext() 
{
Integer ii = (Integer)get_Value("CurrentNext");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Increment.
The number to increment the last document number by */
public void setIncrementNo (int IncrementNo)
{
set_Value ("IncrementNo", new Integer(IncrementNo));
}
/** Get Increment.
The number to increment the last document number by */
public int getIncrementNo() 
{
Integer ii = (Integer)get_Value("IncrementNo");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Serial No Control.
Product Serial Number Control */
public void setM_SerNoCtl_ID (int M_SerNoCtl_ID)
{
set_ValueNoCheck ("M_SerNoCtl_ID", new Integer(M_SerNoCtl_ID));
}
/** Get Serial No Control.
Product Serial Number Control */
public int getM_SerNoCtl_ID() 
{
Integer ii = (Integer)get_Value("M_SerNoCtl_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Name.
Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory");
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name.
Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(getID(), getName());
}
/** Set Prefix.
Prefix before the sequence number */
public void setPrefix (String Prefix)
{
if (Prefix != null && Prefix.length() > 10)
{
log.warning("Length > 10 - truncated");
Prefix = Prefix.substring(0,9);
}
set_Value ("Prefix", Prefix);
}
/** Get Prefix.
Prefix before the sequence number */
public String getPrefix() 
{
return (String)get_Value("Prefix");
}
/** Set Start No.
Starting number/position */
public void setStartNo (int StartNo)
{
set_Value ("StartNo", new Integer(StartNo));
}
/** Get Start No.
Starting number/position */
public int getStartNo() 
{
Integer ii = (Integer)get_Value("StartNo");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Suffix.
Suffix after the number */
public void setSuffix (String Suffix)
{
if (Suffix != null && Suffix.length() > 10)
{
log.warning("Length > 10 - truncated");
Suffix = Suffix.substring(0,9);
}
set_Value ("Suffix", Suffix);
}
/** Get Suffix.
Suffix after the number */
public String getSuffix() 
{
return (String)get_Value("Suffix");
}
}
