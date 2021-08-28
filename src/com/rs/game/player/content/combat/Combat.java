package com.rs.game.player.content.combat;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Entity;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.content.perks.PlayerPerks;
import com.rs.utils.Utils;

public final class Combat {
	
	public static final int MELEE_TYPE = 0, RANGE_TYPE = 1, MAGIC_TYPE = 2, ALL_TYPE = 3;
	
	public static final int NONE_STYLE = 0, ARROW_STYLE = 8, BOLT_STYLE = 9, THROWN_STYLE = 10;

	public static boolean hasAntiDragProtection(Entity target) {
		if (target instanceof NPC)
			return false;
		Player p2 = (Player) target;
		int shieldId = p2.getEquipment().getShieldId();
		return shieldId == 1540 || shieldId == 11283 || shieldId == 11284 || p2.getPerkManager().hasPerk(PlayerPerks.DRAGON_TAMER);
	}

	/**
	 * Gets the Slayer level required to attack certain slayer NPC's.
	 * @param id The NPC Id.
	 * @return the slayer level required.
	 */
    public static int getSlayerLevelForNPC(int id) {
		switch (id) {
		case 24766:
		case 24767:
		case 24768:
			return 117;
		case 24765:
			return 115;
		case 24603:
			return 113;
		case 24602:
			return 111;
		case 24601:
			return 109;
		case 24832:
		case 24833:
			return 108;
		case 24600:
			return 107;
		case 24831:
			return 106;
		case 24599:
			return 105;
		case 24830:
			return 104;
		case 24598:
			return 103;
		case 24172:
			return 101;
		case 24596:
		case 24597:
			return 100;
		case 13761:
		case 25125:
		case 25126:
			return 99;
		case 24171:
			return 98;
		case 24595:
			return 97;
		case 21812:
		case 21992:
		case 21994:
		case 21995:
		case 22689:
		case 22001:
		case 22007:
		case 23073:
		case 23074:
		case 23075:
		case 23076:
			return 96;
		case 24170:
		case 14696:
		case 14697:
		case 14698:
		case 14699:
		case 17149:
		case 17150:
		case 17151:
		case 17152:
		case 17153:
		case 17154:
			return 95;
		case 20630:
		case 24594:	
			return 94;
		case 9463:
			return 93;
		case 18621:
			return 92;
		case 13822:
			return 91;
		case 2783:
			return 90;
		case 20290:
			return 90;
		case 14688:
		case 14689:
			return 88;
		case 13821:
			return 86;
		case 1615:
			return 85;
		case 6278:
		case 6221:
		case 6231:
		case 6257:
		case 17970:
		case 17978:
			return 83;
		case 14700:
		case 14701:
			return 82;
		case 17148:
		case 17147:
		case 17146:
		case 17145:
		case 17144:
			return 81;
		case 13820:
		case 1613:
		case 23696:	
			return 80;
		case 9172:
			return 78;
		case 9465:
			return 77;
		case 1610:
			return 75;
		case 9467:
			return 73;
		case 3068:
		case 3069:
		case 3070:
		case 3071:
			return 72;
		case 1608:
		case 1609:
			return 70;
		case 6219:
		case 6229:
		case 6255:
		case 6277:
			return 68;
		case 1624:
			return 65;
		case 10700:
		case 6276:
		case 6256:
		case 6230:
		case 6220:
			return 63;
		case 1604:
		case 1605:
		case 1606:
		case 1607:
			return 60;
		case 4353:
			return 58;
		case 4354:
		case 4355:
		case 4356:
		case 4357:
			return 58;
		case 3346:
		case 3347:
			return 57;
		case 6296:
		case 6285:
		case 6286:
		case 6287:
		case 6288:
		case 6289:
		case 6290:
		case 6291:
		case 6292:
		case 6293:
		case 6294:
		case 6295:
			return 56;
		case 1623:
			return 55;
		case 1637:
			return 52;
		case 1638:
			return 52;
		case 1639:
			return 52;
		case 1640:
			return 52;
		case 1641:
			return 52;
		case 1642:
			return 52;
		case 7462:
			return 50;
		case 7463:
			return 50;
		case 1618:
			return 50;
		case 1643:
			return 45;
		case 1644:
			return 45;
		case 1645:
			return 45;
		case 1646:
			return 45;
		case 1647:
			return 45;
		case 1616:
			return 40;
		case 1617:
			return 40;
		case 114:
			return 32;
		case 7823:
			return 35;
		case 3201:
			return 37;
		case 3202:
			return 37;
		case 3153:
			return 33;
		case 1633:
			return 30;
		case 1634:
			return 30;
		case 1635:
			return 30;
		case 1636:
			return 30;
		case 1620:
			return 25;
		case 2804:
			return 22;
		case 2805:
			return 22;
		case 2806:
			return 22;
		case 1631:
			return 20;
		case 1632:
			return 20;
		case 1831:
			return 17;
		case 1612:
			return 15;
		case 1600:
			return 10;
		case 1601:
			return 10;
		case 1602:
			return 10;
		case 1603:
			return 10;
		case 1832:
			return 7;
		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1653:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 5;

		default:
			return 0;
		}
	}
    
