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

public class Correspondencia {

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

    private static void guardarSalida(String salida, String id) {
        try (FileWriter fr = new FileWriter("." + File.separator + "salida" + File.separator + "template-" + id + ".txt")) {
            fr.write(salida);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo");
        }
    }

    private static String leerPlantilla(String archivo) {
        String plantilla;
        try {
            plantilla = Files.readString(Paths.get(archivo));
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la plantilla");
        }
        return plantilla;
    }

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
