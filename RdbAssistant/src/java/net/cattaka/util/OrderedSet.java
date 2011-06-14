package net.cattaka.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OrderedSet<V> implements Set<V> {
	private Set<V> set;
	private ArrayList<V> valueList;

	public OrderedSet(Set<V> set) {
		super();
		this.set = set;
		this.valueList = new ArrayList<V>();
	}

	public boolean add(V o) {
		boolean result = set.add(o); 
		if (result) {
			valueList.add(o);
		}
		return result;
	}

	public boolean addAll(Collection<? extends V> c) {
		boolean result = false;
		for (V o : c) {
			result = result | this.add(o);
		}
		return result;
	}

	public void clear() {
		valueList.clear();
		set.clear();
	}

	public boolean contains(Object o) {
		return set.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}

	public boolean isEmpty() {
		return set.isEmpty();
	}

	public Iterator<V> iterator() {
		return valueList.iterator();
	}

	public boolean remove(Object o) {
		boolean result = set.remove(o);
		if (result) {
			valueList.remove(o);
		}
		return result;
	}

	public boolean removeAll(Collection<?> c) {
		boolean result = false;
		for (Object o : c) {
			result = result | this.remove(o);
		}
		return result;
	}

	public boolean retainAll(Collection<?> c) {
		return valueList.retainAll(c) | set.retainAll(c);
	}

	public int size() {
		return set.size();
	}

	public Object[] toArray() {
		return valueList.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return valueList.toArray(a);
	}
	public List<V> getValueList() {
		return Collections.unmodifiableList(this.valueList);
	}
}
