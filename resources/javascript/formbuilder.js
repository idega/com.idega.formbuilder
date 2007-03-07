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

var currentButton = null;
var currentElement = null;
var pressedComponentDelete = false;
var pressedButtonDelete = false;
var pressedPageDelete = false;
var draggingButton = false;
var draggingComponent = false;
var draggingPage = false;

function savePropertyOnEnter(parameter,attribute,e) {
	if (!e) e = window.event;
	if (!e) return true;
	var key = (typeof e.keyCode != 'undefined' ? e.keyCode : e.charCode);
	if(key == '13') {
		switch(attribute) {
		  	case 'compTitle':
		  		saveComponentLabel(parameter);
		    	break;
		  	case 'compErr':
		    	saveErrorMessage(parameter);
		    	break;
		 	case 'compHelp':
		 		saveHelpMessage(parameter);
		 		break;
		 	case 'compAuto':
		 		saveAutofill(parameter);
		 		break;
		 	case 'compEmpty':
		 		saveEmptyLabel(parameter);
		 		break;
		 	case 'compExt':
		  		saveExternalSrc(parameter);
		  		break;
		  	case 'formTitle':
		 		saveFormTitle(parameter);
		 		break;
		 	case 'formThxTitle':
		 		saveThankYouTitle(parameter);
		 		break;
		 	case 'formThxText':
		 		saveThankYouText(parameter);
		 		break;
		 	case 'pageTitle':
		  		savePageTitle(parameter);
		  		break;
		  	default:
		}
	}
}
function handleComponentDrag(element) {
	if(element != null) {
		var type = element.id;
		FormComponent.createComponent(type, placeNewComponent);
	}
}
function handleButtonDrag(element) {
	if(element != null) {
		var type = element.id;
		FormComponent.addButton(type, placeNewButton);
	}
}
function placeNewButton(parameter) {
	if(parameter != null) {
		var node = document.createElement('div');
		node.id = parameter.id;
		node.style.display = 'inline';
		node.setAttribute('class', 'formButton');
		node.setAttribute('onclick', "loadButtonInfo(this.id);");
		var button = document.createElement('input');
		button.setAttribute('type', 'button');
		button.setAttribute('enabled', 'false');
		button.id = parameter.type;
		button.setAttribute('value', parameter.label);
		button.style.display = 'inline';
		node.appendChild(button);
		var db = document.createElement('img');
		db.setAttribute('class', 'fbSpeedBButton');
		db.setAttribute('src', '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png');
		db.setAttribute('onclick', 'removeButton(this.id);');
		node.appendChild(db);
		currentButton = node;
	}
}
function removeButton(parameter) {
	if(parameter != null) {
		pressedButtonDelete = true;
		FormComponent.removeButton(parameter, removeButtonNode);
	}
}
function removeButtonNode(parameter) {
	if(parameter != null) {
		var node = $(parameter);
		if(node != null) {
			var parentNode = node.parentNode;
			if(parentNode != null) {
				parentNode.removeChild(node);
			}
		}
	}
}
function loadButtonInfo(parameter) {
	if(pressedButtonDelete == false && draggingButton == false) {
		FormComponent.getFormButtonInfo(parameter, placeButtonInfo);
	}
	pressedButtonDelete = false;
	draggingButton = false;
}
function placeButtonInfo(parameter) {
	if(parameter != null) {
		var labelTxt = $('propertyTitle');
		if(labelTxt != null) {
			labelTxt.value = parameter.label;
		}
		var compPr = $('basicPropertiesPanel');
		if(compPr != null) {
			compPr.setAttribute('style', 'display: none');
		}
		var advPr = $('advPropertiesPanel');
		if(advPr != null) {
			advPr.setAttribute('style', 'display: none');
		}
		var extPr = $('extPropertiesPanel');
		if(extPr != null) {
			extPr.setAttribute('style', 'display: none');
		}
		var autoPr = $('autoPropertiesPanel');
		if(autoPr != null) {
			autoPr.setAttribute('style', 'display: none');
		}
		var localPr = $('localPropertiesPanel');
		if(localPr != null) {
			localPr.setAttribute('style', 'display: none');
		}
		STATIC_ACCORDEON.showTabByIndex(1, true);
	}
}
function placeNewComponent(parameter) {
	if(parameter != null) {
		currentElement = createTreeNode(parameter.documentElement);
		//alert(currentElement.innerHTML);
	}
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
	}
}
function loadPageInfo(parameter) {
	FormPage.getFormPageInfo(parameter, placePageInfo);
}
function loadConfirmationPage() {
	FormPage.getConfirmationPageInfo(placePageInfo);
}
function loadThxPage() {
	STATIC_ACCORDEON.showTabByIndex(2, false);
}
function placePageInfo(parameter) {
	if(parameter != null) {
		var pageTitleTxt = $('pageTitle');
		if(pageTitleTxt != null) {
			pageTitleTxt.value = parameter.pageTitle;
		}
		STATIC_ACCORDEON.showTabByIndex(3, true);
		$('workspaceform1:refreshViewPanel').click();
	}
}
function setupPagesDragAndDrop(value1, value2) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangePages,scroll:value1,constraint:false});
	Droppables.add(value1,{onDrop:lalala});
}
function lalala(element, container) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangePages,scroll:value1,constraint:false});
	Droppables.add(value1,{onDrop:lalala});
}
function rearrangePages() {
	draggingPage = true;
	var componentIDs = Sortable.serialize('pagesPanel',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormDocument.updatePagesList(componentIDs,idPrefix,delimiter,nothing);
}
function savePageTitle(parameter) {
	if(parameter != null) {
		FormPage.setTitle(parameter, placePageTitle);
	}
}
function placePageTitle(parameter) {
	var container = $('pagesPanel');
	if(container != null) {
		//TODO very important to handle the renaming of pages right after they have been created
		var node = $(parameter.pageId + '_P_page');
		if(node != null) {
			var parent = node.childNodes[1];
			var textNode = parent.childNodes[0];
			var newTextNode = document.createTextNode(parameter.pageTitle);
			parent.replaceChild(newTextNode, textNode);
			$('workspaceform1:refreshViewPanel').click();
		}
	}
}
function setupButtonsDragAndDrop(value1, value2) {
	//alert('setup');
	Position.includeScrollOffsets = true;
	Sortable.create(value1,{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangeButtons,scroll:value1,constraint:false});
	//Droppables.add(value1,{onDrop:handleButtonDrop});
	//Droppables.add('dropBox',{onDrop:handleButtonDrop});
}
function rearrangeButtons() {
	draggingButton = true;
	var componentIDs = Sortable.serialize('pageButtonArea',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormPage.updateButtonList(componentIDs,idPrefix,delimiter,nothing);
}
function handleButtonDrop(element, container) {
	//alert('handleButtonDrop');
	var cont = $('pageButtonArea');
	if(cont == null) {
		var buttonArea = document.createElement('div');
		buttonArea.id = 'pageButtonArea';
		buttonArea.style.position = 'relative';
		buttonArea.setAttribute('class','formElement');
		var dropBox = $('dropBox');
		if(dropBox != null) {
			dropBox.appendChild(buttonArea);
			buttonArea.appendChild(currentButton);
		}
	} else {
		cont.appendChild(currentButton);
	}
	Position.includeScrollOffsets = true;
	Sortable.create('pageButtonArea',{dropOnEmpty:true,tag:'div',only:'formButton',onUpdate:rearrangeButtons,scroll:'pageButtonArea',constraint:false});
	Droppables.add('viewPanel',{onDrop:handleButtonDrop});
}
function setupComponentDragAndDrop(value1,value2) {
	Position.includeScrollOffsets = true;
	Sortable.create(value1 + 'inner',{dropOnEmpty:true,tag:'div',only:value2,onUpdate:rearrangeComponents,scroll:value1,constraint:false});
	Droppables.add('viewPanel',{onDrop:handleComponentDrop,only:'paletteComponent'});
}
function handleComponentDrop(element,container) {
	//alert(element.getAttribute('class'));
	var type = element.getAttribute('class');
	if(type == 'paletteComponent') {
		//alert('handleComponentDrop');
		var empty = $('emptyForm');
		if(empty != null) {
			if(empty.style) {
				empty.style.display = 'none';
			} else {
				empty.display = 'none';
			}
		}
		if(currentElement != null) {
			//$(container.id + 'inner').appendChild(currentElement);
			$('dropBoxinner').appendChild(currentElement);
			currentElement = null;
			//Sortable.create(container.id + 'inner',{dropOnEmpty:true,tag:'div',only:'formElement',onUpdate:rearrangeComponents,scroll:container.id,constraint:false});
			Sortable.create('dropBoxinner',{dropOnEmpty:true,tag:'div',only:'formElement',onUpdate:rearrangeComponents,scroll:'dropBoxinner',constraint:false});
			Droppables.add('viewPanel',{onDrop:handleComponentDrop});
		}
	} else {
		//alert('handleButtonDrop false');
		var cont = $('pageButtonArea');
		if(cont == null) {
			var buttonArea = document.createElement('div');
			buttonArea.id = 'pageButtonArea';
			buttonArea.style.position = 'relative';
			buttonArea.setAttribute('class','formElement');
			var dropBox = $('dropBox');
			if(dropBox != null) {
				dropBox.appendChild(buttonArea);
				buttonArea.appendChild(currentButton);
			}
		} else {
			cont.appendChild(currentButton);
		}
		Position.includeScrollOffsets = true;
		Sortable.create('pageButtonArea',{dropOnEmpty:true,tag:'div',only:'formButton',onUpdate:rearrangeButtons,scroll:'pageButtonArea',constraint:false});
		Droppables.add('viewPanel',{onDrop:handleButtonDrop});
	}
	
}
function rearrangeComponents() {
	draggingComponent = true;
	var componentIDs = Sortable.serialize('dropBoxinner',{tag:'div',name:'id'});
	var delimiter = '&id[]=';
	var idPrefix = 'fbcomp_';
	FormPage.updateComponentList(componentIDs,idPrefix,delimiter,nothing);
}
function loadComponentInfo(parameter) {
	if(pressedComponentDelete == false && draggingComponent == false) {
		FormComponent.getFormComponentInfo(parameter, placeComponentInfo);
	}
	pressedComponentDelete = false;
	draggingComponent = false;
}
function placeComponentInfo(parameter) {
	if(parameter != null) {
		var x = parameter.required;
		var labelTxt = $('propertyTitle');
		if(labelTxt != null) {
			labelTxt.value = parameter.label;
		}
		var compPr = $('basicPropertiesPanel');
		if(compPr != null) {
			compPr.setAttribute('style', 'display: block');
		}
		var requiredChk = $('propertyRequired');
		if(requiredChk != null) {
			requiredChk.checked = parameter.required;
		}
		var errorTxt = $('propertyErrorMessage');
		if(errorTxt != null) {
			errorTxt.value = parameter.errorMessage;
		}
		var helpTxt = $('propertyHelpText');
		if(helpTxt != null) {
			helpTxt.value = parameter.helpMessage;
		}
		if(parameter.autofill == true) {
			var temp = $('propertyHasAutofill');
			if(temp != null) {
				temp.checked = true;
			}
			var compPr = $('autoPropertiesPanel');
			if(compPr != null) {
				compPr.setAttribute('style', 'display: block');
			}
			var autoTxt = $('propertyAutofill');
			if(autoTxt != null) {
				autoTxt.value = parameter.autofillKey;
			}
		} else {
			var temp = $('propertyHasAutofill');
			if(temp != null) {
				temp.checked = false;
			}
			var compPr = $('autoPropertiesPanel');
			if(compPr != null) {
				compPr.setAttribute('style', 'display: none');
			}
		}
		if(parameter.complex == true) {
			var emptyTxt = $('propertyEmptyLabel');
			if(emptyTxt != null) {
				emptyTxt.value = parameter.emptyLabel;
			}
			var advPr = $('advPropertiesPanel');
			if(advPr != null) {
				advPr.setAttribute('style', 'display: block');
			}
			if(parameter.local == true) {
				document.workspaceform1.dataSrcSwitch[0].checked = true;
				var localPr = $('localPropertiesPanel');
				loadItemset('selectOptsInner',parameter.items);
				if(localPr != null) {
					localPr.setAttribute('style', 'display: block');
				}
				var extPr = $('extPropertiesPanel');
				if(extPr != null) {
					extPr.setAttribute('style', 'display: none');
				}
			} else {
				document.workspaceform1.dataSrcSwitch[1].checked = true;
				var localPr = $('localPropertiesPanel');
				if(localPr != null) {
					localPr.setAttribute('style', 'display: none');
				}
				var extTxt = $('propertyExternal');
				if(extTxt != null) {
					extTxt.value = parameter.externalSrc;
				}
				var extPr = $('extPropertiesPanel');
				if(extPr != null) {
					extPr.setAttribute('style', 'display: block');
				}
			}
		} else {
			var advPr = $('advPropertiesPanel');
			if(advPr != null) {
				advPr.setAttribute('style', 'display: none');
			}
			var extPr = $('extPropertiesPanel');
			if(extPr != null) {
				extPr.setAttribute('style', 'display: none');
			}
			var localPr = $('localPropertiesPanel');
			if(localPr != null) {
				localPr.setAttribute('style', 'display: none');
			}
		}
		STATIC_ACCORDEON.showTabByIndex(1, true);
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
	if(parameter == true) {
			
			var compPr = $('autoPropertiesPanel');
			if(compPr != null) {
				compPr.setAttribute('style', 'display: block');
			}
	} else {
			FormComponent.setAutofillKey('',nothing);
			var compPr = $('autoPropertiesPanel');
			if(compPr != null) {
				compPr.setAttribute('style', 'display: none');
			}
	}
	FormComponent.setAutofill(parameter,nothing);
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
function saveEmptyLabel(parameter) {
	if(parameter != null) {
		FormComponent.setEmptyLabel(parameter, refreshViewPanel);
	}
}
function saveExternalSrc(parameter) {
	if(parameter != null) {
		FormComponent.setExternalSrc(parameter, refreshViewPanel);
	}
}
function saveAutofill(parameter) {
	if(parameter != null) {
		FormComponent.setAutofillKey(parameter, refreshViewPanel);
	}
}
function saveHelpMessage(parameter) {
	//alert('Not implemented');
	/*if(parameter != null) {
		FormComponent.setHelpMessage(parameter, refreshViewPanel);
	}*/
}
function switchDataSource() {
	FormComponent.switchDataSource(placeDataSource);
}
function placeDataSource(parameter) {
	if(parameter == true) {
			var extPr = $('extPropertiesPanel');
			if(extPr != null) {
				extPr.setAttribute('style', 'display: none');
			}
			var localPr = $('localPropertiesPanel');
			if(localPr != null) {
				localPr.setAttribute('style', 'display: block');
			}
	} else {
			var extPr = $('extPropertiesPanel');
			if(extPr != null) {
				extPr.setAttribute('style', 'display: block');
			}
			var localPr = $('localPropertiesPanel');
			if(localPr != null) {
				localPr.setAttribute('style', 'display: none');
			}
	}
}
function expandOrCollapse(node,expand) {
	if(expand == true) {
		node.previousSibling.style.display = 'inline';
		var value = node.previousSibling.value;
		if(value.length == 0) {
			node.previousSibling.value = node.previousSibling.previousSibling.value;
		}
		node.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_left.png';
		node.setAttribute('onclick','expandOrCollapse(this,false);');
	} else {
		node.previousSibling.style.display = 'none';
		node.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png';
		node.setAttribute('onclick','expandOrCollapse(this,true);');
	}
}
function saveLabel(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.value;
	if(value.length != 0) {
		FormComponent.saveLabel(index,value,nothing);
	}
}
function saveValue(parameter) {
	var index = parameter.id.split('_')[1];
	var value = parameter.value;
	if(value.length != 0) {
		FormComponent.saveValue(index,value,nothing);
	}
}
function addNewItem(parameter) {
	var par = $(parameter).lastChild;
	var newInd = getNextRowIndex(par);
	par.appendChild(getEmptySelect(newInd,'',''));
}
function deleteThisItem(ind) {
	var index = ind.split('_')[1];
	FormComponent.removeItem(index,refreshViewPanel);
	var currRow = $(ind);
	var node = $(ind);
	if(node != null) {
		var node2 = node.parentNode;
		node2.removeChild(currRow);
	}
	
}
function getNextRowIndex(parameter) {
	var lastC = parameter.lastChild;
	if(lastC != null) {
		var ind = lastC.id.split('_')[1];
		ind++;
		return ind;
	} else {
		return 0;
	}
}
function expandAllItems() {
	var container = $('selectOptsInner');
	if(container != null) {
		for(var i=0;i<container.childNodes.length;i++) {
			container.childNodes[i].childNodes[2].style.display = 'inline';
		}
	}
}
function collapseAllItems() {
	var container = $('selectOptsInner');
	if(container != null) {
		for(var i=0;i<container.childNodes.length;i++) {
			container.childNodes[i].childNodes[2].style.display = 'none';
		}
	}
}
function getEmptySelect(index,lbl,vl) {
	var result = document.createElement('div');
	result.id = 'workspaceform1:rowDiv_' + index;
	var remB = document.createElement('img');
	remB.style.display = 'inline';
	remB.setAttribute('onclick','deleteThisItem(this.parentNode.id);');
	remB.id = 'delB_' + index;
	remB.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png';
	var label = document.createElement('input');
	label.id = 'labelF_' + index;
	label.setAttribute('type','text');
	label.style.display = 'inline';
	label.value = lbl;
	label.setAttribute('onblur','saveLabel(this);');
	label.setAttribute('class','fbSelectListItem');
	var value = document.createElement('input');
	value.id = 'valueF_' + index;
	value.setAttribute('type','text');
	value.value = vl;
	value.style.display = 'none';
	value.setAttribute('onblur','saveValue(this);');
	value.setAttribute('class','fbSelectListItem');
	var expB = document.createElement('img');
	expB.style.display = 'inline';
	expB.id = 'expB_' + index;
	expB.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png';
	expB.setAttribute('onclick','expandOrCollapse(this,true);');
	result.appendChild(remB);
	result.appendChild(label);
	result.appendChild(value);
	result.appendChild(expB);
	return result;
	
}
function saveFormDocument() {
	showLoadingMessage('Saving');
	FormDocument.save(doNothing);
}
function saveSourceCode(source_code) {
	if(source_code != null) {
		showLoadingMessage('Saving');
		FormDocument.saveSrc(source_code, doNothing);
	}
}
function doNothing(parameter) {
	closeLoadingMessage();
}
function nothing(parameter) {}
function createNewPage() {
	FormPage.createNewPage(placeNewPage);
}
function placeNewPage(parameter) {
	var container = $('pagesPanel');
	if(container != null) {
		var page = document.createElement('div');
		page.setAttribute('id', parameter + '_P_page');
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
		db.src = '/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png';
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
	if(parameter != null) {
		var node = $(parameter);
		if(node != null) {
			var parentNode = node.parentNode;
			if(parentNode != null) {
				pressedPageDelete = true;
				FormPage.removePage($(parameter).parentNode.id,removePageNode);
			}
		}
	}
}
function removePageNode(parameter) {
	if(parameter != null) {
		var node = $(parameter);
		if(node != null) {
			var parentNode = node.parentNode;
			if(parentNode != null) {
				parentNode.removeChild(node);
			}
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
		FormDocument.createFormDocument(name,refreshViewPanelW);
	}
}
function refreshViewPanel(parameter) {
	$('workspaceform1:refreshViewPanel').click();
}
function refreshViewPanelW(parameter) {
	$('workspaceform1:refreshViewPanel').click();
	closeLoadingMessage();
}
//--------------------------------
//Handles the deletion of a form component created with JSF
function removeComponent(parameter) {
	var node = parameter.parentNode;
	if(node != null) {
		pressedComponentDelete = true;
		FormComponent.removeComponent(node.id, removeComponentNode);
	}
}
function removeComponentNode(parameter) {
	var node = $(parameter);
	if(node != null) {
		var parentNode = node.parentNode;
		if(parentNode != null) {
			parentNode.removeChild(node);
		}
	}
}
//----------------------------------------
/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);