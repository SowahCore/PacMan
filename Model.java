import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;

class Model {

	// la musique du jeu
	protected Audio son;

	//Lancer la partie
	protected boolean launchGame;
	protected int timerGhost;

	//nbr de ghost mangé
	protected int ghostEat;

	//Nombre de gum sur la map
	protected int nbrGum;

	//Super Gum activer
	protected boolean SGumActive;
	protected int SGumTurn;

	// déclaration des chemins d'accès pour les images et les gifs
	protected final String PATH = "picture/32x/";
	protected final String PATH_MUR = PATH + "mur/";
	protected final String PATH_SKIN = PATH + "skin/";
	protected final String PATH_PACMAN = PATH_SKIN + "pacman/pacman_";
	protected final String PATH_SONIC = PATH_SKIN + "sonic/sonic_";
	protected final String PATH_PIKACHU = PATH_SKIN + "pikachu/pikachu_";
	protected final String PATH_MEGAMAN = PATH_SKIN + "megaman/megaman_";
	protected final String PATH_VOLTALI = PATH_SKIN + "voltali/voltali_";
	protected final String PATH_GHOST = PATH + "ghost/";
	protected final String PATH_PNG = ".png";
	protected final String PATH_GIF = ".gif";
	protected String pathPerso = this.PATH_PACMAN;

	// Chemin pour affichage d'après la direction
	protected final String DROITE = "d";
	protected final String GAUCHE = "g";
	protected final String HAUT = "h";
	protected final String BAS = "b";
	protected String direction = this.GAUCHE;
	protected String direction2 = this.GAUCHE;

	protected String directionYellow;
    protected String directionRed;
    protected String directionBlue;
    protected String directionPink;

	protected boolean departYellow;
	protected boolean departRed;
	protected boolean departBlue;
	protected boolean departPink;

	// Variable pour connaitre le personnage selectionner
	protected String choixPerso = "pacman";

	// Variable pour connaitre l'image utiliser
	protected String pathJoueur = pathPerso + direction + PATH_GIF;

	// Variable pour connaitre l'image des fantomes
	protected final String PATH_RED = PATH_GHOST + "red" + PATH_GIF;
    protected final String PATH_BLUE = PATH_GHOST + "blue" + PATH_GIF;
    protected final String PATH_PINK = PATH_GHOST + "pink" + PATH_GIF;
    protected final String PATH_YELLOW = PATH_GHOST + "yellow" + PATH_GIF;

    protected String pathFantomeRed = this.PATH_RED;
    protected String pathFantomeBlue = this.PATH_BLUE;
    protected String pathFantomePink = this.PATH_PINK;
    protected String pathFantomeYellow = this.PATH_YELLOW;

    // Variables pour l'image des fantomes vulnerable
    protected String pathFantomeWeak = PATH_GHOST + "weakness" + PATH_GIF;

	//Coordonée
	protected final int[] COORDS_ORIGINE = new int[]{23, 14};
	protected int[] coords;
	protected int[] oldCoords;

	//Score (gum)
	protected int score;

	// Tableau des coordonnées des fantômes
    protected int[][] tabCoordFantomes = new int[4][2];

	protected final int[][] TAB_COORDS_GHOST = new int[][]{
        {14,16}, // Yellow
        {14,15}, // Pink
        {14,12}, // Red
        {14,11} // Blue
    };

