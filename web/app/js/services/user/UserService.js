'use strict';
/**
 * User Service
 */
var universeModules = angular.module('services');

universeModules.factory('UserService', function(UniverseService) {
	return {
		list: function (params, successFn, errorFn) {
			return UniverseService.authPost('/user/list.json', params, successFn, errorFn);
		},
		resetAnyUserPassword: function (params, successFn, errorFn) {
			return UniverseService.authPost('/user/resetAnyUserPassword.json', params, successFn, errorFn);
		},
		search: function (params, successFn, errorFn) {
			return UniverseService.authPost('/user/search.json', params, successFn, errorFn);
		},
		listRoleTypeForCreation: function(successFn, errorFn) {
			return UniverseService.authPost('/user/listRoleTypeForCreation.json', "", successFn, errorFn);
		},
		create: function (params, successFn, errorFn) {
			return UniverseService.authPost('/user/create.json', params, successFn, errorFn);
		},
		remove: function (params, successFn, errorFn) {
			return UniverseService.authPost('/user/delete.json', params, successFn, errorFn);
		}
	};
});