var PAGES_PANEL_ID = 'pagesPanel';
var BUTTON_AREA_ID = 'pageButtonArea';
var SP_PAGES_PANEL_ID = 'pagesPanelSpecial';
var CURRENT_ELEMENT_UNDER = null;
var LAST_ELEMENT_UNDER = -1;
var CURRENT_PAGE_ID;
var childBoxes = [];

var newComponentId = null;
var draggingButton = false;
var draggingComponent = false;
var draggingPage = false;
var insideDropzone = false;

var FORMBUILDER_PATH = "/workspace/forms/formbuilder/";
var FORMADMIN_PATH = "/workspace/forms/entries/";
var FORMSHOME_PATH = "/workspace/forms/list/";

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
var fbComponentSort = null;
var fbButtonSort = null;

var statuses = ['unused', 'single', 'multiple'];

var FBDraggable = Element.extend({
	draggableComponent: function(droppables, handle, type, makeDrag, targetElement) {
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
					droppables.setStyle('background-color', '#F9FF9E');
					CURRENT_ELEMENT_UNDER = null;
					childBoxes = [];
					var childNodes = droppables.getElements(targetElement);
					for(var i = 0; i < childNodes.length; i++){
						var child = childNodes[i];
						var pos = child.getCoordinates();
						childBoxes.push({top: pos.top, bottom: pos.bottom, left: pos.left, right: pos.right, height: pos.height, width: pos.width, node: child});
					}
					this.element.removeEvents('mousemove');
					this.element.addEvent('mousemove', function(e) {
						if(!e) e = window.event;
						for(var i = 0, child; i < childBoxes.length; i++){
							with(childBoxes[i]){
								if (e.pageX >= left && e.pageX <= right && e.pageY >= top && e.pageY <= bottom) {
									CURRENT_ELEMENT_UNDER = childBoxes[i].node.getProperty('id');
									if(CURRENT_ELEMENT_UNDER != LAST_ELEMENT_UNDER) {
										LAST_ELEMENT_UNDER = CURRENT_ELEMENT_UNDER;
									}
								}
							}
						}
					});
					if(type == 'fbc' || type == 'fbcp' || type == 'fbvar') {
						if(draggingComponent == false) {
							newComponentId = this.elementOrg.id;
	   						draggingComponent = true;
						}
					} else if(type == 'fbb' || type == 'fbbp' || type == 'fbtrans') {
						if(draggingButton == false) {
							newComponentId = this.elementOrg.id;
							draggingButton = true;
						}
					}
				},
				onComplete: function(event) {
					this.element.removeEvents('mousemove');
					this.element.remove();
					this.element = this.elementOrg;
					this.elementOrg = null;
					droppables.setStyle('background-color', '#FFF');
					if(insideDropzone == false && newComponentId != null) {
						if((type == 'fbc' || type == 'fbcp' || type == 'fbvar') && draggingComponent == true) {
							draggingComponent = false;
						} else if((type == 'fbb' || type == 'fbbp' || type == 'fbtrans') && draggingButton == true) {
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
		var totalHeight = window.getHeight();
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
function initializeButtonArea() {
	var pageButtonArea = $(BUTTON_AREA_ID);
	if(pageButtonArea != null) {
		pageButtonArea.setStyle('background-color', '#FFFFFF');
		pageButtonArea.removeEvents();
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
				this.dragEffect.stop();
				pageButtonArea.setStyle('background-color', '#FFF');
				if(draggingButton == true) {
					draggingButton = false;
					if(el.hasClass('fbb')) {
						if(newComponentId != null) {
							FormComponent.addButton(newComponentId, CURRENT_ELEMENT_UNDER, null, {
								callback: function(result) {
									addButton(result, pageButtonArea, null, null);
								}
							});
						}
					} else if(el.hasClass('fbbp')) {
						showVariableList(el.getLeft(), el.getTop(), true);
					} else if(el.hasClass('fbtrans')) {
						if(CURRENT_ELEMENT_UNDER != null) {
							showLoadingMessage('Binding transition');
							var variableId = el.getProperty('id');
							FormComponent.assignTransition(CURRENT_ELEMENT_UNDER, variableId, {
								callback: function(result) {
									if(result != null) {
										updateVariableItem(variableId, result[0]);
										
										if(result[1] != null) {
											var oldSpan = $(result[1]);
											if(oldSpan != null) {
												if(oldSpan.hasClass('single')) {
													oldSpan.removeClass('single');
												} else if(oldSpan.hasClass('multiple')) {
													oldSpan.removeClass('multiple');
												}
												oldSpan.addClass(result[2]);
											}
										}
										
										var assignLabel = $('trans_' + CURRENT_ELEMENT_UNDER);
										if(assignLabel != null) {
											var cleanVarName = variableId.substring(variableId.indexOf('_') + 1)
											assignLabel.getLast().setText('Assigned to: ' + cleanVarName);
										}
									}
									CURRENT_ELEMENT_UNDER = null;
									closeLoadingMessage();
								}
							});
						} else {
							if(humanMsg) humanMsg.displayMsg("You have to drop the transition on a button");
						}
					}
				}
				pageButtonArea.setStyle('background-color', '#FFFFFF');
				insideDropzone = false;
			}
		});
	}
	$$('div.formButton').each(function(item) {
		item.removeEvents('click');
		item.addEvent('click', function() {
			PropertyManager.selectComponent(item.getProperty('id'), 'button', {
				callback: function(result) {
					var buttonId = item.getProperty('id');
					currentCallback = buttonRerenderCallback;
					placeComponentInfo(result[0], 1, buttonId);
					handleComponentSelection(result[1], buttonId);
				}
			});
		});
	});
	$$('img.fbSpeedBButton').each(function(item) {
		item.removeEvents('click');
		item.addEvent('click', function(e) {
			new Event(e).stopPropagation();
			var buttonId = item.getParent().getParent().getProperty('id');
			FormComponent.removeButton(buttonId, {
				callback: function(result) {
					if(result != null) {
						var node = $(buttonId);
						if(node != null) {
							node.remove();
							initializeButtonArea();
							
							if(result[1] != null) {
								placeComponentInfo(result[1], 1, result[0]);
							}
							if(result[2] != null) {
								var dropBox = $(BUTTON_AREA_ID);
								if(dropBox != null) {
									var parentNode = dropBox.getParent();
									var node2 = parentNode.getLast();
									if(node2 != null) {
										node2.remove();
									}
									insertNodesToContainer(result[2], parentNode);
									initializeDesignView(true);
									initializePagesPanel();
									initializePaletteInner(true);
								}
							}
							
							updateVariableItem(result[3], result[4]);
							
							initializeButtonSorting(fbButtonSort);
						}
					}
				}
			});
		});
	});
}

function initializeLanguageChooser() {
	var languageChooser = $('languageChooserMenu');
	if(languageChooser != null) {
		languageChooser.removeEvents();
		languageChooser.addEvent('change', function(e) {
			if(languageChooser.id == null) {
				return;
			}
			
			var locale = dwr.util.getValue(languageChooser.id);
			if(locale != '') {
				reloadWorkspace(locale);
			}
		});
	}
}

function handleComponentSelection(oldId, componentId) {
	if(oldId != null && oldId != '') {
		var old = $(oldId);
		if(old != null) {
			old.removeClass('selectedComponent');
		}
	}
	if(componentId != null && componentId != '') {
		var newElement = $(componentId);
		if(newElement != null) {
			newElement.addClass('selectedComponent');
			if(newElement.getParent().getProperty('id') == BUTTON_AREA_ID) {
				currentCallback = buttonRerenderCallback;
			} else {
				currentCallback = componentRerenderCallback;
			}
			if(fbLeftAccordion != null) {
				fbLeftAccordion.display(1);
			}
		}
	}	
}

function initializeSelectedComponent() {
	PropertyManager.getSelectedComponentId({
		callback: function(result) {
			handleComponentSelection(null, result);	
		}
	});
}

function initializeComponentSorting(fbComponentSort) {
	if(fbComponentSort) {
		fbComponentSort.detach();
		fbComponentSort = null;
	}
	fbComponentSort = new Sortables($('dropBoxinner'), {
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
}

function initializeButtonSorting(fbButtonSort) {
	if(fbButtonSort) {
		fbButtonSort.detach();
		fbButtonSort = null;
	}
	fbButtonSort = new Sortables($(BUTTON_AREA_ID), {
		onComplete: function(el){
			var children = $(BUTTON_AREA_ID).getChildren();
			var orderList = [];
			for(var i = 0; i < children.length; i++) {
				var element = children[i];
				orderList.push(element.id);
			}
			FormPage.updateButtonList(orderList);
		},
		handles: '.fbButtonHandler'
	});
}

function addButton(data, container, transition, dialog) {
	if($('noButtonsNotice') != null) {
		$('noButtonsNotice').remove();
	}
	if(data[0] == 'append' || data[0] == null) {
		insertNodesToContainer(data[1], container);
	} else {
		var node = $(data[0]);
		insertNodesToContainerBefore(data[1], container, node);
	}
	newComponentId = null;
	if(fbLeftAccordion != null) {
		fbLeftAccordion.display(0);
	}
	initializeButtonSorting(fbButtonSort);
	
	updateVariableItem(transition, data[1]);
										
	if(dialog) dialog.remove();
										
	closeLoadingMessage();
}

function addComponent(data, container, variable, dialog) {
	if($('noFormNotice') != null) {
		$('noFormNotice').remove();
	}
	if(data[0] == 'append' || data[0] == null) {
		insertNodesToContainer(data[1], container);
	} else {
		var node = $(data[0]);
		insertNodesToContainerBefore(data[1], container, node);
	}
	newComponentId = null;
	initializeDesignView(false);
	container.setStyle('background-color', '#FFFFFF');
	closeLoadingMessage();
	if(fbLeftAccordion != null) {
		fbLeftAccordion.display(0);
	}
	
	updateVariableItem(variable, data[2]);
										
	if(dialog) dialog.remove();
}

function initializeDropbox() {
	var dropBoxinner = $('dropBoxinner');
	if(dropBoxinner != null) {
		dropBoxinner.removeEvents();
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
				this.dragEffect.stop();
				dropBoxinner.setStyle('background-color', '#FFF');
				dropBoxinner.getElements('div.formElement').each(function(element) {
					element.addClass('formElementHover');
				});
				if(draggingComponent == true) {
					draggingComponent = false;
					if(el.hasClass('fbc')) {
						showLoadingMessage('Adding component');
						FormComponent.addComponent(newComponentId, CURRENT_ELEMENT_UNDER, null, {
							callback: function(result) {
								addComponent(result, dropBoxinner, null, null);
							}
						});
					} else if(el.hasClass('fbcp')) {
						showVariableList(el.getLeft(), el.getTop(), false);
					} else if(el.hasClass('fbvar')) {
						if(CURRENT_ELEMENT_UNDER != null) {
							showLoadingMessage('Binding variable');
							var variableId = el.getProperty('id');
							FormComponent.assignVariable(CURRENT_ELEMENT_UNDER, variableId, {
								callback: function(result) {
									if(result != null) {
										updateVariableItem(variableId, result[0]);
										
										if(result[1] != null) {
											var oldSpan = $(result[1]);
											if(oldSpan != null) {
												if(oldSpan.hasClass('single')) {
													oldSpan.removeClass('single');
												} else if(oldSpan.hasClass('multiple')) {
													oldSpan.removeClass('multiple');
												}
												oldSpan.addClass(result[2]);
											}
										}
										
										var assignLabel = $('var_' + CURRENT_ELEMENT_UNDER);
										if(assignLabel != null) {
											var cleanVarName = variableId.substring(variableId.indexOf('_') + 1)
											assignLabel.getLast().setText('Assigned to: ' + cleanVarName);
										}
									}
									CURRENT_ELEMENT_UNDER = null;
									closeLoadingMessage();
								}
							});
						} else {
							if(humanMsg) humanMsg.displayMsg("You have to drop the variable on a component");
						}
					}
				}
				insideDropzone = false;
			}
		});
		dropBoxinner.getElements('div.formElement').each(function(item) {
			item.removeEvents();
			item.addEvent('click', function(e) {
				var componentId = item.getProperty('id');
				PropertyManager.selectComponent(componentId, 'component', {
					callback: function(result) {
						currentCallback = componentRerenderCallback;
						placeComponentInfo(result[0], 1, componentId);
						handleComponentSelection(result[1], componentId);
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
							if(result != null) {
								var node = $(result[0]);
								if(node != null) {
									node.remove();
									
									if(result[1] != null) {
										placeComponentInfo(result[1], 1, result[0]);
									}
									
									if(result[2] != null) {
										var dropBox = $('dropBox');
										if(dropBox != null) {
											var parentNode = dropBox.getParent();
											var node2 = parentNode.getLast();
											if(node2 != null) {
												node2.remove();
											}
											insertNodesToContainer(result[2], parentNode);
											initializeDesignView(true);
											initializePagesPanel();
											initializePaletteInner(true);
										}
									}
									
									updateVariableItem(result[3], result[4]);
								}
							}
						}
					});
				}
			});
		});
	}
}

