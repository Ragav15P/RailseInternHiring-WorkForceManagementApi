package com.railse.hiring.workForceManagement.dto;

import java.util.List;

import lombok.Data;

@Data
public class TaskHistoryDetails {
    private TaskManagementDto task; // main task details
    private List<String> activityHistory; // history events
    private List<CommentDto> comments; // user comments
}
