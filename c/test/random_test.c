/**
 * @file
 *
 * @brief Random unit test suite.
 * @details Collects tests and helper methods for the random module.
 *
 * @par random_test.c
 * <tt>
 * This file is part of the reversi program
 * http://github.com/rcrr/reversi
 * </tt>
 * @author Roberto Corradini mailto:rob_corradini@yahoo.it
 * @copyright 2014 Roberto Corradini. All rights reserved.
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

#include <glib.h>

#include "random.h"



/* Test function prototypes. */

static void dummy_test (void);

static void rng_random_seed_test (void);
static void rng_random_choice_from_finite_set_test (void);



int
main (int   argc,
      char *argv[])
{
  g_test_init (&argc, &argv, NULL);

  g_test_add_func("/utils/dummy", dummy_test);

  g_test_add_func("/utils/rng_random_seed_test", rng_random_seed_test);
  g_test_add_func("/utils/rng_random_choice_from_finite_set_test", rng_random_choice_from_finite_set_test);

  return g_test_run();
}



/*
 * Test functions.
 */

static void
dummy_test (void)
{
  g_assert(TRUE);
}

static void
rng_random_seed_test (void)
{
  static const int sample_size = 10;
  unsigned long int seed[sample_size];
  for (int i = 0; i < sample_size; i++) {
    seed[i] = rng_random_seed();
    for (int j = 0; j < i; j++) {
      g_assert(seed[j] != seed[i]);
    }
  }
}

static void
rng_random_choice_from_finite_set_test (void)
{
  const unsigned long int seed = 123;          /* A seed value used to initialize the RNG. */
  const unsigned long int a_prime_number = 17; /* Used to change the random seed at each iteration. */
  const unsigned long int set_size = 2;        /* Like flipping a coin. */
  const int sample_size = 10000;               /* The number of sample taken in each iteration. */
  const int number_of_tests = 1000;            /* The number of iterations (or tests). */
  const int category_count = 8;                /* Number of chi-square categories adopted. */
  /*
   * See: Donald E. Knuth, The Art of Compuer Programming, Volume 2, Seminumarical Algorithms.
   * Paragraph 3.3.1 - General Test Procedures for Studying Random Data, Table 1.
   */
  /*                                        p=1%,    p=5%,  p=25%,  p=50%, p=75%, p=95%, p=99% */
  const double chi_square_table[][7] = {{0.00016, 0.00393, 0.1015, 0.4549, 1.323, 3.841, 6.635},   /* v=1 */
                                        {0.02010, 0.10260, 0.5754, 1.3860, 2.773, 5.991, 9.210},   /* v=2 */
                                        {0.11480, 0.35180, 1.2130, 2.3660, 4.108, 7.815, 11.34}};  /* v=3 */
  const double probability = 1.0 / set_size;
  const double expected_outcome = sample_size * probability;
  
  unsigned long int test_category_frequencies[category_count];
  for (int k = 0; k < category_count; k++) {
    test_category_frequencies[k] = 0;
  }                 
  for (int j = 0; j < number_of_tests; j++) {
    unsigned long int count_0 = 0;
    unsigned long int count_1 = 0;
  
    RandomNumberGenerator *rng = rng_new(seed + a_prime_number * j);
    for (int i = 0; i < sample_size; i++) {
      unsigned long int rn = rng_random_choice_from_finite_set(rng, set_size);
      g_assert(rn < set_size);
      (rn == 0) ? count_0++ : count_1++;    
    }

    const double diff_0 = (expected_outcome - count_0);
    const double diff_1 = (expected_outcome - count_1);
    const double chi_square = ((diff_0 * diff_0) + (diff_1 * diff_1)) / expected_outcome;
    for (int k = 0; k < category_count - 1; k++) {
      if (chi_square < chi_square_table[0][k]) {
        test_category_frequencies[k]++;
        goto out;
      }
    }
    test_category_frequencies[category_count - 1]++;
  out:
    rng = rng_free(rng);
    g_assert(rng == NULL);
  }

  /*
   * The distribution depends on the RNG, the seed, the increment of the seed, the sample size, and
   * it depends on the size of the set on which we are sampling.
   * Anyhow the distribution appears really credible!
   */
  unsigned long int test_category_expected_frequencies[category_count];
  test_category_expected_frequencies[0]=11;
  test_category_expected_frequencies[1]=58;
  test_category_expected_frequencies[2]=180;
  test_category_expected_frequencies[3]=264;
  test_category_expected_frequencies[4]=254;
  test_category_expected_frequencies[5]=192;
  test_category_expected_frequencies[6]=32;
  test_category_expected_frequencies[7]=9;
  for (int k = 0; k < category_count; k++) {
    g_assert(test_category_expected_frequencies[k] == test_category_frequencies[k]);
  }                 
}