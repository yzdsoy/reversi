#
#  Makefile
#
#  This file is part of the reversi program
#  http://github.com/rcrr/reversi
#
#  Copyright (c) 2013, 2014, 2016 Roberto Corradini. All rights reserved.
#
#  This program is free software; you can redistribute it and/or modify it
#  under the terms of the GNU General Public License as published by the
#  Free Software Foundation; either version 3, or (at your option) any
#  later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
#  or visit the site <http://www.gnu.org/licenses/>.
#

# Add all the programs that has a main and that will be compiled and linked as a bin executable.
MAINS = endgame_solver gpdb_verify dump_bitrow_changes utest read_pve_dump intel_intrinsics_trial \
        read_game_tree_log

# Add all the test programs that has a main and that will be compiled and linked as a bin executable.
TEST_PROGS = bit_works_test prng_test sort_utils_test red_black_tree_test board_test game_position_db_test game_position_test \
             game_tree_utils_test endgame_solver_test

UTEST_PROGS = utest_test llist_test

#
# No changes from here to the end, unless a structurar change of the build process happens.
#

#
# Add -pg to CFLAGS and DFLAGS to profile.

CC = gcc 
#CFLAGS = -std=c99 -pedantic -Wall -g -O3 `pkg-config --cflags glib-2.0` -D_POSIX_SOURCE
#LDFLAGS =
# add -pg for gprof execution.
# add -DG_DISABLE_ASSERT -DNDEBUG to disable assertions
# add -fno-inline to prevent inlining
ARCH_FLAGS = -mpopcnt -mavx2
CFLAGS = -std=c99 -pedantic-errors -Wall -g -O3 `pkg-config --cflags glib-2.0` -D_POSIX_C_SOURCE=200112L $(ARCH_FLAGS) -DG_DISABLE_ASSERT -DNDEBUG
LDFLAGS =
CFLAGS_TEST = -std=c99 -pedantic-errors -Wall -g -O3 `pkg-config --cflags glib-2.0` -D_POSIX_C_SOURCE=200112L
LDFLAGS_TEST =
ASMFLAGS = -std=c99 -pedantic-errors -Wall -O3 -masm=intel `pkg-config --cflags glib-2.0` -D_POSIX_C_SOURCE=200112L $(ARCH_FLAGS) -DG_DISABLE_ASSERT -DNDEBUG
LIBS = `pkg-config --libs glib-2.0` -lm
TEST_LIBS =
SRCDIR = src
TESTDIR = test

BUILDDIR = build
OBJDIR   = $(BUILDDIR)/obj
BINDIR   = $(BUILDDIR)/bin
DOCDIR   = $(BUILDDIR)/doc
BTESTDIR = $(BUILDDIR)/test
ASMDIR   = $(BUILDDIR)/asm

OUTDIR = out

