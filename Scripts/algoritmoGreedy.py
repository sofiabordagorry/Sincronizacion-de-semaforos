import random
import subprocess
import xml.etree.ElementTree as ET
import re
from xml.dom import minidom

input_file_path = "C:\\Users\\PC\\OneDrive\\Imágenes\\Escritorio\\Sincronizacion-de-semaforos-main\\InstanciaChiquita.net.xml"

# Función para calcular la velocidad promedio
# Suponiendo que tienes información de semáforos en un arreglo ordenado llamado 'datos_semáforos'
# Cada conjunto de 3 elementos representa: [nombre_del_semaforo, nueva_fase, nuevo_offset]
import xml.etree.ElementTree as ET
import os

def modificar_archivo_xml(configuraciones, input_file_path):
    try:
        tree = ET.parse(input_file_path)
        root = tree.getroot()

        for i, semaforo in enumerate(root.findall(".//tlLogic")):
            semaforo.set("offset", str(configuraciones[(3 * i) + 2]))

            h = 0
            for fase in semaforo.findall(".//phase[@state='G']"):
                fase.set("duration", str(configuraciones[(3 * i) + h]))
                h += 1

        output_file_path = os.path.abspath(input_file_path)
        tree.write(output_file_path, encoding="utf-8")
        
    except Exception as e:
        print(f"Error al procesar el archivo XML: {e}")

def calcular_velocidad_promedio(configuracion_semaforos, input_file_path):
    modificar_archivo_xml(configuracion_semaforos, input_file_path)
    resultado = subprocess.run(["python", "velocidadPromedio.py"], capture_output=True, text=True)
    
    # Utilizamos expresiones regulares para buscar el número después de la frase "La velocidad promedio es:"
    velocidad_promedio = re.search(r"La velocidad promedio es: ([0-9.]+)", resultado.stdout)
    
    if velocidad_promedio:
        return float(velocidad_promedio.group(1))
    else:
        return None

def generar_instancia(linea):
    partes = linea.strip().split(';')
    
    # Verificar si la línea tiene el formato esperado
    if len(partes) != 3 or not partes[1].isdigit() or not partes[2].isdigit():
        return None
    
    id_semaforo = partes[0]
    num_fases = int(partes[1])
    suma_total_fases = int(partes[2])

    # Generar duraciones de fases
    d1 = 29
    d2 = suma_total_fases - d1
    offset = 0

    return [d1, d2, offset]

def generar_configuracion_inicial():
    with open("SemaforosChiquita.txt", 'r') as file:
        lineas_originales = file.readlines()

    configuracion = []
    for linea_original in lineas_originales:
        nueva_linea = generar_instancia(linea_original)
        if nueva_linea is not None:
            configuracion.append(nueva_linea[0])
            configuracion.append(nueva_linea[1])
            configuracion.append(nueva_linea[2])
    return configuracion

def sincroniza_semaforos_greedy(iteraciones):
    configuracion = generar_configuracion_inicial()

    for _ in range(iteraciones):
        beneficios = []

        for i in range(24):
            for param in [(1, 0, 0), (-1, 0, 0), (0, 1, 0), (0, -1, 0), (0, 0, 1), (0, 0, -1)]:
                nueva_config = configuracion.copy()
                nueva_config[3 * i] += param[0]
                nueva_config[3 * i + 1] += param[1]
                nueva_config[3 * i + 2] += param[2]

                beneficio = calcular_velocidad_promedio(nueva_config, input_file_path)
                beneficios.append((beneficio, nueva_config))

        beneficios.sort(reverse=True)
        mejor_beneficio, mejor_config = beneficios[0]

        if mejor_beneficio > 0:
            configuracion = mejor_config
        else:
            break

    mejor_velocidad = calcular_velocidad_promedio(configuracion, input_file_path)
    return configuracion, mejor_velocidad


# Ejemplo de uso
iteraciones = 2+48  # Puedes ajustar el número de iteraciones según sea necesario
configuracion, mejor_velocidad = sincroniza_semaforos_greedy(iteraciones)

print("Mejor configuración de semáforos:")
print(configuracion)
print("Mejor velocidad promedio:")
print(mejor_velocidad)
