package hangman_editor;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        WordListEditor editor = new WordListEditor(primaryStage);
        editor.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
