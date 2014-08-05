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
 * $Id: RdbaConnectionUtil.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.core;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import net.cattaka.rdbassistant.sql.ColumnConverter;
import net.cattaka.rdbassistant.sql.DateColumnConverter;
import net.cattaka.rdbassistant.sql.DateTimeColumnConverter;
import net.cattaka.rdbassistant.sql.BigDecimalColumnConverter;
import net.cattaka.rdbassistant.sql.OtherColumnConverter;
import net.cattaka.rdbassistant.sql.StringColumnConverter;
import net.cattaka.rdbassistant.sql.TimeColumnConverter;
import net.cattaka.rdbassistant.sql.TimestampColumnConverter;

public class RdbaConnectionUtil {
	/**
	 * 
	 * @param rdbaConnection
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static ColumnConverter[] createColumnConverterList(RdbaConnection rdbaConnection, ResultSetMetaData rsmd) throws SQLException {
		ColumnConverter[] results = new ColumnConverter[rsmd.getColumnCount()];
		for (int i=0;i<results.length;i++) {
			Class<?> columnClass = rdbaConnection.getClassFromSqlType(rsmd.getColumnType(i+1));
			if (columnClass == String.class) {
				results[i] = new StringColumnConverter(rsmd.getPrecision(i+1));
			} else if (columnClass == BigDecimal.class) {
				results[i] = new BigDecimalColumnConverter(rsmd.getPrecision(i+1), rsmd.getScale(i+1));
			} else if (columnClass == Timestamp.class) {
				results[i] = new TimestampColumnConverter();
			} else if (columnClass == Time.class) {
				results[i] = new TimeColumnConverter();
			} else if (columnClass == Date.class) {
				results[i] = new DateColumnConverter();
			} else if (columnClass == java.util.Date.class) {
				results[i] = new DateTimeColumnConverter();
			} else {
				results[i] = new OtherColumnConverter();
			}
		}
		
		return results;
	}
}
