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
function createNewForm() {
	//$('noFormNotice').style.visibility = 'hidden';
	//var componentIDs = Sortable.serialize("dropBox",{tag:"div",name:"components"});
	//alert(componentIDs);
	//setupDragAndDrop();
}
function setupDragAndDrop() {
	Droppables.add('dropBox',{onDrop:handleComponentDrop});
	Position.includeScrollOffsets = true;
	Sortable.create("dropBox",{dropOnEmpty:true,tag:"div",only:"formElement",onUpdate:testing,scroll:"dropBox",constraint:false});
}
function handleComponentDrop(element,container) {
	$('dropBox').appendChild(currentElement);
	currentElement = null;
	Sortable.create("dropBox",{dropOnEmpty:true,tag:"div",only:"formElement",onUpdate:testing,scroll:"dropBox",constraint:false});
	Droppables.add('dropBox',{onDrop:handleComponentDrop});
}
function addDecoyElement() {
	var decoyElement = document.createElement('div');
	$('dropBox').appendChild(decoyElement);
}
function testing() {
	var componentIDs = Sortable.serialize("dropBox",{tag:"div",name:"id"});
}
setupDragAndDrop();

/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);
		