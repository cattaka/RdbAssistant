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
 * $Id: StdComboBoxEditor.java 232 2009-08-01 07:06:41Z cattaka $
 */
package net.cattaka.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import net.cattaka.swing.text.StdTextField;

public class StdComboBoxEditor extends MetalComboBoxEditor {


    public StdComboBoxEditor() {
        super();
        //editor.removeFocusListener(this);
        editor = new StdTextField("",9) {
			private static final long serialVersionUID = 1L;
				// workaround for 4530952
                public void setText(String s) {
                    if (getText().equals(s)) {
                        return;
                    }
                    super.setText(s);
                }
            // The preferred and minimum sizes are overriden and padded by
            // 4 to keep the size as it previously was.  Refer to bugs
            // 4775789 and 4517214 for details.
            public Dimension getPreferredSize() {
                Dimension pref = super.getPreferredSize();
                pref.height += 4;
                return pref;
            }
            public Dimension getMinimumSize() {
                Dimension min = super.getMinimumSize();
                min.height += 4;
                return min;
            }
            };

        editor.setBorder( new EditorBorder() );
        //editor.addFocusListener(this);
    }

    protected static Insets editorBorderInsets = new Insets( 2, 2, 2, 0 );
    private static final Insets SAFE_EDITOR_BORDER_INSETS = new Insets( 2, 2, 2, 0 );

    class EditorBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;

		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.translate( x, y );

            if (MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme) {
                g.setColor(MetalLookAndFeel.getControlDarkShadow());
                g.drawRect(0, 0, w, h - 1);
                g.setColor(MetalLookAndFeel.getControlShadow());
                g.drawRect(1, 1, w - 2, h - 3);
            }
            else {
                g.setColor( MetalLookAndFeel.getControlDarkShadow() );
                g.drawLine( 0, 0, w-1, 0 );
                g.drawLine( 0, 0, 0, h-2 );
                g.drawLine( 0, h-2, w-1, h-2 );
                g.setColor( MetalLookAndFeel.getControlHighlight() );
                g.drawLine( 1, 1, w-1, 1 );
                g.drawLine( 1, 1, 1, h-1 );
                g.drawLine( 1, h-1, w-1, h-1 );
                g.setColor( MetalLookAndFeel.getControl() );
                g.drawLine( 1, h-2, 1, h-2 );
            }

            g.translate( -x, -y );
        }

        public Insets getBorderInsets( Component c ) {
            if (System.getSecurityManager() != null) {
                return SAFE_EDITOR_BORDER_INSETS;
            } else {
                return editorBorderInsets;
            }
        }
    }


    /**
     * A subclass of BasicComboBoxEditor that implements UIResource.
     * BasicComboBoxEditor doesn't implement UIResource
     * directly so that applications can safely override the
     * cellRenderer property with BasicListCellRenderer subclasses.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is
     * appropriate for short term storage or RMI between applications running
     * the same version of Swing.  As of 1.4, support for long term storage
     * of all JavaBeans<sup><font size="-2">TM</font></sup>
     * has been added to the <code>java.beans</code> package.
     * Please see {@link java.beans.XMLEncoder}.
     */
    public static class UIResource extends MetalComboBoxEditor
    implements javax.swing.plaf.UIResource {
    }
}