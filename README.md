# CompilHER

A Java to x86-64 compiler built from scratch using Flex, Bison, and C++. Takes a subset of Java 17 as input and produces native x86-64 assembly that can be assembled and executed on Linux.

---

## Overview

compilHER implements a complete compilation pipeline — from lexical analysis all the way to executable x86-64 assembly. The compiler was built in four milestones, each adding a new stage to the pipeline.

```
Java Source (.java)
      |
      v
  Lexer (Flex)
      |
      v
  Parser (Bison)
      |
      v
  AST Construction
      |
      v
  Semantic Analysis + Symbol Table
      |
      v
  Three-Address Code (3AC IR)
      |
      v
  x86-64 Assembly Generation
      |
      v
Executable Binary
```

---

## Project Structure

```
compilHER/
├── include/
│   ├── global_vars.hpp       # Global variables and extern declarations
│   ├── node.hpp              # AST node structure and methods
│   ├── symbol_table.hpp      # Symbol table structures
│   ├── tac.hpp               # Three-address code (quad) structure
│   └── x86.hpp               # x86-64 code generation structures
│
├── src/
│   ├── lexer.l               # Flex lexer for Java tokens
│   ├── semaction.l           # Flex file that generates parser actions
│   ├── parser_empty.y        # Bison grammar (without semantic actions)
│   ├── main.cpp              # Entry point and CLI argument handling
│   ├── node.cpp              # AST node methods implementation
│   ├── symbol_table.cpp      # Symbol table implementation
│   ├── tac.cpp               # 3AC generation and optimization
│   ├── x86.cpp               # x86-64 assembly generation
│   ├── allocmem.s            # Assembly routine for heap allocation (malloc wrapper)
│   ├── print_func.s          # Assembly routine for println
│   └── Makefile
│
├── tests/
│   ├── test_1.java           # Knapsack (recursion, arrays)
│   ├── test_2.java           # Fibonacci (tail recursion)
│   ├── test_3.java           # 100th prime number
│   ├── test_4.java           # Memoized Fibonacci (classes, objects)
│   ├── test_5.java           # Factorial (basic recursion)
│   ├── test_6.java           # Array sum (loops, arrays)
│   ├── test_7.java           # Binary search
│   ├── test_8.java           # Method overloading
│   ├── test_9.java           # Bubble sort
│   ├── test_10.java          # Quick sort
│   └── test_11.java          # Classes and objects
│
├── scripts/
│   ├── compile.sh            # Builds the compiler from source
│   ├── run.sh                # Compiles and runs a single test
│   └── script.sh             # Batch runs all tests
│
└── out/
    ├── tac_*.txt             # Generated 3AC output per test
    ├── asm_*.s               # Generated assembly per test
    ├── ast_*.png             # Generated AST visualization per test
    └── exec_*                # Compiled executables per test
```

---

## Building the Compiler

### Prerequisites

| Tool | Version | Purpose |
|------|---------|---------|
| `flex` | >= 2.6 | Lexical analysis |
| `bison` | >= 3.0 | Parsing |
| `g++` | >= 9.0 | Compiling the compiler |
| `gcc` | >= 9.0 | Assembling generated code |
| `graphviz` | any | AST visualization |

Install on Ubuntu/Debian:

```bash
sudo apt install flex bison gcc g++ graphviz
```

### Build

```bash
cd src/
make
```

This runs the following steps internally:

1. Runs `semaction.l` through Flex to generate `parser.y` with semantic actions
2. Runs `parser.y` through Bison to generate `parser.tab.c` and `parser.tab.h`
3. Runs `lexer.l` through Flex to generate `lex.yy.c`
4. Compiles everything into the `WCTC.o` binary

---

## Usage

```
./WCTC.o [options]

Options:
  -i, --input   <file>    Input Java source file
  -o, --output  <file>    Output DOT file for AST (default: tree.gv)
  -t, --taco    <file>    Output file for 3AC (default: tac.txt)
  -s, --asm     <file>    Output file for x86-64 assembly (default: asm.s)
  -v, --verbose           Print the full parse tree to stdout
  -h, --help              Show this help message
```

### Example

```bash
# Compile a Java file
./WCTC.o -i ../tests/test_5.java -t ../out/tac_5.txt -s ../out/asm_5.s

# Generate AST image
dot -Tpng tree.gv -o ../out/ast_5.png

# Assemble and link
gcc -c ../out/asm_5.s -o ../out/obj_5.o
gcc ../out/obj_5.o -o ../out/exec_5

# Run
../out/exec_5
```

---

## Running All Tests

