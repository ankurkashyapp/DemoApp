package paytm.ankur.com.paytm;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PaymentSuccessful extends AppCompatActivity {

    private boolean isNameAvailable;
    private String mobileNumber;
    private String mobileCustomerName;
    private String amount;
    private String description;

    private ImageView successAnimImage;
    private TextView amountText;
    private TextView mobileCustomerNameText;
    private TextView mobileNumberText;
    private TextView dateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
        successAnimImage = (ImageView)findViewById(R.id.success_anim);
        amountText = (TextView)findViewById(R.id.amount);
        mobileCustomerNameText = (TextView)findViewById(R.id.mobile_customer_name);
        mobileNumberText = (TextView)findViewById(R.id.mobile_number);
        dateTime = (TextView)findViewById(R.id.date_time);
        successAnimImage.setBackgroundResource(R.drawable.succeess_anim);
        AnimationDrawable anim = (AnimationDrawable)successAnimImage.getBackground();
        anim.start();
        Intent intent = getIntent();
        isNameAvailable = intent.getBooleanExtra("isNameAvailable", false);
        mobileNumber = intent.getStringExtra("mobileNumber");
        mobileCustomerName = intent.getStringExtra("mobileCustomerName");
        amount = intent.getStringExtra("amount");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(calendar.getTime());
        String hour;
        String AM_PM;
        if (calendar.get(Calendar.HOUR) > 12) {
            hour = String.valueOf(calendar.get(Calendar.HOUR) - 12);
            AM_PM = "PM";
        }
        else if (calendar.get(Calendar.HOUR) >9 && calendar.get(Calendar.HOUR) < 12) {
            hour = String.valueOf(calendar.get(Calendar.HOUR));
            AM_PM = "AM";
        }
        else if (calendar.get(Calendar.HOUR) < 10) {
            hour = "0" + calendar.get(Calendar.HOUR);
            AM_PM = "AM";
        }
        else {
            hour = String.valueOf(calendar.get(Calendar.HOUR));
            AM_PM = "PM";
        }

        dateTime.setText(hour + ":" + calendar.get(Calendar.MINUTE) + " " + AM_PM + ", " + formattedDate);

        amountText.setText(amount);
        if (isNameAvailable) {
            mobileCustomerNameText.setText(mobileCustomerName);
            mobileNumberText.setText(mobileNumber);
            mobileNumberText.setVisibility(View.VISIBLE);
        }
        else {
            mobileCustomerNameText.setText(mobileNumber);
            mobileNumberText.setVisibility(View.GONE);
        }
    }
}
