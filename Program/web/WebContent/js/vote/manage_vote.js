var $j = jQuery.noConflict();
var color_map = null;
var ctx = null;
var vote_chart = null;
var isPhone = false;
var timeout = null;
var survey_id = 0;
$j(document).ready(function() {
	if ($j('#vote_chart').length) {
		ctx = document.getElementById("vote_chart").getContext('2d');
		if ($j(window).outerWidth() <= 576) {
			isPhone = true;
		}
		if ($j('span#survey_id').length) {
			survey_id = $j('span#survey_id').text();
		}
		//loadVoteResult();
		$j('#chart-wrapper').on('show.bs.collapse', function() {
			loadVoteResult();
		});
		$j('#chart-wrapper').on('shown.bs.collapse', function() {
			$j('body,html').animate({
				scrollTop:$j('#vote_chart').offset().top
			},"slow");
		});
//		$j('#chart-wrapper').on('hide.bs.collapse', function() {
//			if (timeout != null) {
//				clearTimeout(timeout);
//			}
//		});
		// detail
		$j('#detail-wrapper').on('show.bs.collapse', function() {
			loadResultDetail();
		});
		$j('#detail-wrapper').on('shown.bs.collapse', function() {
			$j('#refresh-detail').show();
			$j('#collapse-detail').text("Less Detail");
		});
		$j('#detail-wrapper').on('hide.bs.collapse', function() {
			$j('#refresh-detail').hide();
		});
		$j('#detail-wrapper').on('hidden.bs.collapse', function() {
			$j('#collapse-detail').text("More Detail");
		});
		$j('#collapse-chart').click(function() {
			$j(this).blur();
			$j('#chart-wrapper').collapse("toggle");
		});
		$j('#collapse-detail').click(function() {
			$j(this).blur();
			$j('#detail-wrapper').collapse("toggle");
		});
		$j('#refresh-detail').click(function() {
			if ($j(this).is(':visible')) {
				$j(this).blur();
				$j('#table-detail').toggleClass('text-secondary');
				if ($j('button#btn-unvote').length) {
					loadUnvoteDetail();
				} else {
					loadResultDetail();
				}
			}
		});
	}
	if ($j('table#survey_table').length && $j('td#no-survey-record').length <= 0) {
		$j('table#survey_table').DataTable( {
	        responsive: true,
	        paging:false,
	        searching:false,
	        ordering:false,
	        info:false
	    });
	}
	if ($j('table#option_table').length && $j('td#no-option-record').length <= 0) {
		$j('table#option_table').DataTable( {
	        responsive: true,
	        paging:false,
	        searching:false,
	        ordering:false,
	        info:false
	    });
	}
	$j('#delete-button').click(function() {
		let answ = confirm("Are you sure to process delete?");
		if (answ == false) {
			return;
		}
		$j(this).closest('form').append('<input type="hidden" name="delete" value="1" />');
		$j(this).closest('form').submit();
	});
//	$j(window).on('scroll resize', function() {
//		if ($j(window).outerWidth() <= 576) {
//			if (!isPhone && vote_chart != null) {
//				vote_chart.type = 'bar';
//				vote_chart.update();
//				isPhone = true;
//			}
//		} else {
//			if (isPhone && vote_chart != null) {
//				vote_chart.type = 'horizontalBar';
//				vote_chart.update();
//				isPhone = false;
//			}
//		}
//	});
});

var isLoadingUnvote = false;
function loadUnvoteDetail() {
	if (typeof survey_id == "undefined" || survey_id == null || survey_id <= 0) {
		return;
	}
	if (isLoadingUnvote) {
		return;
	}
	isLoadingUnvote = true;
	$j.ajax({
		type:"POST",
		url:CONTEXT + "/vote/vote_data_loader.json?survey_id=" + survey_id + "&mode=result_detail&type=0",
		dataType:"json",
		success: function(result) {
			if (result) {
				drawUnvoteDetail(result.result_detail);
			}
		},
		error:function() {
			console.log("error!");
			isLoadingUnvote = false;
		}
	});
}