```bash
cd scripts/
bash script.sh
# Enter number of tests when prompted (1-9)
```

Or manually:

```bash
cd src/
for i in $(seq 1 9); do
    ./WCTC.o -i ../tests/test_$i.java -t ../out/tac_$i.txt -s ../out/asm_$i.s
    dot -Tpng tree.gv -o ../out/ast_$i.png
    gcc -c ../out/asm_$i.s -o ../out/obj_$i.o
    gcc ../out/obj_$i.o -o ../out/exec_$i
    echo "test_$i: $(../out/exec_$i)"
done
```

---

## Test Cases

| Test | Program | Expected Output |
|------|---------|----------------|
| test_1 | 0/1 Knapsack (W=50) | 220 |
| test_2 | Fibonacci (n=10) | 55 |
| test_3 | 100th prime number | 541 |
| test_4 | Memoized Fibonacci (n=10) | 55, 19 |
| test_5 | Factorial (n=5) | 120 |
| test_6 | Sum of array [0,1,4,9,16] | 30 |
| test_7 | Binary search (found, not found) | 2, -1 |
| test_8 | Method overloading | 10, 200 |
| test_9 | Bubble sort (9 elements) | sorted array |

---

## Supported Language Features

### Data Types

| Type | Supported |
|------|-----------|
| `int` | Yes |
| `long` | Yes |
| `float` | Yes |
| `double` | Yes |
| `boolean` | Yes |
| `char` | Partial |
| `String` | No |

### Language Constructs

| Feature | Supported |
|---------|-----------|
| Arithmetic operators | Yes |
| Relational operators | Yes |
| Logical operators | Yes |
| Bitwise operators | Yes |
| Assignment operators | Yes |
| Ternary operator | Yes |
| Pre/post increment/decrement | Yes |
| `if / else` | Yes |
| `for` loop | Yes |
| `while` loop | Yes |
| `do-while` | No |
| `switch` | No |
| `break / continue` | Yes |
| `return` | Yes |
| Static methods | Yes |
| Non-static methods | Yes |
| Recursion | Yes |
| Method overloading | Yes |
| Classes and objects | Yes |
| `new` (heap allocation) | Yes |
| 1D / 2D / 3D arrays | Yes |
| `System.out.println()` | Yes (primitives only) |
| `import` statements | No |
| Generics | No |
| Interfaces | No |
| Exception handling | No |
| Inheritance | No |

---

## Compiler Internals

### Grammar

| Metric | Count |
|--------|-------|
| AST node types (non-terminals) | 139 |
| Total grammar rules | 340 |
| Terminal tokens | ~80 |

### 3AC Instruction Types

| Instruction | Description |
|-------------|-------------|
| `begin_func` | Start of a function |
| `end_func` | End of a function |
| `push_param` | Push a parameter onto the stack |
| `pop_param` | Pop a parameter from the stack |
| `call_func` | Call a function |
| `return` | Return from a function |
| `return_value` | Retrieve return value |
| `shift_pointer` | Adjust stack pointer |
| `if_true / if_false` | Conditional jump |
| `goto` | Unconditional jump |
| Binary ops | `+`, `-`, `*`, `/`, `%`, etc. |
| Unary ops | `!`, `-`, `~` |
| Cast | `(type) expr` |
| Load / Store | Array and pointer access |

### Optimizations

The compiler applies the following optimizations on the 3AC before code generation:

| Optimization | Description |
|-------------|-------------|
| Redundant temporary elimination | Removes unnecessary temporaries |
| Constant folding | Evaluates constant expressions at compile time |
| Constant propagation | Replaces variables with known constant values |
| Temporary renaming | Renames temporaries sequentially for clarity |

### Memory Model

| Storage | Location |
|---------|----------|
| Local variables | Stack (rbp-relative) |
| Function parameters | Stack (pushed by caller) |
| Arrays (`new int[]`) | Heap via `malloc` |
| Objects (`new Class()`) | Heap via `malloc` |
| Return values | `%rax` register |

---

## Known Limitations

- `String` type is not supported
- `import` statements are ignored
- Multiple classes in a single file may cause issues
- `do-while` and `switch` are not supported
- Floating-point arithmetic in x86-64 generation is limited
- Complex expressions with many temporaries may cause register pressure

---

## Tools Used

| Tool | Purpose |
|------|---------|
| Flex 2.6 | Lexical analysis |
| Bison 3.x | LALR(1) parsing |
| C++17 | Compiler implementation |
| Graphviz | AST visualization |
| GAS (GNU Assembler) | Assembling generated x86-64 |
| GCC | Linking final executable |

---

## Authors

Adnan Khan
