'use strict';
/**
 * Login Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('TestCtrl',['$scope', '$rootScope', '$location','TestService',
	function($scope, $rootScope, $location, TestService) {
		$scope.testResults={
							"configurationFailures":[],
							"passedTests":[],
							"skippedTests":[],
							"failedTests":[]
							};

		$scope.runTests = function(){
			TestService.runTest(function(response){
				$scope.testResults=JSON.parse(response.data);
			},$rootScope.manageError);
		};
	}
]);
