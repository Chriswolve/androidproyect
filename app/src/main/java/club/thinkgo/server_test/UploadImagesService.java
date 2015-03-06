package club.thinkgo.server_test;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.File;

import club.thinkgo.server_test.models.FTPManager;
import club.thinkgo.server_test.models.FileManager;

/**
 * Created by Cristhian on 4/03/15.
 * Upload File in to Web Server
 */
public class UploadImagesService extends IntentService {

    // TAG del servicio
    private static final String TAG = "UploadImagesService";
    FTPManager ftp_manager;





    //CONSTRUCTOR
    public UploadImagesService() {
        super(UploadImagesService.class.getName());

    }

    //EXECUTE SERVICE
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        try {
            // Crear objeto FTPManager
            ftp_manager =  new FTPManager(this);
            Log.i("UploadImagesService:","Objeto FtpManager Creado");

            //Definir directorio del servidor
            ftp_manager.change_directory(""); // imei del usuario


            // Definir directorio imagenes
            ftp_manager.Set_local_Directory(getString(R.string.directory_img));
            Log.i("UploadImagesService:",ftp_manager.uploadFilePath);

            // Cargar y Subir imagenes al servidor
            SendImages();

            //Cerramos la conexion
            ftp_manager.Close_connection();
        }
        catch (Exception e){
            Log.i("IntenService Error :" ,e.toString());
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }


    public void SendImages(){

       String uploadFileName = "";
        File f = new File(ftp_manager.uploadFilePath);
        Log.i("UploadImagesService:" , "Send from -> "+ftp_manager.uploadFilePath);
        //Creo el array de tipo File con el contenido de la carpeta
        File[] files = f.listFiles();
        FileManager fm = new FileManager("text.txt",getString(R.string.directory_img));

        String[] imagenes = fm.fileToArray();
        //Hacemos un Loop por cada fichero para extraer el nombre de cada uno
        for (int i = 0; i < files.length; i++)

        {

            //Sacamos del array files un fichero
            File file = files[i];

            uploadFileName = file.getName();
            // Extraer la extencion
            String filenameArray[] = uploadFileName.split("\\.");
            String extension = filenameArray[filenameArray.length-1];
            Log.i("Extencion :",extension);
            if(extension.equals("jpg") || extension.equals("png")|| extension.equals("jpeg"))
            {
                if (!exist_img(imagenes,uploadFileName)) {
                    ftp_manager.SubirArchivo(uploadFileName);
                    if (fm.sdDisponible()) {
                        fm.writeFile(uploadFileName);
                    }
                }
                else
                    Log.i("File",uploadFileName +" Existe!!");
                //break;
            }
        }
    }

    public boolean exist_img(String[] array,String obj){

        for(int i = 0 ; i < array.length;i++){
            if(array[i].equals(obj)){
                return true;
            }
        }
     return false;
    }



}