function initializeDesignView(initializeInline) {
	initializeSelectedComponent();
	initializeLanguageChooser();
	initializeComponentSorting(fbComponentSort);
	initializeButtonSorting(fbButtonSort);
	initializeButtonArea();
	initializeDropbox();
	if(selectedPaletteTab == null) {
		selectedPaletteTab = 'processes';
	}
	if(initializeInline == true) {
		initializeInlineEdits();
	}
}

function saveComponentErrorMessage(errorType, value, event) {
    if(event.type == 'blur' || event.type == 'change' || isEnterEvent(event)) {
        PropertyManager.saveComponentErrorMessage(errorType, value, currentCallback);
    }
}

function initializeVariableDragging(enable) {
	var viewer = $('variableViewer');
	if(viewer != null) {
		viewer.getElements('.fbvar').each(function(el){
			el.removeEvents();
			el.draggableComponent($('dropBoxinner'), null, 'fbvar', enable, 'div.formElement');
		});
		viewer.getElements('.fbtrans').each(function(el){
			el.removeEvents();
			el.draggableComponent($(BUTTON_AREA_ID), null, 'fbtrans', enable, 'div.formButton');
		});
	}
}

function initializePaletteComponents(tab, dropBoxinner, pageButtonArea, enable) {
	$(tab).getElements('.fbc').each(function(el){
		el.removeEvents();
		el.draggableComponent(dropBoxinner, null, 'fbc', enable, 'div.formElement');
	});
	$(tab).getElements('.fbcp').each(function(el){
		el.removeEvents();
		el.draggableComponent(dropBoxinner, null, 'fbcp', enable, 'div.formElement');
	});
	$(tab).getElements('.fbb').each(function(el){
		el.removeEvents();
		el.draggableComponent(pageButtonArea, null, 'fbb', enable, 'div.formButton');
	});
	$(tab).getElements('.fbbp').each(function(el){
		el.removeEvents();
		el.draggableComponent(pageButtonArea, null, 'fbbp', enable, 'div.formButton');
	});
}
function createNewForm() {
	if(modalFormName == null || modalFormName == '') {
		return;
	}
	var dialog = $('TB_window');
	if(dialog != null) {
		dialog.setStyle('visibility', 'hidden');
		showLoadingMessage('Creating form');
	}
	FormDocument.createFormDocument(modalFormName, {
		callback: function(result) {
			if(result == true) {
				window.location=FORMBUILDER_PATH;
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
}
function initializePalette() {
	Workspace.getView({
		callback: function(result) {
			if(result == 'Design') {
				initializePaletteInner(true);
			} else {
				initializePaletteInner(false);
			}
		}
	});
}
function initializePaletteInner(enable) {
	$('firstList').getElements('li.stateFullTab').each(function(item) {
		item.removeEvents();
	});
	var tabs = new mootabs('firstList', {width: '100%', height: '358px', changeTransition: 'none'});
	var tabElements = $('firstList').getElements('li.stateFullTab');
	tabElements.each(function(item) {
		item.addEvent('click', function(e){
			selectedPaletteTab = item.getProperty('title');
			initializePaletteComponents(selectedPaletteTab, $('dropBoxinner'), $(BUTTON_AREA_ID), enable);
		});
	});
	if(tabElements.length > 0) {
		var firstTab = tabElements[0];
		initializePaletteComponents(firstTab.getProperty('title'), $('dropBoxinner'), $(BUTTON_AREA_ID), enable);
	}
}
function initializeVariableViewer() {
	/*$$('.addVariableIcon').each(function(el){
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
	$$('img.removeVarIcon').each(function(item) {
		item.addEvent('click', function(event) {
			new Event(event).stopPropagation();
			var componentId = item.getParent().getParent().getProperty('id');
			FormComponent.removeVariableBinding(componentId, {
				callback: function(result) {
					if(result != null) {
						updateVariableItem(result[0], result[1]);
						
						item.getNext().setText('Not assigned');
					}
				}
			});
		});
	});
	$$('img.removeTransIcon').each(function(item) {
		item.addEvent('click', function(event) {
			new Event(event).stopPropagation();
			var componentId = item.getParent().getParent().getProperty('id');
			FormComponent.removeTransitionBinding(componentId, {
				callback: function(result) {
					if(result != null) {
						updateVariableItem(result[0], result[1]);
						
						item.getPrevious().setText('Not assigned');
					}
				}
			});
		});
	});
	initializeVariableDragging(true);*/
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
				replaceNode(resultDOM, $('mainApplication').getFirst(), $('mainApplication'));
				initializeAccordions();
				initializePalette(true);
				initializePagesPanel();
				initializeDesignView(true);
				initializeVariableViewer();
				initializeBottomToolbar();
			}
			closeLoadingMessage();
		}
	});
}
function setHrefToVoidFunction(element) {
	element.setProperty('href', 'javascript:void(0)');
}
function showVariableList(positionLeft, positionTop, transition) {
	FormComponent.getAvailableProcessDataList(newComponentId, transition, {
		callback : function(result) {
			insertNodesToContainer(result, $('mainApplication'));
			
			var container = $('selectVariableDialog');
			if(container != null) {
				var noVariableBtn = $('noVariableBtn');
				if(noVariableBtn != null) {
					setHrefToVoidFunction(noVariableBtn);
					noVariableBtn.removeEvents('click');
					noVariableBtn.addEvent('click', function(e) {
						if(transition) {
							showLoadingMessage('Adding button');
							FormComponent.addButton(newComponentId, CURRENT_ELEMENT_UNDER, null, {
								callback: function(result) {
									addButton(result, pageButtonArea, null, container);
								}
							});
						} else {
							showLoadingMessage('Adding component');
							FormComponent.addComponent(newComponentId, CURRENT_ELEMENT_UNDER, null, {
								callback: function(result) {
									addComponent(result, dropBoxinner, null, container);
								}
							});
						}
					});
				}
				var cancelVariableBtn = $('cancelVariableBtn');
				if(cancelVariableBtn != null) {
					setHrefToVoidFunction(cancelVariableBtn);
					cancelVariableBtn.removeEvents('click');
					cancelVariableBtn.addEvent('click', function(e) {
						container.remove();
					});
				}
				container.setStyle('left', positionLeft);
				container.setStyle('top', positionTop);
				container.setStyle('position', 'fixed');
				var list = $('variablePopupList');
				if(list != null) {
					list.getElements('span').each(function(item) {
						item.addEvent('click', function(e) {
							new Event(e).stop();
							var variable = item.getProperty('rel');
							if(transition) {
								showLoadingMessage('Adding button');
								FormComponent.addButton(newComponentId, CURRENT_ELEMENT_UNDER, variable, {
									callback: function(data) {
										addButton(data, pageButtonArea, variable, container);
									}
								});
							} else {
								showLoadingMessage('Adding component');
								FormComponent.addComponent(newComponentId, CURRENT_ELEMENT_UNDER, variable, {
									callback : function(data) {
										addComponent(data, dropBoxinner, variable, container);
									} 
								});
							}
						});
					});
				}
			}
		}
	});
}

