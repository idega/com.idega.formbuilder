	var initializedTabsMenu = false;
	var menuAlignment = 'left';	// Align menu to the left or right?		
	var topMenuSpacer = 30; // Horizontal space(pixels) between the main menu items	
	var activateSubOnClick = false; // if true-> Show sub menu items on click, if false, show submenu items onmouseover
	var leftAlignSubItems = false; 	// left align sub items t
	
	var activeMenuItem = false;	// Don't change this option. It should initially be false
	var activeTabIndex = 0;	// Index of initial active tab	(0 = first tab) - If the value below is set to true, it will override this one.
	
	var MSIE = navigator.userAgent.indexOf('MSIE')>=0?true:false;
	var Opera = navigator.userAgent.indexOf('Opera')>=0?true:false;
	var navigatorVersion = navigator.appVersion.replace(/.*?MSIE ([0-9]\.[0-9]).*/g,'$1')/1;
	
	function showHide()
	{
		if(activeMenuItem){
			activeMenuItem.className = 'inactiveMenuItem'; 	
			var theId = activeMenuItem.id.replace(/[^0-9]/g,'');
			document.getElementById('paletteBody_'+theId).style.display='none';
			var img = activeMenuItem.getElementsByTagName('IMG');
			if(img.length>0)img[0].style.display='none';			
		}

		var img = this.getElementsByTagName('IMG');
		if(img.length>0)img[0].style.display='inline';
				
		activeMenuItem = this;		
		this.className = 'activeMenuItem';
		var theId = this.id.replace(/[^0-9]/g,'');
		document.getElementById('paletteBody_'+theId).style.display='block';
	}
	
	function initMenu()
	{
		var mainMenuObj = document.getElementById('mainPalette');
		if(mainMenuObj == null) {
			return;
		}
		var menuItems = mainMenuObj.childNodes;
		if(document.all){
			mainMenuObj.style.visibility = 'hidden';
			document.getElementById('paletteBody').style.visibility='hidden';
		}				
		var currentLeftPos = 35;
		for(var no=0;no<menuItems.length;no++){			
			if(activateSubOnClick) {
				menuItems[no].onclick = showHide; 
			} else {
				menuItems[no].onmouseover = showHide;
			}
			menuItems[no].id = 'mainMenuItem' + (no+1);
			if(menuAlignment=='left') {
				menuItems[no].style.left = currentLeftPos + 'px';
			} else {
				menuItems[no].style.right = currentLeftPos + 'px';
			}
			currentLeftPos = currentLeftPos + menuItems[no].offsetWidth + topMenuSpacer; 
						
			if(no==activeTabIndex){
				menuItems[no].className='activeMenuItem';
				activeMenuItem = menuItems[no];
			} else {
				menuItems[no].className='inactiveMenuItem';
			}
			if(!document.all) {
				menuItems[no].style.bottom = '-1px';
			}
			if(MSIE && navigatorVersion < 6) {
				menuItems[no].style.bottom = '-2px';
			}
		}		
		
		/*var mainMenuLinks = mainMenuObj.getElementsByTagName('a');*/
		var mainMenuLinks = mainMenuObj.childNodes;
		
		var subCounter = 1;
		var parentWidth = mainMenuObj.offsetWidth;
		while(document.getElementById('paletteBody_' + subCounter)){
			var subItem = document.getElementById('paletteBody_' + subCounter);
			if(leftAlignSubItems){
				// No action
			}else{							
				var leftPos = mainMenuLinks[subCounter-1].offsetLeft;
				document.getElementById('paletteBody_'+subCounter).style.paddingLeft =  leftPos + 'px';
				subItem.style.position ='absolute';
				if(subItem.offsetWidth > parentWidth){
					leftPos = leftPos - Math.max(0,subItem.offsetWidth-parentWidth); 	
				}
				subItem.style.paddingLeft =  leftPos + 'px';
				subItem.style.position ='static';
			}
			if(subCounter==(activeTabIndex+1)){
				subItem.style.display='block';
			}else{
				subItem.style.display='none';
			}
			subCounter++;
		}
		if(document.all){
			mainMenuObj.style.visibility = 'visible';
			document.getElementById('paletteBody').style.visibility='visible';
		}		
		document.getElementById('paletteBody').style.display='block';
		initializedTabsMenu = true;
	}