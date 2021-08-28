package com.rs.game.player.content.pet;

import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

public class SkillingPetsManager {
	public enum TYPES {
		GATHER_AND_SUPPORT,
		COMBAT,
		PRODUCTION
	};

	public enum SkillingPet {

		DOJO(38075, 23176, Skills.AGILITY, TYPES.GATHER_AND_SUPPORT),
		YAGA(38077, 23178, Skills.CONSTRUCTION, TYPES.PRODUCTION),
		RAMSAY(38079, 23180, Skills.COOKING, TYPES.PRODUCTION),
		GEMI(38081, 23182, Skills.CRAFTING, TYPES.PRODUCTION),
		WILLOW(38083, 23184, Skills.DIVINATION, TYPES.PRODUCTION),
		GORDIE(38085, 23186, Skills.DUNGEONEERING, TYPES.PRODUCTION),
		BRAINS(38087, 23188, Skills.FARMING, TYPES.PRODUCTION),
		BERNIE(38089, 23190, Skills.FIREMAKING, TYPES.GATHER_AND_SUPPORT),
		BUBBLES(38091, 23192, Skills.FISHING, TYPES.GATHER_AND_SUPPORT),
		FLO(38093, 23194, Skills.FLETCHING, TYPES.PRODUCTION),
		HERBERT(38095, 23196, Skills.HERBLORE, TYPES.PRODUCTION),
		ACE(38097, 23198, Skills.HUNTER, TYPES.GATHER_AND_SUPPORT),
		MALCOLM(38099, 23200, Skills.INVENTION, TYPES.PRODUCTION),
		ROCKY(38101, 23202, Skills.MINING, TYPES.GATHER_AND_SUPPORT),
		RUE(38103, 23204, Skills.RUNECRAFTING, TYPES.PRODUCTION),
		CRABBE(38105, 23206, Skills.SLAYER, TYPES.GATHER_AND_SUPPORT),
		SMITHY(38107, 23208, Skills.SMITHING, TYPES.PRODUCTION),
		RALPH(38109, 23210, Skills.THIEVING, TYPES.GATHER_AND_SUPPORT),
		WOODY(38111, 23212, Skills.WOODCUTTING, TYPES.GATHER_AND_SUPPORT),
		/**
		 * combat skilling pets
		 */
		SIFU(41406, 25017, Skills.ATTACK, TYPES.COMBAT),
		KANGALI(41408, 25019, Skills.STRENGTH, TYPES.COMBAT),
		WALLACE(41410, 25021, Skills.DEFENCE, TYPES.COMBAT),
		MORTY(41412, 25023, Skills.HITPOINTS, TYPES.COMBAT),
		SPARKY(41414, 25025, Skills.RANGE, TYPES.COMBAT),
		GHOSTLY(41416, 25027, Skills.PRAYER, TYPES.GATHER_AND_SUPPORT),
		NEWTON(41418, 25029, Skills.MAGIC, TYPES.COMBAT),
		SHAMINI(41420, 25031, Skills.SUMMONING, TYPES.PRODUCTION);
		/**
		 * 
		 */
		private int item, npc, skill;
		@SuppressWarnings("unused")
		private TYPES type;

		SkillingPet(int itemId, int npcId, int skillId, TYPES type) {
			setItem(itemId);
			npc = npcId;
			setSkill(skillId);
			this.type = type;
		}

		public static SkillingPet forId(int skill) {
			for (SkillingPet pet : SkillingPet.values()) {
				if (pet.getSkill() == skill) {
					return pet;
				}
			}
			return null;
		}

		public static SkillingPet forNpcId(int npc) {
			for (SkillingPet pet : SkillingPet.values()) {
				if (pet.npc == npc) {
					return pet;
				}
			}
			return null;
		}

		/**
		 * @return the item
		 */
		public int getItem() {
			return item;
		}

		/**
		 * @param item
		 *            the item to set
		 */
		public void setItem(int item) {
			this.item = item;
		}

		/**
		 * @return the skill
		 */
		public int getSkill() {
			return skill;
		}

		/**
		 * @param skill
		 *            the skill to set
		 */
		public void setSkill(int skill) {
			this.skill = skill;
		}
	}

	public static void dropChance(int skill, Player player, double exp) {
		SkillingPet pet = SkillingPet.forId(skill);
		if (pet != null) {
			int mod = (int) (exp * player.getSkills().getLevel(pet.getSkill()) / 10000);
			double drop = Utils.random(12000);
			if (mod >= drop) {
				player.getBank().addItem(new Item(pet.getItem(), 1), true);
				player.sm("<col=00BD00>Congratulations! You have the " + Skills.SKILL_NAME[pet.getSkill()] + " pet ! It has been added to your bank!</col>");
				if (player.getRights() < 2)
				World.sendWorldMessage("<col=00BD00><img=5>News: " + player.getDisplayName() + "" + (player.isIronman() ? " (Iron Man) " : " ") + "has just the received the " + Skills.SKILL_NAME[pet.getSkill()] + " pet!" + "</col> ", false, false);
			}
		}
	}
}