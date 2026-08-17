CREATE TABLE sites (
    id BIGINT NOT NULL AUTO_INCREMENT,
    site_id VARCHAR(100) NOT NULL,
    public_key VARCHAR(255) NOT NULL,
    name VARCHAR(200) NOT NULL,
    domain VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uk_sites_site_id
        UNIQUE (site_id),

    CONSTRAINT uk_sites_public_key
        UNIQUE (public_key),

    CONSTRAINT uk_sites_domain
        UNIQUE (domain)
);

CREATE TABLE conversations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    site_id BIGINT NOT NULL,
    conversation_id VARCHAR(36) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_conversations_site
        FOREIGN KEY (site_id)
        REFERENCES sites(id),

    CONSTRAINT uk_site_conversation
        UNIQUE (site_id, conversation_id)
);