'use strict';
/**
 * User Credit Charge Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('UserCreditChargeCtrl',['$scope', '$rootScope', 'UserCreditChargeService',
                                                       'TicketPrintService', 'ParameterService', '$filter',
	function($scope, $rootScope, UserCreditChargeService, TicketPrintService, ParameterService, $filter) {
	
		$scope.creditCharge = {
				registeredUser: {
				}
		};
		
		$scope.lastCreditCharge = {};
		$scope.accountNumberFormat = "";
		$scope.accountNumberPattern = /^\d{1}$/;
		
		$scope.setAccountNumberValue = function(value) { 
			$("#accountId").val(value);
			if (!$scope.$$phase) {
				$scope.$apply(function() {
					$scope.creditCharge.registeredUser.userAccountId = value;
				});
			} else {
				$scope.creditCharge.registeredUser.userAccountId = value;
			}
		};
		
		$scope.resetAccountNumber = function() {
			if ($scope.accountNumberFormat.length > 0) {
				$scope.setAccountNumberValue(new Array($scope.accountNumberFormat.length + 1).join("0"));
			} else {
				$scope.setAccountNumberValue("");
			}
			$("#accountId").focus();
		};
		
		UserCreditChargeService.info(
			function(response) {
				if (response.ok) {
					var info = JSON.parse(response.data);
					$scope.emitsTickets = info.emitsTickets;
				}
		}, $rootScope.errorManager);
		
		ParameterService.get(['user.registration.account.id.format'], function(response) {
			if (response.ok) {
				var parameters = JSON.parse(response.data);
				$scope.accountNumberFormat  = parameters['user.registration.account.id.format'];
				
				$scope.accountNumberPattern = new RegExp("^\\d{" + $scope.accountNumberFormat.length + "}$");
				$scope.resetAccountNumber();
			} else {
				$rootScope.errorManager();
			}
		}, $scope.errorManager);
		
		$scope.clean = function() {
			$scope.initialize();
		};
	
		$scope.initialize = function() {
			$scope.creditCharge = {};
	
			$rootScope.areErrorMessages = false;
			$rootScope.messages = [];
	
		};
	
		$scope.charge = function() {
			$rootScope.dim();
			
			if ($scope.accountNumberForm.$valid) {
				UserCreditChargeService.creditCharge($scope.creditCharge,
						function(response) {
							if (response.ok) {
								$scope.lastCreditCharge = JSON.parse(response.data);
								
								if (response.payload) {
									//print ticket
									TicketPrintService.constructTicketIframeBody(JSON.parse(response.payload), $("#ticket").get(0));
								}
							}
							$rootScope.unDim();
						}, $rootScope.errorManager);
			} else {
				$rootScope.messages = [i18n.t('userCreditCharge.error.accountNumber')];
				$rootScope.areErrorMessages = true;
				$rootScope.unDim();
			}
		};
	
		$scope.reprint = function() {
			UserCreditChargeService.reprintTicket($scope.lastCreditCharge.id, function(
					response) {
				if (response.ok) {
					$scope.lastCreditCharge = JSON.parse(response.data);
					
					if (response.payload) {
						//print ticket
						TicketPrintService.constructTicketIframeBody(JSON.parse(response.payload), $("#ticket").get(0));
					}
				}
			}, $rootScope.errorManager);
		};
		
		$scope.resetAccountNumber();
		
		$("#accountId").keydown(function(ev) {
			if (ev.which != 9 && !((ev.which >= 48 && ev.which <= 57))) { //TAB
				ev.preventDefault();
			}
			if (ev.which == 8) {
				$scope.setAccountNumberValue("0" + $("#accountId").val().substr(0, $("#accountId").val().length - 1));
			};
			
		});
		$("#accountId").keypress(function(ev) {
			if (ev.which >= 48 && ev.which <= 57) {
				var newNumber = $scope.accountNumberFormat + (new Number($("#accountId").val())).toString() + (ev.which - 48).toString();
				$scope.setAccountNumberValue(newNumber.substr(newNumber.length - $scope.accountNumberFormat.length, $scope.accountNumberFormat.length));
			}
			ev.preventDefault();
		});
	}
]);