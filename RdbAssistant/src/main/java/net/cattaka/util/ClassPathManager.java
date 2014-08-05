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
 * $Id: ClassPathManager.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


public class ClassPathManager {
	private HashMap<String, String> classPathMap;
	private HashMap<String, URL> classPathUrlMap;
	
	public ClassPathManager() {
		this.classPathMap = new HashMap<String, String>();
		this.classPathUrlMap = new HashMap<String, URL>();
	}
	
	public String getClassPath(Class<?> targetClass) {
		String result = this.classPathMap.get(targetClass.getName());
		if (result == null) {
			result = prepareClassPath(targetClass);
			if (result != null) {
				this.classPathMap.put(targetClass.getName(), result);
			}
		}
		return result;
	}
	
	private String prepareClassPath(Class<?> targetClass) {
		String result = null;
		String dummyName = ".root";
		URL url = targetClass.getClassLoader().getResource(dummyName);
		if (url != null) {
			result = url.toString();
			
			if (result.startsWith("jar:") && result.endsWith("!/" + dummyName)) {
				result = result.substring(4, result.length() - (dummyName.length()+2));
			} else {
				result = result.substring(0, result.length() - (dummyName.length()+1));
			}
			if (result.startsWith("http://") || result.startsWith("https://")) {
				result = downloadJar(result);
			}
		}
		return result;
	}
	
	private String downloadJar(String urlString) {
		InputStream in = null;
		FileOutputStream fout = null;
		try {
			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			in = urlConnection.getInputStream();
			
			File tmpJarFile = File.createTempFile("rdbassistant", ".jar");
			fout = new FileOutputStream(tmpJarFile);
			int r;
			while((r=in.read()) != -1) {
				fout.write((char)r);
			}
			fout.close();
			in.close();
			
			return tmpJarFile.getAbsolutePath();
		} catch (IOException e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e2) {
				}
			}
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e2) {
				}
			}
			return null;
		}
	}

	public URL getClassPathUrl(Class<?> targetClass) {
		URL result = this.classPathUrlMap.get(targetClass.getName());
		if (result == null) {
			result = prepareClassPathUrl(targetClass);
			if (result != null) {
				this.classPathUrlMap.put(targetClass.getName(), result);
			}
		}
		return result;
	}
	
	private URL prepareClassPathUrl(Class<?> targetClass) {
		URL result = null;
		String dummyName = ".root";
		URL url = targetClass.getClassLoader().getResource(dummyName);
		if (url != null) {
			String str = url.toString();
			str = str.substring(0, str.length() - (dummyName.length()));
			try {
				result = new URL(str);
			} catch (IOException e) {
				ExceptionHandler.error(e);
			}
		}
		return result;
	}
}
