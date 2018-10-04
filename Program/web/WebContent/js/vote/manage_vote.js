var $j = jQuery.noConflict();
var color_map = null;
var ctx = null;
var vote_chart = null;
var isPhone = false;
$j(document).ready(function() {
	ctx = document.getElementById("vote_chart").getContext('2d');
	if ($j(window).outerWidth() <= 576) {
		isPhone = true;
	}
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
	loadVoteResult();
});

function loadVoteResult() {
	$j.ajax({
		type:"POST",
		url:CONTEXT + "/vote/vote_data_loader.json",
		dataType:"json",
		success: function(result) {
			if (result) {
				drawVoteData(result.option_list);
			}
		},
		error:function() {
			console.log("error!");
		}
	});
}

function drawVoteData(option_list) {
	if (color_map == null) {
		color_map = new Map();
	}
	let len = option_list.length;
	let arr_labels = [];
	let datas = [];
	let bg_color = [];
	let border_color = [];
	let type = (isPhone ? 'bar' : 'horizontalBar');
	for (let i = 0; i < len; i++) {
		arr_labels.push(option_list[i].name);
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
			trElem.find('td.name').text(option_list[i].name);
			trElem.find('td.rate').text(option_list[i].rate);
			trElem.find('td.count').text(option_list[i].count);
		}
	}
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
		            }]
		        }
		    }
		});
	} else {
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
	
	html += '</tr>';
	$j('tbody').append(html);
}