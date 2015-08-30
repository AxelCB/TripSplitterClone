/**
 * Routes Definitions Module
 */
angular.module('routes', []).config(['$routeProvider', function($routeProvider) {

	var prefix = '';
	
	//routes definitions
	var routes = [
	    {path: '/login', template: prefix + 'app/views/login.html', controller: 'LoginCtrl'},
	    {path: '/register', template: prefix + 'app/views/register.html', controller: null},//'RegisterCtrl'
		{path: '/', template: prefix + 'app/views/home.html', controller: 'HomeCtrl'},
		{path: '/test', template: prefix + 'app/views/admin/test.html', controller: 'TestCtrl'},
		{path: '/trip/new', template: prefix + 'app/views/trip/createTrip.html', controller: 'TripCreateCtrl'},
		{path: '/trip', template: prefix + 'app/views/trip/trips.html', controller: 'TripCtrl'},


		{path: '/404', template: prefix + 'app/views/404.html', controller: 'NoPageCtrl'}
    ];

	//for every route definition in the array, creates an Angular route definition
	$.each(routes, function() {
		var newPath = {
			templateUrl: this.template,
			controller: this.controller
		};
		if (this.resolve) {
			newPath.resolve = this.resolve;
		}
		
		$routeProvider.when(this.path, newPath);
    });
	//in any other case, redirects to the main view
	$routeProvider.otherwise({redirectTo:'/404'});

}]);