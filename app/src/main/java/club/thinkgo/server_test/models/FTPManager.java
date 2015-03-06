package club.thinkgo.server_test.models;

import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.InetAddress;
import club.thinkgo.server_test.R;

/**
 * Created by Cristhian on 5/03/15.
 *
 */
public class FTPManager {

    // Ruta del directorio mnt/sdcard
    final String BasePath = Environment.getExternalStorageDirectory().getPath();
    // Ruta especifica del archivo
    public String uploadFilePath;
    // Contexto actual
    Context context;
    // Objeto FTPClient apache.commons.net
    FTPClient ftpClient;

    public FTPManager(Context context_act){
        context = context_act;
        ftpClient = new FTPClient();

        connect_to_server();
    }

    public void connect_to_server(){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ftpClient.connect(InetAddress.getByName(context.getString(R.string.ftp_server)));
            ftpClient.login(context.getString(R.string.ftp_user), context.getString(R.string.ftp_password));
            Log.i("FTPManager:","Conectado al Servidor");
        }
        catch (Exception e){
            Log.i(" Error FTPManager :Connect to server",e.toString());
        }
    }
    public void change_directory(String folder_id){
        try{
            String directory = context.getString(R.string.ftp_base_directory)+folder_id;
            ftpClient.changeWorkingDirectory(directory);
            Log.i("FTPManager:","Server directory :" +directory);
        }
        catch (Exception e){
            Log.i("Error FTPManager Change_directory :",e.toString());
        }

    }

    public void Set_local_Directory(String directory){
        uploadFilePath =  BasePath + directory;
    }
    public void SubirArchivo(String uploadFileName )  {

        try {


            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            BufferedInputStream buffIn=null;
            buffIn=new BufferedInputStream(new FileInputStream(uploadFilePath + uploadFileName));
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(uploadFileName, buffIn);

            buffIn.close();

            Log.i("Enviado :", uploadFileName);

        } catch (Exception e){
            Log.i("Error FTPManager SubirArchivo :",e.toString());}
    }

    public void Close_connection(){
        try {
            ftpClient.logout();
            ftpClient.disconnect();
            Log.i("FTPManager :","Desconectado");
        }
        catch (Exception e ){
            Log.i("Error FTPManager Close_connection:",e.toString());
        }

    }

}
