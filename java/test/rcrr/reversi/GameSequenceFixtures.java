/*
 *  GameSequenceFixtures.java
 *
 *  Copyright (c) 2011 Roberto Corradini. All rights reserved.
 *
 *  This file is part of the reversi program
 *  http://github.com/rcrr/reversi
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation; either version 3, or (at your option) any
 *  later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 *  or visit the site <http://www.gnu.org/licenses/>.
 */

package rcrr.reversi;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class host a number of predefined game sequences.
 * <p>
 * The {@code GameSequence} class defines immutable objects thus {@code GameSequenceFixtures}
 * implements game sequence instances as public static shared objects. Tests can
 * freely share the instances without any modification issue.
 */
public final class GameSequenceFixtures {

    /**
     * The null instance.
     */
    public static final GameSequence NULL = null;

    /**
     * A generic instance.
     */
    public static final GameSequence AN_INSTANCE = anInstance();

    /**
     * An instance having three game snapshot.
     * <p>
     * The instance is described as follow:
     * <ul>
     *  <li><i>1st game snapshot</i> {@code GameSnapshotFixtures.G00_S00}</li>
     *  <li><i>2nd game snapshot</i> {@code GameSnapshotFixtures.G00_S01}</li>
     *  <li><i>3rd game snapshot</i> {@code GameSnapshotFixtures.G00_S02}</li>
     * </ul>
     */
    public static final GameSequence THREE_SNAPSHOTS
        = GameSequence.valueOf(Arrays.asList(GameSnapshotFixtures.G00_S00,
                                             GameSnapshotFixtures.G00_S01,
                                             GameSnapshotFixtures.G00_S02));

    /** Class constructor. */
    private GameSequenceFixtures() { }

    /** Returns a game sequence generic instance. */
    private static final GameSequence anInstance() {
        return GameSequence.valueOf(Arrays.asList(GameSnapshotFixtures.AN_INSTANCE));
    }

}