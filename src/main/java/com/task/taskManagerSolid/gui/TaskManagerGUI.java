package com.task.taskManagerSolid.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskManagerGUI {
    public static void main (String[] args){
        JFrame frame = new JFrame("Task Manager GUI");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));

        JTextField taskField = new JTextField(20);
        JButton createButton = new JButton("Create Task");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskName = taskField.getText();
                if ( !taskName.isEmpty()){
                    createTask(taskName);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a task name!");
                }
            }
        });

        frame.add(new JLabel("Task Name:"));

    }
}
