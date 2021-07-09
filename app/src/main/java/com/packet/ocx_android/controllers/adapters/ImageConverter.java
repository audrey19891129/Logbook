package com.packet.ocx_android.controllers.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

public class ImageConverter {
/*
    public static String encodeTo64(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        stream.read(bytes);
        return new String(Base64.getEncoder().encodeToString(bytes));
    }*/

    public static String encodeTo64(Bitmap file) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        file.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.getEncoder().encodeToString(byteArray);
        return encoded;
    }

    public static Bitmap compress(Bitmap original){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.PNG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return decoded;
    }
}
