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



import com.example.drawandplay.R;



public class Fauteuil extends GameItem{
	
	
	private Boolean blockedDown=false;
	private Boolean blockedLeft=false;
	private Boolean blockedRight=false;
	private boolean blockedUp = false;
	private int upDown=1;
	

	
	private float[] finalMatrix = new float[16];

	


	
	
	public  Fauteuil(float posX,float posY,Context context){
		super(posX,posY);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setWidth(0.5f);
		this.setLength(0.5f);
		this.setContext(context);
        float[] trans={0.0f,0.0f};
        this.setTranslation(trans);
		
		
	}

	@Override
	public void init() {

		this.setBuffers(new int[3]);
		ByteBuffer bb;
		FloatBuffer fb;
		GLES20.glGenBuffers(3, this.getBuffers(),0);
		
		float[] vertices = {this.getPosX(),this.getPosY(),0.0f,this.getPosX()+0.5f,this.getPosY(),0.0f,this.getPosX()+0.5f,this.getPosY()+0.5f,0.0f,this.getPosX(),this.getPosY()+0.5f,0.0f};
		bb = ByteBuffer.allocateDirect(vertices.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(vertices);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
	
		
		float[] textureCoordsUp = {0.0f,1.0f,
				1.0f,1.0f,
				1.0f,0.0f,
				0.0f,0.0f,
				};
		bb = ByteBuffer.allocateDirect(textureCoordsUp.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoordsUp);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		float[] textureCoordsDown = {1.0f,0.0f,
				0.0f,0.0f,
				0.0f,1.0f,
				1.0f,1.0f
				};
		bb = ByteBuffer.allocateDirect(textureCoordsDown.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoordsDown);
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
	
		
	
		Matrix.setIdentityM(this.getTransformationMatrix(),0);
		
		
		
		
	}

	@Override
	public void draw() {
		
		
	
	this.fall();
		
		GLES20.glUseProgram(this.getProgram());
		
		 GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	     GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.getTexture());
	     GLES20.glUniform1i(this.getTextureHandler(),0);
	   
			
	     
	     GLES20.glEnableVertexAttribArray(this.getVertexHandler());
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[0]);
	     GLES20.glVertexAttribPointer(this.getVertexHandler(),3, GLES20.GL_FLOAT, false, 0, 0);
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
		
	     GLES20.glEnableVertexAttribArray(this.getTextureCoordHandler());
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[upDown]);
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
	    Bitmap tex = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.blocmobile, bo);
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
	public void moveRight(){
		this.checkEnvironmentRight();
		if(!blockedRight){
			getTranslation()[0]=getTranslation()[0]+0.03f;
			this.setPosX(this.getPosX()+0.03f);}
		blockedRight=false;
	}
	
	@Override
	public void moveLeft(){
		this.checkEnvironmentLeft();
		if(!blockedLeft){
			getTranslation()[0]=getTranslation()[0]-0.03f;
			this.setPosX(this.getPosX()-0.03f);}
		blockedLeft=false;
	}
	
	public void fall(){
		if(this.getController().getGravityDown()){
			this.upDown=1;
		this.checkEnvironmentDown();
		if(!blockedDown){
			getTranslation()[1]=getTranslation()[1]-0.03f;
			this.setPosY(this.getPosY()-0.03f);
		}
		this.blockedDown=false;}
		
		if(!this.getController().getGravityDown()){
			this.upDown=2;
			this.checkEnvironmentUp();
			if(!blockedUp){
				getTranslation()[1]=getTranslation()[1]+0.03f;
				this.setPosY(this.getPosY()+0.03f);
			}
			this.blockedUp=false;}
		
	}
	
	public void checkEnvironmentDown(){
		float x = this.getPosX();
		float y = this.getPosY();
        float xi;
        float yi;
        float width;
        float length;
		for(GameItem item : this.getRenderer().getDisplayList()){
			if(item!=this&&!item.isDead()){
			xi=item.getPosX();
			yi=item.getPosY();
			width=item.getWidth();
			length = item.getLength();
			//le blocked down
			if(( x >xi-0.45f)&&( x <xi+length)&&(yi+width-0.07<=y)&&(y<=(yi+width+0.025f))){
				if(item instanceof PlateformeMobileVerticale){
					getTranslation()[1]=getTranslation()[1]+0.01f;
					this.setPosY(this.getPosY()+0.01f);
				}
				if(item instanceof GrosseGoutte){item.setDead(true);}
				if(item instanceof ClapetBas ){}else{blockedDown=true;}
				if(item instanceof PlateformeMobileHorizontale){
					this.checkEnvironmentLeft();
					this.checkEnvironmentRight();
				if(item.isGoingRight()&&!blockedRight){
					getTranslation()[0]=getTranslation()[0]+0.01f;
					this.setPosX(this.getPosX()+0.01f);}
				if(!item.isGoingRight()&&!blockedLeft){
					getTranslation()[0]=getTranslation()[0]-0.01f;
					this.setPosX(this.getPosX()-0.01f);}
				}
			
			
			}
			
		
			
			}}
		
	}
	
	public void checkEnvironmentLeft(){
		float x = this.getPosX();
		float y = this.getPosY();
		for(GameItem item : this.getRenderer().getDisplayList()){
			if(item!=this&&!item.isDead()){
			float xi=item.getPosX();
			float yi=item.getPosY();
			float width=item.getWidth();
			float length = item.getLength();
			//le blockedLeft
			if(( x <(xi+length))&&( x >xi+length-0.4)&&((yi<=y+0.7f)&&(y<=(yi+width)))){
			blockedLeft=true;}
			}}
		
	}

	public void checkEnvironmentRight(){
		float x = this.getPosX();
		float y = this.getPosY();
        float xi;
        float yi;
        float width;

		for(GameItem item : this.getRenderer().getDisplayList()){
			if(item!=this&&!item.isDead()){
			 xi=item.getPosX();
			yi=item.getPosY();
			 width=item.getWidth();
			
			//le blockedRight
			if(( x + 0.5f >xi)&&( x+0.5 <xi+0.1f)&&(yi<=y+0.5f)&&(y<=yi+width)){
			blockedRight=true;}
			
			}}
		
	}
	
	public void checkEnvironmentUp(){
		float x = this.getPosX();
		float y = this.getPosY();
        float xi;
        float yi;
        float length;
		for(GameItem item : this.getRenderer().getDisplayList()){
			if(item!=this&&!item.isDead()){
			xi=item.getPosX();
			yi=item.getPosY();
		length = item.getLength();
		
			if(( x >xi-0.45f)&&( x <xi+length)&&(yi-0.5<=y)&&(y<=(yi-0.45f))){
				if(item instanceof PlateformeMobileHorizontale){
					this.checkEnvironmentLeft();
					this.checkEnvironmentRight();
				if(item.isGoingRight()&&!blockedRight){
					getTranslation()[0]=getTranslation()[0]+0.01f;
					this.setPosX(this.getPosX()+0.01f);}
				if(!item.isGoingRight()&&!blockedLeft){
					getTranslation()[0]=getTranslation()[0]-0.01f;
					this.setPosX(this.getPosX()-0.01f);}
				}
				
				if(item instanceof PlateformeMobileVerticale){
					getTranslation()[1]=getTranslation()[1]+0.01f;
					this.setPosY(this.getPosY()+0.01f);
				}
				if(item instanceof ClapetHaut){}else{
			blockedUp=true;}}
			
		
			
			}}
		
		
		
	}

	@Override
	public  void kill(){
		GLES20.glDeleteBuffers(3, this.getBuffers(),0);
	}



}

