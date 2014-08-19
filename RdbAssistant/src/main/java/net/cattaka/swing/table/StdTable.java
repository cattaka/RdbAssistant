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
 * $Id: StdTable.java 251 2010-02-21 13:05:24Z cattaka $
 */
package net.cattaka.swing.table;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JTable;

import net.cattaka.swing.util.HtmlSelection;
import net.cattaka.util.StringUtil;

public class StdTable extends JTable {
	private static final long serialVersionUID = 1L;
/* もう使ってない
	static class StringRenderer extends StdTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public StringRenderer() {
			super();
		}

		public void setValue(Object value) {
			super.setValue(value);
		}
	}

	static class NumberRenderer extends StdTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public NumberRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}

		public void setValue(Object value) {
			super.setValue(value);
		}
	}
*/
	public StdTable() {
		super();
		initialize();
	}

	private void initialize() {
		// レンダラーを設定
		this.setDefaultRenderer(Object.class, new StdTableStyledCellRenderer());
		this.setDefaultRenderer(String.class, new StdTableStyledCellRenderer());
		this.setDefaultRenderer(Number.class, new StdTableStyledCellRenderer());
		this.setDefaultRenderer(Date.class, new StdTableStyledCellRenderer());
		this.setDefaultRenderer(StdStyledCell.class, new StdTableStyledCellRenderer());

		// コピーアクションを上書き
		getActionMap().put("copy", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				copyAsTsv(false);
			}
		});
	}

	public String getFilteredString(int rowIndex, int columnIndex) {
		Object value = getValueAt(rowIndex, columnIndex);
		return String.valueOf(value);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void copyAsCsv(boolean withHeader) {
		int[] rows = this.getSelectedRows();
		int[] cols = this.getSelectedColumns();
		if (rows == null || rows.length == 0 || cols == null
				|| cols.length == 0) {
			// 選択されてない
			return;
		}
		int rs = rows[0];
		int re = rows[rows.length - 1];
		int cs = cols[0];
		int ce = cols[cols.length - 1];
		if (rs > re) {
			int t = rs;
			rs = re;
			re = t;
		}
		if (cs > ce) {
			int t = cs;
			cs = ce;
			ce = t;
		}
		if (!getCellSelectionEnabled()) {
			cs = 0;
			ce = getColumnCount() - 1;
		}

		StringBuilder sb = new StringBuilder();
		if (withHeader) {
			for (int c = cs; c <= ce; c++) {
				if (c != cs) {
					sb.append(',');
				}
				Object obj = getColumnModel().getColumn(c).getHeaderValue();
				if (obj != null) {
					sb.append(StringUtil.escapeCsvString(obj.toString()));
				}
			}
			sb.append('\n');
		}
		for (int r = rs; r <= re; r++) {
			if (r != rs) {
				sb.append('\n');
			}
			for (int c = cs; c <= ce; c++) {
				if (c != cs) {
					sb.append(',');
				}
				Object obj = getValueAt(r, c);
				if (obj != null) {
					sb.append(StringUtil.escapeCsvString(obj.toString()));
				}
			}
		}
		StringSelection ss = new StringSelection(sb.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(ss, ss);
	}

	public void copyAsTsv(boolean withHeader) {
		int[] rows = this.getSelectedRows();
		int[] cols = this.getSelectedColumns();
		if (rows == null || rows.length == 0 || cols == null
				|| cols.length == 0) {
			// 選択されてない
			return;
		}
		int rs = rows[0];
		int re = rows[rows.length - 1];
		int cs = cols[0];
		int ce = cols[cols.length - 1];
		if (rs > re) {
			int t = rs;
			rs = re;
			re = t;
		}
		if (cs > ce) {
			int t = cs;
			cs = ce;
			ce = t;
		}
		if (!getCellSelectionEnabled()) {
			cs = 0;
			ce = getColumnCount() - 1;
		}

		StringBuilder sb = new StringBuilder();
		if (withHeader) {
			for (int c = cs; c <= ce; c++) {
				if (c != cs) {
					sb.append('\t');
				}
				Object obj = getColumnModel().getColumn(c).getHeaderValue();
				if (obj != null) {
					sb.append(StringUtil.escapeTsvString(obj.toString()));
				}
			}
			sb.append('\n');
		}
		for (int r = rs; r <= re; r++) {
			if (r != rs) {
				sb.append('\n');
			}
			for (int c = cs; c <= ce; c++) {
				if (c != cs) {
					sb.append('\t');
				}
				Object obj = getValueAt(r, c);
				if (obj != null) {
					sb.append(StringUtil.escapeTsvString(obj.toString()));
				}
			}
		}
		StringSelection ss = new StringSelection(sb.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(ss, ss);
	}
	public void copyAsHtml(boolean withHeader) {
		int[] rows = this.getSelectedRows();
		int[] cols = this.getSelectedColumns();
		if (rows == null || rows.length == 0 || cols == null
				|| cols.length == 0) {
			// 選択されてない
			return;
		}
		int rs = rows[0];
		int re = rows[rows.length - 1];
		int cs = cols[0];
		int ce = cols[cols.length - 1];
		if (rs > re) {
			int t = rs;
			rs = re;
			re = t;
		}
		if (cs > ce) {
			int t = cs;
			cs = ce;
			ce = t;
		}
		if (!getCellSelectionEnabled()) {
			cs = 0;
			ce = getColumnCount() - 1;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\""
				+ " xmlns:x=\"urn:schemas-microsoft-com:office:excel\""
				+ " xmlns=\"http://www.w3.org/TR/REC-html40\">\n");
		sb.append("<body>\n");
		sb.append("<table style=\"white-space:nowrap; border-collapse:collapse; \">\n");

		if (withHeader) {
			sb.append("<tr>");
			for (int c = cs; c <= ce; c++) {
				Object obj = getColumnModel().getColumn(c).getHeaderValue();
				if (obj != null) {
					sb.append(toHtmlTd(obj));
				}
			}
			sb.append("</tr>\n");
		}
		for (int r = rs; r <= re; r++) {
			sb.append("<tr>");
			for (int c = cs; c <= ce; c++) {
				Object obj = getValueAt(r, c);
				sb.append(toHtmlTd(obj));
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");
		
		HtmlSelection ss = new HtmlSelection(sb.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(ss, ss);
	}
	private String toHtmlTd(Object obj) {
		String result = null;
		if (obj == null) {
			result = "<td></td>";
		} else if (obj instanceof StdStyledCell) {
			StdStyledCell cell = (StdStyledCell)obj;
			StringBuilder sb = new StringBuilder();
			StringBuilder style = new StringBuilder();
			// スタイルの作成
			if (cell.getForeground() != null) {
				style.append("color:");
				style.append(StringUtil.colorToHex(cell.getForeground()));
				style.append("; ");
			}
			if (cell.getBackground() != null) {
				style.append("background-color:");
				style.append(StringUtil.colorToHex(cell.getBackground()));
				style.append("; ");
			}
			if (cell.getBorderThickness() > 0 && cell.getBorderColor() != null) {
				style.append("border-style: solid; ");
				style.append("border-width: "+ cell.getBorderThickness() +"pt; ");
				style.append("border-color: ");
				style.append(StringUtil.colorToHex(cell.getBorderColor()));
				style.append("; ");
			}
			// TDタグの作成
			sb.append("<td");
			if (style.length() > 0) {
				sb.append(" style=\"");
				sb.append(style.toString());
				sb.append("\"");
			}
			if (cell.getValue() != null) {
				sb.append(" x:str");
			}
			sb.append('>');
			if (cell.getValue() != null) {
				sb.append(StringUtil.escapeHtmlString(cell.getValue()));
			}
			sb.append("</td>");
			result = sb.toString();
		} else {
			result = "<td x:str>" + String.valueOf(obj)+ "</td>";
		}
		return result;
	}
}
