package com.rs.game.player.appearance;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

import com.rs.cache.loaders.BodyDefinitions;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.World;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.clans.ClansManager;
import com.rs.game.player.content.dungeoneering.DungeonController;
import com.rs.io.OutputStream;
import com.rs.utils.Utils;

public class Appearence implements Serializable {

	/**
	 * The serial UID
	 */
	private static final long serialVersionUID = 7655608569741626586L;

	/**
	 * The emote at which the player is rendered at
	 */
	private transient int renderEmote;
	/**
	 * TransformedNPCId
	 */
	private transient short transformedNpcId;
	/**
	 * 
	 * The player's title
	 */
	private int title;

	/**
	 * The player's body looks.
	 */
	private int[] bodyStyle;
	/**
	 * The cosmetic items
	 */
	Item[] cosmeticItems;
	/**
	 * The player's body color
	 */
	private byte[] bodyColors;
	/**
	 * If the player's gender is a male
	 */
	private boolean male;
	/**
	 * If the player's eyes glow red
	 */
	private transient boolean glowRed;
	/**
	 * The appearance block
	 */
	private transient byte[] appearanceBlock;
	/**
	 * The encyrpted appearance block
	 */
	private transient byte[] encyrptedAppearanceBlock;
	/**
	 * The NPC the player is transformed into
	 */
	private transient short asNPC;
	/**
	 * If we should skip the character block
	 */
	private transient boolean hidePlayer;
	/**
	 * If we should show the player's skill level rather then combat level
	 */
	private boolean showSkillLevel;
	/**
	 * The player being appearance rendered
	 */
	private transient Player player;

	/**
	 * Constructs a new {@code PlayerAppearance} object
	 */
	public Appearence() {
		male = true;
		renderEmote = -1;
		title = -1;
		resetAppearance();
	}

	/**
	 * Sets the glow red flag
	 * 
	 * @param glowRed True or false
	 */
	public void setGlowRed(boolean glowRed) {
		this.glowRed = glowRed;
		generateAppearenceData();
	}

	/**
	 * Sets the player
	 * 
	 * @param player The player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
		transformedNpcId = -1;
		renderEmote = -1;
		if (bodyStyle == null || cosmeticItems == null)
			resetAppearance();
	}

	/**
	 * Sets the npc mask
	 * 
	 * @param id The NPC to set
	 */
	public void asNPC(int id) {
		asNPC = (short) id;
		generateAppearenceData();

	}

	/**
	 * Hides and unhides the player
	 */
	public void switchHidden() {
		hidePlayer = !hidePlayer;
		generateAppearenceData();
	}

	/**
	 * If this player is hidden
	 * 
	 * @return True if hidden; false otherwise
	 */
	public boolean isHidden() {
		return hidePlayer;
	}

	/**
	 * If this player's eyes glow red
	 * 
	 * @return True if so; false otherwise
	 */
	public boolean isGlowRed() {
		return glowRed;
	}

	/**
	 * Loads this player's appearance to a buffer and is sent to the client within a
	 * packet containing information on how this player should be viewed as
	 * graphically
	 */
	public void generateAppearenceData() {
		OutputStream stream = new OutputStream();
		writeFlags(stream);
		int flag = 0;

		/**
		 * If there is no title we skip the title block
		 */
		if (title != 0)
			writeTitle(stream);
		/**
		 * Writes the skull of this player
		 */
		writeSkull(stream);

		stream.writeBytes(getAppearenceLook());

		/**
		 * Display name
		 */
		stream.writeString(player.getDisplayName());

		/**
		 * Combat level data
		 */
		boolean pvpArea = World.isPvpArea(player);
		stream.writeByte(
				pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);

		/**
		 * Higher level acc name appears on top
		 */
		stream.writeByte(-1);

		/**
		 * NPC Transmogrification
		 */
		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
		if (transformedNpcId >= 0) {
			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
			stream.writeShort(defs.anInt876);
			stream.writeShort(defs.anInt842);
			stream.writeShort(defs.anInt884);
			stream.writeShort(defs.anInt875);
			stream.writeByte(defs.anInt875);
		}

		/**
		 * Custom particle shit.
		 */
		// stream.writeByte(0); thats useless

		// done separated for safe because of synchronization
		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
		this.appearanceBlock = appeareanceData;
		encyrptedAppearanceBlock = md5Hash;
	}

