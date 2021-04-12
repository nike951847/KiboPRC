package jp.jaxa.iss.kibo.rpc.emu;

import android.graphics.Bitmap;
import android.text.LoginFilter;
import android.util.Log;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;

import org.opencv.android.OpenCVLoader;

import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.*;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.common.HybridBinarizer;
import java.lang.String;
import java.lang.Math;


public class YourService extends KiboRpcService {
    static int finishQR = 0;
    static double xpoz,ypoz,zpoz,xqua,yqua,zqua,wqua;
    @Override
    protected void runPlan1() {
        api.judgeSendStart();
        Log.d("Timer","____moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);start");
        moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
        Log.d("Timer","____moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);finish");
        waiting();

        Log.d("Timer","____getQRstart");
        Thread t = new Thread(new Runnable() {
            public void run() {
             getQR(0);
             finishQR ++;
             // your code here ...
            }
        });

        t.start();
       // getQR(0);
        Log.d("Timer","____getQRfinish");
        waiting();
        Log.d("Timer","____");
        moveToWrapper(11,-6, 5.52, 0, -0.7071068, 0, 0.7071068);
        Log.d("Timer","____");
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                getQR(1);
                finishQR ++;// your code here ...
            }
        });

        t1.start();
        Log.d("Timer","____");
        waiting();
        Log.d("Timer","____");
        moveToWrapper(11,-5.5, 4.35, 0, 0.7071068, 0, 0.7071068);
        Log.d("Timer","____");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                getQR(2);   // your code here ...
                finishQR ++;
            }
        });

        t2.start();
        moveToWrapper(10.5,-5.5, 4.5, 0, 0.7071068, 0, 0.7071068);
        moveToWrapper(10.3,-7, 4.6, 0, 0.7071068, 0, 0.7071068);
        moveToWrapper(11.3,-7, 4.6, 0, 0.7071068, 0, 0.7071068);
        moveToWrapper(11.3,-7.5, 4.6, 0, 0.7071068, 0, 0.7071068);
        moveToWrapper(10.30,-7.5, 4.7,0, 0, 1, 0);

        Log.d("Timer","____");
        waiting();
        Thread t3 = new Thread(new Runnable() {
            public void run() {
                getQR(3);   // your code here ...
                finishQR ++;

                }
        });

        t3.start();
        Log.d("Timer","____");

        moveToWrapper(11.5, -8, 5,0, 0, 0, 1);
        Log.d("Timer","____");
        waiting();
        getQR(4);
        Thread t4 = new Thread(new Runnable() {
            public void run() {
                getQR(4);   // your code here ...
                finishQR++;
            }
        });

        t4.start();
        moveToWrapper(11, -7.7, 5.4 , 0, -0.7071068, 0, 0.7071068);
        waiting();
        Thread t5 = new Thread(new Runnable() {
            public void run() {
                getQR(5);   // your code here ...
                finishQR++;
            }
        });

        t5.start();





        while (finishQR<6){
        }
        Log.d("finalqr:","x"+xpoz+"y"+ypoz+"z"+zpoz+"x"+xqua+"y"+yqua+"z"+zqua+"w"+getquaw());
        moveToWrapper(xpoz, ypoz, zpoz, xqua, yqua, zqua, getquaw());
        Log.d("moveto", "finalQR");


        Thread t6 = new Thread(new Runnable() {
            public void run() {
                getQR(6);   // your code here ...
                finishQR++;
            }
        });

        t6.start();

        api.judgeSendFinishSimulation();
        /*


*/


    }

    @Override
    protected void runPlan2() {
        // write here your plan 2
    }

    @Override
    protected void runPlan3() {
        // write here your plan 3
    }
    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w) {
        final int LOOP_MAX =3;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float) qua_x, (float) qua_y,
                (float) qua_z, (float) qua_w);


        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while (!result.hasSucceeded() || loopCounter < LOOP_MAX) {
            ++loopCounter;
            try{
                Thread.sleep(300);

            }catch (Exception e){
                Log.d("TAG" ,"moveToWrapper: Something");
            }

            result = api.moveTo(point, quaternion, true);
            waiting();
        }
    }
    private void waiting(){
        try{
            Thread.sleep(200);
        }catch (Exception e){

        }
    }
    public static String convert(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(
                    width, height, pixels);
            Log.d("tagg", "con1");
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    rgbLuminanceSource));
            Log.d("tagg", "con2");
            QRCodeReader qrCodeReader = new QRCodeReader();
            com.google.zxing.Result result = qrCodeReader.decode(binaryBitmap);
            if(result.getNumBits()==0){
            }

            else if(result.getNumBits()!=0){
            }
            else {
            }
            return result.getText();
        } catch (Exception e) {
            return null;

        }
    }
    private String getQR(int number) {
        String tag = "getQR:";
        Log.d("getQR:", number + "part");
        waiting();
        String getQRString = convert(api.getBitmapNavCam());
        if (getQRString == null) {
            Log.d(tag, "nope");
            getQRString = convert(api.getBitmapNavCam());
            if (getQRString != null) {
                sort(number, getQRString);
                api.judgeSendDiscoveredQR(number, getQRString);
            }

        } else if (getQRString != null) {
            try {
                Log.d(tag, getQRString);
                sort(number, getQRString);
                api.judgeSendDiscoveredQR(number, getQRString);
            } catch (Exception e) {
                Log.e("tagerror", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            }
        }
        return getQRString;
    }
    private String getQRD(int number) {
        String tag = "getQR:";
        Log.d("getQR:", number + "part");
        waiting();
        String getQRString = convert(api.getBitmapNavCam());
        if (getQRString == null) {
            Log.d(tag, "nope");
            getQRString = convert(api.getBitmapDockCam());
            if (getQRString != null) {
                sort(number, getQRString);
                api.judgeSendDiscoveredQR(number, getQRString);
            }

        } else if (getQRString != null) {
            try {
                Log.d(tag, getQRString);
                sort(number, getQRString);
                api.judgeSendDiscoveredQR(number, getQRString);
            } catch (Exception e) {
                Log.e("tagerror", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            }
        }
        return getQRString;
    }
    private double getquaw(){
        wqua = Math.pow(1-xqua*xqua-yqua*yqua-zqua*zqua,0.5);
        return wqua;
    }
    private void sort(int number,String qrcode){

        String[] split =qrcode.split(",");
        Log.d("qrcodesort", split[0]);
        Log.d("qrcodesort", split[1]);
        double sp = Double.parseDouble(split[1]);
        if(number==0){
            xpoz=sp;
            Log.d("qrcodesort", split[1]);
        }
        else if (number==1){
            ypoz=sp;
            Log.d("qrcodesort", split[1]);
        }
        else if (number==2){
            zpoz=sp;
            Log.d("qrcodesort", split[1]);
        }
        else if (number==3){
            xqua=sp;
            Log.d("qrcodesort", split[1]);
        }
        else if (number==4){
            yqua=sp;
            Log.d("qrcodesort", split[1]);
        }
        else if (number==5){
            zqua=sp;
            Log.d("qrcodesort", split[1]);
        }


    }


}

