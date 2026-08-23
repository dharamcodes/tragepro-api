package com.tragepro.api.journal.core.repository;

import com.tragepro.api.domain.journal.entity.JournalEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends MongoRepository<JournalEntity, String>, JournalRepositoryCustom {

    List<JournalEntity> findByAccountId(String accountId);
}
