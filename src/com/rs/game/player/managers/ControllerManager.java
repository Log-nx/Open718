package com.rs.game.player.managers;

import java.io.Serializable;

import com.rs.Settings;
import com.rs.game.Entity;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.godwars2.Helwyr;
import com.rs.game.player.Player;
import com.rs.game.player.actions.cooking.Foods.Food;
import com.rs.game.player.content.items.Potions.Pot;
import com.rs.game.player.controllers.Controller;
import com.rs.game.player.controllers.ControllerHandler;

public final class ControllerManager implements Serializable {

	private static final long serialVersionUID = 2084691334731830796L;

	private transient Player player;
	private transient Controller controller;
	private transient boolean inited;
	private Object[] lastControllerArguments;

	private String lastController;

	public ControllerManager() {
		lastController = Settings.START_CONTROLER;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Controller getController() {
		return controller;
	}
	
	public void startController(Object key, Object... parameters) {
		if (controller != null)
			forceStop();
		controller = (Controller) (key instanceof Controller ? key : ControllerHandler.getController(key));
		if (controller == null)
			return;
		controller.setPlayer(player);
		lastControllerArguments = parameters;
		lastController = (String) key;
		controller.start();
		inited = true;
	}

	public void login() {
		if (lastController == null)
			return;
		controller = ControllerHandler.getController(lastController);
		if (controller == null) {
			forceStop();
			return;
		}
		controller.setPlayer(player);
		if (controller.login())
			forceStop();
		else
			inited = true;
	}

	public void logout() {
		if (controller == null)
			return;
		if (controller.logout())
			forceStop();
	}

	public boolean canMove(int dir) {
		return controller == null || !inited || controller.canMove(dir);
	}

	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if (controller == null || !inited)
			return true;
		return controller.checkWalkStep(lastX, lastY, nextX, nextY);
	}

	public boolean keepCombating(Entity target) {
		if (controller == null || !inited)
			return true;
		return controller.keepCombating(target);
	}

	public boolean canEquip(int slotId, int itemId) {
		if (controller == null || !inited)
			return true;
		return controller.canEquip(slotId, itemId);
	}

	public boolean canAddInventoryItem(int itemId, int amount) {
		if (controller == null || !inited)
			return true;
		return controller.canAddInventoryItem(itemId, amount);
	}

	public void trackXP(int skillId, int addedXp) {
		if (controller == null || !inited)
			return;
		controller.trackXP(skillId, addedXp);
	}

