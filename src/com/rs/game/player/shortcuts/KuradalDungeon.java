package com.rs.game.player.shortcuts;





import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.agility.Agility;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class KuradalDungeon {
 
	private static boolean checkSide(Player player, int corX, int corY) {
		if (player.getX() == corX && player.getY() == corY) {
			return true;
		}
		return false;
	}
	
	public static boolean handleBarriers(final Player player, WorldObject object) {
        switch (object.getId()) {
        case 47236:
        	 if (player.getX() == 1650 && player.getY() == 5281 || player.getX() == 1651 && player.getY() == 5281 || player.getX() == 1650 && player.getY() == 5281) {
                 player.addWalkSteps(1651, 5280, 1, false);
             }      
             if (player.getX() == 1652 && player.getY() == 5280 || player.getX() == 1651 && player.getY() == 5280 || player.getX() == 1653 && player.getY() == 5280) {
                     player.addWalkSteps(1651, 5281, 1, false);
             }      
             if (player.getX() == 1650 && player.getY() == 5301 || player.getX() == 1650 && player.getY() == 5302 || player.getX() == 1650 && player.getY() == 5303) {
                     player.addWalkSteps(1649, 5302, 1, false);
             }      
             if (player.getX() == 1649 && player.getY() == 5303 || player.getX() == 1649 && player.getY() == 5302 || player.getX() == 1649 && player.getY() == 5301) {
                     player.addWalkSteps(1650, 5302, 1, false);
             }      
             if (player.getX() == 1626 && player.getY() == 5301 || player.getX() == 1626 && player.getY() == 5302 || player.getX() == 1626 && player.getY() == 5303) {
                     player.addWalkSteps(1625, 5302, 1, false);
             }      
             if (player.getX() == 1625 && player.getY() == 5301 || player.getX() == 1625 && player.getY() == 5302 || player.getX() == 1625 && player.getY() == 5303) {
                     player.addWalkSteps(1626, 5302, 1, false);
             }      
             if (player.getX() == 1609 && player.getY() == 5289 || player.getX() == 1610 && player.getY() == 5289 || player.getX() == 1611 && player.getY() == 5289) {
                     player.addWalkSteps(1610, 5288, 1, false);
             }      
             if (player.getX() == 1609 && player.getY() == 5288 || player.getX() == 1610 && player.getY() == 5288 || player.getX() == 1611 && player.getY() == 5288) {
                     player.addWalkSteps(1610, 5289, 1, false);
             }      
             if (player.getX() == 1606 && player.getY() == 5265 || player.getX() == 1605 && player.getY() == 5265 || player.getX() == 1604 && player.getY() == 5265) {
                     player.addWalkSteps(1605, 5264, 1, false);
             }      
             if (player.getX() == 1606 && player.getY() == 5264 || player.getX() == 1605 && player.getY() == 5264 || player.getX() == 1604 && player.getY() == 5264) {
                     player.addWalkSteps(1605, 5265, 1, false);
             }      
             if (player.getX() == 1634 && player.getY() == 5254 || player.getX() == 1634 && player.getY() == 5253 || player.getX() == 1634 && player.getY() == 5252) {
                     player.addWalkSteps(1635, 5253, 1, false);
             }      
             if (player.getX() == 1635 && player.getY() == 5254 || player.getX() == 1635 && player.getY() == 5253 || player.getX() == 1635 && player.getY() == 5252) {
                     player.addWalkSteps(1634, 5253, 1, false);
             }      
     }
		return true;
        }
	
	
	  public static void crossGaps(final Player player, final WorldObject object) {
		 if(player.getX() == 1633 && player.getY() == 5292) {
			 player.lock(2);
			 player.setNextAnimation(new Animation(839));
			 final WorldTile toTile = new WorldTile(1633, 5294, object.getPlane());
			 player.setNextForceMovement(new ForceMovement(player, 1, toTile, 2, ForceMovement.NORTH));
			    WorldTasksManager.schedule(new WorldTask() {

				     @Override
				     public void run() {
				      player.setNextWorldTile(toTile);
				     }
				    
				    }, 1);
				    
				   } else {
				    player.lock(2);
				    player.setNextAnimation(new Animation(839));
				    final WorldTile toTile = new WorldTile(1633, 5292, object.getPlane());
				    player.setNextForceMovement(new ForceMovement(player, 1, toTile, 2, ForceMovement.SOUTH)); 
				    WorldTasksManager.schedule(new WorldTask() {

				     public void run() {
				      player.setNextWorldTile(toTile);
				     }
				    }, 1);
				         
				   }
	  }


	
	
		 public static void crossGap(final Player player, final WorldObject object) {
				if (!Agility.hasLevel(player, 90)) {
					return;
				}
		   if (player.getX() == 1641 && player.getY() == 5260) {
		    player.faceObject(object);
		    player.lock(3);
		    player.setNextAnimation(new Animation(2922));
		    final WorldTile toTile = new WorldTile(1641, 5268, object.getPlane());
		    player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH)); 
		    WorldTasksManager.schedule(new WorldTask() {

		     @Override
		     public void run() {
		      player.setNextWorldTile(toTile);
		     }
		    
		    }, 1);
		    
		   } else {
		    player.faceObject(object);
		    player.lock(3);
		    player.setNextAnimation(new Animation(2922));
		    final WorldTile toTile = new WorldTile(1641, 5260, object.getPlane());
		    player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.SOUTH)); 
		    WorldTasksManager.schedule(new WorldTask() {

		     public void run() {
		      player.setNextWorldTile(toTile);
		     }
		    }, 1);
		         
		   }
		
		
		   
		    
		    
		    
		   
		   
		 

		
	
	
		 }	
}
