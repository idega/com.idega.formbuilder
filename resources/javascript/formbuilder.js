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
function handleComponentDrag(element) {
	var type = element.childNodes[1].childNodes[0].nodeValue;
	dwrmanager.getElement(receiveComponenr,type);
}
function receiveComponenr(element) {
	currentElement = createTreeNode(element.documentElement);
}
function createTreeNode(element) {
	if(element.nodeName == '#text') {
		var textNode = document.createTextNode(element.nodeValue);
		return textNode;
	} else {
		var result = document.createElement(element.nodeName);
		for(var i=0;i<element.attributes.length;i++) {
			result.setAttribute(element.attributes[i].nodeName,element.attributes[i].nodeValue);
		}
		for(var j=0;j<element.childNodes.length;j++) {
			result.appendChild(createTreeNode(element.childNodes[j]));
		}
		return result;
	}
}
function createNewForm() {
	$('noFormNotice').style.visibility = 'hidden';
	//setupDragAndDrop();
}
function setupDragAndDrop() {
	var palette = $('workspaceform1:firstlist');
	for(var i=0;i<palette.childNodes.length;i++) {
		new Draggable('field[' + i + ']', {tag:"div",starteffect:handleComponentDrag,revert:true});
	}
	Droppables.add('dropBox',{onDrop:handleComponentDrop});
	Position.includeScrollOffsets = true;
	Sortable.create("dropBox",{dropOnEmpty:true,tag:"div",only:"formElement",containment:["dropBox"],scroll:"dropBox",constraint:false});
}
function handleComponentDrop(element,container) {
	$('dropBox').appendChild(currentElement);
	currentElement = null;
	Sortable.create("dropBox",{dropOnEmpty:true,tag:"div",only:"formElement",containment:["dropBox"],scroll:"dropBox",constraint:false});
	Droppables.add('dropBox',{onDrop:handleComponentDrop});
	var componentIDs = Sortable.serialize("dropBox",{tag:"div",name:"components"});
}
function addDecoyElement() {
	var decoyElement = document.createElement('div');
	$('dropBox').appendChild(decoyElement);
}
var currentElement = null;
setupDragAndDrop();

/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);

/*Setup form components drag and drop functionality with scriptaculous*/
		