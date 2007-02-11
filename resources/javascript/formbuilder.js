/*function selectFormField(selectedField) {
	document.forms['workspaceform1'].elements['selected_field_id'].value=selectedField.parentNode.id;
	document.getElementById('select_field_button').onclick();
}
function selectFormHeader() {
	document.getElementById('select_form_header_button').onclick();
}*/

//Handles the display of a modal dialog window
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
//---------------------------------------------

//Handles the closing of the loading indicator
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
//--------------------------------------------
/*function addEmptyOption() {
	$('workspaceform1:addNewOption').click();
}
function removeOption() {
	alert('Removing option');
	$('workspaceform1:removeOption').click();
}
*/

//Handles the creation of a new form
function createNewForm() {
	var name = document.forms['newFormDialogForm'].elements['formName'].value;
	if(name != '') {
		closeMessage();
		showLoadingMessage("Creating");
		/*createFormDocument(name);*/
		dwrmanager.createNewFormDocument(refreshMainApplicationW, name);
	}
}
function refreshMainApplicationW() {
	$('workspaceform1:createFormProxy').click();
}
function refreshMainApplication() {
	$('workspaceform1:loadPageFunction').click();
}
/*function createdNewForm(element) {
	$('workspaceform1:createFormProxy').click();
}*/
//--------------------------------


/*function saveProperties() {
	showLoadingMessage("Saving");
	dwrmanager.saveChanges(deletedComponent);
}
function savedProperties() {
	closeLoadingMessage();
}
function switchDataSrc() {
	document.forms['workspaceform1'].elements['workspaceform1:switchSrcProxy'].click();
}
function applyChanges() {
	$('workspaceform1:applyChangesProxy').click();
}*/

//Handles the deletion of a form component created with JSF
function deleteComponentJSF(element) {
	pressedDelete = true;
	showLoadingMessage('Removing');
	var id = element.parentNode.id;
	dwrmanager.removeComponent(deletedComponentJSF,id);
}
function deletedComponentJSF() {
	$('workspaceform1:removeCompProxy').click();
}
//----------------------------------------

//Handles the deletion of a form component created with Javascript
function deleteComponentJS(element) {
	showLoadingMessage('Removing');
	dwrmanager.removeComponent(deletedComponentJS,element.parentNode.id);
}
function deletedComponentJS(id) {
	if(id != '') {
		var dropBox = $('dropBoxinner');
		if(dropBox) {
			var element = document.getElementById(id);
			if(element) {
				dropBox.removeChild(element);
				if(dropBox.childNodes.length == 0) {
					var empty = $('workspaceform1:emptyForm');
					if(empty) {
						if(empty.style) {
							empty.style.display = 'block';
						} else {
							empty.display = 'block';
						}
					}
				}
			}
		}
	}
	closeLoadingMessage();
}
//----------------------------------------

//Handles the retrieval of component properties on mouse click
function editProperties(id) {
	var temp = pressedDelete;
	if(!temp) {
		showLoadingMessage('Fetching');
		//var id = element.id;
		dwrmanager.getComponentProperties(gotComponentProperties,id);
	}
	pressedDelete = false;
}
function gotComponentProperties() {
	$('workspaceform1:getCompProperties').click();
}
//-------------------------------------------


function switchDataSource() {
	dwrmanager.switchDataSource(switchedDataSrc);
}
function switchedDataSrc() {
	$('workspaceform1:srcSwitcher').click();
}

/*function clearDesignSpace() {
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
}*/
function decoy() {
	closeLoadingMessage();
}

function changeMenu(id) {
	dwrmanager.changeMenu(changedMenu,id);
}
function changedMenu() {
	$('workspaceform1:changeMenuProxy').click();
}

function switchSelectedForm() {
	showLoadingMessage("Loading");
}
function formSwitched() {
	closeLoadingMessage();
}
var pressedDelete = false;
/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);
/*A4J.AJAX.onError = customFunction(req,status,message) {
	alert(message);
};
A4J.AJAX.onExpired = func2(loc,expiredMsg){
	alert(expiredMsg);
};*/
		