# 🐶 EdadCanina · v1

Aplicación Android (Java) que calcula la edad "humana" de un perro multiplicando su edad por 7.

Esta es la **versión 1**: funciona, pero no comprueba lo que escribe el usuario. Tu tarea es convertirla en la **versión 2** (rama `v2-validacion`), que valida la entrada y controla los errores sin que la app se cierre.

---

## Objetivos

Al terminar sabrás:

- Detectar por qué una app se cierra (`NumberFormatException`) y evitarlo con `try/catch`.
- Validar datos de entrada: campo vacío, texto no numérico y rango de valores.
- Mostrar errores al usuario con `EditText.setError()`.
- Sustituir "números mágicos" por constantes.
- Llevar los textos a `strings.xml` y usar formatos con varios parámetros.
- Trabajar en una rama de Git distinta de `main`.

---

## Paso 0 · Prepara el proyecto

1. Haz un **fork** de este repositorio (botón *Fork*) y clona tu fork:
   ```bash
   git clone https://github.com/TU_USUARIO/edadCaninav1.git
   ```
2. Ábrelo en Android Studio y ejecútalo en el emulador.

## Paso 1 · Rompe la app (a propósito)

Antes de arreglar nada, comprueba qué falla. Prueba estas entradas y anota qué pasa:

| Entrada | ¿Qué ocurre? |
|---|---|
| *(campo vacío)* | |
| `abc` | |
| `3.5` | |
| `0` | |
| `-4` | |
| `99999999999` | |
| `400000000` | |

> 💡 Cuando la app se cierre, abre el **Logcat** y busca la excepción. ¿Qué método la lanza? ¿Por qué el último caso no cierra la app pero da un resultado absurdo?

## Paso 2 · Crea la rama de trabajo

```bash
git checkout -b v2-validacion
```

Todo lo que hagas a partir de aquí irá en esta rama; `main` se queda como está.

## Paso 3 · Corrige los fallos que ya trae la v1

Antes de añadir nada nuevo, limpia lo que hay:

1. **`activity_main.xml`**: busca la etiqueta de cierre `</TextView>>` y quita el `>` que sobra.
2. **`strings.xml`**: en `tu_edad_format` hay una falta de ortografía ("fuero"). Corrígela.
3. **Texto escrito "a fuego"**: el `android:hint="Ejemplo: 6"` del `EditText` está directamente en el layout. Llévalo a `strings.xml` con el nombre `hint_edad` y úsalo con `@string/hint_edad`.
   > 💡 Android Studio marca este aviso en amarillo (*Hardcoded string*). Prueba `Alt + Enter` encima.
4. **`MainActivity.java`**: hay tres `import` que no se usan (`Insets`, `ViewCompat`, `WindowInsetsCompat`). Elimínalos (`Ctrl + Alt + O`).

## Paso 4 · Añade los mensajes de error

En `res/values/strings.xml` añade tres textos nuevos:

```xml
<string name="error_vacio">Introduce la edad de tu perro</string>
<string name="error_no_numero">Introduce un número entero válido</string>
<string name="error_rango">La edad debe estar entre %1$d y %2$d años</string>
```

> ❓ ¿Qué significan `%1$d` y `%2$d`? ¿En qué orden se rellenan?

## Paso 5 · Restringe la entrada desde el layout

En el `EditText` (`@+id/age_input`) añade:

```xml
android:inputType="number"
android:maxLength="2"
android:gravity="center"
android:importantForAutofill="no"
```

| Atributo | Para qué sirve |
|---|---|
| `inputType="number"` | Muestra el teclado numérico: sin letras, sin signo menos ni decimales. |
| `maxLength="2"` | No deja escribir más de 2 cifras. |
| `gravity="center"` | Centra el número en el campo. |
| `importantForAutofill="no"` | Evita que el sistema ofrezca autocompletar. |

> ⚠️ Esto **no sustituye** a la validación en Java: el campo puede quedarse vacío y nunca conviene fiarse solo de la interfaz. Por eso seguimos con el código.

## Paso 6 · Sustituye los números mágicos por constantes

