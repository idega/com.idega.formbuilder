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
//--------------------------------

function deletePage(id) {
	pressedDeletePage = true;
	dwrmanager.deletePage(refreshMainApplication, id);
}

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

function loadPage(id) {
	var temp2 = pressedDeletePage;
	if(!temp2) {
		dwrmanager.loadPage(refreshMainApplicationW, id)
	}
	pressedDeletePage = false;
}

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
var pressedDeletePage = false;
/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);
		