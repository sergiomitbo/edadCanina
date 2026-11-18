package es.davante.edadcanina.modelo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import es.davante.edadcanina.BR;

/**
 * Modelo de la app: guarda los datos, los valida y calcula la edad humana.
 * <p>
 * No importa nada de android.* (solo androidx.databinding): así se puede probar con
 * JUnit sin abrir el emulador y, si la app se traduce o cambia el diseño, esta clase
 * no se toca.
 * <p>
 * Hereda de BaseObservable para poder avisar a la vista cuando cambia un dato: el
 * binding se apunta como oyente y repinta solo lo que depende de ese dato.
 */
public class EdadCaninaModelo extends BaseObservable {

    // Reglas de negocio: antes estaban en MainActivity. Son públicas porque la vista
    // necesita la mínima y la máxima para escribir el mensaje del error de rango.
    public static final int FACTOR_CANINO = 7;
    public static final int EDAD_MINIMA = 1;
    public static final int EDAD_MAXIMA = 30; // ningún perro supera esta edad

    private String edadTexto = "";   // lo que ha escrito el usuario, tal cual
    private int edadHumana = 0;      // resultado del último cálculo correcto
    private Validacion validacion = Validacion.SIN_CALCULAR;

    // @Bindable crea la constante BR.edadTexto, que es el "nombre" con el que se avisa
    @Bindable
    public String getEdadTexto() {
        return edadTexto;
    }

    // Público: lo llama la vista con @={} cada vez que el usuario escribe
    public void setEdadTexto(String nuevo) {
        if (nuevo == null) {
            nuevo = "";
        }
        // Si no ha cambiado no se avisa. Con @={} es imprescindible: la vista llama a este
        // setter, el aviso hace que la vista vuelva a poner el texto, y si avisáramos
        // siempre volvería a empezar. Este if corta el viaje de vuelta.
        if (nuevo.equals(edadTexto)) {
            return;
        }
        edadTexto = nuevo;
        notifyPropertyChanged(BR.edadTexto);
        // Regla de negocio: si el texto cambia, el resultado anterior ya no vale.
        // Va aquí porque el setter es el único sitio por el que cambia el texto.
        setEdadHumana(0);
        setValidacion(Validacion.SIN_CALCULAR);
    }

    @Bindable
    public int getEdadHumana() {
        return edadHumana;
    }

    // Privado: solo calcular() puede cambiar el resultado
    private void setEdadHumana(int nueva) {
        if (nueva == edadHumana) {
            return;
        }
        edadHumana = nueva;
        notifyPropertyChanged(BR.edadHumana);
    }

    @Bindable
    public Validacion getValidacion() {
        return validacion;
    }

    // Privado por lo mismo: la validación solo cambia al calcular o al cambiar el texto
    private void setValidacion(Validacion nueva) {
        if (nueva == validacion) {
            return;
        }
        validacion = nueva;
        notifyPropertyChanged(BR.validacion);
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

        // Reto «Casos raros»: solo se aceptan cifras. Integer.parseInt da por buenos
        // "+5" y "-4", pero una edad no lleva signo (el teclado numérico tampoco deja
        // escribirlo) y el modelo no debe fiarse de lo que filtre la vista.
        // "05" sí vale: el cero de la izquierda no cambia el número.
        if (!texto.matches("[0-9]+")) {
            return guardarError(Validacion.NO_NUMERO);
        }

        int edad;
        try {
            edad = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            // Son cifras, pero tantas que el número no cabe en un int
            return guardarError(Validacion.NO_NUMERO);
        }

        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            return guardarError(Validacion.FUERA_DE_RANGO);
        }

        setEdadHumana(edad * FACTOR_CANINO);
        setValidacion(Validacion.OK);
        return validacion;
    }

    /**
     * Deja el modelo como recién creado (botón Limpiar).
     * Se ponen las tres cosas a mano porque, si el texto ya estaba vacío (por ejemplo,
     * tras el error de campo vacío), setEdadTexto("") no cambia nada y el error seguiría.
     */
    public void limpiar() {
        setEdadTexto("");
        setEdadHumana(0);
        setValidacion(Validacion.SIN_CALCULAR);
    }

    // Si hay un error, el resultado anterior se borra para que no se quede a la vista
    private Validacion guardarError(Validacion error) {
        setEdadHumana(0);
        setValidacion(error);
        return validacion;
    }
}
