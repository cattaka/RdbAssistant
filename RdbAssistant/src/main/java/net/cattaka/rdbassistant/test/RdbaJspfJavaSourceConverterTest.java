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
 * $Id: RdbaJspfJavaSourceConverterTest.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.test;

import java.io.File;
import net.cattaka.jspf.JspfBundle;
import net.cattaka.rdbassistant.script.RdbaScriptJavaSourceConverter;
import net.cattaka.rdbassistant.script.core.RdbaScript;

public class RdbaJspfJavaSourceConverterTest {
	public static void main(String[] args) throws Exception {
		convertTest();
	}
	
	public static void convertTest() throws Exception {
		File workDir = new File("RdbaScriptWork");
		RdbaScriptJavaSourceConverter rjjsc = new RdbaScriptJavaSourceConverter();
		JspfBundle<RdbaScript> jspfBundle = new JspfBundle<RdbaScript>(rjjsc, RdbaScript.class);
		jspfBundle.setWorkDir(workDir);
		
//		StringWriter stringWriter = new StringWriter();
//		PrintWriterEx printWriter = new PrintWriterEx(stringWriter);
//		jspfBundle.compile("Test.jspf", "System.out.println(\"test\");", printWriter);
//		printWriter.flush();
//		System.out.println(stringWriter.toString());
//		
//		RdbaScript rs = jspfBundle.getNewInstance("Test.jspf");
//		rs.runScript(null, null);
	}
}