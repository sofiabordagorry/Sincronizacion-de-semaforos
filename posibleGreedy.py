import random
import subprocess
import xml.etree.ElementTree as ET
import re

archivo_entrada = 'E:\\Facultad\\2023\\Segundo Semestre\\Algoritmos Evolutivos\\LaboratiorioAEUltimoIntento\\Sincronizacion-de-semaforos-main\\RED-simulacionProbada-2fases.net.xml'
archivo_salida = 'E:\\Facultad\\2023\\Segundo Semestre\\Algoritmos Evolutivos\\LaboratiorioAEUltimoIntento\\Sincronizacion-de-semaforos-main\\RED-simulacionProbada-2fases.net.xml'

# Función para calcular la velocidad promedio

def modificar_archivo_xml(configuracion_semaforos, archivo_entrada, archivo_salida):
    tree = ET.parse(archivo_entrada)
    root = tree.getroot()

    for semaforo in root.findall('.//tlLogic'):
        semaforo_id = semaforo.get('id')

        for config in configuracion_semaforos:
            if config[0] == semaforo_id:
                phase1_duration, phase2_duration, offset = config[1], config[2], config[3]

                # Obtener todas las fases con estado 'G'
                green_phases = [phase for phase in semaforo.findall('.//phase') if 'G' in phase.get('state')]

                # Modificar duraciones de las fases con estado 'G'
                for phase in green_phases:
                    if phase.get('duration') == 'phase1':
                        phase.set('duration', str(phase1_duration))
                    elif phase.get('duration') == 'phase2':
                        phase.set('duration', str(phase2_duration))

                semaforo.set('offset', str(offset))

    tree.write(archivo_salida)

def calcular_velocidad_promedio(configuracion_semaforos):
    modificar_archivo_xml(configuracion_semaforos, archivo_entrada, archivo_salida)
    resultado = subprocess.run(["python", "velocidadPromedio.py"], capture_output=True, text=True)
    
    # Utilizamos expresiones regulares para buscar el número después de la frase "La velocidad promedio es:"
    velocidad_promedio = re.search(r"La velocidad promedio es: ([0-9.]+)", resultado.stdout)
    
    if velocidad_promedio:
        return float(velocidad_promedio.group(1))
    else:
        return None
    pass

# Función para generar una configuración aleatoria de semáforos
def generar_configuracion_aleatoria():
    configuracion = []
    for _ in range(100):
        fase1 = random.randint(1, 65)
        fase2 = 66 - fase1
        offset = random.randint(0, 66)
        configuracion.append((fase1, fase2, offset))
    return configuracion

# Algoritmo greedy para encontrar la mejor configuración
def greedy_algoritmo(iteraciones):
    mejor_configuracion = generar_configuracion_aleatoria()
    mejor_velocidad = calcular_velocidad_promedio(mejor_configuracion)

    for _ in range(iteraciones):
        nueva_configuracion = generar_configuracion_aleatoria()
        nueva_velocidad = calcular_velocidad_promedio(nueva_configuracion)
        print(mejor_velocidad)
        if nueva_velocidad > mejor_velocidad:
            mejor_configuracion = nueva_configuracion
            mejor_velocidad = nueva_velocidad

    return mejor_configuracion, mejor_velocidad

# Ejemplo de uso
iteraciones = 5  # Puedes ajustar el número de iteraciones según sea necesario
mejor_configuracion, mejor_velocidad = greedy_algoritmo(iteraciones)

print("Mejor configuración de semáforos:")
print(mejor_configuracion)
print("Mejor velocidad promedio:")
print(mejor_velocidad)
