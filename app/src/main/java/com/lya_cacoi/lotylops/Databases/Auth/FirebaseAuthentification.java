package com.lya_cacoi.lotylops.Databases.Auth;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class FirebaseAuthentification {

    public static void registerAsAnonym(final Context context, final AuthRegistrable view){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.i("main", "register completed");
                    if (user != null) {
                        transportPreferences.setUserId(context, user.getUid());
                        Log.i("main", "id - " + user.getUid());
                    }
                    view.onCompleteListener();
                }
                }
            });
        }
    public static String getId(Context context){
        return transportPreferences.getUserId(context);
    }

    public interface AuthRegistrable{
        void onCompleteListener();
    }
}
