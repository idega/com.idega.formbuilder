function resulting(fieldId) {
	alert(fieldId);
}
function dropHandler(idOfDraggedItem,targetId,x,y) {
	var fieldType = document.getElementById(idOfDraggedItem).lastChild.firstChild.nodeValue;
	document.forms['workspaceform1'].elements['selected_field_type'].value=fieldType;
	
	document.getElementById('add_field_button').onclick();
}

function selectFormField(selectedField) {
	document.forms['workspaceform1'].elements['selected_field_id'].value=selectedField.parentNode.id;
	/*ajaxAnywhere.submitAJAX();*/
	document.getElementById('select_field_button').onclick();
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
var dragDropObj = new DHTMLSuite_dragDrop();
setup('workspaceform1:firstlist');
dragDropObj.init();
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);


								