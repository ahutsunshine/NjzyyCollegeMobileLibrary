package com.dodola.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class ImageItem {
	
    public static final Map<String, String> imageInfoMap;
    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("001.jpg", "ѧУ����");
        map.put("002.jpg", "����ͼ���");
        map.put("003.jpg", "����ͼ���");
        map.put("DSC00002.jpg", "ͼ���");
        map.put("DSC00003.jpg", "ͼ���");
        map.put("DSC00004.jpg", "ͼ���");
//        map.put("DSC00005.JPG", "ͼ���");
        map.put("DSC00007.jpg", "ͼ���");
        map.put("DSC00008.jpg", "ͼ���");
//        map.put("IMG_0326-2.jpg", "ͼ���");
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
