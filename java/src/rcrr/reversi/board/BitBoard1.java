/*
 *  BitBoard1.java
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
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MUST  02111-1307, USA
 *  or visit the site <http://www.gnu.org/licenses/>.
 */

package rcrr.reversi.board;

import java.math.BigInteger;

import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * A board concrete implementation in the bitboard family.
 * <p>
 * A {@code BitBoard1} object holds the information of the state of each board's square.
 * The board state is kept into a long array having a length equal two.
 * The first entry keeps the black squares, the second the whites.
 * <p>
 * The board uses wouldFlip ....
 * <p>
 * {@code BitBoard1} is immutable.
 * <p>
 * @see Square
 */
public final class BitBoard1 extends BitBoard {

    private static final boolean LOG = true;
    private static int callsTolegalMoves = 0;
    private static int callsToMakeMove = 0;
    private static int callsToConstructor = 0;

    private static final int BLACK = 0;
    private static final int WHITE = 1;

    /** Should be moved completely to Direction enum? I think so. */
    private static final int DIR_NW = -9;
    private static final int DIR_NN = -8;
    private static final int DIR_NE = -7;
    private static final int DIR_WW = -1;
    private static final int DIR_EE = +1;
    private static final int DIR_SW = +7;
    private static final int DIR_SS = +8;
    private static final int DIR_SE = +9;

    private static final int[] DIRECTION_SHIFTS = Direction.shifts(); 

    private static final Square[] SQUARE_VALUES = Square.values();

    private static final long CORE_SQUARES                = 0x007E7E7E7E7E7E00L;
    private static final long EDGES_SQUARES               = 0xFF818181818181FFL;
    private static final long ALL_SQUARES_EXCEPT_COLUMN_A = 0x7F7F7F7F7F7F7F7FL;
    private static final long ALL_SQUARES_EXCEPT_COLUMN_H = 0xFEFEFEFEFEFEFEFEL;

    /** Used for masking a byte when using integer values. */
    private static final int BYTE_MASK_FOR_INT = 0xFF;

    /**
     * This array has sixtyfour entries. The index, having range 0-63, represent one of the squares
     * of the table. Each entry is a bitboard mask having set all the squares that are
     * reachable moving along the eigth directions, when starting from the square identified by
     * the index itself.
     * <p>
     * Values do not change.
     */
    private static final long[] BITBOARD_MASK_FOR_ALL_DIRECTIONS = new long[] {
        0x81412111090503FEL, 0x02824222120A07FDL, 0x0404844424150EFBL, 0x08080888492A1CF7L, 
        0x10101011925438EFL, 0x2020212224A870DFL, 0x404142444850E0BFL, 0x8182848890A0C07FL, 
        0x412111090503FE03L, 0x824222120A07FD07L, 0x04844424150EFB0EL, 0x080888492A1CF71CL, 
        0x101011925438EF38L, 0x20212224A870DF70L, 0x4142444850E0BFE0L, 0x82848890A0C07FC0L, 
        0x2111090503FE0305L, 0x4222120A07FD070AL, 0x844424150EFB0E15L, 0x0888492A1CF71C2AL, 
        0x1011925438EF3854L, 0x212224A870DF70A8L, 0x42444850E0BFE050L, 0x848890A0C07FC0A0L, 
        0x11090503FE030509L, 0x22120A07FD070A12L, 0x4424150EFB0E1524L, 0x88492A1CF71C2A49L, 
        0x11925438EF385492L, 0x2224A870DF70A824L, 0x444850E0BFE05048L, 0x8890A0C07FC0A090L, 
        0x090503FE03050911L, 0x120A07FD070A1222L, 0x24150EFB0E152444L, 0x492A1CF71C2A4988L, 
        0x925438EF38549211L, 0x24A870DF70A82422L, 0x4850E0BFE0504844L, 0x90A0C07FC0A09088L, 
        0x0503FE0305091121L, 0x0A07FD070A122242L, 0x150EFB0E15244484L, 0x2A1CF71C2A498808L, 
        0x5438EF3854921110L, 0xA870DF70A8242221L, 0x50E0BFE050484442L, 0xA0C07FC0A0908884L, 
        0x03FE030509112141L, 0x07FD070A12224282L, 0x0EFB0E1524448404L, 0x1CF71C2A49880808L, 
        0x38EF385492111010L, 0x70DF70A824222120L, 0xE0BFE05048444241L, 0xC07FC0A090888482L, 
        0xFE03050911214181L, 0xFD070A1222428202L, 0xFB0E152444840404L, 0xF71C2A4988080808L, 
        0xEF38549211101010L, 0xDF70A82422212020L, 0xBFE0504844424140L, 0x7FC0A09088848281L
    };
    /**
     * This array is an implementation of the precomputed table that contains the effects of moving
     * a piece in any of the eigth squares in a row.
     * The size is so computed:
     *  - there are 256 arrangments of player discs,
     *  - and 256 arrangements of opponent pieces,
     *  - the potential moves are 8.
     * So the array size is 256 * 256 * 8 = 524,288 Bytes = 512kB.
     * Not all the entries are legal! The first set of eigth bits and the second one (opponent row)
     * must not set the same position. 
     * 
     * The index of the array is computed by this formula:
     * index = playerRow | (opponentRow << 8) | (movePosition << 16);
     */
    private static final byte[] BITROW_CHANGES_FOR_PLAYER_ARRAY = initializeBitrowChangesForPlayerArray();

