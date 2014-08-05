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
 * $Id: StatementExecuteThread.java 430 2010-12-05 10:36:14Z cattaka $
 */
package net.cattaka.rdbassistant.gui.table;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.cattaka.util.ExceptionHandler;

public class StatementExecuteThread extends Thread {
	private PreparedStatement statement;
	private boolean executeResult;
	private SQLException sqlException;
	private boolean canceled;
	
	public StatementExecuteThread(PreparedStatement statement) {
		super();
		this.statement = statement;
		this.canceled = false;
	}
	@Override
	public void run() {
		try {
			this.executeResult = statement.execute();
		} catch(SQLException e) {
			sqlException = e;
			ExceptionHandler.debug(e);
		}
	}

	public void cancel() {
		this.canceled = true;
		try {
			this.statement.cancel();
		} catch(SQLException e) {
			if (sqlException != null) {
				sqlException = e;
			}
		}
	}
	
	public boolean getExecuteResult() {
		return executeResult;
	}
	
	public SQLException getSqlException() {
		return sqlException;
	}

	public boolean isCanceled() {
		return canceled;
	}
}
