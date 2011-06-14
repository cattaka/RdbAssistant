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
 * $Id: ExceptionHandler.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.cattaka.swing.util.ExceptionInfo;
import net.cattaka.swing.util.ExceptionListTableModel;
import net.cattaka.swing.util.ExceptionViewDialog;

public class ExceptionHandler {
	public enum Priority {
		DEBUG,
		INFO,
		WARNING,
		ERROR,
		FATAL
	};
	private static ArrayList<ExceptionInfo> excepionInfoList;
	private static ExceptionListTableModel exceptionListTableModel;
	private static ExceptionViewDialog exceptionViewDialog;
	private static Priority currentPriority;
	
	static {
		excepionInfoList = new ArrayList<ExceptionInfo>();
		exceptionListTableModel = new ExceptionListTableModel(excepionInfoList);
		exceptionViewDialog = new ExceptionViewDialog(exceptionListTableModel);
		currentPriority = Priority.INFO;
	}
	
	public static void setCurrentPriority(Priority currentPriority) {
		ExceptionHandler.currentPriority = currentPriority;
	}
	public static Priority getCurrentPriority() {
		return ExceptionHandler.currentPriority;
	}
	public static void debug(String msg) {
		insertExceptionInfo(Priority.DEBUG, msg);
	}
	public static void warn(String msg) {
		insertExceptionInfo(Priority.WARNING, msg);
	}
	public static void info(String msg) {
		insertExceptionInfo(Priority.INFO, msg);
	}
	public static void error(String msg) {
		insertExceptionInfo(Priority.ERROR, msg);
	}
	public static void fatal(String msg) {
		insertExceptionInfo(Priority.FATAL, msg);
	}
	public static void debug(Throwable e) {
		insertExceptionInfo(Priority.DEBUG,e);
	}
	public static void warn(Throwable e) {
		insertExceptionInfo(Priority.WARNING,e);
	}
	public static void info(Throwable e) {
		insertExceptionInfo(Priority.INFO,e);
	}
	public static void error(Throwable e) {
		insertExceptionInfo(Priority.ERROR,e);
	}
	public static void fatal(Throwable e) {
		insertExceptionInfo(Priority.FATAL,e);
	}
	
	private static void insertExceptionInfo(Priority priority, String msg) {
		if (currentPriority.ordinal() <= priority.ordinal()) {
			System.err.println(msg);
			ExceptionInfo exceptionInfo = createExceptionInfo(priority, msg);
			excepionInfoList.add(exceptionInfo);
			int size = excepionInfoList.size();
			exceptionListTableModel.fireTableRowsInserted(size, size);
		}
	}
	
	private static void insertExceptionInfo(Priority priority, Throwable e) {
		if (currentPriority.ordinal() <= priority.ordinal()) {
			e.printStackTrace();
			ExceptionInfo exceptionInfo = createExceptionInfo(priority, e);
			excepionInfoList.add(exceptionInfo);
			int size = excepionInfoList.size();
			exceptionListTableModel.fireTableRowsInserted(size, size);
		}
	}
	
	public static List<ExceptionInfo> getExcepionInfoList() {
		return excepionInfoList;
	}
	
	public static ExceptionViewDialog getExceptionViewDialog() {
		return exceptionViewDialog;
	}
	
	private static ExceptionInfo createExceptionInfo(Priority priority, String message) {
		ExceptionInfo result = new ExceptionInfo(new Date(), priority.name(), message, "");
		return result;
	}
	private static ExceptionInfo createExceptionInfo(Priority priority, Throwable e) {
		ExceptionInfo result = new ExceptionInfo(new Date(), priority.name(), e.getMessage(), ExceptionUtil.toString(e));
		return result;
	}
	
	public static String[] getPriorityNames() {
		return new String[] {
			Priority.DEBUG.name(),
			Priority.INFO.name(),
			Priority.WARNING.name(),
			Priority.ERROR.name(),
			Priority.FATAL.name()
		};
	}
}