    public static String printLog() {
        String ret = "callsTolegalMoves=" + callsTolegalMoves + ", callsToMakeMove=" + callsToMakeMove + ", callsToConstructor=" + callsToConstructor;
        return ret;
    }

    /**
     * Base static factory for the class.
     * <p>
     * {@code squares} must be not null, and must have an entry for every board square.
     * Given that the map cannot have duplicate keys, its size must be equal to the number
     * of class instances defined by the {@code Square} enum.
     *
     * @param  squares the map of squares
     * @return         a new board having as state the given square map
     * @throws NullPointerException     if parameter {@code squares} is null
     * @throws IllegalArgumentException if the {@code squares} is not complete
     */
    static Board valueOf(final Map<Square, SquareState> squares) {
        BoardUtils.checkForConsistencyTheSquareMap(squares);
        return new BitBoard1(BoardUtils.mapToBitboard(squares));
    }

    static Board valueOf(final long[] bitboard) {
        return new BitBoard1(bitboard);
    }

    /**
     * Class constructor.
     * <p>
     * {@code bitboard} must be not null, and must have a size equal to
     * two. Overlapping bit set to one are not allowed.
     *
     * @param  bitboard the bitboard field
     */
    private BitBoard1(final long[] bitboard) {
        super(bitboard);
        if (LOG) callsToConstructor++;
    }

