package TuringMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    static BufferedReader reader;
    static Scanner input;
    static TuringMachine tm;


    // A Turing Machine Class that acts as a Struct in other programming languages
    public static class TuringMachine {
        /*
         *The Turing Machine Formal Definition
         *    A Turing Machine is a 5-tuple (K, Σ, Γ, s, σ), where
         *        K is a finite set of states, not including the halt state h.
         *        Σ is a finite set of input symbols.
         *        Γ is a finite set of tape symbols (Stores machine symbols and internal symbols), where Σ ⊆ Γ.
         *        s is the initial state, where s ∈ K.
         *        σ is the transition function, where σ: K x Γ -> K x (Γ U {<}) x A, where A is a set of Actions A ∈ {L, R, N, Y}.
         *           Actions (A) are:
         *              L :Move the head one step to the left
         *              R :Move the head one step to the right
         *              N :Reject the string
         *              Y :Accept the string
         */

        int numOfStates;
        int numOfAlphabet;
        int numOfMachineAlphabet;
        int numOfTransitions;
        int head;

        String[] statesAlphabet;
        String[] MachineAlphabet;

        String initialState;
        String blankSymbol;

        char[] tape;
        char[][] transitions;
    }

    /**************************************************************************************************/

    static void menu() throws IOException, InterruptedException {
        System.out.print(
                "The Turing Machine Simulator\n" +
                        "Enter the number of states |K|: "
        );
        tm.numOfStates = input.nextInt();

        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

        System.out.print("Enter the number of string(tape) alphabet |Σ|: ");
        tm.numOfAlphabet = input.nextInt();

        tm.statesAlphabet = new String[tm.numOfAlphabet];
        for (int i = 0; i < tm.numOfAlphabet; i++) {
            System.out.print("Enter the " + (i + 1) + "th alphabet: ");
            tm.statesAlphabet[i] = String.valueOf(reader.readLine().charAt(0));
        }

        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

        System.out.print("Enter number of Machine alphabet |Γ|: ");
        tm.numOfMachineAlphabet = input.nextInt();

        tm.MachineAlphabet = new String[tm.numOfMachineAlphabet];
        for (int i = 0; i < tm.numOfMachineAlphabet; i++) {
            System.out.print("Enter the " + (i + 1) + "th alphabet: ");
            tm.MachineAlphabet[i] = String.valueOf(reader.readLine().charAt(0));
        }

        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

        transitionsMenu();

        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

        inputStringMenu();

    }

    /**************************************************************************************************/

    static void transitionsMenu() throws IOException, InterruptedException {
        tm.numOfTransitions = tm.numOfStates * (tm.numOfMachineAlphabet + tm.numOfAlphabet);

        System.out.print(
                "1. Enter number of transitions\n" +
                        "2. Use the number of all possible transitions (" + tm.numOfTransitions + " transitions)\n"
        );

        int choice = input.nextInt();
        switch (choice) {
            case 1:
                do {
                    System.out.print("Enter number of transitions: ");
                    int temp = input.nextInt();
                    if (temp > tm.numOfTransitions) {
                        System.out.println("Error: number of transitions is greater than the number of all possible transitions");

                    } else {
                        tm.numOfTransitions = temp;
                    }
                } while (tm.numOfTransitions > tm.numOfStates * (tm.numOfMachineAlphabet + tm.numOfAlphabet));
            case 2: {
                tm.transitions = new char[tm.numOfTransitions][5];
                System.out.println(
                        "\nnumber of transitions: " + tm.numOfTransitions + "\n" +
                                "\tformat: <state> <input> <next_state> <output> <action>\n" +
                                "\tactions: L: Left, R: Right, N: Reject, Y: Accept\n" +
                                "\tExample: to enter this transition: (s0, a) -> (s1, b, R)\n" +
                                "\tenter this format: 0 a 1 b R"
                );

                int counterY = 0;
                int counterN = 0;

                for (int i = 0; i < (tm.numOfTransitions); i++) {
                    System.out.println("Enter the " + (i + 1) + "th transition: ");
                    tm.transitions[i] = reader.readLine().replaceAll("\\s+", "").toCharArray();
                    if (tm.transitions[i][4] == 'Y') {
                        counterY++;
                    } else if (tm.transitions[i][4] == 'N') {
                        counterN++;
                    }
                }

                //validate transitions
                if (tm.numOfTransitions == 0) {
                    simulate();
                    return;
                }
                if (counterY == 0 && counterN == 0) {
                    System.out.println("Error Message: There must be at least one transition that accepts and one transition that rejects\n" +
                            "Re-enter the transition functions again\n");
                    transitionsMenu();
                    return;
                }

                for (int i = 0; i < tm.numOfTransitions; i++) {
                    if (!isAlphabet(tm.transitions[i][1])) {
                        System.out.println("\nTransition function " + (i + 1) + ": (s" + tm.transitions[i][0] + ", " + tm.transitions[i][1] +
                                ") -> (s" + tm.transitions[i][2] + ", " + tm.transitions[i][3] + ", " + tm.transitions[i][4] + "), is invalid");

                        System.out.println("Error Message: The 'input' symbol '" + tm.transitions[i][1] + "' must be in the defined tape symbols");
                        System.out.println("Alphabet: " + Arrays.toString(tm.statesAlphabet) + Arrays.toString(tm.MachineAlphabet));
                        System.out.println("Re-enter the transition functions again");
                        transitionsMenu();
                        return;
                    } else if (!isAlphabet(tm.transitions[i][3])) {
                        System.out.println("\nTransition function " + (i + 1) + ": (s" + tm.transitions[i][0] + ", " + tm.transitions[i][1] +
                                ") -> (s" + tm.transitions[i][2] + ", " + tm.transitions[i][3] + ", " + tm.transitions[i][4] + "), is invalid");

                        System.out.println("Error Message: The 'output' symbol '" + tm.transitions[i][3] + "' must be in the defined tape symbols");
                        System.out.println("Alphabet: " + Arrays.toString(tm.statesAlphabet) + Arrays.toString(tm.MachineAlphabet));
                        System.out.println("Re-enter the transition functions again");
                        transitionsMenu();
                        return;
                    }
                }

                System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

                for (int i = 0; i < tm.numOfTransitions; i++) {
                    System.out.println("Transition function " + (i + 1) + ": (s" + tm.transitions[i][0] + ", " + tm.transitions[i][1] +
                            ") -> (s" + tm.transitions[i][2] + ", " + tm.transitions[i][3] + ", " + tm.transitions[i][4] + ")");
                }
                break;
            }
            default:
                System.out.println("Error: invalid input");
                transitionsMenu();
                return;
        }
    }

    /**************************************************************************************************/

    static void inputStringMenu() throws IOException, InterruptedException {
        System.out.print("Enter String: ");
        tm.tape = reader.readLine().replaceAll("\\s+", "").toCharArray();
        System.out.print("Enter initial position of the head: ");
        tm.head = input.nextInt();

        while (tm.head < 0 || tm.head >= tm.tape.length) {
            System.out.println("\nError Message: The initial position of the head must be within the input string");
            System.out.println("Re-enter the initial position of the head again: ");
            tm.head = input.nextInt();
        }

        for (int i = 0; i < tm.tape.length; i++) {
            if (!isAlphabet(tm.tape[i])) {
                System.out.println("\nError Message: The input string must contain the defined tape symbols only");
                System.out.println("Alphabet: " + Arrays.toString(tm.statesAlphabet) + Arrays.toString(tm.MachineAlphabet));
                System.out.println("Re-enter the input string again\n");
                inputStringMenu();
                return;
            }
        }

        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

        System.out.println("Tape (^^^ is the current head position): ");
        printTape();

        System.out.println("‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");

        // start the simulation
        System.out.println("Simulation started...");
        simulate();
    }

    /**************************************************************************************************/

    static void simulate() throws InterruptedException {
        try {
            printTape();
            int index = 0;
            while (true) {
                char nextState = tm.transitions[index][2];
                char output = tm.transitions[index][3];
                char action = tm.transitions[index][4];

                switch (action) {
                    case 'L':
                        tm.tape[tm.head] = output;
                        tm.head--;
                        index = findNextStateIndex(nextState, tm.tape[tm.head]);
                        printTape();
                        break;
                    case 'R':
                        tm.tape[tm.head] = output;
                        tm.head++;
                        index = findNextStateIndex(nextState, tm.tape[tm.head]);
                        printTape();
                        break;
                    case 'N':
                        System.out.println("Final String: ");
                        printTape();
                        System.out.println("Location of head: " + tm.head);
                        System.out.println("String is rejected\n");
                        return;
                    case 'Y':
                        System.out.println("Final String: ");
                        printTape();
                        System.out.println("Location of head: " + tm.head);
                        System.out.println("String is accepted\n");
                        return;
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.out.println("Final String: ");
            System.out.println("Location of head: " + tm.head);
            System.out.println("No alphabet defined to do actions\n");
            System.out.println(e.getMessage());
            return;
        }
    }

    /*
     *
     *      Helper Methods
     *
     */

    static boolean isAlphabet(char c) {
        for (int i = 0; i < tm.numOfMachineAlphabet; i++) {
            if (tm.MachineAlphabet[i].charAt(0) == c) {
                return true;
            }
        }
        for (int i = 0; i < tm.numOfAlphabet; i++) {
            if (tm.statesAlphabet[i].charAt(0) == c) {
                return true;
            }
        }
        return false;
    }

    /**************************************************************************************************/

    static int findNextStateIndex(char currentState, char input) {
        for (int i = 0; i < tm.numOfTransitions; i++) {
            if (tm.transitions[i][0] == currentState && tm.transitions[i][1] == input) {
                return i;
            }
        }
        return -1;
    }

    /**************************************************************************************************/

    static void printTape() {
        System.out.println("\nTape:");
        for (int i = 0; i <= tm.tape.length; i++) {
            System.out.print("____");
        }
        System.out.println("_");

        System.out.print("| < ");
        for (int i = 0; i < tm.tape.length; i++) {
            System.out.print("| " + tm.tape[i] + " ");
        }
        System.out.println("|");
        for (int i = 0; i <= tm.tape.length; i++) {
            if (i == tm.head + 1)
                System.out.print("‾^^^‾");
            else
                System.out.print("‾‾‾‾");
        }
        System.out.println();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        // initialize the controls
        input = new Scanner(System.in);
        reader = new BufferedReader(new InputStreamReader(System.in));
        tm = new TuringMachine();
        char choice;

        System.out.println(
                "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n" +
                        "The Turing Machine Formal Definition\n" +
                        "    A Turing Machine is a 5-tuple (K, Σ, Γ, s, σ), where\n" +
                        "        K is a finite set of states, not including the halt state h.\n" +
                        "        Σ is a finite set of input symbols.\n" +
                        "        Γ is a finite set of tape symbols (Stores machine symbols and internal symbols), where Σ ⊆ Γ.\n" +
                        "        s is the initial state, where s ∈ K.\n" +
                        "        σ is the transition function, where σ: K x Γ -> K x (Γ U {<}) x A, where A is a set of Actions A ∈ {L, R, N, Y}.\n" +
                        "           Actions (A) are:\n" +
                        "              L :Move the head one step to the left\n" +
                        "              R :Move the head one step to the right\n" +
                        "              N :Reject the string\n" +
                        "              Y :Accept the string\n\n" +
                        "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n"
        );

        do {
            menu();

            do {
                System.out.println(
                        "1. New Turing Machine\n" +
                                "2. Modify current Turing Machine\n" +
                                "3. Exit\n");

                choice = reader.readLine().charAt(0);

                if (choice == '1') {
                    tm = new TuringMachine();
                    break;
                } else if (choice == '2') {
                    System.out.println(
                            "1. Modify input string\n" +
                                    "2. Modify transitions\n" +
                                    "3. Modify initial position of the head\n" +
                                    "4. Modify input string and transitions\n"
                    );
                    choice = reader.readLine().charAt(0);

                    if (choice == '1') {
                        inputStringMenu();
                    } else if (choice == '2') {
                        transitionsMenu();
                        System.out.print("Enter initial position of the head on the previous string " + Arrays.toString(tm.tape) + ": ");
                        tm.head = input.nextInt();
                        simulate();
                    } else if (choice == '3') {
                        System.out.print("Enter initial position of the head on the previous string " + Arrays.toString(tm.tape) + ": ");
                        tm.head = input.nextInt();
                        simulate();
                    } else if (choice == '4') {
                        transitionsMenu();
                        inputStringMenu();
                    }
                }
            } while (!(choice == '3'));
        } while (!(choice == '3'));
    }
}