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
        $scope.selectedCountry = {};
        $scope.cities = [];
        $scope.countries = [{'name':'Otro País','id':null}];

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

                $scope.trip = {};
                if(cities.length>0){
                    $scope.trip.destination=$scope.cities[0];
                }
                $scope.editing = false;
            }

            $scope.initialize();
        };

        paginationHelper = PaginationHelper($scope, 'tripNameSpace', true);

        $scope.initialize = function() {

            $scope.editing = false;
            $scope.trip = {};

            $rootScope.areErrorMessages = false;
        };
        $scope.initialize();

        $scope.createDestination = function() {
            if($scope.otherCountry){
                $scope.city.country= $scope.country;
            }else{
                $scope.city.country = $scope.selectedCountry;
            };
            DestinationService.createCity($scope.city,function(response){
                if(response.ok){
                    $('#createDestinationModal').modal('hide');
                    $scope.city={};
                    $scope.country={};
                    $scope.listCities();
                    $scope.listCountries();
                }
            },$rootScope.manageError);
        };
        $scope.listCities=function(){
            DestinationService.listCities({},function(response) {
                if (response.ok) {
                    $scope.cities = JSON.parse(response.data);
                    $scope.trip.destination=$scope.cities[0];
                }
            }, $rootScope.manageError);
        };

        $scope.listCountries=function(){
            DestinationService.listCountries({},function(response) {
                if (response.ok) {
                    var countries = JSON.parse(response.data);
                    countries.push($scope.countries[$scope.countries.length-1]);
                    $scope.countries = countries;

                    if($scope.countries.length<=1){
                        $scope.otherCountry=true;
                        $scope.selectedCountry=$scope.countries[$scope.countries.length-1];
                    }else{
                        $scope.otherCountry=false;
                        $scope.selectedCountry=$scope.countries[0];
                    };
                }
            }, $rootScope.manageError);
        };

        $scope.listCities();
        $scope.listCountries();

    }
]);