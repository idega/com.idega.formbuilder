/************************************************************************************************************

	CUSTOM DRAG AND DROP SCRIPT
	This script is a part of DHTMLSuite for application which will be released before christmas 2006.
	(C) www.dhtmlgoodies.com, August 2006

	This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.	
	
	Terms of use:
	Look at the terms of use at http://www.dhtmlgoodies.com/index.html?page=termsOfUse
	
	Thank you!
	
	www.dhtmlgoodies.com
	Alf Magne Kalleland

************************************************************************************************************/


/**
 * 
 * @package DHTMLSuite for applications
 * @copyright Copyright &copy; 2006, www.dhtmlgoodies.com
 * @author Alf Magne Kalleland <post@dhtmlgoodies.com>
 */


/************************************************************************************************************
*
* Global variables
*
************************************************************************************************************/

var standardObjectsCreated = false;	// The classes below will check this variable, if it is false, default help objects will be created
var clientInfoObj;	// Object of class DHTMLSuite_clientInfo
var dhtmlSuiteConfigObj = false; 	// Object of class DHTMLsuite_config
var dhtmlSuiteCommonObj;	// Object of class DHTMLSuite_common

// {{{ DHTMLsuite_createStandardObjects()
/**
 * Create objects used by all scripts
 *
 * @public
 */
    
function DHTMLsuite_createStandardObjects()
{
	clientInfoObj = new DHTMLSuite_clientInfo();	// Create browser info object
	clientInfoObj.init();	
	if(!dhtmlSuiteConfigObj){	// If this object isn't allready created, create it.
		dhtmlSuiteConfigObj = new DHTMLsuite_config();	// Create configuration object.
		dhtmlSuiteConfigObj.init();
	}
	dhtmlSuiteCommonObj = new DHTMLSuite_common();	// Create configuration object.
	dhtmlSuiteCommonObj.init();
}

/************************************************************************************************************
*	Configuration class used by most of the scripts
*
*	Created:			August, 19th, 2006
*	Purpose of class:	Store global variables/configurations used by the classes below. Example: If you want to  
*						change the path to the images used by the scripts, change it here. An object of this   
*						class will always be available to the other classes. The name of this object is 
*						"dhtmlSuiteConfigObj".	
*			
*						If you want to create an object of this class manually, remember to name it "dhtmlSuiteConfigObj"
*						This object should then be created before any other objects. This is nescessary if you want
*						the other objects to use the values you have put into the object. 
* 	Update log:
*
************************************************************************************************************/

// {{{ DHTMLsuite_config()
/**
 * Constructor
 *
 * @public
 */
function DHTMLsuite_config()
{
	var imagePath;	// Path to images used by the classes. 
	var cssPath;	// Path to CSS files used by the DHTML suite.		
}


DHTMLsuite_config.prototype = {
	// {{{ init()
	/**
	 *
	 * @public
	 */
	init : function()
	{
		this.imagePath = 'images_dhtmlsuite/';	// Path to images		
		this.cssPath = 'css_dhtmlsuite/';	// Path to images		
	}	
	// }}}
	,
	// {{{ setCssPath()
    /**
     * This method will save a new CSS path, i.e. where the css files of the dhtml suite are located.
     *
     * @param string newCssPath = New path to css files
     * @public
     */
    	
	setCssPath : function(newCssPath)
	{
		this.cssPath = newCssPath;
	}
	// }}}
	,
	// {{{ setImagePath()
    /**
     * This method will save a new image file path, i.e. where the image files used by the dhtml suite ar located
     *
     * @param string newImagePath = New path to image files
     * @public
     */
	setImagePath : function(newImagePath)
	{
		this.imagePath = newImagePath;
	}
	// }}}
}

/************************************************************************************************************
*	A class with general methods used by most of the scripts
*
*	Created:			August, 19th, 2006
*	Purpose of class:	A class containing common method used by one or more of the gui classes below, 
* 						example: loadCSS. 
*						An object("dhtmlSuiteCommonObj") of this  class will always be available to the other classes. 
* 	Update log:
*
************************************************************************************************************/

