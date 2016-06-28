package services.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import play.Logger;

public class FileUtils {

	public static byte[] toByteArray(File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] buff = IOUtils.toByteArray(in);
			return buff;
		} catch (Exception e) {
			Logger.error("file to ByteArray error:" + e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
