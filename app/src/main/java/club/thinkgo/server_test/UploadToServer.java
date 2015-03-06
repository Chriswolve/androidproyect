package club.thinkgo.server_test;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import static android.os.Process.killProcess;


public class UploadToServer extends Activity {

    // Atributos
    protected TextView messageText;
    protected  Button buton;
    protected int contador =0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);
        kill_services(":my_process_img");
        kill_services(":my_process_db");


        /*  HERENCIA DE SERVICE*/
        final Intent i_db = new Intent(this,UploadDBService.class);
        startService(i_db);


        /* HERENCIA DE INTENSERVICE*/
        final Intent i_img = new Intent(Intent.ACTION_SYNC, null, this, UploadImagesService.class);

        /*  CONTROLES */
        messageText  = (TextView)findViewById(R.id.messageText);
        buton  = (Button)findViewById(R.id.button);

        /* Evento CLICK BUTTON */
        buton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(contador == 0)
                {
                    // Inicia el servicio
                    startService(i_img);
                }
                // Aumenta el contador
                messageText.setText("Tap :" +contador++);
            }
        });
    }

    public void kill_services(String process_id){

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        // List of Process
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        // Iterator of Process
        Iterator<ActivityManager.RunningAppProcessInfo> iter = runningAppProcesses.iterator();

        while(iter.hasNext())
        {
            ActivityManager.RunningAppProcessInfo next = iter.next();
            String processName = getPackageName() + process_id;
            if(next.processName.equals(processName))
            {
                // Kill of Process
                killProcess(next.pid);
                break;
            }
        }
    }
}


