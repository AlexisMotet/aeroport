package core.outils;

import core.elements.Aeroport;
import core.elements.Emplacement;
import core.elements.Piste;
import core.elements.Terminal;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class ParserConfig {
    public static void parse(String cheminConfig, Aeroport aeroport)
    {
        File file = new File(cheminConfig);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            HashMap<String, Piste> mapPistes = new HashMap<>();
            HashMap<String, Terminal> mapTerminaux = new HashMap<>();

            System.out.println("========== DEBUT PARSING ==========");
            String line;
            while ((line = br.readLine()) != null)
            {
                Scanner scanner = new Scanner(line);
                scanner.useDelimiter(" ");
                switch (scanner.next()) {
                    case "AJOUTER_PISTE" -> {
                        String nomPiste = scanner.next();
                        if (mapPistes.containsKey(nomPiste)){
                            throw new Exception("Le nom de la piste a deja ete donne");
                        }
                        Piste piste = aeroport.ajouterPiste();
                        mapPistes.put(nomPiste, piste);
                        System.out.printf("Piste %s ajoute a l'aeroport\n", nomPiste);
                    }
                    case "AJOUTER_TERMINAL" -> {
                        String nomPiste = scanner.next();
                        Piste piste = mapPistes.get(nomPiste);
                        Terminal terminal = piste.ajouterTerminal();
                        String nomTerminal = scanner.next();
                        if (mapTerminaux.containsKey(nomTerminal)){
                            throw new Exception("Le nom du terminal a deja ete donne");
                        }
                        mapTerminaux.put(nomTerminal, terminal);
                        System.out.printf("Terminal %s ajoute a la piste %s\n", nomTerminal, nomPiste);
                    }
                    case "AJOUTER_EMPLACEMENT" -> {
                        String nomTerminal = scanner.next();
                        Terminal terminal = mapTerminaux.get(nomTerminal);
                        int nombre = scanner.nextInt();
                        for (int i = 0; i<nombre; i ++)
                            terminal.ajouterEmplacement();
                        System.out.printf("%d emplacements ajoutes au terminal %s\n", nombre,
                                nomTerminal);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("========== FIN PARSING ==========");

    }
}
