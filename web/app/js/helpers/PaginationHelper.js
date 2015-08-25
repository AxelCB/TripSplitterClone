/**
 * Adds pagination capability to a scope in a certain namespace.
 * 
 * Requires that the scopes has the 'list' and 'search' methods defined.
 */
var PaginationHelper = function(scope, nameSpace, fetchTotal, listFn, searchFn, local) {
	
	//creates the namespace object in the scoped
	scope[nameSpace] = {};
	
	//assumes there are 'list' and 'search' functions in the scope
	scope[nameSpace].name = nameSpace;
	scope[nameSpace].page = 1;
	scope[nameSpace].total = 0;
	scope[nameSpace].pageTotal = 0;
	scope[nameSpace].itemsPerPage = 0;
	scope[nameSpace].currentFunction = scope.list;
	scope[nameSpace].fetchTotal = fetchTotal;
	scope[nameSpace].local = local;
	
	if (!local) {
		var auxListFn = listFn ? listFn : 'list';
		var auxSearchFn = searchFn ? searchFn : 'search';
		var _auxListFn = '_' + auxListFn;
		var _auxSearchFn = '_' + auxSearchFn;
		
		scope[_auxListFn] = scope[auxListFn];
		scope[_auxSearchFn] = scope[auxSearchFn];
		
		//we wrap the list function in a paginated list function
		scope[auxListFn] = function (paginationObject) {
			scope[nameSpace].currentFunction = scope[auxListFn];
			if (!paginationObject) {
				paginationObject = {page:1, fetchTotal: fetchTotal};
			}
			scope[_auxListFn](paginationObject);
		};
		
		//we wrap the list function in a paginated list function
		scope[auxSearchFn] = function (paginationObject) {
			scope[nameSpace].currentFunction = scope[auxSearchFn];
			if (!paginationObject) {
				paginationObject = {page:1, fetchTotal: fetchTotal};
			}
			scope[_auxSearchFn](paginationObject);
		};
	} else {
		scope[nameSpace].updateLocalItems = function() {
			var start = (scope[nameSpace].page - 1) * scope[nameSpace].itemsPerPage;
			var end = start + scope[nameSpace].itemsPerPage;
			end = Math.min(end, scope[nameSpace].internalItems.length);
			scope[nameSpace].pageTotal = end - start;
			scope[scope[nameSpace].itemsReference] = scope[nameSpace].internalItems.slice(start, end);
		};
	}
	
	return {
		extendCallback: function(responseObject) {
			scope[nameSpace].page = responseObject.page;
			scope[nameSpace].total = responseObject.totalItems;
			scope[nameSpace].pageTotal = responseObject.items.length;
			scope[nameSpace].itemsPerPage = responseObject.itemsPerPage;
		},
		
		activateLocalPagination: function(itemsReference, itemsPerPage) {
			scope[nameSpace].itemsPerPage = itemsPerPage;
			scope[nameSpace].pageTotal = Math.min(itemsPerPage, scope[itemsReference].length);
			scope[nameSpace].total = scope[itemsReference].length;
			scope[nameSpace].itemsReference = itemsReference;
			scope[nameSpace].local = true;
			
			// copies entire array
			scope[nameSpace].internalItems = scope[itemsReference].slice(0);
			scope[nameSpace].updateLocalItems();
		}
	};
};