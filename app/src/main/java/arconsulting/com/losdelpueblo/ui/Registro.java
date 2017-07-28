package arconsulting.com.losdelpueblo.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

import arconsulting.com.losdelpueblo.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class Registro extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    CallbackManager mFacebookCallbackManager;

    @BindView(R.id.registro_image_profile)
    CircleImageView imageProfile;

    @BindView(R.id.registro_text_input_nombre)
    TextInputEditText etNombre;

    @BindView(R.id.registro_text_input_fecha)
    TextInputEditText etFecha;

    @BindView(R.id.facebook_sign_in_button)
    LoginButton btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        focusEvents();
        initFacebookItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void focusEvents(){
        // TODO hacer los eventos para cada TextInput a validar
        etNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus){
                    if (etNombre.getText().length() == 0){
                        etNombre.setError("El campo no puede estar vacio.");
                    }
                }
            }
        });
    }

    public void initFacebookItems(){
        mFacebookCallbackManager = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));
        btnFacebook.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            // get profile information
                            String name = "";
                            String birthday = "";
                            String email = "";
                            String uriPicture = "";
                            Log.e("Facebook Login data", object.toString());
                            if (object.getString("name") != null) {
                                name = object.getString("name");
                            }
                            if (object.getString("birthday") != null) {
                                birthday = object.getString("birthday");
                            }
                            if (object.getString("email") != null) {
                                email = object.getString("email");
                            }
                            if (object.getString("picture") != null) {
                                JSONObject imagen = new JSONObject(object.getString("picture"));
                                JSONObject imagen2 = new JSONObject(imagen.getString("data"));
                                uriPicture = imagen2.getString("url");
                            }
                            // TODO almacenar o redireccionar cuando se obtengan los datos
                            etNombre.setText(name);
                            etFecha.setText(birthday);
                            Picasso.with(Registro.this).load(uriPicture).into(imageProfile);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // TODO actions on cancel facebook login
                Toast.makeText(Registro.this, "Inicio de sesión cancelado ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                // TODO actions on error facebook login
                Toast.makeText(Registro.this, "Error Facebook: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.registro_take_image)
    public void takeImage(View v){
        // TODO programar selección de imagen o lanzar camara
        Toast.makeText(this, "Tomar imagen", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.registro_text_input_fecha)
    public void setFechaNacimiento(View v)
    {
        // TODO obtener fecha
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(Registro.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO setear fecha
                        etFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
