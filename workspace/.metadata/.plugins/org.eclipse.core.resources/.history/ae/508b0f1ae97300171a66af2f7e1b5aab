package boardFeatures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SquareSet implements List<Square> {
	private static final int NUM_SQUARES = Square.values().length;
	
	private final List<Square> squaresList = new ArrayList<>(NUM_SQUARES);
	private final boolean[] squaresArray = new boolean[NUM_SQUARES];
	
	public SquareSet() {
		
	}
	
	
	@Override
	public int size() {
		return squaresList.size();
	}

	@Override
	public boolean isEmpty() {
		return squaresList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Square)) {
			return false;
		}
		return squaresArray[((Square) o).getIndex()];
	}

	@Override
	public Iterator<Square> iterator() {
		return squaresList.iterator();
	}

	@Override
	public Object[] toArray() {
		return squaresList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return squaresList.toArray(a);
	}

	@Override
	public boolean add(Square e) {
		squaresArray[e.getIndex()] = true;
		return squaresList.add(e);
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Square)) {
			return false;
		}
		squaresArray[((Square) o).getIndex()] = false;
		return squaresList.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object e : c) {
			if (!contains(e)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Square> c) {
		boolean toReturn = true;
		for (Square s : c) {
			if (!add(s)) {
				toReturn = false;
			}
		}
		return toReturn;
	}

	


	@Override
	public boolean addAll(int index, Collection<? extends Square> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Square get(int index) {
		return squaresList.get(index);
	}


	@Override
	public Square set(int index, Square element) {
		Square toReturn = remove(index);
		add(index, element);
		return toReturn;
	}


	@Override
	public void add(int index, Square element) {
		squaresArray[element.getIndex()] = true;
		squaresList.add(index, element);
		
	}


	@Override
	public Square remove(int index) {
		Square toRemove = squaresList.remove(index);
		squaresArray[toRemove.getIndex()] = false;
		return toRemove;
	}


	@Override
	public int indexOf(Object o) {
		return squaresList.indexOf(o);
	}


	@Override
	public int lastIndexOf(Object o) {
		return squaresList.lastIndexOf(o);
	}


	@Override
	public ListIterator<Square> listIterator() {
		return squaresList.listIterator();
	}


	@Override
	public ListIterator<Square> listIterator(int index) {
		return squaresList.listIterator(index);
	}


	@Override
	public List<Square> subList(int fromIndex, int toIndex) {
		return squaresList.subList(fromIndex, toIndex);
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
