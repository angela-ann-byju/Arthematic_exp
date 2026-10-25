ARITHMETIC EXPRESSION MINI COMPILER — MEMBER 4 AST + SEMANTIC CHECKS

Team module names:
- Member 2: lexer.c / lexer.h (based on lexicalanalyser.c)
- Member 3: parser.c / parser.h (grammar and syntax parsing; extended to build AST)
- Member 4: ast.c / ast.h (AST and semantic checks)
- main.c: one entry point for the integrated program

Compile (GCC):
    gcc main.c lexer.c parser.c ast.c -o mini_compiler

Run:
    ./mini_compiler

Windows:
    gcc main.c lexer.c parser.c ast.c -o mini_compiler.exe
    mini_compiler.exe

Examples:
1) a+b*c
   Should pass. The AST shows + at the top, with * as the right child.

2) 12/(3-3)
   Syntax passes, but the semantic check only detects division by a literal zero,
   not division by an expression that evaluates to zero. This example therefore
   passes the simple literal-zero check.

3) a/0
   Syntax passes; semantic check reports division by literal zero.

4) (a+2
   Syntax error: missing closing parenthesis.

5) a+*b
   Syntax error: an operand is expected after +.

Semantic-check note:
This grammar accepts expressions containing identifiers but has no variable declarations
or symbol table. Therefore, it cannot determine whether an identifier such as "total"
has been declared. The code validates identifier spelling and checks division by a
numeric literal zero. Detecting undeclared identifiers would require adding a symbol
table or a list of declared variables.

Integration note:
The uploaded lexicalanalyser.c contains its own main(). Do not compile that file
alongside main.c. Use the supplied lexer.c version, where the lexer logic is retained
and the interactive main() is moved to main.c. Keep the original file as Member 2's
standalone version if required by your team.
