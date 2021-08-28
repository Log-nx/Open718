package com.rs.game.npc.slayer;

import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Utils;

@SuppressWarnings("serial")
public class Strykewyrm extends NPC {

	private int stompId;

	public Strykewyrm(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, true);
		stompId = id;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		if (getId() != stompId && !isCantInteract() && !isUnderCombat()) {
			setNextAnimation(new Animation(12796));
			setCantInteract(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					transformIntoNPC(9462);
					setCantInteract(false);
				}
			});
		}
	}

	@Override
	public void reset() {
		setNPC(stompId);
		super.reset();
	}

	public static void handleStomping(final Player player, final NPC npc) {
		if (npc.isCantInteract())
			return;
		int requiredLevel = npc.getId() == 20629 ? 94
				: npc.getId() == 9462 ? 93 : npc.getId() == 9464 ? 77 : npc
						.getId() == 9466 ? 73 : 1;
		if (player.getSkills().getLevel(Skills.SLAYER) < requiredLevel) {
			player.getPackets().sendGameMessage(
					"You need a slayer level of at least " + requiredLevel
							+ " to fight that stykewyrm.");
			return;
		}
		player.setNextAnimation(new Animation(4278));
		npc.setCantInteract(true);
		WorldTasksManager.schedule(new WorldTask() {

			int ticks;

			@Override
			public void run() {
				ticks++;
				if (ticks == 2) {
					npc.setNextAnimation(new Animation(12795));
					npc.setNextNPCTransformation(npc.getId() + 1);
					npc.setHitpoints(npc.getMaxHitpoints());
				} else if (ticks == 4) {
					npc.getCombat().setTarget(player);
					npc.setCantInteract(false);
					stop();
					return;
				}
			}
		}, 0, 0);
	}

}
