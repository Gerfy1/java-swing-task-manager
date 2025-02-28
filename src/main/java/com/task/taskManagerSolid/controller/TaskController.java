package com.task.taskManagerSolid.controller;

import com.task.taskManagerSolid.dto.TaskDTO;
import com.task.taskManagerSolid.entity.Task;
import com.task.taskManagerSolid.repository.TaskRepository;
import com.task.taskManagerSolid.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private TaskService taskService;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Task>> getAll(){
        return ResponseEntity.ok(taskService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<Task> create(@RequestBody TaskDTO taskDTO){
        return ResponseEntity.ok(taskService.create(taskDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task){
        Optional<Task> existingTask = taskRepository.findById(id);
        if(!existingTask.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Task updateTask = existingTask.get();
        updateTask.setCompleted(task.isCompleted());
        taskRepository.save(updateTask);

        return ResponseEntity.ok(updateTask);
    }
}
