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
 * $Id: DefaultJspfBundle.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.standard;

import net.cattaka.rdbassistant.jspf.core.RdbaJspfBundle;

public class DefaultJspfBundle extends RdbaJspfBundle {

	public DefaultJspfBundle() {
		super();
		// 標準のJSPFを登録
		JspfBaseCreateSelectSql createSelectSql = new JspfBaseCreateSelectSql();
		JspfBaseCreateInsertSql createInsertSql = new JspfBaseCreateInsertSql();
		JspfBaseCreateUpdateSql createUpdateSql = new JspfBaseCreateUpdateSql();
		JspfBaseCreateDeleteSql createDeleteSql = new JspfBaseCreateDeleteSql();
		this.put(createSelectSql.getName(), createSelectSql);
		this.put(createInsertSql.getName(), createInsertSql);
		this.put(createUpdateSql.getName(), createUpdateSql);
		this.put(createDeleteSql.getName(), createDeleteSql);
	}
}
