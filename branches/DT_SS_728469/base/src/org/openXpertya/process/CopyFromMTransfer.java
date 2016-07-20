package org.openXpertya.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.openXpertya.model.MLocator;
import org.openXpertya.model.MTransfer;
import org.openXpertya.model.MTransferLine;
import org.openXpertya.model.PO;
import org.openXpertya.util.DB;
import org.openXpertya.util.Msg;

public class CopyFromMTransfer extends SvrProcess {
	
	/** Descripción de Campos */

    protected int p_M_Transfer_ID = 0;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();

        for( int i = 0;i < para.length;i++ ) {
            String name = para[ i ].getParameterName();

            if( para[ i ].getParameter() == null ) {
                ;
            } else if( name.equals( "M_Transfer_ID" )) {
            	p_M_Transfer_ID = (( BigDecimal )para[ i ].getParameter()).intValue(); 
        	} else {
                log.log( Level.SEVERE,"Unknown Parameter: " + name );
            }
        }
		
	}

	@Override
	protected String doIt() throws Exception {
		int to_M_Transfer_ID = getRecord_ID();
		
		//Valido que se haya recuperado bien los IDs de los documentos
		if (p_M_Transfer_ID == 0 || to_M_Transfer_ID == 0) {
			throw new IllegalArgumentException(Msg.getMsg(getCtx(), "CopyError"));
		}
		
		MTransfer from = new MTransfer(getCtx(), p_M_Transfer_ID, get_TrxName());
		MTransfer to = new MTransfer(getCtx(), to_M_Transfer_ID, get_TrxName());
		
		for (MTransferLine line : from.getLines()) {
			MTransferLine newLine = new MTransferLine(to);
			//Copio la linea completa
			PO.copyValues(line, newLine);
			
			//Corrijo los campos que varían
			newLine.setM_Transfer_ID(to_M_Transfer_ID);
			newLine.setM_Locator_ID(getMLocatorIdFrom(from, to, line));
			newLine.setM_Locator_To_ID(getMLocatorIdTo(from, to, line));
			
			if (!newLine.save()) {
				throw new IllegalArgumentException(Msg.getMsg(getCtx(), "CopyError") + ": " + newLine.getProcessMsg());
			}
		}
				
		return Msg.getMsg(getCtx(), "CopySuccessful");
	}
	
	private int getMLocatorIdFrom(MTransfer from, MTransfer to, MTransferLine line) {
		//Si el almacen origen de ambas Transferencias coinciden, copio la ubicación de origen
		if (from.getM_Warehouse_ID() == to.getM_Warehouse_ID()) {
			return line.getM_Locator_ID();
		} else {
			//Caso contrario recupero la ubicación por defecto para el almacen origen, utilizando el mismo criterio que en las ventanas.
			MLocator locator = null;
			
			String sql = "SELECT * FROM m_locator WHERE m_warehouse_id = ? ORDER BY isdefault desc LIMIT 1";
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(sql, get_TrxName());
				ps.setInt(1, to.getM_Warehouse_ID());
				rs = ps.executeQuery();
				while (rs.next()) {
					locator = new MLocator(getCtx(), rs, get_TrxName());
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
			
			return locator.getID();
		}
	}
	
	private int getMLocatorIdTo(MTransfer from, MTransfer to, MTransferLine line) {
		//Si el almacen destino de ambas Transferencias coinciden, copio la ubicación de destino
		if (from.getM_WarehouseTo_ID() == to.getM_WarehouseTo_ID()) {
			return line.getM_Locator_To_ID();
		} else {
			//Caso contrario recupero la ubicación por defecto para el almacen destino, utilizando el mismo criterio que en las ventanas.
			MLocator locator = null;
			
			String sql = "SELECT * FROM m_locator WHERE m_warehouse_id = ? ORDER BY isdefault desc LIMIT 1";
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = DB.prepareStatement(sql, get_TrxName());
				ps.setInt(1, to.getM_WarehouseTo_ID());
				rs = ps.executeQuery();
				while (rs.next()) {
					locator = new MLocator(getCtx(), rs, get_TrxName());
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
			
			return locator.getID();
		}
	}

	

}
