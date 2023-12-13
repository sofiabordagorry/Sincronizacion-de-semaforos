import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JMetalSUMOIntegration {
    public static void main(String[] args) {
        int maxGenerations = 500;

        List<DoubleSolution> initialPopulation = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("archivo_generado.txt"))) {
            String line;
            List<Semaforo> semaforosList = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    // Fin de una instancia, verifica si hay exactamente 100 semáforos
                    if (semaforosList.size() == 100) {
                        TrafficLightsProblem problem = new TrafficLightsProblem(
                                semaforosList.toArray(new Semaforo[0])
                        );
                        initialPopulation.add(problem);
                    }
                    semaforosList.clear();
                } else {
                    // Continuar leyendo semáforos
                    Semaforo semaforo = parseSemaforoFromLine(line);
                    semaforosList.add(semaforo);
                }
            }

            // Agregar la última instancia si no termina con línea vacía y tiene 100 semáforos
            if (!semaforosList.isEmpty() && semaforosList.size() == 100) {
                TrafficLightsProblem problem = new TrafficLightsProblem(
                        semaforosList.toArray(new Semaforo[0])
                );
                initialPopulation.add(problem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Algorithm<DoubleSolution> algorithm = new GeneticAlgorithm(
                initialPopulation,
                maxGenerations
        );
        algorithm.run();
        DoubleSolution bestSolution = algorithm.getResult();
    }

    private static Semaforo parseSemaforoFromLine(String line) {
        String[] parts = line.split(";");
        String id = parts[0];
        String[] fasesArray = parts[1].split(",");
        int[] fases = new int[fasesArray.length];
        for (int i = 0; i < fasesArray.length; i++) {
            fases[i] = Integer.parseInt(fasesArray[i]);
        }
        int offset = Integer.parseInt(parts[2]);

        return new Semaforo(id, fases, offset);
    }
}
