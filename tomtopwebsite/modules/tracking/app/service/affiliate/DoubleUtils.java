package service.affiliate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import play.Logger;

public class DoubleUtils {

	public static  double total(List<Double> values,double factor) {

		if (values != null) {
			double total = 0.00;
			for (Double value : values) {
				total += value*factor;
			}

			return total;
		}
		return 0.;
	}
	
	public static double format(double value,String pattern){
		DecimalFormat df = new DecimalFormat(pattern);
		String result = df.format(value);
		try {
			return df.parse(result).doubleValue();
		} catch (ParseException e) {
			Logger.error("formatDouble error:{}", e.getMessage());
		}
		return 0;
	}
}
