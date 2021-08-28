package com.rs.game.player.controllers.minigames;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.MessageBuilder;

import com.rs.GameServer;
import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.item.ItemIdentifiers;
import com.rs.game.npc.NPC;
import com.rs.game.npc.others.BarrowsBrother;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.achievements.Achievements;
import com.rs.game.player.content.combat.Combat;
import com.rs.game.player.controllers.Controller;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Colors;
import com.rs.utils.Utils;

public final class Barrows extends Controller {

	private static enum Hills {

		AHRIM_HILL(new WorldTile(3564, 3287, 0), new WorldTile(3557, 9703, 3)),
		DHAROK_HILL(new WorldTile(3573, 3296, 0), new WorldTile(3556, 9718, 3)),
		GUTHAN_HILL(new WorldTile(3574, 3279, 0), new WorldTile(3534, 9704, 3)),
		KARIL_HILL(new WorldTile(3563, 3276, 0), new WorldTile(3546, 9684, 3)),
		TORAG_HILL(new WorldTile(3553, 3281, 0), new WorldTile(3568, 9683, 3)),
        VERAC_HILL(new WorldTile(3556, 3296, 0), new WorldTile(4077, 5710, 0));

		private WorldTile outBound;
		private WorldTile inside;

		private Hills(WorldTile outBound, WorldTile in) {
			this.outBound = outBound;
			inside = in;
		}
	}

