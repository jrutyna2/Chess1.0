package Chess;
import java.util.*;
import Chess.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Component of the Chess game that detects check mates in the game.
 *
 * @author Jussi Lundstedt
 *
 */
public class Check {
    private Board b;
    private LinkedList<Piece> wPieces;
    private LinkedList<Piece> bPieces;
    private LinkedList<Square> legalMoves;
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> wMoves;
    private HashMap<Square,List<Piece>> bMoves;

    /**
     * Constructs a new instance of CheckmateDetector on a given board. By
     * convention should be called when the board is in its initial state.
     *
     * @param b The board which the detector monitors
     * @param wPieces White pieces on the board.
     * @param bPieces Black pieces on the board.
     * @param wk Piece object representing the white king
     * @param bk Piece object representing the black king
     */
    public Check(Board b, LinkedList<Piece> wPieces, LinkedList<Piece> bPieces, King wk, King bk) {
        this.b = b;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;

        // Initialize other fields
        squares = new LinkedList<Square>();
        legalMoves = new LinkedList<Square>();
        wMoves = new HashMap<Square,List<Piece>>();
        bMoves = new HashMap<Square,List<Piece>>();

        Square[][] brd = b.getSquareArray();

        // add all squares to squares list and as hashmap keys
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares.add(brd[y][x]);
                wMoves.put(brd[y][x], new LinkedList<Piece>());
                bMoves.put(brd[y][x], new LinkedList<Piece>());
            }
        }

        // update situation
        update();
    }

    /**
     * Updates the object with the current situation of the game.
     */
    public void update() {
        // Iterators through pieces
        Iterator<Piece> wIter = wPieces.iterator();
        Iterator<Piece> bIter = bPieces.iterator();

        // empty moves and movable squares at each update
        for (List<Piece> pieces : wMoves.values()) {
            pieces.removeAll(pieces);
        }

        for (List<Piece> pieces : bMoves.values()) {
            pieces.removeAll(pieces);
        }

        legalMoves.removeAll(legalMoves);

        // Add each move white and black can make to map
        while (wIter.hasNext()) {
            Piece p = wIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getSquare() == null) {
                    wIter.remove();
                    continue;
                }

                List<Square> mvs = p.getMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = wMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }

        while (bIter.hasNext()) {
            Piece p = bIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getSquare() == null) {
                    wIter.remove();
                    continue;
                }

                List<Square> mvs = p.getMoves(b);
                Iterator<Square> iter = mvs.iterator();
                while (iter.hasNext()) {
                    List<Piece> pieces = bMoves.get(iter.next());
                    pieces.add(p);
                }
            }
        }
    }

    /**
     * Checks if the black king is threatened
     * @return boolean representing whether the black king is in check.
     */
    public boolean blackInCheck() {
        update();
        Square sq = bk.getSquare();
        if (wMoves.get(sq).isEmpty()) {
            legalMoves.addAll(squares);
            return false;
        } else return true;
    }

    /**
     * Checks if the white king is threatened
     * @return boolean representing whether the white king is in check.
     */
    public boolean whiteInCheck() {
        update();
        Square sq = wk.getSquare();
        if (bMoves.get(sq).isEmpty()) {
            legalMoves.addAll(squares);
            return false;
        } else return true;
    }

    /**
     * Checks whether black is in checkmate.
     * @return boolean representing if black player is checkmated.
     */
    public boolean blackCheckMated() {
        boolean checkmate = true;
        // Check if black is in check
        if (!this.blackInCheck()) return false;

        // If yes, check if king can evade
        if (canEvade(wMoves, bk)) checkmate = false;

        // If no, check if threat can be captured
        List<Piece> threats = wMoves.get(bk.getSquare());
        if (canCapture(bMoves, threats, bk)) checkmate = false;

        // If no, check if threat can be blocked
        if (canBlock(threats, bMoves, bk)) checkmate = false;

        // If no possible ways of removing check, checkmate occurred
        return checkmate;
    }

    /**
     * Checks whether white is in checkmate.
     * @return boolean representing if white player is checkmated.
     */
    public boolean whiteCheckMated() {
        boolean checkmate = true;
        // Check if white is in check
        if (!this.whiteInCheck()) return false;

        // If yes, check if king can evade
        if (canEvade(bMoves, wk)) checkmate = false;

        // If no, check if threat can be captured
        List<Piece> threats = bMoves.get(wk.getSquare());
        if (canCapture(wMoves, threats, wk)) checkmate = false;

        // If no, check if threat can be blocked
        if (canBlock(threats, wMoves, wk)) checkmate = false;

        // If no possible ways of removing check, checkmate occurred
        return checkmate;
    }

    /*
     * Helper method to determine if the king can evade the check.
     * Gives a false positive if the king can capture the checking piece.
     */
    private boolean canEvade(Map<Square,List<Piece>> tMoves, King tKing) {
        boolean evade = false;
        List<Square> kingsMoves = tKing.getMoves(b);
        Iterator<Square> iterator = kingsMoves.iterator();

        // If king is not threatened at some square, it can evade
        while (iterator.hasNext()) {
            Square sq = iterator.next();
            if (!testMove(tKing, sq)) continue;
            if (tMoves.get(sq).isEmpty()) {
                legalMoves.add(sq);
                evade = true;
            }
        }

        return evade;
    }

    /*
     * Helper method to determine if the threatening piece can be captured.
     */
    private boolean canCapture(Map<Square,List<Piece>> poss, List<Piece> threats, King k) {

        boolean capture = false;
        if (threats.size() == 1) {
            Square sq = threats.get(0).getSquare();

            if (k.getMoves(b).contains(sq)) {
                legalMoves.add(sq);
                if (testMove(k, sq)) {
                    capture = true;
                }
            }

            List<Piece> caps = poss.get(sq);
            ConcurrentLinkedDeque<Piece> capturers = new ConcurrentLinkedDeque<Piece>();
            capturers.addAll(caps);

            if (!capturers.isEmpty()) {

                for (Piece p : capturers) {
                    if (testMove(p, sq)) {
                        capture = true;
                    }
                }
            }
        }

        return capture;
    }

    /*
     * Helper method to determine if check can be blocked by a piece.
     */
    private boolean canBlock(List<Piece> threats, Map <Square,List<Piece>> blockMoves, King k) {
        boolean blockable = false;

        if (threats.size() == 1) {
            Square ts = threats.get(0).getSquare();
            Square ks = k.getSquare();
            Square[][] brdArray = b.getSquareArray();

            if (ks.getFile() == ts.getFile()) {
                int max = Math.max(ks.getRank(), ts.getRank());
                int min = Math.min(ks.getRank(), ts.getRank());

                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks = blockMoves.get(brdArray[i][ks.getFile()]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);

                    if (!blockers.isEmpty()) {
                        legalMoves.add(brdArray[i][ks.getFile()]);

                        for (Piece p : blockers) {
                            if (testMove(p,brdArray[i][ks.getFile()])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }

            if (ks.getRank() == ts.getRank()) {
                int max = Math.max(ks.getFile(), ts.getFile());
                int min = Math.min(ks.getFile(), ts.getFile());

                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks = blockMoves.get(brdArray[ks.getRank()][i]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);

                    if (!blockers.isEmpty()) {

                        legalMoves.add(brdArray[ks.getRank()][i]);

                        for (Piece p : blockers) {
                            if (testMove(p, brdArray[ks.getRank()][i])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }

            Class<? extends Piece> tC = threats.get(0).getClass();

            if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
                int kX = ks.getFile();
                int kY = ks.getRank();
                int tX = ts.getFile();
                int tY = ts.getRank();

                if (kX > tX && kY > tY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY++;
                        List<Piece> blks = blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            legalMoves.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }

                if (kX > tX && tY > kY) {
                    for (int i = tX + 1; i < kX; i++) {
                        tY--;
                        List<Piece> blks = blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            legalMoves.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }

                if (tX > kX && kY > tY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY++;
                        List<Piece> blks = blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            legalMoves.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }

                if (tX > kX && tY > kY) {
                    for (int i = tX - 1; i > kX; i--) {
                        tY--;
                        List<Piece> blks = blockMoves.get(brdArray[tY][i]);
                        ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                        blockers.addAll(blks);

                        if (!blockers.isEmpty()) {
                            legalMoves.add(brdArray[tY][i]);

                            for (Piece p : blockers) {
                                if (testMove(p, brdArray[tY][i])) {
                                    blockable = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return blockable;
    }

    /**
     * Method to get a list of allowable squares that the player can move.
     * Defaults to all squares, but limits available squares if player is in
     * check.
     * @param b boolean representing whether it's white player's turn (if yes,
     * true)
     * @return List of squares that the player can move into.
     */
    public List<Square> getAllowableSquares(boolean b) {
        legalMoves.removeAll(legalMoves);
        if (whiteInCheck()) {
            whiteCheckMated();
        } else if (blackInCheck()) {
            blackCheckMated();
        }
        return legalMoves;
    }

    /**
     * Tests a move a player is about to make to prevent making an illegal move
     * that puts the player in check.
     * @param p Piece moved
     * @param sq Square to which p is about to move
     * @return false if move would cause a check
     */
    public boolean testMove(Piece p, Square sq) {
        Piece c = sq.getPiece();

        boolean movetest = true;
        Square init = p.getSquare();

        p.move(sq);
        update();

        if (p.getColor() == 0 && blackInCheck()) movetest = false;
        else if (p.getColor() == 1 && whiteInCheck()) movetest = false;

        p.move(init);
        if (c != null) sq.put(c);

        update();

        legalMoves.addAll(squares);
        return movetest;
    }

}
