var FORM_NAME_PREFIX = 'xformsProcess:';
var JBPM_ACTOR_NAME = 'jbpm_actor_name';
var JBPM_VIEW_NAME = 'jbpm_view_name';
var JBPM_ACTOR_TYPE = 'jbpm_actor_type';
var JBPM_ACTOR_ID = 'jbpm_actor_id';

function loadProcessTaskList(value) {
	if(value != null && value != '') {
		ActorManager.getTaskList(value, {
			callback: function(result) {
				if(result != null) {
					DWRUtil.removeAllOptions(FORM_NAME_PREFIX + 'taskSelector');
					DWRUtil.addOptions(FORM_NAME_PREFIX + 'taskSelector', result, 'id', 'value');
				}
			}
		});
	}
}
function loadTaskProperties(value) {
	if(value != null) {
		var processSelector = $(FORM_NAME_PREFIX + 'processSelector');
		if(processSelector != null) {
			XFormsProcess.getTaskProperties(processSelector.value, value, {
				callback: function(result) {
					if(result != null) {
						var taskForm = result[0];
						if(taskForm != null && taskForm.id == JBPM_VIEW_NAME) {
							var taskFormNode = $(FORM_NAME_PREFIX + 'taskForm');
							taskFormNode.value = taskForm.value;
							taskFormNode.disabled = true;
						}
						var actorType = result[2];
						var actorName = result[3];
						if(actorName != null && actorName.id == JBPM_ACTOR_NAME && actorType != null && actorType.id == JBPM_ACTOR_TYPE) {
							var taskGroupNode = $(FORM_NAME_PREFIX + 'actorNameLabel');
							taskGroupNode.innerHTML = '(' + actorType.value + ') ' + actorName.value;
						}
					}
				}
			});
		}
	}
}
function assignViewToTask(value) {
	if(value != null && value != '') {
		var processSelector = $(FORM_NAME_PREFIX + 'processSelector');
		var taskSelector = $(FORM_NAME_PREFIX + 'taskSelector');
		if(processSelector != null && taskSelector != null) {
			showLoadingMessage('Setting');
			XFormsProcess.assignTaskForm(processSelector.value, taskSelector.value, value, {
				callback: function(result) {
					if(result != null) {
						var taskForm = result[0];
						if(taskForm != null && taskForm.id == JBPM_VIEW_NAME) {
							var taskFormNode = $(FORM_NAME_PREFIX + 'taskForm');
							taskFormNode.disabled = false;
							taskFormNode.value = taskForm.value;
						}
						var taskActor = result[1];
						if(taskGroup != null && taskGroup.id == JBPM_GROUP_NAME) {
							var taskGroupNode = $(FORM_NAME_PREFIX + 'taskGroup');
							taskGroupNode.disabled = false;
						}
					}
					closeLoadingMessage();
				}
			});
		}
	}
}
function assignUserGroupToTask(value) {
	if(value != null && value != '') {
		var processSelector = $(FORM_NAME_PREFIX + 'processSelector');
		var taskSelector = $(FORM_NAME_PREFIX + 'taskSelector');
		if(processSelector != null && taskSelector != null) {
			showLoadingMessage('Setting');
			/*XFormsProcess.assignTaskForm(processSelector.value, taskSelector.value, value, {
				callback: function(result) {
					if(result != null) {
						var taskForm = result[0];
						if(taskForm != null && taskForm.id == JBPM_VIEW_NAME) {
							var taskFormNode = $(FORM_NAME_PREFIX + 'taskForm');
							taskFormNode.disabled = false;
							taskFormNode.value = taskForm.value;
						}
						var taskGroup = result[1];
						if(taskGroup != null && taskGroup.id == JBPM_GROUP_NAME) {
							var taskGroupNode = $(FORM_NAME_PREFIX + 'taskGroup');
							taskGroupNode.disabled = false;
						}
					}
					closeLoadingMessage();
				}
			});*/
		}
	}
}
function setActorType(value) {
	if(value != null) {
		ActorManager.getActorsByType(value, true, {
			callback: function(result) {
				if(result != null) {
					DWRUtil.removeAllOptions(FORM_NAME_PREFIX + 'actorSelector');
					DWRUtil.addOptions(FORM_NAME_PREFIX + 'actorSelector', result, 'id', 'value');
				}
			}
		});
	}
}
function enableFormList() {
	var taskFormNode = $(FORM_NAME_PREFIX + 'taskForm');
	if(taskFormNode != null) {
		taskFormNode.disabled = false;
	}
}