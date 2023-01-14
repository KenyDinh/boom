package dev.boom.pages.tools.sub;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import dev.boom.common.CommonDefine;
import dev.boom.common.CommonMethod;
import dev.boom.common.enums.DeviceDept;
import dev.boom.common.enums.DeviceFlag;
import dev.boom.common.enums.DeviceStatus;
import dev.boom.common.enums.DeviceType;
import dev.boom.common.enums.ManageLogType;
import dev.boom.core.GameLog;
import dev.boom.dao.CommonDaoFactory;
import dev.boom.dao.DaoValue;
import dev.boom.pages.JsonPageBase;
import dev.boom.services.Device;
import dev.boom.services.DeviceRegister;
import dev.boom.services.DeviceRegisterService;
import dev.boom.services.DeviceService;
import dev.boom.services.ManageLogService;
import dev.boom.services.User;
import dev.boom.services.UserService;
import dev.boom.socket.endpoint.DeviceEndPoint;
import dev.boom.tbl.info.TblDeviceInfo;

public class DeviceConfirm extends JsonPageBase {

	private static final long serialVersionUID = 1L;
	private static final String DATE_PATTERN = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private static final String MODE_ADD = "add";
	private static final String MODE_EDIT = "edit";
	private static final String MODE_CHECKOUT = "checkout";
	private static final String MODE_ACCEPT = "accept";
	private static final String MODE_DECLINE = "decline";
	private static final String MODE_RETURN = "return";
	private static final String MODE_CANCEL = "cancel";
	private static final String MODE_REMOVE = "remove";
	private static final String MODE_UPDATE = "update";
	private static final String MODE_IMPORT = "import";

	private String mode = null;
	private String responseMsg = null;
	private boolean error = false;
	private Device device = null;
	private DeviceRegister deviceRegister = null;

	@Override
	public boolean onSecurityCheck() {
		if (!super.onSecurityCheck()) {
			return false;
		}
		if (!initUserInfo()) {
			return false;
		}
		return true;
	}

	@Override
	public void onInit() {
		super.onInit();
		String strMode = getContext().getRequestParameter("mode");
		if (StringUtils.isNotBlank(strMode)) {
			mode = strMode;
		}
		String strDeviceId = getContext().getRequestParameter("deviceid");
		if (CommonMethod.isValidNumeric(strDeviceId, 1, Integer.MAX_VALUE)) {
			device = DeviceService.getDeviceById(Integer.parseInt(strDeviceId));
		}
		String strRgid = getContext().getRequestParameter("rgid");
		if (CommonMethod.isValidNumeric(strRgid, 1, Long.MAX_VALUE)) {
			deviceRegister = DeviceRegisterService.getDeviceRegisterById(Long.parseLong(strRgid));
		}
	}

	@Override
	public void onPost() {
		super.onPost();
		if (error) {
			return;
		}
		switch (mode) {
		case MODE_ADD:
			doAddDevice();
			break;
		case MODE_EDIT:
			doEditDevice();
			break;
		case MODE_CHECKOUT:
			doBorrowDevice();
			break;
		case MODE_UPDATE:
			doAdjustDate();
			break;
		case MODE_IMPORT:
			doImportItem();
			break;
		default:
			doUpdateStatus();
			break;
		}
	}

	@Override
	public void onRender() {
		if (error) {
			putJsonData("error", 1);
		}
		if (responseMsg != null) {
			putJsonData("response", responseMsg);
		}
		super.onRender();
	}

