package com.network.moeidbannerlibrary.banner;

public class Utils {

    public static final String ip = "http://thestockbazaar.com/admin/e-commerce/village_mart";
    public static final String IMAGE_URL = ip + "/images/";

    public static String getResizedImage(String path, boolean isResized) {
        if (isResized) {
            return IMAGE_URL+"small/"+path.substring(path.lastIndexOf("/")+1);
        }
        return path;
    }
}
