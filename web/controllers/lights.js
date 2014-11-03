app.controller("LightsController", function ($scope, $http) {

	$scope.turnOn = function (id,hours) {
		$http.get("../cgi-bin/turn.cgi?id=" + id + "&hours=" + hours + "&action=1").success(function () {
			
		});
	};
	
	$scope.turnOff = function (id) {
		$http.get("../cgi-bin/turn.cgi?id=" + id + "&hours=4&action=0").success(function () {
			
		});
	};
	
});