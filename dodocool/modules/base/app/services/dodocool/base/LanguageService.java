package services.dodocool.base;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

import services.ILanguageService;

import com.google.inject.Inject;

import dto.Language;
import extensions.InjectorInstance;

public class LanguageService {

	@Inject
	ServiceFactory serviceFactory;

	@Inject
	ILanguageService languageService;

	public List<Language> getAllLanguages() throws InstantiationException,
			IllegalAccessException, MalformedURLException,
			ClassNotFoundException {
		// return languageService.getAllLanguage();
		return languageService.getAllLanguage().stream()
				.filter(l -> l.getCname().equals("en"))
				.collect(Collectors.toList());
	}

	public static List<Language> _getAllLanguagesInstance()
			throws InstantiationException, IllegalAccessException,
			MalformedURLException, ClassNotFoundException {
		return InjectorInstance.getInstance(LanguageService.class)
				.getAllLanguages();
	}

}
