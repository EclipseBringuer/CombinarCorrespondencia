package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String plantilla = leerPlantilla("template.txt");
        ArrayList<String> datos = obtenerDatos("data.csv");

        for (String st : datos) {
            String salida = procesarDatos(st, plantilla);
            guardarSalida(salida);
        }
    }

    private static void guardarSalida(String salida) {
        File carpeta = new File("."+File.separator+"salida");
        if(!carpeta.exists()){
            carpeta.mkdir();
            try(FileWriter fr = new FileWriter("./salida/ejemplo.txt")){
                fr.write(salida);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            try(FileWriter fr = new FileWriter("./salida/ejemplo.txt")){
                fr.write(salida);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private static String leerPlantilla(String archivo) {
        String plantilla;
        try {
            plantilla = Files.readString(Paths.get(archivo));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return filas;
    }

    private static String procesarDatos(String st, String plantilla) {
        String salida;
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
        salida = plantilla;
        return salida;
    }
}