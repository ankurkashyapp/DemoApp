package paytm.ankur.com.paytm;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class PaymentSuccessful extends AppCompatActivity {

    private ImageView successAnimImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
        successAnimImage = (ImageView)findViewById(R.id.success_anim);
        successAnimImage.setBackgroundResource(R.drawable.succeess_anim);
        AnimationDrawable anim = (AnimationDrawable)successAnimImage.getBackground();
        anim.start();
    }
}
