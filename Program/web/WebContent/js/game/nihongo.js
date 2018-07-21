var $j = jQuery.noConflict();

var app = angular.module('fridayApp', []);

app.controller('nihongoCtrl', function($scope, $http, $timeout) {

	$scope.openingPlayPopup = false;

	$scope.openPlayPopup = function(index){
		$scope.choosenUnit = index;
		$scope.getUnitWordList($scope.choosenUnit);
		$scope.openingPlayPopup = true;
	}

	$scope.closePlayPopup = function(){
		$scope.openingPlayPopup = false;
	}

	$scope.keepPlayPopup = function($event){
		 $event.stopPropagation();
	}

	$scope.openingShopPopup = false;

	$scope.openShopPopup = function(){
		$scope.openingShopPopup = true;
	}

	$scope.closeShopPopup = function(){
		$scope.openingShopPopup = false;
	}

	$scope.keepShopPopup = function($event){
		 $event.stopPropagation();
	}

	$scope.getWordList = function(){
		$http({
			method: 'GET',
			url: 'json/nihongo_json.json'
		}).then(function successCallback(response) {
			$scope.unitList = [];
			$scope.unitWordList = [];
			for (var k in response.data.wordMap){
				if (response.data.wordMap.hasOwnProperty(k)) {
					$scope.unitList.push(k);
					$scope.unitWordList.push(response.data.wordMap[k]);
			    }
			}

			$scope.unitProgressList = [];

			for ( var i = 0; i < $scope.unitList.length; i++) {
				if (response.data.userProgress && response.data.userProgress.hasOwnProperty($scope.unitList[i])) {
					$scope.unitProgressList.push(response.data.userProgress[$scope.unitList[i]]);
				} else {
					$scope.unitProgressList.push(0);
				}
			}

			$scope.starCount = response.data.userStar;
			$scope.owningList = response.data.owningList;
			$scope.petMap = response.data.petMap;
		}, function errorCallback(response) {
			//error log
		});
	}

	$scope.getProgressInt = function(index) {
		return $scope.unitProgressList[index];
	}
	$scope.getProgress = function(index){
		var doneCount = $scope.unitProgressList[index]%100;
		var lengthCount = Math.floor($scope.unitProgressList[index]/100);
		return doneCount + "/" + lengthCount;
	}

	$scope.getPetName = function(pet_id){
		if ($scope.petMap.hasOwnProperty(pet_id)) {
			return $scope.petMap[pet_id].pet_name;
		} else {
			return "";
		}
	}

	$scope.canLevelUp = function(userPet){
		if ($scope.petMap.hasOwnProperty(userPet.pet_id)) {
			return userPet.current_level <$scope.petMap[userPet.pet_id].max_level;
		} else {
			return false;
		}
	}

	var unitIndex = -1;

	$scope.getUnitWordList = function(index){
		unitIndex = index;
		$scope.wordList = $scope.unitWordList[index];
		$scope.testLength = $scope.wordList.length;

		for (var i = 0; i < $scope.testLength; i++) {
			var ran = Math.floor(Math.random() * $scope.testLength);
			var temp = angular.copy($scope.wordList[i]);
			$scope.wordList[i] = angular.copy($scope.wordList[ran]);
			$scope.wordList[ran] = angular.copy(temp);
		}

		$scope.curentIndex = 0;
		$scope.currentWord = "";
		$scope.inAnimation = false;
		$scope.pomuStyle = { bottom: '0px', left: '0px' };
		$scope.tatamiStyle = {};
		$scope.isHole = -1;
		$scope.wrongAnswer = false;
		$scope.pomuPos = 0;
		$scope.endGame = false;
		$scope.endGameMessage = "";

		$scope.loadQuestion();
	}

	$scope.loadQuestion = function(){
		$scope.currentWord = $scope.wordList[$scope.curentIndex];
		$scope.answerList = [];
		$scope.answerList.push($scope.currentWord.meaning);
		var answerIndex = [];
		answerIndex.push($scope.curentIndex);
		for (var i = 0; i < 3; i++) {
			var ran = Math.floor(Math.random() * $scope.testLength);
			while(answerIndex.includes(ran)){
				ran = Math.floor(Math.random() * $scope.testLength);
			}
			answerIndex.push(ran);
			$scope.answerList.push($scope.wordList[ran].meaning);
		}
		for (var i = 0; i < 4; i++) {
			var ran = Math.floor(Math.random() * 4);
			var temp = angular.copy($scope.answerList[i]);
			$scope.answerList[i] = angular.copy($scope.answerList[ran]);
			$scope.answerList[ran] = angular.copy(temp);
		}
	}

	$scope.chooseAnswer = function(index){
		if($scope.endGame){
			return;
		}
		$scope.inAnimation = true;
		var pomuMoveTime = 300 + 100*Math.abs(index-$scope.pomuPos);
		$scope.pomuPos = index;
		$scope.pomuStyle = { bottom: '100px', left: (index*150) + 'px', transition: pomuMoveTime +'ms all ease-in-out' };
		if($scope.answerList[index] === $scope.currentWord.meaning){
			$timeout(function() {
				if($scope.curentIndex < ($scope.testLength-1)){
					$scope.tatamiStyle = { transform: 'translateY(150px)', transition: '500ms all ease-in-out', overflow: 'visible'  };
					$scope.pomuStyle = { bottom: '150px', left: (index*150) + 'px', transition: '500ms all ease-in-out' };
					$timeout(function() {
						$scope.pomuStyle = { bottom: '0px', left: (index*150) + 'px' };
						$scope.tatamiStyle = {};
						$scope.inAnimation = false;

						$scope.curentIndex++;
						$scope.loadQuestion();
					}, 500);
				} else {
					$http({
						method: 'POST',
						url: 'json/nihongo_json.json',
						params: {
							'testId' : $scope.unitList[$scope.choosenUnit],
							'progress' : 99
						}
					}).then(function successCallback(response) {
						if(response.data.progressChanged){
							$scope.unitProgressList[unitIndex] = 99;
							$scope.starCount = response.data.userStar;
						}
					}, function errorCallback(response) {
						//error log
					});

					$scope.endGame = true;
					$scope.endGameMessage = "勝ちです！";
					$scope.inAnimation = false;
				}
			}, pomuMoveTime);
		} else {
			$scope.isHole = index;
			$timeout(function() {
				var progress = $scope.curentIndex + $scope.testLength*100;
				$http({
					method: 'POST',
					url: 'json/nihongo_json.json',
					params: {
						'testId' : $scope.unitList[$scope.choosenUnit],
						'progress' : progress
					}
				}).then(function successCallback(response) {
					if(response.data.progressChanged){
						$scope.unitProgressList[unitIndex] = progress;
					}
				}, function errorCallback(response) {
					//error log
				});

				$scope.wrongAnswer = true;
				$scope.endGame = true;
				$scope.endGameMessage = "負けです。。。";
				$scope.inAnimation = false;
			}, pomuMoveTime);
		}
	}

	$scope.getAnswerStyle = function(index){
		if(index == $scope.isHole){
			return { background: "url('../../img/game/nihongo/hole.png') no-repeat center" };
		} else {
			return {};
		}
	}

	$scope.choosenUnit = -1;
	$scope.getWordList();

	$scope.levelUp = function(id){
		var progress = $scope.curentIndex + $scope.testLength*100;
		$http({
			method: 'POST',
			url: 'json/nihongo_json.json',
			params: {
				'owningId' : id
			}
		}).then(function successCallback(response) {
			if(response.data.dataChanged){
				$scope.starCount = response.data.userStar;
				$scope.owningList = response.data.owningList;
			}
		}, function errorCallback(response) {
			//error log
		});
	}

	$scope.buyPet = function(id){
		var progress = $scope.curentIndex + $scope.testLength*100;
		$http({
			method: 'POST',
			url: 'json/nihongo_json.json',
			params: {
				'petId' : id
			}
		}).then(function successCallback(response) {
			if(response.data.dataChanged){
				$scope.starCount = response.data.userStar;
				$scope.owningList = response.data.owningList;
			}
		}, function errorCallback(response) {
			//error log
		});
	}
});

app.filter('unsafe', function($sce) {
	return function(val) {
		return $sce.trustAsHtml(val);
	};
});