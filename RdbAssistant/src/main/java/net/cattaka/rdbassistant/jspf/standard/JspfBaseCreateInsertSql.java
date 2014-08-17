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
 * $Id: JspfBaseCreateInsertSql.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.standard;

import java.io.PrintWriter;
import java.sql.SQLException;

import net.cattaka.rdbassistant.jspf.JspfConstants;
import net.cattaka.rdbassistant.jspf.core.JspfBase;
import net.cattaka.rdbassistant.jspf.core.JspfMessageException;
import net.cattaka.util.MessageBundle;

public class JspfBaseCreateInsertSql extends JspfBase {

	public JspfBaseCreateInsertSql() {
		this.setName(JspfConstants.JSPF_NAME_CREATE_INSERT_SQL);
		this.setDisplayName(MessageBundle.getInstance().getMessage("create_insert_sql"));
	}
	
	@Override
	public void runJspf(PrintWriter out) throws SQLException, JspfMessageException {
		out.println("INSERT INTO");
		if (selectionInfo.getTables().length == 1) {
			out.println("\t" + selectionInfo.getTables()[0]);
		} else {
			error(MessageBundle.getInstance().getMessage("select_single_table"));
		}
		
		// VALUESを作成
		String[] columns = selectionInfo.getColumns(); 
		if (columns.length > 0) {
			out.print("(\n");
			for (int i=0;i<columns.length;i++) {
				if (i>0) {
					out.print(",\n");
				}
				out.print("\t"+columns[i]);
			}
			out.print("\n)\n");
			out.print("VALUES\n(\n");
			for (int i=0;i<columns.length;i++) {
				if (i<columns.length-1) {
					out.print("\t\'\',\t/* "+columns[i] + " */\n");
				} else {
					out.print("\t\'\'\t/* "+columns[i] + " */\n");
				}
			}
			out.println(")");
		}
	}
}
