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



public class Escargot extends GameItem{
	
	private Boolean blockedDown=false;
	private Boolean blockedLeft=false;
	private Boolean blockedRight=false;
	
	private float spriteFrameX=0;
	private int spriteFrameXHandler;
    private long previousTime;
    private boolean toOn=true;
    
    private boolean toRight=false;
    private float maxX;
    private float minX;
    private int rightLeft=1;
    
    private float jumpHeight;
	

	
	private float[] finalMatrix = new float[16];
	
	


	
	
	public  Escargot(float posX,float posY,Context context){
		super(posX,posY);
		this.setPosX(posX);
		this.setPosY(posY);
		this.setWidth(0.35f);
		this.setLength(1.0f);
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
		
		float[] vertices = {this.getPosX(),this.getPosY(),0.0f,this.getPosX()+1.0f,this.getPosY(),0.0f,this.getPosX()+1.0f,this.getPosY()+0.5f,0.0f,this.getPosX(),this.getPosY()+0.5f,0.0f};
		bb = ByteBuffer.allocateDirect(vertices.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(vertices);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
	
		
		float[] textureCoords = {0.0f,1.0f,
				0.333f,1.0f,
				0.333f,0.4f,
				0.0f,0.4f,
				};
		bb = ByteBuffer.allocateDirect(textureCoords.length*4);
		bb.order(ByteOrder.nativeOrder());
		fb =bb.asFloatBuffer();
		fb.put(textureCoords);
		fb.position(0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.getBuffers()[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,fb.capacity()*4, fb, GLES20.GL_STATIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		float[] textureCoords2 = {0.333f,1.0f,
				0.0f,1.0f,
				0.0f,0.4f,
				0.333f,0.4f};
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
		spriteFrameXHandler =GLES20.glGetUniformLocation(this.getProgram(), "spriteFrameX");
		
		
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
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,this.getBuffers()[rightLeft]);
	     GLES20.glVertexAttribPointer(this.getTextureCoordHandler(),2, GLES20.GL_FLOAT, false, 0, 0);
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
	     
	    
	     Matrix.multiplyMM(finalMatrix, 0, this.getRenderer().getMVPMatrix(), 0, this.getTransformationMatrix(), 0);
	     Matrix.translateM(finalMatrix, 0, getTranslation()[0], getTranslation()[1], 0);
	     
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
	    Bitmap tex = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.escargotsprite, bo);
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
		
			getTranslation()[0]=getTranslation()[0]+0.01f;
			this.setPosX(this.getPosX()+0.01f);}
	
	
	@Override
	public void moveLeft(){
		
			getTranslation()[0]=getTranslation()[0]-0.01f;
			this.setPosX(this.getPosX()-0.01f);
		
	}
	
	public void fall(){
		if(!this.isDead()){
		this.checkEnvironmentDown();}else{this.blockedDown=false;}
		if(!blockedDown){
			getTranslation()[1]=getTranslation()[1]-0.03f;
			this.setPosY(this.getPosY()-0.03f);
		}
		this.blockedDown=false;
		
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
			if(( x >xi-0.6f)&&( x <xi+length)&&(yi+width-0.07<=y)&&(y<=(yi+width+0.025f))){
			
				if(item instanceof PlateformeMobileVerticale){
					getTranslation()[1]=getTranslation()[1]+0.01f;
					this.setPosY(this.getPosY()+0.01f);
				}
				if(item instanceof ClapetBas ){}else{blockedDown=true;
				this.minX=item.getPosX();
				this.maxX=item.getPosX()+item.getLength()-1.0f;}
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
				if(item instanceof Escargot){
					this.checkEnvironmentRight();
                   if(!blockedRight){
					this.moveRight();}else{
						this.checkEnvironmentLeft();
						if(this.blockedLeft){
						this.moveLeft();}
					}
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
		for(GameItem item : this.getRenderer().getDisplayList()){
			if(item!=this&&!item.isDead()){
			float xi=item.getPosX();
			float yi=item.getPosY();
			float width=item.getWidth();
			
			//le blockedRight
			if(( x + 1.0f >xi)&&( x+0.7 <xi+0.1f)&&(yi<=y+1.0f)&&(y<=yi+width)){
			blockedRight=true;}
			
			}}
		
	}
	
	@Override
	public void updateCoordinates(){
		
		if(!this.isDead()){
			this.jumpHeight=0.0f;
	    this.fall();
		this.checkEnvironmentLeft();
		this.checkEnvironmentRight();
		
		if(toRight&&!blockedRight&&this.getPosX()<=maxX){
			this.moveRight();
			}else{toRight=false;
			rightLeft=1;}
		if(!toRight&&!blockedLeft&&this.getPosX()>=minX){
			this.moveLeft();
			}else{toRight=true;
			rightLeft=2;}
		
		this.blockedLeft=false;
		this.blockedRight=false;}
		else{
			if(this.jumpHeight<=0.5f){
				this.getTranslation()[1]=this.getTranslation()[1]+0.03f;
				this.jumpHeight=this.jumpHeight+0.03f;
			}else{
				this.fall();}
			
			
		}
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
