'use strict';
/**
 * Role Type Service
 */
var tripSplitterCloneModules = angular.module('services');

tripSplitterCloneModules.factory('RoleTypeService', function(UniverseService) {
	return {
		create: function (params, successFn, errorFn) {
			return UniverseService.authPost('/roleType/create.json', params, successFn, errorFn);
		},
		remove: function (params, successFn, errorFn) {
			return UniverseService.authPost('/roleType/delete.json', params, successFn, errorFn);
		},
		modify: function (params, successFn, errorFn) {
			return UniverseService.authPost('/roleType/modify.json', params, successFn, errorFn);
		},
		list: function (pagination, successFn, errorFn) {
			return UniverseService.authPost('/roleType/list.json', pagination, successFn, errorFn);
		},
		listFunctions: function (successFn, errorFn) {
			return UniverseService.authPost('/roleType/listFunctions.json', "", successFn, errorFn);
		},
		listRoleTypes: function (successFn, errorFn) {
			return UniverseService.authPost('/roleType/listRoleTypes.json', "", successFn, errorFn);
		},
		listRoleTypeEnums: function (successFn, errorFn) {
			return UniverseService.authPost('/roleType/listRoleTypeEnums.json', "", successFn, errorFn);
		},
		listMenuOrders: function (successFn, errorFn) {
			return UniverseService.authPost('/roleType/listMenuOrders.json', "", successFn, errorFn);
		},
		search: function (params, successFn, errorFn) {
			return UniverseService.authPost('/roleType/search.json', params, successFn, errorFn);
		}
	};
});