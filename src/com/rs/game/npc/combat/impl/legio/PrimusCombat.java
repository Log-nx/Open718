package com.rs.game.npc.combat.impl.legio;

import java.util.ArrayList;
import java.util.Random;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.Hit.HitLook;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public class PrimusCombat extends CombatScript{
	public int damageBuff;
	int Stage;

	@Override
	public Object[] getKeys() {
		return new Object[] { 17149 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if(npc.getPossibleTargets().size() != 0){ 
			for (final Entity t : possibleTargets) {
				if (t instanceof Player) {
					final Player p = (Player) t;
					if (npc.getHitpoints() > 4500) { // higher then 75% 
						  p.primusStage = 0;
					}
					if (npc.getHitpoints() <= 4500 && p.primusStage == 0) { // 75% 
						p.primusStage ++;
						damageBuff = 2;
					}
					if (npc.getHitpoints() <= 3000 && p.primusStage == 1) { // 50% 
						p.primusStage ++;
						damageBuff = 4;
					}
					if(npc.getHitpoints() <= 1500 && p.primusStage == 2) {  // 25% 
						damageBuff = 5;
					}
	                  WorldTasksManager.schedule(new WorldTask() {// NORMAL ATTACK
                		Random rn = new Random();
                		int max = 100;
                		int min = 75;
                		int range = max- min + 1;
                		int randomNum =  rn.nextInt(range) + min;
							private int gameTick;
	                        @Override
	                        public void run() {
	                        	gameTick ++;         	
	                        	if(gameTick == 1){
	                        		npc.setNextAnimation(new Animation(20277));
	                        		npc.setNextGraphics(new Graphics(3977));
	                        	}
	                        	if(gameTick == 2){
	                        		  World.sendProjectile(npc, t, 3978, 10, 6, 40, 5, 0, 0);
	                        		  p.applyHit(new Hit(p, randomNum, HitLook.MAGIC_DAMAGE, 0));
	
	                        	}
	                        }

	                    }, 0, 0);
	                  WorldTasksManager.schedule(new WorldTask() { // AOE SPELL
	                		int x;
	                		int y;;
	                  		int max = 600;
	                		int min = 100;
	                		Random rn = new Random();
	                		int range = max- min + 1 ;
	                		int randomNum =  rn.nextInt(range) + min;
							private int gameTick;
	                        @Override
	                        public void run() {
	                        	gameTick ++;
	                        	if (gameTick == 1){
	                        		npc.setNextAnimation(new Animation(20277));
	                        		x = p.getX();
	            					 y = p.getY();
	
	                        	}
	                        	if (gameTick == 4) {
	                        		World.sendGraphics(null, new Graphics(3974), new WorldTile(x, y, 1));
	                        	}                      
	                        	if (gameTick == 5) {
	                           if (target.getX() == x && target.getY() == y){
	                        	   p.applyHit(new Hit(p, randomNum, HitLook.MAGIC_DAMAGE, 0));
	                                }

	                            }
	                        }

	                    }, 0, 0);
				}
			}
		}
		
		return npc.getAttackSpeed();
	}
}