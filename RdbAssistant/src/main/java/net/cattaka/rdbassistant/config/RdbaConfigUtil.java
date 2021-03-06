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
 * $Id: RdbaConfigUtil.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.cattaka.rdbassistant.RdbaConstants;
import net.cattaka.rdbassistant.driver.sqlite.SqliteRdbaConnectionInfo;
import net.cattaka.util.ExceptionHandler;

public class RdbaConfigUtil {
	public static RdbaConfig loadRdbaConfig() {
		RdbaConfig result = new RdbaConfig();
		try {
			InputStream in = new FileInputStream(RdbaConstants.RDBA_CONFIG_FILE);
			result.loadFromXML(in);
			in.close();
		} catch(FileNotFoundException e) {
			result.updateArtificialInfo();
			{	// add tutorial
				SqliteRdbaConnectionInfo info = new SqliteRdbaConnectionInfo();
				info.setLabel("Tutorial");
				info.setDatabase("tutorial/tutorial.db");
				result.getRdbaConnectionInfo().add(info);
			}
		} catch(IOException e) {
			ExceptionHandler.error(e);
			result.updateArtificialInfo();
		}
		
		return result;
	}
	public static void saveRdbaConfig(RdbaConfig rdbaConfig) {
		try {
			OutputStream out = new FileOutputStream(RdbaConstants.RDBA_CONFIG_FILE);
			rdbaConfig.storeToXML(out, "");
			out.flush();
		} catch(IOException e) {
			ExceptionHandler.error(e);
		}
	}
}
