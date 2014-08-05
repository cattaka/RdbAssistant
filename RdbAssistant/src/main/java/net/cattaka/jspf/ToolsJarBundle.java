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
 * $Id: ToolsJarBundle.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.jspf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.List;
import net.cattaka.rdbassistant.RdbaConfigConstants;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.util.ExceptionHandler;

public class ToolsJarBundle {
	private Object javacMain;

	class MyURLClassLoader extends URLClassLoader {
		private List<Permission> extraPermission = new ArrayList<Permission>();
		public MyURLClassLoader(URL[] urls) {
			super(urls);
		}

		public MyURLClassLoader(URL[] urls, ClassLoader parent,
				URLStreamHandlerFactory factory) {
			super(urls, parent, factory);
		}

		public MyURLClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		public List<Permission> getExtraPermission() {
			return extraPermission;
		}

		@Override
		protected PermissionCollection getPermissions(CodeSource codesource) {
			PermissionCollection pc = super.getPermissions(codesource);
			for(Permission perm:extraPermission) {
				pc.add(perm);
			}
			return pc;
		}
	}
	
	public ToolsJarBundle() {
	}

	public void reloadTools(RdbaConfig rdbaConfig) {
		this.javacMain = null;
		
		String toolsFileName;
		if (rdbaConfig.isUseDefaultToolsJar()) {
			toolsFileName = getDefaultToolsJar();
		} else {
			toolsFileName = rdbaConfig.getProperty(RdbaConfigConstants.LIB_TOOLS);
		}
		if (toolsFileName == null || toolsFileName.length() == 0) {
			return;
		}
		
		// JarのURLを取得
		URL toolsUrl = null;
		try {
			toolsUrl = new URL(toolsFileName);
		} catch (MalformedURLException e) {
			// URLでない場合はファイルとして試みる
			File toolsFile = new File(toolsFileName);
			if (toolsFile.exists()) {
				try {
					toolsUrl = toolsFile.toURI().toURL();
				} catch (MalformedURLException e2) {
					// 面倒見切れん
				}
			}
		}
		if (toolsUrl == null) {
			// URLが見つからないので諦め
			return;
		}
		
		// Jarファイルをロード
		MyURLClassLoader urlClassLoader = new MyURLClassLoader(new URL[]{toolsUrl}, ToolsJarBundle.class.getClassLoader());
		urlClassLoader.getExtraPermission().add(new AllPermission());
		try {
			Class<?> javacMainClass = urlClassLoader.loadClass("com.sun.tools.javac.Main");
			this.javacMain = javacMainClass.newInstance();
		} catch (ClassNotFoundException e) {
			// コンパイルにミスってる
			ExceptionHandler.error(e);
		} catch (InstantiationException e) {
			// ありえない
			ExceptionHandler.fatal(e);
		} catch (IllegalAccessException e) {
			// ありえない
			ExceptionHandler.fatal(e);
		}
	}

	public boolean isAvailable() {
		return (this.javacMain != null);
	}
	
	public void compile(String[] args, PrintWriter printWriter) throws JspfException {
		if (this.javacMain == null) {
			throw new JspfException("Java compiler is not set.");
		}
		try {
			Method method = this.javacMain.getClass().getMethod("compile", String[].class, PrintWriter.class);
			method.invoke(null, args, printWriter);
		} catch (NoSuchMethodException e) {
			ExceptionHandler.fatal(e);
			throw new JspfException(e);
		} catch (InvocationTargetException e) {
			ExceptionHandler.fatal(e);
			throw new JspfException(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.fatal(e);
			throw new JspfException(e);
		}
	}
	
	public static String getDefaultToolsJar() {
		String result = null;
		String javaHome = System.getProperty("java.home");
		String toolsJarPath = javaHome + File.separatorChar + ".." + File.separatorChar + "lib" + File.separatorChar + "tools.jar"; 
		File file = new File(toolsJarPath);
		try {
			if (file.exists()) {
				result = file.getCanonicalPath();
			}
		} catch (IOException e) {
		}
		return result;
	}
}
