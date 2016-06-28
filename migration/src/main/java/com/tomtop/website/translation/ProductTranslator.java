package com.tomtop.website.translation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tomtop.website.migration.product.SellingPointService;
import com.website.dto.product.Product;
import com.website.dto.product.TranslateItem;

public class ProductTranslator {

	@Inject
	TranslatorUtils translatorUtils;
	@Inject
	SellingPointService sellingPointService;
	@Inject
	FreeTranslation trans;

	public void Translation(String sourcePath, String targetPath,
			List<String> targetLanguageCodes, String sellPointPath)
			throws JsonParseException, JsonMappingException, IOException {
		File f = new File(sourcePath);
		File[] files = f.listFiles();
		ObjectMapper om = new ObjectMapper();

		long count = files.length;
		System.out.println("filecount-->" + count);

		List<File> fileList = Arrays.asList(files);
		Stream<String> skuProcessed = fileList
				.parallelStream()
				// process only .json file
				.filter(ff -> (ff.isFile() && ff.getName().endsWith(".json")))
				// JSON File -> Product Object Conversion
				.map(ff -> {
					Product p = null;
					try {
						p = om.readValue(ff, Product.class);
					} catch (Exception e) {
						System.err.println("Product JSON Parse Error: "
								+ ff.getName());
						e.printStackTrace();
					}
					return p;
				})
				// Skip NULL product object
				.filter(p -> p != null)
				// Translate and Output File
				.map(p -> {
					String sku = p.getSku();
					try {
						System.out.println(count + "--sku->" + sku);
						Optional<TranslateItem> english = FluentIterable
								.from(p.getItems())
								.filter(ti -> ti.getLanguageId() == 1).first();
						if (english.isPresent()) {
							TranslateItem englishItem = english.get();
							Set<Language> targetLang = Sets.newHashSet(Lists
									.transform(targetLanguageCodes,
											ts -> Language.valueOf(ts)));
							Set<Language> existingLang = FluentIterable
									.from(p.getItems())
									.transform(i -> i.getLanguageId())
									.filter(id -> id != 1)
									.transform(
											id -> {
												for (Language lang : Language
														.values()) {
													if (lang.id() == id)
														return lang;
												}
												return null;
											}).filter(lang -> lang != null)
									.toSet();
							targetLang.removeAll(existingLang);

							// Translate English Item -> targetLang
							List<TranslateItem> additional = translate(
									englishItem, targetLang);
							p.getItems().addAll(additional);
						}

						String filename = sku + ".json";
						File output = (targetPath != null) ? new File(
								targetPath, filename) : new File(filename);

						om.writeValue(output, p);
						return sku;
					} catch (Exception e) {
						System.err.println("Product Translation Error: " + sku);
						e.printStackTrace();
						return null;
					}
				});
		System.out.println("Processed: " + skuProcessed.count() + " SKU");
	}

	private List<TranslateItem> translate(TranslateItem enItem,
			Set<Language> targetLang) {
		return FluentIterable
				.from(targetLang)
				.transform(
						lang -> {
							try {

								TranslateItem newti = new TranslateItem();
								newti.setDescription(trans.translateHtml(
										enItem.getDescription(), Language.en,
										lang));
								String[] text = { enItem.getUrl(),
										enItem.getTitle(), enItem.getKeyword(),
										enItem.getMetaDescription(),
										enItem.getMetaKeyword(),
										enItem.getMetaTitle(),
										enItem.getShortDescription() };
								String[] result = trans.translate(text,
										Language.en, lang);
								newti.setUrl(result[0]);
								newti.setTitle(result[1]);
								newti.setKeyword(result[2]);
								newti.setMetaDescription(result[3]);
								newti.setMetaKeyword(result[4]);
								newti.setMetaTitle(result[5]);
								newti.setShortDescription(result[6]);
								newti.setLanguageId(lang.id());
								if (null != enItem.getSellingPoints()
										&& enItem.getSellingPoints().size() > 0) {
									List<String> spoints = enItem
											.getSellingPoints();
									String[] npoints = trans.translate(
											spoints.toArray(new String[spoints
													.size()]), Language.en,
											lang);
									newti.setSellingPoints(Lists.newArrayList(Collections2.filter(
											Lists.newArrayList(npoints),
											tsp -> tsp != null)));
								}
								return newti;
							} catch (Exception ex) {
								ex.printStackTrace();
								return null;
							}
						}).filter(ti -> ti != null).toList();
	}

}
