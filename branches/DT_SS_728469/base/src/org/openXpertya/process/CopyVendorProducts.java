package org.openXpertya.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.openXpertya.model.MProductPO;
import org.openXpertya.model.PO;
import org.openXpertya.util.DB;
import org.openXpertya.util.Msg;

public class CopyVendorProducts extends SvrProcess{
	
	/** Descripción de Campos */

    protected int p_C_BPartner_ID = 0;
    
    protected boolean keepProducts = false;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();

        for( int i = 0;i < para.length;i++ ) {
            String name = para[ i ].getParameterName();

            if( para[ i ].getParameter() == null ) {
                ;
            } else if( name.equals( "C_BPartner_ID" )) {
            	p_C_BPartner_ID = (( BigDecimal )para[ i ].getParameter()).intValue();
            } else if( name.equals( "Keep_Items" )) {
            	keepProducts = ((String)para[i].getParameter()).equalsIgnoreCase("Y"); 
        	} else {
                log.log( Level.SEVERE,"Unknown Parameter: " + name );
            }
        }
		
	}

	@Override
	protected String doIt() throws Exception {
		int to_C_BPartner_ID = getRecord_ID();
		
		//Valido que se haya recuperado bien los IDs de las entidades comerciales
		if (p_C_BPartner_ID == 0 || to_C_BPartner_ID == 0) {
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), "CopyError"));
		}
		
		//Recupero las asociaciones de artículos del proveedor de origen
		List<MProductPO> productsPO = getProductsPO(p_C_BPartner_ID);
		
		//Copio las asociaciones al proveedor destino
		copyLines(productsPO, to_C_BPartner_ID);
		
		//Si corresponde según parámetro, elimino las asociaciones del proveedor origen
		if (!keepProducts) {
			removeProductsPO(productsPO);
		}
		
		return Msg.getMsg(getCtx(), "CopySuccessful");
	}
	
	private List<MProductPO> getProductsPO(int bPartner_ID) {
		List<MProductPO> productsPO = new ArrayList<MProductPO>();
		
		String sql = "SELECT * FROM m_product_po WHERE c_bpartner_id = ? and IsActive = 'Y'";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, get_TrxName());
			ps.setInt(1, bPartner_ID);
			rs = ps.executeQuery();
			while (rs.next()) {
				productsPO.add(new MProductPO(getCtx(), rs, get_TrxName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return productsPO;
	}
	
	private void copyLines(List<MProductPO> productsPO, int to_C_BPartner_ID) {
		if (productsPO == null || productsPO.isEmpty() || to_C_BPartner_ID == 0)
			return;
		
		for (MProductPO productPO : productsPO) {
			MProductPO newProductPO = new MProductPO(getCtx(), 0, get_TrxName());
			PO.copyValues(productPO, newProductPO);
			newProductPO.setC_BPartner_ID(to_C_BPartner_ID);
			//Tuve que poner a mano el productID aunque sea el mismo, porque al ser parte de la clave si no lo ponía da un error el saveNew de la clase PO
			newProductPO.setM_Product_ID(productPO.getM_Product_ID());
			newProductPO.setIsActive(true);
			if (!newProductPO.save()) {
				throw new IllegalArgumentException(Msg.getMsg(getCtx(), "CopyError") + ": " + newProductPO.getProcessMsg());
			}
		}
	}
	
	private void removeProductsPO(List<MProductPO> productsPO) {
		if (productsPO == null || productsPO.isEmpty())
			return;
		
		for (MProductPO productPO : productsPO) {
			if (!productPO.delete(false, get_TrxName())) {
				throw new IllegalArgumentException(Msg.getMsg(getCtx(), "CopyError") + ": " + productPO.getProcessMsg());
			}
		}
	}
	
}
