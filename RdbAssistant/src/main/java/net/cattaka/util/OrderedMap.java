package net.cattaka.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class OrderedMap<K, V> {
	private Map<K, V> map;
	private ArrayList<K> keyList;

	public OrderedMap(Map<K, V> map) {
		super();
		this.map = map;
		this.keyList = new ArrayList<K>();
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public V get(Object key) {
		return map.get(key);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public V put(K key, V value) {
		V result = map.put(key, value);
		if (result == null) {
			this.keyList.add(key);
		}
		return result;
	}

	public void putAll(OrderedMap<? extends K, ? extends V> m) {
		int numKeysToBeAdded = m.size();
		if (numKeysToBeAdded == 0) {
			return;
		}

		for (K key:m.getKeyList()) {
			put(key, m.get(key));
		}
	}

	public V remove(Object key) {
		this.keyList.remove(key);
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		return map.values();
	}

	public List<K> getKeyList() {
		return Collections.unmodifiableList(keyList);
	}
}
