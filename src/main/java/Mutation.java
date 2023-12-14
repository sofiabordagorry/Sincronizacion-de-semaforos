import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

@SuppressWarnings("serial")
public class Mutation implements MutationOperator<DoubleSolution> {
    private double mutationProbability;
    private int perturbation;

    public Mutation(double probability, double perturbation) {
        this.mutationProbability = probability;
        this.perturbation = (int) perturbation;
    }

    @Override
    public DoubleSolution execute(DoubleSolution solution) {
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            if (JMetalRandom.getInstance().nextDouble() <= mutationProbability) {
                double value = solution.getVariable(i);
                value += JMetalRandom.getInstance().nextInt(-perturbation, perturbation);
                solution.setVariable(i, value);
            }
        }
        return solution;
    }
    
    public int getNumberOfRequiredParents() {
        return 1; // Indicar cuántos padres necesita el operador de mutación
    }


    public int getNumberOfGeneratedChildren() {
        return 1; // Indicar cuántos hijos genera el operador de mutación
    }

	@Override
	public double getMutationProbability() {
		// TODO Auto-generated method stub
		return 0;
	}
}