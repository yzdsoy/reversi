 /*
    Copyright (c) 2010 Roberto Corradini

    This file is part of the reversi program
    http://github.com/rcrr/reversi

    This program is free software; you can redistribute it and/or modify it
    under the terms of the GNU General Public License as published by the
    Free Software Foundation; either version 3, or (at your option) any
    later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
    or visit the site <http://www.gnu.org/licenses/>.
*/

package rcrr.reversi;

public enum SquareState {
    EMPTY("An empty square", "."),
	BLACK("A black piece", "@"),
	WHITE("A white piece", "0"),
	OUTER("Marks squares outside the 8x8 board", "?");

    private String description;
    private String name;

    SquareState(String description, String name) {
	this.description = description;
	this.name = name;
    }

    public String toString() { return name; }

    public static SquareState opponent(SquareState player) {
	switch (player) {
	case EMPTY: return null;
	case BLACK: return WHITE;
	case WHITE: return BLACK;
	case OUTER: return null;
	}
	return null;
    }
}