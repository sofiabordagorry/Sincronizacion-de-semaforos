import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


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
		List<IntegerSolution> currentPopulation = this.initialPopulation;
		
		for(int i=0; i < this.maxGenerations;i++) {
			if (currentPopulation.size()>2) {
			List<IntegerSolution> poblacionRankeada = evaluarPoblacion(currentPopulation);
			List<IntegerSolution> offspringPopulation = new  ArrayList<>();
			int si = poblacionRankeada.size()/2;
			if(poblacionRankeada.size()%2 ==0) {
				si--;
			}
			for(int j = 0; j  < si; j++) {//Se elije la mejor mitad de los padres
				List<IntegerSolution> parents = Arrays.asList(poblacionRankeada.get(j), poblacionRankeada.get(j+1));
				List<IntegerSolution> children = this.crossover.execute(parents);
				offspringPopulation.addAll(children);

			}
			
			for(IntegerSolution s : offspringPopulation) {
				this.mutation.execute(s);
			}
			offspringPopulation.add(poblacionRankeada.get(0));
			offspringPopulation.add(poblacionRankeada.get(1));
			currentPopulation = offspringPopulation;
			}
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
	
	@SuppressWarnings("unchecked")
	public List<IntegerSolution> evaluarPoblacion(List<IntegerSolution> currentPopulation) {
	    for (IntegerSolution s : currentPopulation) {
	        problem.evaluate(s);
	        // Obtener la velocidad promedio de la solución y almacenarla en la solución misma
	        double velocidadPromedio = s.getObjective(0);
	        s.setAttribute("VelocidadPromedio", velocidadPromedio);
	    }

	    //Ordenar la lista según la velocidad promedio
	    Collections.sort(currentPopulation, Comparator.comparingDouble(s -> (double) ((Solution<Integer>) s).getAttribute("VelocidadPromedio")).reversed());
	    return currentPopulation;
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
