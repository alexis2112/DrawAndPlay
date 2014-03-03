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

public class PlateformeFixe extends GameItem{
	
	public PlateformeFixe(float posX,float posY,float length,Context context){
		super(posX,posY);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setWidth(0.3f);
		this.setLength(length);
		this.setContext(context);}

	@Override
	public void init() {
		this.setBuffers(new int[2]);
		ByteBuffer bb;
		FloatBuffer fb;
		GLES20.glGenBuffers(2, this.getBuffers(),0);
		
		float[] vertices = {this.getPosX(),this.getPosY(),0.0f,this.getPosX()+this.getLength(),this.getPosY(),0.0f,this.getPosX()+this.getLength(),this.getPosY()+0.3f,0.0f,this.getPosX(),this.getPosY()+0.3f,0.0f};
		bb = ByteBuffer.allocateDirect(vertices.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(vertices);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
	
		
		float[] textureCoords = {this.getLength(),1.0f,
				0.0f,1.0f,
				0.0f,0.0f,
				this.getLength(),0.0f,
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
		
		
		
		
		
		
	}

	@Override
	public void draw() {
		GLES20.glUseProgram(this.getProgram());
		
		 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture());
	     GLES20.glUniform1i(this.getTextureHandler(),0);
	    
			
	     
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
	    Bitmap tex = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.plateforme, bo);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]); 

	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

	    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, tex, 0);
	    tex.recycle();


	    this.setTexture(texture[0]);
	}

	

}
