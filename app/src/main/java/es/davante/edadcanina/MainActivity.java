package es.davante.edadcanina;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Constantes
    private static final int FACTOR_CANINO = 7;
    private static final int EDAD_MINIMA = 1;
    private static final int EDAD_MAXIMA = 30; // ningún perro supera esta edad

    // Vistas que se usan en varios métodos
    private EditText ageEdit;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ageEdit = findViewById(R.id.age_input);
        resultText = findViewById(R.id.result_text);
        Button button1 = findViewById(R.id.btn_calc);

        button1.setOnClickListener(view -> calcularEdad());
    }

    private void calcularEdad() {
        // 1. Leer el texto quitando espacios
        String ageString = ageEdit.getText().toString().trim();

        // 2. Campo vacío
        if (ageString.isEmpty()) {
            mostrarError(getString(R.string.error_vacio));
            return;
        }

        // 3. Convertir a número
        int age;
        try {
            age = Integer.parseInt(ageString);
        } catch (NumberFormatException e) {
            mostrarError(getString(R.string.error_no_numero));
            return;
        }

        // 4. Comprobar el rango
        if (age < EDAD_MINIMA || age > EDAD_MAXIMA) {
            mostrarError(getString(R.string.error_rango, EDAD_MINIMA, EDAD_MAXIMA));
            return;
        }

        // 5. Todo correcto: quitar el error y mostrar el resultado
        ageEdit.setError(null);
        int dogAge = age * FACTOR_CANINO;
        resultText.setText(getString(R.string.tu_edad_format, dogAge));
    }

    private void mostrarError(String mensaje) {
        ageEdit.setError(mensaje);
        ageEdit.requestFocus();
        resultText.setText("");
    }
}
