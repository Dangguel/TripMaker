package kr.dangguel.domestictravel;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

public class PostActivity extends AppCompatActivity {
    Spinner spinner;
    boolean isWish=false;

    EditText etTitle;
    EditText etDevice;
    EditText etBug;
    EditText etWish;

    TextView tvImageInput;

    View deviceLayout;
    View bugLayout;
    View wishLayout;

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        isWish=false;
                        deviceLayout.setVisibility(View.VISIBLE);
                        bugLayout.setVisibility(View.VISIBLE);
                        tvImageInput.setVisibility(View.VISIBLE);
                        wishLayout.setVisibility(View.GONE);
                        break;

                    case 1:
                        isWish=true;
                        deviceLayout.setVisibility(View.GONE);
                        bugLayout.setVisibility(View.GONE);
                        tvImageInput.setVisibility(View.GONE);
                        wishLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etTitle = findViewById(R.id.et_title);
        etDevice = findViewById(R.id.et_device);
        etBug = findViewById(R.id.et_bug);
        etWish = findViewById(R.id.et_wish);

        tvImageInput = findViewById(R.id.tv_image_input);

        deviceLayout = findViewById(R.id.device_layout);
        bugLayout = findViewById(R.id.bug_layout);
        wishLayout = findViewById(R.id.wish_layout);
    }

    public void clickImageInput(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,52);
    }

    public void clickSend(View view) {
        String serverUrl = null;
        if(!isWish){
            serverUrl = "http://dangguel.dothome.co.kr/TripMaker/insertDBbug.php";
        }else{
            serverUrl = "http://dangguel.dothome.co.kr/TripMaker/insertDBwish.php";
        }

        String title = etTitle.getText().toString();

        SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if(!isWish){
            String device = etDevice.getText().toString();
            String bug = etBug.getText().toString();


            multiPartRequest.addStringParam("title",title);
            multiPartRequest.addStringParam("device",device);
            multiPartRequest.addStringParam("bug",bug);
            multiPartRequest.addFile("upload",imgPath);

        }else{
            String wish = etWish.getText().toString();

            multiPartRequest.addStringParam("title",title);
            multiPartRequest.addStringParam("wish",wish);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multiPartRequest);

        setResult(RESULT_OK);
        finish();
    }

    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 52:
                    Uri uri = data.getData();
                    if(uri!=null){
                        imgPath = getRealPathFromUri(uri);
                        Toast.makeText(this, "이미지 첨부 완료", Toast.LENGTH_SHORT).show();
                        tvImageInput.setText("이미지 첨부 완료");
                    }
                    break;
            }
        }
    }
}
