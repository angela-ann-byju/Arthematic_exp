
#include <stdio.h>
#include <ctype.h>
#include <string.h>

#define MAX 512

char input[MAX];
int pos = 0;

typedef enum {
    TOK_ID,
    TOK_NUM,
    TOK_PLUS,
    TOK_MINUS,
    TOK_MUL,
    TOK_DIV,
    TOK_LPAREN,
    TOK_RPAREN,
    TOK_INVALID,
    TOK_END
} TokenType;

typedef struct {
    TokenType type;
    char lexeme[100];
    int position;
} Token;

const char *token_name(TokenType type)
{
    switch (type) {
        case TOK_ID:     return "IDENTIFIER";
        case TOK_NUM:    return "NUMBER";
        case TOK_PLUS:   return "PLUS";
        case TOK_MINUS:  return "MINUS";
        case TOK_MUL:    return "MULTIPLY";
        case TOK_DIV:    return "DIVIDE";
        case TOK_LPAREN: return "LEFT_PAREN";
        case TOK_RPAREN: return "RIGHT_PAREN";
        case TOK_INVALID:return "INVALID";
        case TOK_END:    return "END";
        default:         return "UNKNOWN";
    }
}

Token next_token(void)
{
    Token t;
    int start, len, dot = 0;

    while (isspace((unsigned char)input[pos]))
        pos++;

    t.position = pos;
    t.lexeme[0] = '\0';

    if (input[pos] == '\0') {
        t.type = TOK_END;
        strcpy(t.lexeme, "END");
        return t;
    }

    start = pos;

    /* Recognize identifiers */
    if (isalpha((unsigned char)input[pos]) ||
        input[pos] == '_') {

        while (isalnum((unsigned char)input[pos]) ||
               input[pos] == '_')
            pos++;

        t.type = TOK_ID;
    }
    else if (isdigit((unsigned char)input[pos]) ||
             (input[pos] == '.' &&
              isdigit((unsigned char)input[pos + 1]))) {

        while (isdigit((unsigned char)input[pos]) ||
               input[pos] == '.') {
            if (input[pos] == '.') {
                if (dot) {
                    pos++;
                    t.type = TOK_INVALID;
                    goto save_lexeme;
                }
                dot = 1;
            }
            pos++;
        }

        t.type = TOK_NUM;
    }

    
    else {
        switch (input[pos]) {
            case '+': t.type = TOK_PLUS;   break;
            case '-': t.type = TOK_MINUS;  break;
            case '*': t.type = TOK_MUL;    break;
            case '/': t.type = TOK_DIV;    break;
            case '(': t.type = TOK_LPAREN; break;
            case ')': t.type = TOK_RPAREN; break;
            default:  t.type = TOK_INVALID;break;
        }
        pos++;
    }

save_lexeme:
    len = pos - start;

    if (len >= (int)sizeof(t.lexeme))
        len = sizeof(t.lexeme) - 1;

    strncpy(t.lexeme, input + start, len);
    t.lexeme[len] = '\0';

    return t;
}

int main(void)
{
    Token t;
    int errors = 0;

    printf("====================================\n");
    printf("   ARITHMETIC EXPRESSION LEXER\n");
    printf("====================================\n");

    printf("Enter an arithmetic expression: ");

    if (fgets(input, sizeof(input), stdin) == NULL)
        return 1;

    input[strcspn(input, "\n")] = '\0';

    printf("\n%-15s %-15s %-10s\n",
           "LEXEME", "TOKEN TYPE", "POSITION");
    printf("--------------------------------------------\n");

    while (1) {
        t = next_token();

        if (t.type == TOK_END)
            break;

        printf("%-15s %-15s %-10d\n",
               t.lexeme, token_name(t.type), t.position);

        if (t.type == TOK_INVALID) {
            printf("Lexical error: unsupported or invalid token '%s'\n",
                   t.lexeme);
            errors++;
        }
    }

    printf("--------------------------------------------\n");

    if (errors == 0)
        printf("Lexical analysis completed successfully.\n");
    else
        printf("Lexical analysis completed with %d error(s).\n",
               errors);

    return 0;
}
