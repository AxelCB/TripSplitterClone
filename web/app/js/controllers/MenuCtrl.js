/**
 * Menu Controller
 */
var universeControllers = angular.module('universeControllers');

universeControllers.controller('MenuCtrl',['$scope', '$rootScope', '$location', '$route',
	function($scope, $rootScope, $location, $route) {

		$scope.menuItems = [];
		$rootScope.currentArray = [];
		
		$scope.createOrGetMenuRoot = function(menuItems, menuName, uri) {
			var menuItem = null;
			for (var m = 0; m < menuItems.length; m++) {
				if (menuItems[m].menuName == menuName) {
					menuItem = menuItems[m]; 
				}
			}
			if (menuItem == null) {
				menuItem = {
					menuName: menuName,
					title: i18n.t('menu.' + menuName + ($rootScope.globalInspectorFlag ? '.shortName' : '.name')),
					enabled:true,
					options: [],
					uri: uri
				};
				menuItems.push(menuItem);
			}
			return menuItem;
		};
		
		$scope.goTo = function(newUrl) {
			if (newUrl == $location.path()) {
				$route.reload();
			} else {
				$location.path(newUrl);
			}
		};
		
		$rootScope.$watch("permissions", function() {
			$scope.menuItems = [];
			
			for (var i = 0; i < $rootScope.permissions.length; i++) {
				var parts = $rootScope.permissions[i].split(":");
				var menuUri = parts[0];
				var menuRoutes = parts[0].split("/");
				menuRoutes.splice(0,1);
				
				var menuItem = null;
				if (menuRoutes.length == 1) {
					$scope.createOrGetMenuRoot($scope.menuItems, menuRoutes[0], menuUri);
				} else {
					menuItem = $scope.createOrGetMenuRoot($scope.menuItems, menuRoutes[0]);
				}
				
				if (menuRoutes.length == 3) {
					var subMenuItem = $scope.createOrGetMenuRoot(menuItem.options, menuRoutes[0] + "." + menuRoutes[1]);
					$scope.createOrGetMenuRoot(subMenuItem.options, menuRoutes[0] + "." + menuRoutes[1] + '.' + menuRoutes[2], menuUri);
				} else if (menuRoutes.length == 2) {
					$scope.createOrGetMenuRoot(menuItem.options, menuRoutes[0] + "." + menuRoutes[1], menuUri);
				}
			}
		});
		
		$scope.logout = function() {
			//LoginService.logout();
		};
	}
]);
