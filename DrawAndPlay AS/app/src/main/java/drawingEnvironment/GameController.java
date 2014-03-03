package drawingEnvironment;

import java.util.ArrayList;

import DAPGameItems.ClapetBas;
import DAPGameItems.ClapetHaut;
import DAPGameItems.DegoulinuresEffaceur;
import DAPGameItems.Effaceur;
import DAPGameItems.Escargot;
import DAPGameItems.Fauteuil;
import DAPGameItems.GameItem;
import DAPGameItems.GrosseGoutte;
import DAPGameItems.Ink;
import DAPGameItems.Pics;
import DAPGameItems.Pieuvre;
import DAPGameItems.PlateformeMobileHorizontale;
import DAPGameItems.PlateformeMobileVerticale;
import DAPGameItems.Sortie;


public class GameController {

	private Ink ink; //le personnage
	private float x;//position en x de ink
	private float y;//position de y de ink
	private ArrayList<GameItem> DisplayList;//le niveau
	
	private boolean down=false;//si on appuie sur l'écran
	
	private boolean jump=false;//si on saute
	private float jumpHeight=0;//hauteur courrante de notre saut
	
	private float fallHeight=4.5f;//variable qui dit de quelle hauteur on saute (4.5 rendait un résultat pas mal )
	private float inertiaCoeff=1;//un coefficient qui permet que quand on arrête de bouger de droite à gauche pendant un saut on continue d'avancer un peut pendant un moment
	
	private boolean onAMobilePlatform=false;//si on est sur une plateforme mobile verticale
	private GameItem mobilePlatform;//la plateforme verticale sur laquelle on est

    //si on est bloqué dans une direction
	private boolean blockedRight=false;
	private boolean blockedLeft=false;
	private boolean blockedUp=false;
	private boolean blockedDown=false;

	private boolean lookleft=false;//notre orientation
	private Boolean gravityDown=true;//la gravité
	
	private DAPGLSurfaceView surfaceView;//la surface view pour avoir acces au renderer
	private float xEvent=0;//position en x du toucher de l'écran

    //les limites du niveau, si on les franchit on meurt
	private float maxHeight;
	private float minHeight;
	private float maxX;
    //position de la camera et translation du personnage par rapport à la position initiale
	private float[] eyePos;
	private float[] translation;
	

