/*
 *  Actor.java
 *
 *  Copyright (c) 2010 Roberto Corradini. All rights reserved.
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

/**
 * An actor is an identity playing the game.
 * <p>
 * {@code Actor} is immutable.
 */
public final class Actor {

    /**
     * Base static factory for the class.
     * <p>
     * Parameter {@code name} must be not null.
     * Parameter {@code strategy} must be not null.
     *
     * @param name     the actor's name
     * @param strategy the actor's strategy
     * @return        a new actor
     * @throws NullPointerException when either name or strategy parameter is null
     */
    public static Actor valueOf(final String name, final Strategy strategy) {
        if (name == null) { throw new NullPointerException("Parameter name cannot be null."); }
        if (strategy == null) { throw new NullPointerException("Parameter strategy cannot be null."); }
        return new Actor(name, strategy);
    }

    /** The name field. */
    private final String name;

    /** The strategy field. */
    private final Strategy strategy;

    /**
     * Class constructor.
     * <p>
     * Parameter {@code name} must be not null.
     * Parameter {@code strategy} must be not null.
     *
     * @param name     the actor's name
     * @param strategy the actor's strategy
     */
    private Actor(final String name, final Strategy strategy) {
        assert (name != null) : "Parameter name cannot be null.";
        assert (strategy != null) : "Parameter strategy cannot be null.";
        this.name = name;
        this.strategy = strategy;
    }

    /**
     * Returns the name field.
     *
     * @return the actor's name
     */
    public String name() {
        return name;
    }

    /**
     * Returns the strategy field.
     *
     * @return the actor's strategy
     */
    public Strategy strategy() {
        return strategy;
    }

}
