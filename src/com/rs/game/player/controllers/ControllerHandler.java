package com.rs.game.player.controllers;

import java.util.HashMap;

import com.rs.game.minigames.BrimhavenAgility;
import com.rs.game.minigames.RefugeOfFear;
import com.rs.game.minigames.clanwars.FfaZone;
import com.rs.game.minigames.clanwars.RequestController;
import com.rs.game.minigames.clanwars.WarControler;
import com.rs.game.minigames.duel.DuelArena;
import com.rs.game.minigames.duel.DuelControler;
import com.rs.game.minigames.hunger.HungerGamesControler;
import com.rs.game.player.content.activities.gungame.Survival;
import com.rs.game.player.content.activities.ports.PlayerPortsController;
import com.rs.game.player.content.dungeoneering.DungeonController;
import com.rs.game.player.controllers.castlewars.CastleWarsPlaying;
import com.rs.game.player.controllers.castlewars.CastleWarsWaiting;
import com.rs.game.player.controllers.events.DeathEvent;
import com.rs.game.player.controllers.events.DeathEventRS3;
import com.rs.game.player.controllers.fightpits.FightPitsArena;
import com.rs.game.player.controllers.fightpits.FightPitsLobby;
import com.rs.game.player.controllers.instances.ArmadylInstance;
import com.rs.game.player.controllers.instances.BandosInstance;
import com.rs.game.player.controllers.instances.BlinkInstance;
import com.rs.game.player.controllers.instances.CorpInstance;
import com.rs.game.player.controllers.instances.DagannothKingInstance;
import com.rs.game.player.controllers.instances.GiantMoleInstance;
import com.rs.game.player.controllers.instances.KalphiteKingInstance;
import com.rs.game.player.controllers.instances.KalphiteQueenInstance;
import com.rs.game.player.controllers.instances.KingBlackDragonInstance;
import com.rs.game.player.controllers.instances.LegioPrimusInstance;
import com.rs.game.player.controllers.instances.NexInstance;
import com.rs.game.player.controllers.instances.RegularInstance;
import com.rs.game.player.controllers.instances.SaradominInstance;
import com.rs.game.player.controllers.instances.SharedInstance;
import com.rs.game.player.controllers.instances.SunfreetInstance;
import com.rs.game.player.controllers.instances.ZamorakInstance;
import com.rs.game.player.controllers.instances.gwd2.BossInstanceController;
import com.rs.game.player.controllers.instances.gwd2.GodWars2BossInstanceController;
import com.rs.game.player.controllers.minigames.Barrows;
import com.rs.game.player.controllers.minigames.SophanemSlayerDungeon;
import com.rs.game.player.controllers.pestcontrol.PestControlGame;
import com.rs.game.player.controllers.pestcontrol.PestControlLobby;
import com.rs.game.player.controllers.tutorial.DiccusTutorial;
import com.rs.utils.Logger;

public class ControllerHandler {

