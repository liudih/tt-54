package services.product;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import play.libs.Json;

import com.google.common.collect.Lists;
import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.MultiProduct;
import com.website.dto.product.TranslateItem;
import com.website.dto.product.VideoItem;

public class ProductUpdateServiceTest extends ProductBaseTest {

	@Inject
	IProductUpdateService productUpdateService;

	@Test
	public void test() {
		run(() -> {
			// ~ publish produt
			try {
				testPublishProduct();
			} catch (Exception e) {
				fail(e.getMessage());
			}
			//~ delete 
			//testDeleteProduct();
		});
	}
	
	private void testDeleteProduct(){
		String listingId= "cdf33010-74cd-43eb-92e5-9a6b0c5b6147";
		String psku = null;
		String sku = "HP001";
		productUpdateService.delete(listingId, psku, sku);
		
	}

	private void testPublishProduct() throws Exception {
		MultiProduct pitem = new MultiProduct();
		// Boolean canCreate = productUpdateService.validationProduct(pitem);
		pitem.setCategoryIds(Lists.newArrayList(1));
		pitem.setCleanrstocks(false);
		pitem.setFeatured(false);
		pitem.setHot(false);
		pitem.setIsNew(true);
		// pitem.setMultiAttribute(false);
		pitem.setNewFromDate(new Date());
		Calendar c = Calendar.getInstance();
		c.setTime(pitem.getNewFromDate());
		c.add(c.MONTH, 2);
		pitem.setNewToDate(c.getTime());
		pitem.setParentSku("C00");
		pitem.setPrice(20.3);
		pitem.setQty(10);
		pitem.setSku("C001");
		pitem.setSpecial(false);
		pitem.setStatus(1);
		pitem.setVisible(true);
		pitem.setWebsiteId(1);
		pitem.setWeight(0.5);
		List<TranslateItem> list = new ArrayList<TranslateItem>();
		TranslateItem ptbase = new TranslateItem();
		ptbase.setDescription("<p>my desciprt</p>");
		ptbase.setKeyword("kool");
		ptbase.setLanguageId(1);
		ptbase.setMetaDescription("desc");
		ptbase.setMetaKeyword("kool");
		ptbase.setMetaTitle("m my firt test");
		ptbase.setShortDescription("my short");
		ptbase.setTitle("aaaaaaaaaaaaaaaaa test");
		List<String> sellingponts = Lists.newArrayList();
		sellingponts.add("my point!!1");
		sellingponts.add("my point!!2");
		ptbase.setSellingPoints(sellingponts);
		list.add(ptbase);
		pitem.setItems(list);

		List<ImageItem> imgitems = Lists.newArrayList();
		ImageItem imgitem = new ImageItem();
		imgitem.setBaseImage(true);
		imgitem.setImageUrl("http://img2.tomtop.com/media/catalog/product/cache/1/image/560x560/ced77cb19565515451b3578a3bc0ea5e/2/0/20100614_img_090307.jpg");
		imgitem.setLabel("label");
		imgitem.setOrder(1);
		imgitem.setSmallImage(true);
		imgitem.setThumbnail(true);
		imgitems.add(imgitem);
		pitem.setImages(imgitems);

		List<VideoItem> videoItems = Lists.newArrayList();
		VideoItem vitem = new VideoItem();
		vitem.setLabel("label");
		vitem.setVideoUrl("www.youtube.com/embed/2LPjoATwgQA?feature=player_detailpage");
		videoItems.add(vitem);
		pitem.setVideos(videoItems);

		List<AttributeItem> mlist = new ArrayList<AttributeItem>();
		AttributeItem aitem = new AttributeItem();
		aitem.setKeyId(1);
		aitem.setKeyId(1);
		mlist.add(aitem);
		pitem.setMultiAttributes(mlist);

		List<AttributeItem> palist = new ArrayList<AttributeItem>();
		AttributeItem maitem = new AttributeItem();
		maitem.setKeyId(1);
		maitem.setValueId(2);
		palist.add(maitem);
		pitem.setAttributes(palist);
		pitem.setStorages(Lists.newArrayList(1, 2));
		System.out.println(Json.toJson(pitem));
		String result = productUpdateService.createProduct(
				pitem, "lele");
		assertEquals(result, "");
	}

	private void testUpdateProduct() {

	}
}
