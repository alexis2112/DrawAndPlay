package DAPGameItems;

import drawingEnvironment.DAPGLRenderer;

import drawingEnvironment.GameController;
import android.content.Context;
import android.opengl.GLES20;



public abstract class GameItem {
	
	private float posX;
	private float posY;
	private float length;
	private float width;
	private int program;
	private int[] buffers;
	private float[] transformationMatrix=new float[16];
	private int texture;
	private Context context;
	private DAPGLRenderer renderer;
	private boolean on=true;
	private boolean dead= false;
	private GameController controller;
	
	
	private int vertexHandler;
	private int textureCoordHandler;
	private int transformationMatrixHandler;
	private int textureHandler;
	
	private float initialPositionX;
	private float initialPositionY;
	private float[] translation;
	private int[] textures = new int[1];
	
	
	
	
	public abstract void init();
	public abstract void draw();
	public abstract void loadTexture();
	public  void kill(){
		GLES20.glDeleteBuffers(2, this.getBuffers(),0);
		
	}
	public  void killForGood(){
		this.kill();
		GLES20.glDeleteTextures(1, textures,0);
	}
	
	public GameItem(float x,float y){
		this.initialPositionX=x;
		this.initialPositionY=y;
	}
	
	
	public int getProgram() {
		return program;
	}
	public void setPosX(float posX) {
		this.posX = posX;
	}
	public void setPosY(float posY) {
		this.posY = posY;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getPosX() {
		return posX;
	}
	public float getPosY() {
		return posY;
	}
	public float getLength() {
		return length;
	}
	public float getWidth() {
		return width;
	}
	public void setProgram(int program) {
		this.program = program;
	}
	public int[] getBuffers() {
		return buffers;
	}
	public void setBuffers(int[] buffers) {
		this.buffers = buffers;
	}
	public float[] getTransformationMatrix() {
		return transformationMatrix;
	}
	public int getVertexHandler() {
		return vertexHandler;
	}
	public void setVertexHandler(int vertexHandler) {
		this.vertexHandler = vertexHandler;
	}
	public int getTextureCoordHandler() {
		return textureCoordHandler;
	}
	public void setTextureCoordHandler(int textureCoordHandler) {
		this.textureCoordHandler = textureCoordHandler;
	}
	public int getTransformationMatrixHandler() {
		return transformationMatrixHandler;
	}
	public void setTransformationMatrixHandler(int transformationMatrixHandler) {
		this.transformationMatrixHandler = transformationMatrixHandler;
	}
	public int getTextureHandler() {
		return textureHandler;
	}
	public void setTextureHandler(int textureHandler) {
		this.textureHandler = textureHandler;
	}
	public int getTexture() {
		return texture;
	}
	public void setTexture(int texture) {
		this.texture = texture;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public DAPGLRenderer getRenderer() {
		return renderer;
	}
	public void setRenderer(DAPGLRenderer renderer) {
		this.renderer = renderer;
	}
	public void moveLeft(){}
	public void moveRight(){}
	public boolean isGoingUp(){
		return false;}
	public boolean isGoingRight() {
			return false;
		}
	public boolean isOn() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public void setTranslation(float[] translation) {
		this.translation = translation;
	}
	public float[] getTranslation() {
		return translation;
	}
	
	public void updateCoordinates(){
		
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}
	public GameController getController() {
		return controller;
	}
	public int[] getTextures() {
		return textures;
	}

	

	

	
	
	

}
