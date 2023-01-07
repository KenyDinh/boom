package dev.boom.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.DeviceDept;
import dev.boom.common.enums.DeviceStatus;
import dev.boom.common.enums.DeviceType;
import dev.boom.common.enums.ManageLogType;
import dev.boom.core.GameLog;
import dev.boom.dao.core.DaoValue;
import dev.boom.socket.endpoint.DeviceEndPoint;
import dev.boom.tbl.info.TblDeviceInfo;
import dev.boom.tbl.info.TblDeviceRegisterInfo;

public class DeviceService {
	
	private DeviceService() {
	}
	
	public static final int DEVICE_IMAGE_WIDTH = 64;
	public static final int DEVICE_IMAGE_HEIGHT = 64;
	public static final String DATE_PATTERN = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	
	public static List<Device> listAllDevice() {
		return listAllDevice(null, -1, -1);
	}
	
	public static List<Device> listAllDevice(String option, int limit, int offset) {
		TblDeviceInfo info = new TblDeviceInfo();
		info.setSelectOption("WHERE id > 0 AND available <> 0");
		if (option != null && !option.isEmpty()) {
			info.setSelectOption(option);
		}
		info.setSelectOption("ORDER BY id ASC");
		if (limit > 0) {
			info.setLimit(limit);
			if (offset >= 0) {
				info.setOffset(offset);
			}
		}
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		List<Device> ret = new ArrayList<>();
		for (DaoValue dao : list) {
			ret.add(new Device((TblDeviceInfo) dao));
		}
		
		return ret;
	}

	public static Device getDeviceById(int id) {
		TblDeviceInfo info = new TblDeviceInfo();
		info.setId(id);
		info.setAvailable((byte)1);
		List<DaoValue> list = CommonDaoService.select(info);
		if (list == null || list.size() != 1) {
			return null;
		}
		return new Device((TblDeviceInfo) list.get(0));
	}

	public static boolean insertDevice(String name, String serial, String image, Date buyDate, String note, byte type, byte dep, int flag) {
		TblDeviceInfo deviceInfo = new TblDeviceInfo();
		deviceInfo.Set("name", name);
		deviceInfo.Set("serial", serial);
		deviceInfo.Set("image", image);
		deviceInfo.Set("type", type);
		deviceInfo.Set("buy_date", buyDate);
		deviceInfo.Set("note", note);
		deviceInfo.Set("dep", dep);
		deviceInfo.Set("flag", flag);
		deviceInfo.Set("available", (byte) 1);
		return (CommonDaoService.insert(deviceInfo) != null);
	}
	
	public static String doBorrowDevice(UserInfo user, Device device, String strStartDate, String strEndDate) {
		Map<String, String> returnData = doBorrowDevice(user, device, strStartDate, strEndDate, new HashMap<>());
		return returnData.get("msg");
	}
	
