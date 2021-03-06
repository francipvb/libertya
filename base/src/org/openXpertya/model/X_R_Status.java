/** Modelo Generado - NO CAMBIAR MANUALMENTE - Copyright (C) 2006 FUNDESLE */
package org.openXpertya.model;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por R_Status
 *  @author Comunidad de Desarrollo openXpertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2008-01-03 10:26:41.39 */
public class X_R_Status extends PO
{
/** Constructor estándar */
public X_R_Status (Properties ctx, int R_Status_ID, String trxName)
{
super (ctx, R_Status_ID, trxName);
/** if (R_Status_ID == 0)
{
setIsClosed (false);	// N
setIsDefault (false);
setIsFinalClose (false);	// N
setIsOpen (false);
setIsWebCanUpdate (false);
setName (null);
setR_Status_ID (0);
setValue (null);
}
 */
}
/** Load Constructor */
public X_R_Status (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=776 */
public static final int Table_ID=776;

/** TableName=R_Status */
public static final String Table_Name="R_Status";

protected static KeyNamePair Model = new KeyNamePair(776,"R_Status");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_R_Status[").append(getID()).append("]");
return sb.toString();
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
/** Set Comment/Help.
Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
/** Set Closed Status.
The status is closed */
public void setIsClosed (boolean IsClosed)
{
set_Value ("IsClosed", new Boolean(IsClosed));
}
/** Get Closed Status.
The status is closed */
public boolean isClosed() 
{
Object oo = get_Value("IsClosed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Default.
Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", new Boolean(IsDefault));
}
/** Get Default.
Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Final Close.
Entries with Final Close cannot be re-opened */
public void setIsFinalClose (boolean IsFinalClose)
{
set_Value ("IsFinalClose", new Boolean(IsFinalClose));
}
/** Get Final Close.
Entries with Final Close cannot be re-opened */
public boolean isFinalClose() 
{
Object oo = get_Value("IsFinalClose");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Open Status.
The status is closed */
public void setIsOpen (boolean IsOpen)
{
set_Value ("IsOpen", new Boolean(IsOpen));
}
/** Get Open Status.
The status is closed */
public boolean isOpen() 
{
Object oo = get_Value("IsOpen");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Web Can Update.
Entry can be updated from the Web */
public void setIsWebCanUpdate (boolean IsWebCanUpdate)
{
set_Value ("IsWebCanUpdate", new Boolean(IsWebCanUpdate));
}
/** Get Web Can Update.
Entry can be updated from the Web */
public boolean isWebCanUpdate() 
{
Object oo = get_Value("IsWebCanUpdate");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
public static final int NEXT_STATUS_ID_AD_Reference_ID=345;
/** Set Next Status.
Move to next status automatically after timeout */
public void setNext_Status_ID (int Next_Status_ID)
{
if (Next_Status_ID <= 0) set_Value ("Next_Status_ID", null);
 else 
set_Value ("Next_Status_ID", new Integer(Next_Status_ID));
}
/** Get Next Status.
Move to next status automatically after timeout */
public int getNext_Status_ID() 
{
Integer ii = (Integer)get_Value("Next_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Status.
Request Status */
public void setR_Status_ID (int R_Status_ID)
{
set_ValueNoCheck ("R_Status_ID", new Integer(R_Status_ID));
}
/** Get Status.
Request Status */
public int getR_Status_ID() 
{
Integer ii = (Integer)get_Value("R_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Timeout in Days.
Timeout in Days to change Status automatically */
public void setTimeoutDays (int TimeoutDays)
{
set_Value ("TimeoutDays", new Integer(TimeoutDays));
}
/** Get Timeout in Days.
Timeout in Days to change Status automatically */
public int getTimeoutDays() 
{
Integer ii = (Integer)get_Value("TimeoutDays");
if (ii == null) return 0;
return ii.intValue();
}
public static final int UPDATE_STATUS_ID_AD_Reference_ID=345;
/** Set Update Status.
Automatically change the status after entry from web */
public void setUpdate_Status_ID (int Update_Status_ID)
{
if (Update_Status_ID <= 0) set_Value ("Update_Status_ID", null);
 else 
set_Value ("Update_Status_ID", new Integer(Update_Status_ID));
}
/** Get Update Status.
Automatically change the status after entry from web */
public int getUpdate_Status_ID() 
{
Integer ii = (Integer)get_Value("Update_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory");
if (Value.length() > 40)
{
log.warning("Length > 40 - truncated");
Value = Value.substring(0,39);
}
set_Value ("Value", Value);
}
/** Get Search Key.
Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
