'use strict';
/**
 * Created by AxelCollardBovy on 8/29/15.
 *
 * Trip Controller
 */
var universeControllers = angular.module('tripSplitterCloneControllers');

universeControllers.controller('TripCtrl',['$scope', '$rootScope', 'TripService', '$filter',
    function($scope, $rootScope, TripService, $filter) {
        $scope.trip = {};
        $scope.trips = [];
        $scope.editing = false;

        var paginationHelper;

        $scope.create = function() {
            if ($scope.myForm.$valid) {
                TripService.create($scope.trip, function(response) {
                    if (response.ok) {
                        // trip successfully created
                        var par = JSON.parse(response.data);

                        $rootScope.keepMessages = true;
                        $scope.initialize();
                    }

                }, $scope.errorManager);
            } else {
                $rootScope.showErrorMessage(i18n.t('commissionSchema.validation.numberFormat'), true);
            }
        };

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

                $scope.trip = null;
                $scope.editing = false;
            }

            $scope.initialize();
        };

        $scope.remove = function(trip) {
            TripService.remove(trip, function(response) {
                if (response.ok) {
                    $scope.trips.splice($scope.trips.indexOf(trip),
                        1);
                }
            }, this.errorManager);
        };

        $scope.search = function(pagination) {
            var data;

            if (pagination) {
                pagination.vo = $scope.trip;
                data = pagination;
            } else {
                data = $scope.trip;
            }

            TripService.search(data, function(response) {
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

        paginationHelper = PaginationHelper($scope, 'tripNameSpace', true);

        $scope.initialize = function() {

            $scope.editing = false;
            $scope.trip = null;

            if ($rootScope.canAccess('/configuration/trip:listTrip')) {
                $scope.list();
            }

            $rootScope.areErrorMessages = false;
        };
        $scope.initialize();
    }
]);