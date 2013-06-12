
/**
 * CS 3210 Assignment 1 Main.java Purpose: This program parses through a file to
 * determine if it is consistent with the language in the handout. I followed
 * the suggestion to use the variables on the left as methods.
 *
 * @author David Firestone
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static Scanner inFromUser, inFromFile;
    public static PrintWriter outToFile;
    public static String token;

    public static void main(String[] args) {
        program();
        outToFile.close();
    }

    /**
     * This is the start of the language.
     */
    public static void program() {
        inFromUser = new Scanner(System.in);

        //used for reading just in from System.in
        //outToFile = new PrintWriter(System.out);
        
        //Used when reading from a file
        System.out.println("Please enter file to be parsed");
        //inFromFile = new Scanner(System.in);
        File file = new File(inFromUser.nextLine());
        try {

            inFromFile = new Scanner(file); 
            //For testing purposes 
            //outToFile = new PrintWriter("ouputFor-" + file.getName()); 
            outToFile = new PrintWriter(System.out);
        } catch (FileNotFoundException ex) {
            System.err.println("File not found. Program exiting");
            System.exit(1);
        }
        //end read from file area

        statementList();
    }

    /**
     * StatementList is a list keeps reading from the file until the file has
     * been fully read trough. You will notice that the ending words for the
     * statements are here. They had been in statements but this was causing
     * extra layers of recursion to remain.
     */
    public static void statementList() {
        outToFile.println("StatementList found");
        while (inFromFile.hasNext()) {
            token = inFromFile.next();
            if (token.equals("rof")) {
                outToFile.println("End for loop found");
                break;
            }
            if (token.equals("elihw")) {
                outToFile.println("End while loop found");
                break;
            }
            if (token.equals("fi")) {
                outToFile.println("End if statement found");
                break;
            }
            statement();
        }
    }

    /**
     * Statement is a list of the different statements that can be found. The
     * method looks at the current token to determine which of the statements is
     * being called. When none of the other statements are called we assume that
     * it is an expression statement.
     */
    public static void statement() {
        boolean assinment = true;
        outToFile.println("Statment found");
        if (token.equals("read")) {
            outToFile.println("read statement found");
            token = inFromFile.next();
            if (!ID()) {
                outToFile.println(token + "Is not a valid ID");
                outToFile.println("Program exiting");
                outToFile.close();
                System.exit(1);
            }
            assinment = false;
        }
        if (token.equals("write")) {
            outToFile.println("write statement found");
            token = inFromFile.next();
            if (!ID()) {
                outToFile.println(token + "Is not a valid ID");
                outToFile.println("Program exiting");
                outToFile.close();
                System.exit(1);
            }
            assinment = false;
        }
        if (token.equals("for")) {
            outToFile.println("for loop found");
            forLoop();
            assinment = false;
        }
        if (token.equals("while")) {
            outToFile.println("while loop found");
            whileLoop();
            assinment = false;
        }

        if (token.equals("if")) {
            outToFile.println("if statement found");
            conditional();
            assinment = false;
        }
        if (assinment) {
            outToFile.println("assinment statement found");
            // token = inFromFile.next();
            if (!ID()) {
                outToFile.println(token + "Is not a valid ID");
                outToFile.println("Program exiting");
                outToFile.close();
                System.exit(1);
            }
            //next token should be := in the assinment statment
            if (inFromFile.next().equals(":=")) {
                expression();
            } else {
                outToFile.println("Bad assinment statement.\nProgram exiting");
                outToFile.close();
                System.exit(1);
            }
        }
    }

    /**
     * Expression is defined as a term followed by a operation followed by
     * another term.
     */
    public static void expression() {
        outToFile.println("Expression statement found");
        term();
        operation();
        term();
    }

    /**
     * A term is defined as a ID OR number. So we use the if statement to make
     * sure that at the token is either a ID or a number. If the token is
     * neither a ID or number then a error message is printed out and the
     * program exits.
     */
    public static void term() {
        outToFile.println("Term statement found");
        //ID();
        //number();
        token = inFromFile.next();
        if (!ID() && !number()) {
            outToFile.println(token + "Is not a valid ID or number");
            outToFile.println("Program exiting");
            outToFile.close();
            System.exit(1);
        }
    }

    /**
     * An ID is defined as any token with letters from a to z both capital and
     * lower case letters. This method doesn't end the program but rather any
     * method that calls it will determine if closing the program is needed.
     *
     * @return true if the token is from a-z small or capital. False otherwise.
     */
    public static boolean ID() {
        if (!token.matches("[a-zA-Z]+")) {
            outToFile.println("Not a valid ID");
            return false;
        }
        outToFile.println("ID Found: " + token);
        return true;
    }

    /**
     * A number is defined as any token with numbers from 0 to 9 . This method
     * doesn't end the program but rather any method that calls it will
     * determine if closing the program is needed.
     *
     * @return true if the token is from 0-9. False otherwise.
     */
    public static boolean number() {
        if (!token.matches("[0-9]+")) {
            outToFile.println("Not a vaild number");
            return false;
        }
        outToFile.println("Number Found: " + token);
        return true;
    }

    /**
     * Operation is any of the +, -, /, or * symbols. Nothing needs a operation
     * or something else so it handles its own invalid operation procedure.
     */
    public static void operation() {
        token = inFromFile.next();
        if (!token.equals("+") && !token.equals("-") && !token.equals("/") && !token.equals("*")) {
            outToFile.println("Not a valid operation");
            outToFile.println("Program exiting");
            outToFile.close();
            System.exit(1);
        }
        outToFile.println("Operation Found: " + token);
    }

    /**
     * A for loop is defined as a ID and then to consecutive numbers. Since term
     * and number don't move to the next token the for loop has to call them
     * first. Again since ID and number don't handle bad tokens the forLoop has
     * too.
     */
    public static void forLoop() {
        token = inFromFile.next();
        if (!ID()) {
            outToFile.println(token + "Is not a valid ID");
            outToFile.println("Program exiting");
            outToFile.close();
            System.exit(1);
        }
        token = inFromFile.next();
        if (!number()) {
            outToFile.println(token + "Is not a valid number");
            outToFile.println("Program exiting");
            outToFile.close();
            System.exit(1);
        }
        token = inFromFile.next();
        if (!number()) {
            outToFile.println(token + "Is not a valid number");
            outToFile.println("Program exiting");
            outToFile.close();
            System.exit(1);
        }
        statementList();
    }

    /**
     * A while loop is defined as a condition and a statementList. Because it
     * doesn't have to call ID or number there is no need for error checking.
     */
    public static void whileLoop() {
        condition();
        statementList();
    }

    /**
     * A condition statement is defined as a condition and a statementList.
     * Because it doesn't have to call ID or number there is no need for error
     * checking.
     */
    public static void conditional() {
        condition();
        statementList();
    }

    /**
     * A condition statement is defined as a ID followed by a comparison then by
     * a term. Like the others condition handles the case when false is returned
     * by a terminating method. The non terminal method (term) doesn't need to
     * do error handling since term will handle it.
     */
    public static void condition() {
        outToFile.println("Condition statement found");
        token = inFromFile.next();
        if (!ID()) {
            outToFile.println(token + "Is not a valid ID or number");
            outToFile.println("Program exiting");
            outToFile.close();
            System.exit(1);
        }
        if (!comparison()) {
            outToFile.println("Not a valid comparison");
            outToFile.print("Program exiting");
            outToFile.close();
            System.exit(1);
        }
        term();

    }

    /**
     * Comparison checks if the current token is a ==, !=, <, >, <=, or >=. If
     * the token isn't one of those values it prints a value and returns false.
     * Otherwise it prints the current token and returns true.
     *
     * @return true if the token is the approved symbol. otherwise false.
     */
    public static boolean comparison() {
        token = inFromFile.next();
        if (!token.equals("==") && !token.equals("!=") && !token.equals("<") && !token.equals(">")
                && !token.equals("<=") && !token.equals(">=")) {
            outToFile.println("Not a valid comparison");
            return false;

        }
        outToFile.println("Comparison Found: " + token);
        return true;
    }
    
}

