package net.cattaka.util;

import java.util.Comparator;

public class StringArrayComparator implements Comparator<String[]> {
	public int compare(String[] o1, String[] o2) {
		int result = 0;
		for (int i=0;i<o1.length && i<o2.length;i++) {
			String s1 = o1[i];
			String s2 = o2[i];
			if (s1 == null) {
				if (s2 == null) {
					result = 0;
				} else {
					result = 1;
				}
			} else {
				if (s2 == null) {
					result = -1;
				} else {
					result = s1.compareTo(s2);
				}
			}
			if (result != 0) {
				break;
			}
		}
		if (result == 0) {
			if (o1.length < o2.length) {
				result = -1;
			} else if (o1.length > o2.length) {
				result = 1;
			}
		}
		
		return result;
	}
}
