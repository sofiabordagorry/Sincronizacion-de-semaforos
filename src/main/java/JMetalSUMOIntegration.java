import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

public class JMetalSUMOIntegration {
    public static void main(String[] args) {
        int maxGenerations = 5;
        double crossProbability = 0.5;
        double mutationProbability = 0.01;
        int perturbation = 1;

        List<DoubleSolution> initialPopulation = new ArrayList<>();
//
//        //falta llamar al archivo generador
//        try (BufferedReader br = new BufferedReader(new FileReader("archivo_generado.txt"))) {
//            String line;
//            List<Semaforo> semaforosList = new ArrayList<>();
//
//            while ((line = br.readLine()) != null) {
//                if (line.trim().isEmpty()) {
//                    // Fin de una instancia, verifica si hay exactamente 100 semáforos
//                    if (semaforosList.size() <= 100) {
//                        TrafficLightsProblem problem = new TrafficLightsProblem(
//                                semaforosList.toArray(new Semaforo[0])
//                        );
//                        initialPopulation.add(null) .add(problem);
//                    }
//                    semaforosList.clear();
//                } else {
//                    // Continuar leyendo semáforos
//                    Semaforo semaforo = parseSemaforoFromLine(line);
//                    semaforosList.add(semaforo);
//                }
//            }
//
//            // Agregar la última instancia si no termina con línea vacía y tiene 100 semáforos
//            if (!semaforosList.isEmpty() && semaforosList.size() == 100) {
//                TrafficLightsProblem problem = new TrafficLightsProblem(
//                        semaforosList.toArray(new Semaforo[0])
//                );
//                initialPopulation.add((DoubleSolution) problem);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        List<Semaforo> semaforosList = new ArrayList<>(); // Cambio a List<Semaforo>

        try (BufferedReader br = new BufferedReader(new FileReader("archivo_generado.txt"))) {
            String line;
            int j = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                //System.out.println(parts.length + "a" + j);

                String id = parts[0];
                String[] fasesString = parts[1].split(",");
                int[] fases = new int[fasesString.length];
                for (int i = 0; i < fasesString.length; i++) {
                    fases[i] = Integer.parseInt(fasesString[i]);
                }
                int offset = Integer.parseInt(parts[2]);
                
                Semaforo semaforo = new Semaforo(id, fases, offset);
                semaforosList.add(semaforo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Luego de leer el archivo, creas las instancias de TrafficLightsProblem con los semáforos
        int i = 0;
        Semaforo[] tfProblem = null;
        for(Semaforo s : semaforosList) {
        	if(i%100 == 0) {
        		tfProblem = new Semaforo[100];
        	}
        	tfProblem[i] = s;
        	if(i%100 == 99) {
        		TrafficLightsProblem solution = new TrafficLightsProblem(tfProblem);
        		initialPopulation.add(solution);
        	}
        	i = (i+1)%100;
        }
        Algorithm<DoubleSolution> algorithm = new GeneticAlgorithm(
                initialPopulation,
                maxGenerations,
                crossProbability,
                mutationProbability,
                perturbation
        );
        algorithm.run();
        DoubleSolution bestSolution = algorithm.getResult();
        System.out.println("La mejor solucion obnetida es: ");
        TrafficLightsProblem trafficLightsBS = (TrafficLightsProblem) bestSolution;
        Semaforo[] semaforosBS = trafficLightsBS.getPhasesAndOffsets();
        for(Semaforo s : semaforosBS) {
        	System.out.println(s.getId());
        	System.out.println(s.getOffset());
        	int[] fases = s.getFases();
        	for(int j : fases) {
        		System.out.println("-FASE "+j+" QUE TIENE VERDE:"+fases[j]);
        	}
        	System.out.println("---------------------------");
        }
    }

}
