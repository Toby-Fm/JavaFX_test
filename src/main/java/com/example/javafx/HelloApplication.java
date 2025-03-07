package com.example.javafx;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.util.Objects;

public class HelloApplication extends Application {
    private VBox sidebar;
    private boolean sidebarExpanded = true;
    private BorderPane root;
    private TabPane tabPane;
    private MFXButton toggleSidebarButton;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();

        // Sidebar erstellen
        sidebar = createSidebar();
        root.setLeft(sidebar);

        // TabPane für verschiedene Seiten
        tabPane = new TabPane();
        tabPane.getTabs().addAll(createDashboardTab(), createSettingsTab(), createToDoTab());
        root.setCenter(tabPane);

        // Szene erstellen
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        // Stage konfigurieren
        stage.setTitle("🚀 MaterialFX & JavaFX UI");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Erstellt das Sidebar-Menü mit Animation
     */
    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #242424;");
        sidebar.setPrefWidth(250);

        Label title = new Label("📁 Navigation");
        title.getStyleClass().add("sidebar-title");

        MFXButton btnDashboard = createSidebarButton("🏠 Dashboard");
        MFXButton btnSettings = createSidebarButton("⚙ Einstellungen");
        MFXButton btnToDo = createSidebarButton("✅ To-Do Liste");

        btnDashboard.setOnAction(e -> tabPane.getSelectionModel().select(0));
        btnSettings.setOnAction(e -> tabPane.getSelectionModel().select(1));
        btnToDo.setOnAction(e -> tabPane.getSelectionModel().select(2));

        // Toggle Button
        toggleSidebarButton = new MFXButton("⬅");
        toggleSidebarButton.getStyleClass().add("toggle-sidebar-btn");
        toggleSidebarButton.setOnAction(e -> toggleSidebar());

        VBox.setVgrow(toggleSidebarButton, Priority.ALWAYS);
        sidebar.getChildren().addAll(title, btnDashboard, btnSettings, btnToDo, new Spacer(), toggleSidebarButton);

        return sidebar;
    }

    /**
     * Sidebar Animation für sanftes Ein- und Ausklappen
     */
    private void toggleSidebar() {
        double targetX = sidebarExpanded ? -200 : 0;
        double contentShift = sidebarExpanded ? -150 : 0;

        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), sidebar);
        sidebarTransition.setToX(targetX);
        sidebarTransition.play();

        TranslateTransition contentTransition = new TranslateTransition(Duration.millis(300), tabPane);
        contentTransition.setToX(contentShift);
        contentTransition.play();

        toggleSidebarButton.setText(sidebarExpanded ? "➡" : "⬅");
        sidebarExpanded = !sidebarExpanded;
    }

    /**
     * Erstellt einen MaterialFX Button für die Sidebar
     */
    private MFXButton createSidebarButton(String text) {
        MFXButton button = new MFXButton(text);
        button.getStyleClass().add("sidebar-btn");
        return button;
    }

    /**
     * Erstellt das Dashboard-Tab mit Buttons und Slider
     */
    private Tab createDashboardTab() {
        Tab tab = new Tab("🏠 Dashboard");
        tab.setClosable(false);

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Willkommen im Dashboard!");
        title.getStyleClass().add("main-title");

        MFXButton dialogButton = new MFXButton("📢 Info anzeigen");
        dialogButton.setOnAction(e -> showInfoDialog());

        MFXProgressBar progressBar = new MFXProgressBar();
        progressBar.setProgress(0.5);

        MFXSlider slider = new MFXSlider(0, 100, 50);

        content.getChildren().addAll(title, dialogButton, progressBar, slider);
        tab.setContent(content);
        return tab;
    }

    /**
     * Erstellt ein modales Info-Dialogfenster mit MaterialFX
     */
    private void showInfoDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Info");

        VBox dialogVBox = new VBox(20);
        dialogVBox.setPadding(new Insets(20));
        dialogVBox.getChildren().add(new Label("🚀 JavaFX & MaterialFX funktionieren großartig!"));

        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    /**
     * Erstellt ein Einstellungen-Tab mit Input-Feldern
     */
    private Tab createSettingsTab() {
        Tab tab = new Tab("⚙ Einstellungen");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        MFXTextField nameField = new MFXTextField();
        nameField.setPromptText("Dein Name");

        MFXPasswordField passwordField = new MFXPasswordField();
        passwordField.setPromptText("Passwort");

        MFXCheckbox darkMode = new MFXCheckbox("🌙 Dark Mode aktivieren");

        content.getChildren().addAll(new Label("Benutzer-Einstellungen"), nameField, passwordField, darkMode);
        tab.setContent(content);

        return tab;
    }

    /**
     * Erstellt eine To-Do-Liste mit MaterialFX ListView
     */
    private Tab createToDoTab() {
        Tab tab = new Tab("✅ To-Do Liste");
        tab.setClosable(false);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label title = new Label("📌 Deine Aufgaben:");
        title.getStyleClass().add("main-title");

        MFXLegacyListView<String> toDoList = new MFXLegacyListView<>();
        MFXTextField inputField = new MFXTextField();
        HBox inputRow = new HBox(10, inputField, new MFXButton("Hinzufügen"), new MFXButton("Löschen"));

        content.getChildren().addAll(title, inputRow, toDoList);
        tab.setContent(content);

        return tab;
    }
    /**
     * Platzhalter für dynamisches Layout (Spacer)
     */
    private static class Spacer extends Region {
        public Spacer() {
            VBox.setVgrow(this, Priority.ALWAYS);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
