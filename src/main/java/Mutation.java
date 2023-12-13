public class CustomMutation implements MutationOperator<DoubleSolution> {
    private double mutationProbability;
    private int perturbation;

    public CustomMutation(double probability, double perturbation) {
        this.mutationProbability = probability;
        this.perturbation = perturbation;
    }

    @Override
    public DoubleSolution execute(DoubleSolution solution) {
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            if (JMetalRandom.getInstance().nextDouble() <= mutationProbability) {
                double value = solution.getVariableValue(i);
                value += JMetalRandom.getInstance().nextInt(-perturbation, perturbation);
                solution.setVariableValue(i, value);
            }
        }
        return solution;
    }
    
    @Override
    public int getNumberOfRequiredParents() {
        return 1; // Indicar cuántos padres necesita el operador de mutación
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 1; // Indicar cuántos hijos genera el operador de mutación
    }
}