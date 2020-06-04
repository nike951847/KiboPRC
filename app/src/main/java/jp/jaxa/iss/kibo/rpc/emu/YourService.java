package jp.jaxa.iss.kibo.rpc.emu;

import android.graphics.Bitmap;
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
public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1() {
        api.judgeSendStart();
        Log.d("Timer","____");
        moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
        Log.d("Timer","____");
        waiting();
        Log.d("Timer","____");
        getQR(0);
        Log.d("Timer","____");
        waiting();
        Log.d("Timer","____");
        moveToWrapper(11,-6, 5.55, 0, -0.7071068, 0, 0.7071068);
        Log.d("Timer","____");
        getQR(1);
        Log.d("Timer","____");
        waiting();
        Log.d("Timer","____");
        moveToWrapper(11,-5.5, 4.33, 0, -0.7071068, 0, 0.7071068);
        Log.d("Timer","____");
        getQRD(2);
        moveToWrapper(10.30,-7.5, 4.7,0, 0, 1, 0);
        Log.d("Timer","____");
        waiting();
        getQR(3);
        Log.d("Timer","____");
        moveToWrapper(11.5, -8, 5,0, 0, 0, 1);
        Log.d("Timer","____");
        waiting();
        getQR(4);
        moveToWrapper(11, -7.7, 5.55 , 0, -0.7071068, 0, 0.7071068);
        waiting();
        getQR(5);
        moveToWrapper(11, -7.7, 5.55 , 0, -0.7071068, 0, 0.7071068);
        api.judgeSendFinishSimulation();
      /*

        String[] ypoz =xposition.split(",");
        double ypos = Double.parseDouble(xpoz[1]);
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
            Log.d("tagg", "con3");
            com.google.zxing.Result result = qrCodeReader.decode(binaryBitmap);
            Log.d("tagg", "con4");
            if(result.getNumBits()==0){
                Log.d("tagg", "con11null");
            }

            else if(result.getNumBits()!=0){
                Log.d("tagg", "con12some");
            }
            else {
                Log.d("tagg", "con13wt");
            }
            return result.getText();
        } catch (Exception e) {
            Log.e("emuu","confail");
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

                api.judgeSendDiscoveredQR(number, getQRString);
            }

        } else if (getQRString != null) {
            try {
                Log.d(tag, getQRString);
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

                api.judgeSendDiscoveredQR(number, getQRString);
            }

        } else if (getQRString != null) {
            try {
                Log.d(tag, getQRString);
                api.judgeSendDiscoveredQR(number, getQRString);
            } catch (Exception e) {
                Log.e("tagerror", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            }
        }
        return getQRString;
    }

}

