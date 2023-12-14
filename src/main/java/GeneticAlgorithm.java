import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


@SuppressWarnings("serial")
public class GeneticAlgorithm implements Algorithm<DoubleSolution>{
	private List<DoubleSolution> initialPopulation;
	private int maxGenerations;
	private SinglePointCrossover crossover;
	private Mutation mutation;
	
	public GeneticAlgorithm(List<DoubleSolution> initialPopulation,int maxGenerations, double crossProbability, double mutationProbability, int perturbation) {
		this.initialPopulation = initialPopulation;
		this.maxGenerations = maxGenerations;
		this.crossover = new SinglePointCrossover(crossProbability);
		this.mutation = new Mutation(mutationProbability, perturbation);
	}
	
	public void run() {
		List<DoubleSolution> currentPopulation = this.initialPopulation;
		for(int i=0; i < this.maxGenerations;i++) {
			 // Evaluar la aptitud de la población actual
			List<DoubleSolution> poblacionRankeada = evaluarPoblacion(currentPopulation);
			
			// Rankear la poblacion actual para elegir padres
			// Aplicar cruzamientos y crear nueva generacion
			List<DoubleSolution> offspringPopulation = new  ArrayList<>();
			for(int j = 0; i  < (poblacionRankeada.size()/ 2); j++) {//Se elije la mejor mitad de los padres
				List<DoubleSolution> parents = Arrays.asList(currentPopulation.get(j), currentPopulation.get(j+1));
				List<DoubleSolution> children = this.crossover.execute(parents);
				offspringPopulation.addAll(children);
			}
			List<DoubleSolution> p1 = Arrays.asList(currentPopulation.get(0), currentPopulation.get(2));
			List<DoubleSolution> ch1 = this.crossover.execute(p1);
			List<DoubleSolution> p2 = Arrays.asList(currentPopulation.get(1), currentPopulation.get(3));
			List<DoubleSolution> ch2 = this.crossover.execute(p2);
			offspringPopulation.addAll(ch1);
			offspringPopulation.addAll(ch2);
			
			for(DoubleSolution s : offspringPopulation) {
				this.mutation.execute(s);
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
	
	public List<TrafficLightsProblem> crearPoblacionInicial(){
        List<TrafficLightsProblem> initialPopulation = new ArrayList<>();
        List<Semaforo> semaforosList = new ArrayList<>(); // Cambio a List<Semaforo>
        try (BufferedReader br = new BufferedReader(new FileReader("archivo_generado.txt"))) {
            String line;
            int j = 1;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                //System.out.println(parts.length + "a" + j);

                String id = parts[0];
                String[] fasesString = parts[1].split(",");
                int[] fases = new int[fasesString.length];
                for (int i = 0; i < fasesString.length; i++) {
                    fases[i] = Integer.parseInt(fasesString[i]);
                }
                int offset = Integer.parseInt(parts[2]);
                
                Semaforo semaforo = new Semaforo(id, fases, offset);
                semaforosList.add(semaforo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Luego de leer el archivo, creas las instancias de TrafficLightsProblem con los semáforos
        int i = 0;
        Semaforo[] tfProblem = null;
        for(Semaforo s : semaforosList) {
        	if(i%100 == 0) {
        		tfProblem = new Semaforo[100];
        	}
        	tfProblem[i] = s;
        	if(i%100 == 99) {
        		TrafficLightsProblem solution = new TrafficLightsProblem(tfProblem);
        		initialPopulation.add(solution);
        	}
        	i = (i+1)%100;
        }
        
        return initialPopulation;
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


}
