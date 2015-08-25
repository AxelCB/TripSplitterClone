/**
 * Menu Controller
 */
var universeControllers = angular.module('universeControllers');

universeControllers.controller('MenuCtrl',['$scope', '$rootScope', '$location', '$route',
	function($scope, $rootScope, $location, $route) {

		$scope.goTo = function(newUrl) {
			if (newUrl == $location.path()) {
				$route.reload();
			} else {
				$location.path(newUrl);
			}
		};
		
		$scope.logout = function() {
			//LoginService.logout();
		};
	}
]);
