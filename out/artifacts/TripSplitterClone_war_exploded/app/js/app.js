'use strict';
/**
 * Main App Module. Includes all other modules.
 */
angular.module('tripSplitterCloneApp', ['ngRoute','tripSplitterCloneControllers','filters', 'directives', 'routes', 'services','ngCookies','ui.bootstrap.typeahead','template/typeahead/typeahead-match.html','template/typeahead/typeahead-popup.html','ui.bootstrap.collapse','ui.bootstrap.tabs',"template/tabs/tab.html","template/tabs/tabset.html"]).
	config(['$routeProvider', function($routeProvider) {
	}])
	.run(['$rootScope', '$window', '$location', '$cookieStore', function ($rootScope, $window, $location, $cookieStore) {
		$rootScope.lang = defaultLang;
		$rootScope.canSubscribe = canSubscribe;
		
		//UniverseService.post("/lang/l", "", function(res) {
		//	if (supported.indexOf(res) != -1) {
		//		defaultLang = res;
		//	}
		//	$rootScope.lang = defaultLang;
		//	_run();
		//}, _run);
		

		$rootScope.$on('$routeChangeStart', function (event, next, current) {
			$("body").removeClass("modal-open");
			$(".modal-backdrop").remove();
			if (next.redirectTo == undefined) {
				//it's a real path
				
				//cleans previous messages from previous page
				if ($rootScope.keepMessages) {
					$rootScope.keepMessages = false;
				} else {
					$rootScope.messages = [];
				}
				
				var path = $location.path();
				var auxArray = path.substr(1, path.length).split("/");
				$rootScope.currentArray = [];
				if (auxArray.length > 0) {
					$rootScope.currentArray.push(auxArray[0]);
					for (var i = 1; i < auxArray.length; i++) {
						$rootScope.currentArray.push($rootScope.currentArray[i-1] + "." + auxArray[i]);
					}
				}
				
				if (path == "/login") {
					if ($rootScope.loggedUser) {
						next.redirectTo = "/";
					}
				} else {
					if (!$rootScope.loggedUser && path != "/register") {
						next.redirectTo = "/login";
					}
				}
			}
		});
		
		$rootScope.dim = function() {
			//$("#processing").css("display", "block");
			document.getElementById("processing").style.display = 'block';
		};
		
		$rootScope.unDim = function() {
			//$("#processing").css("display", "none");
			document.getElementById("processing").style.display = 'none';
		};
		
		$rootScope.swap = function(vector, i, j) {
			var temp = vector[i];
			vector[i] = vector[j];
			vector[j] = temp;
		};
		
		$rootScope.messages = [];
		$rootScope.areErrorMessages = false;
		$rootScope.keepMessages = false;
		$rootScope.loggedUser = $cookieStore.get("loggedUser");
		
		$rootScope.go = function(path, removeParams) {
			if (removeParams) {
				$location.url(path);
			} else {
				$location.path(path);
			}
		};
		
		$rootScope.manageError = function () {
			$rootScope.unDim();
			if (!$rootScope.messages) {
				$rootScope.messages = [];
			}
			$rootScope.messages.push(i18n.t('default.server.error'));
			$rootScope.areErrorMessages = true;
		};
		
		$rootScope.errorManager = $rootScope.manageError;
		
		$rootScope.canAccess = function(uri) {
			return true;
			//$rootScope.permissions.indexOf(uri) != -1;
		};
		

		$rootScope.showErrorMessage = function(message, error) {
			$rootScope.areErrorMessages = error;
			$rootScope.messages = [message];
		};
		
		$rootScope.clearMessages = function() {
			$rootScope.areErrorMessages = false;
			$rootScope.messages = [];
		};
		
		/**
		 * Performs the login on the client side, once the login on the server
		 * was successful.
		 * 
		 * @param loggedUser the loggedUser (as object)
		 * @param rememberMe the rememberMe flag
		 * @param checkRememberMe flag, if true, the rememberMe flag will be consulted and used
		 * 
		 */
		$rootScope.performLogin = function(loggedUser, rememberMe, checkRememberMe) {
			// save the logged user to the rootscope (for global accessing)
			$rootScope.loggedUser = loggedUser;
			
			// sets the cookie with the user
			var userCookie = {};
			angular.copy($rootScope.loggedUser, userCookie);

			// store the cookie
			$cookieStore.put("loggedUser", userCookie);
			
			// manage the remember me cookie if necessary
			if (checkRememberMe) {
				if (rememberMe) {
					$cookieStore.put("userCredentials", $scope.userCredentials);
				} else {
					$cookieStore.remove("userCredentials");
				}
			}
			
			// un-dim the screen
			$rootScope.unDim();
			
			// redirects to the /
			$location.path("/");
		};
		
		/**
		 * Adds functionality to the object to be able to rollback it's status 
		 */
		$rootScope.snapshot = function(ob) {
			ob._snapshot = {};
			ob._snapshot = angular.copy(ob, ob._snapshot);
			
			ob.rollback = function() {
				ob = ob._snapshot;
				
				return ob;
			};
			
			ob.unSnapshot = function() {
				delete ob['_snapshot'];
				return ob;
			};
			
			return ob;
		};
		
		/**
		 * Formats the Time part of a Date Object to the formats
		 * 
		 * HH:mm
		 * HH:mm:ss
		 * HH:mm:ss.SSS
		 * 
		 * addSeconds and addMilliseconds controls the precision of the formatting.
		 * 
		 */
		$rootScope.timeToString = function(date, addSeconds, addMilliseconds) {
			return DateFormatHelper.timeToString(date, addSeconds, addMilliseconds);
		};
		
		/**
		 * Formats the date part of a Date Object to the format: dd/MM/yyyy
		 */
		$rootScope.dateToString = function (date) {
			return DateFormatHelper.dateToString(date);
		};
		
		/**
		 * Formats the Date and the Time part of a Date Object to the formats:
		 * 
		 * HH:mm
		 * HH:mm:ss
		 * HH:mm:ss.SSS
		 * 
		 * addSeconds and addMilliseconds controls the precision of the formatting.
		 */
		$rootScope.dateTimeToString = function (date, addSeconds, addMilliseconds) {
			return DateFormatHelper.dateTimeToString(date, addSeconds, addMilliseconds);
		};
		
		/**
		 * Formats the Date and the Time part of a Date Object to the format: HH:mm:ss.SSS.
		 */
		$rootScope.fullDateTimeToString = function(date) {
			return DateFormatHelper.fullDateTimeToString(date);
		};
		
		/**
		 * Parses a Date string of any of the formats:
		 * 
		 * HH:mm
		 * HH:mm:ss
		 * HH:mm:ss.SSS
		 * 
		 *  (in this case the date part is set to 01/01/1970)
		 * 
		 * into a Date Object
		 */
		$rootScope.stringToTime = function(timeString) {
			return DateFormatHelper.stringToTime(timeString);
		};
		
		/**
		 * Parses a Date string of the format:
		 * 
		 * dd/MM/yyyy (in this case the time part is set to 00:00:00.000)
		 * 
		 * into a Date object
		 */
		$rootScope.stringToDate = function(dateString) {
			return DateFormatHelper.stringToDate(dateString);
		};
		
		/**
		 * Parses a Date string of any of the formats:
		 * 
		 * dd/MM/yyyy (in this case the time part is set to 00:00:00.000)
		 * HH:mm (in this case the date part is set to 01/01/1970)
		 * HH:mm:ss (in this case the date part is set to 01/01/1970)
		 * HH:mm:ss.SSS (in this case the date part is set to 01/01/1970)
		 * dd/MM/yyyy HH:mm
		 * dd/MM/yyyy HH:mm:ss
		 * dd/MM/yyyy HH:mm:ss.SSS
		 * 
		 * into a Date Object.
		 */
		$rootScope.stringToDateTime = function (dateTimeString) {
			return DateFormatHelper.stringToDateTime(dateTimeString);
		};
		
		function _run() {
			//i18n object initialization
			var i18nextOptions = {
				resGetPath: 'locales/__lng__/__ns__.json',
				load: 'unspecific',
				lng: defaultLang,
				fallbackLng: defaultLang,
				useCookie: false,
				supportedLngs: supported
			};
			
			i18n.init(i18nextOptions, function() {	
				document.title = "Trip Splitter Clone"; //$rootScope.systemTitle
				// this flag is watched by the LoginCtrl, in order to construct the tooltip only
				// when the i18n system has been initialized
				$rootScope.readyToConstructTooltip = true;
				$rootScope.$digest();
			});
			
		}
		
	}]);