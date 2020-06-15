package message.system.repository.cache;

public interface HwListener<K, V> {

    void notify(K key, V value, String action);
}
