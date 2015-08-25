'use strict';
/**
 * Custom Filters Module
 */
angular.module('filters', [])

.filter('interpolate', [ 'version', function(version) {
	return function(text) {
		return String(text).replace(/\%VERSION\%/mg, version);
	};
} ])

// i18n
.filter('translate', function() {
	return function(text) {
		if (text)
			return i18n.t(text);
		else
			return "i18 - NOT FOUND";
	};
})

// Add zero's to the beginning of a number
.filter(
		'twoFrontZeros',
		function() {
			return function(number) {
				return (number < 10) ? ((!(parseInt(number / 10))) ? "00"
						+ number : "0" + number) : number;
			};
		})

// iif filter
.filter('iif', function() {
	return function(input, trueValue, falseValue) {
		return input ? trueValue : falseValue;
	};
})
// coalesce filter
.filter('coalesce', function() {
	return function(input, falseValue) {
		return input ? input : falseValue;
	};
})
// date format filter
.filter(
		'dateFormat',
		function() {
			return function(input, format, addSeconds, addMilliseconds) {

				var auxDate = input;
				if (typeof input == "string") {
					auxDate = DateFormatHelper.stringToDateTime(input);
				}

				switch (format) {
				case "date":
					return DateFormatHelper.dateToString(auxDate);
					break;
				case "time":
					return DateFormatHelper.timeToString(auxDate, addSeconds,
							addMilliseconds);
					break;
				case "dateTime":
					return DateFormatHelper.dateTimeToString(auxDate,
							addSeconds, addMilliseconds);
					break;
				case "month":
					return DateFormatHelper.dateToString(auxDate, true);
					break;
				default:
					return "no-format-selected";
					break;
				}
			};
		})

// replace filter
.filter('replace', function() {
	return function(input, regexp, regexpmodifiers, replacement) {
		return input.replace(new RegExp(regexp, regexpmodifiers), replacement);
	};
})
// to date filter
.filter('toDate', function() {
	return function(input) {
		if (typeof input == "string") {
			return DateFormatHelper.stringToDateTime(input);
		}else{
			return input;
		}
	};
})
//concat fiter
.filter('concat', function(){
	return function(input, before, after) {
		return before + '' + input + '' + after;
	};
}).filter('clip', function(){
	return function(input, start, length) {
		var newString = input.substr(start, length);
		return newString + ((newString.length < input.length) ? "..." : "");
	};
})
//integer range filter
.filter('range', function () {
        return function (input, max,min) {
        	input = [];
        	min = min || 1;
//            min = parseInt(min); //Make string input int
//            max = parseInt(max);
            for (var i = min; i <= max; i++)
                input.push(i); //.toString().padLeft(2, "0")
            return input;
        };
});