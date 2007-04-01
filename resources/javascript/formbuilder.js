dojo.require("dojo.html.*");

var PAGES_PANEL_ID = 'pagesPanel';
var SP_PAGES_PANEL_ID = 'pagesPanelSpecial';
var PAGE_ICON_STYLE = 'formPageIcon';
var PAGE_ICON_SELECTED = 'formPageIconSelected';
var CURRENT_ELEMENT_UNDER = -1;
var LAST_ELEMENT_UNDER = -1;
var childBoxes = [];

var CURRENT_PAGE_ID;
var PREVIOUS_PAGE_ID;
var CURRENT_ELEMENT_ID;
var PREVIOUS_ELEMENT_ID;
var TEMP_INLINE_VALUE;
var TEMP_INLINE_ID;
var ON_BLUR;
var ON_RETURN;
var ON_SELECT;

var currentButton = null;
var currentElement = null;
var pressedComponentDelete = false;
var pressedButtonDelete = false;
var pressedPageDelete = false;
var draggingButton = false;
var draggingComponent = false;
var draggingPage = false;

$('statusContainer').style.visibility = 'hidden';
Rico.Corner.round('statusContainer');

var FBDraggable = Class.create();

FBDraggable.prototype = (new Rico.Draggable()).extend( {

   	initialize: function(htmlElement, name, type, autofill) {
      	this.type        = type;
      	this.htmlElement = $(htmlElement);
      	this.name        = name;
      	this.autofill    = autofill;
   	},

   	select: function() {
      	this.selected = true;
      	var el = this.htmlElement;
      	el.style.color           = "#ffffff";
      	el.style.backgroundColor = "#08246b";
      	el.style.border          = "1px solid blue";
   	},

   	deselect: function() {
      	this.selected = false;
      	var el = this.htmlElement;
      	el.style.color           = "#2b2b2b";
      	el.style.backgroundColor = "transparent";
      	el.style.border = "1px solid #ffffee";
   	},

   	startDrag: function() {
   		if(this.type == 'fbcomp') {
   			CURRENT_ELEMENT_UNDER = -1;
   			childBoxes = [];
			var dropBox = $('dropBoxinner');
			var child;
			for(var i = 0; i < dropBox.childNodes.length; i++){
				child = dropBox.childNodes[i];
				if(child.nodeType != dojo.html.ELEMENT_NODE){ continue; }
				var pos = dojo.html.getAbsolutePosition(child, true);
				var inner = dojo.html.getBorderBox(child);
				childBoxes.push({top: pos.y, bottom: pos.y+inner.height,left: pos.x, right: pos.x+inner.width, height: inner.height, width: inner.width, node: child});
			}
			var info = new PaletteComponentInfo(this.name, this.autofill);// { type:this.name, name: "", iconPath: "", autofill_key: "" };
   			FormComponent.addComponent(info, placeNewComponent);
   		} else if(this.type == 'fbbutton') {
   			FormComponent.addButton(this.name, placeNewButton);
   		}
   	},

   	cancelDrag: function() {
   		if(this.type == 'fbcomp') {
   			FormComponent.removeComponent(currentElement.getAttribute('id'),nothing);
   		} else if(this.type == 'fbbutton') {
   			FormComponent.removeButton(currentButton.getAttribute('id'),nothing);
   		}
   	},

   	endDrag: function() {
   	},

   	getSingleObjectDragGUI: function() {
      	var el = this.htmlElement;
		var div = el.cloneNode(true);
		div.onmousemove = currentMousePosition;
      	return div;
   	},

   	getMultiObjectDragGUI: function( draggables ) {
   	},

   	getDroppedGUI: function() {
   	}

} );

function PaletteComponentInfo(type,autofill) {
	this.type = type;
	this.autofill = autofill;
}

var FBDropzone = Class.create();

