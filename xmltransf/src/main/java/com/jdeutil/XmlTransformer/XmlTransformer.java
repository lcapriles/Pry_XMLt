package com.jdeutil.XmlTransformer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XmlTransformer {
  public static void main(String[] args) throws Exception {
    //Inicializamos...
    String pathIn = args[0];            
    String pathProcesados = args[1]; 
    String pathTransformed = args[2];   
    String ficheroLog = args[3];
    String xsltFile = "transform.xslt";
    Writer out = new BufferedWriter(new FileWriter(ficheroLog, true));

    try{
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date fecha = new Date();
      out.append("Inicio*** Fecha: " + dateFormat.format(fecha) + "\n");

      //Obtenemos del directorio 'pathIn' los archivos a transformar...
      out.append("Obteniendo ficheros a transformar:" + pathIn + " ");
      ArrayList<File> filesToXMLtransform = getFilesToXMLtransform(pathIn);
      out.append("OK \n");

      Boolean procesadosFlag = Boolean.valueOf(false);
      if (filesToXMLtransform.size() > 0)
        procesadosFlag = Boolean.valueOf(true); 
      for (int i = 0; i < filesToXMLtransform.size(); i++) {
        String fileToXMLtransform = pathIn + ((File)filesToXMLtransform.get(i)).getName();
        
        out.append("Tranformando fichero:" + fileToXMLtransform + " ");
        DateFormat dateFormatMove = new SimpleDateFormat("yyyyMMdd_HHmmss_");
        Date dateMove = new Date();
        String fileXMLtransformed = pathTransformed + dateFormatMove.format(dateMove) + ((File)filesToXMLtransform.get(i)).getName();      

        // Set up del transformer...
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

        // Set up the input source and output result...
        Source source = new StreamSource(fileToXMLtransform);
        Result result = new StreamResult(fileXMLtransformed);

        out.append("OK \n");
        
        // Hacemos la transformacion...
        out.append("Fichero transformado:" + fileXMLtransformed + " ");
        transformer.transform(source, result);

        out.append("OK \n");
        
        //Movemos el archivo al directorio 'pathProcesados'...
        String fileXMLDst = pathProcesados + ((File)filesToXMLtransform.get(i)).getName();
        File fileSrc = new File(fileToXMLtransform);
        File fileDst = new File(fileXMLDst);

        out.append("Moviendo a fichero:" + fileXMLDst + " ");
        InputStream inStream = new FileInputStream(fileSrc);
        OutputStream outStream = new FileOutputStream(fileDst);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inStream.read(buffer)) > 0)
          outStream.write(buffer, 0, length); 
        inStream.close();
        outStream.close();
        fileSrc.delete();
        out.append("OK \n");
        
      }

      if (procesadosFlag.booleanValue()) {
        out.append("Todos los archivos se han transformado correctamente!\n");
      } else {
        out.append("No hay archivos para enviar en el directorio (" + pathIn + ")\n");
      }

      //Terminamos...
      fecha = new Date();
      out.append("Fin*** Fecha: " + dateFormat.format(fecha) + "\n\n");
      out.close();
    } catch (Exception e) {
      out.append("\n" + e.toString() + "\n\n");
      out.close();
    } 
    System.exit(0);      
  }

  public static ArrayList<File> getFilesToXMLtransform(String path) {
    File folder = new File(path);
    File[] listOfFiles = folder.listFiles();
    ArrayList<File> filesToXMLtransform = new ArrayList<>();
    for (File elem : listOfFiles) {
      if (elem.isFile())
        filesToXMLtransform.add(elem); 
    } 
    return filesToXMLtransform;
  }  
}