var $j = jQuery.noConflict();

var gameCount = 0;
var savedTime = new Date();
console.log(savedTime);

function appendGameField(content){
	var numberOfGames = $j('#demo-number').val();
	gameCount = numberOfGames;
	var fillInContent = [];
	$j("#demo-games-container").html('');
	if (isDefined(content) && isDefined(content["content"])) {
		if (!isDefined(numberOfGames)) {
			numberOfGames = content["content"].length;
			gameCount = numberOfGames;
			$j('#demo-number').val(numberOfGames);
		}
		fillInContent = content["content"];
	}
	
	if (fillInContent.length > numberOfGames) {
		fillInContent = fillInContent.slice(0, numberOfGames);
	}
	if (numberOfGames > 0) {
		var fieldWrapper = $j("<div class=\"fieldwrapper\">");
		for (i = 1; i <= numberOfGames; i++) {
			var titleLabel = $j("<label><b>Game number " + i + ":</b></label>");
			var gameNameLabel = $j("<label style='padding-right:10px;'><b>Game Name</b></label>");
			var gameNameField = $j("<input type=\"text\" id=\"gameName" + i + "\" onchange=\"getGameInfoFromInput()\" /><br>");
			var imageUrlLabel = $j("<label style='padding-right:27px;'><b>Image Url</b></label>");
			var imageUrlField = $j("<input type=\"text\" id=\"imageUrl" + i + "\" onchange=\"getGameInfoFromInput()\" />");
			var imageUrlNotice = $j("<label style='padding-left:10px;'>*The chosen image should be a square</label>");
			var speakerLabel = $j("<label style='padding-right:38px;'><b>Speaker</b></label>");
			var speakerField = $j("<input type=\"text\" id=\"speaker" + i + "\" onchange=\"getGameInfoFromInput()\" /><br>");
			
			if (fillInContent.length >= i) {
				var currentContent = fillInContent[i - 1];
				gameNameField.val(currentContent["name"]);
				imageUrlField.val(currentContent["imageUrl"]);
				speakerField.val(currentContent["speaker"]);
			}
			
			
			fieldWrapper.append($j("<br>"));
			fieldWrapper.append(titleLabel);
			fieldWrapper.append($j("<br>"));
			fieldWrapper.append(gameNameLabel);
			fieldWrapper.append(gameNameField);
			fieldWrapper.append(imageUrlLabel);
			fieldWrapper.append(imageUrlField);
			fieldWrapper.append(imageUrlNotice);
			fieldWrapper.append($j("<br>"));
			fieldWrapper.append(speakerLabel);
			fieldWrapper.append(speakerField);
		}
		$j('#demo-content').val(JSON.stringify({"content": fillInContent}));
		$j("#demo-games-container").append(fieldWrapper);
	}
}

function isDefined(object) {
	 return object != null && object != undefined && object != "";
}

function getGameInfoFromInput() {
	var inputContent = {"content": []};
	if (gameCount > 0) {
		for (i = 1; i <= gameCount; i++) {
			var gameName = $j('#gameName' + i).val();
			var imageUrl = $j('#imageUrl' + i).val();
			var speaker = $j('#speaker' + i).val();
			var contentToAdd = {"name": gameName, "imageUrl": imageUrl, "speaker": speaker}
			
			inputContent["content"].push(contentToAdd);
		}
	}
	$j('#demo-content').val(JSON.stringify(inputContent));
	return inputContent;
}

function twoDigitValue(s) {
	s = s.toString();
	if (s.length == 1) {
		return "0" + s;
	} else {
		return s;
	}
}


$j(function() {
	var originalContent = isDefined($j('#demo-content').val()) ? JSON.parse($j('#demo-content').val()) : null;
	var demoContent = originalContent;
	appendGameField(demoContent);
	
	$j("body").on('DOMSubtreeModified', "#demotimepicker", function() {
		$j('#demo-time').val($j("#demotimepicker").data("date"));
	});
	
	if (isDefined($j('#demo-time').val())) {
		savedTime = new Date($j('#demo-time').val());
	} else {
		var intDate = twoDigitValue(savedTime.getDate());
		var intMonth = twoDigitValue(savedTime.getMonth() + 1);
		var intYear = savedTime.getFullYear();
		var intHour = twoDigitValue(savedTime.getHours());
		var intMinute = twoDigitValue(savedTime.getMinutes());
		var intSecond = twoDigitValue(savedTime.getSeconds());
		savedTime = new Date(intYear + "-" + intMonth + "-" + intDate + "T" + intHour + ":" + intMinute + ":" + intSecond);
	}
	
	$j('#demotimepicker').datetimepicker({
		defaultDate: savedTime,
		format: 'DD-MM-YYYY HH:mm:ss',
		inline : true,
		sideBySide : true
	});
	
	$j('#demo-number').change(function(){
		demoContent = getGameInfoFromInput();
		appendGameField(demoContent);
	});
});
