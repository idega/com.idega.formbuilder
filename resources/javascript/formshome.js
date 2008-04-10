var FORMBUILDER_PATH = "/workspace/forms/formbuilder/";
var FORMADMIN_PATH = "/workspace/forms/formadmin/";
var FORMSHOME_PATH = "/workspace/forms/";
var TRANSITION_DURATION = 500;

var modalFormName = null;
var modalGoToDesigner = true;
var modalSelectedForm = null;
var SELECTED_PROCESS = null;
var SELECTED_TASK = null;

function setHrefToVoidFunction(element) {
	element.setProperty('href', 'javascript:void(0)');
}
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
function createNewTaskForm() {
	FormDocument.createTaskFormDocument(modalFormName, SELECTED_PROCESS, SELECTED_TASK, {
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
function attachTaskForm() {
	FormDocument.attachFormDocumentToTask(SELECTED_PROCESS, SELECTED_TASK, modalSelectedForm, modalGoToDesigner,  {
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
function registerFormsHomeActions() {
	var widthForTabs = Math.round(window.getWidth() * 0.93);
	var heightForTabs = Math.round(window.getHeight() * 0.7);
	Workspace.getActiveHomepageTab({
		callback: function(result) {
			if(result == null) {
				result = 'first';
			}
			var tabs = new mootabs('formListContainer', {width: widthForTabs + 'px', height: (heightForTabs - 50) + 'px', changeTransition: 'none', activateOnLoad:	result});
		}
	});
	var newFormButton = $('newFormButton');
	if(newFormButton != null) {
		newFormButton.setProperty('Title', 'Create new form');
		newFormButton.setProperty('href', '#TB_inline?height=100&width=300&inlineId=newFormDialog');
	}
	
	var newFormInput = $('taskFormNameInput');
	newFormInput.addEvent('keydown', function(e) {
		var event = new Event(e);
		if(isEnterEvent(e)) {
			createNewForm(event.target.value);
		}
	});
	$ES("a.smoothbox").each(function(item) {
		item.addEvent('click', function(e){
			var relAttribute = item.getProperty('rel');
			if(relAttribute != null) {
				var values = relAttribute.split('_')
				SELECTED_TASK = values[1];
				SELECTED_PROCESS = values[0];
			}
		});
	});
	FormDocument.getStandaloneForms({
		callback: function(resultList) {
			if(resultList != null) {
				var select = $('formSelector');
				if(select != null) {
					var option = new Element('option',{
						'value': ''
					}).setText('');
					option.injectInside(select);
					for(var i = 0; i < resultList.length; i++) {
						var it = resultList[i];
						var option = new Element('option',{
							'value': it.id
						}).setText(it.value);
						option.injectInside(select);
					}
				}
			}
		}
	});
	$ES("a.entriesButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocumentEntries(formId, {
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
	$ES("li.stateFullTab").each(function(item) {
		item.addEvent('click', function(e){
			var title = e.target.getProperty('title');
			Workspace.setActiveHomepageTab(title);
		});
	});
	$ES('div.processItem').each(function(item) {
		item.setStyle('cursor', 'pointer');
		item.addEvent('click', function(e){
			var link = item.getElement('ul.processButtonList').getLast().getFirst();
			var transition = new Fx.Style(item, 'height' ,{duration: TRANSITION_DURATION, onComplete: function() {transitionButtons(item, link, false);}});
			if(item.getStyle('height').toInt() == 35) {
				var listSize = item.getLast().getChildren().length;
				transition.start((listSize * 55) + 53);
			} else {
				item.getElements('li.procBtnClass').each(function(button) {
					button.setStyle('visibility', 'hidden');
				});
				transition.start(35);
			}
		});
	});
	$ES('a.transitionButton').each(function(item) {
		var container = item.getParent().getParent().getParent();
		container.getElements('li.procBtnClass').each(function(button) {
			button.setStyle('visibility', 'hidden');
		});
		item.addEvent('click', function(e){
			new Event(e).stop();
			var transition = new Fx.Style(item.getParent().getParent().getParent(), 'height' ,{duration: TRANSITION_DURATION, onComplete: function() {transitionButtons(container, item, true);}});
			if(container.getStyle('height').toInt() == 35) {
				var listSize = container.getLast().getChildren().length;
				transition.start((listSize * 55) + 53);
			} else {
					container.getElements('li.procBtnClass').each(function(button) {
					button.setStyle('visibility', 'hidden');
				});
				transition.start(35);
			}
		});
	});
	$ES("a.editButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocument(formId, {
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
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocumentPreview(formId, {
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
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocumentCode(formId, {
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
	$ES("a.deleteButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			var answer = confirm("Delete form?")
			if (answer) {
				showLoadingMessage('Deleting');
				
				var formId = item.getProperty('formId');
				
				FormDocument.deleteFormDocument(formId, {
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
				
				var formId = item.getProperty('formId');
				
				FormDocument.deleteTaskFormDocument(formId, {
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
function transitionButtons(container, item, linkTarget) {
	if(item.hasClass('expandButton')) {
		item.removeClass('expandButton');
		item.addClass('collapseButton');
		item.setText('Collapse');
		
		if(linkTarget) {
			container.getElements('li.procBtnClass').each(function(button) {
				button.setStyle('visibility', 'visible');
			});
		} else {
			container.getElements('ul.processButtonList').getElements('li.procBtnClass').each(function(button) {
				button.setStyle('visibility', 'visible');
			});
		}
	} else {
		item.removeClass('collapseButton');
		item.addClass('expandButton');
		item.setText('Expand');
	}
}