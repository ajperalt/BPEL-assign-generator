package pl.eiti.bpelag.model.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Graph node element implementation.
 * 
 * @param <T> type of node data
 */
public class GraphNode<T> {
	private T data;
	private State state;

	private Set<GraphNode<T>> previousNodes;
	private Set<GraphNode<T>> nextNodes;

	/**
	 * Graph node data set constructor.
	 * 
	 * @param newData graph node data element to set
	 */
	public GraphNode(T newData) {
		this.data = newData;
		this.state = State.UNVISITED;

		this.previousNodes = new HashSet<>();
		this.nextNodes = new HashSet<>();
	}

	/**
	 * Next element existing check.
	 * 
	 * @return next element existing bool value
	 */
	public Boolean hasNext() {
		return this.nextNodes.size() > 0;
	}

	/**
	 * Previous element existing check.
	 * 
	 * @return previous element existing bool value
	 */
	public Boolean hasPrevious() {
		return this.previousNodes.size() > 0;
	}

	/**
	 * Unvisited state setter.
	 */
	public void setUnvisited() {
		this.state = State.UNVISITED;
	}

	/**
	 * Processed state setter.
	 */
	public void setProcessed() {
		this.state = State.PROCESSED;
	}

	/**
	 * Visited state setter.
	 */
	public void setVisited() {
		this.state = State.VISITED;
	}

	/** Graph node accessors section */
	public T getData() {
		return data;
	}

	public State getState() {
		return state;
	}
	
	public Set<GraphNode<T>> getPreviousNodes() {
		return previousNodes;
	}

	public void setPreviousNodes(Set<GraphNode<T>> previousNodes) {
		this.previousNodes = previousNodes;
	}

	public void addPreviousNode(GraphNode<T> previousNode) {
		this.previousNodes.add(previousNode);
	}

	public Set<GraphNode<T>> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(Set<GraphNode<T>> nextNodes) {
		this.nextNodes = nextNodes;
	}

	public void addNextNode(GraphNode<T> nextNode) {
		this.nextNodes.add(nextNode);
	}

	/**
	 * Enumerated list of graph node states.
	 */
	enum State {
		UNVISITED, PROCESSED, VISITED
	}
}
