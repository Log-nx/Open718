package com.rs.game.player.content.items;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.Region;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.player.Player;
import com.rs.utils.Color;

public class Portables {
    
    public static void placePortable(Player owner, Item item, int objectId) {
    	WorldTile tile = new WorldTile(owner.getX() + 1, owner.getY(), owner.getPlane());
    	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
    		tile = new WorldTile(owner.getX() - 1, tile.getY(), tile.getPlane());
        	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
        		tile = new WorldTile(owner.getX(), tile.getY() + 1, tile.getPlane());
            	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
    	    		tile = new WorldTile(tile.getX(), owner.getY() - 1, tile.getPlane());
    	        	if (!canPlaceObject(owner, tile.getPlane(), tile.getX(), tile.getY())) {
    					owner.sendMessage("You cannot place a portable here; try moving around.");
    					return;
    				}
    			}
    		}
    	}
    	if (item.getId() == ItemIdentifiers.PORTABLE_FORGE) {
    		objectId = 97270;
    	}
    	if (item.getId() == ItemIdentifiers.PORTABLE_RANGE) {
    		objectId = 89768;
    	}
    	if (item.getId() == ItemIdentifiers.PORTABLE_DEPOSIT_BOX) {
    		objectId = 2428;
    	}
    	if (item.getId() == ItemIdentifiers.PORTABLE_WELL) {
    		objectId = 89770;
    	}
    	if (objectId == 0) {
    		return;
    	}
    	final WorldTile finalTile = tile;
    	WorldObject object = new WorldObject(objectId, 10, 0, finalTile.getX(), finalTile.getY(), finalTile.getPlane(), owner);
		World.spawnPortable(object, 300000, owner); //5 minutes
	    owner.setNextAnimation(new Animation(21217));
		owner.faceObject(object);
		owner.getInventory().deleteItem(item);
    }
    
    private static boolean canPlaceObject(Player owner, int plane, int x, int y) {
    	if (!World.canMoveNPC(plane, x, y, 2) || World.getObjectWithSlot(owner, Region.OBJECT_SLOT_FLOOR) != null || owner.getControllerManager().getController() != null || owner.isAnIronMan())
			return false;
    	return true;
    }
    
    public static void removePortable(Player owner, WorldObject object) {
    	owner.getPackets().sendGameMessage(Color.RED, "Your " + ObjectDefinitions.getObjectDefinitions(object.getId()).name.toLowerCase() + " has ran out of time.");
		World.getRegion(object.getRegionId()).removeObject(object, object.getPlane(), object.getXInRegion(), object.getYInRegion());
	}
}