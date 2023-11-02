package com.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static Scanner in = new Scanner(System.in); // System.in és global
	
	public static void main(String[] args) throws InterruptedException, IOException {
		
		try {

			boolean running = true;
			
			while (running) {
				
				System.out.println("\n*************************************************************************************************\n\nEscull una opcio:\n" +
                                   "\n0) Lectura del fitxer 'llibres_input.json'" +
                                   "\n1) Modificar l'any de publicacio del llibre amb id 1 a 1995" +
								   "\n2) Afegir un nou llibre amb id 4, titol 'Histories de la ciutat', autor 'Miquel Soler', any 2022" +
                                   "\n3) Esborrar el llibre amb id 2\n100) Sortir\n");
				try {
					
					int opcio = Integer.valueOf(llegirLinia("Opcio: "));
					
					switch (opcio) {
						case 0: lectura();
								break;
                        case 1: modificar();
								break;
						case 2: afegir();
								break;
						case 3: esborrar();
								break;
						case 100: running = false;
								break;
						default: System.out.println("\nOpcio fora del rang!");
								break;
					}

				} catch (Exception e) {
					System.out.println("\nOpcio no numerica!");
				}
			}
            in.close();
		} catch (Exception e) {
			System.out.println("\nOpcio no numerica!");
		}
	}

    public static String llegirLinia (String text) {
		System.out.print(text);
		return in.nextLine();
    }

    public static void lectura() {
        
        try {
            // Crea un objecte ObjectMapper de Jackson
            ObjectMapper mapper = new ObjectMapper();

            // Llegeix l'arxiu JSON i carrega el seu contingut en una llista de HashMaps
            List<Map<String, Object>> llibres = mapper.readValue(new File("data/llibres_input.json"), List.class);

            // Imprimeix les dades de l'arxiu JSON
            System.out.println("");
            for (Map<String, Object> llibre : llibres) {
                System.out.println(llibre);
            }

        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void modificar() {

        try {
            // Crea un objecte ObjectMapper de Jackson
            ObjectMapper mapper = new ObjectMapper();

            // Llegeix l'arxiu JSON i carrega el seu contingut en una llista de HashMaps
            List<Map<String, Object>> llibres = mapper.readValue(new File("data/llibres_input.json"), List.class);

            // Modifica l'any de publicació del llibre amb id 1
            for (Map<String, Object> llibre : llibres) {
                if (llibre.containsKey("id") && Integer.parseInt(llibre.get("id").toString()) == 1) {
                    llibre.put("any", 1995);
                    break;
                }
            }

            // Desa l'arxiu JSON actualitzat
            mapper.writeValue(new File("data/llibres_output.json"), llibres);
            System.out.println("\nDades guardades amb exit a llibres_output.json!");

        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void afegir() {

        try {
            // Crea un objecte ObjectMapper de Jackson
            ObjectMapper mapper = new ObjectMapper();

            // Llegeix l'arxiu JSON actual i carrega el seu contingut en un ArrayNode
            ArrayNode node = (ArrayNode) mapper.readTree(new File("data/llibres_output.json"));

            // Comprova si ja existeix un llibre amb id 4
            boolean bookExists = false;
            for (JsonNode jsonNode : node) {
                if (jsonNode.has("id") && Integer.parseInt(jsonNode.get("id").toString()) == 4) {
                    bookExists = true;
                    break;
                }
            }

            if (!bookExists) {
                // Crea un nou HashMap en el qual s'insereixen les dades del nou llibre
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", 4);
                map.put("titol", "Histories de la ciutat");
                map.put("autor", "Miquel Soler");
                map.put("any", 2022);

                // Agrega el nou llibre a l'ArrayNode
                node.add(mapper.valueToTree(map));

                // Desa l'arxiu JSON actualitzat
                mapper.writeValue(new File("data/llibres_output.json"), node);
                System.out.println("\nDades guardades amb exit a llibres_output.json!");
            }
            else {
                System.out.println("Aquest llibre ja existeix!");
            }
            
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void esborrar() {

        try {
            // Crea un objecte ObjectMapper de Jackson
            ObjectMapper mapper = new ObjectMapper();

            // Llegeix l'arxiu JSON i carrega el seu contingut en una llista de HashMaps
            List<Map<String, Object>> llibres = mapper.readValue(new File("data/llibres_output.json"), List.class);

            // Cerca el llibre amb id 2 i l'elimina de la llista
            for (Map<String, Object> llibre : llibres) {
                if (llibre.containsKey("id") && Integer.parseInt(llibre.get("id").toString()) == 2) {
                    llibres.remove(llibre);
                    break;
                }
            }

            // Desa l'arxiu JSON actualitzat
            mapper.writeValue(new File("data/llibres_output.json"), llibres);
            System.out.println("\nDades guardades amb exit a llibres_output.json!");

        } catch (IOException e) { e.printStackTrace(); }
    }
}