Al principio de la clase `MainActivity` declara:

```java
private static final int FACTOR_CANINO = 7;
private static final int EDAD_MINIMA = 1;
private static final int EDAD_MAXIMA = 30; // ningún perro supera esta edad
```

y usa `FACTOR_CANINO` en lugar del `7`.

## Paso 7 · Convierte las vistas en atributos de la clase

Ahora `ageEdit` y `resultText` son variables locales de `onCreate()`. Como vas a usarlas desde otros métodos, decláralas como atributos:

```java
private EditText ageEdit;
private TextView resultText;
```

y en `onCreate()` asígnalas **sin volver a escribir el tipo**:

```java
ageEdit = findViewById(R.id.age_input);
resultText = findViewById(R.id.result_text);
```

> ❓ ¿Qué pasaría si dejaras `EditText ageEdit = findViewById(...)` dentro de `onCreate()`?

## Paso 8 · Saca la lógica del botón a un método

El `setOnClickListener` debe quedar así de corto:

```java
button1.setOnClickListener(view -> calcularEdad());
```

Toda la lógica pasa a un método nuevo: `private void calcularEdad()`.

## Paso 9 · Crea un método para mostrar errores

```java
private void mostrarError(String mensaje) {
    // 1. Mostrar el mensaje en el EditText (setError)
    // 2. Poner el foco en el EditText (requestFocus)
    // 3. Borrar el resultado anterior del TextView
}
```

## Paso 10 · Valida la entrada en `calcularEdad()`

Implementa el método siguiendo este orden. En cuanto algo falle, llama a `mostrarError(...)` y sal con `return`:

1. **Leer** el texto del `EditText` quitando espacios (`trim()`).
2. **¿Está vacío?** → `error_vacio`. Recuerda que `Integer.parseInt("")` lanza `NumberFormatException`.
3. **Convertir a `int`** dentro de un `try/catch (NumberFormatException e)` → si falla, `error_no_numero`.
4. **¿Está en rango?** Si `age < EDAD_MINIMA || age > EDAD_MAXIMA` → `error_rango`. Para rellenar `%1$d` y `%2$d`:
   ```java
   getString(R.string.error_rango, EDAD_MINIMA, EDAD_MAXIMA)
   ```
5. **Todo correcto**: quita el error del campo (`setError(null)`), calcula `age * FACTOR_CANINO` y muestra el resultado con `tu_edad_format`.

> ❓ ¿Por qué limitar la edad a 30 evita también el desbordamiento que viste en el Paso 1?

## Paso 11 · Vuelve a probar

Comprueba que **la app nunca se cierra**:

| Entrada | Resultado esperado |
|---|---|
| *(vacío)* | Error: "Introduce la edad de tu perro" |
| `0` | Error: "La edad debe estar entre 1 y 30 años" |
| `31` | Error de rango |
| `1` | "Si tu perro fuera humano tendría 7 años" |
| `30` | "Si tu perro fuera humano tendría 210 años" |

> 💡 Con el teclado numérico no puedes escribir `abc` ni `-4`. ¿Cómo comprobarías que el `try/catch` funciona? (Pista: quita `inputType` un momento.)

## Paso 12 · Guarda y sube tu trabajo

```bash
git add .
git commit -m "Validación de la entrada y control de errores aritméticos"
git push -u origin v2-validacion
```

Comprueba en GitHub que tu fork tiene ahora dos ramas: `main` y `v2-validacion`.

---

## ✅ Lista de comprobación

- [ ] La app no se cierra con ninguna entrada.
- [ ] Todos los textos están en `strings.xml` (ningún aviso *Hardcoded string*).
- [ ] No quedan números mágicos en `MainActivity`.
- [ ] `onCreate()` solo prepara la pantalla; la lógica está en `calcularEdad()`.
- [ ] El trabajo está en la rama `v2-validacion`, no en `main`.

---

## 📝 Respuestas · Sergio Mitchell Bocero

### Paso 1 · Pruebas con la v1

