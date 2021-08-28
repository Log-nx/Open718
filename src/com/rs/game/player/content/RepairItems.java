package com.rs.game.player.content;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class RepairItems {
	
	/*
	 * Author @Tatsuya 
	 */

	public enum BrokenItems {
		
		/*
		 * Ancient Warrior Armor/Weapons
		 */
		
		MORRIGAN_TOP(-1, 13872, 13870, 5000000),

		MORRIGAN_BOTTOM(-1, 13875, 13873, 5000000),

		MORRIGAN_COIF(-1, 13878, 13876, 5000000),

		STATIUS_BODY(-1, 13886, 13884, 5000000),

		STATIUS_LEGS(-1, 13892, 13890, 5000000),

		STATIUS_HELM(-1, 13896, 13898, 5000000),

		STATIUS_HAMMER(-1, 13900, 13902, 5000000),

		VESTA_BODY(-1, 13889, 13887, 5000000),

		VESTA_SKIRT(-1, 13895, 13893, 5000000),

		VESTA_LONG(-1, 13901, 13899, 5000000),

		VESTA_SPEAR(-1, 13907, 13905, 5000000),

		ZURIEL_TOP(-1, 13860, 13858, 5000000),

		ZURIEL_BOTTOM(-1, 13863, 13861, 5000000),

		ZURIEL_HOOD(-1, 13866, 13854, 5000000),

		ZURIEL_STAFF(-1, 13869, 13857, 5000000),
		
		/*
		 * Barrows Armor/Weapons
		 */

		GUTHANS_HELM(4908, -1, 4724, 1000000),

		GUTHANS_PLATEBODY(4920, -1, 4738, 1000000),

		GUTHANS_PLATESKIRT(4926, -1, 4730, 1000000),

		GUTHANS_WARSPEAR(4914, -1, 4726, 1000000),

		AHRIMS_HOOD(4860, -1, 4708, 1000000),

		AHRIMS_STAFF(4866, -1, 4710, 1000000),

		AHRIMS_ROBE_TOP(4872, -1, 4712, 1000000),

		AHRIMS_ROBE_SKIRT(4878, -1, 4714, 1000000),

		DHAROKS_HELM(4884, -1, 4716, 1000000),

		DHAROKS_GREATAXE(4890, -1, 4718, 1000000),

		DHAROKS_PLATEBODY(4896, -1, 4720, 1000000),

		DHAROKS_PLATELEGS(4902, -1, 4722, 1000000),

		KARILS_COIF(4932, -1, 4732, 1000000),

		KARILS_CROSSBOW(4938, -1, 4734, 1000000),

		KARILS_TOP(4944, -1, 4736, 1000000),

		KARILS_SKIRT(4950, -1, 4738, 1000000),

		TORAGS_HELM(4956, -1, 4745, 1000000),

		TORAGS_HAMMER(4962, -1, 4747, 1000000),

		TORAGS_PLATEBODY(4968, -1, 4749, 1000000),

		TORAGS_PLATELEGS(4974, -1, 4751, 1000000),

		VERACS_HELM(4980, -1, 4753, 1000000),

		VERACS_FLAIL(4968, -1, 4755, 1000000),

		VERACS_BRASSARD(4992, -1, 4757, 1000000),

		VERACS_PLATESKIRT(4998, -1, 4759, 1000000),
		
		/*
		 * Nex Armor/Weapons
		 */

		TORVA_HELM(20138, 20137, 20135, 5000000),

		TORVA_PLATE(20142, 20141, 20139, 5000000),

		TORVA_LEGS(20146, 20145, 20143, 5000000),

		TORVA_GLOVES(24979, 20178, 24977, 5000000),

		TORVA_BOOTS(24985, 20184, 24983, 5000000),

		PERNIX_COWL(20150, 20149, 20147, 5000000),

		PERNIX_BODY(20154, 20153, 20151, 5000000),

		PERNIX_CHAPS(20158, 20157, 20155, 5000000),

		PERNIX_GLOVES(24976, 24975, 24974, 5000000),

		PERNIX_BOOTS(24991, 24990, 24989, 5000000),

		ZARYTE_BOW(20174, 20173, 20171, 7500000),

		VIRTUS_MASK(20162, 20161, 20159, 5000000),

		VIRTUS_TOP(20166, 20165, 20163, 5000000),

		VIRTUS_LEGS(20170, 20169, 20167, 5000000),

		VIRTUS_GLOVES(24982, 20181, 24980, 5000000),

		VIRTUS_BOOTS(24988, 20187, 24986, 5000000),
		
		/*
		 * Nightmare Armor
		 */
		
		NIGHTMARE_GAUNTLETS(39251, 39250, 39248, 5000000),
		
		/*
		 * Khopesh's
		 */
		
		KHOPESH_OF_THE_KHARDIAN(40315, 40314, 40312, 2200000),
		
		KHOPESH_OF_THE_KHARDIAN_OFFHAND(40319, 40318, 40316, 2200000),
		
		/*
		 * Defenders
		 */
		
		CORRUPTED_DEFENDER(36155, 36154, 36153, 100000),
		
		KALPHITE_DEFENDER(36162, 36163, 36164, 1500000),
		
		/*
		 * Noxious Scythe
		 */
		
		NOXIOUS_SCYTHE(31728, 31727, 31725, 4500000),
		
		NOXIOUS_SCYTHE_BARROWS(33332, 33331, 33330, 4500000),
		
		NOXIOUS_SCYTHE_SHADOW(33398, 33397, 33396, 4500000),
		
		NOXIOUS_SCYTHE_BLOOD(33464, 33463, 33462, 4500000),
		
		NOXIOUS_SCYTHE_THIRD_AGE(36333, 36334, 36335, 4500000),
		
		/*
		 * Noxious Staff
		 */
		
		NOXIOUS_STAFF(31732, 31731, 31729, 4500000),
		
		NOXIOUS_STAFF_BARROWS(33335, 33334, 33333, 4500000),
		
		NOXIOUS_STAFF_SHADOW(33399, 33400, 33401, 4500000),
		
		NOXIOUS_STAFF_THIRD_AGE(33465, 33466, 33467, 4500000),
		
		NOXIOUS_STAFF_BLOOD(36338, 36337, 36336, 4500000),
		
		/*
		 * Noxious Longbow
		 */
		
		NOXIOUS_LONGBOW(31736, 31735, 31733, 4500000),
		
		NOXIOUS_LONGBOW_BARROWS(33338, 33337, 33336, 4500000),
		
		NOXIOUS_LONGBOW_SHADOW(33404, 33403, 33402, 4500000),
		
		NOXIOUS_LONGBOW_THIRD_AGE(33470, 33469, 33468, 4500000),
		
		NOXIOUS_LONGBOW_BLOOD(36341, 36340, 36339, 4500000),
		
		/*
		 * Seismic Wand
		 */
		
		SEISMIC_WAND(28620, 28619, 28617, 3000000),
		
		SEISMIC_WAND_BARROWS(33326, 33325, 33324, 3000000),
		
		SEISMIC_WAND_SHADOW(33392, 33391, 33390, 3000000),
		
		SEISMIC_WAND_THIRD_AGE(33458, 33457, 33456, 3000000),
		
		SEISMIC_WAND_BLOOD(36339, 36340, 36341, 3000000),
		
		/*
		 * Seismic Singularity
		 */
		
		SEISMIC_SINGULARITY(28621, 28623, 28624, 1500000),
		
		SEISMIC_SINGULARITY_BARROWS(33327, 33328, 33326, 1500000),
		
		SEISMIC_SINGULARITY_SHADOW(33395, 33396, 33397, 1500000),
		
		SEISMIC_SINGULARITY_THIRD_AGE(33459, 33460, 33461, 1500000),
		
		SEISMIC_SINGULARITY_BLOOD(36330, 36331, 36332, 1500000),
		
		/*
		 * Drygores
		 */
		
		DRYGORE_RAPIER(26582, 26581, 26579, 3000000),
		
		DRYGORE_RAPIER_OFFHAND(26586, 26585, 26583, 3000000),
		
		DRYGORE_RAPIER_BARROWS(33308, 33307, 33306, 3000000),
		
		DRYGORE_RAPIER_OFFHAND_BARROWS(33311, 33310, 33309, 3000000),
		
		DRYGORE_RAPIER_SHADOW(33374, 33373, 33372, 3000000),
		
		DRYGORE_RAPIER_OFFHAND_SHADOW(33377, 33376, 33375, 3000000),
		
		DRYGORE_RAPIER_THIRD_AGE(33440, 33439, 33438, 3000000),
		
		DRYGORE_RAPIER_OFFHAND_THIRD_AGE(33443, 33442, 33441, 3000000),
		
		DRYGORE_RAPIER_BLOOD(36311, 363110, 36309, 3000000),
		
		DRYGORE_RAPIER_OFFHAND_BLOOD(36314, 36313, 36312, 3000000),
		
		DRYGORE_LONGSWORD(26590, 26589, 26587, 3000000),
		
		DRYGORE_LONGSWORD_OFFHAND(26594, 26593, 26591, 3000000),
		
		DRYGORE_LONGSWORD_BARROWS(33314, 33313, 33312, 3000000),
		
		DRYGORE_LONGSWORD_OFFHAND_BARROWS(33317, 33316, 33315, 3000000),
		
		DRYGORE_LONGSWORD_SHADOW(33380, 33379, 33378, 3000000),	
		
		DRYGORE_LONGSWORD_OFFHAND_SHADOW(33383, 33382, 33381, 3000000),	
		
		DRYGORE_LONGSWORD_THIRD_AGE(33446, 33445, 33444, 3000000),
		
		DRYGORE_LONGSWORD_OFFHAND_THIRD_AGE(33449, 33448, 33447, 3000000),
		
		DRYGORE_LONGSWORD_BLOOD(36317, 36316, 36315, 3000000),
		
		DRYGORE_LONGSWORD_OFFHAND_BLOOD(36320, 36319, 36318, 3000000),
		
		DRYGORE_MACE(26598, 26598, 26595, 3000000),
		
		DRYGORE_MACE_OFFHAND(26602, 26601, 26599, 3000000),
		
		DRYGORE_MACE_BARROWS(33302, 33301, 33300, 3000000),
		
		DRYGORE_MACE_OFFHAND_BARROWS(33305, 33304, 33303, 3000000),
		
		DRYGORE_MACE_SHADOW(33368, 33367, 33366, 3000000),
		
		DRYGORE_MACE_OFFHAND_SHADOW(33371, 33370, 33369, 3000000),
		
		DRYGORE_MACE_THIRD_AGE(33434, 33433, 33432, 3000000),
		
		DRYGORE_MACE_OFFHAND_THIRD_AGE(33437, 33436, 33435, 3000000),
		
		DRYGORE_MACE_BLOOD(36305, 36304, 36303, 3000000),
		
		DRYGORE_MACE_OFFHAND_BLOOD(36308, 36307, 36306, 3000000),
		
		/*
		 * Ascension Crossbow
		 */
		
		ASCENSION_CROSSBOW(28440, 28439, 28437, 4500000),
		
		ASCENSION_CROSSBOW_OFFHAND(28444, 28443, 28441, 4500000),
		
		ASCENSION_CROSSBOW_BARROWS(33320, 33319, 33318, 4500000),
		
		ASCENSION_CROSSBOW_OFFHAND_BARROWS(33323, 33322, 33321, 4500000),
		
		ASCENSION_CROSSBOW_SHADOW(33386, 33385, 33384, 4500000),
		
		ASCENSION_CROSSBOW_OFFHAND_SHADOW(33389, 33388, 33387, 4500000),
		
		ASCENSION_CROSSBOW_THIRD_AGE(33452, 33451, 33450, 4500000),
		
		ASCENSION_CROSSBOW_OFFHAND_THIRD_AGE(33455, 33454, 33453, 4500000),
		
		ASCENSION_CROSSBOW_BLOOD(36323, 36322, 36321, 4500000),
		
		ASCENSION_CROSSBOW_OFFHAND_BLOOD(36326, 36325, 36324, 4500000),
		
		/*
		 * Polypore
		 */
		
		FUNGAL_VISOR(22461, 22460, 22458, 10000),

		FUNGAL_PONCHO(22469, 22468, 22466, 50000),

		FUNGAL_LEGGINGS(22465, 22464, 22462, 40000),

		GRIFOLIC_VISOR(22473, 22472, 22470, 100000),

		GRIFOLIC_PONCHO(22481, 22480, 22478, 500000),

		GRIFOLIC_LEGGINGS(22477, 22476, 22474, 400000),
		
		/*
		 * Ports Armor/Weapons
		 */
		
		SUPERIOR_TETSU_HELM(26331, -1, 26322, 600000),
		
		SUPERIOR_TETSU_BODY(26332, -1, 26323, 2400000),
		
		SUPERIOR_TETSU_LEGS(26333, -1, 26324, 1200000),
		
		SUPERIOR_TETSU_KOTE(38815, -1, 38812, 300000),
		
		SUPERIOR_TETSU_KOGAKE(38809, -1, 38806, 300000),
		
		SUPERIOR_TETSU_KATANA(33881, 33880, 33879, 2500000),
		
		SUPERIOR_DEATH_LOTUS_HELM(26355, -1, 26352, 600000),
		
		SUPERIOR_DEATH_LOTUS_CHESTPLATE(26356, -1, 26353, 2400000),
		
		SUPERIOR_DEATH_LOTUS_CHAPS(26357, -1, 26354, 1200000),
		
		SUPERIOR_SEASINGERS_HOOD(26343, -1, 26334, 600000),
		
		SUPERIOR_SEASINGERS_ROBE_TOP(26344, -1, 26335, 2400000),
		
		SUPERIOR_SEASINGERS_ROBE_BOTTOM(26345, -1, 26336, 1200000),
		
		SUPERIOR_SEASINGERS_ASARI(38808, -1, 38805, 300000),
		
		SUPERIOR_SEASINGERS_AONORI(38814, -1, 38811, 300000),
		
		SUPERIOR_SEASINGER_KIBA(33888, 33887, 33886, 2500000),
		
		SUPERIOR_SEASINGER_MAKIGAI(33891, 33890, 33889, 2500000),
		
		SUPERIOR_REEFWALKER_CAPE(30573, 30572, 30571, 600000),
		
		SUPERIOR_LEVIATHAN_RING(30581, 30580, 30579, 600000),
		
		/*
		 * Mizuyari 
		 */
		
		MIZUYARI(37287, 37286, 37284, 3750000),
		
		/*
		 * Praesul
		 */
		
		WAND_OF_PRAESUL(39578, 39577, 39574, 3000000),
		
		/*
		 * Ripper Claws
		 */
		
		RIPPER_CLAW(36007, 36006, 36004, 2500000),
		
		OFFHAND_RIPPER_CLAW(36011, 36010, 36008, 2500000),
		
		/*
		 * Wyvern Crossbow
		 */
		
		WYVERN_CROSSBOW(35988, 35987, 35985, 2500000),
		
		/*
		 * Camel Staff
		 */
		
		CAMEL_STAFF(36022, 36021, 36019, 4125000),
		
		/*
		 * Zaros Godsword
		 */
		
		ZAROS_GODSWORD(37643, 37642, 37640, 4800000),
		
		/*
		 * Seren Godbow
		 */
		
		SEREN_GODBOW(37635, 37634, 37632, 4800000),
		
		/*
		 * Staff of Sliske
		 */
		
		STAFF_OF_SLISKE(37639, 37638, 37636, 4800000),
		
		/*
		 * Linza's Gear
		 */
		
		LINZA_HELM(37436, 37435, 37433, 500000),
		
		LINZA_CHEST(37440, 37439, 37437, 2000000),
		
		LINZA_LEGS(37444, 37443, 37441, 1000000),
		
		LINZA_HAMMER(37448, 37447, 37445, 1500000),
		
		LINZA_SHIELD(37452, 37451, 37449, 750000),
		
		/*
		 * Tempest Armor
		 */
		
		TEMPEST_COWL(35184, 35184, 35184, 2500000),
		
		TEMPEST_BODY(35187, 35187, 35187, 2500000),
		
		TEMPEST_LEGS(35188, 35188, 35188, 2500000),
		
		TEMPEST_GLOVES(35185, 35185, 35185, 2500000),
		
		TEMPEST_BOOTS(35186, 35186, 35186, 2500000),
		
		/*
		 * Achto Tempest Armor
		 */
		
		ACHTO_TEMPEST_COWL(35190, 35190, 35189, 2500000),
		
		ACHTO_TEMPEST_BODY(35196, 35196, 35195, 2500000),
		
		ACHTO_TEMPEST_LEGS(35198, 35198, 35197, 2500000),
		
		ACHTO_TEMPEST_GLOVES(35192, 35192, 35191, 2500000),
		
		ACHTO_TEMPEST_BOOTS(35194, 35194, 35193, 2500000),
		
		/*
		 * Teralith Armor
		 */
		
		TERALITH_HELM(35169, 35169, 35169, 2500000),
		
		TERALITH_CHEST(35172, 35172, 35172, 2500000),
		
		TERALITH_LEGS(35173, 35173, 35173, 2500000),
		
		TERALITH_GLOVES(35170, 35170, 35170, 2500000),
		
		TERALITH_BOOTS(35171, 35171, 35171, 2500000),
		
		/*
		 * Achto Teralith Armor
		 */
		
		ACHTO_TERALITH_HELM(35175, 35175, 35174, 2500000),
		
		ACHTO_TERALITH_CHEST(35181, 35181, 35180, 2500000),
		
		ACHTO_TERALITH_LEGS(35183, 35183, 35182, 2500000),
		
		ACHTO_TERALITH_GLOVES(35177, 35177, 35176, 2500000),
		
		ACHTO_TERALITH_BOOTS(35179, 35179, 35178, 2500000),
		
		/*
		 * Primeval Armor
		 */
		
		PRIMEVAL_HELM(35154, 35154, 35154, 1600000),
		
		PRIMEVAL_CHEST(35157, 35157, 35157, 3200000),
		
		PRIMEVAL_LEGS(35158, 35158, 35158, 2400000),
		
		PRIMEVAL_GLOVES(35155, 35155, 35155, 400000),
		
		PRIMEVAL_BOOTS(35156, 35156, 35156, 400000),
		
		/*
		 * Achto Primeval Armor
		 */
		
		ACHTO_PRIMEVAL_HELM(35160, 35160, 35159, 1600000),
		
		ACHTO_PRIMEVAL_CHEST(35166, 35166, 35165, 3200000),
		
		ACHTO_PRIMEVAL_LEGS(35168, 35168, 35167, 2400000),
		
		ACHTO_PRIMEVAL_GLOVES(35162, 35162, 35161, 400000),
		
		ACHTO_PRIMEVAL_BOOTS(35164, 35164, 35163, 400000),
		
		/*
		 * Dragon Rider Lance
		 */
		
		DRAGON_RIDER_LANCE(37074, 37073, 37070, 3750000),
		
		;

		private static Map<Integer, BrokenItems> BROKENITEMS = new HashMap<Integer, BrokenItems>();

		private static Map<Integer, BrokenItems> DEGRADEDITEMS = new HashMap<Integer, BrokenItems>();

		static {
			for (final BrokenItems brokenitems : BrokenItems.values()) {
				if (brokenitems.getBrokenId() != -1)
					BROKENITEMS.put(brokenitems.getBrokenId(), brokenitems);
				if (brokenitems.getDegradedId() != -1)
					DEGRADEDITEMS.put(brokenitems.getDegradedId(), brokenitems);
			}
		}

		public static BrokenItems forBrokenId(int id) {
			return BROKENITEMS.get(id);
		}

		public static BrokenItems forDegradedId(int id) {
			return DEGRADEDITEMS.get(id);
		}

		private final int brokenId, degradedId, fixedId;

		private final int price;

		private BrokenItems(int brokenId, int degradedId, int fixedId, int price) {
			this.brokenId = brokenId;
			this.degradedId = degradedId;
			this.fixedId = fixedId;
			this.price = price;
		}

		public int getBrokenId() {
			return brokenId;
		}

		public int getDegradedId() {
			return degradedId;
		}

		public int getFixedId() {
			return fixedId;
		}

		public int getFullRepairPrice() {
			return price;
		}

		public int getPrice(Player player, int itemId) {
			return itemId == degradedId ? getDegradedPrice(player, this) : price;
		}

	}

    public static void CheckPrice(Player player, int itemId, int amount) {
        BrokenItems brokenitems = BrokenItems.forBrokenId(itemId);
        if (brokenitems == null)
            brokenitems = BrokenItems.forDegradedId(itemId);
        if (brokenitems == null) {
                player.sm("Item does not have 'forDegradedId' data: "+itemId+"");
                return;
        }
        final int price = brokenitems.getPrice(player, itemId);
        player.getDialogueManager().startDialogue("SimpleNPCMessage", 945, "These items will cost you " + getFormattedNumber(price * amount) + " coins.");
        return;
    }

	public static int getDegradedPrice(Player player, BrokenItems brokenitems) {
		final double charges = player.getCharges().getCharges(brokenitems.getDegradedId());
		final int maxPrice = brokenitems.getFullRepairPrice();
		if (charges == 0)
			return maxPrice;
		int discount = (int) (maxPrice * (charges / getTotalCharges(brokenitems.getDegradedId())));
		if (discount > maxPrice)
			discount = maxPrice;
		return maxPrice - discount;
	}

	private static String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount).toString();
	}

	// return amt of charges
	public static double getTotalCharges(int id) {
		// pvp armors
		if (id >= 13910 && id <= 13952)
			return 1500;
		if (id >= 13960 && id <= 13975)
			return 3000;
		if ((id >= 14094 && id <= 14121) || (id >= 21527 && id <= 21530))
			return 1800;
		if (id >= 13860 && id <= 13960)
			return 30000; // 5 hours
		// nex armors
		if (id >= 20137 && id <= 20173)
			return 60000;
		return 1;// keep it positive for getDegradedPrice
	}

	public static void Repair(Player player, int itemId, int amount) {
		BrokenItems brokenitems = BrokenItems.forBrokenId(itemId);
		if (brokenitems == null)
			brokenitems = BrokenItems.forDegradedId(itemId);
		final Item item = new Item(itemId, 1);
		final int price = brokenitems.getPrice(player, itemId);
		if (amount == 1) {
			if (player.getInventory().removeItemMoneyPouch(995, price)) {
				player.getInventory().deleteItem(itemId, 1);
				player.getInventory().removeItemMoneyPouch(995, price);
				player.getInventory().addItem(brokenitems.getFixedId(), 1);
				player.getDialogueManager().startDialogue("SimpleMessage", "You have repaired your item(" + item.getName() + ") for " + getFormattedNumber(price) + " coins.");
				return;
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage", "You dont have enough money to repair this item." + "You need " + getFormattedNumber(price) + " coins.");
				return;
			}
		} else {
			if (player.getInventory().removeItemMoneyPouch(995, price)) {
				player.getInventory().deleteItem(itemId, amount);
				player.getInventory().removeItemMoneyPouch(995, price);
				player.getInventory().addItem(brokenitems.getFixedId(), amount);
				player.getDialogueManager().startDialogue("SimpleMessage", "You have repaired your items(" + amount + " X " + item.getName() + ") for " + getFormattedNumber(price * amount) + " coins.");
				return;
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage", "You dont have enough money to repair these items." + "You need " + getFormattedNumber(price * amount) + " coins.");
				return;
			}
		}
	}
}