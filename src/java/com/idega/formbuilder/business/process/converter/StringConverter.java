package com.idega.formbuilder.business.process.converter;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/21 11:30:29 $ by $Author: civilis $
 */
public class StringConverter implements DataConverter {

	public Object convert(Element o) {

//		TODO: if element contains nodes, serialize them to string and return. for now assume that everything is passed correctly with one text node
		return o.getTextContent();
	}
	public Element revert(Object o) {
		
		return null;
	}
}