| Entrada | ¿Qué ocurre? |
|---|---|
| *(campo vacío)* | La app se cierra: `NumberFormatException: For input string: ""` |
| `abc` | La app se cierra: `NumberFormatException` |
| `3.5` | La app se cierra: `parseInt` no acepta decimales |
| `0` | No se cierra, pero dice que tendría 0 años |
| `-4` | No se cierra, pero da -28 años |
| `99999999999` | La app se cierra: el número no cabe en un `int` |
| `400000000` | No se cierra, pero da -1494967296 años |

La excepción la lanza `Integer.parseInt()`. El último caso no se cierra porque 400000000 sí cabe en un `int`, pero al multiplicarlo por 7 (2800000000) se pasa del máximo (2147483647) y el resultado se desborda y sale negativo.

### Preguntas

- **¿Qué significan `%1$d` y `%2$d`?** Son huecos para números enteros. El número indica qué parámetro va en cada uno: `%1$d` se rellena con el primero que se pasa a `getString()` (`EDAD_MINIMA`) y `%2$d` con el segundo (`EDAD_MAXIMA`).
- **¿Qué pasaría si dejaras `EditText ageEdit = findViewById(...)` en `onCreate()`?** Se crearía una variable local con el mismo nombre que tapa al atributo. El atributo seguiría a `null` y al usarlo en `calcularEdad()` la app se cerraría con un `NullPointerException`.
- **¿Por qué limitar la edad a 30 evita el desbordamiento?** Porque el resultado máximo es 30 × 7 = 210, muy lejos del límite de un `int`.
- **¿Cómo comprobar que el `try/catch` funciona?** Quitando un momento `android:inputType="number"` del layout para poder escribir `abc` o `3.5`: la app muestra "Introduce un número entero válido" y no se cierra.

---

## EdadCaninaMVC

Versión 3 de la app, en la rama `v3-mvc`. Hace lo mismo que la v2, pero organizada con **DataBinding** y el patrón **MVC**. El `applicationId` es `es.davante.edadcanina.mvc` para que la v2 y la v3 puedan estar instaladas a la vez. Cada paso de la guía tiene su commit (`Paso N: …`) y su etiqueta (`paso-1` … `paso-7`).

| Pieza | Dónde | Qué hace |
|---|---|---|
| Modelo | `modelo/EdadCaninaModelo`, `modelo/Validacion` | Guarda el texto, valida, calcula y avisa cuando algo cambia |
| Vista | `activity_main.xml`, `vista/AdaptadoresVista` | Se pinta leyendo el modelo y manda los clics al controlador |
| Controlador | `controlador/EdadCaninaControlador` | Convierte los clics de Calcular y Limpiar en órdenes al modelo |
| `MainActivity` | — | Crea las piezas, las conecta y guarda el estado al girar |

En `app/src/test` hay 21 pruebas JUnit (17 del modelo y 4 del controlador) que se ejecutan sin emulador. Las preguntas *Piensa* de cada paso están respondidas en los comentarios del código.

### Reto: casos raros

He elegido **Casos raros**: ¿qué hace la app con `05` o con `+5`?

- Con el modelo del paso 7, `Integer.parseInt` daba por buenas las dos: `05` → 35 años y `+5` → 35 años. Con `-4` también convertía y luego saltaba el error de rango.
- **Decisión:** `05` es válido, porque el cero de la izquierda no cambia el número. `+5` no: una edad no lleva signo, y el modelo tiene que aceptar solo lo que deja escribir el teclado numérico (cifras) sin fiarse de que la vista lo filtre.
- **Cómo:** en `calcular()`, antes del `parseInt`, compruebo con `texto.matches("[0-9]+")` que el texto solo tiene cifras. Si no, devuelve `NO_NUMERO`. El `try/catch` se queda para los números con tantas cifras que no caben en un `int`.
- **Pruebas:** `ceroALaIzquierda_esValido`, `signoMas_daErrorNoNumero`, `signoMenos_daErrorNoNumero` y `numeroQueNoCabeEnUnInt_daErrorNoNumero`. Con el modelo del paso 7 fallaban las de `+5` y `-4`; con el cambio pasan todas.

