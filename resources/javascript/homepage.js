var arrowImageHeight = 35;
var displayWaitMessage=true;
var previewImage = false;
var previewImageParent = false;
var slideSpeed = 0;
var previewImagePane = false;
var slideEndMarker = false;
var galleryContainer = false;
var imageGalleryCaptions = new Array();

var ITEM_ID_PREFIX = 'ItemBottom';
var NEW_FORM_COMP_STATE = false;

function showInputField() {
	new Rico.Effect.Size('newFormComp', 350, null, 500, 10, {complete:toggleControls});
}
function toggleControls() {
	var newBt = $('workspaceform1:newBt');
	var input = $('workspaceform1:newTxt');
	var okBt = $('workspaceform1:okBt_div');
	var cancelBt = $('workspaceform1:cancelBt_div');
	if(NEW_FORM_COMP_STATE == false) {
		if(newBt != null) {
			newBt.style.display = 'none';
		}
		if(okBt != null) {
			okBt.style.display = 'inline';
		}
		if(cancelBt != null) {
			cancelBt.style.display = 'inline';
		}
		if(input != null) {
			input.style.display = 'inline';
			input.focus();
		}
		NEW_FORM_COMP_STATE = true;
	} else {
		if(input != null) {
			input.style.display = 'none';
		}
		if(newBt != null) {
			newBt.style.display = 'inline';
		}
		if(okBt != null) {
			okBt.style.display = 'none';
		}
		if(cancelBt != null) {
			cancelBt.style.display = 'none';
		}
		NEW_FORM_COMP_STATE = false;
	}
}
function hideInputField() {
	toggleControls();
	new Rico.Effect.Size('newFormComp', 200, null, 500, 10);
}
function confirmFormDelete(parameter) {
	var go = confirm("Do you really want to delete this form?");
	if (go == true) {
		if(parameter != null) {
			var str = parameter.id;
			var idStr = str.substring(0,str.lastIndexOf('_div'));
			document.forms[0].elements[idStr].value='true';
			document.forms[0].submit();
		}
	   	return true;
	} else {
	  	return false;
	}
}
function getTopPos(inputObj) {
	var returnValue = inputObj.offsetTop;
	while((inputObj = inputObj.offsetParent) != null)
		returnValue += inputObj.offsetTop;
	return returnValue;
}
function initSlide(e) {
	if(document.all) {
		e = event;
	}
	if(this.src.indexOf('over')<0) {
		this.src = this.src.replace('.gif','-over.gif');
	}
	slideSpeed = e.clientY + Math.max(document.body.scrollTop,document.documentElement.scrollTop) - getTopPos(this);
	if(this.src.indexOf('down')>=0) {
		slideSpeed = (slideSpeed)*-1;	
	} else {
		slideSpeed = arrowImageHeight - slideSpeed;
	}
	slideSpeed = Math.round(slideSpeed * 10 / arrowImageHeight);
	slideSpeed = slideSpeed * 2;
}
function stopSlide() {
	slideSpeed = 0;
	this.src = this.src.replace('-over','');
}
function slidePreviewPane() {
	if(slideSpeed!=0) {
		var topPos = previewImagePane.style.top.replace(/[^\-0-9]/g,'')/1;	
		if(slideSpeed<0 && slideEndMarker.offsetTop<(previewImageParent.offsetHeight - topPos)) {
			slideSpeed=0;
		}
		topPos = topPos + slideSpeed;
		if(topPos>0) {
			topPos=0;
		}
		previewImagePane.style.top = topPos + 'px';
	}
	setTimeout('slidePreviewPane()',30);
}
function initGalleryScript() {
	previewImageParent = document.getElementById('forms');
	previewImagePane = document.getElementById('forms').getElementsByTagName('DIV')[0];
	previewImagePane.style.top = '0px';
	galleryContainer  = document.getElementById('formListContainer');
	slideEndMarker = document.getElementById('slideEnd');
	document.getElementById('workspaceform1:arrow_up_image').onmousemove = initSlide;
	document.getElementById('workspaceform1:arrow_up_image').onmouseout = stopSlide;
	document.getElementById('workspaceform1:arrow_down_image').onmousemove = initSlide;
	document.getElementById('workspaceform1:arrow_down_image').onmouseout = stopSlide;	
	slidePreviewPane();
	var newBt = $('workspaceform1:newBt');
	var input = $('workspaceform1:newTxt');
	var okBt = $('workspaceform1:okBt_div');
	var cancelBt = $('workspaceform1:cancelBt_div');
	if(input != null) {
			input.style.display = 'none';
		}
		if(newBt != null) {
			newBt.style.display = 'inline';
		}
		if(okBt != null) {
			okBt.style.display = 'none';
		}
		if(cancelBt != null) {
			cancelBt.style.display = 'none';
		}
	Rico.Corner.round('newFormComp');
}
function duplicateForm(parameter) {
	var container = $(parameter);
	hideDialog(parameter);
	new Rico.Effect.Size(parameter, null, 100, 500, 10, {complete:function() {showDialog(parameter,'duplicate')}});
}
function duplicateFormDocument(parameter, newTitle) {
	if(parameter.indexOf(ITEM_ID_PREFIX) == 0) {
		var formId = parameter.substring(ITEM_ID_PREFIX.length);
		if(newTitle != '') {
			FormDocument.duplicateFormDocument(formId,newTitle,refreshHomePageView);
		}
	}
}
function deleteForm(parameter) {
	var container = $(parameter);
	hideDialog(parameter);
	new Rico.Effect.Size(parameter, null, 100, 500, 10, {complete:function() {showDialog(parameter,'delete');}});
}
function showDialog(parameter,type) {
	if(type == 'delete') {
		var dialogNode = createDeleteDialog();
		if(dialogNode != null) {
			var containerNode = $(parameter).childNodes[1];
			var oldNode = $(parameter + '_DD');
			if(oldNode == null) {
				dialogNode.id = parameter + '_DD';
				dialogNode.childNodes[1].setAttribute("onclick","deleteFormDocument('" + parameter + "');");
				dialogNode.childNodes[2].setAttribute("onclick","hideDialog('" + parameter + "');");
				containerNode.appendChild(dialogNode);
			}
		}
	} else if(type == 'duplicate') {
		var dialogNode = createDuplicateDialog();
		if(dialogNode != null) {
			var containerNode = $(parameter).childNodes[1];
			var oldNode = $(parameter + '_DupD');
			if(oldNode == null) {
				dialogNode.id = parameter + '_DupD';
				dialogNode.childNodes[2].setAttribute("onclick","duplicateFormDocument('" + parameter + "',this.previousSibling.value);");
				dialogNode.childNodes[3].setAttribute("onclick","hideDialog('" + parameter + "');");
				containerNode.appendChild(dialogNode);
			}
		}
	}
}
function hideDialog(parameter) {
	var root = $(parameter);
	if(root != null) {
		var bottomHalf = root.childNodes[1];
		if(bottomHalf != null) {
			var index = bottomHalf.childNodes.length - 1;
			if(index > 9) {
				var node = bottomHalf.childNodes[index];
				bottomHalf.removeChild(node);
				new Rico.Effect.Size(parameter, null, 60, 50, 1);
			}
		}
	}
	
}
function deleteFormDocument(parameter) {
	if(parameter.indexOf(ITEM_ID_PREFIX) == 0) {
		var formId = parameter.substring(ITEM_ID_PREFIX.length);
		FormDocument.deleteFormDocument(formId,refreshHomePageView);
	}
}
function refreshHomePageView(result) {
	if(result != null) {
		window.location="/workspace/forms/";
	}
}
function showDuplicateDialog(parameter) {
	new Rico.Effect.Size(parameter, null, 100, 500, 10);
}
function pressOk(e) {
	if (!e) e = window.event;
	if (!e) return true;
	var key = (typeof e.keyCode != 'undefined' ? e.keyCode : e.charCode);
	if(key == '13') {
		$('workspaceform1:okBt').click();
		//document.forms['workspaceform1'].submit();
	}
}
function createDuplicateDialog() {
	var root = document.createElement('div');
	root.setAttribute('class','duplicateFormDialog');
	
	var lbl = document.createElement('label');
	var txt = document.createTextNode('Please enter the name of the new form:');
	var formInput = document.createElement('input');
	formInput.type = 'text';
	formInput.value = '';
	var okBtn = document.createElement('input');
	okBtn.type = 'button';
	okBtn.value = 'OK';
	var cancelBtn = document.createElement('input');
	cancelBtn.type = 'button';
	cancelBtn.value = 'Cancel';
	lbl.appendChild(txt);
	root.appendChild(lbl);
	root.appendChild(formInput);
	root.appendChild(okBtn);
	root.appendChild(cancelBtn);
	return root;
}
function createDeleteDialog() {
	var root = document.createElement('div');
	root.setAttribute('class','deleteFormDialog');
	
	var lbl = document.createElement('label');
	var txt = document.createTextNode('Are you sure you want to send this form to trash?');
	var okBtn = document.createElement('input');
	okBtn.type = 'button';
	okBtn.value = 'OK';
	var cancelBtn = document.createElement('input');
	cancelBtn.type = 'button';
	cancelBtn.value = 'Cancel';
	lbl.appendChild(txt);
	root.appendChild(lbl);
	root.appendChild(okBtn);
	root.appendChild(cancelBtn);
	return root;
}