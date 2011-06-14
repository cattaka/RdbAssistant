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
 * $Id: DocumentUtil.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import net.cattaka.util.ExceptionHandler;

public class DocumentUtil {
	private static ArrayList<int[]> cache = new ArrayList<int[]>();
	public static int[][] getWordIndexs(Document document, int offset, int length, int margin) {
		cache.clear();
		
		int start = offset - margin;
		int end = offset+length + margin;

		if (start < 0) {
			start = 0;
		}
		if (end >= document.getLength()) {
			end = document.getLength();
		}
		try {
			String tmpStr = document.getText(start, end-start);
			Pattern pattern = Pattern.compile("[^\\s]+");
			Matcher mr = pattern.matcher(tmpStr);
			while(mr.find()) {
				int[] t = new int[]{start + mr.start(),start + mr.end()};
				cache.add(t);
			}
			if (start > 0) {
				cache.remove(0);
			}
			if (end < document.getLength()) {
				cache.remove(cache.size()-1);
			}
		} catch (BadLocationException e) {
			ExceptionHandler.error(e);
		}
		
		return cache.toArray(new int[cache.size()][]);
	}
}
