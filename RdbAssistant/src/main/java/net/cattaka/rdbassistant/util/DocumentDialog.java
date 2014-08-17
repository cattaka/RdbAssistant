/*
 * Copyright (c) 2009-2014, Takao Sumitomo
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
 * $Id: DocumentDialog.java 232 2009-08-01 07:06:41Z cattaka $
 */

package net.cattaka.rdbassistant.util;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JTextArea;

import net.cattaka.swing.StdScrollPane;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.ResourceUtil;


/**
 * @author cattaka
 */
public class DocumentDialog extends JDialog {
	private static final long serialVersionUID = 9080140024027339670L;
	private StdScrollPane textPane;
	private JTextArea textArea;
	
	public DocumentDialog(Frame frame, String title, String document) {
		setTitle(title);
		textArea = new JTextArea();
		textPane = new StdScrollPane(textArea,
				StdScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				StdScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textArea.setEditable(false);
		getContentPane().add(textPane);
		
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader in = new BufferedReader(ResourceUtil.getResourceAsReader(document));
			String releaseNumber = MessageBundle.getInstance().getReleaseNumber();
			String tmp;
			while ((tmp = in.readLine()) != null) {
				tmp = tmp.replace("${release_number}", releaseNumber);
				sb.append(tmp + "\n");
			}
			textArea.setText(sb.toString());
			textArea.setCaretPosition(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}
}
