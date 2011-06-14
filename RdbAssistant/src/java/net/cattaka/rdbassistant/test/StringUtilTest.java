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
 * $Id: StringUtilTest.java 244 2009-11-14 07:39:36Z cattaka $
 */
package net.cattaka.rdbassistant.test;

import java.awt.Color;
import java.util.List;

import net.cattaka.util.StringUtil;

public class StringUtilTest {
	public static void main(String[] args) {
		test5();
	}
	public static void test1() {
		String str = "ab\\\\cd\\nef\\tgi";
		System.out.println(str);
		System.out.println();
		System.out.println(StringUtil.replaceEscapedChar(str));
	}
	public static void test2() {
		String str = "page attr1 = \"abc1\" attr2=\"abc2\"";
		List<String[]> result = StringUtil.parseAttributeString(str);
		for (String[] r:result) {
			System.out.println(r[0] + " : " + r[1]);
		}
	}
	public static void test3() {
		System.out.println(StringUtil.camelToComposite("testUserTable"));
		System.out.println(StringUtil.camelToComposite("TestUserTable"));
		System.out.println(StringUtil.camelToComposite("AGradeQuantity"));
		System.out.println(StringUtil.compositeToCamel("TEST_USER_TABLE", false));
		System.out.println(StringUtil.compositeToCamel("TEST_USER_TABLE", true));
		System.out.println(StringUtil.compositeToCamel("A_GRADE_QUANTITY", false));
		System.out.println(StringUtil.compositeToCamel("A_GRADE_QUANTITY", true));
	}
	public static void test4() {
		System.out.println(StringUtil.colorToHex(new Color(0x0F,0x0F,0x0F)));
	}
	public static void test5() {
		String src = "'fea.faew'.abc.'abcde.'..a";
		String[] ary = StringUtil.split(src, '.', '\'');
		for (String str:ary) {
			System.out.println(str);
		}
	}
}
