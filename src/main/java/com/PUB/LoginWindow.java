package com.PUB;

import com.PUB.db.DB;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import java.sql.*;

public class LoginWindow {

    private boolean success = false;

    public boolean show() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(0);
        root.setStyle(
            "-fx-background-color:#06101e;" +
            "-fx-border-color:#00b4a6;-fx-border-width:1;"
        );

        // Top bar
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color:#050d1a;-fx-padding:10 16;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label topLbl = new Label("PUB EMS v6  —  Login");
        topLbl.setStyle("-fx-text-fill:#7a9cc0;-fx-font-size:13px;-fx-font-weight:bold;");
        javafx.scene.layout.HBox.setHgrow(topLbl, Priority.ALWAYS);
        Button closeBtn = new Button("X");
        closeBtn.setStyle(
            "-fx-background-color:rgba(239,68,68,0.15);" +
            "-fx-text-fill:#ef4444;-fx-font-size:13px;-fx-font-weight:bold;" +
            "-fx-cursor:hand;-fx-padding:4 12;-fx-background-radius:6;"
        );
        closeBtn.setOnAction(e -> { success = false; stage.close(); });
        topBar.getChildren().addAll(topLbl, closeBtn);

        // Form
        VBox form = new VBox(16);
        form.setPadding(new Insets(40, 50, 40, 50));
        form.setAlignment(Pos.CENTER);

        // Logo
        StackPane logo = new StackPane();
        Circle c = new Circle(44);
        c.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(1, Color.web("#00b4a6"))));
        c.setEffect(new DropShadow(24, Color.web("#00b4a660")));
        Label pl = new Label("PUB");
        pl.setStyle("-fx-text-fill:white;-fx-font-size:16px;-fx-font-weight:bold;");
        logo.getChildren().addAll(c, pl);

        Label title = new Label("Welcome Back");
        title.setStyle("-fx-text-fill:#ffffff;-fx-font-size:24px;-fx-font-weight:bold;");

        Label sub = new Label("Pundra University of Science and Technology");
        sub.setStyle("-fx-text-fill:#00b4a6;-fx-font-size:12px;-fx-font-weight:bold;");

        Label loc = new Label("Bogura, Bangladesh");
        loc.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:11px;");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color:#111f38;");

        // Username
        VBox userBox = new VBox(6);
        Label userLbl = new Label("USERNAME");
        userLbl.setStyle("-fx-text-fill:#00b4a6;-fx-font-size:11px;-fx-font-weight:bold;");
        TextField userField = new TextField("PUB");
        userField.setStyle(inp()); userField.setPrefHeight(44);
        userBox.getChildren().addAll(userLbl, userField);

        // Password
        VBox passBox = new VBox(6);
        Label passLbl = new Label("PASSWORD");
        passLbl.setStyle("-fx-text-fill:#00b4a6;-fx-font-size:11px;-fx-font-weight:bold;");
        PasswordField passField = new PasswordField();
        passField.setStyle(inp()); passField.setPrefHeight(44);
        passField.setPromptText("Enter your password...");
        passBox.getChildren().addAll(passLbl, passField);

        // Error
        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill:#ef4444;-fx-font-size:12px;-fx-font-weight:bold;");
        errorLbl.setMinHeight(20);

        // Login Button
        Button loginBtn = new Button("LOGIN TO PUB EMS");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(48);
        loginBtn.setStyle(
            "-fx-background-color:linear-gradient(to right,#1d4ed8,#00b4a6);" +
            "-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:15px;" +
            "-fx-background-radius:10;-fx-cursor:hand;"
        );

        loginBtn.setOnAction(e -> {
            String u = userField.getText().trim();
            String p = passField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                errorLbl.setText("Username and password required!");
                return;
            }
            try {
                PreparedStatement ps = DB.get().prepareStatement(
                    "SELECT id FROM admin_users WHERE username=? AND password=?");
                ps.setString(1, u); ps.setString(2, p);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    success = true;
                    stage.close();
                } else {
                    errorLbl.setText("Invalid username or password!");
                    passField.clear();
                }
            } catch (SQLException ex) {
                errorLbl.setText("DB Error: " + ex.getMessage());
            }
        });

        passField.setOnAction(e -> loginBtn.fire());
        userField.setOnAction(e -> passField.requestFocus());

        Label footer = new Label("Default:  PUB  /  PUB123");
        footer.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:11px;");

        form.getChildren().addAll(
            logo, title, sub, loc, sep,
            userBox, passBox, errorLbl, loginBtn, footer
        );

        root.getChildren().addAll(topBar, form);

        // Drag
        final double[] drag = {0, 0};
        topBar.setOnMousePressed(e -> {
            drag[0] = e.getSceneX(); drag[1] = e.getSceneY();
        });
        topBar.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - drag[0]);
            stage.setY(e.getScreenY() - drag[1]);
        });

        Scene scene = new Scene(root, 440, 580);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
        return success;
    }

    private String inp() {
        return
            "-fx-background-color:#0a1628;" +
            "-fx-border-color:#1a3a5c;" +
            "-fx-border-radius:9;-fx-background-radius:9;" +
            "-fx-text-fill:#e2e8f0;-fx-prompt-text-fill:#2a4a70;" +
            "-fx-font-size:14px;-fx-padding:10 14;";
    }
}