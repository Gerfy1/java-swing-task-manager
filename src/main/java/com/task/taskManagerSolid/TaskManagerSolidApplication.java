package com.task.taskManagerSolid;

import com.task.taskManagerSolid.gui.TaskManagerGUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class TaskManagerSolidApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerSolidApplication.class, args);

		SwingUtilities.invokeLater(() -> {
			TaskManagerGUI.collectCredentials();
			new TaskManagerGUI();
		});

	}

}
