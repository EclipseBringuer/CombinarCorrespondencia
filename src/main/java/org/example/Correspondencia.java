package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase proporciona métodos para combinar correspondencia a partir de una plantilla y un archivo de datos.
 */
public class Correspondencia {
    /**
     * Combina correspondencia a partir de una plantilla y un archivo de datos.
     *
     * @param template El nombre del archivo de la plantilla.
     * @param archivo El nombre del archivo de datos.
     */
    public static void combinarCorrespondencia(String template, String archivo) {
        comprobarCarpeta();
        String plantilla = leerPlantilla(template);
        ArrayList<String> datos = obtenerDatos(archivo);

        int contador = 1;

        for (String st : datos) {
            if (st.split(",").length == 5) {
                procesarDatos(st, plantilla);
            } else {
                System.out.println("Error, falta información en el archivo " + archivo + " en la linea " + contador);
            }
            contador++;
        }
    }
    /**
     * Comprueba la existencia de la carpeta "salida" y elimina archivos existentes si los hay, o la crea si no existe.
     */
    private static void comprobarCarpeta() {
        File carpeta = new File("." + File.separator + "salida");
        if (carpeta.exists()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    archivo.delete();
                }
            }
        } else {
            carpeta.mkdir();
        }
    }
    /**
     * Guarda el resultado de la correspondencia en un archivo de salida.
     *
     * @param salida El contenido de la correspondencia combinada.
     * @param id El identificador del archivo de salida.
     */
    private static void guardarSalida(String salida, String id) {
        try (FileWriter fr = new FileWriter("." + File.separator + "salida" + File.separator + "template-" + id + ".txt")) {
            fr.write(salida);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo");
        }
    }
    /**
     * Lee el contenido de la plantilla desde un archivo.
     *
     * @param archivo El nombre del archivo de plantilla.
     * @return El contenido de la plantilla.
     */
    private static String leerPlantilla(String archivo) {
        String plantilla;
        try {
            plantilla = Files.readString(Paths.get(archivo));
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la plantilla");
        }
        return plantilla;
    }
    /**
     * Obtiene los datos a partir de un archivo de datos.
     *
     * @param datos El nombre del archivo de datos.
     * @return Una lista de cadenas de texto representando los datos.
     */
    private static ArrayList<String> obtenerDatos(String datos) {
        ArrayList<String> filas = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("." + File.separator + datos))) {
            while (sc.hasNext()) {
                filas.add(sc.nextLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al obtener los datos");
        }
        return filas;
    }
    /**
     * Procesa los datos de correspondencia y reemplaza las etiquetas en la plantilla con valores correspondientes.
     *
     * @param st Una cadena de datos de correspondencia.
     * @param plantilla La plantilla con etiquetas a reemplazar.
     */
    private static void procesarDatos(String st, String plantilla) {
        String[] datos = st.split(",");
        Pattern patron = Pattern.compile("%%(\\d+)%%");
        Matcher matcher = patron.matcher(plantilla);
        while (matcher.find()) {
            String numeroEncontrado = matcher.group(1);
            plantilla = switch (numeroEncontrado) {
                case "1" -> plantilla.replace("%%1%%", datos[0]);
                case "2" -> plantilla.replace("%%2%%", datos[1]);
                case "3" -> plantilla.replace("%%3%%", datos[2]);
                case "4" -> plantilla.replace("%%4%%", datos[3]);
                case "5" -> plantilla.replace("%%5%%", datos[4]);
                default -> plantilla;
            };
        }
        guardarSalida(plantilla, datos[0]);
    }
}