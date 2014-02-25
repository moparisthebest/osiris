package osiris;

/*
 * Osiris Emulator
 * Copyright (C) 2011  Garrett Woodard, Blake Beaupain, Travis Burtrum
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import static osiris.util.Settings.NPC_MOVEMENT_TIME;
import static osiris.util.Settings.TICKRATE;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import osiris.game.event.GameEvent;
import osiris.game.event.impl.ServiceRequestEvent;
import osiris.game.model.Character;
import osiris.game.model.DeathManager;
import osiris.game.model.InfoUpdater;
import osiris.game.model.Npc;
import osiris.game.model.NpcMovement;
import osiris.game.model.Player;
import osiris.game.model.combat.CombatManager;
import osiris.game.model.combat.Hit;
import osiris.game.model.ground.GroundManager;
import osiris.util.Settings;

// TODO: Auto-generated Javadoc
/**
 * The main server engine.
 * 
 * @author Blake
 * 
 */
public class ServerEngine implements Runnable, ThreadFactory {

	/**
	 * The scheduler.
	 */
	private static ScheduledExecutorService scheduler;

	/**
	 * The Constant eventsToProcess.
	 */
	private static final Queue<GameEvent> eventsToProcess = new LinkedList<GameEvent>();

	/** The hit queue. */
	private static PriorityQueue<Hit> hitQueue;

	/** The death manager. */
	private static DeathManager deathManager;

	/** The lockdown. */
	private static boolean lockdown = false;

	/**
	 * The ticks.
	 */
	private static int ticks = 0;

