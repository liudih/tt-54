package services.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.Logger;
import play.twirl.api.Html;
import services.ICountryService;
import services.ILanguageService;
import services.IVhostService;
import services.base.VhostService;
import services.base.WebsiteService;
import services.dropship.DropShipLevelEnquiryService;
import services.image.FilePathEnquiryService;
import services.interaction.product.post.ProductPostTypeService;
import services.member.IMemberRoleService;
import services.product.CategoryLabelTypeService;
import services.product.ProductExplainTypeService;
import session.ISessionService;
import valueobjects.manager.ChooseObject;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import dto.Country;
import dto.ProductPostType;
import dto.SimpleLanguage;
import dto.Website;
import dto.image.UploadFilePath;
import dto.member.DropShipLevel;
import dto.member.role.MemberRoleBase;
import dto.product.AttachmentTypeEnum;
import dto.product.CategoryLabelType;
import dto.product.ProductExplainType;
import entity.manager.AdminUser;
import extensions.InjectorInstance;
import extensions.payment.IPaymentProvider;

/**
 * 使用参考：views.manager.collections.edit
 *
 * @ClassName: ChooseService
 * @Description: 获取下拉框
 * @author luojiaheng
 * @date 2015年2月12日 下午3:25:46
 *
 */
@Singleton
public class ChooseService {
	@Inject
	ILanguageService languageService;
	@Inject
	WebsiteService websiteService;
	@Inject
	ISessionService sessionService;
	@Inject
	AdminUserWebsiteMapService userWebsiteMapService;
	@Inject
	CategoryLabelTypeService categoryLabelTypeService;
	@Inject
	FilePathEnquiryService filePathEnquiryService;
	@Inject
	Set<IPaymentProvider> providers;
	@Inject
	ProductExplainTypeService productExplainTypeService;
	@Inject
	VhostService vhostEnquiryService;
	@Inject
	DropShipLevelEnquiryService dropShipLevelEnquiryService;

