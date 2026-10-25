
1. Project Overview
The Arithmetic Expression Compiler is a Compiler Design mini project developed using C. It implements a compiler front end that processes arithmetic expressions and performs lexical analysis, syntax analysis, Abstract Syntax Tree (AST) construction, and semantic checks.

2. Objectives
- Recognize arithmetic expressions containing operators, parentheses, identifiers, and numeric constants.
- Perform lexical and syntax analysis.
- Construct an Abstract Syntax Tree (AST).
- Perform semantic checks and detect errors.
- Validate expressions using valid and invalid test cases.

3. Technologies Used
- Programming Language: C
- Development Environment: Visual Studio Code
- Compiler: GCC
- Version Control: Git and GitHub

4. Project Modules
- Lexical Analyzer: Identifies tokens in the input expression.
- Parser: Checks whether the expression follows the grammar.
- AST Construction: Represents the structure of the parsed expression.
- Semantic Analysis: Performs the semantic checks implemented in the project.
- Main Module: Coordinates the compiler front-end operations.

5. Supported Expressions
The project is designed to process arithmetic expressions using:
- Addition ("+")
- Subtraction ("-")
- Multiplication ("*")
- Division ("/")
- Parentheses
- Identifiers and numeric constants, where supported by the implementation

6. Execution
1. Open the project folder in Visual Studio Code.
2. Open the terminal in the project directory.
3. Compile the source files using the appropriate GCC command for the project.
4. Run the generated executable.
5. Enter an arithmetic expression when prompted.
6. Observe the syntax analysis, AST output, semantic checks, and error messages.

7. Testing
The project is tested using at least five valid and five invalid arithmetic expressions. Actual outputs are recorded and screenshots are maintained in the "screenshots_output" folder.

8. Documentation
The "docs" folder contains the Context-Free Grammar (CFG) and grammar transformation documentation.

9. Repository Structure
- "docs/" – CFG and grammar documentation
- "screenshots_output/" – Test output screenshots
- "member4_ast_semantic_project/" – Project source files and related implementation
- "lexicalanalyser.c" – Lexical analyzer source file
- "parser.c" and "parser.h" – Parser source and header files
- "README.md" – Project overview and instructions

10. Expected Output
For a valid arithmetic expression, the compiler should report successful syntax analysis, display the AST, and perform the implemented semantic checks. Invalid expressions should be rejected with appropriate error messages when detected by the parser.

11. Conclusion
This project demonstrates the fundamental stages of compiler front-end design, including token recognition, parsing, AST construction, and semantic checking, using the C programming language.

12. Team Contributions
The project was developed collaboratively, with each team member responsible for a specific component.
Ann Maria Mathew- Integration Core
Anns Thomas- Lexical Analysis
Angela Ann Byju- Grammar Parser
Ayrin Anna Binu- Semantic Checks
Albert Joseph- Documentation Architecture
Alwin Pramod- Quality Assurance
Each member contributed to their assigned component to support the development, integration, testing, and documentation of the Arithmetic Expression Compiler.
