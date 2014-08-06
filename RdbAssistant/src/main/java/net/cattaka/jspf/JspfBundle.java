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
 * $Id: JspfBundle.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.jspf;

import java.io.File;
import java.io.PrintWriter;

import net.cattaka.util.StringUtil;

public class JspfBundle<T> {
	private File workDir;
	private JspfJavaSourceConverter<T> jspfJavaSourceConverter;
	Class<?> targetClass;

	// private ClassPathManager classPathManager;

	public JspfBundle(JspfJavaSourceConverter<T> jspfJavaSourceConverter,
			Class<T> targetClass) {
		this.jspfJavaSourceConverter = jspfJavaSourceConverter;
		this.targetClass = targetClass;
		// this.classPathManager = new ClassPathManager();
	}

	public JspfEntry<T> compile(String labelName, String sourceText,
			PrintWriter out) throws JspfException {
		sourceText = StringUtil.removeCarriageReturn(sourceText);

		// コンパイル実行
		JspfEntry<T> jspfEntry = new JspfEntry<T>();
		jspfEntry.setSourceText(sourceText);

		// 変換する(失敗したら勝手にExceptionが飛ぶ)
		this.jspfJavaSourceConverter.convertSourceFile(this, jspfEntry);

		return jspfEntry;
	}

	public File getWorkDir() {
		return workDir;
	}

	public boolean setWorkDir(File workDir) {
		boolean result = false;
		if (workDir == null || !workDir.isDirectory()) {
			this.workDir = null;
			result = false;
		} else {
			// 設定する
			this.workDir = workDir;
			result = true;
		}
		return result;
	}
}
