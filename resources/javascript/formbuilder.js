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

var RESERVED_HEIGHT = 82;
var RESERVED_HEIGHT_FOR_FB = RESERVED_HEIGHT + 94;

var modalFormName = null;
var modalGoToDesigner = false;
var modalSelectedForm = null;
var SELECTED_PROCESS = null;
var SELECTED_TASK = null;

var fbLeftAccordion = null;
var fbRightAccordion = null;

var currentCallback = null;
var selectedPaletteTab = null;
var fbPageSort = null;

var FBDraggable = Element.extend({
	draggableTag: function(droppables, handle, type, makeDrag) {
		type = type;
		handle = handle || this;
		if(makeDrag) {
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
						var dropBox = $('dropBoxinner');
						dropBox.setStyle('background-color', '#F9FF9E');
						CURRENT_ELEMENT_UNDER = -1;
			   			childBoxes = [];
						var childNodes = $$('#dropBoxinner div.formElement');
						for(var i = 0; i < childNodes.length; i++){
							var child = childNodes[i];
							var pos = child.getCoordinates();
							childBoxes.push({top: pos.top, bottom: pos.bottom, left: pos.left, right: pos.right, height: pos.height, width: pos.width, node: child});
						}
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
									} else {
										var messagePanel = $('messageDialog');
										if(messagePanel != null) {
											messagePanel.removeProperty('style');
										}
									}
								}
							});
	   						draggingComponent = true;
						}
					} else if(type == 'fbb') {
						$('pageButtonArea').setStyle('background-color', '#F9FF9E');
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
						$('pageButtonArea').setStyle('background-color', '#F9FF9E');
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
						var dropBox = $('dropBoxinner');
						dropBox.setStyle('background-color', '#F9FF9E');
						CURRENT_ELEMENT_UNDER = -1;
			   			childBoxes = [];
						var childNodes = $$('#dropBoxinner div.formElement');
						for(var i = 0; i < childNodes.length; i++){
							var child = childNodes[i];
							var pos = child.getCoordinates();
							childBoxes.push({top: pos.top, bottom: pos.bottom, left: pos.left, right: pos.right, height: pos.height, width: pos.width, node: child});
						}
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
	   						FormComponent.addTaskComponent(componentType, {
								callback: function(results) {
									if(results != null && results.length == 3) {
										currentElement = results[0];
										COMPONENT_DATATYPE = results[1];
										VARIABLE_LIST = results[2];
									}
								}
							});
	   						draggingComponent = true;
						}
					}
				},
				onComplete: function(event) {
					this.element.removeEvents('mousemove');
					this.element.remove();
					this.element = this.elementOrg;
					this.elementOrg = null;
					if(type == 'fbc') {
						$('dropBoxinner').removeProperty('style');
						if(insideDropzone == false && currentElement != null && draggingComponent == true) {
							var currentId = currentElement.documentElement.getAttribute('id');
							FormComponent.removeComponent(currentId);
							draggingComponent = false;
						}
					} else if(type == 'fbb') {
						$('pageButtonArea').removeProperty('style');
						if(draggingButton == true && insideDropzone == false && CURRENT_BUTTON != null) {
							FormComponent.removeButton(CURRENT_BUTTON.documentElement.getAttribute('id'));
							draggingButton = false;
						}
					} else if(type == 'fbcp') {
						$('dropBoxinner').removeProperty('style');
						if(insideDropzone == false && currentElement != null && draggingComponent == true) {
							var currentId = currentElement.documentElement.getAttribute('id');
							this.element.removeEvents('mousemove');
							FormComponent.removeComponent(currentId);
							draggingComponent = false;
						}
					} else if(type == 'fbbp') {
						$('pageButtonArea').removeProperty('style');
						if(draggingButton == true && insideDropzone == false && CURRENT_BUTTON != null) {
							FormComponent.removeButton(CURRENT_BUTTON.documentElement.getAttribute('id'));
							draggingButton = false;
						}
					}
				},
			});
		} else {
			this.removeEvent('mousedown');
			handle.removeEvent('mousedown');
		}
		
		this.setStyles({
			'position': ''
		});
		return this;
	}
});
function controlFormbuilderAppWindow() {
	resizeAccordion(RESERVED_HEIGHT_FOR_FB, 'firstList', false);
	resizeAccordion(RESERVED_HEIGHT_FOR_FB, 'pagesPanelMain', true);
}
function resizeAccordion(reservedHeight, containerId, variableTabs) {
	var siteTreeContainer = $(containerId);
	if (siteTreeContainer) {
		var totalHeight = getTotalHeight();
		var height = totalHeight - reservedHeight;
		if (height > 0) {
			siteTreeContainer.setStyle('height', height + 'px');
		}
		
		var diff = 0;
		if(variableTabs) {
			var rightTabsCount = $('accordionRight').getElements('span.toggler').length;
			if(rightTabsCount == 1) {
				diff = 18;
			}
		}

		var heightForAccordion = totalHeight - (186 - diff);
		if (heightForAccordion > 0) {
			var el = $$('.selectedAccElement');
			el.each(function (element) {element.setStyle('height', heightForAccordion + 'px');});
		}
	}
}
function initializeDesignView(initializeInline) {
	PropertyManager.getSelectedComponentId({
		callback: function(result) {
			if(result != null && result != '') {
				var old = $('dropBoxinner').getElement('div.selectedComponent');
				if(old != null) {
					old.removeClass('selectedComponent');
				}
				var newNode = $(result);
				if(newNode != null) {
					newNode.addClass('selectedComponent');
				}
			}							
		}
	});
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
	var pageButtonArea = $('pageButtonArea');
	if(pageButtonArea != null) {
		pageButtonArea.addEvents({
			'over': function(el){
				if (!this.dragEffect) {
					this.dragEffect = new Fx.Style(this, 'background-color');
				}
				this.dragEffect.stop().start('#F9FF9E', '#FFFF00');
				insideDropzone = true;
			},
			'leave': function(el){
				this.dragEffect.stop().start('#FFFF00', '#F9FF9E');
				insideDropzone = false;
			},
			'drop': function(el, drag){
				if(draggingButton == true) {
					draggingButton = false;
					if(el.hasClass('fbb')) {
						if(CURRENT_BUTTON != null) {
							insertNodesToContainer(CURRENT_BUTTON, pageButtonArea);
						}
					} else if(el.hasClass('fbbp')) {
						showVariableList('selectVariableDialog', el.getLeft(), el.getTop(), VARIABLE_LIST, true);
					}
				}
				insideDropzone = false;
			}
		});
	}
	var dropBoxinner = $('dropBoxinner');
	if(dropBoxinner != null) {
		dropBoxinner.addEvents({
			'over': function(el){
				if (!this.dragEffect) {
					this.dragEffect = new Fx.Style(this, 'background-color');
				}
				this.dragEffect.stop().start('#F9FF9E', '#FFFF00');
				insideDropzone = true;
				dropBoxinner.getElements('div.formElement').each(function(element) {
					element.removeClass('formElementHover');
				});
			},
			'leave': function(el){
				this.dragEffect.stop().start('#FFFF00', '#F9FF9E');
				insideDropzone = false;
				dropBoxinner.getElements('div.formElement').each(function(element) {
					element.addClass('formElementHover');
				});
			},
			'drop': function(el, drag){
				dropBoxinner.getElements('div.formElement').each(function(element) {
					element.addClass('formElementHover');
				});
				if(draggingComponent == true) {
					draggingComponent = false;
					if(el.hasClass('fbc')) {
						if(currentElement != null) {
							if(currentElement.documentElement) {
								var currentId = currentElement.documentElement.getAttribute('id');
							    if(CURRENT_ELEMENT_UNDER != null) {
									FormComponent.moveComponent(currentId, CURRENT_ELEMENT_UNDER, {
										callback: function(result) {
											if($('noFormNotice') != null) {
												$('noFormNotice').remove();
											}
											if(result == 'append') {
												insertNodesToContainer(currentElement, dropBoxinner);
												currentElement = null;
											} else {
												var node = $(result);
												insertNodesToContainerBefore(currentElement, dropBoxinner, node);
												currentElement = null;
											}
											initializeDesignView(false);
										}
									});
							    }
							}
						}
					} else if(el.hasClass('fbcp')) {
						showVariableList('selectVariableDialog', el.getLeft(), el.getTop(), VARIABLE_LIST, false);
					}
				}
				insideDropzone = false;
			}
		});
		dropBoxinner.getElements('div.formElement').each(function(item) {
			item.addEvent('click', function(e) {
				var componentId = item.getProperty('id');
				PropertyManager.selectComponent(componentId, 'component', {
					callback: function(resultDOM) {
						currentCallback = componentRerenderCallback;
						placeComponentInfo(resultDOM, 1, componentId);
						var old = dropBoxinner.getElement('div.selectedComponent');
						if(old != null) {
							old.removeClass('selectedComponent');
						}
						item.addClass('selectedComponent');
					}
				});
			});
			item.getElement('img.speedButton').removeEvents('click');
			item.getElement('img.speedButton').addEvent('click', function(e) {
				new Event(e).stopPropagation();
				var node = e.target.getParent();
				if(node != null) {
					FormComponent.removeComponent(node.getProperty('id'), {
						callback: function(result) {
							var node = $(result);
							if(node != null) {
								node.remove();
								var nodes = $('dropBoxinner').getElements('div.formElement');
								if(nodes.length == 0) {
									Workspace.getDesignView({
										callback: function(resultDOM) {
											if(resultDOM != null) {
												var dropBox = $('dropBox');
												if(dropBox != null) {
													var parentNode = dropBox.getParent();
													var node2 = parentNode.getLast();
													if(node2 != null) {
														node2.remove();
													}
													insertNodesToContainer(resultDOM, parentNode);
													initializeDesignView(true);
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
			});
		});
	}
	if(selectedPaletteTab == null) {
		selectedPaletteTab = 'processes';
	}
	initializePaletteComponents(selectedPaletteTab, dropBoxinner, pageButtonArea, true);
	if(initializeInline == true) {
		initializeInlineEdits();
	}
}
function initializePaletteComponents(tab, dropBoxinner, pageButtonArea, enable) {
	$(tab).getElements('.fbc').each(function(el){
		el.draggableTag(dropBoxinner, null, 'fbc', enable);
	});
	$(tab).getElements('.fbcp').each(function(el){
		el.draggableTag(dropBoxinner, null, 'fbcp', enable);
	});
	$(tab).getElements('.fbb').each(function(el){
		el.draggableTag(pageButtonArea, null, 'fbb', enable);
	});
	$(tab).getElements('.fbbp').each(function(el){
		el.draggableTag(pageButtonArea, null, 'fbbp', enable);
	});
}
function createNewForm() {
	if(modalFormName == null || modalFormName == '') {
		return;
	}
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
		var currentId = currentElement.documentElement.getAttribute('id');
		if(CURRENT_ELEMENT_UNDER != null) {
			FormComponent.moveComponent(currentId, CURRENT_ELEMENT_UNDER, {
				callback: function(result) {
					if($('emptyForm') != null) {
						$('emptyForm').remove();
					}
					dropBoxinner = $('dropBoxinner');
					if(result == 'append') {
						insertNodesToContainer(currentElement, dropBoxinner);
					} else {
						var node = $(result);
						insertNodesToContainerBefore(currentElement, dropBoxinner, node);
					}
					currentElement = null;
					initializeDesignView(false);
				}
			});
		}
		closeVariableListDialog($('selectVariableDialog'));
	});
	var cancelVariableBtn = $('cancelVariableBtn');
	setHrefToVoidFunction(cancelVariableBtn);
	cancelVariableBtn.addEvent('click', function(e) {
		var currentId = currentElement.documentElement.getAttribute('id');
		FormComponent.removeComponent(currentId);
		closeVariableListDialog($('selectVariableDialog'));
	});
}
function initializePalette() {
	var tabs = new mootabs('firstList', {width: '100%', height: '358px', changeTransition: 'none'});
	$ES("li.stateFullTab").each(function(item) {
		item.addEvent('click', function(e){
			selectedPaletteTab = e.target.getProperty('title');
			initializePaletteComponents(selectedPaletteTab, $('dropBoxinner'), $('pageButtonArea'), true);
		});
	});
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
			var icon = labelContainer.getElement('img');
			icon.setStyle('display', 'none');
			var label = labelContainer.getElement('a');
			var labelText = label.getText().substring(label.getText().indexOf(':') + 2);
			label.setStyle('display', 'none');
			var select = labelContainer.getElement('select');
			if(select == null) {
				select = new Element('select');
				select.injectInside(labelContainer);
			}
			removeChildren(select);
			select.setStyle('display', 'inline');
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
				var selValue = select.options[select.selectedIndex].getProperty('value');
				var selText = select.options[select.selectedIndex].getText();
				var compId = select.getParent().getParent().getProperty('id');
				FormComponent.assignVariable(compId, selValue, {
					callback: function(resultDOM) {
						select.setStyle('display', 'none');
						label.setStyle('display', 'inline');
						icon.setStyle('display', 'inline');
						label.setText('Assigned to: ' + selText);
						setHrefToVoidFunction(label);
						var parentNode = $('panel0Content2');
						if(parentNode != null && resultDOM != null) {
							removeChildren(parentNode);
							insertNodesToContainer(resultDOM, parentNode);
						}
					}
				});
			});
			select.addEvent('blur', function(e) {
				select.setStyle('display', 'none');
				label.setStyle('display', 'inline');
				icon.setStyle('display', 'inline');
				setHrefToVoidFunction(label);
				initializeVariableViewer();
			});
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
	if(list.length > 0) {
		for(var i = 0; i < list.length; i++) {
			var variable = list[i];
			var li = new Element('li');
			var link = new Element('a');
			var tokens = variable.split(':');
			link.setText(tokens[1]);
			link.setProperty('rel', variable);
			link.injectInside(li);
			link.addEvent('click', function(e) {
				new Event(e).stop();
				var target = e.target;
				var variableName = target.getProperty('rel');
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
								FormComponent.assignVariableAndMoveComponent(componentId, variableName, CURRENT_ELEMENT_UNDER, {
									callback: function(results) {
										if(results != null && results.length == 3) {
											var parentNode = $('panel0Content2');
											if(parentNode != null && results[0] != null) {
												removeChildren(parentNode);
												insertNodesToContainer(results[0], parentNode);
											}
													
											if($('emptyForm') != null) {
												$('emptyForm').remove();
											}
											if(results[1] == 'append') {
												insertNodesToContainer(results[2], $('dropBoxinner'));
												currentElement = null;
											} else {
												var node = $(results[1]);
												insertNodesToContainerBefore(results[2], $('dropBoxinner'), node);
												currentElement = null;
											}
											initializeDesignView(false);
										}
									}
								});
							}
						}
					}
				}
				closeVariableListDialog(container);
			});
			li.injectInside(vlist);
		}
	} else {
		var message = new Element('span');
		message.setText('No variables for this component');
		message.addClass('vPopupMessage');
		message.injectAfter(vlist);
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
				PropertyManager.selectComponent(button.id, 'button', {
					callback: function(resultDOM) {
						currentCallback = buttonRerenderCallback;
						CURRENT_ELEMENT = button.id;
						var tabIndex = 1;
						var parentNode = $('panel' + tabIndex + 'Content');
						if(parentNode != null && resultDOM != null) {
							removeChildren(parentNode);
							insertNodesToContainer(resultDOM, parentNode);
							fbLeftAccordion.display(0);
							fbLeftAccordion.display(tabIndex);
						}
					}
				});
			}
		}
	}
	pressedButtonDelete = false;
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
		FormDocument.setThankYouText(parameter);
	}
}
function markSelectedPage(parameter) {
	var pagesPanel = $('pagesPanelMain');
	if(pagesPanel != null) {
		var oldPageIcon = pagesPanel.getElement('div.selectedPage');
		if(oldPageIcon != null) {
			oldPageIcon.removeClass('selectedPage');
		}
		var pageNode = $(parameter);
		if(pageNode == null) {
			pageNode = $(parameter + '_P_page');
			CURRENT_PAGE_ID = parameter + '_P_page';
		} else {
			CURRENT_PAGE_ID = parameter;
		}
		if(pageNode != null) {
			pageNode.addClass('selectedPage');
		}
	}
}
function placePageTitle(parameter) {
	if(parameter != null) {
		var node = $(parameter.pageId + '_P_page');
		if(node != null) {
			var parent = node.getFirst().getNext();
			var textNode = parent.getFirst();
			var newTextNode = document.createTextNode(parameter.pageTitle);
			parent.replaceChild(newTextNode, textNode);
		}
	}
}
function enablePagesPanelActions(enable) {
	if(enable == true) {
		initializePagesPanelActions();
		$('pagesPanelMessageBox').setStyle('display', 'none');
	} else {
		if(fbPageSort) {
			fbPageSort.detach();
			fbPageSort = null;
		}
		
		$('pagesPanelMessageBox').setStyle('display', 'block');
		
		$('newPageButton').removeEvents('click');
		setHrefToVoidFunction($('newPageButton'));
		
		$('previewPageButton').removeEvents('click');
		setHrefToVoidFunction($('previewPageButton'));
		
		var pagesPanel = $('pagesPanel');
		if(pagesPanel != null) {
			pagesPanel.getElements("div.formPageIcon").each(function(item) {
				item.removeEvents('click');
				item.getElement('img.pageSpeedButton').removeEvents('click');
			});
		}
		
		var thankyoupage = $E('div.thankyou');
		if(thankyoupage != null) {
			thankyoupage.removeEvents('click');
		}
		
		var previewp = $E('div.preview');
		if(previewp != null) {
			previewp.removeEvents('click');
		}
	}
}
function initializeBottomToolbar() {
	var toolbar = $('bottomButtonsContainer');
	if(toolbar != null) {
		toolbar.getElements('a.viewSwitchBtn').each(function(item) {
			item.addEvent('click', function(e){
				new Event(e).stop();
				showLoadingMessage('Switching...');
				var view = item.getText();
				Workspace.switchView(view, {
					callback: function(resultDOM) {
						var viewPanel = $('viewPanel');
						var mainWorkspace = viewPanel.getParent();
						replaceNode(resultDOM, viewPanel, mainWorkspace);
						if(view != 'Design') {
							enablePagesPanelActions(false);
						} else {
							enablePagesPanelActions(true);
							initializeDesignView(true);
						}
						if(view == 'Source') {
							var area = $('sourceTextarea');
							if(area != null) {
								area.setText(area.innerHTML);
							}
						}
						var selView = toolbar.getElement('a.activeViewButton');
						if(selView != null) {
							selView.removeClass('activeViewButton');
						}
						item.addClass('activeViewButton');
						closeLoadingMessage();
					}
				});
			});
		});
	}
}
function initializePagesPanelActions() {
	fbPageSort = new Sortables($(PAGES_PANEL_ID), {
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
	$('newPageButton').removeEvents('click');
	$('previewPageButton').removeEvents('click');
	$('previewPageButton').addEvent('click', function(e){
		new Event(e).stop();
		var target = e.target;
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
						initialiazePreviewPage();
					} else {
						$('previewPageButton').removeClass('removePreviewPageBtn').addClass('addPreviewPageBtn');
						$('pagesPanelSpecial').getFirst().remove();
					}
				}
			}
		});
	});
	$('newPageButton').addEvent('click', function(e){
		new Event(e).stop();
		FormPage.createNewPage({
			callback: function(resultDOMs) {
				insertNodesToContainer(resultDOMs[1], $(PAGES_PANEL_ID));
				var panel = $('pagesPanel');
				if(panel != null) {
					panel.getLast().addEvent('click', function(e){
						initializeGeneralPage(e);
					});
				}
				var dropBox = $('dropBox');
				if(dropBox != null) {
					var parentNode = dropBox.getParent();
					var node = parentNode.getLast();
					if(node != null) {
						node.remove();
					}
					insertNodesToContainer(resultDOMs[0], parentNode);
					initializeDesignView(true);
					initializePagesPanel();
					var newIcon = $(PAGES_PANEL_ID).getLast();
					markSelectedPage(newIcon);
				}
			}
		});
	});
	FormPage.getId(markSelectedPage);
	var pagesPanel = $('pagesPanel');
	if(pagesPanel != null) {
		pagesPanel.getElements('div.formPageIcon').each(function(item) {
			item.addEvent('click', function(e){
				initializeGeneralPage(e);
			});
			item.getElement('img.pageSpeedButton').removeEvents('click');
			item.getElement('img.pageSpeedButton').addEvent('click', function(e) {
				new Event(e).stopPropagation();
				var root = $(PAGES_PANEL_ID);
				if(root != null) {
					var nodes = root.getChildren();
					if(nodes.length == 1) {
						return;
					}
				}
				var node = e.target;
				var parentNode = node.getParent();
				if(parentNode != null) {
					var targetId = parentNode.getProperty('id');
					if(targetId.indexOf('_P_page') != -1) {
						var actualId = targetId.substring(0, targetId.indexOf('_P_page'));
						FormPage.removePage(actualId, {
							callback: function(result) {
								if(result != null) {
									showLoadingMessage('Loading section...');
									var iconNode = $(result[0] + '_P_page');
									if(iconNode != null) {
										iconNode.remove();
									}
									markSelectedPage(result[1])
									var designViewDOM = result[2];
									var dropBox = $('dropBox');
									if(dropBox != null) {
										var parentNode = dropBox.getParent();
										var node = parentNode.getLast();
										node.remove();
										insertNodesToContainer(designViewDOM, parentNode);
										initializeDesignView(true);
									}
									closeLoadingMessage();
								}
							}
						});
					}
				}
				initializePagesPanel();
			});
		});
	}
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
	initialiazePreviewPage();
}
function initializePagesPanel() {
	Workspace.getView({
		callback: function(result) {
			if(result == 'Design') {
				initializePagesPanelActions();
				$('designButton').addClass('activeViewButton');
			} else {
				$('newPageButton').removeEvents('click');
				setHrefToVoidFunction($('newPageButton'));
				
				$('previewPageButton').removeEvents('click');
				setHrefToVoidFunction($('previewPageButton'));
				
				if(result == 'Source') {
					$('sourceCodeButton').addClass('activeViewButton');
				} else if(result == 'Preview') {
					$('previewButton').addClass('activeViewButton');
				}
			}
		}
	});
}
function getTotalHeight() {
	return window.getHeight();
}

