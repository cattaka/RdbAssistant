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
 * $Id: JspfBaseCreateSelectSql.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.standard;

import java.io.PrintWriter;
import java.sql.SQLException;

import net.cattaka.rdbassistant.jspf.JspfConstants;
import net.cattaka.rdbassistant.jspf.core.JspfBase;
import net.cattaka.util.MessageBundle;

public class JspfBaseCreateSelectSql extends JspfBase {

	public JspfBaseCreateSelectSql() {
		this.setName(JspfConstants.JSPF_NAME_CREATE_SELECT_SQL);
		this.setDisplayName(MessageBundle.getInstance().getMessage("create_select_sql"));
	}
	
	@Override
	public void runJspf(PrintWriter out) throws SQLException {
		out.println("SELECT");
		boolean firstFlag = true;
		for (String columnName:selectionInfo.getColumns()) {
			if (firstFlag) {
				firstFlag = false;
			} else {
				out.print(",\n");
			}
			out.print("\t"+columnName);
		}
		out.print("\n");
		out.println("FROM");
		
		firstFlag = true;
		for (String tableName:selectionInfo.getTables()) {
			if (firstFlag) {
				firstFlag = false;
			} else {
				out.print(",\n");
			}
			out.print("\t"+tableName);
		}
		out.print("\n");
	}
}
