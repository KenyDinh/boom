package dev.boom.pages.milktea;

import java.util.List;

import org.apache.click.element.CssImport;

import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaSocketMessage;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.MenuInfo;
import dev.boom.services.MenuService;
import dev.boom.services.ShopInfo;
import dev.boom.services.ShopService;

public class MilkTeaListShop extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;
	
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
		headElements.add(importJs("/js/milktea/milktea-menu.js"));
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
		addModel("msg_id", MilkTeaSocketMessage.UPDATE_SHOP_LIST.getId());
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
		addModel("shop_list", MilkTeaCommonFunc.getHtmlListShop(getHostURL() + getContextPath(), getMessages(), page));
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
