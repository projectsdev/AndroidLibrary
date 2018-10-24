package domain.project1;

public class getUrl {
    String url;
    String setUrl(String dir){
        url = "http://192.168.0.10:10010/"+dir;
        return url;
    }
}
