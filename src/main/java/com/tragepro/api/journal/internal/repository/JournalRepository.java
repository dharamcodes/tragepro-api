package com.tragepro.api.journal.internal.repository;

import com.tragepro.api.journal.internal.entity.JournalEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository
    extends MongoRepository<JournalEntity, String>, JournalRepositoryCustom {

  List<JournalEntity> findByAccountId(String accountId);
}
