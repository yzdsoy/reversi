/*
 *  LineStateTest.java
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

import java.util.Map;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Test Suite for {@code LineState} enum.
 */
public class LineStateTest {

    /** Class constructor. */
    public LineStateTest() { }


    /**
     * EARLY_GAME_C_12_MOVES
     */
    @Test
    public final void testGetDeltas() {

        final LineIndex li = LineIndex.valueOf(Line.C, 414);
        final LineIndexMove lim = LineIndexMove.valueOf(li, Square.C4);

        final int[] expected = {  0, // [ 0] --> R1
                                  0, // [ 1] --> R2
                                  0, // [ 2] --> R3
                                  0, // [ 3] --> R4
                                 -9, // [ 4] --> R5
                                  0, // [ 5] --> R6
                                  0, // [ 6] --> R7
                                  0, // [ 7] --> R8
                                  0, // [ 8] --> A
                                  0, // [ 9] --> B
                                -81, // [10] --> C
                                  0, // [11] --> D
                                  0, // [12] --> E
                                  0, // [13] --> F
                                  0, // [14] --> G
                                  0, // [15] --> H
                                  0, // [16] --> A6_C8
                                  0,
                                  0,
                                 -9,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                -81,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0,
                                  0};

        assertThat("",
                   lim.deltas(),
                   is(expected));
    }

    @Test
    public final void testLineIndexMoveClass_fileTransitions() {

        final LineIndex fi = LineIndex.valueOf(Line.C1_A3, 7);
        final LineIndexMove fim = LineIndexMove.valueOf(fi, Square.A3);

        final List<SquareTransition> expected = Arrays.asList(SquareTransition.NO_TRANSITION,
                                                              SquareTransition.WHITE_TO_BLACK,
                                                              SquareTransition.EMPTY_TO_BLACK);

        assertThat("Expected transition list is NO_TTRANSITION, WHITE_TO_BLACK, EMPTY_TO_BLACK.",
                   fim.transitions(),
                   is(expected));


    }

    @Test
    public final void testLineIndexClass() {

        final LineIndex li = LineIndex.valueOf(Line.A, 0);

        assertTrue(true);

    }

    @Test
    public final void testLineIndexMoveClass() {

        final LineIndexMove lim = LineIndexMove.valueOf(LineIndex.valueOf(Line.A, 0), Square.A1);

        assertTrue(true);

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_index_isInvalid_caseA() {
        LineState.valueOf(8, 6561);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_index_isInvalid_caseB() {
        LineState.valueOf(7, 6560);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_index_isInvalid_caseC() {
        LineState.valueOf(8, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_order_isInvalid_caseA() {
        LineState.valueOf(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_order_isInvalid_caseB() {
        LineState.valueOf(9, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public final void testValueOf_when_parameter_order_isInvalid_caseC() {
        LineState.valueOf(2, 0);
    }

    /**
     * Test the {@code valueOf(int, int} factory.
     *
     * @see LineState#valueOf(int, int)
     */
    @Test
    public final void testValueOf() {
        assertThat("LineState.valueOf(3, 10) has to be an instance of LineState class.",
                   LineState.valueOf(3, 10),
                   instanceOf(LineState.class));
        assertThat("LineState.valueOf(8, 6560) has to be an instance of LineState class.",
                   LineState.valueOf(8, 6560),
                   instanceOf(LineState.class));
        assertThat("LineState.valueOf(5, 0) has to be an instance of LineState class.",
                   LineState.valueOf(5, 0),
                   instanceOf(LineState.class));
    }

    @Test
    public final void testFlip() {
        assertThat("LineState.valueOf(3, 11).flip() must be LineState.valueOf(3, 19).",
                   LineState.valueOf(3, 11).flip(),
                   is(LineState.valueOf(3, 19)));
        assertThat("Flipping twice has to return the file state itself.",
                   LineState.valueOf(3, 11).flip().flip(),
                   is(LineState.valueOf(3, 11)));
    }

    @Test
    public final void testIndex() {
        assertThat("LineState.valueOf(3, 11).index() must be 11.",
                   LineState.valueOf(3, 11).index(),
                   is(11));
    }

    @Test
    public final void testLegalMoves() {
        final Map<Integer, Integer> result = LineState.valueOf(3, 15).legalMoves();
        assertThat("LineState.valueOf(3, 15).legalMoves() must have ONE entry <0, 13>.",
                   result.size(),
                   is(1));
        assertThat("LineState.valueOf(3, 15).legalMoves() must have one entry <0, 13>, having a 0 key.",
                   result.containsKey(0),
                   is(true));
        assertThat("LineState.valueOf(3, 15).legalMoves() must have one entry <0, 13>, having a 13 value.",
                   result.get(0),
                   is(13));
    }

    @Test
    public final void testOrder() {
        assertThat("LineState.valueOf(3, 11).order() must be 3.",
                   LineState.valueOf(3, 11).order(),
                   is(3));
    }

    @Test
    public final void testToString() {
        assertThat("LineState.valueOf(3, 10).toString() has to return the appropriate string.",
                   LineState.valueOf(3, 10).toString(),
                   is("[(order=3, index=10) [ @ . @ ]]"));
    }

    @Test
    public final void testPrintLegalMoves() {
        /**
         * Turn the statistic into tests ... 
         */
        /*
        for (int order = 3; order <= 8; order ++) {
            int moves = 0;
            int[] moveDistribution = new int[5];
            final int boundary = LineState.indexBoundary(order);
            for (int index = 0; index <= boundary; index++) {
                final LineState fs = LineState.valueOf(order, index);
                final int moveCount = fs.legalMoves().size();
                moves += moveCount;
                switch (moveCount) {
                case 0: moveDistribution[0] += 1; break;
                case 1: moveDistribution[1] += 1; break;
                case 2: moveDistribution[2] += 1; break;
                case 3: moveDistribution[3] += 1; break;
                case 4: moveDistribution[4] += 1; break;
                default: throw new RuntimeException("Too many moves ....");
                }
            }
            System.out.println("order=" + order + ", boundary=" + boundary + ", moves=" + moves
                               + ", D:["
                               + moveDistribution[0] + ", "
                               + moveDistribution[1] + ", "
                               + moveDistribution[2] + ", "
                               + moveDistribution[3] + ", "
                               + moveDistribution[4] + "]");
        }

        System.out.println();

        for (final File file : FileUtils.files()) {
            final List<Square> squares = file.squares();
            final int order = squares.size();
            System.out.println("file=" + file + ", order=" + order + ", squares=" + file.squares());
        }
        */

        assertTrue(true);
    }


}
