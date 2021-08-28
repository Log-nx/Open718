package com.rs.game.player.actions.mining;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Action;
import com.rs.game.player.actions.mining.Mining.RockDefinitions;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

public abstract class MiningBase extends Action {
	
	public static final int LEVEL_REQUIREMENT_OP = 771;
	
    public static void propect(final Player player, final String endMessage) {
    	propect(player, "You examine the rock for ores....", endMessage);
    }

    public static void propect(final Player player, String startMessage, final String endMessage) {
		player.getPackets().sendGameMessage(startMessage, true);
		player.lock(5);
		WorldTasksManager.schedule(new WorldTask() {
		    @Override
		    public void run() {
		    	player.getPackets().sendGameMessage(endMessage);
		    }
		}, 4);
    }

    protected int emoteId;

    protected int pickaxeTime;

    @Override
    public void stop(Player player) {
    	player.setNextAnimation(new Animation (-1));
    	setActionDelay(player, 3);
    }
    
    
    /**
     * Starts/sets the animation and graphics for mining.
     * @param player The player to handle.
     */
    public void setAnimationAndGFX(Player player) {
    	if (player.getPerkManager().hasPerk(PlayerPerks.CHI_BLAST_MINING)) {
			player.setNextAnimation(new Animation(17310));
			player.setNextGraphics(new Graphics(3304));
		}
    	else if (player.getPerkManager().hasPerk(PlayerPerks.BLAST_MINING)) {
			player.setNextAnimation(new Animation(17947));
			player.setNextGraphics(new Graphics(3918));
		} 
    	else if (player.getPerkManager().hasPerk(PlayerPerks.STRONG_ARM_MINING)) {
			player.setNextAnimation(new Animation(20284));
			player.setNextGraphics(new Graphics(3998));
		} 
    	else if (player.getPerkManager().hasPerk(PlayerPerks.HEADBUTT_MINING)) {
			player.setNextAnimation(new Animation(17083));
		} else {
	    	player.setNextAnimation(getMiningAnimation(player, getPickAxeDefinitions(player)));
		}
    }
    
    public enum PickAxeDefinitions {
		
		BRONZE_PICKAXE(1265, 32540),

		GILDED_BRONZE_PICKAXE(20780, 32542),

		CLAN_PICKAXE(21405, 32544),

		ODDLY_SHAPED_STICK(20820, 32546),

		IRON_PICKAXE(1267, 32548),

		IRON_PICKAXE_PLUS_ONE(45952, 32548),

		GILDED_IRON_PICKAXE(20781, 32550),

		STEEL_PICKAXE(45467, 32552),

		STEEL_PICKAXE_PLUS_ONE(47063, 32552),

		GILDED_STEEL_PICKAXE(20782, 32554),

		DWARVEN_ARMY_AXE(21340, 32556),

		MITHRIL_PICKAXE(45494, 32558),

		MITHRIL_PICKAXE_PLUS_ONE(46288, 32558),

		MITHRIL_PICKAXE_PLUS_TWO(46291, 32558),

		GILDED_MITHRIL_PICKAXE(20784, 32560),

		ADAMANT_PICKAXE(22407, 32562),

		ADAMANT_PICKAXE_PLUS_ONE(45069, 32562),

		ADAMANT_PICKAXE_PLUS_TWO(45072, 32562),

		GILDED_ADAMANT_PICKAXE(20783, 32564),

		RUNE_PICKAXE(45548, 32566),

		RUNE_PICKAXE_PLUS_ONE(46942, 32566),

		RUNE_PICKAXE_PLUS_TWO(46945, 32566),

		RUNE_PICKAXE_PLUS_THREE(46948, 32566),

		GILDED_RUNE_PICKAXE(20785, 32568),

		SACRED_CLAY_PICKAXE_1(14122, 32570),

		SACRED_CLAY_PICKAXE_2(14124, 32572),

		SACRED_CLAY_PICKAXE_3(14126, 32574),

