/**
 * @file
 *
 * @brief Red black tree unit test suite.
 * @details Collects tests and helper methods for the red-black tree module.
 *
 * @par red_black_tree_test.c
 * <tt>
 * This file is part of the reversi program
 * http://github.com/rcrr/reversi
 * </tt>
 * @author Roberto Corradini mailto:rob_corradini@yahoo.it
 * @copyright 2015 Roberto Corradini. All rights reserved.
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
#include <assert.h>
#include <time.h>
#include <unistd.h>

#include <glib.h>

#include "red_black_tree.h"
#include "random.h"
#include "sort_utils.h"



/* Time spec type definition. See sys/time.h for more details. */
typedef struct timespec timespec_t;



/* Static constants. */

static const char *const program_documentation_string =
  "Description:\n"
  "Runs unit tests and performance tests for the red-black tree module.";



/* Static variables. */

static gchar *output_perf_log_dir = NULL;

static const GOptionEntry entries[] =
  {
    { "output-perf-log-dir", 0, 0, G_OPTION_ARG_STRING, &output_perf_log_dir, "Directory for output performance log files.", NULL },
    { NULL }
  };



/* Test function prototypes. */

static void creation_and_destruction_test (void);
static void probe_test (void);
static void copy_test (void);
static void copy_test (void);
static void insert_replace_and_find_test (void);
static void delete_test (void);
static void volume_test (void);
static void random_key_volume_test (void);
static void traverser_basic_test (void);
static void traverser_find_and_copy_test (void);
static void traverser_insert_test (void);
static void traverser_replace_test (void);
static void traverser_on_changing_table_test (void);
static void performance_a_test (void);



/* Helper function prototypes. */

static int
compare_int (const void *item_a,
             const void *item_b,
             void *param);

static int *
prepare_data_array (const size_t len,
                    const int seed);

int
timespec_diff (timespec_t *result,
               const timespec_t *start,
               const timespec_t *end);

int
sort_utils_element_cmp (const void *const a,
                        const void *const b);



int
main (int   argc,
      char *argv[])
{
  g_test_init(&argc, &argv, NULL);

  /* GLib command line options and argument parsing. */
  GError *error = NULL;
  GOptionContext *context = g_option_context_new("- Unit tests for the red-black tree module.");
  g_option_context_add_main_entries(context, entries, NULL);
  g_option_context_set_description(context, program_documentation_string);
  g_option_context_set_ignore_unknown_options(context, TRUE);
  if (!g_option_context_parse(context, &argc, &argv, &error)) {
    g_print("Option parsing failed: %s\n", error->message);
    return -1;
  }

  g_test_add_func("/red_black_tree/creation_and_destruction_test", creation_and_destruction_test);
  g_test_add_func("/red_black_tree/probe_test", probe_test);
  g_test_add_func("/red_black_tree/copy_test", copy_test);
  g_test_add_func("/red_black_tree/insert_replace_and_find_test", insert_replace_and_find_test);
  g_test_add_func("/red_black_tree/delete_test", delete_test);
  g_test_add_func("/red_black_tree/volume_test", volume_test);
  g_test_add_func("/red_black_tree/random_key_volume_test", random_key_volume_test);

  g_test_add_func("/red_black_tree/traverser_basic_test", traverser_basic_test);
  g_test_add_func("/red_black_tree/traverser_find_and_copy_test", traverser_find_and_copy_test);
  g_test_add_func("/red_black_tree/traverser_insert_test", traverser_insert_test);
  g_test_add_func("/red_black_tree/traverser_replace_test", traverser_replace_test);
  g_test_add_func("/red_black_tree/traverser_on_changing_table_test", traverser_on_changing_table_test);

  if (g_test_perf()) {
    g_test_add_func("/red_black_tree/performance_a_test", performance_a_test);
  }

  g_option_context_free(context);
  return g_test_run();
}



/*
 * Test functions for the table structure.
 */

static void
creation_and_destruction_test (void)
{
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  size_t count = rbt_count(table);
  g_assert(count == 0);

  rbt_destroy(table, NULL);
}

