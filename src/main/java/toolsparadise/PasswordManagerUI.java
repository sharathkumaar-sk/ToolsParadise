package toolsparadise;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PasswordManagerUI {
    private Stage stage;

    public PasswordManagerUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Text title = new Text("Password Manager");
        title.setStyle("-fx-font-size: 24px; -fx-fill: white; -fx-font-weight: bold;");

        Button backButton = new Button("🔙 Back");
        backButton.setStyle("-fx-background-color: #1f1f1f; -fx-text-fill: white; -fx-font-size: 14px;");
        backButton.setOnAction(e -> new ToolsParadise().start(stage)); // Navigate back

        VBox layout = new VBox(20, title, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #121212;");

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
    }
}
