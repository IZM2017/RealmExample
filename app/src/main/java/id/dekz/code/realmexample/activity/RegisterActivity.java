package id.dekz.code.realmexample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.code.realmexample.R;
import id.dekz.code.realmexample.model.User;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etRegisterFullname)EditText fullname;
    @BindView(R.id.etRegisterUsername)EditText username;
    @BindView(R.id.etRegisterPassword)EditText password;
    @BindView(R.id.btnRegister)Button register;

    @BindView(R.id.tvList)TextView listLog;

    private Realm realm;
    private int idUser;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        realm = Realm.getDefaultInstance();

        listLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmQuery<User> listUser = realm.where(User.class);
                RealmResults<User> resultUser = realm.where(User.class).findAll();
                Log.d("userlist",resultUser.toString());
                Toast.makeText(RegisterActivity.this, resultUser.get(0).getFullName(), Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullname.getText().length()==0 ||
                        username.getText().length()==0 ||
                        password.getText().length()==0){
                    Toast.makeText(RegisterActivity.this,"fill all fields!",Toast.LENGTH_SHORT).show();
                }else{
                    RealmResults<User> resultUser = realm.where(User.class).findAll();
                    //check if any user already registered
                    if(resultUser.size()>0){
                        //found users, get last userID
                        User exist = resultUser.get(resultUser.size()-1);
                        idUser = exist.getId()+1;
                        Log.d("userID",String.valueOf(idUser));
                        registerProccess(idUser);
                    }else{
                        //empty users
                        idUser = 1;
                        Log.d("userID",String.valueOf(idUser));
                        registerProccess(idUser);
                    }
                }
            }
        });
    }

    private void registerProccess(int id){
        realm.beginTransaction();
        User user = realm.createObject(User.class);
        user.setId(id);
        user.setFullName(fullname.getText().toString());
        user.setUserName(username.getText().toString());
        user.setPassword(password.getText().toString());
        realm.commitTransaction();
        Toast.makeText(getApplicationContext(), "register success", Toast.LENGTH_SHORT).show();
        RegisterActivity.this.finish();
    }
}
