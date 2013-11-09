package pl.eiti.bpelag.model.graph;

import java.util.HashSet;
import java.util.Set;

public class Graph<T> {
	private GraphNode<T> root = null;
	private GraphNode<T> last = null;
	private Set<GraphNode<T>> nodes = null;

	private Boolean isRoot = Boolean.FALSE;

	public Graph() {

	}

	public Graph(T data) {
		this(new GraphNode<T>(data));
	}

	public Graph(GraphNode<T> rootNode) {
		this.root = rootNode;
		this.nodes = new HashSet<>();
		this.last = this.root;
		this.nodes.add(this.root);
	}

	public void addNode(GraphNode<T> node) {
		if (!isRoot) {
			setRoot(node);
		}
		if (null == node.getNextNodes() || node.getNextNodes().isEmpty()) {
			this.last = node;
		}
		this.nodes.add(node);
	}

	public GraphNode<T> getRoot() {
		return this.root;
	}

	private void setRoot(GraphNode<T> rootNode) {
		if (null == this.root && (null == rootNode.getPreviousNodes() || rootNode.getPreviousNodes().isEmpty())) {
			this.root = rootNode;
			this.isRoot = Boolean.TRUE;
		}
	}

	public GraphNode<T> getLast() {
		return this.last;
	}

}
