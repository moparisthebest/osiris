package osiris.game.model;

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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import osiris.game.update.UpdateFlags.UpdateFlag;

/**
 * A characters movement queue.
 * 
 * @author Blake
 * 
 */
public class MovementQueue {

	/**
	 * The maximum size of the queue. If any additional steps are added, they
	 * are discarded.
	 */
	private static final int MAXIMUM_SIZE = 128;

	/**
	 * Represents a single point in the queue.
	 * 
	 * @author Graham Edgecombe
	 */
	private static final class Point {

		/**
		 * The point's position.
		 */
		private final Position position;

		/**
		 * The direction to walk to this point.
		 */
		private final Direction direction;

		/**
		 * Creates a point.
		 * 
		 * @param position
		 *            The position.
		 * @param direction
		 *            The direction.
		 */
		public Point(Position position, Direction direction) {
			this.position = position;
			this.direction = direction;
		}

		@Override
		public String toString() {
			return Point.class.getName() + " [direction=" + direction + ", position=" + position + "]";
		}

		public Position getPosition() {
			return position;
		}

	}

	/**
	 * The character whose walking queue this is.
	 */
	private Character character;

	private boolean locked;

	/**
	 * The queue of directions.
	 */
	private Deque<Point> points = new ArrayDeque<Point>();

	/**
	 * The old queue of directions.
	 */
	private Deque<Point> oldPoints = new ArrayDeque<Point>();

	/**
	 * Flag indicating if this queue (only) should be ran.
	 */
	private boolean runningQueue;

	/**
	 * Creates a walking queue for the specified character.
	 * 
	 * @param character
	 *            The character.
	 */
	public MovementQueue(Character character) {
		this.character = character;
	}

	/**
	 * Called every pulse, updates the queue.
	 */
	public void cycle() {
		Position position = character.getPosition();
		Direction first = Direction.NONE;
		Direction second = Direction.NONE;
		Point next = points.poll();
		if (next != null) {
			first = next.direction;
			position = next.position;
			if (runningQueue || character.isRunToggled()) {
				if (character.getEnergy() > 0) {
					next = points.poll();
					if (next != null) {
						second = next.direction;
						position = next.position;
						if (!(character instanceof Player) || (((Player) character).getPlayerStatus() < 2))
							character.setEnergy(character.getEnergy() - 1, true);
					}
				} else {
					if (character.isRunToggled()) {
						character.setRunToggle(false);
						runningQueue = false;
					}
					runningQueue = false;
				}
			}
		} else {
			if (locked)
				locked = false;
		}
		character.setPrimaryDirection(first);
		character.setSecondaryDirection(second);
		character.setPosition(position);
		if (first != Direction.NONE) {
			character.getUpdateFlags().flag(UpdateFlag.UPDATE);
		}
	}

	/**
	 * Sets the running queue flag.
	 * 
	 * @param running
	 *            The running queue flag.
	 */
	public void setRunningQueue(boolean running) {
		this.runningQueue = running;
	}

	/**
	 * Gets the running queue flag.
	 * 
	 * @return True if the player is running, false if not.
	 */
	public boolean getRunningQueue() {
		return runningQueue;
	}

	/**
	 * Adds the first step to the queue, attempting to connect the server and
	 * client position by looking at the previous queue.
	 * 
	 * @param clientConnectionPosition
	 *            The first step.
	 * @return {@code true} if the queues could be connected correctly,
	 *         {@code false} if not.
	 */
	public boolean addFirstStep(Position clientConnectionPosition) {
		Position serverPosition = character.getPosition();
		int deltaX = clientConnectionPosition.getX() - serverPosition.getX();
		int deltaY = clientConnectionPosition.getY() - serverPosition.getY();
		if (Direction.isConnectable(deltaX, deltaY)) {
			points.clear();
			oldPoints.clear();

			addStep(clientConnectionPosition);
			return true;
		}
		Queue<Position> travelBackQueue = new ArrayDeque<Position>();
		Point oldPoint;
		while ((oldPoint = oldPoints.pollLast()) != null) {
			Position oldPosition = oldPoint.position;
			deltaX = oldPosition.getX() - serverPosition.getX();
			deltaY = oldPosition.getX() - serverPosition.getY();
			travelBackQueue.add(oldPosition);
			if (Direction.isConnectable(deltaX, deltaY)) {
				points.clear();
				oldPoints.clear();
				for (Position travelBackPosition : travelBackQueue) {
					addStep(travelBackPosition);
				}
				addStep(clientConnectionPosition);
				return true;
			}
		}
		oldPoints.clear();
		return false;
	}

	/**
	 * Adds a step to the queue.
	 * 
	 * @param step
	 *            The step to add.
	 */
	public void addStep(Position step) {
		Point last = getLast();
		int x = step.getX();
		int y = step.getY();
		int deltaX = x - last.position.getX();
		int deltaY = y - last.position.getY();
		int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0) {
				deltaX++;
			} else if (deltaX > 0) {
				deltaX--;
			}
			if (deltaY < 0) {
				deltaY++;
			} else if (deltaY > 0) {
				deltaY--;
			}
			addStep(x - deltaX, y - deltaY);
		}
	}

	/**
	 * Adds a step.
	 * 
	 * @param x
	 *            The x coordinate of this step.
	 * @param y
	 *            The y coordinate of this step.
	 */
	private void addStep(int x, int y) {
		if (points.size() >= MAXIMUM_SIZE) {
			return;
		}
		Point last = getLast();
		int deltaX = x - last.position.getX();
		int deltaY = y - last.position.getY();
		Direction direction = Direction.fromDeltas(deltaX, deltaY);
		if (direction != Direction.NONE) {
			Point p = new Point(new Position(x, y, character.getPosition().getZ()), direction);
			points.add(p);
			oldPoints.add(p);
		}
	}

	/**
	 * Gets the last point.
	 * 
	 * @return The last point.
	 */
	private Point getLast() {
		Point last = points.peekLast();
		if (last == null) {
			return new Point(character.getPosition(), Direction.NONE);
		}
		return last;
	}

	public Position getLastPosition() {
		Point last = getLast();
		if (last != null)
			return last.position;
		return null;
	}

	public void reset() {
		if (locked)
			return;
		points.clear();
		oldPoints.clear();
		character.setPrimaryDirection(Direction.NONE);
		character.setSecondaryDirection(Direction.NONE);
		/*
		 * if (character instanceof Player) ((Player)
		 * character).getEventWriter().stopMovement();
		 */
	}

	public boolean isEmpty() {
		return points.isEmpty();
	}

	public boolean isLocked() {
		return locked;
	}

	public void lock() {
		locked = true;
	}

}
