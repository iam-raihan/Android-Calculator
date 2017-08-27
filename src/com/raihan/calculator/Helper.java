package com.raihan.calculator;

public class Helper {
	public static Boolean checkString(String str){
		return str.equals("Invalid Expression")
				|| str.equals("NaN")
				|| str.contains("Infinity");
	}
}
