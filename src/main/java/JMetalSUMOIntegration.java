import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

public class JMetalSUMOIntegration {
	public static void main(String[] args) {
		//Problem<DoubleSolution> problem = new TrafficLightsProblem(phaseDurations, offsets);
		int maxGenerations = 500;
		
		List<DoubleSolution> initialPopulation = new ArrayList<>();
		

		for(int i = 0; i < 100;i++) {
			Semaforo[] semaforosList = new Semaforo[100];
			try (BufferedReader br = new BufferedReader(new FileReader("semaforos.txt"))) {//quizas la direccion sea incorrecta
	            String line;
	            int j = 0;
	            while ((line = br.readLine()) != null) {
	            		semaforosList[j].setId(line);
	            		j++;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
			//A cada problema habria que agregarle fases y offsets iniciales creados de manera random
			TrafficLightsProblem solution = new TrafficLightsProblem(semaforosList);
			
			initialPopulation.add((DoubleSolution) solution);
		}
		
		Algorithm<DoubleSolution> algorithm = new GeneticAlgorithm(//Se crea el algoritmo con el problema, operador de cruce y mutacion
				initialPopulation,
				maxGenerations
		);
		algorithm.run();
		DoubleSolution bestSolution = algorithm.getResult();
    }
}

