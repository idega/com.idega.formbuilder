var arrowImageHeight = 35;
var displayWaitMessage=true;
var previewImage = false;
var previewImageParent = false;
var slideSpeed = 0;
var previewImagePane = false;
var slideEndMarker = false;
var galleryContainer = false;
var imageGalleryCaptions = new Array();
var myMorph = null;

//var ITEM_ID_PREFIX = 'ItemBottom';
//var NEW_FORM_COMP_STATE = false;
var FORMBUILDER_PATH = "/workspace/forms/formbuilder/";
var FORMADMIN_PATH = "/workspace/forms/formadmin/";
var FORMSHOME_PATH = "/workspace/forms/";

Window.onDomReady(function() {
	previewImageParent = $('forms');
	var temp = previewImageParent;
	previewImagePane = $('forms').getElementsByTagName('DIV')[0];
	previewImagePane.style.top = '0px';
	galleryContainer  = $('formListContainer');
	slideEndMarker = $('slideEnd');
	$('arrow_up_image').onmousemove = initSlide;
	$('arrow_up_image').onmouseout = stopSlide;
	$('arrow_down_image').onmousemove = initSlide;
	$('arrow_down_image').onmouseout = stopSlide;	
	
	$('fbHomePageWelcomeBlock').makeRounded(false, {radius: 10});
	$('newFormComp').makeRounded(false, {radius: 6});
	slidePreviewPane();
	
	myMorph = new Fx.Morph('newFormComp', {wait: false});
	$('newBt').addEvent('click', function(e){
		new Event(e).stop();
		myMorph.start('newFormComponentExpand');
	});
	$('greetingTextL').addEvent('click', function(e){
		new Event(e).stop();
		myMorph.start('newFormComponentExpand');
	});
	$('newIcon').addEvent('click', function(e){
		new Event(e).stop();
		myMorph.start('newFormComponentExpand');
	});
	$('cancelBt').addEvent('click', function(e){
		new Event(e).stop();
		myMorph.start('newFormComponentIdle');
	});
	$('newTxt').addEvent('keypress', function(e){
		if(isEnterEvent(e)) {
			new Event(e).stop();
			var formName = $('newTxt').value;
			createNewForm(formName);
		}
	});
	$('okBt').addEvent('click', function(e){
		new Event(e).stop();
		var formName = $('newTxt').value;
		createNewForm(formName);
	});
	/*$ES("div.formListItem").each(function(item) {
		item.makeRounded(false, {radius: 10});
	});*/
	$ES("a.entriesButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			FormDocument.loadFormDocumentEntries(item.id, {
				callback: function(result) {
					if(result == true) {
						window.location=FORMADMIN_PATH;
					} else {
						alert('Error occured trying to load admin mode');
						closeLoadingMessage();
					}
				}
			});
		});
	});
	$ES("a.editButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			FormDocument.loadFormDocument(item.id, {
				callback: function(result) {
					if(result == true) {
						window.location=FORMBUILDER_PATH;
					} else {
						alert('Error occured trying to load editing mode');
						closeLoadingMessage();
					}
				}
			});
		});
	});
	$ES("a.codeButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			showLoadingMessage('Loading');
			FormDocument.loadFormDocumentCode(item.id, {
				callback: function(result) {
					if(result == true) {
						window.location=FORMBUILDER_PATH;
					} else {
						alert('Error occured trying to load code view mode');
						closeLoadingMessage();
					}
				}
			});
		});
	});
	$ES("a.duplicateButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			//showLoadingMessage('Loading');
			alert('Duplicate disabled');
		});
	});
	$ES("a.deleteButton").each(function(item) {
		item.addEvent('click', function(e){
			new Event(e).stop();
			var answer = confirm("Delete form?")
			if (answer) {
				showLoadingMessage('Deleting');
				FormDocument.deleteFormDocument(item.id, {
					callback: function(result) {
						if(result == true) {
							window.location = FORMSHOME_PATH;
						} else {
							alert('Error trying to delete');
							closeLoadingMessage();
						}
					}
				});
			}
		});
	});
	//$("formListContainer").makeRounded(false, {radius: 10});
});
function createNewForm(formName) {
	if(formName.length < 1) {
		
	} else {
		showLoadingMessage('Creating');
		FormDocument.createFormDocument(formName, createdNewForm);
	}
}
function createdNewForm(result) {
	if(result != null) {
		window.location=FORMBUILDER_PATH;
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
/*function duplicateForm(parameter) {
	var container = $(parameter);
	hideDialog(parameter);
	//new Rico.Effect.Size(parameter, null, 100, 500, 10, {complete:function() {showDialog(parameter,'duplicate')}});
}
function duplicateFormDocument(parameter, newTitle) {
	if(parameter.indexOf(ITEM_ID_PREFIX) == 0) {
		var formId = parameter.substring(ITEM_ID_PREFIX.length);
		if(newTitle != '') {
			FormDocument.duplicateFormDocument(formId,newTitle,refreshHomePageView);
		}
	}
}*/
/*function deleteForm(parameter) {
	var container = $(parameter);
	hideDialog(parameter);
	//new Rico.Effect.Size(parameter, null, 100, 500, 10, {complete:function() {showDialog(parameter,'delete');}});
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
	//new Rico.Effect.Size(parameter, null, 100, 500, 10);
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
* */
Fx.Morph = Fx.Styles.extend({
	start: function(className){
		var to = {};
		if(className == 'newFormComponentExpand') {
			$('newFormComp').removeClass('newFormComponentIdle');
			$('newFormComp').addClass('newFormComponentExpand');
			$('newTxt').value = '';
			$('newTxt').focus();
		} else {
			$('newFormComp').removeClass('newFormComponentExpand');
			$('newFormComp').addClass('newFormComponentIdle');
		}
		return this.parent(to);
	}
});