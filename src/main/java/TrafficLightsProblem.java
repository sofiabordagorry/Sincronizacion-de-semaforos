import org.uma.jmetal.problem.doubleproblem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@SuppressWarnings("serial")
public class TrafficLightsProblem extends AbstractDoubleProblem{
	
	private Semaforo[] phasesAndOffsets;
	
	public TrafficLightsProblem (Semaforo[] phasesAndOffsets) {
		this.phasesAndOffsets = phasesAndOffsets;
	}

	private Semaforo buscarSemaforo(String id) {
		for(Semaforo s : this.phasesAndOffsets) {
			if(s.getId().equals(id)) {
				return s;
			}
		}
		
		return null;
	}
	
	@Override
	public void evaluate(DoubleSolution solution) {
        try {
		File inputFile = new File("RED-simulacionProbada.net.xml");
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder;
        	Document doc;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(inputFile);        		
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
	        					fase.setAttribute("duracion", String.valueOf(fasesSemaforoSolucion[j]));
	        				}
	        			}
	        		}	
	        	}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
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
	
	public Semaforo[] getPhasesAndOffsets() {
		return this.phasesAndOffsets;
	}
}