	private void doAddDevice() {
		if (!getUserInfo().isDeviceAdmin()) {
			error = true;
			responseMsg = "Error! No permission!";
			return;
		}
		String strName = getContext().getRequestParameter("devicename");
		String strSerial = getContext().getRequestParameter("deviceserial");
		String strBuyDate = getContext().getRequestParameter("buydate");
		String strType = getContext().getRequestParameter("devicetype");
		String strDep = getContext().getRequestParameter("devicedep");
		String strFlag = getContext().getRequestParameter("deviceflag");
		String strNote = getContext().getRequestParameter("devicenote");
		if (strName == null || strName.isEmpty()) {
			error = true;
			responseMsg = "Error! Invalid item name!";
			return;
		}
		if (StringUtils.isBlank(strSerial)) {
			strSerial = "";
		}
		if (StringUtils.isBlank(strNote)) {
			strNote = "";
		}
		if (StringUtils.isBlank(strBuyDate)) {
			strBuyDate = CommonDefine.DEFAULT_DATE_TIME;
		} else {
			if (!strBuyDate.matches(DeviceService.DATE_PATTERN)) {
				error = true;
				responseMsg = "Error! Invalid item buy date!";
				return;
			} else {
				strBuyDate = String.format("%s 00:00:00", strBuyDate);
			}
		}
		Date buyDate = CommonMethod.getDate(strBuyDate);
		if (buyDate == null) {
			error = true;
			responseMsg = "Error! Invalid item buy date!";
			return;
		}
		
		if (!CommonMethod.isValidNumeric(strType, DeviceType.NONE.getType(), DeviceType.XBOX.getType())) {
			error = true;
			responseMsg = "Error! Invalid item type!";
			return;
		}
		if (!CommonMethod.isValidNumeric(strDep, 0, Byte.MAX_VALUE)) {
			error = true;
			responseMsg = "Error! Invalid item dep!";
			return;
		}
		if (!CommonMethod.isValidNumeric(strFlag, 0, Integer.MAX_VALUE)) {
			error = true;
			responseMsg = "Error! Invalid item flag!";
			return;
		}
		DeviceType type = DeviceType.valueOf(Byte.parseByte(strType));
		if (!type.isAvailable()) {
			error = true;
			responseMsg = "Error! Invalid parameter!";
			return;
		}
		String deviceImage = "";
		FileItem image = getContext().getFileItem("deviceimg");
		if (image != null) {
			String imgName = image.getName();
			if (StringUtils.isNotBlank(imgName)) {
				GameLog.getInstance().info("[ManageVote] getImage: " + image.getName());
				String imgExt = FilenameUtils.getExtension(imgName);
				deviceImage = RandomStringUtils.randomAlphanumeric(10) + "." + imgExt;
				File newFile = new File(getFileUploadDir(), deviceImage);
				if (CommonMethod.isImageFileAllowed(imgExt)) {
					try {
						BufferedImage bufferImage = CommonMethod.convertImage(image.getInputStream(), DeviceService.DEVICE_IMAGE_WIDTH * 2, DeviceService.DEVICE_IMAGE_HEIGHT * 2);
						ImageIO.write(bufferImage, imgExt, newFile);
					} catch (IOException e) {
						e.printStackTrace();
						deviceImage = "";
					}
				}
			}
		}
		if (!DeviceService.insertDevice(strName.trim(), strSerial.trim(), deviceImage, buyDate, strNote.trim(), type.getType(), DeviceDept.valueOf(Byte.parseByte(strDep)).getDep(), Integer.parseInt(strFlag))) {
			error = true;
			responseMsg = "Error! Create new item failed!";
			return;
		}
		ManageLogService.createManageLog(getUserInfo(), ManageLogType.ADD_DEVICE, strName);
		responseMsg = "Succeed! Added new item [ " + strName + " ]";
		DeviceEndPoint.sendMessageUpdate(userInfo.getId());
	}

