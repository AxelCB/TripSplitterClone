'use strict';
/**
 * User Service
 */
var tripSplitterCloneModules = angular.module('services');

tripSplitterCloneModules.factory('UserService', function(TripSplitterCloneService) {
	return {
		list: function (params, successFn, errorFn) {
			return TripSplitterCloneService.authPost('/user/list.json', params, successFn, errorFn);
		},
		//resetAnyUserPassword: function (params, successFn, errorFn) {
		//	return UniverseService.authPost('/user/resetAnyUserPassword.json', params, successFn, errorFn);
		//},
		listUsers: function (params, successFn, errorFn) {
			return TripSplitterCloneService.authPost('/user/listAll.json', params, successFn, errorFn);
		},
		register: function (params, successFn, errorFn) {
			return TripSplitterCloneService.post('/user/register.json', params, successFn, errorFn);
		},
		login: function (params, successFn, errorFn) {
			return TripSplitterCloneService.post('/user/login.json', params, successFn, errorFn);
		},
		logout: function (params, successFn, errorFn) {
			return TripSplitterCloneService.authPost('/user/logout.json', params, successFn, errorFn);
		}
	};
});