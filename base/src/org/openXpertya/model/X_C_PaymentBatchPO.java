/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por C_PaymentBatchPO
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2016-11-01 17:59:46.24 */
public class X_C_PaymentBatchPO extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_C_PaymentBatchPO (Properties ctx, int C_PaymentBatchPO_ID, String trxName)
{
super (ctx, C_PaymentBatchPO_ID, trxName);
/** if (C_PaymentBatchPO_ID == 0)
{
setBatchDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
setC_DoctypeAllocTarget_ID (0);
setC_DocType_ID (0);
setC_PaymentBatchPO_ID (0);
setDocAction (null);	// CO
setDocStatus (null);	// DR
setDocumentNo (null);
setGrandTotal (Env.ZERO);
setPaymentDateRule (null);
setProcessed (false);
}
 */
}
/** Load Constructor */
public X_C_PaymentBatchPO (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("C_PaymentBatchPO");

/** TableName=C_PaymentBatchPO */
public static final String Table_Name="C_PaymentBatchPO";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_PaymentBatchPO");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_C_PaymentBatchPO[").append(getID()).append("]");
return sb.toString();
}
/** Set AddDays */
public void setAddDays (int AddDays)
{
set_Value ("AddDays", new Integer(AddDays));
}
/** Get AddDays */
public int getAddDays() 
{
Integer ii = (Integer)get_Value("AddDays");
if (ii == null) return 0;
return ii.intValue();
}
/** Set BatchDate */
public void setBatchDate (Timestamp BatchDate)
{
if (BatchDate == null) throw new IllegalArgumentException ("BatchDate is mandatory");
set_Value ("BatchDate", BatchDate);
}
/** Get BatchDate */
public Timestamp getBatchDate() 
{
return (Timestamp)get_Value("BatchDate");
}
public static final int C_DOCTYPEALLOCTARGET_ID_AD_Reference_ID = MReference.getReferenceID("C_DocType");
/** Set C_DoctypeAllocTarget_ID */
public void setC_DoctypeAllocTarget_ID (int C_DoctypeAllocTarget_ID)
{
set_Value ("C_DoctypeAllocTarget_ID", new Integer(C_DoctypeAllocTarget_ID));
}
/** Get C_DoctypeAllocTarget_ID */
public int getC_DoctypeAllocTarget_ID() 
{
Integer ii = (Integer)get_Value("C_DoctypeAllocTarget_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Document Type.
Document type or rules */
public void setC_DocType_ID (int C_DocType_ID)
{
set_Value ("C_DocType_ID", new Integer(C_DocType_ID));
}
/** Get Document Type.
Document type or rules */
public int getC_DocType_ID() 
{
Integer ii = (Integer)get_Value("C_DocType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_PaymentBatchPO_ID */
public void setC_PaymentBatchPO_ID (int C_PaymentBatchPO_ID)
{
set_ValueNoCheck ("C_PaymentBatchPO_ID", new Integer(C_PaymentBatchPO_ID));
}
/** Get C_PaymentBatchPO_ID */
public int getC_PaymentBatchPO_ID() 
{
Integer ii = (Integer)get_Value("C_PaymentBatchPO_ID");
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
if (DocAction.equals("AP") || DocAction.equals("CL") || DocAction.equals("PR") || DocAction.equals("IN") || DocAction.equals("CO") || DocAction.equals("--") || DocAction.equals("RC") || DocAction.equals("RJ") || DocAction.equals("RA") || DocAction.equals("WC") || DocAction.equals("XL") || DocAction.equals("RE") || DocAction.equals("PO") || DocAction.equals("VO"));
 else throw new IllegalArgumentException ("DocAction Invalid value - Reference = DOCACTION_AD_Reference_ID - AP - CL - PR - IN - CO - -- - RC - RJ - RA - WC - XL - RE - PO - VO");
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
if (DocStatus.equals("VO") || DocStatus.equals("NA") || DocStatus.equals("IP") || DocStatus.equals("CO") || DocStatus.equals("AP") || DocStatus.equals("CL") || DocStatus.equals("WC") || DocStatus.equals("WP") || DocStatus.equals("??") || DocStatus.equals("DR") || DocStatus.equals("IN") || DocStatus.equals("RE"));
 else throw new IllegalArgumentException ("DocStatus Invalid value - Reference = DOCSTATUS_AD_Reference_ID - VO - NA - IP - CO - AP - CL - WC - WP - ?? - DR - IN - RE");
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
/** Set GenerateElectronicPayments */
public void setGenerateElectronicPayments (String GenerateElectronicPayments)
{
if (GenerateElectronicPayments != null && GenerateElectronicPayments.length() > 1)
{
log.warning("Length > 1 - truncated");
GenerateElectronicPayments = GenerateElectronicPayments.substring(0,1);
}
set_Value ("GenerateElectronicPayments", GenerateElectronicPayments);
}
/** Get GenerateElectronicPayments */
public String getGenerateElectronicPayments() 
{
return (String)get_Value("GenerateElectronicPayments");
}
/** Set GeneratePaymentProposal */
public void setGeneratePaymentProposal (String GeneratePaymentProposal)
{
if (GeneratePaymentProposal != null && GeneratePaymentProposal.length() > 1)
{
log.warning("Length > 1 - truncated");
GeneratePaymentProposal = GeneratePaymentProposal.substring(0,1);
}
set_Value ("GeneratePaymentProposal", GeneratePaymentProposal);
}
/** Get GeneratePaymentProposal */
public String getGeneratePaymentProposal() 
{
return (String)get_Value("GeneratePaymentProposal");
}
/** Set Grand Total.
Total amount of document */
public void setGrandTotal (BigDecimal GrandTotal)
{
if (GrandTotal == null) throw new IllegalArgumentException ("GrandTotal is mandatory");
set_Value ("GrandTotal", GrandTotal);
}
/** Get Grand Total.
Total amount of document */
public BigDecimal getGrandTotal() 
{
BigDecimal bd = (BigDecimal)get_Value("GrandTotal");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set PaymentDate */
public void setPaymentDate (Timestamp PaymentDate)
{
set_Value ("PaymentDate", PaymentDate);
}
/** Get PaymentDate */
public Timestamp getPaymentDate() 
{
return (Timestamp)get_Value("PaymentDate");
}
public static final int PAYMENTDATERULE_AD_Reference_ID = MReference.getReferenceID("Payment Date Rule");
/** Last Due Date = U */
public static final String PAYMENTDATERULE_LastDueDate = "U";
/** Fixed Date = F */
public static final String PAYMENTDATERULE_FixedDate = "F";
/** Average Date = P */
public static final String PAYMENTDATERULE_AverageDate = "P";
/** Set PaymentDateRule */
public void setPaymentDateRule (String PaymentDateRule)
{
if (PaymentDateRule.equals("U") || PaymentDateRule.equals("F") || PaymentDateRule.equals("P"));
 else throw new IllegalArgumentException ("PaymentDateRule Invalid value - Reference = PAYMENTDATERULE_AD_Reference_ID - U - F - P");
if (PaymentDateRule == null) throw new IllegalArgumentException ("PaymentDateRule is mandatory");
if (PaymentDateRule.length() > 1)
{
log.warning("Length > 1 - truncated");
PaymentDateRule = PaymentDateRule.substring(0,1);
}
set_Value ("PaymentDateRule", PaymentDateRule);
}
/** Get PaymentDateRule */
public String getPaymentDateRule() 
{
return (String)get_Value("PaymentDateRule");
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
/** Set RemovePaymentProposal */
public void setRemovePaymentProposal (String RemovePaymentProposal)
{
if (RemovePaymentProposal != null && RemovePaymentProposal.length() > 1)
{
log.warning("Length > 1 - truncated");
RemovePaymentProposal = RemovePaymentProposal.substring(0,1);
}
set_Value ("RemovePaymentProposal", RemovePaymentProposal);
}
/** Get RemovePaymentProposal */
public String getRemovePaymentProposal() 
{
return (String)get_Value("RemovePaymentProposal");
}
}
