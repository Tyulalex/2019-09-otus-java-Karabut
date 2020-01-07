package hw11.mycache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwCacheDemo {

    private static final Logger logger = LoggerFactory.getLogger(HwCacheDemo.class);

    public static void main(String[] args) {
        HwCache<Integer, Integer> cache = new MyCache<>();
        HwListener<Integer, Integer> listener =
                (key, value, action) -> logger.info("key:{}, value:{}, action: {}", key, value, action);
        cache.addListener(listener);
        cache.put(1, 1);

        logger.info("getValue:{}", cache.get(1));
        cache.remove(1);
        cache.removeListener(listener);
    }
}
