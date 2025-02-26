package com.task.taskManagerSolid.service;

import com.task.taskManagerSolid.dto.TaskDTO;
import com.task.taskManagerSolid.entity.Task;
import com.task.taskManagerSolid.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task create (TaskDTO taskDTO) {
        Task task = new Task(null, taskDTO.title(), taskDTO.description(), taskDTO.completed(), null);
        return taskRepository.save(task);
    }

}
