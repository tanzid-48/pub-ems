package com.PUB;

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

public class SplashScreen {

    public void show(Runnable onFinish) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        // ── Background ──────────────────────────────────────────
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color:#06101e;");

        // ── Logo Circle ─────────────────────────────────────────
        StackPane logo = new StackPane();
        Circle circle = new Circle(55);
        circle.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(1, Color.web("#00b4a6"))));
        circle.setEffect(new DropShadow(30, Color.web("#00b4a680")));
        Label pl = new Label("PUB");
        pl.setStyle("-fx-text-fill:white;-fx-font-size:20px;-fx-font-weight:bold;");
        logo.getChildren().addAll(circle, pl);

        // ── Title ───────────────────────────────────────────────
        Label title = new Label("PUB EMS v6");
        title.setStyle("-fx-text-fill:white;-fx-font-size:28px;-fx-font-weight:bold;");

        Label subtitle = new Label("Pundra University of Science and Technology");
        subtitle.setStyle("-fx-text-fill:#7a9cc0;-fx-font-size:13px;");

        Label location = new Label("Bogura, Bangladesh");
        location.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:11px;");

        // ── Progress Bar ────────────────────────────────────────
        ProgressBar pb = new ProgressBar(0);
        pb.setPrefWidth(320);
        pb.setPrefHeight(6);
        pb.setStyle("-fx-accent:#00b4a6;-fx-background-color:#0a1628;-fx-background-radius:4;");

        // ── Status Label ────────────────────────────────────────
        Label status = new Label("Initializing...");
        status.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:11px;");

        root.getChildren().addAll(logo, title, subtitle, location,
                new Label(""), pb, status);

        // ── Animation ───────────────────────────────────────────
        String[] messages = {
            "Connecting to database...",
            "Loading faculty data...",
            "Preparing dashboard...",
            "Almost ready..."
        };

        Timeline tl = new Timeline();
        for (int i = 0; i <= 100; i++) {
            final double val = i / 100.0;
            final int idx = Math.min((int)(val * messages.length), messages.length - 1);
            tl.getKeyFrames().add(new KeyFrame(Duration.millis(i * 22), e -> {
                pb.setProgress(val);
                status.setText(messages[idx]);
            }));
        }

        tl.setOnFinished(e -> {
            stage.close();
            onFinish.run();
        });

        Scene scene = new Scene(root, 420, 320);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        tl.play();
    }
}