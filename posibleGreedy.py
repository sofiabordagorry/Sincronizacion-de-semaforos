import random

# Función para calcular la velocidad promedio
def calcular_velocidad_promedio(configuracion_semaforos):
    # Aquí deberías llamar a tu archivo .py que realiza el cálculo de velocidad promedio
    # y devolver el resultado.
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

        if nueva_velocidad > mejor_velocidad:
            mejor_configuracion = nueva_configuracion
            mejor_velocidad = nueva_velocidad

    return mejor_configuracion, mejor_velocidad

# Ejemplo de uso
iteraciones = 1000  # Puedes ajustar el número de iteraciones según sea necesario
mejor_configuracion, mejor_velocidad = greedy_algoritmo(iteraciones)

print("Mejor configuración de semáforos:")
print(mejor_configuracion)
print("Mejor velocidad promedio:")
print(mejor_velocidad)
