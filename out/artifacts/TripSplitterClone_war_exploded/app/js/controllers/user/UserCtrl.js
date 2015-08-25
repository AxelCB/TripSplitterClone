'use strict';
/**
 * User Controller
 */
var universeControllers = angular.module('universeControllers');

universeControllers.controller('UserCtrl',['$scope', '$rootScope', 'UserService',
	function($scope, $rootScope, UserService) {

		$rootScope.messages = [];
		$rootScope.areErrorMessages = false;
		
		$scope.roleTypeEnumsList = [];
		$scope.users = [];
		
		var paginationHelper;
		
		$scope.clean = function() {
			$scope.user = {
					role: {
						roleType: {
							roleTypeEnum: null
						}
					}
			};
		};
		
		UserService.listRoleTypeForCreation(function(response) {
			if (response.ok) {
				$scope.roleTypeEnumsList = JSON.parse(response.data);
			}
		}, $rootScope.errorManager);
		
		$scope.list = function(pagination) {
			$scope.currentFunction = $scope.list;
			UserService.list(pagination, function(response) {
				if (response.ok) {
					var responseObject = JSON.parse(response.data);
					
					if (responseObject.page) {
						$scope.users = responseObject.items;
						paginationHelper.extendCallback(responseObject);
					} else {
						$scope.users = responseObject;
					}	
				}
			}, $rootScope.manageError);
		};
	
		$scope.resetPassword = function(user) {
			UserService.resetAnyUserPassword(user, function (response) {
				
			},$rootScope.manageError);
		};
		
		$scope.create = function() {
			$rootScope.dim();
			UserService.create($scope.user, function(response) {
				if (response.ok) {
					$rootScope.keepMessages = true;
					$scope.initialize();
				}
				$rootScope.unDim();
			}, $scope.errorManager);

		};
		
		$scope.canDelete = function(user) {
			return $scope.roleTypeEnumsList.indexOf(user.role.roleType.roleTypeEnum) != -1;
		};
		
		$scope.remove = function(user) {
			UserService.remove(user, function(response) {
				if (response.ok) {
					$rootScope.keepMessages = true;
					$scope.initialize();
				}
			}, this.errorManager);
		};
		
		//add pagination capability
		paginationHelper = PaginationHelper($scope, 'userNameSpace', true);
		
		$scope.initialize = function() {
			if ($rootScope.canAccess('/configuration/user:listUser')) {
				$scope.list();
			}
			$scope.clean();
		};
		
		$scope.initialize();
	}
]);