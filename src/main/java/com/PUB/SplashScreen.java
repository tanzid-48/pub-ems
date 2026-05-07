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

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color:#06101e;");

        VBox content = new VBox(16);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));

        StackPane logo = new StackPane();
        Circle ring2 = new Circle(58);
        ring2.setFill(Color.TRANSPARENT);
        ring2.setStroke(Color.web("#00b4a6", 0.3));
        ring2.setStrokeWidth(1.5);
        ring2.getStrokeDashArray().addAll(6.0, 6.0);

        Circle ring1 = new Circle(46);
        ring1.setFill(Color.TRANSPARENT);
        ring1.setStroke(Color.web("#2563eb", 0.5));
        ring1.setStrokeWidth(2);

        Circle inner = new Circle(36);
        inner.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(1, Color.web("#00b4a6"))));
        inner.setEffect(new DropShadow(28, Color.web("#00b4a660")));

        Label pubLabel = new Label("PUB");
        pubLabel.setStyle(
            "-fx-text-fill:white;-fx-font-size:16px;" +
            "-fx-font-weight:bold;-fx-font-family:'Consolas';"
        );
        logo.getChildren().addAll(ring2, ring1, inner, pubLabel);

        RotateTransition r2 = new RotateTransition(Duration.seconds(12), ring2);
        r2.setByAngle(-360);
        r2.setCycleCount(Animation.INDEFINITE);
        r2.setInterpolator(Interpolator.LINEAR);
        r2.play();

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(1.5), inner);
        pulse.setFromX(1.0); pulse.setToX(1.08);
        pulse.setFromY(1.0); pulse.setToY(1.08);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        Label title = new Label("PUB EMS v6");
        title.setStyle("-fx-text-fill:white;-fx-font-size:30px;-fx-font-weight:bold;");
        title.setEffect(new DropShadow(10, Color.web("#00b4a640")));

        Label subtitle = new Label("Employee Management System");
        subtitle.setStyle("-fx-text-fill:#00b4a6;-fx-font-size:14px;-fx-font-weight:bold;");

        Label uniName = new Label("Pundra University of Science and Technology");
        uniName.setStyle("-fx-text-fill:#7a9cc0;-fx-font-size:13px;");

        Label location = new Label("Bogura, Bangladesh");
        location.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:11px;");

        HBox statsRow = new HBox(14);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
            statBox("37", "Employees"),
            statBox("10", "Departments"),
            statBox("28", "Faculty"),
            statBox("5",  "Bus Routes")
        );

        Label statusLbl = new Label("Initializing system...");
        statusLbl.setStyle(
            "-fx-text-fill:#2a4a70;-fx-font-size:12px;-fx-font-family:'Consolas';"
        );

        StackPane progressBg = new StackPane();
        progressBg.setPrefWidth(380);
        progressBg.setPrefHeight(8);
        progressBg.setMaxWidth(380);

        javafx.scene.shape.Rectangle track = new javafx.scene.shape.Rectangle(380, 8);
        track.setFill(Color.web("#0a1628"));
        track.setArcWidth(8); track.setArcHeight(8);
        track.setStroke(Color.web("#111f38")); track.setStrokeWidth(1);

        javafx.scene.shape.Rectangle fill = new javafx.scene.shape.Rectangle(0, 8);
        fill.setFill(new LinearGradient(0,0,1,0,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(0.5, Color.web("#00b4a6")),
            new Stop(1, Color.web("#00d4c4"))));
        fill.setArcWidth(8); fill.setArcHeight(8);
        fill.setEffect(new DropShadow(6, Color.web("#00b4a6")));

        StackPane.setAlignment(fill, Pos.CENTER_LEFT);
        progressBg.getChildren().addAll(track, fill);

        Label pctLbl = new Label("0%");
        pctLbl.setStyle(
            "-fx-text-fill:#00b4a6;-fx-font-size:11px;" +
            "-fx-font-family:'Consolas';-fx-font-weight:bold;"
        );

        content.getChildren().addAll(
            logo, title, subtitle, uniName, location,
            statsRow, statusLbl, progressBg, pctLbl
        );
        root.getChildren().add(content);

        String[] messages = {
            "Connecting to database...",
            "Loading employee records...",
            "Preparing analytics dashboard...",
            "Loading department data...",
            "Almost ready..."
        };

        root.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0); fadeIn.setToValue(1); fadeIn.play();

        Timeline tl = new Timeline();
        for (int i = 0; i <= 100; i++) {
            final double val     = i / 100.0;
            final int    msgIdx  = Math.min((int)(val * messages.length), messages.length - 1);
            final double barW    = val * 380;
            final int    pct     = i;
            tl.getKeyFrames().add(new KeyFrame(Duration.millis(i * 24), e -> {
                fill.setWidth(barW);
                statusLbl.setText(messages[msgIdx]);
                pctLbl.setText(pct + "%");
            }));
        }

        tl.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(350), root);
            fadeOut.setFromValue(1); fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                stage.close();
                onFinish.run();
            });
            fadeOut.play();
        });

        Scene scene = new Scene(root, 460, 500);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        tl.play();
    }

    private VBox statBox(String value, String label) {
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(9, 14, 9, 14));
        box.setStyle(
            "-fx-background-color:rgba(0,180,166,0.06);" +
            "-fx-border-color:rgba(0,180,166,0.18);" +
            "-fx-border-radius:10;-fx-background-radius:10;"
        );
        Label val = new Label(value);
        val.setStyle(
            "-fx-text-fill:#00b4a6;-fx-font-size:16px;" +
            "-fx-font-weight:bold;-fx-font-family:'Consolas';"
        );
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill:#2a4a70;-fx-font-size:10px;");
        box.getChildren().addAll(val, lbl);
        return box;
    }
}