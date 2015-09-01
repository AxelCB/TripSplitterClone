'use strict';
/**
 * Created by AxelCollardBovy on 8/29/15.
 *
 * Trip Controller
 */
var universeControllers = angular.module('tripSplitterCloneControllers');

universeControllers.controller('TripCtrl',['$scope', '$rootScope', 'TripService', '$filter','DestinationService','UserService',
    function($scope, $rootScope, TripService, $filter,DestinationService,UserService) {
        $scope.trip = {};
        $scope.trips = [];
        $scope.editing = false;
        $scope.selectedTraveler = {};

        $scope.users = [];
        $scope.currentTrip = {};

        var paginationHelper;

        $scope.list = function(pagination) {
            TripService.list(pagination, function(response) {
                if (response.ok) {
                    var responseObject = JSON.parse(response.data);

                    if (responseObject.page) {
                        $scope.trips = responseObject.items;
                        paginationHelper.extendCallback(responseObject);
                    } else {
                        $scope.trips = responseObject;
                    }
                }
            }, $rootScope.manageError);
        };

        $scope.listUsers = function() {
            UserService.listUsers({}, function(response) {
                if (response.ok){
                    $scope.users = JSON.parse(response.data);
                    $scope.selectedTraveler = $scope.users[0];
                }
            }, $rootScope.manageError);
        };

        $scope.edit = function(trip) {
            $scope.editing = true;

            $scope.trip =  trip;

            $scope.trips.splice($scope.trips.indexOf($scope.trip), 1);
        };

        $scope.modify = function() {
            if ($scope.myForm.$valid) {
                TripService.modify($scope.trip, function(response) {
                    if (response.ok) {
                        // trip successfully edited
                        var par = JSON.parse(response.data);

                        $scope.trips.push(par);

                        $rootScope.keepMessages = true;
                        $scope.initialize();
                    }
                }, this.errorManager);
            } else {
                $rootScope.showErrorMessage(i18n.t('commissionSchema.validation.numberFormat'), true);
            }
        };

        $scope.clean = function() {
            $scope.initialize();
        };

        $scope.cancel = function() {
            if ($scope.editing) {

                $scope.trip = {};
                $scope.editing = false;
            }

            $scope.initialize();
        };

        $scope.deleteTrip = function(trip){
            $('#deleteTripModal').modal('show');
            $scope.trip=trip;
        };

        $scope.remove = function() {
            TripService.remove($scope.trip, function(response) {
                if (response.ok) {
                    $scope.trips.splice($scope.trips.indexOf($scope.trip),
                        1);
                    $('#deleteTripModal').modal('hide');
                }
            }, this.errorManager);
        };

        $scope.addTraveler = function(){
            var trip = {};
            if($scope.selectedTraveler){
                if(!$scope.trip.travelers){
                    $scope.trip.travelers=[];
                }
                trip = angular.copy($scope.trip);
                trip.travelers.push({'user':$scope.selectedTraveler});
            };
            TripService.addTraveler(trip,function(response){
                if(response.ok){
                    $scope.trip.travelers=trip.travelers;
                    $('#addTravelerModal').modal('hide');
                }
            },$rootScope.manageError);

        };

        $scope.showAddTravelerModal = function(currentTrip){
            $scope.trip = currentTrip;
            $('#addTravelerModal').modal('show');
        }

        paginationHelper = PaginationHelper($scope, 'tripNameSpace', true);

        $scope.initialize = function() {

            $scope.editing = false;
            $scope.trip = {};

            if ($rootScope.canAccess('/configuration/trip:listTrip')) {
                $scope.list();
                $scope.listUsers();
            }

            $rootScope.areErrorMessages = false;
        };
        $scope.initialize();
    }
]);