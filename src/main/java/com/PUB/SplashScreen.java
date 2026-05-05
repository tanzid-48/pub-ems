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

        // ── Root ─────────────────────────────────────────────────
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:#06101e;");

        // ── Background circles (decorative) ──────────────────────
        StackPane bg = new StackPane();
        Circle bgC1 = new Circle(220);
        bgC1.setFill(Color.web("#0d2137", 0.6));
        bgC1.setTranslateX(-180); bgC1.setTranslateY(-120);

        Circle bgC2 = new Circle(160);
        bgC2.setFill(Color.web("#00b4a6", 0.04));
        bgC2.setTranslateX(160); bgC2.setTranslateY(120);

        Circle bgC3 = new Circle(80);
        bgC3.setFill(Color.web("#2563eb", 0.06));
        bgC3.setTranslateX(180); bgC3.setTranslateY(-140);

        bg.getChildren().addAll(bgC1, bgC2, bgC3);

        // ── Main content ─────────────────────────────────────────
        VBox content = new VBox(18);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));

        // ── Logo with rings ───────────────────────────────────────
        StackPane logo = new StackPane();

        Circle ring3 = new Circle(72);
        ring3.setFill(Color.TRANSPARENT);
        ring3.setStroke(Color.web("#00b4a6", 0.15));
        ring3.setStrokeWidth(1);
        ring3.getStrokeDashArray().addAll(4.0, 8.0);

        Circle ring2 = new Circle(60);
        ring2.setFill(Color.TRANSPARENT);
        ring2.setStroke(Color.web("#00b4a6", 0.3));
        ring2.setStrokeWidth(1.5);
        ring2.getStrokeDashArray().addAll(6.0, 6.0);

        Circle ring1 = new Circle(48);
        ring1.setFill(Color.TRANSPARENT);
        ring1.setStroke(Color.web("#2563eb", 0.5));
        ring1.setStrokeWidth(2);

        Circle inner = new Circle(38);
        inner.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(0.5, Color.web("#0d2137")),
            new Stop(1, Color.web("#00b4a6"))));
        inner.setEffect(new DropShadow(30, Color.web("#00b4a670")));

        Label pubLabel = new Label("PUB");
        pubLabel.setStyle(
            "-fx-text-fill:white;" +
            "-fx-font-size:18px;" +
            "-fx-font-weight:bold;" +
            "-fx-font-family:'Consolas';"
        );

        logo.getChildren().addAll(ring3, ring2, ring1, inner, pubLabel);

        // Ring animations
        RotateTransition r3 = new RotateTransition(Duration.seconds(20), ring3);
        r3.setByAngle(360); r3.setCycleCount(Timeline.INDEFINITE);
        r3.setInterpolator(Interpolator.LINEAR); r3.play();

        RotateTransition r2 = new RotateTransition(Duration.seconds(12), ring2);
        r2.setByAngle(-360); r2.setCycleCount(Timeline.INDEFINITE);
        r2.setInterpolator(Interpolator.LINEAR); r2.play();

        // Scale pulse on inner circle
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.5), inner);
        pulse.setFromX(1.0); pulse.setToX(1.08);
        pulse.setFromY(1.0); pulse.setToY(1.08);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true); pulse.play();

        // ── Title ─────────────────────────────────────────────────
        Label title = new Label("PUB EMS v6");
        title.setStyle(
            "-fx-text-fill:white;" +
            "-fx-font-size:32px;" +
            "-fx-font-weight:bold;"
        );
        title.setEffect(new DropShadow(10, Color.web("#00b4a650")));

        Label subtitle = new Label("Employee Management System");
        subtitle.setStyle(
            "-fx-text-fill:#00b4a6;" +
            "-fx-font-size:14px;" +
            "-fx-font-weight:bold;"
        );

        Label uniName = new Label("Pundra University of Science and Technology");
        uniName.setStyle(
            "-fx-text-fill:#7a9cc0;" +
            "-fx-font-size:13px;"
        );

        Label location = new Label("📍  Bogura, Bangladesh");
        location.setStyle(
            "-fx-text-fill:#2a4a70;" +
            "-fx-font-size:11px;"
        );

        // ── Stats row ─────────────────────────────────────────────
        HBox statsRow = new HBox(16);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
            statBox("👥", "37", "Employees"),
            statBox("🏛", "10", "Departments"),
            statBox("🎓", "28", "Faculty"),
            statBox("🚌", "5",  "Bus Routes")
        );

        // ── Progress section ──────────────────────────────────────
        VBox progressSection = new VBox(10);
        progressSection.setAlignment(Pos.CENTER);
        progressSection.setMaxWidth(380);

        Label statusLbl = new Label("Initializing system...");
        statusLbl.setStyle(
            "-fx-text-fill:#2a4a70;" +
            "-fx-font-size:12px;" +
            "-fx-font-family:'Consolas';"
        );

        // Custom progress bar using StackPane
        StackPane progressBg = new StackPane();
        progressBg.setPrefWidth(380);
        progressBg.setPrefHeight(8);
        progressBg.setMaxWidth(380);

        Rectangle trackRect = new Rectangle(380, 8);
        trackRect.setFill(Color.web("#0a1628"));
        trackRect.setArcWidth(8); trackRect.setArcHeight(8);
        trackRect.setStroke(Color.web("#111f38")); trackRect.setStrokeWidth(1);

        Rectangle fillRect = new Rectangle(0, 8);
        fillRect.setFill(new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(0.5, Color.web("#00b4a6")),
            new Stop(1, Color.web("#00d4c4"))));
        fillRect.setArcWidth(8); fillRect.setArcHeight(8);
        fillRect.setEffect(new DropShadow(6, Color.web("#00b4a6")));

        StackPane.setAlignment(fillRect, Pos.CENTER_LEFT);
        progressBg.getChildren().addAll(trackRect, fillRect);

        // Percentage label
        Label pctLbl = new Label("0%");
        pctLbl.setStyle(
            "-fx-text-fill:#00b4a6;" +
            "-fx-font-size:11px;" +
            "-fx-font-family:'Consolas';" +
            "-fx-font-weight:bold;"
        );

        progressSection.getChildren().addAll(statusLbl, progressBg, pctLbl);

        content.getChildren().addAll(
            logo, title, subtitle, uniName, location,
            statsRow, progressSection
        );

        root.getChildren().addAll(bg, content);

        // ── Animations ────────────────────────────────────────────
        String[] messages = {
            "Connecting to database...",
            "Loading employee records...",
            "Preparing analytics dashboard...",
            "Loading department data...",
            "Setting up bus schedule...",
            "Almost ready — launching EMS..."
        };

        // Fade in
        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0); fadeIn.setToValue(1); fadeIn.play();

        // Progress animation
        Timeline tl = new Timeline();
        for (int i = 0; i <= 100; i++) {
            final double val = i / 100.0;
            final int msgIdx = Math.min((int)(val * messages.length), messages.length - 1);
            final double barWidth = val * 380;
            final int pct = i;

            tl.getKeyFrames().add(new KeyFrame(Duration.millis(i * 25), e -> {
                fillRect.setWidth(barWidth);
                statusLbl.setText(messages[msgIdx]);
                pctLbl.setText(pct + "%");
            }));
        }

        tl.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
            fadeOut.setFromValue(1); fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                stage.close();
                onFinish.run();
            });
            fadeOut.play();
        });

        Scene scene = new Scene(root, 480, 520);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        tl.play();
    }

    private VBox statBox(String icon, String value, String label) {
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 16, 10, 16));
        box.setStyle(
            "-fx-background-color:rgba(0,180,166,0.06);" +
            "-fx-border-color:rgba(0,180,166,0.18);" +
            "-fx-border-radius:10;" +
            "-fx-background-radius:10;"
        );
        Label ic  = new Label(icon);  ic.setStyle("-fx-font-size:16px;");
        Label val = new Label(value); val.setStyle(
            "-fx-text-fill:#00b4a6;-fx-font-size:16px;" +
            "-fx-font-weight:bold;-fx-font-family:'Consolas';");
        Label lbl = new Label(label); lbl.setStyle(
            "-fx-text-fill:#2a4a70;-fx-font-size:10px;");
        box.getChildren().addAll(ic, val, lbl);
        return box;
    }
}