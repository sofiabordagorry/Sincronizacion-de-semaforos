import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

@SuppressWarnings("serial")
public class Mutation implements MutationOperator<IntegerSolution> {
    private double mutationProbability;
    private int perturbation;
    private List<Pair<Integer,Integer>> bounds;

    public Mutation(double probability, double perturbation, List<Pair<Integer,Integer>> bounds) {
        this.mutationProbability = probability;
        this.perturbation = (int) perturbation;
        this.setBounds(bounds);
    }

    @Override
    public IntegerSolution execute(IntegerSolution solution) {
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            if (JMetalRandom.getInstance().nextDouble() <= mutationProbability) {
                int value = solution.getVariable(i);
                value += JMetalRandom.getInstance().nextInt(-perturbation, perturbation);

                if (value > 9 && value < this.getBounds().get(i).getRight()) {
                solution.setVariable(i, value);

                // Si i es un índice de fase 1
                if (i % 3 == 0) {
                	int bound = this.getBounds().get(i).getRight();
                    int newValue = bound - value;
                    solution.setVariable(i+1, newValue);
                }
                // Si i es un índice de fase 2
                else if (i % 3 == 1) {
                	int bound = this.getBounds().get(i).getRight();
                    int newValue = bound - value;
                    solution.setVariable(i-1, newValue);
                }
                
                //Si i es un offset no pasa nada 
               }
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

    public List<Pair<Integer,Integer>> getBounds() {
        return bounds;
    }

    public void setBounds(List<Pair<Integer,Integer>> bounds) {
        this.bounds = bounds;
    }
}
