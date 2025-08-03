package com.railse.hiring.workForceManagement.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workForceManagement.common.model.enums.ReferenceType;
import com.railse.hiring.workForceManagement.model.enums.Priority;
import com.railse.hiring.workForceManagement.model.enums.Task;
import com.railse.hiring.workForceManagement.model.enums.TaskStatus;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
   private Long id;
   private Long referenceId;
   private ReferenceType referenceType;
   private Task task;
   private String description;
   private TaskStatus status;
   private Long assigneeId;
   private Long taskDeadlineTime;
   private Priority priority;
}



