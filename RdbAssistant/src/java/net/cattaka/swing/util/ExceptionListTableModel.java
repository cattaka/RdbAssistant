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
 * $Id: ExceptionListTableModel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.util;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;



public class ExceptionListTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<ExceptionInfo> exceptionInfoList;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private String[] columnNames;

	public ExceptionListTableModel(List<ExceptionInfo> exceptionInfoList) {
		if (exceptionInfoList == null) {
			throw new NullPointerException();
		}
		this.columnNames = new String[]{"TimeStamp", "Type", "Message", "StackTrace"};
		this.exceptionInfoList = exceptionInfoList;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return this.exceptionInfoList.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		String result;
		ExceptionInfo ei = this.exceptionInfoList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			result = dateFormat.format(ei.getTimestamp());
			break;
		case 1:
			result = ei.getType();
			break;
		case 2:
			result = ei.getMessage();
			break;
		case 3:
			result = ei.getStackTrace();
			break;
		default:
			throw new ArrayIndexOutOfBoundsException(columnIndex);	
		}
		return result;
	}
}
