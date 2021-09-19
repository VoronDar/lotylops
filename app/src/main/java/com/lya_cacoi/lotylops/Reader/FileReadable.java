package com.lya_cacoi.lotylops.Reader;

import android.content.Context;
import android.content.Intent;

// интерфейс активностей/фрагментов, из которых будет происходить чтение колоды
public interface FileReadable {
    void showError(String string);
    void startActivityForResult(Intent intent, int reqest_code);
    Context getContext();
    void update();
}
