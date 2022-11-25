import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        Position[] walls = {new Position(5, 7), new Position(10, 4), new Position(12, 12)};
        Position[] longWall = new Position[10];
        Position[] longWall2 = new Position[10];

        boolean continueReadingInput = true;
        terminal.setCursorVisible(false);
        int x = 10;
        int y = 10;
        final char player = '\u236B';
        final char block = '\u2588';
        terminal.setCursorPosition(x, y);
        terminal.putCharacter(player);

        for (int i = 0; i < longWall.length; i++) {
            longWall[i] = new Position((10 + i), 17);
            terminal.setCursorPosition(longWall[i].x, longWall[i].y);
            terminal.putCharacter(block);
        }
        for (int i = 0; i < longWall2.length; i++) {
        longWall2[i] = new Position((10 + i), 16);
        terminal.setCursorPosition(longWall2[i].x, longWall2[i].y);
        terminal.putCharacter(block); }

        for (Position p : walls) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter(block);
        }

        //Bomber
        Random r = new Random();
        Position bombPosition = new Position(r.nextInt(80), r.nextInt(24));
        terminal.setCursorPosition(bombPosition.x, bombPosition.y);
        terminal.putCharacter('O');

        Random r2 = new Random();
        Position bombPosition2 = new Position(r.nextInt(80), r.nextInt(24));
        terminal.setCursorPosition(bombPosition2.x, bombPosition2.y);
        terminal.putCharacter('O');


        //Monster
        Monster monster = new Monster('\u1F60', new Position(1, 1));
        Monster monster2 = new Monster('\u1F60', new Position(20, 20));
        Monster monster3 = new Monster('\u1F60', new Position(30, 10));

        Monster[] monsters = {monster, monster2, monster3};

        for (Monster m : monsters) {
            terminal.setCursorPosition(m.position.x, m.position.y);
            terminal.putCharacter(m.type);
        }


        terminal.flush();

        while (continueReadingInput) {

            KeyStroke keyStroke = null;
            do {
                Thread.sleep(5); // might throw InterruptedException
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);

            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter();

            if (c == Character.valueOf('q')) {
                continueReadingInput = false;
                System.out.println("quit");
                terminal.close();
            }

            int oldX = x;
            int oldY = y;



            switch (keyStroke.getKeyType()) {
                case ArrowDown:
                    y += 2;
                    break;
                case ArrowUp:
                    y -= 2;
                    break;

                case ArrowLeft:
                    x -= 2;
                    break;

                case ArrowRight:
                    x += 2;
                    break;
            }


            for (Monster m : monsters) {
                boolean monsterCrash = false;
                int monsterOldX = m.position.x;
                int monsterOldY = m.position.y;

                if (m.position.x > x) {
                    m.position.x -= 1;

                } else if (m.position.x < x) {
                    m.position.x += 1;

                }
                if (m.position.y > y) {
                    m.position.y -= 1;

                } else if (m.position.y < y) {
                    m.position.y += 1;
                }

                for (Position p : walls) {
                    if (p.x == m.position.x && p.y == m.position.y) {
                        monsterCrash = true;
                    }
                }

                for (Position p : longWall) {
                    if (p.x == m.position.x && p.y == m.position.y) {
                        monsterCrash = true;
                    }
                }

                for (Position p : longWall2) {
                    if (p.x == m.position.x && p.y == m.position.y) {
                        monsterCrash = true;
                    }
                }

                if (m.position.x == x && m.position.y == y) {
                    String message = "*** GAME OVER ***";
                    for (int i = 0; i < message.length(); i++) {
                        terminal.setCursorPosition(i+33, 9);
                        terminal.putCharacter(message.charAt(i));
                    }continueReadingInput = false;
                    //System.out.println("quit");
                    //terminal.close();
                }

                if (monsterCrash) {
                    m.position.x = monsterOldX;
                    m.position.y = monsterOldY;
                } else {
                    terminal.setCursorPosition(monsterOldX, monsterOldY);
                    terminal.putCharacter(' ');
                    terminal.setCursorPosition(m.position.x, m.position.y);
                    terminal.putCharacter(m.type);

                }

            }

            boolean crash = false;


            for (Position p : walls) {
                if (p.x == x && p.y == y) {
                    crash = true;
                }
            }

            for (Position p : longWall) {
                if (p.x == x && p.y == y) {
                    crash = true;
                }
            }

            for (Position p : longWall2) {
                if (p.x == x && p.y == y) {
                    crash = true;
                }
            }


            if (bombPosition.x == x && bombPosition.y == y) {
                String message = "*** GAME OVER ***";
                for (int i = 0; i < message.length(); i++) {
                    terminal.setCursorPosition(i+33, 9);
                    terminal.putCharacter(message.charAt(i));
                }continueReadingInput = false;
                //System.out.println("Game Over");
                //terminal.close();

            }

            if (bombPosition2.x == x && bombPosition2.y == y) {
               // continueReadingInput = false;
                //System.out.println("Game Over");
                String message = "Game Over";
                for (int i = 0; i < message.length(); i++) {
                    terminal.setCursorPosition(i, 30);
                    terminal.putCharacter(message.charAt(i));
                }continueReadingInput = false;
                //System.out.println("Game Over");
                //terminal.close();
            }


            if (crash) {
                x = oldX;
                y = oldY;
            } else {
                terminal.setCursorPosition(oldX, oldY);
                terminal.putCharacter(' ');
                terminal.setCursorPosition(x, y);
                terminal.putCharacter(player);

            }
            terminal.flush();
        }

    }
}