// {{{ DHTMLSuite_common()
/**
 * Constructor
 *
 */
function DHTMLSuite_common()
{
	var loadedCSSFiles;	// Array of loaded CSS files. Prevent same CSS file from being loaded twice.
}

DHTMLSuite_common.prototype = {
	
	// {{{ init()
    /**
     * This method initializes the DHTMLSuite_common object.
     *
     * @public
     */
    	
	init : function()
	{
		this.loadedCSSFiles = new Array();
	}	
	// }}}
	,
	// {{{ loadCSS()
    /**
     * This method loads a CSS file(Cascading Style Sheet) dynamically - i.e. an alternative to <link> tag in the document.
     *
     * @param string cssFileName = New path to image files
     * @public
     */
	
	loadCSS : function(cssFileName)
	{
		if(!this.loadedCSSFiles[cssFileName]){
			var linkTag = document.createElement('LINK');
			linkTag.href = dhtmlSuiteConfigObj.cssPath + cssFileName;
			linkTag.rel = 'stylesheet';
			linkTag.media = 'screen';
			linkTag.type = 'text/css';
			document.body.appendChild(linkTag);	
			this.loadedCSSFiles[cssFileName] = true;
		}
	}	
	// }}}
	,
	// {{{ getTopPos()
    /**
     * This method will return the top coordinate(pixel) of an object
     *
     * @param Object inputObj = Reference to HTML element
     * @public
     */	
	getTopPos : function(inputObj)
	{		
	  var returnValue = inputObj.offsetTop;
	  while((inputObj = inputObj.offsetParent) != null){
	  	if(inputObj.tagName!='HTML'){
	  		returnValue += inputObj.offsetTop;
	  		if(document.all)returnValue+=inputObj.clientTop;
	  	}
	  } 
	  return returnValue;
	}
	// }}}
	
	,
	// {{{ getLeftPos()
    /**
     * This method will return the left coordinate(pixel) of an object
     *
     * @param Object inputObj = Reference to HTML element
     * @public
     */	
	getLeftPos : function(inputObj)
	{	  
	  var returnValue = inputObj.offsetLeft;
	  while((inputObj = inputObj.offsetParent) != null){
	  	if(inputObj.tagName!='HTML'){
	  		returnValue += inputObj.offsetLeft;
	  		if(document.all)returnValue+=inputObj.clientLeft;
	  	}
	  }
	  return returnValue;
	}
	// }}}
	,
	
	// {{{ getCookie()
    /**
     *
     * 	These cookie functions are downloaded from 
	 * 	http://www.mach5.com/support/analyzer/manual/html/General/CookiesJavaScript.htm
	 *
     *  This function returns the value of a cookie
     *
     * @param String name = Name of cookie
     * @param Object inputObj = Reference to HTML element
     * @public
     */	
	getCookie : function(name) { 
	   var start = document.cookie.indexOf(name+"="); 
	   var len = start+name.length+1; 
	   if ((!start) && (name != document.cookie.substring(0,name.length))) return null; 
	   if (start == -1) return null; 
	   var end = document.cookie.indexOf(";",len); 
	   if (end == -1) end = document.cookie.length; 
	   return unescape(document.cookie.substring(len,end)); 
	} 	
	// }}}
	,	
	
	// {{{ setCookie()
    /**
     *
     * 	These cookie functions are downloaded from 
	 * 	http://www.mach5.com/support/analyzer/manual/html/General/CookiesJavaScript.htm
	 *
     *  This function creates a cookie. (This method has been slighhtly modified)
     *
     * @param String name = Name of cookie
     * @param String value = Value of cookie
     * @param Int expires = Timestamp - days
     * @param String path = Path for cookie (Usually left empty)
     * @param String domain = Cookie domain
     * @param Boolean secure = Secure cookie(SSL)
     * 
     * @public
     */	
	setCookie : function(name,value,expires,path,domain,secure) { 
		expires = expires * 60*60*24*1000;
		var today = new Date();
		var expires_date = new Date( today.getTime() + (expires) );
	    var cookieString = name + "=" +escape(value) + 
	       ( (expires) ? ";expires=" + expires_date.toGMTString() : "") + 
	       ( (path) ? ";path=" + path : "") + 
	       ( (domain) ? ";domain=" + domain : "") + 
	       ( (secure) ? ";secure" : ""); 
	    document.cookie = cookieString; 
	}
	// }}}
	,
	// {{{ cancelEvent()
    /**
     *
     *  This function only returns false. It is used to cancel selections and drag
     *
     * 
     * @public
     */	
    	
	cancelEvent : function()
	{
		return false;
	}
	// }}}	
	
}


