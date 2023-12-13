import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

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
                double temp = child1.getVariableValue(i);
                child1.setVariableValue(i, child2.getVariableValue(i));
                child2.setVariableValue(i, temp);
            }

            java.util.List<DoubleSolution> offspring = new java.util.ArrayList<>();
            offspring.add(child1);
            offspring.add(child2);
            return offspring;
        } else {
            return parents; // No se aplica el cruzamiento
        }
    }
}
