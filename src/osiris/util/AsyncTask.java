package osiris.util;

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

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * An asynchronous task object that calls a method upon completion instead of
 * blocking and waiting for the task to finish.
 * 
 * @author Blake
 * @param <V>
 *            the value type
 * 
 */
public class AsyncTask<V> extends FutureTask<V> {

	/**
	 * The Interface CompletionReceiver.
	 * 
	 * @param <V>
	 *            the value type
	 */
	public static interface CompletionReceiver<V> {

		/**
		 * On complete.
		 * 
		 * @param task
		 *            the task
		 */
		public void onComplete(AsyncTask<V> task);

	}

	/**
	 * The recv.
	 */
	private final CompletionReceiver<V> recv;

	/**
	 * Instantiates a new async task.
	 * 
	 * @param task
	 *            the task
	 * @param recv
	 *            the completion receiver
	 */
	public AsyncTask(Callable<V> task, CompletionReceiver<V> recv) {
		super(task);
		this.recv = recv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.FutureTask#done()
	 */

	@Override
	public void done() {
		recv.onComplete(this);
	}

}
