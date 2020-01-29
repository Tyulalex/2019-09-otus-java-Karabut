package hw11.mycache.cache;

public interface HwListener<K, V> {

    void notify(K key, V value, String action);
}