function updateVariableItem(variable, status) {
	if(variable == null || status == null) {
		return;
	}

	var varSpan = $(variable);
	if(varSpan != null) {
		for(var i = 0; i < statuses.length; i++) {
			if(status == statuses[i]) {
				varSpan.addClass(status);
			} else {
				varSpan.removeClass(statuses[i]);
			}
		}
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
function saveThankYouTitle(parameter) {
	if(parameter != null) {
		FormDocument.setThankYouTitle(parameter, placePageTitle);
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
			parent.setText(parameter.pageTitle);
		}
	}
}
function enablePalettePanelActions(enable) {
	if(enable == true) {
		$('optionsPanelMessageBox').setStyle('display', 'none');
		initializePalette(false);
	} else {
		$('optionsPanelMessageBox').setStyle('display', 'block');
		initializePalette(true);
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
		
		initializeNewPageAction(false);
		
		initializePreviewPageAction(false);
		
		var pagesPanel = $(PAGES_PANEL_ID);
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
					callback: function(result) {
						if(result != null) {
							var viewPanel = $('viewPanel');
							var mainWorkspace = viewPanel.getParent();
							replaceNode(result[0], viewPanel, mainWorkspace);
							if(view != 'Design') {
								enablePagesPanelActions(false);
								enablePalettePanelActions(false);
							} else {
								enablePagesPanelActions(true);
								enablePalettePanelActions(true);
								initializeDesignView(true);
							}
							placeComponentInfo(result[1], 1, null);
							if(view == 'Source') {
								initializeSourceView();
							}
							var selView = toolbar.getElement('a.activeViewButton');
							if(selView != null) {
								selView.removeClass('activeViewButton');
							}
							item.addClass('activeViewButton');
							fbLeftAccordion.display(0);
							closeLoadingMessage();
						}
					}
				});
			});
		});
	}
}

