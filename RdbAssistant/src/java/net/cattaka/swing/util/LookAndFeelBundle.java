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
 * $Id: LookAndFeelBundle.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.util;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.cattaka.util.ExceptionHandler;

public class LookAndFeelBundle {
	
	public static String getDefaultLookAndFeelName() {
		return UIManager.getSystemLookAndFeelClassName();
	}

	public static String[] getLookAndFeelClassNames() {
		UIManager.LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
		String[] result = new String[lafis.length];
		for (int i=0;i<lafis.length;i++) {
			result[i] = lafis[i].getClassName();
		}
		return result;
	}
	
	public static String[] getLookAndFeelNames() {
		UIManager.LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
		String[] result = new String[lafis.length];
		for (int i=0;i<lafis.length;i++) {
			result[i] = lafis[i].getName();
		}
		return result;
	}
	
	public static String getLookAndFeelClassName() {
		String result;
		LookAndFeel laf = UIManager.getLookAndFeel();
		if (laf != null) {
			result = laf.getClass().getName();
		} else {
			result = UIManager.getSystemLookAndFeelClassName();
		}
		return result;
	}

	public static void setLookAndFeelClassName(String lookAndFeelClassName) {
		try {
			UIManager.setLookAndFeel(lookAndFeelClassName);
		} catch (ClassNotFoundException e) {
			ExceptionHandler.error(e);
		} catch (UnsupportedLookAndFeelException e) {
			ExceptionHandler.error(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.error(e);
		} catch (InstantiationException e) {
			ExceptionHandler.error(e);
		}
		
	}
}
