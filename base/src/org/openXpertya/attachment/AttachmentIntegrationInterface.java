package org.openXpertya.attachment;

import java.io.File;

public interface AttachmentIntegrationInterface {

	
	/** Inserta una entrada en la ubicacion remota especificada */
	public String insertEntry(byte[] data, String name, int clientID, int orgID) throws Exception;
	
	/** Elimina una entrada remota a partir de su UID externo */
	public boolean deleteEntry(String externalUID) throws Exception;

	/** Recupera una entrada remota a partir de su UID externo */
	public byte[] retrieveEntry(String externalUID) throws Exception;
	
	/** Tamaño maximo permitido en adjuntos, especificado en bytes */
	public Integer getMaxSizeAllowed();

}