function initializeGeneralPage(element) {
	var targetId = getPageID(element);
	if(draggingPage == false && targetId.indexOf('_P_page') != -1) {
		var actualId = targetId.substring(0, targetId.indexOf('_P_page'));
		showLoadingMessage('Loading section...');
		FormPage.getFormPageInfo(actualId, {
			callback: function(resultDOM) {
				reloadDesignView(resultDOM, targetId);
			}
		});
	}
}
function initialiazePreviewPage() {
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
function getPageID(event) {
	var target = event.target;
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
			initializeDesignView(true);
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
function createAccordion(tabClass, containerClass, heightOffset, componentId) {
	return new Accordion(tabClass, containerClass, {
		opacity: false,
		display: 0,
		height: false,
		transition: Fx.Transitions.quadOut,
		onActive: function(toggler, element){
			toggler.addClass('selectedToggler');

			element.removeClass('hiddenElement');
			element.addClass('selectedAccElement');

			var heightForAccordion = getTotalHeight() - heightOffset;
			if (heightForAccordion > 0) {
				element.setStyle('height', heightForAccordion + 'px');
			}
		},
 
		onBackground: function(toggler, element){
			toggler.removeClass('selectedToggler');

			element.removeClass('selectedAccElement');
			element.addClass('hiddenElement');
			element.setStyle('height', '0px');
		}
	}, $(componentId));
}
function initializeAccordions() {
	fbLeftAccordion = createAccordion('span.atStart', 'div.atStart', 187, 'accordionLeft');
	
	var diff = 0;
	var rightTabsCount = $('accordionRight').getElements('span.toggler').length;
	if(rightTabsCount == 1) {
		diff = 18;
	}
	
	fbRightAccordion = createAccordion('span.atStartRight', 'div.atStartRight', (187 - diff), 'accordionRight');
}
function placeComponentInfo(resultDOM, tabIndex, component) {
	var parentNode = $('panel' + tabIndex + 'Content');
	if(parentNode != null) {
		if(component != null) 
			CURRENT_ELEMENT = component;
		removeChildren(parentNode);
		insertNodesToContainer(resultDOM, parentNode);
		fbLeftAccordion.display(1);
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
		PropertyManager.saveAutofill(parameter);
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
		var button = parameter.getFirst();
		if(button != null) {
			button.setProperty('value', parameter.label);
		}
	}
}
var componentRerenderCallback = function(result) {
	if(result[1] != null) {
		var componentNode = result[1].documentElement;
		var oldNode = $(result[0]);
		if(oldNode != null) {
			replaceNode(result[1], oldNode, $('dropBoxinner'));
		}
	}
}
var buttonRerenderCallback = function(results) {
	var btn = $(results[0]);
	if(btn != null) {
		btn.getFirst().setProperty('value', results[1]);
	}
};
function saveComponentProperty(id,type,value,event) {
	if(event.type == 'blur' || event.type == 'change' || isEnterEvent(event)) {
		PropertyManager.saveComponentProperty(id,type,value,currentCallback);
	}
}
function saveLabel(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.getProperty('value');
	if(value.length != 0) {
		PropertyManager.saveSelectOptionLabel(index,value,currentCallback);
	}
}
function saveValue(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.getProperty('value');
	if(value.length != 0) {
		PropertyManager.saveSelectOptionValue(index,value,currentCallback);
	}
}
function switchDataSource() {
	PropertyManager.switchDataSource({
		callback: function(results) {
			if(results != null) {
				placeComponentInfo(results[2], 1, null);
				componentRerenderCallback(results);
			}
		}
	});
}
function expandOrCollapse(node,expand) {
	if(expand == true) {
		node.getPrevious().setStyle('display', 'inline');
		var value = node.getPrevious().getProperty('value');
		if(value.length == 0) {
			node.getPrevious().setProperty('value', node.getPrevious().getPrevious().getProperty('value'));
		}
		node.setProperty('src', '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_left.png');
		node.setProperty('onclick', 'expandOrCollapse(this,false);');
	} else {
		node.getPrevious().setStyle('display', 'none');
		node.setProperty('src', '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png');
		node.setProperty('onclick', 'expandOrCollapse(this,true);');
	}
}
function addNewItem(parameter) {
	var par = $(parameter).getLast();
	var newInd = getNextRowIndex(par);
	par.appendChild(getEmptySelect(newInd,'',''));
}
function deleteThisItem(ind) {
	var index = ind.split('_')[1];
	var currRow = $(ind);
	var node = $(ind);
	if(node != null) {
		var node2 = node.getParent();
		node2.removeChild(currRow);
	}
	PropertyManager.removeSelectOption(index,currentCallback);
	var rows = $('selectOptsInner').getChildren();
	for(var i = 0; i < rows.length; i++) {
		var row = rows[i];
		row.setProperty('id', 'rowDiv_' + i);
		var delB = row.getFirst();
		delB.setProperty('id', 'delB_' + i);
		var labelF = delB.getNext();
		labelF.setProperty('id', 'labelF_' + i);
		var valueF = labelF.getNext();
		valueF.setProperty('id', 'valueF_' + i);
		var expB = valueF.getNext();
		expB.setProperty('id', 'expB_' + i);
	}
}
function getNextRowIndex(parameter) {
	var lastC = parameter.getLast();
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
	var parentId = 'workspaceform1:rowDiv_' + index;

	var result = new Element('div', {
		'id' : parentId,
	});
	
	var remB = new Element('img', {
		'id' : 'delB_' + index,
		'src': '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png',
		'styles': {
	        'display': 'inline'
	    },
	    'events': {
	    	'click': function() {
	    		deleteThisItem(parentId);
	    	}
	    }
	}).injectInside(result);
	
	var label = new Element('input', {
		'id' : 'labelF_' + index,
		'type':'text',
		'value' : lbl,
		'class' : 'fbSelectListItem',
		'styles': {
	        'display': 'inline'
	    },
	    'events': {
	    	'blur': function() {
	    		saveLabel(this);
	    	}
	    }
	}).injectInside(result);
	
	var value = new Element('input', {
		'id' : 'valueF_' + index,
		'type':'text',
		'value' : vl,
		'class' : 'fbSelectListItem',
		'styles': {
	        'display': 'inline'
	    },
	    'events': {
	    	'blur': function() {
	    		saveValue(this);
	    	}
	    }
	}).injectInside(result);
	
	var expB = new Element('img', {
		'id' : 'expB_' + index,
		'src': '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right-tiny.png',
		'styles': {
	        'display': 'inline'
	    },
	    'events': {
	    	'click': function() {
	    		expandOrCollapse(this,true);
	    	}
	    }
	}).injectInside(result);
	
	return result;
}
function fbsave() {
	var node = $('sourceViewDiv');
	if(node != null) {
		showLoadingMessage('Saving');
		try {
			if(sourceTextarea != null) {
				FormDocument.saveSrc(sourceTextarea.getCode(), closeLoadingMessage);
			} else {
				FormDocument.saveSrc(node.getLast().getProperty('value'), closeLoadingMessage);
			}
		} catch(e) {
			FormDocument.saveSrc(node.getLast().getProperty('value'), closeLoadingMessage);
		}
	} else {
		showLoadingMessage('Saving document...');
		FormDocument.save(closeLoadingMessage);
	}
}
function fbsavesource() {
	showLoadingMessage('Saving');
	var area = $('sourceTextarea');
	var text = area.getText();
	var html = area.innerHTML;
	FormDocument.saveSrc(html, closeLoadingMessage);
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
	var pagesPanel = $('pagesPanelMain');
	if(pagesPanel != null) {
		var pageIcon = pagesPanel.getElement('div.selectedPage');
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
function initializeDesign() {
	initializeDesignView(true);
}