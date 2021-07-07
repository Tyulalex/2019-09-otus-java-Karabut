package message.system.repository.cache.impl;

import message.system.repository.cache.HwCache;
import message.system.repository.cache.HwListener;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class CacheImpl<K, V> implements HwCache<K, V> {

    private WeakHashMap<K, V> store;
    private List<HwListener<K, V>> listeners;

    public CacheImpl() {
        this.store = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        this.store.put(key, value);
        this.notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var value = this.store.get(key);
        this.store.remove(key);
        this.notifyListeners(key, value, "remove");
    }

    @Override
    public V get(K key) {
        var value = this.store.get(key);
        this.notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (HwListener<K, V> listener : this.listeners) {
            listener.notify(key, value, action);
        }
    }
}
