import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class GeneticAlgorithm implements Algorithm<DoubleSolution>{
	private List<DoubleSolution> initialPopulation;
	private int maxGenerations;
	private SinglePoint	crossover;
	private UniformMutation mutation;
	
	public GeneticAlgorithm(List<DoubleSolution> initialPopulation,int maxGenerations, double crossProbability, double mutationProbability, int perturbation) {
		this.initialPopulation = initialPopulation;
		this.maxGenerations = maxGenerations;
		this.crossover = new SinglePointCrossover(crossProbabilty);
		this.mutation = new UniformMutation(mutationProbability, perturbation);
	}
	
	public void run() {
		List<DoubleSolution> currentPopulation = this.initialPopulation;
		for(int i=0; i < this.maxGenerations;i++) {
			 // Evaluar la aptitud de la población actual
			List<DoubleSolution> poblacionRankeada = evaluarPoblacion(currentPopulation);
			
			// Rankear la poblacion actual para elegir padres
			// Aplicar cruzamientos y crear nueva generacion
			List<DoubleSolution> offspringPopulation = new  ArrayList<>();
			for(int j = 0; i  < (poblacionRankeada.length / 2); j++) {//Se elije la mejor mitad de los padres
				List<DoubleSolution> parents = Arrays.asList(currentPopulation.get(j), currentPopulation.get(j+1));
				List<DoubleSolution> children = this.crossover.execute(parents);
				offspringPopulation.addAll(children);
			}
			
			List<DoubleSolution> p1 = Arrays.asList(currentPopulation.get(0), currentPopulation.get(2));
			List<DoubleSolution> ch1 = this.crossover.execute(p1);
			List<DoubleSolution> p2 = Arrays.asList(currefntPopulation.get(1), currentPopulation.get(3));
			List<DoubleSolution> ch2 = this.crossover.execute(p2);
			offspringPopulation.addAll(ch1);
			offspringPopulation.addAll(ch2);
			
			for(DoubleSolution s : offspringPopulation) {
				
			}
			
            // Aplicar mutación a los descendientes

			currentPopulation = offspringPopulation;
		}
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public DoubleSolution getResult() {
		return null;
	}
	
    public List<DoubleSolution> evaluarPoblacion(List<DoubleSolution> currentPopulation) {
        Map<DoubleSolution, Double> ranks = new HashMap<>();

        // Calcular la velocidad promedio de cada solución y almacenarla en el mapa
        for (DoubleSolution s : currentPopulation) {
            TrafficLightsProblem tfp = (TrafficLightsProblem) s;
            tfp.evaluate(s);

            // Obtener la velocidad promedio de la solución
            double velocidadPromedio = s.getObjective(0);
            ranks.put(s, velocidadPromedio);
        }

        // Ordenar el mapa por las velocidades promedio de mayor a menor
        List<Map.Entry<DoubleSolution, Double>> sortedEntries = new ArrayList<>(ranks.entrySet());
        Collections.sort(sortedEntries, Comparator.comparing(Map.Entry::getValue, Collections.reverseOrder()));

        // Crear un nuevo mapa ordenado para almacenar las soluciones y velocidades
        List<DoubleSolution> sortedRanks = new ArrayList<>();
        for (Map.Entry<DoubleSolution, Double> entry : sortedEntries) {
            sortedRanks.put(entry.getKey());
        }

        // Ahora, 'sortedRanks' contiene las soluciones y velocidades ordenadas según las velocidades

        return sortedRanks;
    }


}
