'use strict';
/**
 * Total Owed Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('TotalOwedCtrl',['$scope','$rootScope','QueriesService','TripService',
	function($scope, $rootScope,QueriesService,TripService) {
		$scope.trips = [];
		$scope.selectedTrip={};
		$scope.totalOwedList=[];

		$scope.loadTrips = function(){
			TripService.list({}, function(response) {
				if (response.ok) {
					$scope.trips = JSON.parse(response.data);
					if($scope.trips.length>0){
						$scope.selectedTrip = $scope.trips[0];
						$scope.listTotalOwed();
					}
				}
			}, $rootScope.manageError);
		};

		$scope.listTotalOwed = function(){
			QueriesService.totalOwed($scope.selectedTrip,function(response){
				if(response.ok){
					$scope.totalOwedList = JSON.parse(response.data);
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
