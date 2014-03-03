package drawingEnvironment;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import main.GameOverActivity;

import DAPGameItems.Background;
import DAPGameItems.BackgroundDynamic;
import DAPGameItems.ClapetBas;
import DAPGameItems.ClapetHaut;

import DAPGameItems.DegoulinuresEffaceur;
import DAPGameItems.Effaceur;
import DAPGameItems.Escargot;
import DAPGameItems.GameItem;
import DAPGameItems.GameOver;
import DAPGameItems.GrosseGoutte;
import DAPGameItems.Ink;
import DAPGameItems.Pieuvre;
import DAPGameItems.Yeah;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;


public class DAPGLRenderer implements GLSurfaceView.Renderer {
	
	private float[] viewMatrix = new float[16];
	private float[] projectionMatrix = new float[16];
	private float[] MVPMatrix = new float[16];
	
    private Ink ink;
    private Background background;
    private BackgroundDynamic backgroundDynamic;
    
 
	private ArrayList<GameItem> displayList=new ArrayList<GameItem>(0);
	private Context context;
	private float[] eyePos ={0.0f,0.0f};
	
	private GameController controller;
	
	public DAPGLRenderer( ArrayList<GameItem> displayList,Context context,Ink ink){
		this.displayList=displayList;
		this.context=context;
		this.ink=ink;
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		long time=SystemClock.currentThreadTimeMillis();
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
		Matrix.setLookAtM(this.viewMatrix, 0, eyePos[0], eyePos[1], 5f, eyePos[0], eyePos[1], 0.0f, 0f, 1.0f, 0.0f);
		Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
		
		if(ink.getCurrentState().equals("DEAD")||ink.getCurrentState().equals("YEAH")){
			
			try {
				
				Thread.sleep((long)2000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			
			Intent intent = new Intent(this.context,GameOverActivity.class);
			 this.context.startActivity(intent);
			 
		
			 
			 try {
					
					Thread.sleep((long)2000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			
		
			
		}
		this.controller.run();
		this.redefineDisplayList();
		this.drawLevel();
		
		
		long timeElapsed = SystemClock.currentThreadTimeMillis()-time;
		if(timeElapsed<=16.66){
			try {
				Thread.sleep((long) (16.66-timeElapsed));
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
		}
		

		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		float ratio =(float) width/height;
		Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3, 7);
		
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		this.eyePos[0]=this.ink.getPosX();
		this.eyePos[1]=this.ink.getPosY();
		
		GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glClearColor(0.0f, 0.6f, 12.0f, 3.0f);
		
		Matrix.setLookAtM(this.viewMatrix, 0, 0.0f, 0.0f, 5f, 0.0f, 0.0f, 0.0f, 0f, 1.0f, 0.0f);
		
		this.initLevel();
		
	}
	
	public void initLevel(){
		//shader pour les objets sans animation de texture
		String vertexShaderCode ="uniform mat4 uMVPMatrix;  "+
				"attribute vec4 a_Position;"+
				"attribute vec2 a_TexCoordinate;"+
				"varying vec2 v_TexCoordinate;"+
			    "void main() {"+
				"v_TexCoordinate =a_TexCoordinate;"+
				" gl_Position = uMVPMatrix*a_Position;}";
		String fragmentShaderCode="precision mediump float;"+
				"varying vec2 v_TexCoordinate;"+
				"uniform sampler2D u_Texture;"+
			    "void main() {"+
				"gl_FragColor = texture2D(u_Texture, v_TexCoordinate);"+
				"}";
		//shader pour ink
		String vertexShaderCode2= 
				"uniform mat4 uMVPMatrix;  "+
				"uniform float spriteFrameX;"+
				"uniform float spriteFrameY;"+
		        "attribute vec4 a_Position;"+
				"attribute vec2 a_TexCoordinate;"+
				"varying vec2 v_TexCoordinate;"+
		        "void main() {"+
				"vec2 spriteVector=vec2(0.1*spriteFrameX,0.333*spriteFrameY);"+
				"v_TexCoordinate =a_TexCoordinate + spriteVector;"+
				" gl_Position = uMVPMatrix*a_Position;}";
				
		String fragmentShaderCode2=
				"precision mediump float;"+
				"varying vec2 v_TexCoordinate;"+
				"uniform sampler2D u_Texture;"+
		        "void main() {"+
			    "gl_FragColor = texture2D(u_Texture, v_TexCoordinate);"+
				"}";
		
		//shader pour les textures anim�e par 2 frames
		String vertexShaderCode3= 
				"uniform mat4 uMVPMatrix;  "+
				"uniform float spriteFrameX;"+
			
		        "attribute vec4 a_Position;"+
				"attribute vec2 a_TexCoordinate;"+
				"varying vec2 v_TexCoordinate;"+
		        "void main() {"+
				"vec2 spriteVector=vec2(0.5*spriteFrameX,0.0);"+
				"v_TexCoordinate =a_TexCoordinate + spriteVector;"+
				" gl_Position = uMVPMatrix*a_Position;}";
				
		String fragmentShaderCode3=
				"precision mediump float;"+
				"varying vec2 v_TexCoordinate;"+
				"uniform sampler2D u_Texture;"+
		        "void main() {"+
			    "gl_FragColor = texture2D(u_Texture, v_TexCoordinate);"+
				"}";
		
		//textures anim�es par 3 frames
		String vertexShaderCode4= 
				"uniform mat4 uMVPMatrix;  "+
				"uniform float spriteFrameX;"+
			
		        "attribute vec4 a_Position;"+
				"attribute vec2 a_TexCoordinate;"+
				"varying vec2 v_TexCoordinate;"+
		        "void main() {"+
				"vec2 spriteVector=vec2(0.333*spriteFrameX,0.0);"+
				"v_TexCoordinate =a_TexCoordinate + spriteVector;"+
				" gl_Position = uMVPMatrix*a_Position;}";
				
		String fragmentShaderCode4=
				"precision mediump float;"+
				"varying vec2 v_TexCoordinate;"+
				"uniform sampler2D u_Texture;"+
		        "void main() {"+
			    "gl_FragColor = texture2D(u_Texture, v_TexCoordinate);"+
				"}";
		
		
		//on cree les programs
		int program = this.makeProgram(vertexShaderCode, fragmentShaderCode);
		int program2 = this.makeProgram(vertexShaderCode2, fragmentShaderCode2);
		int program3= this.makeProgram(vertexShaderCode3, fragmentShaderCode3);
		int program4 =this.makeProgram(vertexShaderCode4, fragmentShaderCode4);
		//on charge les textures pour les items
		for(GameItem item :this.displayList){
		item.loadTexture();
		}
		//on met les programs dans les differents items et on les initialises
		for(GameItem item :this.displayList){
			item.setRenderer(this);
			if((item instanceof Effaceur)||item instanceof DegoulinuresEffaceur){item.setProgram(program3);}
			else{
				if(item instanceof ClapetBas||item instanceof ClapetHaut||item instanceof Escargot||item instanceof GrosseGoutte||item instanceof Pieuvre){item.setProgram(program4);}else{item.setProgram(program);}
				}
			item.init();
		}
		
		
				
				//on fait pareil pour des items particuliers
				this.ink.setRenderer(this);
				this.ink.setProgram(program2);
				this.ink.loadTexture();
				this.ink.init();
				this.background = new Background(-4.0f,-2.3f,8.0f,4.5f,context);
				this.background.setRenderer(this);
				this.background.setProgram(program);
				this.background.loadTexture();
				this.background.init();
				
				
				this.backgroundDynamic.setRenderer(this);
				this.backgroundDynamic.setProgram(program);
				this.backgroundDynamic.loadTexture();
				this.backgroundDynamic.init();
				
	}
	


	//methode qui dessine le niveau
	public void drawLevel(){

		this.background.draw();
		this.backgroundDynamic.draw();
	
		for(GameItem item :this.displayList){
			item.updateCoordinates();
			if(item.isOn()){item.draw();}
			}
		this.ink.draw();
		this.DeadOrEnd();
		
		
	}
	//fait les programs
	public int makeProgram(String vertexShaderCode,String fragmentShaderCode){
		int vertexShader = this.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
	    int fragmentShader = this.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
	    int program = GLES20.glCreateProgram();   
	    GLES20.glAttachShader(program, fragmentShader);
	    GLES20.glAttachShader(program, vertexShader);
	    GLES20.glLinkProgram(program);
	    return program;
		
	}
//charge les shaders
public int loadShader(int type, String shaderCode){
		
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
		
		}

public float[] getMVPMatrix() {
	return MVPMatrix;
}

public float[] getEyePos() {
	return eyePos;
}



public Ink getInk() {
	return ink;
}

public ArrayList<GameItem> getDisplayList() {
	return displayList;
}

    //redéfinit les items de la display list suivant si ils sont loins ou près
public void redefineDisplayList(){
    float x;
    float y;
    float width;
    float length;
	for(GameItem item : displayList){
		x = item.getPosX();
		y = item.getPosY();
		length = item.getLength();
		width = item.getWidth();

        //on verifie si l'item est loin
		if((y>=this.eyePos[1]+4.0f)||(y+width<=this.eyePos[1]-4.0f)||(x>=this.eyePos[0]+4.0f)||(x+length<=this.eyePos[0]-4.0f)){
			if(item.isOn()){
                //si il l'est on met so isOn a false ( si ison est false on  ne dessine pas l'item
				item.setOn(false);
                //on supprime les buffers
				item.kill();
				}
		}else{
			if(!item.isOn()){

				float[] translation ={0.0f,0.0f};
				item.setTranslation(translation);
                //on réinitialise l'item si il revient dans le champ
				item.init();
				item.setOn(true);
				
			}
			
		}
		
	}
}

public void setController(GameController controller) {
	this.controller = controller;
}


public void setBackgroundDynamic(BackgroundDynamic backgroundDynamic) {
	this.backgroundDynamic = backgroundDynamic;
}

public BackgroundDynamic getBackgroundDynamic() {
	return backgroundDynamic;
}

public Background getBackground() {
	return background;
}
//methode qui affiche gameover ou yeah selon si on gagne ou perd
public void DeadOrEnd(){
	if(this.ink.getCurrentState().equals("DEAD")||this.ink.getCurrentState().equals("YEAH")){
		String vertexShaderCode ="uniform mat4 uMVPMatrix;  "+
				"attribute vec4 a_Position;"+
				"attribute vec2 a_TexCoordinate;"+
				"varying vec2 v_TexCoordinate;"+
			    "void main() {"+
				"v_TexCoordinate =a_TexCoordinate;"+
				" gl_Position = uMVPMatrix*a_Position;}";
		String fragmentShaderCode="precision mediump float;"+
				"varying vec2 v_TexCoordinate;"+
				"uniform sampler2D u_Texture;"+
			    "void main() {"+
				"gl_FragColor = texture2D(u_Texture, v_TexCoordinate);"+
				"}";
		int program = this.makeProgram(vertexShaderCode, fragmentShaderCode);
		float x =ink.getPosX() + ink.getTranslation()[0];
		float y =ink.getPosY() + ink.getTranslation()[1];
		
		GameItem item;
		if(this.ink.getCurrentState().equals("DEAD")){
		item = new GameOver(x-2.5f,y-1.5f,3.0f,5.5f,this.context);}else{
			item = new Yeah(x-3.0f,y-1.5f,3.0f,5.5f,this.context);
		}
		item.setProgram(program);
		item.setRenderer(this);
		item.loadTexture();
		item.init();
		item.draw();
		}
}





}
