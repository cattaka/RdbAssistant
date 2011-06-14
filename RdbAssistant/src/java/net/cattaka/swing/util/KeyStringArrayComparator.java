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
 * $Id: KeyStringArrayComparator.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.util;

import java.util.Comparator;

public class KeyStringArrayComparator implements Comparator<String[]> {
	int[] indicesOfKeys;
	
	public KeyStringArrayComparator(int[] indicesOfKeys) {
		this.indicesOfKeys = new int[indicesOfKeys.length];
		System.arraycopy(indicesOfKeys, 0, this.indicesOfKeys, 0, this.indicesOfKeys.length);
	}
	
	public int compare(String[] ssc1, String[] ssc2) {
		for (int idx:indicesOfKeys) {
			String str1 = ssc1[idx];
			String str2 = ssc2[idx];
			if (str1 == null && str2 == null) {
				continue;
			} else if (str1 != null && str2 == null) {
				return -1;
			} else if (str1 == null && str2 != null) {
				return 1;
			} else {
				int r = str1.compareTo(str2);
				if (r == 0) {
					continue;
				} else {
					return r;
				}
			}
		}
		return 0;
	}

}
