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
 * $Id: RdbaModeTabbedPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui;


import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.script.RdbaScriptModePanel;
import net.cattaka.rdbassistant.gui.sql.RdbaSqlModePanel;
import net.cattaka.util.MessageBundle;

public class RdbaModeTabbedPanel extends JTabbedPane implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;
	
	private RdbaSqlModePanel rdbaSqlModePanel;
	private RdbaScriptModePanel rdbaScriptModePanel;
	private RdbaConnection rdbConnection;
	private RdbaGuiInterface parentComponent;

	class ChangeListenerImpl implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBAASSISTANTPANEL_UPDATEMENU, null, RdbaModeTabbedPanel.this, null));
		}
	}
	
	public RdbaModeTabbedPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}

	private void makeLayout() {
		this.rdbaSqlModePanel = new RdbaSqlModePanel(this.parentComponent);
		this.rdbaScriptModePanel = new RdbaScriptModePanel(this.parentComponent);
		
		this.add(MessageBundle.getInstance().getMessage("sql"), this.rdbaSqlModePanel);
		this.add(MessageBundle.getInstance().getMessage("script"), this.rdbaScriptModePanel);

		// タブ操作についての処理を追加
		this.addChangeListener(new ChangeListenerImpl());
	}

	public void setRdbConnection(RdbaConnection rdbConnection) {
		this.rdbConnection = rdbConnection;
		this.rdbaSqlModePanel.setRdbConnection(this.rdbConnection);
		this.rdbaScriptModePanel.setRdbConnection(this.rdbConnection);
	}
	
	public void doGuiLayout() {
		this.rdbaSqlModePanel.doGuiLayout();
		this.rdbaScriptModePanel.doGuiLayout();
	}
	
	public void reloadRdbaConfig() {
		this.rdbaSqlModePanel.reloadRdbaConfig();
		this.rdbaScriptModePanel.reloadRdbaConfig();
	}
	
	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}

	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rbdaMessage) {
		this.rdbaSqlModePanel.relayRdbaMessage(rbdaMessage);
		this.rdbaScriptModePanel.relayRdbaMessage(rbdaMessage);
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.parentComponent.getRdbaSingletonBundle();
	}
}
