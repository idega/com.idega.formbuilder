/**
 * 
 */
package com.idega.formbuilder;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;


/**
 * <p>
 * TODO tryggvil Describe Type IWBundleStarter
 * </p>
 *  Last modified: $Date: 2006/08/17 15:01:03 $ by $Author: civilis $
 * 
 * @author <a href="mailto:tryggvil@idega.com">tryggvil</a>
 * @version $Revision: 1.1 $
 */
public class IWBundleStarter implements IWBundleStartable{

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		FormbuilderViewManager viewMan = FormbuilderViewManager.getInstance(starterBundle.getApplication());
		viewMan.getFormbuilderViewNode();
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		// TODO Auto-generated method stub
		
	}
	
}
