import traci

# Iniciar SUMO y cargar la simulación
traci.start(["sumo", "-c", "simulacion.sumo.cfg"])

# Simular un determinado tiempo
tiempo_simulacion = 3600  # segundos
traci.simulationStep()

# Recolectar información de los vehículos
velocidades = []
while traci.simulation.getTime() < tiempo_simulacion:
    traci.simulationStep()
    vehiculos = traci.vehicle.getIDList()
    for vehiculo in vehiculos:
        velocidad = traci.vehicle.getSpeed(vehiculo)
        velocidades.append(velocidad)

# Calcular la velocidad promedio
velocidad_promedio = sum(velocidades) / len(velocidades)
print(f"La velocidad promedio es: {velocidad_promedio} m/s")

# Finalizar la conexión con SUMO
traci.close()