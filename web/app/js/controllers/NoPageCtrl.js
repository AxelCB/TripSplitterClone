'use strict';
/**
 * No Page Controller
 */
var universeControllers = angular.module('universeControllers');

universeControllers.controller('NoPageCtrl',['$scope', '$rootScope',
	function($scope, $rootScope) {
	
		if (!$rootScope.keepMessages) {
			$rootScope.messages = [i18n.t('404')];
			$rootScope.areErrorMessages = true;
		}
		
	}
]);
