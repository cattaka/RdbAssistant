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
 * $Id: RdbaConnectionTabbedPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoListPanel;
import net.cattaka.rdbassistant.util.CloseableTabbedPane;
import net.cattaka.rdbassistant.util.CloseableTabbedPaneListener;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class RdbaConnectionTabbedPanel extends CloseableTabbedPane implements RdbaGuiInterface, RdbaModeInterface {
	private static final long serialVersionUID = 1L;
	private RdbaConnectionInfoListPanel rdbaConnectionInfoListPanel;
	private RdbaGuiInterface parentComponent;
	
	class RdbaConnectionInfoListPanelEx extends RdbaConnectionInfoListPanel {
		private static final long serialVersionUID = 1L;

		public RdbaConnectionInfoListPanelEx(RdbaGuiInterface parentComponent) {
			super(parentComponent);
			setButtonsVisible(true, false);
		}

		@Override
		public void doCancel() {
			// 無し
		}

		@Override
		public void doConnect(RdbaConnectionInfo rci) {
			String messageName = rci.getDisplayStrings()[1];
			try {
				RdbaConnection rdbaConnection = rci.createConnection(getRdbaConfig().getRdbaJdbcBundle());
				RdbaConnectionPanel rdbaConnectionPanel = new RdbaConnectionPanel(parentComponent);
				rdbaConnectionPanel.setRdbConnection(rdbaConnection);
				String displayName = rci.getLabel();
				RdbaConnectionTabbedPanel.this.addTab(displayName, rdbaConnectionPanel);
				rdbaConnectionPanel.reloadRdbaConfig();

				int tabCount = RdbaConnectionTabbedPanel.this.getTabCount();

				// ツールチップを設定
				String tooltipText = rci.getTooltipText();
				RdbaConnectionTabbedPanel.this.setToolTipTextAt(tabCount-1, tooltipText);

				// 新しく作ったタブを表示
				RdbaConnectionTabbedPanel.this.setSelectedIndex(tabCount-1);
				
				// 新しく作ったタブのレイアウトを初期化する
				RdbaGuiUtil.doLayout(rdbaConnectionPanel);
				
				// ステータスバーに表示。
				String message = String.format(MessageBundle.getInstance().getMessage("connecting_is_succeeded"), messageName);
				sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBASTATUSBAR_MESSAGE, null, RdbaConnectionTabbedPanel.this, message));
			} catch (RdbaException e) {
				JOptionPane.showMessageDialog(RdbaConnectionTabbedPanel.this, e.getMessage());
				ExceptionHandler.error(e);
				// ステータスバーに表示。
				String message = String.format(MessageBundle.getInstance().getMessage("connecting_is_failed"), messageName);
				sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBASTATUSBAR_MESSAGE, null, RdbaConnectionTabbedPanel.this, message));
			}
		}
		
		public RdbaSingletonBundle getRdbaSingletonBundle() {
			return parentComponent.getRdbaSingletonBundle();
		}
	}
	
	class ChangeListenerImpl implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBAASSISTANTPANEL_UPDATEMENU, null, RdbaConnectionTabbedPanel.this, null));
		}
	}
	
	class CloseableTabbedPaneListenerEx implements CloseableTabbedPaneListener {
		public boolean closeTab(int tabIndexToClose) {
			Component comp = getComponentAt(tabIndexToClose);
			if (comp instanceof RdbaConnectionPanel) {
				((RdbaConnectionPanel)comp).dispose();
			}
			return true;
		}
	}
	
	public RdbaConnectionTabbedPanel(RdbaGuiInterface parentComponent) {
		this.parentComponent = parentComponent;
		
		// 新規タブを作成
		this.rdbaConnectionInfoListPanel = new RdbaConnectionInfoListPanelEx(this.parentComponent);
		this.rdbaConnectionInfoListPanel.putClientProperty("isClosable", new Boolean(false));
		this.add(MessageBundle.getInstance().getMessage("new_connection"), this.rdbaConnectionInfoListPanel);

		// タブ操作についての処理を追加
		this.addChangeListener(new ChangeListenerImpl());
		this.addCloseableTabbedPaneListener(new CloseableTabbedPaneListenerEx());
	}
	
	public void doGuiLayout() {
		for (int i=0;i<this.getComponentCount();i++) {
			Component comp = this.getComponentAt(i);
			if (comp instanceof RdbaGuiInterface) {
				((RdbaGuiInterface)comp).doGuiLayout();
			}
		}
	}
	
	public void reloadRdbaConfig() {
		for (int i=0;i<this.getComponentCount();i++) {
			Component comp = this.getComponentAt(i);
			if (comp instanceof RdbaGuiInterface) {
				((RdbaGuiInterface)comp).reloadRdbaConfig();
			}
		}
	}
	
	public RdbaConfig getRdbaConfig() {
		return this.parentComponent.getRdbaConfig();
	}

	public void sendRdbaMessage(RdbaMessage rdbaMessage) {
		this.parentComponent.sendRdbaMessage(rdbaMessage);
	}
	public void relayRdbaMessage(RdbaMessage rdbaMessage) {
		Component comp = this.getSelectedComponent();
		if (comp instanceof RdbaGuiInterface) {
			((RdbaGuiInterface)comp).relayRdbaMessage(rdbaMessage);
		}
	}
	public RdbaSingletonBundle getRdbaSingletonBundle() {
		return this.parentComponent.getRdbaSingletonBundle();
	}

	/** {@link RdbaModeInterface} */
	public JMenu[] getExtraMenu() {
		Component component = getSelectedComponent();
		if (component instanceof RdbaModeInterface) {
			return ((RdbaModeInterface)component).getExtraMenu();
		} else {
			return new JMenu[0];
		}
	}

	/** {@link RdbaModeInterface} */
	public boolean updateMenu(TargetMenu targetMenu, JMenu menu) {
		Component component = getSelectedComponent();
		if (component instanceof RdbaModeInterface) {
			return ((RdbaModeInterface)component).updateMenu(targetMenu, menu);
		} else {
			return false;
		}
	}
}
