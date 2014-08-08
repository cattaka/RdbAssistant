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
 * $Id: ButtonsBundle.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.util;

import java.awt.Insets;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.PropertiesEx;
import net.cattaka.util.ResourceUtil;


public class ButtonsBundle {
	private static HashMap<String, ButtonDefinition> buttonDifinitionMap;
	static {
		try {
			buttonDifinitionMap = new HashMap<String, ButtonDefinition>();
			PropertiesEx properties = ButtonDifinitionBundleLoader.getProperties();
			Enumeration<Object> keys = properties.keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				String[] strArray = properties.getPropertyArray(key.toString());
				ButtonDefinition buttonDefinition = new ButtonDefinition();
				if (buttonDefinition.restoreStringArray(strArray)) {
					buttonDifinitionMap.put(key.toString(), buttonDefinition);
				}
			}
		} catch(Exception e) {
			// 起こりえない
			ExceptionHandler.error(e);
		}
	}

	public static void applyMenuDifinition(JMenuItem button, String key) {
		ButtonDefinition buttonDefinition = buttonDifinitionMap.get(key);
		if (buttonDefinition != null) {
			Character mnemonic = buttonDefinition.getMnemonic();
			String buttonText = buttonDefinition.getButtonText();
			KeyStroke keyStroke = buttonDefinition.createKeyStroke();
//			String toolTip = buttonDefinition.getButtonToolTip();
			if (buttonText == null || buttonText.length() == 0) {
				buttonText = key;
			}
			if (mnemonic != null) {
				buttonText += "(" + mnemonic + ")";
				button.setMnemonic(mnemonic);
			}
			if (keyStroke != null) {
				button.setAccelerator(keyStroke);
			}
			button.setText(buttonText);
		} else {
			button.setText(key);
		}
	}
	
	public static void applyButtonDifinition(AbstractButton button, String key) {
		ButtonDefinition buttonDefinition = buttonDifinitionMap.get(key);
		if (buttonDefinition != null) {
			Character mnemonic = buttonDefinition.getMnemonic();
			String buttonText = buttonDefinition.getButtonText();
			String toolTip = buttonDefinition.getButtonToolTip();
			if (buttonText == null || buttonText.length() == 0) {
				buttonText = key;
			}
			if (mnemonic != null) {
				buttonText += "(" + mnemonic + ")";
				button.setMnemonic(mnemonic);
			}
			button.setText(buttonText);
			if (toolTip != null && toolTip.length() > 0) {
				button.setToolTipText(toolTip);
			}
		} else {
			button.setText(key);
		}
	}
	public static void applyButtonDifinition(AbstractButton button, Icon icon, String key, boolean withoutText) {
		ButtonDefinition buttonDefinition = buttonDifinitionMap.get(key);
		if (buttonDefinition != null) {
			Character mnemonic = buttonDefinition.getMnemonic();
			String buttonText = buttonDefinition.getButtonText();
			String toolTip = buttonDefinition.getButtonToolTip();
			button.setIcon(icon);
			button.setMargin(new Insets(0,0,0,0));
			if (buttonText == null || buttonText.length() == 0) {
				buttonText = key;
			}
			if (mnemonic != null) {
				buttonText += "(" + mnemonic + ")";
				button.setMnemonic(mnemonic);
			}
			if (!withoutText) {
				button.setText(buttonText);
			}
			if (toolTip != null && toolTip.length() > 0) {
				button.setToolTipText(toolTip);
			}
		} else {
			button.setText(key);
		}
	}
}

class ButtonDifinitionBundleLoader {
	public static PropertiesEx getProperties() {
		return ResourceUtil.getPropertiesExResourceAsStream("buttons%1$s.properties", true);
	}
}
