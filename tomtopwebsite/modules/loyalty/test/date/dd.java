package date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import org.junit.Test;

public class dd {

	@Test
	public void test() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String dateStr = sdf.format(new Date());

		try {
			Date date = sdf.parse(dateStr);
			System.out.println(dateStr);
			System.out.println(sdfh.format(date));	
			System.out.println(date.getTime());
			System.out.println("时间戳："+ date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
