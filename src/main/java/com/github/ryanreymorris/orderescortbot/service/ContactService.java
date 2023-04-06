package com.github.ryanreymorris.orderescortbot.service;

import org.telegram.telegrambots.meta.api.objects.Contact;

/**
 * Service for {@link Contact}.
 */
public interface ContactService {

    /**
     * Save contact to db.
     *
     * @param contact - contact.
     */
    void save(Contact contact);

    /**
     * Find contact by id.
     *
     * @param id - id of contact.
     */
    Contact findById(Long id);
}
