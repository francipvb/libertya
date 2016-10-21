/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.openXpertya.util.KeyNamePair;
/** Modelo Generado por C_BankPaymentStatusAssociation
 *  @author Comunidad de Desarrollo Libertya
 *  Basado en Codigo Original Modificado, Revisado y Optimizado de: Jorg Janke 
 *  @version  - 2016-10-13 16:34:18.12 */
public class X_C_BankPaymentStatusAssociation extends org.openXpertya.model.PO
{
private static final long serialVersionUID = 1L;
/** Constructor estándar */
public X_C_BankPaymentStatusAssociation (Properties ctx, int C_BankPaymentStatusAssociation_ID, String trxName)
{
super (ctx, C_BankPaymentStatusAssociation_ID, trxName);
}
/** Load Constructor */
public X_C_BankPaymentStatusAssociation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("C_BankPaymentStatusAssociation");

/** TableName=C_BankPaymentStatusAssociation */
public static final String Table_Name="C_BankPaymentStatusAssociation";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_BankPaymentStatusAssociation");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_C_BankPaymentStatusAssociation[").append(getID()).append("]");
return sb.toString();
}
public static final int C_BANK_ID_AD_Reference_ID = MReference.getReferenceID("C_Bank");
/** Set Bank.
Bank */
public void setC_Bank_ID (int C_Bank_ID)
{
set_Value ("C_Bank_ID", new Integer(C_Bank_ID));
}
/** Get Bank.
Bank */
public int getC_Bank_ID() 
{
Integer ii = (Integer)get_Value("C_Bank_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_BankPaymentStatusAssociation_ID */
public void setC_BankPaymentStatusAssociation_ID (int C_BankPaymentStatusAssociation_ID)
{
set_ValueNoCheck ("C_BankPaymentStatusAssociation_ID", new Integer(C_BankPaymentStatusAssociation_ID));
}
/** Get C_BankPaymentStatusAssociation_ID */
public int getC_BankPaymentStatusAssociation_ID() 
{
Integer ii = (Integer)get_Value("C_BankPaymentStatusAssociation_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int C_BANKPAYMENTSTATUS_ID_AD_Reference_ID = MReference.getReferenceID("C_BankPaymentStatus");
/** Set Bank Payment Status ID */
public void setC_BankPaymentStatus_ID (int C_BankPaymentStatus_ID)
{
set_Value ("C_BankPaymentStatus_ID", new Integer(C_BankPaymentStatus_ID));
}
/** Get Bank Payment Status ID */
public int getC_BankPaymentStatus_ID() 
{
Integer ii = (Integer)get_Value("C_BankPaymentStatus_ID");
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
Name = Name.substring(0,60);
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
/** Set Search Key.
Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory");
if (Value.length() > 40)
{
log.warning("Length > 40 - truncated");
Value = Value.substring(0,40);
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
