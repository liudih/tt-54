package com.rabbit.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

public class ObjectConverterUtils {

	private static Logger log = Logger.getLogger(ObjectConverterUtils.class
			.getName());

	public static Object ByteToObject(byte[] bytes) {
		Object obj = null;
		try {
			 ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
					 new ByteArrayInputStream(bytes)));  
			     obj = ois.readObject(); 
			// bytearray to object
			/*ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();
			bi.close();
			oi.close();*/
		} catch (Exception e) {
			log.error("translation", e);
		}
		return obj;
	}

	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			log.error("translation", e);
		}
		return bytes;
	}
}