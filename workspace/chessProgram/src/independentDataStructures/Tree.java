package independentDataStructures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A tree implementation, having a root node, edges connecting nodes to their children, and a set of children
 * for each node. Each node contains the wrapped value at that node, but each edge is a value itself, not something
 * wrapped. Therefore, because the child nodes are mapped to in a hashmap for each node, the edges must have {@code equals}
 * and {@code hashCode} implementations. In addition, this tree implementation is stateful. That means that there is always
 * a current node in the tree which is what is visible to external uses, and to get to other nodes the tree must be traversed
 * down or up. Lastly, each node has a default child, in that if you want to move down in the tree, but you don't know which
 * node to move to, that default child is selected. Along that line, that means there is a main branch in the tree, down
 * which the tree can be iterated. This main child can also be replaced by another child when desired.
 * @author matthewslesinski
 *
 * @param <E> The type of values used as edges. These must be able to be stored as keys in a {@code HashMap}
 * @param <V> The type of values wrapped in nodes.
 */
public class Tree<E, V> implements Iterable<V> {

	/**
	 * Holds the values and connections between nodes at each vertex of the tree
	 * @author matthewslesinski
	 */
	protected class Node {
		
		/** The parent node in the tree */
		private final Node parent;
		
		/** The value wrapped by this node */
		private final V value;
		
		/** The favorite edge of this node. This will be assumed to be the edge to navigate through unless specified otherwise */
		private E mainChild = null;
		
		/** All of the edges and the node they connect to, other than the parent */
		private final Map<E, Node> children = new HashMap<>();
		
		/**
		 * Assume the parent is null and this is the root
		 * @param value The wrapped value for this node
		 */
		private Node(V value) {
			this(null, value);
		}
		
		/**
		 * Create the node and connect it to a parent
		 * @param parent The parent
		 * @param value The value to put in this node
		 */
		private Node(Node parent, V value) {
			this.parent = parent;
			this.value = value;
		}
		
		/**
		 * Retrieves the parent node for this one
		 * @return The parent
		 */
		private Node getParent() {
			return parent;
		}
		
		/**
		 * Retrieves the value wrapped in this node
		 * @return The value
		 */
		private V getValue() {
			return value;
		}
		
		/**
		 * Retrieves the default edge to a child
		 * @return The edge
		 */
		private E getMainEdge() {
			return mainChild;
		}
		
		/**
		 * Retrieves the child along the main edge
		 * @return The child node, or null if none
		 */
		private Node getMainChild() {
			return children.get(getMainEdge());
		}
		
		/**
		 * Retrieves the child along a specific edge
		 * @param edge The edge
		 * @return The child along that edge, or null if none
		 */
		private Node getChild(E edge) {
			return children.get(edge);
		}
		
		/**
		 * Switches the main edge from one to another
		 * @param edge The edge to make the main one
		 */
		private void designateMainChild(E edge) {
			if (children.containsKey(edge)) {
				mainChild = edge;
			}
		}
		
		/**
		 * Adds a child node along a given edge, and possibly makes it the main child
		 * @param edge The edge leading to the node
		 * @param childValue The value to put in the new node
		 * @param designateMainChild Whether it should be the main child if there is already a main child
		 */
		private void addChild(E edge, V childValue, boolean designateMainChild) {
			if (!children.containsKey(edge)) {
				children.put(edge, new Node(this, childValue));
				if (mainChild == null || designateMainChild) {
					designateMainChild(edge);
				}
			}
		}
		
		/**
		 * Adds a child along an edge, and does not demand it be the main child
		 * @param edge The edge
		 * @param childValue The value to put in the child to put at the other end of that edge
		 */
		private void addChild(E edge, V childValue) {
			addChild(edge, childValue, false);
		}
		
		/**
		 * Adds a child along an edge, and makes it the main child
		 * @param edge The edge
		 * @param childValue The value to put in the child node along that edge
		 */
		private void addMainChild(E edge, V childValue) {
			addChild(edge, childValue, true);
		}
	}

	/** The root node for the tree */
	private final Node root;
	
	/** The current node being looked at in the tree */
	private Node currentNode;
	
	/** What depth the current node is at */
	private int depth;
	
	/**
	 * Initializes the tree with a root
	 * @param root The root
	 */
	public Tree(V root) {
		this.root = new Node(root);
		this.currentNode = this.root;
		depth = 1;
	}
	
	/**
	 * Retrieves the depth of the currently visible node
	 * @return The depth
	 */
	public int getCurrentDepth() {
		return depth;
	}
	
	/**
	 * Retrieves the value wrapped in the currently visible node
	 * @return The value
	 */
	public V getCurrentNodeValue() {
		return currentNode.getValue();
	}
	
	/**
	 * Moves the currently visible node up a level, and returns the node, or null if moving up is not possible
	 * @return The new visible node, or null
	 */
	public Node moveUp() {
		if (currentNode != root) {
			currentNode = currentNode.getParent();
			depth -= 1;
			return currentNode;
		}
		return null;
	}
	
	/**
	 * Sets the currently visible node to the provided child, and increments the depth
	 * @param child The child to move down to
	 * @return The new visible node
	 */
	private Node moveDownHelper(Node child) {
		if (child != null) {
			currentNode = child;
			depth += 1;
		}
		return child;
	}
	
	/**
	 * Moves the currently visible node downwards through the default main child.
	 * @return The newly visible node, or null if there are no children
	 */
	public Node moveDown() {
		return moveDownHelper(currentNode.getMainChild());
	}
	
	/**
	 * Moves the currently visible node downwards through the provided edge.
	 * @param edge The edge to move along
	 * @return The newly visible node, or null if there are no children
	 */
	public Node moveDown(E edge) {
		return moveDownHelper(currentNode.getChild(edge));
	}
	
	/**
	 * Adds a child to the current node as its main child
	 * @param edge The edge to the new node
	 * @param value The value to be wrapped in the new node
	 */
	public void addMainChild(E edge, V value) {
		currentNode.addMainChild(edge, value);
	}
	
	/**
	 * Adds a child to the current node as a not-necessarily-main child
	 * @param edge The edge to the new node
	 * @param value The value to be wrapped in the new node
	 */
	public void addChild(E edge, V value) {
		currentNode.addChild(edge, value);
	}
	
	/**
	 * Gets the leaf node at the end of the main branch
	 * @return The value in that node
	 */
	public V getMainLeafValue() {
		V last = null;
		for (V value : this) {
			last = value;
		}
		return last;
	}

	@Override
	public Iterator<V> iterator() {
		return new Iterator<>() {
			Node next = currentNode;
			
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public V next() {
				Node current = next;
				next = current.getMainChild();
				return current.getValue();
			}
			
		};
	}
}
