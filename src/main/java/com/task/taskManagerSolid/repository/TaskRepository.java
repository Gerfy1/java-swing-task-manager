package com.task.taskManagerSolid.repository;

import com.task.taskManagerSolid.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