	private static final HashMap<Object, Class<? extends Controller>> handledControllers = new HashMap<Object, Class<? extends Controller>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Controller> value1 = (Class<Controller>) Class.forName(Wilderness.class.getCanonicalName());handledControllers.put("Wilderness", value1);
			Class<Controller> value2 = (Class<Controller>) Class.forName(Kalaboss.class.getCanonicalName());handledControllers.put("Kalaboss", value2);
			Class<Controller> value4 = (Class<Controller>) Class.forName(GodWars.class.getCanonicalName());handledControllers.put("GodWars", value4);
			Class<Controller> value5 = (Class<Controller>) Class.forName(ZGDControler.class.getCanonicalName());handledControllers.put("ZGDControler", value5);
			Class<Controller> value6 = (Class<Controller>) Class.forName(TutorialIsland.class.getCanonicalName());handledControllers.put("TutorialIsland", value6);
			Class<Controller> value7 = (Class<Controller>) Class.forName(StartTutorial.class.getCanonicalName());handledControllers.put("StartTutorial", value7);
			Class<Controller> value9 = (Class<Controller>) Class.forName(DuelArena.class.getCanonicalName());handledControllers.put("DuelArena", value9);
			Class<Controller> value10 = (Class<Controller>) Class.forName(DuelControler.class.getCanonicalName());handledControllers.put("DuelControler", value10);
			Class<Controller> value11 = (Class<Controller>) Class.forName(CorpBeastControler.class.getCanonicalName());handledControllers.put("CorpBeastControler", value11);
			Class<Controller> value14 = (Class<Controller>) Class.forName(DTControler.class.getCanonicalName());handledControllers.put("DTControler", value14);
			Class<Controller> value15 = (Class<Controller>) Class.forName(JailControler.class.getCanonicalName());handledControllers.put("JailControler", value15);
			Class<Controller> value17 = (Class<Controller>) Class.forName(CastleWarsPlaying.class.getCanonicalName());handledControllers.put("CastleWarsPlaying", value17);
			Class<Controller> value18 = (Class<Controller>) Class.forName(CastleWarsWaiting.class.getCanonicalName());handledControllers.put("CastleWarsWaiting", value18);
			Class<Controller> value20 = (Class<Controller>) Class.forName(NewHomeControler.class.getCanonicalName());handledControllers.put("NewHomeControler", value20);
			handledControllers.put("JadinkoLair", (Class<Controller>) Class.forName(JadinkoLair.class.getCanonicalName()));
			handledControllers.put("RegularInstance", (Class<Controller>) Class.forName(RegularInstance.class.getCanonicalName()));
			handledControllers.put("PlayerPortsController", (Class<Controller>) Class.forName(PlayerPortsController.class.getCanonicalName()));
			handledControllers.put("AraxxiController", (Class<Controller>) Class.forName(AraxxiController.class.getCanonicalName()));
			handledControllers.put("Survival", (Class<Controller>) Class.forName(Survival.class.getCanonicalName()));
			handledControllers.put("AraxyteHyveController",(Class<Controller>) Class.forName(AraxyteHyveController.class.getCanonicalName()));
			handledControllers.put("KalphiteQueenInstance", (Class<Controller>) Class.forName(KalphiteQueenInstance.class.getCanonicalName()));
			handledControllers.put("KalphiteKingInstance", (Class<Controller>) Class.forName(KalphiteKingInstance.class.getCanonicalName()));
			handledControllers.put("DagannothKingInstance", (Class<Controller>) Class.forName(DagannothKingInstance.class.getCanonicalName()));
			handledControllers.put("KingBlackDragonInstance", (Class<Controller>) Class.forName(KingBlackDragonInstance.class.getCanonicalName()));
			handledControllers.put("ZGDControler", (Class<Controller>) Class.forName(ZGDControler.class.getCanonicalName()));
			handledControllers.put("BandosInstance", (Class<Controller>) Class.forName(BandosInstance.class.getCanonicalName()));
			handledControllers.put("SharedInstance", (Class<Controller>) Class.forName(SharedInstance.class.getCanonicalName()));
			handledControllers.put("NexInstance", (Class<Controller>) Class.forName(NexInstance.class.getCanonicalName()));
			handledControllers.put("ArmadylInstance", (Class<Controller>) Class.forName(ArmadylInstance.class.getCanonicalName()));
			handledControllers.put("LegioPrimusInstance", (Class<Controller>) Class.forName(LegioPrimusInstance.class.getCanonicalName()));
			handledControllers.put("ZamorakInstance", (Class<Controller>) Class.forName(ZamorakInstance.class.getCanonicalName()));
			handledControllers.put("GiantMoleInstance", (Class<Controller>) Class.forName(GiantMoleInstance.class.getCanonicalName()));
			handledControllers.put("SaradominInstance", (Class<Controller>) Class.forName(SaradominInstance.class.getCanonicalName()));
			handledControllers.put("BlinkInstance", (Class<Controller>) Class.forName(BlinkInstance.class.getCanonicalName()));
			handledControllers.put("SunfreetInstance", (Class<Controller>) Class.forName(SunfreetInstance.class.getCanonicalName()));
			handledControllers.put("CorpInstance", (Class<Controller>) Class.forName(CorpInstance.class.getCanonicalName()));
			handledControllers.put("clan_wars_request", (Class<Controller>) Class.forName(RequestController.class.getCanonicalName()));
			handledControllers.put("clan_war", (Class<Controller>) Class.forName(WarControler.class.getCanonicalName()));
			handledControllers.put("clan_wars_ffa", (Class<Controller>) Class.forName(FfaZone.class.getCanonicalName()));
			handledControllers.put("NomadsRequiem", (Class<Controller>) Class.forName(NomadsRequiem.class.getCanonicalName()));
			handledControllers.put("BorkController", (Class<Controller>) Class.forName(BorkController.class.getCanonicalName()));	
			handledControllers.put("BrimhavenAgility", (Class<Controller>) Class.forName(BrimhavenAgility.class.getCanonicalName()));
			handledControllers.put("FightCavesControler", (Class<Controller>) Class.forName(FightCaves.class.getCanonicalName()));
			handledControllers.put("FightKilnControler", (Class<Controller>) Class.forName(FightKiln.class.getCanonicalName()));
			handledControllers.put("FightPitsLobby", (Class<Controller>) Class.forName(FightPitsLobby.class.getCanonicalName()));
			handledControllers.put("FightPitsArena", (Class<Controller>) Class.forName(FightPitsArena.class.getCanonicalName()));
			handledControllers.put("PestControlGame", (Class<Controller>) Class.forName(PestControlGame.class.getCanonicalName()));
			handledControllers.put("PestControlLobby", (Class<Controller>) Class.forName(PestControlLobby.class.getCanonicalName()));
			handledControllers.put("Barrows", (Class<Controller>) Class.forName(Barrows.class.getCanonicalName()));
			handledControllers.put("RefugeOfFear", (Class<Controller>) Class.forName(RefugeOfFear.class.getCanonicalName()));
			handledControllers.put("BarrelchestControler", (Class<Controller>) Class.forName(BarrelchestControler.class.getCanonicalName()));
			handledControllers.put("Falconry", (Class<Controller>) Class.forName(Falconry.class.getCanonicalName()));
			handledControllers.put("QueenBlackDragonController", (Class<Controller>) Class.forName(QueenBlackDragonController.class.getCanonicalName()));
			handledControllers.put("RuneSpanControler", (Class<Controller>) Class.forName(RunespanControler.class.getCanonicalName()));
			handledControllers.put("DeathEvent", (Class<Controller>) Class.forName(DeathEvent.class.getCanonicalName()));
			handledControllers.put("DeathEventRS3", (Class<Controller>) Class.forName(DeathEventRS3.class.getCanonicalName()));
			handledControllers.put("SorceressGarden", (Class<Controller>) Class.forName(SorceressGarden.class.getCanonicalName()));
			handledControllers.put("CrucibleControler", (Class<Controller>) Class.forName(CrucibleControler.class.getCanonicalName()));
			handledControllers.put("PestControlGame", (Class<Controller>) Class.forName(PestControlGame.class.getCanonicalName()));
			handledControllers.put("PestControlLobby", (Class<Controller>) Class.forName(PestControlLobby.class.getCanonicalName()));
			handledControllers.put("HungerGamesControler", (Class<Controller>) Class.forName(HungerGamesControler.class.getCanonicalName()));
			handledControllers.put("ImpossibleJadControler", (Class<Controller>) Class.forName(ImpossibleJad.class.getCanonicalName()));
			handledControllers.put("DungeonController", (Class<Controller>) Class.forName(DungeonController.class.getCanonicalName()));
			handledControllers.put("RiseOfTheSix", (Class<Controller>) Class.forName(RiseOfTheSix.class.getCanonicalName()));
			handledControllers.put("VoragoController", (Class<Controller>) Class.forName(VoragoController.class.getCanonicalName()));
			handledControllers.put("PestInvasionControler", (Class<Controller>) Class.forName(PestInvasion.class.getCanonicalName()));
			handledControllers.put("MercArena", (Class<Controller>) Class.forName(MercArena.class.getCanonicalName()));
			handledControllers.put("ConstructionControler", (Class<Controller>) Class.forName(ConstructionControler.class.getCanonicalName()));
			handledControllers.put("HouseController", (Class<Controller>) Class.forName(HouseController.class.getCanonicalName()));
			handledControllers.put("SawmillController", (Class<Controller>) Class.forName(SawmillController.class.getCanonicalName()));
			handledControllers.put("DiccusTutorial", (Class<Controller>) Class.forName(DiccusTutorial.class.getCanonicalName()));
			handledControllers.put("SophanemSlayerDungeon", (Class<Controller>) Class.forName(SophanemSlayerDungeon.class.getCanonicalName()));
			handledControllers.put("BossInstanceController", (Class<Controller>) Class.forName(BossInstanceController.class.getCanonicalName()));
			handledControllers.put("GodWars2BossInstanceController", (Class<Controller>) Class.forName(GodWars2BossInstanceController.class.getCanonicalName()));
			
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void reload() {
		handledControllers.clear();
		init();
	}

	public static final Controller getController(Object key) {
		if (key instanceof Controller)
			return (Controller) key;
		Class<? extends Controller> classC = handledControllers.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (Throwable e) {
			Logger.handle(e);
		}
		return null;
	}
}