var isLoadingDetail = false;
function loadResultDetail() {
	if (typeof survey_id == "undefined" || survey_id == null || survey_id <= 0) {
		return;
	}
	if (isLoadingDetail) {
		return;
	}
	isLoadingDetail = true;
	$j.ajax({
		type:"POST",
		url:CONTEXT + "/vote/vote_data_loader.json?survey_id=" + survey_id + "&mode=result_detail&type=1",
		dataType:"json",
		success: function(result) {
			if (result) {
				drawResultDetail(result.result_detail);
			}
		},
		error:function() {
			console.log("error!");
			isLoadingDetail = false;
		}
	});
}

function drawUnvoteDetail(detail) {
	if (detail === null || detail === undefined) {
		return;
	}
	let wrapper = $j('div#detail-wrapper');
	if (wrapper.length <= 0) {
		return;
	}
	let html = '<div><button id="btn-unvote" class="btn btn-info" onclick="loadResultDetail();this.blur();return false;">Not-vote</button></div>';
	html += '<div class="font-weight-bold text-center text-info" style="margin:0.5rem 0;"><label>Those users who not voted yet</label></div>';
	html += '<table class="table table-striped" id="table-detail">';
		html += '<thead>';
			html += '<tr class="text-info">';
			html += '<th>Phone Number</th>';
			html += '<th>Full Name</th>';
			html += '</tr>';
		html += '</thead>';
		
		html += '<tbody>';
			for (let i = 0; i < detail.length; i++) {
				html += '<tr>';
					html += '<td>' + detail[i].user + '</td>';
					html += '<td>' + detail[i].info + '</td>';
				html += '</tr>';
			}
		html += '</tbody>';
	html += '<table>';
	wrapper.html(html);
	$j('table#table-detail').DataTable( {
        responsive: true,
        ordering:false,
    });
	isLoadingUnvote = false;
}

function drawResultDetail(detail) {
	if (detail === null || detail === undefined) {
		return;
	}
	let wrapper = $j('div#detail-wrapper');
	if (wrapper.length <= 0) {
		return;
	}
	let html = '<div><button class="btn btn-info" onclick="loadUnvoteDetail();this.blur();return false;">Voted</button></div>';
	html += '<div class="font-weight-bold text-center text-info" style="margin:0.5rem 0;"><label>Those users who already voted</label></div>';
	html += '<table class="table table-striped" id="table-detail">';
		html += '<thead>';
			html += '<tr class="text-info">';
			html += '<th>Phone Number</th>';
			html += '<th>Full Name</th>';
			html += '<th>Upvote</th>';
			html += '<th>Action</th>';
			html += '</tr>';
		html += '</thead>';
		
		html += '<tbody>';
			for (let i = 0; i < detail.length; i++) {
				html += '<tr>';
					html += '<td>' + detail[i].user + '</td>';
					html += '<td>' + detail[i].info + '</td>';
					html += '<td>' + detail[i].result + '</td>';
					html += '<td>' + detail[i].edit + '</td>';
				html += '</tr>';
			}
		html += '</tbody>';
	html += '<table>';
	wrapper.html(html);
	$j('table#table-detail').DataTable( {
        responsive: true,
        ordering:false,
    });
	isLoadingDetail = false;
}

var isLoadingResult = false;
function loadVoteResult() {
	if (typeof survey_id == "undefined" || survey_id == null || survey_id <= 0) {
		return;
	}
	if (isLoadingResult) {
		return;
	}
	isLoadingResult = true;
	if (timeout != null) {
		clearTimeout(timeout);
	}
	$j.ajax({
		type:"POST",
		url:CONTEXT + "/vote/vote_data_loader.json?survey_id=" + survey_id,
		dataType:"json",
		success: function(result) {
			if (result) {
				if (result.count) {
					$j('span#result-count').text(result.count);
				}
				drawVoteData(result.option_list);
				timeout = setTimeout(() => {
					loadVoteResult();
				}, 5000);
			}
		},
		error:function() {
			console.log("error!");
			isLoadingResult = false;
		}
	});
}

