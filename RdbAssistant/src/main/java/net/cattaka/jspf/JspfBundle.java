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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import net.cattaka.util.FileUtil;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.StringUtil;

public class JspfBundle<T> {
	private int javaSourceCount = 0;
	private File workDir;
	private JspfJavaSourceConverter<T> jspfJavaSourceConverter;
	private HashMap<String, JspfEntry<T>> jspfEntryMap;
	private URLClassLoader urlClassLoader;
	Class<?> targetClass;
//	private ClassPathManager classPathManager;
	
	public JspfBundle(JspfJavaSourceConverter<T> jspfJavaSourceConverter, Class<T> targetClass) {
		this.jspfJavaSourceConverter = jspfJavaSourceConverter;
		this.jspfEntryMap = new HashMap<String, JspfEntry<T>>();
		this.targetClass = targetClass;
//		this.classPathManager = new ClassPathManager();
	}
	
	public File getJavaSource(String labelName) {
		JspfEntry<T> jspfEntry = jspfEntryMap.get(labelName);
		if (jspfEntry != null) {
			return jspfEntry.getJavaSourceFile();
		} else {
			// TODO
			return null;
		}
	}
	public Class<T> getJspfClass(String labelName) {
		JspfEntry<T> jspfEntry = jspfEntryMap.get(labelName);
		if (jspfEntry != null) {
			return jspfEntry.getJspfClass();
		} else {
			// TODO
			return null;
		}
	}
	public T getNewInstance(String labelName) {
		T result = null;
		Class<T> jspfClass = getJspfClass(labelName);
		
		if (jspfClass != null) {
			try {
				result = jspfClass.newInstance();
			} catch (IllegalAccessException e) {
				ExceptionHandler.fatal(e);
			} catch (InstantiationException e) {
				ExceptionHandler.fatal(e);
			}
		}
		
		return result;
	}
	
	public boolean isNeedUpdate(String labelName, String sourceText) {
		boolean needUpdate = true;
		JspfEntry<T> jspfEntry = this.jspfEntryMap.get(labelName);
		
		if (jspfEntry != null) {
			needUpdate = !(sourceText.equals(jspfEntry.getSourceText())) || jspfEntry.getJspfClass() == null;
		}
		return needUpdate;
	}
	
	@SuppressWarnings("unchecked")
	public void compile(ToolsJarBundle toolsJarBundle, String labelName, String sourceText, PrintWriter out) throws JspfException {
		sourceText = StringUtil.removeCarriageReturn(sourceText);
		
		// コンパイル実行
		JspfEntry<T> jspfEntry = this.jspfEntryMap.get(labelName);
		if (jspfEntry != null) {
			jspfEntry.setSourceText(sourceText);
		} else {
			jspfEntry = new JspfEntry<T>();
			jspfEntry.setSourceText(sourceText);
			this.jspfEntryMap.put(labelName, jspfEntry);
		}
		
		String className = getNewClassName();
		File javaSourceFile = getJavaSourceFile(className);
		jspfEntry.setClassName(className);
		jspfEntry.setJavaSourceFile(javaSourceFile);
		jspfEntry.setJspfClass(null);
		
		// 変換する(失敗したら勝手にExceptionが飛ぶ)
		this.jspfJavaSourceConverter.convertSourceFile(this,jspfEntry);
		
		// コンパイル実行
		try {
			String[] args;
			{
				args = new String[]{
						"-encoding", "UTF-8",
						"-sourcepath", getJavaSourceDir().getAbsolutePath(),
						"-d", getClassDir().getAbsolutePath(),
						jspfEntry.getJavaSourceFile().getAbsolutePath()
					};
			}
			toolsJarBundle.compile(args , out);
		} catch (Exception e) {
			throw new JspfException(e);
		}
		
		try {
			Class<T> jspfClass = (Class<T>)this.urlClassLoader.loadClass(className);
			jspfEntry.setJspfClass(jspfClass);
		} catch(ClassNotFoundException e) {
			throw new JspfException(e);
		} catch (ClassCastException e) {
			throw new JspfException(e);
		}
	}
	
	public boolean isAvailable() {
		return (this.urlClassLoader != null);
	}
	
	public File getWorkDir() {
		return workDir;
	}

	public boolean setWorkDir(File workDir) {
		boolean result = false;
		if (workDir == null || !workDir.isDirectory()) {
			this.jspfEntryMap.clear();
			this.urlClassLoader = null;
			this.workDir = null;
			result = false;
		} else {
			// 変更された場合は一回全てクリア
			if (this.isAvailable() && !(this.workDir.getAbsolutePath().equals(workDir.getAbsolutePath()))) {
				this.workDir = null;
				this.jspfEntryMap.clear();
				this.urlClassLoader = null;
			}
			// 設定する
			try {
				this.workDir = workDir;
				File classDir = getClassDir();
				FileUtil.deleteFile(classDir);
				classDir.mkdir();
				this.urlClassLoader = new URLClassLoader(new URL[]{classDir.toURI().toURL()},JspfBundle.class.getClassLoader());
				result = true;
			} catch (JspfException e) {
				// 失敗したら未設定状態に戻す
				this.workDir = null;
				this.jspfEntryMap.clear();
				this.urlClassLoader = null;
				result = false;
			} catch (MalformedURLException e) {
				// 失敗したら未設定状態に戻す
				this.workDir = null;
				this.jspfEntryMap.clear();
				this.urlClassLoader = null;
				result = false;
			}
		}
		return result;
	}

	private File getJavaSourceDir() throws JspfException {
		if (this.workDir != null && workDir.isDirectory()) {
			File dir = new File(this.workDir.getAbsoluteFile() + File.separator + "java");
			if (dir != null) {
				dir.mkdir();
			}
			return dir;
		} else {
			throw new JspfException("work directory is invalid.");
		}
	}
	private File getClassDir() throws JspfException {
		if (this.workDir != null && workDir.isDirectory()) {
			File dir = new File(this.workDir.getAbsoluteFile() + File.separator + "class");
			if (dir != null) {
				dir.mkdir();
			}
			return dir;
		} else {
			throw new JspfException("work directory is invalid.");
		}
	}
		
	private File getJavaSourceFile(String className) throws JspfException {
		if (this.workDir != null && workDir.isDirectory()) {
			return new File(getJavaSourceDir() + File.separator + className + ".java");
		} else {
			throw new JspfException("work directory is invalid.");
		}
	}

	private String getNewClassName() {
		return String.format("JspfTmp%06d", javaSourceCount++);
	}
}
