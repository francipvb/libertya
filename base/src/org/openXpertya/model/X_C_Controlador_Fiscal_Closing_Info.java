/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por C_Controlador_Fiscal_Closing_Info
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2019-11-26 17:07:34.318 */
public class X_C_Controlador_Fiscal_Closing_Info extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_C_Controlador_Fiscal_Closing_Info (Properties ctx, int C_Controlador_Fiscal_Closing_Info_ID, String trxName)
{
super (ctx, C_Controlador_Fiscal_Closing_Info_ID, trxName);
/** if (C_Controlador_Fiscal_Closing_Info_ID == 0)
{
setC_Controlador_Fiscal_Closing_Info_ID (0);
setC_Controlador_Fiscal_ID (0);
setCreditNote_A_LastEmitted (0);
setCreditNoteAmt (Env.ZERO);
setCreditNote_BC_LastEmitted (0);
setCreditNoteExemptAmt (Env.ZERO);
setCreditNoteGravadoAmt (Env.ZERO);
setCreditNoteInternalTaxAmt (Env.ZERO);
setCreditNoteNoGravadoAmt (Env.ZERO);
setCreditNoteNotRegisteredTaxAmt (Env.ZERO);
setCreditNotePerceptionAmt (Env.ZERO);
setCreditNoteTaxAmt (Env.ZERO);
setFiscalClosingDate (new Timestamp(System.currentTimeMillis()));
setFiscalClosingType (null);
setFiscalDocument_A_LastEmitted (0);
setFiscalDocumentAmt (Env.ZERO);
setFiscalDocument_BC_LastEmitted (0);
setFiscalDocumentExemptAmt (Env.ZERO);
setFiscalDocumentGravadoAmt (Env.ZERO);
setFiscalDocumentInternalTaxAmt (Env.ZERO);
setFiscalDocumentNoGravadoAmt (Env.ZERO);
setFiscalDocumentNotRegisteredTaxAmt (Env.ZERO);
setFiscalDocumentPerceptionAmt (Env.ZERO);
setFiscalDocumentTaxAmt (Env.ZERO);
setNoFiscalHomologatedAmt (Env.ZERO);
setProcessed (false);
setQtyCanceledCreditNote (0);
setQtyCanceledFiscalDocument (0);
setQtyCreditNote (0);
setQtyCreditNoteA (0);
setQtyCreditNoteBC (0);
setQtyFiscalDocument (0);
setQtyFiscalDocumentA (0);
setQtyFiscalDocumentBC (0);
setQtyNoFiscalDocument (0);
setQtyNoFiscalHomologated (0);
}
 */
}
/** Load Constructor */
public X_C_Controlador_Fiscal_Closing_Info (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("C_Controlador_Fiscal_Closing_Info");

/** TableName=C_Controlador_Fiscal_Closing_Info */
public static final String Table_Name="C_Controlador_Fiscal_Closing_Info";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_Controlador_Fiscal_Closing_Info");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_C_Controlador_Fiscal_Closing_Info[").append(getID()).append("]");
return sb.toString();
}
/** Set C_Controlador_Fiscal_Closing_Info_ID */
public void setC_Controlador_Fiscal_Closing_Info_ID (int C_Controlador_Fiscal_Closing_Info_ID)
{
set_ValueNoCheck ("C_Controlador_Fiscal_Closing_Info_ID", new Integer(C_Controlador_Fiscal_Closing_Info_ID));
}
/** Get C_Controlador_Fiscal_Closing_Info_ID */
public int getC_Controlador_Fiscal_Closing_Info_ID() 
{
Integer ii = (Integer)get_Value("C_Controlador_Fiscal_Closing_Info_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set C_Controlador_Fiscal_ID */
public void setC_Controlador_Fiscal_ID (int C_Controlador_Fiscal_ID)
{
set_Value ("C_Controlador_Fiscal_ID", new Integer(C_Controlador_Fiscal_ID));
}
/** Get C_Controlador_Fiscal_ID */
public int getC_Controlador_Fiscal_ID() 
{
Integer ii = (Integer)get_Value("C_Controlador_Fiscal_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set CreditNote_A_LastEmitted */
public void setCreditNote_A_LastEmitted (int CreditNote_A_LastEmitted)
{
set_Value ("CreditNote_A_LastEmitted", new Integer(CreditNote_A_LastEmitted));
}
/** Get CreditNote_A_LastEmitted */
public int getCreditNote_A_LastEmitted() 
{
Integer ii = (Integer)get_Value("CreditNote_A_LastEmitted");
if (ii == null) return 0;
return ii.intValue();
}
/** Set CreditNoteAmt */
public void setCreditNoteAmt (BigDecimal CreditNoteAmt)
{
if (CreditNoteAmt == null) throw new IllegalArgumentException ("CreditNoteAmt is mandatory");
set_Value ("CreditNoteAmt", CreditNoteAmt);
}
/** Get CreditNoteAmt */
public BigDecimal getCreditNoteAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNoteAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CreditNote_BC_LastEmitted */
public void setCreditNote_BC_LastEmitted (int CreditNote_BC_LastEmitted)
{
set_Value ("CreditNote_BC_LastEmitted", new Integer(CreditNote_BC_LastEmitted));
}
/** Get CreditNote_BC_LastEmitted */
public int getCreditNote_BC_LastEmitted() 
{
Integer ii = (Integer)get_Value("CreditNote_BC_LastEmitted");
if (ii == null) return 0;
return ii.intValue();
}
/** Set CreditNoteExemptAmt */
public void setCreditNoteExemptAmt (BigDecimal CreditNoteExemptAmt)
{
if (CreditNoteExemptAmt == null) throw new IllegalArgumentException ("CreditNoteExemptAmt is mandatory");
set_Value ("CreditNoteExemptAmt", CreditNoteExemptAmt);
}
/** Get CreditNoteExemptAmt */
public BigDecimal getCreditNoteExemptAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNoteExemptAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CreditNoteGravadoAmt */
public void setCreditNoteGravadoAmt (BigDecimal CreditNoteGravadoAmt)
{
if (CreditNoteGravadoAmt == null) throw new IllegalArgumentException ("CreditNoteGravadoAmt is mandatory");
set_Value ("CreditNoteGravadoAmt", CreditNoteGravadoAmt);
}
/** Get CreditNoteGravadoAmt */
public BigDecimal getCreditNoteGravadoAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNoteGravadoAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CreditNoteInternalTaxAmt */
public void setCreditNoteInternalTaxAmt (BigDecimal CreditNoteInternalTaxAmt)
{
if (CreditNoteInternalTaxAmt == null) throw new IllegalArgumentException ("CreditNoteInternalTaxAmt is mandatory");
set_Value ("CreditNoteInternalTaxAmt", CreditNoteInternalTaxAmt);
}
/** Get CreditNoteInternalTaxAmt */
public BigDecimal getCreditNoteInternalTaxAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNoteInternalTaxAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CreditNoteNoGravadoAmt */
public void setCreditNoteNoGravadoAmt (BigDecimal CreditNoteNoGravadoAmt)
{
if (CreditNoteNoGravadoAmt == null) throw new IllegalArgumentException ("CreditNoteNoGravadoAmt is mandatory");
set_Value ("CreditNoteNoGravadoAmt", CreditNoteNoGravadoAmt);
}
/** Get CreditNoteNoGravadoAmt */
public BigDecimal getCreditNoteNoGravadoAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNoteNoGravadoAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CreditNoteNotRegisteredTaxAmt */
public void setCreditNoteNotRegisteredTaxAmt (BigDecimal CreditNoteNotRegisteredTaxAmt)
{
if (CreditNoteNotRegisteredTaxAmt == null) throw new IllegalArgumentException ("CreditNoteNotRegisteredTaxAmt is mandatory");
set_Value ("CreditNoteNotRegisteredTaxAmt", CreditNoteNotRegisteredTaxAmt);
}
/** Get CreditNoteNotRegisteredTaxAmt */
public BigDecimal getCreditNoteNotRegisteredTaxAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNoteNotRegisteredTaxAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CreditNotePerceptionAmt */
public void setCreditNotePerceptionAmt (BigDecimal CreditNotePerceptionAmt)
{
if (CreditNotePerceptionAmt == null) throw new IllegalArgumentException ("CreditNotePerceptionAmt is mandatory");
set_Value ("CreditNotePerceptionAmt", CreditNotePerceptionAmt);
}
/** Get CreditNotePerceptionAmt */
public BigDecimal getCreditNotePerceptionAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNotePerceptionAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set CreditNoteTaxAmt */
public void setCreditNoteTaxAmt (BigDecimal CreditNoteTaxAmt)
{
if (CreditNoteTaxAmt == null) throw new IllegalArgumentException ("CreditNoteTaxAmt is mandatory");
set_Value ("CreditNoteTaxAmt", CreditNoteTaxAmt);
}
/** Get CreditNoteTaxAmt */
public BigDecimal getCreditNoteTaxAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditNoteTaxAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalClosingDate */
public void setFiscalClosingDate (Timestamp FiscalClosingDate)
{
if (FiscalClosingDate == null) throw new IllegalArgumentException ("FiscalClosingDate is mandatory");
set_Value ("FiscalClosingDate", FiscalClosingDate);
}
/** Get FiscalClosingDate */
public Timestamp getFiscalClosingDate() 
{
return (Timestamp)get_Value("FiscalClosingDate");
}
/** Set FiscalClosingNo */
public void setFiscalClosingNo (int FiscalClosingNo)
{
set_Value ("FiscalClosingNo", new Integer(FiscalClosingNo));
}
/** Get FiscalClosingNo */
public int getFiscalClosingNo() 
{
Integer ii = (Integer)get_Value("FiscalClosingNo");
if (ii == null) return 0;
return ii.intValue();
}
public static final int FISCALCLOSINGTYPE_AD_Reference_ID = MReference.getReferenceID("Fiscal_Close_Types");
/** Cierre X = X */
public static final String FISCALCLOSINGTYPE_CierreX = "X";
/** Cierre Z = Z */
public static final String FISCALCLOSINGTYPE_CierreZ = "Z";
/** Set FiscalClosingType */
public void setFiscalClosingType (String FiscalClosingType)
{
if (FiscalClosingType.equals("X") || FiscalClosingType.equals("Z") || ( refContainsValue("CORE-AD_Reference-1010119", FiscalClosingType) ) );
 else throw new IllegalArgumentException ("FiscalClosingType Invalid value: " + FiscalClosingType + ".  Valid: " +  refValidOptions("CORE-AD_Reference-1010119") );
if (FiscalClosingType == null) throw new IllegalArgumentException ("FiscalClosingType is mandatory");
if (FiscalClosingType.length() > 1)
{
log.warning("Length > 1 - truncated");
FiscalClosingType = FiscalClosingType.substring(0,1);
}
set_Value ("FiscalClosingType", FiscalClosingType);
}
/** Get FiscalClosingType */
public String getFiscalClosingType() 
{
return (String)get_Value("FiscalClosingType");
}
/** Set FiscalDocument_A_LastEmitted */
public void setFiscalDocument_A_LastEmitted (int FiscalDocument_A_LastEmitted)
{
set_Value ("FiscalDocument_A_LastEmitted", new Integer(FiscalDocument_A_LastEmitted));
}
/** Get FiscalDocument_A_LastEmitted */
public int getFiscalDocument_A_LastEmitted() 
{
Integer ii = (Integer)get_Value("FiscalDocument_A_LastEmitted");
if (ii == null) return 0;
return ii.intValue();
}
/** Set FiscalDocumentAmt */
public void setFiscalDocumentAmt (BigDecimal FiscalDocumentAmt)
{
if (FiscalDocumentAmt == null) throw new IllegalArgumentException ("FiscalDocumentAmt is mandatory");
set_Value ("FiscalDocumentAmt", FiscalDocumentAmt);
}
/** Get FiscalDocumentAmt */
public BigDecimal getFiscalDocumentAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalDocument_BC_LastEmitted */
public void setFiscalDocument_BC_LastEmitted (int FiscalDocument_BC_LastEmitted)
{
set_Value ("FiscalDocument_BC_LastEmitted", new Integer(FiscalDocument_BC_LastEmitted));
}
/** Get FiscalDocument_BC_LastEmitted */
public int getFiscalDocument_BC_LastEmitted() 
{
Integer ii = (Integer)get_Value("FiscalDocument_BC_LastEmitted");
if (ii == null) return 0;
return ii.intValue();
}
/** Set FiscalDocumentExemptAmt */
public void setFiscalDocumentExemptAmt (BigDecimal FiscalDocumentExemptAmt)
{
if (FiscalDocumentExemptAmt == null) throw new IllegalArgumentException ("FiscalDocumentExemptAmt is mandatory");
set_Value ("FiscalDocumentExemptAmt", FiscalDocumentExemptAmt);
}
/** Get FiscalDocumentExemptAmt */
public BigDecimal getFiscalDocumentExemptAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentExemptAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalDocumentGravadoAmt */
public void setFiscalDocumentGravadoAmt (BigDecimal FiscalDocumentGravadoAmt)
{
if (FiscalDocumentGravadoAmt == null) throw new IllegalArgumentException ("FiscalDocumentGravadoAmt is mandatory");
set_Value ("FiscalDocumentGravadoAmt", FiscalDocumentGravadoAmt);
}
/** Get FiscalDocumentGravadoAmt */
public BigDecimal getFiscalDocumentGravadoAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentGravadoAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalDocumentInternalTaxAmt */
public void setFiscalDocumentInternalTaxAmt (BigDecimal FiscalDocumentInternalTaxAmt)
{
if (FiscalDocumentInternalTaxAmt == null) throw new IllegalArgumentException ("FiscalDocumentInternalTaxAmt is mandatory");
set_Value ("FiscalDocumentInternalTaxAmt", FiscalDocumentInternalTaxAmt);
}
/** Get FiscalDocumentInternalTaxAmt */
public BigDecimal getFiscalDocumentInternalTaxAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentInternalTaxAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalDocumentNoGravadoAmt */
public void setFiscalDocumentNoGravadoAmt (BigDecimal FiscalDocumentNoGravadoAmt)
{
if (FiscalDocumentNoGravadoAmt == null) throw new IllegalArgumentException ("FiscalDocumentNoGravadoAmt is mandatory");
set_Value ("FiscalDocumentNoGravadoAmt", FiscalDocumentNoGravadoAmt);
}
/** Get FiscalDocumentNoGravadoAmt */
public BigDecimal getFiscalDocumentNoGravadoAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentNoGravadoAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalDocumentNotRegisteredTaxAmt */
public void setFiscalDocumentNotRegisteredTaxAmt (BigDecimal FiscalDocumentNotRegisteredTaxAmt)
{
if (FiscalDocumentNotRegisteredTaxAmt == null) throw new IllegalArgumentException ("FiscalDocumentNotRegisteredTaxAmt is mandatory");
set_Value ("FiscalDocumentNotRegisteredTaxAmt", FiscalDocumentNotRegisteredTaxAmt);
}
/** Get FiscalDocumentNotRegisteredTaxAmt */
public BigDecimal getFiscalDocumentNotRegisteredTaxAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentNotRegisteredTaxAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalDocumentPerceptionAmt */
public void setFiscalDocumentPerceptionAmt (BigDecimal FiscalDocumentPerceptionAmt)
{
if (FiscalDocumentPerceptionAmt == null) throw new IllegalArgumentException ("FiscalDocumentPerceptionAmt is mandatory");
set_Value ("FiscalDocumentPerceptionAmt", FiscalDocumentPerceptionAmt);
}
/** Get FiscalDocumentPerceptionAmt */
public BigDecimal getFiscalDocumentPerceptionAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentPerceptionAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set FiscalDocumentTaxAmt */
public void setFiscalDocumentTaxAmt (BigDecimal FiscalDocumentTaxAmt)
{
if (FiscalDocumentTaxAmt == null) throw new IllegalArgumentException ("FiscalDocumentTaxAmt is mandatory");
set_Value ("FiscalDocumentTaxAmt", FiscalDocumentTaxAmt);
}
/** Get FiscalDocumentTaxAmt */
public BigDecimal getFiscalDocumentTaxAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("FiscalDocumentTaxAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set NoFiscalHomologatedAmt */
public void setNoFiscalHomologatedAmt (BigDecimal NoFiscalHomologatedAmt)
{
if (NoFiscalHomologatedAmt == null) throw new IllegalArgumentException ("NoFiscalHomologatedAmt is mandatory");
set_Value ("NoFiscalHomologatedAmt", NoFiscalHomologatedAmt);
}
/** Get NoFiscalHomologatedAmt */
public BigDecimal getNoFiscalHomologatedAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("NoFiscalHomologatedAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set POSName */
public void setPOSName (String POSName)
{
if (POSName != null && POSName.length() > 100)
{
log.warning("Length > 100 - truncated");
POSName = POSName.substring(0,100);
}
set_Value ("POSName", POSName);
}
/** Get POSName */
public String getPOSName() 
{
return (String)get_Value("POSName");
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
/** Set Punto De Venta */
public void setPuntoDeVenta (int PuntoDeVenta)
{
set_Value ("PuntoDeVenta", new Integer(PuntoDeVenta));
}
/** Get Punto De Venta */
public int getPuntoDeVenta() 
{
Integer ii = (Integer)get_Value("PuntoDeVenta");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyCanceledCreditNote */
public void setQtyCanceledCreditNote (int QtyCanceledCreditNote)
{
set_Value ("QtyCanceledCreditNote", new Integer(QtyCanceledCreditNote));
}
/** Get QtyCanceledCreditNote */
public int getQtyCanceledCreditNote() 
{
Integer ii = (Integer)get_Value("QtyCanceledCreditNote");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyCanceledFiscalDocument */
public void setQtyCanceledFiscalDocument (int QtyCanceledFiscalDocument)
{
set_Value ("QtyCanceledFiscalDocument", new Integer(QtyCanceledFiscalDocument));
}
/** Get QtyCanceledFiscalDocument */
public int getQtyCanceledFiscalDocument() 
{
Integer ii = (Integer)get_Value("QtyCanceledFiscalDocument");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyCreditNote */
public void setQtyCreditNote (int QtyCreditNote)
{
set_Value ("QtyCreditNote", new Integer(QtyCreditNote));
}
/** Get QtyCreditNote */
public int getQtyCreditNote() 
{
Integer ii = (Integer)get_Value("QtyCreditNote");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyCreditNoteA */
public void setQtyCreditNoteA (int QtyCreditNoteA)
{
set_Value ("QtyCreditNoteA", new Integer(QtyCreditNoteA));
}
/** Get QtyCreditNoteA */
public int getQtyCreditNoteA() 
{
Integer ii = (Integer)get_Value("QtyCreditNoteA");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyCreditNoteBC */
public void setQtyCreditNoteBC (int QtyCreditNoteBC)
{
set_Value ("QtyCreditNoteBC", new Integer(QtyCreditNoteBC));
}
/** Get QtyCreditNoteBC */
public int getQtyCreditNoteBC() 
{
Integer ii = (Integer)get_Value("QtyCreditNoteBC");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyFiscalDocument */
public void setQtyFiscalDocument (int QtyFiscalDocument)
{
set_Value ("QtyFiscalDocument", new Integer(QtyFiscalDocument));
}
/** Get QtyFiscalDocument */
public int getQtyFiscalDocument() 
{
Integer ii = (Integer)get_Value("QtyFiscalDocument");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyFiscalDocumentA */
public void setQtyFiscalDocumentA (int QtyFiscalDocumentA)
{
set_Value ("QtyFiscalDocumentA", new Integer(QtyFiscalDocumentA));
}
/** Get QtyFiscalDocumentA */
public int getQtyFiscalDocumentA() 
{
Integer ii = (Integer)get_Value("QtyFiscalDocumentA");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyFiscalDocumentBC */
public void setQtyFiscalDocumentBC (int QtyFiscalDocumentBC)
{
set_Value ("QtyFiscalDocumentBC", new Integer(QtyFiscalDocumentBC));
}
/** Get QtyFiscalDocumentBC */
public int getQtyFiscalDocumentBC() 
{
Integer ii = (Integer)get_Value("QtyFiscalDocumentBC");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyNoFiscalDocument */
public void setQtyNoFiscalDocument (int QtyNoFiscalDocument)
{
set_Value ("QtyNoFiscalDocument", new Integer(QtyNoFiscalDocument));
}
/** Get QtyNoFiscalDocument */
public int getQtyNoFiscalDocument() 
{
Integer ii = (Integer)get_Value("QtyNoFiscalDocument");
if (ii == null) return 0;
return ii.intValue();
}
/** Set QtyNoFiscalHomologated */
public void setQtyNoFiscalHomologated (int QtyNoFiscalHomologated)
{
set_Value ("QtyNoFiscalHomologated", new Integer(QtyNoFiscalHomologated));
}
/** Get QtyNoFiscalHomologated */
public int getQtyNoFiscalHomologated() 
{
Integer ii = (Integer)get_Value("QtyNoFiscalHomologated");
if (ii == null) return 0;
return ii.intValue();
}
}
