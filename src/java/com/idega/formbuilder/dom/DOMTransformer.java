package com.idega.formbuilder.dom;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMTransformer {
	
	public static void renderNode(Node node, UIComponent component, ResponseWriter writer) throws IOException {
		//Node node = component
		String nodeName = node.getNodeName();
		writer.startElement(node.getNodeName(), component);
		Node attr = null;
		if(node.hasAttributes()) {
			NamedNodeMap attributes = node.getAttributes();
			for(int i = 0; i < attributes.getLength(); i++) {
				attr = attributes.item(i);
				writer.writeAttribute(attr.getNodeName(), attr.getNodeValue(), null);
			}
		}
		if(node.hasChildNodes()) {
			NodeList children = node.getChildNodes();
			for(int i = 0; i < children.getLength(); i++) {
				if(children.item(i).getNodeName().equals("#text")) {
					writer.writeText(children.item(i).getNodeValue(), null);
				} else {
					renderNode(children.item(i), component, writer);
				}
			}
		}
		writer.endElement(nodeName);
	}
	
}
