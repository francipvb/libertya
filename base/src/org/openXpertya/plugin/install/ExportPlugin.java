package org.openXpertya.plugin.install;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openXpertya.model.MComponentVersion;
import org.openXpertya.model.MProcess;
import org.openXpertya.plugin.common.PluginConstants;
import org.openXpertya.process.ProcessInfoParameter;
import org.openXpertya.process.SvrProcess;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

public class ExportPlugin extends SvrProcess{

	// Variables de instancia
	
	/** Versión de componente */
	
	private Integer componentVersionID;
	
	/** Directorio destino de los archivos */
	
	private String directoryPath;
	
	/** Id de Proceso custom */
	
	private Integer processID;
	
	/** AD_ChangeLog_ID inicial */
	
	private Integer changeLogIDFrom = null;  
	
	/** AD_ChangeLog_ID fin */
	
	private Integer changeLogIDTo = null;
	
	/** changeLogUID inicial */
	
	private String changeLogUIDFrom = null;  
	
	/** changeLogUID final */
	
	private String changeLogUIDTo = null;
	
	/** Usuario registrado en registros del changelog */
	
	private Integer userID = null;
	
	/** Patch */
	
	private boolean patch = false;
	
	/** Builders de archivos */
	
	private List<PluginDocumentBuilder> builders;
	
	/** Proceso */
	MProcess process = null;
	
	/** Checkear si la los metadatos en el changelog coincide los metadatos */
	protected boolean validateChangelogConsistency = false;
	
	/** Deshabilitar las entradas del changelog inconsistentes con los metadatos */
	protected boolean disableInconsistentChangelog = false;
	