/************************************************************************************************************
*	Client info class
*
*	Created:			August, 18th, 2006
*	Purpose of class:	Provide browser information to the classes below. Instead of checking for
*						browser versions and browser types in the classes below, they should check this
*						easily by referncing properties in the class below. An object("clientInfoObj") of this 
*						class will always be accessible to the other classes. 
* 	Update log:
*
************************************************************************************************************/

/* 
Constructor 
*/

function DHTMLSuite_clientInfo()
{
	var browser;			// Complete user agent information
	
	var isOpera;			// Is the browser "Opera"
	var isMSIE;				// Is the browser "Internet Explorer"	
	var isFirefox;			// Is the browser "Firefox"
	var navigatorVersion;	// Browser version
}
	
DHTMLSuite_clientInfo.prototype = {
	
	/**
	* 	Constructor
	*	Params: 		none:
	*  	return value: 	none;
	**/
	// {{{ init()
    /**
     *
	 *
     *  This method initializes the script
     *
     * 
     * @public
     */	
    	
	init : function()
	{
		this.browser = navigator.userAgent;	
		this.isOpera = (this.browser.toLowerCase().indexOf('opera')>=0)?true:false;
		this.isFirefox = (this.browser.toLowerCase().indexOf('firefox')>=0)?true:false;
		this.isMSIE = (this.browser.toLowerCase().indexOf('msie')>=0)?true:false;
		this.navigatorVersion = navigator.appVersion.replace(/.*?MSIE (\d\.\d).*/g,'$1')/1;
	}	
	// }}}		
}


/************************************************************************************************************
*	Drag and drop class
*
*	Created:			August, 18th, 2006
*	Purpose of class:	A general drag and drop class. By creating objects of this class, you can make elements
*						on your web page dragable and also assign actions to element when an item is dropped on it.
*						A page should only have one object of this class.
*
*						IMPORTANT when you use this class: Don't assign layout to the dragable element ids
*						Assign it to classes or the tag instead. example: If you make <div id="dragableBox1" class="aBox">
*						dragable, don't assign css to #dragableBox1. Assign it to div or .aBox instead.
*
* 	Update log:
*
************************************************************************************************************/

var referenceToDragDropObject;	// A reference to an object of the class below. 

/* 
Constructor 
*/
function DHTMLSuite_dragDrop()
{
	var mouse_x;					// mouse x position when drag is started
	var mouse_y;					// mouse y position when drag is started.
	
	var el_x;						// x position of dragable element
	var el_y;						// y position of dragable element
	
	var dragDropTimer;				// Timer - short delay from mouse down to drag init.
	var numericIdToBeDragged;		// numeric reference to element currently being dragged.
	var dragObjCloneArray;			// Array of cloned dragable elements. every
	var dragDropSourcesArray;		// Array of source elements, i.e. dragable elements.
	var dragDropTargetArray;		// Array of target elements, i.e. elements where items could be dropped.
	var currentZIndex;				// Current z index. incremented on each drag so that currently dragged element is always on top.
	var okToStartDrag;				// Variable which is true or false. It would be false for 1/100 seconds after a drag has been started.
									// This is useful when you have nested dragable elements. It prevents the drag process from staring on
									// parent element when you click on dragable sub element.
	var moveBackBySliding;			// Variable indicating if objects should slide into place moved back to their location without any slide animation.
}

