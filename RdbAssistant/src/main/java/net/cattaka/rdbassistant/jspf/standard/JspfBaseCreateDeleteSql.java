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
 * $Id: JspfBaseCreateDeleteSql.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.standard;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import net.cattaka.rdbassistant.jspf.JspfConstants;
import net.cattaka.rdbassistant.jspf.core.JspfBase;
import net.cattaka.rdbassistant.jspf.core.JspfMessageException;
import net.cattaka.rdbassistant.jspf.core.bean.ColumnInfo;
import net.cattaka.util.MessageBundle;

public class JspfBaseCreateDeleteSql extends JspfBase {

	public JspfBaseCreateDeleteSql() {
		this.setName(JspfConstants.JSPF_NAME_CREATE_DELETE_SQL);
		this.setDisplayName(MessageBundle.getMessage("create_delete_sql"));
	}
	
	@Override
	public void runJspf(PrintWriter out) throws SQLException,JspfMessageException {
		String tableName = "";
		out.println("DELETE FROM");
		if (selectionInfo.getTables().length == 1) {
			tableName = selectionInfo.getTables()[0];
			out.println("\t" + tableName);
		} else {
			error(MessageBundle.getMessage("select_single_table"));
		}
		
		// WHERE句の作成
		out.println("WHERE");
		List<ColumnInfo> cil = databaseInfo.getTableInfo(tableName).getColumnInfoList();
		boolean firstFlag = true;
		for (ColumnInfo ci : cil) {
			if (ci.isPrimaryKey()) {
				if (firstFlag) {
					firstFlag = false;
					out.print("\t");
				} else {
					out.print("\tAND ");
				}
				out.println(ci.getName() + " = ''");
			}
		}
	}
}