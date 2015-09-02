/**
 * Menu Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('MenuCtrl',['$scope', '$rootScope', '$location', '$route','UserService','$cookieStore',
	function($scope, $rootScope, $location, $route, UserService, $cookieStore) {

		$scope.goTo = function(newUrl) {
			if (newUrl == $location.path()) {
				$route.reload();
			} else {
				$location.path(newUrl);
			}
		};

		$scope._logout = function () {
			//$rootScope.permissions = [];
			$rootScope.loggedUser = null;
			$cookieStore.remove("loggedUser");
			$cookieStore.remove("userCredentials");
			//we redirect to the /login
			$rootScope.unDim();
			$location.path("/login");
		};
		
		$scope.logout = function() {
			UserService.logout("",$scope._logout,$scope._logout);
		};
	}
]);
