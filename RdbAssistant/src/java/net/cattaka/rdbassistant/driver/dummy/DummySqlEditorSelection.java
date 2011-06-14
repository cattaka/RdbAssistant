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
 * $Id: DummySqlEditorSelection.java 258 2010-02-27 13:43:19Z cattaka $
 */
package net.cattaka.rdbassistant.driver.dummy;

import java.util.ArrayList;
import java.util.List;

import net.cattaka.rdbassistant.core.SqlEditorSelection;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModel;
import net.cattaka.rdbassistant.gui.table.StaticResultSetTableModel;

public class DummySqlEditorSelection implements SqlEditorSelection {
	public static String DUMMY = "DUMMY";
	private DummyRdbaConnection rdbConnection;
	
	public DummySqlEditorSelection(DummyRdbaConnection rdbConnection) {
		this.rdbConnection = rdbConnection;
	}

	public String getDefaultDatabase() {
		return rdbConnection.getDefaultDatabase();
	}

	public List<String> getDatabaseList() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(DUMMY);
		return result;
	}

	public List<String> getObjectTypeList() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("TABLE");
		return result;
	}
	
	public ResultSetTableModel getTableList(String database, String objectType) {
		StaticResultSetTableModel result = new StaticResultSetTableModel();
		return result;
	}

	public List<ResultSetTableModel> getTableProperties(String database,String objectType, String table) {
		ArrayList<ResultSetTableModel> result = new ArrayList<ResultSetTableModel>();
		return result;
	}
}
