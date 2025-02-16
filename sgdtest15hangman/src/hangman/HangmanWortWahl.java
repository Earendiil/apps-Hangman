package hangman;

import java.io.*;
import java.util.*;

//Diese class dient dazu, ein zufälliges Wort auszuwählen
public class HangmanWortWahl {
	//Das zufällig ausgewählte Wort für das Spiel
    private String geheimWort;
    
    private String wordListFile = "wortList.txt";

    public HangmanWortWahl() {
        ladenRandomWort();
    }
    //Die Methode  liest alle Wörter aus der Datei in eine Liste ein 
    //und wählt dann zufällig ein Wort aus
    private void ladenRandomWort() {
    	//Liste zu speichern alle die Wörter
        List<String> wortList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(wordListFile))) {
            String line = reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                wortList.add(line.trim());
            }
         // Prüfen, dass die Liste nicht leer ist
            if (!wortList.isEmpty()) {
            	// Zufällig ein Wort aus der Liste auswählen
                Random random = new Random();
                geheimWort = wortList.get(random.nextInt(wortList.size()));
            } else {
            	// "default" setzen, falls die Liste leer ist
                geheimWort = "default"; 
            }
        } catch (IOException e) {
            System.err.println("Error reading word list file: " + e.getMessage());
            geheimWort = "default"; 
        }
    }
    // Gibt das ausgewählte Wort zurück
    public String getSelectedWord() {
        return geheimWort;
    }

    
}
