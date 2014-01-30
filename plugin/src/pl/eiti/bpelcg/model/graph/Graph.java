package pl.eiti.bpelcg.model.graph;

/**
 * Graph implementation with root node selected.
 *
 * @param T graph nodes type
 */
public class Graph<T> {
	/** Graph root element */
	private GraphNode<T> root = null;

	/**
	 * Graph default constructor.
	 */
	public Graph() {

	}

	/**
	 * Constructor creating new node from given data.
	 * 
	 * @param data node data element
	 */
	public Graph(T data) {
		this(new GraphNode<T>(data));
	}

	/**
	 * Constructor setting given graph node as root.
	 * 
	 * @param rootNode node element to set as root element
	 */
	public Graph(GraphNode<T> rootNode) {
		this.root = rootNode;
	}

	/**
	 * Graph root element getter.
	 * 
	 * @return root node
	 */
	public GraphNode<T> getRoot() {
		return this.root;
	}

	/**
	 * Graph root element setter.
	 * 
	 * @param rootNode node to set as graph root
	 */
	public void setRoot(GraphNode<T> rootNode) {
		if (null == this.root && (null == rootNode.getPreviousNodes() || rootNode.getPreviousNodes().isEmpty())) {
			this.root = rootNode;
		}
	}

}
