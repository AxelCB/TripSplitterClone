'use strict';
/**
 * No Page Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('NoPageCtrl',['$scope', '$rootScope',
	function($scope, $rootScope) {
	
		if (!$rootScope.keepMessages) {
			$rootScope.messages = [i18n.t('404')];
			$rootScope.areErrorMessages = true;
		}
		
	}
]);
