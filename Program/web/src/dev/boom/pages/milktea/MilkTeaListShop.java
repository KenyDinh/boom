package dev.boom.pages.milktea;

import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.CommonDaoService;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.ShopInfo;
import dev.boom.services.ShopService;
import dev.boom.tbl.info.TblShopInfo;

public class MilkTeaListShop extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;
	
	private static final int MAX_VIEW_PER_PAGE = 100;
	private int page = 1;
	private ShopInfo shopInfo = null;

	public MilkTeaListShop() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
		}
		headElements.add(new JsImport("/js/milktea/milktea-menu.js"));
		headElements.add(new CssImport("/css/milktea/milktea-menu.css"));
		return headElements;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strPage = getContext().getRequestParameter("page");
		if (strPage != null && CommonMethod.isValidNumeric(strPage, 1, Integer.MAX_VALUE)) {
			page = Integer.parseInt(strPage);
		}
		String strId = getContext().getRequestParameter("id");
		if (strId != null && CommonMethod.isValidNumeric(strId, 1, Long.MAX_VALUE)) {
			shopInfo = ShopService.getShopById(Long.parseLong(strId));
		}
	}

	@Override
	public void onRender() {
		super.onRender();
		if (shopInfo != null) {
			initShopDetail();
		} else {
			initListShop();
		}
	}
	
	private void initShopDetail() {
		String contextPath = getHostURL() + getContextPath();
		List<MenuItem> listMenuItem = MenuService.getMenuItemListByShopId(shopInfo.getId());
		if (listMenuItem != null) {
			MenuInfo menuInfo = new MenuInfo();
			menuInfo.setExpired(menuInfo.getCreated());
			menuInfo.setShopId(shopInfo.getId());
			addModel("dish_list", MilkTeaCommonFunc.getHtmlListMenuItem(menuInfo, listMenuItem, contextPath, getUserInfo(), getMessages()));
			addModel("dish_type", MilkTeaCommonFunc.getHtmlListMenuItemType(listMenuItem, contextPath));
		}
		List<MenuInfo> menuList = MenuService.getMenuListByShopId(shopInfo.getId());
		if (menuList != null) {
			addModel("menu_list", MilkTeaCommonFunc.getHtmlListMenuOnShop(menuList, contextPath, getMessages()));
		}
		addModel("shopInfo", shopInfo);
	}
	
	private void initListShop() {
		int total = (int)CommonDaoService.count(new TblShopInfo());
		if (total <= 0) {
			return;
		}
		int maxPage = (total - 1) / MAX_VIEW_PER_PAGE + 1;
		if (page > maxPage) {
			page = maxPage;
		}
		int offset = (page - 1) * MAX_VIEW_PER_PAGE;
		List<ShopInfo> shopList = ShopService.getShopList("ORDER BY opening_count DESC", MAX_VIEW_PER_PAGE, offset);
		if (shopList == null || shopList.isEmpty()) {
			return;
		}
		addModel("shop_list", MilkTeaCommonFunc.getHtmlListShop(shopList, getHostURL() + getContextPath(), getMessages()));
	}
	
	@Override
	protected String initMilkTeaIntro() {
		if (shopInfo != null) {
			return MilkTeaCommonFunc.getHtmlShopDetail(shopInfo, getUserInfo(), getHostURL() + getContextPath(), getMessages());
		} 
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"col-lg-12\">");
		sb.append("<div style=\"height:100%;");
			sb.append("background-image:");
				sb.append("url('" + getContextPath() + "/img/milktea/ranking_banner_c.png');");
			sb.append("background-repeat:repeat-x;");
		sb.append("\">");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.LIST_SHOP.getIndex();
	}
	
}
