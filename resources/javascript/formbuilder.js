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
var CURRENT_ELEMENT = null;

var FORM_TITLE = 'formHeadingHeader';
var PAGE_TITLE = 'currentPageTitle';
var CURRENT_ACTIVE_INLINE = null;
var INLINE_ACTION = null;
var INLINE_VALUE = null;
var INLINE_ID = null;

var CURRENT_BUTTON = null;

var currentElement = null;
var pressedComponentDelete = false;
var pressedButtonDelete = false;
var pressedPageDelete = false;
var draggingButton = false;
var draggingComponent = false;
var draggingPage = false;
var insideDropzone = false;
var existSelected = false;
var isThxPage = false;
var editButton = false;

Window.onDomReady(function() {
	var formTitle = $(FORM_TITLE);
	if(formTitle != null) {
		formTitle.addEvent('dblclick', function(e){
			enableInlineEdit(saveFormTitleAction, e);
		});
		formTitle.addEvent('click', function(e){
			loadFormInfo();
		});
	}
	var pageTitle = $(PAGE_TITLE);
	if(pageTitle != null) {
		pageTitle.addEvent('dblclick', function(e){
			enableInlineEdit(savePageTitleAction, e);
		});
	}
});
var FBDraggable = Element.extend({
	draggableTag: function(droppables, handle, type, autofill) {
		type = type;
		autofill = autofill;
		handle = handle || this;
		this.makeDraggable({
			'handle': handle,
			'droppables': droppables,
			onStart: function() {
				this.elementOrg = this.element;
				var now = {'x': this.element.getLeft(), 'y': this.element.getTop()};
				//console.log("x:" + now.x + "y:" + now.y);
				this.element = this.element.clone().setStyles({
					'position': 'absolute',
					'left': now.x + 'px',
					'top':  now.y + 'px',
					'opacity': '0.75'
				}).injectInside(document.body);
				this.value.now = now;
				if(type == 'fbcomp') {
					CURRENT_ELEMENT_UNDER = -1;
		   			childBoxes = [];
					var childNodes = $$('#dropBoxinner div.formElement');
					for(var i = 0; i < childNodes.length; i++){
						var child = childNodes[i];
						var pos = child.getCoordinates();
						childBoxes.push({top: pos.top, bottom: pos.bottom, left: pos.left, right: pos.right, height: pos.height, width: pos.width, node: child});
					}
					var temp = childBoxes;
					var dropBox = $('dropBoxinner');
					this.element.addEvent('mousemove', function(e) {
						if(!e) e = window.event;
						for(var i = 0, child; i < childBoxes.length; i++){
							with(childBoxes[i]){
								if (e.pageX >= left && e.pageX <= right && e.pageY >= top && e.pageY <= bottom) {
									CURRENT_ELEMENT_UNDER = i;
									var children;
									if(CURRENT_ELEMENT_UNDER != LAST_ELEMENT_UNDER) {
										LAST_ELEMENT_UNDER = CURRENT_ELEMENT_UNDER;
										var node = getEmptySpaceBox();
										children = $$('#dropBoxinner div.formElement');
										var count = children.length;
										var beforeNode = children[CURRENT_ELEMENT_UNDER];
										var ids = beforeNode.id;
										//console.log('Marker: '+ node.id + ' Before: '+ beforeNode.id);
										dropBox.insertBefore(node, beforeNode);
									}
								}
							}
						}
					});
					var info = new PaletteComponentInfo(this.elementOrg.id, this.autofill);
					if(draggingComponent == false) {
   						FormComponent.addComponent(info, placeNewComponent);
   						//console.log('Adding new component on drag start');
   						draggingComponent = true;
					}
				} else if(type == 'fbbutton') {
					
					/*var cont = $('pageButtonArea');
					if(cont == false) {
						cont = document.createElement('div');
						cont.id = 'pageButtonArea';
						cont.style.position = 'relative';
						cont.setAttribute('class','formElement');
						cont.style.backgroundColor = 'Silver';
						cont.injectInside($('dropBox'));
					}*/
					if(draggingButton == false) {
						//console.log('Dragging a button');
						FormComponent.addButton(this.elementOrg.id, placeNewButton);
						draggingButton = true;
					}
				}
			},
			onComplete: function(event) {
				if(!event) event = window.event;
				var now = {'x': this.element.getLeft(), 'y': this.element.getTop()};
				this.element.remove();
				this.element = this.elementOrg;
				this.elementOrg = null;
				if(type == 'fbcomp') {
					if(draggingComponent == true) {
						//console.log('Completing dragging');
						//draggingComponent = false;
						var currentId = currentElement.getAttribute('id');
						var dropBox = $('dropBoxinner');
						this.element.removeEvents('mousemove');
						if(insideDropzone == false) {
							FormComponent.removeComponent(currentId,nothing);
						}
					}
				} else if(type == 'fbbutton') {
					if(draggingButton == true) {
						//console.log('Stopepd Dragging a button');
						//draggingButton = false;
						if(insideDropzone == false) {
							//console.log('Button not inside dropZone');
							//FormComponent.removeButton(CURRENT_BUTTON.getAttribute('id'),nothing);
						}
					}
				}
				insideDropzone = false;
			}
		});
		this.setStyles({
			'position': ''
		});
		return this;
	}
});
Window.onDomReady(function() {
	if($('dropBoxinner') != null) {
		$('dropBoxinner').addEvents({
			'over': function(el){
				//console.log('Over a dropZone');
				if (!this.dragEffect) this.dragEffect = new Fx.Style(this, 'background-color');
				this.dragEffect.stop().start('ffffff', 'dddddd');
				insideDropzone = true;
			},
			'leave': function(el){
				//console.log('Leaving a dropZone');
				this.dragEffect.stop().start('dddddd', 'ffffff');
				insideDropzone = false;
			},
			'drop': function(el, drag){
				//console.log('Dropping');
				this.dragEffect.stop().start('ff8888', 'ffffff');
				if(draggingComponent == true) {
					draggingComponent = false;
					var currentId = currentElement.getAttribute('id');
				    if(CURRENT_ELEMENT_UNDER != null) {
						FormComponent.moveComponent(currentId, CURRENT_ELEMENT_UNDER, insertNewComponent);
				    }
				} else if(draggingButton == true) {
					//console.log('Dropping button');
					draggingButton = false;
					insertNodesToContainer(CURRENT_BUTTON.documentElement, $('pageButtonArea'));
				}
				insideDropzone = false;
			}
		});
	}
	/*if($('pageButtonArea') != null) {
		$('pageButtonArea').addEvents({
			'over': function(el){
				console.log('Over a dropZone');
				if (!this.dragEffect) this.dragEffect = new Fx.Style(this, 'background-color');
				this.dragEffect.stop().start('ffffff', 'dddddd');
				insideDropzone = true;
			},
			'leave': function(el){
				console.log('Leaving a dropZone');
				this.dragEffect.stop().start('dddddd', 'ffffff');
				insideDropzone = false;
			},
			'drop': function(el, drag){
				console.log('Dropped a button');
				this.dragEffect.stop().start('ff8888', 'ffffff');
				CURRENT_BUTTON.injectInside($('pageButtonArea'));
				insideDropzone = false;
			}
		});
	}*/
	$$('.fbcomp').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbcomp', false);
	});
	$$('.fbauto').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbcomp', false);
	});
	$$('.fbbutton').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbbutton', false);
	});
	var mySort = new Sortables($('dropBoxinner'), {
				onComplete: function(el){
				}
			});
});

