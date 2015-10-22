'use strict';
/**
 * Created by AxelCollardBovy on 10/18/15.
 *
 * Queries Service
 */
var universeModules = angular.module('services');

universeModules.factory('QueriesService', function(TripSplitterCloneService){
    return {
        debts: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/queries/debts.json', params, successFn, errorFn);
        },
        totalOwed: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/queries/totalOwed.json', params, successFn, errorFn);
        },
        totalSpent: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/queries/totalSpent.json', params, successFn, errorFn);
        },
    };
});