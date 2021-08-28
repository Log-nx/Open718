package com.rs.game.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Animation;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.utils.Colors;

public class ItemDyes {

	public static int barrowsDye = 33294, shadowDye = 33296, thirdAgeDye = 33298, bloodDye = 36274;

	private static enum DyeOnItem {
		BLOOD_TEC_MASK(28608, bloodDye, 36276),
		BLOOD_TEC_ROBE_TOP(28611, bloodDye, 36279),
		BLOODTEC_ROBE_BOT(28614, bloodDye, 36282),
		BLOOD_SIRENIC_MAC(29854, bloodDye, 36285),
		BLOOD_SIRENIC_HAUB(29857, bloodDye, 36288),
		BLOOD_SIRENIC_CHAPS(29860, bloodDye, 36291),
		BLOOD_MALE_HELM(30005, bloodDye, 36294),
		BLOOD_MALE_CUIRASS(30008, bloodDye, 36297),
		BLOOD_MALE_GREAVES(30011, bloodDye, 36300),
		BLOOD_DRYG_MAC(26595, bloodDye, 36303),
		BLOOD_OH_DRYG_MAC(26599, bloodDye, 36306),
		BLOOD_DRYG_RAP(26579, bloodDye, 36309),
		BLOOD_DRYG_OH_RAP(26583, bloodDye, 36312),
		BLOOD_DRYG_LS(26587, bloodDye, 36315),
		BLOOD_OH_DRYG_LS(26591, bloodDye, 36318),
		BLOOD_ASC_CBOW(28437, bloodDye, 36321),
		BLOOD_OH_ASC_CBOW(28441, bloodDye, 36324),
		BLOOD_SEISMIC_WAND(28617, bloodDye, 36327),
		BLOOD_SEISMIC_SING(28621, bloodDye, 36330),
		BLOOD_NOX_STAFF(31729, bloodDye, 36336),
		BLOOD_NOX_LBOW(31733, bloodDye, 36339),
		BLOOD_NOX_SCYTHE(31725, bloodDye, 36333),
		BARROWS_DRYGORE_MACE(26595, barrowsDye, 33300),
		BARROWS_OFFHAND_DRYGORE_MACE(26599, barrowsDye, 33303),
		BARROWS_DRYGORE_RAPIER(26579, barrowsDye, 33306),
		BARROWS_OFFHAND_DRYGORE_RAPIER(26583, barrowsDye, 33306),
		BARROWS_DRYGORE_LONGSWORD(26587, barrowsDye, 33312),
		BARROWS_OFFHAND_DRYGORE_LONGSWORD(26591, barrowsDye, 33315),
		B_NOX_LB(31733, barrowsDye, 33336),
		B_NOX_SC(31725, barrowsDye, 33330),
		B_NOX_ST(31729, barrowsDye, 33333),
		B_ASC_CB(28437, barrowsDye, 33318),
		B_OH_ASC_CB(28441, barrowsDye, 33321),
		B_SEISMIC_WAND(28617, barrowsDye, 33324),
		B_SEISMIC_SING(28621, barrowsDye, 33324),
		B_TECTONIC_MASK(28608, barrowsDye, 33339),
		B_TECTONIC_ROBE_T(28611, barrowsDye, 33342),
		B_TECTONIC_ROBE_B(28614, barrowsDye, 33345),
		B_SIRENIC_MASK(29854, barrowsDye, 33348),
		B_SIRENIC_HAUBERK(29857, barrowsDye, 33351),
		B_SIRENIC_CHAPS(29860, barrowsDye, 33354),
		B_MALE_HELM(30005, barrowsDye, 33357),
		B_MALE_CIR(30008, barrowsDye, 33360),
		B_MALE_GR(30011, barrowsDye, 33363),
		SH_DRYGORE_MACE(26595, shadowDye, 33366),
		SH_OH_DRYGORE_MACE(26599, shadowDye, 33366),
		SH_DRYGORE_RAPIER(26579, shadowDye, 33372),
		SH_OH_DRYGORE_RAPIER(26583, shadowDye, 33375),
		SH_DRYG_LS(26587, shadowDye, 33378),
		SH_OH_DRYG_LS(26591, shadowDye, 33381),
		SH_NOX_LB(31733, shadowDye, 33402),
		SH_NOX_SC(31725, shadowDye, 33396),
		SH_NOX_STAFF(31729, shadowDye, 33399),
		SH_ASC_CBOW(28437, shadowDye, 33384),
		SH_OH_ASC_CBOW(28441, shadowDye, 33387),
		SH_SEISMIC_WAND(28617, shadowDye, 33390),
		SH_SEISMIC_SING(28621, shadowDye, 33393),
		SH_TECTONIC_MASK(28608, shadowDye, 33405),
		SH_TECTONIC_ROBE_T(28611, shadowDye, 33408),
		SH_TECTONIC_ROBE_B(28614, shadowDye, 33411),
		SH_SIRENIC_MASK(29854, shadowDye, 33414),
		SH_SIRENIC_HAUBERK(29857, shadowDye, 33417),
		SH_SIRENIC_CHAPS(29860, shadowDye, 33420),
		SH_MALE_HELM(30005, shadowDye, 33423),
		SH_MALE_CIR(30008, shadowDye, 33426),
		SH_MALE_GR(30011, shadowDye, 33429),
		TA_DRYGORE_MACE(26595, thirdAgeDye, 33432),
		TA_OH_DRYGORE_MACE(26599, thirdAgeDye, 33435),
		TA_DRYG_RAPIER(26579, thirdAgeDye, 33438),
		TA_OH_DRYG_RAPIER(26583, thirdAgeDye, 33441),
		TA_DRYG_LS(26587, thirdAgeDye, 33444),
		TA_OH_DRYG_LS(26591, thirdAgeDye, 33447),
		TA_NOX_LB(31733, thirdAgeDye, 33468),
		TA_NOX_SC(31725, thirdAgeDye, 33462),
		TA_NOX_STAFF(31729, thirdAgeDye, 33465),
		TA_ASC_CBOW(28437, thirdAgeDye, 33450),
		TA_OH_ASC_CBOW(28441, thirdAgeDye, 33453),
		TA_SEISMIC_WAND(28617, thirdAgeDye, 33456),
		TA_SEISMIC_SING(28621, thirdAgeDye, 33459),
		TA_TECTONIC_MASK(28608, thirdAgeDye, 33471),
		TA_TECTONIC_ROBE_T(28611, thirdAgeDye, 33474),
		TA_TECTONIC_ROBE_B(28614, thirdAgeDye, 33477),
		TA_SIRENIC_MASK(29854, thirdAgeDye, 33480),
		TA_SIRENIC_HAUBERK(29857, thirdAgeDye, 33483),
		TA_SIRENIC_CHAPS(29860, thirdAgeDye, 33486),
		TA_MALE_HELM(30005, thirdAgeDye, 33489),
		TA_MALE_CUIR(30008, thirdAgeDye, 33492),
		TA_MALE_GR(30011, thirdAgeDye, 33495),
		SHADOW_DRYGORE_MACE(26595, shadowDye, 33366);