function initializePageSorting(fbPageSort) {
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
}

function initializeNewPageAction(enable) {
	var newPageButton = $('newPageButton');
	if(newPageButton != null) {
		newPageButton.removeEvents('click');
		setHrefToVoidFunction(newPageButton);
		
		if(enable == true) {
			newPageButton.addEvent('click', function(e) {
				new Event(e).stop();
				FormPage.createNewPage({
					callback: function(result) {
						if(result != null) {
							insertNodesToContainer(result[1], $(PAGES_PANEL_ID));
							var panel = $(PAGES_PANEL_ID);
							if(panel != null) {
								panel.getLast().addEvent('click', function(e){
									initializeGeneralPage(panel.getLast());
								});
							}
							var dropBox = $('dropBox');
							if(dropBox != null) {
								var parentNode = dropBox.getParent();
								var node = parentNode.getLast();
								if(node != null) {
									node.remove();
								}
								insertNodesToContainer(result[0], parentNode);
								initializeDesignView(true);
								initializePagesPanel();
								initializePaletteInner(true);
								var newIcon = $(PAGES_PANEL_ID).getLast();
								markSelectedPage(newIcon);
							}
							
							placeComponentInfo(result[2], 1, null);
							fbLeftAccordion.display(0);
						}
					}
				});
			});
		}
	}
}

