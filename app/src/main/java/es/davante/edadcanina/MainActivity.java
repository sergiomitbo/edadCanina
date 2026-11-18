package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.controlador.EdadCaninaControlador;
import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

/**
 * La Activity ya no valida, ni calcula, ni pinta, ni escucha clics: solo crea las tres
 * piezas (modelo, vista y controlador), las conecta y guarda el estado al girar.
 */
public class MainActivity extends AppCompatActivity {

    // Claves del Bundle en constantes: se usan al guardar y al restaurar, y así no
    // hay riesgo de escribirlas distinto en los dos sitios
    private static final String CLAVE_EDAD_TEXTO = "edad_texto";
    private static final String CLAVE_CALCULADO = "calculado";

    // El binding sustituye a los findViewById: tiene un campo por cada vista con id.
    // Si un id está mal escrito, ahora el error sale al compilar y no al ejecutar.
    private ActivityMainBinding binding;

    // Atributo y no variable local porque onSaveInstanceState también lo necesita
    private EdadCaninaModelo modelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Esta versión de setContentView infla el layout y además devuelve el binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Al girar (o al cambiar a modo oscuro) Android crea una Activity nueva y con ella
        // un modelo nuevo y vacío: si hay algo guardado, se reconstruye antes de enlazar.
        // (Con MVVM el modelo viviría en un ViewModel, que sobrevive al giro, y esto sobraría)
        modelo = new EdadCaninaModelo();
        if (savedInstanceState != null) {
            restaurarEstado(savedInstanceState);
        }

        // Se entregan al binding las dos variables del XML. Si faltara el controlador,
        // las lambdas de los botones no harían nada al pulsarlos.
        binding.setModelo(modelo);
        binding.setControlador(new EdadCaninaControlador(modelo));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Se guarda lo mínimo: el texto y si ya se había calculado. El resultado o el
        // error no hace falta guardarlos porque calcular() da lo mismo con el mismo texto.
        outState.putString(CLAVE_EDAD_TEXTO, modelo.getEdadTexto());
        outState.putBoolean(CLAVE_CALCULADO, modelo.getValidacion() != Validacion.SIN_CALCULAR);
    }

    private void restaurarEstado(Bundle estado) {
        modelo.setEdadTexto(estado.getString(CLAVE_EDAD_TEXTO, ""));
        if (estado.getBoolean(CLAVE_CALCULADO)) {
            modelo.calcular(); // vuelve a dejar el resultado o el error que había
        }
    }
}
