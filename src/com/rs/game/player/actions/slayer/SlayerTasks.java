package com.rs.game.player.actions.slayer;

public enum SlayerTasks {
	/*
	 * Kuradel
	 */
	ABERRANT_SPECTRES("Aberrant spectres", TaskSet.KURADEL, 60, 20, 40, "Aberrant spectre"),
	ABYSSAL_DEMONS("Abyssal demons", TaskSet.KURADEL, 85, 20, 40, "Ayssal demon"),
	BLACK_DEMONS("Black demons", TaskSet.KURADEL, 1, 20, 40, "Black demon"),
	BLACK_DRAGONS("Black dragons", TaskSet.KURADEL, 1, 20, 40, "Black dragon"),
	BLUE_DRAGONS("Blue Dragons", TaskSet.KURADEL, 1, 20, 40, "Blue dragon"),
	BLOODVELDS("Bloodvelds", TaskSet.KURADEL, 50, 20, 40, "Bloodveld"),
	DAGANNOTHS("Dagannoths", TaskSet.KURADEL, 1, 20, 40, "Dagannoth", "Dagannoth Mother", "Dagannoth guardian", "Dagannoth spawn", "Dagannoth Prime", "Dagannoth Supreme", "Dagannoth Rex"),
	DARK_BEASTS("Dark beasts", TaskSet.KURADEL, 90, 20, 40, "Dark beast"), 
	DESERT_STRYKEWYRMS("Desert strykewyrms", TaskSet.KURADEL, 77, 20, 40, "Desert strykewyrms"),
	DUST_DEVILS("Dust devil", TaskSet.KURADEL, 65, 20, 40, "Dust devil"),
	FIRE_GIANTS("Fire giants", TaskSet.KURADEL, 1, 20, 40, "Fire giant"),
	GANODERMIC_BEASTS("Ganodermic beasts", TaskSet.KURADEL, 95, 20, 40, "Ganodermic beast"),
	GARGOYLES("Gargoyles", TaskSet.KURADEL, 75, 20, 40, "Gargoyle"),
	GREATER_DEMONS("Greater demons", TaskSet.KURADEL, 1, 20, 40, "Greater demon"),
	GRIFALOPINES("Grifalopines", TaskSet.KURADEL, 88, 20, 40, "Grifalopine"),
	GRIFALOROO("Grifaloroo", TaskSet.KURADEL, 82, 20, 40, "Grifaloroo"),
	GROTWORMS("Grotworms", TaskSet.KURADEL, 1, 20, 40, "Young grotworm", "Grotworm", "Mature grotworm"),
	HELLHOUNTS("Hellhounds", TaskSet.KURADEL, 1, 20, 40, "Hellhound"),
	ICE_STRYKEWYRMS("Ice strykewyrm", TaskSet.KURADEL, 93, 20, 40, "Ice strykewyrm"),
	IRON_DRAGONS("Iron dragons", TaskSet.KURADEL, 1, 20, 40, "Iron dragon"), 
	LIVING_ROCK_CREATURES("Living rock creatures", TaskSet.KURADEL, 1, 20, 40, "Living rock protector", "Living rock striker", "Living rock patriarch"),
	MITHRIL_DRAGONS("Mithril dragons", TaskSet.KURADEL, 1, 20, 40, "Mithril dragon"),
	MUTATED_JADINKOS("Mutated jadinkos", TaskSet.KURADEL, 80, 20, 40, "Mutated jadinko baby", "Mutated jadinko guard", "Mutated jadinko male"),
	NECHRYAELS("Nechryaels", TaskSet.KURADEL, 80, 20, 40, "Nechryael"),
	SKELETAL_WYVERNS("Skeletal Wyverns", TaskSet.KURADEL, 72, 20, 40, "Skeletal Wyvern"),
	SPIRITUAL_MAGES("Spiritual Mages", TaskSet.KURADEL, 83, 20, 40, "Spiritual mage"),
	STEEL_DRAGONS("Steel Dragons", TaskSet.KURADEL, 1, 20, 40, "Steel dragon"),
	OGRES("Ogres", TaskSet.KURADEL, 1, 30, 40, "Ogre", "Zogre", "Skogre", "Jogre"),
	ANKOUS("Ankou", TaskSet.KURADEL, 1, 40, 45, "Ankou"),
	WATERFIENDS("Waterfiends", TaskSet.KURADEL, 1, 20, 40, "Waterfiend"),
	COCKROACHES("Cockroaches", TaskSet.KURADEL, 1, 30, 40, "Cockroach drone", "Cockroach worker", "Cockroach soldier", "Cockroach_soldier", "Cockroach_worker", "Cockroach_drone");

	/**
	 * A simple name for the task
	 */
	public final String simpleName;

	/**
	 * The task set
	 */
	@SuppressWarnings("unused")
	private final TaskSet type;

	/**
	 * The monsters that will effect this task
	 */
	public final String[] slayable;
	/**
	 * The minimum amount of monsters the player may be assigned to kill
	 */
	@SuppressWarnings("unused")
	private final int min;
	/**
	 * The maximum amount of monsters the player may be assigned to kill
	 */
	@SuppressWarnings("unused")
	private final int max;
	/*
	 * Slayer level for monsters
	 */
	private final int level;

	SlayerTasks(String simpleName, TaskSet type, int level, int min, int max, String... monsters) {
		this.type = type;
		this.slayable = monsters;
		this.simpleName = simpleName;
		this.level = level;
		this.min = min;
		this.max = max;
	}

	public int getLevel() {
		return level;
	}
}