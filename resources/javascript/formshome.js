var FORMBUILDER_PATH = "/workspace/forms/formbuilder/";
var FORMADMIN_PATH = "/workspace/forms/entries/";
var FORMPREVIEW_PATH = "/workspace/forms/preview/";
var FORMSHOME_PATH = "/workspace/forms/list/";
var TRANSITION_DURATION = 500;

var modalFormName = null;
var modalGoToDesigner = true;
var modalSelectedForm = null;
var SELECTED_PROCESS = null;
var SELECTED_TASK = null;

function loadTaskFormDocument(processName, processId, taskName, formId) {
	showLoadingMessage('Loading');
	var errorMessage = 'Error occured trying to load editing mode';
	FormDocument.loadTaskFormDocument(processId, taskName, formId, {
		callback: function(result) {
			handleActionRedirect(result, true, FORMBUILDER_PATH, null, errorMessage);
		},
		errorHandler: function() {
			closeAllLoadingMesssages();
			if (humanMsg) humanMsg.displayMsg(errorMessage);
		}
	});
	return false;
}
function handleActionRedirect(result, indicator, successPath1, successPath2, errorMessage) {
	if(result == true) {
		if(indicator == true) {
			window.location=successPath1;
		} else {
			window.location=successPath2;
		}
	} else {
		closeAllLoadingMesssages();
		if(humanMsg) humanMsg.displayMsg(errorMessage);
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
			handleActionRedirect(result, modalGoToDesigner, FORMBUILDER_PATH, FORMSHOME_PATH, 'Error creating new form document');
		}
	});
}
function createNewTaskForm() {
	FormDocument.createTaskFormDocument(modalFormName, SELECTED_PROCESS, SELECTED_TASK, {
		callback: function(result) {
			handleActionRedirect(result, modalGoToDesigner, FORMBUILDER_PATH, FORMSHOME_PATH, 'Error creating new form document');
		}
	});
}
function attachTaskForm() {
	FormDocument.attachFormDocumentToTask(SELECTED_PROCESS, SELECTED_TASK, modalSelectedForm, modalGoToDesigner,  {
		callback: function(result) {
			handleActionRedirect(result, modalGoToDesigner, FORMBUILDER_PATH, FORMSHOME_PATH, 'Error attaching a form to a task');
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
	$$("a.smoothbox").each(function(item) {
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
					}).setText( '');
					option.injectInside(select);
					for(var i = 0; i < resultList.length; i++) {
						var it = resultList[i];
						var option = new Element('option',{
							'value': it.id
						}).setText( it.value);
						option.injectInside(select);
					}
				}
			}
		}
	});
	$$("a.entriesButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocumentEntries(formId, {
				callback: function(result) {
					handleActionRedirect(result, true, FORMADMIN_PATH + '?formId=' + result, null, 'Error occured trying to load admin mode');
				}
			});
		});
	});
	$$("li.stateFullTab").each(function(item) {
		item.addEvent('click', function(e){
			var title = e.target.getProperty('title');
			Workspace.setActiveHomepageTab(title);
		});
	});
	$$('div.processItem').each(function(item) {
		item.setStyle('cursor', 'pointer');
		var select = item.getElement('div.processButtonList').getFirst();
		item.addEvent('click', function(e){
			if (!$(e.target).hasClass('processItem')) {
				return false;
			}
			var transition = new Fx.Style(item, 'height' ,{
				duration: TRANSITION_DURATION, onComplete: function() {
			}
			});
			if(item.getElement('.taskFormList').getStyle('display') == 'none') {
				var size = item.getElement('.taskFormList').getChildren().length;
				if (size > 0) {
					transition.start((size * 55) + 53);
					item.getElement('.taskFormList').setStyle('display', 'block');
				} else {
					var version = dwr.util.getValue(select);
					var processName = dwr.util.getValue(item.getElement('.formTitle'));
					loadProcessTasks(processName, version, item.getElement('.taskFormList'), item, transition);
				}
			} else {
				transition.start(35);
				item.getElement('.taskFormList').setStyle('display', 'none');
			}
		});
		select.addEvent('change', function(e) {
			var version = dwr.util.getValue(e.target.id);
			var processName = dwr.util.getValue(item.getElement('.formTitle'));
			var transition = new Fx.Style(item, 'height' ,{
				duration: TRANSITION_DURATION, onComplete: function() {
			}
			});
			loadProcessTasks(processName, version, item.getElement('.taskFormList'), item, transition);
		});
	});
	$$('a.transitionButton').each(function(item) {
		var container = item.getParent().getParent().getParent();
		var taskList = container.getElement('.taskFormList');
		container.getElements('li.procBtnClass').each(function(button) {
			button.setStyle('visibility', 'hidden');
		});
		item.addEvent('click', function(e){
			new Event(e).stop();
			var transition = new Fx.Style(item.getParent().getParent().getParent(), 'height' ,{
				duration: TRANSITION_DURATION, onComplete: function() {
					transitionButtons(container, item, true);
				}
			});
			if(taskList.getStyle('display') == 'none') {
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
	$('standalone').getElements('a.editButton').each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocument(formId, {
				callback: function(result) {
					handleActionRedirect(result, true, FORMBUILDER_PATH, null, 'Error occured trying to load editing mode');
				}
			});
		});
	});
	$$("a.tryButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocumentPreview(formId, {
				callback: function(result) {
					handleActionRedirect(result, true, FORMPREVIEW_PATH, null, 'Error occured trying to load preview mode');
				}
			});
		});
	});
	$$("a.codeButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			
			var formId = item.getProperty('formId');
			
			FormDocument.loadFormDocumentCode(formId, {
				callback: function(result) {
					handleActionRedirect(result, true, FORMBUILDER_PATH, null, 'Error occured trying to load code view mode');
				}
			});
		});
	});
	$$("a.deleteButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			var answer = confirm("Delete form?")
			if (answer) {
				showLoadingMessage('Deleting');
				
				var formId = item.getProperty('formId');
				
				FormDocument.deleteFormDocument(formId, {
					callback: function(result) {
						handleActionRedirect(result, true, FORMSHOME_PATH, null, 'Error trying to delete a form');
					}
				});
			}
		});
	});
	$$("a.deleteTFButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			var answer = confirm("Delete form?")
			if (answer) {
				showLoadingMessage('Deleting');
				
				var formId = item.getProperty('formId');
				
				FormDocument.deleteTaskFormDocument(formId, {
					callback: function(result) {
						handleActionRedirect(result, true, FORMSHOME_PATH, null, 'Error trying to delete a form');
					}
				});
			}
		});
	});
}
function loadProcessTasks(processName, version, node, container, transition) {
	showLoadingMessage('Loading');
	FormDocument.getProcessTasks(processName, version, {
		callback: function(component) {
		replaceNode(component.document, node, container);
		var tasks = component.taskCount;
		var forms = component.taskFormCount;
		container.getElement(".processSummaryText").getElement(".tasks").setText(tasks);
		container.getElement(".processSummaryText").getElement(".forms").setText(forms);
		transition.start((forms * 55) + 53);
		container.getElement('.taskFormList').setStyle('display', 'block');
		closeAllLoadingMessages();
	}
});
}

function transitionButtons(container, item, linkTarget) {
	if(item.hasClass('expandButton')) {
		item.removeClass('expandButton');
		item.addClass('collapseButton');
		item.setText( 'Collapse');
		
		container.getElement('.taskFormList').setStyle('display', 'block');
		
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
		container.getElement('.taskFormList').setStyle('display', 'none');
		item.removeClass('collapseButton');
		item.addClass('expandButton');
		item.setText( 'Expand');
	}
}