import random
import sys

def generar_instancia(linea):
    partes = linea.strip().split(';')
    
    # Verificar si la línea tiene el formato esperado
    if len(partes) != 3 or not partes[1].isdigit() or not partes[2].isdigit():
        return None
    
    id_semaforo = partes[0]
    num_fases = int(partes[1])
    suma_total_fases = int(partes[2])

    # Generar duraciones de fases
    duraciones_fases = [random.randint(1, suma_total_fases - num_fases + 1) for _ in range(num_fases - 1)]
    duraciones_fases.append(suma_total_fases - sum(duraciones_fases))

    # Ajustar a valores positivos
    duraciones_fases = [max(1, duracion) for duracion in duraciones_fases]

    offset = random.randint(0, 66)

    return f"{id_semaforo};" + ",".join(map(str, duraciones_fases)) + f";{offset}"

def generar_archivo_salida(archivo_entrada, num_instancias):
    with open(archivo_entrada, 'r') as file:
        lineas_originales = file.readlines()

    with open("archivo_generado.txt", 'w') as file_salida:
        for _ in range(num_instancias):
            for linea_original in lineas_originales:
                nueva_linea = generar_instancia(linea_original)
                if nueva_linea is not None:
                    file_salida.write(nueva_linea + '\n')
            file_salida.write('\n')  # Agregar salto de línea entre instancias

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Uso: python generdor.py semaforos.txt num_instancias")
        sys.exit(1)

    archivo_entrada = sys.argv[1]
    num_instancias = int(sys.argv[2])

    generar_archivo_salida(archivo_entrada, num_instancias)
    print(f"Se generaron {num_instancias} instancias en 'archivo_generado.txt'")
