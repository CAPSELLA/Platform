package gr.uoa.di.madgik.datatransformation.harvester.utils.container;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ListNodeContainer {
	
	NodeList nodes;
	String verb;
	Node node;
	
	public NodeList getNodes() {
		return nodes;
	}
	public void setNodes(NodeList nodes) {
		this.nodes = nodes;
	}
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
}