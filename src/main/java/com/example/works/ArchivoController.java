package com.example.works;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoController {

    private ProcesadorService procesadorService;

    @Autowired
    public ArchivoController(ProcesadorService procesadorService) {
        this.procesadorService = procesadorService;
    }

    @PostMapping("/procesar")
    public ResponseEntity<String> procesarArchivo(@RequestParam("archivo") MultipartFile archivo) {
        procesadorService.procesarArchivo(archivo);
        return ResponseEntity.ok("Archivo procesado exitosamente como CSV: \n Archivos validos: "+ procesadorService.getValidos()+"\n Archivos invalidos: "+procesadorService.getInvalidos());
    }

    @PostMapping("/procesarExcel")
    public ResponseEntity<String> procesarArchivoExcel(@RequestParam("archivo") MultipartFile archivo) {
        procesadorService.procesarArchivo(archivo);
        return ResponseEntity.ok("Archivo procesado exitosamente como Excel:\n  Archivos validos: "+ procesadorService.getValidos()+"\n Archivos invalidos: "+procesadorService.getInvalidos());
        //ResponseEntity<String> response = procesadorService.procesarArchivo(archivo);
       // return ResponseEntity.ok("Archivo procesado exitosamente como Excel");
    }
}

