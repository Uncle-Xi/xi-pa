package com.xipa.cache;

import com.xipa.cache.decorators.TransactionalCache;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: TransactionalCacheManager
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class TransactionalCacheManager {

    private final Map<Cache, TransactionalCache> transactionalCaches = new HashMap<>();

    public void clear(Cache cache) {
        getTransactionalCache(cache).clear();
    }

//    public Object getObject(Cache cache, CacheKey key) {
//        return getTransactionalCache(cache).getObject(key);
//    }
//
//    public void putObject(Cache cache, CacheKey key, Object value) {
//        getTransactionalCache(cache).putObject(key, value);
//    }

//    public void commit() {
//        for (TransactionalCache txCache : transactionalCaches.values()) {
//            txCache.commit();
//        }
//    }
//
//    public void rollback() {
//        for (TransactionalCache txCache : transactionalCaches.values()) {
//            txCache.rollback();
//        }
//    }

    private TransactionalCache getTransactionalCache(Cache cache) {
        return transactionalCaches.computeIfAbsent(cache, TransactionalCache::new);
    }

}
