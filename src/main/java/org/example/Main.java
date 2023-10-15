package org.example;
/**
 * Esta clase proporciona un punto de entrada para el programa que combina correspondencia.
 */
public class Main {
    /**
     * Método principal que inicia el programa de combinación de correspondencia.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este programa).
     */
    public static void main(String[] args) {

        Correspondencia.combinarCorrespondencia("template.txt", "data.csv");

    }
}