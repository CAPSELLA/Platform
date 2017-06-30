package gr.uoa.di.madgik.ckan.core;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Node;


public class RetrievedNodes {

	/* identifier, node */
	private Map<String, Node> nodes = new HashMap<String, Node>();
	/* set of identifiers */
	private Set<String> nodesToDelete = new HashSet<String>();

	public Map<String, Node> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, Node> nodes) {
		this.nodes = nodes;
	}

	public void addToNodes(String key, Node value) {
		this.nodes.put(key, value);
	}

	public Set<String> getNodesToDelete() {
		return nodesToDelete;
	}

	public void setNodesToDelete(Set<String> nodesToDelete) {
		this.nodesToDelete = nodesToDelete;
	}

	public void addToNodesToDelete(String nodesToDelete) {
		this.nodesToDelete.add(nodesToDelete);
	}

}