function initializeThankyouPage() {
	var thankyoupage = $(SP_PAGES_PANEL_ID).getElement('div.thankyou');
	if(thankyoupage != null) {
		thankyoupage.addEvent('click', function(e){
			showLoadingMessage('Loading section...');
			var targetId = getPageID(thankyoupage);
			FormPage.getThxPageInfo({
				callback: function(result) {
					if(result != null) {
						reloadDesignView(result[0], targetId);
						placeComponentInfo(result[1], 1, null);
					}
				}
			});
		});
	}
}

function initializePreviewPageAction(enable) {
	var previewPageButton = $('previewPageButton');
	if(previewPageButton != null) {
		previewPageButton.removeEvents('click');
		setHrefToVoidFunction(previewPageButton);
		
		if(enable == true) {
			previewPageButton.addEvent('click', function(e){
				new Event(e).stop();
				var checked;
				if(previewPageButton.hasClass('addPreviewPageBtn')) {
					checked = true;
				} else {
					checked = false;
				}
				FormDocument.togglePreviewPage(checked, {
					callback: function(resultDOM) {
						if(resultDOM != null) {
							if(checked == true) {
								previewPageButton.removeClass('addPreviewPageBtn').addClass('removePreviewPageBtn');
								insertNodesToContainerBefore(resultDOM, $(SP_PAGES_PANEL_ID), $(SP_PAGES_PANEL_ID).childNodes[0]);
								initializePreviewPage();
							} else {
								previewPageButton.removeClass('removePreviewPageBtn').addClass('addPreviewPageBtn');
								$(SP_PAGES_PANEL_ID).getFirst().remove();
							}
						}
					}
				});
			});
		}
	}
}

