package project.virtualkdc_app;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView sentText;
    private Button getButton;

    public String message;
    public Socket s = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sentText = (TextView) findViewById(R.id.sentText);
        getButton = (Button) findViewById(R.id.getButton);


        //set up socket
        try {
            s = new Socket("192.168.254.11", 8888);
        } catch (IOException e) {
            sentText.setText("Error creating socket");
        }

        //set up onclicklistener for button
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //take in message from server
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    message = input.readLine();
                    sentText.setText(message);
                } catch (IOException e) {
                    sentText.setText("Error getting message");
                }
            }
        });




    }
}
