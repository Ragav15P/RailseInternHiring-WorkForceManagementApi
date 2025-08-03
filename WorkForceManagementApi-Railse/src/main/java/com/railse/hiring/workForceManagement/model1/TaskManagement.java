package com.railse.hiring.workForceManagement.model1;

import java.util.ArrayList;
import java.util.List;

import com.railse.hiring.workForceManagement.common.model.enums.ReferenceType;
import com.railse.hiring.workForceManagement.model.enums.Priority;
import com.railse.hiring.workForceManagement.model.enums.Task;
import com.railse.hiring.workForceManagement.model.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskManagement {
   private Long id;
   private Long referenceId;
   private ReferenceType referenceType;
   private Task task;
   private String description;
   private TaskStatus status;
   private Long assigneeId; // Simplified from Entity for this assignment
   private Long taskDeadlineTime;
   private Priority priority;
   
   
   
   
   
   
   
   
   private List<ActivityLog> activityHistory = new ArrayList<>();
   private List<Comment> comments = new ArrayList<>();
}



