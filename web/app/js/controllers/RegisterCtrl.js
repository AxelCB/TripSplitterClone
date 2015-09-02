'use strict';
/**
 * Register Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('RegisterCtrl',['$scope','$rootScope','UserService',
	function($scope, $rootScope, UserService) {
	
		$scope.registration = {};
		$scope.enabling = false;

		$scope.register = function() {
			if ($scope.registration.password != $scope.registration.passwordConfirmation) {
				$rootScope.messages = "Las contraseñas no coinciden";
				$rootScope.areErrorMessages = true;
			} else {
				$rootScope.dim();

				UserService.register($scope.registration, function (response) {
					$rootScope.unDim();
					if (response.ok) {
						$rootScope.keepMessages = true;
						$rootScope.go("/login", true);
					}
				},$rootScope.errorManager);
			}
		};
	}
]);