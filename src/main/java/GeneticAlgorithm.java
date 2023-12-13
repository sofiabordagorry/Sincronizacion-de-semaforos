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
	
	public GeneticAlgorithm(List<DoubleSolution> initialPopulation,int maxGenerations) {
		this.initialPopulation = initialPopulation;
		this.maxGenerations = maxGenerations;
	}
	
	public void run() {

		List<DoubleSolution> currentPopulation = this.initialPopulation;
		for(int i=0; i < this.maxGenerations;i++) {
			 // Evaluar la aptitud de la población actual
			Map<DoubleSolution, Double> rankings = evaluarPoblacion(currentPopulation);

            // Seleccionar padres

            // Cruzar los padres para crear descendientes

            // Aplicar mutación a los descendientes

            // Evaluar la aptitud de los descendientes

            // Reemplazar la población actual con los descendientes si son mejores
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
	
    public Map<DoubleSolution, Double> evaluarPoblacion(List<DoubleSolution> currentPopulation) {
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
        Map<DoubleSolution, Double> sortedRanks = new LinkedHashMap<>();
        for (Map.Entry<DoubleSolution, Double> entry : sortedEntries) {
            sortedRanks.put(entry.getKey(), entry.getValue());
        }

        // Ahora, 'sortedRanks' contiene las soluciones y velocidades ordenadas según las velocidades

        return sortedRanks;
    }


}
