package net.cattaka.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class BufferedImageUtil {
	public static void clearImage(BufferedImage bufferedImage) {
		Graphics2D g = bufferedImage.createGraphics();
		g.setBackground(new Color(0,0,0,0));
		g.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g.dispose();
	}

	public static void clearImage(BufferedImage bufferedImage, Color color) {
		Graphics2D g = bufferedImage.createGraphics();
		g.setBackground(new Color(0,0,0,0));
		g.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g.setColor(color);
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g.dispose();
	}

	public static void fillImage(BufferedImage bufferedImage, Color color) {
		Graphics2D g = bufferedImage.createGraphics();
		g.setColor(color);
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g.dispose();
	}
	
	public static BufferedImage glueHorizontal(BufferedImage[] images) {
		int w = 0;
		int h = 0;
		for (BufferedImage image:images) {
			if (images != null) {
				w += image.getWidth();
				h = Math.max(h, image.getHeight());
			}
		}
		BufferedImage result = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		int x = 0;
		Graphics2D g = result.createGraphics();
		for (BufferedImage image:images) {
			if (images != null) {
				g.drawImage(image, x, 0, null);
				x += image.getWidth();
			}
		}
		g.dispose();
		return result;
	}
	public static BufferedImage glueVertical(BufferedImage[] images) {
		int w = 0;
		int h = 0;
		for (BufferedImage image:images) {
			if (images != null) {
				w = Math.max(w, image.getWidth());
				h += image.getHeight();
			}
		}
		BufferedImage result = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		int y = 0;
		Graphics2D g = result.createGraphics();
		for (BufferedImage image:images) {
			if (images != null) {
				g.drawImage(image, 0, y, null);
				y += image.getHeight();
			}
		}
		g.dispose();
		return result;
	}
	public static BufferedImage cloneBufferedImage(BufferedImage image) {
		String[] pnames = image.getPropertyNames();
		Hashtable<String, Object> cproperties = new Hashtable<String, Object>();
		if(pnames != null) {
			for(int i = 0; i < pnames.length; i++) {
				cproperties.put(pnames[i], image.getProperty(pnames[i]));
			}
		}
		WritableRaster wr = image.getRaster();
		WritableRaster cwr = wr.createCompatibleWritableRaster();
		cwr.setRect(wr);
		BufferedImage cimage = new BufferedImage(
			image.getColorModel(), // should be immutable
			cwr,
			image.isAlphaPremultiplied(),
			cproperties);
		return cimage;
	}
}
