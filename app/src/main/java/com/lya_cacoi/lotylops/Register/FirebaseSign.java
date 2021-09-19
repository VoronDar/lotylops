package com.lya_cacoi.lotylops.Register;

import android.util.Log;


public class FirebaseSign{
    private Assignable view;
    //private FirebaseAuth mAuth;
    private static final String SIGN_TAG = "FAuth";

    public FirebaseSign(Assignable view){
        this.view = view;
    }

    public void signIn(String email, String password){
        //mAuth = FirebaseAuth.getInstance();
        Log.i(SIGN_TAG, "signInWithEmail:start");
        /*
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(SIGN_TAG, "signInWithEmail:success");
                            view.onSuccess();
                        } else {
                            Log.i(SIGN_TAG, "signInWithEmail:failure");
                            view.onFailure();
                        }

                    }
                });

         */
    }
}