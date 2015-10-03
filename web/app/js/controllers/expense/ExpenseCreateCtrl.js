'use strict';
/**
 * Created by AxelCollardBovy on 9/20/15.
 *
 * Expense Controller
 */
var universeControllers = angular.module('tripSplitterCloneControllers');

universeControllers.controller('ExpenseCreateCtrl',['$scope', '$rootScope', 'TripService', '$routeParams',
    function($scope, $rootScope, TripService, $routeParams) {
        $scope.expense = {};
        $scope.expenses = [];
        $scope.editing = false;
        $scope.trip = {'id':$routeParams.tripId};
        $scope.payingUser={};
        $scope.splittingForms=[];
        $scope.travelers=[];
        $scope.travelerProportions = [];
        $scope.travelerProportion = {};
        $scope.amount=null;
        $scope.description=null;

        $scope.create = function() {
            if ($scope.myForm.$valid && $scope.payingUser && $scope.selectedSplittingForm && $scope.travelerProportions.length>0) {
                var newExpense = {
                    "payingUser":$scope.payingUser,
                    "splittingForm":$scope.selectedSplittingForm,
                    "trip":$scope.trip,
                    "travelerProportions":$scope.travelerProportions,
                    "amount":$scope.amount,
                    "description":$scope.description
                };
                TripService.addExpense(newExpense, function(response) {
                    if (response.ok) {
                        // expense successfully created
                        var par = JSON.parse(response.data);

                        $rootScope.keepMessages = true;
                        $scope.initialize();
                    }

                }, $scope.errorManager);
            } else {
                $rootScope.showErrorMessage(i18n.t('commissionSchema.validation.numberFormat'), true);
            }
        };

        $scope.search = function(pagination) {
            TripService.search($scope.trip, function(response) {
                if (response.ok) {
                    $scope.trip = JSON.parse(response.data);
                    angular.forEach($scope.trip.travelers,function(elem,index){
                        $scope.travelers.push(elem.user);
                    });
                    $scope.payingUser = $scope.travelers[0];
                }
            }, $rootScope.manageError);
        };

        $scope.initialize = function() {
            $scope.editing = false;
            $scope.travelerProportions = [];
            $scope.amount=null;
            if($scope.travelers.length>0){
                $scope.payingUser = $scope.travelers[0];
            }
            if($scope.splittingForms.length>0){
                $scope.selectedSplittingForm=$scope.splittingForms[0];
            }
            $rootScope.areErrorMessages = false;
        };


        $scope.listSplittingForms=function(){
            TripService.listSplittingForms({},function(response) {
                if (response.ok) {
                    $scope.splittingForms = JSON.parse(response.data);
                    $scope.selectedSplittingForm=$scope.splittingForms[0];
                }
            }, $rootScope.manageError);
        };

        $scope.addTravelerProportion = function(){
            if(($scope.travelerProportion.proportion || $scope.selectedSplittingForm == 'EQUAL_SPLITTING') && $scope.travelerProportion.traveler) {
                $scope.travelerProportions.push($scope.travelerProportion);
                $scope.travelerProportion={};
                $('#addTravelerProportionsModal').modal('hide');
            }else{
                //TODO mostrar algun mensaje de error
            }
        };

        $scope.restingUsers = function(value, index, array){
            angular.forEach($scope.travelerProportions,function(elem,index){
               return value.id != elem.traveler.id;
            });
            return true;
        };

        $scope.makePretty = function(splittingForm){
            switch (splittingForm){
                case 'EQUAL_SPLITTING':
                    return 'División Equitativa';
                    break;
                case 'PROPORTIONAL_SPLITTING':
                    return 'División Porcentual o Proporcional';
                    break;
            };
        };

        $scope.start= function(){
            $scope.search();
            $scope.listSplittingForms();

            $scope.initialize();
        };

        $scope.start();
    }
]);