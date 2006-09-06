function dropItems(idOfDraggedItem,targetId,x,y) {
	/*if(targetId=='dropBox'){	// Item dropped on <div id="dropBox">
		document.getElementById('dropBox').appendChild(document.getElementById(idOfDraggedItem));	// Appending dragged element as child of target box
	}*/
	var html = document.getElementById('dropBox').innerHTML;
	var caption = document.getElementById(idOfDraggedItem).firstChild.firstChild.nodeValue;
	if(html.length>0)html = html + '<div class="' + 'form_component' + '"><label>' + caption + '</label></div>';
	document.getElementById('dropBox').innerHTML = html;
}
function setup(listObjId) {
	var listObj = document.getElementById(listObjId);
	var subDivs = listObj.getElementsByTagName('li');
	for(i=0;i<subDivs.length;i++) {
		dragDropObj.addSource('field[' + i + ']',true);
	}
	dragDropObj.addTarget('dropBox','dropItems');
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
initTabs('dhtmlgoodies_tabView1',Array('Form properties','Add new field','Field properties'),1,477,450);
var dragDropObj = new DHTMLSuite_dragDrop();
setup('firstlist');
dragDropObj.init();
messageObj = new DHTML_modalMessage();
messageObj.setShadowOffset(5);

								