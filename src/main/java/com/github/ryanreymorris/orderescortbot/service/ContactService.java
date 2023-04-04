package com.github.ryanreymorris.orderescortbot.service;

import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.Optional;

/**
 * Service for {@link Contact}.
 */
public interface ContactService {

    void saveContact(Contact contact);

    Contact findContactById(Long id);
}
