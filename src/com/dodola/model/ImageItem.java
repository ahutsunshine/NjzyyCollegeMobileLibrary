package com.dodola.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class ImageItem {
	
    public static final Map<String, String> imageInfoMap;
    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("001.jpg", "—ß–£∏≈¿¿");
        map.put("002.jpg", "æ¥ŒƒÕº Èπ›");
        map.put("003.jpg", "æ¥ŒƒÕº Èπ›");
        map.put("DSC00002.jpg", "Õº Èπ›");
        map.put("DSC00003.jpg", "Õº Èπ›");
        map.put("DSC00004.jpg", "Õº Èπ›");
//        map.put("DSC00005.JPG", "Õº Èπ›");
        map.put("DSC00007.jpg", "Õº Èπ›");
        map.put("DSC00008.jpg", "Õº Èπ›");
//        map.put("IMG_0326-2.jpg", "Õº Èπ›");
        imageInfoMap = Collections.unmodifiableMap(map);
    }
    
	private int height;
	public String fileName;
	private String id = "";
	public Bitmap image;

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
