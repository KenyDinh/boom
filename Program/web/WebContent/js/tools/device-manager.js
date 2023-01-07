var table;
$j.extend({
	init_table : function() {
		if ($j('#device-table').length) {
			table = $j('#device-table').DataTable({
				"responsive" : true,
				"ordering" : false,
				"info" : false,
				"paging" : false,
				"lengthChange" : false
			});
			let dep_select = '<div class="dataTables_dep" id="device-table-dep">';
			dep_select += '<label style="font-weight:normal;white-space:nowrap;text-align:left;">Department: ';
			dep_select += '<select name="device-table-dep" aria-controls="device-table" class="custom-select custom-select-sm form-control form-control-sm">';
			dep_select += $j('#devicedep').html();
			dep_select += '</select></label>';
			dep_select += '</div>';
			$j('#device-table_filter').parent().parent().find(' > div:first-child').append(dep_select);
			$j.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
				if (0 != $j('ul.nav-tabs > li.active').data('type')) {
					const type = $j('ul.nav-tabs > li.active > a').data('type');
					if (type) {
						if (type != table.cells(dataIndex, 2).data()[0]) {
							return false;
						}
					}
				}
				if ($j('#device-table-dep').length) {
					if (0 != $j('#device-table-dep select[name="device-table-dep"]').val()) {
						const dep = $j('#device-table-dep select[name="device-table-dep"]').find('option:selected').text();
						if (dep) {
							if (dep != $j(table.cells(dataIndex, 1).data()[0]).eq(0).text()) {
								return false;
							}
						}
					}
				}
				return true;
			});
			$j('#device-table-dep select[name="device-table-dep"]').change(function() {
				if (table != null) {
					table.draw();
				}
			});
			table.on('draw.dt', function() {
				initTextDotAnimate();
			});
		}
		$j.init_popover();
		$j.update_device_count();
	},
	update_device_count : function() {
		$j('span[id^="device-count"]').text(0);
		if ($j('.device-count').length) {
			$j('.device-count').each(function() {
				const type = $j(this).data('type');
				$j('span#device-count-' + type).text($j(this).text());
			});
		}
	},
	init_popover : function() {
		$j('[data-toggle="popover"]:not(.dv-more-info)').popover({
			html: true,
			content : function() {
				return $j(this).data('pop');
			}
		});
		if ($j('.dv-more-info').length) {
			$j('.dv-more-info').popover({
				html: true,
				content : function() {
					const deviceName = $j(this).closest('tr').find('>td:first-child').find(">div").eq(0).text();
					const serial = $j(this).closest('tr').find('>td:first-child').find(">div").eq(1).text();
					const note = $j(this).closest('tr').find('>td:nth-child(2)').find(">div").eq(1).html();
					const dept = $j(this).closest('tr').find('>td:nth-child(2)').find(">div").eq(0).html();
					let html = '';
					html += '<div style="margin-bottom:0.5rem;">';
						html += '<span>Name:</span>';
						html += '<div style="background-color:#444;border-radius:10px;padding:5px;">' + deviceName + '</div>';
					html += '</div>';
					html += '<div style="margin-bottom:0.5rem;">';
						html += '<span>Serial:</span>';
						html += '<div style="background-color:#444;border-radius:10px;padding:5px;min-height:22px;">' + serial + '</div>';
					html += '</div>';
					html += '<div style="margin-bottom:0.5rem;">';
						html += '<span>Owner:</span>';
						html += '<div style="background-color:#444;border-radius:10px;padding:5px;">' + dept + '</div>';
					html += '</div>';
					html += '<div style="margin-bottom:0.5rem;">';
						html += '<span>Note:</span>';
						html += '<div style="background-color:#444;border-radius:10px;padding:5px;min-height:22px;">' + note.replaceAll(/\r\n|\r|\n/g,'<br>') + '</div>';
					html += '</div>';
					return html;
				}
			});
		}
	},
	reload_device_table : function() {
		$j.ajax({
			url : CONTEXT + "/tools/sub/device_loader.htm",
			type : "get",
			data : "",
			datatyle : "html",
			success : function(ret) {
				$j('#device-list-table').html(ret);
				if (ret) {
					$j.init_table();
					if (table != null) {
						table.draw();
					}
					initToolTip();
					//initTextDotAnimate();
				}
			},
			error : function() {
				$j('#device-list-table').html("");
				//$j.alert_error("An error occured! Can not load device table!");
			}
		});
	}
});
$j(document).ready(function() {
	$j.init_table();
	if ($j('span#device-token').length) {
		var deviceSocket = new BoomSocket($j('span#device-token').text());
		$j('span#device-token').remove();
		deviceSocket.init({
			onmessage: function(event){
				if (event.data) {
					let obj = JSON.parse(event.data);
					switch(obj.msg_type) {
					case 'update':
						$j.reload_device_table();
						break;
					default:
						break;
					}
				}
			}
		});
	}
	$j('#add-item-btn').click(function() {
		$j(this).blur();
		$j('#form-add-item').trigger('reset');
		$j('#form-add-item').find('.modal-header h4').text('Add game');
		$j('#form-add-item').find('input[name="mode"]').val('add');
		$j('#form-add-item').find('input[name="id"]').val(0);
		$j('#form-add-item').find('button[type="submit"]').text('Add');
		$j('#form-add-item').find('#dv-btn-remove').hide();
		$j('#preview-img').attr('src',$j('#preview-img').data('src'));
		$j('#devicetype').trigger('change');
		$j('#add-item-form-modal').off('hidden.bs.modal');
		$j('#add-item-form-modal').modal('show');
	});
	$j('#form-add-item').submit(function(e) {
		e.preventDefault();
		const form_data = new FormData($j('#form-add-item')[0]);
		$j.ajax({
			url : CONTEXT + "/tools/sub/device_confirm.json",
			type : "post",
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false,
			cache: false,
			data : form_data,
			dataType : "json",
			success : function(ret) {
				if (ret.error) {
					if (typeof ret.response === "undefined") {
						ret.response = "Error!!!";
					}
					$j.alert_error(ret.response);
					return;
				}
				if (typeof ret.response === "undefined") {
					ret.response = "Succeed!!!";
				}
				$j.alert_success(ret.response);
				$j.reload_device_table();
				$j('#add-item-form-modal').modal('hide');
			},
			error : function() {
				$j.alert_error("An error occured!");
			}
		});
	});
	$j('#form-checkout-item').submit(function(e) {
		e.preventDefault();
		const device_id = $j(this).find('#device-id').val();
		const mode = $j(this).find('#mode').val();
		const startdate = $j(this).find('#startdate').val();
		const enddate = $j(this).find('#enddate').val();
		const data = {
			"mode" : mode,
			"deviceid" : device_id,
			"startdate" : startdate,
			"enddate" : enddate
		}
		if ($j(this).find('#username').length) {
			data['username'] = $j(this).find('#username').val();
		}
		$j.ajax({
			url : CONTEXT + "/tools/sub/device_confirm.json",
			type : "post",
			data : data,
			dataType : "json",
			success : function(ret) {
				if (ret.error) {
					if (typeof ret.response === "undefined") {
						ret.response = "Error!!!";
					}
					$j.alert_error(ret.response);
					return;
				}
				if (typeof ret.response === "undefined") {
					ret.response = "Succeed!!!";
				}
				$j.alert_success(ret.response);
				$j.reload_device_table();
				$j('#check-out-form-modal').modal('hide');
			},
			error : function() {
				$j.alert_error("An error occured!");
			}
		});
	});
	
	$j('#form-update-item').submit(function(e) {
		e.preventDefault();
		const device_id = $j(this).find('input[name="deviceid"]').val();
		const mode = $j(this).find('input[name="mode"]').val();
		const extenddate = $j(this).find('#extenddate').val();
		const data = {
			"mode" : mode,
			"deviceid" : device_id,
			"extenddate" : extenddate
		}
		$j.ajax({
			url : CONTEXT + "/tools/sub/device_confirm.json",
			type : "post",
			data : data,
			dataType : "json",
			success : function(ret) {
				if (ret.error) {
					if (typeof ret.response === "undefined") {
						ret.response = "Error!!!";
					}
					$j.alert_error(ret.response);
					return;
				}
				if (typeof ret.response === "undefined") {
					ret.response = "Succeed!!!";
				}
				$j.alert_success(ret.response);
				$j.reload_device_table();
				$j('#update-form-modal').modal('hide');
			},
			error : function() {
				$j.alert_error("An error occured!");
			}
		});
	});
	
	$j('#form-import-item').submit(function(e) {
		e.preventDefault();
		const mode = $j(this).find('input[name="mode"]').val();
		const importData = $j(this).find('#import-data').val();
		const data = {
			"mode" : mode,
			"import_data" : importData
		}
		$j.ajax({
			url : CONTEXT + "/tools/sub/device_confirm.json",
			type : "post",
			data : data,
			dataType : "json",
			success : function(ret) {
				if (ret.error) {
					if (typeof ret.response === "undefined") {
						ret.response = "Error!!!";
					}
					$j.alert_error(ret.response);
					return;
				}
				if (typeof ret.response === "undefined") {
					ret.response = "Succeed!!!";
				}
				$j.alert_success(ret.response);
				$j.reload_device_table();
				$j('#import-form-modal').modal('hide');
			},
			error : function() {
				$j.alert_error("An error occured!");
			}
		});
	});

	if ($j('#device-form-modal').length) {
		$j('#device-form-modal').on('hidden.bs.modal', function() {
			const id = ($j('#device-form-modal').find('.divice-modal-btn').first().data('id') || 0);
			if (id > 0) {
				const regModal = $j('#register-list-' + id);
				if (regModal.length) {
					regModal.off('hidden.bs.modal');
				}
			}
		});
		$j('#device-form-modal').find('.divice-modal-btn').on('click', function() {
			const mode = $j(this).data('mode');
			const device_id = $j(this).data('id');
			const device_rgid = ($j(this).data('rgid') || 0);
			if (isNaN(device_id) || device_id <= 0 || typeof mode === "undefined" || mode.length <= 0) {
				$j.alert_error("Invalid parameter!");
				return;
			}
			$j.ajax({
				url : CONTEXT + "/tools/sub/device_confirm.json",
				type : "post",
				data : {
					"mode" : mode,
					"deviceid" : device_id,
					"rgid" : device_rgid
				},
				dataType : "json",
				success : function(ret) {
					if (ret.error) {
						if (typeof ret.response === "undefined") {
							ret.response = "Error!!!";
						}
						$j.alert_error(ret.response);
						return;
					}
					if (typeof ret.response === "undefined") {
						ret.response = "Succeed!!!";
					}
					$j.alert_success(ret.response);
					$j.reload_device_table();
					$j('#device-form-modal').modal('hide');
				},
				error : function() {
					$j.alert_error("An error occured!");
				}
			});
		});
	}
	$j('#device-list-table').off('click', '.device-edit').on('click', '.device-edit', function() {
		$j(this).blur();
		const image = $j(this).closest('tr').find('>td:first-child').find(">img").eq(0).attr('src');
		const deviceName = $j(this).closest('tr').find('>td:first-child').find(">div").eq(0).text();
		const serial = $j(this).closest('tr').find('>td:first-child').find(">div").eq(1).text();
		const platform = $j(this).closest('tr').find('>td:nth-child(3)').text();
		const buyDate = $j(this).closest('tr').find('>td:nth-child(4)').text();
		const note = $j(this).closest('tr').find('>td:nth-child(2)').find(">div").eq(1).html();
		const dept = $j(this).data('dept');
		const flag = $j(this).data('flag');
		const id = $j(this).data('id');
		$j('#form-add-item').trigger('reset');
		$j('#form-add-item').find('.modal-header h4').text('Edit game');
		$j('#form-add-item').find('input[name="mode"]').val('edit');
		$j('#form-add-item').find('input[name="id"]').val(id);
		$j('#form-add-item').find('button[type="submit"]').text('Confirm');
		$j('#form-add-item').find('#devicename').val(deviceName);
		$j('#form-add-item').find('#deviceserial').val(serial);
		$j('#form-add-item').find('#buydate').val(buyDate.replaceAll('/','-'));
		$j('#form-add-item').find('#devicenote').val(note);
		$j('#form-add-item').find('#devicetype').val($j('#form-add-item').find('#devicetype > option:contains(' + platform + ')').val());
		$j('#form-add-item').find('#devicedep').val(dept);
		$j('#form-add-item').find('#deviceflag').val(flag);
		$j('#form-add-item').find('#dv-btn-remove').show();
		$j('#preview-img').attr('src',image);
		$j('#devicetype').trigger('change');
		
		$j('#add-item-form-modal').off('hidden.bs.modal');
		$j('#add-item-form-modal').modal('show');
	});
	$j('#device-list-table').off('dblclick', '.dv-update').on('dblclick', '.dv-update', function() {
		if ($j('#update-form-modal').length <= 0) {
			return;
		}
		const deviceName = $j(this).closest('tr').find('>td:first-child').find(">div").eq(0).text();
		$j('#form-update-item').trigger('reset');
		$j('#update-form-modal').modal('show');
		$j('#form-update-item').find('input[name="deviceid"]').val($j(this).data('id'));
		$j('#form-update-item').find('#device-name-update').text(deviceName);
		return;
	});
	$j('#dv-btn-remove').on('click', function() {
		const device_modal = $j('#device-form-modal');
		if (device_modal.lengtrh <= 0) {
			return;
		}
		const deviceName = $j(this).closest('#form-add-item').find('#devicename').val();
		const id = $j(this).closest('#form-add-item').find('input[name="id"]').val();
		retMsg = $j("#remove-confirm-txt").html().replace("{0}", deviceName);
		mode = "remove";
		if (retMsg != null) {
			device_modal.find('.pop-msg').html(retMsg);
		}
		device_modal.find('.divice-modal-btn').addClass('d-none');
		device_modal.find('.divice-modal-btn').data('id',id);
		device_modal.find('#confirm-btn').removeClass('d-none');
		device_modal.find('#confirm-btn').data('mode',mode);
		const curModal = $j('#add-item-form-modal');
		if (curModal.length && curModal.hasClass('show')) {
			curModal.off('hidden.bs.modal').on('hidden.bs.modal', function() {
				device_modal.modal('show');
			});
			curModal.modal('hide');
		} else {
			device_modal.modal('show');
		}
	});
	$j('#device-list-table').off('click', '.dv-action').on('click', '.dv-action', function() {
		$j(this).blur();
		const userName = $j(this).closest('tr').find('>td:nth-child(8)').text();
		const deviceName = $j(this).closest('tr').find('>td:first-child').find(">div").eq(0).text();
		const device_modal = $j('#device-form-modal');
		if (device_modal.lengtrh <= 0) {
			return;
		}
		let retMsg = null;
		let mode = null;
		if ($j(this).hasClass('device-check-out')) {
			$j('#form-checkout-item').trigger('reset');
			$j('#check-out-form-modal').modal('show');
			$j('#device-id').val($j(this).data('id'));
			return; //
		} else if ($j(this).hasClass('device-respond')) {
			const uName = $j(this).closest('tr').find('>td:nth-child(1)').text();
			const dName = $j(this).data('dname');
			retMsg = $j("#respond-confirm-txt").html().replace("{0}", uName).replace("{1}", dName);
		} else if ($j(this).hasClass('device-confirm')) {
			retMsg = $j("#confirm-confirm-txt").html().replace("{0}", userName).replace("{1}", deviceName);
		} else if ($j(this).hasClass('device-return')) {
			retMsg = $j("#return-confirm-txt").html().replace("{0}", deviceName);
			mode = "return";
		} else if ($j(this).hasClass('device-cancel')) {
			retMsg = $j("#cancel-confirm-txt").html().replace("{0}", deviceName);
			mode = "cancel";
		} else if ($j(this).hasClass('device-confirm-change')) {
			retMsg = $j("#change-confirm-txt").html().replace("{0}", userName).replace("{1}", deviceName);
		}
		if (retMsg != null) {
			device_modal.find('.pop-msg').html(retMsg);
		}
		device_modal.find('.divice-modal-btn').addClass('d-none');
		device_modal.find('.divice-modal-btn').data('id',$j(this).data('id'));
		const rgid = ($j(this).data('rgid') || 0);
		device_modal.find('.divice-modal-btn').data('rgid',rgid);
		if (mode == null) {
			device_modal.find('#accept-btn').removeClass('d-none');
			device_modal.find('#decline-btn').removeClass('d-none');
		} else {
			device_modal.find('#confirm-btn').removeClass('d-none');
			device_modal.find('#confirm-btn').data('mode',mode);
		}
		const curModal = $j('#register-list-' + $j(this).data('id'));
		if (curModal.length && curModal.hasClass('show')) {
			curModal.off('hidden.bs.modal').on('hidden.bs.modal', function() {
				device_modal.modal('show');
			});
			curModal.modal('hide');
		} else {
			device_modal.modal('show');
		}
	});
	
	$j('.nav-item').click(function() {
		$j(this).blur();
		$j(this).find('a').blur();
		$j('.nav-item').removeClass("active bg-info");
		$j(this).addClass("active bg-info");
		if (table != null) {
			table.draw();
		}
		//initTextDotAnimate();
	});
	if ($j('#username').length) {
		$j('#username').autoComplete({
			"minChars" : 2,
			source: function(term, suggest){
				$j.ajax({
					url : CONTEXT + "/sub/search_user.json",
					type : "get",
					data : {"username" : term},
					dataType : "json",
					success : function(ret) {
						if (ret.user_list) {
							suggest(ret.user_list);
						}
					},
					error : function() {
						console.log('error!');
					}
				});
			},
			renderItem : function(item) {
				return '<div class="autocomplete-suggestion" data-id="' + item.id + '" data-username="' + item.username + '">' + item.username + '</div>';
			},
			onSelect : function(event, term, item) {
				$j('#username').val(item.data('username'));
			}
		});
	}
	$j('#deviceimg').change(function() {
		showPreviewImage(this);
	});
	$j('#import-file').change(function() {
		const fileInput = this.files[0];
		if (fileInput) {
			const reader = new FileReader();
            reader.onload = function (e) {
                showFileImportContent(reader.result);
            }
            reader.onerror = function (e) {
                alert('File read error!');
            }
            reader.readAsText(fileInput);
        }
		
	});
});

function showPreviewImage(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		
		reader.onload = function(e) {
			$j('#preview-img').attr('src', e.target.result);
		}
	    reader.readAsDataURL(input.files[0]);
	}
}
function showFileImportContent(content) {
	const modal = $j('#import-form-modal');
	if (modal.length <= 0) {
		return;
	}
	$j('#form-import-item').find('#import-data').val(content);
	modal.off('hidden.bs.modal').on('hidden.bs.modal', function() {
		$j('#import-file').val('');
	});
	modal.modal('show');
}