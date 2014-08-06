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
 * $Id: RdbaSingletonBundle.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.bean;

import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

import net.cattaka.jspf.JspfBundle;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.jspf.core.RdbaJspfBundle;
import net.cattaka.rdbassistant.script.core.RdbaScriptUtil;
import net.cattaka.swing.TextFileChooser;

public class RdbaSingletonBundle {
	private HashMap<String,Object> resourceMap = new HashMap<String, Object>();
	private TextFileChooser sqlTextFileChooser;
	private TextFileChooser scriptTextFileChooser;
	private RdbaJspfBundle jspfDefaultBundle;
	private JspfBundle<RdbaScriptUtil> scriptBundle;
	
	public RdbaSingletonBundle() {
		createResource();
	}
	
	// -- getter/setter -------------------------------------------------
	public RdbaJspfBundle getDefaultJspfBundle() {
		return jspfDefaultBundle;
	}

	public void setDefaultJspfBundle(RdbaJspfBundle jspfBundle) {
		this.jspfDefaultBundle = jspfBundle;
	}
	
	public JspfBundle<RdbaScriptUtil> getScriptBundle() {
		return scriptBundle;
	}

	public void setScriptBundle(JspfBundle<RdbaScriptUtil> scriptBundle) {
		this.scriptBundle = scriptBundle;
	}

	public TextFileChooser getSqlTextFileChooser() {
		return sqlTextFileChooser;
	}

	public void setSqlTextFileChooser(TextFileChooser sqlTextFileChooser) {
		this.sqlTextFileChooser = sqlTextFileChooser;
	}

	public TextFileChooser getScriptTextFileChooser() {
		return scriptTextFileChooser;
	}

	public void setScriptTextFileChooser(TextFileChooser scriptTextFileChooser) {
		this.scriptTextFileChooser = scriptTextFileChooser;
	}

	public Object getResource(String name) {
		return resourceMap.get(name);
	}

	// -- other -------------------------------------------------
	// リソースの作成
	private void createResource() {
		// アイコン
		{
			URL iconNewUrl = this.getClass().getClassLoader().getResource("icon/m_new.png");
			URL iconOpenUrl = this.getClass().getClassLoader().getResource("icon/m_open.png");
			URL iconSaveUrl = this.getClass().getClassLoader().getResource("icon/m_save.png");
			URL iconSaveasUrl = this.getClass().getClassLoader().getResource("icon/m_saveas.png");
			URL iconRunUrl = this.getClass().getClassLoader().getResource("icon/m_run.png");
			URL iconCompileUrl = this.getClass().getClassLoader().getResource("icon/m_compile.png");
			URL iconFindUrl = this.getClass().getClassLoader().getResource("icon/m_find.png");
			URL iconGcUrl = this.getClass().getClassLoader().getResource("icon/m_gc.png");
			URL iconTransposeUrl = this.getClass().getClassLoader().getResource("icon/m_transpose.png");
			ImageIcon iconNew = new ImageIcon(iconNewUrl);
			ImageIcon iconOpen = new ImageIcon(iconOpenUrl);
			ImageIcon iconSave = new ImageIcon(iconSaveUrl);
			ImageIcon iconSaveas = new ImageIcon(iconSaveasUrl);
			ImageIcon iconRun = new ImageIcon(iconRunUrl);
			ImageIcon iconCompile = new ImageIcon(iconCompileUrl);
			ImageIcon iconFind = new ImageIcon(iconFindUrl);
			ImageIcon iconGc = new ImageIcon(iconGcUrl);
			ImageIcon iconTranspose = new ImageIcon(iconTransposeUrl);
			
			resourceMap.put(RdbaMessageConstants.ICON_NEW, iconNew);
			resourceMap.put(RdbaMessageConstants.ICON_OPEN, iconOpen);
			resourceMap.put(RdbaMessageConstants.ICON_SAVE, iconSave);
			resourceMap.put(RdbaMessageConstants.ICON_SAVEAS, iconSaveas);
			resourceMap.put(RdbaMessageConstants.ICON_RUN, iconRun);
			resourceMap.put(RdbaMessageConstants.ICON_COMPILE, iconCompile);
			resourceMap.put(RdbaMessageConstants.ICON_FIND, iconFind);
			resourceMap.put(RdbaMessageConstants.ICON_GC, iconGc);
			resourceMap.put(RdbaMessageConstants.ICON_TRANSPOSE, iconTranspose);
		}
	}
}