    /**
     * Returns the boolean value telling if the move, done by the
     * specified player, is legal.
     * <p>
     * Parameter {@code move} must be not {@code null}.
     * Parameter {@code player} must be not {@code null}.
     *
     * A few more optimizations are possible:
     * Precompute the shiftDistance value, remake signedhift using a shift.
     * Eliminate the check for diagonals shorter than 3 pieces.
     * Verify that the square is empty. !!!! Eureka! -15%
     *
     *
     * @param move   the square where to put the new disk
     * @param player the player moving
     * @return       true if the move is legal, otherwise false
     * @throws NullPointerException if parameter {@code move} or {@code player} is null
     */
    public boolean isLegal(final Square move, final Player player) {
        if (move == null) {
            throw new NullPointerException("Parameter move must be not null.");
        }
        if (player == null) {
            throw new NullPointerException("Parameter player must be not null.");
        }

        final long bitmove = 1L << move.ordinal();
        if ((bitmove & (bitboard[0] | bitboard[1])) != 0) {
            return false;
        }

        final int intPlayer = player.ordinal(); 
        final int column = move.column().ordinal();
        final int row = move.row().ordinal();

        final long playerBitboard;
        final long opponentBitboard;

        if (intPlayer == WHITE) {
            playerBitboard = bitboard[1];
            opponentBitboard = bitboard[0];
        } else {
            playerBitboard = bitboard[0];
            opponentBitboard = bitboard[1];
        }

        int playerBitrow;
        int opponentBitrow;
        int shiftDistance;

        /** Check for flipping on row. */
        playerBitrow = (int)(playerBitboard >>> (8 * row)) & 0xFF;
        opponentBitrow = (int)(opponentBitboard >>> (8 * row)) & 0xFF;
        if (bitrowChangesForPlayer(playerBitrow, opponentBitrow, column) != playerBitrow) {
            return true;
        }

        /** Check for flipping on column. */
        playerBitrow = trasformColumnAInRow0(playerBitboard >>> column);
        opponentBitrow = trasformColumnAInRow0(opponentBitboard >>> column);
        if (bitrowChangesForPlayer(playerBitrow, opponentBitrow, row) != playerBitrow) {
            return true;
        }

        /** Check for flipping on diagonal having direction A1-H8. */
        shiftDistance = (column - row) << 3;
        playerBitrow = trasformDiagonalA1H8InRow0(BitWorks.signedLeftShift(playerBitboard, shiftDistance));
        opponentBitrow = trasformDiagonalA1H8InRow0(BitWorks.signedLeftShift(opponentBitboard, shiftDistance));
        if (bitrowChangesForPlayer(playerBitrow, opponentBitrow, column) != playerBitrow) {
            return true;
        }

        /** Check for flipping on diagonal having direction H1-A8. */
        shiftDistance = (7 - column - row) << 3;
        playerBitrow = trasformDiagonalH1A8InRow0(BitWorks.signedLeftShift(playerBitboard, shiftDistance));
        opponentBitrow = trasformDiagonalH1A8InRow0(BitWorks.signedLeftShift(opponentBitboard, shiftDistance));
        if (bitrowChangesForPlayer(playerBitrow, opponentBitrow, column) != playerBitrow) {
            return true;
        }

        /** If no capture on the four directions happens, return false. */
        return false;
    }

    private static int trasformColumnAInRow0(long x) {
        x &= 0x0101010101010101L;
        x |= x >> 28;
        x |= x >> 14;
        x |= x >> 7;
        return (int)x & 0xFF;
    }

    private static int trasformDiagonalA1H8InRow0(long x) {
        x &= 0x8040201008040201L;
        x |= x >> 32;
        x |= x >> 16;
        x |= x >> 8;
        return (int)x & 0xFF;
    }

    private static int trasformDiagonalH1A8InRow0(long x) {
        x &= 0x0102040810204080L;
        x |= x >> 32;
        x |= x >> 16;
        x |= x >> 8;
        return (int)x & 0xFF;
    }

    private static long reTrasformRow0BackToColumnA(int x) {
        x |= x << 7;
        x |= x << 14;
        long z = (long)x | ((long)x << 28);
        return z & 0x0101010101010101L;
    }

    private static long reTrasformRow0BackToDiagonalA1H8(int x) {
        x |= x << 8;
        long z = (long)x | ((long)x << 16);
        z |= z << 32;
        return z & 0x8040201008040201L;
    }

    private static long reTrasformRow0BackToDiagonalH1A8(int x) {
        x |= x << 8;
        x |= (x & 0x1122) << 16;
        long z = (long)x | ((long)x << 32);
        return z & 0x0102040810204080L;
    }

    /**
     * Returns an 8-bit row representation of the player pieces after applying the move.
     * 
     * @param playerRow    8-bit bitboard corrosponding to player pieces
     * @param opponentRow  8-bit bitboard corrosponding to opponent pieces
     * @param movePosition square to move
     * @return             the new player's row index after making the move
     */
    private static int bitrowChangesForPlayer(final int playerRow, final int opponentRow, final int movePosition) {
        final int arrayIndex = playerRow | (opponentRow << 8) | (movePosition << 16);
        return (int)BITROW_CHANGES_FOR_PLAYER_ARRAY[arrayIndex] & BYTE_MASK_FOR_INT;
    }

