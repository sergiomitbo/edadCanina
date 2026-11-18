package es.davante.edadcanina.modelo;

/**
 * Modelo de la app: guarda los datos, los valida y calcula la edad humana.
 * <p>
 * No importa nada de android.*: así se puede probar con JUnit sin abrir el emulador
 * y, si la app se traduce o cambia el diseño, esta clase no se toca.
 */
public class EdadCaninaModelo {

    // Reglas de negocio: antes estaban en MainActivity. Son públicas porque la vista
    // necesita la mínima y la máxima para escribir el mensaje del error de rango.
    public static final int FACTOR_CANINO = 7;
    public static final int EDAD_MINIMA = 1;
    public static final int EDAD_MAXIMA = 30; // ningún perro supera esta edad

    private String edadTexto = "";   // lo que ha escrito el usuario, tal cual
    private int edadHumana = 0;      // resultado del último cálculo correcto
    private Validacion validacion = Validacion.SIN_CALCULAR;

    public String getEdadTexto() {
        return edadTexto;
    }

    public void setEdadTexto(String nuevo) {
        if (nuevo == null) {
            nuevo = "";
        }
        if (nuevo.equals(edadTexto)) {
            return;
        }
        edadTexto = nuevo;
        // Regla de negocio: si el texto cambia, el resultado anterior ya no vale.
        // Va aquí porque el setter es el único sitio por el que cambia el texto.
        edadHumana = 0;
        validacion = Validacion.SIN_CALCULAR;
    }

    public int getEdadHumana() {
        return edadHumana;
    }

    public Validacion getValidacion() {
        return validacion;
    }

    /**
     * Valida el texto guardado y, si es correcto, calcula la edad humana.
     * Devuelve una Validacion y no un mensaje para no depender de Android.
     */
    public Validacion calcular() {
        // Los espacios de los lados no cuentan
        String texto = edadTexto.trim();

        if (texto.isEmpty()) {
            return guardarError(Validacion.VACIO);
        }

        int edad;
        try {
            edad = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            // Letras, decimales o un número que no cabe en un int
            return guardarError(Validacion.NO_NUMERO);
        }

        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            return guardarError(Validacion.FUERA_DE_RANGO);
        }

        edadHumana = edad * FACTOR_CANINO;
        validacion = Validacion.OK;
        return validacion;
    }

    // Si hay un error, el resultado anterior se borra para que no se quede a la vista
    private Validacion guardarError(Validacion error) {
        edadHumana = 0;
        validacion = error;
        return validacion;
    }
}
