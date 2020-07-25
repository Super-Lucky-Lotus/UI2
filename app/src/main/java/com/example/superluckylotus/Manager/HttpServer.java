package com.example.superluckylotus.Manager;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpServer {

    private static final String TAG = "HttpServer";

    public static class SuperHttpUtil {
        private static Handler handler=new Handler();
        public static void get(final Map strMap, final String strUrl, final HttpCallBack callBack){
            Thread thread=new Thread(){
                @Override
                public void run() {
                    HttpURLConnection connection=null;
                    InputStream is=null;
                    try {
                        StringBuilder stringBuffer=new StringBuilder(strUrl);
                        stringBuffer.append("?");
                        for (Object key:strMap.keySet()) {
                            stringBuffer.append(key+"="+strMap.get(key)+"&");
                        }
                        stringBuffer.deleteCharAt(stringBuffer.length()-1);
                        URL url=new URL(stringBuffer.toString());
                        connection= (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(10*1000);
                        if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                            is=connection.getInputStream();
                            final String result=InputStreamToString(is);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        callBack.onSuccess(result);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }else{
                            throw new Exception("ResponseCode:"+connection.getResponseCode());
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(e);
                            }
                        });

                    }finally {
                        if (connection!=null)connection.disconnect();
                        if (is!=null) try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFinish();
                            }
                        });
                    }
                }
            };
            thread.start();

        }
        public static void post(final Map strMap, final String strUrl, final HttpCallBack callBack){
            Thread thread=new Thread(){
                @Override
                public void run() {
                    HttpURLConnection connection=null;
                    OutputStream os=null;
                    InputStream is=null;
                    try {
                        StringBuilder stringBuilder=new StringBuilder();
                        for (Object key:strMap.keySet()){
                            stringBuilder.append(key+"="+strMap.get(key)+"&");
                        }
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);
                        URL url=new URL(strUrl);
                        Log.v(TAG,strUrl);
                        connection= (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(10*1000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setUseCaches(false);
                        connection.setRequestProperty("Charset","utf-8");
                        connection.connect();
                        os=connection.getOutputStream();
                        os.write(stringBuilder.toString().getBytes());
                        os.flush();
                        if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                            is=connection.getInputStream();
                            final String result=InputStreamToString(is);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        callBack.onSuccess(result);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else{
                            throw new Exception("ResponseCode:"+connection.getResponseCode());
                        }
                    }catch (final Exception e){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(e);
                            }
                        });
                    }finally {
                        if (connection!=null)connection.disconnect();
                        try {
                            if (is!=null) is.close();
                            if(os!=null)os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFinish();
                            }
                        });
                    }
                }
            };
            thread.start();
        }
        public static void post2(final String urlStr, final Map<String, String> textMap, final Map<String, String> fileMap, final HttpCallBack callBack) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    String res = "";
                    HttpURLConnection conn = null;
                    OutputStream os = null;
                    InputStream is = null;
                    String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
                    try {
                        URL url = new URL(urlStr);
                        Log.v(TAG, url.toString());
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(10 * 1000);
                        conn.setReadTimeout(30000);
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                        conn.connect();
                        Log.v(TAG, "connectsuccess");
                        os = new DataOutputStream(conn.getOutputStream());

                        // text
                        if (textMap != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Object key : textMap.keySet()) {
                                stringBuilder.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                                stringBuilder.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n");
                                stringBuilder.append(textMap.get(key));
                            }
                            os.write(stringBuilder.toString().getBytes());
                            Log.v(TAG, stringBuilder.toString());
                        }

                        if (fileMap != null) {
                            Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry<String, String> entry = iter.next();
                                String inputName = (String) entry.getKey();
                                String inputValue = (String) entry.getValue();
                                Log.v(TAG, "inputName:" + inputName);
                                Log.v(TAG, "inputValue:" + inputValue);
                                if (inputValue == null) {
                                    continue;
                                }
                                File file = new File(inputValue);

                                String path = file.getAbsolutePath();
                                String filename = file.getName();
                                String contentType = FileType.getFileType(path).mimeType;//"video/mpeg4";//
                                Log.v(TAG, "type:" + contentType + "filename:" + filename + "path:" + path);

                                StringBuffer strBuf = new StringBuffer();
                                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                                strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                                strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                                Log.v(TAG, strBuf.toString());
                                os.write(strBuf.toString().getBytes());
                                Log.v(TAG, "filedatastart");
                                FileInputStream a = new FileInputStream(file);
                                Log.v(TAG, "filedatastart2");
                                DataInputStream in = new DataInputStream(a);
                                Log.v(TAG, "filedataend");
                                int bytes = 0;
                                byte[] bufferOut = new byte[1024];
                                while ((bytes = in.read(bufferOut)) != -1) {
                                    os.write(bufferOut, 0, bytes);
                                }
                            }
                        }
                        byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                        os.write(endData);
                        os.flush();
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            is = conn.getInputStream();
                            final String result = InputStreamToString(is);
                            Log.v(TAG, result);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        callBack.onSuccess(result);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            throw new Exception("ResponseCode:" + conn.getResponseCode());
                        }
                    } catch (final Exception e) {
                        Log.v(TAG, e.toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(e);
                            }
                        });
                    } finally {
                        if (conn != null) conn.disconnect();
                        try {
                            if (is != null) is.close();
                            if (os != null) os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFinish();
                            }
                        });
                    }
                }
            };
            thread.start();
        }


        public static String InputStreamToString(InputStream is) throws IOException {
            ByteArrayOutputStream os=new ByteArrayOutputStream();
            byte[] data=new byte[1024];
            int len=-1;
            while((len=is.read(data))!=-1){
                os.write(data,0,len);
            }
            os.flush();
            os.close();
            String result=new String(data,"UTF-8");
            return result;
        }


        public interface HttpCallBack{
            public void onSuccess(String result) throws JSONException;
            public void onError(Exception e);
            public void onFinish();
        }
    }
}