DHTMLSuite_dragDrop.prototype = {
	
	// {{{ init()
    /**
     * Initialize the script
     * This method should be called after you have added sources and destinations.
     * 
     * @public
     */	
	init : function()
	{
		if(!standardObjectsCreated)DHTMLsuite_createStandardObjects();	// This line starts all the init methods
		this.currentZIndex = 10000;
		this.dragDropTimer = -1;
		this.dragObjCloneArray = new Array();
		this.numericIdToBeDragged = false;	
		this.__initDragDropScript();	
		referenceToDragDropObject = this;	
		this.okToStartDrag = true;
		this.moveBackBySliding = true;
	}
	// }}}	
	,
	// {{{ addSource()
    /**
     * Add dragable element
     *
     * @param String sourceId = Id of source
     * @param boolean slideBackAfterDrop = Slide the item back to it's original location after drop.
     * 
     * @public
     */	
	addSource : function(sourceId,slideBackAfterDrop)
	{
		if(!this.dragDropSourcesArray)this.dragDropSourcesArray = new Array();
		if(!document.getElementById(sourceId))alert('The source element with id ' + sourceId + ' does not exists');
		var obj = document.getElementById(sourceId);
		this.dragDropSourcesArray[this.dragDropSourcesArray.length]  = [obj,slideBackAfterDrop];		
		obj.setAttribute('dragableElement',this.dragDropSourcesArray.length-1);
		obj.dragableElement = this.dragDropSourcesArray.length-1;
		
	}
	// }}}	
	,
	// {{{ addTarget()
    /**
     * Add drop target
     *
     * @param String targetId = Id of drop target
     * @param String functionToCallOnDrop = name of function to call on drop. 
	 *		Input to this the function specified in functionToCallOnDrop function would be 
	 *		id of dragged element 
	 *		id of the element the item was dropped on.
	 *		mouse x coordinate when item was dropped
	 *		mouse y coordinate when item was dropped     
     * 
     * @public
     */	
	addTarget : function(targetId,functionToCallOnDrop)
	{
		if(!this.dragDropTargetArray)this.dragDropTargetArray = new Array();
		if(!document.getElementById(targetId))alert('The target element with id ' + targetId + ' does not exists');
		var obj = document.getElementById(targetId);
		this.dragDropTargetArray[this.dragDropTargetArray.length]  = [obj,functionToCallOnDrop];		
	}
	// }}}	
	,
	
	// {{{ setSlide()
    /**
     * Activate or deactivate sliding animations.
     *
     * @param boolean slide = Move element back to orig. location in a sliding animation
     * 
     * @public
     */	
	setSlide : function(slide)
	{
		this.moveBackBySliding = slide;	
		
	}
	// }}}	
	,
	
	/* Start private methods */
	
	// {{{ __initDragDropScript()
    /**
     * Initialize drag drop script - this method is called by the init() method.
     * 
     * @private
     */	
	__initDragDropScript : function()
	{
		var refToThis = this;
		for(var no=0;no<this.dragDropSourcesArray.length;no++){
			var el = this.dragDropSourcesArray[no][0].cloneNode(true);
			el.onmousedown =this.__initDragDropElement;		
			el.id = 'DHTMLSuite_dragableElement' + no;
			el.style.position='absolute';
			el.style.visibility='hidden';
			el.style.display='none';			

			this.dragDropSourcesArray[no][0].parentNode.insertBefore(el,this.dragDropSourcesArray[no][0]);
			
			el.style.top = dhtmlSuiteCommonObj.getTopPos(this.dragDropSourcesArray[no][0]) + 'px';
			el.style.left = dhtmlSuiteCommonObj.getLeftPos(this.dragDropSourcesArray[no][0]) + 'px';
					
			this.dragDropSourcesArray[no][0].onmousedown =this.__initDragDropElement;
										
			this.dragObjCloneArray[no] = el; 
		}
		
		document.documentElement.onmousemove = this.__moveDragableElement;
		document.documentElement.onmouseup = this.__stop_dragDropElement;
		document.documentElement.onselectstart = function() { return refToThis.__cancelSelectionEvent(false,this) };
		document.documentElement.ondragstart = function() { return dhtmlSuiteCommonObj.cancelEvent(false,this) };		
	}	
	// }}}	
	,	
	
	// {{{ __initDragDropElement()
    /**
     * Initialize drag process
     *
     * @param Event e = Event object, used to get x and y coordinate of mouse pointer
     * 
     * @private
     */	
	__initDragDropElement : function(e)
	{
		if(!referenceToDragDropObject.okToStartDrag)return;
		referenceToDragDropObject.okToStartDrag = false;
		setTimeout('referenceToDragDropObject.okToStartDrag = true;',100);
		if(document.all)e = event;
		referenceToDragDropObject.numericIdToBeDragged = this.getAttribute('dragableElement');
		referenceToDragDropObject.numericIdToBeDragged = referenceToDragDropObject.numericIdToBeDragged + '';
		if(referenceToDragDropObject.numericIdToBeDragged=='')referenceToDragDropObject.numericIdToBeDragged = this.dragableElement;
		referenceToDragDropObject.dragDropTimer=0;
		
		referenceToDragDropObject.mouse_x = e.clientX;
		referenceToDragDropObject.mouse_y = e.clientY;
		
		referenceToDragDropObject.currentZIndex = referenceToDragDropObject.currentZIndex + 1;
		
		referenceToDragDropObject.dragObjCloneArray[referenceToDragDropObject.numericIdToBeDragged].style.zIndex = referenceToDragDropObject.currentZIndex;
		
		// Reposition dragable element
		if(referenceToDragDropObject.dragDropSourcesArray[referenceToDragDropObject.numericIdToBeDragged][1]){
			referenceToDragDropObject.dragObjCloneArray[referenceToDragDropObject.numericIdToBeDragged].style.top = dhtmlSuiteCommonObj.getTopPos(referenceToDragDropObject.dragDropSourcesArray[referenceToDragDropObject.numericIdToBeDragged][0]) + 'px';
			referenceToDragDropObject.dragObjCloneArray[referenceToDragDropObject.numericIdToBeDragged].style.left = dhtmlSuiteCommonObj.getLeftPos(referenceToDragDropObject.dragDropSourcesArray[referenceToDragDropObject.numericIdToBeDragged][0]) + 'px';
		}
		referenceToDragDropObject.el_x = referenceToDragDropObject.dragObjCloneArray[referenceToDragDropObject.numericIdToBeDragged].style.left.replace('px','')/1;
		referenceToDragDropObject.el_y = referenceToDragDropObject.dragObjCloneArray[referenceToDragDropObject.numericIdToBeDragged].style.top.replace('px','')/1;

		
				
		referenceToDragDropObject.__timerDragDropElement();
		

		
		return false;
	}	
	// }}}	
	,
	
	// {{{ __timerDragDropElement()
    /**
     * A small delay from mouse down to drag starts 
     * 
     * @private
     */	
	__timerDragDropElement : function()
	{
		window.thisRef = this;
		if(this.dragDropTimer>=0 && this.dragDropTimer<5){
			this.dragDropTimer = this.dragDropTimer + 1;
			setTimeout('window.thisRef.__timerDragDropElement()',2);
			return;			
		}
		if(this.dragDropTimer>=5){
			if(this.dragObjCloneArray[this.numericIdToBeDragged].style.display=='none'){
				this.dragDropSourcesArray[this.numericIdToBeDragged][0].style.visibility = 'hidden';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.display = 'block';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.visibility = 'visible';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.top = dhtmlSuiteCommonObj.getTopPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
				this.dragObjCloneArray[this.numericIdToBeDragged].style.left = dhtmlSuiteCommonObj.getLeftPos(this.dragDropSourcesArray[this.numericIdToBeDragged][0]) + 'px';
			}
		}		
	}	
	// }}}	
	,
	
	// {{{ __cancelSelectionEvent()
    /**
     * Cancel text selection when drag is in progress
     * 
     * @private
     */	
	__cancelSelectionEvent : function()
	{
		if(this.dragDropTimer>=0)return false;
		return true;
	}	
	// }}}	
	,
	
	// {{{ __moveDragableElement()
    /**
     * Move dragable element according to mouse position when drag is in process.
     *
     * @param Event e = Event object, used to get x and y coordinate of mouse pointer
     * 
     * @private
     */	
	__moveDragableElement : function(e)
	{
		if(document.all)e = event;
		if(referenceToDragDropObject.dragDropTimer<5)return;	
		referenceToDragDropObject.dragObjCloneArray[referenceToDragDropObject.numericIdToBeDragged].style.left = (e.clientX - referenceToDragDropObject.mouse_x + referenceToDragDropObject.el_x) + 'px'; 
		referenceToDragDropObject.dragObjCloneArray[referenceToDragDropObject.numericIdToBeDragged].style.top = (e.clientY - referenceToDragDropObject.mouse_y + referenceToDragDropObject.el_y) + 'px'; 
	}
	// }}}	
	,
	
	// {{{ __stop_dragDropElement()
    /**
     * Drag process stopped.
     * Note! In this method "this" refers to the element being dragged. referenceToDragDropObject refers to the dragDropObject.
     *
     * @param Event e = Event object, used to get x and y coordinate of mouse pointer
     * 
     * @private
     */	
	__stop_dragDropElement : function(e)
	{
		if(referenceToDragDropObject.dragDropTimer<5)return;
		if(document.all)e = event;
				
		// Dropped on which element
		if (e.target) dropDestination = e.target;
			else if (e.srcElement) dropDestination = e.srcElement;
			if (dropDestination.nodeType == 3) // defeat Safari bug
				dropDestination = dropDestination.parentNode;	
		
		
		var leftPosMouse = e.clientX + Math.max(document.body.scrollLeft,document.documentElement.scrollLeft);
		var topPosMouse = e.clientY + Math.max(document.body.scrollTop,document.documentElement.scrollTop);
		
		if(!referenceToDragDropObject.dragDropTargetArray)referenceToDragDropObject.dragDropTargetArray = new Array();
		// Loop through drop targets and check if the coordinate of the mouse is over it. If it is, call specified drop function.
		for(var no=0;no<referenceToDragDropObject.dragDropTargetArray.length;no++){
			var leftPosEl = dhtmlSuiteCommonObj.getLeftPos(referenceToDragDropObject.dragDropTargetArray[no][0]);
			var topPosEl = dhtmlSuiteCommonObj.getTopPos(referenceToDragDropObject.dragDropTargetArray[no][0]);
			var widthEl = referenceToDragDropObject.dragDropTargetArray[no][0].offsetWidth;
			var heightEl = referenceToDragDropObject.dragDropTargetArray[no][0].offsetHeight;
			
			if(leftPosMouse > leftPosEl && leftPosMouse < (leftPosEl + widthEl) && topPosMouse > topPosEl && topPosMouse < (topPosEl + heightEl)){
				eval(referenceToDragDropObject.dragDropTargetArray[no][1] + '("' + referenceToDragDropObject.dragDropSourcesArray[referenceToDragDropObject.numericIdToBeDragged][0].id + '","' + referenceToDragDropObject.dragDropTargetArray[no][0].id + '",' + e.clientX + ',' + e.clientY + ')');
				break;
			}			
		}	
		
		if(referenceToDragDropObject.dragDropSourcesArray[referenceToDragDropObject.numericIdToBeDragged][1]){
			referenceToDragDropObject.__slideElementBackIntoItsOriginalPosition(referenceToDragDropObject.numericIdToBeDragged);
		}
		
		// Variable cleanup after drop
		referenceToDragDropObject.dragDropTimer = -1;
		referenceToDragDropObject.numericIdToBeDragged = false;
									
	}	
	// }}}	
	,
	
	// {{{ __slideElementBackIntoItsOriginalPosition()
    /**
     * Slide an item back to it's original position
     *
     * @param Integer numId = numeric index of currently dragged element	
     * 
     * @private
     */	
	__slideElementBackIntoItsOriginalPosition : function(numId)
	{
		// Coordinates current element position
		var currentX = this.dragObjCloneArray[numId].style.left.replace('px','')/1;
		var currentY = this.dragObjCloneArray[numId].style.top.replace('px','')/1;
		
		// Coordinates - where it should slide to
		var targetX = dhtmlSuiteCommonObj.getLeftPos(referenceToDragDropObject.dragDropSourcesArray[numId][0]);
		var targetY = dhtmlSuiteCommonObj.getTopPos(referenceToDragDropObject.dragDropSourcesArray[numId][0]);;
		
		if(this.moveBackBySliding){
			// Call the step by step slide method
			this.__processSlide(numId,currentX,currentY,targetX,targetY);
		}else{
			this.dragObjCloneArray[numId].style.display='none';
			this.dragDropSourcesArray[numId][0].style.visibility = 'visible';			
		}
			
	}
	// }}}	
	,
	
	// {{{ __processSlide()
    /**
     * Move the element step by step in this method
     *
     * @param Int numId = numeric index of currently dragged element
     * @param Int currentX = Elements current X position
     * @param Int currentY = Elements current Y position
     * @param Int targetX = Destination X position, i.e. where the element should slide to
     * @param Int targetY = Destination Y position, i.e. where the element should slide to
     * 
     * @private
     */	
	__processSlide : function(numId,currentX,currentY,targetX,targetY)
	{				
		// Find slide x value
		var slideX = Math.round(Math.abs(Math.max(currentX,targetX) - Math.min(currentX,targetX)) / 10);		
		// Find slide y value
		var slideY = Math.round(Math.abs(Math.max(currentY,targetY) - Math.min(currentY,targetY)) / 10);
		
		if(slideY<3 && Math.abs(slideX)<10)slideY = 3;	// 3 is minimum slide value
		if(slideX<3 && Math.abs(slideY)<10)slideX = 3;	// 3 is minimum slide value
		
		
		if(currentX > targetX) slideX*=-1;	// If current x is larger than target x, make slide value negative<br>
		if(currentY > targetY) slideY*=-1;	// If current y is larger than target x, make slide value negative
		
		// Update currentX and currentY
		currentX = currentX + slideX;	
		currentY = currentY + slideY;

		// If currentX or currentY is close to targetX or targetY, make currentX equal to targetX(or currentY equal to targetY)
		if(Math.max(currentX,targetX) - Math.min(currentX,targetX) < 4)currentX = targetX;
		if(Math.max(currentY,targetY) - Math.min(currentY,targetY) < 4)currentY = targetY;

		// Update CSS position(left and top)
		this.dragObjCloneArray[numId].style.left = currentX + 'px';
		this.dragObjCloneArray[numId].style.top = currentY + 'px';	
		
		// currentX different than targetX or currentY different than targetY, call this function in again in 5 milliseconds
		if(currentX!=targetX || currentY != targetY){
			window.thisRef = this;	// Reference to this dragdrop object
			setTimeout('window.thisRef.__processSlide("' + numId + '",' + currentX + ',' + currentY + ',' + targetX + ',' + targetY + ')',5);
		}else{	// Slide completed. Make absolute positioned element invisible and original element visible
			this.dragObjCloneArray[numId].style.display='none';
			this.dragDropSourcesArray[numId][0].style.visibility = 'visible';
		}		
	}
}