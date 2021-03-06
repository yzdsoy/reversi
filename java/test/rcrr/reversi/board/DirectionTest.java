/*
 *  DirectionTest.java
 *
 *  Copyright (c) 2010, 2011, 2012 Roberto Corradini. All rights reserved.
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

package rcrr.reversi.board;

import org.junit.Test;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

/**
 * Test Suite for {@code Direction} enum.
 */
public class DirectionTest {

    /** Class constructor. */
    public DirectionTest() { }

    /**
     * Tests the {@code versus()} method.
     */
    @Test
    public final void testVersus() {
        assertThat("Direction.N.versus() must return Versus.NEGATIVE.",
                   Direction.N.versus(),
                   is(Versus.NEGATIVE));
    }

    /**
     * Tests the {@code axis()} method.
     */
    @Test
    public final void testAxis() {
        assertThat("Direction.N.deltaRow() must return Axis.VE.",
                   Direction.N.axis(),
                   is(Axis.VE));
    }

}
