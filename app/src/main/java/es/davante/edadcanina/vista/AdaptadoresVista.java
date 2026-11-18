package es.davante.edadcanina.vista;

import android.content.Context;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import es.davante.edadcanina.R;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

/**
 * Atributos XML propios para la vista.
 * <p>
 * Está en el paquete vista y no en modelo porque traduce la Validacion a un texto de
 * strings.xml y toca un EditText: eso es pintar, y el modelo no debe saber de Android.
 */
public final class AdaptadoresVista {

    // Solo tiene métodos estáticos: no tiene sentido crear objetos de esta clase
    private AdaptadoresVista() {
    }

    /**
     * Atributo app:errorValidacion. DataBinding llama a este método cada vez que cambia
     * la Validacion del modelo: recibe la vista (el EditText) y el valor de @{...}.
     */
    @BindingAdapter("errorValidacion")
    public static void mostrarErrorValidacion(EditText campo, Validacion validacion) {
        String mensaje = mensajeDeError(campo.getContext(), validacion);
        campo.setError(mensaje); // con null se quita el error
        if (mensaje != null) {
            // Igual que en la v2: el foco va al campo para que se vea el mensaje
            campo.requestFocus();
        }
    }

    // El switch que antes estaba en MainActivity. Devuelve null si no hay error.
    private static String mensajeDeError(Context contexto, Validacion validacion) {
        if (validacion == null) {
            return null;
        }
        switch (validacion) {
            case VACIO:
                return contexto.getString(R.string.error_vacio);
            case NO_NUMERO:
                return contexto.getString(R.string.error_no_numero);
            case FUERA_DE_RANGO:
                return contexto.getString(R.string.error_rango,
                        EdadCaninaModelo.EDAD_MINIMA, EdadCaninaModelo.EDAD_MAXIMA);
            default:
                // SIN_CALCULAR u OK: no hay nada que avisar
                return null;
        }
    }
}
