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
 * $Id: FindConditionPanel.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing.text;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.cattaka.swing.StdComboBox;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.StringUtil;

public class FindConditionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public int maxHistoryNum = 10;

	private ArrayList<String> searchHistory;
	private ArrayList<String> replaceHistory;
	
	private StdComboBox<String> searchField;
	private StdComboBox<String> replaceField;
	
	private JRadioButton downwardRadio;
	private JRadioButton upwardRadio;

	private JCheckBox senseCaseSearchCheckBox;
	private JCheckBox loopSearchCheckBox;
	private JCheckBox wordUnitSearchCheckBox;
	private JCheckBox regexSearchCheckBox;
	
	class ActionListenerEx implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("search")) {
				updateHistory();
				doSearch();
			} else if (e.getActionCommand().equals("replace_search")) {
				updateHistory();
				doReplaceSearch();
			} else if (e.getActionCommand().equals("replace")) {
				updateHistory();
				doReplace();
			} else if (e.getActionCommand().equals("replace_all")) {
				updateHistory();
				doReplaceAll();
			} else if (e.getActionCommand().equals("close")) {
				doClose();
			}
		}
	}
	
	class KeyListenerSearch extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				updateHistory();
				doSearch();
				e.consume();
			}
		}
	}
	class KeyListenerReplace extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				updateHistory();
				doReplaceSearch();
				e.consume();
			}
		}
	}
	
	class ItemListenerEx implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			updateButtons();
		}
	}
	
	public FindConditionPanel() {
		makeLayout();
	}
	
	private void makeLayout() {
		this.searchHistory = new ArrayList<String>();
		this.replaceHistory = new ArrayList<String>();
		
		this.searchField = new StdComboBox<String>();
		this.replaceField = new StdComboBox<String>();
		this.replaceField.setEditable(true);
		{
			this.searchField.addKeyListener(new KeyListenerSearch());
			this.replaceField.addKeyListener(new KeyListenerReplace());
		}
		
		this.downwardRadio = new JRadioButton();
		this.upwardRadio = new JRadioButton();
		{
			ButtonsBundle.applyButtonDifinition(downwardRadio, "downward");
			ButtonsBundle.applyButtonDifinition(upwardRadio, "upward");
		}
		this.senseCaseSearchCheckBox = new JCheckBox();
		this.loopSearchCheckBox = new JCheckBox();
		this.wordUnitSearchCheckBox = new JCheckBox();
		this.regexSearchCheckBox = new JCheckBox();
		{
			ItemListener il = new ItemListenerEx();
			this.senseCaseSearchCheckBox.addItemListener(il);
			this.loopSearchCheckBox.addItemListener(il);
			this.wordUnitSearchCheckBox.addItemListener(il);
			this.regexSearchCheckBox.addItemListener(il);
			
			ButtonsBundle.applyButtonDifinition(senseCaseSearchCheckBox, "sense_case_search");
			ButtonsBundle.applyButtonDifinition(loopSearchCheckBox, "loop_search");
			ButtonsBundle.applyButtonDifinition(wordUnitSearchCheckBox, "word_unit_search");
			ButtonsBundle.applyButtonDifinition(regexSearchCheckBox, "regex_search");
		}
		
		JLabel searchLabel = new JLabel(MessageBundle.getMessage("search_field"));
		JLabel replaceLabel = new JLabel(MessageBundle.getMessage("replace_field"));
		
		JButton searchButton = new JButton();
		JButton replaceSearchButton = new JButton();
		JButton replaceButton = new JButton();
		JButton replaceAllButton = new JButton();
		JButton closeButton = new JButton();
		ButtonsBundle.applyButtonDifinition(searchButton, "search");
		ButtonsBundle.applyButtonDifinition(replaceSearchButton, "replace_search");
		ButtonsBundle.applyButtonDifinition(replaceButton, "replace");
		ButtonsBundle.applyButtonDifinition(replaceAllButton, "replace_all");
		ButtonsBundle.applyButtonDifinition(closeButton, "close");
		{
			ActionListener al = new ActionListenerEx();
			replaceSearchButton.addActionListener(al);
			replaceButton.addActionListener(al);
			replaceAllButton.addActionListener(al);
			closeButton.addActionListener(al);
			
			searchButton.setActionCommand("search");
			replaceSearchButton.setActionCommand("replace_search");
			replaceButton.setActionCommand("replace");
			replaceAllButton.setActionCommand("replace_all");
			closeButton.setActionCommand("close");
			searchButton.addActionListener(al);
		}

		//レイアウト処理
		{
			Dimension d1;
			Dimension d2;
			d1 = searchButton.getPreferredSize();

			d2 = replaceSearchButton.getPreferredSize();
			d1.width = Math.max(d1.width, d2.width);
			d1.height = Math.max(d1.height, d2.height);
			d2 = replaceButton.getPreferredSize();
			d1.width = Math.max(d1.width, d2.width);
			d1.height = Math.max(d1.height, d2.height);
			d2 = replaceAllButton.getPreferredSize();
			d1.width = Math.max(d1.width, d2.width);
			d1.height = Math.max(d1.height, d2.height);
			
			searchButton.setPreferredSize(d1);
			replaceSearchButton.setPreferredSize(d1);
			replaceButton.setPreferredSize(d1);
			replaceAllButton.setPreferredSize(d1);
		}
		
		JPanel upDownWordPanel = new JPanel();
		{
			upDownWordPanel.setBorder(BorderFactory.createTitledBorder(MessageBundle.getMessage("search_direction")));
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(this.upwardRadio);
			bg.add(this.downwardRadio);
			BoxLayout bl = new BoxLayout(upDownWordPanel, BoxLayout.PAGE_AXIS);
			upDownWordPanel.setLayout(bl);
			upDownWordPanel.add(upwardRadio);
			upDownWordPanel.add(downwardRadio);
		}
		
		JPanel optionPanel = new JPanel();
		{
			optionPanel.setBorder(BorderFactory.createTitledBorder(MessageBundle.getMessage("option")));
			
			BoxLayout bl = new BoxLayout(optionPanel, BoxLayout.PAGE_AXIS);
			optionPanel.setLayout(bl);
			optionPanel.add(senseCaseSearchCheckBox);
			optionPanel.add(loopSearchCheckBox);
			optionPanel.add(wordUnitSearchCheckBox);
			optionPanel.add(regexSearchCheckBox);
		}
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets.set(5,5,5,5);
		gbl.setConstraints(searchLabel, gbc);
		gbc.gridy++;
		gbl.setConstraints(replaceLabel, gbc);
		gbc.insets.set(0,0,0,0);
		gbc.gridwidth = 3;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbl.setConstraints(searchField, gbc);
		gbc.gridy++;
		gbl.setConstraints(replaceField, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbl.setConstraints(upDownWordPanel, gbc);
		gbc.gridheight = 2;
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbl.setConstraints(optionPanel, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbl.setConstraints(searchButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(replaceButton, gbc);
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbl.setConstraints(replaceSearchButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(replaceAllButton, gbc);
		gbc.gridy++;
		gbl.setConstraints(closeButton, gbc);
		
		this.setLayout(gbl);
		this.add(searchLabel);
		this.add(replaceLabel);
		this.add(searchField);
		this.add(replaceField);
		this.add(upDownWordPanel);
		this.add(optionPanel);
		this.add(searchButton);
		this.add(replaceButton);
		this.add(replaceSearchButton);
		this.add(replaceAllButton);
		this.add(closeButton);
		this.downwardRadio.setSelected(true);
	}
	
	public void updateHistory() {
		String searchStr = this.searchField.getEditor().getItem().toString();
		String replaceStr = this.replaceField.getEditor().getItem().toString();

		searchHistory.remove(searchStr);
		searchHistory.add(0,searchStr);
		while (searchHistory.size() > maxHistoryNum) {
			searchHistory.remove(maxHistoryNum);
		}
		replaceHistory.remove(replaceStr);
		replaceHistory.add(0,replaceStr);
		while (replaceHistory.size() > maxHistoryNum) {
			replaceHistory.remove(maxHistoryNum);
		}
		
		this.searchField.removeAllItems();
		for(String str : searchHistory) {
			this.searchField.addItem(str);
		}
		this.replaceField.removeAllItems();
		for(String str : replaceHistory) {
			this.replaceField.addItem(str);
		}
	}
	
	public void updateButtons() {
		this.wordUnitSearchCheckBox.setEnabled(!this.regexSearchCheckBox.isSelected());
	}
	
	public ArrayList<String> getSearchHistory() {
		return searchHistory;
	}

	public void setSearchHistory(ArrayList<String> searchHistory) {
		this.searchHistory = searchHistory;
	}

	public ArrayList<String> getReplaceHistory() {
		return replaceHistory;
	}

	public void setReplaceHistory(ArrayList<String> replaceHistory) {
		this.replaceHistory = replaceHistory;
	}

	public void setFindCondition(FindCondition findCondition) {
		this.searchField.getEditor().setItem(findCondition.getSearch());
		this.replaceField.getEditor().setItem(findCondition.getReplace());
		if (findCondition.isDownward()) {
			this.downwardRadio.setSelected(true);
		} else {
			this.upwardRadio.setSelected(true);
		}
		
		this.senseCaseSearchCheckBox.setSelected(findCondition.isSenseCaseSearch());
		this.loopSearchCheckBox.setSelected(findCondition.isLoopSearch());
		this.wordUnitSearchCheckBox.setSelected(findCondition.isWordUnitSearch());
		this.regexSearchCheckBox.setSelected(findCondition.isRegexSearch());
	}
	
	public FindCondition getFindCondition() {
		FindCondition fc = new FindCondition();
		fc.setReplace(replaceField.getEditor().getItem().toString());
		fc.setDownward(downwardRadio.isSelected());
		fc.setSenseCaseSearch(senseCaseSearchCheckBox.isSelected());
		fc.setLoopSearch(loopSearchCheckBox.isSelected());
		fc.setWordUnitSearch(wordUnitSearchCheckBox.isSelected());
		fc.setRegexSearch(regexSearchCheckBox.isSelected());
		fc.setSearch(StringUtil.replaceEscapedChar(searchField.getEditor().getItem().toString()));
//		if (fc.isRegexSearch() || fc.isWordUnitSearch()) {
//			fc.setSearch(searchField.getEditor().getItem().toString());
//		} else {
//			fc.setSearch(StringUtil.replaceEscapedChar(searchField.getEditor().getItem().toString()));
//		}
		return fc;
	}
	
	public void onShow() {
		this.searchField.getEditor().selectAll();
		this.searchField.requestFocusInWindow();
	}
	
	public void doSearch() {
	}
	public void doReplaceSearch() {
	}
	public void doReplace() {
	}
	public void doReplaceAll() {
	}
	public void doClose() {
	}
}
