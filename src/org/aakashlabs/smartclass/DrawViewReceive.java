package org.aakashlabs.smartclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class DrawViewReceive extends View{
	
	static boolean board_start=false;
	
    private Canvas  mCanvas;
    private Path    mPath,mPath1;
    static float x,y;
    private Paint       mPaint;   
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>(); 
    private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();
    private Map<Path, Integer> widthMap = new HashMap<Path, Integer>();
    private Bitmap im;
    public DrawViewReceive(Context context) 
    {
    	
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);      
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        mCanvas = new Canvas();
        mPath = new Path();

        im=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);


    }               
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {


            for (Path p : paths){
            	
            	 mPaint.setColor(colorsMap.get(p));
            	 mPaint.setStrokeWidth(widthMap.get(p));
            	 
                canvas.drawPath(p, mPaint);
                
            }
            mPaint.setColor(CommonUtilities.selectedcolour);
            mPaint.setStrokeWidth(CommonUtilities.widthvalue);
            canvas.drawPath(mPath, mPaint);
            
            
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        public void touch_start(float x, float y) {
        	
        	
        	 if((CommonUtilities.start_board!=2)&&(CommonUtilities.start_board!=0))
             {
             	 touch_up();
             }
    		Log.d("Draw", "starting");
            undonePaths.clear();
           
            CommonUtilities.start_board=1;
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            invalidate();
            
        }
        public void touch_move(float x, float y) {
        	
        	if((CommonUtilities.start_board!=1))
    		{
    					touch_start(x,y);
    			
            }
    		Log.d("Draw", "moving");
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        		Log.d("Draw", "moving passed tolerance");
        		
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                Log.d("Draw", "moving quadto done");
                mX = x; 
                mY = y;
                invalidate();
            }
        }
        public void touch_up() {
           // mPath.lineTo(mX, mY);
            // commit the path to our offscreen
        	CommonUtilities.start_board=2;
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            paths.add(mPath);
            colorsMap.put(mPath,CommonUtilities.selectedcolour);
            widthMap.put(mPath,CommonUtilities.widthvalue);
            mPath = new Path();
            
            
            invalidate();
        }

        public void onClickUndo () { 
            if (paths.size()>0) 
            { 
               undonePaths.add(paths.remove(paths.size()-1));
               invalidate();
             }
            else
            {
            	//Toast.makeText(con, "All undone", Toast.LENGTH_SHORT).show();
            }
              
        }

        public void onClickRedo (){
           if (undonePaths.size()>0) 
           { 
               paths.add(undonePaths.remove(undonePaths.size()-1)); 
               invalidate();
           } 
           else 
           {

           }
             //toast the user 
        }
        
       

   /* @Override
    public boolean onTouch(View arg0, MotionEvent event) {
         x = event.getX();
         y = event.getY();
         board_start=true;
     	new Thread(new BoardClient()).start();
          
          switch (event.getAction()) {
              case MotionEvent.ACTION_DOWN:
                  touch_start(x, y);
                  invalidate();
                  break;
              case MotionEvent.ACTION_MOVE:
                  touch_move(x, y);
                  invalidate();
                  break;
              case MotionEvent.ACTION_UP:
                  touch_up();
                  invalidate();
                  break;
          }
          return true;
    }*/
   
}