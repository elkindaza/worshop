package com.example.works;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ValidadorService {

    public boolean esArchivoCSV(MultipartFile archivo) {
        String nombreArchivo = archivo.getOriginalFilename();
        return nombreArchivo != null && nombreArchivo.toLowerCase().endsWith(".csv");
    }

    public boolean esArchivoExcel(MultipartFile archivo) {
        String nombreArchivo = archivo.getOriginalFilename();
        return nombreArchivo != null && (nombreArchivo.toLowerCase().endsWith(".xls") || nombreArchivo.toLowerCase().endsWith(".xlsx"));
    }

    public boolean validarRegistrosExcel(String registros) {

        boolean excel = validarReport(registros);
        //System.out.println("REGISTRO == "+registros);
            // Validar cada registro individualmente
           // if (!validarInjury(registros)) {
        //System.out.println("VALIDAR +++++++ "+ !validarReport(registros));
        if (validarInjury(registros)==true && validarReport(registros)==true) {
            return true; // Si ambas condiciones no se cumplen, retornar false
        }
        return false; // Ambas condiciones se cumplieron, todos los registros son válidos

    }




    private boolean validarInjury(String registro) {
        String[] datos = registro.split(",");
        String jobTitle = datos[1].trim();

        String[] titulosValidos = {
                "N/A"
        };

        for (String tituloValido : titulosValidos) {
            if (jobTitle.equalsIgnoreCase(tituloValido)) {
                return true; // Retorna true si el título de trabajo es válido
            }
        }

        return false; // Retorna false si el título de trabajo no es válido
    }

    private boolean validarReport(String registro) {

        String[] datos = registro.split(",");
        String jobTitle2 = datos[7].trim();

        String[] titulosValidos = {
                "Near Miss",
                "Lost Time",
                "First Aid"
        };
        for (String tituloValido : titulosValidos) {
            if (jobTitle2.equalsIgnoreCase(tituloValido)) {
                return true; // Retorna true si el título de trabajo es válido
            }
        }
        return false; // Retorna false si el título de trabajo no es válido
    }




    private String getStringValueFromCell(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }

        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue()).trim();
        }

        return "";
    }


    public boolean validarRegistro(String registro) {

        // validar el correo
        boolean correoValido = validarCorreoElectronico(registro);
        //Validar el Date of birth 1980-01-01
        boolean fechaValido= validarFechaNacimiento(registro);
        //Validar el Job Title ,
        boolean jobtitle =validarJobTitle(registro);
        if(correoValido == true && fechaValido == true && jobtitle == true){
            return true;
        }else {
            return false;
        }


    }

    private boolean validarCorreoElectronico(String registro) {
        String[] datos = registro.split(","); // Suponiendo que los datos estén separados por comas

        for (String dato : datos) {
            if (dato.trim().matches("^[A-Za-z0-9+_.-]+@(example\\.com|gmail\\.com)$")) {
                return true; // Se encontró un correo válido en el formato deseado
            }
        }
        return false; // No se encontró ningún correo válido en el formato deseado
    }

    private boolean validarFechaNacimiento(String registro) {
        String[] datos = registro.split(","); // Suponiendo que los datos estén separados por comas


        // Si es la primera línea (encabezados de columna), la ignoramos
        if (datos[0].equalsIgnoreCase("Index") || datos[0].equalsIgnoreCase("Date of birth") ) {
            return true; // Retorna true para ignorar la validación en la primera línea
        }

        String fechaNacimiento = datos[7]; // Ajusta el índice según la posición de la fecha en tus datos

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaLimite;

            try {
                fechaLimite = dateFormat.parse("1980-01-01");
            } catch (ParseException e) {
                e.printStackTrace();
                return false; // Manejo de excepción, podrías ajustarlo según tu necesidad
            }

        try {
            Date fechaNacimientoDate = dateFormat.parse(fechaNacimiento);
            if (fechaNacimientoDate.after(fechaLimite)) {
                return true; // Retorna true si la fecha de nacimiento es posterior al 1980-01-01
            } else {
                return false; // Retorna false si no cumple la condición
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Si ocurre una excepción al parsear la fecha de nacimiento, se retorna false
        }
    }

    private boolean validarJobTitle(String registro) {
        String[] datos = registro.split(",");
        String jobTitle = datos[8].trim();

        String[] titulosValidos = {
                "Haematologist",
                "Phytotherapist",
                "Building surveyor",
                "Insurance account manager",
                "Educational psychologist"
        };

        for (String tituloValido : titulosValidos) {
            if (jobTitle.equalsIgnoreCase(tituloValido)) {
                return true; // Retorna true si el título de trabajo es válido
            }
        }

        return false; // Retorna false si el título de trabajo no es válido
    }

}
