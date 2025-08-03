package com.railse.hiring.workForceManagement.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workForceManagement.common.model.enums.ReferenceType;
import com.railse.hiring.workForceManagement.model.enums.Priority;
import com.railse.hiring.workForceManagement.model.enums.Task;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskCreateRequest {
   private List<RequestItem> requests;


   @Data
   @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
   public static class RequestItem {
       private Long referenceId;
       private ReferenceType referenceType;
       private Task task;
       private Long assigneeId;
       private Priority priority;
       private Long taskDeadlineTime;
   }
}


