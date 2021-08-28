package com.rs.net.decoders;

import java.io.IOException;

import org.javacord.api.entity.message.MessageBuilder;

import com.rs.GameServer;
import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.instances.BossInstanceHandler;
import com.rs.game.instances.BossInstanceHandler.Boss;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.clanwars.ClanWars;
import com.rs.game.minigames.duel.DuelRules;
import com.rs.game.minigames.hunger.HungerGamesControler;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.npc.glacor.EnduringGlacyte;
import com.rs.game.npc.glacor.Glacor;
import com.rs.game.npc.glacor.SappingGlacyte;
import com.rs.game.npc.glacor.UnstableGlacyte;
import com.rs.game.player.Bank;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.QuickChatMessage;
import com.rs.game.player.Skills;
import com.rs.game.player.TemporaryAtributtes.Key;
import com.rs.game.player.actions.PlayerFollow;
import com.rs.game.player.actions.combat.PlayerCombat;
import com.rs.game.player.actions.combat.PlayerCombatScript;
import com.rs.game.player.actions.construction.House;
import com.rs.game.player.actions.construction.Sawmill;
import com.rs.game.player.actions.construction.Sawmill.Plank;
import com.rs.game.player.actions.firemaking.Firemaking;
import com.rs.game.player.actions.magic.Magic;
import com.rs.game.player.actions.summoning.Summoning;
import com.rs.game.player.appearance.Equipment;
import com.rs.game.player.appearance.LogicPacket;
import com.rs.game.player.appearance.Equipment.SavedCosmetic;
import com.rs.game.player.content.Commands;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.MoneyPouch;
import com.rs.game.player.content.Notes.Note;
import com.rs.game.player.content.Shop;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.activities.XPWell;
import com.rs.game.player.content.clans.ChatMessage;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.commands.DeveloperConsole;
import com.rs.game.player.content.custom.Censor;
import com.rs.game.player.content.custom.LunarSpells;
import com.rs.game.player.content.input.InputEvent;
import com.rs.game.player.content.input.impl.InputIntegerEvent;
import com.rs.game.player.content.input.impl.InputNameEvent;
import com.rs.game.player.content.interfaces.AccountManager;
import com.rs.game.player.content.interfaces.LootbeamSettings;
import com.rs.game.player.content.items.RepairItems;
import com.rs.game.player.content.overrides.CosmeticsHandler;
import com.rs.game.player.content.pet.Pets;
import com.rs.game.player.controllers.SawmillController;
import com.rs.game.player.managers.LendingManager;
import com.rs.game.route.RouteFinder;
import com.rs.game.route.strategy.FixedTileStrategy;
import com.rs.io.InputStream;
import com.rs.io.OutputStream;
import com.rs.net.Session;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.net.decoders.handlers.NPCHandler;
import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.utils.DiscordBot;
import com.rs.utils.DisplayNames;
import com.rs.utils.Encrypt;
import com.rs.utils.ItemExamines;
import com.rs.utils.Lend;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[106];

	private final static int WALKING_PACKET = 8;
	private final static int MINI_WALKING_PACKET = 58;
	private final static int AFK_PACKET = -1;
	public final static int ACTION_BUTTON1_PACKET = 14;
	public final static int ACTION_BUTTON2_PACKET = 67;
	public final static int ACTION_BUTTON3_PACKET = 5;
	private final static int ITEM_ON_FLOOR_EXAMINE = 102;
	public final static int ACTION_BUTTON4_PACKET = 55;
	public final static int ACTION_BUTTON5_PACKET = 68;
	public final static int ACTION_BUTTON6_PACKET = 90;
	public final static int ACTION_BUTTON7_PACKET = 6;
	public final static int ACTION_BUTTON8_PACKET = 32;
	public final static int ACTION_BUTTON9_PACKET = 27;
	public final static int WORLD_MAP_CLICK = 38;
	private final static int WORLD_LIST_UPDATE = 87;
	private final static int LOBBY_MAIN_CLICK_PACKET = 91;
	private final static int LOBBY_FRIEND_CHAT_SETTINGS = 79;
	public final static int ACTION_BUTTON10_PACKET = 96;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 33;
	private final static int MAGIC_ON_ITEM_PACKET = -1;
	private final static int PLAYER_OPTION_4_PACKET = 17;
	private final static int PLAYER_OPTION_5_PACKET = 77;
	private final static int PLAYER_OPTION_6_PACKET = 49;
	private final static int MOVE_CAMERA_PACKET = 103;
	private final static int INTERFACE_ON_OBJECT = 37;
	private final static int CLICK_PACKET = -1;
	private final static int MOUVE_MOUSE_PACKET = -1;
	private final static int KEY_TYPED_PACKET = -1;
	private final static int CLOSE_INTERFACE_PACKET = 54;
	private final static int COMMANDS_PACKET = 60;
	private final static int ITEM_ON_ITEM_PACKET = 3;
	private final static int IN_OUT_SCREEN_PACKET = -1;
	private final static int DONE_LOADING_REGION_PACKET = 30;
	private final static int PING_PACKET = 21;
	private final static int SCREEN_PACKET = 98;
	private final static int CHAT_TYPE_PACKET = 83;
	private final static int CHAT_PACKET = 53;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 86;
	private final static int ADD_FRIEND_PACKET = 89;
	private final static int ADD_IGNORE_PACKET = 4;
	private final static int REMOVE_IGNORE_PACKET = 73;
	private final static int JOIN_FRIEND_CHAT_PACKET = 36;
	private final static int CHANGE_FRIEND_CHAT_PACKET = 22;
	private final static int KICK_FRIEND_CHAT_PACKET = 74;
	private final static int REMOVE_FRIEND_PACKET = 24;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 82;
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 0;
	private final static int OBJECT_CLICK1_PACKET = 26;
	private final static int OBJECT_CLICK2_PACKET = 59;
	private final static int OBJECT_CLICK3_PACKET = 40;
	private final static int OBJECT_CLICK4_PACKET = 23;
	private final static int OBJECT_CLICK5_PACKET = 80;
	private final static int OBJECT_EXAMINE_PACKET = 25;
	private final static int NPC_CLICK1_PACKET = 31;
	private final static int NPC_CLICK2_PACKET = 101;
	private final static int NPC_CLICK3_PACKET = 34;
	private final static int NPC_CLICK4_PACKET = 65;
	private final static int ATTACK_NPC = 20;
	private final static int PLAYER_OPTION_1_PACKET = 42;
	private final static int PLAYER_OPTION_2_PACKET = 46;
	private final static int ITEM_TAKE_PACKET = 57;
	private final static int GROUND_ITEM_OPTION_2_PACKET = 62;
	private final static int GROUND_ITEM_OPTION_EXAMINE = 102;
	private final static int DIALOGUE_CONTINUE_PACKET = 72;
	private final static int ENTER_INTEGER_PACKET = 81;
	private final static int ENTER_NAME_PACKET = 29;
	private final static int ENTER_LONG_TEXT_PACKET = 48;
	private final static int ENTER_STRING_PACKET = -1;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 76;
	private final static int INTERFACE_ON_PLAYER = 50;
	private final static int INTERFACE_ON_NPC = 66;
	private final static int COLOR_ID_PACKET = 97;
	private static final int NPC_EXAMINE_PACKET = 9;
	private final static int PLAYER_OPTION_9_PACKET = 56;
	private final static int REPORT_ABUSE_PACKET = 11;
	private final static int KICK_CLAN_CHAT_PACKET = 92;
	private final static int JOIN_CLAN_CHAT_PACKET = 133;
	private final static int GRAND_EXCHANGE_ITEM_SELECT_PACKET = 71;
	private final static int DROP_ITEM_PACKET = 104;
	public final static int ESCAPE_PRESSED_PACKET = 105;

	static {
		loadPacketSizes();
	}

	public static void loadPacketSizes() {
		PACKET_SIZES[0] = -1;
		PACKET_SIZES[1] = -2;
		PACKET_SIZES[2] = -1;
		PACKET_SIZES[3] = 16;
		PACKET_SIZES[4] = -1;
		PACKET_SIZES[5] = 8;
		PACKET_SIZES[6] = 8;
		PACKET_SIZES[7] = 3;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[9] = 3;
		PACKET_SIZES[10] = -1;
		PACKET_SIZES[11] = -1;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[13] = 7;
		PACKET_SIZES[14] = 8;
		PACKET_SIZES[15] = 6;
		PACKET_SIZES[16] = 2;
		PACKET_SIZES[17] = 3;
		PACKET_SIZES[18] = -1;
		PACKET_SIZES[19] = -2;
		PACKET_SIZES[20] = 3;
		PACKET_SIZES[21] = 0;
		PACKET_SIZES[22] = -1;
		PACKET_SIZES[23] = 9;
		PACKET_SIZES[24] = -1;
		PACKET_SIZES[25] = 9;
		PACKET_SIZES[26] = 9;
		PACKET_SIZES[27] = 8;
		PACKET_SIZES[28] = 4;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[30] = 0;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[32] = 8;
		PACKET_SIZES[33] = 4;
		PACKET_SIZES[34] = 3;
		PACKET_SIZES[35] = -1;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[37] = 17;
		PACKET_SIZES[38] = 4;
		PACKET_SIZES[39] = 4;
		PACKET_SIZES[40] = 9;
		PACKET_SIZES[41] = -1;
		PACKET_SIZES[42] = 3;
		PACKET_SIZES[43] = 7;
		PACKET_SIZES[44] = -2;
		PACKET_SIZES[45] = 7;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[47] = 4;
		PACKET_SIZES[48] = -1;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[50] = 11;
		PACKET_SIZES[51] = 3;
		PACKET_SIZES[52] = -1;
		PACKET_SIZES[53] = -1;
		PACKET_SIZES[54] = 0;
		PACKET_SIZES[55] = 8;
		PACKET_SIZES[56] = 3;
		PACKET_SIZES[57] = 7;
		PACKET_SIZES[58] = -1;
		PACKET_SIZES[59] = 9;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[61] = 7;
		PACKET_SIZES[62] = 7;
		PACKET_SIZES[63] = 12;
		PACKET_SIZES[64] = 4;
		PACKET_SIZES[65] = 3;
		PACKET_SIZES[66] = 11;
		PACKET_SIZES[67] = 8;
		PACKET_SIZES[68] = 8;
		PACKET_SIZES[69] = 15;
		PACKET_SIZES[70] = 1;
		PACKET_SIZES[71] = 2;
		PACKET_SIZES[72] = 6;
		PACKET_SIZES[73] = -1;
		PACKET_SIZES[74] = -1;
		PACKET_SIZES[75] = -2;
		PACKET_SIZES[76] = 16;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[78] = 1;
		PACKET_SIZES[79] = 3;
		PACKET_SIZES[80] = 9;
		PACKET_SIZES[81] = 4;
		PACKET_SIZES[82] = -2;
		PACKET_SIZES[83] = 1;
		PACKET_SIZES[84] = 1;
		PACKET_SIZES[85] = 3;
		PACKET_SIZES[86] = -1;
		PACKET_SIZES[87] = 4;
		PACKET_SIZES[88] = 3;
		PACKET_SIZES[89] = -1;
		PACKET_SIZES[90] = 8;
		PACKET_SIZES[91] = -2;
		PACKET_SIZES[92] = -1;
		PACKET_SIZES[93] = -1;
		PACKET_SIZES[94] = 9;
		PACKET_SIZES[95] = -2;
		PACKET_SIZES[96] = 8;
		PACKET_SIZES[97] = 2;
		PACKET_SIZES[98] = 6;
		PACKET_SIZES[99] = 2;
		PACKET_SIZES[100] = -2;
		PACKET_SIZES[101] = 3;
		PACKET_SIZES[102] = 7;
		PACKET_SIZES[103] = 4;
		PACKET_SIZES[104] = 8;
		PACKET_SIZES[105] = 0;
	}

	private Player player;
	private int chatType;

	private int value;

	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) throws Exception {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readPacket(player);
			if (packetId >= PACKET_SIZES.length || packetId < 0) {
				if (Settings.DEBUG)
					System.out.println("PacketId " + packetId + " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				if (Settings.DEBUG)
					System.out.println("Invalid size for PacketId " + packetId + ". Size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				if (Settings.DEBUG)
					System.out.println("PacketId " + packetId + " has fake size. - expected size " + length);
				// break;

			}
			/*
			 * System.out.println("PacketId " +packetId+ " has . - expected size " +length);
			 */
			int startOffset = stream.getOffset();
			try {
				processPackets(packetId, stream, length);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stream.setOffset(startOffset + length);
		}
	}

	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		int packetId = packet.getId();
		InputStream stream = new InputStream(packet.getData());
		if (packetId == NPC_CLICK4_PACKET) {
			NPCHandler.handleOption4(player, stream);
			return;
		}
		if (packetId == PLAYER_OPTION_5_PACKET) {
			stream.readByte();
			int playerIndex = stream.readUnsignedShortLE128();
			Player other = World.getPlayers().get(playerIndex);
			if (other == null || other.isDead() || other.hasFinished()
					|| !player.getMapRegionsIds().contains(other.getRegionId()))
				return;
			if (player.isLocked())
				return;
			if (!other.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + other.getDisplayName() + ".");
				return;
			}
			// player.getDialogueManager().startDialogue("ModPanel",
			// playerIndex);
		}

		if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.isLocked())
				return;
			if (player.getFreezeDelay() >= Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("A magical force prevents you from moving.");
				return;
			}
			int length = stream.getLength();
			int baseX = stream.readUnsignedShort128();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			int baseY = stream.readUnsignedShort128();
			int steps = (length - 5) / 2;
			if (steps > 25)
				steps = 25;
			player.stopAll();
			if (forceRun)
				player.setRun(forceRun);

			if (steps > 0) {
				int x = 0, y = 0;
				for (int step = 0; step < steps; step++) {
					x = baseX + stream.readUnsignedByte();
					y = baseY + stream.readUnsignedByte();
				}

				steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(),
						player.getPlane(), player.getSize(), new FixedTileStrategy(x, y), true);
				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				int last = -1;
				for (int i = steps - 1; i >= 0; i--) {
					if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true))
						break;
					last = i;
				}
				if (last != -1) {
					WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getPlane());
					player.getPackets().sendMinimapFlag(
							tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()),
							tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
				} else {
					player.getPackets().sendResetMinimapFlag();
				}
			}
		} else if (packetId == INTERFACE_ON_OBJECT) {
			boolean forceRun = stream.readByte128() == 1;
			int itemId = stream.readUnsignedShortLE128();
			int y = stream.readShortLE128();
			int objectId = stream.readIntV2();
			int interfaceHash = stream.readInt();
			final int interfaceId = interfaceHash >> 16;
			int slot = stream.readShortLE();
			int x = stream.readShort128();
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() >= currentTime || player.getEmotesManager().getNextEmoteEnd() >= currentTime)
				return;
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getObjectWithId(tile, objectId);
			if (mapObject == null || mapObject.getId() != objectId)
				return;
			final WorldObject object = !player.isAtDynamicRegion() ? mapObject
					: new WorldObject(objectId, mapObject.getType(), mapObject.getRotation(), x, y, player.getPlane());
			final Item item = player.getInventory().getItem(slot);
			if (player.isDead() || Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (item == null || item.getId() != itemId)
				return;
			player.stopAll(false); // false
			if (forceRun)
				player.setRun(forceRun);
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE: // inventory
				ObjectHandler.handleItemOnObject(player, object, interfaceId, item);
				break;
			}
		} else if (packetId == PLAYER_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerFollow(p2));
		} else if (packetId == PLAYER_OPTION_6_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerFollow(p2));
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + p2.getDisplayName());
				return;
			}
			if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("<col=B00000>You view " + p2.getDisplayName()
						+ " Stats's until 10 seconds after the end of combat.");
				return;
			}
			player.getInterfaceManager().sendInterface(1314);
			player.getPackets().sendIComponentText(1314, 30, "" + "Prestige Level");
			player.getPackets().sendIComponentText(1314, 60, "" + p2.getPrestigeLevel());
			player.getPackets().sendIComponentText(1314, 91, "" + p2.getDisplayName() + "'s stats");
			player.getPackets().sendIComponentText(1314, 90, "");
			player.getPackets().sendIComponentText(1314, 61, "" + p2.getSkills().getLevel(0));
			player.getPackets().sendIComponentText(1314, 62, "" + p2.getSkills().getLevel(2));
			player.getPackets().sendIComponentText(1314, 63, "" + p2.getSkills().getLevel(1));
			player.getPackets().sendIComponentText(1314, 65, "" + p2.getSkills().getLevel(4));
			player.getPackets().sendIComponentText(1314, 66, "" + p2.getSkills().getLevel(5));
			player.getPackets().sendIComponentText(1314, 64, "" + p2.getSkills().getLevel(6));
			player.getPackets().sendIComponentText(1314, 78, "" + p2.getSkills().getLevel(20));
			player.getPackets().sendIComponentText(1314, 81, "" + p2.getSkills().getLevel(22));
			player.getPackets().sendIComponentText(1314, 76, "" + p2.getSkills().getLevel(24));
			player.getPackets().sendIComponentText(1314, 82, "" + p2.getSkills().getLevel(3));
			player.getPackets().sendIComponentText(1314, 83, "" + p2.getSkills().getLevel(16));
			player.getPackets().sendIComponentText(1314, 84, "" + p2.getSkills().getLevel(15));
			player.getPackets().sendIComponentText(1314, 80, "" + p2.getSkills().getLevel(17));
			player.getPackets().sendIComponentText(1314, 70, "" + p2.getSkills().getLevel(12));
			player.getPackets().sendIComponentText(1314, 85, "" + p2.getSkills().getLevel(9));
			player.getPackets().sendIComponentText(1314, 77, "" + p2.getSkills().getLevel(18));
			player.getPackets().sendIComponentText(1314, 79, "" + p2.getSkills().getLevel(21));
			player.getPackets().sendIComponentText(1314, 68, "" + p2.getSkills().getLevel(14));
			player.getPackets().sendIComponentText(1314, 69, "" + p2.getSkills().getLevel(13));
			player.getPackets().sendIComponentText(1314, 74, "" + p2.getSkills().getLevel(10));
			player.getPackets().sendIComponentText(1314, 75, "" + p2.getSkills().getLevel(7));
			player.getPackets().sendIComponentText(1314, 73, "" + p2.getSkills().getLevel(11));
			player.getPackets().sendIComponentText(1314, 71, "" + p2.getSkills().getLevel(8));
			player.getPackets().sendIComponentText(1314, 72, "" + p2.getSkills().getLevel(19));
			player.getPackets().sendIComponentText(1314, 67, "" + p2.getSkills().getLevel(23));
			player.getPackets().sendIComponentText(1314, 87, "" + p2.getMaxHitpoints());
			player.getPackets().sendIComponentText(1314, 86, "" + p2.getSkills().getCombatLevelWithSummoning());
			player.getPackets().sendIComponentText(1314, 88, "" + p2.getSkills().getTotalLevel(p2));
			player.getPackets().sendIComponentText(1314, 89, "" + p2.getSkills().getTotalXp(p2));
			player.getTemporaryAttributtes().put("finding_player", Boolean.FALSE);
		} else if (packetId == PLAYER_OPTION_4_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			if (player.isCantTrade()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + p2.getDisplayName());
				return;
			}

			if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
				p2.getTemporaryAttributtes().remove("TradeTarget");
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.getTemporaryAttributtes().put("TradeTarget", p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		} else if (packetId == PLAYER_OPTION_1_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis()
					|| !player.getControllerManager().canPlayerOption1(p2))
				return;
			if (!World.isPvpArea(new WorldTile(player.getX(), player.getY(), player.getPlane()))
					&& !(player.getControllerManager().getController() instanceof HungerGamesControler)) {
				if (player.getControllerManager().getController() == null) {
					player.stopAll();
					if (p2.isLocked()) {
						player.getPackets().sendGameMessage("The other player is busy.");
						return;
					}
					if (p2.getTemporaryAttributtes().get("DuelChallenged") == player) {
						player.getControllerManager().removeControllerWithoutCheck();
						p2.getControllerManager().removeControllerWithoutCheck();
						p2.getTemporaryAttributtes().remove("DuelChallenged");
						player.setLastDuelRules(new DuelRules(player, p2));
						p2.setLastDuelRules(new DuelRules(p2, player));
						player.setDuelLocation(new WorldTile(player.getX(), player.getY(), player.getPlane()));
						p2.setDuelLocation(new WorldTile(p2.getX(), p2.getY(), p2.getPlane()));
						player.getControllerManager().startController("DuelArena", p2,
								p2.getTemporaryAttributtes().get("DuelFriendly"));
						p2.getControllerManager().startController("DuelArena", player,
								p2.getTemporaryAttributtes().remove("DuelFriendly"));
						return;
					}
					player.getTemporaryAttributtes().put("DuelTarget", p2);
					player.getDialogueManager().startDialogue("OnSpotDueling");
					player.getTemporaryAttributtes().put("WillDuelFriendly", true);
					player.getPackets().sendConfig(283, 67108864);
					return;
				}
			}
			if (!player.isCanPvp())
				return;
			if (!player.getControllerManager().canAttack(p2))
				return;

			if (!player.isCanPvp() || !p2.isCanPvp()) {
				player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
				return;
			}
			if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You are already in combat.");
					return;
				}
				if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
					if (p2.getAttackedBy() instanceof NPC) {
						p2.setAttackedBy(player); // changes enemy to player,
						// player has priority over
						// npc on single areas
					} else {
						player.getPackets().sendGameMessage("That player is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(p2));
		} else if (packetId == ATTACK_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead()) {
				return;
			}
			if (player.getLockDelay() > Utils.currentTimeMillis()) {
				return;
			}
			int npcIndex = stream.readUnsignedShort128();
			boolean forceRun = stream.read128Byte() == 1;
			if (forceRun)
				player.setRun(forceRun);
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId())
					|| !npc.getDefinitions().hasAttackOption()) {
				return;
			}
			if (!player.getControllerManager().canAttack(npc)) {
				return;
			}
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.getPackets().sendGameMessage("You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.getPackets().sendGameMessage("You can't attack this npc.");
					return;
				}
			} else if (!npc.isForceMultiAttacked()) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
						player.getPackets().sendGameMessage("You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
						player.getPackets().sendGameMessage("This npc is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setAction(new PlayerCombat(npc));
		} else if (packetId == INTERFACE_ON_PLAYER) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			final int itemId = stream.readUnsignedShort();
			int playerIndex = stream.readUnsignedShort();
			int interfaceHash = stream.readIntV2();
			int interfaceSlot = stream.readUnsignedShortLE128();
			final boolean forceRun = stream.read128Byte() == 1;
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.stopAll(false);
			if (interfaceId == 679) {
				player.getPackets().sendGameMessage(" hanle item " + itemId + " ");
				if (!player.getControllerManager().processItemOnPlayer(p2, itemId)) {
					return;
				}
				final Item item = player.getInventory().getItem(interfaceSlot);
				if (item == null || item.getId() != itemId)
					return;
				if (LendingManager.isLendedItem(player, item)) {
					Lend lend = LendingManager.getLend(player);
					if (lend == null) {
						return;
					}
					if (!lend.getLender().equals(p2.getUsername())) {
						player.sm("You can't give your lent item to a stranger...");
						return;
					}
					player.getDialogueManager().startDialogue("LendReturn", lend);
					return;
				}
				InventoryOptionsHandler.handleItemOnPlayer(player, p2, item);
				return;
			}
			int npcIndex = stream.readUnsignedShort128();
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			switch (interfaceId) {
			case 1110:
				if (componentId == 87)
					ClansManager.invite(player, p2);
				break;
			case Inventory.INVENTORY_INTERFACE:// Item on player
				Item item = player.getInventory().getItems().lookup(itemId);
				if (!player.getControllerManager().processItemOnPlayer(p2, item, interfaceSlot))
					return;
				if (!player.getControllerManager().processItemOnPlayer(p2, itemId))
					return;
				InventoryOptionsHandler.handleItemOnPlayer(player, p2, item);
				break;
			case 662:
			case 747:
				if (npc.getId() == 14301) {
					Glacor glacor = (Glacor) npc;
					if (glacor != null) {
						if (glacor.getTarget() == null) {
							if (player.getAttackedByDelay() > Utils.currentTimeMillis()) {
								return;
							} else {
								glacor.setTarget(player);
								player.setAttackedByDelay(10000);
								return;
							}
						}
						if (!glacor.attackable(player)) {
							return;
						}

						player.setAttackedByDelay(10000);
					}
				}
				if (npc.getId() == 14302) {
					UnstableGlacyte unstable = (UnstableGlacyte) npc;
					if (unstable != null) {
						if (unstable.getTarget() == null)
							unstable.setTarget(player);
						if (!unstable.attackable(player)) {
							return;
						}
					}
				}
				if (npc.getId() == 14303) {
					SappingGlacyte sapping = (SappingGlacyte) npc;
					if (sapping != null) {
						if (sapping.getTarget() == null)
							sapping.setTarget(player);
						if (!sapping.attackable(player)) {
							return;
						}
					}
				}
				if (npc.getId() == 14304) {
					EnduringGlacyte enduring = (EnduringGlacyte) npc;
					if (enduring != null) {
						if (enduring.getTarget() == null)
							enduring.setTarget(player);
						if (!enduring.attackable(player)) {
							return;
						}
					}
				}
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 24
							|| interfaceId == 747 && componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.sm("You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(p2)) {
						player.sm("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18);
						player.getFamiliar().setTarget(p2);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()),
								p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControllerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
									.sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
								player.getPackets().sendGameMessage(
										"That " + (player.getAttackedBy() instanceof Player ? "player" : "npc")
												+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets().sendGameMessage("That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(p2));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 56: // magic dart
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()),
								p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControllerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets()
									.sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2
									&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
								player.getPackets().sendGameMessage(
										"That " + (player.getAttackedBy() instanceof Player ? "player" : "npc")
												+ " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets().sendGameMessage("That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(p2));
					}
					break;
				}
				break;
			}
			if (Settings.DEBUG)
				System.out.println("Spell:" + componentId);
		} else if (packetId == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getLockDelay() > Utils.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int interfaceHash = stream.readInt();
			int npcIndex = stream.readUnsignedShortLE();
			int interfaceSlot = stream.readUnsignedShortLE128();
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			player.stopAll(false);
			if (interfaceId != Inventory.INVENTORY_INTERFACE) {
				if (!npc.getDefinitions().hasAttackOption()) {
					player.getPackets().sendGameMessage("You can't attack this npc.");
					return;
				}
			}
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(interfaceSlot);
				if (item == null || !player.getControllerManager().processItemOnNPC(npc, item))
					return;
				InventoryOptionsHandler.handleItemOnNPC(player, npc, item);
				break;
			case 1165:
				// Summoning.attackDreadnipTarget(npc, player);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65)
						|| (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18
						|| interfaceId == 747 && componentId == 24) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.sm("You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar().canAttack(familiar.getOwner())) {
							player.sm("You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.sm("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
								npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControllerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage("You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage("You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(npc));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 91: // fire surge
				case 99: // storm of Armadyl
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
								npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControllerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage("You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage("You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(new PlayerCombat(npc));
					}
					break;
				}
				break;
			}
			if (Settings.DEBUG)
				System.out.println("Spell:" + componentId);
		} else if (packetId == NPC_CLICK1_PACKET)
			NPCHandler.handleOption1(player, stream);
		else if (packetId == NPC_CLICK2_PACKET)
			NPCHandler.handleOption2(player, stream);
		else if (packetId == NPC_CLICK3_PACKET)
			NPCHandler.handleOption3(player, stream);
		else if (packetId == NPC_CLICK4_PACKET)
			NPCHandler.handleOption4(player, stream);
		else if (packetId == OBJECT_CLICK1_PACKET)
			ObjectHandler.handleOption(player, stream, 1);
		else if (packetId == OBJECT_CLICK2_PACKET)
			ObjectHandler.handleOption(player, stream, 2);
		else if (packetId == OBJECT_CLICK3_PACKET)
			ObjectHandler.handleOption(player, stream, 3);
		else if (packetId == OBJECT_CLICK4_PACKET)
			ObjectHandler.handleOption(player, stream, 4);
		else if (packetId == OBJECT_CLICK5_PACKET)
			ObjectHandler.handleOption(player, stream, 5);
		else if (packetId == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() > currentTime)
				// || player.getFreezeDelay() >= currentTime)
				return;
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final int id = stream.readUnsignedShort();
			boolean forceRun = stream.read128Byte() == 1;
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			if (forceRun)
				player.setRun(forceRun);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
					if (item == null)
						return;
					if (item.getOwner() != player.getDisplayName()) {
						if (player.isIronman()) {
							player.getDialogueManager().startDialogue("IronMan");
							return;
						}
					}
					player.setNextFaceWorldTile(tile);
					player.addWalkSteps(tile.getX(), tile.getY(), 1);
					World.removeGroundItem(player, item);
				}
			}));
		}
	}

	public void processPackets(final int packetId, InputStream stream, int length) throws Exception {
		player.setPacketsDecoderPing(Utils.currentTimeMillis());
		if (packetId == PING_PACKET) {
			// kk we ping :) NO ALEX
			// OutputStream packet = new OutputStream(0);
			// packet.writePacket(player, 153);
			// player.getSession().write(packet);
		} else if (packetId == MOUVE_MOUSE_PACKET) {
			// USELESS PACKET
		} else if (packetId == KEY_TYPED_PACKET) {
			int keyPressed = stream.readByte();
			switch (keyPressed) {
			case 13:
				player.getInterfaceManager().closeScreenInterface();
				return;
			default:
				System.out.println(keyPressed);
			}
			// USELESS PACKET
		} else if (packetId == RECEIVE_PACKET_COUNT_PACKET) {
			// interface packets
			stream.readInt();
		} else if (packetId == ITEM_ON_ITEM_PACKET) {
			InventoryOptionsHandler.handleItemOnItem(player, stream);
		} else if (packetId == ITEM_ON_FLOOR_EXAMINE) {
			int y = stream.readUnsignedShort(); // CLIENT SENDS THEM BACKWARDS
												// ON PURPOSE
			int x = stream.readUnsignedShortLE();
			int id = stream.readUnsignedShort();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			player.getPackets().sendItemMessage(0, 15263739, id, x, y, ItemExamines.getExamine(new Item(id))); // ChatboxMessage
		} else if (packetId == MAGIC_ON_ITEM_PACKET) {
			int inventoryInter = stream.readInt() >> 16;
			int itemId = stream.readShort128();
			@SuppressWarnings("unused")
			int junk = stream.readShort();
			@SuppressWarnings("unused")
			int itemSlot = stream.readShortLE();
			int interfaceSet = stream.readIntV1();
			int spellId = interfaceSet & 0xFFF;
			int magicInter = interfaceSet >> 16;
			if (inventoryInter == 149 && magicInter == 430) {
				switch (spellId) {
				case 33:
					LunarSpells.PlankMaking(player);
					return;
				}
			}

			if (inventoryInter == 149 && magicInter == 192) {
				switch (spellId) {
				case 59:// High Alch
					if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
						player.getPackets().sendGameMessage("You do not have the required level to cast this spell.");
						return;
					}
					if (itemId == 995) {
						player.getPackets().sendGameMessage("You can't alch this!");
						return;
					}
					if (player.getEquipment().getWeaponId() == 1401 || player.getEquipment().getWeaponId() == 3054
							|| player.getEquipment().getWeaponId() == 19323) {
						if (!player.getInventory().containsItem(561, 1)) {
							player.getPackets()
									.sendGameMessage("You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(9633));
						player.setNextGraphics(new Graphics(112));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue() >> 6);
					} else {
						if (!player.getInventory().containsItem(561, 1)
								|| !player.getInventory().containsItem(554, 5)) {
							player.getPackets()
									.sendGameMessage("You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(713));
						player.setNextGraphics(new Graphics(113));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(554, 5);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue() >> 6);
					}
					break;
				default:
					System.out.println("Spell:" + spellId + ", Item:" + itemId);
				}
				System.out.println("Spell:" + spellId + ", Item:" + itemId);
			}

		} else if (packetId == AFK_PACKET) {
			player.getSession().getChannel().close();
		} else if (packetId == CLOSE_INTERFACE_PACKET) {
			if (player.hasStarted() && !player.hasFinished() && !player.isRunning()) { // used
																						// for
																						// old
																						// welcome
																						// screen
				player.run();
				return;
			}
			player.stopAll();
		} else if (packetId == ESCAPE_PRESSED_PACKET) {
			player.closeInterfaces();
		} else if (packetId == MOVE_CAMERA_PACKET) {
			// not using it atm
			stream.readUnsignedShort();
			stream.readUnsignedShort();
		} else if (packetId == PLAYER_OPTION_9_PACKET) {
			boolean forceRun = stream.readUnsignedByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked())
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			ClansManager.viewInvite(player, p2);
		} else if (packetId == IN_OUT_SCREEN_PACKET) {
			// not using this check because not 100% efficient
			@SuppressWarnings("unused")
			boolean inScreen = stream.readByte() == 1;
		} else if (packetId == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());
			@SuppressWarnings("unused")
			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if (!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode()
					|| !player.getInterfaceManager().containsInterface(742))
				return;
			player.setDisplayMode(displayMode);
			player.getInterfaceManager().removeAll();
			player.getInterfaceManager().sendInterfaces();
			player.getInterfaceManager().sendInterface(742);
		} else if (packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE128();
			int mouseButton = mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); // time
			int positionHash = stream.readIntV1();
			int y = positionHash >> 16; // y;
			int x = positionHash - (y << 16); // x
			@SuppressWarnings("unused")
			boolean clicked;
			// mass click or stupid autoclicker, lets stop lagg
			if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0 || y > player.getScreenHeight()) {
				// player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		} else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			int interfaceHash = stream.readInt();
			int junk = stream.readShort128();
			int interfaceId = interfaceHash >> 16;
			int buttonId = interfaceHash & 0xFF;
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				Logger.log("Dialogue_Continue_Packet error!");
				return;
			}
			if (!player.isRunning()) {
				return;
			}
			if (Settings.DEBUG) {
				Logger.log(this, "Dialogue: " + interfaceId + ", " + buttonId + ", " + junk);
			}
			int componentId = interfaceHash - (interfaceId << 16);
			if (interfaceId == 667) {
				player.getInterfaceManager().removeScreenInterface();
				CosmeticsHandler.openCosmeticsHandler(player);
				if (junk == Equipment.SLOT_SHIELD) {
					Item weapon = player.getEquipment().getCosmeticItems().get(Equipment.SLOT_WEAPON);
					if (weapon != null) {
						boolean isTwoHandedWeapon = Equipment.isTwoHandedWeapon(weapon);
						if (isTwoHandedWeapon) {
							player.getPackets().sendGameMessage(
									"You have a two handed cosmetic weapon. You can't edit shield slot unless you remove it.");
							return;
						}
					}
				}
				player.getDialogueManager().startDialogue(
						(player.isShowSearchOption() && player.getEquipment().getCosmeticItems().get(junk) == null)
								? "CosmeticsSelect"
								: "CosmeticsD",
						junk);
			} else
				player.getDialogueManager().continueDialogue(interfaceId, componentId);
		} else if (packetId == WORLD_MAP_CLICK) {
			int coordinateHash = stream.readInt();
			int x = coordinateHash >> 14;
			int y = coordinateHash & 0x3fff;
			int plane = coordinateHash >> 28;
			Integer hash = (Integer) player.getTemporaryAttributtes().get("worldHash");
			if (hash == null || coordinateHash != hash)
				player.getTemporaryAttributtes().put("worldHash", coordinateHash);
			else {
				player.getTemporaryAttributtes().remove("worldHash");
				player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2, -1, true);
				player.getPackets().sendConfig(1159, coordinateHash);
			}
		} else if (packetId == WORLD_LIST_UPDATE) {
			int updateType = stream.readInt();
			player.getPackets().sendWorldList(updateType == 0);
		} else if (packetId == LOBBY_MAIN_CLICK_PACKET) {// add these near the
															// bottom
			int idk1 = stream.readShort();
			String idk2 = stream.readString();
			String idk3 = stream.readString();
			String idk4 = stream.readString();
			int idk5 = stream.readByte();
			if (idk3.equalsIgnoreCase("account_settings.ws?mod=recoveries")) {
				// open recover question link
			} else if (idk3.equalsIgnoreCase("account_settings.ws?mod=email")) {
				// opens players email
			} else if (idk3.equalsIgnoreCase("account_settings.ws?mod=messages")) {
				// opens players messages
			} else if (idk3.equalsIgnoreCase("purchasepopup.ws?externalName=rs")) {
				// open donation page
			}
		} else if (packetId == LOBBY_FRIEND_CHAT_SETTINGS) {
			int idk = stream.readByte();
			int status = stream.readByte();
			int idk3 = stream.readByte();
			player.getFriendsIgnores().setPrivateStatus(status);
		} else if (packetId == ACTION_BUTTON1_PACKET || packetId == ACTION_BUTTON2_PACKET
				|| packetId == ACTION_BUTTON4_PACKET || packetId == ACTION_BUTTON5_PACKET
				|| packetId == ACTION_BUTTON6_PACKET || packetId == ACTION_BUTTON7_PACKET
				|| packetId == ACTION_BUTTON8_PACKET || packetId == ACTION_BUTTON3_PACKET
				|| packetId == ACTION_BUTTON9_PACKET || packetId == ACTION_BUTTON10_PACKET) {
			ButtonHandler.handleButtons(player, stream, packetId);
		} else if (packetId == DROP_ITEM_PACKET) {
			@SuppressWarnings("unused")
			int interfaceHash = stream.readIntV2();
			final int slotId2 = stream.readUnsignedShort128();
			final int slotId = stream.readUnsignedShortLE128();
			if (slotId > 27 || player.getInterfaceManager().containsInventoryInter())
				return;
			Item item = player.getInventory().getItem(slotId);
			if (item == null || item.getId() != slotId2)
				return;
			InventoryOptionsHandler.handleItemOption7(player, slotId, slotId2, item);
		} else if (packetId == ENTER_NAME_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			if (value.equals("")) {
				return;
			}
			InputEvent<?> inputEvent = player.getInputEvent();
			if (inputEvent instanceof InputNameEvent) {
				((InputNameEvent) inputEvent).onEvent(value);
				player.resetInputEvent();
				return;
			}
			if (player.getTemporaryAttributtes().remove("SaveCosmetic") != null) {
				if (value.equals(""))
					return;
				ItemsContainer<Item> cosmeticItems = new ItemsContainer<Item>(19, false);
				for (int i = 0; i < player.getEquipment().getCosmeticItems().getSize(); i++) {
					cosmeticItems.set(i, player.getEquipment().getCosmeticItems().get(i));
				}
				SavedCosmetic cosmetic = new SavedCosmetic(value, cosmeticItems);
				player.getEquipment().getSavedCosmetics().add(cosmetic);
				player.getPackets().sendGameMessage("<col=00ff00>You have succecfully added " + value
						+ " to your saved costumes. You can find it by doing ::cosmetics and clicking on ring slot.");
				return;
			}
            if (player.getTemporaryAttributtes().get("CosmeticsKeyWord") != null) {
                int slotId = (int) player.getTemporaryAttributtes().remove("CosmeticsKeyWord");
                int choosenOption = (int) player.getTemporaryAttributtes().remove("CosmeticsChoosenOption");
                if (value.equals(""))
                    return;
                if (choosenOption != -1)
                    player.getDialogueManager().startDialogue("CosmeticsD", slotId, value, choosenOption);
                else
                    player.getDialogueManager().startDialogue("CosmeticsD", slotId, value);
                return;
            }
            if (player.getTemporaryAttributtes().remove("CosmeticsStoreKeyWord") != null) {
                if (value.equals(""))
                    return;
                CosmeticsHandler.openCosmeticsStore(player, value, 0);
                return;
            }
			if (player.getTemporaryAttributtes().get("editing_shop_item") != null) {
				player.getTemporaryAttributtes().remove("editing_shop_item");
			}
			if (player.getInterfaceManager().containsInterface(1108))
				player.getFriendsIgnores().setChatPrefix(value);
			else if (player.getTemporaryAttributtes().remove("setclan") != null)
				ClansManager.createClan(player, value);
			else if (player.getTemporaryAttributtes().remove("joinguestclan") != null)
				ClansManager.connectToClan(player, value, true);
			else if (player.getTemporaryAttributtes().remove("joinguesthouse") != null)
				House.enterHouse(player, value);
			else if (player.getTemporaryAttributtes().remove("banclanplayer") != null)
				ClansManager.banPlayer(player, value);
			else if (player.getTemporaryAttributtes().remove("unbanclanplayer") != null)
				ClansManager.unbanPlayer(player, value);
			else {
				Boss boss = (Boss) player.getTemporaryAttributtes().remove(Key.JOIN_BOSS_INSTANCE);
				if (boss != null)
					BossInstanceHandler.joinInstance(player, boss, value.toLowerCase(), false);
			}
			if (player.getInterfaceManager().containsInterface(1108))
				player.getFriendsIgnores().setChatPrefix(value);
			else if (player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
				if (value.length() != 6) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else if (Utils.containsInvalidCharacter(value) || value.contains("_")) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"The requested yell color can only contain numeric and regular characters.");
				} else {
					player.setYellColor(value);
					player.getDialogueManager().startDialogue("SimpleMessage",
							"Your yell color has been changed to <col=" + player.getYellColor() + ">"
									+ player.getYellColor() + "</col>.");
				}
				player.getTemporaryAttributtes().put("yellcolor", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("kickinstanceplayer") == Boolean.TRUE) {
				Player partner = World.getPlayerByDisplayName(value);
				if (partner == player) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("You can't kick yourself.");
				}
				if (partner == null) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("Couldn't find player.");
					return;
				}
				if (partner.getLeaderName() == player) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("You have kicked " + partner.getDisplayName() + ".");
					partner.sm("You were kicked by the leader.");
					partner.setInstanceKick(true);
				} else {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("This player doesn't have an instance.");
				}
				player.getTemporaryAttributtes().put("kickinstanceplayer", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("instance_name_hm") == Boolean.TRUE) {
				Player leader = World.getPlayerByDisplayName(value);
				player.setLeaderName(leader);
				if (leader == player) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("You can't join your own group.");
				}
				if (leader == null) {
					player.setLeaderName(null);
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("Couldn't find player.");
					return;
				}
				if ((leader.getControllerManager().toString() == null)) {
					player.sm("This player isn't in an instance.");
					return;
				}
				if (!leader.getControllerManager().toString().contains(player.instanceDialogue)) {
					player.setLeaderName(null);
					player.sm("That player isn't in the correct instance you're trying to join.");
					player.getInterfaceManager().closeChatBoxInterface();
					return;
				}
				if (leader.getInstancePin() > 0 && leader.getInstancePin() != 1 && leader.getInstancePin() != 2) {
					player.setLeaderName(leader);
					player.setInstancePin(0);
					player.getTemporaryAttributtes().put("leader_pin_hm", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter the leader's pin:" });
					player.getInterfaceManager().closeChatBoxInterface();
				} else if (leader.getInstancePin() == 1) {
					player.setLeaderName(null);
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("This player doesn't allow players.");
				} else if (leader.getInstancePin() == 2) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.setLeaderName(leader);
					if (leader.joined.size() > 6) {
						player.setLeaderName(null);
						player.sm("Hard mode trio doesn't allow more than 6 players in an instance.");
						return;
					}
					player.sm("Connecting to player...");
					player.getControllerManager().startController("SharedInstanceHardMode");
				} else {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("This player doesn't have an instance.");
				}
				player.getTemporaryAttributtes().put("instance_name_hm", Boolean.FALSE);

			} else if (player.getTemporaryAttributtes().get("instance_name") == Boolean.TRUE) {
				Player leader = World.getPlayerByDisplayName(value);
				player.setLeaderName(leader);
				if (leader == player) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("You can't join your own group.");
				}
				if (leader == null) {
					player.setLeaderName(null);
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("Couldn't find player.");
					return;
				}
				if (!leader.getControllerManager().toString().contains(player.instanceDialogue)) {
					player.setLeaderName(null);
					player.sm("That player isn't in the correct instance you're trying to join.");
					player.getInterfaceManager().closeChatBoxInterface();
					return;
				}
				if (leader.getInstancePin() > 0 && leader.getInstancePin() != 1 && leader.getInstancePin() != 2) {
					player.setLeaderName(leader);
					player.setInstancePin(0);
					player.getTemporaryAttributtes().put("leader_pin", Boolean.TRUE);
					player.getPackets().sendRunScript(108, new Object[] { "Please enter the leader's pin:" });
					player.getInterfaceManager().closeChatBoxInterface();
				} else if (leader.getInstancePin() == 1) {
					player.setLeaderName(null);
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("This player doesn't allow players.");
				} else if (leader.getInstancePin() == 2) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.setLeaderName(leader);
					player.sm("Connecting to player...");
					player.getControllerManager().startController("SharedInstance");
				} else {
					player.setLeaderName(null);
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("This player doesn't have an instance.");
				}
				player.getTemporaryAttributtes().put("instance_name", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("view_name") == Boolean.TRUE) {
				player.getTemporaryAttributtes().remove("view_name");
				Player other = World.getPlayerByDisplayName(value);
				if (other == null) {
					player.getPackets().sendGameMessage("Couldn't find player.");
					return;
				}
				ClanWars clan = other.getCurrentFriendChat() != null ? other.getCurrentFriendChat().getClanWars()
						: null;
				if (clan == null) {
					player.getPackets().sendGameMessage("This player's clan is not in war.");
					return;
				}
				if (clan.getSecondTeam().getOwnerDisplayName() != other.getCurrentFriendChat().getOwnerDisplayName()) {
					player.getTemporaryAttributtes().put("view_prefix", 1);
				}
				player.getTemporaryAttributtes().put("view_clan", clan);
				ClanWars.enter(player);
			} else if (player.getTemporaryAttributtes().remove("setdisplay") != null) {
				if (Utils.invalidAccountName(Utils.formatPlayerNameForProtocol(value))) {
					player.getPackets().sendGameMessage("Invalid name!");
					return;
				}
				if (!DisplayNames.setDisplayName(player, value)) {
					player.getPackets().sendGameMessage("Name already in use!");
					return;
				}
				player.getPackets().sendGameMessage("Changed display name!");
			}
		} else if (packetId == ENTER_LONG_TEXT_PACKET) {
			if (!player.isRunning() || player.isDead()) {
				return;
			}
			String value = stream.readString();
			if (value.equals("")) {
				return;
			}
			if (player.getTemporaryAttributtes().remove("entering_note") == Boolean.TRUE) {
				player.getNotes().add(value);
			} else if (player.getTemporaryAttributtes().remove("editing_note") == Boolean.TRUE) {
				player.getNotes().edit(value);
			} else if (player.getTemporaryAttributtes().remove("change_pass") == Boolean.TRUE) {
				if (value.length() < 5 || value.length() > 15) {
					player.getPackets().sendGameMessage("Password length is limited to 5-15 characters.");
					return;
				}
				player.setPassword(Encrypt.encryptSHA1(value));
				player.getPackets()
						.sendGameMessage("You have changed your password! Your new password is \"" + value + "\".");
			} else if (player.getTemporaryAttributtes().remove("change_troll_name") == Boolean.TRUE) {
				value = Utils.formatPlayerNameForDisplay(value);
				if (value.length() < 3 || value.length() > 14) {
					player.getPackets()
							.sendGameMessage("You can't use a name shorter than 3 or longer than 14 characters.");
					return;
				}
				if (value.equalsIgnoreCase("none")) {
					player.getPetManager().setTrollBabyName(null);
				} else {
					player.getPetManager().setTrollBabyName(value);
					if (player.getPet() != null && player.getPet().getId() == Pets.TROLL_BABY.getBabyNpcId()) {
						player.getPet().setName(value);
					}
				}
			} else if (player.getTemporaryAttributtes().remove("servermsg") == Boolean.TRUE) {
				player.getTemporaryAttributtes().put("servermsg", Boolean.FALSE);
				World.sendWorldMessage("<col=ff0000>[Sever Message]: " + Utils.fixChatMessage(value), false, false);
			} else if (player.getTemporaryAttributtes().remove("yellcolor") == Boolean.TRUE) {
				if (value.length() != 6) {
					player.getPackets().sendGameMessage(
							"The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else if (Utils.containsInvalidCharacter(value) || value.contains("_")) {
					player.getPackets().sendGameMessage(
							"The requested yell color can only contain numeric and regular characters.");
				} else {
					player.setYellColor(value);
					player.getPackets().sendGameMessage("Your yell color has been changed to <col="
							+ player.getYellColor() + ">" + player.getYellColor() + "</col>.");
				}
			} else if (player.getTemporaryAttributtes().remove("setdisplay") == Boolean.TRUE) {
				if (Utils.invalidAccountName(Utils.formatPlayerNameForProtocol(value))) {
					player.getPackets().sendGameMessage("Name contains invalid characters or is too short/long.");
					return;
				}
				if (!DisplayNames.setDisplayName(player, value)) {
					player.getPackets().sendGameMessage("This name is already in use.");
					return;
				}
				player.getPackets().sendGameMessage("Your display name was successfully changed.");
			} else if (player.getInterfaceManager().containsInterface(1103)) {
				ClansManager.setClanMottoInterface(player, value);
			}
		} else if (packetId == ENTER_STRING_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			if (value.equals(""))
				return;
		} else if (packetId == ENTER_INTEGER_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			int value = stream.readInt();
			InputEvent<?> inputEvent = player.getInputEvent();
			if (inputEvent instanceof InputIntegerEvent) {
				((InputIntegerEvent) inputEvent).onEvent(value);
				player.resetInputEvent();
				return;
			}
			if (player.getTemporaryAttributtes().get(Key.ADD_X_TO_POUCH) != null) {
				if (value > player.getInventory().getAmountOf(995))
					value = player.getInventory().getAmountOf(995);
				if (value <= 0) {
					player.getTemporaryAttributtes().remove(Key.ADD_X_TO_POUCH);
					return;
				}
				player.getMoneyPouch().sendDynamicInteraction(value, false, MoneyPouch.TYPE_POUCH_INVENTORY);
				player.getTemporaryAttributtes().remove(Key.ADD_X_TO_POUCH);
				return;
			}
			if (player.getTemporaryAttributtes().remove("LOOTBEAM_VALUE") != null) {
				if (value < 1000) {
					player.out("You cannot set your value this low, setting to one thousand.");
					player.setLootBeamValue(1000);
				} else if (value > 1000000) {
					player.out("You cannot set your value this high, setting to one million.");
					player.setLootBeamValue(1000000);
				} else
					player.setLootBeamValue(value);
				LootbeamSettings.sendInterface(player, false);
			}
			if (player.getTemporaryAttributtes().remove("SOF") != null) {
				if (value > player.getSquealOfFortune().getTotalSpins()) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You do not have this many spins.");
					player.getTemporaryAttributtes().remove("SOF");
				} else
					player.getSquealOfFortune().quickSpin(value);
				player.getTemporaryAttributtes().remove("SOF");
			}
			if (player.getTemporaryAttributtes().get("edit_price") != null) {
				if (value < 0) {
					player.getTemporaryAttributtes().remove("edit_price");
					return;
				}
				if (player.getTemporaryAttributtes().get("edit_price") == Boolean.TRUE) {
					player.getTemporaryAttributtes().put("edit_price", Boolean.FALSE);
					player.getGEManager().modifyPricePerItem(value);
					player.getTemporaryAttributtes().remove("edit_price");
				}
				return;
			}
			if (player.getTemporaryAttributtes().get("WellOfExperience") == Boolean.TRUE) {
				try {
					XPWell.addWellAmount(player, value);
					player.getTemporaryAttributtes().put("WellOfExperience", Boolean.FALSE);
				} catch (Exception e) {
					player.getDialogueManager().startDialogue("SimpleMessage", "Invalid format.");
				}
			}
			if (player.getTemporaryAttributtes().get("edit_quantity") != null) {
				if (value < 0) {
					player.getTemporaryAttributtes().remove("edit_quantity");
					return;
				}
				if (player.getTemporaryAttributtes().get("edit_quantity") == Boolean.TRUE) {
					player.getTemporaryAttributtes().put("edit_quantity", Boolean.FALSE);
					player.getGEManager().modifyAmount(value);
					player.getTemporaryAttributtes().remove("edit_quantity");
				}
				return;
			}
			if ((player.getInterfaceManager().containsInterface(762)
					&& player.getInterfaceManager().containsInterface(763))
					|| player.getInterfaceManager().containsInterface(11)) {
				if (value < 0)
					return;
				Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bank_item_X_Slot");
				if (bank_item_X_Slot == null)
					return;
				player.getBank().setLastX(value);
				player.getBank().refreshLastX();
				if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null)
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				else
					player.getBank().depositItem(bank_item_X_Slot, value,
							player.getInterfaceManager().containsInterface(11) ? false : true);
			} else if (player.getTemporaryAttributtes().get("Repair") != null) {
				int repairId = (int) player.getTemporaryAttributtes().get("Ritem");
				if (!player.getInventory().containsItem(repairId, value))
					value = player.getInventory().getAmountOf(repairId);
				RepairItems.repair(player, repairId, value);
				player.getTemporaryAttributtes().remove("Repair");
			} else if (player.getTemporaryAttributtes().get("Write_pin") == Boolean.TRUE) {
				player.getTemporaryAttributtes().put("Write_pin", Boolean.FALSE);
				Bank.WritePin(player, value, true);
			} else if (player.getTemporaryAttributtes().get("Remove_pin") == Boolean.TRUE) {
				player.getTemporaryAttributtes().put("Remove_pin", Boolean.FALSE);
				Bank.RemovePin(player, value, true);
			} else if (player.getInterfaceManager().containsInterface(206)
					&& player.getInterfaceManager().containsInterface(207)) {
				if (value < 0)
					return;
				Integer pc_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null)
					player.getPriceCheckManager().removeItem(pc_item_X_Slot, value);
				else
					player.getPriceCheckManager().addItem(pc_item_X_Slot, value);
			} else if (player.getTemporaryAttributtes().remove("withdrawingPouch") == Boolean.TRUE) {
				player.getMoneyPouch().sendDynamicInteraction(value, true, MoneyPouch.TYPE_POUCH_INVENTORY);
			} else if (player.getTemporaryAttributtes().get("leader_pin") == Boolean.TRUE) {
				Player leader = player.getLeaderName();
				if (value == leader.getInstancePin()) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("Pin accepted");
					player.getControllerManager().startController("SharedInstance");
				} else {
					player.setLeaderName(null);
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("Incorrect pin.");
					player.setTimer(0);
				}
				player.getTemporaryAttributtes().put("leader_pin", Boolean.FALSE);

			} else if (player.getTemporaryAttributtes().get("leader_pin_hm") == Boolean.TRUE) {
				Player leader = player.getLeaderName();
				if (value == leader.getInstancePin()) {
					player.getInterfaceManager().closeChatBoxInterface();
					if (leader.joined.size() > 6) {
						player.setLeaderName(null);
						player.sm("Hard mode trio doesn't allow more than 6 players in an instance.");
						return;
					}
					player.sm("Pin accepted");
					player.getControllerManager().startController("SharedInstanceHardMode");
				} else {
					player.setLeaderName(null);
					player.getInterfaceManager().closeChatBoxInterface();
					player.sm("Incorrect pin.");
					player.setTimer(0);
				}
				player.getTemporaryAttributtes().put("leader_pin_hm", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("Sawing") != null && player.getTemporaryAttributtes().get("Sawitem") != null) {
				int amount = value;
				int itemId = ((Integer) player.getTemporaryAttributtes().remove("Sawitem")).intValue();
				Item item = new Item(itemId);
				if (amount == 0) {
					player.getDialogueManager().startDialogue("SimpleMessage", "...");
					return;
				}
				if (player.getInventory().containsItem(960, amount)) {
					player.getInventory().deleteItem(960, amount);
					player.setNextAnimation(new Animation(9031));
					player.getSkills().addXp(Skills.CONSTRUCTION, 25 * amount);
					return;
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You dont have " + amount + " " + item.getName() + "s.");
					return;
				}
			}
			if (player.getInterfaceManager().containsInterface(403)
					&& player.getTemporaryAttributtes().get("PlanksConvert") != null) {
				Sawmill.convertPlanks(player, (Plank) player.getTemporaryAttributtes().remove("PlanksConvert"), value);
			}
			if (player.getInterfaceManager().containsInterface(902)
					&& player.getTemporaryAttributtes().get("PlankMake") != null) {
				Integer type = (Integer) player.getTemporaryAttributtes().remove("PlankMake");
				if (player.getControllerManager().getController() instanceof SawmillController)
					((SawmillController) player.getControllerManager().getController()).cutPlank(type, value);
			}
			if (player.getInterfaceManager().containsInterface(903)
					&& player.getTemporaryAttributtes().get("PlankWithdraw") != null) {
				Integer type = (Integer) player.getTemporaryAttributtes().remove("PlankWithdraw");
				if (player.getControllerManager().getController() instanceof SawmillController)
					((SawmillController) player.getControllerManager().getController()).withdrawFromCart(type, value);
			} else if (player.getInterfaceManager().containsInterface(671)
					&& player.getInterfaceManager().containsInterface(665)) {
				if (player.getFamiliar() == null || player.getFamiliar().getBob() == null)
					return;
				if (value < 0)
					return;
				Integer bob_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob().removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob().addItem(bob_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(335)
					&& player.getInterfaceManager().containsInterface(336)) {
				if (value < 0)
					return;
				if (player.getTemporaryAttributtes().get("lend_item_time") != null) {
					if (value <= 0)
						return;
					Integer slot = (Integer) player.getTemporaryAttributtes().remove("lend_item_time");
					if (value > 24) {
						player.getPackets().sendGameMessage("You can only lend for a maximum of 24 hours");
						return;
					}
					// player.getTrade().lendItem(slot, value);
					player.getTemporaryAttributtes().remove("lend_item_time");
					return;
				}
				Integer trade_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("trade_item_X_Slot");
				if (trade_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("trade_isRemove") != null)
					player.getTrade().removeItem(trade_item_X_Slot, value);
				else
					player.getTrade().addItem(trade_item_X_Slot, value);
			} else if (player.getTemporaryAttributtes().get("createinstancepin") == Boolean.TRUE) {
				if (value != 0 && value < 2147483647) {
					player.setInstancePin(value);
					player.sm("Pin created. Your pin is " + player.getInstancePin() + ".");
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "Invalid input.");
				}
				player.getInterfaceManager().closeChatBoxInterface();
				player.getTemporaryAttributtes().put("createinstancepin", Boolean.FALSE);

			} else if (player.getTemporaryAttributtes().get("startaninstancepin") == Boolean.TRUE) {
				if (value != 0 && value < 2147483647) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.setInstancePin(value);
					player.sm("Pin created. Your pin is " + player.getInstancePin());
					if (player.getInventory().contains(995, 200000)) {
						player.getInventory().deleteItem(995, 200000);
						player.sm("Enjoy your instance, the NPCs will stop spawning in an hour.");
						player.getControllerManager().startController(player.getInstanceControler());
					} else {
						player.sm("Sorry, but you don't have enough money.");
					}
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "Invalid input.");
				}
				player.getTemporaryAttributtes().put("startaninstancepin", Boolean.FALSE);

			} else if (player.getTemporaryAttributtes().get("startinstancepin") == Boolean.TRUE) {
				if (value != 0 && value < 2147483647) {
					player.getInterfaceManager().closeChatBoxInterface();
					player.setInstancePin(value);
					player.sm("Pin created. Your pin is " + player.getInstancePin());
					if (player.getInventory().contains(995, 200000)) {
						player.getInventory().deleteItem(995, 200000);
						player.sm("Enjoy your instance, the NPCs will stop spawning in an hour.");
						player.getControllerManager().startController(player.getInstanceControler());
					} else {
						player.sm("Sorry, but you don't have enough money.");
					}
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "Invalid input.");
				}
				player.getTemporaryAttributtes().put("startinstancepin", Boolean.FALSE);

			} else if (player.getTemporaryAttributtes().get("startfreeinstancepin") == Boolean.TRUE) {
				if (value != 0 && value < 2147483647) {
					player.setInstancePin(value);
					player.sm("Pin created. Your pin is " + player.getInstancePin());
					player.sm("Enjoy your instance, the NPCs will stop spawning in an hour.");
					player.getInterfaceManager().closeChatBoxInterface();
					player.getControllerManager().startController(player.getInstanceControler());
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "Invalid input.");
				}
				player.getTemporaryAttributtes().put("startfreeinstancepin", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().get("skillId") != null) {
				if (player.getEquipment().wearingArmour()) {
					player.getDialogueManager().finishDialogue();
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You cannot do this while having armour on!");
					return;
				}
				int skillId = (Integer) player.getTemporaryAttributtes().remove("skillId");
				if (skillId == Skills.HITPOINTS && value <= 9)
					value = 10;
				else if (value < 1)
					value = 1;
				else if (value > 99)
					value = 99;
				player.getSkills().set(skillId, value);
				player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
				player.getAppearence().generateAppearenceData();
				player.getDialogueManager().finishDialogue();
			} else if (player.getTemporaryAttributtes().get("kilnX") != null) {
				int index = (Integer) player.getTemporaryAttributtes().get("scIndex");
				int componentId = (Integer) player.getTemporaryAttributtes().get("scComponentId");
				int itemId = (Integer) player.getTemporaryAttributtes().get("scItemId");
				player.getTemporaryAttributtes().remove("kilnX");
			} else if (player.getTemporaryAttributtes().get("xpSkillTarget") != null) {
				int xpTarget = value;
				Integer skillId = (Integer) player.getTemporaryAttributtes().remove("xpSkillTarget");
				if (xpTarget < player.getSkills().getXp(player.getSkills().getSkillIdByTargetId(skillId))
						|| player.getSkills().getXp(player.getSkills().getSkillIdByTargetId(skillId)) >= 200000000) {
					return;
				}
				if (xpTarget > 500000000) {
					xpTarget = 500000000;
				}

				player.getSkills().setSkillTarget(false, skillId, xpTarget);

			} else if (player.getTemporaryAttributtes().get("levelSkillTarget") != null) {
				int levelTarget = value;
				Integer skillId = (Integer) player.getTemporaryAttributtes().remove("levelSkillTarget");
				int curLevel = player.getSkills().getLevel(player.getSkills().getSkillIdByTargetId(skillId));
				if (curLevel >= (skillId == 24 ? 120 : 120)) {
					return;
				}
				if (levelTarget > (skillId == 24 ? 120 : 120)) {
					levelTarget = skillId == 24 ? 120 : 120;
				}
				if (levelTarget < player.getSkills().getLevel(player.getSkills().getSkillIdByTargetId(skillId))) {
					return;
				}
				player.getSkills().setSkillTarget(true, skillId, levelTarget);
			}
			if (player.getTemporaryAttributtes().get("ports_plate") != null) {
				player.getTemporaryAttributtes().remove("ports_plate");
				if (value < 1 || value > 2147000000) {
					value = 0;
				}
				int plate = player.getPorts().plate;
				if (value > plate) {
					value = plate;
				}
				if (value <= 0) {
					return;
				}
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 18891,
						"I've exchanged " + value + " plate for " + value * 5 + " Chime.");
				player.getPorts().chime += value * 5;
				player.getPorts().plate -= value;
				return;
			}
			if (player.getTemporaryAttributtes().get("ports_chiGlobe") != null) {
				player.getTemporaryAttributtes().remove("ports_chiGlobe");
				if (value < 1 || value > 2147000000) {
					value = 0;
				}
				int plate = player.getPorts().chiGlobe;
				if (value > plate) {
					value = plate;
				}
				if (value <= 0) {
					return;
				}
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 18891,
						"I've exchanged " + value + " chi globe for " + value * 5 + " Chime.");
				player.getPorts().chime += value * 5;
				player.getPorts().chiGlobe -= value;
				return;
			}
			if (player.getTemporaryAttributtes().get("ports_lacquer") != null) {
				player.getTemporaryAttributtes().remove("ports_lacquer");
				if (value < 1 || value > 2147000000) {
					value = 0;
				}
				int plate = player.getPorts().lacquer;
				if (value > plate) {
					value = plate;
				}
				if (value <= 0) {
					return;
				}
				player.getDialogueManager().startDialogue("SimpleNPCMessage", 18891,
						"I've exchanged " + value + " lacquer for " + value * 5 + " Chime.");
				player.getPorts().chime += value * 5;
				player.getPorts().lacquer -= value;
				return;
			}
		} else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			stream.readShortLE128();
			int fromInterfaceHash = stream.readIntV1();
			int toInterfaceHash = stream.readInt();
			int fromSlot = stream.readUnsignedShort();
			int toSlot = stream.readUnsignedShortLE128();
			stream.readUnsignedShortLE();

			int toInterfaceId = toInterfaceHash >> 16;
			int toComponentId = toInterfaceHash - (toInterfaceId << 16);
			int fromInterfaceId = fromInterfaceHash >> 16;
			int fromComponentId = fromInterfaceHash - (fromInterfaceId << 16);

			if (!player.getControllerManager().processSwitchComponent(toInterfaceId, toComponentId, toSlot,
					fromInterfaceId, fromComponentId, fromSlot))
				return;
			if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId
					|| Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(fromInterfaceId)
					|| !player.getInterfaceManager().containsInterface(toInterfaceId))
				return;
			if (fromComponentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
				return;
			if (toComponentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
				return;
			if (fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromComponentId == 0
					&& toInterfaceId == Inventory.INVENTORY_INTERFACE && toComponentId == 0) {
				toSlot -= 28;
				if (toSlot < 0 || toSlot >= player.getInventory().getItemsContainerSize()
						|| fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
				if (toSlot >= player.getInventory().getItemsContainerSize()
						|| fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
				player.getBank().switchItem(fromSlot, toSlot, fromComponentId, toComponentId);
				return;
			} else if (fromInterfaceId == 1265 && toInterfaceId == 1266
					&& player.getTemporaryAttributtes().get("shop_buying") != null) {
				if ((boolean) player.getTemporaryAttributtes().get("shop_buying") == true) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null)
						return;
					shop.buy(player, fromSlot, 1);
				}
			}
			if (fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromComponentId == 0
					&& toInterfaceId == Inventory.INVENTORY_INTERFACE && toComponentId == 0) {
				toSlot -= 28;
				if (toSlot < 0 || toSlot >= player.getInventory().getItemsContainerSize()
						|| fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
				if (toSlot >= player.getInventory().getItemsContainerSize()
						|| fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (Settings.DEBUG)
				System.out.println("Switch item " + fromInterfaceId + ", " + fromSlot + ", " + toSlot);
		} else if (packetId == GROUND_ITEM_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() > currentTime)
				// || player.getFreezeDelay() >= currentTime)
				return;
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final int id = stream.readUnsignedShort();
			boolean forceRun = stream.read128Byte() == 1;
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			if (forceRun)
				player.setRun(forceRun);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
					if (item == null)
						return;
					if (item.getOwner() != player.getDisplayName()) {
						if (player.isIronman()) {
							player.getDialogueManager().startDialogue("IronMan");
							return;
						}
					}
					player.setNextFaceWorldTile(tile);
					player.addWalkSteps(tile.getX(), tile.getY(), 1);
					if (Firemaking.isFiremaking(player, item.getId())) {
						World.removeGroundItem(player, item);
						return;
					}
				}
			}));
		} else if (packetId == GROUND_ITEM_OPTION_EXAMINE) {
			if (!player.clientHasLoadedMapRegion() || player.isDead()) {
				return;
			}
			long currentTime = Utils.currentTimeMillis();
			if (player.getLockDelay() > currentTime) {
				return;
			}
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final int id = stream.readInt();
			boolean forceRun = stream.read128Byte() == 1;
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId)) {
				return;
			}
			final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
			if (item == null) {
				return;
			}
			player.sendMessage(ItemExamines.getExamine(item));
		} else if (packetId == DONE_LOADING_REGION_PACKET) {
		} else if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET || packetId == ITEM_TAKE_PACKET
				|| packetId == GROUND_ITEM_OPTION_2_PACKET || packetId == PLAYER_OPTION_2_PACKET
				|| packetId == PLAYER_OPTION_4_PACKET || packetId == PLAYER_OPTION_5_PACKET
				|| packetId == PLAYER_OPTION_6_PACKET || packetId == PLAYER_OPTION_1_PACKET || packetId == ATTACK_NPC
				|| packetId == INTERFACE_ON_PLAYER || packetId == INTERFACE_ON_NPC || packetId == NPC_CLICK1_PACKET
				|| packetId == NPC_CLICK2_PACKET || packetId == NPC_CLICK3_PACKET || packetId == NPC_CLICK4_PACKET
				|| packetId == OBJECT_CLICK1_PACKET || packetId == SWITCH_INTERFACE_ITEM_PACKET
				|| packetId == OBJECT_CLICK2_PACKET || packetId == OBJECT_CLICK3_PACKET
				|| packetId == OBJECT_CLICK4_PACKET || packetId == OBJECT_CLICK5_PACKET
				|| packetId == INTERFACE_ON_OBJECT || packetId == PLAYER_OPTION_9_PACKET)
			player.addLogicPacketToQueue(new LogicPacket(packetId, length, stream));
		else if (packetId == OBJECT_EXAMINE_PACKET) {
			ObjectHandler.handleOption(player, stream, -1);
		} else if (packetId == NPC_EXAMINE_PACKET) {
			NPCHandler.handleExamine(player, stream);
		} else if (packetId == JOIN_CLAN_CHAT_PACKET) {

		} else if (packetId == JOIN_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			FriendChatsManager.joinChat(stream.readString(), player);
		} else if (packetId == KICK_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
			// message
			// appearing
			player.kickPlayerFromFriendsChannel(stream.readString());
		} else if (packetId == KICK_CLAN_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
			// message
			// appearing
			boolean guest = stream.readByte() == 1;
			if (!guest)
				return;
			stream.readUnsignedShort();
			player.kickPlayerFromClanChannel(stream.readString());
		} else if (packetId == CHANGE_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108))
				return;
			player.getFriendsIgnores().changeRank(stream.readString(), stream.readUnsignedByte128());
		}
		if (packetId == GRAND_EXCHANGE_ITEM_SELECT_PACKET) {
			int itemId = stream.readUnsignedShort();
			player.getGEManager().chooseItem(itemId);
		} else if (packetId == ADD_FRIEND_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			player.getFriendsIgnores().addFriend(stream.readString());
		} else if (packetId == REMOVE_FRIEND_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			player.getFriendsIgnores().removeFriend(stream.readString());
		} else if (packetId == ADD_IGNORE_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			player.getFriendsIgnores().addIgnore(stream.readString(), stream.readUnsignedByte() == 1);
		} else if (packetId == REMOVE_IGNORE_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			player.getFriendsIgnores().removeIgnore(stream.readString());
		} else if (packetId == SEND_FRIEND_MESSAGE_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
				return;
			}
			String username = stream.readString();
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null) {
				p2 = World.getLobbyPlayerByDisplayName(username); // getLobbyPlayerByDisplayName
				if (p2 == null) {
					return;
				}
			}

			player.getFriendsIgnores().sendMessage(p2, Utils.fixChatMessage(Huffman.readEncryptedMessage(150, stream)));
		} else if (packetId == SEND_FRIEND_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			String username = stream.readString();
			int fileId = stream.readUnsignedShort();
			if (!Utils.isQCValid(fileId))
				return;
			byte[] data = null;
			if (length > 3 + username.length()) {
				data = new byte[length - (3 + username.length())];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				return;
			player.getFriendsIgnores().sendQuickChatMessage(p2, new QuickChatMessage(fileId, data));
		} else if (packetId == PUBLIC_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			// just tells you which client script created packet
			@SuppressWarnings("unused")
			boolean secondClientScript = stream.readByte() == 1;// script 5059
			// or 5061
			int fileId = stream.readUnsignedShort();
			if (!Utils.isQCValid(fileId))
				return;
			byte[] data = null;
			if (length > 3) {
				data = new byte[length - 3];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			if (chatType == 0)
				player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
			else if (chatType == 1)
				player.sendFriendsChannelQuickMessage(new QuickChatMessage(fileId, data));
			else if (chatType == 2)
				player.sendClanChannelQuickMessage(new QuickChatMessage(fileId, data));
			else if (chatType == 3)
				player.sendGuestClanChannelQuickMessage(new QuickChatMessage(fileId, data));
			else if (Settings.DEBUG)
				Logger.log(this, "Unknown chat type: " + chatType);
		} else if (packetId == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if (packetId == CHAT_PACKET) {
			if (!player.hasStarted() && !World.containsLobbyPlayer(player.getUsername()))
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			if (player.getColorID() > 0) {
				colorEffect = player.getColorID();
			}
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.readEncryptedMessage(200, stream);
			if (message == null || message.replaceAll(" ", "").equals(""))
				return;
			if (message.startsWith("::") || message.startsWith(";;") || message.startsWith(".")) {
				DeveloperConsole.processCommand(player, message.replace("::", "").replace(";;", "").replace(".", ""),
						false, false);
				return;
			}
			if (message.startsWith("\\") || message.startsWith("\\")) {
				DeveloperConsole.processCommand(player, message.replace("\\", "answer ").replace("\\", "answer "),
						false, false);
				return;
			}
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
				return;
			}
			for (String censoredWords : Censor.BLACKLIST) {
				if (message.toLowerCase().contains(censoredWords)) {
					return;
				}
			}
			if (message.contains(player.getSession().getIP())) {
				player.getPackets().sendGameMessage("You appear to be telling someone your IP - please don't!");
				return;
			}
			int effects = (colorEffect << 8) | (moveEffect & 0xff);
			if (chatType == 1) {
				player.sendFriendsChannelMessage(Utils.fixChatMessage(message));
				if (Settings.DISCORD) {
					new MessageBuilder()
							.append(player.getCurrentFriendChat().getChannelName() + ": " + player.getDisplayName()
									+ ":  " + message)
							.send(GameServer.getDiscordBot().getAPI().getTextChannelById("504469472116211712").get());
				}
			} else if (chatType == 2) {
				player.sendClanChannelMessage(new ChatMessage(message));
				if (Settings.DISCORD) {
					new MessageBuilder().append(player.getDisplayName() + ":  " + Utils.fixChatMessage(message))
							.send(GameServer.getDiscordBot().getAPI().getTextChannelById("510259834084655146").get());
				}
			} else if (chatType == 3)
				player.sendGuestClanChannelMessage(new ChatMessage(message));
			else {
				player.sendPublicChatMessage(new PublicChatMessage(message, effects));
				if (Settings.DISCORD) {
					new MessageBuilder().append(player.getDisplayName() + ":  " + Utils.fixChatMessage(message))
							.send(GameServer.getDiscordBot().getAPI().getTextChannelById("504493600110018560").get());
				}
			}
			player.setLastMsg(message);
		} else if (packetId == COMMANDS_PACKET) {
			if (!player.isRunning())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			boolean unknown = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			if (!DeveloperConsole.processCommand(player, command, true, clientCommand))
				System.out.println("Command: " + command);
		} else if (packetId == REPORT_ABUSE_PACKET) {
			if (!player.hasStarted())
				return;
			@SuppressWarnings("unused")
			String username = stream.readString();
			@SuppressWarnings("unused")
			int type = stream.readUnsignedByte();
			@SuppressWarnings("unused")
			boolean mute = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			String unknown2 = stream.readString();
		} else if (packetId == COLOR_ID_PACKET) {
			if (!player.hasStarted())
				return;
			int colorId = stream.readUnsignedShort();
			if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null)
				SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId);
			else if (player.getTemporaryAttributtes().get("MotifCustomization") != null)
				ClansManager.setMottifColor(player, colorId);
			else if (player.getTemporaryAttributtes().remove("COSTUME_COLOR_CUSTOMIZE") != null)
				SkillCapeCustomizer.handleCostumeColor(player, colorId);
		} else if (packetId == REPORT_ABUSE_PACKET) {
			if (!player.hasStarted())
				return;
			@SuppressWarnings("unused")
			String username = stream.readString();
			@SuppressWarnings("unused")
			int type = stream.readUnsignedByte();
			@SuppressWarnings("unused")
			boolean mute = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			String unknown2 = stream.readString();
		} else {
		}
	}

}
