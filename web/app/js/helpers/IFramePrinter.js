/**
 * IFramePrinter.
 * v. 1.0
 * 30/07/2014
 * 
 * Enables cross-browser printing of iframes.
 * 
 * Usage:
 * 		
 * 		// will print iframe with HTML id "iframeId"
 * 		IFramePrinter.print("iframeId");
 * 
 * Tested on:
 * 		- Google Chrome: 
 * 			v: 35.0.1916.153 m
 * 		- Internet Explorer:
 * 			v: 11.0.9600.17207
 * 			v: 10.0 (emulated on IE 11.0.9600.17207)
 * 			v:  9.0 (emulated on IE 11.0.9600.17207)
 * 			v:  8.0 (emulated on IE 11.0.9600.17207)
 * 			v:  7.0 (emulated on IE 11.0.9600.17207)
 * 		- Mozilla Firefox:
 * 			v: 32.0
 * 
 * @author fgonzalez
 * 
 */
var IFramePrinter = {
	// IE browser flag, we need to know this in order to print the IFrame in IE versions
	// (from v. 11, will not print the IFrame if it's not visible and prints the parent instead;
	//  uses the 'execCommand' command for this to work)
	ie: (navigator.userAgent.indexOf("MSIE") != -1) || (navigator.userAgent.indexOf("Trident/7") != -1)
	//TODO: this will need revision/updating as long as new IE versions come along
};

/**
 * Print an IFrame given by its ID.
 * 
 * @params iframeID the ID of the iframe to print
 * 
 */
IFramePrinter.printIFrame = function (iframeId) {
	
	// uses document.getElementById for maximum compatibility
	var iframeElement = document.getElementById(iframeId);
	
	if (IFramePrinter.ie) {
		// it's IE!, uses execCommand
		iframeElement.contentWindow.document.execCommand("print", false, null);
	} else {
		// uses the preferred method
		iframeElement.contentWindow.print();
	}
};