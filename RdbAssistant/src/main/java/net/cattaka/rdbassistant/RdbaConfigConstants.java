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
 * $Id: RdbaConfigConstants.java 259 2010-02-27 13:45:56Z cattaka $
 */
package net.cattaka.rdbassistant;

public class RdbaConfigConstants {
	public static final int CURRENT_CONFIGFILE_REVISION = 2;
	
	public static final String LOG_LEVEL = "rdba.log.level";
	public static final String CONFIGFILE_REVISION = "rdba.configfile.revision";
	public static final String CONNECTION_INFO_KEY = "rdba.connectioninfo";
	public static final String FONT_FOR_TABLE_KEY = "rdba.fontForTable";
	public static final String FONT_FOR_EDITOR_KEY = "rdba.fontForEditor";
	public static final String DISPLAY_STRING_IF_NULL = "rdba.displayStringIfNull";
	public static final String NULL_STRING = "rdba.nullString";
	public static final String LOOK_AND_FEEL = "rdba.lookAndFeel";
	public static final String CHARACTER_PER_TAB = "rdba.charactersPerTab";
	public static final String SQL_QUICK_ACCESS_ROOT = "rdba.sqlQuickAccessRoot";
	public static final String SCRIPT_ROOT = "rdba.scriptRoot";
	
	public static final String DRIVER_JDBC_MYSQL = "rdba.jdbc.mysql";
	public static final String DRIVER_JDBC_ORACLE = "rdba.jdbc.oracle";
	public static final String DRIVER_JDBC_SQLITE = "rdba.jdbc.sqlite";

	public static final String LIB_TOOLS = "rdba.lib.tools";
	public static final String LIB_USE_DEFAULT_TOOLS = "rdba.lib.useDefaultTools";

	public static final String DEFAULT_FONT_TABLE = "Monospaced";
	public static final String DEFAULT_FONT_EDITOR = "Monospaced";
	
	public static final String DEFAULT_SQL_QUICK_ACCESS_ROOT = "sql";
	public static final String DEFAULT_SCRIPT_ROOT = "script";
}
