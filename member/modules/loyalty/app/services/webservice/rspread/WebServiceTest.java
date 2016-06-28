package services.webservice.rspread;
import java.rmi.RemoteException;

import org.apache.axis.AxisFault;
public class WebServiceTest {

	public static void main(String args[]) throws RemoteException{  
		ServiceSoapProxy s = new ServiceSoapProxy();
		s.setEndpoint("http://service.reasonablespread.com/");
		DoubleOptIn addOption = DoubleOptIn.Off;
		String leimu = "Lenovo";
		String leimu2 = "Cell Phones";
//		try {
//			boolean a = s.addSubscriberByEmail("sales160@tomtop.com","Tt1428.EDM","liudih@qq.com",leimu, addOption);
//			System.out.println(a+"++++++");
//		} catch (AxisFault a) {
//			a.printStackTrace();
//		}
		try {
			boolean flag = s.createSubscription("sales160@tomtop.com","Tt1428.EDM",leimu,leimu);
			System.out.println(flag+"++++++");
		} catch (AxisFault a) {
			if(a!=null){
				System.out.println("you yi chang");
			}
		}
	}

}
