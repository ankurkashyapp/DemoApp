package paytm.ankur.com.paytm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView paytmMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        paytmMain = (ImageView)findViewById(R.id.paytm_main_image);
        paytmMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PaymentActivity.class));
            }
        });
        storeData();
    }

    private void storeData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CustomerData", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("9730617621", "Mukesh Choudhary");
        editor.putString("9586922166", "Ramani Payal");
        editor.putString("7218035367", "Hari Thapa");
        editor.putString("8237409775", "sirvi fresh mart mart");
        editor.putString("8378986198", "Food Ginie");
        editor.putString("8623945998", "Mithai Magic");
        editor.putString("8888611555", "Ghar Ka Khana");
        editor.putString("8149121841", "Thali Gujarati");
        editor.putString("9225513099", "Manpasand Dabeli");
        editor.putString("9225513099", "Manpasand Dabeli");
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
