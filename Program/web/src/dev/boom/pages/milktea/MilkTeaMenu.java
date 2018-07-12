package dev.boom.pages.milktea;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.milktea.MilkTeaCommonFunc;
import dev.boom.common.milktea.MilkTeaMenuStatus;
import dev.boom.common.milktea.MilkTeaTabEnum;
import dev.boom.entity.info.MenuInfo;
import dev.boom.entity.info.OrderInfo;
import dev.boom.entity.info.ShopInfo;
import dev.boom.entity.info.ShopOptionInfo;
import dev.boom.milktea.object.MenuItem;
import dev.boom.services.MenuService;
import dev.boom.services.OrderService;
import dev.boom.services.ShopService;

public class MilkTeaMenu extends MilkTeaMainPage {

	private static final long serialVersionUID = 1L;
	
	private MenuInfo menuInfo;

	public MilkTeaMenu() {
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
		String strMenuId = getContext().getRequestParameter("id");
		if (CommonMethod.isValidNumeric(strMenuId, 1, Long.MAX_VALUE)) {
			menuInfo = MenuService.getMenuById(Long.parseLong(strMenuId));
		}
	}



	@Override
	public void onRender() {
		super.onRender();
		String contextPath = getHostURL() + getContextPath();
		if (menuInfo != null) {
			List<MenuItem> listMenuItem = MenuService.getMenuItemListByShopId(menuInfo.getShop_id());
			if (listMenuItem != null) {
				boolean hasLogin = (boolean)(getUserInfo() != null);
				List<ShopOptionInfo> listShopOption = ShopService.getShopOptionListByShopId(menuInfo.getShop_id());
				addModel("dish_list", MilkTeaCommonFunc.getHtmlListMenuItem(menuInfo, listMenuItem, listShopOption, contextPath, hasLogin, messages));
				addModel("dish_type", MilkTeaCommonFunc.getHtmlListMenuItemType(listMenuItem, contextPath));
				List<OrderInfo> listOrder = OrderService.getOrderInfoListByMenuId(menuInfo.getId());
				addModel("order_list", MilkTeaCommonFunc.getHtmlListOrder(listOrder, menuInfo, getUserInfo(), listShopOption, contextPath, messages));
			}
			addModel("menuInfo", menuInfo);
		} else {
			List<ShopInfo> listShopInfo = null;
			List<MenuInfo> listOpeningMenu = MenuService.getMenuListByStatus((byte)MilkTeaMenuStatus.OPENING.ordinal());
			if (listOpeningMenu != null) {
				addModel("listOpeningMenu", MilkTeaCommonFunc.getHtmlListMenu(listOpeningMenu, listShopInfo, messages, contextPath));
			}
			List<MenuInfo> listDeliveringMenu = MenuService.getMenuListByStatus((byte)MilkTeaMenuStatus.DELIVERING.ordinal());
			if (listDeliveringMenu != null) {
				addModel("listDeliveringMenu", MilkTeaCommonFunc.getHtmlListMenu(listDeliveringMenu, listShopInfo, messages, contextPath));
			}
			List<MenuInfo> listCompletedMenu = MenuService.getMenuListByStatus((byte)MilkTeaMenuStatus.COMPLETED.ordinal());
			if (listCompletedMenu != null) {
				addModel("listCompletedMenu", MilkTeaCommonFunc.getHtmlListMenu(listCompletedMenu, listShopInfo, messages, contextPath));
			}
		}
	}
	
	@Override
	protected String initMilkTeaIntro() {
		StringBuilder sb = new StringBuilder();
		if (menuInfo != null) {
			ShopInfo shopInfo = ShopService.getShopById(menuInfo.getShop_id());
			if (shopInfo != null) {
				sb.append("<div class=\"col-lg-3 col-md-6\" style=\"\">");
				String url = shopInfo.getImage_url();
				if (url != null && url.indexOf("youtube") >= 0) {
					sb.append(String.format("<iframe style=\"%s\" src=\"%s\"></iframe>", "width:100%;height:15.625rem;", url));
				} else if (url != null && url.startsWith("http")) {
					sb.append(String.format("<img src=\"%s\" style=\"%s\"/>", url, "width:100%;max-height:15.625rem;"));
				} else {
					sb.append(String.format("<img src=\"%s\" style=\"%s\" />", getHostURL() + getContextPath() + MilkTeaCommonFunc.getMenuCommonImage(), "width:100%;max-height:15.625rem;"));
				}
				sb.append("</div>");
				sb.append("<div class=\"col-lg-9 col-md-6\">");
					sb.append("<h5 class=\"font-weight-bol text-info\" style=\"margin-top:1rem;\">").append(menuInfo.getName()).append("</h5>");
					sb.append("<div class=\"font-italic\" style=\"font-size:0.875rem;margin-bottom:0.5rem;\">").append(shopInfo.getAddress()).append("</div>");
					sb.append("<div class=\"rating\">");
						double rating = 0.0;
						if (shopInfo.getVoting_count() > 0) {
							rating = (double)shopInfo.getStar_count() / shopInfo.getVoting_count();
							rating = ((double)Math.round(rating * 10) / 10);
						}
						sb.append("<div class=\"stars\">");
							for (int i = 0; i < CommonDefine.MAX_MILKTEA_VOTING_STAR; i++) {
								double gap = rating - i;
								if (gap >= 0.3) {
									if (gap <= 0.8 || (gap < 1.0 && i == CommonDefine.MAX_MILKTEA_VOTING_STAR - 1)) {
										sb.append("<span class=\"half\"></span>");
									} else {
										sb.append("<span class=\"full\"></span>");
									}
								} else {
									sb.append("<span class=\"empty\"></span>");
								}
							}
						sb.append("</div>");
						sb.append("<span class=\"text-info font-italic\" style=\"margin-left:0.4rem;\">").append(MessageFormat.format(getMessage("MSG_MILK_TEA_SHOP_RATING_S"), shopInfo.getStar_count(), shopInfo.getVoting_count())).append("</span>");
						if (shopInfo.getOpening_count() > 0) {
							sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append(MessageFormat.format(getMessage("MSG_MILK_TEA_SHOP_INFO_STATISTIC"), shopInfo.getOpening_count(), shopInfo.getOrdered_dish_count())).append("</div>");
						} else {
							sb.append("<div class=\"text-success\" style=\"margin-bottom:0.5rem;font-size:0.875rem;\">").append(getMessage("MSG_MILK_TEA_SHOP_INFO_FIRST_TIME_OPEN")).append("</div>");
						}
						sb.append("<div style=\"margin-bottom:0.5rem;\">");
							sb.append("<span>");
								sb.append(MessageFormat.format(getMessage("MSG_MILK_TEA_MENU_INFO_SALE_RATE"), menuInfo.getSale()));
							sb.append("</span>");
							sb.append("<span style=\"margin-left:1rem;\">");
								sb.append(MessageFormat.format(getMessage("MSG_MILK_TEA_MENU_INFO_SHIPPING_FEE"), MilkTeaCommonFunc.getShowPriceWithUnit(menuInfo.getShipping_fee(), "", messages)));
							sb.append("</span>");
						sb.append("</div>");
					sb.append("</div>");
				sb.append("</div>");
			}
		}
		return sb.toString();
	}

	@Override
	protected int getMilkTeaTabIndex() {
		return MilkTeaTabEnum.OPENING_MENU.getIndex();
	}

}
