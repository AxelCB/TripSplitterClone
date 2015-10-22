'use strict';
/**
 * Debts Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('DebtsCtrl',['$scope','$rootScope','QueriesService','TripService',
	function($scope, $rootScope,QueriesService,TripService) {
		$scope.trips = [];
		$scope.selectedTrip={};
		$scope.debts=[];

		$scope.loadTrips = function(){
			TripService.list({}, function(response) {
				if (response.ok) {
					$scope.trips = JSON.parse(response.data);
					if($scope.trips.length>0){
						$scope.selectedTrip = $scope.trips[0];
						$scope.listDebts();
					}
				}
			}, $rootScope.manageError);
		};

		$scope.listDebts = function(){
			QueriesService.debts($scope.selectedTrip,function(response){
				if(response.ok){
					$scope.debts = JSON.parse(response.data);
				}
			},$rootScope.manageError)
		};

		$scope.initialize = function() {

			$scope.editing = false;
			$scope.selectedTrip = {};

			//if ($rootScope.canAccess('/configuration/trip:listTrip')) {
			$scope.loadTrips();
			//}

			$rootScope.areErrorMessages = false;
		};
		$scope.initialize();
	}
]);
