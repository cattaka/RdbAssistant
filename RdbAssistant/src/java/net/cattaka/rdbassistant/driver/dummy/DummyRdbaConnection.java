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
 * $Id: DummyRdbaConnection.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.driver.dummy;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.SqlEditorSelection;
import net.cattaka.rdbassistant.sql.Other;
import net.cattaka.rdbassistant.util.ConnectionWrapper;
import net.cattaka.swing.text.StdStyledDocument;

/**
 * 作成にはRdbConnectionFactoryを使用すること。
 * 
 * @author cattaka
 */
public class DummyRdbaConnection implements RdbaConnection {
	private DummySqlEditorSelection myEditorSelection;
	private String defaultDatabase;
	private ConnectionWrapper connection;
	
	DummyRdbaConnection(String defaultDatabase) {
		this.defaultDatabase = defaultDatabase.toUpperCase();
		myEditorSelection = new DummySqlEditorSelection(this);
		connection = new ConnectionWrapper(new DummyConnection());
	}
	
	public ConnectionWrapper getConnection() {
		return this.connection;
	}

	public void dispose() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// あり得ない
		}
	}

	public String getDefaultDatabase() {
		return defaultDatabase;
	}

	public SqlEditorSelection getSqlEditorSelection() {
		return myEditorSelection;
	}

	public Class<?> getClassFromSqlType(int columnType) throws SQLException {
		return Other.class;
	}

	public Object extractResultSetData(ResultSet resultSet, Class<?> columnClass, int columnIndex) throws SQLException {
		return null;
	}

	public StdStyledDocument createNewSqlDocument() {
		DummyStyledDocument styledDocument = new DummyStyledDocument();
		return styledDocument;
	}
	
	public String getLastDbmsOutput() {
		return "";
	}
	
	public String getLineComment() {
		return "--";
	}
}
