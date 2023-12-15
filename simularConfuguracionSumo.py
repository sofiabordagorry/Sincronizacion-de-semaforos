import traci
import random

# Iniciar SUMO y cargar la simulación
traci.start(["sumo", "-c", "simulacion.sumo.cfg"])

# Simular un determinado tiempo
tiempo_simulacion = 3600  # segundos
traci.simulationStep()

# Tiempo de espera para considerar un vehículo atascado (en segundos)
tiempo_atasco = 30

# Diccionario para mantener registro del tiempo que un vehículo está atascado
vehiculos_atascados = {}

# Obtener los nodos de la red vial
nodos = traci.junction.getIDList()

x_min, x_max = traci.simulation.getNetBoundary()
y_min, y_max = traci.simulation.getNetBoundary()

# Lista para almacenar las velocidades de los vehículos
velocidades = []

while traci.simulation.getTime() < tiempo_simulacion:
    traci.simulationStep()
    vehiculos = traci.vehicle.getIDList()
    
    for vehiculo in vehiculos:
        velocidad = traci.vehicle.getSpeed(vehiculo)
        velocidades.append(velocidad)  # Agregar la velocidad a la lista
        
        # Verificar si el vehículo está atascado y realizar el teletransporte
        if velocidad < 1e-5:  # Definir un umbral de velocidad bajo
            if vehiculo not in vehiculos_atascados:
                vehiculos_atascados[vehiculo] = tiempo_atasco
                
            # Acceder al tiempo de atasco del vehículo solo si está presente en el diccionario
            elif vehiculos_atascados[vehiculo] <= 0:
                x_random = random.uniform(x_min, x_max)
                y_random = random.uniform(y_min, y_max)
                
                traci.vehicle.moveToXY(vehiculo, "", x_random, y_random, 1)
                vehiculos_atascados[vehiculo] = tiempo_atasco

# Calcular la velocidad promedio
velocidad_promedio = sum(velocidades) / len(velocidades) if velocidades else 0

# Imprimir la velocidad promedio
print(f"La velocidad promedio es: {velocidad_promedio} m/s")

# Finalizar la conexión con SUMO
traci.close()