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

var CURRENT_BUTTON = null;
var VARIABLE_LIST;
var COMPONENT_DATATYPE;

var currentElement = null;
var pressedComponentDelete = false;
var pressedButtonDelete = false;
var pressedPageDelete = false;
var draggingButton = false;
var draggingComponent = false;
var draggingPage = false;
var insideDropzone = false;

var FORMBUILDER_PATH = "/workspace/forms/formbuilder/";
var FORMADMIN_PATH = "/workspace/forms/formadmin/";
var FORMSHOME_PATH = "/workspace/forms/";
var TRANSITION_DURATION = 500;

var modalFormName = null;
var modalGoToDesigner = false;
var modalSelectedForm = null;
var SELECTED_PROCESS = null;
var SELECTED_TASK = null;

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
				if(type == 'fbc') {
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
						var componentType = this.elementOrg.id;
   						FormComponent.addComponent(componentType, {
							callback: function(resultDOM) {
								if(resultDOM != null) {
									currentElement = resultDOM;
								}
							}
						});
   						draggingComponent = true;
					}
				} else if(type == 'fbb') {
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
				} else if(type == 'fbbp') {
					if(draggingButton == false) {
						var componentType = this.elementOrg.id;
						dwr.engine.beginBatch();
						FormComponent.addButton(componentType, {
							callback: function(resultDOM) {
								if(resultDOM != null) {
									CURRENT_BUTTON = resultDOM;
								}
							}
						});
						ProcessData.getAvailableTransitions(componentType, {
							callback: function(list) {
								VARIABLE_LIST = list;
							}
						});
						dwr.engine.endBatch();
						draggingButton = true;
					}
				} else if(type == 'fbcp') {
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
						var componentType = this.elementOrg.id;
						dwr.engine.beginBatch();
   						FormComponent.addComponent(componentType, {
							callback: function(resultDOM) {
								if(resultDOM != null) {
									currentElement = resultDOM;
									var temp = currentElement;
									var temp2 = 2 + 2;
								}
							}
						});
						ProcessPalette.getComponentDatatype(componentType, {
							callback: function(datatype) {
								if(datatype != null) {
									COMPONENT_DATATYPE = datatype;
								}
							}
						});
						ProcessData.getComponentTypeVariables(componentType, {
							callback: function(list) {
								VARIABLE_LIST = list;
							}
						});
						dwr.engine.endBatch();
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
				if(type == 'fbc') {
					if(draggingComponent == true) {
						if(insideDropzone == false) {
							var currentId = currentElement.documentElement.getAttribute('id');
							this.element.removeEvents('mousemove');
							FormComponent.removeComponent(currentId);
							draggingComponent = false;
						}
					}
				} else if(type == 'fbb') {
					if(draggingButton == true) {
						if(insideDropzone == false) {
							FormComponent.removeButton(CURRENT_BUTTON.documentElement.getAttribute('id'));
							draggingButton = false;
						}
					}
					
				} else if(type == 'fbcp') {
					if(draggingComponent == true) {
						if(insideDropzone == false) {
							var currentId = currentElement.documentElement.getAttribute('id');
							this.element.removeEvents('mousemove');
							FormComponent.removeComponent(currentId);
						}
					}
				} else if(type == 'fbbp') {
					if(draggingButton == true) {
						if(insideDropzone == false) {
							FormComponent.removeButton(CURRENT_BUTTON.documentElement.getAttribute('id'));
							draggingButton = false;
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
function createNewForm() {
	FormDocument.createFormDocument(modalFormName, {
		callback: function(result) {
			if(result == true) {
				if(modalGoToDesigner == true) {
					window.location=FORMBUILDER_PATH;
				} else {
					window.location=FORMSHOME_PATH;
				}
			}
		}
	});
}
function registerFormbuilderActions() {
	var saveFormButton = $('saveFormButton');
	if(saveFormButton != null) {
		saveFormButton.addEvent('click', function(e) {
			new Event(e).stop();
			fbsave();
		});
	}
	var newFormButton = $('newFormButton');
	if(newFormButton != null) {
		newFormButton.setProperty('Title', 'Create new form');
		newFormButton.setProperty('href', '#TB_inline?height=100&width=300&inlineId=newFormDialog');
	}
	var formListButton = $('homeButton');
	if(formListButton != null) {
		formListButton.setProperty('Title', 'Save form?');
		formListButton.setProperty('href', '#TB_inline?height=100&width=300&inlineId=formListDialog');
	}
	var languageChooser = $('languageChooser');
	if(languageChooser != null) {
		languageChooser.addEvent('change', function(e) {
			var locale = e.target.value;
			if(locale != '') {
				reloadWorkspace(locale);
			}
		});
	}
	var noVariableBtn = $('noVariableBtn');
	setHrefToVoidFunction(noVariableBtn);
	noVariableBtn.addEvent('click', function(e) {
		closeVariableListDialog($('selectVariableDialog'));
	});
	var cancelVariableBtn = $('cancelVariableBtn');
	setHrefToVoidFunction(cancelVariableBtn);
	cancelVariableBtn.addEvent('click', function(e) {
		closeVariableListDialog($('selectVariableDialog'));
	});
}
function initializePalette() {
	var tabs = new mootabs('firstList', {width: '100%', height: '358px', changeTransition: 'none'});
}
function initializeVariableViewer() {
	$$('.addVariableIcon').each(function(el){
		el.addEvent('click', function(e) {
			var id = el.getProperty('id');
			var datatype = id.substring(0, id.indexOf('_'));
			var input = new Element('input');
			input.setProperty('type', 'text');
			input.addEvent('keypress', function(event) {
				if(isEnterEvent(event)) {
					var value = event.target.value;
					createVariable(datatype, value, input, el);
				}
			});
			input.addEvent('blur', function(event) {	
				input.replaceWith(el);
			});
			el.replaceWith(input);
			input.focus();
		});
	});
	$$('a.assignLabel').each(function(item) {
		setHrefToVoidFunction(item);
		item.addEvent('click', function(e) {
			handleAssignLabelWidget(e);
		});
	});
}
function handleAssignLabelWidget(e) {
	var target = e.target;
	var labelContainer = target.getParent();
	var compType = labelContainer.getProperty('rel');
			
	FormComponent.getAvailableComponentVariables(compType, {
		callback: function(resultItems) {
			var icon = labelContainer.getFirst();
			icon.remove();
			var label = labelContainer.getFirst();
			var labelText = label.getText().substring(label.getText().indexOf(':') + 2);
			label.remove();
			var select = new Element('select');
			var option = new Element('option',{
				'value': ''
			}).setText('Not assigned');
			option.injectInside(select);
			for(var i = 0; i < resultItems.length; i++) {
				var it = resultItems[i];
				var option = new Element('option',{
					'value': it.id
				}).setText(it.value);
				if(it.value == labelText) {
					option.setProperty('selected', 'selected');
				}
				option.injectInside(select);
			}
			select.addEvent('change', function(e) {
				var selValue = select.options[select.selectedIndex].getText();
				FormComponent.assignVariable(null, selValue, 'string', {
					callback: function(result) {
						select.setStyle('display', 'none');
						icon.injectInside(labelContainer);
						label.injectInside(labelContainer);
						label.setText('Assigned to: ' + selValue);
						label.removeEvents('click');
						setHrefToVoidFunction(label);
					}
				});
			});
			select.addEvent('blur', function(e) {
				this.setStyle('display', 'none');
				icon.injectInside(labelContainer);
				label.injectInside(labelContainer);
				label.removeEvents('click');
				setHrefToVoidFunction(label);
				/*label.addEvent('click', function(event) {
					handleAssignLabelWidget(event);
				});*/
				initializeVariableViewer();
			});
			select.injectInside(labelContainer);
			select.focus();
		}
	});
}
function createVariable(datatype, value, element, image) {
	ProcessData.createVariable(value, datatype, {
		callback: function(result) {
			var span = new Element('span');
			span.setProperty('id', value + '_var');
			span.addClass('varEntry');
			span.addClass('unused');
			span.setText(value);
			span.injectBefore(element);
			element.replaceWith(image);
		}
	});
}
function reloadWorkspace(locale) {
	showLoadingMessage('Changing locale...');
	Workspace.getWorkspace(locale, {
		callback: function(resultDOM) {
			if(resultDOM != null) {
				replaceNode(resultDOM, $('mainWorkspace'), $('mainApplication'));
			}
			closeLoadingMessage();
		}
	});
}
function setHrefToVoidFunction(element) {
	element.setProperty('href', 'javascript:void(0)');
}
function showVariableList(containerId, positionLeft, positionTop, list, transition) {
	var container = $(containerId);
	if (container == null) {
		return false;
	}
	
	container.setStyle('visibility', 'visible');
	container.setStyle('left', positionLeft);
	container.setStyle('top', positionTop);
	container.setStyle('position', 'fixed');
	
	var vlist = container.getFirst().getNext();
	vlist.empty();
	for(var i = 0; i < list.length; i++) {
		var variable = list[i];
		var li = new Element('li');
		var link = new Element('a');
		link.setText(variable);
		link.injectInside(li);
		link.addEvent('click', function(e) {
			new Event(e).stop();
			var target = e.target;
			var variableName = target.getText();
			if(transition == true) {
				if(CURRENT_BUTTON != null) {
					if(CURRENT_BUTTON.documentElement) {
						var buttonId = CURRENT_BUTTON.documentElement.getAttribute('id');
						if(buttonId != null) {
							FormComponent.assignTransition(buttonId, variableName, {
								callback: function(result) {
									if(result != null) {
										toggleVariableStatus(variableName + '_trans', result);
									}
								}
							});
							insertNodesToContainer(CURRENT_BUTTON, $('pageButtonArea'));
						}
					}
				}
			} else {
				if(currentElement != null) {
					if(currentElement.documentElement) {
						var componentId = currentElement.documentElement.getAttribute('id');
						if(componentId != null && CURRENT_ELEMENT_UNDER != null) {
							dwr.engine.beginBatch();
							FormComponent.assignVariable(componentId, variableName, COMPONENT_DATATYPE, {
								callback: function(result) {
									if(result != null) {
										 var componentDOM = currentElement.documentElement;
										 var varName = componentDOM.childNodes[2];
										 if(varName != null) {
										 	varName.textContent = variableName;
										 }
										 toggleVariableStatus(variableName + '_var', result);
									}
								}
							});
							FormComponent.moveComponent(componentId, CURRENT_ELEMENT_UNDER, {
								callback: function(result) {
									if($('emptyForm') != null) {
										$('emptyForm').remove();
									}
									if(result == 'append') {
										insertNodesToContainer(currentElement, $('dropBoxinner'));
										currentElement = null;
									} else {
										var node = $(result);
										insertNodesToContainerBefore(currentElement, $('dropBoxinner'), node);
										currentElement = null;
									}
									initializeDesignView();
								}
							});
							dwr.engine.endBatch();
						}
					}
				}
			}
			closeVariableListDialog(container);
		});
		li.injectInside(vlist);
	}
}
function toggleVariableStatus(componentId, status) {
	if(componentId == null) 
		return;
		
	var node = $(componentId);
	if(node != null) {
		switch(status) {
			case 'unused':
				node.removeClass('single');
				node.removeClass('multiple');
				node.addClass(status);
				break;
			case 'single':
				node.removeClass('unused');
				node.removeClass('multiple');
				node.addClass(status);
				break;
			case 'multiple':
				node.removeClass('single');
				node.removeClass('unused');
				node.addClass(status);
				break;
		}
	}
}
function closeVariableListDialog(container) {
	if (container != null) {
		container.setStyle('visibility', 'hidden');
	}
}
function setDisplayPropertyToElement(id, property, frameChange) {
	if (id == null || property == null) {
		return false;
	}
	var element = $(id);
	if (element == null) {
		return false;;
	}
	
	element.setStyle('display', property);
	window.clearTimeout(SET_DISPLAY_PROPERTY_ID);
	
	if (frameChange != null) {
		changeFrameHeight(frameChange);
	}
}
function initializeDesignView() {
	FormComponent.getId(markSelectedComponent);
	var myComponentSort = new Sortables($('dropBoxinner'), {
		onComplete: function(el){
			var children = $('dropBoxinner').getChildren();
			var orderList = [];
			for(var i = 0; i < children.length; i++) {
				var element = children[i];
				orderList.push(element.id);
			}
			FormPage.updateComponentList(orderList);
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
					if(el.hasClass('fbc')) {
						if(currentElement != null) {
							if(currentElement.documentElement) {
								var currentId = currentElement.documentElement.getAttribute('id');
							    if(CURRENT_ELEMENT_UNDER != null) {
									FormComponent.moveComponent(currentId, CURRENT_ELEMENT_UNDER, {
										callback: function(result) {
											if($('emptyForm') != null) {
												$('emptyForm').remove();
											}
											if(result == 'append') {
												insertNodesToContainer(currentElement, $('dropBoxinner'));
												currentElement = null;
											} else {
												var node = $(result);
												insertNodesToContainerBefore(currentElement, $('dropBoxinner'), node);
												currentElement = null;
											}
											initializeDesignView();
										}
									});
							    }
							}
						}
					} else if(el.hasClass('fbcp')) {
						showVariableList('selectVariableDialog', el.getLeft(), el.getTop(), VARIABLE_LIST, false);
					}
				} else if(draggingButton == true) {
					draggingButton = false;
					if(el.hasClass('fbb')) {
						if(CURRENT_BUTTON != null) {
							insertNodesToContainer(CURRENT_BUTTON, $('pageButtonArea'));
						}
					} else if(el.hasClass('fbbp')) {
						showVariableList('selectVariableDialog', el.getLeft(), el.getTop(), VARIABLE_LIST, true);
					}
					
				}
				insideDropzone = false;
			}
		});
	}
	$$('.fbc').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbc');
	});
	$$('.fbcp').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbcp');
	});
	$$('.fbb').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbb');
	});
	$$('.fbbp').each(function(el){
		el.draggableTag($('dropBoxinner'), null, 'fbbp');
	});
	initializeInlineEdits();
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
			FormDocument.setEnableBubbles(parameter.checked);
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
	}
}
function saveThankYouText(parameter) {
	if(parameter != null) {
		FormDocument.setThankYouText(parameter);
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
		$(PREVIOUS_PAGE_ID).removeClass('selectedElement');
	}
	var pageNode = $(parameter);
	if(pageNode == null) {
		pageNode = $(parameter + '_P_page');
		CURRENT_PAGE_ID = parameter + '_P_page';
	} else {
		CURRENT_PAGE_ID = parameter;
	}
	if(pageNode != null) {
		pageNode.addClass('selectedElement');
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
function initializePagesPanel() {
	var myPagesSort = new Sortables($(PAGES_PANEL_ID), {
		onComplete: function(el){
			var children = $(PAGES_PANEL_ID).getChildren();
			var orderList = [];
			for(var i = 0; i < children.length; i++) {
				var element = children[i];
				orderList.push(element.id);
			}
			FormDocument.updatePagesList(orderList);
		},
		handles: '.fbPageHandler'
	});
	FormPage.getId(markSelectedPage);
	$ES("div.formPageIcon").each(function(item) {
		item.addEvent('click', function(e){
			var targetId = getPageID(e);
			if(draggingPage == false) {
				if(targetId.indexOf('_P_page') != -1) {
					var actualId = targetId.substring(0, targetId.indexOf('_P_page'));
					showLoadingMessage('Loading section...');
					FormPage.getFormPageInfo(actualId, {
						callback: function(resultDOM) {
							reloadDesignView(resultDOM, targetId);
						}
					});
				}
			}
		});
	});
	var thankyoupage = $E('div.thankyou');
	if(thankyoupage != null) {
		thankyoupage.addEvent('click', function(e){
			showLoadingMessage('Loading section...');
			var targetId = getPageID(e);
			FormPage.getThxPageInfo({
				callback: function(resultDOM) {
					reloadDesignView(resultDOM, targetId);
				}
			});
		});
	}
	var previewp = $E('div.preview');
	if(previewp != null) {
		previewp.addEvent('click', function(e){
			showLoadingMessage('Loading section...');
			var targetId = getPageID(e);
			FormPage.getConfirmationPageInfo({
				callback: function(resultDOM) {
					reloadDesignView(resultDOM, targetId);
				}
			});
		});
	}
}
function getPageID(e) {
	var target = e.target;
	if(target.hasClass('formPageIcon')) {
		return target.getProperty('id');
	} else {
		return target.getParent().getProperty('id');
	}
}
function reloadDesignView(resultDOM, targetId) {
	if(resultDOM != null) {
		markSelectedPage(targetId);
		var dropBox = $('dropBox');
		if(dropBox != null) {
			var parentNode = dropBox.getParent();
			var node = parentNode.getLast();
			if(node != null) {
				node.remove();
			}
			insertNodesToContainer(resultDOM, parentNode);
			initializeDesignView();
		}
		closeLoadingMessage();
	}
}
function markSelectedComponent(parameter) {
	if(parameter != null) {
		var element = $(parameter);
		if(element != null) {
			CURRENT_ELEMENT_ID = parameter;
		}
	}
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
        FormComponent.saveComponentAction(value);
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
	FormComponent.saveComponentProcessVariableName(value);
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


function initializeInlineEdits() {
	$$('div.inlineEdit').each(
		function(element) {
			prepareInlineEdit(element);
			$$('div.inlineEdit span').addEvent('click',function(){
		    	this.inlineEdit({
		    		onComplete:function(el,oldContent,newContent){
		    			var relString = el.getParent().getProperty('rel');
		    			var actions = new Array();
						actions = relString.split(' ');
						var action = actions[0];
						if(actions.length == 2) {
							action += "('" + newContent + "', " + actions[1] + ");";
						} else if(actions.length == 1) {
							action += "('" + newContent + "');";
						}
						var customFunction = function() {
							window.eval(action);
						}
						customFunction();
					}
		    	});
		    });
    	}
    );
}
function updatePageIconText(result) {
	var pageIcon = $('pagesPanel').getElement('div.selectedElement');
	if(pageIcon != null) {
		var last = pageIcon.getLast();
		if(last != null) {
			var span = last.getPrevious();
			if(span != null) {
				span.setText(result);
			}
		}
	}
}
function prepareInlineEdit(element) {
	var editIcon = new Element('img');
	element.setStyle('cursor','pointer');
	editIcon.addClass('inlineEditIcon');
	editIcon.setProperty('src', '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit_16.png');
	editIcon.injectInside(element);
}
/*--------------------*/
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
				initializeDesignView();
				initializePagesPanel();
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
	var parentNode = node.getParent();
	if(parentNode != null) {
		var targetId = parentNode.getProperty('id');
		if(targetId.indexOf('_P_page') != -1) {
			var actualId = targetId.substring(0, targetId.indexOf('_P_page'));
			FormPage.removePage(actualId, {
				callback: function(result) {
					if(result != null) {
						showLoadingMessage('Loading section...');
						parentNode.remove();
						markSelectedPage(result[1])
						var designViewDOM = result[2];
						var dropBox = $('dropBox');
						if(dropBox != null) {
							var parentNode = dropBox.parentNode;
							var node = parentNode.getLast();
							node.remove();
							insertNodesToContainer(designViewDOM, parentNode);
							initializeDesignView();
						}
						closeLoadingMessage();
					}
				}
			});
		}
	}
	initializePagesPanel();
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
										initializeDesignView();
										initializePagesPanel();
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