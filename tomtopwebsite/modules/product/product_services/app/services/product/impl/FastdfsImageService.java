package services.product.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.ws.WS;
import services.image.ImageEnquiryService;
import services.product.IImageService;
import valueobjects.product.ImageBo;

import com.google.api.client.util.Maps;
import com.google.common.collect.FluentIterable;

import dto.image.Img;

/**
 * fastdfs 服务器取图片
 * 
 * @author lijun
 *
 */
@Singleton
public class FastdfsImageService implements IImageService {

	public static final int TIMEOUT = 10 * 1000;

	@Inject
	ImageEnquiryService enquiry;

	TrackerClient tClient;

	private String endPoint;

	@Override
	public int getPriority() {
		return 1;
	}

	public FastdfsImageService() {
		Configuration config = Play.application().configuration()
				.getConfig("fastdfs");
		try {

			Integer connect_timeout = config.getInt("connect_timeout");
			if (connect_timeout != null) {
				ClientGlobal.setG_connect_timeout(connect_timeout * 1000);
			}
			Integer network_timeout = config.getInt("network_timeout");
			if (network_timeout != null) {
				ClientGlobal.setG_network_timeout(network_timeout * 1000);
			}
			String charset = config.getString("charset");
			if (charset != null) {
				ClientGlobal.setG_charset(charset);
			}
			Integer tracker_http_port = config.getInt("tracker_http_port");
			if (tracker_http_port != null) {
				ClientGlobal.setG_tracker_http_port(tracker_http_port);
			}
			Boolean anti_steal_token = config.getBoolean("anti_steal_token");
			if (anti_steal_token != null) {
				ClientGlobal.setG_anti_steal_token(anti_steal_token);
			}

			String tracker_server = config.getString("tracker_server");
			if (tracker_server != null) {
				String[] parts = tracker_server.split("\\:", 2);
				if (parts.length != 2) {
					throw new MyException(
							"the value of item \"tracker_server\" is invalid, the correct format is host:port");
				}

				InetSocketAddress[] tracker_servers = new InetSocketAddress[1];
				tracker_servers[0] = new InetSocketAddress(parts[0].trim(),
						Integer.parseInt(parts[1].trim()));

				TrackerGroup tracker_group = new TrackerGroup(tracker_servers);
				ClientGlobal.setG_tracker_group(tracker_group);
			}

			tClient = new TrackerClient();

			this.endPoint = Play.application().configuration()
					.getString("image.server.endPoint");
		} catch (Exception e) {
			Logger.error("init TrackerClient error", e);
		}

	}

	@Override
	public ImageBo getImage(String dir) {
		return null;
	}

	
	@Override
	public void save(Img img) {
		this.saveImg(img, null, null);
	}
	
	private void saveImg(Img img,Integer width, Integer height) {
		Logger.debug("****************save image to fastdfs****************");

		if (img == null) {
			Logger.debug("img is null so igonre upload");
			return;
		}

		byte[] file_buff = img.getBcontent();
		if (file_buff == null || file_buff.length == 0) {
			Logger.debug("image byte is null so igonre upload");
			return;
		}
		String type = img.getCcontenttype();
		if (type == null || type.length() == 0) {
			Logger.debug("image type is null so igonre upload");
			return;
		}

		String[] types = type.split("/");
		if (types.length < 2) {
			Logger.debug("resolve image type error so igonre upload");
			return;
		}
		String ext_name = "javascript".endsWith(types[1]) ? "js" : types[1];

		NameValuePair[] meta_list = null;
		TrackerServer tracker = null;
		try {

			tracker = tClient.getConnection();

			StorageClient sClient = new StorageClient(tracker, null);
			String[] feedback = sClient.upload_file(file_buff, ext_name,
					meta_list);

			String fullDir = feedback[0] + "/" + feedback[1];

			Map<String, String> paras = Maps.newLinkedHashMap();
			
			paras.put("md5", img.getCmd5());
			paras.put("path", img.getCpath());
			paras.put("type", ext_name);
			paras.put("route", fullDir);
			if(width != null){
				paras.put("width", width.toString());
			}
			if(height != null){
				paras.put("height", height.toString());
			}

			WS.client().url(this.endPoint).post(this.toUrlEncoded(paras));

		} catch (Exception e) {
			Logger.error("upload image error", e);
		}finally{
			if(tracker != null){
				try {
					tracker.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return;
	}

	private String toUrlEncoded(Map<String, String> params) {
//		ObjectMapper om = new ObjectMapper();
//		JSONObject json = om.convertValue(params, JSONObject.class);
//		return json.toString();
		return URLEncodedUtils.format(
				FluentIterable
						.from(params.entrySet())
						.transform(
								e -> new BasicNameValuePair(e.getKey(), e
										.getValue())).toList(), "UTF-8");
	}

	@Override
	public void save(Img img, Integer width, Integer height) {
		this.saveImg(img, width, height);
		
	}

}
