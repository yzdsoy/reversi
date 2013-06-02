/**
 * @file
 *
 * @brief Board unit test suite.
 * @details Collects tests and helper methods for the board module.
 *
 * @par board_cu.c
 * <tt>
 * This file is part of the reversi program
 * http://github.com/rcrr/reversi
 * </tt>
 * @author Roberto Corradini mailto:rob_corradini@yahoo.it
 * @copyright 2013 Roberto Corradini. All rights reserved.
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

#include <stdlib.h>
#include <stdio.h>

#include "CUnit/Basic.h"

#include "board.h"

/**
 * The suite initialization function.
 * Returns zero on success, non-zero otherwise.
 */
int init_board_suite(void)
{
  return 0;
}

/**
 * The suite cleanup function.
 * Returns zero on success, non-zero otherwise.
 */
int clean_board_suite(void)
{
  return 0;
}

void test_game_position_print(void)
{
  Board *b;
  Player p;
  GamePosition *gp;

  b = board_new(1LLU, 2LLU);
  p = WHITE_PLAYER;
  gp = game_position_new(b, p);

  char *b_to_string = board_print(b);
  char *gp_to_string = game_position_print(gp);

  printf("\n%s\n", b_to_string);
  printf("\n%s\n", gp_to_string);

  CU_ASSERT_EQUAL(1, 1);
}

void test_direction_shift_square_set(void)
{
  CU_ASSERT_EQUAL(direction_shift_square_set(N,  0xFFFFFFFFFFFFFFFFULL), 0x00FFFFFFFFFFFFFFULL);
  CU_ASSERT_EQUAL(direction_shift_square_set(S,  0xFFFFFFFFFFFFFFFFULL), 0xFFFFFFFFFFFFFF00ULL);
  CU_ASSERT_EQUAL(direction_shift_square_set(E,  0xFFFFFFFFFFFFFFFFULL), 0xFEFEFEFEFEFEFEFEULL);
  CU_ASSERT_EQUAL(direction_shift_square_set(W,  0xFFFFFFFFFFFFFFFFULL), 0x7F7F7F7F7F7F7F7FULL);
  CU_ASSERT_EQUAL(direction_shift_square_set(NE, 0xFFFFFFFFFFFFFFFFULL), 0x00FEFEFEFEFEFEFEULL);
  CU_ASSERT_EQUAL(direction_shift_square_set(SE, 0xFFFFFFFFFFFFFFFFULL), 0xFEFEFEFEFEFEFE00ULL);
  CU_ASSERT_EQUAL(direction_shift_square_set(NW, 0xFFFFFFFFFFFFFFFFULL), 0x007F7F7F7F7F7F7FULL);
  CU_ASSERT_EQUAL(direction_shift_square_set(SW, 0xFFFFFFFFFFFFFFFFULL), 0x7F7F7F7F7F7F7F00ULL);
}

void test_board_get_square(void)
{
  Board *b;

  b = board_new(1LLU, 2LLU);

  CU_ASSERT_EQUAL(board_get_square(b, A1), BLACK_SQUARE);
  CU_ASSERT_EQUAL(board_get_square(b, B1), WHITE_SQUARE);
  CU_ASSERT_EQUAL(board_get_square(b, C1), EMPTY_SQUARE);
  b = board_delete(b);
}

void test_board_count_difference(void)
{
  Board *b;
  SquareSet blacks, whites;

  blacks = 0xFFFFFFFFFFFFFFFFULL;
  whites = 0x0000000000000000ULL;
  b = board_new(blacks, whites);

  CU_ASSERT_EQUAL(board_count_difference(b, BLACK_PLAYER),  64);
  CU_ASSERT_EQUAL(board_count_difference(b, WHITE_PLAYER), -64);
  b = board_delete(b);
}

void test_board_compare(void)
{
  Board *a, *b;

  a = board_new(0xFFFFFFFFFFFFFFFFULL, 0x0000000000000000ULL);
  b = board_new(0xFFFFFFFFFFFFFFFFULL, 0x0000000000000000ULL);
  CU_ASSERT_EQUAL(board_compare(a, b), 0);
  a = board_delete(a);
  b = board_delete(b);

  a = board_new(0xFFFFFFFFFFFFFFFFULL, 0x0000000000000000ULL);
  b = board_new(0x0000000000000000ULL, 0x0000000000000000ULL);
  CU_ASSERT_EQUAL(board_compare(a, b), +1);
  a = board_delete(a);
  b = board_delete(b);

  a = board_new(0x0000000000000000ULL, 0x0000000000000000ULL);
  b = board_new(0x0000000000000001ULL, 0x0000000000000000ULL);
  CU_ASSERT_EQUAL(board_compare(a, b), -1);
  a = board_delete(a);
  b = board_delete(b);

  a = board_new(0x0000000000000007ULL, 0x0000000000000000ULL);
  b = board_new(0x0000000000000007ULL, 0x0000000000000000ULL);
  CU_ASSERT_EQUAL(board_compare(a, b), 0);
  a = board_delete(a);
  b = board_delete(b);

  a = board_new(0x0000000000000007ULL, 0x0100000000000000ULL);
  b = board_new(0x0000000000000007ULL, 0x0000000000000000ULL);
  CU_ASSERT_EQUAL(board_compare(a, b), +1);
  a = board_delete(a);
  b = board_delete(b);

  a = board_new(0x0000000000000007ULL, 0x0000000000000000ULL);
  b = board_new(0x0000000000000007ULL, 0x0100000000000000ULL);
  CU_ASSERT_EQUAL(board_compare(a, b), -1);
  a = board_delete(a);
  b = board_delete(b);
}

