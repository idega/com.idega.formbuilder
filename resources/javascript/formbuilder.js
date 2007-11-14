var PAGES_PANEL_ID = 'pagesPanel';
var BUTTON_AREA_ID = 'pageButtonArea';
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
var isThxPage = false;

var FBDraggable = Element.extend({
	draggableTag: function(droppables, handle, type) {
		type = type;
		handle = handle || this;
		this.makeDraggable({
			'handle': handle,
			'droppables': droppables,
			onStart: function() {
				this.elementOrg = this.element;
				var now = {'x': this.element.getLeft(), 'y': this.element.getTop()};
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
					var dropBox = $('dropBoxinner');
					this.element.addEvent('mousemove', function(e) {
						if(!e) e = window.event;
						for(var i = 0, child; i < childBoxes.length; i++){
							with(childBoxes[i]){
								if (e.pageX >= left && e.pageX <= right && e.pageY >= top && e.pageY <= bottom) {
									CURRENT_ELEMENT_UNDER = i;
									if(CURRENT_ELEMENT_UNDER != LAST_ELEMENT_UNDER) {
										LAST_ELEMENT_UNDER = CURRENT_ELEMENT_UNDER;
									}
								}
							}
						}
					});
					if(draggingComponent == false) {
   						FormComponent.addComponent(this.elementOrg.id, {
							callback: function(resultDOM) {
								if(resultDOM != null) {
									currentElement = resultDOM;
								}
							}
						});
   						draggingComponent = true;
					}
				} else if(type == 'fbbutton') {
					if(draggingButton == false) {
						FormComponent.addButton(this.elementOrg.id, {
							callback: function(resultDOM) {
								if(resultDOM != null) {
									CURRENT_BUTTON = resultDOM;
								}
							}
						});
						draggingButton = true;
					}
				} else if(type == 'fbprocess') {
					CURRENT_ELEMENT_UNDER = -1;
		   			childBoxes = [];
					var childNodes = $$('#dropBoxinner div.formElement');
					for(var i = 0; i < childNodes.length; i++){
						var child = childNodes[i];
						var pos = child.getCoordinates();
						childBoxes.push({top: pos.top, bottom: pos.bottom, left: pos.left, right: pos.right, height: pos.height, width: pos.width, node: child});
					}
					var dropBox = $('dropBoxinner');
					this.element.addEvent('mousemove', function(e) {
						if(!e) e = window.event;
						for(var i = 0, child; i < childBoxes.length; i++){
							with(childBoxes[i]){
								if (e.pageX >= left && e.pageX <= right && e.pageY >= top && e.pageY <= bottom) {
									CURRENT_ELEMENT_UNDER = i;
									if(CURRENT_ELEMENT_UNDER != LAST_ELEMENT_UNDER) {
										LAST_ELEMENT_UNDER = CURRENT_ELEMENT_UNDER;
									}
								}
							}
						}
					});
					if(draggingComponent == false) {
   						FormComponent.addComponentWithVariable(this.elementOrg.id, {
							callback: function(resultDOM) {
								if(resultDOM != null) {
									currentElement = resultDOM;
								}
							}
						});
   						draggingComponent = true;
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
						if(insideDropzone == false) {
							var currentId = currentElement.documentElement.getAttribute('id');
							this.element.removeEvents('mousemove');
							FormComponent.removeComponent(currentId,nothing);
							draggingComponent = false;
						}
					}
				} else if(type == 'fbbutton') {
					if(draggingButton == true) {
						if(insideDropzone == false) {
							FormComponent.removeButton(CURRENT_BUTTON.documentElement.getAttribute('id'),nothing);
							draggingButton = false;
						}
					}
					
				} else if(type == 'fbprocess') {
					if(draggingComponent == true) {
						if(insideDropzone == false) {
							var currentId = currentElement.documentElement.getAttribute('id');
							this.element.removeEvents('mousemove');
							FormComponent.removeComponent(currentId,nothing);
						}
					}
				}
			}
		});
		this.setStyles({
			'position': ''
		});
		return this;
	}
});
function setupDesignView(componentArea, component, pageTitle, formTitle) {
	var formTitle = $(formTitle);
	if(formTitle != null) {
		formTitle.addEvent('dblclick', function(e){
			enableInlineEdit(saveFormTitleAction, e);
		});
		formTitle.addEvent('click', function(e){
			loadFormInfo();
		});
	}
	/*var pageTitle = $(pageTitle);
	if(pageTitle != null) {
		pageTitle.addEvent('dblclick', function(e){
			enableInlineEdit(savePageTitleAction, e);
		});
	}*/
	FormComponent.getId(markSelectedComponent);
	var myComponentSort = new Sortables($('dropBoxinner'), {
		onComplete: function(el){
			var children = $('dropBoxinner').getChildren();
			var orderList = [];
			for(var i = 0; i < children.length; i++) {
				var element = children[i];
				orderList.push(element.id);
			}
			FormPage.updateComponentList(orderList, nothing);
		},
		handles: '.fbCompHandler'
	});
	if($('dropBoxinner') != null) {
		$('dropBoxinner').addEvents({
			'over': function(el){
				if (!this.dragEffect) this.dragEffect = new Fx.Style(this, 'background-color');
				this.dragEffect.stop().start('ffffff', 'dddddd');
				insideDropzone = true;
			},
			'leave': function(el){
				this.dragEffect.stop().start('dddddd', 'ffffff');
				insideDropzone = false;
			},
			'drop': function(el, drag){
				this.dragEffect.stop().start('ff8888', 'ffffff');
				if(draggingComponent == true) {
					draggingComponent = false;
					var currentId = currentElement.documentElement.getAttribute('id');
				    if(CURRENT_ELEMENT_UNDER != null) {
						FormComponent.moveComponent(currentId, CURRENT_ELEMENT_UNDER, insertNewComponent);
				    }
				} else if(draggingButton == true) {
					draggingButton = false;
					if(CURRENT_BUTTON != null) {
						insertNodesToContainer(CURRENT_BUTTON, $('pageButtonArea'));
					}
				}
				insideDropzone = false;
			}
		});
	}
	$$('.fbcomp').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbcomp');
	});
	$$('.fbprocess').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbprocess');
	});
	$$('.fbbutton').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbbutton');
	});
}
Window.onDomReady(function() {
	initMenu();
	setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
	setupPagesPanel();
});
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
function savePropertyOnEnter(value,attribute,event) {
	if(event.type == 'blur' || isEnterEvent(event)) {
		switch(attribute) {
			case 'compText':
				savePlaintext(value);
				break;
		  	case 'compTitle':
		  		saveComponentLabel(value);
		    	break;
		    case 'btnTitle':
		    	saveButtonLabel(value);
		    	break;
		    case 'compProcVar':
		  		saveComponentProcessVariableName(value);
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
function removeButton(parameter) {
	if(parameter != null) {
		if(parameter.parentNode) {
			var buttonId = parameter.parentNode.id;
			pressedButtonDelete = true;
			FormComponent.removeButton(buttonId, {
				callback: function(result) {
					var node = $(result);
					if(node != null) {
						node.remove();
					}
				}
			});
		}
	}
}
function loadButtonInfo(button) {
	if(button != null) {
		if(button.id) {
			if(pressedButtonDelete == false) {
				FormComponent.getFormButtonInfo(button.id, {
					callback: function(resultDOM) {
						CURRENT_ELEMENT = button.id;
						var tabIndex = 1;
						var parentNode = $('panel' + tabIndex + 'Content');
						if(parentNode != null && resultDOM != null) {
							removeChildren(parentNode);
							insertNodesToContainer(resultDOM, parentNode);
							iwAccordionfbMenu.display(0);
							iwAccordionfbMenu.display(tabIndex);
						}
					}
				});
			}
		}
	}
	pressedButtonDelete = false;
}
function reloadInlineEdit(idle, event) {
	var target = event.target;
	var box = target.parentNode;
	Workspace.getRenderedInlineEdit(idle, {
		callback: function(resultDOM) {
			replaceNode(resultDOM, box, box.parentNode);
		}
	});
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
	if(event.type == 'blur' || isEnterEvent(event)) {
		var text = event.target.value;
		if(text != '') {
			var node = $(CURRENT_PAGE_ID);
			if(node != null) {
				var parent = node.childNodes[3];
				var textNode = parent.childNodes[0];
				var newTextNode = document.createTextNode(text);
				parent.replaceChild(newTextNode, textNode);
			}
			FormPage.setTitle(text, disableInlineEdit);
			INLINE_VALUE = text;
		}
	}
}
function saveFormTitleAction(event) {
	if(event.type == 'blur' || isEnterEvent(event)) {
		var text = event.target.value;
		if(text != '') {
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
		insertNodesToContainer(resultDOM, parentNode);
		iwAccordionfbMenu.display(tabIndex);
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
function saveEnableBubbles(parameter) {
	if(parameter != null) {
		if(parameter.checked) {
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
function saveHasPreview(event) {
	new Event(event).stop();
	var target = event.target;
	var checked;
	if(target.hasClass('addPreviewPageBtn')) {
		checked = true;
	} else {
		checked = false;
	}
	FormDocument.togglePreviewPage(checked, {
		callback: function(resultDOM) {
			if(resultDOM != null) {
				if(checked == true) {
					$('previewPageButton').removeClass('addPreviewPageBtn').addClass('removePreviewPageBtn');
					insertNodesToContainerBefore(resultDOM, $('pagesPanelSpecial'), $('pagesPanelSpecial').childNodes[0]);
				} else {
					$('previewPageButton').removeClass('removePreviewPageBtn').addClass('addPreviewPageBtn');
					$('pagesPanelSpecial').getFirst().remove();
				}
			}
		}
	});
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
	if(pageNode != null) 
		pageNode.setAttribute('class','formPageIcon selectedElement');
}
function loadConfirmationPage(parameter) {
	showLoadingMessage('Loading section...');
	FormPage.getConfirmationPageInfo({
		callback: function(resultDOM) {
			isThxPage = false;
			if(resultDOM != null) {
				markSelectedPage(parameter);
				var dropBox = $('dropBox');
				if(dropBox != null) {
					var parentNode = dropBox.parentNode;
					var node = parentNode.getLast();
					node.remove();
					insertNodesToContainer(resultDOM, parentNode);
					setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
				}
				closeLoadingMessage();
			}
		}
	});
}
function loadThxPage(parameter) {
	showLoadingMessage('Loading section...');
	FormPage.getThxPageInfo({
		callback: function(resultDOM) {
			isThxPage = true;
			if(resultDOM != null) {
				markSelectedPage(parameter);
				var dropBox = $('dropBox');
				if(dropBox != null) {
					var parentNode = dropBox.parentNode;
					var node = parentNode.getLast();
					node.remove();
					insertNodesToContainer(resultDOM, parentNode);
					setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
				}
				closeLoadingMessage();
			}
		}
	});
}
function loadPageInfo(targetId) {
	if(draggingPage == false) {
		showLoadingMessage('Loading section...');
		FormPage.getFormPageInfo(targetId, {
			callback: function(resultDOM) {
				isThxPage = false;
				if(resultDOM != null) {
					markSelectedPage(targetId);
					var dropBox = $('dropBox');
					if(dropBox != null) {
						var parentNode = dropBox.parentNode;
						var node = parentNode.getLast();
						node.remove();
						insertNodesToContainer(resultDOM, parentNode);
						setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
					}
					closeLoadingMessage();
				}
			}
		});
	}
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
function setupPagesPanel() {
	var myPagesSort = new Sortables($(PAGES_PANEL_ID), {
		onComplete: function(el){
			var children = $(PAGES_PANEL_ID).getChildren();
			var orderList = [];
			for(var i = 0; i < children.length; i++) {
				var element = children[i];
				orderList.push(element.id);
			}
			FormDocument.updatePagesList(orderList, nothing);
		},
		handles: '.fbPageHandler'
	});
	FormPage.getId(markSelectedPage);
}
function markSelectedComponent(parameter) {
	if(parameter != null) {
		var element = $(parameter);
		if(element != null) {
			CURRENT_ELEMENT_ID = parameter;
		}
	}
}
function insertNewComponent(parameter) {
	if($('emptyForm') != null) {
		$('emptyForm').remove();
	}
	if(parameter == 'append') {
		insertNodesToContainer(currentElement, $('dropBoxinner'));
		currentElement = null;
	} else {
		var node = $(parameter);
		insertNodesToContainerBefore(currentElement, $('dropBoxinner'), node);
		currentElement = null;
	}
	setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
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
		if(component != null) 
			CURRENT_ELEMENT = component;
		removeChildren(parentNode);
		insertNodesToContainer(resultDOM, parentNode);
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
	if(parameter != null) {
		var node = $('propertyAutofill');
		if(parameter == false) {
			node.removeClass('activeAutofill');
		} else {
			node.addClass('activeAutofill');
		}
	}
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
function saveButtonLabel(value) {
	if(value != null) {
		FormComponent.saveComponentLabel(value, {
			callback: function(resultDOM) {
				var btn = $(CURRENT_ELEMENT);
				if(btn != null)
					var nodes = btn.getChildren();
				nodes[0].value = value;
			}
		});
	}
}
function saveButtonAction(value) {
    if(value != null) {
        FormComponent.saveComponentAction(value, nothing);
    }
}
function saveComponentLabel(value) {
	if(value != null) {
		FormComponent.saveComponentLabel(value, {
			callback: function(resultDOM) {
				replaceChangedComponent(resultDOM);
			}
		});
	}
}
function saveComponentProcessVariableName(value) {
	FormComponent.saveComponentProcessVariableName(value, nothing);
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
function switchDataSource() {
	FormComponent.switchDataSource({
		callback: function(resultDOM) {
			placeComponentInfo(resultDOM, 1, null);
		}
	});
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
	remB.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png';
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
function switchView(view, id) {
	showLoadingMessage('Switching...');
	Workspace.switchView(view, {
		callback: function(resultDOM) {
			replaceNode(resultDOM, $('viewPanel'), $('mainWorkspace'));
			closeLoadingMessage();
		}
	});
}
function fbsave() {
	var node = $('sourceViewDiv');
	if(node != null) {
		showLoadingMessage('Saving');
		FormDocument.saveSrc(sourceTextarea.getCode(), closeLoadingMessage);
	} else {
		showLoadingMessage('Saving document...');
		FormDocument.save(closeLoadingMessage);
	}
}
function saveFormDocument() {
	showLoadingMessage('Saving document...');
	FormDocument.save(closeLoadingMessage);
}
function saveSourceCode(source_code) {
	if(source_code != null) {
		showLoadingMessage('Saving');
		FormDocument.saveSrc(source_code, closeLoadingMessage);
	}
}
function reloadAssignVariable(event) {
	new Event(event).stop();
	var target = event.target;
	var container = target.parentNode;
	var containerId = container.id;
	var tokens = containerId.split('-');
	FormComponent.getAvailableComponentVariables(null, tokens[1], tokens[2], {
		callback: function(resultDOM) {
			replaceNode(resultDOM, container, container.parentNode);
			var token1 = tokens[1];
			var token2 = tokens[2];
			var chooser = $('assignV-' + tokens[1] + '-' + tokens[2]);
			chooser.getLast().focus();
			chooser.getLast().addEvent('change', function(e){
				new Event(e).stop();
				var target = e.target;
				var value = target.value;
				var container = target.parentNode;
				var containerId = container.id;
				var tokens = containerId.split('-');
				FormComponent.assignComponentToVariable(value, tokens[1], tokens[2], {
					callback: function(resultDOM) {
						replaceNode(resultDOM, container, container.parentNode);
					}
				});
			});
			chooser.getLast().addEvent('blur', function(e){
				new Event(e).stop();
				var target = e.target;
				var value = target.value;
				var container = target.parentNode;
				var containerId = container.id;
				var tokens = containerId.split('-');
				FormComponent.assignComponentToVariable(null, tokens[1], tokens[2], {
					callback: function(resultDOM) {
						replaceNode(resultDOM, container, container.parentNode);
					}
				});
			});
		}
	});
}
function showNewVariableDialog(parameter) {
	if(parameter != null) {
		var index = parameter.indexOf('_');
		if(index != -1) {
			var datatype = parameter.substring(index + 1);
			if(datatype != null) {
				ProcessPalette.getAddVariableBox(false, datatype, {
					callback: function(resultDOM) {
						replaceNode(resultDOM, $(datatype + '_box'), $(datatype + '_vContainer'));
						$(datatype + '_box').getFirst().focus();
						$('add_' + datatype).addEvent('keypress', function(e){
							if(isEnterEvent(e)) {
								new Event(e).stop();
								var target = e.target;
								var value = target.value;
								var id = target.id
								var index = id.indexOf('_');
								if(index != -1) {
									var datatype = id.substring(index + 1);
									if(datatype != null) {
										FormDocument.addNewVariable(value, datatype, {
											callback: function(resultDOM) {
												replaceNode(resultDOM, $(datatype + '_vContainer'), $(datatype + '_datatypeGroup'));
											}
										});
									}
								}
							}
						});
						$('add_' + datatype).addEvent('blur', function(e){
							new Event(e).stop();
							var target = e.target;
							var id = target.id
							var index = id.indexOf('_');
							if(index != -1) {
								var datatype = id.substring(index + 1);
								if(datatype != null) {
									ProcessPalette.getAddVariableBox(true, datatype, {
										callback: function(resultDOM) {
											replaceNode(resultDOM, $(datatype + '_box'), $(datatype + '_vContainer'));
										}
									});
								}
							}
						});
					}
				});
			}
		}
	}
}
function hideNewVariableDialog(parameter) {
	if(parameter != null) {
		var index = parameter.indexOf('_');
		if(index != -1) {
			var datatype = parameter.substring(index + 1);
			if(datatype != null) {
				ProcessPalette.getAddVariableBox(true, datatype, {
					callback: function(resultDOM) {
						replaceNode(resultDOM, $(datatype + '_box'), $(datatype + '_vContainer'));
					}
				});
			}
		}
	}
}
function addNewVariable(event) {
	if(isEnterEvent(event)) {
		new Event(event).stop();
		var target = event.target;
		var value = target.value;
		var id = target.id
		var index = id.indexOf('_');
		if(index != -1) {
			var datatype = id.substring(index + 1);
			if(datatype != null) {
				FormDocument.addNewVariable(value, datatype, {
					callback: function(resultDOM) {
						replaceNode(resultDOM, $(datatype + '_vContainer'), $(datatype + '_datatypeGroup'));
					}
				});
			}
		}
	}
}
function nothing(parameter) {}
function createNewPage() {
	FormPage.createNewPage({
		callback: function(resultDOMs) {
			insertNodesToContainer(resultDOMs[1], $(PAGES_PANEL_ID));
			var dropBox = $('dropBox');
			if(dropBox != null) {
				var parentNode = dropBox.parentNode;
				var node = parentNode.getLast();
				node.remove();
				insertNodesToContainer(resultDOMs[0], parentNode);
				setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
				setupPagesPanel();
			}
		}
	});
}
function deletePage(event) {
	new Event(event).stopPropagation();
	var root = $(PAGES_PANEL_ID);
	if(root != null) {
		var nodes = root.getChildren();
		if(nodes.length == 1) {
			return;
		}
	}
	var node = event.target;
	var parentNode = node.parentNode;
	if(parentNode != null) {
		FormPage.removePage(parentNode.id, {
			callback: function(result) {
				if(result != null) {
					showLoadingMessage('Loading section...');
					var currentPage = result[0];
					var node = $(currentPage);
					if(node != null) 
						node.remove();
					markSelectedPage(result[1])
					var designViewDOM = result[2];
					var dropBox = $('dropBox');
					if(dropBox != null) {
						var parentNode = dropBox.parentNode;
						var node = parentNode.getLast();
						node.remove();
						insertNodesToContainer(designViewDOM, parentNode);
						setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
					}
					closeLoadingMessage();
				}
			}
		});
	}
	setupPagesPanel();
}
function removeComponent(parameter) {
	var node = parameter.parentNode;
	if(node != null) {
		pressedComponentDelete = true;
		FormComponent.removeComponent(node.id, {
			callback: function(result) {
				var node = $(result);
				if(node != null) {
					node.remove();
					var nodes = getElementsByClassName($('dropBoxinner'), 'div', 'formElement');
					if(nodes.length == 0) {
						Workspace.getDesignView({
							callback: function(resultDOM) {
								if(resultDOM != null) {
									var dropBox = $('dropBox');
									if(dropBox != null) {
										var parentNode = dropBox.parentNode;
										var node2 = parentNode.getLast();
										node2.remove();
										insertNodesToContainer(resultDOM, parentNode);
										setupDesignView('dropBox', 'formElement', PAGE_TITLE, FORM_TITLE);
										setupPagesPanel();
									}
								}
							}
						});
					}
				}
			}
		});
	}
}