package es.davante.edadcanina.modelo;

/**
 * Resultado de validar la edad que ha escrito el usuario.
 * <p>
 * El modelo devuelve uno de estos valores y no un mensaje: los mensajes están en
 * strings.xml y dependen del idioma, y eso es cosa de la vista, no del modelo.
 */
public enum Validacion {
    SIN_CALCULAR,   // todavía no se ha calculado, o el texto ha cambiado después
    OK,             // edad correcta: ya hay resultado
    VACIO,          // el campo está vacío
    NO_NUMERO,      // no se puede convertir a un número entero
    FUERA_DE_RANGO  // es un número, pero no está entre la edad mínima y la máxima
}
