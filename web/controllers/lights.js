app.controller("LightsController", function ($scope, $http) {

	$scope.turnOn = function (id) {
		$http.get("../cgi-bin/turnon.cgi?id=" + id).success(function () {
			
		});
	};
	
	$scope.turnOff = function (id) {
		$http.get("../cgi-bin/turnoff.cgi?id=" + id).success(function () {
			
		});
	};
	
});