	//plateau des objets
	protected final String[][] PLATEAU_OBJET_INIT = new String[][]{
		// position initiale de tout les objets
		{"bd","h","h","h","h","h","h","h","h","h","h","h","h","gb","bd","h","h","h","h","h","h","h","h","h","h","h","h","gb"},
		{"v","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","v","v","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","v"},
		{"v","Sgum","bd","h","h","gb","gum","bd","h","h","h","gb","gum","v","v","gum","bd","h","h","h","gb","Sgum","bd","h","h","gb","gum","v"},
		{"v","gum","v","","","v","gum","v","","","","v","gum","v","v","gum","v","","","","v","gum","v","","","v","gum","v"},
		{"v","gum","hd","h","h","hg","gum","hd","h","h","h","hg","gum","hd","hg","gum","hd","h","h","h","hg","gum","hd","h","h","hg","gum","v"},
		{"v","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","v"},
		{"v","gum","bd","h","h","gb","gum","bd","gb","gum","bd","h","h","h","h","h","h","gb","gum","bd","gb","gum","bd","h","h","gb","gum","v"},
		{"v","gum","hd","h","h","hg","gum","v","v","gum","hd","h","h","gb","bd","h","h","hg","gum","v","v","gum","hd","h","h","hg","gum","v"},
		{"v","gum","gum","gum","gum","gum","gum","v","v","gum","gum","gum","gum","v","v","gum","gum","gum","gum","v","v","gum","gum","gum","gum","gum","gum","v"},
		{"hd","h","h","h","h","gb","gum","v","hd","h","h","gb","","v","v","","bd","h","h","hg","v","gum","bd","h","h","h","h","hg"},
		{"","","","","","v","gum","v","bd","h","h","hg","","hd","hg","","hd","h","h","gb","v","gum","v","","","","",""},
		{"","","","","","v","gum","v","v","","","","","","","","","","","v","v","gum","v","","","","",""},
		{"","","","","","v","gum","v","v","","bd","h","h","h","h","h","h","gb","","v","v","gum","v","","","","",""},
		{"h","h","h","h","h","hg","gum","hd","hg","","v","","","","","","","v","","hd","hg","gum","hd","h","h","h","h","h"},
		{"","","","","","","gum","","","","v","","","","","","","v","","","","gum","","","","","",""},
		{"h","h","h","h","h","gb","gum","bd","gb","","v","","","","","","","v","","bd","gb","gum","bd","h","h","h","h","h"},
		{"","","","","","v","gum","v","v","","hd","h","h","h","h","h","h","hg","","v","v","gum","v","","","","",""},
		{"","","","","","v","gum","v","v","","","","","","","","","","","v","v","gum","v","","","","",""},
		{"","","","","","v","gum","v","v","","bd","h","h","h","h","h","h","gb","","v","v","gum","v","","","","",""},
		{"bd","h","h","h","h","hg","gum","hd","hg","","hd","h","h","gb","bd","h","h","hg","","hd","hg","gum","hd","h","h","h","h","gb"},
		{"v","gum","gum","gum","gum","gum","gum","gum","gum","Sgum","gum","gum","gum","v","v","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","v"},
		{"v","gum","bd","h","h","gb","gum","bd","h","h","h","gb","gum","v","v","gum","bd","h","h","h","gb","gum","bd","h","h","gb","gum","v"},
		{"v","gum","hd","h","gb","v","gum","hd","h","h","h","hg","gum","hd","hg","gum","hd","h","h","h","hg","gum","v","bd","h","hg","gum","v"},
		{"v","gum","gum","gum","v","v","gum","gum","gum","gum","gum","gum","gum","gum","","gum","gum","gum","gum","gum","gum","gum","v","v","gum","gum","gum","v"},
		{"hd","h","gb","gum","v","v","gum","bd","gb","gum","bd","h","h","h","h","h","h","gb","gum","bd","gb","gum","v","v","gum","bd","h","hg"},
		{"bd","h","hg","gum","hd","hg","gum","v","v","gum","hd","h","h","gb","bd","h","h","hg","gum","v","v","gum","hd","hg","gum","hd","h","gb"},
		{"v","gum","gum","gum","gum","gum","gum","v","v","gum","gum","gum","gum","v","v","gum","gum","gum","gum","v","v","gum","gum","gum","gum","gum","gum","v"},
		{"v","gum","bd","h","h","h","h","hg","hd","h","h","gb","gum","v","v","gum","bd","h","h","hg","hd","h","h","h","h","gb","gum","v"},
		{"v","gum","hd","h","h","h","h","h","h","h","h","hg","gum","hd","hg","gum","hd","h","h","h","h","h","h","h","h","hg","gum","v"},
		{"v","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","Sgum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","gum","v"},
		{"hd","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","h","hg"}
		};
	protected String[][] plateauObjet;

