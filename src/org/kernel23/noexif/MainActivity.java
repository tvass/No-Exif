package org.kernel23.noexif;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                
    }

    @Override
    public void onResume()
    {
             super.onResume();

             Intent intent = getIntent();
             Bundle extras = intent.getExtras();
             String action = intent.getAction();

             // if this is from the share menu
             if (Intent.ACTION_SEND.equals(action)) {   if (extras.containsKey(Intent.EXTRA_STREAM)) {
                 // Get resource path
                 Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
                 String filename = parseUriToFilename(uri);
                 
                
                 try{
                	                 	 
             	    File sourceFile=new File(filename);
             	    File root = Environment.getExternalStorageDirectory();
             	    File destFile = new File(root, "no-exif-tmp.jpg");
             	   
             	    FileInputStream inStream = new FileInputStream(sourceFile);
             	    FileOutputStream outStream = new FileOutputStream(destFile);
          
             	    byte[] buffer = new byte[1024];
          
             	    int length;
             	    //copy the file content in bytes 
             	    while ((length = inStream.read(buffer)) > 0){
          
             	    	outStream.write(buffer, 0, length);
          
             	    }
          
             	    inStream.close();
             	    outStream.close();
                           	
                    Uri NoexifUri = Uri.fromFile(destFile);
                    
 
                    ExifInterface exifInterface = new ExifInterface(destFile.getPath());
                    exifInterface.setAttribute(ExifInterface.TAG_APERTURE,"");
                    exifInterface.setAttribute(ExifInterface.TAG_DATETIME, "");
                    exifInterface.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, "");
                    exifInterface.setAttribute(ExifInterface.TAG_FLASH, "");
                    exifInterface.setAttribute(ExifInterface.TAG_FOCAL_LENGTH, "");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, "");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, "");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_DATESTAMP,"");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,"");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD, "");
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, "");
                    exifInterface.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, "");
                    exifInterface.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, "");
                    exifInterface.setAttribute(ExifInterface.TAG_ISO, "");
                    exifInterface.setAttribute(ExifInterface.TAG_MODEL, "");
                    exifInterface.setAttribute(ExifInterface.TAG_MAKE, "");
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "");
                    exifInterface.setAttribute(ExifInterface.TAG_WHITE_BALANCE, "");
                    exifInterface.saveAttributes();

                 	Toast.makeText(getApplicationContext(), "Temporay file created. You can now share it with your usual application.", Toast.LENGTH_LONG).show();
                    
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, NoexifUri);
                    startActivity(Intent.createChooser(sharingIntent, "File is now cleaned. Share image with ..."));
                    
                    destFile.deleteOnExit();

             	}catch(IOException e){
             		e.printStackTrace();
                 	Toast.makeText(getApplicationContext(), "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
             		finish();
             	}
                 
                 
                 
               }
             }else{
             	Toast.makeText(getApplicationContext(), "You have to call NO-EXIF from sharing menu. ", Toast.LENGTH_LONG).show();
             	finish();
             }
             
     }
    
    private String parseUriToFilename(Uri uri) {
    		  String selectedImagePath = null;
    		  String filemanagerPath = uri.getPath();

    		  String[] projection = { MediaStore.Images.Media.DATA };
    		  Cursor cursor = managedQuery(uri, projection, null, null, null);

    		  if (cursor != null) {
    		    // Here you will get a null pointer if cursor is null
    		    // This can be if you used OI file manager for picking the media
    		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    		    cursor.moveToFirst();
    		    selectedImagePath = cursor.getString(column_index);
    		  }

    		  if (selectedImagePath != null) {
    		    return selectedImagePath;
    		  }
    		  else if (filemanagerPath != null) {
    		    return filemanagerPath;
    		  }
    		   return null;
	}
}