	public byte[] getAppearenceLook() {
		return getAppearenceLook(player.getEquipment().getCosmeticPreviewItems() != null
				? player.getEquipment().getCosmeticPreviewItems().getItems()
				: player.getEquipment().getCosmeticItems().getItems(), bodyStyle);
	}

	public byte[] getAppearenceLook(Item[] cosmetics, int[] look) {
		OutputStream stream = new OutputStream();

		if (transformedNpcId >= 0) {
			stream.writeShort(-1); // 65535 tells it a npc
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);
		} else {
			Item[] items = new Item[BodyDefinitions.disabledSlots.length];

			boolean[] skipLook = new boolean[items.length];
			for (int index = 0; index < items.length; index++) {
				Item item = player.getEquipment().isCanDisplayCosmetic() ? cosmetics[index] : null;

				if (index == Equipment.SLOT_AURA && player.getEquipment().isCanDisplayCosmetic() && cosmetics[index] != null && player.getAuraManager().isActivated())
					item = null;
				
				  if ((index == 3 || index == 5) && item != null && player.getEquipment().getCosmeticItems().getItems() == cosmetics) {
					Item originalWeapon = player.getEquipment().getItem(index);
					if (originalWeapon == null || originalWeapon.getDefinitions().getAttackAnimation() != item.getDefinitions().getAttackAnimation()) {
						item = null;
					}
				}
				if (item == null && player.getEquipment().getCosmeticPreviewItems() == null)
					item = player.getEquipment().getItems().get(index);
				if (!(player.getControllerManager().getController() instanceof DungeonController) && index == Equipment.SLOT_SHIELD) {
					Item weapon = cosmetics[Equipment.SLOT_WEAPON] != null ? cosmetics[Equipment.SLOT_WEAPON] : player.getEquipment().getItems().get(Equipment.SLOT_WEAPON);
					if (weapon != null && Equipment.isTwoHandedWeapon(weapon))
						item = null;
				}
				if (item != null) {
					items[index] = item;
					int skipSlotLook = item.getDefinitions().getEquipLookHideSlot();
					if (skipSlotLook != -1)
						skipLook[skipSlotLook] = true;
					int skipSlotLook2 = item.getDefinitions().getEquipLookHideSlot2();
					if (skipSlotLook2 != -1)
						skipLook[skipSlotLook2] = true;
				}
			}
			for (int index = 0; index < items.length; index++) {
				if (BodyDefinitions.disabledSlots[index] != 0)
					continue;
				if (glowRed) {
					if (index == 0) {
						stream.writeShort(16384 + 2910);
						continue;
					}
					if (index == 1) {
						stream.writeShort(16384 + 14641);
						continue;
					}
					if (index == Equipment.SLOT_LEGS) {
						stream.writeShort(16384 + 2908);
						continue;
					}
					if (index == Equipment.SLOT_HANDS) {
						stream.writeShort(16384 + 2912);
						continue;
					}
					if (index == Equipment.SLOT_FEET) {
						stream.writeShort(16384 + 2904);
						continue;
					}
				}
				if (items[index] != null && items[index].getDefinitions().equipSlot != -1) {
					stream.writeShort(16384 + items[index].getId());
					continue;
				}
				if (!skipLook[index]) {
					int bodyStylendex = -1;

					switch (index) {
					case 4:
						bodyStylendex = 2;
						break;
					case 6:
						bodyStylendex = 3;
						break;
					case 7:
						bodyStylendex = 5;
						break;
					case 8:
						bodyStylendex = 0;
						break;
					case 9:
						bodyStylendex = 4;
						break;
					case 10:
						bodyStylendex = 6;
						break;
					case 11:
						bodyStylendex = 1;
						break;
					}
					if (bodyStylendex != -1 && bodyStyle[bodyStylendex] > 0) {
						stream.writeShort(0x100 + bodyStyle[bodyStylendex]);
						continue;
					}
				}
				stream.writeByte(0);
			}

			OutputStream streamModify = new OutputStream();
			int modifyFlag = 0;
			int slotIndex = -1;
			ItemModify[] modify = generateItemModify(items, cosmetics);
			for (int index = 0; index < items.length; index++) {
				if (BodyDefinitions.disabledSlots[index] != 0)
					continue;
				slotIndex++;
				ItemModify im = modify[index];
				if (im == null)
					continue;
				modifyFlag |= 1 << slotIndex;
				int itemFlag = 0;
				OutputStream streamItem = new OutputStream();
				if (im.maleModelId1 != -1 || im.femaleModelId1 != -1) {
					itemFlag |= 0x1;
					streamItem.writeBigSmart(im.maleModelId1);
					streamItem.writeBigSmart(im.femaleModelId1);
					if (im.maleModelId2 != -2 || im.femaleModelId2 != -2) {
						streamItem.writeBigSmart(im.maleModelId2);
						streamItem.writeBigSmart(im.femaleModelId2);
					}
					if (im.maleModelId3 != -2 || im.femaleModelId3 != -2) {
						streamItem.writeBigSmart(im.maleModelId3);
						streamItem.writeBigSmart(im.femaleModelId3);
					}
				}
				if (im.colors != null) {
					itemFlag |= 0x4;
					streamItem.writeShort(0 | 1 << 4 | 2 << 8 | 3 << 12);
					for (int i = 0; i < 4; i++)
						streamItem.writeShort(im.colors[i]);
				}
				if (im.textures != null) {
					itemFlag |= 0x8;
					streamItem.writeByte(0 | 1 << 4);
					for (int i = 0; i < 2; i++)
						streamItem.writeShort(im.textures[i]);
				}
				streamModify.writeByte(itemFlag);
				streamModify.writeBytes(streamItem.getBuffer(), 0, streamItem.getOffset());
			}
			stream.writeShort(modifyFlag);
			stream.writeBytes(streamModify.getBuffer(), 0, streamModify.getOffset());
		}
		for (int index = 0; index < bodyColors.length; index++)
			stream.writeByte(bodyColors[index]);
		stream.writeShort(getRenderEmote());
		byte[] data = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, data, 0, data.length);
		return data;
	}

	private ItemModify[] generateItemModify(Item[] items, Item[] cosmetics) {
		ItemModify[] modify = new ItemModify[items.length];
		for (int slotId = 0; slotId < modify.length; slotId++) {
			if ((slotId == Equipment.SLOT_WEAPON || slotId == Equipment.SLOT_SHIELD)
					&& player.getCombatDefinitions().isSheathe()) {
				Item item = items[slotId];
				if (item != null) {
					int modelId = items[slotId].getDefinitions().getSheatheModelId();
					setItemModifyModel(items[slotId], slotId, modify, modelId, modelId, -1, -1, -1, -1);
				}
			}

			if (slotId == Equipment.SLOT_CHEST) {
				Item item = items[slotId];
				if (item != null && isMale()) {
					int modelId = items[slotId].getDefinitions().getMaleWornModelId1();
					setItemModifyModel(items[slotId], slotId, modify, modelId, modelId, -1, -1, -1, -1);
				}
			}

			if (slotId == Equipment.SLOT_LEGS) {
				Item item = items[slotId];
				if (item != null && isMale()) {
					int modelId = items[slotId].getDefinitions().getMaleWornModelId1();
					setItemModifyModel(items[slotId], slotId, modify, modelId, modelId, -1, -1, -1, -1);
				}
			}
			if (items[slotId] != null && items[slotId] == cosmetics[slotId]) {
				int[] colors = new int[4];
				colors[0] = player.getEquipment().getCostumeColor();
				colors[1] = colors[0] + 12;
				colors[2] = colors[1] + 12;
				colors[3] = colors[2] + 12;
				setItemModifyColor(items[slotId], slotId, modify, colors);
			} else {
				int id = items[slotId] == null ? -1 : items[slotId].getId();
				if (id == 32152 || id == 32153 || id == 20768 || id == 20770 || id == 20772 || id == 20767
						|| id == 20769 || id == 20771)
					setItemModifyColor(items[slotId], slotId, modify,
							id == 32151 || id == 20768 || id == 20767 ? player.getMaxedCapeCustomized()
									: player.getCompletionistCapeCustomized());
				else if (id == 20708 || id == 20709) {
					ClansManager manager = player.getClanManager();
					if (manager == null)
						continue;
					int[] colors = manager.getClan().getMottifColors();
					setItemModifyColor(items[slotId], slotId, modify, colors);
					setItemModifyTexture(items[slotId], slotId, modify,
							new short[] { (short) ClansManager.getMottifTexture(manager.getClan().getMottifTop()),
									(short) ClansManager.getMottifTexture(manager.getClan().getMottifBottom()) });
				} else if (player.getAuraManager().isActivated() && slotId == Equipment.SLOT_AURA) {
					int auraId = player.getEquipment().getAuraId();
					if (auraId == -1)
						continue;
					int modelId = player.getAuraManager().getAuraModelId();
					int modelId2 = player.getAuraManager().getAuraModelId2();
					setItemModifyModel(items[slotId], slotId, modify, modelId, modelId, modelId2, modelId2, -1, -1);
				}
			}
		}
		return modify;
	}

	private void setItemModifyModel(Item item, int slotId, ItemModify[] modify, int maleModelId1, int femaleModelId1,
			int maleModelId2, int femaleModelId2, int maleModelId3, int femaleModelId3) {
		ItemDefinitions defs = item.getDefinitions();
		if (defs.getMaleWornModelId1() == -1 || defs.getFemaleWornModelId1() == -1)
			return;
		if (modify[slotId] == null)
			modify[slotId] = new ItemModify();
		modify[slotId].maleModelId1 = maleModelId1;
		modify[slotId].femaleModelId1 = femaleModelId1;
		if (defs.getMaleWornModelId2() != -1 || defs.getFemaleWornModelId2() != -1) {
			modify[slotId].maleModelId2 = maleModelId2;
			modify[slotId].femaleModelId2 = femaleModelId2;
		}
		if (defs.getMaleWornModelId3() != -1 || defs.getFemaleWornModelId3() != -1) {
			modify[slotId].maleModelId2 = maleModelId3;
			modify[slotId].femaleModelId2 = femaleModelId3;
		}
	}

	private void setItemModifyTexture(Item item, int slotId, ItemModify[] modify, short[] textures) {
		ItemDefinitions defs = item.getDefinitions();
		if (defs.originalTextureColors == null || defs.originalTextureColors.length != textures.length)
			return;
		if (Arrays.equals(textures, defs.originalTextureColors))
			return;
		if (modify[slotId] == null)
			modify[slotId] = new ItemModify();
		modify[slotId].textures = textures;
	}

	private void setItemModifyColor(Item item, int slotId, ItemModify[] modify, int[] colors) {
		ItemDefinitions defs = item.getDefinitions();
		if (defs.originalModelColors == null || defs.originalModelColors.length != colors.length)
			return;
		if (Arrays.equals(colors, defs.originalModelColors))
			return;
		if (modify[slotId] == null)
			modify[slotId] = new ItemModify();
		modify[slotId].colors = colors;
	}

	private static class ItemModify {

		private int[] colors;
		private short[] textures;
		private int maleModelId1;
		private int femaleModelId1;
		private int maleModelId2;
		private int femaleModelId2;
		private int maleModelId3;
		private int femaleModelId3;

		private ItemModify() {
			maleModelId1 = femaleModelId1 = -1;
			maleModelId2 = femaleModelId2 = -2;
			maleModelId3 = femaleModelId3 = -2;
		}
	}

	/**
	 * Returns the size of this player
	 * 
	 * @return The size
	 */
	public int getSize() {
		if (asNPC >= 0)
			return NPCDefinitions.getNPCDefinitions(asNPC).size;
		return 1;
	}

	/**
	 * Sets the render emote of this player
	 * 
	 * @param id The id of the render emote to set
	 */
	public void setRenderEmote(int id) {
		this.renderEmote = id;
		generateAppearenceData();
	}

	/**
	 * Returns the render emote of this player, or if the player has an NPC
	 * appearance then we return the NPC's render
	 * 
	 * @return The render emote
	 */

	public int getRenderEmote() {
		if (renderEmote >= 0)
			return renderEmote;
		if (transformedNpcId >= 0) {
			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
			HashMap<Integer, Object> data = defs.clientScriptData;
			if (data != null && !data.containsKey(2805))
				return defs.renderEmote;
		}
		if (player.getCombatDefinitions().isSheathe() && !player.getCombatDefinitions().isCombatStance()) {
			if (player.getCosmeticManager().getWalk() != null) {
				return player.getCosmeticManager().getWalk().getRender();
			}
			return player.getEquipment().getWeaponStance();
		}
		return player.getEquipment().getWeaponStance();
	}

	/**
	 * Resets the appearance flags
	 */
	public void resetAppearance() {
		bodyStyle = new int[7];
		bodyColors = new byte[10];
		if (cosmeticItems == null)
			cosmeticItems = new Item[14];
		setMale();
	}

	/**
	 * Writes the player's skull to the stream
	 * 
	 * @param stream The stream to write data to
	 */
	private void writeSkull(OutputStream stream) {
		stream.writeByte(player.hasSkull() ? player.getSkullId() : hidePlayer ? -1 : player.isIronman() ? 16 : -1);
		stream.writeByte(player.getPrayer().getPrayerHeadIcon());
		stream.writeByte(hidePlayer ? 1 : 0);
	}

	/**
	 * Writes the player's title to the stream
	 * 
	 * @param stream The stream to write data to
	 */
	private void writeTitle(OutputStream stream) {
		String titleName = title == 999 ? "<col=C12006>Prestige " + player.prestigeLevel + " </col>"
				: title == 9876 ? "<img=1><col=dfd500>Community Manager </col>"
						: title == 5555 ? "<col=C12006>The senseless </col>"
								: title == 5556 ? "<col=C12006>Dreadful man </col>"
										: title == 5557 ? "<col=C12006>Splendid man </col>"
												: title == 5558 ? "<col=C12006>The hunger </col>"
														: title == 5559 ? "<col=C12006>Deadman </col>"
																: ClientScriptMap.getMap(male ? 1093 : 3872)
																		.getStringValue(title);
		stream.writeGJString(titleName);
	}

	/**
	 * Writes the player's default flags to the stream
	 * 
	 * @param stream The stream to write data to
	 */
	private void writeFlags(OutputStream stream) {
		int flag = 0;
		if (!male)
			/**
			 * Female flag
			 */
			flag |= 0x1;
		if (asNPC >= 0 && NPCDefinitions.getNPCDefinitions(asNPC).aBoolean3190)
			/**
			 * Is NPC flag
			 */
			flag |= 0x2;
		if (showSkillLevel)
			flag |= 0x4;
		if (title != 0)
			/**
			 * Has title flag
			 */
			flag |= title >= 32 && title <= 37 ? 0x80 : 0x40; // after/before
		stream.writeByte(flag);
	}

	/**
	 * Sets the player to a male
	 */
	public void setMale() {
		bodyStyle[0] = 3;
		bodyStyle[1] = 14;
		bodyStyle[2] = 18;
		bodyStyle[3] = 26;
		bodyStyle[4] = 34;
		bodyStyle[5] = 38;
		bodyStyle[6] = 42;

		bodyColors[2] = 16;
		bodyColors[1] = 16;
		bodyColors[0] = 3;
		male = true;
	}

	/**
	 * Sets the player to a female
	 */
	public void female() {
		bodyStyle[0] = 48;
		bodyStyle[1] = 57;
		bodyStyle[2] = 57;
		bodyStyle[3] = 65;
		bodyStyle[4] = 68;
		bodyStyle[5] = 77;
		bodyStyle[6] = 80;
		bodyColors[2] = 16;
		bodyColors[1] = 16;
		bodyColors[0] = 3;
		male = false;
	}

	/**
	 * Returns the loaded appearance block
	 * 
	 * @return The appearance block
	 */
	public byte[] getAppearanceBlock() {
		return appearanceBlock;
	}

	/**
	 * Returns the loaded encrypted appearance block
	 * 
	 * @return The encrypted appearance block
	 */
	public byte[] getEncryptedAppearanceBlock() {
		return encyrptedAppearanceBlock;
	}

	/**
	 * If the player is a male
	 * 
	 * @return True if so; false otherwise
	 */
	public boolean isMale() {
		return male;
	}

	/**
	 * Sets the player's body style
	 * 
	 * @param i  The slot
	 * @param i2 The style
	 */
	public void setBodyStyle(int i, int i2) {
		bodyStyle[i] = i2;
	}

	/**
	 * Sets the player's body color
	 * 
	 * @param i  The slot
	 * @param i2 The color
	 */
	public void setBodyColor(int i, int i2) {
		bodyColors[i] = (byte) i2;
	}

	/**
	 * Sets the player's gender
	 * 
	 * @param male If the player is male
	 */
	public void setMale(boolean male) {
		this.male = male;
	}

	/**
	 * Sets the hair style
	 * 
	 * @param i The hair style to set
	 */
	public void setHairStyle(int i) {
		bodyStyle[0] = i;
	}

	/**
	 * Sets the player's top style
	 * 
	 * @param i The style to set
	 */
	public void setTopStyle(int i) {
		bodyStyle[2] = i;
	}

	/**
	 * Returns the player's top style
	 * 
	 * @return The style to set
	 */
	public int getTopStyle() {
		return bodyStyle[2];
	}

	/**
	 * Sets the player's arm style
	 * 
	 * @param i The style to set
	 */
	public void setArmsStyle(int i) {
		bodyStyle[3] = i;
	}

	/**
	 * Sets the player's wrist style
	 * 
	 * @param i The style to set
	 */
	public void setWristsStyle(int i) {
		bodyStyle[4] = i;
	}

	/**
	 * Sets the player's leg style
	 * 
	 * @param i The style to set
	 */
	public void setLegsStyle(int i) {
		bodyStyle[5] = i;
	}

	/**
	 * Sets the player's leg style
	 * 
	 * @param i The style to set
	 */
	public void setShoeStyle(int i) {
		bodyStyle[6] = i;
	}

	/**
	 * Sets the player's hair style
	 * 
	 * @param i The style to set
	 */
	public int getHairStyle() {
		return bodyStyle[0];
	}

	/**
	 * Sets the player's beard style
	 * 
	 * @param i The style to set
	 */
	public void setBeardStyle(int i) {
		bodyStyle[1] = i;
	}

	/**
	 * Returns the player's beard style
	 * 
	 * @return The beard style
	 */
	public int getBeardStyle() {
		return bodyStyle[1];
	}

	/**
	 * Sets the player's facial hair style
	 * 
	 * @param i The facial hair style to set
	 */
	public void setFacialHair(int i) {
		bodyStyle[1] = i;
	}

	/**
	 * Returns the player's facial hair style
	 * 
	 * @return The facial hair style
	 */
	public int getFacialHair() {
		return bodyStyle[1];
	}

	/**
	 * Sets the player's body color
	 * 
	 * @param color The color to set
	 */
	public void setSkinColor(int color) {
		bodyColors[4] = (byte) color;
	}

	/**
	 * Returns the player's skin colors
	 * 
	 * @return The skin colors to set
	 */
	public int getSkinColor() {
		return bodyColors[4];
	}

	/**
	 * Sets the player's hair color
	 * 
	 * @param color The color to set
	 */
	public void setHairColor(int color) {
		bodyColors[0] = (byte) color;
	}

	/**
	 * Sets the player's top color
	 * 
	 * @param color The color to set
	 */
	public void setTopColor(int color) {
		bodyColors[1] = (byte) color;
	}

	/**
	 * Sets the player's leg color
	 * 
	 * @param color The color to set
	 */
	public void setLegsColor(int color) {
		bodyColors[2] = (byte) color;
	}

	/**
	 * Returns the player's hair color
	 * 
	 * @return The hair color
	 */
	public int getHairColor() {
		return bodyColors[0];
	}

	/**
	 * Sets a specified slot as cosmetic
	 * 
	 * @param item The cosmetic item
	 * @param slot The slot to set
	 */
	public void setCosmetic(Item item, int slot) {
		cosmeticItems[slot] = item;
	}

	/**
	 * Returns the cosmetic item corresponding to the specified slot
	 * 
	 * @param slot The slot to get
	 * @return The cosmetic item
	 */
	public Item getCosmeticItem(int slot) {
		return cosmeticItems[slot];
	}

	/**
	 * Clears the cosmetic data
	 */
	public void resetCosmetics() {
		cosmeticItems = new Item[14];
		generateAppearenceData();
	}

	/**
	 * Sets the player's title
	 * 
	 * @param title The title to set
	 */
	public void setTitle(int title) {
		this.title = title;
		generateAppearenceData();
	}

	public void setLooks(short[] look) {
		for (byte i = 0; i < this.bodyStyle.length; i = (byte) (i + 1))
			if (look[i] != -1)
				this.bodyStyle[i] = look[i];
	}

	public void copyColors(short[] colors) {
		for (byte i = 0; i < this.bodyColors.length; i = (byte) (i + 1))
			if (colors[i] != -1)
				this.bodyColors[i] = (byte) colors[i];
	}

	public void print() {
		for (int i = 0; i < bodyStyle.length; i++) {
			System.out.println("look[" + i + " ] = " + bodyStyle[i] + ";");
		}
		for (int i = 0; i < bodyColors.length; i++) {
			System.out.println("colour[" + i + " ] = " + bodyColors[i] + ";");
		}
	}

	/**
	 * Toggles showing skills levels.
	 */
	public void switchShowingSkill() {
		showSkillLevel = !showSkillLevel;
		generateAppearenceData();
	}

	/**
	 * If we are showing the skill level as apposed to the combat level
	 * 
	 * @return True if so; false otherwise
	 */
	public boolean isShowSkillLevel() {
		return showSkillLevel;
	}

	/**
	 * Sets if we should show the skill level
	 * 
	 * @param showSkillLevel If we should show the skill level
	 */
	public void setShowSkillLevel(boolean showSkillLevel) {
		this.showSkillLevel = showSkillLevel;
	}

	/**
	 * Retruns the title
	 * 
	 * @return The title
	 */
	public int getTitle() {
		return title;
	}

	public void transformIntoNPC(int id) {
		transformedNpcId = (short) id;
		generateAppearenceData();
	}

}