	public boolean canDeleteInventoryItem(int itemId, int amount) {
		if (controller == null || !inited)
			return true;
		return controller.canDeleteInventoryItem(itemId, amount);
	}

	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		if (controller == null || !inited)
			return true;
		return controller.canUseItemOnItem(itemUsed, usedWith);
	}

	public boolean canAttack(Entity entity) {
		if (controller == null || !inited)
			return true;
		return controller.canAttack(entity);
	}

	public boolean canPlayerOption1(Player target) {
		if (controller == null || !inited)
			return true;
		return controller.canPlayerOption1(target);
	}

	public boolean canHit(Entity entity) {
		if (controller == null || !inited)
			return true;
		return controller.canHit(entity);
	}

	public void moved() {
		if (controller == null || !inited)
			return;
		controller.moved();
	}

	public void magicTeleported(int type) {
		if (controller == null || !inited)
			return;
		controller.magicTeleported(type);
	}

	public void sendInterfaces() {
		if (controller == null || !inited)
			return;
		controller.sendInterfaces();
	}

	public void process() {
		if (controller == null || !inited)
			return;
		controller.process();
	}

	public boolean sendDeath() {
		if (controller == null || !inited)
			return true;
		return controller.sendDeath();
	}

	public boolean canEat(Food food) {
		if (controller == null || !inited)
			return true;
		return controller.canEat(food);
	}

	public boolean canPot(Pot pot) {
		if (controller == null || !inited)
			return true;
		return controller.canPot(pot);
	}

	public boolean useDialogueScript(Object key) {
		if (controller == null || !inited)
			return true;
		return controller.useDialogueScript(key);
	}

	public boolean processMagicTeleport(WorldTile toTile) {
		if (controller == null || !inited)
			return true;
		return controller.processMagicTeleport(toTile);
	}
	
	public boolean processSwitchComponent(int toInterfaceId, int toComponentId, int toSlotId, int fromInterfaceId, int fromComponentId, int fromSlotId) {
		return controller == null || !inited || controller.processSwitchComponent( toInterfaceId, toComponentId, toSlotId, fromInterfaceId, fromComponentId, fromSlotId );
	}

	public boolean processItemTeleport(WorldTile toTile) {
		if (controller == null || !inited)
			return true;
		return controller.processItemTeleport(toTile);
	}

	public boolean processObjectTeleport(WorldTile toTile) {
		if (controller == null || !inited)
			return true;
		return controller.processObjectTeleport(toTile);
	}

	public boolean processObjectClick1(WorldObject object) {
		return controller == null || !inited || controller.processObjectClick1(object);
	}
	
	public boolean processObjectClick1(WorldObject object, Item item) {
		if (controller == null || !inited)
			return true;
		return controller.processObjectClick1(object, item);
	}

	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (controller == null || !inited)
			return true;
		return controller.processButtonClick(interfaceId, componentId, slotId,
				packetId);
	}

	public boolean processNPCClick1(NPC npc) {
		if (controller == null || !inited)
			return true;
		return controller.processNPCClick1(npc);
	}
	
	public boolean canSummonFamiliar() {
		if (controller == null || !inited)
			return true;
		return controller.canSummonFamiliar();
	}

	public boolean processNPCClick2(NPC npc) {
		if (controller == null || !inited)
			return true;
		return controller.processNPCClick2(npc);
	}
	public boolean processNPCClick3(NPC npc) {
		if (controller == null || !inited)
			return true;
		return controller.processNPCClick3(npc);
	}
	public boolean processNPCClick4(NPC npc) {
		if (controller == null || !inited)
			return true;
		return controller.processNPCClick4(npc);
	}
	public boolean processObjectClick2(WorldObject object) {
		if (controller == null || !inited)
			return true;
		return controller.processObjectClick2(object);
	}

	public boolean processObjectClick3(WorldObject object) {
		if (controller == null || !inited)
			return true;
		return controller.processObjectClick3(object);
	}

	public boolean processItemOnNPC(NPC npc, Item item) {
		if (controller == null || !inited)
			return true;
		return controller.processItemOnNPC(npc, item);
	}
	
	public boolean canDropItem(Item item) {
		if (controller == null || !inited)
			return true;
		return controller.canDropItem(item);
	}

	public void forceStop() {
		if (controller != null) {
			controller.forceClose();
			controller = null;
		}
		lastControllerArguments = null;
		lastController = null;
		inited = false;
	}

	public void removeControllerWithoutCheck() {
		controller = null;
		lastControllerArguments = null;
		lastController = null;
		inited = false;
	}
	
	public String toString() {
		return lastController;
	}

	public Object[] getLastControllerArguments() {
		return lastControllerArguments;
	}

	public void setLastControllerArguments(Object[] lastcontrollerArguments) {
		this.lastControllerArguments = lastcontrollerArguments;
	}

	public boolean processObjectClick4(WorldObject object) {
		return true; //unused atm
	}
	
	public boolean processObjectClick5(WorldObject object) {
		if (controller == null || !inited)
			return true;
		return controller.processObjectClick5(object);
	}

    public boolean processItemOnPlayer(Player p2, Item item) {
	    return controller == null || !inited || controller.processItemOnPlayer( p2, item );
    }
    
    public boolean processItemOnPlayer(Player p2, Item item, int slot) {
	    return controller == null || !inited || controller.processItemOnPlayer( p2, item, slot );
    }
	
	public boolean processItemOnPlayer(Player usedOn, int itemId) {
		return controller == null || !inited || controller.processItemOnPlayer( usedOn, itemId );
	}

	public boolean processMoneyPouch() {
		if (controller == null || !inited)
			return true;
		return controller.processMoneyPouch();
	}

	public boolean addWalkStep(int lastX, int lastY, int nextX, int nextY) {
		return controller == null || !inited || controller.checkWalkStep(lastX, lastY, nextX, nextY);
	}

	public void processNPCDeath(NPC id) {
		if (controller == null || !inited)
			return;
		controller.processNPCDeath(id);
	}
}
