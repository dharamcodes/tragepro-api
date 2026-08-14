package com.tragepro.api.journal.internal;

import com.tragepro.api.journal.model.entity.JournalEntity;
import com.tragepro.api.journal.model.request.JournalRequest;
import com.tragepro.api.journal.model.response.JournalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface JournalMapper {

  JournalEntity toEntity(JournalRequest request);

  JournalResponse toResponse(JournalEntity entity);

  void updateEntityFromRequest(JournalRequest request, @MappingTarget JournalEntity entity);
}
