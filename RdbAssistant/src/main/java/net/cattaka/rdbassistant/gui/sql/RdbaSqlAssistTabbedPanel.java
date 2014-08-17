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
 * $Id: RdbaSqlAssistTabbedPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.sql;

import java.awt.Component;

import javax.swing.JTabbedPane;

import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.util.MessageBundle;

public class RdbaSqlAssistTabbedPanel extends JTabbedPane implements RdbaGuiInterface {
	private static final long serialVersionUID = 1L;
	private RdbaGuiInterface parentComponent;
	private RdbaSqlSelectPanel rdbaSelectPanel;
	private RdbaSqlQuickAccessPanel rdbaSqlQuickAccessPanel;
	
	public RdbaSqlAssistTabbedPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		makeLayout();
	}
	
	private void makeLayout() {
		this.rdbaSelectPanel = new RdbaSqlSelectPanel(this);
		this.rdbaSqlQuickAccessPanel = new RdbaSqlQuickAccessPanel(this);
		
		this.addTab(MessageBundle.getInstance().getMessage("db_info"), this.rdbaSelectPanel);
		this.addTab(MessageBundle.getInstance().getMessage("quick_access"), this.rdbaSqlQuickAccessPanel);
	}

	public void setRdbaConnection(RdbaConnection rdbaConnection) {
		rdbaSelectPanel.setRdbaConnection(rdbaConnection);
	}

	/** {@link RdbaGuiInterface} */
	public void doGuiLayout() {
		this.rdbaSelectPanel.doGuiLayout();
	}

	/** {@link RdbaGuiInterface} */
	public RdbaConfig getRdbaConfig() {
		return parentComponent.getRdbaConfig();
	}

	/** {@link RdbaGuiInterface} */
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return parentComponent.getRdbaSingletonBundle();
	}

	/** {@link RdbaGuiInterface} */
	public void relayRdbaMessage(RdbaMessage rdbaMessage) {
		Component comp = this.getSelectedComponent();
		if (comp != null && comp instanceof RdbaGuiInterface) {
			((RdbaGuiInterface)comp).relayRdbaMessage(rdbaMessage);
		}
	}

	/** {@link RdbaGuiInterface} */
	public void reloadRdbaConfig() {
		this.rdbaSelectPanel.reloadRdbaConfig();
		this.rdbaSqlQuickAccessPanel.reloadRdbaConfig();
	}

	/** {@link RdbaGuiInterface} */
	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		parentComponent.sendRdbaMessage(rdbaMessage);
	}

	/** {@link RdbaSqlSelectPanel} {@link RdbaSqlQuickAccessPanel} */
	public void setRdbaSqlEditorTabbedPanel(RdbaSqlEditorTabbedPanel rdbaSqlEditorPanel) {
		rdbaSelectPanel.setRdbaSqlEditorTabbedPanel(rdbaSqlEditorPanel);
		rdbaSqlQuickAccessPanel.setRdbaSqlEditorTabbedPanel(rdbaSqlEditorPanel);
	}
}
