package paytm.ankur.com.paytm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PaymentDetailsActivity extends AppCompatActivity {

    private EditText enterMobile;
    private EditText enterAmount;
    private EditText enterDescription;

    private TextView descMobile;
    private TextView descAmount;
    private TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        enterAmount = (EditText) findViewById(R.id.enter_amount);
        descAmount = (TextView) findViewById(R.id.desc_amount);
        enterDescription = (EditText) findViewById(R.id.enter_description);
        description = (TextView) findViewById(R.id.desc_optional);

        enterAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("***Inside onFocusChange", "************************");
                if (hasFocus) {
                    if (enterAmount.getText().toString().equals("Enter Amount")) {
                        enterAmount.setText("");
                    }
                    descAmount.setVisibility(View.VISIBLE);
                    descAmount.setTextColor(getResources().getColor(R.color.cyan));
                    enterAmount.setTextColor(getResources().getColor(R.color.black));

                } else {
                    if (enterAmount.getText().toString().equals("")) {
                        enterAmount.setText("Enter Amount");
                        descAmount.setVisibility(View.GONE);
                        enterAmount.setTextColor(getResources().getColor(R.color.colorGrey));
                    } else {
                        descAmount.setVisibility(View.VISIBLE);
                        descAmount.setTextColor(getResources().getColor(R.color.colorGrey));
                        enterAmount.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });

        enterDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (enterDescription.getText().toString().equals("Optional Description")) {
                        enterDescription.setText("");
                    }
                    description.setVisibility(View.VISIBLE);
                    description.setTextColor(getResources().getColor(R.color.cyan));
                    enterDescription.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    if (enterDescription.getText().toString().equals("")) {
                        enterDescription.setText("Optional Description");
                        description.setVisibility(View.GONE);
                        enterDescription.setTextColor(getResources().getColor(R.color.colorGrey));
                    } else {
                        description.setVisibility(View.VISIBLE);
                        description.setTextColor(getResources().getColor(R.color.colorGrey));
                        enterDescription.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });
    }
}
