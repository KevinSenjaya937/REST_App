package au.edu.curtin.userinfo;

import android.app.Activity;
import android.net.Uri;

import java.net.HttpURLConnection;
import java.util.concurrent.Callable;

public class SearchTask implements Callable<String> {

    private String searchkey;
    private String baseUrl;
    private RemoteUtilities remoteUtilities;
    private Activity uiActivity;

    public SearchTask(Activity uiActivity) {
        this.searchkey = null;
        baseUrl ="https://jsonplaceholder.typicode.com/users";
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.uiActivity = uiActivity;
    }

    @Override
    public String call() throws Exception {
        String response=null;
//        String endpoint = getSearchEndpoint();
        HttpURLConnection connection = remoteUtilities.openConnection(baseUrl);
        if(connection!=null){
            if(remoteUtilities.isConnectionOkay(connection)==true){
                response = remoteUtilities.getResponseString(connection);
                connection.disconnect();
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e){

                }
            }
        }
        return response;
    }

    private String getSearchEndpoint(){
        String data = null;
        Uri.Builder url = Uri.parse(this.baseUrl).buildUpon();
        url.appendQueryParameter("method","get");
        String urlString = url.build().toString();
        return urlString;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }
}
