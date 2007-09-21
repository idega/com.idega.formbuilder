package com.idega.formbuilder.business.process.converter;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/21 11:30:29 $ by $Author: civilis $
 */
public interface DataConverter {

	public Object convert(Element o);
	public Element revert(Object o);
}