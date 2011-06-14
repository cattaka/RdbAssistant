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
 * $Id: PropertyTableUtil.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class PropertyTableUtil {
	public static PropertyTable createPropertyTable(ResultSet rs) throws SQLException {
		PropertyTable result = new PropertyTable();
		ResultSetMetaData rsmd = rs.getMetaData();
		String[] columnNames = new String[rsmd.getColumnCount()];
		int[] columnCharacters = new int[rsmd.getColumnCount()];
		for (int i=0;i<columnNames.length;i++) {
			columnNames[i] = rsmd.getColumnLabel(i+1);
			columnCharacters[i] = 10;
		}
		result.setColumnNames(columnNames);
		result.setColumnCharacters(columnCharacters);
		ArrayList<Object[]> columnValues = new ArrayList<Object[]>();
		while(rs.next()) {
			String[] strArray = new String[columnNames.length];
			for (int i=0;i<strArray.length;i++) {
				strArray[i] = rs.getString(i+1);
			}
			columnValues.add(strArray);
		}
		result.setColumnValues(columnValues);
		return result;
	}
}
