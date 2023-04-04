package com.github.ryanreymorris.orderescortbot.storage;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Pagination count for current user storage.
 */
@Component
public class PaginationStorage {

    /**
     * Delta value for increasing/decreasing.
     */
    private static final Integer DELTA_VALUE = 5;

    /**
     * Local pagination count for user storage.
     */
    private static final Map<Long, Integer> USER_PAGINATION = new HashMap<>();

    /**
     * Update value in storage
     *
     * @param key   - key of value.
     */
    public void resetPagination(Long key) {
        USER_PAGINATION.put(key, DELTA_VALUE);
    }

    /**
     * Update value in storage
     *
     * @param key   - key of value.
     * @param value - value to store.
     */
    public void updateValue(Long key, Integer value) {
        USER_PAGINATION.put(key, value);
    }

    /**
     * Get value from storage by key.
     *
     * @param key - key of value.
     * @return value.
     */
    public Integer getValue(Long key) {
        return USER_PAGINATION.get(key);
    }

    /**
     * Increase value from storage by key.
     *
     * @param key - key of value.
     * @return value.
     */
    public Integer increaseValue(Long key) {
        Integer value = getValue(key) == null ? 0 : getValue(key);
        USER_PAGINATION.put(key, value + DELTA_VALUE);
        return value + DELTA_VALUE;
    }

    /**
     * Decrease value from storage by key.
     *
     * @param key - key of value.
     * @return value.
     */
    public Integer decreaseValue(Long key) {
        Integer value = getValue(key) == null ? DELTA_VALUE : getValue(key);
        USER_PAGINATION.put(key, value - DELTA_VALUE);
        return value - DELTA_VALUE;
    }

    /**
     * Get delta value.
     *
     * @return delta value.
     */
    public Integer getDeltaValue() {
        return DELTA_VALUE;
    }
}
