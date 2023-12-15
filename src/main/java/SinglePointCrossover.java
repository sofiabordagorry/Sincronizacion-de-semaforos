import java.util.List;

import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

@SuppressWarnings("serial")
public class SinglePointCrossover implements CrossoverOperator<IntegerSolution> {

	private double crossoverProbability;
    private JMetalRandom randomGenerator;

    public SinglePointCrossover(double probability) {
        this.crossoverProbability = probability;
        this.randomGenerator = JMetalRandom.getInstance();
    }

    @Override
    public int getNumberOfRequiredParents() {
        return 2; // Necesita 2 padres para el cruzamiento
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 2; // Genera 2 hijos
    }

	@Override
	public double getCrossoverProbability() {
		return 0;
	}

	@Override
	public List<IntegerSolution> execute(List<IntegerSolution> parents) {
		if (parents.size() != 2) {
            throw new IllegalArgumentException("Se necesitan exactamente 2 padres para el cruzamiento");
        }

        if (randomGenerator.nextDouble() < crossoverProbability) {
            IntegerSolution parent1 = parents.get(0);
            IntegerSolution parent2 = parents.get(1);

            int vectorLength = parent1.getNumberOfVariables();
            int crossoverPoint;
            do {
                crossoverPoint = randomGenerator.nextInt(0, vectorLength - 1);
            } while (crossoverPoint % 3 != 0);

            IntegerSolution child1 = (IntegerSolution) parent1.copy();
            IntegerSolution child2 = (IntegerSolution) parent2.copy();

            for (int i = crossoverPoint; i < vectorLength; i++) {
                int temp = child1.getVariable(i);
                child1.setVariable(i, child2.getVariable(i));
                child2.setVariable(i, temp);
            }

            java.util.List<IntegerSolution> offspring = new java.util.ArrayList<>();
            offspring.add(child1);
            offspring.add(child2);
            return offspring;
        } else {
            return parents; // No se aplica el cruzamiento
        }
	}
}
