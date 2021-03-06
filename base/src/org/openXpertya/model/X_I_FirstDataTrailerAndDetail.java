/** Modelo Generado - NO CAMBIAR MANUALMENTE - Disytel */
package org.openXpertya.model;
import java.util.logging.Level;
 import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;
/** Modelo Generado por I_FirstDataTrailerAndDetail
 *  @author Comunidad de Desarrollo Libertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2017-03-01 12:33:46.831 */
public class X_I_FirstDataTrailerAndDetail extends org.openXpertya.model.PO
{
/** Constructor estándar */
public X_I_FirstDataTrailerAndDetail (Properties ctx, int I_FirstDataTrailerAndDetail_ID, String trxName)
{
super (ctx, I_FirstDataTrailerAndDetail_ID, trxName);
/** if (I_FirstDataTrailerAndDetail_ID == 0)
{
setI_Firstdatatraileranddetail_ID (0);
setI_IsImported (false);
}
 */
}
/** Load Constructor */
public X_I_FirstDataTrailerAndDetail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID */
public static final int Table_ID = M_Table.getTableID("I_FirstDataTrailerAndDetail");

/** TableName=I_FirstDataTrailerAndDetail */
public static final String Table_Name="I_FirstDataTrailerAndDetail";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"I_FirstDataTrailerAndDetail");
protected static BigDecimal AccessLevel = new BigDecimal(3);