	//cette classe a pour but d'implémenter une méthode qui va controller les mouvements de notre personnage
	public GameController(DAPGLSurfaceView surfaceView){
		this.surfaceView=surfaceView;
		
	}
	
	
	public void run(){
	
	
	
			
		//on initialise un certain nombre de variables pour ne pas avoir à le faire plus tard
		    this.ink =this.surfaceView.getRenderer().getInk();
		    DisplayList = this.surfaceView.getRenderer().getDisplayList();
			x = ink.getPosX()+ink.getTranslation()[0];
			y = ink.getPosY()+ink.getTranslation()[1];
        //le eyepos c'est la pos de la camera
			this.eyePos=this.surfaceView.getRenderer().getEyePos();
			this.translation=ink.getTranslation();
			
			
			
			if(this.gravityDown){
				//on dit au perso le sens de la gravité pour qu'il change de coordonnées de textures
				ink.setGravityDown(true);
                //on regarde si on est bloqué par une plateforme en bas
			    this.checkEnvironmentDownGravityDown();
			    //permet de ne pas tomber quand on saute
			    if( jump){blockedDown=true;}
                //on regarde si on est pas censé bouger sur une plateforme mobile
			    this.checkVerticalMobilePlatform();
                //on tombe par défaut
			    this.fallDown();
			
			
			//sauter (quand on meurt on saute aussi une fois )
			if(jump&&blockedDown&&jumpHeight<=1.0f||ink.isDying()&&jumpHeight<=1.0f){
				//on regarde si rien ne nous bloque en haut
				this.checkEnvironmentUpGravityDown();
				if(!blockedUp||ink.isDying()){
                    //on dit a au personnage quelle animation il doit jouer selon son orientation
				if(lookleft){ink.setCurrentState("JUMP_LEFT");}
				if(!lookleft){ink.setCurrentState("JUMP_RIGHT");}
				
				//on Bouge La Camera
				this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]+0.08f- jumpHeight*jumpHeight*0.07f;
				//on bouge Ink
			    ink.getTranslation()[1]=this.translation[1]+0.08f- jumpHeight*jumpHeight*0.07f;
			    this.jumpHeight=Math.abs(this.jumpHeight+0.08f- jumpHeight*jumpHeight*0.07f);}
                else{//quand on est bloqué on va retomber
                jump=false;
			    jumpHeight=0.0f;
			    }}else{//quand on a fini de sauter on va retomber
                jump=false;
			    jumpHeight=0.0f;
			    }
			
			
			//bouger � droite ou � gauche
               //on vérifie que on est pas mort et que le joueur appuie quelquepart
             if(down&&!ink.isDying()){
                 //on regarde à droite et à gauche si on est pas bloqué
            	 this.checkEnvironmentLeftRight();
				
				if(xEvent<=this.surfaceView.getWidth()/3){
                    //si on appuie à gauche on bouge à gauche
					lookleft=true;
					
					if(blockedDown&&!jump){ink.setCurrentState("MOVE_LEFT");}
					if(!blockedLeft){
					//on Bouge La Camera
					this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]-0.03f;
					//on bouge Ink
				    ink.getTranslation()[0]=this.translation[0]-0.03f;}
					}
				if(xEvent>=2*this.surfaceView.getWidth()/3){
                    //pareil pour la droite
                    lookleft=false;
						if(blockedDown&&!jump){ink.setCurrentState("MOVE_RIGHT");}
						if(!blockedRight){
					this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]+0.03f;
				    ink.getTranslation()[0]=this.translation[0]+0.03f;}}
				}
			
             //ne pas bouger par défault
			if(!down&&blockedDown&&!jump){
				this.checkEnvironmentLeftRight();
				ink.setCurrentState("IDLE");}
			
			}
			
			//on fait pareil que pour avec la gravité vers le bas mais pour la gravité vers le haut
			if(!this.gravityDown){
				
				ink.setGravityDown(false);
				this.checkEnvironmentUpGravityUp();
				//permet de ne pas tomber quand on saute
				if( jump){blockedUp=true;}
				this.checkVerticalMobilePlatform();
				this.fallUp();
				
				
				
				//sauter
				if(jump&&blockedUp&&jumpHeight>=-1.0f||ink.isDying()&&jumpHeight>=-1.0f){
					
					this.checkEnvironmentDownGravityUp();
					if(!blockedDown||ink.isDying()){
					if(lookleft){ink.setCurrentState("JUMP_LEFT");}
					if(!lookleft){ink.setCurrentState("JUMP_RIGHT");}
					
					//on Bouge La Camera
					this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]-0.08f + jumpHeight*jumpHeight*0.07f;
					//on bouge Ink
				    ink.getTranslation()[1]=this.translation[1]-0.08f+ jumpHeight*jumpHeight*0.07f;
				    this.jumpHeight=this.jumpHeight-0.08f+ jumpHeight*jumpHeight*0.07f;}else{jump=false;
				    jumpHeight=0.0f;
				    }
					}else{jump=false;
				    jumpHeight=0.0f;
				    }
				
				//ne pas bouger
				if(!down&&blockedUp&&!jump){
					this.checkEnvironmentLeftRight();
					ink.setCurrentState("IDLE");}
				
				//bouger � droite ou � gauche
				if(down&&!ink.isDying()){
					this.checkEnvironmentLeftRight();
					
					if(xEvent<=this.surfaceView.getWidth()/3){
						lookleft=true;
						
						if(blockedUp&&!jump){ink.setCurrentState("MOVE_LEFT");}
						if(!blockedLeft){
						//on Bouge La Camera
						this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]-0.03f;
						//on bouge Ink
					    ink.getTranslation()[0]=this.translation[0]-0.03f;}
						}
					if(xEvent>=2*this.surfaceView.getWidth()/3){lookleft=false;
							if(blockedUp&&!jump){ink.setCurrentState("MOVE_RIGHT");}
							if(!blockedRight){
						this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]+0.03f;
					    ink.getTranslation()[0]=this.translation[0]+0.03f;}}
					}
				}
			if(y>=this.maxHeight+3.0f||y<=minHeight-3.0f){
				this.reset();
			}
			this.inertia();
			
			blockedRight=false;
			blockedLeft=false;
			blockedDown=false;
			blockedUp=false;
		
			
			if(ink.isDying()&&!jump){
				ink.setDying(false);
				ink.setCurrentState("DEAD");
				
			}
			
		}
		
	



	public void setDown(boolean down) {
		this.down = down;
	}



	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	
	
	public Ink getInk() {
		return ink;
	}

	public Boolean getGravityDown() {
		return gravityDown;
	}

	public void setGravityDown(Boolean gravityDown) {
		this.gravityDown = gravityDown;
	}

	public void setOnAMobilePlatform(boolean onAMobilePlatform) {
		this.onAMobilePlatform = onAMobilePlatform;
	}

	public void reset(){
		ink.setDying(true);
		if(this.gravityDown){
		this.blockedDown=true;}else{this.blockedUp=true;}
		this.jump=true;
		
		
		
	}
	
	public void checkEnvironmentDownGravityDown(){
        float xi;
        float yi;
        float width;
        float length;
		for(GameItem item : DisplayList){
			if(item.isOn()&&!item.isDead()){
				xi=item.getPosX();
				yi=item.getPosY();
				width=item.getWidth();
				length = item.getLength();
				
				//on checkDown
if(( x >xi-0.6f)&&( x <xi+length-0.40f)&&(yi+width+0.1<=y-0.6f)&&(y-0.6f<=(yi+width+0.17f))){
	
	
	              this.fallHeight=4.5f;
					
					if(item instanceof PlateformeMobileVerticale&&!onAMobilePlatform){
						this.onAMobilePlatform=true;
						this.mobilePlatform=item;}
					
					if(item instanceof PlateformeMobileHorizontale){
						if(item.isGoingRight()&&!this.blockedRight){
							this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]+0.01f;
						    ink.getTranslation()[0]=this.translation[0]+0.01f;}
						
						if(!item.isGoingRight()&&!this.blockedLeft){
							this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]-0.01f;
						    ink.getTranslation()[0]=this.translation[0]-0.01f;}
					}
					
             	   if(item instanceof Pics){reset();}else{
					    if(item instanceof ClapetBas ){}else{
					    	if(item instanceof PlateformeMobileVerticale){}else{
					    		if(!this.onAMobilePlatform){
					    	ink.getTranslation()[1]=yi+width + 0.135f-ink.getPosY()+0.6f;
					    	this.surfaceView.getRenderer().getEyePos()[1]=ink.getPosY()+this.translation[1];}}
						  blockedDown=true;
						if(item!=this.mobilePlatform){
				      onAMobilePlatform=false;}}}
             	   if((item instanceof Escargot||item instanceof Pieuvre)&&!this.ink.getCurrentState().equals("JUMP_LEFT")&&!ink.getCurrentState().equals("JUMP_RIGHT")){
             		   item.setDead(true);
             		   this.jump=true;
             	   }
             	  if(item instanceof GrosseGoutte||item instanceof Effaceur){
            		  this.jump=true;
            	   }
             }
				
			}
			
			
		}
	}
	
	public void checkEnvironmentUpGravityDown(){
        float xi;
        float yi;
        float length;
		
		for(GameItem item : DisplayList){
			if(item.isOn()&&!item.isDead()){
				xi=item.getPosX();
				yi=item.getPosY();
			    length = item.getLength();
				
				//on check up
				if(( x >xi-0.6f)&&( x <xi+length-0.4f)&&(yi+0.4<=y)&&(y<=(yi+0.45f))){
					
					if(item instanceof Pieuvre ){reset();}else{
						if(item instanceof ClapetHaut){}else{blockedUp=true;}}
					}
				    }
			}
			
			
		}
		
	public void checkEnvironmentUpGravityUp(){
        float xi;
        float yi;
        float length;
		for(GameItem item : DisplayList){
			//mise en place des variables pour ne pas les recalculer � chaque fois

			if(item.isOn()&&!item.isDead()){
			 xi=item.getPosX();
			 yi=item.getPosY();
		     length = item.getLength();
			//on check up
			if(( x >xi-0.6f)&&( x <xi+length-0.40f)&&(yi+0.23<=y)&&(y<=(yi+0.3f))){
				this.fallHeight=4.5f;
				
				
				if(item instanceof PlateformeMobileVerticale&&!onAMobilePlatform){
					this.onAMobilePlatform=true;
					this.mobilePlatform=item;}
				
				if(item instanceof PlateformeMobileHorizontale){
					if(item.isGoingRight()&&!this.blockedRight){
						this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]+0.01f;
					    ink.getTranslation()[0]=this.translation[0]+0.01f;}
					
					if(!item.isGoingRight()&&!this.blockedLeft){
						this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]-0.01f;
					    ink.getTranslation()[0]=this.translation[0]-0.01f;}
				}
				
				if(item instanceof Pieuvre&&!this.ink.getCurrentState().equals("JUMP_LEFT")&&!ink.getCurrentState().equals("JUMP_RIGHT")){
          		   item.setDead(true);
          		   this.jump=true;
          	   }
         	   if(item instanceof Pics||item instanceof Effaceur){reset();}else{
				    if(item instanceof ClapetHaut ){}else{
				    	if(!this.onAMobilePlatform){
				    	ink.getTranslation()[1]=yi+0.265f-ink.getPosY();
				    	this.surfaceView.getRenderer().getEyePos()[1]=ink.getPosY()+this.translation[1];}
				    	
					  blockedUp=true;
					if(item!=this.mobilePlatform&&this.onAMobilePlatform){
						if(this.mobilePlatform.isGoingUp()){
			      onAMobilePlatform=false;}}}}
				
				
			
				}
			
			}
			
		}
		
		
	}
	
	public void checkEnvironmentDownGravityUp(){
        float xi;
        float yi;
        float width;
        float length;
		for(GameItem item : DisplayList){

			//mise en place des variables pour ne pas les recalculer � chaque fois
			if(item.isOn()&&!item.isDead()){

                xi = item.getPosX();
                yi=item.getPosY();
			    width=item.getWidth();
			    length= item.getLength();
			
			//on BlockDown
			if(( x >xi-0.5f)&&( x <xi+length-0.45f)&&(yi+width+0.15<=y-0.6f)&&(y-0.6f<=(yi+width+0.2f))){
				
				if(item instanceof Effaceur||item instanceof Pieuvre){reset();}else{
					if(item instanceof ClapetBas){}else{blockedDown=true;}}
               
			}
			
			}}
			
	}

	public void checkEnvironmentLeftRight(){
		if(!ink.isDying()){
            float xi;
            float yi;
            float width;
            float length;

		for(GameItem item : DisplayList){
			//mise en place des variables pour ne pas les recalculer � chaque fois
			if(item.isOn()&&!item.isDead()){
                xi = item.getPosX();
                yi=item.getPosY();
                width=item.getWidth();
                length= item.getLength();

			//le blockedRight
			if(( x >(xi-0.55f))&&( x <xi-0.35f)&&(yi-0.3f<=y-0.6f)&&(y-0.6f<=(yi+width+0.1f))){

				if((item instanceof DegoulinuresEffaceur||item instanceof Effaceur||item instanceof Escargot||item instanceof GrosseGoutte||item instanceof Pieuvre)){reset();}
				if(item instanceof Fauteuil){ item.moveRight();}else{
					if(item instanceof Sortie){ink.setCurrentState("YEAH");}else{
				blockedRight=true;}}}

			//le blockedLeft
			if(( x >(xi+length-0.65f))&&( x <xi+length-0.45f)&&(yi-0.3f<=y-0.6f)&&(y-0.6f<=(yi+width+0.1f))){
				if(item instanceof DegoulinuresEffaceur||item instanceof Effaceur||item instanceof Escargot||item instanceof GrosseGoutte||item instanceof Pieuvre){reset();}
				if(item instanceof Fauteuil){ item.moveLeft();}else{
					if(item instanceof Sortie){ink.setCurrentState("YEAH");}else{
				blockedLeft=true;}}}
			}}}
		
	}

    public void checkVerticalMobilePlatform(){
    	if(!jump&&onAMobilePlatform&&( x >this.mobilePlatform.getPosX()-0.5f)&&( x <this.mobilePlatform.getPosX()+this.mobilePlatform.getLength()-0.45f)){
			
			if(this.mobilePlatform.isGoingUp()&&!this.blockedUp){
				this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]+0.01f;
	            ink.getTranslation()[1]=this.translation[1]+0.01f;
			}
			
			if(!this.mobilePlatform.isGoingUp()&&!this.blockedDown){
				this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]-0.01f;
	            ink.getTranslation()[1]=this.translation[1]-0.01f;
			}
			
			if(!this.gravityDown){
			blockedUp=true;}else{blockedDown=true;}
		}else{onAMobilePlatform=false;}
    	
    }

    public void fallDown(){
    	//on tombe si rien ne nous en emp�che
		if(!blockedDown){
			if(lookleft){ink.setCurrentState("FALL_LEFT");}
			if(!lookleft){ink.setCurrentState("FALL_RIGHT");}
			if(0.001f*this.fallHeight*this.fallHeight<=0.06f){
			//on Bouge La Camera
			this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]-0.0015f*this.fallHeight*this.fallHeight;
			this.fallHeight=this.fallHeight + 0.0015f*this.fallHeight*this.fallHeight;
			//on bouge Ink
		    ink.getTranslation()[1]=this.translation[1]-0.0015f*this.fallHeight*this.fallHeight;}
			else{this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]-0.06f;
			this.fallHeight=this.fallHeight + 0.06f;
			//on bouge Ink
		    ink.getTranslation()[1]=this.translation[1]-0.06f;
				
			}
		    
			}
    }

    public void fallUp(){
    	//on tombe si rien ne nous en emp�che
		if(!blockedUp){
			if(lookleft){ink.setCurrentState("FALL_LEFT");}
			if(!lookleft){ink.setCurrentState("FALL_RIGHT");}
			if( 0.0015f*this.fallHeight*this.fallHeight<=0.06f){
			//on Bouge La Camera
			this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]+0.0015f*fallHeight*fallHeight;
			this.fallHeight=this.fallHeight + 0.0015f*this.fallHeight*this.fallHeight;
			//on bouge Ink
		    ink.getTranslation()[1]=this.translation[1]+0.0015f*fallHeight*fallHeight;}
			else{this.surfaceView.getRenderer().getEyePos()[1]=this.eyePos[1]+0.06f;
			this.fallHeight=this.fallHeight + 0.06f;
			//on bouge Ink
		    ink.getTranslation()[1]=this.translation[1]+0.06f;}
		    
		}
    }





	public void setXEvent(float x) {
		this.xEvent = x;
	}


	public boolean isJump() {
		return jump;
	}




	public void setMaxHeight(float maxHeight) {
		this.maxHeight = maxHeight;
	}


	public void setMinHeight(float minHeight) {
		this.minHeight = minHeight;
	}


	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}


	public float getMaxHeight() {
		return maxHeight;
	}


	public float getMinHeight() {
		return minHeight;
	}


	public float getMaxX() {
		return maxX;
	}
	
	public void inertia(){
		String state = ink.getCurrentState();
		this.checkEnvironmentLeftRight();
		if(!this.down&&inertiaCoeff>=0.001){
		if((state.equals("JUMP_LEFT")||state.equals("FALL_LEFT"))&&!blockedLeft){
			this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]-0.03f*inertiaCoeff;
			ink.getTranslation()[0]=this.translation[0]-0.03f*inertiaCoeff;
			this.inertiaCoeff=inertiaCoeff/1.05f;
		}
		if((state.equals("JUMP_RIGHT")||state.equals("FALL_RIGHT"))&&!blockedRight){
			this.surfaceView.getRenderer().getEyePos()[0]=this.eyePos[0]+0.03f*inertiaCoeff;
			ink.getTranslation()[0]=this.translation[0]+0.03f*inertiaCoeff;
			this.inertiaCoeff=inertiaCoeff/1.05f;
		}
	}else{this.inertiaCoeff=1;}
		}



	
    

}
	
	


