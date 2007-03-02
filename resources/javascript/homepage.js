	var arrowImageHeight = 35;
	var displayWaitMessage=true;
	var previewImage = false;
	var previewImageParent = false;
	var slideSpeed = 0;
	var previewImagePane = false;
	var slideEndMarker = false;
	var galleryContainer = false;
	var imageGalleryCaptions = new Array();

function showInputField() {
	var input = $('workspaceform1:newTxt');
	var newBt = $('workspaceform1:newBt');
	var okBt = $('workspaceform1:okBt');
	var cancelBt = $('workspaceform1:cancelBt');
	if(input != null) {
		input.style.display = 'inline';
	}
	if(newBt != null) {
		newBt.style.display = 'none';
	}
	if(okBt != null) {
		okBt.style.display = 'inline';
	}
	if(cancelBt != null) {
		cancelBt.style.display = 'inline';
	}
}
function hideInputField() {
	var input = $('workspaceform1:newTxt');
	var newBt = $('workspaceform1:newBt');
	var okBt = $('workspaceform1:okBt');
	var cancelBt = $('workspaceform1:cancelBt');
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
}
function getTopPos(inputObj) {
	  var returnValue = inputObj.offsetTop;
	  while((inputObj = inputObj.offsetParent) != null)returnValue += inputObj.offsetTop;
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
	}
var container = $('newFormComp');
Rico.Corner.round(container);