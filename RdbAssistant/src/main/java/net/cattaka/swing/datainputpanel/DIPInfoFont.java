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
 * $Id: DIPInfoFont.java 232 2009-08-01 07:06:41Z cattaka $
 */
/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.cattaka.swing.FontChooser;

public class DIPInfoFont implements DIPInfo, ActionListener {
	private String label;
	private Font defaultData;
	private Font selectedData;
	private JTextField field;
	private JComponent component;
	private FontChooser fontChooser;
	private JButton selectButton;
	
	public DIPInfoFont(String label, Font defaultData)
			throws InvalidDataTypeException {
		super();
		if (label == null || defaultData == null)
			throw new NullPointerException();

		this.label = label;
		this.defaultData = defaultData;
		this.selectedData = defaultData;
		this.field = new JTextField(defaultData.getName(), 0);
		this.field.setEditable(false);

		JPanel panel = new JPanel();
		selectButton = new JButton("Select");
		selectButton.setActionCommand("select");
		selectButton.addActionListener(this);
		GridBagLayout gl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gl.setConstraints(field, gc);
		gc.gridx++;
		gc.weightx = 0;
		gl.setConstraints(selectButton, gc);
		panel.setLayout(gl);
		panel.add(field);
		panel.add(selectButton);
		this.component = panel;
	}

	public Font getFontValue() {
		return this.selectedData;
	}
	
	public Object getValue() {
		return this.selectedData;
	}
	
	public void setValue(Font value) {
		this.selectedData = value;
		if (value != null) {
			field.setText(value.getFontName() + "(" + value.getSize() + ")");
		}
	}

	public void makeDefault() {
		this.setValue(defaultData);
	}

	public String getLabel() {
		return label;
	}

	public JComponent getComponent() {
		return component;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("select")) {
			if (fontChooser == null) {
				fontChooser = new FontChooser();
			}
			if (this.selectedData != null) {
				fontChooser.setSelectedFont(this.selectedData);
			}
			Font t = fontChooser.showFontSelectDialog();
			if (t != null) {
				this.setValue(t);
			}
		}
	}

	public boolean isEnable() {
		return field.isEnabled();
	}

	public void setEnable(boolean enable) {
		selectButton.setEnabled(enable);
		field.setEnabled(enable);
	}
}