		private int regId, dyeId, newId;

		DyeOnItem(int regId, int dyeId, int newId) {
			this.regId = regId;
			this.dyeId = dyeId;
			this.newId = newId;
		}
	}

	private static DyeOnItem getItem(Item item1, Item item2) {
		for (DyeOnItem itemDye : DyeOnItem.values()) {
			if (InventoryOptionsHandler.contains(itemDye.regId, itemDye.dyeId, item1, item2)) {
				return itemDye;
			}
		}
		return null;
	}

	public static DyeOnItem getItem(Item item) {
		for (DyeOnItem itemDye : DyeOnItem.values()) {
			if (itemDye.newId == item.getId()) {
				return itemDye;
			}
		}
		return null;
	}

	public static boolean attachDyeOnItem(Player player, Item regularItem, Item dye, int slot1, int slot2) {
		DyeOnItem itemDye = getItem(regularItem, dye);
		if (itemDye == null) {
			return false;
		}
		player.setNextAnimation(new Animation(4348));
		player.getInventory().deleteItem(slot1, regularItem);
		player.getInventory().getItem(slot2).setId(itemDye.newId);
		player.getInventory().refresh(slot2);
		sendItemDialogue(player, itemDye.newId);
		return true;
	}
	
	private static void sendItemDialogue(Player player, int ItemId ) {
		player.getDialogueManager().startDialogue(new Dialogue() {
			@Override
			public void start() {
				sendItemDialogue(ItemId, "You have dyed this item, your <col=ff0000>" + ItemDefinitions.getItemDefinitions(ItemId).getName() + "</col> is no longer tradeable."
						+ "<br><br> Would you like it to be announced?");
			}

			@Override
			public void run(int interfaceId, int componentId)  {
				switch(stage) {
				case -1:
					sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Yes, announce it", "No thanks");
					stage = 1;
					break;
				case 0:
					end();
					break;
				case 1:
					switch(componentId) {
					case OPTION_1:
						end();
						World.sendWorldMessage(Colors.GOLDEN + "<shad=FF0000><img=6>Diccus: " + player.getDisplayName() + " has just dyed " + ItemDefinitions.getItemDefinitions(ItemId).getName() +"!", false, false);
						break;
					case OPTION_2:
						end();
						break;
					}
					break;
				}
			}

			@Override
			public void finish() {
			}
			
		});
	}

}
