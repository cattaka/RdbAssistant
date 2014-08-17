/*
 * Copyright (c) 2014, Takao Sumitomo
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
 * $Id: ButtonBundleTest.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.swing.util.ButtonDefinition;
import net.cattaka.swing.util.ButtonsBundle;

import static org.junit.Assert.*;

public class ButtonBundleTest {
	private ButtonDefinition srcDef;
	private ButtonsBundle target;

	@Before
	public void setup() {
		Map<String, ButtonDefinition> map = new HashMap<String, ButtonDefinition>();
		srcDef = new ButtonDefinition();
		srcDef.setButtonText("Text");
		srcDef.setMnemonic('S');
		srcDef.setButtonToolTip("Tooltip");
		map.put("save", srcDef);
		target = new ButtonsBundle(map);
	}
	
	@Test
	public void testApplyButtonDifinition() {
		JButton button = new JButton();
		target.applyButtonDifinition(button, "save");
		
		assertEquals("Text(S)", button.getText());
		assertEquals(srcDef.getMnemonic(), Character.valueOf((char)button.getMnemonic()));
		assertEquals(srcDef.getButtonToolTip(), button.getToolTipText());
	}
	@Test
	public void testApplyButtonDifinition_withIcon() {
		RdbaSingletonBundle rdbaSingletonBundle = new RdbaSingletonBundle();
		Icon iconNew = (Icon)rdbaSingletonBundle.getResource(RdbaMessageConstants.ICON_NEW);
		JButton button = new JButton();
		target.applyButtonDifinition(button, iconNew, "save", false);
		
		assertEquals("Text(S)", button.getText());
		assertEquals(srcDef.getMnemonic(), Character.valueOf((char)button.getMnemonic()));
		assertEquals(srcDef.getButtonToolTip(), button.getToolTipText());
		assertEquals(iconNew, button.getIcon());
	}
	@Test
	public void testApplyMenuDifinition() {
		JButton button = new JButton();
		target.applyButtonDifinition(button, "save");

		assertEquals("Text(S)", button.getText());
		assertEquals(srcDef.getMnemonic(), Character.valueOf((char)button.getMnemonic()));
		assertEquals(srcDef.getButtonToolTip(), button.getToolTipText());
	}
}