/*
 * Design: I started by following the progression of the program starting at the
 * different statements. I would then follow them down until I reached
 * terminating values for the particular statement. At first I had all the
 * terminating values do all their own error checking. But I figured out that
 * this was a bad idea when working on the term method. If it checked the wrong
 * one first then the program would exit before checking the second one. I used
 * the same approach to discover many other things about where to place them.
 * For instance having the finishing the statements (fi, elihw, rof) needed to
 * be in statementList and not statement because the recursion wouldn't go back
 * to where it left off otherwise. I used this technique throughout the program.
 *
 * Tests:
 *
 * input1.txt while a != b if a > b a := a - b fi if a <= b b := b - a fi elihw
 *
 * StatementList found Statment found while loop found Condition statement found
 * ID Found: a Comparison Found: != Term statement found ID Found: b
 * StatementList found Statment found if statement found Condition statement
 * found ID Found: a Comparison Found: > Term statement found ID Found: b
 * StatementList found Statment found assinment statement found ID Found: a
 * Expression statement found Term statement found ID Found: a Operation Found:
 * - Term statement found ID Found: b End if statement found Statment found if
 * statement found Condition statement found ID Found: a Comparison Found: <=
 * Term statement found ID Found: b StatementList found Statment found assinment
 * statement found ID Found: b Expression statement found Term statement found
 * ID Found: b Operation Found: - Term statement found ID Found: a End if
 * statement found End while loop found
 *
 *
 * input2.txt for apple 5 0 for m 6 3 q := p - 1 rof rof
 *
 * StatementList found Statment found for loop found ID Found: apple Number
 * Found: 5 Number Found: 0 StatementList found Statment found for loop found ID
 * Found: m Number Found: 6 Number Found: 3 StatementList found Statment found
 * assinment statement found ID Found: q Expression statement found Term
 * statement found ID Found: p Operation Found: - Term statement found Not a
 * valid ID Number Found: 1 End for loop found End for loop found
 *
 *
 * input3.txt read hi write bye mokeny := 9 - fish if fight == 5 for i 5 47
 * grr:= monkey + dog while fish != cat write killer elihw rof fi
 *
 * StatementList found Statment found read statement found ID Found: hi Statment
 * found write statement found ID Found: bye Statment found assinment statement
 * found ID Found: mokeny Expression statement found Term statement found Not a
 * valid ID Number Found: 9 Operation Found: - Term statement found ID
 * Found:fish Statment found if statement found Condition statement found ID
 * Found:fight Comparison Found: == Term statement found Not a valid ID Number
 * Found:5 StatementList found Statment found for loop found ID Found: i Number
 * Found:5 Number Found: 47 StatementList found Statment found assinment
 * statement found ID Found: grr Expression statement found Term statement found
 * ID Found: monkey Operation Found: + Term statement found ID Found: dog
 * Statment found while loop found Condition statement found ID Found: fish
 * Comparison Found: != Term statement found ID Found: cat StatementList found
 * Statment found write statement found ID Found: killer End while loop found
 * End for loop found End if statement found
 *
 *
 * input4.txt write hi while 8 == house evil := good + bad elihw
 *
 * StatementList found Statment found write statement found ID Found: hi
 * Statment found while loop found Condition statement found Not a valid ID 8 Is
 * not a valid ID Program exiting
 *
 * Refrances: None used
 */