### Explícalo con tus palabras

**1. ¿Qué diferencia hay entre `@{...}` y `@={...}`?**

`@{}` enlaza en un sentido: la vista lee el dato del modelo y se vuelve a pintar cuando el modelo avisa. Lo uso en el resultado (texto y visibilidad) y en el error. `@={}` enlaza en los dos sentidos: además de leer, lo que escribe el usuario se guarda solo en el modelo (se llama a `setEdadTexto`). Lo uso en el `EditText`. Por eso el setter mira si el texto ha cambiado antes de avisar: si avisara siempre, la vista volvería a poner el texto, el modelo volvería a avisar y así sin parar.

**2. ¿Qué ocurre, paso a paso, desde que pulsas Calcular hasta que aparece «42»?**

1. Al escribir «6», el `@={}` ya ha llamado a `modelo.setEdadTexto("6")`.
2. Al pulsar Calcular, el `onClick` del XML ejecuta la lambda `() -> controlador.onCalcularClick()`.
3. El controlador le da la orden `modelo.calcular()`.
4. El modelo valida (no está vacío, solo tiene cifras y está entre 1 y 30) y calcula 6 × 7 = 42.
5. Guarda el resultado con `setEdadHumana(42)` y `setValidacion(OK)`. Cada setter avisa con `notifyPropertyChanged(BR.edadHumana)` y `notifyPropertyChanged(BR.validacion)`.
6. El binding, que escucha al modelo desde `binding.setModelo(modelo)`, vuelve a evaluar las expresiones que usan esos datos: el texto `@string/tu_edad_format(42)`, la visibilidad pasa a `VISIBLE` y el adaptador `errorValidacion` recibe `OK` y quita el error.
7. En pantalla aparece «Si tu perro fuera humano tendría 42 años».

**3. ¿Qué cambiarías para que el factor fuera 6 en vez de 7, y por qué solo tocas un fichero?**

Solo la constante `FACTOR_CANINO = 6` de `EdadCaninaModelo.java`. Es una regla de negocio, y en MVC todas las reglas están en el modelo: la vista solo muestra `edadHumana` y el controlador solo pide calcular, así que ninguno de los dos sabe cuál es el factor. Lo único que fallaría son las pruebas que esperan 42, y eso es bueno: avisan de que ha cambiado una regla y hay que actualizarlas.

### Autoevaluación

| Criterio | Nivel | Puntos | Por qué |
|---|---|---|---|
| 1 · DataBinding sin `findViewById` | Excelente | 1,5 / 1,5 | `dataBinding = true`, `<layout>` con los `xmlns` en la raíz, `DataBindingUtil` y ningún `findViewById`. Compila sin avisos. |
| 2 · Modelo separado y observable | Excelente | 2,5 / 2,5 | El paquete `modelo` no usa `android.*` ni textos; `calcular()` cubre vacío, no número y rango; `@Bindable` y `notifyPropertyChanged` con comprobación de cambio; setters del resultado privados; si cambia el texto, el resultado caduca. |
| 3 · Vista enlazada | Excelente | 2 / 2 | `<data>` con variables e imports, `@={}` en la entrada, resultado con `@{}` (formato y `INVISIBLE` para que no salte la pantalla) y error con un `@BindingAdapter` en `vista`. La Activity no tiene `setText` ni `setError`. |
| 4 · Controlador y eventos | Excelente | 1,5 / 1,5 | El controlador no tiene vistas, tiene un método por acción, los clics van con `@{() -> …}` desde el XML y Limpiar funciona. |
| 5 · Estado al girar | Excelente | 1 / 1 | Guardo solo el texto y si estaba calculado, con claves constantes, y restauro antes de enlazar. El resultado y el error aguantan el giro y el cambio a modo oscuro. |
| 6 · Pruebas, historial y documentación | Excelente | 1,5 / 1,5 | 21 pruebas en verde (17 del modelo con casos de error y 4 del controlador), un commit y una etiqueta por paso, comentarios que explican el porqué y este README. |
| **Total** | | **10 / 10** | |
