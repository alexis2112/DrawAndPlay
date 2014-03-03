package DAPGameItems;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.SystemClock;

import com.example.drawandplay.R;

public class ClapetHaut extends GameItem{

	private float spriteFrameX=0;
	private int spriteFrameXHandler;
    private long previousTime;
    private boolean toOn=true;
	
	
	
		public ClapetHaut(float posX,float posY,Context context){
			super(posX,posY);
			this.setPosX(posX);
			this.setPosY(posY);
			this.setWidth(0.8f);
			this.setLength(0.8f);
			this.setContext(context);
			
			
		}

		@Override
		public void init() {
			this.setBuffers(new int[2]);
			ByteBuffer bb;
			FloatBuffer fb;
			GLES20.glGenBuffers(2, this.getBuffers(),0);
			
			float[] vertices = {this.getPosX(),this.getPosY(),0.0f,this.getPosX()+0.8f,this.getPosY(),0.0f,this.getPosX()+0.8f,this.getPosY()+0.8f,0.0f,this.getPosX(),this.getPosY()+0.8f,0.0f};
			bb = ByteBuffer.allocateDirect(vertices.length*4);
			bb.order(ByteOrder.nativeOrder());
			fb =bb.asFloatBuffer();
			fb.put(vertices);
			fb.position(0);
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[0]);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			
		
			
			float[] textureCoords = {
					0.333f,1.0f,
					0.0f,1.0f,
					0.0f,0.0f,
					0.333f,0.0f,
					
					
					
					
					};
			bb = ByteBuffer.allocateDirect(textureCoords.length*4);
			bb.order(ByteOrder.nativeOrder());
			fb =bb.asFloatBuffer();
			fb.put(textureCoords);
			fb.position(0);
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[1]);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			
			fb.clear();
			bb.clear();
			
			this.setTransformationMatrixHandler(GLES20.glGetUniformLocation(this.getProgram(), "uMVPMatrix"));
			this.setVertexHandler(GLES20.glGetAttribLocation(this.getProgram(), "a_Position"));
			this.setTextureCoordHandler(GLES20.glGetAttribLocation(this.getProgram(), "a_TexCoordinate"));
			this.setTextureHandler(GLES20.glGetUniformLocation(this.getProgram(), "u_Texture"));
			spriteFrameXHandler =GLES20.glGetUniformLocation(this.getProgram(), "spriteFrameX");
			
			
			
			
			
			
			
			
			
			
			
		}

		@Override
		public void draw() {
			
			long time = SystemClock.currentThreadTimeMillis();
			long interval = Math.abs(time - previousTime);
			if(interval>=20){
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
		     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[1]);
		     GLES20.glVertexAttribPointer(this.getTextureCoordHandler(),2, GLES20.GL_FLOAT, false, 0, 0);
		     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
		     
		     GLES20.glUniformMatrix4fv(this.getTransformationMatrixHandler(), 1, false,this.getRenderer().getMVPMatrix(), 0);
			
				
				
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
		    Bitmap tex = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.clapetsprite2, bo);
		    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]); 

		    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, tex, 0);
		    tex.recycle();


		    this.setTexture(texture[0]);
			
			
		}

	
		
		public void setSpriteFrame(){
			if(toOn){
				if(spriteFrameX<2){
					spriteFrameX++;
				}else{toOn=false;}
			}else{
				if(spriteFrameX>0){
					spriteFrameX--;
					
				}else{toOn=true;}
			}
			
			
		}



}