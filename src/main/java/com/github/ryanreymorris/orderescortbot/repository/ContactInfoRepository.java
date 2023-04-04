package com.github.ryanreymorris.orderescortbot.repository;

import com.github.ryanreymorris.orderescortbot.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * {@link ContactInfo} repository.
 */
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
}
