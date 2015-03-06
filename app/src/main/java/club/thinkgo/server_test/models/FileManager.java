package club.thinkgo.server_test.models;

/**
 * Created by Cristhian on 05/03/15.
 *
 */
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class FileManager {
    //Variables de comprobacion de Estado de la SD
    public boolean sdDisponible = false;
    public boolean sdAccesoEscritura = false;
    public String fileName = "";
    public String path = "";

    public FileManager(String filename ,String _path){
        fileName = filename;
        path = _path;
        //Comprobamos el estado de la SD
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            sdDisponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            sdDisponible = true;
            sdAccesoEscritura = false;
        }
        else
        {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }

    }

    public boolean sdDisponible(){
        if(sdDisponible && sdAccesoEscritura){
            return true;
        }
        return false;
    }

    public void writeFile(String text){
        File ruta = Environment.getExternalStorageDirectory();


        // File f = new File(ruta.getAbsolutePath(), "prueba_sd.txt");
        try{
            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(ruta.getAbsolutePath() + "/" +path+ fileName, true), "UTF-8");
            BufferedWriter bfr = new BufferedWriter(fout);

            bfr.write(text);
            bfr.newLine();
            bfr.close();
        }catch(Exception ex){
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD: " + ex.getMessage());
        }
    }

    public String[] fileToArray(){
        //Conversion a Array
        File ruta = Environment.getExternalStorageDirectory();
        String[] arr = null;

        try{
            Scanner sc = new Scanner(new File(String.valueOf(ruta) + "/" +path+ fileName));
            List<String> lines = new ArrayList<String>();

            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            arr = lines.toArray(new String[0]);
        }catch(Exception ex){
            Log.e("Ficheros", "Error al leer fichero a tarjeta SD: " + ex.getMessage());
        }

        return arr;
    }

}