import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

@SuppressWarnings("serial")
public class SinglePointCrossover implements CrossoverOperator<DoubleSolution> {

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
    public java.util.List<DoubleSolution> execute(java.util.List<DoubleSolution> parents) {
        if (parents.size() != 2) {
            throw new IllegalArgumentException("Se necesitan exactamente 2 padres para el cruzamiento");
        }

        if (randomGenerator.nextDouble() < crossoverProbability) {
            DoubleSolution parent1 = parents.get(0);
            DoubleSolution parent2 = parents.get(1);

            int vectorLength = parent1.getNumberOfVariables();
            int crossoverPoint = randomGenerator.nextInt(0, vectorLength - 1);

            DoubleSolution child1 = (DoubleSolution) parent1.copy();
            DoubleSolution child2 = (DoubleSolution) parent2.copy();

            for (int i = crossoverPoint; i < vectorLength; i++) {
                double temp = child1.getVariable(i);
                child1.setVariable(i, child2.getVariable(i));
                child2.setVariable(i, temp);
            }

            java.util.List<DoubleSolution> offspring = new java.util.ArrayList<>();
            offspring.add(child1);
            offspring.add(child2);
            return offspring;
        } else {
            return parents; // No se aplica el cruzamiento
        }
    }

	@Override
	public double getCrossoverProbability() {
		return 0;
	}
}
