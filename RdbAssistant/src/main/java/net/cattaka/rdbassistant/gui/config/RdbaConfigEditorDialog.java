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
 * $Id: RdbaConfigEditorDialog.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.gui.config;

import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JDialog;

import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.util.MessageBundle;

public class RdbaConfigEditorDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private boolean approvalFlag = false;
	public RdbaConfigEditorPanel rdbaConfigEditorPanel;
	
	public RdbaConfigEditorDialog(Frame owner) throws HeadlessException {
		super(owner);
		this.setSize(600,400);
		this.setTitle(MessageBundle.getInstance().getMessage("config"));
		this.makeLayout();
	}
	
	private void makeLayout() {
		this.rdbaConfigEditorPanel = new RdbaConfigEditorPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void doApprove() {
				approvalFlag = true;
				RdbaConfigEditorDialog.this.setVisible(false);
			}
			@Override
			public void doCancel() {
				approvalFlag = false;
				RdbaConfigEditorDialog.this.setVisible(false);
			}
		};
		this.getContentPane().add(rdbaConfigEditorPanel);
	}
	
	public void loadRdbaConfig(RdbaConfig rdbaConfig) {
		rdbaConfigEditorPanel.loadRdbaConfig(rdbaConfig);
	}

	public void saveRdbaConfig(RdbaConfig rdbaConfig) {
		rdbaConfigEditorPanel.saveRdbaConfig(rdbaConfig);
	}

	public boolean showEditor() {
		this.approvalFlag = false;
		this.setModal(true);
		this.setVisible(true);
		return this.approvalFlag;
	}
}
