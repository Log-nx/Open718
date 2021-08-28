package com.rs.game.player.content.custom;

import java.io.Serializable;

import com.rs.Settings;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.content.PlayerDesign;
import com.rs.utils.ShopsHandler;

public class Options implements Serializable {
 private static final long serialVersionUID = -1811952383990735882L;
 
 private transient Player player;
 public void setPlayer(Player player) {
  this.player = player;
 }
 
 private int stage;
 
 public void sendInterface() {
  player.getInterfaceManager().sendInterface(1312);
  sendStrings("Stores", "Teleports", "Commands", "Account", 
    "", "", "", "", "", "Close");
  stage = 0;
 }
 

	
 
 public void handle(int interfaceId, int componentId) {
  System.out.println("Options: "+interfaceId+", "+componentId+"");
  if (stage == 0) {
   if (componentId == OPTION_1) {
    sendStrings("General Store", "Melee Equipment Store", "Melee Weapon Store", "Ranged Equipment Store", "Ranged Ammo Store", "Magic Equipment Store","Magic Runes Store","Skilling Supplies", "Back");
    stage = 1;
   }
   if (stage == 0) {
   if (componentId == OPTION_2) {
	   sendStrings("Monster Teleports", "Dungeon Teleports", "Skilling Teleports", "City Teleports", "Pker's Area", "Boss Teleports", "", "" ,"Back");
	   stage = 2;
   }
   if (componentId == OPTION_3) {
   }
   if (componentId == OPTION_4) {
	    sendStrings("Character Customization", "Homepage", "Community Forums", "Vote4Rewards", "Upgrade Your Account", "Donation Credit Store","","", "Back");
	    stage = 6;
   }
   if (componentId == OPTION_5) {
  }
   }
  } else if (stage == 1) {
   if (componentId == OPTION_1) {
	 	ShopsHandler.openShop(player, 1); 
   }
   if (componentId == OPTION_2) {
	   if (player.isIronman()) {
			player.getDialogueManager().startDialogue(
			"IronMan");
			return;
			}
	   ShopsHandler.openShop(player, 6); 
   }
   if (componentId == OPTION_3) {
	   if (player.isIronman()) {
			player.getDialogueManager().startDialogue(
			"IronMan");
			return;
			}
	   ShopsHandler.openShop(player, 7); 
   }
   if (componentId == OPTION_4) {
	   if (player.isIronman()) {
			player.getDialogueManager().startDialogue(
			"IronMan");
			return;
			}
	   ShopsHandler.openShop(player, 4); 
   }
   if (componentId == OPTION_5) {
	   if (player.isIronman()) {
			player.getDialogueManager().startDialogue(
			"IronMan");
			return;
			}
	   ShopsHandler.openShop(player, 5); 
   }
   if (componentId == OPTION_6) {
	   if (player.isIronman()) {
			player.getDialogueManager().startDialogue(
			"IronMan");
			return;
			}
	   ShopsHandler.openShop(player, 3); 
   }
   if (componentId == OPTION_7) {
	   if (player.isIronman()) {
			player.getDialogueManager().startDialogue(
			"IronMan");
			return;
			}
	   ShopsHandler.openShop(player, 25);
   }
   if (componentId == OPTION_8) {
	   //player.getDialogueManager().startDialogue("SkillingSupplies");
	   sendStrings("Skilling Outfits", "Tools", "Hoods & Capes", "Crafting Supplies", "Hunter Supplies", "Herblore Supplies","Woodcutting & Mining Supplies","Next", "Back");
  	    stage = 11;
   }
   if (componentId == OPTION_9) {
	   sendStrings("Stores", "Teleports", "Commands", "Account", 
			    "", "", "", "", "", "Close");
	   stage = 0;
	   
   }
   
  }
  else if (stage == 2) {
	  if (componentId == OPTION_1) {
		   sendStrings("Rock Crabs", "Yaks", "Monks", "Alkharid Warriors", "Bandits", "Tormented Demons", "Kalphite Queen", "Barrelchest" ,"Back");
		   stage =3;
	  }
	  if (componentId == OPTION_2) {
		   sendStrings("Taverley dungeon", "Brimhaven dungeon", "Kuradal's Dungeon", "Ancient cavern", "Forinthry Dungeon", "Ape Atoll Dungeon", "Grotworm Dungeon", "Slayer Tower" ,"Back");
		   stage = 4; 
	  }
	  if (componentId == OPTION_3) {
		  sendStrings("Woodcutting", "Fishing", "Mining", "Agility", "Hunter", "Summoning", "Construction", "Next" ,"Back");
		  stage =5;
	  }
	  if (componentId == OPTION_4) {
		  player.getInterfaceManager().sendInterface(1092); //Cities
	  }
	  if (componentId == OPTION_5) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2539, 4715, 0));
		  player.sm("You're being teleported to Pker's area.");
		  player.sm("Don't worry, you're in the safe area, pull the lever for worries!");
		 // sendStrings("East Dragons", "Mage Bank", "Wilderness (Level 50)", "New Gates (Level 47)", " ", " ", " ", " ", "Back");
		  //stage =8;
	  }
	  if (componentId == OPTION_6) {
		  sendStrings("Godwars Dungeon", "Corporal beast", "King Black Dragon", "Queen Black Dragon", "Wildy Wyrm", "Daganoth Kings", "Glacors", "Next", "Back");
		  stage =7; 
	  
		  //player.getDialogueManager().startDialogue("BossTeleports");
		  }
	  if (componentId == OPTION_7) {
		 /* sendStrings("Bandos", "Armadyl", "Saradomin", "Zamorak", "Nex", "", "", "", "Back");
		  stage =8; */
	  
		  }
	  if (componentId == OPTION_8) {
		
	  }
	  if (componentId == OPTION_9) {
		  sendStrings("Stores", "Teleports", "Commands", "Account", 
				    "", "", "", "", "", "Close");
		   stage = 0;
	  }
  }
  
  else if (stage == 3 ) {
	  if (componentId == OPTION_1) {
			Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2674, 3705, 0));
	  }
	  if (componentId == OPTION_2) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2326, 3800, 0));
	  }
	  if (componentId == OPTION_3) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3051, 3491, 0)); 
	  }
	  if (componentId == OPTION_4) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3293, 3173, 0)); 
	  }
	  if (componentId == OPTION_5) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3226, 3108, 0));
	  }
	  if (componentId == OPTION_6) {//Torm. Demons
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2572, 5736, 0));
	  }
	  if (componentId == OPTION_7) {//Kalphite Queen
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3226, 3108, 0));
	  }
	  if (componentId == OPTION_8) {//barrelchest
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3803, 2844, 0));
			
	  }
	  if (componentId == OPTION_9) {
		  sendStrings("Monster Teleports", "Dungeon Teleports", "Skilling Teleports", "City Teleports", "Pker's Area", "Boss Teleports", "", "" ,"Back");
		   stage = 2;
	  }
  }
  
  else if (stage == 4) {
	  if(componentId == OPTION_1) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2885, 9797, 0));
	  }
	  if (componentId == OPTION_2) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2709, 9464, 0));
	  }
	  if (componentId == OPTION_3) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(1735, 5313, 1));
	  }
	  if (componentId == OPTION_4) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(1768, 5366, 1));
	  }
	  if(componentId == OPTION_5){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3080, 10057, 0));
	  }
	  if(componentId == OPTION_6){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2805, 9142, 0));
	  }
	  if(componentId == OPTION_7){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(1205, 6371, 0));
	  }
	  if(componentId == OPTION_8){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3429, 3534, 0));
	  }
	  if (componentId == OPTION_9) {
		  sendStrings("Monster Teleports", "Dungeon Teleports", "Skilling Teleports", "City Teleports", "Pker's Area", "Boss Teleports", "", "" ,"Back");
		   stage = 2;
	  }
  }
  
  else if (stage ==5) {
	  if(componentId == OPTION_1){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2725, 3485, 0)); 
	  }
	  if(componentId == OPTION_2){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2596, 3420, 0));
		  player.sm("");
	  }
	  if(componentId == OPTION_3){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3055, 9776, 0));
	  }
	  if(componentId == OPTION_4){
		  player.getDialogueManager().startDialogue("AgilityTeleport");
	  }
	  if(componentId == OPTION_5){
		  player.getDialogueManager().startDialogue("HunterTeleport");
	  }
	  if(componentId == OPTION_6){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2923, 3449, 0));
	  }
	  if(componentId == OPTION_7){
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2980, 3373, 0));
	  }
	  if(componentId == OPTION_8){
			sendStrings("Runecrafting", "Farming", "", "", "", "", "", "" ,"Back");
	  stage =10;
  }
	  	  if (componentId == OPTION_9) {
	  		sendStrings("Monster Teleports", "Dungeon Teleports", "Skilling Teleports", "City Teleports", "Pker's Area", "Boss Teleports", "", "" ,"Back");
			   stage = 2;
	  }
  }
  //dvfw
  else if (stage ==6) {
	  if (componentId == OPTION_1) {
		  if (player.getEquipment().wearingArmour()) {
				player.getPackets().sendGameMessage("Please remove all of your equipment(s) first.");
				end();
			} else {
		  PlayerDesign.open(player);
	  }}
	  if (componentId == OPTION_2) {
			//player.getPackets().sendOpenURL(Settings.WEBSITE_LINK);
			
		}
		if (componentId == OPTION_3) {	
			player.getPackets().sendOpenURL(Settings.FORUMS_LINK);
			
			}
		if (componentId == OPTION_4) {
			player.getPackets().sendOpenURL(Settings.VOTE_LINK);
			
		}
		if (componentId == OPTION_5) {
			player.getPackets().sendOpenURL(Settings.DONATE_LINK);
			
		}
		if (componentId == OPTION_6) {
					ShopsHandler.openShop(player, 31); 
		}
		if (componentId == OPTION_9) {
		  sendStrings("Stores", "Teleports", "Commands", "Account", 
				    "", "", "", "", "", "Close");
				  stage = 0;
	  }
	  //		  sendStrings("Bandos", "Saradomin", "Armadyl", "Zamorak", "Nex", "Corporal beast", "King Black Dragon", "Queen Black Dragon" ,"Back");
  }
  else if (stage ==7) {
	  if (componentId == OPTION_1) {
		  sendStrings("Bandos", "Armadyl", "Saradomin", "Zamorak", "Nex", "", "", "", "Back");
		  stage =8;
	  }
	  if (componentId == OPTION_2) {
			Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2966, 4383, 2));//corp
	  }
	  if (componentId == OPTION_3) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3067, 10255, 0)); //kbd
	  }
	  if (componentId == OPTION_4) {
			end();
			if (player.getSkills().getLevelForXp(Skills.SUMMONING) < 60) { //qbd
				player.getPackets().sendGameMessage("You need a summoning level of 60.");
				return;
			}
			player.getControllerManager().startController("QueenBlackDragonControler");
		
	  }
	  
	  if (componentId == OPTION_5) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3170, 3872, 0)); //wildy
			
	  }
	  if (componentId == OPTION_6) {
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2899, 4449, 0));
	  }
	  if (componentId == OPTION_7) {
		 
		  Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(4184, 5732, 0));
	  }
	  if (componentId == OPTION_8) {
		 
		  sendStrings("Vorago", "Mercenary Mage", "Sunfreet", "", "", "", "", "" ,"Back");
		   stage = 9;
	  }
	  if (componentId == OPTION_9) {
		  sendStrings("Monster Teleports", "Dungeon Teleports", "Skilling Teleports", "City Teleports", "Pker's Area", "Boss Teleports", "", "" ,"Back");
		   stage = 2;
	  }}
	  else if (stage ==8) { 
		  if (componentId == OPTION_1) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2859, 5357, 0)); //bandos
				end();
			}
			if (componentId == OPTION_2) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2835, 5291, 0)); //Arma
				end();
			}
		    if (componentId == OPTION_3) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2923, 5261, 0)); //sara
				end();
			}
			if (componentId == OPTION_4) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2925, 5336, 0)); //zammy
				end();
			}
			if (componentId == OPTION_5) {
				Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2905, 5203, 0)); //nex
				end();
			}
		  		if (componentId == OPTION_9) {
		  			sendStrings("Godwars Dungeon", "Corporal beast", "King Black Dragon", "Queen Black Dragon", "Wildy Wyrm", "Dagganoth Kings", "Glacors", "Next", "Back");
		  		  stage =7; 
		  	}
	  }
	  else if (stage ==9) { 
		  if (componentId == OPTION_1) {
			  player.getControllerManager().startController("VoragoController");
				end();
			}
			if (componentId == OPTION_2) {
				if (player.getSkills().getCombatLevelWithSummoning() < 126) {
					end();
					player.sm("You must have a combat level of 126 to access.");
					return;
				}
				player.getControllerManager().startController("MercArena");
				end();
			}
			
		    if (componentId == OPTION_3) {
		    	Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2853, 9647, 0));
		    }
		  		if (componentId == OPTION_9) {
		  			sendStrings("Godwars Dungeon", "Corporal beast", "King Black Dragon", "Queen Black Dragon", "Wildy Wyrm", "Dagganoth Kings", "Glacors", "Next", "Back");
		  		  stage =7; 
  }
 }else if (stage ==10) { 
	  if (componentId == OPTION_1) {
	    	Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3107, 3163, 1));
	    }
	  if (componentId == OPTION_2) {
		  sendStrings("Falador Farming Patch", "Falador Tree Patch", "Catherby Farming Patch", "Canifis Farming Patch", "", "", "", "", "Back");
			  stage =14;
	  }
	  		if (componentId == OPTION_9) {
	  			 sendStrings("Woodcutting", "Fishing", "Mining", "Agility", "Hunter", "Summoning", "Construction", "Next" ,"Back");
	  			  stage =5;
}
 }else if (stage ==11) { 
	 if (componentId == OPTION_1) {
		 if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
		 ShopsHandler.openShop(player, 10); 
		  
	}
	if (componentId == OPTION_2) {
		 if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
		 ShopsHandler.openShop(player, 9);
		  
	}
	if (componentId == OPTION_3) {
		sendStrings("Hoods",
				"Capes",
				"Achievements Capes",
				"Master Capes",
				"",
				"",
				"",
				"",
				"Back");
		stage = 12;
	}
	if (componentId == OPTION_4) {
		 if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
		ShopsHandler.openShop(player, 11); 
		  
	}
	if (componentId == OPTION_5) {
		 if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
	   	 ShopsHandler.openShop(player, 12); 
	   	  
	   }
	   if (componentId == OPTION_6) {
		   if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
	   	player.getDialogueManager().startDialogue("JatixShop");
	   }
	   if (componentId == OPTION_7) {
		   if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
	   	 ShopsHandler.openShop(player, 14); 
	   	  
	   }
	   if (componentId == OPTION_8) {
	   	sendStrings("Fishing Supplies", "Potion & Food ", "Farming Supplies", "", "", "","","", "Back");
			    stage = 13;
	}
	 		 if (componentId == OPTION_9) {
	 		   
	 		  sendStrings("General Store", "Melee Equipment Store", "Melee Weapon Store", "Ranged Equipment Store", "Ranged Ammo Store", "Magic Equipment Store","Magic Runes Store","Skilling Supplies", "Back");
	 	    stage = 1;
	 	  }
	} else if (stage == 12) {
	   if (componentId == OPTION_1) {
	   	 ShopsHandler.openShop(player, 17); 
	   	  
	   }
	   if (componentId == OPTION_2) {
	   	 ShopsHandler.openShop(player, 16);
	   	  
	   }
	   if (componentId == OPTION_3) {
	   	 ShopsHandler.openShop(player, 18);
	   	  
	   }
	   if (componentId == OPTION_4) {
	   	 ShopsHandler.openShop(player, 19);
	   	  
	   }
	   if (componentId == OPTION_9) {
	   	   sendStrings("Skilling Outfits", "Tools", "Hoods & Capes", "Crafting Supplies", "Hunter Supplies", "Herblore Supplies","Woodcutting & Mining Supplies","Next", "Back");
	  	    stage = 11;
	    
	   }
	} else if (stage == 13) {
	   if (componentId == OPTION_1) {
		   if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
	   	 ShopsHandler.openShop(player, 15); 
	   	  
	   }
	   if (componentId == OPTION_2) {
		   if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}
	   	 ShopsHandler.openShop(player, 29);
	   	  
	   }
	   if (componentId == OPTION_3) {
		  /* if (player.isIronman()) {
				player.getDialogueManager().startDialogue(
				"IronMan");
				return;
				}*/
	   	 ShopsHandler.openShop(player, 32);
	   	  
	   }
	   if (componentId == OPTION_9) {
	   	   sendStrings("Skilling Outfits", "Tools", "Hoods & Capes", "Crafting Supplies", "Hunter Supplies", "Herblore Supplies","Woodcutting & Mining Supplies", "Next", "Back");
	  	    stage = 11;
	    
	   }
	} else if (stage == 14) {
		if (componentId == OPTION_1) {
			 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3053, 3305, 0));
			end();
		} if (componentId == OPTION_2) {
			 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3000, 3375, 0));
			end();
		} if (componentId == OPTION_3) {
			 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2817, 3461, 0));
			end();
		} if (componentId == OPTION_4) {
			 Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(3602, 3531, 0));
			end();
		   
		   }
		   if (componentId == OPTION_9) {
			   sendStrings("Runecrafting", "Farming", "", "", "", "", "", "" ,"Back");
				  stage =10;
		    
		   }}}

	public void end() {
	player.closeInterfaces();
	}

	public void sendStrings(String... options) {
	player.getPackets().sendHideIComponent(1312, 26, true);
	if (options.length == 0) {
	return;
	}

	for (int i = 0; i < comps.length; i++) {
	if (i > options.length - 1) { 
	 player.getPackets().sendHideIComponent(1312, comps[i], true);
	 continue;
	}

	player.getPackets().sendIComponentText(1312, comps[i], options[i]);
	}
	}

	private void teleportPlayer(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControllerManager().startController("GodWars");
	}
 
 private static final int[] comps = { 38, 46, 54, 62, 70, 78, 86, 94, 102 };
 private static final int[]  btns = { 35, 43, 51, 59, 67, 75, 83, 91, 99 };
 
 private static final int OPTION_1 = 35;
 private static final int OPTION_2 = 43;
 private static final int OPTION_3 = 51;
 private static final int OPTION_4 = 59;
 private static final int OPTION_5 = 67;
 private static final int OPTION_6 = 75;
 private static final int OPTION_7 = 83;
 private static final int OPTION_8 = 91;
 private static final int OPTION_9 = 99;
 
}