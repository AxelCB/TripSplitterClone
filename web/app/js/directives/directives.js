'use strict';
/** 
 * Custom Directives Module 
 */
angular.module('directives', [])
	.directive('ngEnter', function() {
		
	    return function(scope, elm, attrs) {
	        elm.bind('keypress', function(e) {
	            if (e.charCode === 13) {
	            	scope.$apply(attrs.ngEnter); 
            	} else if (e.keyCode === 13) {
            		scope.$apply(attrs.ngEnter);
            	}
	        });
	    };
    })
    .directive('ngPaginator', function() {
	    return {
    		templateUrl: "app/views/layout/pagination.html",
    		replace: true,
    		scope: {
    			pagElements: "=elements",
    			pagNameSpace: "=nameSpace"
    		},
    		link: function(scope, elm, attrs) {
    			scope.pagNameSpace.pages = 5;
    			scope.pagNameSpace.inputPage = scope.pagNameSpace.page;
    			
    			scope.$watch(function(){return scope.pagNameSpace.inputPage;}, function() {
    				var min = 20;
    				var valString = "" + scope.pagNameSpace.inputPage;
    				var newWidth = (valString.length + 1) * 8;
    				newWidth = newWidth < min ? min : newWidth;
    				$("#" + scope.pagNameSpace.name + 'InputId').css("width", newWidth + "px");
    			});
    			
    			scope.$watch(function(){return scope.pagNameSpace.page;}, function() {
    				scope.pagNameSpace.inputPage = scope.pagNameSpace.page;
    			});
    			
    			scope.$watch(function(){return scope.pagNameSpace.total;}, function() {
    				var pages;
    				pages = Math.ceil(scope.pagNameSpace.total / scope.pagNameSpace.itemsPerPage);
    				if (isNaN(pages)) {
    					pages = 5; //TODO: parametize
    				}
    				scope.pagNameSpace.pages = pages;
    			});
    			
    			scope.goToPage = function(pageNumber) {
    				if (pageNumber >=1 && pageNumber <= scope.pagNameSpace.pages) {
    					if (scope.pagNameSpace.local) {
    						scope.pagNameSpace.page = pageNumber;
    						scope.pagNameSpace.updateLocalItems();
    					} else {
    						scope.pagNameSpace.currentFunction({page: pageNumber, fetchTotal: scope.pagNameSpace.fetchTotal});
    					}
    				} else {
    					scope.pagNameSpace.inputPage = scope.pagNameSpace.page;
    				}
    			};
    			
    			scope.next = function() {
    				scope.goToPage(scope.pagNameSpace.page + 1);
    			};
    			
    			scope.previous = function() {
    				scope.goToPage(scope.pagNameSpace.page - 1);
    			};
    			
    			scope.first = function() {
    				scope.goToPage(1);
    			};
    			
    			scope.last = function() {
    				scope.goToPage(scope.pagNameSpace.pages);
    			};
    			
    			scope.enterPage = function() {
    				scope.goToPage(scope.pagNameSpace.inputPage);
    			};
    			
    			scope.blurInput = function() {
    				scope.pagNameSpace.inputPage = scope.pagNameSpace.page;
    			};
	    	}
    	};
    })
    .directive('currencyInput', function() {
	    return {
	        restrict: 'A',
	        scope: {
	            field: '='
	        },
	        replace: true,
	        template: '<input type="text" ng-model="field" ng-class="{positive : (field >0), negative: (field< 0) }"></input>',
	        
	        link: function(scope, element, attrs) {
	
	            $(element).bind('keyup', function(e) {
	                var input = element.find('input');
	                var inputVal = input.val();
	
	                //clearing left side zeros
	                while (scope.field.charAt(0) == '0') {
	                    scope.field = scope.field.substr(1);
	                }
	
	                scope.field = scope.field.replace(/[^\d.\',']/g, '');
	
	                var point = scope.field.indexOf(".");
	                if (point >= 0) {
	                    scope.field = scope.field.slice(0, point + 3);
	                }
	
	                var decimalSplit = scope.field.split(".");
	                var intPart = decimalSplit[0];
	                var decPart = decimalSplit[1];
	
	                intPart = intPart.replace(/[^\d]/g, '');
	                if (intPart.length > 3) {
	                    var intDiv = Math.floor(intPart.length / 3);
	                    while (intDiv > 0) {
	                        var lastComma = intPart.indexOf(",");
	                        if (lastComma < 0) {
	                            lastComma = intPart.length;
	                        }
	
	                        if (lastComma - 3 > 0) {
	                            intPart = intPart.splice(lastComma - 3, 0, ",");
	                        }
	                        intDiv--;
	                    }
	                }
	
	                if (decPart === undefined) {
	                    decPart = "";
	                }
	                else {
	                    decPart = "." + decPart;
	                }
	                var res = intPart + decPart;
	
	                scope.$apply(function() {scope.field = res});
	
	            });
	
	        }
	    };
	})
	.directive('ngDatepicker', function($parse) {
		return function(scope, element, attrs) {
			$(element).datetimepicker({
				language : 'es',
				pickTime : false,
				pick12HourFormat: false,
				startDate: $parse(attrs.startDate)(scope),
				endDate: $parse(attrs.endDate)(scope),
				disabled: $parse(attrs.ngStartDisabled)(scope)
			}).on('change.dp', function(ev) {
				scope.$apply( function(scope){
					
		    		var ngModel = $parse(attrs.ngModel);
		    		var ngChange = $parse(attrs.ngChange);
		    		
		    		if (ev.date != null) {
		    			ngModel.assign(scope, ev.date.toDate());
		    			ngChange(scope);
		    		}
	    		});
	    	});
			var ngModel = $parse(attrs.ngModel);
			var maxDate = $parse(attrs.endDate);
			
			scope.$watch(function(){
				return ngModel(scope);
			}, function(){
				$(element).datetimepicker().data("DateTimePicker").setDate(ngModel(scope));
			});
			scope.$watch(function(){
				return maxDate(scope);
			}, function(){
				$(element).datetimepicker().data("DateTimePicker").setEndDate(maxDate(scope));
			});
			
	  };
  })
  .directive('ngTimepicker', function($parse) {
		return function(scope, element, attrs) {
			$(element).datetimepicker({
				language : 'es',
				pickTime : true,
				pick12HourFormat: false,
				pickDate: false,
				pickSeconds: false,
				startDate: $parse(attrs.startDate)(scope),
				endDate: $parse(attrs.endDate)(scope)
			}).on('change.dp', function(ev) {
				scope.$apply( function(scope){
					
		    		var ngModel = $parse(attrs.ngModel);
		    		var ngChange = $parse(attrs.ngChange);
		    		
		    		if (ev.date != null) {
		    			ngModel.assign(scope, ev.date.toDate());
		    			ngChange(scope);
		    		}
		    		
	    		});
	    	});
			var ngModel = $parse(attrs.ngModel);
			$(element).datetimepicker().data("DateTimePicker").setDate(ngModel(scope));
	  };
  })
  .directive('ngDatetimepicker', function($parse) {
		return function(scope, element, attrs) {
			$(element).datetimepicker({
				language : 'es',
				pickTime : true,
				pick12HourFormat: false,
				pickDate: true,
				pickSeconds: false,
				startDate: $parse(attrs.startDate)(scope),
				endDate: $parse(attrs.endDate)(scope)
			}).on('change.dp', function(ev) {
				scope.$apply( function(scope){
					
		    		var ngModel = $parse(attrs.ngModel);
		    		var ngChange = $parse(attrs.ngChange);
		    		
		    		if (ev.date != null) {
		    			ngModel.assign(scope, ev.date.toDate());
		    			ngChange(scope);
		    		}
	    		});
	    	});
			var ngModel = $parse(attrs.ngModel);
			$(element).datetimepicker().data("DateTimePicker").setDate(ngModel(scope));
	  };
  })
  .directive('removeLater', [ '$timeout', function($timeout) {
	  var def = {
		  restrict : 'A',
		  link : function(scope, element, attrs) {
			  if (scope.$last === true) {				  
				  $timeout(function() {
					  $(".removeLater").each(function(idx, elm) {
						  $(elm).parent().append($(elm).children());
					  });
					  $(".removeLater").remove();
					  
					  $(".divider").each(function(idx, elm) {
						  var next = $(elm).next();
						  next = next.size() == 0 ? null : next.get(0);
						  
						  if (next == null || $(next).hasClass("divider")) {
							  $(elm).remove();
						  }
					  });					  
				  }, 0);
			  }
		  }
	  };
	  return def;
  }]).directive('ngBlur', function() {
	  return function( scope, elem, attrs ) {
		  elem.bind('blur', function() {
			  scope.$apply(attrs.ngBlur);
		  });
	  };
  }).directive('ngColspan', function($parse) {
	  return function( scope, elem, attrs ) {
		  var colspan = $parse(attrs.ngColspan);
		  scope.$watch(function() {
			  return colspan(scope);
		  }, function() {
			  $(elem).attr("colspan", "" + colspan(scope)); 
		  });
	  };
  });