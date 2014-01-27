package pl.eiti.bpelcg.model.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph node element implementation.
 * 
 * @param <T>
 *            type of node data
 */
public class GraphNode<T> {
	private T data;
	private State state;

	private List<GraphNode<T>> previousNodes;
	private List<GraphNode<T>> nextNodes;

	/**
	 * Graph node data set constructor.
	 * 
	 * @param newData
	 *            graph node data element to set
	 */
	public GraphNode(T newData) {
		this.data = newData;
		this.state = State.UNVISITED;

		this.previousNodes = new ArrayList<>();
		this.nextNodes = new ArrayList<>();
	}

	/**
	 * Next element existing check.
	 * 
	 * @return next element existing bool value
	 */
	public Boolean hasNext() {
		return null != this.nextNodes && this.nextNodes.size() > 0;
	}

	/**
	 * Previous element existing check.
	 * 
	 * @return previous element existing bool value
	 */
	public Boolean hasPrevious() {
		return null != this.previousNodes && this.previousNodes.size() > 0;
	}

	/**
	 * Multiple next elements check.
	 * 
	 * @return many next elements existing bool value
	 */
	public Boolean isBranched() {
		return null != this.nextNodes && this.nextNodes.size() > 1;
	}

	/**
	 * Unvisited state setter.
	 */
	public void setUnvisited() {
		this.state = State.UNVISITED;
	}

	/**
	 * Unvisited check.
	 * 
	 * @return if was not visited
	 */
	public Boolean isUnvisited() {
		return State.UNVISITED == this.state;
	}

	/**
	 * Processed state setter.
	 */
	public void setProcessing() {
		this.state = State.PROCESSING;
	}

	/**
	 * Processed check.
	 * 
	 * @return if is processing
	 */
	public Boolean isProcessing() {
		return State.PROCESSING == this.state;
	}

	/**
	 * Visited state setter.
	 */
	public void setVisited() {
		this.state = State.VISITED;
	}

	/**
	 * Visited check.
	 * 
	 * @return if was visited
	 */
	public Boolean isVisited() {
		return State.VISITED == this.state;
	}

	/**
	 * Gets data of node.
	 * 
	 * @return node data element.
	 */
	public T getData() {
		return data;
	}

	/**
	 * Gets state of node.
	 * 
	 * @return enumerated state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Gets previous nodes list.
	 * 
	 * @return list of nodes.
	 */
	public List<GraphNode<T>> getPreviousNodes() {
		return previousNodes;
	}

	/**
	 * Sets previous nodes list.
	 * 
	 * @param previousNodes
	 *            list of nodes.
	 */
	public void setPreviousNodes(List<GraphNode<T>> previousNodes) {
		this.previousNodes = previousNodes;
	}

	/**
	 * Adds previous node to list of previous nodes.
	 * 
	 * @param previousNode
	 *            node to add.
	 */
	public void addPreviousNode(GraphNode<T> previousNode) {
		this.previousNodes.add(previousNode);
	}

	/**
	 * Gets next nodes list.
	 * 
	 * @return list of nodes.
	 */
	public List<GraphNode<T>> getNextNodes() {
		return nextNodes;
	}

	/**
	 * Sets next nodes list.
	 * 
	 * @param nextNodes
	 *            list of nodes.
	 */
	public void setNextNodes(List<GraphNode<T>> nextNodes) {
		this.nextNodes = nextNodes;
	}

	/**
	 * Adds next node to list of next nodes.
	 * 
	 * @param nextNode
	 *            node to add.
	 */
	public void addNextNode(GraphNode<T> nextNode) {
		this.nextNodes.add(nextNode);
	}

	/**
	 * Enumerated list of graph node states.
	 */
	enum State {
		UNVISITED, PROCESSING, VISITED
	}
}
