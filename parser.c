#include <stdio.h>
#include "lexer.h"
#include "parser.h"

/*
 * MEMBER 3: CFG AND SYNTAX PARSER
 *
 * E  -> T E'
 * E' -> + T E' | - T E' | epsilon
 * T  -> F T'
 * T' -> * F T' | / F T' | epsilon
 * F  -> ( E ) | ID | NUM
 */

static Token current;

/* Read the next token from Member 2's lexer */
static void advance(void)
{
    current = next_token();
}

/* Display syntax errors */
static void syntax_error(const char *message)
{
    printf("Syntax error at position %d: %s",
           current.position, message);

    if (current.type != TOK_END)
        printf(" (found '%s')", current.lexeme);

    printf("\n");
}

static int E(void);
static int E_prime(void);
static int T(void);
static int T_prime(void);
static int F(void);

/* E -> T E' */
static int E(void)
{
    if (!T())
        return 0;

    return E_prime();
}

/* E' -> + T E' | - T E' | epsilon */
static int E_prime(void)
{
    while (current.type == TOK_PLUS ||
           current.type == TOK_MINUS) {
        advance();

        if (!T())
            return 0;
    }

    return 1;
}

/* T -> F T' */
static int T(void)
{
    if (!F())
        return 0;

    return T_prime();
}

/* T' -> * F T' | / F T' | epsilon */
static int T_prime(void)
{
    while (current.type == TOK_MUL ||
           current.type == TOK_DIV) {
        advance();

        if (!F())
            return 0;
    }

    return 1;
}

/* F -> ( E ) | ID | NUM */
static int F(void)
{
    if (current.type == TOK_ID ||
        current.type == TOK_NUM) {
        advance();
        return 1;
    }

    if (current.type == TOK_LPAREN) {
        advance();

        if (!E())
            return 0;

        if (current.type != TOK_RPAREN) {
            syntax_error("Expected closing ')'");
            return 0;
        }

        advance();
        return 1;
    }

    if (current.type == TOK_INVALID) {
        syntax_error("Invalid token");
        return 0;
    }

    syntax_error("Expected identifier, number, or '('");
    return 0;
}

/* Public function called by the main compiler */
int parse_expression(void)
{
    pos = 0;
    advance();

    if (current.type == TOK_END) {
        syntax_error("Expression cannot be empty");
        return 0;
    }

    if (!E())
        return 0;

    if (current.type == TOK_INVALID) {
        syntax_error("Invalid token");
        return 0;
    }

    if (current.type != TOK_END) {
        syntax_error("Unexpected token after expression");
        return 0;
    }

    return 1;
}