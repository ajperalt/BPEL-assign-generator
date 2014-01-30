package pl.eiti.bpelcg.analyzer;

import java.util.List;

/**
 * Generic analysis result structure interface.
 * 
 * @param K
 *            key type.
 * @param V
 *            value type as List.
 */
public interface IAnalysisResult<K, V extends List<?>> {
	/**
	 * Puts a value into a mapped by a key given as parameter.
	 * 
	 * @param key
	 *            key element from map.
	 * @param value
	 *            value to put in the map.
	 * @return value put into the map.
	 */
	public V put(K key, V value);

	/**
	 * Gets value from map for key given as parameter.
	 * 
	 * @param key
	 *            key to read from map.
	 * @return value from map mapped by a given key.
	 */
	public V get(K key);
}
