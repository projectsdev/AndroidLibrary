package domain.project1;

import android.content.Context;
import android.content.SharedPreferences;

public class getUrl {
    String url;
    SharedPreferences preferences;
    Context context;
    String setUrl(Context context , String dir){
        preferences = context.getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
        url = preferences.getString("currentLocalhost","null");
        if(url.equals("null"))
        url = "http://192.168.0.10:10010/"+dir;
//        url = "http://192.168.43.11:10010/"+dir;
        else
         url = "http://"+url+":10010/"+dir;
        return url;
    }
}