	public static boolean digIntoGrave(final Player player) {
		for (Hills hill : Hills.values()) {
			if (player.getPlane() == hill.outBound.getPlane() && player.getX() >= hill.outBound.getX()
					&& player.getY() >= hill.outBound.getY() && player.getX() <= hill.outBound.getX() + 3
					&& player.getY() <= hill.outBound.getY() + 3) {
				player.useStairs(-1, hill.inside, 1, 2, "You've broken into a crypt.");

				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getControllerManager().startController("Barrows");
					}
				});
				return true;
			}
		}
		return false;
	}

	private BarrowsBrother target;

	private static final Item[] COMMON_REWARDS = {
			new Item(ItemIdentifiers.MIND_RUNE, Utils.random(250, 4900)),
			new Item(ItemIdentifiers.CHAOS_RUNE, Utils.random(115, 1890)),
			new Item(ItemIdentifiers.DEATH_RUNE, Utils.random(70, 1190)),
			new Item(ItemIdentifiers.BLOOD_RUNE, Utils.random(35, 630)),
			new Item(ItemIdentifiers.BOLT_RACK, Utils.random(35, 280)) };
	
	private static final Item[] RARE_REWARDS = {
			new Item(ItemIdentifiers.DRAGON_HELM, 1),
			new Item(ItemIdentifiers.BARROWS_TOTEM, Utils.random(14)),
			new Item(ItemIdentifiers.LOOP_HALF_OF_A_KEY, 1),
			new Item(ItemIdentifiers.TOOTH_HALF_OF_A_KEY, 1) };
	
	private static final Item[] BARROW_REWARDS = {
			new Item(ItemIdentifiers.AHRIMS_HOOD, 1),
			new Item(ItemIdentifiers.AHRIMS_STAFF, 1),
			new Item(ItemIdentifiers.AHRIMS_ROBE_TOP, 1),
			new Item(ItemIdentifiers.AHRIMS_ROBE_SKIRT, 1),
			new Item(ItemIdentifiers.DHAROKS_HELM, 1),
			new Item(ItemIdentifiers.DHAROKS_GREATAXE, 1),
			new Item(ItemIdentifiers.DHAROKS_PLATEBODY, 1),
			new Item(ItemIdentifiers.DHAROKS_PLATELEGS, 1),
			new Item(ItemIdentifiers.GUTHANS_HELM, 1),
			new Item(ItemIdentifiers.GUTHANS_WARSPEAR, 1),
			new Item(ItemIdentifiers.GUTHANS_PLATEBODY, 1),
			new Item(ItemIdentifiers.GUTHANS_CHAINSKIRT, 1),
			new Item(ItemIdentifiers.KARILS_COIF, 1),
			new Item(ItemIdentifiers.KARILS_CROSSBOW, 1),
			new Item(ItemIdentifiers.KARILS_TOP, 1),
			new Item(ItemIdentifiers.KARILS_SKIRT, 1),
			new Item(ItemIdentifiers.TORAGS_HELM, 1),
			new Item(ItemIdentifiers.TORAGS_HAMMER, 1),
			new Item(ItemIdentifiers.TORAGS_PLATEBODY, 1),
			new Item(ItemIdentifiers.TORAGS_PLATELEGS, 1),
			new Item(ItemIdentifiers.VERACS_HELM, 1),
			new Item(ItemIdentifiers.VERACS_FLAIL, 1),
			new Item(ItemIdentifiers.VERACS_BRASSARD, 1),
			new Item(ItemIdentifiers.VERACS_PLATESKIRT, 1),
			new Item(ItemIdentifiers.AKRISAES_HOOD, 1),
			new Item(ItemIdentifiers.AKRISAES_WAR_MACE, 1),
			new Item(ItemIdentifiers.AKRISAES_ROBE_TOP, 1),
			new Item(ItemIdentifiers.AKRISAES_ROBE_SKIRT, 1)};

	private int headComponentId, timer;

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof BarrowsBrother && target != this.target) {
			player.sm("This isn't your target.");
			return false;
		}
		return true;
	}

	public void drop(Item item) {
		Item dropItem = new Item(item.getId(), Utils.random((int) (item.getDefinitions().isStackable()
				? item.getAmount() : item.getAmount())) + 1);
		if (dropItem.getId() == 995) {
			player.getMoneyPouch().sendDynamicInteraction(dropItem.getAmount(), false);
			return;
		}
		player.getInventory().addItemDrop(item.getId(), item.getAmount());
	}

	private void exit(WorldTile outside) {
		player.setNextWorldTile(outside);
		leave(false);
	}

	@Override
	public void forceClose() {
		leave(true);
	}

	public int getAndIncreaseHeadIndex() {
		Integer head = (Integer) player.getTemporaryAttributtes().remove("BarrowsHead");
		if (head == null || head == player.getKilledBarrowBrothers().length - 1)
			head = 0;
		player.getTemporaryAttributtes().put("BarrowsHead", head + 1);
		return player.getKilledBarrowBrothers()[head] ? head : -1;
	}

	public int getRandomBrother() {
		List<Integer> bros = new ArrayList<Integer>();
		for (int i = 0; i < Hills.values().length; i++) {
			if (player.getKilledBarrowBrothers()[i] || player.getHiddenBrother() == i)
				continue;
			bros.add(i);
		}
		if (bros.isEmpty())
			return -1;
		return bros.get(Utils.random(bros.size()));
	};

	public int getSarcophagusId(int objectId) {
		switch (objectId) {
		case 66017:
			return 0;
		case 63177:
			return 1;
		case 66020:
			return 2;
		case 66018:
			return 3;
		case 66019:
			return 4;
		case 66016:
			return 5;
		case 61189:
			return 6;
		default:
			return -1;
		}
	}

	private void leave(boolean logout) {
		if(target != null)
			target.finish(); //target also calls removing hint icon at remove
		if(!logout) {
			player.getPackets().sendStopCameraShake();
			player.getInterfaceManager().closeScreenInterface();
			player.getInterfaceManager().closeOverlay(true);
			player.getControllerManager().forceStop();
			player.getControllerManager().removeControllerWithoutCheck();
			player.getPackets().sendBlackOut(0); //unblacks minimap
			if (player.getHiddenBrother() == -1)
				player.getPackets().sendStopCameraShake();
			else
				player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 11 : 0); //removes inter
			removeController();
			player.getPackets().sendStopCameraShake();
		}
	}

	public void loadData() {
		resetHeadTimer();
		for (int i = 0; i < player.getKilledBarrowBrothers().length; i++)
			sendBrotherSlain(i, player.getKilledBarrowBrothers()[i]);
		sendCreaturesSlainCount(player.getBarrowsKillCount());
		player.getPackets().sendBlackOut(2); //blacks minimap
	}

	@Override
	public boolean login() {
		if (player.getHiddenBrother() == -1)
			player.getPackets().sendCameraShake(3, 25, 50, 25, 50);
		loadData();
		sendInterfaces();
		return false;
	}

	@Override
	public boolean logout() {
		leave(true);
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		leave(false);
	}

	@Override
	public void process() {
		if (timer > 0) {
			timer--;
			return;
		}
		if (headComponentId == 0) {
			if (player.getHiddenBrother() == -1) {
				player.applyHit(new Hit(player, Utils.random(50) + 1, HitLook.REGULAR_DAMAGE));
				resetHeadTimer();
				return;
			}
			int headIndex = getAndIncreaseHeadIndex();
			if (headIndex == -1) {
				resetHeadTimer();
				return;
			}
			headComponentId = 9 + Utils.random(2);
			player.getPackets().sendItemOnIComponent(24, headComponentId, 4761 + headIndex, 0);
			player.getPackets().sendIComponentAnimation(9810, 24, headComponentId);
			int activeLevel = player.getPrayer().getPrayerpoints();
			if (activeLevel > 0) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER) * 10;
				player.getPrayer().drainPrayer(level / 6);
			}
			timer = 3;
		} else {
			player.getPackets().sendItemOnIComponent(24, headComponentId, -1, 0);
			headComponentId = 0;
			resetHeadTimer();
		}
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() >= 6702 && object.getId() <= 6707) {
			WorldTile out = Hills.values()[object.getId() - 6702].outBound;
			// cant make a perfect middle since 3/ 2 wont make a real integer
			// number or wahtever u call it..
			exit(new WorldTile(out.getX() + 1, out.getY() + 1, out.getPlane()));
			return false;
		} else if (object.getId() == 10284) {
			if (player.getHiddenBrother() == -1) {// reached chest
				player.sm("You found nothing.");
				return false;
			}
			if (!player.getKilledBarrowBrothers()[player.getHiddenBrother()])
				sendTarget(2025 + player.getHiddenBrother(), player);
			if (target != null) {
				player.sm("You found nothing.");
				return false;
			}
			sendReward();
			leave(false);
			player.getPackets().sendSpawnedObject(new WorldObject(6775, 10, 0, 3551, 9695, 0));
			player.resetBarrows();
			player.getStatistics().incrementBarrowsRunsDone();
			player.setNextWorldTile(new WorldTile(3565, 3306, 0));
			return false;
		} else if (object.getId() >= 6716 && object.getId() <= 6749) {
			WorldTile walkTo;
			if (object.getRotation() == 0)
				walkTo = new WorldTile(object.getX() + 5, object.getY(), 0);
			else if (object.getRotation() == 1)
				walkTo = new WorldTile(object.getX(), object.getY() - 5, 0);
			else if (object.getRotation() == 2)
				walkTo = new WorldTile(object.getX() - 5, object.getY(), 0);
			else
				walkTo = new WorldTile(object.getX(), object.getY() + 5, 0);
			if (!World.isNotCliped(walkTo.getPlane(), walkTo.getX(), walkTo.getY(), 1))
				return false;
			player.addWalkSteps(walkTo.getX(), walkTo.getY(), -1, false);
			player.lock(6);
			if (player.getHiddenBrother() != -1) {
				int brother = getRandomBrother();
				if (brother != -1)
					if (brother < 6)
						sendTarget(2025 + brother, walkTo);
					else
						sendTarget(14297, walkTo);
			}
			return false;
		} else {
			int sarcoId = getSarcophagusId(object.getId());
			if (sarcoId != -1) {
				if (sarcoId == player.getHiddenBrother())
					player.getDialogueManager().startDialogue("BarrowsD");
				else if (target != null || player.getKilledBarrowBrothers()[sarcoId])
					player.sm("You found nothing.");
				else if (sarcoId != 6)
					sendTarget(2025 + sarcoId, player);
				else
					sendTarget(14297, player);
				return false;
			}
		}
		return true;
	}

	public void resetHeadTimer() {
		timer = 20 + Utils.random(6);
	}

	public void sendBrotherSlain(int index, boolean slain) {
		if (index == 6)
			return;
		player.getPackets().sendConfigByFile(457 + index, slain ? 1 : 0);
	}

	public void sendCreaturesSlainCount(int count) {
		player.getPackets().sendConfigByFile(464, count);
	}

	@Override
	public boolean sendDeath() {
		leave(false);
		return true;
	}

	@Override
	public void sendInterfaces() {
		if (player.getHiddenBrother() != -1)
			player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? 11 : 0, 24);
	}

	public void sendReward() {
		double percentage = 0;
		for (boolean died : player.getKilledBarrowBrothers()) {
			if (died)
				percentage += 2.5;
		}
		percentage += (player.getBarrowsKillCount() / 40d);
		if (percentage > 90)
			percentage = 90;
		if ((player.getInventory().containsItem(20072, 1) || player.getEquipment().containsOneItem(20072)) && percentage >= Math.random() * 95) {
			drop(new Item(ItemIdentifiers.CORRUPTION_SIGIL));

		}
		if (percentage >= Math.random() * 95) {
			drop(BARROW_REWARDS[Utils.random(BARROW_REWARDS.length)]);

		}
		if (percentage / 2 >= Math.random() * 100) {
			drop(RARE_REWARDS[Utils.random(RARE_REWARDS.length)]);
		}
		for (int i = 0; i < 10; i++)
			if (percentage >= Math.random() * 100)
				drop(COMMON_REWARDS[Utils.random(COMMON_REWARDS.length)]);
	}

	public void sendTarget(int id, WorldTile tile) {
		if (target != null)
			target.disapear();
		target = new BarrowsBrother(id, tile, this);
		target.setTarget(player);
		target.setNextForceTalk(new ForceTalk("You dare disturb my rest!"));
		player.getHintIconsManager().addHintIcon(target, 1, -1, false);
	}

	public void setBrotherSlained(int index) {
		player.getKilledBarrowBrothers()[index] = true;
		sendBrotherSlain(index, true);
	}

	@Override
	public void start() {
		if (player.getHiddenBrother() == -1)
			player.setHiddenBrother(Utils.random(Hills.values().length));
		loadData();
		sendInterfaces();
	}

	public void targetDied() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		if (player.getHiddenBrother() < 6 && target.getId() != 14297)
			setBrotherSlained(target.getId() - 2025);
		else
			setBrotherSlained(6);
		target = null;

	}

	public void targetFinishedWithoutDie() {
		player.getHintIconsManager().removeUnsavedHintIcon();
		target = null;
	}
}