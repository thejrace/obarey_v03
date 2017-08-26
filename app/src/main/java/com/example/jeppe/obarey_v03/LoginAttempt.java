package com.example.jeppe.obarey_v03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jeppe on 09.04.2016.
 */
public class LoginAttempt implements AsyncResponse {

    private Context mContext;
    private String mEmail;
    private ArrayList<LinearLayout> mLayouts;

    public LoginAttempt( Context ctx, String email, ArrayList<LinearLayout> layouts ){
        mEmail = email;
        mContext = ctx;
        mLayouts = layouts;
    }

    public void start(){
        PopupLoader.show( mContext );
        Map<String, String> params= new HashMap<>();
        params.put("email", mEmail);
        params.put("type", "do_check_for_email");
        ServerRequest req = new ServerRequest(ServerRequest.SERVICE_REGISTER, "POST");
        req.send(params);
        req.mDelegate = this;
        req.execute();
    }


    public void processFinishJSON(JSONObject output){
        System.out.println(output);

        /*
        * output:
        *   success(bool)
        *   status(string) [ login = 1, register = 0 ]
        *   text(string)
        *   type(string)
        *   user_id(int) [ login, status = 1 ]
        */

        int STATUS_REGISTER = 0, STATUS_LOGIN = 1;
        TextView errorNotfFirstStep = (TextView)mLayouts.get(0).findViewById(R.id.step_one_error_notf);
        try{
            Boolean success = output.getBoolean("success");
            if( success ){
                // eposta invalid
                if( output.getBoolean("error") ) {
                    errorNotfFirstStep.setText(output.getString("text"));
                    return;
                }
                // hide first step
                mLayouts.get(0).setVisibility(View.GONE);
                Integer status = output.getInt("status");
                if( status == STATUS_REGISTER ){
                    LinearLayout activeLayout = mLayouts.get(2);
                    // show register layout
                    activeLayout.setVisibility(View.VISIBLE);
                    Button registerStepSubmit = (Button)activeLayout.findViewById(R.id.forward_pass_register);
                    final TextView errorNotfSecondStepRegister = (TextView)activeLayout.findViewById(R.id.step_two_error_notf_register);
                    final EditText inputRegisterPass = (EditText)activeLayout.findViewById(R.id.password);
                    final EditText inputRegisterPassAgain = (EditText)activeLayout.findViewById(R.id.password_again);
                    registerStepSubmit.setOnClickListener( new View.OnClickListener(){
                        @Override
                        public void onClick( View v ){
                            String password = inputRegisterPass.getText().toString(),
                                   password_again = inputRegisterPassAgain.getText().toString();
                            if( !password.isEmpty() && !password_again.isEmpty() && password.equals(password_again) ){
                                Register register = new Register( mContext, mEmail, password, errorNotfSecondStepRegister );
                                register.action();
                            } else {
                                errorNotfSecondStepRegister.setText(R.string.form_error_password_match);
                            }
                        }
                    });
                } else if( status == STATUS_LOGIN ){
                    // show login layout
                    LinearLayout activeLayout = mLayouts.get(1);
                    activeLayout.setVisibility(View.VISIBLE);
                    final Integer userId = output.getInt("user_id");
                    Button loginStepSubmit = (Button)activeLayout.findViewById(R.id.forward_pass_login);
                    final TextView errorNotfSecondStepLogin = (TextView)activeLayout.findViewById(R.id.step_two_error_notf_login);
                    final EditText inputLoginPass = (EditText)activeLayout.findViewById(R.id.password_login);
                    loginStepSubmit.setOnClickListener( new View.OnClickListener(){
                        @Override
                        public void onClick( View v ){
                            String password = inputLoginPass.getText().toString();
                            if( !password.isEmpty()  ){
                                Login login = new Login( mContext, userId, password, errorNotfSecondStepLogin);
                                login.action();
                            } else {
                                errorNotfSecondStepLogin.setText(R.string.form_error_password_empty);
                            }
                        }
                    });
                }
            }
        } catch( JSONException e ){
            System.out.println("LoginAttempt Json fail " + e.toString() );
        }
        PopupLoader.hide();
    }




}
