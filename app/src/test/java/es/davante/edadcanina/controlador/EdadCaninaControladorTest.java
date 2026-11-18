package es.davante.edadcanina.controlador;

import org.junit.Before;
import org.junit.Test;

import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

import static org.junit.Assert.assertEquals;

/**
 * Pruebas del controlador: simulan los clics llamando a sus métodos y miran el modelo.
 * No hace falta ninguna vista porque el controlador no tiene referencias a ellas.
 */
public class EdadCaninaControladorTest {

    private EdadCaninaModelo modelo;
    private EdadCaninaControlador controlador;

    @Before
    public void setUp() {
        modelo = new EdadCaninaModelo();
        controlador = new EdadCaninaControlador(modelo);
    }

    @Test
    public void clicEnCalcular_calculaLaEdad() {
        modelo.setEdadTexto("6"); // lo que haría el @={} al escribir

        controlador.onCalcularClick();

        assertEquals(Validacion.OK, modelo.getValidacion());
        assertEquals(42, modelo.getEdadHumana());
    }

    @Test
    public void clicEnCalcular_conError_loGuardaEnElModelo() {
        modelo.setEdadTexto("31");

        controlador.onCalcularClick();

        assertEquals(Validacion.FUERA_DE_RANGO, modelo.getValidacion());
    }

    @Test
    public void clicEnLimpiar_trasUnResultado_loBorraTodo() {
        modelo.setEdadTexto("6");
        controlador.onCalcularClick();

        controlador.onLimpiarClick();

        assertEquals("", modelo.getEdadTexto());
        assertEquals(0, modelo.getEdadHumana());
        assertEquals(Validacion.SIN_CALCULAR, modelo.getValidacion());
    }

    @Test
    public void clicEnLimpiar_trasErrorDeCampoVacio_quitaElError() {
        controlador.onCalcularClick(); // campo vacío: error VACIO

        controlador.onLimpiarClick();

        assertEquals(Validacion.SIN_CALCULAR, modelo.getValidacion());
    }
}
