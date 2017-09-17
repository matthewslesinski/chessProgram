package independentDataStructures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Tree<E, V> implements Iterable<V> {

	/**
	 * Holds the values and connections between nodes at each vertex of the tree
	 * @author matthewslesinski
	 */
	protected class Node {
		
		private final Node parent;
		private final V value;
		private E mainChild = null;
		private final Map<E, Node> children = new HashMap<>();
		
		private Node(V value) {
			this(null, value);
		}
		
		private Node(Node parent, V value) {
			this.parent = parent;
			this.value = value;
		}
		
		private Node getParent() {
			return parent;
		}
		
		private V getValue() {
			return value;
		}
		
		private E getMainEdge() {
			return mainChild;
		}
		
		private Node getMainChild() {
			return children.get(getMainEdge());
		}
		
		private Node getChild(E edge) {
			return children.get(edge);
		}
		
		
		
		private void designateMainChild(E edge) {
			if (children.containsKey(edge)) {
				mainChild = edge;
			}
		}
		
		private void addChild(E edge, V value, boolean designateMainChild) {
			if (!children.containsKey(edge)) {
				children.put(edge, new Node(this, value));
				if (mainChild == null || designateMainChild) {
					designateMainChild(edge);
				}
			}
		}
		
		private void addChild(E edge, V value) {
			addChild(edge, value, false);
		}
		
		private void addMainChild(E edge, V value) {
			addChild(edge, value, true);
		}
	}

	private final Node root;
	private Node currentNode;
	private int depth;
	
	public Tree(V root) {
		this.root = new Node(root);
		this.currentNode = this.root;
		depth = 1;
	}
	
	public int getCurrentDepth() {
		return depth;
	}
	
	public V getCurrentNodeValue() {
		return currentNode.getValue();
	}
	
	public Node moveUp() {
		if (currentNode != root) {
			currentNode = currentNode.getParent();
			depth -= 1;
			return currentNode;
		} else {
			return null;
		}
	}
	
	private Node moveDownHelper(Node child) {
		if (child != null) {
			currentNode = child;
			depth += 1;
		}
		return child;
	}
	
	public Node moveDown() {
		return moveDownHelper(currentNode.getMainChild());
	}
	
	public Node moveDown(E edge) {
		return moveDownHelper(currentNode.getChild(edge));
	}
	
	public void addMainChild(E edge, V value) {
		currentNode.addMainChild(edge, value);
	}
	
	public void addChild(E edge, V value) {
		currentNode.addChild(edge, value);
	}
	
	public V getMainLeafValue() {
		V last = null;
		for (V value : this) {
			last = value;
		}
		return last;
	}

	@Override
	public Iterator<V> iterator() {
		return new Iterator<V>() {
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