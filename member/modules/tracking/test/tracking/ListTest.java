package tracking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.api.client.util.Lists;

public class ListTest {

	@Test
	public void t(){
		
		List<String> list =Lists.newArrayList();

		list.add("d");
		list.add("dsss");
	
		for(String s:list){
			System.out.print(s);
		}
		
		for(int i=0;i<list.size();i++){
			String c=list.get(i);
			System.out.print(c);
		}
		
		for(Iterator<String> it=list.iterator();it.hasNext();){
		  String s=	it.next();
		  System.out.print(s);
		}
		
		
	}
}
