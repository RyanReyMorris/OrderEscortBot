package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.mail.RussianMailItem;
import org.springframework.http.ResponseEntity;

/**
 * Service for api-request to "Russian Mail Group Service" to get cost of package transfer.
 */
public interface RussianMailService {

    /**
     * Get cost of package transfer.
     *
     * @param destinationIndex - destination index.
     * @return RussianMailItem.
     */
    ResponseEntity<RussianMailItem> getPackageTransferCost(String destinationIndex);
}
