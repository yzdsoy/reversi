/*
 *  Square.java
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.EnumMap;
import java.util.HashMap;

import static rcrr.reversi.Row.*;
import static rcrr.reversi.Column.*;

/**
 * Square is an Enum type that realize the base unit of the game board.
 * Squares are represented by two characters, a letter and a numeric digit.
 * <p>
 * For instance, let's take a square: D4.
 * This symbol identifies the square at the cross of column d and row 4.
 * <p>
 * Here is represented the collection of the 64 Square as them are
 * organized in the game board.
 * <pre>
 * {@code
 * .    a    b    c    d    e    f    g    h
 *   =========================================
 * 1 = A1 = B1 = C1 = D1 = E1 = F1 = G1 = H1 =
 *   =========================================
 * 2 = A2 = B2 = C2 = D2 = E2 = F2 = G2 = H2 =
 *   =========================================
 * 3 = A3 = B3 = C3 = D3 = E3 = F3 = G3 = H3 =
 *   =========================================
 * 4 = A4 = B4 = C4 = D4 = E4 = F4 = G4 = H4 =
 *   =========================================
 * 5 = A5 = B5 = C5 = D5 = E5 = F5 = G5 = H5 =
 *   =========================================
 * 6 = A6 = B6 = C6 = D6 = E6 = F6 = G6 = H6 =
 *   =========================================
 * 7 = A7 = B7 = C7 = D7 = E7 = F7 = G7 = H7 =
 *   =========================================
 * 8 = A8 = B8 = C8 = D8 = E8 = F8 = G8 = H8 =
 *   =========================================
 * }
 * </pre>
 * Has to be noticed that the sequence of the squares is organized by rows. It means that
 * the square ordered list is as follow:
 * <p>
 * {@code (A1, B1, C1, D1, E1, F1, G1, H1, A2, ... H8)}.
 */
public enum Square {

    /** Square a1. */
    A1(R1, A),

    /** Square b1. */
    B1(R1, B),

    /** Square c1. */
    C1(R1, C),

    /** Square d1. */
    D1(R1, D),

    /** Square e1. */
    E1(R1, E),

    /** Square f1. */
    F1(R1, F),

    /** Square g1. */
    G1(R1, G),

    /** Square h1. */
    H1(R1, H),

    /** Square a2. */
    A2(R2, A),

    /** Square b2. */
    B2(R2, B),

    /** Square c2. */
    C2(R2, C),

    /** Square d2. */
    D2(R2, D),

    /** Square e2. */
    E2(R2, E),

    /** Square f2. */
    F2(R2, F),

    /** Square g2. */
    G2(R2, G),

    /** Square h2. */
    H2(R2, H),

    /** Square a3. */
    A3(R3, A),

    /** Square b3. */
    B3(R3, B),

    /** Square c3. */
    C3(R3, C),

    /** Square d3. */
    D3(R3, D),

    /** Square e3. */
    E3(R3, E),

    /** Square f3. */
    F3(R3, F),

    /** Square g3. */
    G3(R3, G),

    /** Square h3. */
    H3(R3, H),

    /** Square a4. */
    A4(R4, A),

    /** Square b4. */
    B4(R4, B),

    /** Square c4. */
    C4(R4, C),

    /** Square d4. */
    D4(R4, D),

    /** Square e4. */
    E4(R4, E),

    /** Square f4. */
    F4(R4, F),

    /** Square g4. */
    G4(R4, G),

    /** Square h4. */
    H4(R4, H),

    /** Square a5. */
    A5(R5, A),

    /** Square b5. */
    B5(R5, B),

    /** Square c5. */
    C5(R5, C),

    /** Square d5. */
    D5(R5, D),

    /** Square e5. */
    E5(R5, E),

    /** Square f5. */
    F5(R5, F),

    /** Square g5. */
    G5(R5, G),

    /** Square h5. */
    H5(R5, H),

    /** Square a6. */
    A6(R6, A),

    /** Square b6. */
    B6(R6, B),

    /** Square c6. */
    C6(R6, C),

    /** Square d6. */
    D6(R6, D),

    /** Square e6. */
    E6(R6, E),

    /** Square f6. */
    F6(R6, F),

    /** Square g6. */
    G6(R6, G),

    /** Square h6. */
    H6(R6, H),

    /** Square a7. */
    A7(R7, A),

    /** Square b7. */
    B7(R7, B),

    /** Square c7. */
    C7(R7, C),

    /** Square d7. */
    D7(R7, D),

    /** Square e7. */
    E7(R7, E),

    /** Square f7. */
    F7(R7, F),

    /** Square g7. */
    G7(R7, G),

    /** Square h7. */
    H7(R7, H),

    /** Square a8. */
    A8(R8, A),

    /** Square b8. */
    B8(R8, B),

    /** Square c8. */
    C8(R8, C),

    /** Square d8. */
    D8(R8, D),

    /** Square e8. */
    E8(R8, E),

    /** Square f8. */
    F8(R8, F),

    /** Square g8. */
    G8(R8, G),

    /** Square h8. */
    H8(R8, H);

    /** The null square. */
    static final Square NULL = null;

    /** A generic square instance. */
    static final Square AN_INSTANCE = B3;

    /** The list of the four corners. */
    private static final List<Square> CORNERS = Collections.unmodifiableList(Arrays.asList(A1, H1, H8, A8));

    /** The inverse labels map. It is computed and initialized by the static block. */
    private static final Map<String, Square> INVERSE_LABELS;

