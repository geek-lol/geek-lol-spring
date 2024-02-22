package com.nat.geeklolspring.utils.files;

import org.springframework.http.MediaType;

public class Videos {
    public static MediaType extractFileExtension(String filePath){

        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        switch (ext.toUpperCase()) {
            case "MP4":
                return MediaType.valueOf("video/mp4");
            case "MKV":
                return MediaType.valueOf("video/x-matroska");
            case "AVI":
                return MediaType.valueOf("video/x-msvideo");
            case "WEBM":
                return MediaType.valueOf("video/webm");

            default:
                return null;
        }

    }

}
