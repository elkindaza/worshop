package com.example.works;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;

@Service
public class ProcesadorService {
    private ValidadorService validadorService;

    private int validos2;
    private int invalidos2;

    public int getValidos() {
        return validos2;
    }

    public int getInvalidos() {
        return invalidos2;
    }

    public ProcesadorService(ValidadorService validadorService) {
        this.validadorService = validadorService;
    }


    public void procesarArchivo(MultipartFile filePath) {
        System.out.println("ENTRO AL IF DE PROCESAR ARCHIVO");
        if (validadorService.esArchivoCSV(filePath)) {
            try {
                List<String> registros = leerRegistros(filePath);
                int validos = 0;
                int invalidos = 0;

                for (String registro : registros) {
                    boolean esValido = validadorService.validarRegistro(registro);
                    if (esValido) {
                        validos++;
                    } else {
                        invalidos++;
                    }
                }

                System.out.println("Registros válidos: " + validos);
                validos2=validos;
                System.out.println("Registros inválidos: " + invalidos);
                invalidos2=invalidos;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (validadorService.esArchivoExcel(filePath)) {
            System.out.println("ENTRO AL EXCEL....");
            try {
                List<String> registros = leerRegistrosDesdeExcel(filePath);
                int validos = 0;
                int invalidos = 0;

                for (String registro : registros) {
                   boolean esValido = validadorService.validarRegistrosExcel(registro);
                    //System.out.println("validacion procesador service = "+ esValido);
                   //boolean esValido= true;
                    if (esValido) {
                        validos++;
                    } else {
                        invalidos++;
                    }
                }


                System.out.println("Registros válidos: " + validos);
                validos2=validos;
                System.out.println("Registros inválidos: " + invalidos);
                invalidos2=invalidos;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Archivo no soportado
            System.out.println("Formato de archivo no soportado");
        }
    }

    private List<String> leerRegistros(MultipartFile multipartFile) throws IOException {
        List<String> registros = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                registros.add(linea);
            }
        }

        return registros;
    }

   /* private List<String> leerRegistrosDesdeExcel(MultipartFile multipartFile) throws IOException {
        List<String> registros = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Suponiendo que estamos leyendo la primera hoja

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                StringBuilder registro = new StringBuilder();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case STRING:
                            registro.append(cell.getStringCellValue()).append(",");
                            break;
                        case NUMERIC:
                            registro.append(cell.getNumericCellValue()).append(",");
                            break;
                        // Añadir otros casos para otros tipos de celdas si es necesario
                    }
                }

                // Agregar el registro a la lista, eliminando la última coma
                registros.add(registro.substring(0, registro.length() - 1));
            }
        }

        return registros;
    }*/

    private List<String> leerRegistrosDesdeExcel(MultipartFile multipartFile) throws IOException {
        System.out.println("LEYENDO EXCEL");
        List<String> registros = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Suponiendo que estamos leyendo la primera hoja

            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();

                StringBuilder registro = new StringBuilder();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case STRING:
                            registro.append(cell.getStringCellValue()).append(",");
                            break;
                        case NUMERIC:
                            registro.append(cell.getNumericCellValue()).append(",");
                            break;
                        // Añadir otros casos para otros tipos de celdas si es necesario
                    }
                }

                // Agregar el registro a la lista, eliminando la última coma
                if (registro.length() > 0) {
                    registros.add(registro.substring(0, registro.length() - 1));
                }
            }
        }

        return registros;
    }


}
