if (!FormBuilder) var FormBuilder = {};

FormBuilder.initializeFormBuilder = function() {
	initializeAccordions();
	initializePalette();
	initializePagesPanel();
	initializeDesign();
	registerFormbuilderActions();
	initializeVariables();
	initializeBottomToolbar();
}

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

var fbLeftAccordion = null;
var fbRightAccordion = null;

var currentCallback = null;
var selectedPaletteTab = null;
var fbPageSort = null;
var fbComponentSort = null;
var fbButtonSort = null;

var formCodeEditor = null;

var statuses = ['unused', 'single', 'multiple'];

function controlFormbuilderAppWindow() {
	resizeAccordion(RESERVED_HEIGHT_FOR_FB, 'firstList', false);
	resizeAccordion(RESERVED_HEIGHT_FOR_FB, 'pagesPanelMain', true);
	resizeComponentsArea();
}
function resizeComponentsArea() {
	var totalHeight = window.getHeight();
	var height = totalHeight - RESERVED_HEIGHT_FOR_FB - 104 - jQuery('#pageButtonArea').height();
	if (height > 0) {
		jQuery('#dropBoxinner').css('height', height + 'px');
	}
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
function initializeDroppableArea(areaId, targetElement, hoverClass) {
	var area = $(areaId);
	if(area != null) {
		area.setStyle('background-color', '#FFFFFF');
		area.removeEvents();
		area.addEvents({
			'over': function(el){
				if (!this.dragEffect) {
					this.dragEffect = new Fx.Style(this, 'background-color');
				}
				this.dragEffect.stop().start('#F9FF9E', '#FFFF00');
				insideDropzone = true;
				if(targetElement != null) {
					area.getElements(targetElement).each(function(element) {
						element.removeClass(hoverClass);
					});
				}
			},
			'leave': function(el){
				this.dragEffect.stop().start('#FFFF00', '#F9FF9E');
				insideDropzone = false;
				if(targetElement != null) {
					area.getElements(targetElement).each(function(element) {
						element.addClass(hoverClass);
					});
				}
			},
			'drop': function(el, drag){
				this.dragEffect.stop();
				if(targetElement != null) {
					area.getElements(targetElement).each(function(element) {
						element.addClass(hoverClass);
					});
				}
				if(draggingButton) {
					draggingButton = false;
					if(el.hasClass('fbb')) {
						if(newComponentId != null) {
							FormComponent.addButton(newComponentId, CURRENT_ELEMENT_UNDER, null, {
								callback: function(result) {
									addButton(result, area, null, null);
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
									handleProcessDataAssignment(result, variableId);
								}
							});
						} else {
							if(humanMsg) humanMsg.displayMsg("You have to drop the transition on a button");
						}
					} 
				} else if(draggingComponent) {
					draggingComponent = false;
					if(el.hasClass('fbc')) {
						showLoadingMessage('Adding component');
						FormComponent.addComponent(newComponentId, CURRENT_ELEMENT_UNDER, null, {
							callback: function(result) {
								addComponent(result, area, null, null);
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
									handleProcessDataAssignment(result, variableId);
								}
							});
						} else {
							if(humanMsg) humanMsg.displayMsg("You have to drop the variable on a component");
						}
					}
				}
				area.setStyle('background-color', '#FFFFFF');
				insideDropzone = false;
			}
		});
	}
}

function handleProcessDataAssignment(data, variable) {
	if(data != null) {
		updateVariableItem(variable, data[0]);
		updateVariableItem(data[1], data[2]);
					
		if(CURRENT_ELEMENT_UNDER != null) {
			var assignLabel = $(CURRENT_ELEMENT_UNDER).getElement('span.assignLabel');
			if(assignLabel != null) {
				var cleanVarName = variable.substring(variable.indexOf('_') + 1)
				assignLabel.setText('Assigned to: ' + cleanVarName);
			}
		}
	}
	CURRENT_ELEMENT_UNDER = null;
	closeLoadingMessage();
}

function handleProcessDataAssignmentChange(componentId, data, variable) {
	if(data != null) {
		updateVariableItem(variable, data[0]);
		updateVariableItem(data[1], data[2]);
					
		if(componentId != null) {
			var assignLabel = $(componentId).getElement('span.assignLabel');
			if(assignLabel != null) {
				var cleanVarName = variable.substring(variable.indexOf('_') + 1)
				assignLabel.setText('Assigned to: ' + cleanVarName);
			}
		}
	}
	closeLoadingMessage();
}

