'use strict';
/**
 * Main App Module. Includes all other modules.
 */
angular.module('universeApp', ['ngRoute','universeControllers','filters', 'directives', 'routes', 'services','ngCookies','ui.bootstrap.typeahead','template/typeahead/typeahead-match.html','template/typeahead/typeahead-popup.html','ui.bootstrap.collapse','ui.bootstrap.tabs',"template/tabs/tab.html","template/tabs/tabset.html"]).
	config(['$routeProvider', function($routeProvider) {
	}])
	.run(['$rootScope', '$window', '$location', '$cookieStore', 'UniverseService', function ($rootScope, $window, $location, $cookieStore, UniverseService) {
		$rootScope.lang = defaultLang;
		$rootScope.canSubscribe = canSubscribe;
		
		UniverseService.post("/lang/l", "", function(res) {
			if (supported.indexOf(res) != -1) {
				defaultLang = res;
			}
			$rootScope.lang = defaultLang;
			_run();
		}, _run);
		
		/**
		 * Returns the constructed name for an array of names.
		 */
		$rootScope.constructName = function(index, array) {
			var toReturn = 'menu.';
			
			for (var i = 0; i < (index+1); i++) {
				toReturn += array[i] + '.';
			}
			
			return toReturn + 'name';
		};
		
		/**
		 * Translates Functions. 
		 */
		$rootScope.translateFunctions = function(roleFunctions) {
			$.each(roleFunctions, function() {
				var namesArray = [];
				if (this.menuName) {
					namesArray.push(this.menuName);
				}
				if (this.submenuName) {
					namesArray.push(this.submenuName);
				}
				namesArray.push(this.actionName);
				namesArray.push(this.name);
				
				var constructedNamesArray = [];
				var translatedNamesArray = [];
				for (var i = 0; i < namesArray.length; i++) {
					constructedNamesArray.push($rootScope.constructName(i, namesArray));
					translatedNamesArray.push(i18n.t(constructedNamesArray[i]));
				}
				this.translatedNamesArray = translatedNamesArray;
			});
		};
		
		/**
		 * Orders the role functions.
		 */
		$rootScope.orderRoleFunctions = function(roleFunctions) {
			roleFunctions.sort(function(roleFunctionA, roleFunctionB) {
				if (roleFunctionA.menuOrder != null && roleFunctionB.menuOrder == null) {
					return -1;
				} else if (roleFunctionA.menuOrder == null && roleFunctionB.menuOrder != null) {
					return 1;
				} else if (roleFunctionA.menuOrder != null && roleFunctionB.menuOrder != null) {
					return roleFunctionA.menuOrder - roleFunctionB.menuOrder;
				} else {
					var stop = false;
					var i = 0;
					while (!stop) {
						if (i < roleFunctionA.translatedNamesArray.length && i < roleFunctionB.translatedNamesArray.length) {
							if (roleFunctionA.translatedNamesArray[i] == roleFunctionB.translatedNamesArray[i]) {
								i++;
							} else {
								stop = true;
								if (roleFunctionA.translatedNamesArray[i] < roleFunctionB.translatedNamesArray[i]) {
									return -1;
								} else {
									return 1;
								}
							}
						} else if (i < roleFunctionA.translatedNamesArray.length) {
							// we can't compare, and we have a winner with the smallest name
							// here the roleFunctionA is bigger, so we return 1 (roleFunctionB wins)
							stop = true;
							return 1;
						} else {
							stop = true;
							// here the roleFunctionB is bigger, so we return 1 (roleFunctionA wins)
							return -1;
						}
					}
				}
			});
		};
		
		$rootScope.generatePermissions = function(user) {
			var permissions = [];
			var functions = user.role.roleFunctions;
			var index;
			
			$rootScope.translateFunctions(user.role.roleFunctions);
			$rootScope.orderRoleFunctions(user.role.roleFunctions);
			
			for (index = 0; index < user.role.roleFunctions.length; index++) {
				if (functions[index].enabled) {
					var menuPath = "";
					if (functions[index].menuName) {
						menuPath += "/" + functions[index].menuName;
					}
					if (functions[index].submenuName) {
						menuPath += "/" + functions[index].submenuName;
					}
					if (functions[index].actionName) {
						menuPath += "/" + functions[index].actionName;
					}
					if (functions[index].name) {
						menuPath += ":" + functions[index].name;
					}
					if (permissions.indexOf(menuPath) == -1) {
						permissions.push(menuPath);
					}		
				}
			}
			
			return permissions;
		};	
		
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
				
				if (path == "/login" || path == "/register") {
					if ($rootScope.loggedUser) {
						next.redirectTo = "/";
					}
				} else {
					if (!$rootScope.loggedUser) {
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
		$rootScope.permissions = [];
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
			return $rootScope.permissions.indexOf(uri) != -1;
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
			
			// generate permissions
			$rootScope.permissions = $rootScope.generatePermissions($rootScope.loggedUser);

			// sets the cookie with the user
			var userCookie = {};
			angular.copy($rootScope.loggedUser, userCookie);
			// before, delete all the big data so the cookie is not overflown
			userCookie.role = null;
			userCookie.roleType = null;
			userCookie.menuOrder = null;
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
			
			if (document.globalInspectorFlag) {
				i18nextOptions.resGetPath = '../locales/__lng__/__ns__.json';
			}
			
			i18n.init(i18nextOptions, function() {	
				document.title = $rootScope.systemTitle;
				// this flag is watched by the LoginCtrl, in order to construct the tooltip only
				// when the i18n system has been initialized
				$rootScope.readyToConstructTooltip = true;
				$rootScope.$digest();
			});	
			
			//if ($rootScope.loggedUser) {
			//	LoginService.getLoggedUser(
			//		function(response){
			//			if (response.ok) {
			//				$rootScope.loggedUser = JSON.parse(response.data);
			//				$rootScope.permissions = $rootScope.generatePermissions($rootScope.loggedUser);
			//			}
			//		},
			//		function(){
			//
			//	});
			//}
			
		}
		
	}]);