package hu.webhejj.fsutils.scrape;

public interface Deduplicator<T> {

	T deduplicate(T item);
}
