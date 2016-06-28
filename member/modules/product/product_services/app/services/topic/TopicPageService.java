package services.topic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.base.LanguageMapper;
import mapper.topic.TopicPageMapper;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.image.ImageUpdateService;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.collect.Iterables;

import dto.SimpleLanguage;
import dto.image.Img;
import dto.topic.TopicPage;

public class TopicPageService {
	@Inject
	TopicPageMapper pageMapper;

	@Inject
	LanguageMapper languageMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	ImageUpdateService imgUpdateService;

	public Map<String, List<TopicPage>> getMapByLanguageIdAndYear(
			Integer languageId, Integer year) {
		Map<String, Date> timeInterval = getTimeInterval(year, null);
		List<TopicPage> list = pageMapper.getTopicPage(null, languageId,
				timeInterval.get("start"), timeInterval.get("end"),
				foundationService.getSiteID(), null);
		Map<String, List<TopicPage>> map = new LinkedHashMap<String, List<TopicPage>>();
		for (TopicPage e : list) {
			if (map.get(e.getCtype()) == null) {
				map.put(e.getCtype(), new ArrayList<TopicPage>());
			}
			map.get(e.getCtype()).add(e);
		}
		return map;
	}

	public List<String> getType(Integer siteId) {
		return pageMapper.getTypeBySiteId(foundationService.getSiteID());
	}

	public List<SimpleLanguage> getLanguages() {
		List<Integer> list = pageMapper
				.getLanguageIdsBySiteId(foundationService.getSiteID());
		List<SimpleLanguage> languages = languageMapper.getAllSimpleLanguages();
		return Lists.newArrayList(Iterables.filter(languages,
				e -> list.contains(e.getIid())));
	}

	public List<Integer> getYears() {
		return pageMapper.getYearBySiteId(foundationService.getSiteID());
	}

	public List<TopicPage> filterTopicPage(String type, Integer siteID,
			Integer languageId, Integer year, Integer month, Integer size) {
		Map<String, Date> timeInterval = getTimeInterval(year, month);
		return pageMapper.getTopicPage(type, languageId,
				timeInterval.get("start"), timeInterval.get("end"), siteID,
				size);
	}

	public List<TopicPage> getAll(List<Integer> siteIds, Integer page,
			Integer size) {
		return pageMapper.getAll(siteIds, page, size);
	}

	public Integer count() {
		return pageMapper.count();
	}

	public TopicPage getTopicPageById(Integer id) {
		return pageMapper.getById(id);
	}

	public boolean deleteTopicPageById(Integer id) {
		int i = pageMapper.deleteById(id);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public boolean saveOrUpdate(TopicPage page, MultipartFormData body) {
		FilePart img = body.getFile("cimage");
		FilePart html = body.getFile("html");
		if (StringUtils.isEmpty(page.getChtmlurl())) {
			page.setChtmlurl(saveHtml(html));
		}
		page.setCimage(fileToByte(img));
		Integer i = null;
		if (null == page.getIid()) {
			i = pageMapper.insert(page);
		} else {
			i = pageMapper.update(page);
		}
		if (i == 1) {
			return true;
		}
		return false;
	}

	public String saveHtml(FilePart html) {
		if (null != html) {
			String date = DateFormatUtils.getInstance("yy-MM-dd-HH-mm-ss")
					.getDate(new Date());
			Img image = new Img();
			image.setCpath(date + "-" + html.getFilename());
			image.setBcontent(fileToByte(html));
			image.setCcontenttype(html.getContentType());
			imgUpdateService.createImage(image);
			return image.getCpath();
		}
		return null;
	}

	@SuppressWarnings("resource")
	public byte[] fileToByte(FilePart file) {
		if (null != file) {
			byte[] bytes = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file.getFile());
				bytes = new byte[fis.available()];
				fis.read(bytes);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bytes;
		}
		return null;
	}

	public byte[] getImage(Integer id) {
		TopicPage tp = pageMapper.getImage(id);
		if (null != tp) {
			return tp.getCimage();
		}
		return null;
	}

	private Map<String, Date> getTimeInterval(Integer year, Integer month) {
		Map<String, Date> timeInterval = Maps.newHashMap();
		if (null != year && null == month) {
			Date startDate = DateFormatUtils.getFormatDateByStr(year + "-1-1");
			timeInterval.put("start", startDate);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(startDate);
			gc.add(Calendar.YEAR, 1);
			timeInterval.put("end", gc.getTime());
		} else if (null != year && null != month) {
			Date startDate = DateFormatUtils.getFormatDateByStr(year + "-"
					+ month + "-1");
			timeInterval.put("start", startDate);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(startDate);
			gc.add(Calendar.MONTH, 1);
			timeInterval.put("end", gc.getTime());
		}
		return timeInterval;
	}
}
