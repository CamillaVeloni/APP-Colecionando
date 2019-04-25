package com.app.teste.colecionando.Ajuda;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.media.ExifInterface;

import java.io.FileDescriptor;
import java.io.IOException;

public class TratamentoDeFotos {


    public static String tratarCaminhoFoto(Context context, Uri caminhoImg){

        String[] arquivoCaminho = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver()
                .query(caminhoImg, arquivoCaminho, null, null, null);
        c.moveToFirst();
        int columnindex = c.getColumnIndex(arquivoCaminho[0]);
        String caminhoDaFoto = c.getString(columnindex);
        c.close();

        return caminhoDaFoto;
    }

    public static Bitmap tratarRotação(Bitmap bitmap, String local){

        ExifInterface exifInterface = null;
        try{
            exifInterface = new ExifInterface(local);

        }catch (Exception e){
            e.printStackTrace();
        }
        int orientação = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();

        switch (orientação){
            case ExifInterface.ORIENTATION_NORMAL: // Rotação 0 graus
                matrix.setRotate(0);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90: // Rotação 90 graus
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180: // Rotação 180 graus
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270: // Rotação 270 graus
                matrix.setRotate(270);
                break;
        }

        return Bitmap.createBitmap(bitmap, 0,0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
