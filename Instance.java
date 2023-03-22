import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Instance {
    private Coord startingP; //point de départ
    private int k; //k>=0, nombre de pas disponibles
    private boolean[][] plateau; //orientation: plateau[0][0] en haut à gauche, et plateau[ligne][col]

    //sortie du problème: une Solution (une solution est valide ssi c'est une liste de coordonées de taille au plus k+1, tel que deux coordonnées consécutives sont à distance 1,
    // et les coordonnées ne sortent pas du plateau)


    private ArrayList<Coord> listeCoordPieces;// attribut supplémentaire (qui est certes redondant) qui contiendra la liste des coordonnées des pièces du plateau
    //on numérote les pièces de haut en bas, puis de gauche à droite, par exemple sur l'instance suivante (s représente
    //le startinP et x les pièces
    //.x..x
    //x..s.
    //....x

    //les numéros sont
    //.0..1
    //2..s.
    //....3
    //et donc listeCoordPices.get(0) est la Coord (0,1)


    /************************************************
     **** debut methodes fournies              ******
     *************************************************/
    public Instance(boolean[][] p, Coord s, int kk, int hh) {
        plateau = p;
        startingP = s;
        k = kk;
        listeCoordPieces = getListeCoordPieces();
    }

    public Instance(boolean[][] p, Coord s, int kk) {
        plateau = p;
        startingP = s;
        k = kk;
        listeCoordPieces = getListeCoordPieces();
    }


    public Instance(Instance i) { //créer une instance qui est une deep copy (this doit etre independante de i)
        boolean[][] p2 = new boolean[i.plateau.length][i.plateau[0].length];
        for (int l = 0; l < p2.length; l++) {
            for (int c = 0; c < p2[0].length; c++) {
                p2[l][c] = i.plateau[l][c];
            }
        }

        plateau = p2;
        startingP = new Coord(i.startingP);
        k = i.k;
        listeCoordPieces = new ArrayList<>();
        for (Coord c : i.listeCoordPieces) {
            listeCoordPieces.add(new Coord(c));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instance)) return false;
        Instance instance = (Instance) o;
        return getK() == instance.getK() && getStartingP().equals(instance.getStartingP()) && Algos.egalEnsembliste(getListeCoordPieces(), instance.getListeCoordPieces());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartingP(), getK(), getListeCoordPieces());
    }

    public int getNbL() {
        return plateau.length;
    }

    public int getNbC() {
        return plateau[0].length;
    }

    public Coord getStartingP() {
        return startingP;
    }

    public void setStartingP(Coord c) {
        startingP = c;
    }

    public int getK() {
        return k;
    }

    public void setK(int kk) {
        k = kk;
    }


    public ArrayList<Coord> getListeCoordPieces() {
        if (listeCoordPieces == null) {
            ArrayList<Coord> listeCoordPieces = new ArrayList<>();
            for (int l = 0; l < getNbL(); l++) {
                for (int c = 0; c < getNbC(); c++) {
                    if (piecePresente(new Coord(l, c))) {
                        listeCoordPieces.add(new Coord(l, c));
                    }
                }
            }
            return listeCoordPieces;
        } else
            return listeCoordPieces;
    }


    public boolean piecePresente(Coord c) {
        return plateau[c.getL()][c.getC()];
    }

    public void retirerPiece(Coord c) {
        //si pas de piece en c ne fait rien, sinon la retire du plateau et met à jour les coordonnées
        if (piecePresente(c)) {
            plateau[c.getL()][c.getC()] = false;
            listeCoordPieces.remove(c);
        }
    }


    @Override
    public String toString() {
        //retourne une chaine représentant this,
        StringBuilder res = new StringBuilder("k = " + k + "\n" + "nb pieces = " + getListeCoordPieces().size() + "\nstarting point = " + startingP + "\n");
        for (int l = 0; l < getNbL(); l++) {
            for (int c = 0; c < getNbC(); c++) {

                if (startingP.equals(new Coord(l, c)))
                    res.append("s");
                else {
                    if (piecePresente(new Coord(l, c))) {
                        res.append("x");
                    } else {
                        res.append(".");
                    }
                }

            }
            res.append("\n");
        }
        return res.toString() + "\nliste pieces " + getListeCoordPieces();
    }

    public String toString(Solution s) {

        //retourne une chaine sous la forme suivante
        //o!..
        //.ox.
        //.o..
        //.o..

        //où
        // '.' signifie que la solution ne passe pas là, et qu'il n'y a pas de pièce
        // 'x' signifie que la solution ne passe pas là, et qu'il y a une pièce
        // 'o' signifie que la solution passe par là, et qu'il n'y a pas de pièce
        // '!' signifie que la solution passe par là, et qu'il y a une pièce

        // dans l'exemple ci-dessus, on avait donc 2 pièces dans l'instance (dont 1 ramassée par s)
        //et la chaîne de l'exemple contient 4 fois le caractère "\n" (une fois à chaque fin de ligne)

        if (s == null) return null;
        StringBuilder res = new StringBuilder("");//\n k = " + k + "\n" + "nb pieces = " + listeCoordPieces.size() + "\n");
        for (int l = 0; l < getNbL(); l++) {
            for (int c = 0; c < getNbC(); c++) {
                if (startingP.equals(new Coord(l, c)))
                    res.append("s");
                else {
                    if (s.contains(new Coord(l, c)) && piecePresente(new Coord(l, c))) {
                        res.append("!");
                    }
                    if (!s.contains(new Coord(l, c)) && piecePresente(new Coord(l, c))) {
                        res.append("x");
                    }
                    if (s.contains(new Coord(l, c)) && !piecePresente(new Coord(l, c))) {
                        res.append("o");
                    }
                    if (!s.contains(new Coord(l, c)) && !piecePresente(new Coord(l, c))) {
                        res.append(".");
                    }
                }
            }
            res.append("\n");
        }
        return res.toString();
    }

    /************************************************
     **** méthodes à fournir relatives à une solution **
     *************************************************/


    public boolean estValide(Solution s) {
        //prérequis : s!=null, et les s.get(i) !=null pour tout i (mais par contre s peut contenir n'importe quelle séquence de coordonnées)
        //retourne vrai ssi s est une solution valide (une solution est valide ssi c'est une liste de coordonnées de taille au plus k+1,
        // telle que deux coordonnées consécutives sont à distance 1, et les coordonnées ne sortent pas du plateau,
        // et que la solution démarre bien par le point de départ)

        if (s.size() > k+1 || s.get(0) != startingP) {
            return false;
        }
        // Pour chaque coordonnée
        for (Coord coord : s) {
            // Si une coordonnée sort du plateau
            if (!coord.estDansPlateau(getNbL(), getNbC())) {
                return false;
            }
            // Si deux coordonnées consécutives ne sont pas à distance 1
            if (s.indexOf(coord) + 1 < s.size() && !coord.estADistanceUn(s.get(s.indexOf(coord) + 1))) {
                return false;
            }
        }
        return true;
    }


    public int evaluerSolution(Solution s) {
        //prerequis : s est valide (et donc !=null)
        //action : retourne le nombre de pièces ramassées par s (et ne doit pas modifier this ni s)
        int nbPiecesRamassees = 0;
        for (Coord coord : s) {
            if (piecePresente(coord)) {
                nbPiecesRamassees++;
            }
        }
        return nbPiecesRamassees;
    }


    public int nbStepsToCollectAll(ArrayList<Integer> permut) {
        //prérequis : permut est une permutation des entiers {0,..,listeCoordPieces.size()-1}
        // (et donc permut peut être vide, mais pas null, si il n'y a pas de pièces)

        //retourne le nombre de pas qu'il faudrait pour ramasser toutes les pièces dans l'ordre de permut

        int steps = 0;
        int currentPos = 0;
        for (int i = 0; i < permut.size(); i++) {
            int nextPos = permut.get(i);
            steps += Math.abs(nextPos - currentPos);
            currentPos = nextPos;
        }
        return steps;
    }

    /************************************************
     **** méthodes à fournir relatives au greedy        ******
     *************************************************/


    public ArrayList<Integer> greedyPermut() {
        //retourne une liste (x1,..,xp) où
        //x1 est la pièce la plus proche du point de départ
        //x2 est la pièce restante la plus proche de x1
        //x3 est la pièce restante la plus proche de x2
        //etc
        //Remarques :
        // -on doit donc retourner une sequence de taille listeCoordPieces.size() (donc sequence vide (et pas null) si il n'y a pas de pièces)
        // -si à un moment donné, lorsque l'on est sur une pièce xi, la pièce restante la plus proche de xi n'est pas unique,
        //   alors on peut prendre n'importe quelle pièce (parmi ces plus proches de xi)
        //par exemple,
        //si le plateau est
        //.s.x
        //....
        //x..x
        //avec la pièce 0 en haut à droite, la pièce 1 en bas à gauche, et la pièce 2 en bas à droite,
        //on doit retourner (0,2,1)

        ArrayList<Integer> permut = new ArrayList<Integer>();
        ArrayList<Coord> remainingPieces = new ArrayList<Coord>(this.listeCoordPieces);
        Coord currentPos = this.startingP;
        while (!remainingPieces.isEmpty()) {
            Coord nextPiece = null;
            int minDist = Integer.MAX_VALUE;
            for (Coord remainingPiece : remainingPieces) {
                int dist = currentPos.distanceFrom(remainingPiece);
                if (dist < minDist) {
                    minDist = dist;
                    nextPiece = remainingPiece;
                }
            }
            if (nextPiece == null) {
                break;
            }
            permut.add(this.listeCoordPieces.indexOf(nextPiece));
            remainingPieces.remove(nextPiece);
            currentPos = nextPiece;
        }
        return permut;
    }


    public Solution calculerSol(ArrayList<Integer> permut) {

        //prérequis : permut est une permutation des entiers {0,..,listeCoordPieces.size()-1}
        // (et donc permut peut être vide, mais pas null, si il n'y a pas de pièces)

        //retourne la solution obtenue en concaténant les plus courts chemins successifs pour attraper
        // les pièces dans l'ordre donné par this.permut, jusqu'à avoir k mouvements ou à avoir ramassé toutes les pièces de la permut.
        // Remarque : entre deux pièces consécutives, le plus court chemin n'est pas unique, n'importe quel plus court chemin est ok.
        //par ex, si le plateau est
        //.s.x
        //....
        //x..x
        //avec la pièce 0 en haut à droite, la pièce 1 en bas à gauche, et la pièce 2 en bas à droite,
        //et que permut = (0,2,1), alors pour
        //k=3, il faut retourner (0,1)(0,2)(0,3)(1,3)  (dans ce cas là les plus courts sont uniques)
        //k=10, il faut retourner (0,1)(0,2)(0,3)(1,3)(2,3)(2,2)(2,1)(2,0)  (dans ce cas là les plus courts sont aussi uniques,
        // et on s'arrête avant d'avoir fait k pas car on a tout collecté)

        Solution solution = new Solution(startingP);
        Coord currentPosition = startingP;
        int movesLeft = k;
        for (Integer pieceIndex : permut) {
            Coord targetPosition = listeCoordPieces.get(pieceIndex);
            ArrayList<Coord> shortestPath = plusCourtChemin(currentPosition, targetPosition);
            for (Coord step : shortestPath) {
                if (step.equals(targetPosition)) {
                    solution.add(step);
                    currentPosition = step;
                    movesLeft -= currentPosition.distanceFrom(startingP);
                    break;
                } else {
                    solution.add(step);
                    currentPosition = step;
                    movesLeft--;
                    if (movesLeft <= 0) {
                        return solution;
                    }
                }
            }
        }
        return solution;
    }

    public ArrayList<Coord> plusCourtChemin(Coord start, Coord end) {
        ArrayList<Coord> chemin = new ArrayList<>();
        int lDiff = end.getL() - start.getL();
        int cDiff = end.getC() - start.getC();
        int lStep = (lDiff < 0) ? -1 : 1;
        int cStep = (cDiff < 0) ? -1 : 1;
        while (start.getL() != end.getL() || start.getC() != end.getC()) {
            if (Math.abs(lDiff) > Math.abs(cDiff)) {
                start = new Coord(start.getL() + lStep, start.getC());
            } else {
                start = new Coord(start.getL(), start.getC() + cStep);
            }
            chemin.add(new Coord(start));
            lDiff = end.getL() - start.getL();
            cDiff = end.getC() - start.getC();
        }
        return chemin;
    }


    /************************************************
     **** fin algo algo greedy                      ******
     *************************************************/


    public int borneSup() {
        //soit d0 la distance min entre la position de départ et une pièce
        //soit {d1,..,dx} l'ensemble des distances entre pièces (donc x = (nbpiece-1)*npbpiece / 2), triées par ordre croissant
        //retourne le nombre de pièces que l'on capturerait avec k pas dans le cas idéal où
        //toutes les pièces seraient disposées sur une ligne ainsi : (avec sp le point de départ à gauche)
        //sp .... p .. p .... p ....... p ...
        //    d0    d1    d2      d3
        //(vous pouvez réfléchir au fait que c'est bien une borne supérieure)
        //(pour des exemples précis, cf les tests)

        //à compléter


        return 0;
    }
}