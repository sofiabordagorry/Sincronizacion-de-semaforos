import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class SemaforosProblem extends AbstractIntegerProblem{

	private int numSemaforos;
	private int[] sumaSemaforo;
	private int maxOffset;
	
	public SemaforosProblem(int numSemaforos, int[] sumaSemaforo, int maxOffset) {
		this.numSemaforos = numSemaforos;
		this.sumaSemaforo = sumaSemaforo;
		this.maxOffset = maxOffset;
		
		this.setNumberOfVariables(numSemaforos * 3);
		List<Integer> lowerBounds = new ArrayList<>();
        List<Integer> upperBounds = new ArrayList<>();
		for (int i = 0; i < getNumberOfVariables(); i += 3) {
			int numSemaforo = i / 3;
            // Restricción para las dos variables de fases (variables i e i+1)
            lowerBounds.add(0);
            upperBounds.add(sumaSemaforo[numSemaforo]); 
            lowerBounds.add(0);
            upperBounds.add(sumaSemaforo[numSemaforo]); 

            // Restricción para el offset (variable i + 2)
            lowerBounds.add(0);
            upperBounds.add(maxOffset);
        }

        setVariableBounds(lowerBounds, upperBounds);
	}
	
	@Override
	public void evaluate(IntegerSolution solution) {
		Integer[] TFSolution = solution.getVariables().toArray(new Integer[0]);
		try {
			File inputFile = new File("instancia.net.xml");
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
	        			elemSemaforo.setAttribute("offset", String.valueOf(TFSolution[(3*i)+2]));
	        			NodeList fasesList = elemSemaforo.getElementsByTagName("phase");
	        			int h = 0;
	        			for(int j = 0; j < fasesList.getLength(); j++) {
	        				if(h == 2) {
	        					h=0;
	        				}
	        				Element fase = (Element) fasesList.item(j);
	        				String estadoFase = fase.getAttribute("state");
	        				if(estadoFase.contains("G")) {
	        					fase.setAttribute("duracion", String.valueOf(TFSolution[(3*i)+h]));
	        					h++;
	        				}
	        			}
	        		}	
	        	}	            
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
	            DOMSource source = new DOMSource(doc);
	            StreamResult result = new StreamResult(new File("instancia.net.xml"));
	            transformer.transform(source, result);
	            
			} catch (ParserConfigurationException | SAXException e) {
				e.printStackTrace();
			}catch(TransformerException e) {
				e.printStackTrace();
			}
            ProcessBuilder processBuilder = new ProcessBuilder("python", "velocidadPromedio.py");
            processBuilder.redirectOutput(new File("output.txt"));
            Process process = processBuilder.start();
            process.waitFor();

            //Leer el archivo de salida para obtener la velocidad promedio
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            double velocidadPromedio = 0.0;
            try {
                FileReader fileReader = new FileReader("output.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line2;
                while ((line2 = bufferedReader.readLine()) != null) {
                    if (line2.contains("La velocidad promedio es: ")) {
                        String numeroDespuesDeFrase = line2.substring(line2.indexOf("La velocidad promedio es: ") + "La velocidad promedio es: ".length());
                        
                        Pattern patronNumerico = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+");
                        Matcher matcher = patronNumerico.matcher(numeroDespuesDeFrase);
                        
                        if (matcher.find()) {
                            numeroDespuesDeFrase = matcher.group();
                            velocidadPromedio = Double.parseDouble(numeroDespuesDeFrase);
                        } else {
                            System.out.println("No se encontró un número después de la frase.");
                        }
                        break; 
                    }
                }

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            solution.setObjective(0, velocidadPromedio);
		
		
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
		
	}

	public int getNumSemaforos() {
		return numSemaforos;
	}

	public void setNumSemaforos(int numSemaforos) {
		this.numSemaforos = numSemaforos;
	}

	public int[] getSumaSemaforo() {
		return sumaSemaforo;
	}

	public void setSumaSemaforo(int[] sumaSemaforo) {
		this.sumaSemaforo = sumaSemaforo;
	}

	public int getMaxOffset() {
		return maxOffset;
	}

	public void setMaxOffset(int maxOffset) {
		this.maxOffset = maxOffset;
	}

	
}
