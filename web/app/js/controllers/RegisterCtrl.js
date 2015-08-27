'use strict';
/**
 * Register Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('RegisterCtrl',['$scope','$rootScope','RegisterService','$location','$cookieStore','$routeParams',
	function($scope, $rootScope, RegisterService, $location, $cookieStore, $routeParams) {
	
		$scope.registration = {};
		$scope.documentTypes = [];
		$scope.enabling = false;
		$scope.usesCaptcha = false;
		
		$scope.register = function() {
			if ($scope.registration.password != $scope.registration.passwordConfirmation) {
				$rootScope.messages = [i18n.t('changePassword.validation.confirm.error')];
				$rootScope.areErrorMessages = true;
			} else {
				$rootScope.dim();
				
				if ($scope.usesCaptcha) {
					$scope.registration.challenge = Recaptcha.get_challenge();
					$scope.registration.response = Recaptcha.get_response();
				}
				
				RegisterService.register($scope.registration, function (response) {
					$rootScope.unDim();
					if (response.ok) {
						$rootScope.keepMessages = true;
						$rootScope.go("/login", true);
					} else {
						if ($scope.usesCaptcha) {
							Recaptcha.reload();
						}
					}
				}, function() {
					if ($scope.usesCaptcha) {
						Recaptcha.reload();
					}
					$rootScope.errorManager();
				});
			}
		};
		
		$scope.enabling = $routeParams.enablingHash && $routeParams.enablingHash != '';
		
		if ($scope.enabling) {
			$rootScope.dim();
			RegisterService.enable($routeParams.enablingHash, function (response) {
				$rootScope.unDim();
				$rootScope.keepMessages = true;
				$rootScope.go("/login", true);
			}, function() {
				$rootScope.unDim();
				$rootScope.keepMessages = true;
				$rootScope.go("/login", true);
				$rootScope.errorManager();
			});
		} else {
			RegisterService.documentTypes(function(res) {
				$scope.documentTypes = JSON.parse(res.data);
				
				if ($scope.documentTypes.length > 0) {
					$scope.registration.documentType = $scope.documentTypes[0];
				}
			}, $rootScope.manageError);
			
			RegisterService.usesCaptcha(function(res) {
				var usesCaptcha = JSON.parse(res.data);
				$scope.usesCaptcha = usesCaptcha;
				if (usesCaptcha) {
					Recaptcha.create("6LfZRfgSAAAAAGmGdjD3lanUDE6lO3UbO9GSRnIm", "recaptcha", {
						theme: 'blackglass'
					});
				}
			}, $rootScope.manageError);
		}
	}
]);