static void
probe_test (void)
{
  /* Test data set is composed by an array of ten integers: [0..9]. */
  int data[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  const size_t data_size = sizeof(data) / sizeof(data[0]);

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Count has to be zero. */
  g_assert(rbt_count(table) == 0);

  /* Inserts the [0..9] set of elements in the table in sequential order. */
  for (size_t i = 0; i < data_size; i++) {
    int *item = &data[i];
    int **item_ref = (int **) rbt_probe(table, item);
    g_assert(rbt_count(table) == i + 1);             /* Table count has to be equal to the number of inserted elements. */
    g_assert(*item_ref != NULL);                     /* Item pointer has to be not null. */
    g_assert(*item_ref == &data[i]);                 /* Item pointer has to reference the appropriate array element. */
    g_assert(**item_ref == i);                       /* Item (**item_ref) has to be equal to the loop counter. */
  }

  /* Probes the table again with the same data set. Nothing has to happen. */
  for (size_t i = 0; i < data_size; i++) {
    int *item = &data[i];
    int **item_ref = (int **) rbt_probe(table, item);
    g_assert(rbt_count(table) == data_size);         /* Table count has to stay constat at data_size. */
    g_assert(*item_ref != NULL);
    g_assert(*item_ref == &data[i]);
    g_assert(**item_ref == i);
  }

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
copy_test (void)
{
  /* Test data set is composed by an array of ten integers: [0..9]. */
  int data[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  const size_t data_size = sizeof(data) / sizeof(data[0]);

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Inserts the [0..9] set of elements in the table. */
  for (size_t i = 0; i < data_size; i++) {
    int *item = &data[i];
    rbt_probe(table, item);
  }

  /* We must have data_size elements in the table. */
  g_assert(rbt_count(table) == data_size);

  /* Copies table into a new tree. */
  rbt_item_copy_f *copy = NULL;
  rbt_item_destroy_f *destroy = NULL;
  mem_allocator_t *alloc = NULL;
  rbt_table_t *copied_table = rbt_copy(table, copy, destroy, alloc);

  /* Frees the table. */
  rbt_destroy(table, NULL);

  /* We must have data_size elements in the table. */
  g_assert(rbt_count(copied_table) == data_size);

  /* Frees the table. */
  rbt_destroy(copied_table, NULL);
}

static void
insert_replace_and_find_test (void)
{
  /* We need data with key and content to properly test the replace use case. */
  struct element {
    int key;
    int content;
  };

  /* Values assigned to data. */
  const int value_a = 0;
  const int value_b = 1;

  /* Test data set is composed by an array of ten element structure: [{0,0}..{9,0}]. */
  struct element data_a[] = { {0, value_a},
                              {1, value_a},
                              {2, value_a},
                              {3, value_a},
                              {4, value_a},
                              {5, value_a},
                              {6, value_a},
                              {7, value_a},
                              {8, value_a},
                              {9, value_a} };

  /* Data size is dynamically computed. */
  const size_t data_size = sizeof(data_a) / sizeof(data_a[0]);

  /* A second array set is prepared, having the same size, and same keys, but different content. */
  struct element data_b[data_size];
  for (size_t i = 0; i < data_size; i++) {
    data_b[i].key = data_a[i].key;
    data_b[i].content = value_b;
  }

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Inserts the data_a set of elements in the table. */
  for (size_t i = 0; i < data_size; i++) {
    struct element *e = &data_a[i];
    struct element *e_ref = (struct element *) rbt_insert(table, e);
    g_assert(rbt_count(table) == i + 1);         /* Table count has to be equal to the number of inserted elements. */
    g_assert(e_ref == NULL);                     /* Item pointer has to be null when insertion succeeds. */
  }

  /* We must have data_size elements in the table now. */
  g_assert(rbt_count(table) == data_size);

  /* Inserts the data_b set of elements in the table. Nothing has to happen. */
  for (size_t i = 0; i < data_size; i++) {
    struct element *e = &data_b[i];
    struct element *e_ref = (struct element *) rbt_insert(table, e);
    g_assert(rbt_count(table) == data_size);     /* Table count has to stay constant. */
    g_assert(e_ref != NULL);                     /* Item pointer has to be not null. */
    g_assert(e_ref == &data_a[i]);               /* Item pointer has to reference the appropriate array element. */
    g_assert(e_ref->key == i);                   /* Item's key has to be equal to the loop counter. */
    g_assert(e_ref->content == value_a);         /* Item's content has to be equal to value_a. */
  }

  /* We must have still data_size elements in the table. */
  g_assert(rbt_count(table) == data_size);

  /* Replaces elements using data_b set. All elements has to be replaced. */
  for (size_t i = 0; i < data_size; i++) {
    struct element *e = &data_b[i];
    struct element *e_ref = (struct element *) rbt_replace(table, e);
    g_assert(rbt_count(table) == data_size);     /* Table count has to stay constant. */
    g_assert(e_ref != NULL);                     /* Item pointer has to be not null. */
    g_assert(e_ref == &data_a[i]);               /* Item pointer has to reference the appropriate array element. */
    g_assert(e_ref->key == i);                   /* Item's key has to be equal to the loop counter. */
    g_assert(e_ref->content == value_a);         /* Item's content has to be equal to value_a. */
  }

  /* We must have still data_size elements in the table. */
  g_assert(rbt_count(table) == data_size);

  /* Searches all the ten elements, they must be found, and the contant has to be equal to value_b. */
  for (size_t i = 0; i < data_size; i++) {
    struct element *e = (struct element *) rbt_find(table, &i);
    g_assert(e != NULL);
    g_assert(e == &data_b[i]);
    g_assert(e->key == i);
    g_assert(e->content == value_b);
  }

  /* Searches for a missing key, result has to be null. */
  int missing_key = -1;
  struct element *missing = rbt_find(table, &missing_key);
  g_assert(missing == NULL);

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
delete_test (void)
{
  /* Test data set is composed by an array of 48 integers: [0..47]. */
  int data[] = {  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
                 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
                 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47 };
  const size_t data_size = sizeof(data) / sizeof(data[0]);

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Count has to be zero. */
  g_assert(rbt_count(table) == 0);

  /* Inserts the [0..9] set of elements in the table in sequential order. */
  for (size_t i = 0; i < data_size; i++) {
    int *item = &data[i];
    int **item_ref = (int **) rbt_probe(table, item);
    g_assert(rbt_count(table) == i + 1);             /* Table count has to be equal to the number of inserted elements. */
    g_assert(*item_ref != NULL);                     /* Item pointer has to be not null. */
    g_assert(*item_ref == &data[i]);                 /* Item pointer has to reference the appropriate array element. */
    g_assert(**item_ref == i);                       /* Item (**item_ref) has to be equal to the loop counter. */
  }

  /* Deletes all elements one by one. */
  for (size_t i = 0; i < data_size; i++) {
    int *e = &data[i];
    int *e_ref = (int *) rbt_delete(table, e);
    g_assert(rbt_count(table) == data_size - (i + 1));         /* Table count has to stay constat at data_size. */
    g_assert(*e_ref == *e);
    for (size_t j = 0; j <= i; j++) {
      int *e1 = (int *) rbt_find(table, &j);
      g_assert(e1 == NULL);
    }
    for (size_t j = i + 1; j < data_size; j++) {
      int *e1 = (int *) rbt_find(table, &j);
      g_assert(e1 != NULL);
      g_assert(*e1 == j);
    }
  }

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
volume_test (void)
{
  int *data;
  mem_allocator_t *alloc = &mem_allocator_default;

  const size_t max_size = 1024;

  for (size_t k = 1; k < max_size; k++) {

    data = alloc->malloc(alloc, sizeof(int) * k);
    for (size_t i = 0; i < k; i++) {
      data[i] = i;
    }

    rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
    g_assert(table);

    g_assert(rbt_count(table) == 0);

    /* Inserts elements sequentially. */
    for (size_t i = 0; i < k; i++) {
      int *e = &data[i];
      int **e_ref = (int **) rbt_probe(table, e);
      g_assert(rbt_count(table) == i + 1);         /* Table count has to be equal to the number of inserted elements. */
      g_assert(*e_ref != NULL);                    /* Item pointer has to be not null. */
      g_assert(*e_ref == &data[i]);                /* Item pointer has to reference the appropriate array element. */
      g_assert(**e_ref == i);                      /* Item (**e_ref) has to be equal to the loop counter. */
    }

    /* We must have k elements in the table. */
    g_assert(rbt_count(table) == k);

    /* Searches for each element and checks it exists in the table. */
    for (size_t i = 0; i < k; i++) {
      int *e = (int *) rbt_find(table, &i);
      g_assert(e != NULL);
      g_assert(*e == i);
    }

    /* Deletes odd element and checks it exists in the table. */
    for (size_t i = 0; i < (k + 1) / 2; i++) {
      int i2 = i * 2;
      int *e = (int *) rbt_delete(table, &i2);
      g_assert(e != NULL);
      g_assert(*e == i2);
    }

    /* Final count has to be half of the number of inserted elements. */
    g_assert(rbt_count(table) == (size_t)(k / 2));

    rbt_destroy(table, NULL);

    alloc->free(alloc, data);
  }

}

static void
random_key_volume_test (void)
{
  /* Structure element for data array has key and val. */
  struct element {
    int key;
    int val;
  };

  /* Data size. */
  const size_t data_size = 2048;

  /* Number of insertions. */
  const size_t insertion_count = 4096;

  /* Checksums. */
  size_t element_count_checksum = 0;
  size_t insertion_count_checksum = 0;

  /* Data is initialized having key equal to the element index into the array, and val equal to zero. */
  struct element data[data_size];
  for (size_t i = 0; i < data_size; i++) {
    data[i].key = i;
    data[i].val = 0;
  }

  /* Sets up the RNG. */
  const unsigned int seed = 20150801;
  RandomNumberGenerator *rng = rng_new(seed);
  g_assert(rng);

  /* Creates the table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Populates the table with random selection within the set. Element's val field is incremented on each probe. */
  for (size_t k = 0; k < insertion_count; k++) {
    const unsigned long index = rng_random_choice_from_finite_set(rng, data_size);
    struct element **e = (struct element **) rbt_probe(table, &data[index]);
    (*e)->val++;
  }

  /* Checks that the tree has the probed elements, and doesn't have the skipped ones. */
  for (size_t i = 0; i < data_size; i++) {
    const int key = i;
    const int count = data[i].val;
    insertion_count_checksum += count;
    if (count) element_count_checksum++;
    struct element *e = (struct element *) rbt_find(table, &key);
    if (e) g_assert(count > 0);
    else g_assert(count == 0);
  }
  g_assert(insertion_count_checksum == insertion_count);
  g_assert(element_count_checksum == rbt_count(table));
  g_assert(element_count_checksum == 1776);      /* Depends on RNG, seed, data_size, and insertion_count. */

  /* Frees resources. */
  rbt_destroy(table, NULL);
  rng_free(rng);
}



/*
 * Test functions for the traverser structure.
 */

static void
traverser_basic_test (void)

{
  rbt_traverser_t traverser;
  rbt_traverser_t *t = &traverser;
  int *e;
  int counter;

  /* Test data set is composed by an array of ten integers: [0..9]. */
  int data[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  const size_t data_size = sizeof(data) / sizeof(data[0]);

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Inserts the [0..9] set of elements in the table in sequential order. */
  for (size_t i = 0; i < data_size; i++) {
    rbt_probe(table, &data[i]);
  }

  /* Traverses the table clockwise starting from the null element. */
  counter = 0;
  rbt_t_init(t, table);
  while ((e = rbt_t_next(t))) {
    g_assert(*e == counter++);
    g_assert(rbt_t_cur(t) == e);
  }
  g_assert(counter == data_size);
  g_assert(rbt_t_cur(t) == NULL);

  /* Traverses the table counterclockwise starting from the null element. */
  counter = data_size;
  rbt_t_init(t, table);
  while ((e = rbt_t_prev(t))) {
    g_assert(*e == --counter);
    g_assert(rbt_t_cur(t) == e);
  }
  g_assert(counter == 0);
  g_assert(rbt_t_cur(t) == NULL);

  /* Traverses the table clockwise starting from the first element. */
  for (e = rbt_t_first(t, table), counter = 0; e; e = rbt_t_next(t), counter++) {
    g_assert(*e == counter);
    g_assert(rbt_t_cur(t) == e);
  }
  g_assert(counter == data_size);
  g_assert(rbt_t_cur(t) == NULL);

  /* Traverses the table counterclockwise starting from the last element. */
  for (e = rbt_t_last(t, table), counter = data_size - 1; e; e = rbt_t_prev(t), counter--) {
    g_assert(*e == counter);
    g_assert(rbt_t_cur(t) == e);
  }
  g_assert(counter == -1);
  g_assert(rbt_t_cur(t) == NULL);

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
traverser_find_and_copy_test (void)

{
  rbt_traverser_t traverser_1, traverser_2;
  rbt_traverser_t *t1 = &traverser_1;
  rbt_traverser_t *t2 = &traverser_2;
  int *e;

  /* Test data set is composed by an array of ten integers: [0..16]. */
  int data[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
  const size_t data_size = sizeof(data) / sizeof(data[0]);

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Inserts the [0..9] set of elements in the table in sequential order. */
  for (size_t i = 0; i < data_size; i++) {
    rbt_probe(table, &data[i]);
  }

  for (int key = 0; key < data_size; key ++) {
    e = (int *) rbt_t_find(t1, table, &key);
    g_assert(*e == key);

    e = rbt_t_copy(t2, t1);
    g_assert(*e == key);

    int *next = (int *) rbt_t_next(t1);
    if (key == data_size - 1) g_assert(!next);
    else g_assert(*next == key + 1);

    int *prev = (int *) rbt_t_prev(t2);
    if (key == 0) g_assert(!prev);
    else g_assert(*prev == key - 1);
  }

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
traverser_insert_test (void)
{
  rbt_traverser_t traverser;
  rbt_traverser_t *t = &traverser;
  int *e;

  /* Test data set is composed by an array of ten integers: [0..16]. */
  int data[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
  const size_t data_size = sizeof(data) / sizeof(data[0]);
  const size_t missing_element = 13;
  int *prev = &data[12];

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Inserts the [0..9] set of elements in the table in sequential order. */
  for (size_t i = 0; i < data_size; i++) {
    if (i != 13) rbt_probe(table, &data[i]);
  }

  /* Table must have one element missing. */
  g_assert(rbt_count(table) == data_size - 1);

  /* Searches for the 12th element and verifies that the next is the 14th. */
  e = (int *) rbt_t_find(t, table, prev);
  g_assert(e && *e == 12);
  e = rbt_t_next(t);
  g_assert(e && *e == 14);

  /* Inserts the missing elements and verifies it. */
  e = (int *) rbt_t_insert(t, table, &data[missing_element]);
  g_assert(e);
  g_assert(*e == 13);
  g_assert(rbt_count(table) == data_size);

  /* Searches for the 12th element and verifies that the next is the 13th. */
  e = (int *) rbt_t_find(t, table, prev);
  g_assert(e && *e == 12);
  e = rbt_t_next(t);
  g_assert(e && *e == 13);

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
traverser_replace_test (void)
{
  rbt_traverser_t traverser;
  rbt_traverser_t *t = &traverser;

  /* We need data with key and content to properly test the replace use case. */
  struct element {
    int key;
    int content;
  };

  /* Values assigned to data. */
  const int value_a = 0;
  const int value_b = 1;

  /* Test data set is composed by an array of ten element structure: [{0,0}..{9,0}]. */
  struct element data_a[] = { {0, value_a},
                              {1, value_a},
                              {2, value_a},
                              {3, value_a},
                              {4, value_a},
                              {5, value_a},
                              {6, value_a},
                              {7, value_a},
                              {8, value_a},
                              {9, value_a} };

  /* Data size is dynamically computed. */
  const size_t data_size = sizeof(data_a) / sizeof(data_a[0]);

  /* A second array set is prepared, having the same size, and same keys, but different content. */
  struct element data_b[data_size];
  for (size_t i = 0; i < data_size; i++) {
    data_b[i].key = data_a[i].key;
    data_b[i].content = value_b;
  }

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Inserts the data_a set of elements in the table. */
  for (size_t i = 0; i < data_size; i++) {
    struct element *e = &data_a[i];
    rbt_insert(table, e);
  }

  size_t index = 3;
  struct element *e = &data_a[index];
  struct element *r = &data_b[index];
  g_assert(e->key == r->key);

  e = (struct element *) rbt_t_find(t, table, e);
  g_assert(e && e->key == index && e->content == value_a);

  rbt_t_replace(t, r);
  e = (struct element *) rbt_t_next(t);
  g_assert(e && e->key == index + 1 && e->content == value_a);
  e = (struct element *) rbt_t_prev(t);
  g_assert(e && e->key == index && e->content == value_b);

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
traverser_on_changing_table_test (void)
{
  rbt_traverser_t traverser;
  rbt_traverser_t *t = &traverser;
  int *e;

  /* Test data set is composed by an array of ten integers: [0..31]. */
  int data[] = {  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
                 16, 17, 18, 18, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };

  /* Set 0. */
  size_t keys_0[] = { 1, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30 };
  const size_t keys_0_size = sizeof(keys_0) / sizeof(keys_0[0]);

  /* Set 1. */
  size_t keys_1[] = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30 };
  const size_t keys_1_size = sizeof(keys_1) / sizeof(keys_1[0]);

  /* Set 2. */
  size_t keys_2[] = { 20, 21, 22, 23, 24, 25, 26, 27 };
  const size_t keys_2_size = sizeof(keys_2) / sizeof(keys_2[0]);

  /* Creates the new empty table. */
  rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
  g_assert(table);

  /* Inserts the data set of elements identified by keys_0 in the table. */
  for (size_t i = 0; i < keys_0_size; i++) {
    e = &data[keys_0[i]];
    int **e_ptr = (int **) rbt_probe(table, e);
    g_assert(e_ptr);
  }
  g_assert(rbt_count(table) == keys_0_size);

  /* The last element has to be 30. */
  e = (int *) rbt_t_last(t, table);
  g_assert(e && *e == 30);

  /* Inserts the data set of elements identified by keys_1 in the table. */
  for (size_t i = 0; i < keys_1_size; i++) {
    e = &data[keys_1[i]];
    int **e_ptr = (int **) rbt_probe(table, e);
    g_assert(e_ptr);
  }
  g_assert(rbt_count(table) == 21);

  /* Going back from element 30 we shoud find element 28. */
  e = (int *) rbt_t_prev(t);
  g_assert(e && *e == 28);

  /* Removes the data set of elements identified by keys_2 from the table. */
  for (size_t i = 0; i < keys_2_size; i++) {
    e = &data[keys_2[i]];
    rbt_delete(table, e);
  }
  g_assert(rbt_count(table) == 15);

  /* Going back from element 28 we shoud find element 18. */
  e = (int *) rbt_t_prev(t);
  g_assert(e && *e == 18);

  /* Frees the table. */
  rbt_destroy(table, NULL);
}

static void
performance_a_test (void)
{
  const size_t initial_len = 1000;
  const size_t step_len = 2000;
  const size_t steps = 500;
  const size_t delta = 10;
  const size_t repeats = 17;
  g_assert(initial_len > delta * (repeats + 1));

  const int initial_seed = 1898;
  const int seed_increment = 37;
  const char *const out_perf_log_file_name = "rbt_performance_a_log.csv";

  int *data;
  timespec_t time_0, time_1, time_diff;
  FILE *fp;
  char *op_type;
  size_t op_initial_count, op_final_count;
  char ltime_to_s[64];
  time_t ltime;
  int ret;

  /* Opens the log file. */
  char fname[512];
  int access_check;
  if (output_perf_log_dir) {
    access_check = access(output_perf_log_dir, W_OK);
    snprintf(fname, sizeof(fname), "%s/%s", output_perf_log_dir, out_perf_log_file_name);
  } else {
    access_check = access(".", W_OK);
    snprintf(fname, sizeof(fname), "%s", out_perf_log_file_name);
  }
  if (access_check != 0) {
    fprintf(stderr, "Directory doesn't exist, or access denied for file: %s\n", fname);
    exit(1);
  }
  fp = fopen(fname, "w");
  g_assert(fp);
  fprintf(fp, "%s;%s;%s;%s;%s;%s;%s\n",
          "LTIME",
          "OP_TYPE",
          "OP_SIZE",
          "OP_INITIAL_COUNT",
          "OP_FINAL_COUNT",
          "CPUTIME_SEC",
          "CPUTIME_NSEC");

  for (size_t k = 0; k < steps; k++) {
    size_t len = initial_len + k * step_len;
    size_t tlen = len + (repeats * delta);
    int seed = initial_seed + k * seed_increment;

    /* Prepares the array of integers [0..tlen], then shuffled. */
    data = prepare_data_array(tlen, seed);

    /* Creates the new empty table. */
    rbt_table_t *table = rbt_create(compare_int, NULL, NULL);
    g_assert(table);

    /* Operation 1: populate the table. */
    op_type = "rnd_populate";
    op_initial_count = rbt_count(table);

    /* Takes initial time for operation. */
    ltime = time(NULL);
    strftime(ltime_to_s, sizeof(ltime_to_s), "%Y%m%d-%H:%M:%S-%Z", localtime(&ltime));

    /* Starts the stop-watch. */
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);

    /* Inserts the data set of elements in the table. */
    for (size_t i = 0; i < len; i++) {
      rbt_probe(table, &data[i]);
    }

    /* Stops the stop-watch. */
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_1);

    /* Verifies that the size of the table is equal to the count of inserted elements. */
    op_final_count = rbt_count(table);
    g_assert(op_final_count == len);

    /* Computes the time taken. */
    ret = timespec_diff(&time_diff, &time_0, &time_1);
    g_assert(!ret);

    fprintf(fp, "%s;%s;%zu;%zu;%zu;%ld;%ld\n",
            ltime_to_s,
            op_type,
            len,
            op_initial_count,
            op_final_count,
            time_diff.tv_sec,
            time_diff.tv_nsec);

    /* End of operation 1. */


    /* Repeats insert and delete sequences a number of times equal to repeats constant. */
    for (size_t j = 0; j < repeats; j++) {

      /* Operation 2: inserts delta new elements in table. */
      op_type = "rnd_insert_new_elm";
      op_initial_count = rbt_count(table);

      /* Takes initial time for operation. */
      ltime = time(NULL);
      strftime(ltime_to_s, 64, "%Y%m%d-%H:%M:%S-%Z", localtime(&ltime));

      /* Starts the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);

      /* Inserts the data set of elements in the table. */
      for (size_t i = 0; i < delta; i++) {
        rbt_probe(table, &data[len + delta * j + i]);
      }

      /* Stops the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_1);

      /* Verifies that the size of the table is equal to the count of inserted elements. */
      op_final_count = rbt_count(table);
      g_assert(op_final_count == len + delta);

      /* Computes the time taken. */
      ret = timespec_diff(&time_diff, &time_0, &time_1);
      g_assert(!ret);

      fprintf(fp, "%s;%s;%zu;%zu;%zu;%ld;%ld\n",
              ltime_to_s,
              op_type,
              delta,
              op_initial_count,
              op_final_count,
              time_diff.tv_sec,
              time_diff.tv_nsec);

      /* End of operation 2. */


      /* Operation 3: deletes delta elements from table. */
      op_type = "rnd_remove_existing_elm";
      op_initial_count = rbt_count(table);

      /* Takes initial time for operation. */
      ltime = time(NULL);
      strftime(ltime_to_s, 64, "%Y%m%d-%H:%M:%S-%Z", localtime(&ltime));

      /* Starts the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);

      /* Deletes the data set of elements from the table. */
      for (size_t i = 0; i < delta; i++) {
        rbt_delete(table, &data[delta * j + i]);
      }

      /* Stops the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_1);

      /* Verifies that the size of the table is equal to the count of inserted elements. */
      op_final_count = rbt_count(table);
      g_assert(op_final_count == len);

      /* Computes the time taken. */
      ret = timespec_diff(&time_diff, &time_0, &time_1);
      g_assert(!ret);

      fprintf(fp, "%s;%s;%zu;%zu;%zu;%ld;%ld\n",
              ltime_to_s,
              op_type,
              delta,
              op_initial_count,
              op_final_count,
              time_diff.tv_sec,
              time_diff.tv_nsec);

      /* End of operation 3. */


      /* Operation 4a: searches for existing elements. */
      op_type = "find_existing_a_elm";
      op_initial_count = rbt_count(table);

      /* Takes initial time for operation. */
      ltime = time(NULL);
      strftime(ltime_to_s, 64, "%Y%m%d-%H:%M:%S-%Z", localtime(&ltime));

      /* Starts the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);

      /* Searches for the first group of elements. */
      for (size_t i = 0; i < delta; i++) {
        int *e = (int *) rbt_find(table, &data[delta * (j + 1) + i]);
        g_assert(e);
      }

      /* Stops the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_1);

      /* Verifies that the size of the table is equal to the count of inserted elements. */
      op_final_count = rbt_count(table);
      g_assert(op_final_count == len);

      /* Computes the time taken. */
      ret = timespec_diff(&time_diff, &time_0, &time_1);
      g_assert(!ret);

      fprintf(fp, "%s;%s;%zu;%zu;%zu;%ld;%ld\n",
              ltime_to_s,
              op_type,
              delta,
              op_initial_count,
              op_final_count,
              time_diff.tv_sec,
              time_diff.tv_nsec);

      /* End of operation 4a. */


      /* Operation 4z: searches for existing elements. */
      op_type = "find_existing_z_elm";
      op_initial_count = rbt_count(table);

      /* Takes initial time for operation. */
      ltime = time(NULL);
      strftime(ltime_to_s, 64, "%Y%m%d-%H:%M:%S-%Z", localtime(&ltime));

      /* Starts the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);

      /* Searches for the first group of elements. */
      for (size_t i = 0; i < delta; i++) {
        int *e = (int *) rbt_find(table, &data[len - delta * j + i]);
        g_assert(e);
      }

      /* Stops the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_1);

      /* Verifies that the size of the table is equal to the count of inserted elements. */
      op_final_count = rbt_count(table);
      g_assert(op_final_count == len);

      /* Computes the time taken. */
      ret = timespec_diff(&time_diff, &time_0, &time_1);
      g_assert(!ret);

      fprintf(fp, "%s;%s;%zu;%zu;%zu;%ld;%ld\n",
              ltime_to_s,
              op_type,
              delta,
              op_initial_count,
              op_final_count,
              time_diff.tv_sec,
              time_diff.tv_nsec);

      /* End of operation 4z. */


      /* Operation 5: searches for missing elements. */
      op_type = "find_missing_elm";
      op_initial_count = rbt_count(table);

      /* Takes initial time for operation. */
      ltime = time(NULL);
      strftime(ltime_to_s, 64, "%Y%m%d-%H:%M:%S-%Z", localtime(&ltime));

      /* Starts the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);

      /* Searches for the elements just deleted. */
      for (size_t i = 0; i < delta; i++) {
        int *e = (int *) rbt_find(table, &data[delta * j + i]);
        g_assert(!e);
      }

      /* Stops the stop-watch. */
      clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_1);

      /* Verifies that the size of the table is equal to the count of inserted elements. */
      op_final_count = rbt_count(table);
      g_assert(op_final_count == len);

      /* Computes the time taken. */
      ret = timespec_diff(&time_diff, &time_0, &time_1);
      g_assert(!ret);

      fprintf(fp, "%s;%s;%zu;%zu;%zu;%ld;%ld\n",
              ltime_to_s,
              op_type,
              delta,
              op_initial_count,
              op_final_count,
              time_diff.tv_sec,
              time_diff.tv_nsec);

      /* End of operation 5. */


    } /* End of repeat cicles. */


    /*
     * Operation 6:
     * - Prepares an index of keys pointing to the portion of data contained in table.
     * - Sorts the keys.
     * - Verifies that the traverser provides the same sequence of pointers prepared in
     *   the sorted index.
     */
    op_type = "traverse_table";
    op_initial_count = rbt_count(table);

    int **index = (int **) malloc(len * sizeof(int *));
    g_assert(index);
    for (size_t j = 0; j < len; j++) {
      index[j] = &data[delta * repeats + j];
    }
    sort_utils_heapsort(index, len, sizeof(int *), sort_utils_element_cmp);
    rbt_traverser_t trav;

    /* Takes initial time for operation. */
    ltime = time(NULL);
    strftime(ltime_to_s, 64, "%Y%m%d-%H:%M:%S-%Z", localtime(&ltime));

    /* Starts the stop-watch. */
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);

    /* Traverses the table. */
    int *e = (int *) rbt_t_first(&trav, table);
    size_t j = 0;
    while (e) {
      g_assert(*e == *index[j]);
      e = (int *) rbt_t_next(&trav);
      j++;
    }

    /* Stops the stop-watch. */
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_1);

    /* Verifies that the size of the table is equal to the count of inserted elements. */
    op_final_count = rbt_count(table);
    g_assert(op_final_count == len);

    /* Computes the time taken. */
    ret = timespec_diff(&time_diff, &time_0, &time_1);
    g_assert(!ret);

    fprintf(fp, "%s;%s;%zu;%zu;%zu;%ld;%ld\n",
            ltime_to_s,
            op_type,
            len,
            op_initial_count,
            op_final_count,
            time_diff.tv_sec,
            time_diff.tv_nsec);

    free(index);

    /* End of operation 6. */


    /* Frees the table. */
    rbt_destroy(table, NULL);

    /* Frees the data array. */
    free(data);

  } /* End of steps loop. */

  /* Closes the log file. */
  int fclose_ret = fclose(fp);
  g_assert(fclose_ret == 0);
}


/*
 * Internal functions.
 */

static int
compare_int (const void *item_a,
             const void *item_b,
             void *param)
{
  assert(item_a);
  assert(item_b);
  const int *a = (int *) item_a;
  const int *b = (int *) item_b;
  return (*a > *b) - (*a < *b);
}

static int *
prepare_data_array (const size_t len,
                    const int seed)
{
  int *a = (int *) malloc(len * sizeof(int));
  g_assert(a);

  for (size_t i = 0; i < len; i++) {
    a[i] = i;
  }

  RandomNumberGenerator *rng = rng_new(seed);
  rng_shuffle_array_int(rng, a, len);
  rng_free(rng);

  return a;
}

/*
 * The struct timespec structure represents an elapsed time. It is declared in time.h and has the following members:
 *
 * - time_t     tv_sec    This represents the number of whole seconds of elapsed time.
 * - long int   tv_nsec   This is the rest of the elapsed time (a fraction of a second),
 *                        represented as the number of nanoseconds. It is always less than one billion.
 *
 * The structure is also defined as:
 *
 *    typedef struct timespec timespec_t;
 *
 *
 * A way of obtaining the timespec structure value is:
 *
 *    timespec_t time_0;
 *    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &time_0);
 *
 *
 * The function timespec_diff works as follow:
 *
 *    Subtracts the timespec_t values `start` and `end`,
 *    storing the result in `result`.
 *    Return 1 if the difference is negative, otherwise 0.
 */
int
timespec_diff (timespec_t *const result,
               const timespec_t *const start,
               const timespec_t *const end)
{
  g_assert(result);
  g_assert(start);
  g_assert(end);
  if ((end->tv_sec - start->tv_sec) < 0) return 1;
  if ((end->tv_sec - start->tv_sec) == 0 &&
      (end->tv_nsec - start->tv_nsec) < 0) return 1;

  if ((end->tv_nsec - start->tv_nsec) < 0) {
    result->tv_sec = end->tv_sec - start->tv_sec - 1;
    result->tv_nsec = 1000000000 + end->tv_nsec - start->tv_nsec;
  } else {
    result->tv_sec = end->tv_sec - start->tv_sec;
    result->tv_nsec = end->tv_nsec - start->tv_nsec;
  }
  return 0;
}

int
sort_utils_element_cmp (const void *const a,
                        const void *const b)
{
  const int **const x = (const int **const) a;
  const int **const y = (const int **const) b;
  return (**x > **y) - (**x < **y);
}
