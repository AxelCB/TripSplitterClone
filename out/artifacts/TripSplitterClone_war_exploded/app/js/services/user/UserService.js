'use strict';
/**
 * User Service
 */
var tripSplitterCloneModules = angular.module('services');

tripSplitterCloneModules.factory('UserService', function(UniverseService) {
	return {
		list: function (params, successFn, errorFn) {
			return UniverseService.authPost('/user/list.json', params, successFn, errorFn);
		},
		//resetAnyUserPassword: function (params, successFn, errorFn) {
		//	return UniverseService.authPost('/user/resetAnyUserPassword.json', params, successFn, errorFn);
		//},
		//search: function (params, successFn, errorFn) {
		//	return UniverseService.authPost('/user/search.json', params, successFn, errorFn);
		//},
		register: function (params, successFn, errorFn) {
			return UniverseService.authPost('/user/register.json', params, successFn, errorFn);
		},
		login: function (params, successFn, errorFn) {
			return UniverseService.post('/user/login.json', params, successFn, errorFn);
		}
	};
});