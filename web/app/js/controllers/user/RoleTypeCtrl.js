'use strict';
/**
 * RoleType Controller
 */
var tripSplitterCloneControllers = angular.module('tripSplitterCloneControllers');

tripSplitterCloneControllers.controller('RoleTypeCtrl',['$scope', '$rootScope', 'RoleTypeService',
	function($scope, $rootScope, RoleTypeService) {
	
		$scope.roleType = {};
		$scope.roleTypes = [];
		$scope.editing = false;
		
		$scope.functionsList = [];
		$scope.roleTypesList = [];
		$scope.roleTypeEnumsList = [];
		$scope.selected = [];
		$scope.searched = [];
		$scope.allSelected = false;
		
		$scope.actionsMap = {};
		$scope.actions = [];
		$scope.maxLevel = -1;
		$scope.orderRange = [];
		
		var paginationHelper;
		
		/**
		 * Ordering function for the sort.
		 */
		$scope.orderingFunction = function(actionA, actionB) {
			if (actionA.functions[0].menuOrder != null && actionB.functions[0].menuOrder == null) {
				return -1;
			} else if (actionA.functions[0].menuOrder == null && actionB.functions[0].menuOrder != null) {
				return 1;
			} else if (actionA.functions[0].menuOrder != null && actionB.functions[0].menuOrder != null) {
				return actionA.functions[0].menuOrder - actionB.functions[0].menuOrder;
			} else {
				var stop = false;
				var i = 0;
				while (!stop) {
					if (i < actionA.translatedNamesArray.length && i < actionB.translatedNamesArray.length) {
						if (actionA.translatedNamesArray[i] == actionB.translatedNamesArray[i]) {
							i++;
						} else {
							stop = true;
							if (actionA.translatedNamesArray[i] < actionB.translatedNamesArray[i]) {
								return -1;
							} else {
								return 1;
							}
						}
					} else if (i < actionA.translatedNamesArray.length) {
						// we can't compare, and we have a winner with the smallest name
						// here the actionA is bigger, so we return 1 (actionB wins)
						stop = true;
						return 1;
					} else {
						stop = true;
						// here the actionB is bigger, so we return 1 (actionA wins)
						return -1;
					}
				}
			}
		};
		
		/**
		 * Sets the menu order for all functions.
		 */
		$scope.changOrderForAllFunctions = function(action, menuOrder) {
			for (var i = 0; i < action.functions.length; i++) {
				action.functions[i].menuOrder = menuOrder;
			}
		};
		
		$scope.changeOrderByLevel = function (level, action, setNullOrder) {
			var order = setNullOrder ? null : action.functions[0].menuOrder;
			
			var collected = $.grep($scope.actions, function(toGrepAction, index) {
				if (level < toGrepAction.namesArray.length) {
					return action.namesArray[level] == toGrepAction.namesArray[level];
				} else {
					return false;
				}
			});
			$.each(collected, function() {
				$scope.changOrderForAllFunctions(this, order);
			});
			$scope.orderActions();
		};
		
		$scope.clearAllOrders = function () {
			$.each($scope.actions, function() {
				$.each(this.functions, function() {
					this.menuOrder = null;
				});
			});
			$scope.orderActions();
		};
		
		$scope.addOneToAll = function () {
			$.each($scope.actions, function() {
				$.each(this.functions, function() {
					if (this.menuOrder != null) {
						this.menuOrder++; 
					}
				});
			});
			$scope.orderActions();
		};
		
		$scope.subtractOneFromAll = function () {
			$.each($scope.actions, function() {
				$.each(this.functions, function() {
					if (this.menuOrder != null) {
						this.menuOrder--; 
					}
				});
			});
			$scope.orderActions();
		};
		
		/**
		 * Orders the current actions.
		 */
		$scope.orderActions = function() {
			$scope.actions.sort($scope.orderingFunction);
		};
		
		/**
		 * Watch for the change in the selected model.
		 */
		$scope.$watch(function() {
			return $scope.searched;
		}, function() {
			$scope.allSelected = $scope.searched.length > 0;
			
			$.each($scope.searched, function() {
				$scope.allSelected = $scope.allSelected && this.selected;
			});
		});
		
		/**
		 * Construct actions.
		 */
		$scope.constructActions = function() {
			$scope.actionsMap = {};
			$scope.actions = [];
			$scope.maxLevel = -1;
			
			$.each($scope.selected, function() {
				// the action already exists in the list
				if ($scope.actionsMap[this.constructedNamesArray[this.constructedNamesArray.length -2]]) {
					$scope.actionsMap[this.constructedNamesArray[this.constructedNamesArray.length -2]].functions.push(this);
				} else {
					// create the action
					var namesArray = [].concat(this.namesArray);
					namesArray.pop();
					var constructedNamesArray = [].concat(this.constructedNamesArray);
					constructedNamesArray.pop();
					var translatedNamesArray = [].concat(this.translatedNamesArray);
					translatedNamesArray.pop();
					var newAction = {
						namesArray: namesArray,
						constructedNamesArray: constructedNamesArray,
						translatedNamesArray: translatedNamesArray,
						functions: [this]
					};
					$scope.actions.push(newAction);
					$scope.actionsMap[this.constructedNamesArray[this.constructedNamesArray.length -2]] = newAction;
					
					if (namesArray.length > $scope.maxLevel) {
						$scope.maxLevel = namesArray.length; 
					}
				}
			});
			// sort the actions
			$scope.orderActions();
			$scope.orderRange = [];
			for (var i = 0; i < $scope.actions.length; i++) {
				$scope.orderRange.push(i);
			}
		};
		
		/**
		 * Forced refresh.
		 */
		$scope.refreshAllSelected = function() {
			$scope.allSelected = $scope.searched.length > 0;
			
			$.each($scope.searched, function() {
				$scope.allSelected = $scope.allSelected && this.selected;
			});
		};
		
		/**
		 * Searches for the functions that have the string on $scope.functionSearchTerm anywhere in it's path.
		 * If $scope.functionSearchTerm is null or blank, saves 0 functions.
		 */
		$scope.searchFunctions = function() {
			$("#functionSearch").attr("disabled", "true");
			$scope.searched = [];
			if ($scope.functionSearchTerm && $scope.functionSearchTerm.trim() != "" && $scope.functionSearchTerm.trim().length >= 5) {
				$.each($scope.functionsList, function() {
					var searched = false;
					
					for (var i = 0; i < this.namesArray.length; i++) {
						searched = searched ||
							this.namesArray[i].match(new RegExp("^.*" + $scope.functionSearchTerm + ".*$", "i")) ||
							this.translatedNamesArray[i].match(new RegExp("^.*" + $scope.functionSearchTerm + ".*$", "i"));
						
					}
					
					if (searched) {
						$scope.searched.push(this);
					}
				});
			}
			$("#functionSearch").removeAttr("disabled");
		};
		
		/**
		 * Changes the selection status for a list of functions.
		 */
		$scope.changeSelection = function(functions, selectionStatus) {
			$.each(functions, function() {
				this.selected = selectionStatus;
			});
			$scope.selected = $scope.getSelected();
		};
		
		/**
		 * Clears all selected functions.
		 */
		$scope.clearSelection = function() {
			$scope.changeSelection($scope.functionsList, false);
			$scope.refreshAllSelected();
		};
		
		/**
		 * Gets the selected functions.
		 */
		$scope.getSelected = function() {
			var functions = [];
			
			$.each($scope.functionsList, function() {
				if (this.selected) {
					functions.push(this);
				}
			});
			
			return functions;
		};
		
		$scope.clearOrder = function() {
			// refresh all orders
			$.each($scope.functionsList, function() {
				this.menuOrder = null;
			});
		};
		
		/**
		 * Selects the functions passed by parameter.
		 */
		$scope.selectFunctions = function(roleFunctions) {
			$scope.clearSelection();
			$scope.clearOrder();
			$.each(roleFunctions, function(ind, rlFn) {
				var functionArray = $.grep($scope.functionsList, function(fn) { return fn.fn.id == rlFn.functionId; });
				if (functionArray.length > 0) {
					functionArray[0].selected = true;
					functionArray[0].menuOrder = rlFn.menuOrder;
				}
			});
			// saves the selected functions to show
			$scope.selected = $scope.getSelected();
			$scope.refreshAllSelected();
		};

		/**
		 * Updates the functions of a role type.
		 */
		$scope.updateFunctions = function(roleType, functions) {
			var updatedRoleTypeFunctions = [];
			
			$.each(functions, function(ind, fn) {
				var roleTypeFnArray = $.grep(roleType.roleTypeFunctions, function(rtFn) { return rtFn.functionId == fn.fn.id; });
				if (roleTypeFnArray.length == 0) {
					updatedRoleTypeFunctions.push({functionId:fn.fn.id, enabled: true, menuOrder: fn.menuOrder});
				} else {
					var newFunc = roleTypeFnArray[0];
					newFunc.menuOrder = fn.menuOrder;
					updatedRoleTypeFunctions.push(newFunc);
				}
			});
			
			roleType.roleTypeFunctions = updatedRoleTypeFunctions;
		};
		
		/**
		 * List all the functions.
		 */
		RoleTypeService.listFunctions(function(response) {
			if (response.ok) {
				var functions = JSON.parse(response.data);
				$scope.functionsList = [];
				$.each(functions, function() {
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
					$scope.functionsList.push({fn: this, name: this.name, selected: false, options:[],
						namesArray: namesArray, constructedNamesArray: constructedNamesArray,
						translatedNamesArray: translatedNamesArray});
				});
			}
		}, $rootScope.manageError);
		
		/**
		 * Lists all the role types.
		 */
		RoleTypeService.listRoleTypes(function(response) {
			if (response.ok) {
				$scope.roleTypesList = JSON.parse(response.data);
			}
		}, $rootScope.manageError);
		
		/**
		 * Lists all the role type enums.
		 */
		RoleTypeService.listRoleTypeEnums(function(response) {
			if (response.ok) {
				$scope.roleTypeEnumsList = JSON.parse(response.data);
			}
		}, $rootScope.manageError);
		
		$scope.create = function() {
			$scope.roleType.roleTypeFunctions = [];
			
			$scope.updateFunctions($scope.roleType, $scope.getSelected());
			
			RoleTypeService.create($scope.roleType, function(response) {
				if (response.ok) {
					// role type successfully created
					var roleType = JSON.parse(response.data);
	
					$scope.roleTypes.push(roleType);
				}
			}, $rootScope.manageError);
		};
	
		$scope.list = function(pagination) {
			RoleTypeService.list(pagination, function(response) {
				if (response.ok) {
					var responseObject = JSON.parse(response.data);
					
					if (responseObject.page) {
						$scope.roleTypes = responseObject.items;
						paginationHelper.extendCallback(responseObject);
					} else {
						$scope.roleTypes = responseObject;
					}	
				}
			}, $rootScope.manageError);
		};
	
		$scope.edit = function(roleType) {
			$scope.editing = true;
			$scope.roleType = $rootScope.snapshot(roleType);
			$scope.roleTypes.splice($scope.roleTypes.indexOf(roleType), 1);
			$scope.selectFunctions(roleType.roleTypeFunctions);
		};
		
		$scope.cancel = function() {		
			$scope.roleTypes.push($scope.roleType.rollback());
			$scope.roleType = null;
			$scope.editing = false;
			$scope.clearSelection();
			$scope.clearOrder();
		};
		
		$scope.modify = function() {
			var roleType = $scope.roleType.unSnapshot();		
			
			$scope.updateFunctions(roleType, $scope.getSelected());
			
			RoleTypeService.modify(roleType, function(response) {
				if (response.ok) {
					var role = JSON.parse(response.data);
	
					$scope.roleTypes.push(role);
					$scope.editing = false;
					
					$scope.clearSelection();
					$scope.clearOrder();
					$scope.clean();
				}
			}, $rootScope.manageError);
		};
	
		$scope.clean = function() {
			$scope.roleType = {};
			$scope.clearSelection();
		};
		
		$scope.remove = function(roleType) {
			RoleTypeService.remove(roleType, function(response) {
				if (response.ok) {
					$scope.roleTypes.splice($scope.roleTypes.indexOf(roleType),
							1);
				}
			}, $rootScope.manageError);
		};
		
		$scope.editFunctions = function() {
			$("#editFunctions").modal("show");
		};
		$("#editFunctions").on("hidden.bs.modal", function() {
			$scope.$apply(function () {
				$scope.selected = $scope.getSelected();
			});			
		});
		
		$scope.orderFunctions = function() {
			$scope.constructActions();
			$("#orderFunctions").modal("show");
		};
		
		//add pagination capability
		paginationHelper = PaginationHelper($scope, 'roleTypeNameSpace', true);
		
		if ($rootScope.canAccess('/configuration/roleType:listRoleType')) {
			$scope.list();
		}
	}
]);