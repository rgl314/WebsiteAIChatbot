package com.ragul.ChatBot.repository;

import com.ragul.ChatBot.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    Optional<Site> findBySiteId(String siteId);
    Optional<Site> findByPublicKey(String publicKey);

}
