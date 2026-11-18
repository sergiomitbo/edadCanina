package es.davante.edadcanina;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import es.davante.edadcanina.databinding.ActivityMainBinding;
import es.davante.edadcanina.modelo.EdadCaninaModelo;

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

        // Resultado y error los pinta el XML cuando el modelo avisa: aquí solo se calcula
        binding.btnCalc.setOnClickListener(view -> modelo.calcular());
    }
}
