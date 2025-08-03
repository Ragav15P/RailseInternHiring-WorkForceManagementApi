package com.railse.hiring.workForceManagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.railse.hiring.workForceManagement.common.model.response.Response;
import com.railse.hiring.workForceManagement.dto.AssignByReferenceRequest;
import com.railse.hiring.workForceManagement.dto.TaskCreateRequest;
import com.railse.hiring.workForceManagement.dto.TaskFetchByDateRequest;
import com.railse.hiring.workForceManagement.dto.TaskHistoryDetails;
import com.railse.hiring.workForceManagement.dto.TaskManagementDto;
import com.railse.hiring.workForceManagement.dto.UpdateTaskRequest;
import com.railse.hiring.workForceManagement.model.enums.Priority;
import com.railse.hiring.workForceManagement.service.TaskManagementService;

@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {


   private final TaskManagementService taskManagementService;


   public TaskManagementController(TaskManagementService taskManagementService) {
       this.taskManagementService = taskManagementService;
   }


   @GetMapping("/{id}/details")
   public Response<TaskHistoryDetails> getTaskDetails(@PathVariable Long id) {
       return new Response<>(taskManagementService.findTaskDetailsWithHistory(id));
   }


   @PostMapping("/create")
   public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
       return new Response<>(taskManagementService.createTasks(request));
   }


   @PostMapping("/update")
   public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
       return new Response<>(taskManagementService.updateTasks(request));
   }


   @PostMapping("/assign-by-ref")
   public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
       return new Response<>(taskManagementService.assignByReference(request));
   }


   @PostMapping("/fetch-by-date/v2")
   public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
       return new Response<>(taskManagementService.fetchTasksByDate(request));
      
   }
   
   //New Api's added
   
   @PutMapping("/{id}/priority")
   public Response<TaskManagementDto> updateTaskPriority(
           @PathVariable Long id,
           @RequestParam Priority priority) {
       return new Response<>(taskManagementService.updateTaskPriority(id, priority));
   }
   
   @GetMapping("/priority/{priority}")
   public Response<List<TaskManagementDto>> getTasksByPriority(
           @PathVariable Priority priority) {
       return new Response<>(taskManagementService.getTasksByPriority(priority));
       
       }
   
   
   @PostMapping("/{id}/comments")
   public Response<String> addComment(
           @PathVariable Long id,
           @RequestParam String user,
           @RequestParam String text) {

       taskManagementService.addComment(id, user, text);
       return new Response<>("Comment added successfully");
   }

}




