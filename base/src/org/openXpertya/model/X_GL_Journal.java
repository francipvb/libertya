/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por GL_Journal
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2018-05-16 15:04:32.054 */
public class X_GL_Journal extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_GL_Journal (Properties ctx, int GL_Journal_ID, String trxName)
{
super (ctx, GL_Journal_ID, trxName);
/** if (GL_Journal_ID == 0)
{
setC_AcctSchema_ID (0);	// @$C_AcctSchema_ID@
setC_ConversionType_ID (0);
setC_Currency_ID (0);	// @C_Currency_ID@
setC_DocType_ID (0);	// @C_DocType_ID@
setC_Period_ID (0);	// @C_Period_ID@
setCurrencyRate (Env.ZERO);	// 1
setDateAcct (new Timestamp(System.currentTimeMillis()));	// @DateAcct@
setDateDoc (new Timestamp(System.currentTimeMillis()));	// @DateDoc@
setDescription (null);
setDocAction (null);	// CO
setDocStatus (null);	// DR
setDocumentNo (null);
setGL_Category_ID (0);	// @GL_Category_ID@
setGL_Journal_ID (0);
setIsApproved (true);	// Y
setIsPrinted (false);	// N
setIsReActivated (false);
setPosted (false);	// N
setPostingType (null);	// @PostingType@
setTotalCr (Env.ZERO);	// 0
setTotalDr (Env.ZERO);	// 0
}
 */
}
/** Load Constructor */
public X_GL_Journal (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("GL_Journal");

/** TableName=GL_Journal */
public static final String Table_Name="GL_Journal";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"GL_Journal");
protected static BigDecimal AccessLevel = new BigDecimal(1);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_GL_Journal[").append(getID()).append("]");
return sb.toString();
}
/** Set Accounting Schema.
Rules for accounting */
public void setC_AcctSchema_ID (int C_AcctSchema_ID)
{
set_ValueNoCheck ("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
}
/** Get Accounting Schema.
Rules for accounting */
public int getC_AcctSchema_ID() 
{
Integer ii = (Integer)get_Value("C_AcctSchema_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Currency Type.
Currency Conversion Rate Type */
public void setC_ConversionType_ID (int C_ConversionType_ID)
{
set_Value ("C_ConversionType_ID", new Integer(C_ConversionType_ID));
}
/** Get Currency Type.
Currency Conversion Rate Type */
public int getC_ConversionType_ID() 
{
Integer ii = (Integer)get_Value("C_ConversionType_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Currency.
The Currency for this record */
public void setC_Currency_ID (int C_Currency_ID)
{
set_Value ("C_Currency_ID", new Integer(C_Currency_ID));
}
/** Get Currency.
The Currency for this record */
public int getC_Currency_ID() 
{
Integer ii = (Integer)get_Value("C_Currency_ID");
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
/** Set Control Amount.
If not zero, the Debit amount of the document must be equal this amount */
public void setControlAmt (BigDecimal ControlAmt)
{
set_Value ("ControlAmt", ControlAmt);
}
/** Get Control Amount.
If not zero, the Debit amount of the document must be equal this amount */
public BigDecimal getControlAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("ControlAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Copy From.
Copy From Record */
public void setCopyFrom (String CopyFrom)
{
if (CopyFrom != null && CopyFrom.length() > 1)
{
log.warning("Length > 1 - truncated");
CopyFrom = CopyFrom.substring(0,1);
}
set_Value ("CopyFrom", CopyFrom);
}
/** Get Copy From.
Copy From Record */
public String getCopyFrom() 
{
return (String)get_Value("CopyFrom");
}
public static final int C_PERIOD_ID_AD_Reference_ID = MReference.getReferenceID("C_Period (all)");
/** Set Period.
Period of the Calendar */
public void setC_Period_ID (int C_Period_ID)
{
set_Value ("C_Period_ID", new Integer(C_Period_ID));
}
/** Get Period.
Period of the Calendar */
public int getC_Period_ID() 
{
Integer ii = (Integer)get_Value("C_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Rate.
Currency Conversion Rate */
public void setCurrencyRate (BigDecimal CurrencyRate)
{
if (CurrencyRate == null) throw new IllegalArgumentException ("CurrencyRate is mandatory");
set_Value ("CurrencyRate", CurrencyRate);
}
/** Get Rate.
Currency Conversion Rate */
public BigDecimal getCurrencyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("CurrencyRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Account Date.
Accounting Date */
public void setDateAcct (Timestamp DateAcct)
{
if (DateAcct == null) throw new IllegalArgumentException ("DateAcct is mandatory");
set_Value ("DateAcct", DateAcct);
}
/** Get Account Date.
Accounting Date */
public Timestamp getDateAcct() 
{
return (Timestamp)get_Value("DateAcct");
}
/** Set Document Date.
Date of the Document */
public void setDateDoc (Timestamp DateDoc)
{
if (DateDoc == null) throw new IllegalArgumentException ("DateDoc is mandatory");
set_Value ("DateDoc", DateDoc);
}
/** Get Document Date.
Date of the Document */
public Timestamp getDateDoc() 
{
return (Timestamp)get_Value("DateDoc");
}
/** Set Description.
Optional short description of the record */
public void setDescription (String Description)
{
if (Description == null) throw new IllegalArgumentException ("Description is mandatory");
if (Description.length() > 255)
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
Document sequence number of the document */
public void setDocumentNo (String DocumentNo)
{
if (DocumentNo == null) throw new IllegalArgumentException ("DocumentNo is mandatory");
if (DocumentNo.length() > 30)
{
log.warning("Length > 30 - truncated");
DocumentNo = DocumentNo.substring(0,30);
}
set_ValueNoCheck ("DocumentNo", DocumentNo);
}
/** Get Document No.
Document sequence number of the document */
public String getDocumentNo() 
{
return (String)get_Value("DocumentNo");
}
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(getID(), getDocumentNo());
}
/** Set Budget.
General Ledger Budget */
public void setGL_Budget_ID (int GL_Budget_ID)
{
if (GL_Budget_ID <= 0) set_Value ("GL_Budget_ID", null);
 else 
set_Value ("GL_Budget_ID", new Integer(GL_Budget_ID));
}
/** Get Budget.
General Ledger Budget */
public int getGL_Budget_ID() 
{
Integer ii = (Integer)get_Value("GL_Budget_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set GL Category.
General Ledger Category */
public void setGL_Category_ID (int GL_Category_ID)
{
set_Value ("GL_Category_ID", new Integer(GL_Category_ID));
}
/** Get GL Category.
General Ledger Category */
public int getGL_Category_ID() 
{
Integer ii = (Integer)get_Value("GL_Category_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Journal Batch.
General Ledger Journal Batch */
public void setGL_JournalBatch_ID (int GL_JournalBatch_ID)
{
if (GL_JournalBatch_ID <= 0) set_ValueNoCheck ("GL_JournalBatch_ID", null);
 else 
set_ValueNoCheck ("GL_JournalBatch_ID", new Integer(GL_JournalBatch_ID));
}
/** Get Journal Batch.
General Ledger Journal Batch */
public int getGL_JournalBatch_ID() 
{
Integer ii = (Integer)get_Value("GL_JournalBatch_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Journal.
General Ledger Journal */
public void setGL_Journal_ID (int GL_Journal_ID)
{
set_ValueNoCheck ("GL_Journal_ID", new Integer(GL_Journal_ID));
}
/** Get Journal.
General Ledger Journal */
public int getGL_Journal_ID() 
{
Integer ii = (Integer)get_Value("GL_Journal_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Approved.
Indicates if this document requires approval */
public void setIsApproved (boolean IsApproved)
{
set_ValueNoCheck ("IsApproved", new Boolean(IsApproved));
}
/** Get Approved.
Indicates if this document requires approval */
public boolean isApproved() 
{
Object oo = get_Value("IsApproved");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Printed.
Indicates if this document / line is printed */
public void setIsPrinted (boolean IsPrinted)
{
set_ValueNoCheck ("IsPrinted", new Boolean(IsPrinted));
}
/** Get Printed.
Indicates if this document / line is printed */
public boolean isPrinted() 
{
Object oo = get_Value("IsPrinted");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsReActivated */
public void setIsReActivated (boolean IsReActivated)
{
set_Value ("IsReActivated", new Boolean(IsReActivated));
}
/** Get IsReActivated */
public boolean isReActivated() 
{
Object oo = get_Value("IsReActivated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Posted.
Posting status */
public void setPosted (boolean Posted)
{
set_ValueNoCheck ("Posted", new Boolean(Posted));
}
/** Get Posted.
Posting status */
public boolean isPosted() 
{
Object oo = get_Value("Posted");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
public static final int POSTINGTYPE_AD_Reference_ID = MReference.getReferenceID("_Posting Type");
/** Presupuestaria = B */
public static final String POSTINGTYPE_Presupuestaria = "B";
/** Pendientes = E */
public static final String POSTINGTYPE_Pendientes = "E";
/** Estadisticos = S */
public static final String POSTINGTYPE_Estadisticos = "S";
/** Actual = A */
public static final String POSTINGTYPE_Actual = "A";
/** Set PostingType.
The type of amount that this journal updated */
public void setPostingType (String PostingType)
{
if (PostingType.equals("B") || PostingType.equals("E") || PostingType.equals("S") || PostingType.equals("A") || ( refContainsValue("CORE-AD_Reference-125", PostingType) ) );
 else throw new IllegalArgumentException ("PostingType Invalid value: " + PostingType + ".  Valid: " +  refValidOptions("CORE-AD_Reference-125") );
if (PostingType == null) throw new IllegalArgumentException ("PostingType is mandatory");
if (PostingType.length() > 1)
{
log.warning("Length > 1 - truncated");
PostingType = PostingType.substring(0,1);
}
set_Value ("PostingType", PostingType);
}
/** Get PostingType.
The type of amount that this journal updated */
public String getPostingType() 
{
return (String)get_Value("PostingType");
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
/** Set Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Total Credit.
Total Credit in document currency */
public void setTotalCr (BigDecimal TotalCr)
{
if (TotalCr == null) throw new IllegalArgumentException ("TotalCr is mandatory");
set_ValueNoCheck ("TotalCr", TotalCr);
}
/** Get Total Credit.
Total Credit in document currency */
public BigDecimal getTotalCr() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalCr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Total Debit.
Total debit in document currency */
public void setTotalDr (BigDecimal TotalDr)
{
if (TotalDr == null) throw new IllegalArgumentException ("TotalDr is mandatory");
set_ValueNoCheck ("TotalDr", TotalDr);
}
/** Get Total Debit.
Total debit in document currency */
public BigDecimal getTotalDr() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalDr");
if (bd == null) return Env.ZERO;
return bd;
}
}
