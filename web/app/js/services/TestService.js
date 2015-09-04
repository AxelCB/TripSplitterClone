'use strict';
/**
 * Test Service
 */
var tripSplitterCloneModules = angular.module('services');

tripSplitterCloneModules.factory('TestService', function(TripSplitterCloneService) {
	return {
		runTest: function (successFn, errorFn) {
			return TripSplitterCloneService.authPost('/test/run.json', "", successFn, errorFn);
		},
		runTestWithoutAuth: function (successFn, errorFn) {
			return TripSplitterCloneService.post('/test/run.json', "", successFn, errorFn);
		}
	};
});