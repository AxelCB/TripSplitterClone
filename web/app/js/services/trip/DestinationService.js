'use strict';
/**
 * Created by AxelCollardBovy on 8/29/15.
 *
 * Trip Service
 */
var universeModules = angular.module('services');

universeModules.factory('DestinationService', function(TripSplitterCloneService){
    return {
        createCity: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/destination/create.json', params, successFn, errorFn);
        },
        listCities: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/destination/listCities.json', params, successFn, errorFn);
        },
        listCountries: function (params, successFn, errorFn) {
            return TripSplitterCloneService.authPost('/destination/listCountries.json', params, successFn, errorFn);
        }
    };
});