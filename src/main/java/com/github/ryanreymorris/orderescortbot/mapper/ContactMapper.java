package com.github.ryanreymorris.orderescortbot.mapper;

import com.github.ryanreymorris.orderescortbot.entity.ContactInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telegram.telegrambots.meta.api.objects.Contact;

/**
 * Mapstruct Mapper of {@link Contact}.
 */
@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Mapping(target = "userId", source = "contactInfo.id")
    Contact contactInfoToContact(ContactInfo contactInfo);

    @Mapping(target = "id", source = "contact.userId")
    ContactInfo contactToContactInfo(Contact contact);
}
