'use strict';
/**
 * Test Service
 */
var tripSplitterCloneModules = angular.module('services');

tripSplitterCloneModules.factory('TestService', function(UniverseService) {
	return {
		runTest: function (successFn, errorFn) {
			return UniverseService.authPost('/test/run.json', "", successFn, errorFn);
		}
	};
});