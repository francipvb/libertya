/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.openXpertya.util.Env;
import org.openXpertya.util.KeyNamePair;
/** Modelo Generado por C_ExpenseConcepts
 *  @author Comunidad de Desarrollo Libertya Basado en Codigo Original Modificado, Revisado y Optimizado de: Jorg Janke 
 *  @version  - 2017-01-28 15:46:21.269 */
public class X_C_ExpenseConcepts extends org.openXpertya.model.PO
{
private static final long serialVersionUID = 1L;
/** Constructor estándar */
public X_C_ExpenseConcepts (Properties ctx, int C_ExpenseConcepts_ID, String trxName)
{
super (ctx, C_ExpenseConcepts_ID, trxName);
}
/** Load Constructor */
public X_C_ExpenseConcepts (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("C_ExpenseConcepts");

/** TableName=C_ExpenseConcepts */
public static final String Table_Name="C_ExpenseConcepts";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_ExpenseConcepts");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_C_ExpenseConcepts[").append(getID()).append("]");
return sb.toString();
}
/** Set Amount.
Amount in a defined currency */
public void setAmount (BigDecimal Amount)
{
set_Value ("Amount", Amount);
}
/** Get Amount.
Amount in a defined currency */
public BigDecimal getAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount");
if (bd == null) return Env.ZERO;
return bd;
}
public static final int C_CARDSETTLEMENTCONCEPTS_ID_AD_Reference_ID = MReference.getReferenceID("C_CardSettlementConcepts");
/** Set C_Cardsettlementconcepts_ID */
public void setC_Cardsettlementconcepts_ID (int C_Cardsettlementconcepts_ID)
{
set_Value ("C_Cardsettlementconcepts_ID", new Integer(C_Cardsettlementconcepts_ID));
}
/** Get C_Cardsettlementconcepts_ID */
public int getC_Cardsettlementconcepts_ID() 
{
Integer ii = (Integer)get_Value("C_Cardsettlementconcepts_ID");
if (ii == null) return 0;
return ii.intValue();
}
public static final int C_CREDITCARDSETTLEMENT_ID_AD_Reference_ID = MReference.getReferenceID("Settlements (number)");
/** Set Credit Card Settlement */
public void setC_CreditCardSettlement_ID (int C_CreditCardSettlement_ID)
{
set_Value ("C_CreditCardSettlement_ID", new Integer(C_CreditCardSettlement_ID));
}
/** Get Credit Card Settlement */
public int getC_CreditCardSettlement_ID() 
{
Integer ii = (Integer)get_Value("C_CreditCardSettlement_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Expense Concepts ID */
public void setC_ExpenseConcepts_ID (int C_ExpenseConcepts_ID)
{
set_ValueNoCheck ("C_ExpenseConcepts_ID", new Integer(C_ExpenseConcepts_ID));
}
/** Get Expense Concepts ID */
public int getC_ExpenseConcepts_ID() 
{
Integer ii = (Integer)get_Value("C_ExpenseConcepts_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
