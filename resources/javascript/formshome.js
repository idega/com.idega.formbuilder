var FORMBUILDER_PATH = "/workspace/forms/formbuilder/";
var FORMADMIN_PATH = "/workspace/forms/formadmin/";
var FORMSHOME_PATH = "/workspace/forms/";
var SHOW_ELEMENT_TRANSITION_DURATION = 250;
var SET_DISPLAY_PROPERTY_ID = 0;

function loadTaskFormDocument(processName, processId, taskName, formId) {
	showLoadingMessage('Loading');
	FormDocument.loadTaskFormDocument(processId, taskName, formId, {
		callback: function(result) {
			if(result == true) {
				window.location=FORMBUILDER_PATH;
			} else {
				alert('Error occured trying to load editing mode');
			}
		}
	});
	closeLoadingMessage();
	return false;
}
function createNewForm(formName) {
	if(formName.length < 1) {
		return false;
	}
		
	showLoadingMessage('Creating');
	FormDocument.createFormDocument(formName, {
		callback: function(result) {
			if(result != null) {
				window.location=FORMBUILDER_PATH;
			}
		}
	});
}
function resetAddTaskForm(event) {
	new Event(event).stop();
	var target = event.target;
	var divBox = target.parentNode;
	var targetId = divBox.id;
	var parent = divBox.parentNode;
	var tokens = targetId.split('_');
	FormDocument.getRenderedAddTaskFormComponent(tokens[1], null, null, true, {
		callback: function(resultDOM) {
			replaceNode(resultDOM, divBox, parent);
		}
	});
}
function reloadAddTaskForm2(event) {
	new Event(event).stop();
	var target = event.target;
	var divBox = target.parentNode;
	var targetId = divBox.id;
	var parent = divBox.parentNode;
	var tokens = targetId.split('_');
	if(tokens[3] == '2') {
		FormDocument.getRenderedAddTaskFormComponent(tokens[1], target.value, null, false, {
			callback: function(resultDOM) {
				replaceNode(resultDOM, divBox, parent);
				$('newTF_' + tokens[1] + '_input').focus();
				$('newTF_' + tokens[1] + '_input').addEvent('keypress', function(e){
					if(isEnterEvent(e)) {
						new Event(e).stop();
						var target = e.target;
						var divBox = target.parentNode;
						var targetId = divBox.id;
						var parent = divBox.parentNode;
						var tokens = targetId.split('_');
						if(tokens[3] == '3') {
							FormDocument.createTaskFormDocument(target.value, {
								callback: function(result) {
									window.location=FORMBUILDER_PATH;
								}
							});
						}
					}
				});
			}
		});
	}
}
function reloadAddTaskForm1(event) {
	var target = event.target;
	var divBox = target.parentNode;
	var targetId = divBox.id;
	var parent = divBox.parentNode;
	var tokens = targetId.split('_');
	if(tokens[3] == '1') {
		FormDocument.getRenderedAddTaskFormComponent(tokens[1], null, null, false, {
			callback: function(resultDOM) {
				replaceNode(resultDOM, divBox, parent);
				$('newTF_' + tokens[1] + '_chooser').focus();
			}
		});
	}
}
function setHrefToVoidFunction(element) {
	element.setProperty('href', 'javascript:void(0)');
}
function registerFormsHomeActions() {
	var widthForTabs = Math.round(window.getWidth() * 0.93);
	var heightForTabs = Math.round(window.getHeight() * 0.7);
	var tabs = new mootabs('formListContainer', {width: widthForTabs + 'px', height: (heightForTabs - 50) + 'px', changeTransition: 'none'});
	var newFormButton = $('newFormButton');
	setHrefToVoidFunction(newFormButton);
	newFormButton.addEvent('click', function() {
		showNewFormDialog('newFormDialog', 'newFormButton', "New form", newFormButton.getLeft());
	});
	var newFormInput = $('newFormDialogInput');
	newFormInput.addEvent('keydown', function(e) {
		var event = new Event(e);
		if(isEnterEvent(e)) {
			createNewForm(event.target.value);
		}
	});
	$('createFormBtn').addEvent('click', function(e) {
		createNewForm(newFormInput.value);
	});
	/*$ES(".smoothbox").each(function(button) {
		button.setProperty('href', '#TB_inline?height=50&width=300&inlineId=newFormDialog');
		button.setProperty('title', 'New form');
	});*/
	$ES("a.entriesButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			FormDocument.loadFormDocumentEntries(item.id, {
				callback: function(result) {
					if(result == true) {
						window.location=FORMADMIN_PATH;
					} else {
						alert('Error occured trying to load admin mode');
						closeLoadingMessage();
					}
				}
			});
		});
	});
	$ES("a.editButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			FormDocument.loadFormDocument(item.id, {
				callback: function(result) {
					if(result == true) {
						window.location=FORMBUILDER_PATH;
					} else {
						alert('Error occured trying to load editing mode');
						closeLoadingMessage();
					}
				}
			});
		});
	});
	$ES("a.tryButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			FormDocument.loadFormDocumentPreview(item.id, {
				callback: function(result) {
					if(result == true) {
						window.location=FORMBUILDER_PATH;
					} else {
						alert('Error occured trying to load preview mode');
						closeLoadingMessage();
					}
				}
			});
		});
	});
	$ES("a.codeButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			FormDocument.loadFormDocumentCode(item.id, {
				callback: function(result) {
					if(result == true) {
						window.location=FORMBUILDER_PATH;
					} else {
						alert('Error occured trying to load code view mode');
						closeLoadingMessage();
					}
				}
			});
		});
	});
	$ES("a.duplicateButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			//showLoadingMessage('Loading');
			alert('Duplicate disabled');
		});
	});
	$ES("a.deleteButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			var answer = confirm("Delete form?")
			if (answer) {
				showLoadingMessage('Deleting');
				FormDocument.deleteFormDocument(item.id, {
					callback: function(result) {
						if(result == true) {
							window.location = FORMSHOME_PATH;
						} else {
							alert('Error trying to delete');
							closeLoadingMessage();
						}
					}
				});
			}
		});
	});
	$ES("a.deleteTFButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			var answer = confirm("Delete form?")
			if (answer) {
				showLoadingMessage('Deleting');
				FormDocument.deleteTaskFormDocument(item.id, {
					callback: function(result) {
						if(result == true) {
							window.location = FORMSHOME_PATH;
						} else {
							alert('Error trying to delete');
							closeLoadingMessage();
						}
					}
				});
			}
		});
	});
}
function showNewFormDialog(containerId, buttonId, buttonText, positionFromLeft) {
	var container = $(containerId);
	if (container == null) {
		return false;
	}
	
	var containerOpen = buttonText != $(buttonId).getText();
	if (containerOpen) {
		setButtonText(buttonId, buttonText);
		closeNewPage(container);
	}
	else {
		container.setStyle('left', positionFromLeft-190);
		setButtonText(buttonId, "Close");
		var showNewPage = new Fx.Style(container, 'opacity', {duration: SHOW_ELEMENT_TRANSITION_DURATION});
		showNewPage.start(0, 1);
		SET_DISPLAY_PROPERTY_ID = window.setTimeout("setDisplayPropertyToElement('"+container.id+"', 'block', null)", SHOW_ELEMENT_TRANSITION_DURATION);
	}
}
function setButtonText(id, text) {
	var button = $(id);
	if (button != null) {
		button.setText(text);
	}
}
function closeNewPage(container) {
	if (container != null) {
		var hideNewPage = new Fx.Style(container, 'opacity', {duration: SHOW_ELEMENT_TRANSITION_DURATION});
		hideNewPage.start(1, 0);
		SET_DISPLAY_PROPERTY_ID = window.setTimeout("setDisplayPropertyToElement('"+container.id+"', 'none', null)", SHOW_ELEMENT_TRANSITION_DURATION);
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