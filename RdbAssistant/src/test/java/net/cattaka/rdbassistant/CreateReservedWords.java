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
 * $Id: CreateReservedWords.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.junit.Test;

import net.cattaka.util.ExceptionHandler;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CreateReservedWords {
	@Test
	public void testParse() throws Exception {
		InputStream in = null;
		XMLDecoder dec = null;
		try {
			in = getClass().getResourceAsStream("/ReservedWords.test.properties");
			dec = new XMLDecoder(in);
			@SuppressWarnings("unchecked")
			HashSet<String> words = (HashSet<String>)dec.readObject();
			assertThat(words, hasItem("NOT"));
			assertThat(words, hasItem("FOREIGN"));
			assertThat(words, hasItem("SEPARATOR"));
			assertThat(words, hasItem("MOD"));
			assertThat(words, hasItem("PRECISION"));
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					ExceptionHandler.warn(e);
				}
			}
			if (dec != null) {
				dec.close();
			}
		}
	}
}
