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
	dwrmanager.getElement(gotComponent,"fbcomp_text");
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
	/*alert("onDragStart");
	alert(element.id);*/
	dwrmanager.getElement(gotComponent,"fbcomp_multiple_select_minimal");
	/*alert("onDragEnd");*/
}
function gotComponent(result) {
	alert("Result of method" + result);
	document.getElementById('dropBox').appendChild(result.childNodes[0]);
}
function temp(element) {
	alert("onUpdate");
	actionmanager.rebuildFormComponentsTree(empty);
}

/*Setup drag and drop from palette to main area with DHTMLGoodies*/
/*var dragDropObj = new DHTMLSuite_dragDrop();
setup('workspaceform1:firstlist');*/
/*setup('firstlist');*/
/*dragDropObj.init();*/
/*var palette = document.getElementById('workspaceform1:firstlist');
for(var i=0;i<palette.childNodes.length;i++) {
	new Draggable('field[' + i + ']', {tag:"li",starteffect:startDrag,revert:true});
}
Droppables.add('dropBox',{onDrop:empty});*/
/*Setup modal message windows functionality*/
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);

/*Setup form components drag and drop functionality with scriptaculous*/
Position.includeScrollOffsets = true;
/*Sortable.create("workspaceform1:firstlist",{dropOnEmpty:true,tag:"li",containment:["workspaceform1:firstlist","dropBox"],constraint:false});*/
Sortable.create("dropBox",{dropOnEmpty:true,tag:"div","onUpdate":temp,containment:["dropBox","workspaceform1:firstlist"],scroll:"dropBox",constraint:false});

								