	public static Map<String, String> doBorrowDevice(UserInfo user, Device device, String strStartDate, String strEndDate, Map<String, String> returnData) {
		if (device == null) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! No game selected!");
			return returnData;
		}
		if (!(device.getStatus() == DeviceStatus.AVAILABLE.getStatus() || device.getStatus() == DeviceStatus.PENDING.getStatus())) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! Game is not available!");
			return returnData;
		}
		if (StringUtils.isBlank(strStartDate) || StringUtils.isBlank(strEndDate)) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! Start date and End date are required!");
			return returnData;
		}
		if (!strStartDate.matches(DATE_PATTERN) || !strEndDate.matches(DATE_PATTERN)) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! Invalid date time input!!");
			return returnData;
		}
		
		if (!user.isDeviceAdmin() && !user.isActive()) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! Your account is not active yet!");
			return returnData;
		}
		if (!user.isDeviceAdmin() && user.isDeviceBanned()) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! You are banned from checking out game!");
			return returnData;
		}
		strStartDate += " 00:00:00";
		strEndDate += " 00:00:00";
		Date startDate = CommonMethod.getDate(strStartDate);
		Date endDate = CommonMethod.getDate(strEndDate);
		if (startDate == null || endDate == null) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! Date input is invalid!");
			return returnData;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();
		if (startDate.before(today) || endDate.before(startDate)) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! Date input is invalid!");
			return returnData;
		}
		boolean request = false;
		if (!device.isEditable(user)) {
			request = true;
		}
		if (!borrowDevice(user, device, startDate, endDate)) {
			returnData.put("error", "1");
			returnData.put("msg", "Error! Checkout device failed!");
			return returnData;
		}
		if (request) {
			ManageLogService.createManageLog(user, ManageLogType.REQUEST_DEVICE, device.getName());
			returnData.put("msg", "Your request for borrowing game has been sent!");
		} else {
			ManageLogService.createManageLog(user, ManageLogType.CHECKOUT_DEVICE, device.getName());
			returnData.put("msg", "Checkout item successfully!");
		}
		DeviceEndPoint.sendMessageUpdate(user.getId());
		
		return returnData;
	}

	public static boolean borrowDevice(UserInfo user, Device device, Date startDate, Date endDate) {
		if (user == null) {
			GameLog.getInstance().error("[DeviceService][Borrow] user not found");
			return false;
		}
		if (device == null) {
			GameLog.getInstance().error("[DeviceService][Borrow] device not found");
			return false;
		}
		if (device.isEditable(user)) {
			device.setStatus(DeviceStatus.UNAVAILABLE.getStatus());
			device.setHoldDate(startDate);
			device.setReleaseDate(endDate);
			device.setUserId(user.getId());
			device.setUsername(user.getUsername());
			return CommonDaoService.update(device.getDeviceInfo());
		} else {
			List<DaoValue> updateList = new ArrayList<>();
			device.setStatus(DeviceStatus.PENDING.getStatus());
			device.incRegistCount();
			updateList.add(device.getDeviceInfo());
			DeviceRegister dr = DeviceRegisterService.getDeviceRegisterByUserId(device.getId(), user.getId());
			TblDeviceRegisterInfo registerInfo;
			if (dr != null) {
				if (!dr.isExpired()) {
					return false;
				}
				registerInfo = dr.getDeviceRegisterInfo();
			} else {
				registerInfo = new TblDeviceRegisterInfo();
				registerInfo.Set("user_id", user.getId());
				registerInfo.Set("username", user.getUsername());
			}
			registerInfo.Set("device_id", device.getId());
			registerInfo.Set("device_name", device.getName());
			registerInfo.Set("start_date", startDate);
			registerInfo.Set("end_date", endDate);
			registerInfo.Set("expired", (byte)0);
			registerInfo.Set("updated", new Date());
			updateList.add(registerInfo);
			return CommonDaoService.update(updateList);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static String getRenderDeviceTab(Map messages) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"nav nav-tabs\">");
		sb.append("<li class=\"nav-item active bg-info\" data-type=\"0\">");
		sb.append("<a href=\"javascript:void(0);\" class=\"nav-link\">All Games");
		sb.append(String.format("&nbsp;<span class=\"badge badge-primary\" id=\"device-count-0\"><span>"));
		sb.append("</a>");
		sb.append("</li>");
		
		for (DeviceType deviceType : DeviceType.values()) {
			if (!deviceType.isAvailable()) {
				continue;
			}
			sb.append("<li class=\"nav-item\" data-type=\"").append(deviceType.getType()).append("\">");
			sb.append("<a href=\"javascript:void(0);\" class=\"nav-link\" data-type=\"").append((String)messages.get(deviceType.getLabel()));
			sb.append("\">").append((String)messages.get(deviceType.getLabel()));
			sb.append(String.format("&nbsp;<span class=\"badge badge-primary\" id=\"device-count-%d\"><span>", deviceType.getType()));
			sb.append("</a>");
			sb.append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String getRenderTableDeviceList(UserInfo userInfo, List<Device> deviceList, Map messages, String contextPath) {
		List<Integer> deviceIds = new ArrayList<>();
		for (Device device : deviceList) {
			deviceIds.add(device.getId());
		}
		Map<Integer, List<DeviceRegister>> deviceRegisterMap = DeviceRegisterService.getRegisterListById(deviceIds);
		Date now = new Date();
		StringBuilder sb = new StringBuilder();
		StringBuilder sbModal = new StringBuilder();
		Map<Byte, Integer> typeCount = new HashMap<>();
		for (DeviceType deviceType : DeviceType.values()) {
			if (!deviceType.isAvailable()) {
				continue;
			}
			typeCount.put(deviceType.getType(), 0);
		}
		sb.append("<table id=\"device-table\" class=\"table table-stripped table-hover\" style=\"width:100%;\">");
		sb.append("<thead>");
		sb.append("<tr role=\"row\" class=\"text-success\">");
		sb.append("<th style=\"width:20%;\">").append("Name").append("</th>");
		sb.append("<th class=\"d-none\">").append("Dep").append("</th>");
		sb.append("<th style=\"max-width:120px;\">").append("Platform").append("</th>");
		sb.append("<th style=\"max-width:120px;\">").append("Buy Date").append("</th>");
		sb.append("<th style=\"max-width:120px;\">").append("Status").append("</th>");
		sb.append("<th style=\"max-width:120px;\">").append("Start Date").append("</th>");
		sb.append("<th style=\"max-width:120px;\">").append("Due Date").append("</th>");
		sb.append("<th style=\"max-width:150px;\">").append("User").append("</th>");
		sb.append("<th style=\"max-width:120px;\">").append("").append("</th>");
		if (userInfo.isDeviceAdmin()) {
			sb.append("<th style=\"max-width:120px;\">").append("").append("</th>");
		}
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		for (Device device : deviceList) {
			//_/_/_/ modal _/_/_///
			int registCount = 0;
			boolean hasBorrow = false;
			boolean hasRegistModal = (deviceRegisterMap != null && deviceRegisterMap.containsKey(device.getId()));
			if (device.getStatus() == DeviceStatus.PENDING.getStatus()) {
				if (hasRegistModal) {
					sbModal.append(String.format("<div class='modal fade' id='register-list-%d'>", device.getId()));
						sbModal.append("<div class='modal-dialog modal-dialog-centered modal-md'>");
							sbModal.append("<div class='modal-content'>");
								sbModal.append("<div class='modal-header bg-info'>");
								sbModal.append("<h4 class='modal-title'>").append("Response").append("</h4>");
								sbModal.append("<button type='button' class='close' data-dismiss='modal'>&times;</button>");
								sbModal.append("</div>");
								
								sbModal.append("<div class='modal-body'>");
								sbModal.append("<div class='text-center form-group'>List users who registered for item <span class='text-info font-weight-bold'>");
								sbModal.append(device.getName()).append("</span>").append("</div>");
								sbModal.append("<table class='table table-stripped table-hover nowrap'>");
								sbModal.append("<thead>");
								sbModal.append("<tr role='row' class='text-info'>");
									sbModal.append("<th>").append("Username").append("</th>");
										sbModal.append("<th>").append("From").append("</th>");
										sbModal.append("<th>").append("To").append("</th>");
										sbModal.append("<th>").append("").append("</th>");
										sbModal.append("</tr>");
								sbModal.append("</thead>");
								sbModal.append("<tbody>");
								List<DeviceRegister> registerList = deviceRegisterMap.get(device.getId());
								registCount = registerList.size();
								for (DeviceRegister dr : registerList) {
									if (dr.getUser_id() == userInfo.getId()) {
										hasBorrow = true;
									}
									sbModal.append("<tr role='row' class=''>");
									sbModal.append("<td>").append(dr.getUsername()).append("</td>");
									sbModal.append("<td>").append(CommonMethod.getFormatDateWithoutTimeString(dr.getStartDate())).append("</td>");
									sbModal.append("<td>").append(CommonMethod.getFormatDateWithoutTimeString(dr.getEndDate())).append("</td>");
									sbModal.append("<td>").append(String.format("<button class='btn btn-danger dv-action device-respond' data-id='%d' data-rgid='%d' data-dname='%s' type='button'>Response</button>", device.getId(), dr.getId(), device.getName())).append("</td>");
									sbModal.append("</tr>");
								}
								sbModal.append("</tbody>");
								sbModal.append("</table>");
								sbModal.append("</div>");
							sbModal.append("</div>");
						sbModal.append("</div>");
					sbModal.append("</div>");
				} else {
					GameLog.getInstance().error("[DeviceService] Not found register list for device id : " + device.getId());
				}
			}
			//_/_/_/ modal end _/_/_///
			
			byte deviceType = device.getType();
			if (typeCount.containsKey(deviceType)) {
				typeCount.put(device.getType(), typeCount.get(deviceType) + 1);
			} else {
				typeCount.put(device.getType(), 1);
			}
			sb.append("<tr role=\"row\">");
			sb.append("<td>");
				if (StringUtils.isBlank(device.getImage())) {
					sb.append(String.format("<img class=\"float-left device-image\" src=\"%s\" />", contextPath + "/img/tools/device/game.png"));
				} else {
					sb.append(String.format("<img class=\"float-left device-image\" src=\"%s\" />", CommonMethod.getStaticFile(device.getImage())));
				}
				sb.append("<div>").append(device.getName()).append(String.format("<i class=\"dv-more-info text-info fas fa-info-circle\" style=\"cursor:help;margin-left:2px;\" data-toggle=\"popover\" data-trigger=\"hover\"></i></div>", device.getId()));
				if (StringUtils.isNotBlank(device.getSerial())) {
					sb.append("<div class=\"font-italic text-info\" style=\"max-width:12.5rem;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;font-size:0.75rem;\" title=\"Serial : ");
					sb.append(device.getSerial()).append("\">").append(device.getSerial()).append("</div>");
					
				}
			sb.append("</td>");
			sb.append("<td class=\"d-none\">");
			sb.append(String.format("<div>%s</div>", DeviceDept.valueOf(device.getDept()).getLabel()));
			sb.append(String.format("<div>%s</div>", device.getFormatNote()));
			sb.append("</td>");
			sb.append("<td>").append((String)messages.get(DeviceType.valueOf(deviceType).getLabel())).append("</td>");
			sb.append("<td>").append(CommonMethod.getFormatDateWithoutTimeString(device.getBuyDate())).append("</td>");
			sb.append("<td>").append((String)messages.get(DeviceStatus.valueOf(device.getStatus()).getLabel())).append("</td>");

			if (!device.isShowDate() || device.getHoldDate().getTime() == CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME).getTime()) {
				sb.append("<td>").append("---").append("</td>");
			} else {
				sb.append("<td>").append(CommonMethod.getFormatDateWithoutTimeString(device.getHoldDate())).append("</td>");
			}
			if (!device.isShowDate() || device.getReleaseDate().getTime() == CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME).getTime()) {
				sb.append("<td>").append("---").append("</td>");
			} else {
				sb.append("<td>");
				if (device.getStatus() == DeviceStatus.CHANGE_PENDING.getStatus()) {
					sb.append(String.format("<div class=\"%s\" style=\"text-decoration:line-through;font-style:italic;\">%s</div>", (device.getReleaseDate().before(now) ? "text-danger font-weight-bold" : ""), CommonMethod.getFormatDateWithoutTimeString(device.getReleaseDate())));
					sb.append(String.format("<div class=\"%s\">%s</div>", (device.getExtendDate().before(now) ? "text-danger font-weight-bold" : ""), CommonMethod.getFormatDateWithoutTimeString(device.getExtendDate())));
				} else {
					if (device.getUserId() == userInfo.getId() || userInfo.isDeviceAdmin()) {
						sb.append(String.format("<div class=\"%s\" style=\"position:relative;\"><span class=\"dv-update none-select\" style=\"cursor:pointer;\" data-id=\"%d\">%s</span><i class=\"fas fa-question-circle\" style=\"position: absolute;top: 0;margin-left: 2px;color: #dee2e6;\" data-toggle=\"popover\" data-trigger=\"hover\" data-pop=\"Double click on the date to change it\"></i></div>", (device.getReleaseDate().before(now) ? "text-danger font-weight-bold" : ""), device.getId(), CommonMethod.getFormatDateWithoutTimeString(device.getReleaseDate())));
					} else {
						sb.append(String.format("<div class=\"%s\">%s</div>", (device.getReleaseDate().before(now) ? "text-danger font-weight-bold" : ""), CommonMethod.getFormatDateWithoutTimeString(device.getReleaseDate())));
					}
				}
				sb.append("</td>");
			}
			if (device.getStatus() == DeviceStatus.AVAILABLE.getStatus()) {
				sb.append("<td>---</td>");
			} else {
				sb.append("<td>").append(device.getUsername()).append("</td>");
			}
			if (device.getStatus() == DeviceStatus.AVAILABLE.getStatus()) {
				if (userInfo.isActive()) {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-primary device-check-out\" data-id=\"%d\">Checkout</button>", device.getId())).append("</td>");
				} else {
					sb.append("<td><span class=\"text-warning\">Active account!</span></td>");
				}
			} else if (device.getStatus() == DeviceStatus.PENDING.getStatus()) {
				if (userInfo.isDeviceAdmin()) {
					if (hasRegistModal) {
						sb.append("<td>").append(String.format("<button class=\"btn btn-danger\" data-toggle='modal' data-target='#register-list-%d'>Response", device.getId()));
						if (registCount > 0) {
							sb.append(String.format(" <span class='badge badge-primary'>%d</span>", registCount));
						}
						sb.append("</button></td>");
					} else {
						sb.append("<td>").append("").append("</td>");
					}
				} else if (hasBorrow) {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-danger device-cancel\" data-id=\"%d\">Cancel</button>", device.getId())).append("</td>");
				} else {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-primary device-check-out\" data-id=\"%d\">Checkout</button>", device.getId())).append("</td>");
				}
			} else if (device.getStatus() == DeviceStatus.RETURN_PENDING.getStatus()) {
				if (userInfo.isDeviceAdmin()) {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-danger device-confirm\" data-id=\"%d\" rq-id=\"%d\">Confirm</button>", device.getId(), device.getUserId())).append("</td>");
				} else if (device.getUserId() == userInfo.getId()) {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-danger device-cancel\" data-id=\"%d\">Cancel</button>", device.getId())).append("</td>");
				} else {
					sb.append("<td>").append("").append("</td>");
				}
			} else if (device.getStatus() == DeviceStatus.CHANGE_PENDING.getStatus()) {
				if (userInfo.isDeviceAdmin()) {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-danger device-confirm-change\" data-id=\"%d\" rq-id=\"%d\">Confirm</button>", device.getId(), device.getUserId())).append("</td>");
				} else if (device.getUserId() == userInfo.getId()) {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-danger device-cancel\" data-id=\"%d\">Cancel</button>", device.getId())).append("</td>");
				} else {
					sb.append("<td>").append("").append("</td>");
				}
			} else if (device.getStatus() == DeviceStatus.UNAVAILABLE.getStatus()) {
				if (device.getUserId() == userInfo.getId() || userInfo.isDeviceAdmin()) {
					sb.append("<td>").append(String.format("<button class=\"dv-action btn btn-secondary device-return\" data-id=\"%d\">Return</button>", device.getId())).append("</td>");
				} else {
					sb.append("<td></td>");
				}
			} else {
				sb.append("<td></td>");
			}
			if (userInfo.isDeviceAdmin()) {
				sb.append("<td>").append(String.format("<button class=\"btn btn-primary device-edit\" data-id=\"%d\" data-flag=\"%d\" data-dept=\"%d\" style=\"width:6rem;\">Edit</button>", device.getId(), device.getFlag(), device.getDept())).append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("<div class=\"d-none\">");
		sb.append(String.format("<span data-type=\"0\" class=\"device-count\" >%d</span>", deviceList.size()));
		for (byte type : typeCount.keySet()) {
			sb.append(String.format("<span data-type=\"%d\" class=\"device-count\" >%d</span>", type, typeCount.get(type)));
		}
		sb.append("</div>");
		sb.append(sbModal);
		return sb.toString();
	}
	
	// patpat command
	public static String getRenderTableDevice(List<Device> deviceList) {
		if (deviceList == null || deviceList.isEmpty()) {
			return "No item found!";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("|#ID|#Name|#Type|#Department|#Serial|#Borrower|#Status|#BorrowDate|#ReturnDate|");
		sb.append("\n");
		sb.append("|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|");
		sb.append("\n");
		String username;
		for(Device device : deviceList) {
			sb.append("|").append(device.getId()).append("|").append(device.getName()).append("|").append(DeviceType.valueOf(device.getType()).name()).append("|").append(DeviceDept.valueOf(device.getDept()).getLabel()).append("|").append(device.getSerial()).append("|");
			username = device.getUsername();
			if (StringUtils.isBlank(username)) {
				sb.append("---");
			} else {
				sb.append(device.getUsername());
			}
			sb.append("|").append(DeviceStatus.valueOf(device.getStatus()).getMsg()).append("|");
			if (device.isShowDate() && device.getReleaseDate().getTime() != CommonMethod.getDate(CommonDefine.DEFAULT_DATE_TIME).getTime()) {
				sb.append(CommonMethod.getFormatDateWithoutTimeString(device.getHoldDate())).append("|");
				if (device.getStatus() == DeviceStatus.CHANGE_PENDING.getStatus()) {
					sb.append(CommonMethod.getFormatDateWithoutTimeString(device.getExtendDate())).append("|");
				} else {
					sb.append(CommonMethod.getFormatDateWithoutTimeString(device.getReleaseDate())).append("|");
				}
			} else {
				sb.append("---").append("|");
				sb.append("---").append("|");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	public static String getRenderTableRegister(List<DeviceRegister> registerList) {
		if (registerList == null || registerList.isEmpty()) {
			return "No borrow request found for this device!";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("|#Name|#BorrowDate|#ReturnDate|");
		sb.append("\n");
		sb.append("|:---:|:---:|:---:|");
		sb.append("\n");
		for (DeviceRegister dr : registerList) {
			sb.append("|").append(dr.getUsername()).append("|").append(CommonMethod.getFormatDateWithoutTimeString(dr.getStartDate())).append("|").append(CommonMethod.getFormatDateWithoutTimeString(dr.getEndDate())).append("|");
		}
		
		return sb.toString();
	}
}
