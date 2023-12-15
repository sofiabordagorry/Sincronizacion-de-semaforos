import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


@SuppressWarnings("serial")
public class GeneticAlgorithm implements Algorithm<IntegerSolution>{
	private SemaforosProblem problem;
	private List<IntegerSolution> initialPopulation;
	private int maxGenerations;
	private SinglePointCrossover crossover;
	private Mutation mutation;
	private IntegerSolution bestSolution;
	
	public GeneticAlgorithm(SemaforosProblem problem,List<IntegerSolution> initialPopulation,int maxGenerations, double crossProbability, double mutationProbability, int perturbation) {
		this.setProblem(problem);
		this.setInitialPopulation(initialPopulation);
		this.setMaxGenerations(maxGenerations);
		this.setCrossover(new SinglePointCrossover(crossProbability));
		this.setMutation(new Mutation(mutationProbability, perturbation, problem.getBounds()));
	}
	
	public void run() {
		System.out.println("VELOCIDADES DE GENERACION");
		System.out.println();
		List<IntegerSolution> currentPopulation = this.initialPopulation;
		
		for(int i=0; i < this.maxGenerations;i++) {
			//System.out.println("Current: "+currentPopulation.size());
			List<IntegerSolution> poblacionRankeada = evaluarPoblacion(currentPopulation);
			List<IntegerSolution> offspringPopulation = new  ArrayList<>();
			int si = poblacionRankeada.size()/2;
			System.out.println("cant variables: "+ poblacionRankeada.get(0).getNumberOfVariables());
			if(si%2 ==0) {
				si--;
			}
			System.out.println("pob rankeada: " + poblacionRankeada.size());
			for(int j = 0; j  < si; j++) {//Se elije la mejor mitad de los padres
				//System.out.println("CEIL: "+(int) Math.ceil(poblacionRankeada.size()/ 2));
				List<IntegerSolution> parents = Arrays.asList(currentPopulation.get(j), currentPopulation.get(j+1));
				List<IntegerSolution> children = this.crossover.execute(parents);
				offspringPopulation.addAll(children);
			}
			//System.out.println("Cruzamiento 1 OFFSPRING: "+offspringPopulation.size());
			//System.out.println(currentPopulation.size());
			List<IntegerSolution> p1 = Arrays.asList(currentPopulation.get(0), currentPopulation.get(2));
			List<IntegerSolution> ch1 = this.crossover.execute(p1);
			//List<IntegerSolution> p2 = Arrays.asList(currentPopulation.get(1), currentPopulation.get(3));
			//List<IntegerSolution> ch2 = this.crossover.execute(p2);
			offspringPopulation.addAll(ch1);
			//System.out.println("Cruzamiento 2 OFFSPRING: "+offspringPopulation.size());
			//offspringPopulation.addAll(ch2);
			
			for(IntegerSolution s : offspringPopulation) {
				this.mutation.execute(s);
			}
			//System.out.println("Mutacion OFFSPRING: "+offspringPopulation.size());
			System.out.println("Mejor velocidad de esta Generacion: "+poblacionRankeada.get(0).getObjective(0));
			System.out.println();
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
	public IntegerSolution getResult() {
		return bestSolution;
	}
	
    public List<IntegerSolution> evaluarPoblacion(List<IntegerSolution> currentPopulation) {
        Map<IntegerSolution, Double> ranks = new HashMap<>();

        // Calcular la velocidad promedio de cada solución y almacenarla en el mapa
        
        for (IntegerSolution s : currentPopulation) {
            problem.evaluate(s);

            // Obtener la velocidad promedio de la solución
            double velocidadPromedio = s.getObjective(0);
            ranks.put(s, velocidadPromedio);
        }

        // Ordenar el mapa por las velocidades promedio de mayor a menor
        List<Map.Entry<IntegerSolution, Double>> sortedEntries = new ArrayList<>(ranks.entrySet());
        Collections.sort(sortedEntries, Comparator.comparing(Map.Entry::getValue, Collections.reverseOrder()));

        // Crear un nuevo mapa ordenado para almacenar las soluciones y velocidades
        List<IntegerSolution> sortedRanks = new ArrayList<>();
        for (Map.Entry<IntegerSolution, Double> entry : sortedEntries) {
            sortedRanks.add(entry.getKey());
        }

        // Ahora, 'sortedRanks' contiene las soluciones y velocidades ordenadas según las velocidades

        return sortedRanks;
    }

	public Mutation getMutation() {
		return mutation;
	}

	public void setMutation(Mutation mutation) {
		this.mutation = mutation;
	}

	public List<IntegerSolution> getInitialPopulation() {
		return initialPopulation;
	}

	public void setInitialPopulation(List<IntegerSolution> initialPopulation) {
		this.initialPopulation = initialPopulation;
	}

	public int getMaxGenerations() {
		return maxGenerations;
	}

	public void setMaxGenerations(int maxGenerations) {
		this.maxGenerations = maxGenerations;
	}

	public SinglePointCrossover getCrossover() {
		return crossover;
	}

	public void setCrossover(SinglePointCrossover crossover) {
		this.crossover = crossover;
	}

	public IntegerSolution getBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(IntegerSolution bestSolution) {
		this.bestSolution = bestSolution;
	}

	public SemaforosProblem getProblem() {
		return problem;
	}

	public void setProblem(SemaforosProblem problem) {
		this.problem = problem;
	}


}