	private void doEditDevice() {
		if (!getUserInfo().isDeviceAdmin()) {
			error = true;
			responseMsg = "Error! No permission!";
			return;
		}
		String strId = getContext().getRequestParameter("id");
		if (!CommonMethod.isValidNumeric(strId, 1, Integer.MAX_VALUE)) {
			error = true;
			responseMsg = "Error! No Item selected!";
			return;
		}
		Device device = DeviceService.getDeviceById(Integer.parseInt(strId));
		if (device == null) {
			error = true;
			responseMsg = "Error! Item not found!";
			return;
		}
		boolean update = false;
		String strName = getContext().getRequestParameter("devicename");
		String strSerial = getContext().getRequestParameter("deviceserial");
		String strBuyDate = getContext().getRequestParameter("buydate");
		String strType = getContext().getRequestParameter("devicetype");
		String strDep = getContext().getRequestParameter("devicedep");
		String strFlag = getContext().getRequestParameter("deviceflag");
		String strNote = getContext().getRequestParameter("devicenote");
		if (strName == null || strName.isEmpty()) {
			error = true;
			responseMsg = "Error! Invalid item name!";
			return;
		}
		strName = strName.trim();
		update = !update ? !(device.getName().equals(strName)) : update;
		if (StringUtils.isBlank(strSerial)) {
			strSerial = "";
		}
		strSerial = strSerial.trim();
		update = !update ? !(device.getSerial().equals(strSerial)) : update;
		if (StringUtils.isBlank(strNote)) {
			strNote = "";
		}
		strNote = strNote.trim();
		update = !update ? !(device.getNote().equals(strNote)) : update;
		if (StringUtils.isBlank(strBuyDate)) {
			strBuyDate = CommonDefine.DEFAULT_DATE_TIME;
		} else {
			if (!strBuyDate.matches(DeviceService.DATE_PATTERN)) {
				error = true;
				responseMsg = "Error! Invalid item buy date!";
				return;
			} else {
				strBuyDate = String.format("%s 00:00:00", strBuyDate);
			}
		}
		Date buyDate = CommonMethod.getDate(strBuyDate);
		if (buyDate == null) {
			error = true;
			responseMsg = "Error! Invalid item buy date!";
			return;
		}
		update = !update ? !(device.getBuyDateDate().getTime() == buyDate.getTime()) : update;
		if (!CommonMethod.isValidNumeric(strType, DeviceType.NONE.getType(), DeviceType.XBOX.getType())) {
			error = true;
			responseMsg = "Error! Invalid item type!";
			return;
		}
		if (!CommonMethod.isValidNumeric(strDep, 0, Byte.MAX_VALUE)) {
			error = true;
			responseMsg = "Error! Invalid item dep!";
			return;
		}
		if (!CommonMethod.isValidNumeric(strFlag, 0, Integer.MAX_VALUE)) {
			error = true;
			responseMsg = "Error! Invalid item flag!";
			return;
		}
		DeviceType type = DeviceType.valueOf(Byte.parseByte(strType));
		if (!type.isAvailable()) {
			error = true;
			responseMsg = "Error! Invalid parameter!";
			return;
		}
		DeviceDept dept = DeviceDept.valueOf(Byte.parseByte(strDep));
		int flag = Integer.parseInt(strFlag);
		update = !update ? !(device.getType() == (type.getType())) : update;
		update = !update ? !(device.getDept() == (dept.getDep())) : update;
		update = !update ? !(device.getFlag() == flag) : update;
		String deviceImage = "";
		FileItem image = getContext().getFileItem("deviceimg");
		if (image != null) {
			String imgName = image.getName();
			if (StringUtils.isNotBlank(imgName)) {
				GameLog.getInstance().info("[ManageVote] getImage: " + image.getName());
				String imgExt = FilenameUtils.getExtension(imgName);
				deviceImage = RandomStringUtils.randomAlphanumeric(10) + "." + imgExt;
				File newFile = new File(getFileUploadDir(), deviceImage);
				if (CommonMethod.isImageFileAllowed(imgExt)) {
					try {
						BufferedImage bufferImage = CommonMethod.convertImage(image.getInputStream(), DeviceService.DEVICE_IMAGE_WIDTH * 2, DeviceService.DEVICE_IMAGE_HEIGHT * 2);
						ImageIO.write(bufferImage, imgExt, newFile);
					} catch (IOException e) {
						e.printStackTrace();
						deviceImage = "";
					}
				}
			}
		}
		String oldImage = null;
		if (StringUtils.isNotBlank(deviceImage) && !(device.getImage().equals(deviceImage))) {
			oldImage = device.getImage();
			update = true;
		}
		if (update) {
			device.setName(strName);
			device.setSerial(strSerial);
			device.setBuyDate(CommonMethod.getFormatDateString(buyDate));
			device.setNote(strNote);
			device.setType(type.getType());
			device.setDept(dept.getDep());
			device.setFlag(flag);
			if (oldImage != null) {
				device.setImage(deviceImage);
			}
			if (CommonDaoFactory.Update(device.getDeviceInfo()) < 0) {
				error = true;
				responseMsg = "Error! Edit item failed!";
				return;
			} else if (StringUtils.isNotBlank(oldImage)) {
				File oldFile = new File(getFileUploadDir(), oldImage);
				if (oldFile.exists()) {
					if (!oldFile.delete()) {
						GameLog.getInstance().error("[DeviceConfirm] cannot delete old image file: " + oldImage);
					} else {
						GameLog.getInstance().info("[DeviceConfirm] Deleted old image file: " + oldImage);
					}
				}
			}
			ManageLogService.createManageLog(getUserInfo(), ManageLogType.UPDATE_DEVICE, strName);
			responseMsg = "Succeed! Updated item [ " + strName + " ]";
			DeviceEndPoint.sendMessageUpdate(userInfo.getId());
		} else {
			responseMsg = "No change!";
		}
	
	}
	
