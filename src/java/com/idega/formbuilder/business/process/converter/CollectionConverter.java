package com.idega.formbuilder.business.process.converter;

import java.util.ArrayList;
import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/21 11:30:29 $ by $Author: civilis $
 */
public class CollectionConverter implements DataConverter {

	
	public Object convert(Element o) {

		@SuppressWarnings("unchecked")
		List<Element> childElements = DOMUtil.getChildElements(o);
		
		List<String> values = new ArrayList<String>();
		
		if(childElements == null || childElements.isEmpty())
			return values;
		
		for (Element element : childElements)
			values.add(element.getTextContent());
		
		System.out.println("list values: "+values);
		
		return values;
	}
	public Element revert(Object o) {
		
		return null;
	}
}