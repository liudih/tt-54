package base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Util {
	
	
	public static ArrayList<Integer> base62(int id) {
		
		ArrayList<Integer> value = new ArrayList<Integer>();
		while (id > 0) {
			int remainder = id % 62;
			value.add(remainder);
			id = id / 62;
		}
		
		return value;
	}
	
	
	public static int base10(ArrayList<Integer> base62) {
		//make sure the size of base62 is 6
		for (int i = 1; i <= 6 - base62.size(); i++) {
			base62.add(0, 0);
		}
		
		int id = 0;
		int size = base62.size();
		for (int i = 0; i < size; i++) {
			int value = base62.get(i);
			id += (int) (value * Math.pow(62, size - i - 1));
		}
		
		return id;
	}
	
}