	@Inject
	IVhostService vhostService;
	@Inject
	ProductPostTypeService productPostTypeService;
	@Inject
	ICountryService contryService;
	@Inject
	IMemberRoleService memberRoleService;

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author luojiaheng
	 */
	public Html language(String name, Integer id) {
		List<SimpleLanguage> list = languageService.getAllSimpleLanguages();
		return createChooseHtml(list, "iid", "cname", name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author renyy
	 */
	public Html contry(String name, Integer id) {
		List<Country> countryList = contryService.getAllCountries();
		List<ChooseObject> chooses = Lists.transform(countryList, c -> {
			ChooseObject co = new ChooseObject();
			co.setId(c.getCshortname());
			co.setName(c.getCname());
			return co;
		});
		return getChooseHtml(chooses, name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author renyy
	 */
	public Html yesOrNoSerach(String name, String id) {
		List<ChooseObject> list = Lists.newArrayList();
		ChooseObject yes = new ChooseObject("true", "Yes");
		ChooseObject no = new ChooseObject("false", "No");
		list.add(yes);
		list.add(no);
		return getChooseHtml(list, name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author luojiaheng
	 */
	public Html yesOrNo(String name, Boolean id) {
		ChooseObject yes = new ChooseObject("true", "Yes");
		ChooseObject no = new ChooseObject("false", "No");
		List<ChooseObject> list = Lists.newArrayList();
		list.add(yes);
		list.add(no);
		return getChooseHtml(list, name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @param b
	 *            是否根据用户权限显示
	 * @return
	 * @author luojiaheng
	 */
	public Html website(String name, Integer id, Boolean b) {
		List<Website> temp = websiteService.getAll();
		List<Website> list = null;
		if (b) {
			AdminUser user = (AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			if (!user.isBadmin()) {
				List<Integer> siteIds = userWebsiteMapService
						.getAdminUserWebsitMapsByUserId(user.getIid());
				list = Lists.newArrayList(Iterables.filter(temp,
						e -> siteIds.contains(e.getIid())));
			} else {
				list = temp;
			}
		}
		return createChooseHtml(list, "iid", "curl", name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author lijia
	 */
	public Html categoryLabelType(String name, String id) {
		List<CategoryLabelType> list = categoryLabelTypeService
				.getAllCategoryLabelTypes();

		return createChooseHtml(list, "ctype", "ctype", name, id);
	}

	public List<Website> website() {
		List<Website> temp = websiteService.getAll();
		List<Website> list = null;
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (!user.isBadmin()) {
			List<Integer> siteIds = userWebsiteMapService
					.getAdminUserWebsitMapsByUserId(user.getIid());
			list = Lists.newArrayList(Iterables.filter(temp,
					e -> siteIds.contains(e.getIid())));
		} else {
			list = temp;
		}
		return list;
	}

	/**
	 *
	 * @param list
	 *            需转换为页面选项的数据源List，如：List<Websie>
	 * @param idParam
	 *            作为input中value的数据源属性名， 如："iid"
	 * @param nameParam
	 *            作为input中显示的数据源属性名， 如："curl"
	 * @param name
	 *            select中的name属性值，如："iwebsiteid"
	 * @param id
	 *            默认被选中的id，可为null
	 * @return 一个div包含的select
	 * @author luojiaheng
	 */
	public <T> Html createChooseHtml(List<T> list, String idParam,
			String nameParam, String name, Object id) {
		List<ChooseObject> chooses = parseToChoose(list, idParam, nameParam);
		return getChooseHtml(chooses, name, id);
	}

	/**
	 *
	 * @param list
	 *            选项对象ChooseObject的List
	 * @param name
	 *            select中的name属性值，如："iwebsiteid"
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author luojiaheng
	 */
	public Html getChooseHtml(List<ChooseObject> list, String name, Object id) {
		return views.html.manager.choose.render(list, name,
				id != null ? id.toString() : null);
	}

	private <T> List<ChooseObject> parseToChoose(List<T> list, String idParam,
			String nameParam) {
		if (null != list && !list.isEmpty()) {
			Class<?> classObj = list.get(0).getClass();
			try {
				Field idField = classObj.getDeclaredField(idParam);
				idField.setAccessible(true);
				Field nameField = classObj.getDeclaredField(nameParam);
				nameField.setAccessible(true);
				List<ChooseObject> chooseObjects = Lists.transform(list, e -> {
					try {
						String id = idField.get(e).toString();
						String name = nameField.get(e).toString();
						return new ChooseObject(id, name);
					} catch (Exception e1) {
						Logger.error("parseToChoose error", e1);
					}
					return null;
				});
				return Lists.newArrayList(Iterables.filter(chooseObjects,
						e -> e != null));
			} catch (NoSuchFieldException e) {
				Logger.error("parseToChoose error", e);
			} catch (SecurityException e) {
				Logger.error("parseToChoose error", e);
			}
		}
		return new ArrayList<ChooseObject>();
	}

	public static ChooseService getInstance() {
		return InjectorInstance.getInjector().getInstance(ChooseService.class);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author lijia
	 */
	public Html filePath(String name, String id) {
		List<UploadFilePath> list = filePathEnquiryService.getAllFilePath();

		return createChooseHtml(list, "cpath", "cpath", name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author lijia
	 */
	public Html payment(String name, String id) {
		List<IPaymentProvider> payments = Lists.newArrayList(providers);
		List<ChooseObject> chooses = Lists.transform(payments, p -> {
			ChooseObject co = new ChooseObject();
			co.setId(p.id());
			co.setName(p.name());
			return co;
		});
		List<ChooseObject> chooses2 = Lists.newArrayList();
		chooses2.addAll(chooses);
		chooses2.add(new ChooseObject("paypal-ec","paypal-ec"));
		return getChooseHtml(chooses2, name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author lijia
	 */
	public Html productExplain(String name, String id) {
		List<ProductExplainType> list = productExplainTypeService
				.getAllExplainType();

		return createChooseHtml(list, "ctype", "ctype", name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name及id属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author lijia
	 */
	public Html vhost(String name, String id) {
		List<String> cvhost = vhostEnquiryService.getCvhost();
		List<ChooseObject> chooses = Lists.transform(cvhost, p -> {
			ChooseObject co = new ChooseObject();
			co.setId(p);
			co.setName(p);
			return co;
		});
		return getChooseHtml(chooses, name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name及id属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author lijia
	 */
	public Html dropshipLevel(String name, String id) {
		List<DropShipLevel> dropShipLevels = dropShipLevelEnquiryService
				.getDropShipLevels();

		return createChooseHtml(dropShipLevels, "iid", "clevelname", name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @param b
	 *            是否根据用户权限显示
	 * @return
	 * @author xcf
	 */
	public Html device(String name, Integer id, Boolean b) {
		List<dto.Vhost> deviceList = vhostService.getAllDevice();
		return createChooseHtml(deviceList, "cdevice", "cdevice", name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name及id属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author lijia
	 */
	public Html postType(String name, String id) {
		List<ProductPostType> postTypes = productPostTypeService.getAll();

		return createChooseHtml(postTypes, "iid", "ccode", name, id);
	}

	public Html attachmentType(String name, String id) {
		List<AttachmentTypeEnum> list = Arrays.asList(AttachmentTypeEnum
				.values());

		return createChooseHtml(list, "type", "type", name, id);
	}

	/**
	 *
	 * @param name
	 *            select中的name属性值
	 * @param id
	 *            默认被选中的id，可为null
	 * @return
	 * @author renyy
	 */
	public Html memberRole(String name, Integer id) {
		List<MemberRoleBase> memberRoleList = memberRoleService.getMemberRole();
		List<ChooseObject> chooses = Lists.transform(memberRoleList, c -> {
			ChooseObject co = new ChooseObject();
			co.setId(c.getIid().toString());
			co.setName(c.getCrolename());
			return co;
		});
		return getChooseHtml(chooses, name, id);
	}
}
