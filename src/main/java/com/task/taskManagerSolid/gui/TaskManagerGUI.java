package com.task.taskManagerSolid.gui;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TaskManagerGUI extends JFrame {

    private static String username;
    private static String token;

    public TaskManagerGUI(){

        setTitle("Task Manager GUI");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JTextField taskField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        JToggleButton toggleButton = new JToggleButton("Completed: No");
        toggleButton.setPreferredSize(new Dimension(120, 50));
        toggleButton.setFont(new Font("Arial", Font.PLAIN, 12));
        toggleButton.setBackground(new Color(200, 0, 0));
        toggleButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        toggleButton.setFocusPainted(false);

        toggleButton.addItemListener(e -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("Completed: Yes");
                toggleButton.setBackground(new Color(0, 200, 0));
            } else {
                toggleButton.setText("Completed: No");
                toggleButton.setBackground(new Color(200, 0, 0));
            }
        });

        JButton createButton = new JButton("Create Task");
        JButton listButton = new JButton("List Tasks");

        createButton.addActionListener(e -> {
            String taskName = taskField.getText();
            String description = descriptionArea.getText();
            boolean completed = toggleButton.isSelected();
            if (!taskName.isEmpty() && !description.isEmpty()) {
                createTask(taskName, description, completed);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a task name!");
            }
        });

        listButton.addActionListener(e -> listTasks());

        add(new JLabel("Task Name:"));
        add(taskField);
        add(new JLabel("Description:"));
        add(descriptionScrollPane);
        add(toggleButton);
        add(createButton);
        add(listButton);

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

    public static void createTask(String taskName, String description, boolean completed){
        try{
            URL url = new URL("http://localhost:8081/tasks/create");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String auth = username + ":" + token;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            connection.setDoOutput(true);


            String jsonInput = "{\"title\":\"" + taskName + "\", \"description\":\"" + description + "\", \"completed\":" + completed + "}";
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
    public static void listTasks(){
        try {
            URL url = new URL("http://localhost:8081/tasks/getall");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            String auth = username + ":" + token;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) !=null){
                    response.append(inputLine);
                }
                in.close();

                JSONArray taskArray = new JSONArray(response.toString());

                String[] columnNames = {"Id", "Titulo", "Descrição", "Completada", "Criado em"};

                Object[][] data = new Object[taskArray.length()][5];
                for (int i = 0; i< taskArray.length(); i++){
                    JSONObject task = taskArray.getJSONObject(i);
                    data[i][0] = task.getInt("id");
                    data[i][1] = task.getString("title");
                    data[i][2] = task.get("description");

                    boolean completed = task.getBoolean("completed");
                    data[i][3] = completed ? "Sim" : "Não";

                    data[i][4] = task.get("createdAt");
                }

                JTable taskTable = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(taskTable);
                JOptionPane.showMessageDialog(null, scrollPane, "Tarefas", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(null, "Erro ao listar as Tarefas. Código: " +responseCode);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao Conectar a API para listar as Tarefas!");
        }

    }
    public static void main(String[] args) {
        collectCredentials();


        SwingUtilities.invokeLater(() -> new TaskManagerGUI());
    }
}

