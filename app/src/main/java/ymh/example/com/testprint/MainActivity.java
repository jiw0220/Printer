package ymh.example.com.testprint;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gprinter.command.EscCommand;
import com.gprinter.command.TscCommand;
import com.gprinter.service.GpPrintService;
import com.gprinter.aidl.GpService;

import org.apache.commons.lang.ArrayUtils;

import java.util.Vector;

public class MainActivity extends Activity {

    private GpService mGpService = null;
    private PrinterServiceConnection conn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("hahahah", "onCreate");
        startService();
        connection();
    }

    private void startService() {
        Intent i = new Intent(this, GpPrintService.class);
        startService(i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
//            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, "绑定成功！！！" + mGpService, Toast.LENGTH_SHORT).show();
        }
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, com.gprinter.service.GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    public void click(View view) {
        try {
            mGpService.openPort(1, 4, "88:C2:55:9E:92:1A", 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printclick(View view) {

        try {
            printLable();
//            mGpService.printeTestPage(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printLable() {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 3);
//        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
//        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);//设置为倍高倍宽
//        esc.addText("Sample\n"); // 打印文字
//        esc.addPrintAndLineFeed();
///*打印文字*/
//        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);//取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
//        esc.addText("Print text\n"); // 打印文字
        esc.addText("大麦简餐\n"); // 打印文字
        esc.addText("设备：第一分组->国粹精华\n"); // 打印文字
        esc.addText("时间：16-03-02 12:39:50\n"); // 打印文字
        esc.addText("单号：000020-160302-00253\n"); // 打印文字
        esc.addPrintAndLineFeed();
        esc.addText("卡号：2967364513\n"); // 打印文字
        esc.addText("编号：002083\n"); // 打印文字
        esc.addText("姓名：王游\n"); // 打印文字
        esc.addText("本卡折扣：90%\n"); // 打印文字
        esc.addText("消费：16.2元\n"); // 打印文字
        esc.addText("余额：245.9元\n"); // 打印文字
        esc.addPrintAndLineFeed();
        esc.addText("欢迎光临高新三路大麦简餐\n"); // 打印文字
        esc.addText("投诉建议电话：029-62962050\n"); // 打印文字
        esc.addText("欢迎下次光临\n"); // 打印文字

///*打印图片*/
//        esc.addText("Print bitmap!\n"); // 打印文字
//        Bitmap b = BitmapFactory.decodeResource(getResources(),
//                R.drawable.bb);
//        esc.addRastBitImage(b, b.getWidth(), 0); //打印图片
///*打印一维条码*/
//        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
//        esc.addText("Print code128\n"); // 打印文字
//        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.ABOVE);//设置条码可识
//        esc.addSetBarcodeHeight((byte) 120); //设置条码高度为 60 点
////        esc.addSetFontForHRICharacter(EscCommand.FONT.FONTB);
//        esc.addSetBarcodeWidth((byte)3 );
////        esc.addUPCA("12345678910");
////        addITF
//        //addCODE93最多可以打10位数字
//        esc.addCODE93("9876543210"); //打印 Code128
//        esc.addPrintAndLineFeed();


///*QRCode 命令打印
//此命令只在支持 QRCode 命令打印的机型才能使用。
//在不支持二维码指令打印的机型上，则需要发送二维条码图片
////*/
//        esc.addText("银泰APK\n"); // 打印文字
        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); //设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 10);//设置 qrcode 模块大小
        esc.addStoreQRCodeData("http://weixin.qq.com/r/_0jUzDnECmR1rX2v9x3p");//设置 qrcode 内容
        esc.addPrintQRCode();//打印 QRCode
        esc.addPrintAndLineFeed();
///*打印文字*/
//        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印左对齐
        esc.addText("扫描二维码关注微信公众号\n"); // 打印结束
//        esc.addPrintAndFeedLines((byte) 8);
        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(1, str);
//            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
//            if (r != GpCom.ERROR_CODE.SUCCESS) {
//                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r),
//                        Toast.LENGTH_SHORT).show();
//            }
        } catch (RemoteException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
