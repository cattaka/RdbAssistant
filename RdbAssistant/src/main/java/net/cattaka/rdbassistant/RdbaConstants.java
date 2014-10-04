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
 * $Id: RdbaConstants.java 275 2010-03-02 13:42:49Z cattaka $
 */
package net.cattaka.rdbassistant;

import java.nio.charset.Charset;

public class RdbaConstants {
	public static final String RDBA_CONNECTION_INFO_FILE = "connectioninfo.xml";
	public static final String RDBA_CONFIG_FILE = "config.xml";
	public static final String MYSQL_RESERVED_WORDS_FILE = "ReservedWords.mysql.properties";
	public static final String ORACLE_RESERVED_WORDS_FILE = "ReservedWords.oracle.properties";
	public static final String SCRIPT_RESERVED_WORDS_FILE = "ReservedWords.script.properties";

	public static final String RDBA_SCRIPT_DIR = "script";
	public static final String RDBA_SCRIPT_BASE = "script/RdbaScriptBase.txt";
	
	public static final String STRING_FOR_MESURE_WIDTH = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final int TABLE_MARGIN_CHARS = 2;
	public static final int MAX_CACHE_ROWS = 1000;
	public static final int DEFAULT_COLUMN_WIDTH = 150;
	public static final int MINIMUM_COLUMN_CHARACTERS = 10;
	public static final int MAXIMUM_COLUMN_CHARACTERS = 30;
	public static final String DEFAULT_NULL_STRING = "(null)";
	public static final String NOT_SUPPORTED_STRING = "(Not Supported)";
	
	public static final java.nio.charset.Charset DEFAULT_CHAR_SET = Charset.forName("UTF-8"); 
}
