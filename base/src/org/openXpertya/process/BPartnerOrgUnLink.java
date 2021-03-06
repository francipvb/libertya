/*
 *    El contenido de este fichero está sujeto a la  Licencia Pública openXpertya versión 1.1 (LPO)
 * en tanto en cuanto forme parte íntegra del total del producto denominado:  openXpertya, solución 
 * empresarial global , y siempre según los términos de dicha licencia LPO.
 *    Una copia  íntegra de dicha  licencia está incluida con todas  las fuentes del producto.
 *    Partes del código son CopyRight (c) 2002-2007 de Ingeniería Informática Integrada S.L., otras 
 * partes son  CopyRight (c) 2002-2007 de  Consultoría y  Soporte en  Redes y  Tecnologías  de  la
 * Información S.L.,  otras partes son  adaptadas, ampliadas,  traducidas, revisadas  y/o mejoradas
 * a partir de código original de  terceros, recogidos en el  ADDENDUM  A, sección 3 (A.3) de dicha
 * licencia  LPO,  y si dicho código es extraido como parte del total del producto, estará sujeto a
 * su respectiva licencia original.  
 *     Más información en http://www.openxpertya.org/ayuda/Licencia.html
 */



package org.openXpertya.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.openXpertya.model.MBPartner;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */

public class BPartnerOrgUnLink extends SvrProcess {

    /** Descripción de Campos */

    private int p_C_BPartner_ID;

    /**
     * Descripción de Método
     *
     */

    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();

        for( int i = 0;i < para.length;i++ ) {
            String name = para[ i ].getParameterName();

            if( para[ i ].getParameter() == null ) {
                ;
            } else if( name.equals( "C_BPartner_ID" )) {
                p_C_BPartner_ID = (( BigDecimal )para[ i ].getParameter()).intValue();
            } else {
                log.log( Level.SEVERE,"Unknown Parameter: " + name );
            }
        }
    }    // prepare

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws Exception
     */

    protected String doIt() throws Exception {
        log.info( "doIt - C_BPartner_ID=" + p_C_BPartner_ID );

        if( p_C_BPartner_ID == 0 ) {
            throw new IllegalArgumentException( "No Business Partner ID" );
        }

        MBPartner bp = new MBPartner( getCtx(),p_C_BPartner_ID,get_TrxName());

        if( bp.getID() == 0 ) {
            throw new IllegalArgumentException( "Business Partner not found - C_BPartner_ID=" + p_C_BPartner_ID );
        }

        //

        if( bp.getAD_OrgBP_ID_Int() == 0 ) {
            throw new IllegalArgumentException( "Business Partner not linked to an Organization" );
        }

        bp.setAD_OrgBP_ID( null );

        if( !bp.save()) {
            throw new IllegalArgumentException( "Business Partner not changed" );
        }

        return "OK";
    }    // doIt
}    // BPartnerOrgUnLink



/*
 *  @(#)BPartnerOrgUnLink.java   02.07.07
 * 
 *  Fin del fichero BPartnerOrgUnLink.java
 *  
 *  Versión 2.2
 *
 */
