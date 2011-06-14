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
 * $Id: AbstractDateColumnConverter.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.sql;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractDateColumnConverter implements ColumnConverter {
	private String pattern;
	private DateFormat dateFormat;
	
	public AbstractDateColumnConverter(String pattern) {
		super();
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
	}

	public Class<?> getInClass() {
		return Date.class;
	}

	public Class<?> getOutClass() {
		return String.class;
	}
	
	public int getColumnMaxCharacters() {
		return this.pattern.length();
	}

	public Object reverseConvert(Object arg) throws ConvertException {
		try {
			if (arg instanceof String) {
				return dateFormat.parse((String)arg);
			} else {
				throw new ConvertException();
			}
		} catch (ParseException e) {
			throw new ConvertException(e);
		}
	}

	public Object convert(Object arg) throws ConvertException {
		if (arg != null) {
			if (arg instanceof Date) {
				return dateFormat.format(arg);
			} else {
				throw new ConvertException();
			}
		} else {
			return null;
		}
	}

	public boolean isAvalConvert() {
		return true;
	}
	
	public boolean isAvalReverseConvert() {
		return true;
	}
}
