package net.cattaka.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;

public class ResourceUtil {
	public static InputStream getResourceAsStream(String name) {
		Locale locale = Locale.getDefault();
		String[] tags = new String[] { "_" + locale.toLanguageTag(),
				"_" + locale.getLanguage(), "" };
		for (String tag : tags) {
			InputStream in = ResourceUtil.class.getClassLoader()
					.getResourceAsStream(String.format(Locale.ROOT, name, tag));
			if (in != null) {
				return in;
			}
		}
		return null;
	}
	public static Reader getResourceAsReader(String name) {
		InputStream in = getResourceAsStream(name);
		if (in != null) {
			try {
				return new InputStreamReader(in, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return new InputStreamReader(in);
			}
		} else {
			return null;
		}
	}

	public static Properties getPropertiesResourceAsStream(String name,
			boolean fromXml) {
		Properties properties = new Properties();
		try {
			InputStream in = null;
			try {
				in = getResourceAsStream(name);
				if (in != null) {
					if (fromXml) {
						properties.loadFromXML(in);
					} else {
						properties.load(in);
					}
				}
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// ignore
						ExceptionHandler.warn(e);
					}
				}
			}
		} catch (Exception e) {
			ExceptionHandler.error(e);
		}
		return properties;
	}

	public static PropertiesEx getPropertiesExResourceAsStream(String name,
			boolean fromXml) {
		PropertiesEx properties = new PropertiesEx();
		try {
			InputStream in = getResourceAsStream(name);
			if (in != null) {
				if (fromXml) {
					properties.loadFromXML(in);
				} else {
					properties.load(in);
				}
			}
		} catch (Exception e) {
			ExceptionHandler.error(e);
		}
		return properties;
	}
}
