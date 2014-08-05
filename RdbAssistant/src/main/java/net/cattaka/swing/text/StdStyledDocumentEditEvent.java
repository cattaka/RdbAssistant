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
 * $Id: StdStyledDocumentEditEvent.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.text;

public class StdStyledDocumentEditEvent {
	private int offset;
	private String before;
	private String after;
	private EVENT_TYPE eventType;
	
	public enum EVENT_TYPE {
		INSERT,
		REMOVE,
		REPLACE,
		SEPALATOR
	};
	
	public StdStyledDocumentEditEvent() {
		this.eventType = EVENT_TYPE.SEPALATOR;
	}
	
	public StdStyledDocumentEditEvent(int offset, String before, String after) {
		super();
		this.offset = offset;
		this.before = before;
		this.after = after;
		
		if (before.length() == 0) {
			if (after.length() == 0) {
				this.eventType = EVENT_TYPE.SEPALATOR;
			} else {
				this.eventType = EVENT_TYPE.INSERT;
			}
		} else {
			if (after.length() == 0) {
				this.eventType = EVENT_TYPE.REMOVE;
			} else {
				this.eventType = EVENT_TYPE.REPLACE;
			}
		}
	}
	public int getOffset() {
		return offset;
	}
	public String getBefore() {
		return before;
	}
	public String getAfter() {
		return after;
	}
	public EVENT_TYPE getEventType() {
		return eventType;
	}
	@Override
	public String toString() {
		return "[" +
				offset + ", " +
				before + ", " +
				after + "]";
	}
}
