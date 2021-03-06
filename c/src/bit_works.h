/**
 * @file
 *
 * @brief Bit Works module definitions.
 * @details This module defines signed and unsigned
 * integer types, and some procedures processing the bits
 * of these objects.
 *
 * @par bit_works.h
 * <tt>
 * This file is part of the reversi program
 * http://github.com/rcrr/reversi
 * </tt>
 * @author Roberto Corradini mailto:rob_corradini@yahoo.it
 * @copyright 2013, 2014, 2016 Roberto Corradini. All rights reserved.
 *
 * @par License
 * <tt>
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 3, or (at your option) any
 * later version.
 * \n
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * \n
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 * or visit the site <http://www.gnu.org/licenses/>.
 * </tt>
 */

#ifndef BIT_WORKS_H
#define BIT_WORKS_H

#include <stdint.h>

/**
 * @brief Used to return a two digit representation on a given base, for an integer.
 */
typedef struct {
  unsigned char lo;   /**< @brief The low digit. */
  unsigned char hi;   /**< @brief The high digit. */
} HiLo;

extern unsigned int
bit_works_bitcount_64 (uint64_t x);

__attribute__((always_inline))
inline unsigned int
bit_works_bitcount_64_popcnt (uint64_t x)
{
  uint64_t out;
  __asm__ __volatile__ ("popcnt %1, %0" : "=g" (out) : "g" (x));
  return (int) out;
}

extern void
bit_works_bitscan_MS1B_to_base8 (HiLo *result, uint64_t bit_sequence);

extern uint64_t
bit_works_signed_left_shift (uint64_t bit_sequence, int shift);

extern uint64_t
bit_works_signed_left_shift (uint64_t bit_sequence, int shift);

extern uint32_t
bit_works_highest_bit_set_32 (uint32_t bit_sequence);

extern uint8_t
bit_works_highest_bit_set_8 (uint8_t bit_sequence);

extern uint8_t
bit_works_fill_in_between (uint8_t bit_sequence);

extern uint8_t
bit_works_bitscanMS1B_64 (const uint64_t bit_sequence);

extern uint8_t
bit_works_bitscanMS1B_8 (const uint8_t bit_sequence);

extern uint8_t
bit_works_bitscanLS1B_64 (const uint64_t bit_sequence);

__attribute__((always_inline))
inline uint8_t
bit_works_bitscanLS1B_64_bsf (const uint64_t bit_sequence)
{
  uint64_t out;
  __asm__ __volatile__ ("bsf %1, %0" : "=g" (out) : "g" (bit_sequence));
  return (uint8_t) out;
}

extern uint64_t
bit_works_reset_lowest_bit_set_64 (const uint64_t bit_sequence);

__attribute__((always_inline))
inline uint64_t
bit_works_reset_lowest_bit_set_64_blsr (const uint64_t bit_sequence)
{
  uint64_t out;
  __asm__ __volatile__ ("blsr %1, %0" : "=g" (out) : "g" (bit_sequence));
  return (uint64_t) out;
}

extern uint64_t
bit_works_lowest_bit_set_64 (const uint64_t bit_sequence);

extern uint32_t
bit_works_lowest_bit_set_32 (const uint32_t bit_sequence);

extern uint8_t
bit_works_lowest_bit_set_8 (const uint8_t bit_sequence);

extern uint64_t
bit_works_ror_64 (const uint64_t bit_sequence,
                  const unsigned int shift);

extern uint64_t
bit_works_rol_64 (const uint64_t bit_sequence,
                  const unsigned int shift);

#endif /* BIT_WORKS_H */
