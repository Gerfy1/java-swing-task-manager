package com.task.taskManagerSolid.controller;

import com.task.taskManagerSolid.dto.TaskDTO;
import com.task.taskManagerSolid.entity.Task;
import com.task.taskManagerSolid.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAll(){
        return ResponseEntity.ok(taskService.findAll());
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody TaskDTO taskDTO){
        return ResponseEntity.ok(taskService.create(taskDTO));
    }
}