	public static boolean hasOffhand(Player player) {
		return player.getEquipment() != null && player.getEquipment().getShieldId() != -1;
	}

	public static int getDefenceEmote(Entity target) {
		if (target instanceof NPC) {
			NPC n = (NPC) target;
			return n.getCombatDefinitions().getDefenceEmote();
		} else {
			Player p = (Player) target;
			boolean legacy = true;
			if (p.getEquipment().getShieldId() != -1)
				return legacy ? 424 : 18346;
			Item weapon = p.getEquipment().getItem(Equipment.SLOT_WEAPON);
			if (weapon == null)
				return legacy ? 424 : 18346;
			int emote = weapon.getDefinitions().getCombatOpcode(legacy ? 4387 : 2917 /* 4371 */);
			return emote == 0 ? legacy ? 424 : 18346 : emote;
		}
	}

	private Combat() {
	}

	public static final boolean fullGuthanEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinitions.getItemDefinitions(helmId).getName().contains("Guthan's")
				&& ItemDefinitions.getItemDefinitions(chestId).getName().contains("Guthan's")
				&& ItemDefinitions.getItemDefinitions(legsId).getName().contains("Guthan's")
				&& ItemDefinitions.getItemDefinitions(weaponId).getName().contains("Guthan's");
	}

	public static final String getProtectMessage(Player player) {
		boolean hasFireImmune = player.getFireImmune() > Utils.currentTimeMillis() && player.getFireImmune() != 0;
		boolean hasFirePrayerProtection = player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 17);
		if (player.getPerkManager().hasPerk(PlayerPerks.DRAGON_TAMER)) {
			return "Your dragon trainer perk fully absorbs the heat of the dragon's breath!";
		}
		if (player.getSuperAntiFire() > Utils.currentTimeMillis()) {
			return "Your potion fully absorbs the heat of the dragon's breath!";
		}
		if (hasFireImmune && hasFirePrayerProtection) {
			return "Your prayer and potion fully absorbs the heat of the dragon's breath!";
		}
		if (hasDFS(player) && hasFirePrayerProtection) {
			return "Your prayer and shield fully absorbs the heat of the dragon's breath!";
		}
		if (hasFireImmune && hasDFS(player)) {
			return "Your potion and shield fully absorbs the heat of the dragon's breath!";
		}
		if (hasDFS(player)) {
			return "Your shield absorbs some of the dragon's breath!";
		}
		if (hasFireImmune) {
			return "Your potion absorbs some of the dragon's breath!";
		}
		if (hasFirePrayerProtection) {
			return "Your prayer absorbs some of the dragon's breath!";
		}
		return null;
	}
	
    public static boolean hasDFS(Player player) {
		int shieldId = player.getEquipment().getShieldId();
		return shieldId == 1540 || shieldId == 11283 || shieldId == 11284 || shieldId == 25558
				 || shieldId == 25559 || shieldId == 25561 || shieldId == 25562 || shieldId == 16933 
				 || player.getPerkManager().hasPerk(PlayerPerks.DRAGON_TAMER);
	}
}
