package com.example.superluckylotus;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
//        public static void post2(final String urlStr, final Map<String, String> textMap, final Map<String, String> fileMap, final HttpCallBack callBack){
//            Thread thread=new Thread(){
//                @Override
//                public void run() {
//                    String res = "";
//                    HttpURLConnection conn = null;
//                    OutputStream os=null;
//                    InputStream is=null;
//                    String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
//                    try {
//                        URL url = new URL(urlStr);
//                        conn = (HttpURLConnection) url.openConnection();
//                        conn.setConnectTimeout(5000);
//                        conn.setReadTimeout(30000);
//                        conn.setDoOutput(true);
//                        conn.setDoInput(true);
//                        conn.setUseCaches(false);
//                        conn.setRequestMethod("POST");
//                        conn.setRequestProperty("Connection", "Keep-Alive");
//                        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//
//                        os = new DataOutputStream(conn.getOutputStream());
//                        // text
//                        if (textMap != null) {
//                            StringBuffer strBuf = new StringBuffer();
//                            Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
//                            while (iter.hasNext()) {
//                                Map.Entry<String, String> entry = iter.next();
//                                String inputName = (String) entry.getKey();
//                                String inputValue = (String) entry.getValue();
//                                if (inputValue == null) {
//                                    continue;
//                                }
//                                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//                                strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
//                                strBuf.append(inputValue);
//                            }
//                            os.write(strBuf.toString().getBytes());
//                        }
//
//                        // file
//                        if (fileMap != null) {
//                            Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
//                            while (iter.hasNext()) {
//                                Map.Entry<String, String> entry = iter.next();
//                                String inputName = (String) entry.getKey();
//                                String inputValue = (String) entry.getValue();
//                                if (inputValue == null) {
//                                    continue;
//                                }
//                                File file = new File(inputValue);
//                                String filename = file.getName();
//                                String contentType =getFileType(filename);
//
//                                StringBuffer strBuf = new StringBuffer();
//                                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//                                strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
//                                strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
//
//                                os.write(strBuf.toString().getBytes());
//
//                                DataInputStream in = new DataInputStream(new FileInputStream(file));
//                                int bytes = 0;
//                                byte[] bufferOut = new byte[1024];
//                                while ((bytes = in.read(bufferOut)) != -1) {
//                                    os.write(bufferOut, 0, bytes);
//                                }
//                            }
//                        }
//
//                        byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
//                        os.write(endData);
//                        os.flush();
//                        if (conn.getResponseCode()==HttpURLConnection.HTTP_OK){
//                            is=conn.getInputStream();
//                            final String result=InputStreamToString(is);
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    callBack.onSuccess(result);
//                                }
//                            });
//                        }else{
//                            throw new Exception("ResponseCode:"+conn.getResponseCode());
//                        }
//                    }catch (final Exception e){
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onError(e);
//                            }
//                        });
//                    }finally {
//                        if (conn!=null)conn.disconnect();
//                        try {
//                            if (is!=null) is.close();
//                            if(os!=null)os.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onFinish();
//                            }
//                        });
//                    }
//                }
//            };
//            thread.start();
//        }

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

//        public static HashMap<String, String> mFileTypes = new HashMap<String, String>() {{
//            //images
//            put("FFD8FF","jpg");
//            put("89504E47","png");
////            mFileTypes.put("47494638","gif");
////            mFileTypes.put("49492A00","tif");
////            mFileTypes.put("424D","bmp");
////            //
////            mFileTypes.put("41433130","dwg"); //CAD
////            mFileTypes.put("38425053","psd");
////            mFileTypes.put("7B5C727466","rtf"); //日记本
////            mFileTypes.put("3C3F786D6C","xml");
////            mFileTypes.put("68746D6C3E","html");
////            mFileTypes.put("44656C69766572792D646174653A","eml"); //邮件
////            mFileTypes.put("D0CF11E0","doc");
////            mFileTypes.put("5374616E64617264204A","mdb");
////            mFileTypes.put("252150532D41646F6265","ps");
////            mFileTypes.put("255044462D312E","pdf");
////            mFileTypes.put("504B0304","zip");
////            mFileTypes.put("52617221","rar");
////            mFileTypes.put("57415645","wav");
////            mFileTypes.put("41564920","avi");
////            mFileTypes.put("2E524D46","rm");
////            mFileTypes.put("000001BA","mpg");
////            mFileTypes.put("000001B3","mpg");
////            mFileTypes.put("6D6F6F76","mov");
////            mFileTypes.put("3026B2758E66CF11","asf");
////            mFileTypes.put("4D546864","mid");
////            mFileTypes.put("1F8B08","gz");
////            mFileTypes.put("","");
////            mFileTypes.put("","");
//        }};
//
//        public static String getFileType(String filePath) {
//            return mFileTypes.get(getFileHeader(filePath));
//        }
// //获取文件头信息
//         public static String getFileHeader(String filePath) {
//            FileInputStream is = null;
//            String value = null;
//            try {
//                is = new FileInputStream(filePath);
//                byte[] b = new byte[3];
//                is.read(b, 0, b.length);
//                value = bytesToHexString(b);
//            } catch (Exception e) {
//            } finally {
//                if(null != is) {
//                    try {
//                        is.close();
//                    } catch (IOException e) {}
//                }
//            }
//            return value;
//        }
//        private static String bytesToHexString(byte[] src){
//            StringBuilder builder = new StringBuilder();
//            if (src == null || src.length <= 0) {
//                return null; }String hv;
//            for (int i = 0; i < src.length; i++) {
//
// hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
//
// if (hv.length() < 2) {
//
//builder.append(0);
//
// }
//
// builder.append(hv);
//
//}
//            return builder.toString();
//        }

        public interface HttpCallBack{
            public void onSuccess(String result) throws JSONException;
            public void onError(Exception e);
            public void onFinish();
        }
    }
}
