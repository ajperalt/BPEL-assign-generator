package pl.eiti.bpelag.model.graph;

import java.util.HashSet;
import java.util.Set;

public class GraphNode<T> {
	private T data;
	private State state;

	private Set<GraphNode<T>> previousNodes;
	private Set<GraphNode<T>> nextNodes;

	public GraphNode(T newData) {
		this.data = newData;
		this.state = State.UNVISITED;

		this.previousNodes = new HashSet<>();
		this.nextNodes = new HashSet<>();
	}

	public Boolean hasNext() {
		return this.nextNodes.size() > 0;
	}

	public Boolean hasPrevious() {
		return this.previousNodes.size() > 0;
	}

	public T getData() {
		return data;
	}

	public State getState() {
		return state;
	}

	public void setUnvisited() {
		this.state = State.UNVISITED;
	}

	public void setProcessed() {
		this.state = State.PROCESSED;
	}

	public void setVisited() {
		this.state = State.VISITED;
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
