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
 * $Id: RdbAssistant.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.config.RdbaConfigUtil;
import net.cattaka.rdbassistant.gui.RdbAssistantPanel;
import net.cattaka.util.MessageBundle;

public class RdbAssistant {
	static class WindowAdapterEx extends WindowAdapter {
		RdbAssistantPanel rdbAssistantPanel;
		public WindowAdapterEx(RdbAssistantPanel rdbAssistantPanel) {
			this.rdbAssistantPanel = rdbAssistantPanel;
		}
		
		public void windowClosing(WindowEvent e) {
			this.rdbAssistantPanel.doExit();
		}
	}
	
	static class RdbAssistantFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		private RdbAssistantPanel rdbAssistantPanel;
		
		public RdbAssistantFrame(RdbaConfig rdbaConfig) {
			this.rdbAssistantPanel = new RdbAssistantPanel(rdbaConfig) {
				private static final long serialVersionUID = 1L;
				@Override
				public void doExit() {
					RdbAssistantFrame.this.doExit();
				}
			};
			
			String title = String.format("%s %s - build %s -", MessageBundle.getMessage("rdbassistant_title"), MessageBundle.getReleaseNumber(), MessageBundle.getBuildNumber());
			this.setTitle(title);
			this.setSize(800,600);
			this.getContentPane().add(rdbAssistantPanel);
			this.setJMenuBar(rdbAssistantPanel.getMenuBar());
			this.addWindowListener(new WindowAdapterEx(rdbAssistantPanel));

			rdbAssistantPanel.doGuiLayout();
			rdbAssistantPanel.createWindowRelatedObject();
		}
		public void doExit() {
			this.rdbAssistantPanel.saveRdbaConfig();
			this.setVisible(false);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		RdbaConfig rdbaConfig = RdbaConfigUtil.loadRdbaConfig();
		RdbAssistantFrame f = new RdbAssistantFrame(rdbaConfig);
		f.setVisible(true);
	}
}
