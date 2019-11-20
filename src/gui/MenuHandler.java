package src.gui;

import java.util.*;
import java.util.Map.Entry;

public class MenuHandler {

    // Map pour garantir l'unicité des clés
    private Map<Integer,String> options;
    private Integer optionsCount;

    public MenuHandler(){
        optionsCount = 0;
        options = new HashMap<>();
    }

    public void addOption(String line){
        optionsCount++;
        options.put(optionsCount,line);
    }

    public Integer getIntResponse(){
        Scanner scanner = new Scanner(System.in);
        String selectedLine = "";
        Integer userEntry = -1; // valeur réservé pour ça

        while( selectedLine == null || selectedLine.isEmpty()){
            System.out.println("Options :");
            Set<Entry<Integer, String>> setOptions = options.entrySet();
            Iterator<Entry<Integer, String>> it = setOptions.iterator();
            while(it.hasNext()){
                Entry<Integer, String> e = it.next();
                System.out.println(e.getKey() + " ) " + e.getValue());
            }
            System.out.println("Entrez le numéro associé à l'option choisie.");

            try {
                userEntry = Integer.parseInt( scanner.next() );
                selectedLine = options.get(userEntry);

                if(selectedLine == null || selectedLine.isEmpty() )
                    System.err.println("Erreur : Le numéro entré est inconnu ! Entrez le numéro figurant devant l'option voulue !");

            }catch (NumberFormatException e){
                System.err.println("Erreur de format : L'entrée doit être le numéro de l'option.");
            }

            if(selectedLine == null || selectedLine.isEmpty() )
                System.err.println("Erreur : Le numéro entré est inconnu ! Entrez le numéro figurant devant l'option voulue !");
        }
        return userEntry;
    }

    // Attention pour remove, maintenir le compteur.
}
