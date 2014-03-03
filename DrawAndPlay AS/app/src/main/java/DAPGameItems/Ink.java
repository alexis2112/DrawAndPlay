package DAPGameItems;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.example.drawandplay.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;

public class Ink extends GameItem{
	
	private int spriteFrameXHandler;
	private int spriteFrameYHandler;
	private float spriteFrameX=0;
	private float spriteFrameY=0;
	private long previousTime;
	private Boolean gravityDown;

	private String currentState="MOVE_RIGHT";
	private float[] finalMatrix = new float[16];
	private float[] translation={0.0f,0.0f};
	private int leftRight=1;
	private boolean dying=false;
	
	
	public  Ink(float posX,float posY,Context context){
		super(posX,posY);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setContext(context);
		
	}

	@Override
	public void init() {
		this.setBuffers(new int[5]);
		ByteBuffer bb;
		FloatBuffer fb;
		GLES20.glGenBuffers(5, this.getBuffers(),0);
		
		float[] vertices = {this.getPosX(),this.getPosY(),0.0f,this.getPosX()+1.0f,this.getPosY(),0.0f,this.getPosX()+1.0f,this.getPosY()-1.0f,0.0f,this.getPosX(),this.getPosY()-1.0f,0.0f};
		bb = ByteBuffer.allocateDirect(vertices.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(vertices);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
	
		
		float[] textureCoordsRight = {0.1f,0.0f,
				0.0f,0.0f,
				0.0f,0.33f,
				0.1f,0.33f,
				};
		bb = ByteBuffer.allocateDirect(textureCoordsRight.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoordsRight);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		float[] textureCoordsLeftDown = {0.0f,0.33f,
				0.1f,0.33f,
				
				0.1f,0.0f,
				0.0f,0.0f
				};
		bb = ByteBuffer.allocateDirect(textureCoordsLeftDown.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoordsLeftDown);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[3]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		float[] textureCoordsLeft = {0.0f,0.0f,
				0.1f,0.0f,
				0.1f,0.33f,
				0.0f,0.33f
				
				};
		bb = ByteBuffer.allocateDirect(textureCoordsLeft.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoordsLeft);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[2]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		float[] textureCoordsRightDown = {0.1f,0.33f,
				0.0f,0.33f,
				0.0f,0.0f,
				0.1f,0.0f,
				
				
				};
		bb = ByteBuffer.allocateDirect(textureCoordsRightDown.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoordsRightDown);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[4]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		fb.clear();
		bb.clear();
		
		this.setTransformationMatrixHandler(GLES20.glGetUniformLocation(this.getProgram(), "uMVPMatrix"));
		this.setVertexHandler(GLES20.glGetAttribLocation(this.getProgram(), "a_Position"));
		this.setTextureCoordHandler(GLES20.glGetAttribLocation(this.getProgram(), "a_TexCoordinate"));
		this.setTextureHandler(GLES20.glGetUniformLocation(this.getProgram(), "u_Texture"));
		spriteFrameXHandler =GLES20.glGetUniformLocation(this.getProgram(), "spriteFrameX");
		spriteFrameYHandler =GLES20.glGetUniformLocation(this.getProgram(), "spriteFrameY");
		
		
		Matrix.setIdentityM(this.getTransformationMatrix(),0);
		
		
		
		
	}

	@Override
	public void draw() {
		
		long time =SystemClock.currentThreadTimeMillis();
		long interval =time- previousTime;
		if(interval>=7){
		this.setSpriteFrame();
		previousTime=time;}
		
		
		GLES20.glUseProgram(this.getProgram());
		
		 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture());
	     GLES20.glUniform1i(this.getTextureHandler(),0);
	     GLES20.glUniform1f(spriteFrameXHandler, spriteFrameX);
	     GLES20.glUniform1f(spriteFrameYHandler, spriteFrameY);
			
	     
	     GLES20.glEnableVertexAttribArray(this.getVertexHandler());
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[0]);
	     GLES20.glVertexAttribPointer(this.getVertexHandler(),3, GLES20.GL_FLOAT, false, 0, 0);
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
		
	     GLES20.glEnableVertexAttribArray(this.getTextureCoordHandler());
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[leftRight]);
	     GLES20.glVertexAttribPointer(this.getTextureCoordHandler(),2, GLES20.GL_FLOAT, false, 0, 0);
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
	     
	    
	     Matrix.multiplyMM(finalMatrix, 0, this.getRenderer().getMVPMatrix(), 0, this.getTransformationMatrix(), 0);
	     Matrix.translateM(finalMatrix, 0, translation[0], translation[1], 0);
	     
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
	    Bitmap tex = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.finalsprite2, bo);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]); 

	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

	    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, tex, 0);
	    tex.recycle();


	    this.setTexture(texture[0]);
		
	
	}

	
	
	public void setSpriteFrame(){
		if(!dying){
		if(currentState.equals("MOVE_RIGHT")){
			this.spriteFrameY=2;
			if(this.gravityDown){this.leftRight=1;}else{this.leftRight=4;}
			
		if( spriteFrameX<9){
			spriteFrameX++;
		}else{spriteFrameX=0;}}
		
		if(currentState.equals("MOVE_LEFT")){
			this.spriteFrameY=2;
			if(this.gravityDown){this.leftRight=2;}else{this.leftRight=3;}
			
		if( spriteFrameX<9){
			spriteFrameX++;
		}else{spriteFrameX=0;}}
		
		if(currentState.equals("JUMP_LEFT")||currentState.equals("FALL_LEFT")){
			this.spriteFrameY=0;
			if(this.gravityDown){this.leftRight=2;}else{this.leftRight=3;}
			
			if( spriteFrameX<9){
				spriteFrameX++;
			}else{spriteFrameX=0;}
		}
		
		if(currentState.equals("JUMP_RIGHT")||currentState.equals("FALL_RIGHT")){
			this.spriteFrameY=0;
			if(this.gravityDown){this.leftRight=1;}else{this.leftRight=4;}
		
			if( spriteFrameX<9){
				spriteFrameX++;
			}else{spriteFrameX=0;}
		}
		
		if(currentState.equals("IDLE")){
			this.spriteFrameY=1;
			if( spriteFrameX<9){
				spriteFrameX++;
			}else{spriteFrameX=0;}
		}
		}
		if(dying){
			this.spriteFrameY=0;
			this.spriteFrameX=9;
		}
		
		
	}


	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public float[] getTranslation() {
		return translation;
	}

	public void setTranslation(float[] translation) {
		this.translation = translation;
	}

	public void setGravityDown(Boolean gravityDown) {
		this.gravityDown = gravityDown;
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}
	
	


	@Override
	public  void kill(){
		GLES20.glDeleteBuffers(5, this.getBuffers(),0);
	}

	




	
	

}
