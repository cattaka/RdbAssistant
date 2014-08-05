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
 * $Id: DIPInfoSelect.java 232 2009-08-01 07:06:41Z cattaka $
 */
/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import javax.swing.JComboBox;
import javax.swing.JComponent;

public class DIPInfoSelect implements DIPInfo {
	private String label;
	private String[] items;
	private Object[] values;
	private int defaultData;
	private JComboBox comboBox;

	public DIPInfoSelect(String label, String[] items, int defaultData)
			throws InvalidDataTypeException {
		super();
		if (label == null || items == null)
			throw new NullPointerException();
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				throw new NullPointerException();
		}
		if (defaultData < 0 || items.length <= defaultData)
			throw new ArrayIndexOutOfBoundsException();

		this.label = label;
		this.items = items;
		this.values = items;
		this.defaultData = defaultData;
		this.comboBox = new JComboBox(items);
		this.comboBox.setSelectedIndex(defaultData);
	}

	public DIPInfoSelect(String label, String[] items, String defaultItem)
			throws InvalidDataTypeException {
		super();
		if (label == null || items == null)
			throw new NullPointerException();
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				throw new NullPointerException();
		}
		int defaultData;
		for (defaultData = 0; defaultData < items.length; defaultData++) {
			if (defaultItem.equals(items[defaultData])) {
				break;
			}
		}
		if (items.length <= defaultData)
			throw new IllegalArgumentException();

		this.label = label;
		this.items = items;
		this.values = items;
		this.defaultData = defaultData;
		this.comboBox = new JComboBox(items);
		this.comboBox.setSelectedIndex(defaultData);
	}

	public DIPInfoSelect(String label, String[] items, Object[] values, Object defaultValue)
			throws InvalidDataTypeException {
		super();
		if (label == null || items == null)
			throw new NullPointerException();
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				throw new NullPointerException();
		}
		int defaultData;
		for (defaultData = 0; defaultData < values.length; defaultData++) {
			if (defaultValue.equals(values[defaultData])) {
				break;
			}
		}
		if (items.length <= defaultData)
			throw new IllegalArgumentException();

		this.label = label;
		this.items = items;
		this.values = values;
		this.defaultData = defaultData;
		this.comboBox = new JComboBox(items);
		this.comboBox.setSelectedIndex(defaultData);
	}
	
	public Object getValue() {
		return new Integer(comboBox.getSelectedIndex());
	}

	public void setValue(int value) {
		if (value < 0 || comboBox.getItemCount() <= value)
			throw new ArrayIndexOutOfBoundsException();
		comboBox.setSelectedIndex(value);
	}

	public String getStringValue() {
		return this.items[comboBox.getSelectedIndex()];
	}
	
	public void setStringValue(String value) {
		int idx;
		for (idx = 0; idx < this.items.length; idx++) {
			if (value.equals(this.items[idx])) {
				break;
			}
		}
		if (this.items.length <= idx) {
			throw new ArrayIndexOutOfBoundsException();
		}
		comboBox.setSelectedIndex(idx);
	}

	public Object getObjectValue() {
		return this.values[comboBox.getSelectedIndex()];
	}

	public void setObjectValue(Object value) {
		int idx;
		for (idx = 0; idx < this.values.length; idx++) {
			if (value.equals(this.values[idx])) {
				break;
			}
		}
		if (this.values.length <= idx) {
			throw new ArrayIndexOutOfBoundsException();
		}
		comboBox.setSelectedIndex(idx);
	}

	public void makeDefault() {
		this.comboBox.setSelectedIndex(defaultData);
	}

	public String getLabel() {
		return label;
	}

	public JComponent getComponent() {
		return comboBox;
	}

	public boolean isEnable() {
		return comboBox.isEnabled();
	}

	public void setEnable(boolean enable) {
		comboBox.setEnabled(enable);
	}
}
