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
 * $Id: JspfSelectionInfo.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.jspf.core;

public class JspfSelectionInfo {
	/** デフォルトのデータベース */
	private String defaultDatabase;
	
	/** 選択中のDatabese(実質1つだけ) */
	private String[] databases = new String[0];

	/** 選択中のType(実質1つだけ) */
	private String[] types = new String[0];
	
	/**
	 * 選択中のTable.０〜ｎ個。
	 */
	private String[] tables = new String[0];
	
	/**
	 * 選択中のColumn.
	 * 選択中のTableが複数なら０個。
	 * 選択中のTableが一つならｎ個。
	 */
	private String[] columns = new String[0];

	/** コンストラクタ */
	public JspfSelectionInfo() {
	}
	
	public String getDefaultDatabase() {
		return defaultDatabase;
	}

	public void setDefaultDatabase(String defaultDatabase) {
		this.defaultDatabase = defaultDatabase;
	}

	public String[] getDatabases() {
		return databases;
	}

	public void setDatabases(String[] databases) {
		if (databases == null) {
			// NULLを渡すなよ・・・
			throw new NullPointerException();
		}
		this.databases = databases;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		if (types == null) {
			// NULLを渡すなよ・・・
			throw new NullPointerException();
		}
		this.types = types;
	}

	public String[] getTables() {
		return tables;
	}

	public void setTables(String[] tables) {
		if (tables == null) {
			// NULLを渡すなよ・・・
			throw new NullPointerException();
		}
		this.tables = tables;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		if (columns == null) {
			// NULLを渡すなよ・・・
			throw new NullPointerException();
		}
		this.columns = columns;
	}
}
