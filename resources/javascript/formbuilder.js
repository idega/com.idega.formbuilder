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
var currentElement = null;
function handleComponentDrag(element) {
	var type = element.id;
	FormComponent.createComponent(type, receiveComponent);
}
function handleButtonDrag(element) {
	if(element != null) {
		var type = element.id;
		FormComponent.addButton(type, placeNewButton);
	}
}
function placeNewButton(parameter) {
	
}
function receiveComponent(parameter) {
	currentElement = createTreeNode(parameter.documentElement);
}
function createTreeNode(element) {
	if(element.nodeName == '#text') {
		var textNode = document.createTextNode(element.nodeValue);
		return textNode;
	} else {
		var result = document.createElement(element.nodeName);
		if(element.nodeName == 'input' || element.nodeName == 'textarea' || element.nodeName == 'select') {
			result.setAttribute('disabled','true');
		}
		for(var i=0;i<element.attributes.length;i++) {
			result.setAttribute(element.attributes[i].nodeName,element.attributes[i].nodeValue);
		}
		for(var j=0;j<element.childNodes.length;j++) {
			result.appendChild(createTreeNode(element.childNodes[j]));
		}
		return result;
	}
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
		FormDocument.setThankYouTitle(parameter, placeThankYouTitle);
	}
}
function placeThankYouTitle(parameter) {
	var container = $('pagesPanelSpecial');
	if(container != null) {
		var node = $(parameter.pageId + '_P_page');
		if(node != null) {
			var parent = node.childNodes[1];
			var textNode = parent.childNodes[0];
			var newTextNode = document.createTextNode(parameter.pageTitle);
			parent.replaceChild(newTextNode, textNode);
		}
	}
}
function saveThankYouText(parameter) {
	if(parameter != null) {
		FormDocument.setThankYouText(parameter, nothing);
	}
}
function saveHasPreview(parameter) {
	if(parameter != null) {
		FormDocument.togglePreviewPage(parameter.checked, placePreviewPage);
	}
}
function placePreviewPage(parameter) {
	var container = $('pagesPanelSpecial');
	if(container != null) {
		if(parameter.pageTitle != null) {
		
			var page = document.createElement('div');
			page.setAttribute('id', parameter.pageId + '_page');
			page.setAttribute('class', 'formPageIcon');
			page.setAttribute('styleClass', 'formPageIcon');
			page.setAttribute('style', 'position: relative');
			
			var icon = document.createElement('img');
			icon.setAttribute('id', parameter.pageId + '_pi');
			icon.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png';
			icon.setAttribute('onclick', 'loadPageInfo(this.id)');
			icon.style.display = 'block';
			
			var label = document.createElement('span');
			label.style.display = 'block';
			
			var text = document.createTextNode(parameter.pageTitle);
			
			label.appendChild(text);
			
			var db = document.createElement('img');
			db.id = parameter.pageId + '_db';
			db.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png';
			db.setAttribute('onclick', 'deletePage(this.id)');
			db.setAttribute('class', 'speedButton');
			
			page.appendChild(icon);
			page.appendChild(label);
			page.appendChild(db);
			
			var child = container.childNodes[0];
			//container.appendChild(page);
			container.insertBefore(page, child);
		} else {
			var node = $(parameter.pageId + '_page');
			container.removeChild(node);
		}
		//$('workspaceform1:refreshViewPanel').click();
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
		STATIC_ACCORDEON.showTabByIndex(3, false);
		$('workspaceform1:refreshViewPanel').click();
	}
}
function setupPagesDragAndDrop(value1, value2) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,ghosting:false,onUpdate:rearrangePages,scroll:value1,constraint:false});
}
function rearrangePages() {
	alert('Dragging');
	var componentIDs = Sortable.serialize('pagesPanel',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormDocument.updatePagesList(nothing,componentIDs,idPrefix,delimiter);
}
function savePageTitle(parameter) {
	if(parameter != null) {
		FormPage.setTitle(parameter, refreshPageTitle);
	}
}
function refreshPageTitle() {
	alert('Not implemented');
}
function setupComponentDragAndDrop(value1,value2) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1 + 'inner',{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangeComponents,scroll:value1,constraint:false});
	Droppables.add(value1,{onDrop:handleComponentDrop});
}
function handleComponentDrop(element,container) {
	var empty = $('emptyForm');
	if(empty != null) {
		if(empty.style) {
			empty.style.display = 'none';
		} else {
			empty.display = 'none';
		}
	}
	if(currentElement != null) {
		$(container.id + 'inner').appendChild(currentElement);
		currentElement = null;
		Sortable.create(container.id + 'inner',{dropOnEmpty:true,tag:'div',only:'formElement',onUpdate:rearrangeComponents,scroll:container.id,constraint:false});
		Droppables.add(container.id,{onDrop:handleComponentDrop});
	}
}
function rearrangeComponents() {
	var componentIDs = Sortable.serialize('dropBoxinner',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormPage.updateComponentList(componentIDs,idPrefix,delimiter,nothing);
	pressedDelete = true;
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
function saveComponentLabel(parameter) {
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
function nothing(parameter) {}
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
function createNewPage() {
	FormPage.createNewPage(placeNewPage);
}
function placeNewPage(parameter) {
	var container = $('pagesPanel');
	if(container != null) {
		var page = document.createElement('div');
		page.setAttribute('id', parameter + '_page');
		page.setAttribute('class', 'formPageIcon');
		page.setAttribute('styleClass', 'formPageIcon');
		page.setAttribute('style', 'position: relative');
		
		var icon = document.createElement('img');
		icon.setAttribute('id', parameter + '_pi');
		icon.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png';
		icon.setAttribute('onclick', 'loadPageInfo(this.id)');
		icon.style.display = 'block';
		
		var label = document.createElement('span');
		label.style.display = 'block';
		
		var text = document.createTextNode('Page');
		
		label.appendChild(text);
		
		var db = document.createElement('img');
		db.id = parameter + '_db';
		db.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png';
		db.setAttribute('onclick', 'deletePage(this.id)');
		db.setAttribute('class', 'speedButton');
		
		page.appendChild(icon);
		page.appendChild(label);
		page.appendChild(db);
		
		container.appendChild(page);
		$('workspaceform1:refreshViewPanel').click();
	}
}
function deletePage(parameter) {
	//pressedDeletePage = true;
	FormPage.removePage(parameter,handleDeletedForm);
}
function handleDeletedForm(parameter) {
	var container = $('pagesPanel');
	if(container != null) {
		var element = $(parameter);
		if(element != null) {
			container.removeChild(element.parentNode);
		}
	}
	$('workspaceform1:refreshViewPanel').click();
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
function refreshPagesPanel(parameter) {
	$('workspaceform1:refreshPagesPanel').click();
}
function refreshMainApplication() {
	$('workspaceform1:loadPageFunction').click();
}
//--------------------------------



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
		