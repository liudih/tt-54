package services.mobile.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.InterestTagMapper;

import org.apache.commons.lang3.StringUtils;

import utils.ValidataUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import entity.mobile.InterestTag;
import entity.mobile.TagType;

public class InterestTagService {

	@Inject
	InterestTagMapper interestTagMapper;

	public boolean addInterestTags(List<InterestTag> ics, String imei) {
		boolean flag = true;
		interestTagMapper.delete(imei);
		if (ics != null && ics.size() > 0) {
			for (InterestTag ic : ics) {
				int result = interestTagMapper.insert(ic);
				if (result < 0) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public List<String> getInterestTags(String imei, int type) {
		if (StringUtils.isNotBlank(imei)) {
			List<InterestTag> tags = interestTagMapper.getInterestTags(imei);
			List<Integer> tagStrs = Lists.transform(tags, t -> t.getTagid());
			if (tagStrs != null && tagStrs.size() > 0) {
				List<TagType> tagTyps = interestTagMapper.getTagTypeByTagIds(
						tagStrs, type);
				return Lists.transform(tagTyps, tt -> tt.getTagvalue());
			}
		}
		return null;
	}

	public List<Map<String, Object>> getTagTypes(int lang) {
		List<TagType> tags = interestTagMapper.getTagTypes(lang);
		List<Map<String, Object>> result = Lists.transform(tags,
				new Function<TagType, Map<String, Object>>() {
					@Override
					public Map<String, Object> apply(TagType tagType) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("tagid", tagType.getIid());
						map.put("tagname",
								ValidataUtils.validataStr(tagType.getTagname()));
						map.put("imgurl",
								ValidataUtils.validataStr(tagType.getImgurl()));
						return map;
					}
				});
		return result;
	}
}
