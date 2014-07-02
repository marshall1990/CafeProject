package com.marshall.cafeproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
public class JSONParser {
 
    static InputStream inputstream = null;
    static JSONObject jsonObj = null;
    static String json = "";
 
    public JSONParser() {}
 
    // Funkcja do parsowania JSONa poprzez ��dnia HTTP: obs�uga POST i GET.
    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

        // Wykrywanie ��dania HTTP
        try {
 
            // Sprawdza czy ��danie HTTP jest metod� POST
            if(method == "POST"){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                // wysy�a w pakiecie parametry
                if (params != null)
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputstream = httpEntity.getContent();
 
            }else if(method == "GET"){
                // ��danie HTTP jest metod� GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
 
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputstream = httpEntity.getContent();
            }           
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("brak neta", "3 + " + e.toString());
            
        } 
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputstream.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Exception", "Wyj�tek: " + e.toString());
        }
 
        // Parsowanie Stringa do JSONa
        // Po stworzeniu obiektu StringBuilder i zwr�ceniu z niego �a�cucha znak�w s� one parsowane do obiektu JSON
        try {
        	jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Wyj�tek: " + e.toString());
            Log.e("JSON Exception", "OBIEKT JSON: " + json.toString());
            e.printStackTrace();
        }
 
        return jsonObj;
    }
}