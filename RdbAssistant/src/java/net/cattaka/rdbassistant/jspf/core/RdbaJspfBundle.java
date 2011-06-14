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
 * $Id: RdbaJspfBundle.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.core;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.cattaka.rdbassistant.jspf.core.bean.DatabaseInfo;

public class RdbaJspfBundle {
	private List<String> jspfList;
	private HashMap<String, JspfBase> jspfBaseMap;
	
	public RdbaJspfBundle() {
		this.jspfList = new ArrayList<String>();
		this.jspfBaseMap = new HashMap<String, JspfBase>();
	}
	
	public JspfBase[] getJspfList() {
		JspfBase[] result = new JspfBase[this.jspfList.size()];
		for (int i=0;i<result.length;i++) {
			result[i] = jspfBaseMap.get(this.jspfList.get(i));
		}
		return result;
	}
	
	public void runJspfBundle(String name, JspfSelectionInfo selectionInfo, DatabaseInfo databaseInfo, PrintWriter out)
		throws SQLException, JspfNotFoundException, JspfMessageException
	{
		JspfBase jspfBase = this.get(name);
		if (jspfBase == null) {
			throw new JspfNotFoundException("JSPF : "+name+" is not found.");
		}
		jspfBase.setDatabaseInfo(databaseInfo);
		jspfBase.setSelectionInfo(selectionInfo);
		jspfBase.runJspf(out);
	}

	public JspfBase put(String key, JspfBase value) {
		JspfBase jspfBase = jspfBaseMap.put(key, value);
		if (!this.jspfList.contains(key)) {
			this.jspfList.add(key);
		}
		return jspfBase;
	}

	public JspfBase remove(Object key) {
		jspfList.remove(key);
		return jspfBaseMap.remove(key);
	}

	public JspfBase get(String name) {
		return jspfBaseMap.get(name);
	}
	
}
