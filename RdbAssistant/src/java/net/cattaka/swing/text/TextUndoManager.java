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
 * $Id: TextUndoManager.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.text;

import javax.swing.text.AbstractDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class TextUndoManager extends UndoManager {
	private static final long serialVersionUID = 1L;
	
	@Override
	public synchronized void undo() throws CannotUndoException {
		if (!canUndo())
			return;
		AbstractDocument.DefaultDocumentEvent lastUndo = null;
		AbstractDocument.DefaultDocumentEvent nextUndo = null;
		UndoableEdit ue;
		while((ue = super.editToBeUndone()) != null) {
			if (!canUndo()) {
				break;
			}
			if (ue instanceof AbstractDocument.DefaultDocumentEvent) {
				nextUndo = (AbstractDocument.DefaultDocumentEvent)ue;
				if (lastUndo == null || lastUndo.getPresentationName().equals(nextUndo.getPresentationName())) {
					boolean breakFlag = false;
					if (lastUndo != null) {
						breakFlag = (Math.abs(nextUndo.getOffset() - lastUndo.getOffset()) > 1);
					}
					if (breakFlag) {
						break;
					} else {
						super.undo();
						lastUndo = nextUndo;
					}
				} else {
					break;
				}
			} else {
				// あり得ない
				throw new RuntimeException();
				//super.undo();
				//break;
			}
		}
	}
	@Override
	public synchronized void redo() throws CannotRedoException {
		if (!canRedo())
			return;
		
		AbstractDocument.DefaultDocumentEvent lastRedo = null;
		AbstractDocument.DefaultDocumentEvent nextRedo;
		UndoableEdit ue;
		while((ue = super.editToBeRedone()) != null) {
			if (!canRedo()) {
				break;
			}
			if (ue instanceof AbstractDocument.DefaultDocumentEvent) {
				nextRedo = (AbstractDocument.DefaultDocumentEvent)ue;
				if (lastRedo == null || lastRedo.getPresentationName().equals(nextRedo.getPresentationName())) {
					boolean breakFlag = false;
					if (lastRedo != null) {
						breakFlag = (Math.abs(nextRedo.getOffset() - lastRedo.getOffset()) > 1);
					}
					super.redo();
					lastRedo = nextRedo;
					if (breakFlag) {
						break;
					}
				} else {
					break;
				}
			} else {
				// あり得ない
				throw new RuntimeException();
				//super.redo();
				//break;
			}
		}
	}
	
}
