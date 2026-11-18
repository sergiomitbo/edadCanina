package es.davante.edadcanina.modelo;

import androidx.databinding.Observable;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.davante.edadcanina.BR;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void calcular_avisaDeLosCambios() {
        // Apuntamos un oyente igual que hace el binding y anotamos de qué nos avisa
        List<Integer> avisos = new ArrayList<>();
        modelo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                avisos.add(propertyId);
            }
        });
        modelo.setEdadTexto("6");
        avisos.clear();

        modelo.calcular();

        assertTrue(avisos.contains(BR.validacion));
        assertTrue(avisos.contains(BR.edadHumana));
    }

    @Test
    public void mismoTexto_noAvisa() {
        modelo.setEdadTexto("6");
        List<Integer> avisos = new ArrayList<>();
        modelo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                avisos.add(propertyId);
            }
        });

        // Es lo que pasa en el viaje de vuelta del @={}: el texto llega igual que estaba
        modelo.setEdadTexto("6");

        assertTrue(avisos.isEmpty());
    }
}
