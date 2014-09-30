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
 * $Id: StdTextPaneTest.java 259 2010-02-27 13:45:56Z cattaka $
 */
package net.cattaka.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.junit.Assert;
import org.junit.Test;

import net.cattaka.rdbassistant.driver.oracle.OracleStyledDocument;
import net.cattaka.swing.text.FindCondition;
import net.cattaka.swing.text.FindConditionDialog;
import net.cattaka.swing.text.StdStyledDocument;
import net.cattaka.swing.text.StdTextPane;
import net.cattaka.swing.text.TextLineInfo;
import static org.hamcrest.CoreMatchers.*;

public class StdTextPaneTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private StdTextPane stdTextPane;
	private FindConditionDialog findConditionDialog;
	
	@Test
	public void test() {
		StdTextPane stdTextPane = new StdTextPane();
		stdTextPane.setStdStyledDocument(new OracleStyledDocument());
		stdTextPane.setText("ab ab ab ab\nac ac ac ac\n");
		StdStyledDocument ssd = stdTextPane.getStdStyledDocument();
		{
			TextLineInfo tli = ssd.getLine(0);
			Assert.assertThat(ssd.getPrevLine(tli), is(nullValue()));
			Assert.assertThat(tli, is(new TextLineInfo(0, 11, "ab ab ab ab")));
			Assert.assertThat(ssd.getNextLine(tli), is(new TextLineInfo(12, 23, "ac ac ac ac")));
		}
		{
			TextLineInfo tli = ssd.getLine(12);
			Assert.assertThat(ssd.getPrevLine(tli), is(new TextLineInfo(0, 11, "ab ab ab ab")));
			Assert.assertThat(tli, is(new TextLineInfo(12, 23, "ac ac ac ac")));
			Assert.assertThat(ssd.getNextLine(tli), is(new TextLineInfo(24, 24, "")));
		}
	}

	public StdTextPaneTest() {
		setSize(800,600);
		stdTextPane = new StdTextPane();
		stdTextPane.setStdStyledDocument(new OracleStyledDocument());
		findConditionDialog = new FindConditionDialog(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void doAction(FindCondition findCondition) {
				super.doAction(findCondition);
				stdTextPane.doFindAction(findCondition);
			}
		};
		findConditionDialog.setModal(false);
		
		stdTextPane.setText("ab ab ab ab\nab ab ab ab\n");
		
		getContentPane().add(stdTextPane);
		
		/*
		 * StdStyledDocument.getLineのテスト
		 */
		stdTextPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F3) {
					int pos = stdTextPane.getCaretPosition();
					StdStyledDocument ssd = stdTextPane.getStdStyledDocument();
					TextLineInfo tli = ssd.getLine(pos);
					System.out.println("P:"+ssd.getPrevLine(tli));
					System.out.println("C:"+tli);
					System.out.println("N:"+ssd.getNextLine(tli));
				}
			}
		});
		
	}
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		//findConditionDialog.setVisible(b);
	}
	
	public static void main(String[] args) {
		StdTextPaneTest f = new StdTextPaneTest();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
