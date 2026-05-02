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

        // ── Root ────────────────────────────────────────────────
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:#06101e;-fx-border-color:#111f38;-fx-border-width:1;");

        // ── Top bar ─────────────────────────────────────────────
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color:#050d1a;-fx-padding:12 16;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label topLbl = new Label("🔐  PUB EMS v6 — Login");
        topLbl.setStyle("-fx-text-fill:#7a9cc0;-fx-font-size:12px;");
        HBox spacer = new HBox(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color:transparent;-fx-text-fill:#7a9cc0;-fx-cursor:hand;-fx-font-size:14px;");
        closeBtn.setOnAction(e -> { stage.close(); });
        topBar.getChildren().addAll(topLbl, spacer, closeBtn);

        // ── Form ────────────────────────────────────────────────
        VBox form = new VBox(18);
        form.setPadding(new Insets(40, 50, 40, 50));
        form.setAlignment(Pos.CENTER);

        // Logo
        StackPane logo = new StackPane();
        Circle c = new Circle(40);
        c.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(1, Color.web("#00b4a6"))));
        c.setEffect(new DropShadow(20, Color.web("#00b4a660")));
        Label pl = new Label("PUB");
        pl.setStyle("-fx-text-fill:white;-fx-font-size:15px;-fx-font-weight:bold;");
        logo.getChildren().addAll(c, pl);

        Label title = new Label("Welcome Back");
        title.setStyle("-fx-text-fill:#e2e8f0;-fx-font-size:22px;-fx-font-weight:bold;");
        Label sub = new Label("Pundra University EMS");
        sub.setStyle("-fx-text-fill:#7a9cc0;-fx-font-size:12px;");

        // Username
        VBox userBox = new VBox(6);
        Label userLbl = new Label("USERNAME");
        userLbl.setStyle("-fx-text-fill:#7a9cc0;-fx-font-size:10px;-fx-font-weight:bold;");
        TextField userField = new TextField("PUB");
        userField.setStyle(inp());
        userBox.getChildren().addAll(userLbl, userField);

        // Password
        VBox passBox = new VBox(6);
        Label passLbl = new Label("PASSWORD");
        passLbl.setStyle("-fx-text-fill:#7a9cc0;-fx-font-size:10px;-fx-font-weight:bold;");
        PasswordField passField = new PasswordField();
        passField.setStyle(inp());
        passField.setPromptText("Enter password...");
        passBox.getChildren().addAll(passLbl, passField);

        // Error label
        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill:#ef4444;-fx-font-size:11px;");

        // Login button
        Button loginBtn = new Button("LOGIN  →");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
            "-fx-background-color:linear-gradient(to right,#1d4ed8,#2563eb);" +
            "-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:14px;" +
            "-fx-padding:12;-fx-background-radius:9;-fx-cursor:hand;" +
            "-fx-effect:dropshadow(gaussian,rgba(37,99,235,0.5),14,0,0,4);");

        // Login action
        Runnable doLogin = () -> {
            String u = userField.getText().trim();
            String p = passField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                errorLbl.setText("⚠  Username and password required!");
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
                    errorLbl.setText("✕  Invalid username or password!");
                    passField.clear();
                    passField.requestFocus();
                }
            } catch (SQLException ex) {
                errorLbl.setText("✕  DB Error: " + ex.getMessage());
            }
        };

        loginBtn.setOnAction(e -> doLogin.run());
        passField.setOnAction(e -> doLogin.run());
        userField.setOnAction(e -> passField.requestFocus());

        // Footer
        Label footer = new Label("Default:  PUB  /  PUB123");
        footer.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:10px;");

        form.getChildren().addAll(logo, title, sub,
            new Label(""), userBox, passBox, errorLbl, loginBtn, footer);

        root.getChildren().addAll(topBar, form);

        // Drag window
        final double[] drag = {0, 0};
        topBar.setOnMousePressed(e -> { drag[0] = e.getSceneX(); drag[1] = e.getSceneY(); });
        topBar.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - drag[0]);
            stage.setY(e.getScreenY() - drag[1]);
        });

        Scene scene = new Scene(root, 400, 520);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.showAndWait();
        return success;
    }

    private String inp() {
        return "-fx-background-color:#0a1628;-fx-border-color:#111f38;" +
               "-fx-border-radius:9;-fx-background-radius:9;" +
               "-fx-text-fill:#e2e8f0;-fx-prompt-text-fill:#2a4a70;" +
               "-fx-padding:10 14;-fx-font-size:13px;";
    }
}