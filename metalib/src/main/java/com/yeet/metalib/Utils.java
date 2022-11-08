package com.yeet.metalib;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@SuppressWarnings("all") // UGH ANNOYING WHY CARES IT JUST WORKS
public class Utils {

    static final String METADATA_URI = "http://meta.yeetos-metadata.ml";
    static final short METADATA_PORT = 4616;

//    static String test = "{\"rom\":{\"latest\":{\"version\":\"0.0.1_a\",\"packageUri\":\"http://github.com/wdnmd/nmsl\",\"changeLogUri\":\"http://github.com/nmsl/cnmb\",\"lastUpgradableVersion\":\"0.0.0_a\",\"nonApplicableMachineType\":[],\"type\":\"system\",\"md5\":\"wdddddnmmmmmdd\"},\"history\":[{\"version\":\"0.0.1_a\",\"packageUri\":\"http://github.com/wdnmd/nmsl\",\"changeLogUri\":\"http://github.com/nmsl/cnmb\",\"lastUpgradableVersion\":\"0.0.0_a\",\"nonApplicableMachineType\":[],\"type\":\"boot\",\"md5\":\"wdddddnmmmmmdd\"}],\"bannedVersions\":[{\"version\":\"0.0.1_a\",\"reason\":\"banned reason\"}]},\"app\":[{\"name\":\"com.yeet.setupwizard\",\"data\":{\"wow\":\"test\"}},{\"name\":\"com.yeet.metalibtest\",\"data\":{\"wow\":\"tes222t\"}},{\"name\":\"com.yeet.setupwssizard\",\"data\":{\"wow\":\"t333est\"}}]}{$:b6bc1d624d14275108cda27af454ad77}";
    private static SplitJson splitJson(String json) throws IllegalArgumentException {
        String[] _split = json.split("\\{\\$:");
        _split[1] = _split[1].substring(0, 32);
        if (_split.length != 2) {
            throw new IllegalArgumentException("Array length longer than expected 2: " + _split.length);
        }
        return new SplitJson(_split[0], _split[1]);
    }

    private static boolean verifyJson(String json, String expectedMd5) {
        String _md5_gen;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(json.getBytes());
            _md5_gen = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException ignored) {
            //impossible
            return false;
        }
        return _md5_gen.equals(expectedMd5);
    }

    public static MetadataStructure readMetadata() throws Exception{
        SplitJson sj = splitJson(getJson());
        if (verifyJson(sj.json, sj.md5)) {
            return new Gson().fromJson(sj.json, MetadataStructure.class);
        } else {
            throw new IllegalArgumentException("JSON does not match expected md5");
        }
    }

    public static <T> T readAppData(Context c, T struct) throws Exception{
        SplitJson sj = splitJson(getJson());
        if (verifyJson(sj.json, sj.md5)) {
            Gson gson = new Gson();
            AppDataArray ada = gson.fromJson(sj.json, AppDataArray.class);
            String appPackageName = c.getPackageName();
            for (AppDataStructure a: ada.app){
                if(Objects.equals(a.name, appPackageName)){
                    String o = gson.toJson(a.data);
                    return (T) gson.fromJson(o, struct.getClass());
                }
            }
        } else {
            throw new IllegalArgumentException("JSON does not match expected md5");
        }
        return null;
    }

    public static String getJson() throws ProtocolException, IOException, UnknownHostException {
            URL url = new URL(METADATA_URI+":"+METADATA_PORT);
            for(int c = 0; c != 3; c++){
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                if(conn.getResponseCode() == 200){
                    String _r = getStringByStream(conn.getInputStream());
                    if (_r == null){
                        throw new UnknownHostException("Received Null!");
                    }
                    return _r;
                }
            }
            throw new UnknownHostException("Cannot reach to the metadata server "+url.toString());
    }

    private static String getStringByStream(InputStream inputStream){
        Reader reader;
        try {
            reader=new InputStreamReader(inputStream,"UTF-8");
            char[] rawBuffer=new char[512];
            StringBuffer buffer=new StringBuffer();
            int length;
            while ((length=reader.read(rawBuffer))!=-1){
                buffer.append(rawBuffer,0,length);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

class AppDataArray {
    AppDataStructure[] app;
}

class SplitJson {
    String json;
    String md5;

    public SplitJson(String json, String md5) {
        this.json = json;
        this.md5 = md5;
    }
}