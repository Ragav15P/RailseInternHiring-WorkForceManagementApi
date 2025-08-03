package com.railse.hiring.workForceManagement.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.railse.hiring.workForceManagement.common.exception.ResourceNotFoundException;
import com.railse.hiring.workForceManagement.dto.AssignByReferenceRequest;
import com.railse.hiring.workForceManagement.dto.CommentDto;
import com.railse.hiring.workForceManagement.dto.TaskCreateRequest;
import com.railse.hiring.workForceManagement.dto.TaskFetchByDateRequest;
import com.railse.hiring.workForceManagement.dto.TaskHistoryDetails;
import com.railse.hiring.workForceManagement.dto.TaskManagementDto;
import com.railse.hiring.workForceManagement.dto.UpdateTaskRequest;
import com.railse.hiring.workForceManagement.mapper.ITaskManagementMapper;
import com.railse.hiring.workForceManagement.model.enums.Priority;
import com.railse.hiring.workForceManagement.model.enums.Task;
import com.railse.hiring.workForceManagement.model.enums.TaskStatus;
import com.railse.hiring.workForceManagement.model1.TaskManagement;
import com.railse.hiring.workForceManagement.repository.TaskRepository;
import com.railse.hiring.workForceManagement.service.TaskManagementService;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    // In-memory history & comments storage
    private Map<Long, List<String>> activityHistoryStore = new HashMap<>();
    private Map<Long, List<CommentDto>> commentStore = new HashMap<>();

    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");

            TaskManagement savedTask = taskRepository.save(newTask);
            createdTasks.add(savedTask);

            logActivity(savedTask.getId(),
                    "Task created for reference ID " + savedTask.getReferenceId() +
                            " assigned to user " + savedTask.getAssigneeId());
        }
        return taskMapper.modelListToDtoList(createdTasks);
    }

    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return taskMapper.modelListToDtoList(updatedTasks);
    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks =
                taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .toList();

            if (!tasksOfType.isEmpty()) {
                // Cancel all old active tasks of this type
                for (TaskManagement oldTask : tasksOfType) {
                    oldTask.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(oldTask);
                }
            }

            // Always create a new task for the new assignee
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(request.getReferenceId());
            newTask.setReferenceType(request.getReferenceType());
            newTask.setTask(taskType);
            newTask.setAssigneeId(request.getAssigneeId());
            newTask.setStatus(TaskStatus.ASSIGNED);
            taskRepository.save(newTask);

            logActivity(newTask.getId(),
                    "Task assigned to user " + request.getAssigneeId() + " for reference ID " + request.getReferenceId());
        }

        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        LocalDate startDate = Instant.ofEpochMilli(request.getStartDate())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(request.getEndDate())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> {
                    LocalDate taskDate = Instant.ofEpochMilli(task.getTaskDeadlineTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    boolean startedInRange = !taskDate.isBefore(startDate) && !taskDate.isAfter(endDate);
                    boolean startedBeforeRangeAndStillOpen =
                            taskDate.isBefore(startDate) &&
                                    task.getStatus() != TaskStatus.COMPLETED &&
                                    task.getStatus() != TaskStatus.CANCELLED;

                    if (startedInRange) {
                        return task.getStatus() != TaskStatus.COMPLETED &&
                                task.getStatus() != TaskStatus.CANCELLED;
                    }
                    return startedBeforeRangeAndStillOpen;
                })
                .toList();

        return taskMapper.modelListToDtoList(filteredTasks);
    }

    @Override
    public TaskManagementDto updateTaskPriority(Long taskId, Priority newPriority) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        Priority oldPriority = task.getPriority();
        task.setPriority(newPriority);

        TaskManagement updatedTask = taskRepository.save(task);

        logActivity(taskId, "Priority changed from " + oldPriority + " to " + newPriority);

        return taskMapper.modelToDto(updatedTask);
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(Priority priority) {
        List<TaskManagement> tasks = taskRepository.findByPriority(priority);
        return taskMapper.modelListToDtoList(tasks);
    }

    @Override
    public void addComment(Long taskId, String user, String text) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        CommentDto commentDto = new CommentDto();
        commentDto.setTimestamp(System.currentTimeMillis());
        commentDto.setUser(user);
        commentDto.setComment(text);

        commentStore.computeIfAbsent(taskId, k -> new ArrayList<>()).add(commentDto);

        logActivity(taskId, "Comment added by " + user);
    }

    @Override
    public TaskHistoryDetails findTaskDetailsWithHistory(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));

        TaskHistoryDetails detailsDto = new TaskHistoryDetails();
        detailsDto.setTask(taskMapper.modelToDto(task));

        detailsDto.setActivityHistory(activityHistoryStore.getOrDefault(id, new ArrayList<>()));
        detailsDto.setComments(commentStore.getOrDefault(id, new ArrayList<>()));

        return detailsDto;
    }

    private void logActivity(Long taskId, String activity) {
        activityHistoryStore.computeIfAbsent(taskId, k -> new ArrayList<>()).add(activity);
    }
}
