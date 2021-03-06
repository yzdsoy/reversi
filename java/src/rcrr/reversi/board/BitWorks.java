/*
 *  BitWorks.java
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

/**
 * The class is a tool box of static methods that work on long and int values
 * manipulating the bits of such primitives.
 * <p>
 * See: <a href="https://chessprogramming.wikispaces.com/Bitscan" target="_blank">
 *      Chess programming techniques related to bitscan operations</a>.
 */
public final class BitWorks {

    /** Array for de Bruijn multiplication. */
    private static final int[] DEBRUIJN_64_INDEX = {
        63,  0, 58,  1, 59, 47, 53,  2,
        60, 39, 48, 27, 54, 33, 42,  3,
        61, 51, 37, 40, 49, 18, 28, 20,
        55, 30, 34, 11, 43, 14, 22,  4,
        62, 57, 46, 52, 38, 26, 32, 41,
        50, 36, 17, 19, 29, 10, 13, 21,
        56, 45, 25, 31, 35, 16,  9, 12,
        44, 24, 15,  8, 23,  7,  6,  5
    };

    /** 64 bit value for the de Bruijn's "magical" constant. */
    private static final long DEBRUIJN_64_MAGIC_CONSTANT = 0x07EDD5E59A4E28C2L;

    /** Right shift for the de Bruijn's algorithm. */
    private static final int DEBRUIJN_64_SHIFT_VALUE = 58;

    /** Log2 table. */
    private static final int[] LOG2_ARRAY = new int[] {
        0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
    };

    /** Macic number 4. */
    private static final int MAGIC_NUMBER_4 = 4;

    /** Macic number 7. */
    private static final int MAGIC_NUMBER_7 = 7;

    /** Macic number 8. */
    private static final int MAGIC_NUMBER_8 = 8;

    /** Macic number 16. */
    private static final int MAGIC_NUMBER_16 = 16;

    /** Macic number 32. */
    private static final int MAGIC_NUMBER_32 = 32;

    /** Macic number 64. */
    private static final int MAGIC_NUMBER_64 = 64;

    /** Macic number 0x000000FF. */
    private static final int MAGIC_NUMBER_000000FF = 0x000000FF;

    /** Macic number 0x0000FF00. */
    private static final int MAGIC_NUMBER_0000FF00 = 0x0000FF00;

    /** Macic number 0xFFFF0000. */
    private static final int MAGIC_NUMBER_FFFF0000 = 0xFFFF0000;

    /** Macic number 0x00010000. */
    private static final int MAGIC_NUMBER_00010000 = 0x00010000;

    /** Macic number 0x00000000000000FFL. */
    private static final long LONG_00000000000000FF = 0x00000000000000FFL;

    /** Macic number 0x000000000000FFFFL. */
    private static final long LONG_000000000000FFFF = 0x000000000000FFFFL;

    /** Macic number 0xFFFFFFFF00000000L. */
    private static final long LONG_FFFFFFFF00000000 = 0xFFFFFFFF00000000L;

