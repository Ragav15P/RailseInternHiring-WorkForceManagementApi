package com.railse.hiring.workForceManagement.repository;

import java.util.List;
import java.util.Optional;

import com.railse.hiring.workForceManagement.model.enums.Priority;
import com.railse.hiring.workForceManagement.model1.TaskManagement;

public interface TaskRepository {
	   Optional<TaskManagement> findById(Long id);
	   TaskManagement save(TaskManagement task);
	   List<TaskManagement> findAll();
	   List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, com.railse.hiring.workForceManagement.common.model.enums.ReferenceType referenceType);
	   List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
	   
	   
	   List<TaskManagement> findByPriority(Priority priority);

	}
