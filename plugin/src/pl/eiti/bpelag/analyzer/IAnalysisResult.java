package pl.eiti.bpelag.analyzer;

import java.util.List;

public interface IAnalysisResult<K, V extends List<?>> {
	public V put(K key, V value);

	public V get(K key);
}