    /** The labels map. It is computed and initialized by the static block. */
    private static final Map<Square, String> LABELS;

    /** The neighbor table. */
    private static final Map<Square, Map<Direction, Square>> NEIGHBOR_TABLE = neighborTable();

    /**
     * Static initialization block:
     * . - sets and initializes {@code LABELS} map
     * . - sets and initializes {@code INVERSE_LABELS} map
     */
    static {
        final Map<Square, String> labelMap = new HashMap<Square, String>();
        final Map<String, Square> inverseLabelMap = new HashMap<String, Square>();
        for (Square sq : values()) {
            String label = sq.column.label() + sq.row.label();
            labelMap.put(sq, label);
            inverseLabelMap.put(label, sq);
        }
        LABELS = Collections.unmodifiableMap(labelMap);
        INVERSE_LABELS = Collections.unmodifiableMap(inverseLabelMap);
    }

    /**
     * Returns the list of the four corner squares.
     *
     * @return the four corners
     */
    public static List<Square> corners() { return CORNERS; }

    /**
     * Returns the square matching the specified label. Throws
     * an exception in case the label is not associated with a square.
     *
     * @param label the square's label
     * @return      the identified square
     * @throws IllegalArgumentException if the label is not valid
     */
    public static Square getInstance(final String label) {
        final Square sq = INVERSE_LABELS.get(label);
        if (sq == null) {
            throw new IllegalArgumentException("The specified label: <"
                                               + label + ">, does not match any square's label.");
        } else {
            return sq;
        }
    }

    /**
     * Returns the square instance matching the row and column parameters. Returns
     * null is either the row or the column parameters are null.
     *
     * @param row    square's row
     * @param column square's column
     * @return the square pointed by row and column, or null in case row or column are themself null
     **/
    public static Square getInstance(final Row row, final Column column) {
        if (row == null || column == null) {
            return null;
        } else {
            return Square.values()[Row.values().length * row.ordinal() + column.ordinal()];
        }
    }

    /**
     * Returns the square at the specified position.
     *
     * @param index the square index
     * @return      the identified square
     * @throws IndexOutOfBoundsException if the index is
     *                                   out of range {@code (index < 0 || index >= Square.values().length)}
     */
    public static Square getInstance(final int index) { return Square.values()[index]; }

    /**
     * Returns the square at the specified position.
     *
     * @param rowIndex    the row index
     * @param columnIndex the colum index
     * @return            the identified square
     * @throws IndexOutOfBoundsException if either columnIndex or
     *             rowIndex is out of range:
     *             {@code (columnIndex < 0 || columnIndex >= Column.values().length)}
     *             {@code (rowIndex < 0 || rowIndex >= Row.values().length)}
     */
    public static Square getInstance(final int rowIndex, final int columnIndex) {
        final Row row = Row.values()[rowIndex];
        final Column column = Column.values()[columnIndex];
        return Square.getInstance(row, column);
    }

    /**
     * Computes the neighborTable.
     *
     * @return the neighbor table
     */
    private static Map<Square, Map<Direction, Square>> neighborTable() {
        Map<Square, Map<Direction, Square>> nt = new EnumMap<Square, Map<Direction, Square>>(Square.class);
        for (Square sq : values()) {
            Map<Direction, Square> snt = new EnumMap<Direction, Square>(Direction.class);
            for (Direction dir : Direction.values()) {
                Square n = getInstance(sq.row().shift(dir.deltaRow()), sq.column().shift(dir.deltaColumn()));
                snt.put(dir, n);
            }
            nt.put(sq, Collections.unmodifiableMap(snt));
        }
        return Collections.unmodifiableMap(nt);
    }

    /** The row field. */
    private final Row row;

    /** The column field. */
    private final Column column;

    /**
     * Enum constructor.
     *
     * @param row    square's row
     * @param column square's column
     */
    private Square(final Row row, final Column column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the square's column.
     *
     * @return the square's column
     **/
    public Column column() { return column; }

    /**
     * Returns the Hasegawa's naming for the edge squares. Returns
     * null if the square is not one of the identified squares.
     *
     * @return the Hasegawa square name, or null if the value is not defined
     **/
    public char getHasegawaLabel() {
        switch (this) {
        case B1:
        case G1:
        case H2:
        case H7:
        case G8:
        case B8:
        case A7:
        case A2:
            return 'C';
        case C1:
        case F1:
        case H3:
        case H6:
        case C8:
        case F8:
        case A6:
        case A3:
            return 'A';
        case D1:
        case E1:
        case H4:
        case H5:
        case E8:
        case D8:
        case A5:
        case A4:
            return 'B';
        case B2:
        case G2:
        case G7:
        case B7:
            return 'X';
        default:
            return ' ';
        }
    }

    /**
     * Returns true if the square is a corner, otherwise false.
     *
     * @return true or false if the square is either a corner or not
     */
    public boolean isCorner() {
        return CORNERS.contains(this);
    }

    /**
     * Returns the square's label.
     *
     * @return the square's label
     **/
    public String label() {
        return LABELS.get(this);
    }

    /**
     * Returns a Map that has the direction has key and the associated neighbor square as value.
     *
     * @return the square's neighbor map
     **/
    public Map<Direction, Square> neighbors() {
        return NEIGHBOR_TABLE.get(this);
    }

    /**
     * Returns the square's row.
     *
     * @return the square's row
     **/
    public Row row() { return row; }

}
