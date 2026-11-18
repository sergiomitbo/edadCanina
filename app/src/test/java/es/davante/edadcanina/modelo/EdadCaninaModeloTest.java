package es.davante.edadcanina.modelo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Pruebas del modelo. Se ejecutan en el PC, sin emulador, porque el modelo
 * no depende de ninguna clase de Android.
 */
public class EdadCaninaModeloTest {

    private EdadCaninaModelo modelo;

    @Before
    public void setUp() {
        // Un modelo nuevo en cada prueba para que no se afecten entre ellas
        modelo = new EdadCaninaModelo();
    }

    @Test
    public void modeloNuevo_estaSinCalcular() {
        assertEquals("", modelo.getEdadTexto());
        assertEquals(Validacion.SIN_CALCULAR, modelo.getValidacion());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void edadValida_calculaPorSiete() {
        modelo.setEdadTexto("6");
        assertEquals(Validacion.OK, modelo.calcular());
        assertEquals(42, modelo.getEdadHumana());
    }

    @Test
    public void limitesDelRango_sonValidos() {
        modelo.setEdadTexto("1");
        assertEquals(Validacion.OK, modelo.calcular());
        assertEquals(7, modelo.getEdadHumana());

        modelo.setEdadTexto("30");
        assertEquals(Validacion.OK, modelo.calcular());
        assertEquals(210, modelo.getEdadHumana());
    }

    @Test
    public void espaciosAlrededor_seIgnoran() {
        modelo.setEdadTexto(" 6 ");
        assertEquals(Validacion.OK, modelo.calcular());
        assertEquals(42, modelo.getEdadHumana());
    }

    @Test
    public void campoVacio_daErrorVacio() {
        modelo.setEdadTexto("");
        assertEquals(Validacion.VACIO, modelo.calcular());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void soloEspacios_daErrorVacio() {
        modelo.setEdadTexto("   ");
        assertEquals(Validacion.VACIO, modelo.calcular());
    }

    @Test
    public void texto_daErrorNoNumero() {
        modelo.setEdadTexto("abc");
        assertEquals(Validacion.NO_NUMERO, modelo.calcular());
    }

    @Test
    public void decimal_daErrorNoNumero() {
        modelo.setEdadTexto("3.5");
        assertEquals(Validacion.NO_NUMERO, modelo.calcular());
    }

    @Test
    public void cero_daErrorDeRango() {
        modelo.setEdadTexto("0");
        assertEquals(Validacion.FUERA_DE_RANGO, modelo.calcular());
        assertEquals(0, modelo.getEdadHumana());
    }

    @Test
    public void treintaYUno_daErrorDeRango() {
        modelo.setEdadTexto("31");
        assertEquals(Validacion.FUERA_DE_RANGO, modelo.calcular());
    }

    @Test
    public void cambiarElTexto_borraElResultadoAnterior() {
        modelo.setEdadTexto("6");
        modelo.calcular();

        modelo.setEdadTexto("7");

        assertEquals(Validacion.SIN_CALCULAR, modelo.getValidacion());
        assertEquals(0, modelo.getEdadHumana());
    }
}
