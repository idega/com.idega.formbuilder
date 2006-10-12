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
function addNewField(element) {
	var type = element.parentNode.childNodes[1].childNodes[0].nodeValue;
	dwrmanager.getElement(gotComponent,type);
}
function showInnerHtml(element) {
	alert(element.parentNode.innerHtml);
	alert(element.parentNode.id);
	alert(document.getElementById('toolbar_container').innerHTML);
}
function empty(element,dropBox) {
	/*alert('Success');
	alert(document.getElementById('field[0]').innerHTML);
	alert(element.innerHTML);
	alert(dropBox.id);*/
}
function tempCheck() {
	alert("Great");
}
function startDrag(element) {
	var type = element.childNodes[1].childNodes[0].nodeValue;
	dwrmanager.getElement(gotComponent,type);
}
function gotComponent(element) {
	$('dropBox').appendChild(createTreeNode(element.documentElement));
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
function temp(element) {
	alert("onUpdate");
	actionmanager.rebuildFormComponentsTree(empty);
}
var palette = $('workspaceform1:firstlist');
for(var i=0;i<palette.childNodes.length;i++) {
	new Draggable('field[' + i + ']', {tag:"div",starteffect:startDrag,revert:true});
}
/*Droppables.add('dropBox',{onDrop:empty});
Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);

/*Setup form components drag and drop functionality with scriptaculous*/
Position.includeScrollOffsets = true;
Sortable.create("dropBox",{dropOnEmpty:true,tag:"div","onUpdate":temp,containment:["dropBox","workspaceform1:firstlist"],scroll:"dropBox",constraint:false});

								