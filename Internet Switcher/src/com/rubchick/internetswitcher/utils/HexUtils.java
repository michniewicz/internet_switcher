package com.rubchick.internetswitcher.utils;

public class HexUtils {

	public static boolean getHexKey(String s) {
		if (s == null) {
			return false;
		}

		int len = s.length();
		if (len != 10 && len != 26 && len != 58) {
			return false;
		}

		for (int i = 0; i < len; ++i) {
			char c = s.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')
					|| (c >= 'A' && c <= 'F')) {
				continue;
			}
			return false;
		}
		return true;
	}
}