FBDropzone.prototype = (new Rico.Dropzone()).extend( {

   	initialize: function(htmlElement, type) {
    	this.htmlElement  = $(htmlElement);
      	this.absoluteRect = null;
      	this.type = type;
   	},

   	activate: function() {
   		if(this.type == 'fbcomp') {
   			var cont = $('dropBoxinner');
   			cont.style.backgroundColor = 'Silver';
   		} else if(this.type == 'fbbutton') {
   			var cont = $('pageButtonArea');
			if(cont == null) {
				cont = document.createElement('div');
				cont.id = 'pageButtonArea';
				cont.style.position = 'relative';
				cont.setAttribute('class','formElement');
				cont.style.backgroundColor = 'Silver';
				var dropBox = $('dropBox');
				if(dropBox != null) {
					dropBox.appendChild(cont);
				}
			} else {
				cont.style.backgroundColor = 'Silver';
			}
   		}
   	},

   	deactivate: function() {
   		if(this.type == 'fbcomp') {
   			var cont = $('dropBoxinner');
   			cont.style.backgroundColor = 'White';
   		} else if(this.type == 'fbbutton') {
   			var cont = $('pageButtonArea');
			if(cont != null) {
				cont.style.backgroundColor = 'White';
			}
   		}
   	},

	showHover: function() {
		if(this.type == 'fbcomp') {
			var children;
			var index = CURRENT_ELEMENT_UNDER;
			if(index != LAST_ELEMENT_UNDER && index != -1) {
				LAST_ELEMENT_UNDER = index;
				var cont = $('dropBoxinner');
					if(cont != null) {
						var marker = $('insertMarker');
						if(marker != null && marker.parentNode == cont) {
							cont.removeChild(marker);
						}
						var node = getEmptySpaceBox();
						children = getPageComponents();
						var count = children.length;
						var beforeNode = children[index];
						var ids = beforeNode.id;
						console.log('Marker: '+node.id + ' Before: '+beforeNode.id);
						cont.insertBefore(node, beforeNode);
					}
			}
		} else if(this.type == 'fbbutton') {
			//TODO precise dropping of buttons
		}
   	},

   	hideHover: function() {
   		if(this.type == 'fbcomp') {
   			var cont = $('dropBoxinner');
   			if(cont != null) {
				var line = $('insertMarker');
				if(line != null) {
					cont.removeChild(line);
				}
			}
   		}
   	},

   	accept: function(draggableObjects) {
   		if(this.type == 'fbcomp') {
      		var empty = $('emptyForm');
			if(empty != null) {
				if(empty.style) {
					empty.style.display = 'none';
				} else {
					empty.display = 'none';
				}
			}
			var index = CURRENT_ELEMENT_UNDER;
      		try {
		         if(index != null) {
		         	console.log("Accepting: " + currentElement);
		         	var currentId = currentElement.getAttribute('id');
		         	if(currentId != null) {
		         		FormComponent.moveComponent(currentId, index, insertNewComponent);
		         	}
		         }
      		} catch(e) {
      			console.log("Canceling: " + currentElement);
      			FormComponent.removeComponent(currentElement.getAttribute('id'),nothing);
      		}
      		Sortable.create('dropBoxinner',{dropOnEmpty:true,tag:'div',only:'formElement',onUpdate:rearrangeComponents,scroll:'dropBoxinner',constraint:false});
      	} else if(this.type == 'fbbutton') {
      		var cont = $('pageButtonArea');
			if(cont == null) {
				var buttonArea = document.createElement('div');
				buttonArea.id = 'pageButtonArea';
				buttonArea.style.position = 'relative';
				buttonArea.setAttribute('class','formElement');
				var dropBox = $('dropBox');
				if(dropBox != null) {
					dropBox.appendChild(buttonArea);
					buttonArea.appendChild(currentButton);
				}
			} else {
				cont.appendChild(currentButton);
			}
			Position.includeScrollOffsets = true;
			Sortable.create('pageButtonArea',{dropOnEmpty:true,tag:'div',only:'formButton',onUpdate:rearrangeButtons,scroll:'pageButtonArea',constraint:false});
      	}
	},

   	canAccept: function(draggableObjects) {
      	for (var i = 0 ; i < draggableObjects.length ; i++) {
         	if (draggableObjects[i].type != this.type)
            	return false;
      	}
      	return true;
   	},
} );

function getEmptySpaceBox() {
	var node = document.createElement('div');
	node.id = 'insertMarker';
	return node;
}

function currentMousePosition(e) {
	if(!e) e = window.event;
	for(var i = 0, child; i < childBoxes.length; i++){
		with(childBoxes[i]){
			if (e.pageX >= left && e.pageX <= right && e.pageY >= top && e.pageY <= bottom) {
				CURRENT_ELEMENT_UNDER = i;
			}
		}
	}
}

function getPageComponents() {
	var dropBox = $('dropBoxinner');
	var result = [];
	if(dropBox != null) {
		var list = dropBox.getElementsByTagName('div');
		for(var i=0;i<list.length;i++) {
			var temp = list[i].id;
			if(temp.indexOf('_i') == -1) {
				result.push(list[i]);
			}
		}
	}
	return result;
}

function displayMessage(url) {
	messageObj.setSource(url);
	messageObj.setCssClassMessageBox(false);
	messageObj.setSize(250,100);
	messageObj.setShadowDivVisible(true);
	messageObj.display();
}
function closeMessage() {
	messageObj.close();
}
function setStatus(s,n) {
	$('statusContainer').style.visibility = "visible";
	$('statusMsg').innerHTML = s;
	setTimeout( "$('statusContainer').style.visibility = 'hidden';", n );
	closeMessage();
}
function showStatus(text) {
	$('statusContainer').style.visibility = "visible";
	$('statusMsg').innerHTML = text;
}
function closeStatus() {
	$('statusContainer').style.visibility = 'hidden';
}

