package date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	
	@Test
	public void t(){
		run(() -> {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String dateStr = sdf.format(new Date());
	
			try {
				Date date = sdf.parse(dateStr);
				System.out.println(dateStr);
				System.out.println(sdfh.format(date));
				System.out.println(sdfh.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			assertEquals(0, 0);
		});
	}
}
