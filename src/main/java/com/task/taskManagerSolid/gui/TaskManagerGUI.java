package com.task.taskManagerSolid.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskManagerGUI extends JFrame {
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
        frame.add(taskField);
        frame.add(createButton);

        frame.setVisible(true);

    }

    public static void createTask(String taskName){
        try{
            URL url = new URL("http://localhost:8081/taks/create");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("Post");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInput = "{\"name\":\"" + taskName + "\", \"description\":\"Tarefa criada pela GUI\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0 , input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200 || responseCode == 201){
                JOptionPane.showMessageDialog(null, "Tarefa criada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao criar uma tarefa. Codigo: " +responseCode);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null , "Erro ao conectar a API!");
        }
    }
}
