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
 * $Id: BigDecimalColumnConverter.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.rdbassistant.sql;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigDecimalColumnConverter implements ColumnConverter {
	private int integerDigits;
	private int fractionDigits;
	private NumberFormat numberFormat;
	
	public BigDecimalColumnConverter(int precision, int scale) {
		super();
		this.integerDigits = (precision > scale) ? precision - scale : 0;
		this.fractionDigits = (scale > 0) ? scale : 0;
		numberFormat = new DecimalFormat();
		numberFormat.setGroupingUsed(false);
		numberFormat.setMaximumIntegerDigits(integerDigits);
		numberFormat.setMaximumFractionDigits(fractionDigits);
//		numberFormat.setMinimumIntegerDigits(integerDigits);
		numberFormat.setMinimumFractionDigits(fractionDigits);
	}

	public Class<?> getInClass() {
		return BigDecimal.class;
	}

	public Class<?> getOutClass() {
//		return String.class;
		return BigDecimal.class;
	}
	
	public int getColumnMaxCharacters() {
		return integerDigits + fractionDigits;
	}

	public Object reverseConvert(Object arg) throws ConvertException {
		return arg;
	}

	public Object convert(Object arg) throws ConvertException {
		if (arg != null) {
			if (arg instanceof BigDecimal) {
				BigDecimal result = (BigDecimal)arg;
				try {
					result = result.setScale(this.fractionDigits);
				} catch (ArithmeticException e) {
					result.setScale(this.fractionDigits, BigDecimal.ROUND_HALF_UP);
				}
				return result;
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
