package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenericFieldParser {
	public static void renderNode(Node node, UIComponent component, ResponseWriter writer) throws IOException {
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
	public static void printNode(Node node, UIComponent component, ResponseWriter writer) throws IOException {
		String nodeName = node.getNodeName();
		System.out.println(node.getNodeName());
		Node attr = null;
		if(node.hasAttributes()) {
			NamedNodeMap attributes = node.getAttributes();
			for(int i = 0; i < attributes.getLength(); i++) {
				attr = attributes.item(i);
				System.out.println(attr.getNodeName() + " " + attr.getNodeValue());
			}
		}
		
		if(node.hasChildNodes()) {
			NodeList children = node.getChildNodes();
			for(int i = 0; i < children.getLength(); i++) {
				if(children.item(i).getNodeName().equals("#text")) {
					System.out.println(children.item(i).getNodeValue());
				} else {
					printNode(children.item(i), component, writer);
				}
			}
		}
		System.out.println(nodeName);
	}
}