/** Load Meta Data */
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
public String toString()
{
StringBuffer sb = new StringBuffer ("X_I_FirstDataTrailerAndDetail[").append(getID()).append("]");
return sb.toString();
}
/** Set alicuota_iva_fo */
public void setalicuota_iva_fo (String alicuota_iva_fo)
{
if (alicuota_iva_fo != null && alicuota_iva_fo.length() > 32)
{
log.warning("Length > 32 - truncated");
alicuota_iva_fo = alicuota_iva_fo.substring(0,32);
}
set_Value ("alicuota_iva_fo", alicuota_iva_fo);
}
/** Get alicuota_iva_fo */
public String getalicuota_iva_fo() 
{
return (String)get_Value("alicuota_iva_fo");
}
/** Set arancel */
public void setarancel (String arancel)
{
if (arancel != null && arancel.length() > 32)
{
log.warning("Length > 32 - truncated");
arancel = arancel.substring(0,32);
}
set_Value ("arancel", arancel);
}
/** Get arancel */
public String getarancel() 
{
return (String)get_Value("arancel");
}
/** Set aranceles_cto_fin */
public void setaranceles_cto_fin (String aranceles_cto_fin)
{
if (aranceles_cto_fin != null && aranceles_cto_fin.length() > 32)
{
log.warning("Length > 32 - truncated");
aranceles_cto_fin = aranceles_cto_fin.substring(0,32);
}
set_Value ("aranceles_cto_fin", aranceles_cto_fin);
}
/** Get aranceles_cto_fin */
public String getaranceles_cto_fin() 
{
return (String)get_Value("aranceles_cto_fin");
}
/** Set aranceles_cto_fin_signo */
public void setaranceles_cto_fin_signo (String aranceles_cto_fin_signo)
{
if (aranceles_cto_fin_signo != null && aranceles_cto_fin_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
aranceles_cto_fin_signo = aranceles_cto_fin_signo.substring(0,32);
}
set_Value ("aranceles_cto_fin_signo", aranceles_cto_fin_signo);
}
/** Get aranceles_cto_fin_signo */
public String getaranceles_cto_fin_signo() 
{
return (String)get_Value("aranceles_cto_fin_signo");
}
/** Set arancel_signo */
public void setarancel_signo (String arancel_signo)
{
if (arancel_signo != null && arancel_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
arancel_signo = arancel_signo.substring(0,32);
}
set_Value ("arancel_signo", arancel_signo);
}
/** Get arancel_signo */
public String getarancel_signo() 
{
return (String)get_Value("arancel_signo");
}
/** Set archivo_id */
public void setarchivo_id (String archivo_id)
{
if (archivo_id != null && archivo_id.length() > 32)
{
log.warning("Length > 32 - truncated");
archivo_id = archivo_id.substring(0,32);
}
set_Value ("archivo_id", archivo_id);
}
/** Get archivo_id */
public String getarchivo_id() 
{
return (String)get_Value("archivo_id");
}
/** Set caja_nro_cinta_posnet */
public void setcaja_nro_cinta_posnet (String caja_nro_cinta_posnet)
{
if (caja_nro_cinta_posnet != null && caja_nro_cinta_posnet.length() > 32)
{
log.warning("Length > 32 - truncated");
caja_nro_cinta_posnet = caja_nro_cinta_posnet.substring(0,32);
}
set_Value ("caja_nro_cinta_posnet", caja_nro_cinta_posnet);
}
/** Get caja_nro_cinta_posnet */
public String getcaja_nro_cinta_posnet() 
{
return (String)get_Value("caja_nro_cinta_posnet");
}
/** Set caratula_terminal_posnet */
public void setcaratula_terminal_posnet (String caratula_terminal_posnet)
{
if (caratula_terminal_posnet != null && caratula_terminal_posnet.length() > 32)
{
log.warning("Length > 32 - truncated");
caratula_terminal_posnet = caratula_terminal_posnet.substring(0,32);
}
set_Value ("caratula_terminal_posnet", caratula_terminal_posnet);
}
/** Get caratula_terminal_posnet */
public String getcaratula_terminal_posnet() 
{
return (String)get_Value("caratula_terminal_posnet");
}
/** Set categoria_iva */
public void setcategoria_iva (String categoria_iva)
{
if (categoria_iva != null && categoria_iva.length() > 32)
{
log.warning("Length > 32 - truncated");
categoria_iva = categoria_iva.substring(0,32);
}
set_Value ("categoria_iva", categoria_iva);
}
/** Get categoria_iva */
public String getcategoria_iva() 
{
return (String)get_Value("categoria_iva");
}
/** Set codigo_cargo_pago */
public void setcodigo_cargo_pago (String codigo_cargo_pago)
{
if (codigo_cargo_pago != null && codigo_cargo_pago.length() > 32)
{
log.warning("Length > 32 - truncated");
codigo_cargo_pago = codigo_cargo_pago.substring(0,32);
}
set_Value ("codigo_cargo_pago", codigo_cargo_pago);
}
/** Get codigo_cargo_pago */
public String getcodigo_cargo_pago() 
{
return (String)get_Value("codigo_cargo_pago");
}
/** Set codigo_movimiento */
public void setcodigo_movimiento (String codigo_movimiento)
{
if (codigo_movimiento != null && codigo_movimiento.length() > 32)
{
log.warning("Length > 32 - truncated");
codigo_movimiento = codigo_movimiento.substring(0,32);
}
set_Value ("codigo_movimiento", codigo_movimiento);
}
/** Get codigo_movimiento */
public String getcodigo_movimiento() 
{
return (String)get_Value("codigo_movimiento");
}
/** Set codigo_origen */
public void setcodigo_origen (String codigo_origen)
{
if (codigo_origen != null && codigo_origen.length() > 32)
{
log.warning("Length > 32 - truncated");
codigo_origen = codigo_origen.substring(0,32);
}
set_Value ("codigo_origen", codigo_origen);
}
/** Get codigo_origen */
public String getcodigo_origen() 
{
return (String)get_Value("codigo_origen");
}
/** Set comercio_centralizador */
public void setcomercio_centralizador (String comercio_centralizador)
{
if (comercio_centralizador != null && comercio_centralizador.length() > 32)
{
log.warning("Length > 32 - truncated");
comercio_centralizador = comercio_centralizador.substring(0,32);
}
set_Value ("comercio_centralizador", comercio_centralizador);
}
/** Get comercio_centralizador */
public String getcomercio_centralizador() 
{
return (String)get_Value("comercio_centralizador");
}
/** Set comercio_participante */
public void setcomercio_participante (String comercio_participante)
{
if (comercio_participante != null && comercio_participante.length() > 32)
{
log.warning("Length > 32 - truncated");
comercio_participante = comercio_participante.substring(0,32);
}
set_Value ("comercio_participante", comercio_participante);
}
/** Get comercio_participante */
public String getcomercio_participante() 
{
return (String)get_Value("comercio_participante");
}
/** Set costo_financiero */
public void setcosto_financiero (String costo_financiero)
{
if (costo_financiero != null && costo_financiero.length() > 32)
{
log.warning("Length > 32 - truncated");
costo_financiero = costo_financiero.substring(0,32);
}
set_Value ("costo_financiero", costo_financiero);
}
/** Get costo_financiero */
public String getcosto_financiero() 
{
return (String)get_Value("costo_financiero");
}
/** Set costo_financiero_signo */
public void setcosto_financiero_signo (String costo_financiero_signo)
{
if (costo_financiero_signo != null && costo_financiero_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
costo_financiero_signo = costo_financiero_signo.substring(0,32);
}
set_Value ("costo_financiero_signo", costo_financiero_signo);
}
/** Get costo_financiero_signo */
public String getcosto_financiero_signo() 
{
return (String)get_Value("costo_financiero_signo");
}
/** Set cuotas_plan */
public void setcuotas_plan (String cuotas_plan)
{
if (cuotas_plan != null && cuotas_plan.length() > 32)
{
log.warning("Length > 32 - truncated");
cuotas_plan = cuotas_plan.substring(0,32);
}
set_Value ("cuotas_plan", cuotas_plan);
}
/** Get cuotas_plan */
public String getcuotas_plan() 
{
return (String)get_Value("cuotas_plan");
}
/** Set cuota_vigente */
public void setcuota_vigente (String cuota_vigente)
{
if (cuota_vigente != null && cuota_vigente.length() > 32)
{
log.warning("Length > 32 - truncated");
cuota_vigente = cuota_vigente.substring(0,32);
}
set_Value ("cuota_vigente", cuota_vigente);
}
/** Get cuota_vigente */
public String getcuota_vigente() 
{
return (String)get_Value("cuota_vigente");
}
/** Set cupon_cupon_posnet */
public void setcupon_cupon_posnet (String cupon_cupon_posnet)
{
if (cupon_cupon_posnet != null && cupon_cupon_posnet.length() > 32)
{
log.warning("Length > 32 - truncated");
cupon_cupon_posnet = cupon_cupon_posnet.substring(0,32);
}
set_Value ("cupon_cupon_posnet", cupon_cupon_posnet);
}
/** Get cupon_cupon_posnet */
public String getcupon_cupon_posnet() 
{
return (String)get_Value("cupon_cupon_posnet");
}
/** Set entidad_emisora */
public void setentidad_emisora (String entidad_emisora)
{
if (entidad_emisora != null && entidad_emisora.length() > 32)
{
log.warning("Length > 32 - truncated");
entidad_emisora = entidad_emisora.substring(0,32);
}
set_Value ("entidad_emisora", entidad_emisora);
}
/** Get entidad_emisora */
public String getentidad_emisora() 
{
return (String)get_Value("entidad_emisora");
}
/** Set entidad_pagadora */
public void setentidad_pagadora (String entidad_pagadora)
{
if (entidad_pagadora != null && entidad_pagadora.length() > 32)
{
log.warning("Length > 32 - truncated");
entidad_pagadora = entidad_pagadora.substring(0,32);
}
set_Value ("entidad_pagadora", entidad_pagadora);
}
/** Get entidad_pagadora */
public String getentidad_pagadora() 
{
return (String)get_Value("entidad_pagadora");
}
/** Set fecha_operacion */
public void setfecha_operacion (String fecha_operacion)
{
if (fecha_operacion != null && fecha_operacion.length() > 32)
{
log.warning("Length > 32 - truncated");
fecha_operacion = fecha_operacion.substring(0,32);
}
set_Value ("fecha_operacion", fecha_operacion);
}
/** Get fecha_operacion */
public String getfecha_operacion() 
{
return (String)get_Value("fecha_operacion");
}
/** Set fecha_presentacion */
public void setfecha_presentacion (String fecha_presentacion)
{
if (fecha_presentacion != null && fecha_presentacion.length() > 32)
{
log.warning("Length > 32 - truncated");
fecha_presentacion = fecha_presentacion.substring(0,32);
}
set_Value ("fecha_presentacion", fecha_presentacion);
}
/** Get fecha_presentacion */
public String getfecha_presentacion() 
{
return (String)get_Value("fecha_presentacion");
}
/** Set fecha_present_original */
public void setfecha_present_original (String fecha_present_original)
{
if (fecha_present_original != null && fecha_present_original.length() > 32)
{
log.warning("Length > 32 - truncated");
fecha_present_original = fecha_present_original.substring(0,32);
}
set_Value ("fecha_present_original", fecha_present_original);
}
/** Get fecha_present_original */
public String getfecha_present_original() 
{
return (String)get_Value("fecha_present_original");
}
/** Set fecha_vencimiento_clearing */
public void setfecha_vencimiento_clearing (String fecha_vencimiento_clearing)
{
if (fecha_vencimiento_clearing != null && fecha_vencimiento_clearing.length() > 32)
{
log.warning("Length > 32 - truncated");
fecha_vencimiento_clearing = fecha_vencimiento_clearing.substring(0,32);
}
set_Value ("fecha_vencimiento_clearing", fecha_vencimiento_clearing);
}
/** Get fecha_vencimiento_clearing */
public String getfecha_vencimiento_clearing() 
{
return (String)get_Value("fecha_vencimiento_clearing");
}
/** Set grupo_presentacion */
public void setgrupo_presentacion (String grupo_presentacion)
{
if (grupo_presentacion != null && grupo_presentacion.length() > 32)
{
log.warning("Length > 32 - truncated");
grupo_presentacion = grupo_presentacion.substring(0,32);
}
set_Value ("grupo_presentacion", grupo_presentacion);
}
/** Get grupo_presentacion */
public String getgrupo_presentacion() 
{
return (String)get_Value("grupo_presentacion");
}
/** Set hash_modelo */
public void sethash_modelo (String hash_modelo)
{
if (hash_modelo != null && hash_modelo.length() > 32)
{
log.warning("Length > 32 - truncated");
hash_modelo = hash_modelo.substring(0,32);
}
set_Value ("hash_modelo", hash_modelo);
}
/** Get hash_modelo */
public String gethash_modelo() 
{
return (String)get_Value("hash_modelo");
}
/** Set id */
public void setid (String id)
{
if (id != null && id.length() > 32)
{
log.warning("Length > 32 - truncated");
id = id.substring(0,32);
}
set_Value ("id", id);
}
/** Get id */
public String getid() 
{
return (String)get_Value("id");
}
/** Set Import Error Message.
Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,2000);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
}
/** Set I_Firstdatatraileranddetail_ID */
public void setI_Firstdatatraileranddetail_ID (int I_Firstdatatraileranddetail_ID)
{
set_ValueNoCheck ("I_Firstdatatraileranddetail_ID", new Integer(I_Firstdatatraileranddetail_ID));
}
/** Get I_Firstdatatraileranddetail_ID */
public int getI_Firstdatatraileranddetail_ID() 
{
Integer ii = (Integer)get_Value("I_Firstdatatraileranddetail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Imported.
Has this import been processed */
public void setI_IsImported (boolean I_IsImported)
{
set_Value ("I_IsImported", new Boolean(I_IsImported));
}
/** Get Imported.
Has this import been processed */
public boolean isI_IsImported() 
{
Object oo = get_Value("I_IsImported");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set importe_arancel */
public void setimporte_arancel (String importe_arancel)
{
if (importe_arancel != null && importe_arancel.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_arancel = importe_arancel.substring(0,32);
}
set_Value ("importe_arancel", importe_arancel);
}
/** Get importe_arancel */
public String getimporte_arancel() 
{
return (String)get_Value("importe_arancel");
}
/** Set importe_arancel_signo */
public void setimporte_arancel_signo (String importe_arancel_signo)
{
if (importe_arancel_signo != null && importe_arancel_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_arancel_signo = importe_arancel_signo.substring(0,32);
}
set_Value ("importe_arancel_signo", importe_arancel_signo);
}
/** Get importe_arancel_signo */
public String getimporte_arancel_signo() 
{
return (String)get_Value("importe_arancel_signo");
}
/** Set importe_costo_financiero */
public void setimporte_costo_financiero (String importe_costo_financiero)
{
if (importe_costo_financiero != null && importe_costo_financiero.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_costo_financiero = importe_costo_financiero.substring(0,32);
}
set_Value ("importe_costo_financiero", importe_costo_financiero);
}
/** Get importe_costo_financiero */
public String getimporte_costo_financiero() 
{
return (String)get_Value("importe_costo_financiero");
}
/** Set importe_costo_financiero_signo */
public void setimporte_costo_financiero_signo (String importe_costo_financiero_signo)
{
if (importe_costo_financiero_signo != null && importe_costo_financiero_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_costo_financiero_signo = importe_costo_financiero_signo.substring(0,32);
}
set_Value ("importe_costo_financiero_signo", importe_costo_financiero_signo);
}
/** Get importe_costo_financiero_signo */
public String getimporte_costo_financiero_signo() 
{
return (String)get_Value("importe_costo_financiero_signo");
}
/** Set importe_costo_tasa_dta */
public void setimporte_costo_tasa_dta (String importe_costo_tasa_dta)
{
if (importe_costo_tasa_dta != null && importe_costo_tasa_dta.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_costo_tasa_dta = importe_costo_tasa_dta.substring(0,32);
}
set_Value ("importe_costo_tasa_dta", importe_costo_tasa_dta);
}
/** Get importe_costo_tasa_dta */
public String getimporte_costo_tasa_dta() 
{
return (String)get_Value("importe_costo_tasa_dta");
}
/** Set importe_costo_tasa_dta_signo */
public void setimporte_costo_tasa_dta_signo (String importe_costo_tasa_dta_signo)
{
if (importe_costo_tasa_dta_signo != null && importe_costo_tasa_dta_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_costo_tasa_dta_signo = importe_costo_tasa_dta_signo.substring(0,32);
}
set_Value ("importe_costo_tasa_dta_signo", importe_costo_tasa_dta_signo);
}
/** Get importe_costo_tasa_dta_signo */
public String getimporte_costo_tasa_dta_signo() 
{
return (String)get_Value("importe_costo_tasa_dta_signo");
}
/** Set importe_total */
public void setimporte_total (String importe_total)
{
if (importe_total != null && importe_total.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_total = importe_total.substring(0,32);
}
set_Value ("importe_total", importe_total);
}
/** Get importe_total */
public String getimporte_total() 
{
return (String)get_Value("importe_total");
}
/** Set importe_total_signo */
public void setimporte_total_signo (String importe_total_signo)
{
if (importe_total_signo != null && importe_total_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
importe_total_signo = importe_total_signo.substring(0,32);
}
set_Value ("importe_total_signo", importe_total_signo);
}
/** Get importe_total_signo */
public String getimporte_total_signo() 
{
return (String)get_Value("importe_total_signo");
}
/** Set imp_sintereses_ley_25063 */
public void setimp_sintereses_ley_25063 (String imp_sintereses_ley_25063)
{
if (imp_sintereses_ley_25063 != null && imp_sintereses_ley_25063.length() > 32)
{
log.warning("Length > 32 - truncated");
imp_sintereses_ley_25063 = imp_sintereses_ley_25063.substring(0,32);
}
set_Value ("imp_sintereses_ley_25063", imp_sintereses_ley_25063);
}
/** Get imp_sintereses_ley_25063 */
public String getimp_sintereses_ley_25063() 
{
return (String)get_Value("imp_sintereses_ley_25063");
}
/** Set imp_sintereses_ley_25063_signo */
public void setimp_sintereses_ley_25063_signo (String imp_sintereses_ley_25063_signo)
{
if (imp_sintereses_ley_25063_signo != null && imp_sintereses_ley_25063_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
imp_sintereses_ley_25063_signo = imp_sintereses_ley_25063_signo.substring(0,32);
}
set_Value ("imp_sintereses_ley_25063_signo", imp_sintereses_ley_25063_signo);
}
/** Get imp_sintereses_ley_25063_signo */
public String getimp_sintereses_ley_25063_signo() 
{
return (String)get_Value("imp_sintereses_ley_25063_signo");
}
/** Set impuesto_deb_cred */
public void setimpuesto_deb_cred (String impuesto_deb_cred)
{
if (impuesto_deb_cred != null && impuesto_deb_cred.length() > 32)
{
log.warning("Length > 32 - truncated");
impuesto_deb_cred = impuesto_deb_cred.substring(0,32);
}
set_Value ("impuesto_deb_cred", impuesto_deb_cred);
}
/** Get impuesto_deb_cred */
public String getimpuesto_deb_cred() 
{
return (String)get_Value("impuesto_deb_cred");
}
/** Set impuesto_deb_cred_signo */
public void setimpuesto_deb_cred_signo (String impuesto_deb_cred_signo)
{
if (impuesto_deb_cred_signo != null && impuesto_deb_cred_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
impuesto_deb_cred_signo = impuesto_deb_cred_signo.substring(0,32);
}
set_Value ("impuesto_deb_cred_signo", impuesto_deb_cred_signo);
}
/** Get impuesto_deb_cred_signo */
public String getimpuesto_deb_cred_signo() 
{
return (String)get_Value("impuesto_deb_cred_signo");
}
/** Set iva_arancel */
public void setiva_arancel (String iva_arancel)
{
if (iva_arancel != null && iva_arancel.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_arancel = iva_arancel.substring(0,32);
}
set_Value ("iva_arancel", iva_arancel);
}
/** Get iva_arancel */
public String getiva_arancel() 
{
return (String)get_Value("iva_arancel");
}
/** Set iva_aranceles_ri */
public void setiva_aranceles_ri (String iva_aranceles_ri)
{
if (iva_aranceles_ri != null && iva_aranceles_ri.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_aranceles_ri = iva_aranceles_ri.substring(0,32);
}
set_Value ("iva_aranceles_ri", iva_aranceles_ri);
}
/** Get iva_aranceles_ri */
public String getiva_aranceles_ri() 
{
return (String)get_Value("iva_aranceles_ri");
}
/** Set iva_aranceles_ri_signo */
public void setiva_aranceles_ri_signo (String iva_aranceles_ri_signo)
{
if (iva_aranceles_ri_signo != null && iva_aranceles_ri_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_aranceles_ri_signo = iva_aranceles_ri_signo.substring(0,32);
}
set_Value ("iva_aranceles_ri_signo", iva_aranceles_ri_signo);
}
/** Get iva_aranceles_ri_signo */
public String getiva_aranceles_ri_signo() 
{
return (String)get_Value("iva_aranceles_ri_signo");
}
/** Set iva_arancel_signo */
public void setiva_arancel_signo (String iva_arancel_signo)
{
if (iva_arancel_signo != null && iva_arancel_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_arancel_signo = iva_arancel_signo.substring(0,32);
}
set_Value ("iva_arancel_signo", iva_arancel_signo);
}
/** Get iva_arancel_signo */
public String getiva_arancel_signo() 
{
return (String)get_Value("iva_arancel_signo");
}
/** Set iva_costo_financiero */
public void setiva_costo_financiero (String iva_costo_financiero)
{
if (iva_costo_financiero != null && iva_costo_financiero.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_costo_financiero = iva_costo_financiero.substring(0,32);
}
set_Value ("iva_costo_financiero", iva_costo_financiero);
}
/** Get iva_costo_financiero */
public String getiva_costo_financiero() 
{
return (String)get_Value("iva_costo_financiero");
}
/** Set iva_costo_financiero_signo */
public void setiva_costo_financiero_signo (String iva_costo_financiero_signo)
{
if (iva_costo_financiero_signo != null && iva_costo_financiero_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_costo_financiero_signo = iva_costo_financiero_signo.substring(0,32);
}
set_Value ("iva_costo_financiero_signo", iva_costo_financiero_signo);
}
/** Get iva_costo_financiero_signo */
public String getiva_costo_financiero_signo() 
{
return (String)get_Value("iva_costo_financiero_signo");
}
/** Set iva_costo_tasa_dta */
public void setiva_costo_tasa_dta (String iva_costo_tasa_dta)
{
if (iva_costo_tasa_dta != null && iva_costo_tasa_dta.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_costo_tasa_dta = iva_costo_tasa_dta.substring(0,32);
}
set_Value ("iva_costo_tasa_dta", iva_costo_tasa_dta);
}
/** Get iva_costo_tasa_dta */
public String getiva_costo_tasa_dta() 
{
return (String)get_Value("iva_costo_tasa_dta");
}
/** Set iva_costo_tasa_dta_signo */
public void setiva_costo_tasa_dta_signo (String iva_costo_tasa_dta_signo)
{
if (iva_costo_tasa_dta_signo != null && iva_costo_tasa_dta_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_costo_tasa_dta_signo = iva_costo_tasa_dta_signo.substring(0,32);
}
set_Value ("iva_costo_tasa_dta_signo", iva_costo_tasa_dta_signo);
}
/** Get iva_costo_tasa_dta_signo */
public String getiva_costo_tasa_dta_signo() 
{
return (String)get_Value("iva_costo_tasa_dta_signo");
}
/** Set iva_dto_pago_anticipado */
public void setiva_dto_pago_anticipado (String iva_dto_pago_anticipado)
{
if (iva_dto_pago_anticipado != null && iva_dto_pago_anticipado.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_dto_pago_anticipado = iva_dto_pago_anticipado.substring(0,32);
}
set_Value ("iva_dto_pago_anticipado", iva_dto_pago_anticipado);
}
/** Get iva_dto_pago_anticipado */
public String getiva_dto_pago_anticipado() 
{
return (String)get_Value("iva_dto_pago_anticipado");
}
/** Set iva_dto_pago_anticipado_signo */
public void setiva_dto_pago_anticipado_signo (String iva_dto_pago_anticipado_signo)
{
if (iva_dto_pago_anticipado_signo != null && iva_dto_pago_anticipado_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_dto_pago_anticipado_signo = iva_dto_pago_anticipado_signo.substring(0,32);
}
set_Value ("iva_dto_pago_anticipado_signo", iva_dto_pago_anticipado_signo);
}
/** Get iva_dto_pago_anticipado_signo */
public String getiva_dto_pago_anticipado_signo() 
{
return (String)get_Value("iva_dto_pago_anticipado_signo");
}
/** Set iva_servicios */
public void setiva_servicios (String iva_servicios)
{
if (iva_servicios != null && iva_servicios.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_servicios = iva_servicios.substring(0,32);
}
set_Value ("iva_servicios", iva_servicios);
}
/** Get iva_servicios */
public String getiva_servicios() 
{
return (String)get_Value("iva_servicios");
}
/** Set iva_servicios_signo */
public void setiva_servicios_signo (String iva_servicios_signo)
{
if (iva_servicios_signo != null && iva_servicios_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
iva_servicios_signo = iva_servicios_signo.substring(0,32);
}
set_Value ("iva_servicios_signo", iva_servicios_signo);
}
/** Get iva_servicios_signo */
public String getiva_servicios_signo() 
{
return (String)get_Value("iva_servicios_signo");
}
/** Set marca_campana */
public void setmarca_campana (String marca_campana)
{
if (marca_campana != null && marca_campana.length() > 32)
{
log.warning("Length > 32 - truncated");
marca_campana = marca_campana.substring(0,32);
}
set_Value ("marca_campana", marca_campana);
}
/** Get marca_campana */
public String getmarca_campana() 
{
return (String)get_Value("marca_campana");
}
/** Set marca_cashback */
public void setmarca_cashback (String marca_cashback)
{
if (marca_cashback != null && marca_cashback.length() > 32)
{
log.warning("Length > 32 - truncated");
marca_cashback = marca_cashback.substring(0,32);
}
set_Value ("marca_cashback", marca_cashback);
}
/** Get marca_cashback */
public String getmarca_cashback() 
{
return (String)get_Value("marca_cashback");
}
/** Set marca_error */
public void setmarca_error (String marca_error)
{
if (marca_error != null && marca_error.length() > 32)
{
log.warning("Length > 32 - truncated");
marca_error = marca_error.substring(0,32);
}
set_Value ("marca_error", marca_error);
}
/** Get marca_error */
public String getmarca_error() 
{
return (String)get_Value("marca_error");
}
/** Set Moneda */
public void setMoneda (String Moneda)
{
if (Moneda != null && Moneda.length() > 32)
{
log.warning("Length > 32 - truncated");
Moneda = Moneda.substring(0,32);
}
set_Value ("Moneda", Moneda);
}
/** Get Moneda */
public String getMoneda() 
{
return (String)get_Value("Moneda");
}
/** Set monto_pend_cuotas */
public void setmonto_pend_cuotas (String monto_pend_cuotas)
{
if (monto_pend_cuotas != null && monto_pend_cuotas.length() > 32)
{
log.warning("Length > 32 - truncated");
monto_pend_cuotas = monto_pend_cuotas.substring(0,32);
}
set_Value ("monto_pend_cuotas", monto_pend_cuotas);
}
/** Get monto_pend_cuotas */
public String getmonto_pend_cuotas() 
{
return (String)get_Value("monto_pend_cuotas");
}
/** Set monto_pend_cuotas_signo */
public void setmonto_pend_cuotas_signo (String monto_pend_cuotas_signo)
{
if (monto_pend_cuotas_signo != null && monto_pend_cuotas_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
monto_pend_cuotas_signo = monto_pend_cuotas_signo.substring(0,32);
}
set_Value ("monto_pend_cuotas_signo", monto_pend_cuotas_signo);
}
/** Get monto_pend_cuotas_signo */
public String getmonto_pend_cuotas_signo() 
{
return (String)get_Value("monto_pend_cuotas_signo");
}
/** Set motivo_rechazo_1 */
public void setmotivo_rechazo_1 (String motivo_rechazo_1)
{
if (motivo_rechazo_1 != null && motivo_rechazo_1.length() > 32)
{
log.warning("Length > 32 - truncated");
motivo_rechazo_1 = motivo_rechazo_1.substring(0,32);
}
set_Value ("motivo_rechazo_1", motivo_rechazo_1);
}
/** Get motivo_rechazo_1 */
public String getmotivo_rechazo_1() 
{
return (String)get_Value("motivo_rechazo_1");
}
/** Set motivo_rechazo_2 */
public void setmotivo_rechazo_2 (String motivo_rechazo_2)
{
if (motivo_rechazo_2 != null && motivo_rechazo_2.length() > 32)
{
log.warning("Length > 32 - truncated");
motivo_rechazo_2 = motivo_rechazo_2.substring(0,32);
}
set_Value ("motivo_rechazo_2", motivo_rechazo_2);
}
/** Get motivo_rechazo_2 */
public String getmotivo_rechazo_2() 
{
return (String)get_Value("motivo_rechazo_2");
}
/** Set motivo_rechazo_3 */
public void setmotivo_rechazo_3 (String motivo_rechazo_3)
{
if (motivo_rechazo_3 != null && motivo_rechazo_3.length() > 32)
{
log.warning("Length > 32 - truncated");
motivo_rechazo_3 = motivo_rechazo_3.substring(0,32);
}
set_Value ("motivo_rechazo_3", motivo_rechazo_3);
}
/** Get motivo_rechazo_3 */
public String getmotivo_rechazo_3() 
{
return (String)get_Value("motivo_rechazo_3");
}
/** Set motivo_rechazo_4 */
public void setmotivo_rechazo_4 (String motivo_rechazo_4)
{
if (motivo_rechazo_4 != null && motivo_rechazo_4.length() > 32)
{
log.warning("Length > 32 - truncated");
motivo_rechazo_4 = motivo_rechazo_4.substring(0,32);
}
set_Value ("motivo_rechazo_4", motivo_rechazo_4);
}
/** Get motivo_rechazo_4 */
public String getmotivo_rechazo_4() 
{
return (String)get_Value("motivo_rechazo_4");
}
/** Set motivo_reversion */
public void setmotivo_reversion (String motivo_reversion)
{
if (motivo_reversion != null && motivo_reversion.length() > 32)
{
log.warning("Length > 32 - truncated");
motivo_reversion = motivo_reversion.substring(0,32);
}
set_Value ("motivo_reversion", motivo_reversion);
}
/** Get motivo_reversion */
public String getmotivo_reversion() 
{
return (String)get_Value("motivo_reversion");
}
/** Set neto_comercios */
public void setneto_comercios (String neto_comercios)
{
if (neto_comercios != null && neto_comercios.length() > 32)
{
log.warning("Length > 32 - truncated");
neto_comercios = neto_comercios.substring(0,32);
}
set_Value ("neto_comercios", neto_comercios);
}
/** Get neto_comercios */
public String getneto_comercios() 
{
return (String)get_Value("neto_comercios");
}
/** Set neto_comercios_signo */
public void setneto_comercios_signo (String neto_comercios_signo)
{
if (neto_comercios_signo != null && neto_comercios_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
neto_comercios_signo = neto_comercios_signo.substring(0,32);
}
set_Value ("neto_comercios_signo", neto_comercios_signo);
}
/** Get neto_comercios_signo */
public String getneto_comercios_signo() 
{
return (String)get_Value("neto_comercios_signo");
}
/** Set nombre_archivo */
public void setnombre_archivo (String nombre_archivo)
{
if (nombre_archivo != null && nombre_archivo.length() > 32)
{
log.warning("Length > 32 - truncated");
nombre_archivo = nombre_archivo.substring(0,32);
}
set_Value ("nombre_archivo", nombre_archivo);
}
/** Get nombre_archivo */
public String getnombre_archivo() 
{
return (String)get_Value("nombre_archivo");
}
/** Set nro_autoriz */
public void setnro_autoriz (String nro_autoriz)
{
if (nro_autoriz != null && nro_autoriz.length() > 32)
{
log.warning("Length > 32 - truncated");
nro_autoriz = nro_autoriz.substring(0,32);
}
set_Value ("nro_autoriz", nro_autoriz);
}
/** Get nro_autoriz */
public String getnro_autoriz() 
{
return (String)get_Value("nro_autoriz");
}
/** Set nro_tarjeta */
public void setnro_tarjeta (String nro_tarjeta)
{
if (nro_tarjeta != null && nro_tarjeta.length() > 32)
{
log.warning("Length > 32 - truncated");
nro_tarjeta = nro_tarjeta.substring(0,32);
}
set_Value ("nro_tarjeta", nro_tarjeta);
}
/** Get nro_tarjeta */
public String getnro_tarjeta() 
{
return (String)get_Value("nro_tarjeta");
}
/** Set numero_liquidacion */
public void setnumero_liquidacion (String numero_liquidacion)
{
if (numero_liquidacion != null && numero_liquidacion.length() > 32)
{
log.warning("Length > 32 - truncated");
numero_liquidacion = numero_liquidacion.substring(0,32);
}
set_Value ("numero_liquidacion", numero_liquidacion);
}
/** Get numero_liquidacion */
public String getnumero_liquidacion() 
{
return (String)get_Value("numero_liquidacion");
}
/** Set otros_creditos */
public void setotros_creditos (String otros_creditos)
{
if (otros_creditos != null && otros_creditos.length() > 32)
{
log.warning("Length > 32 - truncated");
otros_creditos = otros_creditos.substring(0,32);
}
set_Value ("otros_creditos", otros_creditos);
}
/** Get otros_creditos */
public String getotros_creditos() 
{
return (String)get_Value("otros_creditos");
}
/** Set otros_creditos_signo */
public void setotros_creditos_signo (String otros_creditos_signo)
{
if (otros_creditos_signo != null && otros_creditos_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
otros_creditos_signo = otros_creditos_signo.substring(0,32);
}
set_Value ("otros_creditos_signo", otros_creditos_signo);
}
/** Get otros_creditos_signo */
public String getotros_creditos_signo() 
{
return (String)get_Value("otros_creditos_signo");
}
/** Set otros_debitos */
public void setotros_debitos (String otros_debitos)
{
if (otros_debitos != null && otros_debitos.length() > 32)
{
log.warning("Length > 32 - truncated");
otros_debitos = otros_debitos.substring(0,32);
}
set_Value ("otros_debitos", otros_debitos);
}
/** Get otros_debitos */
public String getotros_debitos() 
{
return (String)get_Value("otros_debitos");
}
/** Set otros_debitos_signo */
public void setotros_debitos_signo (String otros_debitos_signo)
{
if (otros_debitos_signo != null && otros_debitos_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
otros_debitos_signo = otros_debitos_signo.substring(0,32);
}
set_Value ("otros_debitos_signo", otros_debitos_signo);
}
/** Get otros_debitos_signo */
public String getotros_debitos_signo() 
{
return (String)get_Value("otros_debitos_signo");
}
/** Set percepc_iva_r3337 */
public void setpercepc_iva_r3337 (String percepc_iva_r3337)
{
if (percepc_iva_r3337 != null && percepc_iva_r3337.length() > 32)
{
log.warning("Length > 32 - truncated");
percepc_iva_r3337 = percepc_iva_r3337.substring(0,32);
}
set_Value ("percepc_iva_r3337", percepc_iva_r3337);
}
/** Get percepc_iva_r3337 */
public String getpercepc_iva_r3337() 
{
return (String)get_Value("percepc_iva_r3337");
}
/** Set percepc_iva_r3337_signo */
public void setpercepc_iva_r3337_signo (String percepc_iva_r3337_signo)
{
if (percepc_iva_r3337_signo != null && percepc_iva_r3337_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
percepc_iva_r3337_signo = percepc_iva_r3337_signo.substring(0,32);
}
set_Value ("percepc_iva_r3337_signo", percepc_iva_r3337_signo);
}
/** Get percepc_iva_r3337_signo */
public String getpercepc_iva_r3337_signo() 
{
return (String)get_Value("percepc_iva_r3337_signo");
}
/** Set percep_ingr_brutos */
public void setpercep_ingr_brutos (String percep_ingr_brutos)
{
if (percep_ingr_brutos != null && percep_ingr_brutos.length() > 32)
{
log.warning("Length > 32 - truncated");
percep_ingr_brutos = percep_ingr_brutos.substring(0,32);
}
set_Value ("percep_ingr_brutos", percep_ingr_brutos);
}
/** Get percep_ingr_brutos */
public String getpercep_ingr_brutos() 
{
return (String)get_Value("percep_ingr_brutos");
}
/** Set percep_ingr_brutos_signo */
public void setpercep_ingr_brutos_signo (String percep_ingr_brutos_signo)
{
if (percep_ingr_brutos_signo != null && percep_ingr_brutos_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
percep_ingr_brutos_signo = percep_ingr_brutos_signo.substring(0,32);
}
set_Value ("percep_ingr_brutos_signo", percep_ingr_brutos_signo);
}
/** Get percep_ingr_brutos_signo */
public String getpercep_ingr_brutos_signo() 
{
return (String)get_Value("percep_ingr_brutos_signo");
}
/** Set plazo_pago */
public void setplazo_pago (String plazo_pago)
{
if (plazo_pago != null && plazo_pago.length() > 32)
{
log.warning("Length > 32 - truncated");
plazo_pago = plazo_pago.substring(0,32);
}
set_Value ("plazo_pago", plazo_pago);
}
/** Get plazo_pago */
public String getplazo_pago() 
{
return (String)get_Value("plazo_pago");
}
/** Set porc_desc */
public void setporc_desc (String porc_desc)
{
if (porc_desc != null && porc_desc.length() > 32)
{
log.warning("Length > 32 - truncated");
porc_desc = porc_desc.substring(0,32);
}
set_Value ("porc_desc", porc_desc);
}
/** Get porc_desc */
public String getporc_desc() 
{
return (String)get_Value("porc_desc");
}
/** Set porcentaje_tasa_directa */
public void setporcentaje_tasa_directa (String porcentaje_tasa_directa)
{
if (porcentaje_tasa_directa != null && porcentaje_tasa_directa.length() > 32)
{
log.warning("Length > 32 - truncated");
porcentaje_tasa_directa = porcentaje_tasa_directa.substring(0,32);
}
set_Value ("porcentaje_tasa_directa", porcentaje_tasa_directa);
}
/** Get porcentaje_tasa_directa */
public String getporcentaje_tasa_directa() 
{
return (String)get_Value("porcentaje_tasa_directa");
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
/** Set producto */
public void setproducto (String producto)
{
if (producto != null && producto.length() > 32)
{
log.warning("Length > 32 - truncated");
producto = producto.substring(0,32);
}
set_Value ("producto", producto);
}
/** Get producto */
public String getproducto() 
{
return (String)get_Value("producto");
}
/** Set promocion_cuotas_alfa */
public void setpromocion_cuotas_alfa (String promocion_cuotas_alfa)
{
if (promocion_cuotas_alfa != null && promocion_cuotas_alfa.length() > 32)
{
log.warning("Length > 32 - truncated");
promocion_cuotas_alfa = promocion_cuotas_alfa.substring(0,32);
}
set_Value ("promocion_cuotas_alfa", promocion_cuotas_alfa);
}
/** Get promocion_cuotas_alfa */
public String getpromocion_cuotas_alfa() 
{
return (String)get_Value("promocion_cuotas_alfa");
}
/** Set provincia_ing_brutos */
public void setprovincia_ing_brutos (String provincia_ing_brutos)
{
if (provincia_ing_brutos != null && provincia_ing_brutos.length() > 32)
{
log.warning("Length > 32 - truncated");
provincia_ing_brutos = provincia_ing_brutos.substring(0,32);
}
set_Value ("provincia_ing_brutos", provincia_ing_brutos);
}
/** Get provincia_ing_brutos */
public String getprovincia_ing_brutos() 
{
return (String)get_Value("provincia_ing_brutos");
}
/** Set resumen_lote_posnet */
public void setresumen_lote_posnet (String resumen_lote_posnet)
{
if (resumen_lote_posnet != null && resumen_lote_posnet.length() > 32)
{
log.warning("Length > 32 - truncated");
resumen_lote_posnet = resumen_lote_posnet.substring(0,32);
}
set_Value ("resumen_lote_posnet", resumen_lote_posnet);
}
/** Get resumen_lote_posnet */
public String getresumen_lote_posnet() 
{
return (String)get_Value("resumen_lote_posnet");
}
/** Set retenciones_fiscales */
public void setretenciones_fiscales (String retenciones_fiscales)
{
if (retenciones_fiscales != null && retenciones_fiscales.length() > 32)
{
log.warning("Length > 32 - truncated");
retenciones_fiscales = retenciones_fiscales.substring(0,32);
}
set_Value ("retenciones_fiscales", retenciones_fiscales);
}
/** Get retenciones_fiscales */
public String getretenciones_fiscales() 
{
return (String)get_Value("retenciones_fiscales");
}
/** Set retenciones_fiscales_signo */
public void setretenciones_fiscales_signo (String retenciones_fiscales_signo)
{
if (retenciones_fiscales_signo != null && retenciones_fiscales_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
retenciones_fiscales_signo = retenciones_fiscales_signo.substring(0,32);
}
set_Value ("retenciones_fiscales_signo", retenciones_fiscales_signo);
}
/** Get retenciones_fiscales_signo */
public String getretenciones_fiscales_signo() 
{
return (String)get_Value("retenciones_fiscales_signo");
}
/** Set ret_imp_ganancias */
public void setret_imp_ganancias (String ret_imp_ganancias)
{
if (ret_imp_ganancias != null && ret_imp_ganancias.length() > 32)
{
log.warning("Length > 32 - truncated");
ret_imp_ganancias = ret_imp_ganancias.substring(0,32);
}
set_Value ("ret_imp_ganancias", ret_imp_ganancias);
}
/** Get ret_imp_ganancias */
public String getret_imp_ganancias() 
{
return (String)get_Value("ret_imp_ganancias");
}
/** Set ret_imp_ganancias_signo */
public void setret_imp_ganancias_signo (String ret_imp_ganancias_signo)
{
if (ret_imp_ganancias_signo != null && ret_imp_ganancias_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
ret_imp_ganancias_signo = ret_imp_ganancias_signo.substring(0,32);
}
set_Value ("ret_imp_ganancias_signo", ret_imp_ganancias_signo);
}
/** Get ret_imp_ganancias_signo */
public String getret_imp_ganancias_signo() 
{
return (String)get_Value("ret_imp_ganancias_signo");
}
/** Set ret_imp_ingresos_brutos */
public void setret_imp_ingresos_brutos (String ret_imp_ingresos_brutos)
{
if (ret_imp_ingresos_brutos != null && ret_imp_ingresos_brutos.length() > 32)
{
log.warning("Length > 32 - truncated");
ret_imp_ingresos_brutos = ret_imp_ingresos_brutos.substring(0,32);
}
set_Value ("ret_imp_ingresos_brutos", ret_imp_ingresos_brutos);
}
/** Get ret_imp_ingresos_brutos */
public String getret_imp_ingresos_brutos() 
{
return (String)get_Value("ret_imp_ingresos_brutos");
}
/** Set ret_imp_ingresos_brutos_signo */
public void setret_imp_ingresos_brutos_signo (String ret_imp_ingresos_brutos_signo)
{
if (ret_imp_ingresos_brutos_signo != null && ret_imp_ingresos_brutos_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
ret_imp_ingresos_brutos_signo = ret_imp_ingresos_brutos_signo.substring(0,32);
}
set_Value ("ret_imp_ingresos_brutos_signo", ret_imp_ingresos_brutos_signo);
}
/** Get ret_imp_ingresos_brutos_signo */
public String getret_imp_ingresos_brutos_signo() 
{
return (String)get_Value("ret_imp_ingresos_brutos_signo");
}
/** Set ret_iva_ventas */
public void setret_iva_ventas (String ret_iva_ventas)
{
if (ret_iva_ventas != null && ret_iva_ventas.length() > 32)
{
log.warning("Length > 32 - truncated");
ret_iva_ventas = ret_iva_ventas.substring(0,32);
}
set_Value ("ret_iva_ventas", ret_iva_ventas);
}
/** Get ret_iva_ventas */
public String getret_iva_ventas() 
{
return (String)get_Value("ret_iva_ventas");
}
/** Set ret_iva_ventas_signo */
public void setret_iva_ventas_signo (String ret_iva_ventas_signo)
{
if (ret_iva_ventas_signo != null && ret_iva_ventas_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
ret_iva_ventas_signo = ret_iva_ventas_signo.substring(0,32);
}
set_Value ("ret_iva_ventas_signo", ret_iva_ventas_signo);
}
/** Get ret_iva_ventas_signo */
public String getret_iva_ventas_signo() 
{
return (String)get_Value("ret_iva_ventas_signo");
}
/** Set revisado */
public void setrevisado (String revisado)
{
if (revisado != null && revisado.length() > 32)
{
log.warning("Length > 32 - truncated");
revisado = revisado.substring(0,32);
}
set_Value ("revisado", revisado);
}
/** Get revisado */
public String getrevisado() 
{
return (String)get_Value("revisado");
}
/** Set subtipo_registro */
public void setsubtipo_registro (String subtipo_registro)
{
if (subtipo_registro != null && subtipo_registro.length() > 32)
{
log.warning("Length > 32 - truncated");
subtipo_registro = subtipo_registro.substring(0,32);
}
set_Value ("subtipo_registro", subtipo_registro);
}
/** Get subtipo_registro */
public String getsubtipo_registro() 
{
return (String)get_Value("subtipo_registro");
}
/** Set sucursal_pagadora */
public void setsucursal_pagadora (String sucursal_pagadora)
{
if (sucursal_pagadora != null && sucursal_pagadora.length() > 32)
{
log.warning("Length > 32 - truncated");
sucursal_pagadora = sucursal_pagadora.substring(0,32);
}
set_Value ("sucursal_pagadora", sucursal_pagadora);
}
/** Get sucursal_pagadora */
public String getsucursal_pagadora() 
{
return (String)get_Value("sucursal_pagadora");
}
/** Set tipo_operacion */
public void settipo_operacion (String tipo_operacion)
{
if (tipo_operacion != null && tipo_operacion.length() > 32)
{
log.warning("Length > 32 - truncated");
tipo_operacion = tipo_operacion.substring(0,32);
}
set_Value ("tipo_operacion", tipo_operacion);
}
/** Get tipo_operacion */
public String gettipo_operacion() 
{
return (String)get_Value("tipo_operacion");
}
/** Set tipo_plan_cuotas */
public void settipo_plan_cuotas (String tipo_plan_cuotas)
{
if (tipo_plan_cuotas != null && tipo_plan_cuotas.length() > 32)
{
log.warning("Length > 32 - truncated");
tipo_plan_cuotas = tipo_plan_cuotas.substring(0,32);
}
set_Value ("tipo_plan_cuotas", tipo_plan_cuotas);
}
/** Get tipo_plan_cuotas */
public String gettipo_plan_cuotas() 
{
return (String)get_Value("tipo_plan_cuotas");
}
/** Set tipo_plazo_pago */
public void settipo_plazo_pago (String tipo_plazo_pago)
{
if (tipo_plazo_pago != null && tipo_plazo_pago.length() > 32)
{
log.warning("Length > 32 - truncated");
tipo_plazo_pago = tipo_plazo_pago.substring(0,32);
}
set_Value ("tipo_plazo_pago", tipo_plazo_pago);
}
/** Get tipo_plazo_pago */
public String gettipo_plazo_pago() 
{
return (String)get_Value("tipo_plazo_pago");
}
/** Set tipo_registro */
public void settipo_registro (String tipo_registro)
{
if (tipo_registro != null && tipo_registro.length() > 32)
{
log.warning("Length > 32 - truncated");
tipo_registro = tipo_registro.substring(0,32);
}
set_Value ("tipo_registro", tipo_registro);
}
/** Get tipo_registro */
public String gettipo_registro() 
{
return (String)get_Value("tipo_registro");
}
/** Set tna */
public void settna (String tna)
{
if (tna != null && tna.length() > 32)
{
log.warning("Length > 32 - truncated");
tna = tna.substring(0,32);
}
set_Value ("tna", tna);
}
/** Get tna */
public String gettna() 
{
return (String)get_Value("tna");
}
/** Set total_importe_final */
public void settotal_importe_final (String total_importe_final)
{
if (total_importe_final != null && total_importe_final.length() > 32)
{
log.warning("Length > 32 - truncated");
total_importe_final = total_importe_final.substring(0,32);
}
set_Value ("total_importe_final", total_importe_final);
}
/** Get total_importe_final */
public String gettotal_importe_final() 
{
return (String)get_Value("total_importe_final");
}
/** Set total_importe_final_signo */
public void settotal_importe_final_signo (String total_importe_final_signo)
{
if (total_importe_final_signo != null && total_importe_final_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
total_importe_final_signo = total_importe_final_signo.substring(0,32);
}
set_Value ("total_importe_final_signo", total_importe_final_signo);
}
/** Get total_importe_final_signo */
public String gettotal_importe_final_signo() 
{
return (String)get_Value("total_importe_final_signo");
}
/** Set total_importe_sin_dto */
public void settotal_importe_sin_dto (String total_importe_sin_dto)
{
if (total_importe_sin_dto != null && total_importe_sin_dto.length() > 32)
{
log.warning("Length > 32 - truncated");
total_importe_sin_dto = total_importe_sin_dto.substring(0,32);
}
set_Value ("total_importe_sin_dto", total_importe_sin_dto);
}
/** Get total_importe_sin_dto */
public String gettotal_importe_sin_dto() 
{
return (String)get_Value("total_importe_sin_dto");
}
/** Set total_importe_sin_dto_signo */
public void settotal_importe_sin_dto_signo (String total_importe_sin_dto_signo)
{
if (total_importe_sin_dto_signo != null && total_importe_sin_dto_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
total_importe_sin_dto_signo = total_importe_sin_dto_signo.substring(0,32);
}
set_Value ("total_importe_sin_dto_signo", total_importe_sin_dto_signo);
}
/** Get total_importe_sin_dto_signo */
public String gettotal_importe_sin_dto_signo() 
{
return (String)get_Value("total_importe_sin_dto_signo");
}
/** Set total_importe_total */
public void settotal_importe_total (String total_importe_total)
{
if (total_importe_total != null && total_importe_total.length() > 32)
{
log.warning("Length > 32 - truncated");
total_importe_total = total_importe_total.substring(0,32);
}
set_Value ("total_importe_total", total_importe_total);
}
/** Get total_importe_total */
public String gettotal_importe_total() 
{
return (String)get_Value("total_importe_total");
}
/** Set total_importe_total_signo */
public void settotal_importe_total_signo (String total_importe_total_signo)
{
if (total_importe_total_signo != null && total_importe_total_signo.length() > 32)
{
log.warning("Length > 32 - truncated");
total_importe_total_signo = total_importe_total_signo.substring(0,32);
}
set_Value ("total_importe_total_signo", total_importe_total_signo);
}
/** Get total_importe_total_signo */
public String gettotal_importe_total_signo() 
{
return (String)get_Value("total_importe_total_signo");
}
/** Set total_registros_detalle */
public void settotal_registros_detalle (String total_registros_detalle)
{
if (total_registros_detalle != null && total_registros_detalle.length() > 32)
{
log.warning("Length > 32 - truncated");
total_registros_detalle = total_registros_detalle.substring(0,32);
}
set_Value ("total_registros_detalle", total_registros_detalle);
}
/** Get total_registros_detalle */
public String gettotal_registros_detalle() 
{
return (String)get_Value("total_registros_detalle");
}
}
