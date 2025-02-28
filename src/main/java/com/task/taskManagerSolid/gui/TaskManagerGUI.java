package com.task.taskManagerSolid.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskManagerGUI extends JFrame {

    private static String username;
    private static String token;

    public TaskManagerGUI(){

        setTitle("Task Manager GUI");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JTextField taskField = new JTextField(20);
        JButton createButton = new JButton("Create Task");

        createButton.addActionListener(e -> {
            String taskName = taskField.getText();
            if (!taskName.isEmpty()) {
                createTask(taskName);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a task name!");
            }
        });

        add(new JLabel("Task Name:"));
        add(taskField);
        add(createButton);

        pack();
        setVisible(true);

    }

    public static void collectCredentials(){
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField(20);
        JTextField tokenField = new JTextField(20);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Token:"));
        panel.add(tokenField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Task Manager", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            username = usernameField.getText();
            token = tokenField.getText();
        } else {
            System.exit(0);
        }
    }

    public static void createTask(String taskName){
        try{
            URL url = new URL("http://localhost:8081/tasks/create");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Username", username);
            connection.setRequestProperty("Authorization", "username=" + username + ", token=" + token);
            connection.setDoOutput(true);

            String jsonInput = "{\"username\":\"" + username + "\", \"token\":\"" + token + "\", \"name\":\"" + taskName + "\", \"description\":\"Tarefa criada pela GUI\"}";
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
    public static void main(String[] args) {
        collectCredentials();


        SwingUtilities.invokeLater(() -> new TaskManagerGUI());
    }
}

