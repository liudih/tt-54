package tracking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapTest {

	public static void main(String[] args) {
		Map<String,String> hashmap =new HashMap<String,String>();
		hashmap.put("Item0",     "Value0");   
        hashmap.put("Item1",     "Value1");   
        hashmap.put("Item2",     "Value2");   
        hashmap.put("Item3",     "Value3"); 
        Set set = hashmap.entrySet(); 
        Iterator iterator =set.iterator();
        while(iterator.hasNext()){   
        	Map.Entry mapentry = (Map.Entry)iterator.next();   
        	System.out.println(mapentry.getKey()+"/"+ mapentry.getValue());   
        }   
	}
}
