package hangman_editor;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WordListEditor extends Stage {
	
    private TextArea wortListDisplay;  
    private TextField newWortField;     
    private final String wortListFile = "wortList.txt"; 

    public WordListEditor(Stage owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Wort List Editor");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        //Setzen  TextArea
        wortListDisplay = new TextArea();
        wortListDisplay.setEditable(false); 
        //Setzen  TextField
        newWortField = new TextField();
        newWortField.setPromptText("Neues Wort eingeben");
        //Setzen die Buttons
        Button addWordButton = new Button("Add Wort");
     
        //Event handlers für Buttons
        addWordButton.setOnAction(event -> addWort());
   
        //hinzufügen Labels und Buttons
        root.getChildren().addAll(new Label("Wort Liste:"), wortListDisplay, 
                                   new Label("Neues Wort eingeben:"), newWortField, 
                                   addWordButton);
        
        Scene scene = new Scene(root, 300, 400);
        setScene(scene);
        //laden die WortList am Anfang
        loadWortList(); 
    }
    //Methode um die WortList zu laden
    private void loadWortList() {
        File file = new File(wortListFile);
        
        // Überprüfen, ob die Datei existiert
        if (!file.exists()) {
            // Wenn nicht, eine neue Datei erstellen 
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Wenn keine Wörter vorhanden sind, setzen die erste Zeile mit "0" als Wortanzahl
                writer.write("0\n");  
            } catch (IOException e) {
                wortListDisplay.setText("Fehler: Wortliste-Datei konnte nicht erstellt werden");
                return;
            }
        }

        try {
            // Alle Zeilen der Datei lesen
            List<String> lines = Files.readAllLines(Paths.get(wortListFile));
            StringBuilder content = new StringBuilder();
            
            // Die erste Zeile enthält die Anzahl der Wörter
            content.append("Wörter insgesamt: ").append(lines.get(0)).append("\n\n");

            // Die restlichen Zeilen enthalten die Wörter in der Liste
            for (int i = 1; i < lines.size(); i++) {
                content.append(lines.get(i)).append("\n");
            }
            
            // Setzen den Text  auf den gelesenen Inhalt
            wortListDisplay.setText(content.toString());
        } catch (IOException e) {
            wortListDisplay.setText("Fehler: Wortliste-Datei konnte nicht gelesen werden");
        }
    }

    //Wenn kein Wort im WortList.txt existiert, mussen wir die erste Linie als "0" setzen
    private void addWort() {
    	 String neuesWort = newWortField.getText().trim();
    	    
    	    // Sicherstellen, dass die Eingabe nur Buchstaben enthält
    	    if (!neuesWort.matches("[a-zA-ZäöüÄÖÜß]+")) {
    	        wortListDisplay.setText("Fehler: Nur Buchstaben sind erlaubt.");
    	        return;
    	    }

    	    if (!neuesWort.isEmpty()) {
    	        try (RandomAccessFile file = new RandomAccessFile(wortListFile, "rw")) {
    	            // Lesen die Wortanzahl
    	            String ersteLinie = file.readLine();
    	            int wortCount = 0;

    	            // Sicherstellen, dass die erste Linie eine gültige Zahl ist
    	            if (ersteLinie != null && !ersteLinie.trim().isEmpty()) {
    	                try {
    	                    wortCount = Integer.parseInt(ersteLinie.trim());
    	                } catch (NumberFormatException e) {
    	                    wortListDisplay.setText("Fehler: Ungültige Anzahl der Wörter im Dateiheader.");
    	                    return;
    	                }
    	            }

    	            // Fügen das neue Wort hinzu
    	            file.seek(file.length());
    	            file.writeBytes(neuesWort + "\n");

    	            // Aktualisieren die Wortanzahl
    	            file.seek(0);
    	            file.writeBytes(String.valueOf(wortCount + 1) + "\n");

    	            newWortField.clear();
    	            // Aktualisieren die Wortliste
    	            loadWortList();
    	        } catch (IOException e) {
    	            wortListDisplay.setText("Fehler: " + e.getMessage());
    	        }
    	    } else {
    	        wortListDisplay.setText("Geben Sie ein Wort ein");
    	    }
        }
    }


