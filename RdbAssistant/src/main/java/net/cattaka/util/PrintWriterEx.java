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
 * $Id: PrintWriterEx.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class PrintWriterEx extends PrintWriter {

	public PrintWriterEx(File file, String csn) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(file, csn);
	}
	public PrintWriterEx(File file) throws FileNotFoundException {
		super(file);
	}
	public PrintWriterEx(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
	}

	public PrintWriterEx(OutputStream out) {
		super(out);
	}

	public PrintWriterEx(String fileName, String csn)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
	}

	public PrintWriterEx(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	public PrintWriterEx(Writer out, boolean autoFlush) {
		super(out, autoFlush);
	}

	public PrintWriterEx(Writer out) {
		super(out);
	}
	@Override
	public void println() {
		super.print('\n');
	}
	@Override
	public void println(boolean x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(char x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(char[] x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(double x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(float x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(int x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(long x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(Object x) {
		super.print(x);
		super.print('\n');
	}
	@Override
	public void println(String x) {
		super.print(x);
		super.print('\n');
	}

	
}
