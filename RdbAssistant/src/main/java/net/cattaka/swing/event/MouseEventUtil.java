package net.cattaka.swing.event;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventUtil {
	static class PipeMouseListener implements MouseListener {
		private Component dest;		
		public PipeMouseListener(Component dest) {
			super();
			this.dest = dest;
		}
		public void mouseClicked(MouseEvent e) {
			e.setSource(dest);
			dest.dispatchEvent(e);
		}
		public void mouseEntered(MouseEvent e) {
			e.setSource(dest);
			dest.dispatchEvent(e);
		}
		public void mouseExited(MouseEvent e) {
			e.setSource(dest);
			dest.dispatchEvent(e);
		}
		public void mousePressed(MouseEvent e) {
			e.setSource(dest);
			dest.dispatchEvent(e);
		}
		public void mouseReleased(MouseEvent e) {
			e.setSource(dest);
			dest.dispatchEvent(e);
		}
	}
	
	public static void createMouseEventPipe(Component src, Component dest) {
		PipeMouseListener pmListener = new PipeMouseListener(dest);
		src.addMouseListener(pmListener);
	}
}