	/**
	 * Starts the server engine.
	 */
	public static void start() {
		// Create a new ServerEngine instance.
		ServerEngine instance = new ServerEngine();
		deathManager = new DeathManager();

		// Initialize the scheduler and schedule the engine instance.
		scheduler = Executors.newSingleThreadScheduledExecutor(instance);
		scheduler.scheduleAtFixedRate(instance, 0, TICKRATE, TimeUnit.MILLISECONDS);

		// Schedule any other utilities.
		scheduler.scheduleAtFixedRate(GroundManager.getManager(), 0, 3, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(new NpcMovement(), 0, NPC_MOVEMENT_TIME, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(new InfoUpdater(), 0, 600, TimeUnit.MILLISECONDS);
		scheduler.scheduleAtFixedRate(deathManager, 0, 600, TimeUnit.MILLISECONDS);

		hitQueue = new PriorityQueue<Hit>(1, new Comparator<Hit>() {
			@Override
			public int compare(Hit a, Hit b) {
				return b.getTimer().getStart() - a.getTimer().getStart();
			}
		}) {
			private static final long serialVersionUID = 412420407031048943L;

			@Override
			public boolean add(Hit hit) {
				return hit.getAttacker().isInSight(hit.getVictim()) && super.add(hit);
			}
		};

	}

	/**
	 * Stops the server engine.
	 */
	public static void stop() {
		getScheduler().shutdown();
	}

	/**
	 * Queues a game event for processing.
	 * 
	 * @param event
	 *            the event
	 */
	public static void queueGameEvent(GameEvent event) {
		synchronized (eventsToProcess) {
			eventsToProcess.add(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#cycle()
	 */

	@Override
	public void run() {

		// 1. Process all game events.
		synchronized (eventsToProcess) {
			while (!eventsToProcess.isEmpty()) {
				// Get the next event.
				GameEvent event = eventsToProcess.poll();
				try {
					// Attempt to process the event.
					event.process();
					event.getPlayer().getTimeoutTimer().reset();
				} catch (Exception ex) {
					// Whoops! Something went wrong. Clean up.
					if (event.getPlayer() != null) {
						ex.printStackTrace();
						// Log out the problematic player.
						event.getPlayer().logout();
					} else if (event.getClass() == ServiceRequestEvent.class || event.getClass().getSuperclass() == ServiceRequestEvent.class) {
						// Or close the problematic service requesting channel.
						ServiceRequestEvent e = (ServiceRequestEvent) event;
						e.getChannel().close();
					} else {
						// Or what the fuck happened?
						ex.printStackTrace();
					}
				}
				event = null;
			}
		}

		// 2. Update all players.
		try {
			synchronized (Main.getPlayers()) {
				CombatManager.updateHits();
				// Perform the pre-update procedures for players and NPCs.
				for (Player player : Main.getPlayers()) {
					if ((player.getInteractingCharacter() instanceof Player && !Main.getPlayers().contains((Player) player.getInteractingCharacter())) || player.getInteractingCharacter() instanceof Npc && !Main.getNpcs().contains((Npc) player.getInteractingCharacter()))
						player.setInteractingCharacter(null);

					// Timeout checking.
					if (player.getTimeoutTimer().elapsed() > Settings.TIMEOUT_DELAY) {
						player.logout();
						continue;
					}

					// Pre-updating.
					player.handleQueuedUpdateBlocks();
					player.getCombatManager().cycle();
					player.getMovementQueue().cycle();
				}
				for (Npc npc : Main.getNpcs()) {
					if ((npc.getInteractingCharacter() instanceof Player && !Main.getPlayers().contains((Player) npc.getInteractingCharacter())) || npc.getInteractingCharacter() instanceof Npc && !Main.getNpcs().contains((Npc) npc.getInteractingCharacter()))
						npc.setInteractingCharacter(null);
					npc.handleQueuedUpdateBlocks();
					npc.getCombatManager().cycle();
					npc.getMovementQueue().cycle();
				}

				// Perform the actual update procedures.
				for (Player player : Main.getPlayers()) {
					player.getPlayerUpdater().run();
					player.getNpcUpdater().run();
				}

				// Perform the post-update procedures for players and NPCs.
				for (Player player : Main.getPlayers()) {
					player.getUpdateFlags().reset();
					player.getUpdateBlocks().clear();
					player.adjustSpecialEnergy(player.specialRegainRate());
				}
				for (Npc npc : Main.getNpcs()) {
					npc.getUpdateFlags().reset();
					npc.getUpdateBlocks().clear();
				}
			}
		} catch (Exception ex) {
			// Something bad happened while updating.
			ex.printStackTrace();
		}

		// 3. Update any other server logic.
		try {

			for (Player player : Main.getPlayers()) {
				if (ticks % 6 == 0) {
					if (!player.getMovementQueue().getRunningQueue() && player.getEnergy() < 100) {
						player.setEnergy(player.getEnergy() + 1, true);
					}
				}
			}
		} catch (Exception ex) {
			// Something bad happened while processing logic.
			ex.printStackTrace();
		}

		ticks++; // Record this tick.
	}

	/**
	 * Gets the ticks.
	 * 
	 * @return the ticks
	 */
	public static int getTicks() {
		return ticks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */

	@Override
	public Thread newThread(Runnable task) {
		// Make sure our thread gets the highest priority.
		Thread thread = new Thread(task);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setName("ServerEngine");
		return thread;
	}

	/**
	 * Send global message.
	 * 
	 * @param msg
	 *            the msg
	 */
	public static void sendGlobalMessage(String msg) {
		for (Player player : Main.getPlayers()) {
			player.getEventWriter().sendMessage(msg);
		}
	}

	/**
	 * Gets the scheduler.
	 * 
	 * @return the scheduler
	 */
	public static ScheduledExecutorService getScheduler() {
		return scheduler;
	}

	/**
	 * Gets the hit queue.
	 * 
	 * @return the hit queue
	 */
	public static PriorityQueue<Hit> getHitQueue() {
		return hitQueue;
	}

	/**
	 * Gets the death manager.
	 * 
	 * @return the death manager
	 */
	public static DeathManager getDeathManager() {
		return deathManager;
	}

	/**
	 * Sets the lockdown.
	 */
	public static void setLockdown() {
		lockdown = true;
	}

	/**
	 * Checks if is under lockdown.
	 * 
	 * @return true, if is under lockdown
	 */
	public static boolean isUnderLockdown() {
		return lockdown;
	}

	/**
	 * Static graphic.
	 * 
	 * @param character
	 *            the character
	 * @param id
	 *            the id
	 * @param height
	 *            the height
	 */
	public static void staticGraphic(Character character, int id, int height) {
		for (Player player : Main.getPlayers())
			if (player.isInSight(character))
				player.getEventWriter().spawnGfx(id, height, character.getPosition());
	}

}
