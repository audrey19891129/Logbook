package com.packet.ocx_android.ui.expense;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.packet.ocx_android.MainActivity;
import com.packet.ocx_android.R;
import com.packet.ocx_android.controllers.adapters.ImageConverter;
import com.packet.ocx_android.controllers.connection.Request;
import com.packet.ocx_android.models.Depences;
import com.packet.ocx_android.models.Depences_Type;
import com.packet.ocx_android.models.Vehicles_Type;
import com.packet.ocx_android.ui.database.Expense_Type.ExpenseTypeDB;
import com.packet.ocx_android.ui.database.Vehicle_Type.VehicleTypeDB;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.HttpResponse;


public class NewExpense extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static String currentPhotoPath;
    ConstraintLayout takePicture;
    Button save;
    ImageView img;
    TextView title;
    Spinner exp_types;
    EditText date, amount, description;
    Depences expense;
    Bitmap picture;
    public Handler handler;

    public NewExpense() {}

    public static NewExpense newInstance(String param1, String param2) {
        NewExpense fragment = new NewExpense();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_expense, container, false);

        save = view.findViewById(R.id.btn_save_new_expense);
        takePicture = view.findViewById(R.id.btn_take_picture);
        img = view.findViewById(R.id.img_bill);
        title = view.findViewById(R.id.textView19);
        takePicture.setClickable(true);
        exp_types = view.findViewById(R.id.spin_new_expense_type);
        date = view.findViewById(R.id.txt_new_expense_date);
        amount = view.findViewById(R.id.txt_new_expense_amount);
        description = view.findViewById(R.id.txt_new_expense_description);
        expense = new Depences();
        handler = new Handler();
        date.setText(MainActivity.getDate());

        //SPINNER
        ExpenseTypeDB etdb = new ExpenseTypeDB(getContext());
        etdb.openForRead();
        ArrayList<Depences_Type> types = etdb.getExpenseTypes();
        etdb.close();
        ArrayAdapter<Depences_Type> spinnerArrayAdapter = new ArrayAdapter<Depences_Type>(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);
        exp_types.setAdapter(spinnerArrayAdapter);


        takePicture.setOnClickListener(v->{
            startCamera();
        });

        save.setOnClickListener(v->{

            File file = new File(currentPhotoPath);
            try {
                //String encoded = ImageConverter.encodeTo64(file);
                Bitmap compressed = ImageConverter.compress(picture);
                String encoded = ImageConverter.encodeTo64(compressed);
                expense.setImageFile(encoded);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(expense.imageFile != null) {

                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                expense.setDescription(description.getText().toString().trim());
                if (description.getText().toString().isEmpty()) {
                    expense.setDescription("aucune");
                }
                try {
                    Date value = sdf.parse(date.getText().toString().trim());
                    expense.setDateDepence(date.getText().toString().trim());

                    try {
                        Double dbl_amount = Double.parseDouble(amount.getText().toString().trim());
                        if (dbl_amount > 0) {
                            expense.setMontant(dbl_amount);
                            expense.setDepense_type_id(types.get(exp_types.getSelectedItemPosition()).id);

                            // WRITE TO SQLITE

                            new Thread(()->{

                                try {
                                    HttpResponse response = Request.POST("addDepence", expense.toJSON(), true);

                                    if(response.getStatusLine().getStatusCode() < 300){
                                        handler.post(()->{
                                            alert(getContext(), android.R.drawable.ic_dialog_info, getResources().getString(R.string.alert_success), getResources().getString(R.string.alert_success_save));
                                            getParentFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_container_expenses_section, ListExpense.class, null)
                                                    .commit();
                                        });
                                    }
                                    else{
                                        handler.post(()->{
                                            alert(getContext(), android.R.drawable.ic_delete, getResources().getString(R.string.alert_error), getResources().getString(R.string.alert_could_not_save));
                                        });
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }).start();


                        } else {
                            alert(getContext(), android.R.drawable.ic_delete, "ERREUR", "Le montant saisi doit etre superieur a 0");
                        }
                    } catch (Exception f) {
                        alert(getContext(), android.R.drawable.ic_delete, "ERREUR", "Le montant saisi n'est pas valide");
                    }

                } catch (ParseException e) {
                    alert(getContext(), android.R.drawable.ic_delete, "ERREUR", "La date saisie n'est pas valide");
                }
            }
            else{
                alert(getContext(), android.R.drawable.ic_delete, "ERREUR", "L'image n'est pas valide");
            }




            //WRITE TO DB



            //SEND TO SERVER

        });

        return view;
    }

    public void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = getOutputMediaFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, 100);
        }
    }


    private static File getOutputMediaFile() throws IOException {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ocx_images");

        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                return null;
            }
        }
        File image = File.createTempFile(
                "bill",
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        int targetW = img.getWidth();
        int targetH = img.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        img.setImageBitmap(bitmap);
        picture = bitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        title.setVisibility(View.VISIBLE);
        img.getLayoutParams().height = 1500;
        img.requestLayout();
        galleryAddPic();
        setPic();
        save.setVisibility(View.VISIBLE);
    }

    private void alert(Context context, int icon, String title, String message){
        new AlertDialog.Builder(context)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dia, gs) -> {
                })
                .show();
    }
}