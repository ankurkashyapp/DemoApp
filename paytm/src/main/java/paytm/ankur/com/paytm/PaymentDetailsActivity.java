package paytm.ankur.com.paytm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PaymentDetailsActivity extends AppCompatActivity {

    private EditText enterMobile;
    private EditText enterAmount;
    private EditText enterDescription;

    private TextView descMobile;
    private TextView descAmount;
    private TextView description;

    private Button submitDetails;

    private ProgressBar progressBar;
    private View maskView;

    private String mobileNumber;
    private String customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        enterMobile = (EditText)findViewById(R.id.enter_mobile);
        enterAmount = (EditText) findViewById(R.id.enter_amount);
        enterDescription = (EditText) findViewById(R.id.enter_description);

        descMobile = (TextView)findViewById(R.id.desc_mobile);
        descAmount = (TextView) findViewById(R.id.desc_amount);
        description = (TextView) findViewById(R.id.desc_optional);

        submitDetails = (Button)findViewById(R.id.submit_details);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        maskView = findViewById(R.id.mask_view);

        enterMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (enterMobile.getText().toString().equals("Mobile Number")) {
                        enterMobile.setText("");
                    }
                    descMobile.setVisibility(View.VISIBLE);
                    descMobile.setTextColor(getResources().getColor(R.color.cyan));
                    enterMobile.setTextColor(getResources().getColor(R.color.black));
                }
                else {
                    if (enterMobile.getText().toString().equals("")) {
                        enterMobile.setText("Mobile Number");
                        descMobile.setVisibility(View.GONE);
                        enterMobile.setTextColor(getResources().getColor(R.color.colorGrey));
                    } else {
                        descMobile.setVisibility(View.VISIBLE);
                        descMobile.setTextColor(getResources().getColor(R.color.colorGrey));
                        enterMobile.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });


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

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(PaymentDetailsActivity.this, PaymentSuccessful.class);
                if (getCustomerName(enterMobile.getText().toString()) != null) {
                    intent.putExtra("isNameAvailable", true);
                    intent.putExtra("mobileCustomerName", getCustomerName(enterMobile.getText().toString()));
                }
                else
                    intent.putExtra("isNameAvailable", false);
                intent.putExtra("mobileNumber", enterMobile.getText().toString());
                intent.putExtra("amount", enterAmount.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
                maskView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        maskView.setVisibility(View.GONE);
                        startActivity(intent);
                    }
                }, 4000);

            }
        });


    }

    private String getCustomerName(String mobileNumber) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CustomerData", 0);
        return pref.getString(mobileNumber, null);
    }
}
