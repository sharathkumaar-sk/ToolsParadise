package toolsparadise;

import java.security.SecureRandom;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

class PasswordGeneratorUI {
    private Stage stage;
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<>?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;
    private static final SecureRandom random = new SecureRandom();

    public PasswordGeneratorUI(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Screen screen = Screen.getPrimary();
        double width = screen.getBounds().getWidth();
        double height = screen.getBounds().getHeight();
        
        Text title = new Text("Password Generator");
        title.setStyle("-fx-font-size: 30px; -fx-fill: white; -fx-font-weight: bold;");

        // Left Section (App Name, Username, Radio Buttons)
        Label appNameLabel = new Label("App/Site Name:");
        appNameLabel.setStyle("-fx-text-fill: white;");
        TextField appNameField = createStyledTextField();

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: white;");
        TextField usernameField = createStyledTextField();

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton generateRadio = new RadioButton("Generate Password");
        RadioButton enterRadio = new RadioButton("Enter Existing Password");
        generateRadio.setToggleGroup(toggleGroup);
        enterRadio.setToggleGroup(toggleGroup);
        generateRadio.setSelected(true);
        
        VBox leftSection = new VBox(15, appNameLabel, appNameField, usernameLabel, usernameField, generateRadio, enterRadio);
        leftSection.setAlignment(Pos.CENTER);
        leftSection.setPadding(new Insets(40));

        // Right Section (Password Controls)
        Label passwordLengthLabel = new Label("Password Length:");
        passwordLengthLabel.setStyle("-fx-text-fill: white;");
        Slider passwordLengthSlider = new Slider(8, 20, 12);
        passwordLengthSlider.setShowTickLabels(true);
        passwordLengthSlider.setShowTickMarks(true);
        passwordLengthSlider.setMajorTickUnit(4);
        passwordLengthSlider.setBlockIncrement(1);
        
        Label generatedPasswordLabel = new Label("Generated Password:");
        generatedPasswordLabel.setStyle("-fx-text-fill: white;");
        TextArea generatedPasswordArea = createStyledTextArea();

        Label existingPasswordLabel = new Label("Enter Password:");
        existingPasswordLabel.setStyle("-fx-text-fill: white;");
        TextField existingPasswordField = createStyledTextField();
        existingPasswordField.setVisible(false);
        existingPasswordLabel.setVisible(false);

        Button generateBtn = createStyledButton("Generate Password");
        generateBtn.setOnAction(e -> generatedPasswordArea.setText(generatePassword((int) passwordLengthSlider.getValue())));
        
        Button saveButton = createStyledButton("💾 Save");
        Button backButton = createStyledButton("🔙 Back");
        backButton.setOnAction(e -> new ToolsParadise().start(stage));
        
        VBox rightSection = new VBox(15, passwordLengthLabel, passwordLengthSlider, generatedPasswordLabel, generatedPasswordArea, generateBtn, existingPasswordLabel, existingPasswordField, saveButton, backButton);
        rightSection.setAlignment(Pos.CENTER);
        rightSection.setPadding(new Insets(40));
        
        generateRadio.setOnAction(e -> {
            passwordLengthSlider.setVisible(true);
            passwordLengthLabel.setVisible(true);
            generatedPasswordLabel.setVisible(true);
            generatedPasswordArea.setVisible(true);
            generateBtn.setVisible(true);
            existingPasswordLabel.setVisible(false);
            existingPasswordField.setVisible(false);
            rightSection.getChildren().remove(existingPasswordLabel);
            rightSection.getChildren().remove(existingPasswordField);
        });

        enterRadio.setOnAction(e -> {
            passwordLengthSlider.setVisible(false);
            passwordLengthLabel.setVisible(false);
            generatedPasswordLabel.setVisible(false);
            generatedPasswordArea.setVisible(false);
            generateBtn.setVisible(false);
            existingPasswordLabel.setVisible(true);
            existingPasswordField.setVisible(true);
            rightSection.getChildren().add(0, existingPasswordLabel);
            rightSection.getChildren().add(1, existingPasswordField);
        });
        
        HBox mainLayout = new HBox(60, leftSection, rightSection);
        mainLayout.setAlignment(Pos.CENTER);

        VBox rootLayout = new VBox(30, title, mainLayout);
        rootLayout.setAlignment(Pos.TOP_CENTER);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setStyle("-fx-background-color: #121212;");
        
        Scene scene = new Scene(rootLayout, width, height);
        stage.setScene(scene);
    }

    private String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }
        return password.toString();
    }
    
    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color: #1f1f1f; -fx-text-fill: white;");
        return textField;
    }

    private TextArea createStyledTextArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setStyle("-fx-background-color: #1f1f1f; -fx-text-fill: white;");
        return textArea;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #1f1f1f; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10px;");
        return button;
    }
}
