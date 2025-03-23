package toolsparadise;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ToolsParadise extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("ToolsParadise");
        primaryStage.setMaximized(true); // Maximize window at start

        showMainMenu();
    }

    private void showMainMenu() {
        // 🏷 Title at the top-center (No flickering)
        Text title = new Text("ToolsParadise");
        title.setStyle("-fx-font-size: 40px; -fx-fill: white; -fx-font-weight: bold;");
        
        // 🔘 Buttons
        Button passwordGenBtn = createStyledButton("🔑 Password Generator", () -> new PasswordGeneratorUI(primaryStage).show());
        Button passwordMgrBtn = createStyledButton("🗄 Password Manager", () -> new PasswordManagerUI(primaryStage).show());
        Button exitBtn = createStyledButton("❌ Exit", () -> primaryStage.close());

        VBox layout = new VBox(20, title, passwordGenBtn, passwordMgrBtn, exitBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #121212;");

        // 📌 Use BorderPane to align components properly
        BorderPane root = new BorderPane();
        root.setCenter(layout);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        // Get the screen dimensions BEFORE showing the stage
        Screen screen = Screen.getPrimary();
        double width = screen.getBounds().getWidth();
        double height = screen.getBounds().getHeight();

        // Apply dimensions BEFORE showing
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #1f1f1f; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 12px 24px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px; -fx-min-width: 250px; -fx-min-height: 50px;");
        button.setOnAction(e -> action.run());
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 12px 24px; -fx-min-width: 250px; -fx-min-height: 50px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #1f1f1f; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 12px 24px; -fx-min-width: 250px; -fx-min-height: 50px;"));
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
