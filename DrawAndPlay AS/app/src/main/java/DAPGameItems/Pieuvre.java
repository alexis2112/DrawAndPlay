package DAPGameItems;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.drawandplay.R;

public class Pieuvre extends GameItem{
	
	private float xRight;
	private float xLeft;
	private float[] finalMatrix = new float[16];
	private int spriteFrameXHandler;
	private float spriteFrameX=0;
	private boolean toOn=false;
	private long previousTime;
	private int leftRight=1;
	private float jumpHeight=0;
	private boolean goingRight=true;
	
	public Pieuvre(float posX,float posY,float xLeft,float xRight,Context context){
		super(posX,posY);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setWidth(0.5f);
		this.setLength(1.0f);
		this.setContext(context);
		this.xRight=xRight;
		this.xLeft=xLeft;
		float[] trans={0.0f,0.0f};
		this.setTranslation(trans);}

	@Override
	public void init() {
		
		this.setBuffers(new int[3]);
		ByteBuffer bb;
		FloatBuffer fb;
		GLES20.glGenBuffers(3, this.getBuffers(),0);
		
		float[] vertices = {this.getPosX(),this.getPosY(),0.0f,this.getPosX()+1.0f,this.getPosY(),0.0f,this.getPosX()+1.0f,this.getPosY()+0.5f,0.0f,this.getPosX(),this.getPosY()+0.5f,0.0f};
		bb = ByteBuffer.allocateDirect(vertices.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(vertices);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
	
		
		float[] textureCoords = {0.33f,1.0f,
				0.0f,1.0f,
				0.0f,0.0f,
				0.33f,0.0f,
				};
		bb = ByteBuffer.allocateDirect(textureCoords.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoords);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		

		float[] textureCoords2 = {0.0f,1.0f,
				0.33f,1.0f,
				0.33f,0.0f,
				0.0f,0.0f,
				};
		bb = ByteBuffer.allocateDirect(textureCoords.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoords2);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[2]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		fb.clear();
		bb.clear();
		
		this.setTransformationMatrixHandler(GLES20.glGetUniformLocation(this.getProgram(), "uMVPMatrix"));
		this.setVertexHandler(GLES20.glGetAttribLocation(this.getProgram(), "a_Position"));
		this.setTextureCoordHandler(GLES20.glGetAttribLocation(this.getProgram(), "a_TexCoordinate"));
		this.setTextureHandler(GLES20.glGetUniformLocation(this.getProgram(), "u_Texture"));
		this.spriteFrameXHandler =GLES20.glGetUniformLocation(this.getProgram(), "spriteFrameX");
		
		Matrix.setIdentityM(this.getTransformationMatrix(),0);
		
		
		
		
	}

	@Override
	public void draw() {
		
		long time = SystemClock.currentThreadTimeMillis();
		long interval = Math.abs(time - previousTime);
		if(interval>=15){
		this.setSpriteFrame();
		previousTime=time;
		}
		
		GLES20.glUseProgram(this.getProgram());
		
		 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture());
	     GLES20.glUniform1i(this.getTextureHandler(),0);
	     GLES20.glUniform1f(this.spriteFrameXHandler, this.spriteFrameX);
			
	     
	     GLES20.glEnableVertexAttribArray(this.getVertexHandler());
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[0]);
	     GLES20.glVertexAttribPointer(this.getVertexHandler(),3, GLES20.GL_FLOAT, false, 0, 0);
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
		
	     GLES20.glEnableVertexAttribArray(this.getTextureCoordHandler());
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[leftRight]);
	     GLES20.glVertexAttribPointer(this.getTextureCoordHandler(),2, GLES20.GL_FLOAT, false, 0, 0);
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
	     
	     Matrix.multiplyMM(finalMatrix, 0, this.getRenderer().getMVPMatrix(), 0, this.getTransformationMatrix(), 0);
	     Matrix.translateM(finalMatrix, 0, this.getTranslation()[0], this.getTranslation()[1], 0);
	     
	     GLES20.glUniformMatrix4fv(this.getTransformationMatrixHandler(), 1, false,finalMatrix, 0);
	  
			
			
	     GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
	    
	     GLES20.glDisableVertexAttribArray(this.getTextureCoordHandler());
	     GLES20.glDisableVertexAttribArray(this.getVertexHandler());
	     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
		
	     
	     
	
		
		
	}

	@Override
	public void loadTexture() {
		int[] texture = new int[1];

	    GLES20.glGenTextures(1, texture, 0);

	    BitmapFactory.Options bo = new BitmapFactory.Options();
	    bo.inScaled = false;
	    Bitmap tex = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.pieuvresprite, bo);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]); 

	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

	    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, tex, 0);
	    tex.recycle();


	    this.setTexture(texture[0]);
	}

	
	@Override
	public void updateCoordinates(){
		if(!isDead()){
		float x= this.getPosX();
		if(this.goingRight){
			
		if(x<=xRight){this.getTranslation()[0]=this.getTranslation()[0]+0.01f;
		this.setPosX(this.getPosX()+0.01f);
		}else{this.goingRight=false;
		this.leftRight=2;}
		}
		if(!this.goingRight){
			
		if(x>=xLeft){this.getTranslation()[0]=this.getTranslation()[0]-0.01f;
		this.setPosX(this.getPosX()-0.01f);
		}else{this.goingRight=true;
		this.leftRight=1;}}
		}else{
			if(this.jumpHeight<=0.5f){
				this.getTranslation()[1]=this.getTranslation()[1]+0.03f;
				this.jumpHeight=this.jumpHeight+0.03f;
			}else{
				this.fall();}
			
			
		}
		}
	
	public void fall(){
		
			getTranslation()[1]=getTranslation()[1]-0.03f;
			this.setPosY(this.getPosY()-0.03f);
		
		
	}
	
@Override
	public boolean isGoingRight() {
		return goingRight;
	}

public void setSpriteFrame(){
	if(!this.isDead()){
	if(toOn){
		if(spriteFrameX<2){
			spriteFrameX++;
		}else{toOn=false;}
	}else{
		if(spriteFrameX>0){
			spriteFrameX--;
			
		}else{toOn=true;}
	}}
	}

@Override
public  void kill(){
	GLES20.glDeleteBuffers(3, this.getBuffers(),0);
}

		

}
