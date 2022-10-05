package au.edu.curtin.userinfo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BackGroundTaskHandler implements Runnable{

    Activity uiActivity;
    String searchKey;
    ProgressBar progressBar;
    ImageView imageView;
    public BackGroundTaskHandler(Activity uiActivity) {
        this.uiActivity = uiActivity;
//        this.searchKey = searchValue;
        this.progressBar = progressBar;
        this.imageView = imageView;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        SearchTask searchTask = new SearchTask(uiActivity);
        searchTask.setSearchkey(searchKey);
        Future<String> searchResponsePlaceholder = executorService.submit(searchTask);
        String searchResult = waitingForSearch(searchResponsePlaceholder);

        if(searchResult!=null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<User>>() {}.getType();
            List<User> userList = gson.fromJson(searchResult, type);

            for (User user : userList) {
                Log.d("KEVIN", user.getUsername());
            }
        }
        else{
            showError(4,"Search");
        }
        executorService.shutdown();
    }

    public String waitingForSearch(Future<String> searchResponsePlaceholder){
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        showToast("Search Starts");
        String searchResponseData =null;
        try {
            searchResponseData = searchResponsePlaceholder.get(6000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
            showError(1, "Search");
        } catch (InterruptedException e) {
            e.printStackTrace();
            showError(2, "Search");
        } catch (TimeoutException e) {
            e.printStackTrace();
            showError(3, "Search");
        }
        showToast("Search Ends");
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        return  searchResponseData;
    }

    public Bitmap waitingForImage(Future<Bitmap> imageResponsePlaceholder){
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        showToast("Image Retrieval Starts");
        Bitmap imageResponseData =null;
        try {
            imageResponseData = imageResponsePlaceholder.get(6000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
            showError(1, "Image Retrieval");
        } catch (InterruptedException e) {
            e.printStackTrace();
            showError(2, "Image Retrieval");
        } catch (TimeoutException e) {
            e.printStackTrace();
            showError(3, "Image Retrieval");
        }
        showToast("Image Retrieval Ends");
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        return  imageResponseData;
    }

    public void showError(int code, String taskName){
        if(code ==1){
            showToast(taskName+ " Task Execution Exception");
        }
        else if(code ==2){
            showToast(taskName+" Task Interrupted Exception");
        }
        else if(code ==3){
            showToast(taskName+" Task Timeout Exception");
        }
        else{
            showToast(taskName+" Task could not be performed. Restart!!");
        }
    }

    public void showToast(String text){
        uiActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(uiActivity,text,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
