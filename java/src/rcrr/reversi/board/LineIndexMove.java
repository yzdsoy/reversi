/*
 *  LineIndexMove.java
 *
 *  Copyright (c) 2012 Roberto Corradini. All rights reserved.
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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

class LineIndexMove {

    private static final Map<LineIndex, Map<Integer, LineIndexMove>> LINE_INDEX_MOVE_MAP;

    public static LineIndexMove valueOf(final LineIndex lineIndex, final int move) {
        return LINE_INDEX_MOVE_MAP.get(lineIndex).get(move);
    }

    static {

        /**
         * Computes LINE_INDEX_MOVE_MAP map.
         */
        final Map<LineIndex, Map<Integer, LineIndexMove>> transientLineIndexMoveMap = new HashMap<LineIndex, Map<Integer, LineIndexMove>>();
        for (final Map.Entry<File, List<LineIndex>> entry : LineIndex.fileIndexMap().entrySet()) {
            for (final LineIndex lineIndex : entry.getValue()) {
                final Map<Integer, LineIndexMove> transientInnerMap = new HashMap<Integer, LineIndexMove>();
                for (final Map.Entry<Integer, LineIndex> move : lineIndex.legalMoves().entrySet()) {
                    transientInnerMap.put(move.getKey(), new LineIndexMove(lineIndex, move.getKey()));
                }
                transientLineIndexMoveMap.put(lineIndex, Collections.unmodifiableMap(transientInnerMap));
            }
        }
        LINE_INDEX_MOVE_MAP = Collections.unmodifiableMap(transientLineIndexMoveMap);

    }

    private final LineIndex lineIndex;
    private final int move;
    LineIndexMove(final LineIndex lineIndex, final int move) {
        this.lineIndex = lineIndex;
        this.move = move;
    }

    /**
     * Transitions are:
     * Empty to Black and White to Black .... all the other are not possible because we are evaluating only moves played by the black.
     */
    public List<SquareTransition> transitions() {
        final List<SquareTransition> transitions = new ArrayList<SquareTransition>();
        final List<SquareState> from = lineIndex.configuration();
        final List<SquareState> to = lineIndex.legalMoves().get(move).configuration();
        for (int i = 0; i < from.size(); i++) {
            SquareTransition st;
            final SquareState fss = from.get(i);
            final SquareState tss = to.get(i);
            if (fss == tss) {
                st = SquareTransition.NO_TRANSITION;
            } else if (fss == SquareState.EMPTY && tss == SquareState.BLACK) {
                st = SquareTransition.EMPTY_TO_BLACK;
            } else if (fss == SquareState.WHITE && tss == SquareState.BLACK) {
                st = SquareTransition.WHITE_TO_BLACK;
            } else {
                throw new RuntimeException("Square transition not allowed. from=" + fss + ", to=" + tss);
            }
            transitions.add(st);
        }
        return Collections.unmodifiableList(transitions);
    }

    public int[] getDeltas() {
        final int[] deltas = new int[Line.NUMBER_OF];
        int squareOrdinal = 0;
        for (final SquareTransition st : transitions()) {
            final Square sq = lineIndex.line().squares().get(squareOrdinal);
            for (final Line affectedLine : Line.linesForSquare(sq)) {
                //final File affectedFile = affectedLine.file();
                //if (affectedFile != null) {
                    int delta = st.delta() * LineState.fileTransferMatrix(lineIndex.file(), squareOrdinal, affectedLine.file());
                    //deltas[FileUtils.files().indexOf(affectedFile)] += delta;
                    deltas[affectedLine.ordinal()] += delta;
                    //}
            }
            squareOrdinal++;
        }
        return deltas;
    }

    /**
     * Returns a {@code String} representing the {@code LineIndexMove} object.
     *
     * @return a {@code String} representing the line index move
     */
    @Override public String toString() {
        return String.format("[move=%d, lineIndex=%s]", move, lineIndex);
    }

}