function initializePagesPanelActions() {
	initializePageSorting(fbPageSort);
	initializeNewPageAction(true);
	initializePreviewPageAction(true);
	
	FormPage.getId(markSelectedPage);
	var pagesPanel = $(PAGES_PANEL_ID);
	if(pagesPanel != null) {
		pagesPanel.getElements('div.formPageIcon').each(function(item) {
			item.addEvent('click', function(e){
				initializeGeneralPage(item);
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
									var iconNode = $(actualId + '_P_page');
									if(iconNode != null) {
										iconNode.remove();
									}
									markSelectedPage(result[0])
									var dropBox = $('dropBox');
									if(dropBox != null) {
										var parentNode = dropBox.getParent();
										var node = parentNode.getLast();
										node.remove();
										insertNodesToContainer(result[1], parentNode);
										initializeDesignView(true);
									}
									placeComponentInfo(result[2], 1, null);
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
	initializeThankyouPage();
	initializePreviewPage();
}
function initializePagesPanel() {
	Workspace.getView({
		callback: function(result) {
			if(result == 'Design') {
				initializePagesPanelActions();
				$('designButton').addClass('activeViewButton');
			} else {
				initializeNewPageAction(false);
				
				initializePreviewPageAction(false);
				
				if(result == 'Source') {
					$('sourceCodeButton').addClass('activeViewButton');
					initializeSourceView();
				} else if(result == 'Preview') {
					$('previewButton').addClass('activeViewButton');
				}
			}
		}
	});
}
function initializeSourceView() {
	FormDocument.getSourceCode({
		callback : function(code) {
			var textarea = $('sourceTextarea');
			if(textarea != null) {
				textarea.setText(code);
				textarea.addClass('codepress');
				textarea.addClass('html');
				textarea.addClass('linenumbers-on');
				CodePress.run();
			}
		}
	});
}
function initializeGeneralPage(element) {
	var targetId = getPageID(element);
	if(draggingPage == false && targetId.indexOf('_P_page') != -1) {
		var actualId = targetId.substring(0, targetId.indexOf('_P_page'));
		showLoadingMessage('Loading section...');
		FormPage.getFormPageInfo(actualId, {
			callback: function(result) {
				if(result != null) {
					reloadDesignView(result[0], targetId);
					placeComponentInfo(result[1], 1, null);
					fbLeftAccordion.display(0);
				}
			}
		});
	}
}
function initializePreviewPage() {
	var previewp = $(SP_PAGES_PANEL_ID).getElement('div.preview');
	if(previewp != null) {
		previewp.addEvent('click', function(e){
			showLoadingMessage('Loading section...');
			var targetId = getPageID(previewp);
			FormPage.getConfirmationPageInfo({
				callback: function(result) {
					if(result != null) {
						reloadDesignView(result[0], targetId);
						placeComponentInfo(result[1], 1, null);
					}
				}
			});
		});
	}
}
function getPageID(pageElement) {
	if(pageElement.hasClass('formPageIcon')) {
		return pageElement.getProperty('id');
	} else {
		return pageElement.getParent().getProperty('id');
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
			initializePaletteInner(true);
		}
		closeLoadingMessage();
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

			var heightForAccordion = window.getHeight() - heightOffset;
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
var componentRerenderCallback = function(result) {
	if(result == null) {
		return;
	}
	if(result[1] != null) {
		var componentNode = result[1].documentElement;
		var oldNode = $(result[0]);
		if(oldNode != null) {
			replaceNode(result[1], oldNode, $('dropBoxinner'));
			initializeSelectedComponent();
			initializeComponentSorting(fbComponentSort);
			initializeDropbox();
		}
	}
	if(result[2] != null) {
		placeComponentInfo(result[2], 1, result[0]);
	}
}
var buttonRerenderCallback = function(results) {
	if(results == null) {
		return;
	}
	var btn = $(results[0]);
	if(btn != null) {
		btn.getFirst().getNext().setProperty('value', results[1]);
	}
};
function saveComponentProperty(id, type, value, event) {
	if(event.type == 'blur' || event.type == 'change' || isEnterEvent(event)) {
		PropertyManager.saveComponentProperty(id,type,value,currentCallback);
	}
}
function saveLabel(parameter) {
	var index = parameter.getProperty('id').split('_')[1];
	var value = parameter.getProperty('value');
	if(value.length != 0) {
		PropertyManager.saveSelectOptionLabel(index, value, currentCallback);
	}
}
function saveValue(parameter) {
	var index = parameter.getProperty('id').split('_')[1];
	var value = parameter.getProperty('value');
	if(value.length != 0) {
		PropertyManager.saveSelectOptionValue(index, value, currentCallback);
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
	getEmptySelect(newInd,'','').injectInside(par);
}
function deleteThisItem(ind) {
	var index = ind.split('_')[1];
	var currRow = $(ind).remove();
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
function expandOrCollapseAllItems(expand) {
	var styleValue = null;
	if(expand == true) {
		styleValue = 'inline';
	} else {
		styleValue = 'none';
	}
	var container = $('selectOptsInner');
	if(container != null) {
		for(var i=0;i<container.getChildren().length;i++) {
			container.childNodes[i].childNodes[2].style.display = styleValue;
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
	        'display': 'none'
	    },
	    'events': {
	    	'blur': function() {
	    		saveValue(this);
	    	}
	    }
	}).injectInside(result);
	
	var expB = new Element('img', {
		'id' : 'expB_' + index,
		'src': '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png',
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
		var dialog = $('TB_window');
		if(dialog != null) {
			dialog.setStyle('visibility', 'hidden');
			showLoadingMessage('Creating form');
		}
		showLoadingMessage('Saving document...');
		FormDocument.save(closeLoadingMessage);
	}
}
function initializeInlineEdits() {
	$$('div.inlineEdit').each(
		function(element) {
			var editIcon = new Element('img');
			element.setStyle('cursor','pointer');
			editIcon.addClass('inlineEditIcon');
			editIcon.setProperty('src', '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit_16.png');
			editIcon.injectInside(element);
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
function initializeDesign() {
	initializeDesignView(true);
}