'use strict';
/**
 * Created by AxelCollardBovy on 9/20/15.
 *
 * Expense Service
 */
var universeModules = angular.module('services');

universeModules.factory('ExpenseService', function(TripSplitterCloneService){
    return {
        create: function (params, successFn, errorFn) {
            return ExpenseSplitterCloneService.authPost('/expense/create.json', params, successFn, errorFn);
        },
        remove: function (params, successFn, errorFn) {
            return ExpenseSplitterCloneService.authPost('/expense/delete.json', params, successFn, errorFn);
        },
        modify: function (params, successFn, errorFn) {
            return ExpenseSplitterCloneService.authPost('/expense/modify.json', params, successFn, errorFn);
        },
        list: function (params, successFn, errorFn) {
            return ExpenseSplitterCloneService.authPost('/expense/list.json', params, successFn, errorFn);
        },
        search: function (params, successFn, errorFn) {
            return ExpenseSplitterCloneService.authPost('/expense/search.json', params, successFn, errorFn);
        },
        addTraveler: function (params, successFn, errorFn) {
            return ExpenseSplitterCloneService.authPost('/expense/addTraveler.json', params, successFn, errorFn);
        },
        addExpense: function (params, successFn, errorFn) {
            return ExpenseSplitterCloneService.authPost('/expense/addExpense.json', params, successFn, errorFn);
        },
    };
});