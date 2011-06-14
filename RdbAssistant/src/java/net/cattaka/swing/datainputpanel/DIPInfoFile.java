/*
 * Copyright (c) 2009, Takao Sumitomo
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the 
 *       above copyright notice, this list of conditions
 *       and the following disclaimer.
 *     * Redistributions in binary form must reproduce
 *       the above copyright notice, this list of
 *       conditions and the following disclaimer in the
 *       documentation and/or other materials provided
 *       with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software
 * and documentation are those of the authors and should
 * not be interpreted as representing official policies,
 * either expressed or implied.
 */
/*
 * $Id: DIPInfoFile.java 232 2009-08-01 07:06:41Z cattaka $
 */
/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DIPInfoFile implements DIPInfo, ActionListener {
	public static final int MODE_OPEN = 0;
	public static final int MODE_SAVE = 1;
	private String label;
	private String defaultData;
	private JTextField field;
	private JComponent component;
	private JFileChooser fileChooser;
	private int mode;
	private boolean checkExist;
	private JButton open;
	private int fileSelectionMode = JFileChooser.FILES_ONLY;
	
	public DIPInfoFile(String label, String defaultData, int mode)
			throws InvalidDataTypeException {
		super();
		if (label == null || defaultData == null)
			throw new NullPointerException();
		if (mode != MODE_OPEN && mode != MODE_SAVE)
			throw new InvalidDataTypeException();

		this.mode = mode;
		this.label = label;
		this.defaultData = defaultData;
		this.field = new JTextField(defaultData, 0);
		this.checkExist = false;

		JPanel panel = new JPanel();
		open = new JButton("Open");
		open.setActionCommand("open");
		open.addActionListener(this);
		GridBagLayout gl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gl.setConstraints(field, gc);
		gc.gridx++;
		gc.weightx = 0;
		gl.setConstraints(open, gc);
		panel.setLayout(gl);
		panel.add(field);
		panel.add(open);
		this.component = panel;
	}

	public File getFileValue() {
		String arg = field.getText();
		File file = new File(arg);
		if (checkExist) {
			if (file.exists())
				return file;
			else
				return null;
		} else {
			return file;
		}
	}
	
	public Object getValue() {
		String arg = field.getText();
		File file = new File(arg);
		if (checkExist) {
			if (file.exists())
				return file;
			else
				return null;
		} else {
			return file;
		}
	}
	
	public String getStringValue() {
		return field.getText();
	}

	public void setValue(File value) {
		if (value != null) {
			field.setText(value.getPath());
		} else {
			field.setText("");
		}
	}

	public void setStringValue(String value) {
		field.setText(value);
	}

	public void makeDefault() {
		field.setText(defaultData);
	}

	public String getLabel() {
		return label;
	}

	public JComponent getComponent() {
		return component;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("open")) {
			if (fileChooser == null) {
				fileChooser = new JFileChooser();
			}
			fileChooser.setFileSelectionMode(fileSelectionMode);
			if (mode == MODE_OPEN) {
				if (fileChooser.showOpenDialog(component) == JFileChooser.APPROVE_OPTION) {
					field.setText(fileChooser.getSelectedFile()
							.getAbsolutePath());
				}
			} else if (mode == MODE_SAVE) {
				if (fileChooser.showSaveDialog(component) == JFileChooser.APPROVE_OPTION) {
					field.setText(fileChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}
		}
	}

	public boolean isEnable() {
		return field.isEnabled();
	}

	public void setEnable(boolean enable) {
		open.setEnabled(enable);
		field.setEnabled(enable);
	}

	/** {@link JFileChooser#getFileSelectionMode()} */
	public int getFileSelectionMode() {
		return fileSelectionMode;
	}

	/** {@link JFileChooser#setFileSelectionMode(int)} */
	public void setFileSelectionMode(int fileSelectionMode) {
		this.fileSelectionMode = fileSelectionMode;
	}
}
