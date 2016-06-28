package com.tomtop.product.utils;

import java.util.regex.Pattern;

public class NumberFormatUtils {

	public static boolean isNumberic(String str){ 
		
		Pattern p = Pattern.compile("[0-9]*"); 
		return p.matcher(str).matches(); 
	} 
}
