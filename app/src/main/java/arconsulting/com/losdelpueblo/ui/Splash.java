package arconsulting.com.losdelpueblo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import arconsulting.com.losdelpueblo.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {
    @BindView(R.id.splashLogo)
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnimations();
    }

    private void initAnimations(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        logo.startAnimation(animation);
        logo.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO init things
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO ingresar lógica de aplicación aquí
                Intent i = new Intent(Splash.this, Registro.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
