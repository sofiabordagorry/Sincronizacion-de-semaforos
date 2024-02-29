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
        int maxGenerations = 30;
        double crossProbability = 0.75;
        double mutationProbability = 0.08;
        int perturbation = 1;
        int cantSemaforos = 100;

        List<IntegerSolution> initialPopulation = new ArrayList<>();
        int[] sumaSemaforos = new int[cantSemaforos];
        
        try (BufferedReader br = new BufferedReader(new FileReader("semaforos.txt"))) {
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
        List<Semaforo> semaforosList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("soluciones.txt"))) {
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
        for(Semaforo s : semaforosList) {
        	if(i%cantSemaforos == 0) {
        		tfSolution = new DefaultIntegerSolution(boundsList,1, 0);
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
        Algorithm<IntegerSolution> algorithm = new GeneticAlgorithm(
        		problem,
                initialPopulation,
                maxGenerations,
                crossProbability,
                mutationProbability,
                perturbation
        );
        algorithm.run();
    }

}