function initializeButtonArea() {
	initializeDroppableArea(BUTTON_AREA_ID, null, null);
	$$('div.formButton').each(function(item) {
		item.removeEvents('click');
		item.addEvent('click', function() {
			PropertyManager.selectComponent(item.getProperty('id'), 'button', {
				callback: function(result) {
					var buttonId = item.getProperty('id');
					currentCallback = buttonRerenderCallback;
					placeComponentInfo(result[0], 1, buttonId);
					handleComponentSelection(result[1], buttonId);
					if(fbLeftAccordion) fbLeftAccordion.display(1);
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

function initializeVersionChooser() {
	var versionChooser = $('versionChooserMenu');
	if(versionChooser != null) {
		versionChooser.removeEvents();
		versionChooser.addEvent('change', function(e) {
			if(versionChooser.id == null) {
				return;
			}
			
			var version = dwr.util.getValue(versionChooser.id);
			if (version != '') {
				loadDocumentByVersion(version);
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
			if(fbLeftAccordion) fbLeftAccordion.display(1);
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
	jQuery('#dropBoxinner').sortable({
		update: function() {
			showLoadingMessage('Saving order...');
			FormPage.updateComponentList(jQuery('#dropBoxinner').sortable('toArray'), {
				callback: function() {
					closeAllLoadingMessages();
				}
			});
		}
	});
}

function initializeButtonSorting(fbButtonSort) {
	jQuery('#' + BUTTON_AREA_ID).sortable({
		scroll: false,
		update: function() {
			showLoadingMessage('Saving order...');
			console.log(jQuery('#' + BUTTON_AREA_ID).sortable('toArray'));
			FormPage.updateButtonList(jQuery('#' + BUTTON_AREA_ID).sortable('toArray'), {
				callback: function() {
					closeAllLoadingMessages();
				}
			});
		}
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
	
	initializeDesignView(false, function() {
		newComponentId = null;
		if(fbLeftAccordion) fbLeftAccordion.display(1);
		
		initializeButtonSorting(fbButtonSort);
		
		updateVariableItem(transition, data[1]);
											
		if(dialog) dialog.remove();
											
		closeLoadingMessage();
	});
}

function addComponent(data, container, variable, dialog) {
	if($('noFormNotice')) $('noFormNotice').remove();
	
	if(data[0] == 'append' || data[0] == null) {
		insertNodesToContainer(data[1], container);
	} else {
		var node = $(data[0]);
		insertNodesToContainerBefore(data[1], container, node);
	}
	newComponentId = null;
	initializeDesignView(false, function() {
		container.setStyle('background-color', '#FFFFFF');
		closeLoadingMessage();
		if(fbLeftAccordion) fbLeftAccordion.display(1);
		
		updateVariableItem(variable, data[2]);
											
		if(dialog) dialog.remove();
		
		if (data[3] != null) {
			clickedFormElementAction(data[3]);
		}
	});
}

function clickedFormElementAction(componentId) {
	PropertyManager.selectComponent(componentId, 'component', {
		callback: function(result) {
			currentCallback = componentRerenderCallback;
			placeComponentInfo(result[0], 1, componentId);
			handleComponentSelection(result[1], componentId);
			if(fbLeftAccordion) fbLeftAccordion.display(1);
		}
	});	
}

function initializeDropbox() {
	initializeDroppableArea('dropBoxinner', 'div.formElement', 'formElementHover');
	var dropBoxinner = $('dropBoxinner');
	if(dropBoxinner != null) {
		dropBoxinner.getElements('div.formElement').each(function(item) {
			item.removeEvents('click');
			item.addEvent('click', function(e) {
				var componentId = item.getProperty('id');				
				clickedFormElementAction(componentId);
			});
			var speedButton = item.getElement('img.speedButton');
			if(speedButton != null) {
				speedButton.removeEvents('click');
				speedButton.addEvent('click', function(e) {
					new Event(e).stopPropagation();
					var node = speedButton.getParent();
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
			}
		});
	}
}
function initializeDesignView(initializeInline, callback) {
	initializeSelectedComponent();
	initializeLanguageChooser();
	initializeVersionChooser();
	initializeComponentSorting(fbComponentSort);
	initializeButtonSorting(fbButtonSort);
	initializeButtonArea();
	initializeDropbox();
	resizeComponentsArea();
	
	if(selectedPaletteTab == null) {
		selectedPaletteTab = 'processes';
	}
	if(initializeInline == true) {
		initializeInlineEdits();
	}
	
	if (callback) {
		callback();
	}
}

function saveComponentErrorMessage(errorType, value, event) {
    if(event.type == 'blur' || event.type == 'change' || isEnterEvent(event)) {
        PropertyManager.saveComponentErrorMessage(errorType, value, currentCallback);
    }
}

function saveComponentCalcExpression(value, event) {
	if(event.type == 'blur' || event.type == 'change' || isEnterEvent(event)) {
        PropertyManager.saveComponentCalcExpression(value, currentCallback);
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
	var tabComponent = $(tab);
	if(tabComponent != null) {
		tabComponent.getElements('.fbc').each(function(el){
			el.removeEvents();
			el.draggableComponent(dropBoxinner, null, 'fbc', enable, 'div.formElement');
		});
		tabComponent.getElements('.fbcp').each(function(el){
			el.removeEvents();
			el.draggableComponent(dropBoxinner, null, 'fbcp', enable, 'div.formElement');
		});
		tabComponent.getElements('.fbb').each(function(el){
			el.removeEvents();
			el.draggableComponent(pageButtonArea, null, 'fbb', enable, 'div.formButton');
		});
		tabComponent.getElements('.fbbp').each(function(el){
			el.removeEvents();
			el.draggableComponent(pageButtonArea, null, 'fbbp', enable, 'div.formButton');
		});
	}
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
	var saveFormButton = getSaveFormButton();
	if(saveFormButton != null) {
		saveFormButton.setProperty('Title', 'Save form');
		saveFormButton.setProperty('href', '#TB_inline?height=60&width=350&inlineId=saveFormDialog');
	}
	
	var saveSrcFormButton = getSaveScourceFormButton();
	if(saveSrcFormButton != null) {
		saveSrcFormButton.setProperty('Title', 'Save source');
		saveSrcFormButton.setProperty('href', '#TB_inline?height=60&width=350&inlineId=saveFormDialog');
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
				enableSaveFormButton(true);
			} else {
				initializePaletteInner(false);
				initializeVariableViewer(false);
			}
			
			if (result == 'Source')					
					enableSaveFormButton(false);
				else 
					enableSaveFormButton(true);
			
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
function initializeVariableViewer(enable) {
	$$('.addVariableIcon').each(function(item){
		item.removeEvents('click');
		if(enable == true) {
			item.addEvent('click', function(e) {
				var id = item.getProperty('id');
				var datatype = id.substring(0, id.indexOf('_'));
				var input = new Element('input');
				input.setProperty('type', 'text');
				input.addEvent('keypress', function(event) {
					if(isEnterEvent(event)) {
						var value = event.target.value;
						createVariable(datatype, value, input, item);
					}
				});
				item.replaceWith(input);
				input.focus();
			});
		}
	});
	$$('img.removeVarIcon').each(function(item) {
		item.removeEvents('click');
		if(enable == true) {
			item.addEvent('click', function(event) {
				var componentId = item.getParent().getParent().getProperty('id');
				
				element = jQuery(item);
				var offsets = element.offset();
				if (offsets == null) {
					return false;
				}
				var left = offsets.left;
				var top = offsets.top;
				showVariableListForChange(left, top, componentId, false, item);
			});
		}
	});
	$$('img.removeTransIcon').each(function(item) {
		item.removeEvents('click');
		if(enable == true) {
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
		}
	});
	initializeVariableDragging(enable);
}

function showVariableListForChange(positionLeft, positionTop, componentId, transition, item) {
	FormComponent.getAvailableProcessDataList(null, transition, {
		callback : function(result) {
			var prevcontainer = $('selectVariableDialog');
			if (prevcontainer != null) {
				prevcontainer.remove() ;
			};
			insertNodesToContainer(result, $('mainApplication'));
			var container = $('selectVariableDialog');
			if(container != null) {
				var noVariableBtn = $('noVariableBtn');
				if(noVariableBtn != null) {
					setHrefToVoidFunction(noVariableBtn);
					noVariableBtn.removeEvents('click');
					noVariableBtn.addEvent('click', function(e) {
						FormComponent.removeVariableBinding(componentId, {
						callback: function(res) {
							if(res != null) {
								updateVariableItem(result[0], result[1]);
								item.getNext().setText('Not assigned');
							}
						}
					});
					container.remove();
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
					list.getElements('span').each(function(it) {
						it.addEvent('click', function(e) {
							new Event(e).stop();
							var variable = it.getProperty('rel');
							showLoadingMessage('Assigning variable');
							FormComponent.assignVariable(componentId, variable, {
								callback: function(res) {
								handleProcessDataAssignmentChange(componentId, res, variable);
								container.remove();
								}
							});
						});
					});
				}
			}
		}
	});
}
function createVariable(datatype, value, element, image) {
	ProcessData.createVariable(value, datatype, false, {
		callback: function(result) {
		if (result) {
			var span = new Element('span', {
				'id': value + '_var',
				'class': 'varEntry unused',
			}).setText(value).injectBefore(element);
			element.replaceWith(image);
		}
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
				initializeVariableViewer(true);
				initializeBottomToolbar();
			}
			closeLoadingMessage();
		}
	});
}
function loadDocumentByVersion(version) {
	showLoadingMessage('Loading...');
	FormDocument.changeTaskFormDocumentVersion(version, {
		callback: function(success) {
			if(success != null) {
				window.location.reload();
			}
		}
	});
	closeLoadingMessage();
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
		CURRENT_PAGE_ID = parameter;
		if(pageNode != null) {
			pageNode.addClass('selectedPage');
		}
	}
}
function placePageTitle(parameter) {
	if(parameter != null) {
		var node = $(parameter.pageId);
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
		
		$$('div.formPageIcon').each(function(item) {
			item.removeEvents('click');
			var speedButton = item.getElement('img.pageSpeedButton');
			if(speedButton != null) {
				speedButton.removeEvents('click');
			}
		});
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
							
				if(view == 'Design' && $('sourceViewDiv') != null) {
					fbsave();
				}
				Workspace.switchView(view, {
					callback: function(result) {
						if(result != null) {
							var viewPanel = $('viewPanel');
							var mainWorkspace = viewPanel.getParent();
							replaceNode(result[0], viewPanel, mainWorkspace);
							if(view != 'Design') {
								enablePagesPanelActions(false);
								enablePalettePanelActions(false);
								initializeVariableViewer(false);
							} else {
								enablePagesPanelActions(true);
								enablePalettePanelActions(true);
								initializeDesignView(true);
								initializeVariableViewer(true);
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
	$$('div.formPageIcon').each(function(item) {
		item.addEvent('click', function(e){
			initializeGeneralPage(item);
		});
		var pageSpeedButton = item.getElement('img.pageSpeedButton');
		if(pageSpeedButton != null) {
			pageSpeedButton.removeEvents('click');
			pageSpeedButton.addEvent('click', function(e) {
				new Event(e).stopPropagation();
				var root = $(PAGES_PANEL_ID);
				if(root != null) {
					var nodes = root.getChildren();
					if(nodes.length == 1) {
						return;
					}
				}
				var parentNode = pageSpeedButton.getParent();
				if(parentNode != null) {
					var targetId = parentNode.getProperty('id');
					FormPage.removePage(targetId, {
						callback: function(result) {
							if(result != null) {
								showLoadingMessage('Loading section...');
								item.remove();
								
								markSelectedPage(result[0])
								var dropBox = $('dropBox');
								if(dropBox != null) {
									var parentNode = dropBox.getParent();
									var node = parentNode.getLast();
									node.remove();
									insertNodesToContainer(result[1], parentNode);
									initializeDesignView(true);
								}
								if(result[3] != null) {
									var viewer = $('variableViewer');
									if(viewer != null) {
										var parentNode = viewer.getParent();
										viewer.remove();
										insertNodesToContainer(result[3], parentNode);
									}
								}
								placeComponentInfo(result[2], 1, null);
								closeLoadingMessage();
							}
						}
					});
				}
				initializePagesPanel();
			});
		}
	});
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
		callback : function(results) {
			var textarea = $('sourceTextarea');
			if (textarea != null) {
				textarea.setText('');
				
				if (results == null) {
					return false;
				}
				
				LazyLoader.load(results[1], function() {
					formCodeEditor = CodeMirror.fromTextArea('sourceTextarea', {
						parserfile: ['parsexml.js'],
						stylesheet: results[2],
						path: results[3],
						content: results[0]
					});
				});
			}
		}
	});
}
function initializeGeneralPage(element) {
	var targetId = element.getProperty('id');
	if(draggingPage == false) {
		showLoadingMessage('Loading section...');
		FormPage.getFormPageInfo(targetId, {
			callback: function(result) {
				if(result != null) {
					reloadDesignView(result[0], targetId);
					placeComponentInfo(result[1], 1, null);
					fbLeftAccordion.display(0);
					
					if(isSpecialPageSelected()) {
						$('optionsPanelMessageBox').setStyle('display', 'block');
						initializePaletteInner(false);
					} else {
						$('optionsPanelMessageBox').setStyle('display', 'none');
						initializePaletteInner(true);
					}
				}
			}
		});
	}
}
function isSpecialPageSelected() {
	var pages = $$('div.selectedPage');
	if(pages != null && pages.length == 1) {
		if(pages[0].getProperty('rel') == 'special') {
			return true;
		} else {
			return false;
		}
	} else {
		return false;
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
	
	jQuery('input[type=\'button\']', '#' + results[0]).attr('value', results[1]);
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
			if(formCodeEditor != null) {
				FormDocument.saveSrc(formCodeEditor.getCode(), closeLoadingMessage);
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
function fbsaveAllVersions() {
	var node = $('sourceViewDiv');
	if(node != null) {
		showLoadingMessage('Saving');
		try {
			if(formCodeEditor != null) {
				FormDocument.saveSrc(formCodeEditor.getCode(), closeLoadingMessage);
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
		FormDocument.saveAllVersions(closeLoadingMessage);
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
function initializeVariables() {
	initializeVariableViewer(true);
}
var FBDraggable = Element.extend({
	draggableComponent: function(droppables, handle, type, makeDrag, targetElement) {
		type = type;
		handle = handle || this;
		if(makeDrag) {
			this.makeDraggable({
				'handle': handle,
				'droppables': droppables,
				onStart: function() {
					this.elementOrg = this.element
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

function showVariableAccessPopup(event, element, variableName) {
	
	if (element == null || event == null) {
		return false;
	}
	
	element = jQuery(element);
	
	var offsets = element.offset();
	if (offsets == null) {
		return false;
	}
	
	var xCoord = offsets.left;
	var yCoord = offsets.top;
	
	if (event) {
		if (event.stopPropagation) {
			event.stopPropagation();
		}
		event.cancelBubble = true;
	}
	
    var htmlForBox = "<div class='variableAccessPopupStyle' />";
	var rightsBox = jQuery(htmlForBox);
		
	jQuery(document.body).append(rightsBox);
	   
	rightsBox.css('top', yCoord + 'px');
	rightsBox.css('left', xCoord + 'px');
	
	var clbck = {
	   callback: function(component) {
            if (component == null) {
                return false;
            }
            insertNodesToContainer(component, rightsBox[0]);
            
            rightsBox.show('fast');
        }
	};
    ProcessData.getVariableAccessesBox(variableName, clbck);
}

function closeVariableAccessRightsBox (element) {
	
	var rightsBoxCands = jQuery(element).parents(".variableAccessPopupStyle");
	
	if (rightsBoxCands == null || rightsBoxCands.length == 0) {
		return false;
	}
	
	var rightsBox = jQuery(rightsBoxCands[0]);
	
	rightsBox.hide('fast', 
        function () {
            rightsBox.remove();
        }
	);
}

function saveVariableAccessRights (element, variableName) {
	
	var box = jQuery(element).parents(".variableAccessPopupStyle");
	var read = jQuery(box).find('.read')[0];
	var write = jQuery(box).find('.write')[0];
	var required = jQuery(box).find('.required')[0];
	var access = '';
	if (read != null && read.checked) {
		access += 'read';
	}
	if (write != null && write.checked) {
		access += 'write';
	}
	if (required != null && required.checked) {
		access += 'required';
	}
	ProcessData.saveVariableAccesses(variableName, access);
}

function enableSaveFormButton(enable){
	
	var saveButton = getSaveFormButton();
	var saveSrcButton = getSaveScourceFormButton();
			
	if (enable) {
			saveButton.setStyle('display', 'block');
			saveSrcButton.setStyle('display', 'none');
			
	}else {
		saveButton.setStyle('display', 'none');
		saveSrcButton.setStyle('display', 'block');
	}
}

function getSaveFormButton() {
	return $('saveFormButton');
}

function getSaveScourceFormButton() {
	return $('saveFormSrcButton');
}

FormBuilder.toggleUseHtmlEditor = function(checkBoxId, componentId, type, event) {
	var value = jQuery('#' + checkBoxId).attr('checked') == 'checked';
	//saveComponentProperty(componentId, type, value, event);
	PropertyManager.saveComponentProperty(componentId, type, value, currentCallback);
}