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
 * $Id: RdbaConfig.java 259 2010-02-27 13:45:56Z cattaka $
 */
package net.cattaka.rdbassistant.config;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

import net.cattaka.jspf.ToolsJarBundle;
import net.cattaka.rdbassistant.RdbaConfigConstants;
import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.swing.util.LookAndFeelBundle;
import net.cattaka.util.PropertiesEx;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.StringUtil;
import net.cattaka.util.ExceptionHandler.Priority;

public class RdbaConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<RdbaConnectionInfo> rdbaConnectionInfo = new ArrayList<RdbaConnectionInfo>();
	private PropertiesEx properties = new PropertiesEx();
	private Font fontForEditor;
	private Font fontForTable;
	private boolean displayStringIfNull;
	private String nullString;
	private int charactersPerTab = 4;
	private RdbaJdbcBundle rdbaJdbcBundle = new RdbaJdbcBundle();
	private boolean useDefaultToolsJar;
	private ToolsJarBundle toolsJarBundle = new ToolsJarBundle();
	private File sqlQuickAccessRoot;
	private File scriptRoot;
	
	public RdbaConfig() {
	}
	
	//-- ConnectionInfo --------------------------------------------------
	public List<RdbaConnectionInfo> getRdbaConnectionInfo() {
		return rdbaConnectionInfo;
	}
	
	public void setRdbaConnectionInfo(List<RdbaConnectionInfo> rdbaConnectionInfo) {
		this.rdbaConnectionInfo = new ArrayList<RdbaConnectionInfo>(rdbaConnectionInfo);
		
		// ConnectionInfo更新
		String[] rciStrArray = new String[rdbaConnectionInfo.size()];
		for (int i=0;i<this.rdbaConnectionInfo.size();i++) {
			RdbaConnectionInfo rci = this.rdbaConnectionInfo.get(i);
			rciStrArray[i] = StringUtil.encodeStringArray(rci.toStringArray());
		}
		this.setPropertyArray(RdbaConfigConstants.CONNECTION_INFO_KEY, rciStrArray);
	}

	//-- FontFotTable --------------------------------------------------
	public Font getFontForTable() {
		return fontForTable;
	}

	public void setFontForTable(Font fontForTable) {
		this.fontForTable = fontForTable;
		String[] fttStrArray = new String[] {
			fontForTable.getFamily(),
			String.valueOf(fontForTable.getStyle()),
			String.valueOf(fontForTable.getSize())
		};
		this.setPropertyArray(RdbaConfigConstants.FONT_FOR_TABLE_KEY, fttStrArray);
	}

	//-- FontFotEditor --------------------------------------------------
	public Font getFontForEditor() {
		return fontForEditor;
	}

	public void setFontForEditor(Font fontForEditor) {
		this.fontForEditor = fontForEditor;
		String[] fttStrArray = new String[] {
			fontForEditor.getFamily(),
			String.valueOf(fontForEditor.getStyle()),
			String.valueOf(fontForEditor.getSize())
		};
		this.setPropertyArray(RdbaConfigConstants.FONT_FOR_EDITOR_KEY, fttStrArray);
	}
	
	// -- displayStringIfNull ---------------------------------------------------
	
	public boolean isDisplayStringIfNull() {
		return displayStringIfNull;
	}

	public void setDisplayStringIfNull(boolean displayStringIfNull) {
		this.displayStringIfNull = displayStringIfNull;
		this.setProperty(RdbaConfigConstants.DISPLAY_STRING_IF_NULL, displayStringIfNull ? "1" : "0");
	}

	// -- nullString ---------------------------------------------------

	public String getNullString() {
		return nullString;
	}

	public void setNullString(String nullString) {
		this.nullString = nullString;
		this.setProperty(RdbaConfigConstants.NULL_STRING, nullString);
	}

	// -- charactersPerTab ----------------------------------------------
	public int getCharactersPerTab() {
		return charactersPerTab;
	}

	public void setCharactersPerTab(int charactersPerTab) {
		this.charactersPerTab = charactersPerTab;
		this.setProperty(RdbaConfigConstants.CHARACTER_PER_TAB, String.valueOf(charactersPerTab));
	}

	// -- sqlQuickAccessRoot ---------------------------------------------
	public File getSqlQuickAccessRoot() {
		return sqlQuickAccessRoot;
	}

	public boolean setSqlQuickAccessRoot(File sqlQuickAccessRoot) {
		if (sqlQuickAccessRoot != null && sqlQuickAccessRoot.isDirectory()) {
			this.sqlQuickAccessRoot = sqlQuickAccessRoot;
			this.setProperty(RdbaConfigConstants.SQL_QUICK_ACCESS_ROOT, sqlQuickAccessRoot.getPath());
			return true;
		} else {
			return false;
		}
	}

	// -- scriptRoot ---------------------------------------------------
	public File getScriptRoot() {
		return scriptRoot;
	}

	public boolean setScriptRoot(File scriptRoot) {
		if (scriptRoot != null && scriptRoot.isDirectory()) {
			this.scriptRoot = scriptRoot;
			this.setProperty(RdbaConfigConstants.SCRIPT_ROOT, scriptRoot.getPath());
			return true;
		} else {
			return false;
		}
	}
	
	// -- JdbcJarMysql ---------------------------------------------------
	public void setJdbcJarMysql(String jarName) {
		this.setProperty(RdbaConfigConstants.DRIVER_JDBC_MYSQL, jarName);
		this.rdbaJdbcBundle.reloadJdbc(this);
	}

	public String getJdbcJarMysql() {
		String result = this.getProperty(RdbaConfigConstants.DRIVER_JDBC_MYSQL);
		return (result != null) ? result : ""; 
	}

	// -- JdbcJarOracle ---------------------------------------------------
	public void setJdbcJarOracle(String jarName) {
		this.setProperty(RdbaConfigConstants.DRIVER_JDBC_ORACLE, jarName);
		this.rdbaJdbcBundle.reloadJdbc(this);
	}

	public String getJdbcJarOracle() {
		String result = this.getProperty(RdbaConfigConstants.DRIVER_JDBC_ORACLE);
		return (result != null) ? result : ""; 
	}
	
	// -- JdbcJarOracle ---------------------------------------------------
	public void setJdbcJarSqlite(String jarName) {
		this.setProperty(RdbaConfigConstants.DRIVER_JDBC_SQLITE, jarName);
		this.rdbaJdbcBundle.reloadJdbc(this);
	}

	public String getJdbcJarSqlite() {
		String result = this.getProperty(RdbaConfigConstants.DRIVER_JDBC_SQLITE);
		return (result != null) ? result : ""; 
	}
	
	// -- useDefaultToolsJar ---------------------------------------------------
	public boolean isUseDefaultToolsJar() {
		return useDefaultToolsJar;
	}

	public void setUseDefaultToolsJar(boolean useDefaultToolsJar) {
		this.useDefaultToolsJar = useDefaultToolsJar;
		this.setProperty(RdbaConfigConstants.LIB_USE_DEFAULT_TOOLS, useDefaultToolsJar ? "1" : "0");
	}
	
	// -- ToolsJar ---------------------------------------------------
	public void setToolsJar(String jarName) {
		this.setProperty(RdbaConfigConstants.LIB_TOOLS, jarName);
		this.toolsJarBundle.reloadTools(this);
	}

	public String getToolsJar() {
		String result = this.getProperty(RdbaConfigConstants.LIB_TOOLS);
		return (result != null) ? result : ""; 
	}
	
	// -- logLevel ---------------------------------------------------
	public String getLogLevel() {
		return ExceptionHandler.getCurrentPriority().name();
	}

	public void setLogLevel(String logLevel) {
		logLevel = (logLevel != null) ? logLevel : "";
		try {
			Priority priority = ExceptionHandler.Priority.valueOf(logLevel);
			ExceptionHandler.setCurrentPriority(priority);
			this.setProperty(RdbaConfigConstants.LOG_LEVEL, logLevel);
		} catch (IllegalArgumentException e) {
			ExceptionHandler.error(e);
		}
	}
	// -- lookAndFeel ---------------------------------------------------
	public void setLookAndFeel(String lookAndFeel) {
		this.setProperty(RdbaConfigConstants.LOOK_AND_FEEL, lookAndFeel);
		LookAndFeelBundle.setLookAndFeelClassName(lookAndFeel);
	}
	
	public String getLookAndFeel() {
		return LookAndFeelBundle.getLookAndFeelClassName();
	}

	//-- 更新処理 --------------------------------------------------
	public void updateArtificialInfo() {
		// Configファイルのリビジョンを設定する
		int configRevision = RdbaConfigConstants.CURRENT_CONFIGFILE_REVISION;
		{
			String revStr = this.getProperty(RdbaConfigConstants.CONFIGFILE_REVISION);
			try {
				if (revStr != null) {
					configRevision = Integer.parseInt(revStr);
				}
			} catch (NumberFormatException e) {
				ExceptionHandler.error(e);
			}
		}
		
		// ConnectionInfo作成
		{
			this.rdbaConnectionInfo = new ArrayList<RdbaConnectionInfo>();
			String[] strArray = this.getPropertyArray(RdbaConfigConstants.CONNECTION_INFO_KEY);
			if (strArray != null && strArray.length > 0) {
				for (String str : strArray) {
					if (str.length() == 0) {
						continue;
					}
					String[] st = StringUtil.decodeStringArray(str);
					try {
						Class<?> connectionInfoClass = Class.forName(st[0]);
						Object obj = connectionInfoClass.newInstance();
						if (obj instanceof RdbaConnectionInfo) {
							RdbaConnectionInfo rci = (RdbaConnectionInfo)obj;
							if (rci.restoreStringArray(st, configRevision)) {
								rdbaConnectionInfo.add(rci);
							}
						}
					} catch (ClassNotFoundException e) {
						ExceptionHandler.error(e);
					} catch (IllegalAccessException e) {
						ExceptionHandler.fatal(e);
					} catch (InstantiationException e) {
						ExceptionHandler.fatal(e);
					}
				}
			}
		}
		// fontForTable作成
		{
			String[] values = this.getPropertyArray(RdbaConfigConstants.FONT_FOR_TABLE_KEY);
			if (values != null && values.length >= 3) {
				int style = Integer.parseInt(values[1]);
				int size = Integer.parseInt(values[2]);
				fontForTable = new Font(values[0], style, size);
			}
			if (fontForTable == null) {
				setFontForTable(new Font(RdbaConfigConstants.DEFAULT_FONT_TABLE, Font.PLAIN, 12));
			}
		}
		// fontForEditor作成
		{
			String[] values = this.getPropertyArray(RdbaConfigConstants.FONT_FOR_EDITOR_KEY);
			if (values != null && values.length >= 3) {
				int style = Integer.parseInt(values[1]);
				int size = Integer.parseInt(values[2]);
				fontForEditor = new Font(values[0], style, size);
			}
			if (fontForEditor == null) {
				setFontForEditor(new Font(RdbaConfigConstants.DEFAULT_FONT_EDITOR, Font.PLAIN, 12));
			}
		}
		// displayStringIfNull
		{
			String value = this.getProperty(RdbaConfigConstants.DISPLAY_STRING_IF_NULL);
			if (value != null) {
				this.displayStringIfNull = "1".equals(value);
			} else {
				this.displayStringIfNull = false;
			}
		}
		// nullString
		{
			String value = this.getProperty(RdbaConfigConstants.NULL_STRING);
			if (value != null) {
				this.nullString = value;
			} else {
				this.nullString = RdbaConstants.DEFAULT_NULL_STRING;
			}
		}
		
		// charactersPerTab作成
		{
			String value = this.getProperty(RdbaConfigConstants.CHARACTER_PER_TAB);
			Integer cpt = null;
			if (value != null) {
				try {
					cpt = Integer.valueOf(value);
					this.charactersPerTab = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					ExceptionHandler.error(e);
				}
			}
			if (cpt == null) {
				cpt = 4;
			}
			this.charactersPerTab = cpt;
		}
		// sqlQuickAccessRoot
		{
			String value = this.getProperty(RdbaConfigConstants.SQL_QUICK_ACCESS_ROOT);
			File sqar = null;
			if (value != null && value.length() > 0) {
				sqar = new File(value);
				if (!sqar.exists() || !sqar.isDirectory()) {
					sqar = null;
				}
			}
			if (sqar == null) {
				// 取得できなかった場合はユーザホームを取得する。
				value = RdbaConfigConstants.DEFAULT_SQL_QUICK_ACCESS_ROOT;
				if (value != null && value.length() > 0) {
					sqar = new File(value);
					if (!sqar.exists() || !sqar.isDirectory()) {
						sqar = null;
					}
				}
			}
			if (sqar == null) {
				// あり得ないが念のため。
				sqar = new File(".");
			}
			this.sqlQuickAccessRoot = sqar;
		}

		// scriptRoot
		{
			String value = this.getProperty(RdbaConfigConstants.SCRIPT_ROOT);
			File sqar = null;
			if (value != null && value.length() > 0) {
				sqar = new File(value);
				if (!sqar.exists() || !sqar.isDirectory()) {
					sqar = null;
				}
			}
			if (sqar == null) {
				// 取得できなかった場合はユーザホームを取得する。
				value = RdbaConfigConstants.DEFAULT_SCRIPT_ROOT;
				if (value != null && value.length() > 0) {
					sqar = new File(value);
					if (!sqar.exists() || !sqar.isDirectory()) {
						sqar = null;
					}
				}
			}
			if (sqar == null) {
				// 無いなら諦める
			}
			this.scriptRoot = sqar;
		}
		
		// rdbaJdbcBundle作成
		{
			this.rdbaJdbcBundle.reloadJdbc(this);
		}

		// useDefaultToolsJar
		{
			String value = this.getProperty(RdbaConfigConstants.LIB_USE_DEFAULT_TOOLS);
			if (value != null) {
				this.useDefaultToolsJar = "1".equals(value);
			} else {
				this.useDefaultToolsJar = true;
			}
		}
		// toolsJar作成
		{
			this.toolsJarBundle.reloadTools(this);
		}
		
		// logLevelの設定
		{
			String value = this.getProperty(RdbaConfigConstants.LOG_LEVEL);
			value = (value != null) ? value : "";
			try {
				Priority priority = ExceptionHandler.Priority.valueOf(value);
				ExceptionHandler.setCurrentPriority(priority);
			} catch (IllegalArgumentException e) {
//				ExceptionHandler.error(e);
			}
		}
		// LookAndFeelの設定
		{
			String value = this.getProperty(RdbaConfigConstants.LOOK_AND_FEEL);
			if (value != null) {
				LookAndFeelBundle.setLookAndFeelClassName(value);
			} else {
				value = LookAndFeelBundle.getLookAndFeelClassName();
			}
		}
	}

	// -- 文字列直接取得＆設定 -------------------------------------------------------------
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Object setProperty(String key, String value) {
		return properties.setProperty(key, value);
	}

	public String[] getPropertyArray(String key) {
		return properties.getPropertyArray(key);
	}

	public String[] setPropertyArray(String key, String[] valueArray) {
		return properties.setPropertyArray(key, valueArray);
	}

	// -- 入出力 -------------------------------------------------------------

	public RdbaJdbcBundle getRdbaJdbcBundle() {
		return rdbaJdbcBundle;
	}
	
	public ToolsJarBundle getToolsJarBundle() {
		return toolsJarBundle;
	}

	public void load(InputStream inStream) throws IOException {
		properties.load(inStream);
		upgradeToNewVersion();
		updateArtificialInfo();
	}

	public void loadFromXML(InputStream in) throws IOException, InvalidPropertiesFormatException {
		properties.loadFromXML(in);
		upgradeToNewVersion();
		updateArtificialInfo();
	}

	public void store(OutputStream out, String comments) throws IOException {
		setProperty(RdbaConfigConstants.CONFIGFILE_REVISION, String.valueOf(RdbaConfigConstants.CURRENT_CONFIGFILE_REVISION));
		properties.store(out, comments);
	}

	public void storeToXML(OutputStream os, String comment, String encoding)
			throws IOException {
		setProperty(RdbaConfigConstants.CONFIGFILE_REVISION, String.valueOf(RdbaConfigConstants.CURRENT_CONFIGFILE_REVISION));
		properties.storeToXML(os, comment, encoding);
	}

	public void storeToXML(OutputStream os, String comment) throws IOException {
		setProperty(RdbaConfigConstants.CONFIGFILE_REVISION, String.valueOf(RdbaConfigConstants.CURRENT_CONFIGFILE_REVISION));
		properties.storeToXML(os, comment);
	}
	
	public void upgradeToNewVersion() {
		int configRevision = RdbaConfigConstants.CURRENT_CONFIGFILE_REVISION;
		{
			String revStr = this.getProperty(RdbaConfigConstants.CONFIGFILE_REVISION);
			try {
				if (revStr != null) {
					configRevision = Integer.parseInt(revStr);
				}
			} catch (NumberFormatException e) {
				ExceptionHandler.error(e);
			}
		}
		if (configRevision == 1) {
			// ConnectionInfo更新
			{
				Map<String, String> driverNameMap = new HashMap<String, String>();
				driverNameMap.put("net.cattaka.rdbassistant.dummy.DummyRdbaConnectionInfo", "net.cattaka.rdbassistant.driver.dummy.DummyRdbaConnectionInfo");
				driverNameMap.put("net.cattaka.rdbassistant.mysql.MySqlRdbaConnectionInfo","net.cattaka.rdbassistant.driver.mysql.MySqlRdbaConnectionInfo");
				driverNameMap.put("net.cattaka.rdbassistant.oracle.OracleRdbaConnectionInfo","net.cattaka.rdbassistant.driver.oracle.OracleRdbaConnectionInfo");
				driverNameMap.put("net.cattaka.rdbassistant.sqlite.SqliteRdbaConnectionInfo","net.cattaka.rdbassistant.driver.sqlite.SqliteRdbaConnectionInfo");
				
				this.rdbaConnectionInfo = new ArrayList<RdbaConnectionInfo>();
				String[] strArray = this.getPropertyArray(RdbaConfigConstants.CONNECTION_INFO_KEY);
				if (strArray != null && strArray.length > 0) {
					for (int i=0;i<strArray.length;i++) {
						String str = strArray[i];
						if (str.length() == 0) {
							continue;
						}
						String[] st = StringUtil.decodeStringArray(str);
						String newValue = driverNameMap.get(st[0]);
						if (newValue != null) {
							st[0] = newValue;
							strArray[i] = StringUtil.encodeStringArray(st);
						}
					}
					this.setPropertyArray(RdbaConfigConstants.CONNECTION_INFO_KEY, strArray);
				}
			}
		}
	}
}
