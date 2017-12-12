package com.farm.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/*
 * This class converts image to byte array
 */
public class ImageUpload {
	
	/*
	 * Compresses and converts an image to byte array
	 * @Param filePath - file path of the image
	 */
	public static String convertImage(String filePath){
		String encImage = null;
		
		try{
		
		File imagefile = new File(filePath);
		FileInputStream fis = null;
		try {
		    fis = new FileInputStream(imagefile);
		    } catch (FileNotFoundException e) {
		    e.printStackTrace();
		}

		Bitmap bm = BitmapFactory.decodeStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);    
		byte[] b = baos.toByteArray(); 
		encImage = Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encImage;
	}
}
