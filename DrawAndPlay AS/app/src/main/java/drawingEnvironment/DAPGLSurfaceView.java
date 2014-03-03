package drawingEnvironment;

import java.io.FileOutputStream;
import java.util.ArrayList;

import DAPGameItems.BackgroundDynamic;

import DAPGameItems.DegoulinuresEffaceur;
import DAPGameItems.Effaceur;

import DAPGameItems.Fauteuil;
import DAPGameItems.GameItem;

import DAPGameItems.Ink;

import DAPGameItems.PlateformeFixe;

import android.content.Context;


import android.opengl.GLSurfaceView;



import android.view.MotionEvent;

public class DAPGLSurfaceView extends GLSurfaceView{
	
	private DAPGLRenderer renderer;
	private GameController gameController;
	private float lastTouchY=0;
	private float distance;
    private int jumpId=-1;
    private int jumpCoin=0;
 
    private int id1;
    private int id2;
  
    
   


	private ArrayList<GameItem> level=new ArrayList<GameItem>(0) ;
	

	public DAPGLSurfaceView(Context context,ArrayList<GameItem> level,Ink ink) {
		super(context);
		this.setEGLContextClientVersion(2);
		this.level=level;
		this.placementDesDegoulinures();
		
		
		
		this.renderer =new DAPGLRenderer(this.level,context,ink);
		gameController = new GameController(this);
		this.setBounds();
		BackgroundDynamic backgroundDynamic = new BackgroundDynamic(-8.0f,-8.0f,this.gameController.getMaxHeight()-this.gameController.getMinHeight()+12.0f,this.gameController.getMaxX()+12.0f,context);
		this.renderer.setBackgroundDynamic(backgroundDynamic);
		
		this.renderer.setController(gameController);
		this.setRenderer(this.renderer);
		this.setSVC();


		
	}
	
	@Override
	public boolean onTouchEvent( MotionEvent arg1) {
		
		int size = arg1.getPointerCount();
		int index = arg1.getActionIndex();
		int id = arg1.getPointerId(index);
		float x = arg1.getX(index);
	
	
		switch(arg1.getActionMasked()){
		
		case MotionEvent.ACTION_DOWN:
			this.gameController.setDown(true);
			this.gameController.setXEvent(x);
			id1=id;
            lastTouchY=0;
			distance=0;
			
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if(size<=2){
			id2=id;
			lastTouchY=0;
			distance=0;}
			break;  
		

			
		case MotionEvent.ACTION_UP:
			this.gameController.setDown(false);
				lastTouchY=0;
				distance=0;
				id1=id2=-1;
				
				if(id==jumpId){
					jumpId=-1;
					jumpCoin=0;
				}
				break;
		case MotionEvent.ACTION_POINTER_UP:
		
			if(arg1.getPointerCount()>1){
				if(id==id1){
					id1=id2;
					float x1 = arg1.getX(id2);
					id2=-1;
					
					this.gameController.setXEvent(x1);
					}
				}
			if(id==jumpId){
				jumpId=-1;
				jumpCoin=0;
			}
			
			break;
		
		case MotionEvent.ACTION_MOVE:
			if(jumpId==-1){
				jumpId=id;
				
			}
			
			if(size>=2){
				if(jumpId==id1){
					lastTouchY=0;
					distance=0;
				}
				jumpId=id2;
			
			
			}else{
				if(jumpId==id2){
					lastTouchY=0;
					distance=0;
				}
				jumpId=id1;}
			
		int jumpIndex = arg1.findPointerIndex(jumpId);
		     final float y = arg1.getY(jumpIndex);

			
			if(lastTouchY==0){
				lastTouchY=y;
			}
			float length =lastTouchY-y;
			distance=distance+length;
			if(length>=this.getWidth()/40&&!this.gameController.isJump()&&!this.gameController.getInk().getCurrentState().equals("FALL_LEFT")&&!this.gameController.getInk().getCurrentState().equals("FALL_RIGHT")){
				if(jumpCoin==0){
					jumpCoin=1;
				
			this.gameController.setJump(true);}
			lastTouchY=0;
			distance=0;
			}
			
			if(length<=-this.getWidth()/6){
				this.gameController.setOnAMobilePlatform(false);
				if(!this.gameController.getInk().getCurrentState().equals("FALL_LEFT")&&!this.gameController.getInk().getCurrentState().equals("FALL_RIGHT")&&!this.gameController.isJump()){
				if(this.gameController.getGravityDown()){this.gameController.setGravityDown(false);}
				else{this.gameController.setGravityDown(true);}}
				lastTouchY=0;
				distance=0;
				}
				
			break;
			
		
			
		}

		return true;}

	public DAPGLRenderer getRenderer() {
		return renderer;
	}
	
	public void placementDesDegoulinures(){
		ArrayList<GameItem> degoulinures = new ArrayList<GameItem>(0);
		//on veut que les d�goulinures aillent jusqu'� la plateforme la plus proche en dessous
		//donc on regarde toutes les plateformes et on d�termine la bonne
		for(GameItem item : level){
			
			
		if(item instanceof Effaceur){
				float x = item.getPosX();
				float y= item.getPosY();
				GameItem plateforme=new PlateformeFixe(0.0f,-10000f,1.0f,this.getContext());
				
				for(GameItem item1 : level){
					if(item1!=item){
						
						float xi = item1.getPosX();
						float yi =item1.getPosY();
						float length = item1.getLength();
						float width = item1.getWidth();
						
						if(( x >xi-0.3f)&&( x <xi+length)&&(yi+width>=plateforme.getPosY()+plateforme.getWidth())&&yi+width<=y){
							plateforme=item1;}}
					
					}
				float posX =item.getPosX()+0.1f;
				float posY= plateforme.getPosY()+plateforme.getWidth();
				float width =Math.abs(item.getPosY()-(plateforme.getPosY()+plateforme.getWidth()));
				if(width!=0){
				DegoulinuresEffaceur deg = new DegoulinuresEffaceur(posX,posY,width,this.getContext());
				degoulinures.add(deg);}
				}
		}
	for(GameItem item : degoulinures){
		level.add(item);
	}
	}


	
	public void setBounds(){
		float maxHeight=0;
		float minHeight=0;
		float maxX=0;
				
		for(GameItem item : level){
			float x=item.getPosX()+item.getLength();
			float y = item.getPosY();
			float width = item.getWidth();
			
			
			if(x>maxX){
				maxX=x;
			}
			if(y+width>maxHeight){
				maxHeight=y+width;
			}
			if(y<minHeight){
				minHeight=y;
			}
			this.gameController.setMaxHeight(maxHeight);
			this.gameController.setMinHeight(minHeight);
			this.gameController.setMaxX(maxX);
			
		}
	}
	
	public void onStop(){
		ArrayList<GameItem> displayList = this.getRenderer().getDisplayList();
		for(GameItem item : displayList){
			item.killForGood();
		
		}
		this.getRenderer().getInk().killForGood();
		this.getRenderer().getBackground().killForGood();
		this.getRenderer().getBackgroundDynamic().killForGood();
	}
	
	public void setSVC(){
		for(GameItem item :level){
			if(item instanceof Fauteuil){
			
				
				item.setController(gameController);
			}
		}
	}


	
	
	
	
}
