package com.github.ryanreymorris.orderescortbot.entity.mail;

import com.github.ryanreymorris.orderescortbot.service.RussianMailServiceImpl;
import lombok.Data;

/**
 * DTO for api-request.
 *
 * @see RussianMailServiceImpl
 */
@Data
public class RussianMailItem {

    /**
     * id of request
     */
    private long id;

    /**
     * Cost of package transfer including tax.
     */
    private String paynds;

}