SRCS = $(wildcard $(SRCDIR)/*.c)
HDRS = $(wildcard $(SRCDIR)/*.h)
OBJS = $(addsuffix .o,$(basename $(subst $(SRCDIR),$(OBJDIR),$(SRCS))))
ASMS = $(addsuffix .asm,$(basename $(subst $(SRCDIR),$(ASMDIR),$(SRCS))))
BINS = $(addprefix $(BINDIR)/,$(MAINS))
DEPS = $(addsuffix .d,$(basename $(subst $(SRCDIR),$(OBJDIR),$(SRCS))))

OBJS_WITH_MAIN = $(addsuffix .o,$(addprefix $(OBJDIR)/,$(MAINS)))
OBJS_NO_MAIN = $(filter-out $(OBJS_WITH_MAIN),$(OBJS))

TEST_OBJDIR  = $(BTESTDIR)/obj
TEST_BINDIR  = $(BTESTDIR)/bin
TEST_SRCS    = $(wildcard $(TESTDIR)/*.c)
TEST_OBJS    = $(addsuffix .o,$(basename $(subst $(TESTDIR)/,$(TEST_OBJDIR)/,$(TEST_SRCS))))
TEST_BINS    = $(addprefix $(TEST_BINDIR)/,$(TEST_PROGS))
UTEST_BINS    = $(addprefix $(TEST_BINDIR)/,$(UTEST_PROGS))
TEST_INCLUDE = -I./src

TEST_OBJS_WITH_MAIN = $(addsuffix .o,$(addprefix $(TEST_OBJDIR)/,$(TEST_PROGS)))
UTEST_OBJS_WITH_MAIN = $(addsuffix .o,$(addprefix $(TEST_OBJDIR)/,$(UTEST_PROGS)))

ALL_TEST_OBJS_WITH_MAIN = $(TEST_OBJS_WITH_MAIN) $(UTEST_OBJS_WITH_MAIN)
TEST_OBJS_NO_MAIN = $(filter-out $(ALL_TEST_OBJS_WITH_MAIN),$(TEST_OBJS))

#.PHONY: deps
#deps: $(DEPS)
#
#$(DEPS): $(OBJDIR)/%.d: $(SRCDIR)/%.c | $(OBJDIR)
#	$(CC) $(ARCH_FLAGS) -MM $< -o $@
#
#include $(DEPS)

all: $(BINS) $(TEST_BINS) $(UTEST_BINS) $(OUTDIR)

$(BINS): $(OBJS) | $(BINDIR)
	$(eval OBJECT = $(addsuffix .o,$(basename $(subst $(BINDIR),$(OBJDIR),$@))))
	$(CC) $(LDFLAGS) $(OBJECT) $(OBJS_NO_MAIN) $(LIBS) -o $@

$(OBJS): $(OBJDIR)/%.o: $(SRCDIR)/%.c $(HDRS) Makefile | $(OBJDIR)
	$(CC) $(CFLAGS) -c $< -o $@

.PHONY: asm
asm: $(ASMS)

$(ASMS): $(ASMDIR)/%.asm: $(SRCDIR)/%.c $(HDRS) | $(ASMDIR)
	$(CC) $(ASMFLAGS) -S $< -o $@

.PHONY: test_obj
test_obj: $(TEST_OBJS)

$(TEST_OBJS): $(TEST_OBJDIR)/%.o: $(TESTDIR)/%.c | $(TEST_OBJDIR)
	$(CC) $(CFLAGS_TEST) -c $< -o $@ $(TEST_INCLUDE)

.PHONY: test_bin
test_bin: $(OBJS) $(TEST_OBJS) $(TEST_BINS)

$(TEST_BINS): $(TEST_OBJS) $(OBJS) | $(TEST_BINDIR)
	$(eval OBJECT = $(addsuffix .o,$(basename $(subst $(TEST_BINDIR),$(TEST_OBJDIR),$@))))
	$(CC) $(LDFLAGS_TEST) $(OBJECT) $(TEST_OBJS_NO_MAIN) $(OBJS_NO_MAIN) $(LIBS) $(TEST_LIBS) -o $@

.PHONY: test_bin
utest_bin: $(OBJS) $(TEST_OBJS) $(UTEST_BINS)

$(UTEST_BINS): $(TEST_OBJS) $(OBJS) | $(TEST_BINDIR)
	$(eval OBJECT = $(addsuffix .o,$(basename $(subst $(TEST_BINDIR),$(TEST_OBJDIR),$@))))
	$(CC) $(LDFLAGS_TEST) $(OBJECT) $(TEST_OBJS_NO_MAIN) $(OBJS_NO_MAIN) $(LIBS) $(TEST_LIBS) -o $@

.PHONY: test
test: $(TEST_BINS)
	gtester --verbose -k -m=quick -o $(BTESTDIR)/gtester-log.xml $(TEST_BINS)

.PHONY: test_slow
test_slow: $(TEST_BINS)
	gtester --verbose -k -m=slow -o $(BTESTDIR)/gtester-log.xml $(TEST_BINS)

.PHONY: test_perf
test_perf: $(TEST_BINS)
	gtester --verbose -k -m=perf -o $(BTESTDIR)/gtester-log.xml $(TEST_BINS)

.PHONY: test_report
test_report: $(BTESTDIR)/gtester-log.html

.PHONY: utest
utest: $(UTEST_BINS)
	./build/bin/utest --verbose -s=/utest/fail -s=/utest/abort -s=/utest/exit -s=/utest/assert_false $(UTEST_BINS)

$(BTESTDIR)/gtester-log.html: $(BTESTDIR)/gtester-log.xml
	gtester-report $(BTESTDIR)/gtester-log.xml > $(BTESTDIR)/gtester-log.html

.PHONY: $(BUILDDIR)
$(BUILDDIR): $(BUILDDIR)/.dir_sentinel

$(BUILDDIR)/.dir_sentinel:
	mkdir -p $(BUILDDIR)
	touch $(BUILDDIR)/.dir_sentinel

.PHONY: $(OUTDIR)
$(OUTDIR): $(OUTDIR)/.dir_sentinel

$(OUTDIR)/.dir_sentinel:
	mkdir -p $(OUTDIR)
	touch $(OUTDIR)/.dir_sentinel


.PHONY: $(OBJDIR)
$(OBJDIR): $(OBJDIR)/.dir_sentinel | $(BUILDDIR)

$(OBJDIR)/.dir_sentinel:
	mkdir -p $(OBJDIR)
	touch $(OBJDIR)/.dir_sentinel


.PHONY: $(BINDIR)
$(BINDIR): $(BINDIR)/.dir_sentinel | $(BUILDDIR)

$(BINDIR)/.dir_sentinel:
	mkdir -p $(BINDIR)
	touch $(BINDIR)/.dir_sentinel


.PHONY: $(DOCDIR)
$(DOCDIR): $(DOCDIR)/.dir_sentinel | $(BUILDDIR)

$(DOCDIR)/.dir_sentinel:
	mkdir -p $(DOCDIR)
	touch $(DOCDIR)/.dir_sentinel


.PHONY: $(ASMDIR)
$(ASMDIR): $(ASMDIR)/.dir_sentinel | $(BUILDDIR)

$(ASMDIR)/.dir_sentinel:
	mkdir -p $(ASMDIR)
	touch $(ASMDIR)/.dir_sentinel


.PHONY: $(BTESTDIR)
$(BTESTDIR): $(BTESTDIR)/.dir_sentinel | $(BUILDDIR)

$(BTESTDIR)/.dir_sentinel:
	mkdir -p $(BTESTDIR)
	touch $(BTESTDIR)/.dir_sentinel


.PHONY: $(TEST_OBJDIR)
$(TEST_OBJDIR): $(TEST_OBJDIR)/.dir_sentinel | $(BTESTDIR)

$(TEST_OBJDIR)/.dir_sentinel:
	mkdir -p $(TEST_OBJDIR)
	touch $(TEST_OBJDIR)/.dir_sentinel


.PHONY: $(TEST_BINDIR)
$(TEST_BINDIR): $(TEST_BINDIR)/.dir_sentinel | $(BTESTDIR)

$(TEST_BINDIR)/.dir_sentinel:
	mkdir -p $(TEST_BINDIR)
	touch $(TEST_BINDIR)/.dir_sentinel


.PHONY: doxygen
doxygen: $(DOCDIR)
	doxygen > build/doxygen.log

.PHONY: doxygen_latex
doxygen_latex: $(DOCDIR)
	(cd build/doc/latex; make >make-stdout_and_stderr.log 2>&1)


ABC_DIR = build/abc

.PHONY: $(ABC_DIR)
$(ABC_DIR): $(BUILDDIR)
	@if test ! -d $(ABC_DIR); then \
	  echo "ABC_DIR = " $(ABC_DIR) " does not exist."; \
	  mkdir -p $(ABC_DIR); \
	else \
	  echo "ABC_DIR = " $(ABC_DIR) " does DO  exist."; \
	fi

ZZZ_DIR = build/zzz

.PHONY: $(ZZZ_DIR)
$(ZZZ_DIR): $(ZZZ_DIR)/.dir_sentinel

$(ZZZ_DIR)/.dir_sentinel:
	mkdir -p $(ZZZ_DIR)
	touch $(ZZZ_DIR)/.dir_sentinel

.PHONY: clean
clean:
	rm -Rf $(BUILDDIR)

.PHONY: clean_out
clean_out:
	rm -Rf $(OUTDIR)

.PHONY: info
info:
	@echo "SRCS = "$(SRCS)
	@echo "HDRS = "$(HDRS)
	@echo "OBJS = "$(OBJS)
	@echo "BINS = "$(BINS)
	@echo "DEPS = "$(DEPS)
	@echo "OBJS_WITH_MAIN = "$(OBJS_WITH_MAIN)
	@echo "OBJS_NO_MAIN = "$(OBJS_NO_MAIN)
	@echo "TEST_SRCS = "$(TEST_SRCS)
	@echo "TEST_OBJS = "$(TEST_OBJS)
	@echo "TEST_BINS = "$(TEST_BINS)
	@echo "UTEST_BINS = "$(UTEST_BINS)
	@echo "TEST_OBJDIR = "$(TEST_OBJDIR)
	@echo "TESTDIR = "$(TESTDIR)
	@echo "TEST_INCLUDE = "$(TEST_INCLUDE)
	@echo "CFLAGS = "$(CFLAGS)
	@echo "UTEST_OBJS_WITH_MAIN = "$(UTEST_OBJS_WITH_MAIN)
	@echo "TEST_OBJS_WITH_MAIN = "$(TEST_OBJS_WITH_MAIN)
	@echo "TEST_OBJS_NO_MAIN = "$(TEST_OBJS_NO_MAIN)



ENDGAME_LOG_DIR = $(BUILDDIR)/out

.PHONY: $(ENDGAME_LOG_DIR)
$(ENDGAME_LOG_DIR): $(ENDGAME_LOG_DIR)/.dir_sentinel | $(BUILDDIR)

$(ENDGAME_LOG_DIR)/.dir_sentinel:
	mkdir -p $(ENDGAME_LOG_DIR)
	touch $(ENDGAME_LOG_DIR)/.dir_sentinel

.PHONY: endgame_log_files
endgame_log_files: $(ENDGAME_LOG_DIR)
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt -q ffo-01 -s es -l $(ENDGAME_LOG_DIR)/exact_solver_log-ffo-01
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt -q ffo-01 -s ifes -l $(ENDGAME_LOG_DIR)/ifes_solver_log-ffo-01
	./$(BINDIR)/endgame_solver -f db/gpdb-sample-games.txt -q initial -s rand -n 100 -l $(ENDGAME_LOG_DIR)/random_game_sampler_log-t100
	./$(BINDIR)/endgame_solver -f db/gpdb-sample-games.txt -q ffo-01-simplified-4 -s minimax -l $(ENDGAME_LOG_DIR)/minimax_log-ffo-01-simplified-4
	./$(BINDIR)/endgame_solver -f db/gpdb-sample-games.txt -q ffo-01-simplified-4 -s rab -n 3 -l $(ENDGAME_LOG_DIR)/rab_solver_log-ffo-01-simplified-4_n3
	./$(BINDIR)/endgame_solver -f db/gpdb-sample-games.txt -q ffo-01-simplified-4 -s ab -l $(ENDGAME_LOG_DIR)/ab_solver_log-ffo-01-simplified-4

.PHONY: endgame_pv_files
endgame_pv_files: $(ENDGAME_LOG_DIR)
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-01 -d $(ENDGAME_LOG_DIR)/pve-ffo-01.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-02 -d $(ENDGAME_LOG_DIR)/pve-ffo-02.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-03 -d $(ENDGAME_LOG_DIR)/pve-ffo-03.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-04 -d $(ENDGAME_LOG_DIR)/pve-ffo-04.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-05 -d $(ENDGAME_LOG_DIR)/pve-ffo-05.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-06 -d $(ENDGAME_LOG_DIR)/pve-ffo-06.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-07 -d $(ENDGAME_LOG_DIR)/pve-ffo-07.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-08 -d $(ENDGAME_LOG_DIR)/pve-ffo-08.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-09 -d $(ENDGAME_LOG_DIR)/pve-ffo-09.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-10 -d $(ENDGAME_LOG_DIR)/pve-ffo-10.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-11 -d $(ENDGAME_LOG_DIR)/pve-ffo-11.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-12 -d $(ENDGAME_LOG_DIR)/pve-ffo-12.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-13 -d $(ENDGAME_LOG_DIR)/pve-ffo-13.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-14 -d $(ENDGAME_LOG_DIR)/pve-ffo-14.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-15 -d $(ENDGAME_LOG_DIR)/pve-ffo-15.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-16 -d $(ENDGAME_LOG_DIR)/pve-ffo-16.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-17 -d $(ENDGAME_LOG_DIR)/pve-ffo-17.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-18 -d $(ENDGAME_LOG_DIR)/pve-ffo-18.dat
	./$(BINDIR)/endgame_solver -f db/gpdb-ffo.txt --pv-full-rec --pv-no-print -s es -q ffo-19 -d $(ENDGAME_LOG_DIR)/pve-ffo-19.dat

# _2 has to be removed ....
.PHONY: dat2csv 
dat2csv:
	./$(BINDIR)/read_game_tree_log -f $(ENDGAME_LOG_DIR)/ab_solver_log-ffo-01-simplified-4_h.dat >     $(ENDGAME_LOG_DIR)/ab_solver_log-ffo-01-simplified-4_h.csv
	./$(BINDIR)/read_game_tree_log -f $(ENDGAME_LOG_DIR)/exact_solver_log-ffo-01_h.dat >               $(ENDGAME_LOG_DIR)/exact_solver_log-ffo-01_h.csv
	./$(BINDIR)/read_game_tree_log -f $(ENDGAME_LOG_DIR)/ifes_solver_log-ffo-01_h.dat >                $(ENDGAME_LOG_DIR)/ifes_solver_log-ffo-01_h.csv
	./$(BINDIR)/read_game_tree_log -f $(ENDGAME_LOG_DIR)/minimax_log-ffo-01-simplified-4_h.dat >       $(ENDGAME_LOG_DIR)/minimax_log-ffo-01-simplified-4_h.csv
	./$(BINDIR)/read_game_tree_log -f $(ENDGAME_LOG_DIR)/rab_solver_log-ffo-01-simplified-4_n3_h.dat > $(ENDGAME_LOG_DIR)/rab_solver_log-ffo-01-simplified-4_n3_h.csv
	./$(BINDIR)/read_game_tree_log -f $(ENDGAME_LOG_DIR)/random_game_sampler_log-t100_h.dat >          $(ENDGAME_LOG_DIR)/random_game_sampler_log-t100_h.csv