function savePropertyOnEnter(parameter,attribute,e) {
	if (!e) e = window.event;
	if (!e) return true;
	var key = (typeof e.keyCode != 'undefined' ? e.keyCode : e.charCode);
	if(key == '13') {
		switch(attribute) {
			case 'compText':
				savePlaintext(parameter);
				break;
		  	case 'compTitle':
		  		saveComponentLabel(parameter);
		    	break;
		  	case 'compErr':
		    	saveErrorMessage(parameter);
		    	break;
		 	case 'compHelp':
		 		saveHelpMessage(parameter);
		 		break;
		 	case 'compAuto':
		 		saveAutofill(parameter);
		 		break;
		 	case 'compExt':
		  		saveExternalSrc(parameter);
		  		break;
		  	case 'formTitle':
		 		saveFormTitle(parameter);
		 		break;
		 	case 'formThxTitle':
		 		saveThankYouTitle(parameter);
		 		break;
		 	case 'formThxText':
		 		saveThankYouText(parameter);
		 		break;
		  	default:
		}
	}
}
function createButtonAreaNode() {
	var node = document.createElement('div');
	node.id = 'pageButtonArea';
	node.setAttribute('class', 'formElement');
	
	return node;
}
function createButtonNode(parameter) {
	var node = document.createElement('div');
	node.id = parameter.id;
	node.style.display = 'inline';
	node.setAttribute('class', 'formButton');
	node.setAttribute('onclick', "loadButtonInfo(this.id);");
	
	var button = document.createElement('input');
	button.setAttribute('type', 'button');
	button.setAttribute('enabled', 'false');
	button.id = parameter.type;
	button.setAttribute('value', parameter.label);
	button.style.display = 'inline';
	node.appendChild(button);
	
	var db = document.createElement('img');
	db.setAttribute('class', 'fbSpeedBButton');
	db.setAttribute('src', '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png');
	db.setAttribute('onclick', 'removeButton(this.parentNode.id);');
	node.appendChild(db);
	
	return node;
}
function createNewComponent(htmlNode) {
	var node = document.createElement('div');
	var nodeId = htmlNode.getAttribute('id');
	node.setAttribute('id', nodeId);
	//htmlNode.setAttribute('id', nodeId + '_i');
	htmlNode.removeAttribute('id');
	node.setAttribute('class', 'formElement');
	node.setAttribute('onclick', 'loadComponentInfo(this);');
	
	node.appendChild(htmlNode);
	
	var delImg = document.createElement('img');
	delImg.id = 'db' + nodeId;
	delImg.setAttribute('src', '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png');
	delImg.setAttribute('onclick', 'removeComponent(this);');
	delImg.setAttribute('class', 'speedButton');
	node.appendChild(delImg);
	
	return node;
}
function placeNewButton(parameter) {
	if(parameter != null) {
		currentButton = createButtonNode(parameter);
	}
}
function removeButton(parameter) {
	if(parameter != null) {
		pressedButtonDelete = true;
		FormComponent.removeButton(parameter, removeButtonNode);
	}
}
function removeButtonNode(parameter) {
	if(parameter != null) {
		var node = $(parameter);
		if(node != null) {
			var parentNode = node.parentNode;
			if(parentNode != null) {
				parentNode.removeChild(node);
			}
		}
	}
}
function loadButtonInfo(parameter) {
	if(pressedButtonDelete == false && draggingButton == false) {
		FormComponent.getFormButtonInfo(parameter, placeButtonInfo);
	}
	pressedButtonDelete = false;
	draggingButton = false;
}
function placeButtonInfo(parameter) {
	if(parameter != null) {
		var labelTxt = $('propertyTitle');
		if(labelTxt != null) {
			labelTxt.value = parameter.label;
		}
		var plainPr = $('plainPropertiesPanel');
		if(plainPr != null) {
			plainPr.setAttribute('style', 'display: none');
		}
		var labelPr = $('labelPropertiesPanel');
		if(labelPr != null) {
			labelPr.setAttribute('style', 'display: block');
		}
		var compPr = $('basicPropertiesPanel');
		if(compPr != null) {
			compPr.setAttribute('style', 'display: none');
		}
		var advPr = $('advPropertiesPanel');
		if(advPr != null) {
			advPr.setAttribute('style', 'display: none');
		}
		var extPr = $('extPropertiesPanel');
		if(extPr != null) {
			extPr.setAttribute('style', 'display: none');
		}
		var autoPr = $('autoPropertiesPanel');
		if(autoPr != null) {
			autoPr.setAttribute('style', 'display: none');
		}
		var localPr = $('localPropertiesPanel');
		if(localPr != null) {
			localPr.setAttribute('style', 'display: none');
		}
		STATIC_ACCORDEON.showTabByIndex(1, true);
	}
}
function placeNewComponent(parameter) {
	if(parameter != null) {
		currentElement = createNewComponent(createTreeNode(parameter.documentElement));
	}
}
function createTreeNode(element) {
	if(element.nodeName == '#text') {
		var textNode = document.createTextNode(element.nodeValue);
		return textNode;
	} else {
		var result = document.createElement(element.nodeName);
		if(element.nodeName == 'input' || element.nodeName == 'textarea' || element.nodeName == 'select') {
			result.setAttribute('disabled','true');
		}
		for(var i=0;i<element.attributes.length;i++) {
			result.setAttribute(element.attributes[i].nodeName,element.attributes[i].nodeValue);
		}
		for(var j=0;j<element.childNodes.length;j++) {
			result.appendChild(createTreeNode(element.childNodes[j]));
		}
		return result;
	}
}
function enableInlineEdit(parameter, OnBlur, onReturn) {
	var node = $(parameter);
	if(node != null) {
		var text = node.childNodes[0].nodeValue;
		TEMP_INLINE_ID = parameter;
		TEMP_INLINE_VALUE = text;
		var parentNode = node.parentNode;
		if(node.onclick != null) {
			ON_SELECT = node.onclick;
		}
		parentNode.removeChild(node);
		var input = document.createElement('input');
		input.setAttribute('type', 'text');
		input.value = text;
		input.id = TEMP_INLINE_ID;
		input.onblur = OnBlur;
		input.onkeydown = onReturn;
		parentNode.appendChild(input);
		ON_BLUR = OnBlur;
		ON_RETURN = onReturn;
		input.focus();
	}
}
function savePageTitleOnBlur(e) {
	if (!e) e = window.event;
	if (!e) return true;
	var text = e.target.value;
	if(text != '') {
		var node = $(CURRENT_PAGE_ID);
		if(node != null) {
			var parent = node.childNodes[1];
			var textNode = parent.childNodes[0];
			var newTextNode = document.createTextNode(text);
			parent.replaceChild(newTextNode, textNode);
		}
		FormPage.setTitle(text, disableInlineEdit);
		TEMP_INLINE_VALUE = text;
	}
}
function saveFormTitleOnBlur(e) {
	if (!e) e = window.event;
	if (!e) return true;
	var text = e.target.value;
	if(text != '') {
		$('formTitle').value = text;
		FormDocument.setFormTitle(text, disableInlineEdit);
		TEMP_INLINE_VALUE = text;
	}
}
function savePageTitleOnReturn(e) {
	if (!e) e = window.event;
	if (!e) return true;
	var key = (typeof e.keyCode != 'undefined' ? e.keyCode : e.charCode);
	if(key == '13') {
		var text = e.target.value;
		if(text != '') {
			var node = $(CURRENT_PAGE_ID);
			if(node != null) {
				var parent = node.childNodes[1];
				var textNode = parent.childNodes[0];
				var newTextNode = document.createTextNode(text);
				parent.replaceChild(newTextNode, textNode);
			}
			FormPage.setTitle(text, disableInlineEdit);
			TEMP_INLINE_VALUE = text;
		}
	}
}
function saveFormTitleOnReturn(e) {
	if (!e) e = window.event;
	if (!e) return true;
	var key = (typeof e.keyCode != 'undefined' ? e.keyCode : e.charCode);
	if(key == '13') {
		var text = e.target.value;
		if(text != '') {
			$('formTitle').value = text;
			FormDocument.setFormTitle(text, disableInlineEdit);
			TEMP_INLINE_VALUE = text;
		}
	}
}
function disableInlineEdit() {
	var node = $(TEMP_INLINE_ID);
	if(node != null) {
		var text = node.value;
		var parentNode = node.parentNode;
		parentNode.removeChild(node);
		var span = getInlineEditSpan(text, ON_SELECT);
		parentNode.appendChild(span);
		
		TEMP_INLINE_VALUE = null;
		TEMP_INLINE_ID = null;
	}
}
function getInlineEditSpan(parameter, click) {
	var span = document.createElement('span');
	span.id = TEMP_INLINE_ID;
	if(click != null) {
		span.onclick = click;
	}
	span.setAttribute('ondblclick','enableInlineEdit(this.id, ' + ON_BLUR + ', ' + ON_RETURN + ');');
		
	var textNode = document.createTextNode(parameter);
	span.appendChild(textNode);
	return span;
}
function loadFormInfo() {
	FormDocument.getFormDocumentInfo(placeFormInfo);
}
function placeFormInfo(parameter) {
	if(parameter != null) {
		DWRUtil.setValue('formTitle', parameter.title);
		DWRUtil.setValue('previewScreen', parameter.hasPreview);
		DWRUtil.setValue('thankYouTitle', parameter.thankYouTitle);
		DWRUtil.setValue('thankYouText', parameter.thankYouText);
		STATIC_ACCORDEON.showTabByIndex(2, true);
	}
}
function saveFormTitle(parameter) {
	if(parameter != null) {
		FormDocument.saveFormTitle(parameter, updateFormTitle);
	}
}
function updateFormTitle(parameter) {
	DWRUtil.setValue('formHeadingHeader', parameter);
}
function saveThankYouTitle(parameter) {
	if(parameter != null) {
		FormDocument.setThankYouTitle(parameter, placeThankYouTitle);
	}
}
function placeThankYouTitle(parameter) {
	var container = $('pagesPanelSpecial');
	if(container != null) {
		var node = $(parameter.pageId + '_P_page');
		if(node != null) {
			var parent = node.childNodes[1];
			var textNode = parent.childNodes[0];
			var newTextNode = document.createTextNode(parameter.pageTitle);
			parent.replaceChild(newTextNode, textNode);
		}
	}
}
function saveThankYouText(parameter) {
	if(parameter != null) {
		FormDocument.setThankYouText(parameter, nothing);
	}
}
function saveHasPreview(parameter) {
	if(parameter != null) {
		FormDocument.togglePreviewPage(parameter.checked, placePreviewPage);
	}
}
//TODO make a createNewPage method
function placePreviewPage(parameter) {
	var container = $('pagesPanelSpecial');
	if(container != null) {
		if(parameter.pageTitle != null) {
		
			var page = document.createElement('div');
			page.setAttribute('id', parameter.pageId + '_P_page');
			page.setAttribute('class', 'formPageIcon');
			page.setAttribute('styleClass', 'formPageIconSpecial');
			page.setAttribute('style', 'position: relative');
			page.setAttribute('onclick', 'loadConfirmationPage(this.id);');
			
			var icon = document.createElement('img');
			icon.setAttribute('id', parameter.pageId + '_pi');
			icon.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png';
			icon.style.display = 'block';
			
			var label = document.createElement('span');
			label.style.display = 'block';
			
			var text = document.createTextNode(parameter.pageTitle);
			
			label.appendChild(text);
			
			page.appendChild(icon);
			page.appendChild(label);
			
			var child = container.childNodes[0];
			container.insertBefore(page, child);
		} else {
			var node = $(parameter.pageId + '_P_page');
			if(node != null) {
				container.removeChild(node);
			}
		}
	}
}
function markSelectedPage(parameter) {
	if(CURRENT_PAGE_ID != null) {
		PREVIOUS_PAGE_ID = CURRENT_PAGE_ID;
		$(PREVIOUS_PAGE_ID).setAttribute('class','formPageIcon');
	}
	CURRENT_PAGE_ID = parameter.pageId + '_P_page';
	$(CURRENT_PAGE_ID).setAttribute('class','formPageIcon selectedElement');
}
function loadPageInfo(parameter) {
	if(pressedPageDelete == false && draggingPage == false) {
		showLoadingMessage('Loading section...');
		FormPage.getFormPageInfo(parameter, placePageInfo);
	}
	pressedPageDelete = false;
	draggingPage = false;
}
function loadConfirmationPage(parameter) {
	showLoadingMessage('Loading section...');
	FormPage.getConfirmationPageInfo(placeConfirmationPageInfo);
}
function placeConfirmationPageInfo(parameter) {
	markSelectedPage(parameter);
	hideAllNotices();
	DWRUtil.setValue('currentPageTitle', parameter.pageTitle);
	clearDesignView();
	showNotice('noFormNotice');
	if(parameter.buttonAreaId != null) {
		var dropBox = $('dropBox');
		if(dropBox != null) {
			var area = $('pageButtonArea');
			if(area != null) {
				dropBox.removeChild(area);
			}
			area = createButtonAreaNode();
			console.log(area.childNodes.length);
			for(var i=0;i<parameter.buttons.length;i++) {
				var buttonInfo = parameter.buttons[i];
				var newNode = createButtonNode(buttonInfo);
				area.appendChild(newNode);
			}
			dropBox.appendChild(area);
		}
	}
	closeLoadingMessage();
}
function loadThxPage(parameter) {
	showLoadingMessage('Loading section...');
	FormPage.getThxPageInfo(placeThxPageInfo);
}
function placeThxPageInfo(parameter) {
	markSelectedPage(parameter);
	STATIC_ACCORDEON.showTabByIndex(2, true);
	hideAllNotices();
	DWRUtil.setValue('currentPageTitle', parameter.pageTitle);
	clearDesignView();
	showNotice('noFormNotice');
	closeLoadingMessage();
}
function clearDesignView() {
	var dropBoxinner = $('dropBoxinner');
	if(dropBoxinner != null) {
		var childCount = dropBoxinner.childNodes.length;
		for(var i=0;i<childCount;i++) {
			var child = dropBoxinner.childNodes[0]
			dropBoxinner.removeChild(child);
		}
	}
	var dropBox = $('dropBox');
	if(dropBox != null) {
		var area = $('pageButtonArea');
		if(area != null) {
			dropBox.removeChild(area);
		}
	}
}
function showNotice(notice) {
	var empty = $(notice);
	if(empty != null) {
		if(empty.style) {
			empty.style.display = 'block';
		} else {
			empty.display = 'block';
		}
	}
}
function hideAllNotices() {
	var empty = $('emptyForm');
	if(empty != null) {
		if(empty.style) {
			empty.style.display = 'none';
		} else {
			empty.display = 'none';
		}
	}
	empty = $('noFormNotice');
	if(empty != null) {
		if(empty.style) {
			empty.style.display = 'none';
		} else {
			empty.display = 'none';
		}
	}
}
function placePageInfo(parameter) {
	if(parameter != null) {
		markSelectedPage(parameter);
		hideAllNotices();
		DWRUtil.setValue('currentPageTitle', parameter.pageTitle);
		var dropBoxinner = $('dropBoxinner');
		if(dropBoxinner != null) {
			var childCount = dropBoxinner.childNodes.length;
			
				for(var i=0;i<childCount;i++) {
					var child = dropBoxinner.childNodes[0]
					dropBoxinner.removeChild(child);
				}
				if(parameter.components.length > 0) {
					for(var i=0;i<parameter.components.length;i++) {
						var element = parameter.components[i];
						var transformed = createTreeNode(element.documentElement);
						var newNode = createNewComponent(transformed);
						dropBoxinner.appendChild(newNode);
					}
				} else {
					showNotice('emptyForm');
				}
		}
		if(parameter.buttonAreaId != null) {
			var dropBox = $('dropBox');
			if(dropBox != null) {
				var area = $('pageButtonArea');
				if(area != null) {
					dropBox.removeChild(area);
				}
				area = createButtonAreaNode();
				console.log(area.childNodes.length);
				for(var i=0;i<parameter.buttons.length;i++) {
					var buttonInfo = parameter.buttons[i];
					var newNode = createButtonNode(buttonInfo);
					area.appendChild(newNode);
				}
				dropBox.appendChild(area);
			}
		}
		closeLoadingMessage();
	}
}
function setupPagesDragAndDrop(value1, value2) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangePages,scroll:value1,constraint:false});
	Droppables.add(value1);
}
function rearrangePages() {
	draggingPage = true;
	var componentIDs = Sortable.serialize('pagesPanel',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormDocument.updatePagesList(componentIDs,idPrefix,delimiter,nothing);
}
function placePageTitle(parameter) {
	if(parameter != null) {
		var node = $(parameter.pageId + '_P_page');
		if(node != null) {
			var parent = node.childNodes[1];
			var textNode = parent.childNodes[0];
			var newTextNode = document.createTextNode(parameter.pageTitle);
			parent.replaceChild(newTextNode, textNode);
		}
	}
}
function setupButtonsDragAndDrop(value1, value2) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangeButtons,scroll:value1,constraint:false});
}
function rearrangeButtons() {
	draggingButton = true;
	var componentIDs = Sortable.serialize('pageButtonArea',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormPage.updateButtonList(componentIDs,idPrefix,delimiter,nothing);
}
function setupComponentDragAndDrop(value1,value2) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1 + 'inner',{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangeComponents,scroll:value1,constraint:false});
	dndMgr.registerDropZone(new FBDropzone('viewPanel', 'fbcomp'));
	dndMgr.registerDropZone(new FBDropzone('dropBox', 'fbbutton'));
	FormComponent.getId(markSelectedComponent);
}
function markSelectedComponent(parameter) {
	if(parameter != null) {
		var element = $(parameter);
		if(element != null) {
			element.setAttribute('class','formElement selectedElement');
			CURRENT_ELEMENT_ID = parameter;
		}
	}
}
function insertNewComponent(parameter) {
	hideAllNotices();
	console.log("Inserting: " + currentElement);
	if(parameter == 'append') {
		$('dropBoxinner').appendChild(currentElement);
		currentElement = null;
	} else {
		var node = $(parameter);
		$('dropBoxinner').insertBefore(currentElement, node);
		currentElement = null;
	}
	Sortable.create('dropBoxinner',{dropOnEmpty:true,tag:'div',only:'formElement',onUpdate:rearrangeComponents,scroll:'dropBoxinner',constraint:false});
}
function rearrangeComponents() {
	draggingComponent = true;
	var componentIDs = Sortable.serialize('dropBoxinner',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormPage.updateComponentList(componentIDs,idPrefix,delimiter,nothing);
}
function loadComponentInfo(component) {
	if(component != null) {
		if(component.id) {
			if(pressedComponentDelete == false && draggingComponent == false) {
				FormComponent.getFormComponentInfo(component.id, placeComponentInfo);
			}
			pressedComponentDelete = false;
			draggingComponent = false;
		}
	}
}
function placeComponentInfo(parameter) {
	if(parameter != null) {
		if(CURRENT_ELEMENT_ID != null) {
			PREVIOUS_ELEMENT_ID = CURRENT_ELEMENT_ID;
			$(PREVIOUS_ELEMENT_ID).setAttribute('class','formElement');
		}
		CURRENT_ELEMENT_ID = parameter.id;
		$(CURRENT_ELEMENT_ID).setAttribute('class','formElement selectedElement');
		
		console.log("Loading element: " + CURRENT_ELEMENT_ID + " instead of: " + PREVIOUS_ELEMENT_ID);
		if(parameter.plain == true) {
			var plainTxt = $('propertyPlaintext');
			if(plainTxt != null) {
				plainTxt.value = parameter.plainText;
				plainTxt.focus();
			}
			var plainPr = $('plainPropertiesPanel');
			if(plainPr != null) {
				plainPr.setAttribute('style', 'display: block');
			}
				var labelPr = $('labelPropertiesPanel');
				if(labelPr != null) {
					labelPr.setAttribute('style', 'display: none');
				}
				var compPr = $('basicPropertiesPanel');
				if(compPr != null) {
					compPr.setAttribute('style', 'display: none');
				}
				var autoPr = $('autoPropertiesPanel');
				if(autoPr != null) {
					autoPr.setAttribute('style', 'display: none');
				}
				var advPr = $('advPropertiesPanel');
				if(advPr != null) {
					advPr.setAttribute('style', 'display: none');
				}
				var extPr = $('extPropertiesPanel');
				if(extPr != null) {
					extPr.setAttribute('style', 'display: none');
				}
				var localPr = $('localPropertiesPanel');
				if(localPr != null) {
					localPr.setAttribute('style', 'display: none');
				}
		} else {
			var plainPr = $('plainPropertiesPanel');
			if(plainPr != null) {
				plainPr.setAttribute('style', 'display: none');
			}
			var labelPr = $('labelPropertiesPanel');
			if(labelPr != null) {
				labelPr.setAttribute('style', 'display: block');
			}
			var x = parameter.required;
			var labelTxt = $('propertyTitle');
			if(labelTxt != null) {
				labelTxt.value = parameter.label;
				labelTxt.focus();
			}
			var compPr = $('basicPropertiesPanel');
			if(compPr != null) {
				compPr.setAttribute('style', 'display: block');
			}
			var requiredChk = $('propertyRequired');
			if(requiredChk != null) {
				requiredChk.checked = parameter.required;
			}
			var errorTxt = $('propertyErrorMessage');
			if(errorTxt != null) {
				errorTxt.value = parameter.errorMessage;
			}
			var helpTxt = $('propertyHelpText');
			if(helpTxt != null) {
				helpTxt.value = parameter.helpMessage;
			}
			if(parameter.autofill == true) {
				var temp = $('propertyHasAutofill');
				if(temp != null) {
					temp.checked = true;
				}
				var compPr = $('autoPropertiesPanel');
				if(compPr != null) {
					compPr.setAttribute('style', 'display: block');
				}
				var autoTxt = $('propertyAutofill');
				if(autoTxt != null) {
					autoTxt.value = parameter.autofillKey;
				}
			} else {
				var temp = $('propertyHasAutofill');
				if(temp != null) {
					temp.checked = false;
				}
				var compPr = $('autoPropertiesPanel');
				if(compPr != null) {
					compPr.setAttribute('style', 'display: none');
				}
			}
			if(parameter.complex == true) {
				var emptyTxt = $('propertyEmptyLabel');
				if(emptyTxt != null) {
					emptyTxt.value = parameter.emptyLabel;
				}
				var advPr = $('advPropertiesPanel');
				if(advPr != null) {
					advPr.setAttribute('style', 'display: block');
				}
				if(parameter.local == true) {
					document.workspaceform1.dataSrcSwitch[0].checked = true;
					var localPr = $('localPropertiesPanel');
					loadItemset('selectOptsInner',parameter.items);
					if(localPr != null) {
						localPr.setAttribute('style', 'display: block');
					}
					var extPr = $('extPropertiesPanel');
					if(extPr != null) {
						extPr.setAttribute('style', 'display: none');
					}
				} else {
					document.workspaceform1.dataSrcSwitch[1].checked = true;
					var localPr = $('localPropertiesPanel');
					if(localPr != null) {
						localPr.setAttribute('style', 'display: none');
					}
					var extTxt = $('propertyExternal');
					if(extTxt != null) {
						extTxt.value = parameter.externalSrc;
					}
					var extPr = $('extPropertiesPanel');
					if(extPr != null) {
						extPr.setAttribute('style', 'display: block');
					}
				}
			} else {
				var advPr = $('advPropertiesPanel');
				if(advPr != null) {
					advPr.setAttribute('style', 'display: none');
				}
				var extPr = $('extPropertiesPanel');
				if(extPr != null) {
					extPr.setAttribute('style', 'display: none');
				}
				var localPr = $('localPropertiesPanel');
				if(localPr != null) {
					localPr.setAttribute('style', 'display: none');
				}
			}
		}	
		STATIC_ACCORDEON.showTabByIndex(1, true);
	}
}
function loadItemset(container,list) {
	var cont = $(container);
	if(cont != null) {
		var size = cont.childNodes.length;
		if(size > 0) {
			for(var k=0;k<size;k++) {
				var temp = cont.childNodes[0];
				if(temp != null) {
					cont.removeChild(temp);
				}
			}
		}
		if(list != null) {
			for(var i=0;i<list.length;i++) {
				var label = list[i].label;
				var value = list[i].value;
				var newInd = getNextRowIndex(cont);
				cont.appendChild(getEmptySelect(newInd,label,value));
			}
		}
	}
}
function toggleAutofill(parameter) {
	if(parameter == true) {
			
			var compPr = $('autoPropertiesPanel');
			if(compPr != null) {
				compPr.setAttribute('style', 'display: block');
			}
	} else {
			FormComponent.setAutofillKey('',nothing);
			var compPr = $('autoPropertiesPanel');
			if(compPr != null) {
				compPr.setAttribute('style', 'display: none');
			}
	}
	FormComponent.setAutofill(parameter,nothing);
}
function replaceChangedComponent(parameter) {
	var newNode = createTreeNode(parameter.documentElement);
	if(newNode != null) {
		var nodeId = newNode.id;
		var oldNode = $(nodeId);
		if(oldNode != null) {
			oldNode.replaceChild(newNode.childNodes[0], oldNode.childNodes[0]);
		}
	}
}
function saveComponentLabel(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentLabel(parameter, replaceChangedComponent);
	}
}
function saveRequired(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentRequired(parameter, replaceChangedComponent);
	}
}
function saveErrorMessage(parameter) {
	if(parameter != null) {
		FormComponent.setComponentErrorMessage(parameter, replaceChangedComponent);
	}
}
function saveExternalSrc(parameter) {
	if(parameter != null) {
		FormComponent.setComponentExternalSrc(parameter, replaceChangedComponent);
	}
}
function saveAutofill(parameter) {
	if(parameter != null) {
		FormComponent.setComponentAutofillKey(parameter, replaceChangedComponent);
	}
}
function savePlaintext(parameter) {
	if(parameter != null) {
		FormComponent.setComponentPlainText(parameter, replaceChangedComponent);
	}
}
function saveHelpMessage(parameter) {
	if(parameter != null) {
		FormComponent.setComponentHelpMessage(parameter, replaceChangedComponent);
	}
}
function switchDataSource() {
	FormComponent.switchDataSource(placeDataSource);
}
function placeDataSource(parameter) {
	if(parameter == true) {
			var extPr = $('extPropertiesPanel');
			if(extPr != null) {
				extPr.setAttribute('style', 'display: none');
			}
			var localPr = $('localPropertiesPanel');
			if(localPr != null) {
				localPr.setAttribute('style', 'display: block');
			}
	} else {
			var extPr = $('extPropertiesPanel');
			if(extPr != null) {
				extPr.setAttribute('style', 'display: block');
			}
			var localPr = $('localPropertiesPanel');
			if(localPr != null) {
				localPr.setAttribute('style', 'display: none');
			}
	}
}
function expandOrCollapse(node,expand) {
	if(expand == true) {
		node.previousSibling.style.display = 'inline';
		var value = node.previousSibling.value;
		if(value.length == 0) {
			node.previousSibling.value = node.previousSibling.previousSibling.value;
		}
		node.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_left.png';
		node.setAttribute('onclick','expandOrCollapse(this,false);');
	} else {
		node.previousSibling.style.display = 'none';
		node.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png';
		node.setAttribute('onclick','expandOrCollapse(this,true);');
	}
}
//TODO change these methods according to the new DWR support
function saveLabel(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.value;
	if(value.length != 0) {
		FormComponent.saveLabel(index,value,reRenderChangedComponent);
	}
}
function saveValue(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.value;
	if(value.length != 0) {
		FormComponent.saveValue(index,value,reRenderChangedComponent);
	}
}
function addNewItem(parameter) {
	var par = $(parameter).lastChild;
	var newInd = getNextRowIndex(par);
	par.appendChild(getEmptySelect(newInd,'',''));
}
function deleteThisItem(ind) {
	var index = ind.split('_')[1];
	FormComponent.removeItem(index,reRenderChangedComponent);
	var currRow = $(ind);
	var node = $(ind);
	if(node != null) {
		var node2 = node.parentNode;
		node2.removeChild(currRow);
	}
	
}
function getNextRowIndex(parameter) {
	var lastC = parameter.lastChild;
	if(lastC != null) {
		var ind = lastC.id.split('_')[1];
		ind++;
		return ind;
	} else {
		return 0;
	}
}
function expandAllItems() {
	var container = $('selectOptsInner');
	if(container != null) {
		for(var i=0;i<container.childNodes.length;i++) {
			container.childNodes[i].childNodes[2].style.display = 'inline';
		}
	}
}
function collapseAllItems() {
	var container = $('selectOptsInner');
	if(container != null) {
		for(var i=0;i<container.childNodes.length;i++) {
			container.childNodes[i].childNodes[2].style.display = 'none';
		}
	}
}
function getEmptySelect(index,lbl,vl) {
	var result = document.createElement('div');
	result.id = 'workspaceform1:rowDiv_' + index;
	var remB = document.createElement('img');
	remB.style.display = 'inline';
	remB.setAttribute('onclick','deleteThisItem(this.parentNode.id);');
	remB.id = 'delB_' + index;
	remB.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png';
	var label = document.createElement('input');
	label.id = 'labelF_' + index;
	label.setAttribute('type','text');
	label.style.display = 'inline';
	label.value = lbl;
	label.setAttribute('onblur','saveLabel(this);');
	label.setAttribute('class','fbSelectListItem');
	var value = document.createElement('input');
	value.id = 'valueF_' + index;
	value.setAttribute('type','text');
	value.value = vl;
	value.style.display = 'none';
	value.setAttribute('onblur','saveValue(this);');
	value.setAttribute('class','fbSelectListItem');
	var expB = document.createElement('img');
	expB.style.display = 'inline';
	expB.id = 'expB_' + index;
	expB.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png';
	expB.setAttribute('onclick','expandOrCollapse(this,true);');
	result.appendChild(remB);
	result.appendChild(label);
	result.appendChild(value);
	result.appendChild(expB);
	return result;
	
}
function saveFormDocument() {
	var saveButton = $('saveFormButton');
	if(saveButton != null) {
		saveButton.setAttribute('disabled','true');
	}
	showLoadingMessage('Saving document...');
	FormDocument.save(savedFormDocument);
}
function savedFormDocument(parameter) {
	closeLoadingMessage();
	var saveButton = $('saveFormButton');
	if(saveButton != null) {
		saveButton.setAttribute('disabled','false');
	}
}
function saveSourceCode(source_code) {
	if(source_code != null) {
		showLoadingMessage('Saving');
		FormDocument.saveSrc(source_code, doNothing);
	}
}
function doNothing(parameter) {
	closeLoadingMessage();
}
function nothing(parameter) {}
function createNewPage() {
	FormPage.createNewPage(placeNewPage);
}
function placeNewPage(parameter) {
	markSelectedPage(parameter);
	hideAllNotices();
	DWRUtil.setValue('currentPageTitle', parameter.pageTitle);
	clearDesignView();
	showNotice('noFormNotice');
	var container = $('pagesPanel');
	if(container != null) {
		var page = document.createElement('div');
		page.setAttribute('id', parameter.pageId + '_P_page');
		page.setAttribute('class', 'formPageIcon');
		page.setAttribute('styleClass', 'formPageIcon');
		page.setAttribute('onclick', 'loadPageInfo(this.id);');
		page.setAttribute('style', 'position: relative');
		
		var icon = document.createElement('img');
		icon.setAttribute('id', parameter.pageId + '_pi');
		icon.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png';
		
		icon.style.display = 'block';
		
		var label = document.createElement('span');
		label.style.display = 'block';
		
		var text = document.createTextNode('Section');
		
		label.appendChild(text);
		
		var db = document.createElement('img');
		db.id = parameter.pageId + '_db';
		db.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png';
		db.setAttribute('onclick', 'deletePage(this.id)');
		db.setAttribute('class', 'speedButton');
		
		page.appendChild(icon);
		page.appendChild(label);
		page.appendChild(db);
		
		container.appendChild(page);
		
	}
}
function deletePage(parameter) {
	var root = $(PAGES_PANEL_ID);
	if(root != null) {
		var nodes = root.getElementsByTagName('div');
		if(nodes.length == 1) {
			pressedPageDelete = true;
			setStatus('Cannot delete section',3000);
			return;
		}
	}
	if(parameter != null) {
		var node = $(parameter);
		if(node != null) {
			var parentNode = node.parentNode;
			if(parentNode != null) {
				pressedPageDelete = true;
				FormPage.removePage($(parameter).parentNode.id,removePageNode);
			}
		}
	}
}
function removePageNode(parameter) {
	if(parameter != null) {
		var node = $(parameter);
		if(node != null) {
			var parentNode = node.parentNode;
			if(parentNode != null) {
				showStatus('Loading section...');
				parentNode.removeChild(node);
			}
		}
	}
	//TODO load previous page
	closeStatus();
}
//Handles the closing of the loading indicator
function closeLoadingMessage() {
 	var elem = document.getElementById('busybuddy');
 	if (elem) {
  		if(elem.style) { 
       		elem.style.display = 'none';
     	} else {
       		elem.display = 'none' ;
     	}
 	}
}
//TODO redo the new form handling to get rid of a4j completely
function createNewForm() {
	var title = document.forms['newFormDialogForm'].elements['formName'].value;
	if(title != '') {
		closeMessage();
		showLoadingMessage("Creating form...");
		FormDocument.createFormDocument(title,createdNewForm);
	}
}
function createdNewForm(parameter) {
	if(parameter != null) {
		placeFormInfo(parameter);
		DWRUtil.setValue('formHeadingHeader', parameter.title);
		FormPage.getThxPageInfo(placeThxPage);
	}
}
function refreshWorkspace(parameter) {
	if(parameter != null) {
		placePageInfo(parameter);
		var container = $('pagesPanel');
		if(container != null) {
			var childCount = container.childNodes.length;
			for(var i=0;i<childCount;i++) {
				container.removeChild(container.childNodes[0]);
			}
			var firstPage = createNewPageNode(parameter,false);
			container.appendChild(firstPage);
		}
		closeLoadingMessage();
	}
}
function createNewPageNode(parameter,special) {
	var page = document.createElement('div');
	page.setAttribute('id', parameter.pageId + '_P_page');
	page.setAttribute('class', 'formPageIcon');
	page.setAttribute('styleClass', 'formPageIcon');
	page.setAttribute('onclick', 'loadPageInfo(this.id);');
	page.setAttribute('style', 'position: relative');
		
	var icon = document.createElement('img');
	icon.setAttribute('id', parameter.pageId + '_pi');
	icon.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png';
	//icon.style.display = 'block';
		
	var label = document.createElement('span');
	//label.style.display = 'block';
		
	var text = document.createTextNode(parameter.pageTitle);
	label.appendChild(text);
	
	page.appendChild(icon);
	page.appendChild(label);
	
	if(special == false) {
		return page;
	}
	var db = document.createElement('img');
	db.id = parameter.pageId + '_db';
	db.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png';
	db.setAttribute('onclick', 'deletePage(this.id)');
	db.setAttribute('class', 'speedButton');	
		
	page.appendChild(db);
	
	return page;
}
function placeThxPage(parameter) {
	var container = $('pagesPanelSpecial');
	if(container != null) {
		var childCount = container.childNodes.length;
		for(var i=0;i<childCount;i++) {
			container.removeChild(container.childNodes[0]);
		}
		var page = createNewPageNode(parameter,true);
		container.appendChild(page);
		FormPage.getFirstPageInfo(refreshWorkspace);
	}
}
function removeComponent(parameter) {
	var node = parameter.parentNode;
	if(node != null) {
		pressedComponentDelete = true;
		FormComponent.removeComponent(node.id, removeComponentNode);
	}
}
function removeComponentNode(parameter) {
	var node = $(parameter);
	if(node != null) {
		var parentNode = node.parentNode;
		if(parentNode != null) {
			parentNode.removeChild(node);
			if(parentNode.getElementsByTagName('div').length == 0) {
				showNotice('emptyForm');
			}
		}
	}
}
function createNewFormOnEnter(e) {
	if (!e) e = window.event;
	if (!e) return true;
	var key = (typeof e.keyCode != 'undefined' ? e.keyCode : e.charCode);
	if(key == '13') {
		createNewForm();
	}
}
function initializeWorkspace() {
	
}
initializeWorkspace();
/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);