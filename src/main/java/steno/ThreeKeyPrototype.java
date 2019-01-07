package steno;

import com.google.common.base.Joiner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static steno.Schemes.GENERIC_CLASS.*;
/*
  A-G: J
  H-O: K
  P-Z: L
  Submit/Next word: I
  Cancel: Space Bar
 */


/*
     * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
     *
     * Redistribution and use in source and binary forms, with or without
     * modification, are permitted provided that the following conditions
     * are met:
     *
     *   - Redistributions of source code must retain the above copyright
     *     notice, this list of conditions and the following disclaimer.
     *
     *   - Redistributions in binary form must reproduce the above copyright
     *     notice, this list of conditions and the following disclaimer in the
     *     documentation and/or other materials provided with the distribution.
     *
     *   - Neither the name of Oracle or the names of its
     *     contributors may be used to endorse or promote products derived
     *     from this software without specific prior written permission.
     *
     * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
     * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
     * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
     * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
     * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
     * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
     * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
     * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
     * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
     * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
     * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
     */

public class ThreeKeyPrototype extends JFrame implements KeyListener, ActionListener {
    JTextArea displayArea;
    JTextField typingArea;
    static final String newline = System.getProperty("line.separator");
    final Keyer keyer = new ThreeButtonKeyer(new AlphabetCompressor(Schemes.THREE_CLASS_ALPHABETIC_SPLIT));
    List<String> rankedPossibleWords = null;
    private List<String> message = new ArrayList<>();
    private int currentIndex = 0;
    List<Enum> currentWord = new ArrayList<>();

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        ThreeKeyPrototype frame = new ThreeKeyPrototype("ThreeKeyPrototype");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        frame.addComponentsToPane();


        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(800, 800));
    }

    private void addComponentsToPane() {

        JButton button = new JButton("Clear");
        button.addActionListener(this);

        typingArea = new JTextField(20);
        typingArea.addKeyListener(this);

        //Uncomment this if you wish to turn off focus
        //traversal.  The focus subsystem consumes
        //focus traversal keys, such as Tab and Shift Tab.
        //If you uncomment the following line of code, this
        //disables focus traversal and the Tab events will
        //become available to the key event listener.
        //typingArea.setFocusTraversalKeysEnabled(false);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));

        getContentPane().add(typingArea, BorderLayout.PAGE_START);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.PAGE_END);
    }

    public ThreeKeyPrototype(String name) {
        super(name);
    }


    /**
     * Handle the key typed event from the text field.
     */
    public void keyTyped(KeyEvent e) {
//        displayInfo(e, "KEY TYPED: ");
    }

    /**
     * Handle the key pressed event from the text field.
     */
    public void keyPressed(KeyEvent e) {
//        displayInfo(e, "KEY PRESSED: ");
    }

        /*
        Key	        Code
        left arrow	37
        up arrow	38
        right arrow	39
        down arrow	40
        i	73
        j	74
        k	75
        l	76
         */

    /**
     * Handle the key released event from the text field.
     */
    public void keyReleased(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        if (rankedPossibleWords == null) {
            switch (keyCode) {
                case 74:
                case 37:
                    currentWord.add(CLASS_A);
                    break;
                case 75:
                case 40:
                    currentWord.add(CLASS_B);
                    break;
                case 76:
                case 39:
                    currentWord.add(CLASS_C);
                    break;
                case 73:
                case 38:
                    processCurrentWord();
                    break;
                case 32:
                    currentWord = new ArrayList<>();
                    break;
            }
        } else {
            switch (keyCode) {
                case 74:
                case 37:
                    addWord();
                    currentWord.add(CLASS_A);
                    break;
                case 75:
                case 40:
                    addWord();
                    currentWord.add(CLASS_B);
                    break;
                case 76:
                case 39:
                    addWord();
                    currentWord.add(CLASS_C);
                    break;
                case 73:
                case 38:
                    currentIndex++;
                    printPossibleWordAtCurrentIndex();
                    break;
                case 32:
                    currentIndex = 0;
                    currentWord = new ArrayList<>();
                    rankedPossibleWords = null;
                    printMessage();
                    break;
            }
        }
    }

    private void print(Object o) {
        displayArea.append(String.valueOf(o) + newline);
    }

    private void processCurrentWord() {
        if (!currentWord.isEmpty()) {
            final Set<String> possibleWords = keyer.getCompressor().decode(currentWord);
            final List<String> context = message.subList(Math.max(0, message.size() - 3), message.size());
            final List<String> rankedPossibleWords = NextWordPredictor.sortByLikelihoodDescending(possibleWords, context);
            this.rankedPossibleWords = rankedPossibleWords;
            printPossibleWordAtCurrentIndex();
            currentWord = new ArrayList<>();
        }
    }

    private void printPossibleWordAtCurrentIndex() {
        clearDisplay();
        print(rankedPossibleWords.get(currentIndex % rankedPossibleWords.size()));
    }

    private void printMessage() {
        clearDisplay();
        print(Joiner.on(" ").join(message));
    }

    private void addWord() {
        message.add(rankedPossibleWords.get(currentIndex));
        currentIndex = 0;
        rankedPossibleWords = null;
        printMessage();
    }

    private void clearDisplay() {
        displayArea.setText("");
    }

    /**
     * Handle the button click.
     */
    public void actionPerformed(ActionEvent e) {
        rankedPossibleWords = null;
        message = new ArrayList<>();
        currentIndex = 0;
        currentWord = new ArrayList<>();

        //Clear the text components.
        clearDisplay();
        typingArea.setText("");

        //Return the focus to the typing area.
        typingArea.requestFocusInWindow();
    }
}