    /**
     * Converts a bit sequence in a printable string.
     *
     * @param bitsequence the value to be printed
     * @return            a string representation of the bit sequence
     */
    public static String byteToString(final byte bitsequence) {
        final StringBuilder sb = new StringBuilder();
        for (int i = MAGIC_NUMBER_7; i >= 0; i--) {
            final char c = ((bitsequence & (1 << i)) != 0) ? '1' : '0';
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Returns the index of the least significant bit set in the {@code bitsequence} parameter
     * via de Bruijn's perfect hashing.
     * <p>
     * Parameter {@code bitsequence} must be different from {@code 0L}.
     * If no bit set is found, meaning that {@code bitsequence} is equal to {@code 0L}, {@code 63} is
     * returned, that is clearly a wrong value.
     * <p>
     * See: <a href="https://chessprogramming.wikispaces.com/Bitscan#DeBruijnMultiplation" target="_blank">
     *      de Bruijn multiplication</a>
     *
     * @param bitsequence long value that is scanned
     * @return            the index of the least significant bit set
     *
     * @see BitWorks#bitscanLS1B0(long)
     */
    public static int bitscanLS1B(final long bitsequence) {
        /** mask isolates the least significant one bit (LS1B). */
        final long mask = bitsequence & (-bitsequence);
        return DEBRUIJN_64_INDEX[(int) ((mask * DEBRUIJN_64_MAGIC_CONSTANT) >>> DEBRUIJN_64_SHIFT_VALUE)];
    }

    /**
     * Returns the index of the least significant bit set in the {@code bitsequence} parameter.
     * <p>
     * When parameter {@code bitsequence} is {@code 0L} it returns {@code 64}.
     * <p>
     * It is here for documentation purposes. The performances of the linear access here proposed are poor,
     * the extimation of the iterations is 64/2 = 32 on avarage.
     * Here the algorithm:
     * <pre>
     *   long mask = 1L;
     *   for (int i = 0; i < 64; i++) {
     *       if ((bitsequence & mask) != 0L) { return i; }
     *       mask <<= 1;
     *   }
     *   return 64;
     * </pre>
     *
     * @param bitsequence long value that is scanned
     * @return            the index of the least significant bit set
     *
     * @see BitWorks#bitscanLS1B(long)
     */
    public static int bitscanLS1B0(final long bitsequence) {
        long mask = 1L;
        for (int i = 0; i < MAGIC_NUMBER_64; i++) {
            if ((bitsequence & mask) != 0L) { return i; }
            mask <<= 1;
        }
        return MAGIC_NUMBER_64;
    }

    /**
     * Returns the index of the most significant bit set in the {@code bitsequence} parameter.
     * <p>
     * Parameter {@code bitsequence} must be different from {@code 0L}.
     * If no bit set is found, meaning that {@code bitsequence} is equal to {@code 0L}, {@code 0} is
     * returned, that is clearly a wrong value.
     * <p>
     * The proposed technique does three divide and conqueror steps, then makes a lookup in a table
     * hosting the log2 value for integers up to 255.
     * <p>
     * So far it is the preferred choice for the reversi implementation.
     *
     * @param bitsequence long value that is scanned
     * @return            the index (0..63) of the most significant bit set
     *
     * @see BitWorks#bitscanMS1B0(long)
     */
    public static int bitscanMS1B(final long bitsequence) {
        long tmp = bitsequence;
        int result = 0;
        if ((tmp & LONG_FFFFFFFF00000000) != 0L) { tmp >>>= MAGIC_NUMBER_32; result  = MAGIC_NUMBER_32; }
        if (tmp > LONG_000000000000FFFF)         { tmp >>>= MAGIC_NUMBER_16; result |= MAGIC_NUMBER_16; }
        if (tmp > LONG_00000000000000FF)         { tmp >>>=  MAGIC_NUMBER_8; result |=  MAGIC_NUMBER_8; }
        result |= LOG2_ARRAY[(int) tmp];
        return result;
    }

    /**
     * Returns an array of two int collecting the octal representation of the position of
     * the most significant bit set in the {@code bitsequence} parameter.
     * The method does not verify that the {@code bitsequence} parameter must be different from {@code 0L}.
     * When this happens it returns a value equal to {@code new int {0, 0}} that is the expected
     * value when the parameter is equal to {@code 1L}.
     *
     * @param bitsequence a long value different from 0L
     * @return            the octal value of the position of the most significant bit set collected in a two cells array
     */
    public static int[] bitscanMS1BtoBase8(final long bitsequence) {
        int tmp;
        int hi = 0;
        if ((bitsequence & LONG_FFFFFFFF00000000) != 0L) {
            tmp = (int) (bitsequence >>> MAGIC_NUMBER_32); hi += MAGIC_NUMBER_4;
        } else {
            tmp = (int) bitsequence;
        }
        if ((tmp & MAGIC_NUMBER_FFFF0000) != 0) { tmp >>>= MAGIC_NUMBER_16; hi += 2; }
        if ((tmp & MAGIC_NUMBER_0000FF00) != 0) { tmp >>>= MAGIC_NUMBER_8;  hi += 1; }
        final int lo = LOG2_ARRAY[tmp];
        return new int[] {lo, hi};
    }

    /**
     * Returns the index of the most significant bit set in the {@code bitsequence} parameter.
     * <p>
     * Parameter {@code bitsequence} must be different from {@code 0L}.
     * If no bit set is found, meaning that {@code bitsequence} is equal to {@code 0L}, {@code 0} is
     * returned, that is clearly a wrong value.
     * <p>
     * The inplementation follows the "divide and conqueror" approach that consumes a number
     * of operations proportional to the logarithm base 2 of 64 that is equal to 6.
     * Here the technique:
     * <pre>
     *   int low = 0;
     *   int high = 64;
     *   while (true) {
     *       final int bit = (low + high) >>> 1;
     *       final long window = bitsequence >>> bit;
     *       if (low == bit) {
     *           return bit;
     *       } else if (window == 0L) {
     *           high = bit;
     *       } else {
     *           low = bit;
     *       }
     *   }
     * </pre>
     *
     * @param bitsequence long value that is scanned
     * @return            the index of the most significant bit set
     *
     * @see BitWorks#bitscanMS1B(long)
     */
    public static int bitscanMS1B0(final long bitsequence) {
        int low = 0;
        int high = MAGIC_NUMBER_64;
        while (true) {
            final int bit = (low + high) >>> 1;
            final long window = bitsequence >>> bit;
            if (low == bit) {
                return bit;
            } else if (window == 0L) {
                high = bit;
            } else {
                low = bit;
            }
        }

    }

    /**
     * Returns an int value having all the bit set in {@code bitsequence} turned to {@code 0}
     * except the most significant one.
     * <p>
     * When parameter {@code bitsequence} is equal to {@code 0} it returns {@code 0}.
     *
     * @param bitsequence the value analyzed
     * @return            an int value having set the bit most significative found in bitsequence
     */
    public static int highestBitSet(final int bitsequence) {
        if (bitsequence == 0) { return 0; }
        int result = 1;
        int tmp = bitsequence;
        if ((tmp & MAGIC_NUMBER_FFFF0000) != 0) { tmp >>>= MAGIC_NUMBER_16; result = MAGIC_NUMBER_00010000; }
        if  (tmp > MAGIC_NUMBER_000000FF)       { tmp >>>= MAGIC_NUMBER_8;  result <<= MAGIC_NUMBER_8; }
        result <<= LOG2_ARRAY[tmp];
        return result;
    }

    /**
     * Returns a string representation of the sequence of bits.
     *
     * @param bitsequence the value passed to the procedure
     * @return            the string representation of the bitsequence
     */
    public static String longToString(final long bitsequence) {
        final StringBuilder sb = new StringBuilder();
        final String toBinaryString = Long.toBinaryString(bitsequence);
        final int len = toBinaryString.length();
        final int missingZeroes = MAGIC_NUMBER_64 - len;
        for (int i = MAGIC_NUMBER_64; i > 0; i--) {
            if ((i != MAGIC_NUMBER_64) && (i % MAGIC_NUMBER_8 == 0)) { sb.append("."); }
            if ((missingZeroes - (MAGIC_NUMBER_64 - i)) > 0) {
                sb.append('0');
            } else {
                sb.append(toBinaryString.charAt(len - i));
            }
        }
        return sb.toString();
    }

    /**
     * The {@code bitsequence} parameter must have one or two bits set.
     * The bits set have to be positioned among the eight bits on the right.
     * Returns a bit sequence of 32 bits having set the bits between the two, or zero
     * when only one bit is set.
     * <p>
     * For example:
     * {@code 00000000.00000000.00000000.00100010} returns {@code 00000000.00000000.00000000.00011100}.
     * <p>
     * When the input data doesn't meet the requirements the result is unpredictable.
     *
     * @param bitsequence the value to be scanned
     * @return            a bit sequence having the internal bits set
     */
    public static int fillInBetween(final int bitsequence) {
        return ((1 << BitWorks.bitscanMS1B(bitsequence)) - 1)
            & ((~bitsequence & MAGIC_NUMBER_000000FF) ^ (bitsequence - 1));
    }

    /**
     * Returns a bit sequence having one bit set, the lowest found in the {@code bitsequence} parameter.
     *
     * @param bitsequence the input value
     * @return            the filtered sequence
     */
    public static long lowestBitSet(final long bitsequence) {
        return (bitsequence & (bitsequence - 1)) ^ bitsequence;
    }

    /**
     * Returns a bit sequence having one bit set, the lowest found in the {@code bitsequence} parameter.
     *
     * @param bitsequence the input value
     * @return            the filtered sequence
     */
    public static int lowestBitSet(final int bitsequence) {
        return (bitsequence & (bitsequence - 1)) ^ bitsequence;
    }

    /**
     * Returns a value computed shifting the {@code bitsequence} parameter
     * to left by a signed amount given by the {@code shift} parameter.
     *
     * @param bitsequence the value that will be shifted
     * @param shift       the number of position to shift
     * @return            the shifted value
     */
    public static long signedLeftShift(final long bitsequence, final int shift) {
        return shift >= 0 ? bitsequence << shift : bitsequence >>> -shift;
    }

    /**
     * Returns a bit sequence being equal to the {@code bitsequence} parameter with
     * the exception of the lowest bit set found.
     *
     * @param bitsequence the input value
     * @return            the resulting bitsequence
     */
    public static long unsetLowestBit(final long bitsequence) {
        return bitsequence & (bitsequence - 1L);
    }

    /** Class constructor. */
    private BitWorks() { }

}
