/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.editor;

import java.beans.PropertyChangeEvent;
import java.util.logging.Level;

import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.window.WFieldRecordInfo;
import org.openXpertya.model.MField;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Mar 11, 2007
 * @version $Revision: 0.10 $
 */
public class WYesNoEditor extends WEditor implements ContextMenuListener
{
    public static final String[] LISTENER_EVENTS = {Events.ON_CHECK};
    private static final CLogger logger;

    static
    {
        logger = CLogger.getCLogger(WYesNoEditor.class);
    }

    private boolean oldValue = false;
	private WEditorPopupMenu popupMenu;

    public WYesNoEditor(MField mField)
    {
        super(new Checkbox(), mField);
        init();
    }

    public WYesNoEditor(String columnName, String label,
			String description, boolean mandatory, boolean readonly,
			boolean updateable) {
		super(new Checkbox(), columnName, label, description, mandatory, readonly, updateable);
		init();
	}

	private void init()
    {
		if (mField != null)
			getComponent().setLabel(mField.getHeader());
		else
			getComponent().setLabel(label.getValue());
        label.setValue("");
        label.setTooltiptext("");
        
        popupMenu = new WEditorPopupMenu(false, false, true);
		popupMenu.addMenuListener(this);
		if (mField != null && mField.getGridTab() != null)
		{
			WFieldRecordInfo.addMenu(popupMenu);
		}
		getComponent().setContext(popupMenu.getId());
    }

    public void onEvent(Event event)
    {
    	if (Events.ON_CHECK.equalsIgnoreCase(event.getName()))
    	{
	        Boolean newValue = (Boolean)getValue();
	        ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), oldValue, newValue);
	        super.fireValueChange(changeEvent);
	        oldValue = newValue;
    	}
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals(org.openXpertya.model.MField.PROPERTY))
        {
            setValue(evt.getNewValue());
        }
    }

    @Override
    public String getDisplay()
    {
        String display = getComponent().isChecked() ? "Y" : "N";
        return Msg.translate(Env.getCtx(), display);
    }

    @Override
    public Object getValue()
    {
        return new Boolean(getComponent().isChecked());
    }

    @Override
    public void setValue(Object value)
    {
        if (value == null || value instanceof Boolean)
        {
            Boolean val = ((value == null) ? false
                    : (Boolean) value);
            getComponent().setChecked(val);
            oldValue = val;
        }
        else if (value instanceof String)
        {
            Boolean val = value.equals("Y");
            getComponent().setChecked(val);
            oldValue = val;
        }
        else
        {
            logger.log(Level.SEVERE,
                    "New field value of unknown type, Type: "
                    + value.getClass()
                    + ", Value: " + value);
            getComponent().setChecked(false);
        }
    }

    @Override
	public Checkbox getComponent() {
		return (Checkbox) component;
	}

	@Override
	public boolean isReadWrite() {
		return getComponent().isEnabled();
	}

	@Override
	public void setReadWrite(boolean readWrite) {
		getComponent().setEnabled(readWrite);
	}

	public String[] getEvents()
    {
        return LISTENER_EVENTS;
    }

	@Override
	public void onMenu(ContextMenuEvent evt) 
	{
		if (WEditorPopupMenu.CHANGE_LOG_EVENT.equals(evt.getContextEvent()))
		{
			WFieldRecordInfo.start(mField);
		}
	}

}
