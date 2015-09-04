'use strict';
/**
 * Login Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('TestCtrl',['$scope', '$rootScope', '$location','TestService',
	function($scope, $rootScope, $location, TestService) {

		$scope.runTests = function(){
			TestService.runTest(function(response){
				if(response.ok){

				}
			},$rootScope.manageError);
		};
	}
]);
