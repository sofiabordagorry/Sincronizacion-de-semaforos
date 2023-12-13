import org.uma.jmetal.problem.doubleproblem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;



public class TrafficLightsProblem extends AbstractDoubleProblem{
	
	Semaforo[] phasesAndOffsets;
	
	public TrafficLightsProblem (Semaforo[] phasesAndOffsets) {
		this.phasesAndOffsets = phasesAndOffsets;
	}

	@Override
	public void evaluate(DoubleSolution solution) {
        try {
		File inputFile = new File("RED-simulacionProbada.net.xml");
        	DocumentBuilerFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder =  dbFactory.newDocumentBuilder();
        	Document doc = dbBuilder.parse(inputFile);
        		
        	doc.getDocumentElement().normalize();
        	
        	NodeList semaforosList = doc.getElementsByTagName("tlLogic");
        	
        	for (int i = 0; i < semaforosList.getLength(); i++) {
        		Node semaforo = semaforosList.item(i);
        		if(semaforo.getNodeType() == Node.ELEMENT_NODE) {
        			Element elemSemaforo = (Element) semaforo;
        			String semaforoID = elemSemaforo.getAttribute("id");
        			Semaforo semaforoSolucion  = buscarSemaforo(semaforoID);
        			int[] fasesSemaforoSolucion = semaforoSolucion.getFases();
        			NodeList fasesList = elemSemaforo.getElementsByTagName("phase");
        			for(int j = 0; j < fasesList.getLength(); j++) {
        				Element fase = (Element) fasesList.item(j);
        				String estadoFase = fase.getAttribute("state");
        				if(estadoFase.contains("G")) {
        					fase.setAttribute("duracion", fasesSemaforoSolucion[j]);
        				}
        			}
        		}	
        	}
            ProcessBuilder processBuilder = new ProcessBuilder("python", "C:\\Users\\PC\\Desktop\\Laboratorio\\velocidadPromedio.py");
            processBuilder.redirectOutput(new File("output.txt"));
            Process process = processBuilder.start();
            process.waitFor(); // Espera a que termine el proceso

            // Leer el archivo de salida para obtener la velocidad promedio
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            String resultado = output.toString();
            double velocidadPromedio = Double.parseDouble(resultado);
            solution.setObjective(0, velocidadPromedio);
            //System.out.println("La velocidad promedio es: " + resultado);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
	
//	public void evaluate(DoubleSolution arg0) {
//		try {
//			Runtime.getRuntime().exec("cmd /c start cmd.exe /K \" cd E:\\Facultad\\2023\\Segundo Semestre\\Algoritmos Evolutivos\\Laboratorio> && python velocidadPromedio.py");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
