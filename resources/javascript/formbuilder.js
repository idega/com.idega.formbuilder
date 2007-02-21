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
function loadFormInfo() {
	FormDocument.getFormDocumentInfo(placeFormInfo);
}
function placeFormInfo(parameter) {
	if(parameter != null) {
		var formTitleTxt = $('formTitle');
		if(formTitleTxt != null) {
			formTitleTxt.value = parameter.title;
		}
		var hasPreviewChk = $('previewScreen');
		if(hasPreviewChk != null) {
			hasPreviewChk.value = parameter.hasPreview;
		}
		var thankYouTitleTxt = $('thankYouTitle');
		if(thankYouTitleTxt != null) {
			thankYouTitleTxt.value = parameter.thankYouTitle;
		}
		var thankYouTextTxt = $('thankYouText');
		if(thankYouTextTxt != null) {
			thankYouTextTxt.value = parameter.thankYouText;
		}
		STATIC_ACCORDEON.showTabByIndex(2, true);
	}
}
function saveFormTitle(parameter) {
	if(parameter != null) {
		FormDocument.setFormTitle(parameter, refreshViewPanel);
	}
}
function saveThankYouTitle(parameter) {
	if(parameter != null) {
		FormDocument.setThankYouTitle(parameter, refreshViewPanel);
	}
}
function saveThankYouText(parameter) {
	if(parameter != null) {
		FormDocument.setThankYouText(parameter, refreshViewPanel);
	}
}
function saveHasPreview(parameter) {
	if(parameter != null) {
		FormDocument.setHasPreview(parameter, refreshViewPanel);
	}
}
function loadPageInfo(parameter) {
	FormPage.getFormPageInfo(parameter, placePageInfo);
}
function placePageInfo(parameter) {
	if(parameter != null) {
		var pageTitleTxt = $('pageTitle');
		if(pageTitleTxt != null) {
			pageTitleTxt.value = parameter.pageTitle;
		}
		STATIC_ACCORDEON.showTabByIndex(3, true);
	}
}
function loadComponentInfo(parameter) {
	FormComponent.getFormComponentInfo(parameter, placeComponentInfo);
}
function placeComponentInfo(parameter) {
	if(parameter != null) {
		var labelTxt = $('propertyTitle');
		if(labelTxt != null) {
			labelTxt.value = parameter.label;
		}
		var requiredChk = $('propertyRequired');
		if(requiredChk != null) {
			requiredChk.value = parameter.required;
		}
		var errorTxt = $('propertyErrorMessage');
		if(errorTxt != null) {
			errorTxt.value = parameter.errorMessage;
		}
		var helpTxt = $('propertyHelpText');
		if(helpTxt != null) {
			helpTxt.value = parameter.helpMessage;
		}
		if(parameter.complex == true) {
			$('advPropsPanel1').setAttribute('style', 'visibility: visible');
			if(parameter.local == true) {
				$('localSrcDiv').setAttribute('style', 'visibility: visible');
			} else {
				$('advPropsPanel2').setAttribute('style', 'visibility: visible');
				$('localSrcDiv').setAttribute('style', 'display: none');
			}
		} else {
			$('advPropsPanel1').setAttribute('style', 'display: none');
			$('advPropsPanel2').setAttribute('style', 'display: none');
			$('localSrcDiv').setAttribute('style', 'display: none');
		}
		STATIC_ACCORDEON.showTabByIndex(1, true);
	}
}
function saveLabel(parameter) {
	if(parameter != null) {
		FormComponent.setLabel(parameter, refreshViewPanel);
	}
}
function saveRequired(parameter) {
	if(parameter != null) {
		FormComponent.setRequired(parameter, refreshViewPanel);
	}
}
function saveErrorMessage(parameter) {
	if(parameter != null) {
		FormComponent.setErrorMessage(parameter, refreshViewPanel);
	}
}
function saveHelpMessage(parameter) {
	alert('Not implemented');
	/*if(parameter != null) {
		FormComponent.setHelpMessage(parameter, refreshViewPanel);
	}*/
}
function saveFormDocument() {
	showLoadingMessage('Saving');
	FormDocument.save(doNothing);
}
function doNothing(parameter) {
	closeLoadingMessage();
}
function switchDataSource() {
	FormComponent.switchDataSource(placeDataSource);
}
function placeDataSource(parameter) {
	if(parameter == true) {
		$('localSrcDiv').setAttribute('style', 'visibility: visible');
		$('advPropsPanel2').setAttribute('style', 'display: none');
	} else {
		$('localSrcDiv').setAttribute('style', 'display: none');
		$('advPropsPanel2').setAttribute('style', 'visibility: visible');
	}
}
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
		dwrmanager.createNewFormDocument(name, refreshViewPanel);
	}
}
function refreshViewPanel(parameter) {
	$('workspaceform1:refreshViewPanel').click();
}
function refreshMainApplication() {
	$('workspaceform1:loadPageFunction').click();
}
//--------------------------------

function deletePage(id) {
	pressedDeletePage = true;
	dwrmanager.deletePage(id, refreshMainApplication);
}

//Handles the deletion of a form component created with JSF
function removeComponent(parameter) {
	alert(parameter);
	/*pressedDelete = true;
	var node = element.parentNode;
	if(node != null) {
		FormComponent.removeComponent(node.id, removeComponentNode);
	}*/
}
/*function deleteComponentJSF(element) {
	pressedDelete = true;
	var node = element.parentNode;
	if(node) {
		dwrmanager.removeComponent(node.id, removeComponent);
	}
}*/
function removeComponentNode(parameter) {
	var node = $(parameter);
	if(node) {
		node.parentNode.removeChild(node);
	}
}
//----------------------------------------

//Handles the deletion of a form component created with Javascript
function deleteComponentJS(element) {
	showLoadingMessage('Removing');
	dwrmanager.removeComponent(element.parentNode.id, deletedComponentJS);
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
		dwrmanager.loadPage(id, refreshMainApplicationW)
	}
	pressedDeletePage = false;
}

//Handles the retrieval of component properties on mouse click
function editProperties(id) {
	var temp = pressedDelete;
	if(!temp) {
		showLoadingMessage('Fetching');
		//var id = element.id;
		dwrmanager.getComponentProperties(id, gotComponentProperties);
	}
	pressedDelete = false;
}
function gotComponentProperties() {
	$('workspaceform1:getCompProperties').click();
}
//-------------------------------------------


/*function switchDataSource() {
	dwrmanager.switchDataSource(switchedDataSrc);
}
function switchedDataSrc() {
	$('workspaceform1:srcSwitcher').click();
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
var pressedDeletePage = false;
/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);
//$('panel1Content').style.overflow = 'none';
//$('panel2Content').style.overflow = 'none';
//$('panel3Content').style.overflow = 'none';
		