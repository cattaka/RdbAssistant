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
 * $Id: RdbaConnectionPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JMenu;
import javax.swing.JPanel;

import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;

public class RdbaConnectionPanel extends JPanel implements RdbaGuiInterface, RdbaModeInterface {
	private static final long serialVersionUID = 1L;
	private RdbaModeTabbedPanel rdbaModeTabbedPanel;
	private RdbaGuiInterface parentComponent;
	
	private RdbaConnection rdbConnection;

	public RdbaConnectionPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		this.rdbaModeTabbedPanel = new RdbaModeTabbedPanel(this.parentComponent);
		
		this.setLayout(new GridLayout());
		this.add(rdbaModeTabbedPanel);
	}

	public void setRdbConnection(RdbaConnection rdbConnection) {
		this.rdbConnection = rdbConnection;
		this.rdbaModeTabbedPanel.setRdbConnection(this.rdbConnection);
	}
	
	public void dispose() {
		rdbConnection.dispose();
	}

	public void doGuiLayout() {
		this.rdbaModeTabbedPanel.doGuiLayout();
	}
	
	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}
	
	public void reloadRdbaConfig() {
		this.rdbaModeTabbedPanel.reloadRdbaConfig();
	}

	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rbdaMessage) {
		this.rdbaModeTabbedPanel.relayRdbaMessage(rbdaMessage);
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.parentComponent.getRdbaSingletonBundle();
	}
	
	/** {@link RdbaModeInterface} */
	public JMenu[] getExtraMenu() {
		Component component = rdbaModeTabbedPanel.getSelectedComponent();
		if (component instanceof RdbaModeInterface) {
			return ((RdbaModeInterface)component).getExtraMenu();
		} else {
			return new JMenu[0];
		}
	}

	/** {@link RdbaModeInterface} */
	public boolean updateMenu(TargetMenu targetMenu, JMenu menu) {
		Component component = rdbaModeTabbedPanel.getSelectedComponent();
		if (component instanceof RdbaModeInterface) {
			return ((RdbaModeInterface)component).updateMenu(targetMenu, menu);
		} else {
			return false;
		}
	}
}
