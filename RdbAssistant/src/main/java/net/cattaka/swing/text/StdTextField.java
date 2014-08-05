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
 * $Id: StdTextField.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.text;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import net.cattaka.swing.JPopupMenuForStandardText;

public class StdTextField extends JTextField implements StdTextComponent {
	private static final long serialVersionUID = 1L;

	private JPopupMenuForStandardText popupMenu;
	private UndoManager undoManager;

	public StdTextField() {
		super();
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}
	
	public StdTextField(JPopupMenuForStandardText popupMenu) {
		super();
		this.popupMenu = popupMenu;
		initialize();
	}
	
	public StdTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public StdTextField(int columns) {
		super(columns);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public StdTextField(String text, int columns) {
		super(text, columns);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public StdTextField(String text) {
		super(text);
		this.popupMenu = new JPopupMenuForStandardText(true);
		initialize();
	}

	public void initialize() {
		this.undoManager = new TextUndoManager();
		
		this.popupMenu.install(this);
		
		this.getDocument().addUndoableEditListener(undoManager);
		this.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_Z:	//CTRL+Zのとき、UNDO実行
					if (e.isControlDown() && undoManager.canUndo()) {
						undoManager.undo();
						e.consume();
					}
					break;
				case KeyEvent.VK_Y:	//CTRL+Yのとき、REDO実行
					if (e.isControlDown() && undoManager.canRedo()) {
						undoManager.redo();
						e.consume();
					}
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});
	}
	
	public boolean canUndo() {
		return undoManager.canUndo();
	}

	public boolean canRedo() {
		return undoManager.canRedo();
	}
	
	public void undo() {
		undoManager.undo();
	}
	
	public void redo() {
		undoManager.redo();
	}
	
	public JPopupMenuForStandardText getPopupMenu() {
		return popupMenu;
	}
	
	public JComponent getJComponent() {
		return this;
	}
}
