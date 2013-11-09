package pl.eiti.bpelag.model.graph;

import java.util.Set;

public class GraphNode<T> {
	private T data;
	private State state;

	private Set<T> previousNodes;
	private Set<T> nextNodes;

	public GraphNode(T newData) {
		this.data = newData;
		this.state = State.UNVISITED;
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

	public Set<T> getPreviousNodes() {
		return previousNodes;
	}

	public void setPreviousNodes(Set<T> previousNodes) {
		this.previousNodes = previousNodes;
	}

	public void addPreviousNode(T previousNode) {
		this.previousNodes.add(previousNode);
	}

	public Set<T> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(Set<T> nextNodes) {
		this.nextNodes = nextNodes;
	}

	public void addNextNode(T nextNode) {
		this.nextNodes.add(nextNode);
	}

	/**
	 * Enumerated list of graph node states.
	 */
	enum State {
		UNVISITED, PROCESSED, VISITED
	}
}