var title_labels;
function drawVoteData(option_list) {
	if (color_map == null) {
		color_map = new Map();
	}
	title_labels = [];
	let len = option_list.length;
	let arr_labels = [];
	let datas = [];
	let bg_color = [];
	let border_color = [];
	let type = (isPhone ? 'bar' : 'horizontalBar');
	for (let i = 0; i < len; i++) {
		let i_name = option_list[i].name;
		if (i_name.length > 5) {
			arr_labels.push((i_name.substr(0,6) + '...'));
		} else {
			arr_labels.push(option_list[i].name);
		}
		title_labels.push(option_list[i].name);
		datas.push(parseInt(option_list[i].count));
		let color = color_map.get(option_list[i].id);
		if (typeof color == "undefined") {
			do {
				if (Math.random() * 10 < 5) {
					color = randomColor({luminosity:'dark',format:'rgba',alpha:0.2});
				} else {
					color = randomColor({luminosity:'bright',format:'rgba',alpha:0.2});
				}
			} while(isExistColor(color));
			color_map.set(option_list[i].id,color);
		}
		bg_color.push(color);
		border_color.push(color.replace('0.2','1'));
		let trElem = $j('#option-rank-' + option_list[i].rank);
		if (trElem.length <= 0) {
			addNewOptionAppend(option_list[i]);
		} else {
			trElem.find('td.name').html(option_list[i].name);
			trElem.find('td.rate').html(option_list[i].rate);
			trElem.find('td.count').html(option_list[i].count);
			trElem.find('td.point').html(option_list[i].point);
		}
	}
	if ($j('#chart-wrapper').is(':visible')) {
		if (vote_chart == null) {
			vote_chart = new Chart(ctx, {
				type: type,
				data: {
					labels: arr_labels,
					datasets: [{
						label: 'total votes',
						data: datas,
						backgroundColor: bg_color,
						borderColor: border_color,
						borderWidth: 1
					}]
				},
				options: {
			        scales: {
			            yAxes: [{
			                ticks: {
			                    beginAtZero:true
			                }
			            }],
			            xAxes: [{
			                ticks: {
			                    beginAtZero:true
			                }
			            }]
			        },
			        tooltips: {
			        	callbacks: {
			        		title: function(tooltipItems, data) {
			        			let _tooltip = title_labels[tooltipItems[tooltipItems.length - 1].index] || '';
			        			if (_tooltip) {
			        				return _tooltip;
			        			}
			        			return 'null';
			        		}
			        	}
			        }
			    }
			});
		} else {
			let update = false;
			if (isNotSameArray(vote_chart.data.labels, arr_labels)) {
				update = true;
			} else if (isNotSameArray(vote_chart.data.datasets[0].data,datas)) {
				update = true;
			}
			
			if (update) {
				vote_chart.data.labels = arr_labels;
				vote_chart.data.datasets = [{
					label: 'total votes',
					data: datas,
					backgroundColor: bg_color,
					borderColor: border_color,
					borderWidth: 1
				}];
				vote_chart.update();
			}
		}
	}
	isLoadingResult = false;
}

function isNotSameArray(arr1,arr2) {
	if (typeof arr1  !== typeof arr2) {
		return true;
	}
	if (arr1.length !== arr2.length) {
		return true;
	}
	for (let i = 0; i < arr1.length; i++) {
		if (arr1[i] !== arr2[i]) {
			return true;
		}
	}
	return false;
}

function isExistColor(check_color) {
	for (let color of color_map.values()) {
		if (color === check_color) {
			return true;
		}
	}
	return false;
}

function addNewOptionAppend(option) {
	let html = '';
	html += '<tr class="option-rank-' + option.rank + '">';
	
	html+= '<td class="rank">' 	+ 	option.rank		+ '</td>';
	html+= '<td class="name">' 	+ 	option.name 	+ '</td>';
	html+= '<td class="rate">' 	+ 	option.rate 	+ '</td>';
	html+= '<td class="count">' + 	option.count 	+ '</td>';
	html+= '<td class="point">' + 	option.point 	+ '</td>';
	html += '</tr>';
	$j('tbody').append(html);
}
var isClearResult = false;
function clearResult(user) {
	if (typeof survey_id == "undefined" || survey_id == null || survey_id <= 0) {
		return;
	}
	if (typeof user == "undefined" || user == null || user.length == 0) {
		return;
	}
	let answ = confirm("Confirm clear up result?");
	if (answ == false) {
		return;
	}
	if (isClearResult) {
		return;
	}
	isClearResult = true;
	$j.ajax({
		type:"POST",
		url:CONTEXT + "/vote/vote_data_loader.json?survey_id=" + survey_id + "&mode=clear&user=" + user,
		dataType:"json",
		success: function(result) {
			if (result.success) {
				loadResultDetail();
				loadVoteResult();
				isClearResult = false;
			}
		},
		error:function() {
			console.log("error!");
			isClearResult = false;
		}
	});
}