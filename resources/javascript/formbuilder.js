function selectFormField(selectedField) {
	document.forms['workspaceform1'].elements['selected_field_id'].value=selectedField.parentNode.id;
	document.getElementById('select_field_button').onclick();
}
function selectFormHeader() {
	document.getElementById('select_form_header_button').onclick();
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
function addEmptyOption() {
	alert('Adding new option');
	$('workspaceform1:addNewOption').click();
}
function createNewForm() {
	var name = document.forms['newFormDialogForm'].elements['formName'].value;
	if(name != '') {
		showLoadingMessage("Creating");
		dwrmanager.createNewForm(createdNewForm,name);
		closeMessage();
		//document.forms['workspaceform1'].elements['workspaceform1:createFormProxy'].click();
	}
}
function saveProperties() {
	showLoadingMessage("Saving");
	dwrmanager.saveChanges(deletedComponent);
}
function savedProperties() {
	closeLoadingMessage();
}
function switchDataSrc() {
	//alert('switching');
	document.forms['workspaceform1'].elements['workspaceform1:switchSrcProxy'].click();
}
function applyChanges() {
	$('workspaceform1:applyChangesProxy').click();
}
function deleteComponent(element) {
	pressedDelete = true;
	showLoadingMessage('Removing');
	var id = element.parentNode.id;
	dwrmanager.removeComponent(deletedComponent,id);
	document.forms['workspaceform1'].elements['workspaceform1:removeCompProxy'].click();
}
function deletedComponent() {
}
function editProperties(element) {
	var temp = pressedDelete;
	if(!temp) {
		showLoadingMessage('Retrieving');
		var id = element.id;
		dwrmanager.editComponentProperties(done,id);
	}
	pressedDelete = false;
}
function done() {
	$('workspaceform1:editCompProxy').click();
	//document.forms['workspaceform1'].elements['workspaceform1:editCompProxy'].click();
}
function createdNewForm(element) {
	$('workspaceform1:createFormProxy').click();
	//document.forms['workspaceform1'].elements['workspaceform1:createFormProxy'].click();
}
function removeComponent(element) {
	showLoadingMessage("Deleting");
	dwrmanager.removeComponent(removedComponent,element.parentNode.id);
}
function removedComponent(id) {
	if(id != '') {
		var dropBox = $('dropBox');
		var element = document.getElementById(id);
		dropBox.removeChild(element);
	}
	closeLoadingMessage();
}
function clearDesignSpace() {
	var dropBox = $('dropBox');
	var child = null;
	for(var i=dropBox.childNodes.length-1;i>-1;i--) {
		child = dropBox.childNodes[i];
		if(child.getAttribute('class') == 'formElement') {
			dropBox.removeChild(child);
		}
	}
}
function getDecoyNode() {
	var node = document.createNode('div');
	return node;
}
function switchFacets(noForm,heading,empty) {
	switchVisibility('noFormNotice',noForm);
	switchVisibility('formHeading',heading);
	switchVisibility('emptyForm',empty);
}
function switchVisibility(id,makeVisible) {
	var component = $(id);
	if(component) {
		if(component.style) {
			if(makeVisible == true) {
				component.style.display = 'block';
			} else {
				component.style.display = 'none';
			}
		} else {
			if(makeVisible == true) {
				component.display = 'block';
			} else {
				component.display = 'none';
			}
		}
	}
}
function decoy() {
	closeLoadingMessage();
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
function switchSelectedForm() {
	showLoadingMessage("Loading");
	//return myFaces_showPanelTab(0,'workspaceform1:options_tabbed_pane_indexSubmit','workspaceform1:tab01_headerCell','workspaceform1:tab01',panelTabbedPane_5Fworkspaceform1_3Aoptions_5Ftabbed_5Fpane_5FHeadersIDs,panelTabbedPane_5Fworkspaceform1_3Aoptions_5Ftabbed_5Fpane_5FIDs,'activeTab','inactiveTab','activeSub','inactiveSub');
}
function formSwitched() {
	closeLoadingMessage();
}
var pressedDelete = false;
/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);
		