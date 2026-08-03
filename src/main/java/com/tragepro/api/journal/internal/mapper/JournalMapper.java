package com.tragepro.api.journal.internal.mapper;

import com.tragepro.api.journal.dto.JournalRequest;
import com.tragepro.api.journal.dto.JournalResponse;
import com.tragepro.api.journal.internal.entity.JournalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JournalMapper {

  JournalEntity toEntity(JournalRequest request);

  JournalResponse toResponse(JournalEntity entity);

  void updateEntityFromRequest(JournalRequest request, @MappingTarget JournalEntity entity);
}
