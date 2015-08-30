'use strict';
/**
 * Created by AxelCollardBovy on 8/29/15.
 *
 * Trip Controller
 */
var universeControllers = angular.module('tripSplitterCloneControllers');

universeControllers.controller('TripCreateCtrl',['$scope', '$rootScope', 'TripService', '$filter','DestinationService',
    function($scope, $rootScope, TripService, $filter,DestinationService) {
        $scope.trip = {};
        $scope.trips = [];
        $scope.editing = false;

        $scope.otherCountry = false;
        $scope.city = {};
        $scope.country = {};
        $scope.cities = [];
        $scope.countries = [];

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

        paginationHelper = PaginationHelper($scope, 'tripNameSpace', true);

        $scope.initialize = function() {

            $scope.editing = false;
            $scope.trip = null;

            $rootScope.areErrorMessages = false;
        };
        $scope.initialize();

        $scope.createDestination=function(){
            if($scope.otherCity){
               $scope.city.country= $scope.country;
            };
            DestinationService.createCity($scope.city,function(response){

            },$rootScope.manageError    );
        };
        $scope.listCities=function(){
            DestinationService.listCities({},function(response) {
                if (response.ok) {
                    $scope.cities = JSON.parse(response.data);
                }
            }, $rootScope.manageError);
        };

        $scope.listCountries=function(){
            DestinationService.listCountries({},function(response) {
                if (response.ok) {
                    $scope.countries = JSON.parse(response.data);
                    if($scope.countries.length<=0){
                        $scope.otherCountry=true;
                    };
                }
            }, $rootScope.manageError);
        };

        $scope.listCities();
        $scope.listCountries();

    }
]);