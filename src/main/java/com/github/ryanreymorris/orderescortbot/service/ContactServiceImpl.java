package com.github.ryanreymorris.orderescortbot.service;

import com.github.ryanreymorris.orderescortbot.entity.ContactInfo;
import com.github.ryanreymorris.orderescortbot.exception.BotException;
import com.github.ryanreymorris.orderescortbot.mapper.ContactMapper;
import com.github.ryanreymorris.orderescortbot.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.text.MessageFormat;

/**
 * Implementation of {@link ContactService} interface.
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactInfoRepository repository;

    @Autowired
    private ContactMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Contact contact) {
        repository.save(mapper.contactToContactInfo(contact));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact findById(Long id) {
        ContactInfo contactInfo = repository.findById(id).orElseThrow(
                () -> new BotException(MessageFormat.format("Не было найдено контактной информации с id:", id)));
        return mapper.contactInfoToContact(contactInfo);
    }
}
