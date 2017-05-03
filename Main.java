package clement.montestandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Clément on 01/05/2017.
 */

public class Main extends AppCompatActivity {

    Button contact = null;
    Button new_event = null;
    Button current_event = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        contact = (Button) findViewById(R.id.contact);
        new_event = (Button) findViewById(R.id.new_event);
        current_event = (Button) findViewById(R.id.current_event);


        Button contact = (Button) findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, Contact.class);
                startActivity(intent);
            }
        });
    }
}
