package com.rs.game.npc.random;

import com.rs.game.Animation;
import com.rs.game.ForceTalk;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

/**
 * Handles Eli NPC (Herblore random).
 * @author BigFuckinChungus
 */
public class EliNPC extends NPC {

	private static final long serialVersionUID = -2385681443539672863L;
	
	private Player target;
    private long createTime;
    private boolean stop;

    public EliNPC(WorldTile tile, Player target) {
    	super(17347, tile, -1, true, true);
    	this.target = target;
    	createTime = Utils.currentTimeMillis();
		setNextForceTalk(new ForceTalk("Hey, "+target.getDisplayName()+", talk to me."));
		setRun(true);
    }

    /**
     * Gives the Player his reward.
     * @param player The player.
     */
    public void giveReward(final Player player) {
    	player.stopAll(true, false, true);
    	player.setNextAnimation(new Animation(-1));
    	if (player != target || player.isLocked()) {
    		player.getDialogueManager().startDialogue("SimpleNPCMessage", 
    				17347, "I don't have time for chit-chats.");
    		return;
    	}
    	if (player.getStatistics().getPotionsMade() >= 1000) {
			if (!player.getInventory().contains(25194))
				player.getInventory().addItem(new Item(25194, 1));
		}
    	if (player.getStatistics().getPotionsMade() >= 2000) {
			if (!player.getInventory().contains(25193))
				player.getInventory().addItem(new Item(25193, 1));
		}
    	if (player.getStatistics().getPotionsMade() >= 3000) {
			if (!player.getInventory().contains(25190))
				player.getInventory().addItem(new Item(25190, 1));
		}
    	if (player.getStatistics().getPotionsMade() >= 4000) {
			if (!player.getInventory().contains(25192))
				player.getInventory().addItem(new Item(25192, 1));
		}
    	if (player.getStatistics().getPotionsMade() >= 5000) {
			if (!player.getInventory().contains(25191))
				player.getInventory().addItem(new Item(25191, 1));
		}
		stop = true;
		player.getMoneyPouch().sendDynamicInteraction(250000, false);
		setNextForceTalk(new ForceTalk("See you later, "+target.getDisplayName()+"!"));
		player.getPackets().sendGameMessage("<col=ff0000>Eli gives you a reward before leaving.", true);
		player.lock(3);
    	WorldTasksManager.schedule(new WorldTask() {
    		@Override
    		public void run() {
    			player.unlock();
    			stop = false;
    			finish();
    			stop();
    		}
    	}, 2);
    }

    @Override
    public void processNPC() {
    	sendFollow(target);
    	if (target.hasFinished() || createTime + 60000 < Utils.currentTimeMillis())
    		finish();
    	if (!stop) {
    		if (Utils.random(50) <= 2) {
    			setNextForceTalk(new ForceTalk(target.getDisplayName()+" talk to me!"));
    		}
    		else if (Utils.random(50) >= 48) {
    			setNextForceTalk(new ForceTalk("Talk to me, "+target.getDisplayName()+"!"));
    		}
    	}
    }

    @Override
    public boolean withinDistance(Player tile, int distance) {
    	return tile == target && super.withinDistance(tile, distance);
    }
    
    /**
     * Follows the player.
     */
    private void sendFollow(Player player) {
    	if (!withinDistance(player, 2))
    		setNextWorldTile(player);
    	if (getLastFaceEntity() != player.getClientIndex())
    		setNextFaceEntity(player);
    	if (isFrozen())
    		return;
    	int size = getSize();
    	int targetSize = player.getSize();
    	if (Utils.colides(getX(), getY(), size, player.getX(), player.getY(), targetSize) && !player.hasWalkSteps()) {
    		resetWalkSteps();
    		if (!addWalkSteps(player.getX() + targetSize, getY())) {
    			resetWalkSteps();
    			if (!addWalkSteps(player.getX() - size, getY())) {
    				resetWalkSteps();
    				if (!addWalkSteps(getX(), player.getY() + targetSize)) {
    					resetWalkSteps();
    					if (!addWalkSteps(getX(), player.getY() - size)) {
    						return;
    					}
    				}
    			}
    		}
    		return;
    	}
    	resetWalkSteps();
    	if (!clipedProjectile(player, true) || 
    			!Utils.isOnRange(getX(), getY(), size, player.getX(), player.getY(), targetSize, 0))
    		calcFollow(player, 2, true, false);
    }
}