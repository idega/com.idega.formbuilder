dojo.require("dojo.html.*");

var CURRENT_ELEMENT_UNDER = -1;
var childBoxes = [];

var FBDraggable = Class.create();

FBDraggable.prototype = (new Rico.Draggable()).extend( {

   	initialize: function(htmlElement, name, type) {
      	this.type        = type;
      	this.htmlElement = $(htmlElement);
      	this.name        = name;
   	},

   	select: function() {
      	this.selected = true;
      	var el = this.htmlElement;

      	el.style.color           = "#ffffff";
      	el.style.backgroundColor = "#08246b";
      	el.style.border          = "1px solid blue";
   	},

   	deselect: function() {
      	this.selected = false;
      	var el = this.htmlElement;
      	el.style.color           = "#2b2b2b";
      	el.style.backgroundColor = "transparent";
      	el.style.border = "1px solid #ffffee";
   	},

   	startDrag: function() {
   		if(this.type == 'fbcomp') {
   			CURRENT_ELEMENT_UNDER = -1;
   			childBoxes = [];
			var dropBox = $('dropBoxinner');
			var child;
			for(var i = 0; i < dropBox.childNodes.length; i++){
				child = dropBox.childNodes[i];
				if(child.nodeType != dojo.html.ELEMENT_NODE){ continue; }
				var pos = dojo.html.getAbsolutePosition(child, true);
				var inner = dojo.html.getBorderBox(child);
				childBoxes.push({top: pos.y, bottom: pos.y+inner.height,left: pos.x, right: pos.x+inner.width, height: inner.height, width: inner.width, node: child});
			}
   			FormComponent.addComponent(this.name, placeNewComponent);
   		} else if(this.type == 'fbbutton') {
   			FormComponent.addButton(this.name, placeNewButton);
   		}
   	},

   	cancelDrag: function() {
   		if(this.type == 'fbcomp') {
   			FormComponent.removeComponent(currentElement.getAttribute('id'),nothing);
   		} else if(this.type == 'fbbutton') {
   			FormComponent.removeButton(currentButton.getAttribute('id'),nothing);
   		}
      	
   	},

   	endDrag: function() {
   	},

   	getSingleObjectDragGUI: function() {
      	var el = this.htmlElement;
		var div = el.cloneNode(true);
		div.onmousemove = currentMousePosition;
      	return div;
   	},

   	getMultiObjectDragGUI: function( draggables ) {
   	},

   	getDroppedGUI: function() {
   		//alert('Dropped');
   	}

} );


var FBDropzone = Class.create();

FBDropzone.prototype = (new Rico.Dropzone()).extend( {

   	initialize: function(htmlElement, type) {
    	this.htmlElement  = $(htmlElement);
      	this.absoluteRect = null;
      	this.type = type;
   	},

   	activate: function() {
   		if(this.type == 'fbcomp') {
   			var cont = $('dropBoxinner');
   			cont.style.backgroundColor = 'Silver';
   		} else if(this.type == 'fbbutton') {
   			var cont = $('pageButtonArea');
			if(cont == null) {
				cont = document.createElement('div');
				cont.id = 'pageButtonArea';
				cont.style.position = 'relative';
				cont.setAttribute('class','formElement');
				cont.style.backgroundColor = 'Silver';
				var dropBox = $('dropBox');
				if(dropBox != null) {
					dropBox.appendChild(cont);
				}
			} else {
				cont.style.backgroundColor = 'Silver';
			}
   		}
   	},

   	deactivate: function() {
   		if(this.type == 'fbcomp') {
   			var cont = $('dropBoxinner');
   			cont.style.backgroundColor = 'White';
   		} else if(this.type == 'fbbutton') {
   			var cont = $('pageButtonArea');
			if(cont != null) {
				cont.style.backgroundColor = 'White';
			}
   		}
   	},

	showHover: function() {
		if(this.type == 'fbcomp') {
			var children;
			if(CURRENT_ELEMENT_UNDER != -1) {
				var cont = $('dropBoxinner');
					if(cont != null) {
						children = getPageComponents();
						var line = $('insertMarker');
						if(line != null) {
							var cloneLine = line.cloneNode(false);
							cont.removeChild(line);
							cont.insertBefore(cloneLine, children[CURRENT_ELEMENT_UNDER]);
							//printStatus("Current childcount: " + children[CURRENT_ELEMENT_UNDER+1].id + " Swap line marker before: " + CURRENT_ELEMENT_UNDER);
						} else {
							line = document.createElement('hr');
							line.id = 'insertMarker';
							cont.insertBefore(line, children[CURRENT_ELEMENT_UNDER]);
							//printStatus("Create line marker before: " + CURRENT_ELEMENT_UNDER);
						}
					}
			}
		} else if(this.type == 'fbbutton') {
			
		}
		
   	},

   	hideHover: function() {
   		if(this.type == 'fbcomp') {
   			var cont = $('dropBoxinner');
   			if(cont != null) {
				var line = $('insertMarker');
				if(line != null) {
					cont.removeChild(line);
				}
			}
   		}
   	},

   	accept: function(draggableObjects) {
   		if(this.type == 'fbcomp') {
   			//alert(CURRENT_ELEMENT_UNDER);
      		var empty = $('emptyForm');
			if(empty != null) {
				if(empty.style) {
					empty.style.display = 'none';
				} else {
					empty.display = 'none';
				}
			}
			var box = $('dropBoxinner');
      		if(currentElement != null) {
		         if(CURRENT_ELEMENT_UNDER != -1) {
		         	FormComponent.moveComponent(currentElement.getAttribute('id'), CURRENT_ELEMENT_UNDER, insertNewComponent);
		         } else {
		         	if(box != null) {
		         		box.appendChild(currentElement);
						currentElement = null;
		         	}
		         }
      		} else {
      			alert('Element is unavailable');
      		}
      		Sortable.create('dropBoxinner',{dropOnEmpty:true,tag:'div',only:'formElement',onUpdate:rearrangeComponents,scroll:'dropBoxinner',constraint:false});
      	} else {
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
      	}
	},

   	canAccept: function(draggableObjects) {
      	for (var i = 0 ; i < draggableObjects.length ; i++) {
         	if (draggableObjects[i].type != this.type)
            	return false;
      	}
      	return true;
   	},
} );

function currentMousePosition(e) {
	var tempBox;
	if(!e) e = window.event;
	for(var i = 0, child; i < childBoxes.length; i++){
		with(childBoxes[i]){
			if (e.pageX >= left && e.pageX <= right && e.pageY >= top && e.pageY <= bottom) {
				CURRENT_ELEMENT_UNDER = i;
			}
			tempBox = childBoxes[i];
		}
	}
}

function getPageComponents() {
	var dropBox = $('dropBoxinner');
	var result = [];
	if(dropBox != null) {
		var list = dropBox.getElementsByTagName('div');
		for(var i=0;i<list.length;i++) {
			var temp = list[i].id;
			if(temp.indexOf('_i') == -1) {
				result.push(list[i]);
			}
		}
	}
	return result;
}

function printStatus(value) {
	window.status = value;
	return true;
}