	private void doBorrowDevice() {
		if (device == null) {
			error = true;
			responseMsg = "Error! No item selected!";
			return;
		}
		if (!(device.getStatus() == DeviceStatus.AVAILABLE.getStatus() || device.getStatus() == DeviceStatus.PENDING.getStatus())) {
			error = true;
			responseMsg = "Error! Item is not available!";
			return;
		}
		String strStartDate = getContext().getRequestParameter("startdate");
		String strEndDate = getContext().getRequestParameter("enddate");
		String strUsername = getContext().getRequestParameter("username");
		User cUser = getUserInfo();
		if (StringUtils.isNotBlank(strUsername)) {
			if (!getUserInfo().isDeviceAdmin()) {
				error = true;
				responseMsg = "Error! No permission for setting username!";
				return;
			}
			User userInfo = UserService.getUserByName(strUsername);
			if (userInfo == null) {
				error = true;
				responseMsg = "Error! User not found :" + strUsername;
				return;
			}
			cUser = userInfo;
		}
		Map<String, String> returnData = DeviceService.doBorrowDevice(cUser, device, strStartDate, strEndDate, new HashMap<>());
		responseMsg = returnData.get("msg");
		error = returnData.containsKey("error");
	}
	
	private void doAdjustDate() {
		if (device == null) {
			error = true;
			responseMsg = "Error! No item selected!";
			return;
		}
		if (device.getUserId() != userInfo.getId() && !userInfo.isDeviceAdmin()) {
			error = true;
			responseMsg = "Error! Invalid action! you dont' have permission to update this item!";
			return;
		}
		if (device.getStatus() != DeviceStatus.UNAVAILABLE.getStatus()) {
			error = true;
			responseMsg = "Error! Invalid action! this item is not in use!";
			return;
		}
		String strExtendDate = getContext().getRequestParameter("extenddate");
		if (StringUtils.isBlank(strExtendDate)) {
			error = true;
			responseMsg = "Error! Update date is required!";
			return;
		}
		if (!strExtendDate.matches(DATE_PATTERN)) {
			error = true;
			responseMsg = "Error! Invalid Update date!!";
			return;
		}
		strExtendDate += " 00:00:00";
		Date extendDate = CommonMethod.getDate(strExtendDate);
		if (extendDate == null) {
			error = true;
			responseMsg = "Error! Update date is invalid!";
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();
		if (extendDate.before(today) || extendDate.before(device.getHoldDateDate())) {
			error = true;
			responseMsg = "Error! Update date is invalid! must be a future date!";
			return;
		}
		if (extendDate.getTime() == device.getReleaseDateDate().getTime()) {
			error = true;
			responseMsg = "Error! Update date is invalid!";
			return;
		}
		if (userInfo.isDeviceAdmin()) {
			device.setReleaseDate(CommonMethod.getFormatDateString(extendDate));
		} else {
			device.setExtendDate(CommonMethod.getFormatDateString(extendDate));
			device.setStatus(DeviceStatus.CHANGE_PENDING.getStatus());
		}
		if (CommonDaoFactory.Update(device.getDeviceInfo()) < 0) {
			error = true;
			responseMsg = "Error! Update item failed!";
			return;
		}
		responseMsg = "Update item successfully!";
		DeviceEndPoint.sendMessageUpdate(userInfo.getId());
	}

	private void doUpdateStatus() {
		switch (mode) {
		case MODE_ACCEPT:
		case MODE_DECLINE:
		case MODE_RETURN:
		case MODE_CANCEL:
		case MODE_REMOVE:
			if (device == null) {
				error = true;
				responseMsg = "Error! No item selected!";
				return;
			}
			if (mode.equals(MODE_ACCEPT) || mode.equals(MODE_DECLINE) || mode.equals(MODE_REMOVE)) {
				if (!userInfo.isDeviceAdmin()) {
					error = true;
					responseMsg = "Error! No permission!";
					return;
				}
				if (mode.equals(MODE_ACCEPT) || mode.equals(MODE_DECLINE)) {
					if (!(device.getStatus() == DeviceStatus.PENDING.getStatus() || device.getStatus() == DeviceStatus.RETURN_PENDING.getStatus() 
							|| device.getStatus() == DeviceStatus.CHANGE_PENDING.getStatus())) {
						error = true;
						responseMsg = "Error! Invalid action! this item is not in pending state!";
						return;
					}
					//
					if (device.getStatus() == DeviceStatus.PENDING.getStatus()) {
						if (deviceRegister == null || deviceRegister.isExpired()) {
							error = true;
							responseMsg = "Error! Invalid action! Request for item not found or expired(1)!";
							return;
						}
					}
				}
				if (mode.equals(MODE_REMOVE) && device.getStatus() != DeviceStatus.AVAILABLE.getStatus()) {
					error = true;
					responseMsg = "Error! Invalid action! this item is now in use!";
					return;
				}
			}
			else if (mode.equals(MODE_CANCEL)) {
				if (!(device.getStatus() == DeviceStatus.PENDING.getStatus() || device.getStatus() == DeviceStatus.RETURN_PENDING.getStatus() 
						|| device.getStatus() == DeviceStatus.CHANGE_PENDING.getStatus())) {
					error = true;
					responseMsg = "Error! Invalid action! this item is not in pending state!";
					return;
				}
				if (device.getStatus() == DeviceStatus.PENDING.getStatus()) {
					if (!userInfo.isDeviceAdmin()) {
						deviceRegister = DeviceRegisterService.getDeviceRegisterByUserId(device.getId(), userInfo.getId());
						if (deviceRegister == null || deviceRegister.isExpired()) {
							error = true;
							responseMsg = "Error! Invalid action! Request for item not found or expired(2)!";
							return;
						}
					}
				} else {
					if (device.getUserId() != userInfo.getId() && !userInfo.isDeviceAdmin()) {
						error = true;
						responseMsg = "Error! Invalid action! you dont' have permission to cancel request!";
						return;
					}
				}
			}
			else if (mode.equals(MODE_RETURN)) {
				if (device.getUserId() != userInfo.getId() && !userInfo.isDeviceAdmin()) {
					error = true;
					responseMsg = "Error! Invalid action! you dont' have permission to return this item!";
					return;
				}
				if (device.getStatus() != DeviceStatus.UNAVAILABLE.getStatus()) {
					error = true;
					responseMsg = "Error! Invalid action! this item is not in use!";
					return;
				}
			}
			List<DaoValue> updateList = new ArrayList<>();
			if (mode.equals(MODE_ACCEPT)) {
				if (device.getStatus() == DeviceStatus.PENDING.getStatus()) {
					device.setUserId(deviceRegister.getUserId());
					device.setUsername(deviceRegister.getUsername());
					device.setHoldDate(deviceRegister.getStartDate());
					device.setReleaseDate(deviceRegister.getEndDate());
					device.setStatus(DeviceStatus.UNAVAILABLE.getStatus());
					device.setRegistCount(0);
					List<DeviceRegister> listRegistDevice = DeviceRegisterService.ListAllDeviceRegisterLog(device.getId());
					if (listRegistDevice == null || listRegistDevice.isEmpty()) {
						error = true;
						responseMsg = "Error! Invalid action! Request for item not found or expired(3)!";
						return;
					}
					boolean f = false;
					for (DeviceRegister dr : listRegistDevice) {
						if (dr.getId() == deviceRegister.getId()) {
							f = true;
						}
						dr.setExpired();
						updateList.add(dr.getDeviceRegisterInfo());
					}
					if (!f) {
						error = true;
						responseMsg = "Error! Invalid action! Request for item not found or expired(4)!";
						return;
					}
				} else if (device.getStatus() == DeviceStatus.CHANGE_PENDING.getStatus()) {
					device.setStatus(DeviceStatus.UNAVAILABLE.getStatus());
					device.setReleaseDate(device.getExtendDate());
				} else {
					device.resetStatus();
				}
			} else if (mode.equals(MODE_DECLINE)) {
				if (device.getStatus() == DeviceStatus.PENDING.getStatus()) {
					device.descRegistCount();
					if (device.getRegistCount() <= 0) {
						device.resetStatus();
					}
					deviceRegister.setExpired();
					updateList.add(deviceRegister.getDeviceRegisterInfo());
				} else {
					device.setStatus(DeviceStatus.UNAVAILABLE.getStatus());
				}
			} else if (mode.equals(MODE_RETURN)) {
				if (device.isEditable(userInfo)) {
					device.resetStatus();
				} else {
					device.setStatus(DeviceStatus.RETURN_PENDING.getStatus());
				}
			} else if (mode.equals(MODE_CANCEL)) {
				if (device.getStatus() == DeviceStatus.PENDING.getStatus()) {
					device.descRegistCount();
					if (device.getRegistCount() <= 0) {
						device.resetStatus();
					}
					if (deviceRegister != null) {
						deviceRegister.setExpired();
						updateList.add(deviceRegister.getDeviceRegisterInfo());
					}
				} else {
					device.setStatus(DeviceStatus.UNAVAILABLE.getStatus());
				}
			} else if (mode.equals(MODE_REMOVE)) {
				device.setAvailable(false);
			}
			updateList.add(device.getDeviceInfo());
			if (CommonDaoFactory.Update(updateList) < 0) {
				error = true;
				responseMsg = "Error! Update item failed!";
				return;
			}
			switch (mode) {
			case MODE_ACCEPT:
				ManageLogService.createManageLog(getUserInfo(), ManageLogType.ACCEPT_REQUEST, device.getName());
				break;
			case MODE_DECLINE:
				ManageLogService.createManageLog(getUserInfo(), ManageLogType.DECLINE_REQUEST, device.getName());
				break;
			case MODE_CANCEL:
				ManageLogService.createManageLog(getUserInfo(), ManageLogType.CANCEL_DEVICE, device.getName());
				break;
			case MODE_REMOVE:
				ManageLogService.createManageLog(getUserInfo(), ManageLogType.REMOVE_DEVICE, device.getName());
				break;
			default:
				break;
			}
			if (mode == MODE_RETURN) {
				if (device.isEditable(userInfo)) {
					ManageLogService.createManageLog(getUserInfo(), ManageLogType.REQUEST_RETURN, device.getName());
					responseMsg = "Your request for returning item has been sent!";
				} else {
					ManageLogService.createManageLog(getUserInfo(), ManageLogType.RETURN_DEVICE, device.getName());
					responseMsg = "Update item successfully!";
				}
			} else {
				responseMsg = "Update item successfully!";
			}
			DeviceEndPoint.sendMessageUpdate(userInfo.getId());
			break;
		default:
			error = true;
			responseMsg = "Error! Invalid parameter!";
			break;
		}
	}

	private void doImportItem() {
		if (!getUserInfo().isDeviceAdmin()) {
			error = true;
			responseMsg = "Error! No permission!";
			return;
		}
		String strData = getContext().getRequestParameter("import_data");
		if (strData == null || strData.length() == 0) {
			error = true;
			responseMsg = "Error! Import data is empty!";
			return;
		}
		final int LIMIT = 6; // Name,Serial,BuyDate,Platform,Owner,Permission
		String[] lineDatas = strData.split("\r\n|\r|\n");
		List<DaoValue> updateList = new ArrayList<>();
		int lineNum = 0;
		for (String line : lineDatas) {
			++lineNum;
			line = line.trim();
			if (line.startsWith("#") || line.isEmpty()) {
				continue;
			}
			String[] columnDatas = line.split(",", LIMIT);
			if (columnDatas.length != LIMIT) {
				error = true;
				responseMsg = String.format("Import data error at line %d: %s", lineNum, line);
				return;
			}
			int idx = 0;
			String strName = columnDatas[idx++].trim();
			String strSerial = columnDatas[idx++].trim();
			String strBuyDate = columnDatas[idx++].trim();
			String strType = columnDatas[idx++].trim();
			String strDep = columnDatas[idx++].trim();
			String strFlag = columnDatas[idx++].trim();
			
			if (StringUtils.isBlank(strName)) {
				error = true;
				responseMsg = String.format("Name is invalid at line %d: %s", lineNum, line);
				return;
			}
			
			if (StringUtils.isBlank(strSerial)) {
				strSerial = "";
			}
			if (StringUtils.isBlank(strBuyDate)) {
				strBuyDate = CommonDefine.DEFAULT_DATE_TIME;
			} else {
				if (!strBuyDate.matches(DeviceService.DATE_PATTERN)) {
					error = true;
					responseMsg = String.format("BuyDate must be in format {yyyy-mm-dd} at line %d: %s", lineNum, line);
					return;
				} else {
					strBuyDate = String.format("%s 00:00:00", strBuyDate);
				}
			}
			Date buyDate = CommonMethod.getDate(strBuyDate);
			if (buyDate == null) {
				error = true;
				responseMsg = String.format("BuyDate is invalid at line %d: %s", lineNum, line);
				return;
			}
			DeviceType type = convertToDeviceType(strType.toUpperCase());
			if (type == null || !type.isAvailable()) {
				error = true;
				responseMsg = String.format("Platform is invalid at line %d: %s", lineNum, line);
				return;
			}
			DeviceDept dept = convertToDeviceDept(strDep.toUpperCase());
			DeviceFlag flag = convertToDeviceFlag(strFlag.toUpperCase());
			TblDeviceInfo deviceInfo = new TblDeviceInfo();
			deviceInfo.Set("name", strName);
			deviceInfo.Set("serial", strSerial);
			deviceInfo.Set("image", "");
			deviceInfo.Set("type", type.getType());
			deviceInfo.Set("buy_date", buyDate);
			deviceInfo.Set("dep", dept.getDep());
			deviceInfo.Set("flag", flag.getFlag());
			deviceInfo.Set("available", (byte) 1);
			updateList.add(deviceInfo);
		}
		int size = updateList.size();
		if (size > 0) {
			if (CommonDaoFactory.Update(updateList) < 0) {
				error = true;
				responseMsg = "Import data error!";
				return;
			}
			ManageLogService.createManageLog(getUserInfo(), ManageLogType.ADD_DEVICE, String.format("%s item(s) from csv file", size));
			responseMsg = String.format("Succeed! Imported %d item(s)", size);
			DeviceEndPoint.sendMessageUpdate(userInfo.getId());
		} else {
			responseMsg = "No valid data to import!";
		}
	}
	
	
	private DeviceType convertToDeviceType(String TYPE) {
		if (StringUtils.isBlank(TYPE)) {
			return null;
		}
		switch (TYPE) {
		case "PS5":
			return DeviceType.PS5;
		case "PS4":
			return DeviceType.PS4;
		case "SWITCH":
			return DeviceType.SWITCH;
		default:
			break;
		}
		return null;
	}
	
	private DeviceDept convertToDeviceDept(String DEPT) {
		if (StringUtils.isBlank(DEPT)) {
			return DeviceDept.NONE;
		}
		switch (DEPT) {
		case "DEV":
			return DeviceDept.DEV;
		case "QA":
			return DeviceDept.QA;
		case "CG":
			return DeviceDept.CG;
		case "AD":
			return DeviceDept.ADMIN;
		default:
			break;
		}
		return DeviceDept.NONE;
	}
	
	private DeviceFlag convertToDeviceFlag(String FLAG) {
		if (StringUtils.isBlank(FLAG)) {
			return DeviceFlag.ALL;
		}
		switch (FLAG) {
		case "ADMIN":
			return DeviceFlag.ADMIN;
		case "DEPT":
			return DeviceFlag.DEPT;
		default:
			break;
		}
		return DeviceFlag.ALL;
	}
}
