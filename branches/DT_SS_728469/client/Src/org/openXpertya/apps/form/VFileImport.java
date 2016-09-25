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

package org.openXpertya.apps.form;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CPanel;
import org.openXpertya.apps.ADialog;
import org.openXpertya.apps.ConfirmPanel;
import org.openXpertya.apps.SwingWorker;
import org.openXpertya.impexp.ImpFormat;
import org.openXpertya.impexp.ImpFormatRow;
import org.openXpertya.impexp.MImpFormat;
import org.openXpertya.model.MRole;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;
import org.openXpertya.util.Trx;

/**
 * Descripción de Clase
 * @version 2.2, 12.10.07
 * @author Equipo de Desarrollo de openXpertya
 */
public class VFileImport extends CPanel implements FormPanel, ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Descripción de Método
	 * @param WindowNo
	 * @param frame
	 */
	public void init(int WindowNo, FormFrame frame) {
		log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;

		try {
			jbInit();
			dynInit();
			frame.getContentPane().add(northPanel, BorderLayout.NORTH);
			frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
			frame.getContentPane().add(confirmPanel, BorderLayout.SOUTH);
		} catch (Exception e) {
			log.log(Level.SEVERE, "init", e);
		}
	} // init

	/** Descripción de Campos */

	private int m_WindowNo = 0;

	/** Descripción de Campos */

	private FormFrame m_frame;

	/** Descripción de Campos */

	private ArrayList<String> m_data = new ArrayList<String>();

	/** Descripción de Campos */

	private ImpFormat m_format;

	/** Descripción de Campos */

	private JLabel[] m_labels;

	/** Descripción de Campos */

	private JTextField[] m_fields;

	/** Descripción de Campos */

	private int m_record = -1;

	/** Descripción de Campos */

	private static CLogger log = CLogger.getCLogger(VFileImport.class);

	/** No format indicator */

	private static final String s_none = "----";

	/** Descripción de Campos */

	private CPanel northPanel = new CPanel();

	/** Descripción de Campos */

	private JButton bFile = new JButton();

	/** Descripción de Campos */

	private JComboBox pickFormat = new JComboBox();

	/** Descripción de Campos */

	private CPanel centerPanel = new CPanel();

	/** Descripción de Campos */

	private BorderLayout centerLayout = new BorderLayout();

	/** Descripción de Campos */

	private JScrollPane rawDataPane = new JScrollPane();

	/** Descripción de Campos */

	private JTextArea rawData = new JTextArea();

	/** Descripción de Campos */

	private JScrollPane previewPane = new JScrollPane();

	/** Descripción de Campos */

	private CPanel previewPanel = new CPanel();

	/** Descripción de Campos */

	private ConfirmPanel confirmPanel = new ConfirmPanel(true);

	/** Descripción de Campos */

	private JLabel info = new JLabel();

	/** Descripción de Campos */

	private JLabel labelFormat = new JLabel();

	/** Descripción de Campos */

	private GridBagLayout previewLayout = new GridBagLayout();

	/** Descripción de Campos */

	private JButton bNext = new JButton();

	/** Descripción de Campos */

	private JButton bPrevious = new JButton();

	/** Descripción de Campos */

	private JLabel record = new JLabel();

	/**
	 * Descripción de Método
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		CompiereColor.setBackground(this);
		bFile.setText(Msg.getMsg(Env.getCtx(), "FileImportFile"));
		bFile.setToolTipText(Msg.getMsg(Env.getCtx(), "FileImportFileInfo"));
		bFile.addActionListener(this);
		info.setText("   ");
		labelFormat.setText(Msg.translate(Env.getCtx(), "AD_ImpFormat_ID"));

		//

		bNext.setToolTipText(Msg.getMsg(Env.getCtx(), "Next"));
		bNext.setMargin(new Insets(2, 2, 2, 2));
		bNext.setText(">");
		bNext.addActionListener(this);
		record.setText("----");
		bPrevious.setToolTipText(Msg.getMsg(Env.getCtx(), "Previous"));
		bPrevious.setMargin(new Insets(2, 2, 2, 2));
		bPrevious.setText("<");
		bPrevious.addActionListener(this);

		//

		northPanel.setBorder(BorderFactory.createEtchedBorder());
		northPanel.add(bFile, null);
		northPanel.add(info, null);
		northPanel.add(labelFormat, null);
		northPanel.add(pickFormat, null);
		northPanel.add(bPrevious, null);
		northPanel.add(record, null);
		northPanel.add(bNext, null);

		//

		centerPanel.setLayout(centerLayout);
		rawData.setFont(new java.awt.Font("Monospaced", 0, 10));
		rawData.setColumns(80);
		rawData.setRows(5);
		rawDataPane.getViewport().add(rawData, null);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rawDataPane, previewPane);
		centerPanel.add(splitPane, BorderLayout.CENTER);

		//

		previewPanel.setLayout(previewLayout);
		previewPane.getViewport().add(previewPanel, null);
		previewPane.setPreferredSize(new Dimension(700, 80));

		//

		confirmPanel.addActionListener(this);
	} // jbInit

	/**
	 * Descripción de Método
	 */
	public void dispose() {
		if (m_frame != null) {
			m_frame.dispose();
		}

		m_frame = null;
	} // dispose

	/**
	 * Descripción de Método
	 */
	private void dynInit() {

		// Load Formats

		pickFormat.addItem(s_none);

		String sql = MRole.getDefault().addAccessSQL("SELECT Name FROM AD_ImpFormat", "AD_ImpFormat", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO) + " AND AD_ImpFormat_Parent_ID IS NULL AND isActive = 'Y' ";

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pickFormat.addItem(rs.getString(1));
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
		}

		pickFormat.setSelectedIndex(0);
		pickFormat.addActionListener(this);

		//

		confirmPanel.getOKButton().setEnabled(false);
	} // dynInit

	/**
	 * Descripción de Método
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bFile) {
			cmd_loadFile();
			invalidate();
			m_frame.pack();
		} else if (e.getSource() == pickFormat) {
			cmd_loadFormat();
			invalidate();
			m_frame.pack();
		} else if (e.getSource() == bNext) {
			cmd_applyFormat(true);
		} else if (e.getSource() == bPrevious) {
			cmd_applyFormat(false);
		} else if (e.getActionCommand().equals(ConfirmPanel.A_OK)) {
			m_frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			confirmPanel.setEnabled(false);
			m_frame.setBusy(true);

			//

			SwingWorker worker = new SwingWorker() {
				public Object construct() {
					cmd_process();

					return Boolean.TRUE;
				}
			};

			worker.start();

		} else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) {
			dispose();
		}

		if ((m_data != null) && (m_data.size() > 0 // file loaded
		) && (m_format != null) && (m_format.getRowCount() > 0)) { // format loaded
			confirmPanel.getOKButton().setEnabled(true);
		} else {
			confirmPanel.getOKButton().setEnabled(false);
		}
	} // actionPerformed

	/**
	 * Descripción de Método
	 */
	private void cmd_loadFile() {
		String directory = org.openXpertya.OpenXpertya.getOXPHome() + File.separator + "data" + File.separator + "import";

		log.config(directory);

		//

		JFileChooser chooser = new JFileChooser(directory);

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.setDialogTitle(Msg.getMsg(Env.getCtx(), "FileImportFileInfo"));

		if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		String fileName = chooser.getSelectedFile().getName();

		log.config(fileName);
		bFile.setText(fileName);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		m_data.clear();
		rawData.setText("");

		try {

			// see NaturalAccountMap

			BufferedReader in = new BufferedReader(new FileReader(chooser.getSelectedFile()), 10240);

			// not safe see p108 Network pgm

			String s = null;

			while ((s = in.readLine()) != null) {
				m_data.add(s);

				if (m_data.size() < 100) {
					rawData.append(s);
					rawData.append("\n");
				}
			}

			in.close();
			rawData.setCaretPosition(0);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			bFile.setText(Msg.getMsg(Env.getCtx(), "FileImportFile"));
		}

		int index = 1; // second line as first may be heading

		if (m_data.size() == 1) {
			index = 0;
		}

		int length = 0;

		if (m_data.size() > 0) {
			length = m_data.get(index).length();
		}

		info.setText(Msg.getMsg(Env.getCtx(), "Records") + "=" + m_data.size() + ", " + Msg.getMsg(Env.getCtx(), "Length") + "=" + length + "   ");
		setCursor(Cursor.getDefaultCursor());
		log.config("Records=" + m_data.size() + ", Length=" + length);
	} // cmd_loadFile

	/**
	 * Descripción de Método
	 */
	private void cmd_loadFormat() {

		// clear panel

		previewPanel.removeAll();

		//

		String formatName = pickFormat.getSelectedItem().toString();

		if (formatName.equals(s_none)) {
			return;
		}

		m_format = ImpFormat.load(formatName);

		if (m_format == null) {
			ADialog.error(m_WindowNo, this, "FileImportNoFormat", formatName);

			return;
		}

		// pointers

		fieldsFromFormat(m_format);

		m_record = -1;
		record.setText("----");
		previewPanel.invalidate();
		previewPanel.repaint();
	} // cmd_format

	private void fieldsFromFormat(ImpFormat m_format) {
		int size = m_format.getRowCount();

		// TODO mejorar para formatos con hijos.

		m_labels = new JLabel[size];
		m_fields = new JTextField[size];

		for (int i = 0; i < size; i++) {
			ImpFormatRow row = m_format.getRow(i);

			m_labels[i] = new JLabel(row.getDisplayName());
			previewPanel.add(m_labels[i], new GridBagConstraints(i, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

			int length = row.getEndNo() - row.getStartNo();

			if (length <= 5) {
				length = 5;
			} else if (length > 20) {
				length = 20;
			}

			m_fields[i] = new JTextField(length);
			previewPanel.add(m_fields[i], new GridBagConstraints(i, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		}
		MImpFormat mImpFormat = new MImpFormat(Env.getCtx(), m_format.getAD_ImpFormat_ID(), Trx.createTrxName("fileImportLoadFormat"));
		List<MImpFormat> chidFormats = mImpFormat.getChilds();
		for (MImpFormat f : chidFormats) {
			fieldsFromFormat(ImpFormat.load(f.getName()));
		}
	}

	/**
	 * Descripción de Método
	 * @param next
	 */
	private void cmd_applyFormat(boolean next) {
		if (m_format == null) {
			return;
		}

		// set position

		if (next) {
			m_record++;
		} else {
			m_record--;
		}

		if (m_record < 0) {
			m_record = 0;
		} else if (m_record >= m_data.size()) {
			m_record = m_data.size() - 1;
		}

		record.setText(" " + String.valueOf(m_record + 1) + " ");

		// Line Info

		// no label, trace, no ignore
		String[] lInfo = m_format.parseLine(m_data.get(m_record), false, true, false);
		int size = m_format.getRowCount();

		if (lInfo.length != size) {
			log.log(Level.SEVERE, "FormatElements=" + size + " != Fields=" + lInfo.length);
		}

		for (int i = 0; i < size; i++) {
			m_fields[i].setText(lInfo[i]);
			m_fields[i].setCaretPosition(0);
		}
	} // cmd_applyFormat

	/**
	 * Descripción de Método
	 */
	private void cmd_process() {
		log.config("");

		if (m_format == null) {
			ADialog.error(m_WindowNo, this, "FileImportNoFormat");

			return;
		}

		// For all rows - update/insert DB table

		int row = 0;
		int imported = 0;

		for (row = 0; row < m_data.size(); row++) {
			if (m_format.updateDB(Env.getCtx(), m_data.get(row))) {
				imported++;
			}
		}

		//

		ADialog.info(m_WindowNo, this, "FileImportR/I", row + " / " + imported + "#");
		dispose();
	} // cmd_process

} // FileImport

/* @(#)VFileImport.java 02.07.07
 * 
 * Fin del fichero VFileImport.java
 * 
 * Versión 2.2 */
