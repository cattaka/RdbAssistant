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
 * $Id: FindCondition.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.text;

public class FindCondition {
	private String search="";
	private String replace="";
	private boolean downward = true;
	private boolean senseCaseSearch = false;
	private boolean loopSearch = false;
	private boolean wordUnitSearch = false;
	private boolean regexSearch = false;
	private ACTION action = ACTION.FIND;
	
	public enum ACTION {
		FIND,
		REPLACE,
		REPLACE_FIND,
		REPLACE_ALL
	};
	
	public FindCondition() {
	}

	public FindCondition(FindCondition orig) {
		this.search = orig.getSearch();
		this.replace = orig.getReplace();
		this.downward = orig.isDownward();
		this.senseCaseSearch = orig.isSenseCaseSearch();
		this.loopSearch = orig.loopSearch;
		this.wordUnitSearch = orig.isWordUnitSearch();
		this.regexSearch = orig.regexSearch;
		this.action = orig.getAction();
	}

	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public boolean isDownward() {
		return downward;
	}
	public void setDownward(boolean downward) {
		this.downward = downward;
	}
	public boolean isSenseCaseSearch() {
		return senseCaseSearch;
	}
	public void setSenseCaseSearch(boolean senseCaseSearch) {
		this.senseCaseSearch = senseCaseSearch;
	}
	public boolean isLoopSearch() {
		return loopSearch;
	}
	public void setLoopSearch(boolean loopSearch) {
		this.loopSearch = loopSearch;
	}
	public boolean isWordUnitSearch() {
		return wordUnitSearch;
	}
	public void setWordUnitSearch(boolean wordUnitSearch) {
		this.wordUnitSearch = wordUnitSearch;
	}
	public boolean isRegexSearch() {
		return regexSearch;
	}
	public void setRegexSearch(boolean regexSearch) {
		this.regexSearch = regexSearch;
	}
	public ACTION getAction() {
		return action;
	}
	public void setAction(ACTION action) {
		this.action = action;
	}
}
