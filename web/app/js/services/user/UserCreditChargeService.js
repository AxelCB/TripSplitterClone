'use strict';
/**
 * User Credit Charge Service
 */
var universeModules = angular.module('services');

universeModules.factory('UserCreditChargeService', function(UniverseService){
	return {
		info: function (successFn, errorFn) {
			return UniverseService.authPost('/userAccount/info.json', "", successFn, errorFn);
		},
		creditCharge: function (params, successFn, errorFn) {
			return UniverseService.authPost('/userAccount/creditCharge.json', params, successFn, errorFn);
		},
		reprintTicket: function (params, successFn, errorFn) {
			return UniverseService.authPost('/userAccount/reprintTicket.json', params, successFn, errorFn);
		}
	};
});