	//plateau des personnages
	protected final String[][] PLATEAU_PERSO_INIT = new String[][]{
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","",pathFantomeBlue,pathFantomeRed,"","",pathFantomePink,pathFantomeYellow,"","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","",pathJoueur,"","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
		{"","","","","","","","","","","","","","","","","","","","","","","","","","","",""}
	};
	protected String[][] plateauPerso;

	//tableau des noeuds pour les mouvements des fantomes
	protected final int[][] TAB_NOEUD = new int[][]{
		{1,1},{1,6},{1,12},{1,15},{1,21},{1,26},
		{5,1},{5,6},{5,9},{5,12},{5,15},{5,18},{5,21},{5,26},
		{8,1},{8,6},{8,9},{8,12},{8,15},{8,18},{8,21},{8,26},
		{11,9},{11,12},{11,15},{11,18},
		{14,6},{14,9},{14,18},{14,21},
		{17,9},{17,18},
		{20,1},{20,6},{20,9},{20,12},{20,15},{20,18},{20,21},{20,26},
		{23,1},{23,3},{23,6},{23,9},{23,12},{23,15},{23,18},{23,21},{23,24},{23,26},
		{26,1},{26,3},{26,6},{26,9},{26,12},{26,15},{26,18},{26,21},{26,24},{26,26},
		{29,1},{29,12},{29,15},{29,26}
	};

	public Model() {
		newGame();
	}

	public void newGame(){
		this.timerGhost = 90;
		this.launchGame = false;
		this.nbrGum = 245;
		this.SGumActive = false;
		this.SGumTurn = 0;

		this.ghostEat = 1;

		this.directionRed = this.DROITE;
		this.directionBlue = this.GAUCHE;
		this.directionPink = this.GAUCHE;
		this.directionYellow = this.DROITE;

		this.departRed = true;
		this.departBlue = true;
		this.departPink = true;
		this.departYellow = true;

		this.direction = this.GAUCHE;
		this.direction2 = this.GAUCHE;
		selectPerso();
		this.pathJoueur = pathPerso + direction + PATH_GIF;

	    this.pathFantomeRed = this.PATH_RED;
	    this.pathFantomeBlue = this.PATH_BLUE;
	    this.pathFantomePink = this.PATH_PINK;
	    this.pathFantomeYellow = this.PATH_YELLOW;

		this.score = 0;

		this.coords = new int[2];
		this.oldCoords = new int[2];
		init(this.coords, this.COORDS_ORIGINE);
		init(this.oldCoords, this.COORDS_ORIGINE);

		init(this.tabCoordFantomes, this.TAB_COORDS_GHOST);

		this.plateauObjet = new String[31][28];
		init(this.plateauObjet, this.PLATEAU_OBJET_INIT);
		this.plateauPerso = new String[31][28];
		init(this.plateauPerso, this.PLATEAU_PERSO_INIT);
	}

	protected void selectPerso(){
		switch (this.choixPerso) {
			case "voltali":
				this.pathPerso = this.PATH_VOLTALI;
				break;
			case "sonic":
				this.pathPerso = this.PATH_SONIC;
				break;
			case "megaman":
				this.pathPerso = this.PATH_MEGAMAN;
				break;
			case "pikachu":
				this.pathPerso = this.PATH_PIKACHU;
				break;
			default:
				this.pathPerso = this.PATH_PACMAN;
		}
	}

	public String getTabObj(int i, int j){
		return this.plateauObjet[i][j];
	}

	public void setTabObj(int i, int j, String n){
		this.plateauObjet[i][j] = n;
	}

	public String getTabPerso(int i, int j){
		return this.plateauPerso[i][j];
	}

	public void setTabPerso(int i, int j, String n){
		this.plateauPerso[i][j] = n;
	}

	public int[] getCoords() {
		return this.coords;
	}

	public void setCoords(int i, int j){
		this.coords[0] = i;
		this.coords[1] = j;
	}

	public int[] getOldCoords() {
		return this.oldCoords;
	}

	public void setOldCoords(int i, int j) {
		this.oldCoords[0] = i;
		this.oldCoords[1] = j;
	}

	public void setOldCoords(int[] tab){
		this.oldCoords[0] = tab[0];
		this.oldCoords[1] = tab[1];
	}

