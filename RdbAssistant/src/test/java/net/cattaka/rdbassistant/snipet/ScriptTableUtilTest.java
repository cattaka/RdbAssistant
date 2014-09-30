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
 * $Id: ScriptTableUtilTest.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.snipet;

import net.cattaka.rdbassistant.script.core.ScriptTable;
import net.cattaka.rdbassistant.script.core.ScriptTableUtil;

public class ScriptTableUtilTest {
	public static void main(String[] args) throws Exception {
		test2();
	}
	public static void test1() throws Exception {
		ScriptTable st = new ScriptTable(3,3);
		for (int r=0;r<st.getRowCount();r++) {
			for (int c=0;c<st.getColumnCount();c++) {
				st.setValueAt(String.format("%d,%d",r,c), r, c);
			}
		}
		st.setValueAt("ab\nc\"n,def", 1, 1);
		ScriptTableUtil.saveCsv(st, "test1.csv");
		ScriptTableUtil.saveTsv(st, "test1.tsv");
	}
	public static void test2() throws Exception {
		ScriptTable st1 = ScriptTableUtil.loadCsv("test1.csv");
		ScriptTable st2 = ScriptTableUtil.loadTsv("test1.tsv");
		ScriptTableUtil.saveCsv(st1, "test2.csv");
		ScriptTableUtil.saveTsv(st2, "test2.tsv");
	}
}
