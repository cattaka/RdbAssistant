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
 * $Id: DummyRdbaConnectionInfo.java 271 2010-03-02 13:40:36Z cattaka $
 */
package net.cattaka.rdbassistant.driver.dummy;

import net.cattaka.rdbassistant.config.RdbaConnectionInfo;
import net.cattaka.rdbassistant.config.RdbaJdbcBundle;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.gui.connectioninfo.RdbaConnectionInfoEditor;
import net.cattaka.util.ExceptionHandler;

public class DummyRdbaConnectionInfo implements RdbaConnectionInfo {
	private static final long serialVersionUID = 1L;
	
	private String label = "";
	
	public String getRdbmsName() {
		return "Dummy";
	}
	
	public String toUrl() {
		return "dummy";
	}
	
	public RdbaConnection createConnection(RdbaJdbcBundle rdbaJdbcBundle) throws RdbaException {
		DummyRdbaConnection rdbaConnection = new DummyRdbaConnection(label);
		
		return rdbaConnection;
	}

	public String[] getDisplayStrings() {
		return new String[]{"Dummy",label, "", ""};
	}

	public String getTooltipText() {
		return label;
	}

	public RdbaConnectionInfoEditor createEditor() {
		return new DummyRdbaConnectionInfoEditor();
	}

	public String[] toStringArray() {
		String[] result = {
			getClass().getCanonicalName(),
			"1",
			label
		};
		return result;
	}
	
	public boolean restoreStringArray(String[] stringArray, int configRevision) {
		boolean result = false;
		if (stringArray[1].equals("1") && stringArray.length == 3) {
			label = stringArray[2];
			result = true;
		}
		return result;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public DummyRdbaConnectionInfo createClone() {
		try {
			return (DummyRdbaConnectionInfo)this.clone();
		} catch (CloneNotSupportedException e) {
			ExceptionHandler.error(e);
			return new DummyRdbaConnectionInfo();
		}
	}
}
