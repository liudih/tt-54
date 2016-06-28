package  com.rabbit.services.serviceImp.shipping;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.rabbit.conf.ordermapper.shipping.ShippingMethodMapper;
import com.rabbit.dto.shipping.ShippingMethod;
import com.rabbit.services.iservice.shipping.IShippingMethodService;
@Service
public class ShippingMethodService implements IShippingMethodService {
	private static Logger log=Logger.getLogger(ShippingMethodService.class.getName());
	
	@Autowired
	private ShippingMethodMapper methodMapper;
	@Autowired
	FillShippingMethod fillShippingMethod;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingMethodService#getShippingMethodById(java.lang
	 * .Integer)
	 */
	@Override
	public ShippingMethod getShippingMethodById(Integer id) {
		return methodMapper.getShippingMethodById(id);
	}



	@Override
	public String push(JsonNode jnode) throws Exception {
		if (null == jnode) {
			 log.info("ShippingMethodService push jnode:"+jnode);
			 throw new Exception( "ShippingMethodService push jnode:"+jnode);
		}
		int i = 0;
		ObjectMapper om = new ObjectMapper();
		if (jnode.isArray()) {
			ShippingMethod[] methods = om.convertValue(jnode,
					ShippingMethod[].class);
			i = add(Lists.newArrayList(methods));
			log.debug("push shipping methods receive: "+methods.length+", save: "+i);
		} else {
			ShippingMethod method = om.convertValue(jnode,
					ShippingMethod.class);
			i = add(Lists.newArrayList(method));
			log.debug("push shipping methods receive: 1, save:"+ i);
		}
		return i==0?"fail":"success";
		
	}
	
	private  int add(List<ShippingMethod> methods) {
		methods = fillShippingMethod.fill(methods);
		if (methods == null || methods.isEmpty()) {
			log.debug("set shipping method enabled = false size: 0");
			return 0;
		} else {
			String code = methods.get(0).getCcode();
			int i = methodMapper.updateEnableByCode(code, false);
			log.debug("set shipping method enabled = false size: "+i);
		}
		int i = 0;
		for (ShippingMethod m : methods) {
			i +=methodMapper.insertBase(m);
		}
		return i;
	}


}