		SACRED_CLAY_PICKAXE_4(14128, 32576),

		SACRED_CLAY_PICKAXE_5(14130, 32578),

		SACRED_CLAY_PICKAXE_WITH_XP(14107, 32580),

		VOLATILE_CLAY_PICKAXE_WITH_XP(14099, 32582),

		SACRED_CLAY_PICKAXE(29662, 32584),

		VOLATILE_CLAY_PICKAXE(29654, 32586),

		DRAGON_PICKAXE(15259, 32588),

		AUGMENTED_DRAGON_PICKAXE(36537, 32590),

		GILDED_DRAGON_PICKAXE(20786, 32592),

		IMCANDO_PICKAXE(29522, 32594),

		AUGMENTED_IMCANDO_PICKAXE(36541, 32596),

		ORIKALKUM_PICKAXE(46578, 32598),

		ORIKALKUM_PICKAXE_PLUS_ONE(46581, 32598),

		ORIKALKUM_PICKAXE_PLUS_TWO(46584, 32598),

		ORIKALKUM_PICKAXE_PLUS_THREE(46587, 32599),

		NECRONIUM_PICKAXE(46372, 32602),

		NECRONIUM_PICKAXE_PLUS_ONE(46374, 32602),

		NECRONIUM_PICKAXE_PLUS_TWO(46376, 32602),

		NECRONIUM_PICKAXE_PLUS_THREE(46378, 32602),

		NECRONIUM_PICKAXE_PLUS_FOUR(46380, 32603),

		BANE_PICKAXE(45154, 32606),

		BANE_PICKAXE_PLUS_ONE(45156, 32606),

		BANE_PICKAXE_PLUS_TWO(45158, 32606),

		BANE_PICKAXE_PLUS_THREE(45160, 32606),

		BANE_PICKAXE_PLUS_FOUR(45162, 32607),

		ELDER_RUNE_PICKAXE(45642, 32610),

		ELDER_RUNE_PICKAXE_PLUS_ONE(45644, 32610),

		ELDER_RUNE_PICKAXE_PLUS_TWO(45646, 32610),

		ELDER_RUNE_PICKAXE_PLUS_THREE(45648, 32610),

		ELDER_RUNE_PICKAXE_PLUS_FOUR(45650, 32610),

		ELDER_RUNE_PICKAXE_PLUS_FIVE(45652, 32611),

		CRYSTAL_PICKAXE(32646, 32614),

		AUGMENTED_CRYSTAL_PICKAXE(36539, 32616),

		PICKAXE_OF_EARTH_AND_SONG(44834, 32618),

		AUGMENTED_PICKAXE_OF_EARTH_AND_SONG(44835, 32620);

		int id;
		private int animationId;
		//int requiredLevel;

		private PickAxeDefinitions(int id, int animationId) {
			this.id = id;
			this.animationId = animationId;
			//this.requiredLevel = requiredLevel;
		}

		public int getAnimationId() {
			return animationId;
		}

		public int getRequiredLevel() {
			return ItemDefinitions.getItemDefinitions(id).getCSOpcode(LEVEL_REQUIREMENT_OP);
		}

	}

    public static PickAxeDefinitions getPickAxeDefinitions(Player player) {
        for (int i = PickAxeDefinitions.values().length - 1; i >= 0; i--) {
            PickAxeDefinitions defs = PickAxeDefinitions.values()[i];
            if (player.getSkills().getLevelForXp(Skills.MINING) >= defs.getRequiredLevel()  && (player.getInventory().containsItem(defs.id, 1) || player.getInventory().containsItemToolBelt(defs.id) || player.getEquipment().getWeaponId() == defs.id))
                return defs;
        }
        return null;
    }
	
	public static Animation getMiningAnimation(Player player, PickAxeDefinitions pickaxeDefs) {
		int emoteId = pickaxeDefs.getAnimationId();
		return new Animation(emoteId);
	}
}