package Chess;
import java.util.*;
import java.lang.Math;
import Chess.*;

public class Game {

  public void printMove(Move m) {
      if (m != null)
          m.printMove();
  }



  public static void main (String [] args) {
      // int[][] board = new int[8][8];
      Game g = new Game();
      int nplayers = 0, turns = 0, color = 0;
      int[] coordinates_from = new int[2];
      int[] coordinates_to = new int[2];
      Scanner input = new Scanner(System.in);
      String line, line_split[];

      double posInf = Double.POSITIVE_INFINITY;
      double negInf = Double.NEGATIVE_INFINITY;

      System.out.println("Welcome! How many players for this game?(Enter 1 or 2)");
      line = input.nextLine();
      line = line.trim();

      nplayers = Integer.parseInt(line);

      if (nplayers == 1) {
          System.out.println("play as white or as black?(Enter 0 for white, 1 for black)");
          line = input.nextLine();
          line = line.trim();

          Board b = new Board();
          b.initializeBoard();
          b.pieceTotals();
          b.printBoard();
          if (line.charAt(0) == '0') {
              while(true) {
                  System.out.printf("enter your move:\n");
                  line = input.nextLine();
                  line = line.trim();
                  line_split = line.split("[ +]");
                  if (line.length() == 1)
                      break;
                  if (line_split.length != 2)
                      continue;

                  coordinates_from = b.getCoordinates(line_split[0]);
                  coordinates_to = b.getCoordinates(line_split[1]);

                  int gameVal = b.playerMove(coordinates_from, coordinates_to, 0);
                  b.printBoard();
                  if (gameVal == 0) {System.out.printf("\nInvalid move!\n");}
                  else if (gameVal == 1) {
                      b.minimax(b.getSquareArray(), 1, negInf, posInf, true, 1);
                      //System.out.println("MINIMIAX = "+b.minimax(b.getSquareArray(), 5, negInf, posInf, true, 5));
                      g.printMove(b.getBestMove());
                      b.cpuMove(b.getBestMove(), 1);
                  }
                  else if (gameVal == 2) {System.out.printf("\nBlack wins!\n"); break;}
                  else if (gameVal == 3) {b.printBoard();System.out.printf("\nWhite wins!\n"); break;}
                  else if (gameVal == 4) {System.out.printf("\nStalemate!\n"); break;}
                  b.pieceTotals();
                  b.printBoard();
              }
          }
          else if (line.charAt(0) == '1') {
              // while(true) {
                  b.minimax(b.getSquareArray(), 5, negInf, posInf, false, 5);
                  g.printMove(b.getBestMove());
                  int gameVal = b.cpuMove(b.getBestMove(), 0);
                  b.printBoard();

                  if (gameVal == 0) {System.out.printf("\nInvalid move!\n");}
                  else if (gameVal == 1) {
                      while (true) {
                          System.out.printf("enter your move:\n");
                          line = input.nextLine();
                          line = line.trim();
                          line_split = line.split("[ +]");
                          if (line.length() == 1)
                              break;
                          if (line_split.length != 2)
                              continue;

                          coordinates_from = b.getCoordinates(line_split[0]);
                          coordinates_to = b.getCoordinates(line_split[1]);

                          int gameVal2 = b.playerMove(coordinates_from, coordinates_to, 1);
                          b.printBoard();
                          if (gameVal2 == 0) {System.out.printf("\nInvalid move!\n");}
                          else if (gameVal2 == 1) {
                              b.minimax(b.getSquareArray(), 5, negInf, posInf, false, 5);
                              //System.out.println("MINIMIAX = "+b.minimax(b.getSquareArray(), 5, negInf, posInf, true, 5));
                              g.printMove(b.getBestMove());
                              b.cpuMove(b.getBestMove(), 1);
                          }
                          else if (gameVal2 == 2) {System.out.printf("\nBlack wins!\n"); break;}
                          else if (gameVal2 == 3) {b.printBoard();System.out.printf("\nWhite wins!\n"); break;}
                          else if (gameVal2 == 4) {System.out.printf("\nStalemate!\n"); break;}
                          b.printBoard();
                      }

              }
          }
      }
      else if (nplayers == 2) {
          Board b = new Board();
          b.initializeBoard();
          b.pieceTotals();
          b.printBoard();
          while(true) {
              color = turns%2;

              System.out.printf("PLAYER %d enter your move:\n", turns%2+1);
              line = input.nextLine();
              line = line.trim();
              line_split = line.split("[ +]");
              if (line.length() == 1)
                  break;
              if (line_split.length != 2)
                  continue;
              // System.out.println(line_split.length);
              // for (int i = 0; i < line_split.length; i++)
              //     System.out.printf("%s\n", line_split[i]);

              coordinates_from = b.getCoordinates(line_split[0]);
              // System.out.println("coord: " + coordinates_from[0] + " " + coordinates_from[1]);
              coordinates_to = b.getCoordinates(line_split[1]);
              // System.out.println("coord: " + coordinates_to[0] + " " + coordinates_to[1]);

              int gameVal = b.playerMove(coordinates_from, coordinates_to, color);

              if (gameVal == 0) System.out.printf("\nInvalid move!\n");
              else if (gameVal == 1) turns++;
              else if (gameVal == 2) {System.out.printf("\nBlack wins!\n"); break;}
              else if (gameVal == 3) {System.out.printf("\nWhite wins!\n"); break;}
              else if (gameVal == 4) {System.out.printf("\nStalemate!\n"); break;}

              b.pieceTotals();
              b.printBoard();
          }

          // ch.canPieceMove(board, coordinates_from, coordinates_to);
          //     ch.print_board(board);
      }
      else {

      }

  }
}
