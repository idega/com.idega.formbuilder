function dropHandler(idOfDraggedItem,targetId,x,y) {
	var fieldType = document.getElementById(idOfDraggedItem).lastChild.firstChild.nodeValue;
	document.forms['workspaceform1'].elements['selected_field_type'].value=fieldType;
	document.getElementById('add_field_button').onclick();
}
function selectFormField(selectedField) {
	document.forms['workspaceform1'].elements['selected_field_id'].value=selectedField.parentNode.id;
	document.getElementById('select_field_button').onclick();
}
function selectFormHeader() {
	document.getElementById('select_form_header_button').onclick();
}
function setup(listObjId) {
	var listObj = document.getElementById(listObjId);
	var subDivs = listObj.getElementsByTagName('li');
	for(i=0;i<subDivs.length;i++) {
		dragDropObj.addSource('field[' + i + ']',true);
	}
	dragDropObj.addTarget('dropBox','dropHandler');
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
function empty() {
	alert('Success');
}
function temp(element) {
	alert(element.id);
	/*for(i=0;i<element.getElementsByTagName('div').length;i++) {
		alert(element.childNodes[i].id);
	}*/
	actionmanager.rebuildFormComponentsTree(empty);
}

/*Setup drag and drop from palette to main area with DHTMLGoodies*/
var dragDropObj = new DHTMLSuite_dragDrop();
setup('workspaceform1:firstlist');
/*setup('firstlist');*/
dragDropObj.init();

/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);

/*Setup form components drag and drop functionality with scriptaculous*/
Position.includeScrollOffsets = true;
Sortable.create("dropBox",{dropOnEmpty:true,tag:"div",only:"form_element","onUpdate":temp,containment:["dropBox"],scroll:"dropBox",constraint:false});

								