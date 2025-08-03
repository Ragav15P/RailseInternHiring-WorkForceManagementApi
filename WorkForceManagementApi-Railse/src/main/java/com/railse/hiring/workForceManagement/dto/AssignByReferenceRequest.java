package com.railse.hiring.workForceManagement.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workForceManagement.common.model.enums.ReferenceType;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AssignByReferenceRequest {
   private Long referenceId;
   private ReferenceType referenceType;
   private Long assigneeId;
}
