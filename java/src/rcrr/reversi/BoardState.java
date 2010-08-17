/*
 *  BoardState.java
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

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.EnumMap;

import java.io.PrintStream;

// To do:
// - change the List into an Map<Square, SquareState>
// - remove MutableBoard
// - Remove the Abstract Class and the Interface. Move all the code into a Board Class.
// - remove the copyBoard() method (BoardState is now immutable ...)
// - javadoc ....
// - complete an exhaustive test suite
// - polish, polish, polish ....

public final class BoardState extends AbstractBoard {

    private final List<SquareState> squares;

    List<SquareState> squares() { throw new UnsupportedOperationException(); }

    /** Test ok */
    public SquareState get(Integer index) {
	return squares.get(index);
    }

    private BoardState(List<SquareState> ssl) {
	this.squares = Collections.unmodifiableList(new ArrayList<SquareState>(ssl));
    }

    // to be removed ....
    private BoardState(MutableBoard mb) {
	this.squares = Collections.unmodifiableList(new ArrayList<SquareState>(mb.squares()));
    }

    /** Test ok */
    public static BoardState valueOf(List<SquareState> ssl) {
	return new BoardState(ssl);
    }

    public static BoardState emptyBoard() {
	List<SquareState> ssl = new ArrayList<SquareState>();
	for (int i=0; i<100; i++) {
	    if (Square.ALL_SQUARES.contains(i)) {
		ssl.add(SquareState.EMPTY);
	    } else {
		ssl.add(SquareState.OUTER);
	    }
	}
	return new BoardState(ssl);
    }

    public static BoardState initialBoard() {
	List<SquareState> ssl = new ArrayList<SquareState>();
	for (int i=0; i<100; i++) {
	    if (Square.ALL_SQUARES.contains(i)) {
		ssl.add(SquareState.EMPTY);
	    } else {
		ssl.add(SquareState.OUTER);
	    }
	}
	ssl.set(44, SquareState.WHITE);
	ssl.set(45, SquareState.BLACK);
	ssl.set(54, SquareState.BLACK);
	ssl.set(55, SquareState.WHITE);
	return new BoardState(ssl);
    }
    
    public BoardState copyBoard() {
	return new BoardState(MutableBoard.copyBoard(this));
    }

    /**
     * Returns a new updated board to reflect move by player.
     *
     * @param  move   an integer that points to the board square where to put the disk
     * @param  player the disk color to put on the board
     */
    public BoardState makeMove(Integer move, Player player) {
	MutableBoard mb = MutableBoard.copyBoard(this);
	mb.makeMove(move, player);
	return new BoardState(mb);
    }

}