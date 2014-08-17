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
 * $Id: RdbaJdbcBundle.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.config;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PropertyPermission;

import net.cattaka.rdbassistant.RdbaConfigConstants;
import net.cattaka.rdbassistant.core.RdbaException;
import net.cattaka.util.ExceptionHandler;
import net.cattaka.util.MessageBundle;

public class RdbaJdbcBundle {
	private HashMap<String, MyURLClassLoader> classLoaderMap = new HashMap<String, MyURLClassLoader>(); 

	public static class MyURLClassLoader extends URLClassLoader {
		private List<Permission> extraPermission = new ArrayList<Permission>();
		public MyURLClassLoader(URL[] urls) {
			super(urls);
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
		
		@Override
		public void close() {
			try {
				super.close();
			} catch (IOException e) {
				ExceptionHandler.warn(e);
			}
		}
	}
	
	public void reloadJdbc(RdbaConfig rdbaConfig) {
		this.classLoaderMap.clear();
		
		HashSet<String> targets = new HashSet<String>();
		targets.add(RdbaConfigConstants.DRIVER_JDBC_MYSQL);
		targets.add(RdbaConfigConstants.DRIVER_JDBC_ORACLE);
		targets.add(RdbaConfigConstants.DRIVER_JDBC_SQLITE);
		
		for (String key : targets) {
			String configValue = rdbaConfig.getProperty(key);
			if (configValue == null || configValue.length() == 0) {
				continue;
			}
			
			// JDBCのJarのURLを取得
			URL jdbcUrl = null;
			try {
				jdbcUrl = new URL(configValue);
			} catch (MalformedURLException e) {
				// URLでない場合はファイルとして試みる
				File jdbcFile = new File(configValue);
				if (jdbcFile.exists()) {
					try {
						jdbcUrl = jdbcFile.toURI().toURL();
					} catch (MalformedURLException e2) {
						// 面倒見切れん
					}
				}
			}
			if (jdbcUrl == null) {
				// URLが見つからないので諦め
				continue;
			}
			
			// Jarファイルをロード
			MyURLClassLoader urlClassLoader = new MyURLClassLoader(new URL[]{jdbcUrl});
			urlClassLoader.getExtraPermission().add(new SocketPermission("*", "connect,resolve"));
			this.classLoaderMap.put(key, urlClassLoader);
		}
		
		// Oracle固有
		{
			MyURLClassLoader urlClassLoader = this.classLoaderMap.get(RdbaConfigConstants.DRIVER_JDBC_ORACLE);
			if (urlClassLoader != null) {
				urlClassLoader.getExtraPermission().add(new PropertyPermission("oracle.*", "read"));
				urlClassLoader.getExtraPermission().add(new PropertyPermission("user.name", "read"));
			}
		}
	}
	
	public Driver getDriver(String key, String className) throws RdbaException {
		URLClassLoader urlClassLoader = classLoaderMap.get(key);
		if (urlClassLoader == null) {
			throw new RdbaException(MessageBundle.getInstance().getMessage("jdbc_not_configured"));
		}
		Class<?> driverClass = null;
		try {
			driverClass = urlClassLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new RdbaException(MessageBundle.getInstance().getMessage("jdbc_class_not_found"));
		}
		Driver driver = null;
		try {
			driver = (Driver)driverClass.newInstance();
		} catch (ClassCastException e) {
			throw new RdbaException(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new RdbaException(e.getMessage(),e);
		} catch (InstantiationException e) {
			throw new RdbaException(e.getMessage(),e);
		}
		
		return driver;
	}
}
