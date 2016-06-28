package services.search;

import interceptors.CacheResult;

import java.util.List;

import javax.inject.Inject;

import mapper.search.KeywordSuggestMapper;
import services.product.CategoryEnquiryService;
import valueobjects.search.Suggestion;

import com.google.common.collect.Lists;

import dto.search.KeywordSearchLog;
import dto.search.KeywordSuggest;

public class KeywordSuggestService implements IKeyWordSuggestService {

	@Inject
	KeywordSuggestMapper suggestMapper;

	@Inject
	CategoryEnquiryService categoryService;

	@Override
	public List<Suggestion> getSuggestions(final String partial,
			final int siteId, final int languageId) {
		if (partial != null) {
			List<KeywordSuggest> kws = suggestMapper.findByKeywordLike(partial
					.trim().toLowerCase() + "%", siteId, languageId, 10);
			return Lists.transform(
					kws,
					ks -> new Suggestion(ks.getCkeyword(), ks.getIrank(), ks
							.getIcategoryid(),
							ks.getIcategoryid() != null ? categoryService
									.getCategoryNameByCategoryIdAndLanguage(
											ks.getIcategoryid(), languageId)
									.getCname() : null, ks.getIresults(), ks
									.getCinfo()));

		}
		return Lists.newArrayList();
	}

	@Override
	@CacheResult
	public List<KeywordSuggest> getHotProductSuggestions(final int siteId,
			final int languageId) {
		List<KeywordSuggest> kws = suggestMapper.showHotProducts(siteId,
				languageId);
		List<KeywordSuggest> result = Lists.newArrayList();
		int count = 0;
		for (KeywordSuggest k : kws) {
			if (count > 20) {
				return result;
			}
			String word = k.getCkeyword();
			if (word != null && !"".equals(word) && word.length() >= 3) {
				result.add(k);
				count++;
			}
		}
		return result;
	}

	@Override
	public Integer saveKeywordSearchLog(KeywordSearchLog keywordSearchLog) {
		try {
			return suggestMapper.insetKeywordSearchLog(keywordSearchLog);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

}
