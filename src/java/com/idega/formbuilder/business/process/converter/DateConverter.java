package com.idega.formbuilder.business.process.converter;

import java.util.Date;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/21 11:30:29 $ by $Author: civilis $
 */
public class DateConverter implements DataConverter {

	public Object convert(Element o) {

//		TODO: parse date from string
		String dateStr = o.getTextContent();
		
		return new Date();
	}
	public Element revert(Object o) {
		
		return null;
	}
}