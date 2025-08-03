package com.railse.hiring.workForceManagement.service;

import java.util.List;

import com.railse.hiring.workForceManagement.dto.AssignByReferenceRequest;
import com.railse.hiring.workForceManagement.dto.TaskCreateRequest;
import com.railse.hiring.workForceManagement.dto.TaskFetchByDateRequest;
import com.railse.hiring.workForceManagement.dto.TaskHistoryDetails;
import com.railse.hiring.workForceManagement.dto.TaskManagementDto;
import com.railse.hiring.workForceManagement.dto.UpdateTaskRequest;
import com.railse.hiring.workForceManagement.model.enums.Priority;

public interface TaskManagementService {
	   List<TaskManagementDto> createTasks(TaskCreateRequest request);
	   List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
	   String assignByReference(AssignByReferenceRequest request);
	   List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
	   TaskManagementDto findTaskById(Long id);
	   
	   
	   //New Features
	   
	   
	   TaskManagementDto updateTaskPriority(Long taskId, Priority newPriority);
	    List<TaskManagementDto> getTasksByPriority(Priority priority);
	    
	    
	    
	    void addComment(Long taskId, String user, String text);
	    
	    
	    TaskHistoryDetails findTaskDetailsWithHistory(Long id);


	}