	// Heredados
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		String name;
        for( int i = 0;i < para.length;i++ ) {
        	name = para[ i ].getParameterName();
        	if(name.equalsIgnoreCase("AD_ComponentVersion_ID")){
        		setComponentVersionID(para[i].getParameterAsInt());
        	}
        	else if(name.equalsIgnoreCase("Directory")){
        		setDirectoryPath(String.valueOf(para[i].getParameter()));
        	}
        	else if(name.equalsIgnoreCase("AD_Process_ID")){
        		setProcessID(para[i].getParameterAsInt());
        	}
        	else if(name.equalsIgnoreCase("AD_ChangeLog_ID")){
        		setChangeLogIDFrom(para[i].getParameterAsInt());
        		setChangeLogIDTo(para[i].getParameter_ToAsInt());
        	}
        	else if(name.equalsIgnoreCase("AD_User_ID")){
        		setUserID(para[i].getParameterAsInt());
        	}
        	else if(name.equalsIgnoreCase("Patch")){
        		setPatch(String.valueOf(para[i].getParameter()).equalsIgnoreCase("Y"));
        	}
        	else if(name.equalsIgnoreCase("ValidateChangelogConsistency")){
        		validateChangelogConsistency = String.valueOf(para[i].getParameter()).equalsIgnoreCase("Y");
        	}
        	else if(name.equalsIgnoreCase("DisableInconsistentChangelog")){
        		disableInconsistentChangelog = String.valueOf(para[i].getParameter()).equalsIgnoreCase("Y");
        	}
        }
	}


	@Override
	protected String doIt() throws Exception {
		MComponentVersion currentComponent = MComponentVersion.getCurrentComponentVersion(getCtx(), get_TrxName());
		if(currentComponent != null){
			throw new Exception(Msg.getMsg(getCtx(), "ExistCurrentPlugin"));
		}
		// Inicializar los builders xml
		initBuilders();
		// Ejecuto los builders
		int i=0;
		for (PluginDocumentBuilder docBuilder : getBuilders()) {
			// Al builder del properties debo indicarle el changelog
			if (++i == 4) {
				// Changelog IDs
				((PluginPropertiesBuilder)docBuilder).setChangelogIDTo(getLastChangelog());
				((PluginPropertiesBuilder)docBuilder).setChangelogIDFrom(getFirstChangelog());
				// Changelog UIDs
				((PluginPropertiesBuilder)docBuilder).setChangeLogUIDTo(getLastChangeLogUID());
				((PluginPropertiesBuilder)docBuilder).setChangeLogUIDFrom(getFirstChangeLogUID());
				// ChangelogGroup UIDs
				((PluginPropertiesBuilder)docBuilder).setChangeLogGroupUIDTo(getLastChangeLogGroupUID());
				((PluginPropertiesBuilder)docBuilder).setChangeLogGroupUIDFrom(getFirstChangeLogGroupUID());
			}
			// Genero el documento
			docBuilder.generateDocument();
		}
		

		
		
		// Mensaje final de proceso
		return getMsg();
	}
	
	
	// Varios
	
	/**
	 * Inicializa los builders de los archivos xml
	 */
	private void initBuilders(){
		process = null;
		if(getProcessID() != null){
			process = new MProcess(Env.getCtx(), getProcessID(), null);
		}
		builders = new ArrayList<PluginDocumentBuilder>();
		// Preinstall - 0
		builders.add(new PluginSQLBuilder(getDirectoryPath(), PluginConstants.FILENAME_PREINSTALL, getComponentVersionID(), getChangeLogIDFrom(), getChangeLogIDTo(), getUserID(), get_TrxName()));
		// Install - 1
		builders.add(new PluginInstallBuilder(getDirectoryPath(), PluginConstants.FILENAME_INSTALL, getComponentVersionID(), getChangeLogIDFrom(), getChangeLogIDTo(), getUserID(), get_TrxName(), validateChangelogConsistency, disableInconsistentChangelog));
		// PostInstall - 2
		builders.add(new PostInstallBuilder(getDirectoryPath(), PluginConstants.FILENAME_POSTINSTALL, getComponentVersionID(), getChangeLogIDFrom(), getChangeLogIDTo(), getUserID(), get_TrxName(), validateChangelogConsistency, disableInconsistentChangelog));
		// Manifest - 3
		builders.add(new PluginPropertiesBuilder(getDirectoryPath(), PluginConstants.PLUGIN_MANIFEST, getComponentVersionID(), process, isPatch(), get_TrxName()));		
	}

	/**
	 * Determina el mayor de los changelogs - cual es el ultimo changelog del export
	 * @return
	 */
	protected int getLastChangelog() 
	{
		int lastChangelogID_install = ((ChangeLogXMLBuilder)(builders.get(1))).getLastChangelogID();
		int lastChangelogID_postInstall = ((ChangeLogXMLBuilder)(builders.get(2))).getLastChangelogID();
		if (lastChangelogID_install >= lastChangelogID_postInstall)
			return lastChangelogID_install;
		return lastChangelogID_postInstall;
	}
	
	/**
	 * Retorna el ultimo changelogUID del export
	 */
	protected String getLastChangeLogUID() 
	{
		return DB.getSQLValueString(get_TrxName(), "SELECT changeLogUID FROM AD_Changelog WHERE AD_Changelog_ID = " + getLastChangelog());
	}
	
	/**
	 * Retorna el ultimo changelogGroupUID del export
	 */
	protected String getLastChangeLogGroupUID() 
	{
		return DB.getSQLValueString(get_TrxName(), "SELECT changeLogGroupUID FROM AD_Changelog WHERE AD_Changelog_ID = " + getLastChangelog());
	}
	
	/**
	 * Determina el menor de los changelogs - cual es el primer changelog del export
	 * Considera la eventual posibilidad de que el install o el postInstall sea -1
	 * @return
	 */
	protected int getFirstChangelog() 
	{
		int firstChangelogID_install = ((ChangeLogXMLBuilder)(builders.get(1))).getFirstChangelogID();
		int firstChangelogID_postInstall = ((ChangeLogXMLBuilder)(builders.get(2))).getFirstChangelogID();
		if (firstChangelogID_install == -1 && firstChangelogID_postInstall == -1)
			return -1;
		return Math.min((firstChangelogID_install<=0	?Integer.MAX_VALUE:firstChangelogID_install), 
						(firstChangelogID_postInstall<=0?Integer.MAX_VALUE:firstChangelogID_postInstall));  
	}
	
	/**
	 * Retorna el primer changelogUID del export
	 */
	protected String getFirstChangeLogUID() 
	{
		return DB.getSQLValueString(get_TrxName(), "SELECT changeLogUID FROM AD_Changelog WHERE AD_Changelog_ID = " + getFirstChangelog());
	}
	
	/**
	 * Retorna el primer changelogGroupUID del export
	 */
	protected String getFirstChangeLogGroupUID() 
	{
		return DB.getSQLValueString(get_TrxName(), "SELECT changeLogGroupUID FROM AD_Changelog WHERE AD_Changelog_ID = " + getFirstChangelog());
	}
	
	/**
	 * @return mensaje final del proceso
	 */
	private String getMsg(){
		StringBuffer msg = new StringBuffer();
		msg.append("Exportacion de plugin realizada correctamente dentro del directorio ");
		msg.append(getDirectoryPath());		
		return msg.toString();
	}


	// Getters y Setters
	
	protected void setComponentVersionID(Integer componentVersionID) {
		this.componentVersionID = componentVersionID;
	}


	protected Integer getComponentVersionID() {
		return componentVersionID;
	}


	protected void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}


	protected String getDirectoryPath() {
		return directoryPath;
	}


	protected void setBuilders(List<PluginDocumentBuilder> builders) {
		this.builders = builders;
	}


	protected List<PluginDocumentBuilder> getBuilders() {
		return builders;
	}


	protected void setProcessID(Integer processID) {
		this.processID = processID;
	}


	protected Integer getProcessID() {
		return processID;
	}


	protected void setChangeLogIDFrom(Integer changeLogIDFrom) {
		if (changeLogIDFrom == 0) {
			changeLogIDFrom = null;
		}
		this.changeLogIDFrom = changeLogIDFrom;
	}


	protected Integer getChangeLogIDFrom() {
		return changeLogIDFrom;
	}


	protected void setChangeLogIDTo(Integer changeLogIDTo) {
		if (changeLogIDTo == 0) {
			changeLogIDTo = null;
		}
		this.changeLogIDTo = changeLogIDTo;
	}


	protected Integer getChangeLogIDTo() {
		return changeLogIDTo;
	}


	protected void setUserID(Integer userID) {
		if (userID == 0) {
			userID = null;
		}
		this.userID = userID;
	}


	protected Integer getUserID() {
		return userID;
	}


	protected void setPatch(boolean patch) {
		this.patch = patch;
	}


	protected boolean isPatch() {
		return patch;
	}

}
