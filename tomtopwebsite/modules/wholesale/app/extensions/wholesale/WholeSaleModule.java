package extensions.wholesale;

import mapper.wholesale.WholeSaleBaseMapper;
import mapper.wholesale.WholeSaleCategoryMapper;
import mapper.wholesale.WholeSaleDiscountLevelMapper;
import mapper.wholesale.WholeSaleOrderProductMapper;
import mapper.wholesale.WholeSaleProductMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import services.cart.ICartFragmentPlugin;
import services.cart.SimpleCartFragmentPlugin;
import services.wholesale.fragment.WholesaleFragmentRenderer;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.wholesale.IWholeSaleBaseEnquiryDao;
import dao.wholesale.IWholeSaleBaseUpdateDao;
import dao.wholesale.IWholeSaleCategoryEnquiryDao;
import dao.wholesale.IWholeSaleCategoryUpdateDao;
import dao.wholesale.IWholeSaleDiscountLevelEnquiryDao;
import dao.wholesale.IWholeSaleDiscountLevelUpdateDao;
import dao.wholesale.IWholeSaleOrderProductUpdateDao;
import dao.wholesale.IWholeSaleProductEnquiryDao;
import dao.wholesale.IWholeSaleProductUpdateDao;
import dao.wholesale.impl.WholeSaleBaseEnquiryDao;
import dao.wholesale.impl.WholeSaleBaseUpdateDao;
import dao.wholesale.impl.WholeSaleCategoryEnquiryDao;
import dao.wholesale.impl.WholeSaleCategoryUpdateDao;
import dao.wholesale.impl.WholeSaleDiscountLevelEnquiryDao;
import dao.wholesale.impl.WholeSaleDiscountLevelUpdateDao;
import dao.wholesale.impl.WholeSaleOrderProductUpdateDao;
import dao.wholesale.impl.WholeSaleProductEnquiryDao;
import dao.wholesale.impl.WholeSaleProductUpdateDao;
import extensions.ModuleSupport;
import extensions.cart.ICartFragmentExtension;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.order.IOrderDetailExtension;
import extensions.order.IOrderDetailProvider;
import extensions.runtime.IApplication;
import extensions.wholesale.member.WholeSaleMenuProvider;
import extensions.wholesale.member.WholeSaleOrderMenuProvider;
import extensions.wholesale.member.WholeSaleProductListMenuProvider;

public class WholeSaleModule extends ModuleSupport implements MyBatisExtension,
		IMemberAccountExtension, ICartFragmentExtension, IOrderDetailExtension {

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IWholeSaleBaseEnquiryDao.class).to(
						WholeSaleBaseEnquiryDao.class);
				bind(IWholeSaleBaseUpdateDao.class).to(
						WholeSaleBaseUpdateDao.class);
				bind(IWholeSaleCategoryUpdateDao.class).to(
						WholeSaleCategoryUpdateDao.class);
				bind(IWholeSaleCategoryEnquiryDao.class).to(
						WholeSaleCategoryEnquiryDao.class);
				bind(IWholeSaleDiscountLevelEnquiryDao.class).to(
						WholeSaleDiscountLevelEnquiryDao.class);
				bind(IWholeSaleDiscountLevelUpdateDao.class).to(
						WholeSaleDiscountLevelUpdateDao.class);
				bind(IWholeSaleProductEnquiryDao.class).to(
						WholeSaleProductEnquiryDao.class);
				bind(IWholeSaleProductUpdateDao.class).to(
						WholeSaleProductUpdateDao.class);
				bind(IWholeSaleOrderProductUpdateDao.class).to(
						WholeSaleOrderProductUpdateDao.class);
			}
		};
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("wholesale", WholeSaleBaseMapper.class);
		service.addMapperClass("wholesale", WholeSaleCategoryMapper.class);
		service.addMapperClass("wholesale", WholeSaleDiscountLevelMapper.class);
		service.addMapperClass("wholesale", WholeSaleProductMapper.class);
		service.addMapperClass("wholesale", WholeSaleOrderProductMapper.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {
		menuProviders.addBinding().to(WholeSaleMenuProvider.class);
		menuProviders.addBinding().to(WholeSaleProductListMenuProvider.class);
		menuProviders.addBinding().to(WholeSaleOrderMenuProvider.class);
	}

	@Override
	public void registerCartFragment(Multibinder<ICartFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleCartFragmentPlugin("wholesale_collect", null,
						WholesaleFragmentRenderer.class));

	}

	@Override
	public void registerOrderDetailProvider(
			Multibinder<IOrderDetailProvider> detailProvider) {
		detailProvider.addBinding().to(WholesaleOrderDetailProvider.class);
	}
}