void test_board_count_pieces(void)
{
  Board *b;

  b = board_new(1LLU, 2LLU);

  CU_ASSERT_EQUAL(board_count_pieces(b, BLACK_SQUARE),  1);
  CU_ASSERT_EQUAL(board_count_pieces(b, WHITE_SQUARE),  1);
  CU_ASSERT_EQUAL(board_count_pieces(b, EMPTY_SQUARE), 62);
  b = board_delete(b);
}


void test_board_new(void)
{
  SquareSet b, w;
  Board *empty_board;

  b = 0LLU;
  w = 0LLU;
  empty_board = board_new(b, w);
  CU_ASSERT_PTR_NOT_NULL(empty_board);
  empty_board = board_delete(empty_board);

  CU_ASSERT_PTR_NULL(empty_board)
}

/**
 * Unit test of function: SquareState player_color(Player p).
 */
void test_player_color(void)
{
  CU_ASSERT(BLACK_SQUARE == player_color(BLACK_PLAYER));
  CU_ASSERT(WHITE_SQUARE == player_color(WHITE_PLAYER));
}

/**
 * Unit test of function: char *player_description(Player p).
 */
void test_player_description()
{
  CU_ASSERT_STRING_EQUAL(player_description(BLACK_PLAYER), "The Black player");
  CU_ASSERT_STRING_EQUAL(player_description(WHITE_PLAYER), "The White player");
}

/**
 * Unit test of function: Player player_opponent(Player p).
 */
void test_player_opponent(void)
{
  CU_ASSERT(BLACK_PLAYER == player_opponent(WHITE_PLAYER));
  CU_ASSERT(WHITE_PLAYER == player_opponent(BLACK_PLAYER));
}

/**
 * The main() function for setting up and running the tests.
 * Returns a CUE_SUCCESS on successful running, another
 * CUnit error code on failure.
 */
int main()
{
   CU_pSuite pSuite = NULL;

   /* Initialize the CUnit test registry */
   if (CUE_SUCCESS != CU_initialize_registry())
      return CU_get_error();

   /* Add a suite to the registry */
   pSuite = CU_add_suite("Board Unit Test", init_board_suite, clean_board_suite);
   if (NULL == pSuite) {
      CU_cleanup_registry();
      return CU_get_error();
   }

   /* Add the tests to the suite */
   if ((NULL == CU_add_test(pSuite, "Player player_opponent(Player p)", test_player_opponent))                           ||
       (NULL == CU_add_test(pSuite, "SquareState player_color(Player p)", test_player_color))                            ||
       (NULL == CU_add_test(pSuite, "char *player_description(Player p)", test_player_description))                      ||
       (NULL == CU_add_test(pSuite, "Board *board_new(SquareSet b, SquareSet w)", test_board_new))                       ||
       (NULL == CU_add_test(pSuite, "SquareState board_get_square(Board *b, Square sq)", test_board_get_square))         ||
       (NULL == CU_add_test(pSuite, "int board_count_pieces(Board *b, SquareState color)", test_board_count_pieces))     ||
       (NULL == CU_add_test(pSuite, "int board_count_difference(Board *b, Player p)", test_board_count_difference))      ||
       (NULL == CU_add_test(pSuite, "SquareSet test_direction_shift_square_set(Direction dir, SquareSet squares)",
                            test_direction_shift_square_set))                                                            ||
       (NULL == CU_add_test(pSuite, "int board_compare(Board *a, Board *b)", test_board_compare))                        ||
       (NULL == CU_add_test(pSuite, "Test game_position_print function", test_game_position_print))
       )
   {
      CU_cleanup_registry();
      return CU_get_error();
   }

   /* Run all tests using the CUnit Basic interface */
   CU_basic_set_mode(CU_BRM_VERBOSE);
   CU_basic_run_tests();
   CU_cleanup_registry();
   return CU_get_error();
}

