package es.davante.edadcanina.controlador;

import es.davante.edadcanina.modelo.EdadCaninaModelo;

/**
 * Controlador: recibe las acciones del usuario (los clics llegan desde el XML) y las
 * convierte en órdenes al modelo.
 * <p>
 * No tiene ninguna referencia a vistas ni calcula nada. Que sea tan corto es buena
 * señal: si creciera, sería porque está haciendo el trabajo de la vista o del modelo.
 */
public class EdadCaninaControlador {

    private final EdadCaninaModelo modelo;

    public EdadCaninaControlador(EdadCaninaModelo modelo) {
        this.modelo = modelo;
    }

    // Botón Calcular
    public void onCalcularClick() {
        modelo.calcular();
    }

    // Botón Limpiar
    public void onLimpiarClick() {
        modelo.limpiar();
    }
}
