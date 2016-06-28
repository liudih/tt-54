package member.service;

import services.member.login.ILoginService;

import com.caucho.hessian.client.HessianProxyFactory;

public class TestLoginService {
	public static void main(String[] args) throws Exception {
		HessianProxyFactory factory = new HessianProxyFactory();
//		factory.setOverloadEnabled(true);

		ILoginService s = (ILoginService) factory.create("http://www.tomtop.com/common/hs/login");

		boolean b = s.login("zliuy@163.com","19820617");
		String str = "0";
		if(b)
		{
			str="1";
		}
		System.out.println("Out: " + str);
	}

}
