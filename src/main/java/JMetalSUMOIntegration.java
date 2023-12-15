import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.solution.integersolution.impl.DefaultIntegerSolution;

public class JMetalSUMOIntegration {
    public static void main(String[] args) {
        int maxGenerations = 10;
        double crossProbability = 0.5;
        double mutationProbability = 0.01;
        int perturbation = 1;
        int cantSemaforos = 100;

        List<IntegerSolution> initialPopulation = new ArrayList<>();
        int[] sumaSemaforos = new int[cantSemaforos];
        
        try (BufferedReader br = new BufferedReader(new FileReader("semaforos2fases.txt"))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                int duracionMax = Integer.parseInt(parts[2]);
                sumaSemaforos[i] = duracionMax;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        SemaforosProblem problem = new SemaforosProblem(cantSemaforos, sumaSemaforos, 66);
        List<Semaforo> semaforosList = new ArrayList<>(); // Cambio a List<Semaforo>

        try (BufferedReader br = new BufferedReader(new FileReader("archivo_generado.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
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

        List<Pair<Integer, Integer>> boundsList = new ArrayList<>();
        for(int j = 0; j < cantSemaforos; j++) {
        	boundsList.add(Pair.of(0, sumaSemaforos[j]));
        	boundsList.add(Pair.of(0, sumaSemaforos[j]));
        	boundsList.add(Pair.of(0, 66));
        }
        
        
        int i = 0;
        IntegerSolution tfSolution = null;
        //System.out.println(semaforosList.size());
        for(Semaforo s : semaforosList) {
        	if(i%cantSemaforos == 0) {
        		tfSolution = new DefaultIntegerSolution(boundsList,1, 0);
        		//System.out.println("Numero de variables:" + tfSolution.getNumberOfVariables());
        		i=0;
        	}
        	int[] fasesSemaforo = s.getFases();
        	for(int k = 0; k < fasesSemaforo.length;k++) {
        		tfSolution.setVariable((3*i)+k, fasesSemaforo[k]);
        	}
        	tfSolution.setVariable((i*3)+2, s.getOffset());
        	i++;
    		if(i==cantSemaforos) {
    			initialPopulation.add(tfSolution);
    		}
        }
        System.out.println(initialPopulation.size());
        Algorithm<IntegerSolution> algorithm = new GeneticAlgorithm(
        		problem,
                initialPopulation,
                maxGenerations,
                crossProbability,
                mutationProbability,
                perturbation
        );
        algorithm.run();
        IntegerSolution bestSolution = algorithm.getResult();
        //System.out.println("La mejor Velocidad:" + bestSolution.getObjective(0));
//        System.out.println("La mejor solucion obnetida es: ");
//        TrafficLightsProblem trafficLightsBS = (TrafficLightsProblem) bestSolution;
//        Semaforo[] semaforosBS = trafficLightsBS.getPhasesAndOffsets();
//        for(Semaforo s : semaforosBS) {
//        	System.out.println(s.getId());
//        	System.out.println(s.getOffset());
//        	int[] fases = s.getFases();
//        	for(int j : fases) {
//        		System.out.println("-FASE "+j+" QUE TIENE VERDE:"+fases[j]);
//        	}
//        	System.out.println("---------------------------");
//        }
    }

}
