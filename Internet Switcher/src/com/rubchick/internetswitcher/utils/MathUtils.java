package com.rubchick.internetswitcher.utils;

import java.text.DecimalFormat;

public class MathUtils {

	public static double roundTwoDecimals(double d) {

		double result = 0;

		try {
			DecimalFormat twoDForm = new DecimalFormat("#.###");

			result = Double.valueOf(twoDForm.format(d));
		} catch (NumberFormatException ex) {

		}

		return result;
	}
}
