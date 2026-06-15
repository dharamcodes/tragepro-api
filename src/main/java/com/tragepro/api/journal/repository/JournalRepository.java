package com.tragepro.api.journal.repository;

import com.tragepro.api.journal.model.entity.JournalEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository
    extends MongoRepository<JournalEntity, String>, JournalRepositoryCustom {

  List<JournalEntity> findByAccountId(String accountId);
}
