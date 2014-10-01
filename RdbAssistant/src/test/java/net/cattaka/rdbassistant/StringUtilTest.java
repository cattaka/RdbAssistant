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
package net.cattaka.rdbassistant;

import java.awt.Color;
import java.util.List;

import org.junit.Test;

import net.cattaka.util.StringUtil;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class StringUtilTest {
	@Test
	public void test1() {
		String str = "ab\\\\cd\\nef\\tgi";
		assertThat(str, is("ab\\\\cd\\nef\\tgi"));
		assertThat(StringUtil.replaceEscapedChar(str), is("ab\\\\cd\nef\tgi"));
	}
	@Test
	public void test2() {
		String str = "page attr1 = \"abc1\" attr2=\"abc2\"";
		List<String[]> result = StringUtil.parseAttributeString(str);
		assertThat(result.size(), is(3));
		assertThat(result.get(0)[0], is("page"));
		assertThat(result.get(0)[1], is(nullValue()));
		assertThat(result.get(1)[0], is("attr1"));
		assertThat(result.get(1)[1], is("abc1"));
		assertThat(result.get(2)[0], is("attr2"));
		assertThat(result.get(2)[1], is("abc2"));
	}
	@Test
	public void test3() {
		assertThat(StringUtil.camelToComposite("testUserTable"), is("TEST_USER_TABLE"));
		assertThat(StringUtil.camelToComposite("TestUserTable"), is("TEST_USER_TABLE"));
		assertThat(StringUtil.camelToComposite("AGradeQuantity"), is("A_GRADE_QUANTITY"));
		assertThat(StringUtil.compositeToCamel("TEST_USER_TABLE", false), is("testUserTable"));
		assertThat(StringUtil.compositeToCamel("TEST_USER_TABLE", true), is("TestUserTable"));
		assertThat(StringUtil.compositeToCamel("A_GRADE_QUANTITY", false), is("aGradeQuantity"));
		assertThat(StringUtil.compositeToCamel("A_GRADE_QUANTITY", true), is("AGradeQuantity"));
	}
	@Test
	public void test4() {
		assertThat(StringUtil.colorToHex(new Color(0x0F,0x0F,0x0F)), is("#0F0F0F"));
	}
	@Test
	public void test5() {
		String src = "'fea.faew'.abc.'abcde.'..a";
		String[] ary = StringUtil.split(src, '.', '\'');
		assertThat(ary.length, is(5));
		assertThat(ary[0], is("fea.faew"));
		assertThat(ary[1], is("abc"));
		assertThat(ary[2], is("abcde."));
		assertThat(ary[3], is(""));
		assertThat(ary[4], is("a"));
	}
}
