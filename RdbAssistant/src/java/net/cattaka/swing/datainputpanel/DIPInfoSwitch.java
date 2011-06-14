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
 * $Id: DIPInfoSwitch.java 232 2009-08-01 07:06:41Z cattaka $
 */
/*
 * 作成日: 2007/02/01
 */
package net.cattaka.swing.datainputpanel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JComponent;


public class DIPInfoSwitch implements DIPInfo, ItemListener
{
    private String label;
    private boolean defaultData;
    private JCheckBox checkBox;
    private HashSet<DIPInfo> enableComponents = new HashSet<DIPInfo>();
    private HashSet<DIPInfo> disnableComponents = new HashSet<DIPInfo>();
    
    public DIPInfoSwitch(String label, boolean defaultData, DIPInfo[] enableComs, DIPInfo[] disnableComs) throws InvalidDataTypeException
    {
        super();
        if (label == null)
            throw new NullPointerException();
        
        this.label = "";
        this.defaultData = defaultData;
        this.checkBox = new JCheckBox(label);
        this.checkBox.addItemListener(this);

        for(int i=0;i<enableComs.length;i++)
        {
            enableComponents.add(enableComs[i]);
        }
        for(int i=0;i<disnableComs.length;i++)
        {
            disnableComponents.add(disnableComs[i]);
        }
        setValue(defaultData);
    }
    public boolean getBooleanValue()
    {
        return checkBox.isSelected();
    }
    public Object getValue()
    {
        return checkBox.isSelected();
    }
    public void setValue(boolean value)
    {
        checkBox.setSelected(value);
        switchEnable(value);
    }
    private void switchEnable(boolean value)
    {
        Iterator<DIPInfo> ite;
        ite = enableComponents.iterator();
        while(ite.hasNext())
        {
            ite.next().setEnable(value);
        }
        ite = disnableComponents.iterator();
        while(ite.hasNext())
        {
            ite.next().setEnable(!value);
        }
    }
    public void makeDefault()
    {
        setValue(defaultData);
    }
    public String getLabel()
    {
        return label;
    }
    public JComponent getComponent()
    {
        return checkBox;
    }
    public boolean isEnable()
    {
        return checkBox.isEnabled();
    }
    public void setEnable(boolean enable)
    {
        checkBox.setEnabled(enable);
    }
    
    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource() == checkBox)
        {
            boolean f = checkBox.isSelected();
            switchEnable(f);
        }
    }
    
}
