'use strict';
/**
 * Created by AxelCollardBovy on 8/29/15.
 *
 * Trip Service
 */
var universeModules = angular.module('services');

universeModules.factory('TripService', function(TripSplitterCloneService){
    return {
        create: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/trip/create.json', params, successFn, errorFn);
        },
        remove: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/trip/delete.json', params, successFn, errorFn);
        },
        modify: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/trip/modify.json', params, successFn, errorFn);
        },
        list: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/trip/list.json', params, successFn, errorFn);
        },
        search: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/trip/search.json', params, successFn, errorFn);
        },
        addTraveler: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/trip/addTraveler.json', params, successFn, errorFn);
        }
    };
});