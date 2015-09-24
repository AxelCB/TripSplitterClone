'use strict';
/**
 * Created by AxelCollardBovy on 9/20/15.
 *
 * Expense Controller
 */
var universeControllers = angular.module('tripSplitterCloneControllers');

universeControllers.controller('ExpenseCtrl',['$scope', '$rootScope', 'ExpenseService', '$filter','UserService','$routeParams','TripService',
    function($scope, $rootScope, ExpenseService, $filter,UserService,$routeParams,TripService) {
        $scope.expense = {};
        $scope.expenses = [];
        $scope.editing = false;
        $scope.selectedTraveler = {};

        $scope.users = [];
        $scope.currentExpense = {};
        $scope.trip = {'id':$routeParams.tripId};

        //var paginationHelper;

        $scope.list = function(pagination) {
            ExpenseService.list(pagination, function(response) {
                if (response.ok) {
                    var responseObject = JSON.parse(response.data);

                    if (responseObject.page) {
                        $scope.expenses = responseObject.items;
                        paginationHelper.extendCallback(responseObject);
                    } else {
                        $scope.expenses = responseObject;
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

        $scope.edit = function(expense) {
            $scope.editing = true;

            $scope.expense =  expense;

            $scope.expenses.splice($scope.expenses.indexOf($scope.expense), 1);
        };

        $scope.modify = function() {
            if ($scope.myForm.$valid) {
                ExpenseService.modify($scope.expense, function(response) {
                    if (response.ok) {
                        // expense successfully edited
                        var par = JSON.parse(response.data);

                        $scope.expenses.push(par);

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

                $scope.expense = {};
                $scope.editing = false;
            }

            $scope.initialize();
        };

        $scope.deleteExpense = function(expense){
            $('#deleteExpenseModal').modal('show');
            $scope.expense=expense;
        };

        $scope.remove = function() {
            ExpenseService.remove($scope.expense, function(response) {
                if (response.ok) {
                    $scope.expenses.splice($scope.expenses.indexOf($scope.expense),
                        1);
                    $('#deleteExpenseModal').modal('hide');
                }
            }, this.errorManager);
        };

        $scope.search = function(pagination) {
            TripService.search($scope.trip, function(response) {
                if (response.ok) {
                    $scope.trip = JSON.parse(response.data);

                }
            }, $rootScope.manageError);
        };

        $scope.initialize = function() {

            $scope.editing = false;
            $scope.expense = {};

            if ($rootScope.canAccess('/configuration/expense:listExpense')) {
                //$scope.list();
                //$scope.listUsers();
                $scope.search();
            }

            $rootScope.areErrorMessages = false;
        };
        $scope.initialize();
    }
]);