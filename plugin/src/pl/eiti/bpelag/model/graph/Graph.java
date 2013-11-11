package pl.eiti.bpelag.model.graph;

public class Graph<T> {
	private GraphNode<T> root = null;
	private GraphNode<T> last = null;

	public Graph() {

	}

	public Graph(T data) {
		this(new GraphNode<T>(data));
	}

	public Graph(GraphNode<T> rootNode) {
		this.root = rootNode;
		this.last = this.root;
	}

	public GraphNode<T> getRoot() {
		return this.root;
	}

	public void setRoot(GraphNode<T> rootNode) {
		if (null == this.root && (null == rootNode.getPreviousNodes() || rootNode.getPreviousNodes().isEmpty())) {
			this.root = rootNode;
		}
	}

	public GraphNode<T> getLast() {
		return this.last;
	}

}
