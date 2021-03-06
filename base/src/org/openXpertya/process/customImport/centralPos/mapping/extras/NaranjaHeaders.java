package org.openXpertya.process.customImport.centralPos.mapping.extras;

import org.openXpertya.process.customImport.centralPos.mapping.GenericMap;
import org.openXpertya.process.customImport.centralPos.pojos.naranja.headers.Datum;

/**
 * Naranja - Headers
 * @author Kevin Feuerschvenger - Sur Software S.H.
 * @version 1.0
 */
public class NaranjaHeaders extends GenericMap {

	/** Campos a almacenar en la DB. */
	public static String[] filteredFields = {
			// Asociación con Detalle
			"comercio", // Comercio.
			"fecha_pago", // Fecha de pago.
			// Info
			"nro_liquidacion", // Número de la liquidación.
			"neto", // Monto neto a liquidar.
			"signo_neto", // Signo monto neto a liquidar.
			// Retenciones
			"retencion_iva_140", // Importe de la retención I.V.A. RG.
			"signo_ret_iva_140", // Signo importe de la retención I.V.A. RG.
			"retencion_ganancias", // Importe de la retención de ganancias RG 3311.
			"signo_ret_ganancias", // Signo importe de la retención de ganancias RG 3311.
			"ret_ingresos_brutos", // Importe retención de ingresos brutos.
			"signo_ret_ing_brutos" // Signo importe retención de ingresos brutos.
	};

	public NaranjaHeaders(Datum values) {
		super(filteredFields, values, null);
		matchingFields = new String[] { "comercio", "nro_liquidacion", "fecha_pago" };
	}

}
