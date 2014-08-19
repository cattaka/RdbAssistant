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

package net.cattaka.swing.util;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.cattaka.util.ExceptionHandler;

public class HtmlSelection implements Transferable, ClipboardOwner {
	private static List<DataFlavor> htmlFlavors = new ArrayList<DataFlavor>();
	static {
		try {
			htmlFlavors.add(new DataFlavor("text/html;class=java.lang.String"));
			htmlFlavors.add(new DataFlavor("text/html;class=java.io.Reader"));
			htmlFlavors.add(new DataFlavor(
					"text/html;charset=unicode;class=java.io.InputStream"));
		} catch (ClassNotFoundException e) {
			ExceptionHandler.error(e);
		}
	}

	private String htmlString;

	public HtmlSelection(String htmlString) {
		this.htmlString = htmlString;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return htmlFlavors.toArray(new DataFlavor[htmlFlavors
				.size()]);

	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return htmlFlavors.contains(flavor);
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException {
		if (String.class.equals(flavor.getRepresentationClass())) {
			return htmlString;
		} else if (Reader.class.isAssignableFrom(flavor.getRepresentationClass())) {
			return new StringReader(htmlString);
		} else if (InputStream.class.isAssignableFrom(flavor.getRepresentationClass())) {
			return new StringReader(htmlString);
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}

	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
}