	public String getDirection() {
		return this.direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection2() {
		return this.direction2;
	}

	public void setDirection2(String direction2) {
		this.direction2 = direction2;
	}

	public void setChoixPerso(String choixPerso) {
		this.choixPerso = choixPerso;
	}

	public int getScore() {
		return this.score;
	}

	public void incrementScore(int i) {
		this.score += i;
	}

	public void setPathJoueur() {
		pathJoueur = pathPerso + direction + PATH_GIF;
	}

	public boolean getLaunchGame() {
		return this.launchGame;
	}

	public void lancerLaGame() {
		this.launchGame = true;
	}

	public void finishGame() {
		this.launchGame = false;
	}

	public int getNbrGum() {
		return this.nbrGum;
	}

	public void decrementNbrGum() {
		this.nbrGum = this.nbrGum - 1;
	}

	protected void init(String[][] tab1, String[][] tab2){
		for (int i = 0; i < 31; i ++) {
			for (int j = 0; j < 28; j ++){
				tab1[i][j] = tab2[i][j];
			}
		}
	}

	protected void init(int[][] tab1, int[][] tab2){
		for (int i = 0; i < 4; i ++) {
			for (int j = 0; j < 2; j ++){
				tab1[i][j] = tab2[i][j];
			}
		}
	}

	protected void init(int[] tab1, int[] tab2){
		for (int i = 0; i < tab1.length; i ++){
			tab1[i] = tab2[i];
		}
	}

	public String getDirectionYellow() {
		return directionYellow;
	}

	public void setDirectionYellow(String directionYellow) {
		this.directionYellow = directionYellow;
	}

	public String getDirectionRed() {
		return directionRed;
	}

	public void setDirectionRed(String directionRed) {
		this.directionRed = directionRed;
	}

	public String getDirectionBlue() {
		return directionBlue;
	}

	public void setDirectionBlue(String directionBlue) {
		this.directionBlue = directionBlue;
	}

	public String getDirectionPink() {
		return directionPink;
	}

	public void setDirectionPink(String directionPink) {
		this.directionPink = directionPink;
	}

	public boolean isSGumActive() {
		return SGumActive;
	}

	public void setSGumActive(boolean SGumActive) {
		this.SGumActive = SGumActive;
	}

	public int getSGumTurn() {
		return SGumTurn;
	}

	public void setSGumTurn(int SGumTurn) {
		this.SGumTurn = SGumTurn;
	}

	public void setPathFantomeRed(String pathFantomeRed) {
		this.pathFantomeRed = pathFantomeRed;
	}

	public void setPathFantomeBlue(String pathFantomeBlue) {
		this.pathFantomeBlue = pathFantomeBlue;
	}

	public void setPathFantomePink(String pathFantomePink) {
		this.pathFantomePink = pathFantomePink;
	}

	public void setPathFantomeYellow(String pathFantomeYellow) {
		this.pathFantomeYellow = pathFantomeYellow;
	}

	public String getPathFantomeWeak() {
		return pathFantomeWeak;
	}

	public String getPATH_RED() {
		return PATH_RED;
	}

	public String getPATH_BLUE() {
		return PATH_BLUE;
	}

	public String getPATH_PINK() {
		return PATH_PINK;
	}

	public String getPATH_YELLOW() {
		return PATH_YELLOW;
	}

	public int getTimerGhost() {
		return timerGhost;
	}

	public void setTimerGhost(int timerGhost) {
		this.timerGhost = timerGhost;
	}

	public boolean isDepartYellow() {
		return departYellow;
	}

	public void setDepartYellow(boolean departYellow) {
		this.departYellow = departYellow;
	}

	public boolean isDepartRed() {
		return departRed;
	}

	public void setDepartRed(boolean departRed) {
		this.departRed = departRed;
	}

	public boolean isDepartBlue() {
		return departBlue;
	}

	public void setDepartBlue(boolean departBlue) {
		this.departBlue = departBlue;
	}

	public boolean isDepartPink() {
		return departPink;
	}

	public void setDepartPink(boolean departPink) {
		this.departPink = departPink;
	}

	public int getGhostEat() {
		return ghostEat;
	}

	public void setGhostEat(int ghostEat) {
		this.ghostEat = ghostEat;
	}
}
