package hw11.mycache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HwCacheDemo {

    private static final Logger logger = LoggerFactory.getLogger(HwCacheDemo.class);

    public static void main(String[] args) throws InterruptedException {
        HwCache<Integer, Integer> cache = new MyCache<>();
        HwListener<Integer, Integer> listener =
                (key, value, action) -> logger.info("key:{}, value:{}, action: {}", key, value, action);
        cache.addListener(listener);
        cache.put(1, 1);

        logger.info("getValue:{}", cache.get(1));
        cache.remove(1);
        cache.removeListener(listener);

        cacheWhenLimitMemoryExample();
    }

    private static void cacheWhenLimitMemoryExample() throws InterruptedException {
        HwCache<String, BigObject> cache = new MyCache<>();
        cache.put("1", new BigObject());
        logger.info("Shall be object in cache {}", cache.get("1"));
        int size = 127;

        for (int k = 0; k < size; k++) {
            cache.put(String.valueOf(k), new BigObject());
        }
        Thread.sleep(100);
        int sum = 0;
        for (int k = 0; k < size; k++) {
            if (cache.get(String.valueOf(k)) != null) {
                sum++;
            }
        }

        logger.info("Weak references: {}", sum);

    }

    static class BigObject {
        final byte[] array = new byte[1024 * 1024];

        public byte[] getArray() {
            return array;
        }
    }
}