function PaletteComponentInfo(type,autofill) {
	this.type = type;
	this.autofill = autofill;
}

function getEmptySpaceBox() {
	var node = document.createElement('div');
	node.id = 'insertMarker';
	node.setAttribute('style', 'backgroundColor: Red');
	return node;
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
function savePropertyOnEnter(value,attribute,event) {
	if(event.type == 'blur' || (event.type == 'keydown' && (typeof event.keyCode != 'undefined' ? event.keyCode : event.charCode) == '13')) {
		switch(attribute) {
			case 'compText':
				savePlaintext(value);
				break;
		  	case 'compTitle':
		  		saveComponentLabel(value);
		    	break;
		  	case 'compErr':
		    	saveErrorMessage(value);
		    	break;
		 	case 'compHelp':
		 		saveHelpMessage(value);
		 		break;
		 	case 'compAuto':
		 		saveAutofill(value);
		 		break;
		 	case 'compExt':
		  		saveExternalSrc(value);
		  		break;
		  	case 'formTitle':
		 		saveFormTitle(value);
		 		break;
		 	case 'formThxTitle':
		 		saveThankYouTitle(value);
		 		break;
		 	case 'formThxText':
		 		saveThankYouText(value);
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
/*function createButtonNode(parameter) {
	var node = document.createElement('div');
	node.id = parameter.id;
	node.style.display = 'inline';
	node.setAttribute('class', 'formButton');
	node.setAttribute('onclick', "loadButtonInfo(this);");
	
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
}*/
function createNewComponent(htmlNode) {
	var node = document.createElement('div');
	var nodeId = htmlNode.getAttribute('id');
	node.setAttribute('id', nodeId);
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
		//console.log('New button ' + parameter);
		CURRENT_BUTTON = parameter;
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
function loadButtonInfo(button) {
	if(button != null) {
		if(button.id) {
			if(pressedButtonDelete == false) {
				FormComponent.getFormButtonInfo(button.id, {
					callback: function(resultDOM) {
						placeButtonInfo(resultDOM, 1);
					}
				});
			}
		}
	}
	pressedButtonDelete = false;
	//draggingButton = false;
}
function placeButtonInfo(resultDOM, tabIndex) {
	var parentNode = $('panel' + tabIndex + 'Content');
	if(parentNode != null && resultDOM != null) {
		removeChildren(parentNode);
		insertNodesToContainer(resultDOM.documentElement, parentNode);
		iwAccordionfbMenu.display(tabIndex);
	}
}
function placeNewComponent(parameter) {
	//console.log('Received from server: ' + parameter);
	if(parameter != null) {
		currentElement = createNewComponent(createTreeNode(parameter.documentElement));
		//console.log('Setting currentElement: ' + currentElement);
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
function enableInlineEdit(action, event) {
	var event = new Event(event);
	var node = event.target;
	if(node != null) {
		var text = node.childNodes[0].nodeValue;
		var parentNode = node.parentNode;
		INLINE_ID = node.id;
		INLINE_VALUE = text;
		CURRENT_ACTIVE_INLINE = node;
		parentNode.removeChild(node);
		var input = document.createElement('input');
		input.setAttribute('type', 'text');
		input.value = text;
		input.id = INLINE_ID;
		input.onblur = action;
		input.onkeydown = action;
		parentNode.appendChild(input);
		INLINE_ACTION = action;
		input.focus();
	}
}
function savePageTitleAction(event) {
	if(event.type == 'blur' || (event.type == 'keydown' && (typeof event.keyCode != 'undefined' ? event.keyCode : event.charCode) == '13')) {
		var text = event.target.value;
		if(text != '') {
			var node = $(CURRENT_PAGE_ID);
			if(node != null) {
				var parent = node.childNodes[1];
				var textNode = parent.childNodes[0];
				var newTextNode = document.createTextNode(text);
				parent.replaceChild(newTextNode, textNode);
			}
			FormPage.setTitle(text, disableInlineEdit);
			INLINE_VALUE = text;
			if(isThxPage == true) {
				$('workspaceform1:thankYouTitle').value = text;
			}
		}
	}
}
function saveFormTitleAction(event) {
	if(event.type == 'blur' || (event.type == 'keydown' && (typeof event.keyCode != 'undefined' ? event.keyCode : event.charCode) == '13')) {
		var text = event.target.value;
		if(text != '') {
			$('formTitle').value = text;
			FormDocument.setFormTitle(text, disableInlineEdit);
			INLINE_VALUE = text;
		}
		DWRUtil.setValue('formTitle', text);
	}
}
function disableInlineEdit() {
	var node = $(INLINE_ID);
	if(node != null) {
		var text = node.value;
		var parentNode = node.parentNode;
		parentNode.removeChild(node);
		if(CURRENT_ACTIVE_INLINE != null) {
			var span = CURRENT_ACTIVE_INLINE;
			span.childNodes[0].nodeValue = text;
			parentNode.appendChild(span);
			CURRENT_ACTIVE_INLINE = null;
		}
		INLINE_VALUE = null;
		INLINE_ID = null;
	}
}

function loadProcessTaskList(value) {
	if(value != null && value != '') {
		JbpmBusiness.getProcessDefinitionTasks(value, placeProcessTaskList);
	}
}
function loadTaskProperties(value) {
	if(value != null) {
		JbpmBusiness.getTaskProperties(value, placeTaskProperties);
	}
}
function placeProcessTaskList(values) {
	if(values != null) {
		DWRUtil.removeAllOptions('taskSelector');
		DWRUtil.addOptions('taskSelector', values, 'id', 'value');
	}
}
function placeTaskProperties(values) {
	if(values != null) {
		$('taskForm').setAttribute('disabled','false');
		$('taskForm').setAttribute('disabled', 'false');
		$('taskGroup').setAttribute('disabled', 'false');
		//DWRUtil.setValue('taskForm', 'dfgdfgdfg');
		//DWRUtil.setValue('taskGroup', 'dfgdfgdfg');
	}
}
function loadFormInfo() {
	FormDocument.getFormDocumentInfo({
		callback: function(resultDOM) {
			placeFormInfo(resultDOM, 2);
		}
	});
}
function placeFormInfo(resultDOM, tabIndex) {
	var parentNode = $('panel' + tabIndex + 'Content');
	if(parentNode != null) {
		removeChildren(parentNode);
		insertNodesToContainer(resultDOM.documentElement, parentNode);
		iwAccordionfbMenu.display(tabIndex);
	}
	/*if(parameter != null) {
		DWRUtil.setValue('formTitle', parameter.title);
		DWRUtil.setValue('previewScreen', parameter.hasPreview);
		DWRUtil.setValue('thankYouTitle', parameter.thankYouTitle);
		DWRUtil.setValue('thankYouText', parameter.thankYouText);
		iwAccordionfbMenu.display(2);
	}*/
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
function saveEnableBubbles(parameter) {
	if(parameter != null) {
		if(parameter.checked) {
			//alert('Not implemented');
			FormDocument.setEnableBubbles(parameter.checked, nothing);
		}
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
		if(isThxPage == true) {
			//alert($('currentPageTitle').nodeValue);
			//$('currentPageTitle').childNodes[0].value = parameter.pageTitle;
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
		if(parameter.checked) {
			FormDocument.togglePreviewPage(parameter.checked, placePreviewPage);
		}
	}
}
function placePreviewPage(parameter) {
	var container = $('pagesPanelSpecial');
	if(container != null) {
		if(parameter.pageTitle != null) {
			var page = createNewPageNode(parameter,true);
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
		if($(PREVIOUS_PAGE_ID) != null) {
			$(PREVIOUS_PAGE_ID).setAttribute('class','formPageIcon');
		}
	}
	var pageNode = $(parameter);
	if(pageNode == null) {
		pageNode = $(parameter + '_P_page');
		CURRENT_PAGE_ID = parameter + '_P_page';
	} else {
		CURRENT_PAGE_ID = parameter;
	}
	pageNode.setAttribute('class','formPageIcon selectedElement');
}
function loadConfirmationPage(parameter) {
	showLoadingMessage('Loading section...');
	FormPage.getConfirmationPageInfo(placeConfirmationPageInfo);
}
function placeConfirmationPageInfo(parameter) {
	isThxPage = false;
	markSelectedPage(parameter.pageId);
	hideAllNotices();
	DWRUtil.setValue('currentPageTitle', parameter.pageTitle);
	clearDesignView();
	showNotice('noFormNotice');
	if(parameter.buttonAreaId != null) {
		var area = $('pageButtonArea');
		if(area != false) area.remove();
		area = createButtonAreaNode();
		for(var i=0;i<parameter.buttons.length;i++) {
			var buttonInfo = parameter.buttons[i];
			var newNode = createButtonNode(buttonInfo);
			area.appendChild(newNode);
		}
		area.injectInside($('dropBox'));
	}
	closeLoadingMessage();
}
function loadThxPage(parameter) {
	showLoadingMessage('Loading section...');
	FormPage.getThxPageInfo(placeThxPageInfo);
}
function placeThxPageInfo(parameter) {
	isThxPage = true;
	markSelectedPage(parameter.pageId);
	iwAccordionfbMenu.display(2);
	hideAllNotices();
	DWRUtil.setValue('currentPageTitle', parameter.pageTitle);
	clearDesignView();
	showNotice('noFormNotice');
	closeLoadingMessage();
}
function clearDesignView() {
	var dropBoxinner = $('dropBoxinner');
	if(dropBoxinner != false) {
		var childCount = dropBoxinner.getChildren();
		for(var i=0;i<childCount.length;i++) {
			var child = childCount[i];
			child.remove();
		}
	}
	var area = $('pageButtonArea');
	if(area != false) area.remove();
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
function loadPageInfo(event) {
	var event = new Event(event);
	if(event != null) {
		if(event.target) {
			if(pressedPageDelete == false && draggingPage == false) {
				showLoadingMessage('Loading section...');
				FormPage.getFormPageInfo(event.target.id, {
					callback: function(resultDOM) {
						placePageInfo(resultDOM, event.target.id);
					}
				});
			}
		}
	}
}
function placePageInfo(resultDOM, pageId) {
	isThxPage = false;
	if(resultDOM != null) {
		markSelectedPage(pageId);
		var dropBox = $('dropBox');
		if(dropBox != null) {
			var parentNode = dropBox.parentNode;
			var node = parentNode.getLast();
			node.remove();
			insertNodesToContainer(resultDOM, parentNode);
			setupComponentDragAndDrop('dropBox', 'formElement');
		}
		closeLoadingMessage();
	}
}
function setupPagesDragAndDrop(value1, value2) {
	//Position.includeScrollOffsets = true;
	//Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangePages,scroll:value1,constraint:false});
	//Droppables.add(value1);
	FormPage.getId(markSelectedPage);
}
function rearrangePages() {
	draggingPage = true;
	//var componentIDs = Sortable.serialize('pagesPanel',{tag:'div',name:'id'});
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
	//Position.includeScrollOffsets = true;
	//Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangeButtons,scroll:value1,constraint:false});
}
function rearrangeButtons() {
	//draggingButton = true;
	//var componentIDs = Sortable.serialize('pageButtonArea',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormPage.updateButtonList(componentIDs,idPrefix,delimiter,nothing);
}
function setupComponentDragAndDrop(value1,value2) {
	//Position.includeScrollOffsets = true;
	//Sortable.create(value1 + 'inner',{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangeComponents,scroll:value1,constraint:false});
	//dndMgr.registerDropZone(new FBDropzone('viewPanel', 'fbcomp'));
	//dndMgr.registerDropZone(new FBDropzone('dropBox', 'fbbutton'));
	FormComponent.getId(markSelectedComponent);
}
function markSelectedComponent(parameter) {
	if(parameter != null) {
		var element = $(parameter);
		if(element != null) {
			//element.setAttribute('class','formElement selectedElement');
			CURRENT_ELEMENT_ID = parameter;
		}
	}
}
function insertNewComponent(parameter) {
	hideAllNotices();
	//console.log("Inserting: " + currentElement);
	if(parameter == 'append') {
		$('dropBoxinner').appendChild(currentElement);
		currentElement = null;
	} else {
		var node = $(parameter);
		$('dropBoxinner').insertBefore(currentElement, node);
		currentElement = null;
	}
	//Sortable.create('dropBoxinner',{dropOnEmpty:true,tag:'div',only:'formElement',onUpdate:rearrangeComponents,scroll:'dropBoxinner',constraint:false});
}
function rearrangeComponents() {
	draggingComponent = true;
	//var componentIDs = Sortable.serialize('dropBoxinner',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormPage.updateComponentList(componentIDs,idPrefix,delimiter,nothing);
}
function loadComponentInfo(component) {
	if(component != null) {
		if(component.id) {
			if(pressedComponentDelete == false && draggingComponent == false) {
				FormComponent.getFormComponentInfo(component.id, {
					callback: function(resultDOM) {
						placeComponentInfo(resultDOM, 1, component.id);
					}
				});
			}
			pressedComponentDelete = false;
			draggingComponent = false;
		}
	}
}
function placeComponentInfo(resultDOM, tabIndex, component) {
	var parentNode = $('panel' + tabIndex + 'Content');
	if(parentNode != null) {
		CURRENT_ELEMENT = component;
		removeChildren(parentNode);
		insertNodesToContainer(resultDOM.documentElement, parentNode);
		iwAccordionfbMenu.display(1);
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
function replaceChangedComponent(resultDOM) {
	if(resultDOM != null) {
		var componentNode = resultDOM.documentElement;
		var oldNode = $(CURRENT_ELEMENT);
		if(oldNode != null) {
			replaceNode(resultDOM, oldNode, $('dropBoxinner'));
		}
	}
}
function replaceChangedButton(parameter) {
	if(parameter != null) {
		var button = $(parameter.id).childNodes[0];
		if(button != null) {
			button.value = parameter.label;
		}
	}
}
function saveComponentLabel(value) {
	if(value != null) {
		if(editButton == true) {
			//console.log('Saving button label: ' + parameter);
			FormComponent.saveButtonLabel(value, replaceChangedButton);
		} else {
			//console.log('Saving component label: ' + parameter);
			FormComponent.saveComponentLabel(value, {
				callback: function(resultDOM) {
					replaceChangedComponent(resultDOM);
				}
			});
		}
	}
}
function saveRequired(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentRequired(parameter, replaceChangedComponent);
	}
}
function saveErrorMessage(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentErrorMessage(parameter, replaceChangedComponent);
	}
}
function saveExternalSrc(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentExternalSrc(parameter, replaceChangedComponent);
	}
}
function saveAutofill(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentAutofillKey(parameter, replaceChangedComponent);
	}
}
function savePlaintext(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentPlainText(parameter, replaceChangedComponent);
	}
}
function saveHelpMessage(parameter) {
	if(parameter != null) {
		FormComponent.saveComponentHelpMessage(parameter, replaceChangedComponent);
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
function saveLabel(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.value;
	if(value.length != 0) {
		FormComponent.saveSelectOptionLabel(index,value,replaceChangedComponent);
	}
}
function saveValue(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.value;
	if(value.length != 0) {
		FormComponent.saveSelectOptionValue(index,value,replaceChangedComponent);
	}
}
function addNewItem(parameter) {
	var par = $(parameter).lastChild;
	var newInd = getNextRowIndex(par);
	par.appendChild(getEmptySelect(newInd,'',''));
}
function deleteThisItem(ind) {
	var index = ind.split('_')[1];
	var currRow = $(ind);
	var node = $(ind);
	if(node != null) {
		var node2 = node.parentNode;
		node2.removeChild(currRow);
	}
	FormComponent.removeSelectOption(index,replaceChangedComponent);
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
	/*var saveButton = $('saveFormButton');
	if(saveButton != null) {
		saveButton.setAttribute('disabled','true');
	}*/
	showLoadingMessage('Saving document...');
	FormDocument.save(savedFormDocument);
}
function savedFormDocument(parameter) {
	closeLoadingMessage();
	/*var saveButton = $('saveFormButton');
	if(saveButton != null) {
		saveButton.setAttribute('disabled','false');
	}*/
}
function saveSourceCode(source_code) {
	if(source_code != null) {
		showLoadingMessage('Saving');
		FormDocument.saveSrc(source_code, savedSourceCode);
	}
}
function savedSourceCode(parameter) {
	closeLoadingMessage();
}
function nothing(parameter) {}
function createNewPage() {
	FormPage.createNewPage(placeNewPage);
}
function placeNewPage(parameter) {
	hideAllNotices();
	DWRUtil.setValue('currentPageTitle', parameter.pageTitle);
	clearDesignView();
	showNotice('emptyForm');
	var box = createButtonAreaNode();
	$('dropBox').appendChild(box);
	var container = $('pagesPanel');
	if(container != null) {
		var page = createNewPageNode(parameter,false);
		container.appendChild(page);
		markSelectedPage(parameter.pageId);
	}
}
function deletePage(parameter) {
	var root = $(PAGES_PANEL_ID);
	if(root != false) {
		var nodes = root.getChildren();
		if(nodes.length == 1) {
			pressedPageDelete = true;
			return;
		}
	}
	if(parameter != null) {
		var node = $(parameter);
		if(node != false) {
			//node.remove();
			var parentNode = node.parentNode;
			if(parentNode != null) {
				pressedPageDelete = true;
				FormPage.removePage(parentNode.id,removePageNode);
			}
		}
	}
}
function removePageNode(parameter) {
	if(parameter != null) {
		var node = $(parameter);
		if(node != false) node.remove();
	}
	//TODO load previous page
}
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
		
	var label = document.createElement('span');
		
	var text = document.createTextNode(parameter.pageTitle);
	label.appendChild(text);
	
	page.appendChild(icon);
	page.appendChild(label);
	
	if(special == true) {
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
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);