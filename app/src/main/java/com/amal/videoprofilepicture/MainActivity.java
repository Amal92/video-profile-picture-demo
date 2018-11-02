package com.amal.videoprofilepicture;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.amal.videoprofilepicture.Models.VideoDetails;
import com.amal.videoprofilepicture.Utils.MyBottomSheetDialogFragment;
import com.amal.videoprofilepicture.Utils.callbacklistener;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements callbacklistener {

    private VideoView videoView;
    private Button video;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView) findViewById(R.id.videoView);
        video = (Button) findViewById(R.id.video);
        imageView = (ImageView) findViewById(R.id.imageView);
        final BottomSheetDialogFragment myBottomSheet = MyBottomSheetDialogFragment.newInstance("Modal Bottom Sheet",this);
       /* video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());

                Intent intent = new Intent();
                intent.setType("video*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 100);

            }
        });*/

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
        videoView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));
        videoView.start();

    }


    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());
                    String ss = file.getPath();
                    String s= getMimeType(uri);
                    File videoFile;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        videoFile = copyInputStreamToFile(inputStream);
                        String p = videoFile.getPath();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Glide.with(MainActivity.this)
                            .load(uri)
                            .into(imageView);

                    String path = getPath(uri);
                    if (s.contains("video")){
                        Log.d("","");
                    }
                }
                break;
        }
    }


    private File copyInputStreamToFile(InputStream in) {
        File filesDir = getApplicationContext().getFilesDir();
        File videoFile = new File(filesDir, System.currentTimeMillis() + ".jpg");
        try {
            OutputStream out = new FileOutputStream(videoFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoFile;
    }

    @Override
    public void passedData(String data) {
        Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
    }
}
