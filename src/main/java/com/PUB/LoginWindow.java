package com.PUB;

import com.PUB.db.DB;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.Duration;
import java.sql.*;

public class LoginWindow {

    private boolean success = false;

    public boolean show() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        // ── Root Layout ──────────────────────────────────────────
        HBox root = new HBox(0);
        root.setStyle("-fx-background-color:#06101e;-fx-border-color:#00b4a6;-fx-border-width:1;");

        // ── LEFT PANEL — Branding ────────────────────────────────
        VBox leftPanel = new VBox(20);
        leftPanel.setMinWidth(220);
        leftPanel.setMaxWidth(220);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(40, 24, 40, 24));
        leftPanel.setStyle(
            "-fx-background-color:linear-gradient(to bottom, #0d2137, #06101e);" +
            "-fx-border-color:transparent #111f38 transparent transparent;" +
            "-fx-border-width:0 1 0 0;"
        );

        // University seal/logo
        StackPane seal = new StackPane();
        Circle outerRing = new Circle(60);
        outerRing.setFill(Color.TRANSPARENT);
        outerRing.setStroke(Color.web("#00b4a6", 0.4));
        outerRing.setStrokeWidth(1.5);

        Circle midRing = new Circle(50);
        midRing.setFill(Color.TRANSPARENT);
        midRing.setStroke(Color.web("#00b4a6", 0.25));
        midRing.setStrokeWidth(1);

        Circle innerCircle = new Circle(42);
        innerCircle.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(0.5, Color.web("#0d2137")),
            new Stop(1, Color.web("#00b4a6"))));
        innerCircle.setEffect(new DropShadow(30, Color.web("#00b4a6", 0.5)));

        Label pubLbl = new Label("PUB");
        pubLbl.setStyle(
            "-fx-text-fill:white;" +
            "-fx-font-size:20px;" +
            "-fx-font-weight:bold;" +
            "-fx-font-family:'Consolas';"
        );
        seal.getChildren().addAll(outerRing, midRing, innerCircle, pubLbl);

        // Rotate animation on outer ring
        RotateTransition rt = new RotateTransition(Duration.seconds(12), outerRing);
        rt.setByAngle(360); rt.setCycleCount(Timeline.INDEFINITE); rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

        Label uniName = new Label("Pundra University");
        uniName.setStyle(
            "-fx-text-fill:#e2e8f0;" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-wrap-text:true;" +
            "-fx-text-alignment:center;"
        );
        uniName.setMaxWidth(180);
        uniName.setAlignment(Pos.CENTER);

        Label uniName2 = new Label("of Science and Technology");
        uniName2.setStyle(
            "-fx-text-fill:#7a9cc0;" +
            "-fx-font-size:11px;" +
            "-fx-wrap-text:true;" +
            "-fx-text-alignment:center;"
        );
        uniName2.setMaxWidth(180);
        uniName2.setAlignment(Pos.CENTER);

        Label bogura = new Label("📍 Bogura, Bangladesh");
        bogura.setStyle(
            "-fx-text-fill:#2a4a70;" +
            "-fx-font-size:10px;"
        );

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color:#111f38;");
        sep1.setPrefWidth(160);

        Label sys = new Label("EMS v6");
        sys.setStyle(
            "-fx-text-fill:#00b4a6;" +
            "-fx-font-size:11px;" +
            "-fx-font-family:'Consolas';" +
            "-fx-font-weight:bold;"
        );

        Label year = new Label("Est. 2010");
        year.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:10px;");

        // Info boxes
        VBox infoBox1 = infoBox("👨‍🏫", "Faculty", "28");
        VBox infoBox2 = infoBox("🏛", "Depts", "10");
        VBox infoBox3 = infoBox("🚌", "Buses", "5");
        HBox infoRow = new HBox(8, infoBox1, infoBox2, infoBox3);
        infoRow.setAlignment(Pos.CENTER);

        leftPanel.getChildren().addAll(
            seal, uniName, uniName2, bogura,
            sep1, sys, year, infoRow
        );

        // ── RIGHT PANEL — Login Form ─────────────────────────────
        VBox rightPanel = new VBox(16);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(40, 40, 40, 40));
        rightPanel.setMinWidth(340);
        rightPanel.setStyle("-fx-background-color:#070e1c;");

        // Top bar (close button)
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        Button closeBtn = new Button("✕");
        closeBtn.setStyle(
            "-fx-background-color:rgba(239,68,68,0.12);" +
            "-fx-text-fill:#ef4444;" +
            "-fx-font-size:13px;" +
            "-fx-font-weight:bold;" +
            "-fx-cursor:hand;" +
            "-fx-padding:4 10;" +
            "-fx-background-radius:6;" +
            "-fx-border-color:rgba(239,68,68,0.3);" +
            "-fx-border-radius:6;"
        );
        closeBtn.setOnAction(e -> stage.close());
        topBar.getChildren().add(closeBtn);

        Label welcomeTitle = new Label("Welcome Back 👋");
        welcomeTitle.setStyle(
            "-fx-text-fill:#ffffff;" +
            "-fx-font-size:22px;" +
            "-fx-font-weight:bold;"
        );

        Label welcomeSub = new Label("Sign in to PUB EMS Dashboard");
        welcomeSub.setStyle(
            "-fx-text-fill:#7a9cc0;" +
            "-fx-font-size:12px;"
        );

        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color:#111f38;");

        // Username field
        VBox userBox = new VBox(6);
        Label userLbl = new Label("USERNAME");
        userLbl.setStyle(
            "-fx-text-fill:#00b4a6;" +
            "-fx-font-size:10px;" +
            "-fx-font-weight:bold;" +
            "-fx-font-family:'Consolas';"
        );
        TextField userField = new TextField("PUB");
        userField.setStyle(inp());
        userField.setPrefHeight(44);
        userBox.getChildren().addAll(userLbl, userField);

        // Password field
        VBox passBox = new VBox(6);
        Label passLbl = new Label("PASSWORD");
        passLbl.setStyle(
            "-fx-text-fill:#00b4a6;" +
            "-fx-font-size:10px;" +
            "-fx-font-weight:bold;" +
            "-fx-font-family:'Consolas';"
        );
        PasswordField passField = new PasswordField();
        passField.setStyle(inp());
        passField.setPrefHeight(44);
        passField.setPromptText("Enter your password...");
        passBox.getChildren().addAll(passLbl, passField);

        // Error label
        Label errorLbl = new Label("");
        errorLbl.setStyle(
            "-fx-text-fill:#ef4444;" +
            "-fx-font-size:11px;" +
            "-fx-font-weight:bold;"
        );
        errorLbl.setMinHeight(18);

        // Login Button
        Button loginBtn = new Button("🔓   LOGIN TO PUB EMS");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(46);
        loginBtn.setStyle(loginBtnStyle(false));
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(loginBtnStyle(true)));
        loginBtn.setOnMouseExited(e  -> loginBtn.setStyle(loginBtnStyle(false)));

        // Login logic
        Runnable doLogin = () -> {
            String u = userField.getText().trim();
            String p = passField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                errorLbl.setText("⚠  Username and password required!");
                shake(rightPanel);
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
                    shake(rightPanel);
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
        HBox footer = new HBox(6);
        footer.setAlignment(Pos.CENTER);
        Label footerLbl = new Label("Default:  PUB  /  PUB123");
        footerLbl.setStyle(
            "-fx-text-fill:#2a4a70;" +
            "-fx-font-size:10px;" +
            "-fx-font-family:'Consolas';"
        );
        footer.getChildren().add(footerLbl);

        rightPanel.getChildren().addAll(
            topBar, welcomeTitle, welcomeSub, sep2,
            userBox, passBox, errorLbl, loginBtn, footer
        );

        root.getChildren().addAll(leftPanel, rightPanel);

        // Drag from left panel
        final double[] drag = {0, 0};
        leftPanel.setOnMousePressed(e -> { drag[0]=e.getSceneX(); drag[1]=e.getSceneY(); });
        leftPanel.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX()-drag[0]);
            stage.setY(e.getScreenY()-drag[1]);
        });

        // Fade in animation
        root.setOpacity(0);
        Scene scene = new Scene(root, 560, 500);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), root);
        fadeIn.setFromValue(0); fadeIn.setToValue(1); fadeIn.play();

        stage.showAndWait();
        return success;
    }

    private VBox infoBox(String icon, String label, String value) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(8, 10, 8, 10));
        box.setStyle(
            "-fx-background-color:rgba(0,180,166,0.08);" +
            "-fx-border-color:rgba(0,180,166,0.2);" +
            "-fx-border-radius:8;" +
            "-fx-background-radius:8;"
        );
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size:14px;");
        Label val = new Label(value);
        val.setStyle(
            "-fx-text-fill:#00b4a6;" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;" +
            "-fx-font-family:'Consolas';"
        );
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:9px;");
        box.getChildren().addAll(ic, val, lbl);
        return box;
    }

    private void shake(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(60), node);
        tt.setFromX(0); tt.setByX(10);
        tt.setCycleCount(6); tt.setAutoReverse(true);
        tt.play();
    }

    private String loginBtnStyle(boolean hover) {
        return hover
            ? "-fx-background-color:linear-gradient(to right,#2563eb,#00d4c4);" +
              "-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:14px;" +
              "-fx-background-radius:10;-fx-cursor:hand;" +
              "-fx-effect:dropshadow(gaussian,rgba(0,212,196,0.6),20,0,0,4);"
            : "-fx-background-color:linear-gradient(to right,#1d4ed8,#00b4a6);" +
              "-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:14px;" +
              "-fx-background-radius:10;-fx-cursor:hand;" +
              "-fx-effect:dropshadow(gaussian,rgba(0,180,166,0.4),14,0,0,3);";
    }

    private String inp() {
        return
            "-fx-background-color:#0a1628;" +
            "-fx-border-color:#1a3a5c;" +
            "-fx-border-radius:9;-fx-background-radius:9;" +
            "-fx-text-fill:#e2e8f0;" +
            "-fx-prompt-text-fill:#2a4a70;" +
            "-fx-font-size:14px;-fx-padding:10 14;";
    }
}