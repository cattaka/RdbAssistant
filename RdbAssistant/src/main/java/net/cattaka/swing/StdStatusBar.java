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
 * $Id: StdStatusBar.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import net.cattaka.swing.util.ButtonsBundle;

public class StdStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel messageLabel;
	private JProgressBar memoryBar;
	private Timer timer;

	public StdStatusBar(Icon gcIcon) {
		makeLayout(gcIcon);
	}
	private void makeLayout(Icon gcIcon) {
		JButton gcButton = new JButton();
		ButtonsBundle.getInstance().applyButtonDifinition(gcButton, gcIcon, "exec_gc", true);
		
		this.messageLabel = new JLabel();
		this.memoryBar = new JProgressBar(0,1);
		this.memoryBar.setStringPainted(true);
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx=0;
		gbc.gridy=0;
		gbl.setConstraints(this.messageLabel, gbc);
		gbc.weightx = 0;
		gbc.gridx++;
		gbl.setConstraints(this.memoryBar, gbc);
		gbc.gridx++;
		gbl.setConstraints(gcButton, gbc);
		
		this.setLayout(gbl);
		this.add(this.messageLabel);
		this.add(this.memoryBar);
		this.add(gcButton);
		
		// memory残量の初期表示
		updateMemoryBar();

		// GCボタンにアクションを設定
		gcButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.gc();
			}
		});
		// タイマーの作成
		this.timer = new Timer(500, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				updateMemoryBar();
			}
		});
	}
	private void updateMemoryBar() {
		Runtime runtime = Runtime.getRuntime();
		int maxMemory;
		int totalMemory;

		// メガバイト単位に直す
		maxMemory = (int)(runtime.maxMemory() / (2<<10));
		totalMemory = (int)((runtime.totalMemory() - runtime.freeMemory()) / (2<<10));
		
		this.memoryBar.setMaximum((int)maxMemory);
		this.memoryBar.setValue((int)totalMemory);
		
		String caption = String.format("%dKB / %dKB", totalMemory, maxMemory);
		this.memoryBar.setString(caption);
	}
	public void startMemoryUpdate() {
		timer.start();
	}
	public void stopMemoryUpdate() {
		timer.stop();
	}
	public void setMessage(String message) {
		this.messageLabel.setText(message);
	}
}
