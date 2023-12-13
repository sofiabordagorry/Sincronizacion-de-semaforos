import traci
import threading

# Función para recopilar datos de velocidad de los vehículos
def collect_speeds(thread_id, total_threads):
    global velocidades
    while traci.simulation.getMinExpectedNumber() > 0:
        traci.simulationStep()
        vehiculos = traci.vehicle.getIDList()
        for i in range(thread_id, len(vehiculos), total_threads):
            velocidad = traci.vehicle.getSpeed(vehiculos[i])
            velocidades.append(velocidad)

# Iniciar SUMO y cargar la simulación
traci.start(["sumo", "-c", "simulacion.sumo.cfg"])

# Simular un determinado tiempo
tiempo_simulacion = 3600  # segundos
velocidades = []
num_threads = 10  # Ajusta según la cantidad de threads deseados

# Iniciar los threads para recopilar velocidades
threads = []
for i in range(num_threads):
    thread = threading.Thread(target=collect_speeds, args=(i, num_threads))
    threads.append(thread)
    thread.start()

# Esperar a que termine la simulación
traci.simulation.run(tiempo_simulacion)

# Esperar a que todos los threads terminen
for thread in threads:
    thread.join()

# Calcular la velocidad promedio
velocidad_promedio = sum(velocidades) / len(velocidades)
print(f"La velocidad promedio es: {velocidad_promedio} m/s")

# Finalizar la conexión con SUMO
traci.close()