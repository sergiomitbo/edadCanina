package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;
import es.davante.edadcanina.modelo.Validacion;

public class MainActivity extends AppCompatActivity {

    // El binding sustituye a los findViewById: tiene un campo por cada vista con id.
    // Si un id está mal escrito, ahora el error sale al compilar y no al ejecutar.
    private ActivityMainBinding binding;

    // Las reglas (validar y calcular) ya no están aquí: las tiene el modelo
    private EdadCaninaModelo modelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Esta versión de setContentView infla el layout y además devuelve el binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        modelo = new EdadCaninaModelo();
        // Desde aquí el binding escucha al modelo y repinta la vista cuando este avisa
        binding.setModelo(modelo);

        binding.btnCalc.setOnClickListener(view -> calcularEdad());
    }

    private void calcularEdad() {
        // No hace falta leer el EditText: con @={} el modelo ya tiene el texto.
        // El resultado tampoco se pinta aquí: lo pinta el XML cuando el modelo avisa.
        Validacion resultado = modelo.calcular();

        // El error, de momento, se sigue pintando a mano
        switch (resultado) {
            case VACIO:
                mostrarError(getString(R.string.error_vacio));
                break;
            case NO_NUMERO:
                mostrarError(getString(R.string.error_no_numero));
                break;
            case FUERA_DE_RANGO:
                mostrarError(getString(R.string.error_rango,
                        EdadCaninaModelo.EDAD_MINIMA, EdadCaninaModelo.EDAD_MAXIMA));
                break;
            default:
                // OK (SIN_CALCULAR no puede salir de calcular()): sin error
                binding.ageInput.setError(null);
                break;
        }
    }

    private void mostrarError(String mensaje) {
        binding.ageInput.setError(mensaje);
        binding.ageInput.requestFocus();
    }
}
