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
 * $Id: FileUtil.java 243 2009-10-21 15:32:54Z cattaka $
 */
package net.cattaka.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileUtil {
	public static class FileComparator implements Comparator<File> {

		public int compare(File o1, File o2) {
			if (o1 == null && o2 == null) {
				return 0;
			} else if (o1 == null && o2 != null) {
				return -1;
			} else if (o1 != null && o2 == null) {
				return 1;
			} else {
				return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
			}
		}
	}
	
	/**
	 * ディレクトリごと消すためのメソッド
	 */
	public static boolean deleteFile(File target) {
		boolean result = false;
		if (target.delete()) {
			result = true;
		} else {
			if (target.isDirectory()) {
				boolean goFlag = true;
				// ディレクトリの要素を削除する
				File[] childFiles = target.listFiles();
				for (File file:childFiles) {
					if (!deleteFile(file)) {
						goFlag = false;
					}
				}
				// 全ての子要素が削除できたなら削除する。
				if (goFlag) {
					result = target.delete();
				}
			}
		}
		return result;
	}
	
	public static List<String> getAllFileNameList(File dir, int limit, String ext) {
		List<String> result = new ArrayList<String>();
		List<File> fileList = getAllFileList(dir, limit);
		Collections.sort(fileList, new FileComparator());
		
		String path = dir.getAbsolutePath();
		for (File file:fileList) {
			String filePath = file.getAbsolutePath();
			if (filePath.toUpperCase().endsWith(ext.toUpperCase())) {
				if (filePath.length() > path.length()) {
					filePath = filePath.substring(path.length()+1);
				}
				result.add(filePath);
			}
		}

		return result;
	}
	
	public static List<File> getAllFileList(File dir, int limit) {
		ArrayList<File> result = new ArrayList<File>();
		getAllFileList(result, dir, limit);
		return result;
	}
	
	public static String cutSubPath(File baseDir, File file) {
		String path = baseDir.getAbsolutePath();
		String result = file.getAbsolutePath();
		if (result.length() > path.length()) {
			result = result.substring(path.length()+1);
		}
		return result;
	}
	
	private static void getAllFileList(List<File> result, File dir, int limit) {
		if (limit == 0) {
			return;
		}
		
		File[] childList = dir.listFiles();
		for (File file:childList) {
//			if (file.getAbsoluteFile().equals(dir.getAbsoluteFile())) {
//				// ./.を避けるため
//				continue;
//			}
			if (file.isFile()) {
				result.add(file);
			} else if (file.isDirectory()) {
				getAllFileList(result, file, limit-1);
			}
		}
	}
}
