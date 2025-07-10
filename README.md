# Compilador2024 – Analizador léxico, sintáctico y de ámbito (en desarrollo)

**Compilador2024** es un proyecto educativo de análisis léxico y sintáctico, desarrollado en Java con interfaz gráfica usando NetBeans.  
El objetivo del proyecto es construir un compilador funcional para un **lenguaje de juguete** inspirado en JavaScript, abordando las principales etapas de procesamiento del lenguaje: análisis léxico, análisis sintáctico, manejo de ámbitos y, en etapas futuras, análisis semántico y generación de cuádruplos.

---

## Características actuales

### Análisis léxico (AFD)
- Implementado mediante un **Autómata Finito Determinista**.
- El código fuente es recorrido carácter por carácter utilizando un puntero y una **matriz de transiciones**.
- Se forman tokens válidos según el estado alcanzado: strings, enteros, reales, booleanos, identificadores, operadores, delimitadores, etc.
- Si se alcanza un estado de error o finalización no válida, el analizador reporta el token como inválido.

### Análisis sintáctico
- También basado en un **AFD**, pero adaptado a producciones gramaticales.
- Se utiliza una matriz y una pila para analizar tokens.
- Las producciones se aplican al detectar coincidencias entre terminales y no terminales.
- Se detectan errores cuando no se encuentra una producción válida según el token actual.

### Manejo de ámbitos
- Tabla de ámbito dinámica con zonas de declaración y ejecución.
- Se detectan variables, funciones y métodos definidos dentro de scopes (main, funciones anidadas, etc.).
- Las variables se registran con su tipo y ámbito correspondiente.
- Si una variable no ha sido declarada o es accedida fuera de su contexto, se marca como error.

### Visualización de resultados
- Exportación a Excel con estadísticas por ámbito (cuántas variables por tipo se encontraron).
- Visualización de la tabla de ámbitos desde la interfaz gráfica.
- Interfaz gráfica donde el usuario puede ingresar código, compilar y revisar los resultados.

---

## En desarrollo

- Validación semántica.
- Generación de cuádruplos intermedios.
- Mejora de la interfaz gráfica.
- Mejora del manejo de errores y reportes.

---

## 🖥️ Tecnologías utilizadas

- Java
- NetBeans
- Apache POI (para generación de archivos Excel)
- Implementación manual de AFD (léxico y sintáctico)
- Estructuras de datos para pila, tabla de símbolos y tabla de ámbitos

---

## Archivos adicionales

Actualmente no se incluyen los diagramas de lenguaje ni las listas de tokens formales dentro del repositorio, pero pueden compartirse bajo solicitud para revisión académica o técnica.

---

## Estado

Este proyecto se encuentra en **etapa activa de desarrollo**.

---

## Contribuciones

Este es un proyecto personal con fines educativos.  
No se aceptan contribuciones externas por ahora.

---

## 📄 Licencia

Este proyecto no tiene una licencia definida aún. Si deseas usar o estudiar el código, eres libre de hacerlo.