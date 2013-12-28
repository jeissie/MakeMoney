package com.jesse.mxapk;

import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;
import android.graphics.Canvas;  
import android.graphics.Color;   
import android.graphics.PorterDuff;  
import android.util.AttributeSet;  
import android.view.SurfaceHolder;  
import android.view.SurfaceView;  
import android.view.SurfaceHolder.Callback;  
  
public class MySurfaceView3 extends SurfaceView implements Callback, Runnable {  
    private Context mContext;  
    private SurfaceHolder surfaceHolder;  
    private static boolean  flag = false;// �̱߳�ʶ  
    private Bitmap bitmap_bg;// ����ͼ  
  
    private float mSurfaceWindth, mSurfaceHeight;// ��Ļ���  
  
    private int mBitposX;// ͼƬ�ĺ���λ��  
    private int mBitposY;// ͼƬ������λ��
    private Canvas mCanvas;  
  
    private Thread thread;  
  
    // �����ƶ�״̬  
    private enum State {  
    	LEFT_DOWN, RINGHT ,LEFT_UP 
    }  
  
    // Ĭ��Ϊ����  
    private State state = State.LEFT_DOWN;  
  
    private final int BITMAP_STEP = 1;// ���������ƶ�����.  
  
    public MySurfaceView3(Context context, AttributeSet attrs) {  
        super(context, attrs);  
          
        this.mContext = context;  
        onStart();
    }  
  
    public void onStart(){
    	setFlag(true);
    	setFocusable(true);  
        setFocusableInTouchMode(true);  
        surfaceHolder = getHolder();  
        surfaceHolder.addCallback(this);  
    }
    /*** 
     * ���л���. 
     */  
    protected void onDraw() {  
        drawBG();  
        updateBG();  
    }  
  
    /*** 
     * ���±���. 
     */  
    public void updateBG() {  
        /** ͼƬ����Ч�� **/  
        switch (state) {  
        case LEFT_DOWN:  
            mBitposX -= BITMAP_STEP;// ��������  
            mBitposY -= BITMAP_STEP;// ��������
            break;  
        case RINGHT:  
            mBitposX += BITMAP_STEP;// ��������  
            break;  
        case LEFT_UP:
        	 mBitposX -= BITMAP_STEP;// ��������
        	 mBitposY += BITMAP_STEP;// ��������
        default:  
            break;  
        }  
        /********�߽��ж�**********/
        if (mBitposX <= -mSurfaceWindth  || mBitposY <= -mSurfaceWindth ) {  
            state = State.RINGHT;  
        }  
        if (mBitposX >= 0 ) {  
            state = State.LEFT_UP;  
        } 
        if (mBitposX <= -mSurfaceWindth  || mBitposY >= 0) {  
            state = State.RINGHT;  
        }
        if (mBitposX >= 0 && mBitposY >= 0) {  
        	state = State.LEFT_DOWN;
        }
    }  
  
    /*** 
     * ���Ʊ��� 
     */  
    public void drawBG() {  
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);// ����Ļ.  
        mCanvas.drawBitmap(bitmap_bg, mBitposX, mBitposY, null);// ���Ƶ�ǰ��Ļ����  
    }  
  
    @Override  
    public void run() {  
        while (isFlag()) {  
            synchronized (surfaceHolder) {  
                mCanvas = surfaceHolder.lockCanvas();  
                onDraw();  
                surfaceHolder.unlockCanvasAndPost(mCanvas);  
                try {  
                    Thread.sleep(100);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
  
    }  
  
    @Override  
    public void surfaceCreated(SurfaceHolder holder) {  
    	mSurfaceWindth = getWidth();  
        mSurfaceHeight = getHeight(); 
    	int mWindth = (int) (mSurfaceWindth * 2);  
        /*** 
         * ��ͼƬ���ŵ���Ļ��3/2��. 
         */  
        bitmap_bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.surface_view_3);  
        
        thread = new Thread(this);  
        thread.start();  
  
    }  
  
    @Override  
    public void surfaceChanged(SurfaceHolder holder, int format, int width,  
            int height) {  
  
    }  
  
    @Override  
    public void surfaceDestroyed(SurfaceHolder holder) {  
        setFlag(false);  
    }

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		MySurfaceView3.flag = flag;
	}
  
}  

