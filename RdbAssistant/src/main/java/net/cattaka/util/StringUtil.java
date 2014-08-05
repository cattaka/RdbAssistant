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
 * $Id: StringUtil.java 252 2010-02-21 13:05:58Z cattaka $
 */
package net.cattaka.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
	public static String longToTimeString(long time) {
		long min = (time/1000) / 60;
		long sec = (time/1000) % 60;
		
		return String.format("%02d:%02d", min, sec);
	}
	
	/**
	 * 文字列配列を一つの文字列にする。
	 * @param valueArray
	 * @return
	 */
	public static String encodeStringArray(String[] valueArray) {
		String result = null;
		if (valueArray != null) {
			StringBuilder sb = new StringBuilder();
			for (int i=0;i<valueArray.length;i++) {
				if (i>0) {
					sb.append("#|#");
				}
				sb.append(valueArray[i].replaceAll("\\|", "||"));
			}
			result = sb.toString();
		}
		return result;
	}
	
	/**
	 * 文字列を文字列配列に分解する。
	 * @param value
	 * @return
	 */
	public static String[] decodeStringArray(String value) {
		String[] result = null;
		if (value != null) {
			result = value.split("#\\|#",-1);
			for (int i=0;i<result.length;i++) {
				result[i] = result[i].replaceAll("\\|\\|", "|");
			}
		}
		return result;
	}
	
	/**
	 * CSV用に文字列をエスケープする。
	 * @param src
	 * @return
	 */
	public static String escapeCsvString(String src) {
		String result = src;
		if (src.indexOf(',') != -1 || src.indexOf('\n') != -1 || src.indexOf('"') != -1) {
			if (src.indexOf('"') != -1) {
				result = "\"" + src.replaceAll("\"", "\"\"") + "\"";
			} else {
				result = "\"" + src + "\"";
			}
		}
		return result;
	}
	
	/**
	 * TSV用に文字列をエスケープする。
	 * @param src
	 * @return
	 */
	public static String escapeTsvString(String src) {
		String result = src;
		if (src.indexOf('\t') != -1 || src.indexOf('\n') != -1 || src.indexOf('"') != -1) {
			if (src.indexOf('"') != -1) {
				result = "\"" + src.replaceAll("\"", "\"\"") + "\"";
			} else {
				result = "\"" + src + "\"";
			}
		}
		return result;
	}
	
	/**
	 * 汎用の文字列エスケープを行う。
	 * @param src
	 * @param delim
	 * @param bracket
	 * @return
	 */
	public static String escapeString(String src, char delim, char bracket) {
		String result = src;
		if (src.indexOf(delim) != -1 || src.indexOf('\n') != -1) {
			if (src.indexOf('"') != -1) {
				result = bracket + StringUtil.replaceString(src, String.valueOf(bracket), String.valueOf(bracket)+String.valueOf(bracket)) + bracket;
			} else {
				result = bracket + src + bracket;
			}
		}
		return result;
	}
	
	/**
	 * Java用に文字列をエスケープする。
	 * @param src
	 * @return
	 */
	public static String escapeJavaString(String src) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<src.length();i++) {
			char ch = src.charAt(i);
			switch(ch) {
			case '\n':
				sb.append("\\n");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '"':
				sb.append("\\\"");
				break;
			default:
				sb.append(ch);
				break;
			}
		}
		return sb.toString();
	}
	
	public static String escapeHtmlString(String src) {
		if (src == null) {
			return src;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < src.length(); i++) {
			char ch = src.charAt(i);
			switch (ch) {
			case '&':
				if (i + 1 <= src.length() && src.charAt(i + 1) == '#') {
					sb.append('&');
				} else {
					sb.append("&amp;");
				}
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&#39;");
				break;
			default:
				sb.append(ch);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * メッセージ表示用にObject配列をカンマ区切りの文字列にする。
	 * 
	 * @param strArr
	 * @return
	 */
	public static String toString(Object[] strArr) {
		StringBuilder sb = new StringBuilder();
		boolean flag = true;
		for (Object str : strArr) {
			if (flag) {
				flag = false;
			} else {
				sb.append(',');
			}
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static String replaceEscapedChar(String src) {
		StringBuilder sb = new StringBuilder();
		boolean escapedFlag = false;
		for (int i=0;i<src.length();i++) {
			char r = src.charAt(i);
			if (escapedFlag) {
				escapedFlag = false;
				switch (r) {
				case 'r':
					sb.append('\r');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 't':
					sb.append('\t');
					break;
				default:
					sb.append('\\');
					sb.append(r);
					break;
				}
			} else {
				if (r=='\\') {
					escapedFlag = true;
				} else {
					sb.append(r);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * {@link String#replaceAll(String, String)}は
	 * バックスラッシュを消してくれるので独自に実装。
	 * @param target
	 * @param replace
	 */
	public static String replaceString(String src, String target, String replace) {
		StringBuilder sb = new StringBuilder(src);
		replaceString(sb, target, replace);
		return sb.toString();
	}
	
	/**
	 * {@link String#replaceAll(String, String)}は
	 * バックスラッシュを消してくれるので独自に実装。
	 * @param sb
	 * @param target
	 * @param replace
	 */
	public static void replaceString(StringBuilder sb, String target, String replace) {
		int p = 0;
		while((p=sb.indexOf(target,p))!=-1) {
			sb.replace(p, p+target.length(), replace);
			p = p + replace.length();
		}
	}
	
	/**
	 * html等の属性を解析し、変換します。
	 * 'attr1="ABC" attr2="abc"'のような文字列が与えられた場合、
	 * {{"attr1","ABC"},{"attr2","abc"}}の形式に変換します。
	 * 
	 * @param arg 属性を表す文字列
	 * @return String[2]のリスト。argがnullの場合はnullを返す。
	 */
	public static List<String[]> parseAttributeString(String arg) {
		if (arg == null) {
			return null;
		}
		
		ArrayList<String[]> result = new ArrayList<String[]>();
		StringBuilder sb = new StringBuilder();
		int mode = 0;
		String[] param = new String[2];
		for (int i=0;i<=arg.length();i++) {
			char ch;
			if (i==arg.length()) {
				ch = ' ';
			} else {
				ch = arg.charAt(i);
			}
			switch(mode) {
			case 0:
				if (ch != ' ' && ch != '\t') {
					sb.append(ch);
					mode = 1;
				}
				break;
			case 1:
				if (ch == ' ' || ch == '\t' || ch == '=') {
					param[0] = sb.toString();
					sb.delete(0, sb.length());
					if (ch == ' ' || ch == '\t') {
						mode = 2;
					} else {
						mode = 3;
					}
				} else {
					sb.append(ch);
				}
				break;
			case 2:
				if (ch == ' ' || ch == '\t') {
					// 継続
				} else if (ch == '=') {
					mode = 3;
				} else {
					sb.delete(0, sb.length());
					result.add(param);
					param = new String[2];
					sb.append(ch);
					mode = 1;
				}
				break;
			case 3:
				if (ch == '"') {
					mode = 5;
				} else if (ch != ' ' && ch != '\t') {
					sb.append(ch);
					mode = 4;
				}
				break;
			case 4:
				if (ch == ' ' || ch == '\t') {
					param[1] = sb.toString();
					sb.delete(0, sb.length());
					result.add(param);
					param = new String[2];
					mode = 0;
				} else {
					sb.append(ch);
				}
				break;
			case 5:
				if (ch == '"') {
					param[1] = sb.toString();
					sb.delete(0, sb.length());
					result.add(param);
					param = new String[2];
					mode = 0;
				} else {
					sb.append(ch);
				}
				break;
			}
		}
		return result;
	}

	/**
	 * 単語連結をキャメルケースに変換します。<br/>
	 * 例:USER_TABLE_NAME → userTableName
	 * 
	 * @param composite 元となる単語連結
	 * @param ucc trueの場合は先頭を大文字にします。
	 * @return 変換されたキャメルケース
	 */
	public static String compositeToCamel(String composite, boolean ucc) {
		StringBuilder sb = new StringBuilder();
		boolean wortInitFlag = ucc;
		for (int i=0;i<composite.length();i++) {
			char ch = composite.charAt(i);
			switch(ch) {
			case '_':
				wortInitFlag = true;
				break;
			default:
				if (wortInitFlag) {
					sb.append(Character.toUpperCase(ch));
					wortInitFlag = false;
				} else {
					sb.append(Character.toLowerCase(ch));
				}
				break;
			}
		}
		return sb.toString();
	}
	
	/**
	 * キャメルケースを単語連結に変換します。<br/>
	 * 例:userTableName → USER_TABLE_NAME 
	 * 
	 * @param camel 元となるキャメルケース
	 * @return 変換された単語連結
	 */
	public static String camelToComposite(String camel) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<camel.length();i++) {
			char ch = camel.charAt(i);
			if (i>0 && Character.isUpperCase(ch)) {
				sb.append('_');
				sb.append(ch);
			} else {
				sb.append(Character.toUpperCase(ch));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 色を#FFFFFFのような文字列に変換して返します。
	 * 
	 * @param color
	 * @return
	 */
	public static String colorToHex(Color color) {
		int colorInt = 0x00FFFFFF & color.getRGB();
		return String.format("#%06X",colorInt);
	}
	
	public static String removeCarriageReturn(String arg) {
		if (arg == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder(arg.length());
		for (int i=0;i<arg.length();i++) {
			char ch = arg.charAt(i);
			if (ch != '\r') {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	public static String[] split(String src, char delim, char bracket) {
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int mode = 0;
		
		for (int i=0;i<src.length();i++) {
			char ch = src.charAt(i);
			switch(mode) {
			case 0:
				if (ch == delim) {
					result.add(sb.toString());
					sb.delete(0, sb.length());
				} else if (ch == bracket) {
					mode = 1;
				} else {
					sb.append(ch);
					mode = 3;
				}
				break;
			case 1:
				if (ch == delim) {
					sb.append(ch);
				} else if (ch == bracket) {
					mode = 2;
				} else {
					sb.append(ch);
				}
				break;
			case 2:
				if (ch == delim) {
					result.add(sb.toString());
					sb.delete(0, sb.length());
					mode = 0;
				} else if (ch == bracket) {
					sb.append(ch);
					mode = 1;
				} else {
					// 本来はあり得ない
					sb.append(delim);
					sb.append(ch);
					mode = 1;
				}
				break;
			case 3:
				if (ch == delim) {
					result.add(sb.toString());
					sb.delete(0, sb.length());
					mode = 0;
				} else if (ch == bracket) {
					sb.append(ch);
				} else {
					sb.append(ch);
				}
				break;
			default:
				throw new RuntimeException("ERROR");
			}
			//System.out.println(sb.toString());
		}
		if (mode != 0) {
			result.add(sb.toString());
		}
		
		return result.toArray(new String[result.size()]);
	}
	
	public static String[][] split(Reader reader, char delim, char bracket) throws IOException {
		ArrayList<String[]> result = new ArrayList<String[]>();
		ArrayList<String> strs = new ArrayList<String>();
		
		StringBuilder sb = new StringBuilder();
		int mode = 0;
		
		int r;
		while((r=reader.read())!= -1) {
			char ch = (char)r;
			switch(mode) {
			case 0:
				if (ch == delim) {
					strs.add(sb.toString());
					sb.delete(0, sb.length());
				} else if (ch == bracket) {
					mode = 1;
				} else if (ch == '\n') {
					result.add(strs.toArray(new String[strs.size()]));
					strs.clear();
				} else {
					sb.append(ch);
					mode = 3;
				}
				break;
			case 1:
				if (ch == delim) {
					sb.append(ch);
				} else if (ch == bracket) {
					mode = 2;
				} else {
					sb.append(ch);
				}
				break;
			case 2:
				if (ch == delim) {
					strs.add(sb.toString());
					sb.delete(0, sb.length());
					mode = 0;
				} else if (ch == bracket) {
					sb.append(ch);
					mode = 1;
				} else if (ch == '\n') {
					strs.add(sb.toString());
					sb.delete(0, sb.length());
					result.add(strs.toArray(new String[strs.size()]));
					strs.clear();
					mode = 0;
				} else {
					// 本来はあり得ない
					sb.append(delim);
					sb.append(ch);
					mode = 1;
				}
				break;
			case 3:
				if (ch == delim) {
					strs.add(sb.toString());
					sb.delete(0, sb.length());
					mode = 0;
				} else if (ch == bracket) {
					sb.append(ch);
				} else if (ch == '\n') {
					strs.add(sb.toString());
					sb.delete(0, sb.length());
					result.add(strs.toArray(new String[strs.size()]));
					strs.clear();
					mode = 0;
				} else {
					sb.append(ch);
				}
				break;
			default:
				throw new RuntimeException("ERROR");
			}
			//System.out.println(sb.toString());
		}
		if (mode != 0) {
			strs.add(sb.toString());
		}
		if (strs.size()>0) {
			result.add(strs.toArray(new String[strs.size()]));
		}
		
		return result.toArray(new String[result.size()][]);
	}
	
	public static String readFile(File file, String charset) throws IOException {
		StringBuilder sb = new StringBuilder();
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file), charset);
			int r=0;
			while((r = reader.read()) != -1) {
				sb.append((char)r);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					ExceptionHandler.warn(e);
				}
			}
		}
		return sb.toString();
	}
}