    /** Used to initialize the BITROW_CHANGES_FOR_PLAYER_ARRAY. */
    private static byte[] initializeBitrowChangesForPlayerArray() {

        final byte[] arrayResult = new byte[256 * 256 * 8];
        for (int playerRow = 0; playerRow < 256; playerRow++) {
            for (int opponentRow = 0; opponentRow < 256; opponentRow++) {
                final int filledInRow = playerRow | opponentRow;
                final int emptiesInRow = ~(filledInRow) & BYTE_MASK_FOR_INT;
                for (int movePosition = 0; movePosition < 8; movePosition++) {
                    final int move = 1 << movePosition;
                    final int arrayResultIndex = playerRow | (opponentRow << 8) | (movePosition << 16);

                    int playerRowAfterMove;

                    /**
                     * It checks two conditions that cannot happen because are illegal.
                     * First player and opponent cannot have overlapping discs.
                     * Second the move cannot overlap existing discs.
                     * When either one of the two condition applys the result is set being equal to the player row index.
                     * Otherwise when black and white do not overlap, and the move is on an empy square it procede with the else block.
                     **/
                    if (((playerRow & opponentRow) != 0) || ((move & filledInRow) != 0)) {
                        playerRowAfterMove = playerRow;
                    } else {

                        /** The square of the move is added to the player configuration of the row after the move. */
                        playerRowAfterMove = playerRow | move;

                        /**
                         * The potential bracketing disc on the right is the first player disc found moving
                         * on the left starting from the square of the move.
                         */
                        final int potentialBracketingDiscOnTheLeft = BitWorks.highestBitSet(playerRow & (move - 1));

                        /**
                         * The left rank is the sequence of adiacent discs that start from the bracketing disc and end
                         * with the move disc. */
                        final int leftRank = BitWorks.fillInBetween(potentialBracketingDiscOnTheLeft | move);

                        /**
                         * If the rank contains empy squares, this is a fake flip, and it doesn't do anything.
                         * If the rank is full, it cannot be full of anything different than opponent discs, so
                         * it adds the discs to the after move player configuration.
                         */
                        if ((leftRank & emptiesInRow) == 0) {
                            playerRowAfterMove |= leftRank;
                        }

                        /** Here it does the same computed on the left also on the right. */
                        final int potentialBracketingDiscOnTheRight = BitWorks.lowestBitSet(playerRow & ~(move - 1));
                        final int rightRank = BitWorks.fillInBetween(potentialBracketingDiscOnTheRight | move);
                        if ((rightRank & emptiesInRow) == 0) {
                            playerRowAfterMove |= rightRank;
                        }

                        /**
                         * It checks that the after move configuration is different from the starting one for the player.
                         * This case can happen because it never checked that the bracketing piece was not adjacent to the move disc,
                         * on such a case, on both side, the move is illegal, and it is recorded setting
                         * the result configuation appropriately.
                         */
                        if (playerRowAfterMove == (playerRow | move)) {
                            playerRowAfterMove = playerRow;
                        }
                    }

                    /** Asignes the computed player row index to the proper array position. */
                    arrayResult[arrayResultIndex] = (byte) playerRowAfterMove;

                }
            }
        }

        return arrayResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Square> legalMoves(final Player player) {

        if (LOG) callsTolegalMoves++;

        //final List<Square> legalMoves = super.legalMoves(player); 

        if (player == null) { throw new NullPointerException("Parameter player must be not null."); }
        final List<Square> legalMoves = new ArrayList<Square>(); 
        // The loop modifies likelyMoves removing the less significative bit set on each iteration.
        for (long likelyMoves = likelyMoves(player); likelyMoves != 0; likelyMoves &= likelyMoves - 1) {
            final int iSquare = BitWorks.bitscanMS1B(BitWorks.lowestBitSet(likelyMoves));
            final Square square = SQUARE_VALUES[iSquare];
            if (isLegal(square, player)) {
                legalMoves.add(square);
            }
        }
        
        /*
        final long lm = legalMoves(player.ordinal());

        final List<Square> lmSquares = new ArrayList<Square>();

        long lmEroding = lm;
        while (lmEroding != 0L) { 
            final int movePosition = BitWorks.bitscanLS1B(lmEroding);
            final Square move = Square.values()[movePosition];
            lmSquares.add(move);
            lmEroding &= ~(1L << movePosition);
        }
        */

        /*
        if (!legalMoves.equals(lmSquares)) {
            System.out.println("legalMoves=" + legalMoves + ", lmSquares=" + lmSquares);
        }
        */

        return legalMoves;
    }

    private long likelyMoves(final Player player) {
        final int intPlayer = player.ordinal(); 
        final int intOpponent = intPlayer ^ WHITE;
        final long empties = ~(bitboard[BLACK] | bitboard[WHITE]);
        // Case 1 is simple. Case 2 removes the likely moves that are on the "second crown" and are neighbor of only edge squares.
        return neighbors(bitboard[intOpponent]) & empties;
        //return ((neighbors(bitboard[intOpponent]) & EDGES_SQUARES) | neighbors(bitboard[intOpponent] & CORE_SQUARES) & empties);
    }

    private static long neighbors(final long squares) {
        long neighbors = squares;
        neighbors |= (neighbors >>> 8);
        neighbors |= (neighbors >>> 1) & ALL_SQUARES_EXCEPT_COLUMN_A;
        neighbors |= (neighbors <<  1) & ALL_SQUARES_EXCEPT_COLUMN_H;
        neighbors |= (neighbors <<  8);
        return neighbors;
    }

    private long legalMoves(final int player) {
        final int opponent = player ^ WHITE;
        final long empties = ~(bitboard[BLACK] | bitboard[WHITE]);

        long lm = 0L;
        for (final int dir : DIRECTION_SHIFTS) {
            long wave = neighbor(empties, dir) & bitboard[opponent];
            for (int shift = 2; shift < 8; shift++) {
                wave = neighbor(wave, dir);
                lm |= neighbor((wave & bitboard[player]), -dir, shift);
                wave &= bitboard[opponent];
            }
        }

        return lm;
    }

    private static long neighbor(final long square, final int dir, final int amount) {
        long result = square;
        for (int i = 0; i < amount; i++) {
            result = neighbor(result, dir);
        }
        return result;
    }

    private static long neighbor(final long square, final int dir) {
        switch (dir) {
        case DIR_NW: return (square >>> 9) & ALL_SQUARES_EXCEPT_COLUMN_A;
        case DIR_NN: return (square >>> 8);
        case DIR_NE: return (square >>> 7) & ALL_SQUARES_EXCEPT_COLUMN_H;
        case DIR_WW: return (square >>> 1) & ALL_SQUARES_EXCEPT_COLUMN_A;
        case DIR_EE: return (square <<  1) & ALL_SQUARES_EXCEPT_COLUMN_H;
        case DIR_SW: return (square <<  7) & ALL_SQUARES_EXCEPT_COLUMN_A;
        case DIR_SS: return (square <<  8);
        case DIR_SE: return (square <<  9) & ALL_SQUARES_EXCEPT_COLUMN_H;
        default: throw new IllegalArgumentException("Undefined value for dir parameter. dir=" + dir);
        }
    }

    /**
     * Returns a new updated board to reflect move by player. This static
     * factory executes a game move to the board and returns a new one,
     * reflecting the move. The original board is not modified.
     * <p>
     * A null value for player is not allowed, a {@code NullPointerException}
     * is thrown in such a case.
     * <p>
     * A null value for move is allowed, and moreover is the only valid value
     * acceptable by the method, when the player has not any legal move.
     * Otherwise a null move is forbidden, and a {@code NullPointerException}
     * is risen.
     * <p>
     * The method does check if the move is legal. It throws an
     * {@code IllegalArgumentException} in case it is not.
     *
     * @param  move   the board square where to put the disk
     * @param  player the disk color to put on the board
     * @return        a new {@code Board} reflecting the move made
     * @throws NullPointerException     if parameter {@code move}
     *                                  or {@code player} is null
     * @throws IllegalArgumentException if the {@code move}
     *                                  by {@code player} is illegal
     */
    public Board makeMove(final Square move, final Player player) {

        if (LOG) callsToMakeMove++;

        if (player == null) {
            throw new NullPointerException("Parameter player must be not null.");
        }
        if (move == null) {
            if (hasAnyLegalMove(player)) {
                throw new NullPointerException("Parameter move must be not null when a legal one is available.");
            } else {
                return this;
            }
        }
        if (!isLegal(move, player)) {
            throw new IllegalArgumentException("The move<"
                                               + move + "> by player<"
                                               + player + "> is illegal.");
        }

        final int intMove = move.ordinal();
        final int intPlayer = player.ordinal(); 
        final int column = move.column().ordinal();
        final int row = move.row().ordinal();

        final long playerBitboard;
        final long opponentBitboard;

        if (intPlayer == WHITE) {
            playerBitboard = bitboard[1];
            opponentBitboard = bitboard[0];
        } else {
            playerBitboard = bitboard[0];
            opponentBitboard = bitboard[1];
        }

        long finalPBoard;
        long finalOBoard;

        int playerBitrow;
        int opponentBitrow;
        int shiftDistance;

        final long unmodifiedMask = ~BITBOARD_MASK_FOR_ALL_DIRECTIONS[intMove];		
        finalPBoard = playerBitboard & unmodifiedMask;
        finalOBoard = opponentBitboard & unmodifiedMask;

        /** Compute row changes. */
        playerBitrow = (int)(playerBitboard >>> (8 * row)) & 0xFF;
        opponentBitrow = (int)(opponentBitboard >>> (8 * row)) & 0xFF;
        playerBitrow = bitrowChangesForPlayer(playerBitrow, opponentBitrow, column);
        opponentBitrow &= ~playerBitrow;
        finalPBoard |= ((long)playerBitrow << (8 * row));
        finalOBoard |= ((long)opponentBitrow << (8 * row));

        /** Compute column changes. */
        playerBitrow = trasformColumnAInRow0(playerBitboard >>> column);
        opponentBitrow = trasformColumnAInRow0(opponentBitboard >>> column);
        playerBitrow = bitrowChangesForPlayer(playerBitrow, opponentBitrow, row);
        opponentBitrow &= ~playerBitrow;
        finalPBoard |= reTrasformRow0BackToColumnA(playerBitrow) << column;
        finalOBoard |= reTrasformRow0BackToColumnA(opponentBitrow) << column;

        /** Compute changes on diagonal having direction A1-H8. */
        shiftDistance = (column - row) << 3;
        playerBitrow = trasformDiagonalA1H8InRow0(BitWorks.signedLeftShift(playerBitboard, shiftDistance));
        opponentBitrow = trasformDiagonalA1H8InRow0(BitWorks.signedLeftShift(opponentBitboard, shiftDistance));
        playerBitrow = bitrowChangesForPlayer(playerBitrow, opponentBitrow, column);
        opponentBitrow &= ~playerBitrow;
        finalPBoard |= BitWorks.signedLeftShift(reTrasformRow0BackToDiagonalA1H8(playerBitrow), - shiftDistance);
        finalOBoard |= BitWorks.signedLeftShift(reTrasformRow0BackToDiagonalA1H8(opponentBitrow), - shiftDistance);

        /** Compute changes on diagonal having direction H1-A8. */
        shiftDistance = (7 - column - row) << 3;
        playerBitrow = trasformDiagonalH1A8InRow0(BitWorks.signedLeftShift(playerBitboard, shiftDistance));
        opponentBitrow = trasformDiagonalH1A8InRow0(BitWorks.signedLeftShift(opponentBitboard, shiftDistance));
        playerBitrow = bitrowChangesForPlayer(playerBitrow, opponentBitrow, column);
        opponentBitrow &= ~playerBitrow;
        finalPBoard |= BitWorks.signedLeftShift(reTrasformRow0BackToDiagonalH1A8(playerBitrow), - shiftDistance);
        finalOBoard |= BitWorks.signedLeftShift(reTrasformRow0BackToDiagonalH1A8(opponentBitrow), - shiftDistance);
 
        final long[] newbitboard2 = new long[2];
        if (intPlayer == WHITE) {
            newbitboard2[0] = finalOBoard;
            newbitboard2[1] = finalPBoard;
        } else {
            newbitboard2[0] = finalPBoard;
            newbitboard2[1] = finalOBoard;
        }

        final Board result = valueOf(newbitboard2);
        return result;
    }

}