import java.io.File;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;

public abstract class Controller {

	Timer chrono = new Timer();

    Model model;
    Vue f;

    public Controller(Model model, Vue f) {
        this.model = model;
        this.f = f;
    }

    protected void newGame() {
        model.newGame();
        initTable();
        initPlayer();
		if(model.son != null) {
            try {
	            model.son.stop(); // arret du son
	            model.son.close(); // fermeture du flux
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        }
    }

    protected void play() {
        try {
        	model.son = new Audio(new File("PacManMusic.wav"));
            model.son.open(); // ouverture du flux
            model.son.play(); // lecture du son
        } catch (Exception e) {
            e.printStackTrace();
        }
        deplacementAllTimer();
    }

    protected void deplacementAllTimer() {
        TimerTask taskDeplacementAll = new TaskDeplacementAll();
        if(this.chrono == null) this.chrono = new Timer();
        chrono.schedule(taskDeplacementAll, 0, 1000/3);
    }

    class TaskDeplacementAll extends TimerTask {
        @Override
        public void run() {
            if (model.getLaunchGame() == false) {
                chrono.cancel();
                chrono.purge();
                chrono = null;
                return;
            }
            deplacementAll();
         }
    }

	protected void deplacementAll() {
		deplacementJoueur();
		colision();
		if (model.getTimerGhost() != 0) {
			model.setTimerGhost(model.getTimerGhost() - 1);
		}
		if (model.isDepartYellow()) {
			f.getLabel(model.tabCoordFantomes[0][0], model.tabCoordFantomes[0][1]).setIcon(null);
			model.tabCoordFantomes[0][0] = 11;
			model.tabCoordFantomes[0][1] = 14;
			model.setDirectionYellow("d");
			model.setDepartYellow(false);
		}
		deplacementYellow();
		colision();

		if (model.getTimerGhost() < 60) {
			if (model.isDepartPink()) {
				f.getLabel(model.tabCoordFantomes[1][0], model.tabCoordFantomes[1][1]).setIcon(null);
				model.tabCoordFantomes[1][0] = 11;
				model.tabCoordFantomes[1][1] = 13;
				model.setDirectionPink("g");
				model.setDepartPink(false);
			}
			deplacementPink();
			colision();
		}

		if (model.getTimerGhost() < 30) {
			if (model.isDepartRed()) {
				f.getLabel(model.tabCoordFantomes[2][0], model.tabCoordFantomes[2][1]).setIcon(null);
				model.tabCoordFantomes[2][0] = 11;
				model.tabCoordFantomes[2][1] = 14;
				model.setDirectionRed("d");
				model.setDepartRed(false);
			}
			deplacementRed();
			colision();
		}

		if (model.getTimerGhost() == 0) {
			if (model.isDepartBlue()) {
				f.getLabel(model.tabCoordFantomes[3][0], model.tabCoordFantomes[3][1]).setIcon(null);
				model.tabCoordFantomes[3][0] = 11;
				model.tabCoordFantomes[3][1] = 13;
				model.setDirectionBlue("g");
				model.setDepartBlue(false);
			}
			deplacementBlue();
			colision();
		}
		victoireJoueur();
		if (model.isSGumActive()) {
			if (model.getSGumTurn() == 0) {
				endSGum();
			}
			else model.setSGumTurn(model.getSGumTurn() - 1);
		}
	}

	protected void deplacementJoueur() {
		direction();
		switch (model.getDirection()) {
			case "h":
				mouvementHaut();
				break;

			case "b":
				mouvementBas();
				break;

			case "d":
				mouvementDroite();
				break;

			case "g":
				mouvementGauche();
				break;
		}
	}

	protected void direction() {
		if (model.getDirection() == model.getDirection2()) {
			return;
		}
		else {
			int[] oldCoords = model.getOldCoords();
			if (model.getDirection2() == "h") {
				if (model.getTabObj(oldCoords[0] - 1, oldCoords[1]) == "" || model.getTabObj(oldCoords[0] - 1, oldCoords[1]) == "gum" || model.getTabObj(oldCoords[0] - 1, oldCoords[1]) == "Sgum") {
					model.setDirection(model.getDirection2());
				}
				else model.setDirection2(model.getDirection());
				return;
			}

			if (model.getDirection2() == "b") {
				if (model.getTabObj(oldCoords[0] + 1, oldCoords[1]) == "" || model.getTabObj(oldCoords[0] + 1, oldCoords[1]) == "gum" || model.getTabObj(oldCoords[0] + 1, oldCoords[1]) == "Sgum") {
					model.setDirection(model.getDirection2());
				}
				else model.setDirection2(model.getDirection());
				return;
			}

			if (model.getDirection2() == "d") {
				if (model.getTabObj(oldCoords[0], oldCoords[1] + 1) == "" || model.getTabObj(oldCoords[0], oldCoords[1] + 1) == "gum" || model.getTabObj(oldCoords[0], oldCoords[1] + 1) == "Sgum") {
					model.setDirection(model.getDirection2());
				}
				else model.setDirection2(model.getDirection());
				return;
			}

			if (model.getDirection2() == "g") {
				if (model.getTabObj(oldCoords[0], oldCoords[1] - 1) == "" || model.getTabObj(oldCoords[0], oldCoords[1] - 1) == "gum" || model.getTabObj(oldCoords[0], oldCoords[1] - 1) == "Sgum") {
					model.setDirection(model.getDirection2());
				}
				else model.setDirection2(model.getDirection());
				return;
			}
			model.setPathJoueur();
		}
	}

	protected void mouvementHaut() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.oldCoords[0];
		oldCoords[1] = model.oldCoords[1];
		if (model.getTabObj(oldCoords[0] - 1, oldCoords[1]) == "" || model.getTabObj(oldCoords[0] - 1, oldCoords[1]) == "gum" || model.getTabObj(oldCoords[0] - 1, oldCoords[1]) == "Sgum") {
            model.setCoords(oldCoords[0] - 1, oldCoords[1]);
			int[] coords;
			coords = model.getCoords();
			if (model.getTabObj(coords[0], coords[1]) == "gum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(10);
			}
            else if (model.getTabObj(coords[0], coords[1]) == "Sgum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(50);
				sGum();
			}
			model.pathJoueur = model.pathPerso + model.direction + model.PATH_GIF;
			model.setTabPerso(coords[0], coords[1], model.pathJoueur);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		else {
			return;
		}
		model.setOldCoords(model.getCoords());
	}

	protected void mouvementBas() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.oldCoords[0];
		oldCoords[1] = model.oldCoords[1];
		if (model.getTabObj(oldCoords[0] + 1, oldCoords[1]) == "" || model.getTabObj(oldCoords[0] + 1, oldCoords[1]) == "gum" || model.getTabObj(oldCoords[0] + 1, oldCoords[1]) == "Sgum") {
            model.setCoords(oldCoords[0] + 1, oldCoords[1]);
			int[] coords;
			coords = model.getCoords();
			if (model.getTabObj(coords[0], coords[1]) == "gum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(10);
			}
            else if (model.getTabObj(coords[0], coords[1]) == "Sgum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(50);
				sGum();
			}
			model.pathJoueur = model.pathPerso + model.direction + model.PATH_GIF;
			model.setTabPerso(coords[0], coords[1], model.pathJoueur);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		else {
			return;
		}
		model.setOldCoords(model.getCoords());
	}

	protected void mouvementGauche() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.oldCoords[0];
		oldCoords[1] = model.oldCoords[1];
		if (teleportPacman()){
			teleportationPacman();
			return;
		}
		else if (model.getTabObj(oldCoords[0], oldCoords[1] - 1) == "" || model.getTabObj(oldCoords[0], oldCoords[1] - 1) == "gum" || model.getTabObj(oldCoords[0], oldCoords[1] - 1) == "Sgum") {
			model.setCoords(oldCoords[0], oldCoords[1] - 1);
			int[] coords;
			coords = model.getCoords();
			if (model.getTabObj(coords[0], coords[1]) == "gum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(10);
			}
            else if (model.getTabObj(coords[0], coords[1]) == "Sgum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(50);
				sGum();
			}
			model.pathJoueur = model.pathPerso + model.direction + model.PATH_GIF;
			model.setTabPerso(coords[0], coords[1], model.pathJoueur);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		else {
			return;
		}
		model.setOldCoords(model.getCoords());
	}

	protected void mouvementDroite() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.oldCoords[0];
		oldCoords[1] = model.oldCoords[1];
		if (teleportPacman()){
			teleportationPacman();
			return;
		}
		else if (model.getTabObj(oldCoords[0], oldCoords[1] + 1) == "" || model.getTabObj(oldCoords[0], oldCoords[1] + 1) == "gum" || model.getTabObj(oldCoords[0], oldCoords[1] + 1) == "Sgum") {
            model.setCoords(oldCoords[0], oldCoords[1] + 1);
			int[] coords;
			coords = model.getCoords();
			if (model.getTabObj(coords[0], coords[1]) == "gum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(10);
			}
            else if (model.getTabObj(coords[0], coords[1]) == "Sgum") {
				model.setTabObj(coords[0], coords[1], "");
				model.decrementNbrGum();
				model.incrementScore(50);
				sGum();
			}
			model.pathJoueur = model.pathPerso + model.direction + model.PATH_GIF;
			model.setTabPerso(coords[0], coords[1], model.pathJoueur);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		else {
			return;
		}
		model.setOldCoords(model.getCoords());
	}

	protected void selectPerso(String perso) {
		model.setChoixPerso(perso);
	}

	protected void initPlayer() {
		for (int i = 0; i < 31; i ++){
			for (int j = 0; j < 28; j ++){
				if (model.getTabPerso(i, j) != "") {
					f.getLabel(i, j).setIcon(new ImageIcon(model.getTabPerso(i, j)));
				}
			}
		}
	}

	protected void initTable() {
		for (int i = 0; i < 31; i ++){
			for (int j = 0; j < 28; j ++){
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
			}
		}
	}

	protected void victoireJoueur(){
		if (model.getNbrGum() == 0) {
			f.creerDialog("Félicitation ! Vous avez gagné \nVous avez réalisé un score de : "+ model.getScore());
			model.finishGame();
			newGame();
		}
	}

	// vérifie si le PacMan est en bout de map
	protected boolean teleportPacman() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.oldCoords[0];
		oldCoords[1] = model.oldCoords[1];

		if (model.getDirection() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) return true;
		else if (model.getDirection() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) return true;

		else return false;
	}

	// effectue la téléportation de PacMan
	protected void teleportationPacman(){
		int[] oldCoords = new int[2];
		oldCoords[0] = model.oldCoords[0];
		oldCoords[1] = model.oldCoords[1];

		int[] coords = new int[2];

		if (model.getDirection() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) {
			coords[0] = 14;
			coords[1] = 0;
			model.coords[0] = 14;
			model.coords[1] = 0;

			model.pathJoueur = model.pathPerso + model.direction + model.PATH_GIF;
			model.setTabPerso(coords[0], coords[1], model.pathJoueur);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);

			model.setOldCoords(model.getCoords());
		}

		else if (model.getDirection() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) {
			coords[0] = 14;
			coords[1] = 27;
			model.coords[0] = 14;
			model.coords[1] = 27;

			model.pathJoueur = model.pathPerso + model.direction + model.PATH_GIF;
			model.setTabPerso(coords[0], coords[1], model.pathJoueur);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);

			model.setOldCoords(model.getCoords());
		}

		return;
	}

	// vérifie si le fantome jaune est en bout de map
	protected boolean teleportYellow() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[0][0];
		oldCoords[1] = model.tabCoordFantomes[0][1];

		if (model.getDirectionYellow() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) return true;
		else if (model.getDirectionYellow() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) return true;

		else return false;
	}

	// effectue la téléportation du fantome jaune
	protected void teleportationYellow(){
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[0][0];
		oldCoords[1] = model.tabCoordFantomes[0][1];

		int[] coords = new int[2];

		if (model.getDirectionYellow() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) {
			coords[0] = 14;
			coords[1] = 0;
			model.tabCoordFantomes[0][0] = 14;
			model.tabCoordFantomes[0][1] = 0;

			model.setTabPerso(coords[0], coords[1], model.pathFantomeYellow);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}

		else if (model.getDirectionYellow() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) {
			coords[0] = 14;
			coords[1] = 27;
			model.tabCoordFantomes[0][0] = 14;
			model.tabCoordFantomes[0][1] = 27;

			model.setTabPerso(coords[0], coords[1], model.pathFantomeYellow);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		return;
	}

	// vérifie si le fantome rose est en bout de map
	protected boolean teleportPink() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[1][0];
		oldCoords[1] = model.tabCoordFantomes[1][1];

		if (model.getDirectionPink() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) return true;
		else if (model.getDirectionPink() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) return true;

		else return false;
	}

	// effectue la téléportation du fantome rose
	protected void teleportationPink(){
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[1][0];
		oldCoords[1] = model.tabCoordFantomes[1][1];

		int[] coords = new int[2];

		if (model.getDirectionPink() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) {
			coords[0] = 14;
			coords[1] = 0;
			model.tabCoordFantomes[1][0] = 14;
			model.tabCoordFantomes[1][1] = 0;

			model.setTabPerso(coords[0], coords[1], model.pathFantomePink);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}

		else if (model.getDirectionPink() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) {
			coords[0] = 14;
			coords[1] = 27;
			model.tabCoordFantomes[1][0] = 14;
			model.tabCoordFantomes[1][1] = 27;

			model.setTabPerso(coords[0], coords[1], model.pathFantomePink);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		return;
	}

	// vérifie si le fantome rouge est en bout de map
	protected boolean teleportRed() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[2][0];
		oldCoords[1] = model.tabCoordFantomes[2][1];

		if (model.getDirectionRed() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) return true;
		else if (model.getDirectionRed() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) return true;

		else return false;
	}

	// effectue la téléportation du fantome rouge
	protected void teleportationRed(){
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[2][0];
		oldCoords[1] = model.tabCoordFantomes[2][1];

		int[] coords = new int[2];

		if (model.getDirectionRed() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) {
			coords[0] = 14;
			coords[1] = 0;
			model.tabCoordFantomes[2][0] = 14;
			model.tabCoordFantomes[2][1] = 0;

			model.setTabPerso(coords[0], coords[1], model.pathFantomeRed);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}

		else if (model.getDirectionRed() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) {
			coords[0] = 14;
			coords[1] = 27;
			model.tabCoordFantomes[2][0] = 14;
			model.tabCoordFantomes[2][1] = 27;

			model.setTabPerso(coords[0], coords[1], model.pathFantomeRed);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		return;
	}

	// vérifie si le fantome bleu est en bout de map
	protected boolean teleportBlue() {
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[3][0];
		oldCoords[1] = model.tabCoordFantomes[3][1];

		if (model.getDirectionBlue() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) return true;
		else if (model.getDirectionBlue() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) return true;

		else return false;
	}

	// effectue la téléportation du fantome bleu
	protected void teleportationBlue(){
		int[] oldCoords = new int[2];
		oldCoords[0] = model.tabCoordFantomes[3][0];
		oldCoords[1] = model.tabCoordFantomes[3][1];

		int[] coords = new int[2];

		if (model.getDirectionBlue() == "d" && oldCoords[0] == 14 && oldCoords[1] == 27) {
			coords[0] = 14;
			coords[1] = 0;
			model.tabCoordFantomes[3][0] = 14;
			model.tabCoordFantomes[3][1] = 0;

			model.setTabPerso(coords[0], coords[1], model.pathFantomeBlue);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}

		else if (model.getDirectionBlue() == "g" && oldCoords[0] == 14 && oldCoords[1] == 0) {
			coords[0] = 14;
			coords[1] = 27;
			model.tabCoordFantomes[3][0] = 14;
			model.tabCoordFantomes[3][1] = 27;

			model.setTabPerso(coords[0], coords[1], model.pathFantomeBlue);
			f.getLabel(coords[0], coords[1]).setIcon(new ImageIcon(model.getTabPerso(coords[0], coords[1])));
			model.setTabPerso(oldCoords[0], oldCoords[1], null);
			f.getLabel(oldCoords[0], oldCoords[1]).setIcon(null);
		}
		return;
	}



	// methode effectuant l'ensemble des actions du fantome jaune
	protected void deplacementYellow() {
		if (verifNoeudYellow()) {
			directionYellow();
		}
		mouvementYellow();
	}

	// methode effectuant l'ensemble des actions du fantome rose
	protected void deplacementPink() {
		if (verifNoeudPink()) {
			directionPink();
		}
		mouvementPink();
	}

	// methode effectuant l'ensemble des actions du fantome rouge
	protected void deplacementRed() {
		if (verifNoeudRed()) {
			directionRed();
		}
		mouvementRed();
	}

	// methode effectuant l'ensemble des actions du fantome bleu
	protected void deplacementBlue() {
		if (verifNoeudBlue()) {
			directionBlue();
		}
		mouvementBlue();
	}

	// fait bouger le fantome jaune par rapport à la direction aléatoire
	protected void mouvementYellow(){
		int i = model.tabCoordFantomes[0][0];
		int j = model.tabCoordFantomes[0][1];
		switch (model.getDirectionYellow()) {
			case "h":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i - 1, j, model.pathFantomeYellow);
				f.getLabel(i - 1, j).setIcon(new ImageIcon(model.getTabPerso(i - 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[0][0] -= 1;
				break;

			case "b":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i + 1, j, model.pathFantomeYellow);
				f.getLabel(i + 1, j).setIcon(new ImageIcon(model.getTabPerso(i + 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[0][0] += 1;
				break;

			case "g":
				if (teleportYellow()){
					teleportationYellow();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j - 1, model.pathFantomeYellow);
					f.getLabel(i, j - 1).setIcon(new ImageIcon(model.getTabPerso(i, j - 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[0][1] -= 1;
					break;
				}

			case "d":
				if (teleportYellow()){
					teleportationYellow();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j + 1, model.pathFantomeYellow);
					f.getLabel(i, j + 1).setIcon(new ImageIcon(model.getTabPerso(i, j + 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[0][1] += 1;
					break;
				}
		}
	}

	// fait bouger le fantome rose par rapport à la direction aléatoire
	protected void mouvementPink(){
		int i = model.tabCoordFantomes[1][0];
		int j = model.tabCoordFantomes[1][1];
		switch (model.getDirectionPink()) {
			case "h":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i - 1, j, model.pathFantomePink);
				f.getLabel(i - 1, j).setIcon(new ImageIcon(model.getTabPerso(i - 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[1][0] -= 1;
				break;

			case "b":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i + 1, j, model.pathFantomePink);
				f.getLabel(i + 1, j).setIcon(new ImageIcon(model.getTabPerso(i + 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[1][0] += 1;
				break;

			case "g":
				if (teleportPink()){
					teleportationPink();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j - 1, model.pathFantomePink);
					f.getLabel(i, j - 1).setIcon(new ImageIcon(model.getTabPerso(i, j - 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[1][1] -= 1;
					break;
				}

			case "d":
				if (teleportPink()){
					teleportationPink();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j + 1, model.pathFantomePink);
					f.getLabel(i, j + 1).setIcon(new ImageIcon(model.getTabPerso(i, j + 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[1][1] += 1;
					break;
				}
		}
	}

	// fait bouger le fantome rouge par rapport à la direction aléatoire
	protected void mouvementRed(){
		int i = model.tabCoordFantomes[2][0];
		int j = model.tabCoordFantomes[2][1];
		switch (model.getDirectionRed()) {
			case "h":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i - 1, j, model.pathFantomeRed);
				f.getLabel(i - 1, j).setIcon(new ImageIcon(model.getTabPerso(i - 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[2][0] -= 1;
				break;

			case "b":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i + 1, j, model.pathFantomeRed);
				f.getLabel(i + 1, j).setIcon(new ImageIcon(model.getTabPerso(i + 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[2][0] += 1;
				break;

			case "g":
				if (teleportRed()){
					teleportationRed();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j - 1, model.pathFantomeRed);
					f.getLabel(i, j - 1).setIcon(new ImageIcon(model.getTabPerso(i, j - 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[2][1] -= 1;
					break;
				}

			case "d":
				if (teleportRed()){
					teleportationRed();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j + 1, model.pathFantomeRed);
					f.getLabel(i, j + 1).setIcon(new ImageIcon(model.getTabPerso(i, j + 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[2][1] += 1;
					break;
				}
		}
	}

	// fait bouger le fantome bleu par rapport à la direction aléatoire
	protected void mouvementBlue(){
		int i = model.tabCoordFantomes[3][0];
		int j = model.tabCoordFantomes[3][1];
		switch (model.getDirectionBlue()) {
			case "h":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i - 1, j, model.pathFantomeBlue);
				f.getLabel(i - 1, j).setIcon(new ImageIcon(model.getTabPerso(i - 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[3][0] -= 1;
				break;

			case "b":
				model.setTabPerso(i, j, null);
				model.setTabPerso(i + 1, j, model.pathFantomeBlue);
				f.getLabel(i + 1, j).setIcon(new ImageIcon(model.getTabPerso(i + 1, j)));
				f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
				model.tabCoordFantomes[3][0] += 1;
				break;

			case "g":
				if (teleportBlue()){
					teleportationBlue();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j - 1, model.pathFantomeBlue);
					f.getLabel(i, j - 1).setIcon(new ImageIcon(model.getTabPerso(i, j - 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[3][1] -= 1;
					break;
				}

			case "d":
				if (teleportBlue()){
					teleportationBlue();
					return;
				}
				else {
					model.setTabPerso(i, j, null);
					model.setTabPerso(i, j + 1, model.pathFantomeBlue);
					f.getLabel(i, j + 1).setIcon(new ImageIcon(model.getTabPerso(i, j + 1)));
					f.getLabel(i, j).setIcon(new ImageIcon(model.PATH_MUR + model.plateauObjet[i][j] + model.PATH_PNG));
					model.tabCoordFantomes[3][1] += 1;
					break;
				}
		}
	}

	// donne une direction aléatoire au fantome jaune s'il n'y a pas de mur
	protected void directionYellow(){
		boolean mouvementSet = false;

		do {
			int rand = (int)(Math.random() * 4);
			int i = model.tabCoordFantomes[0][0];
			int j = model.tabCoordFantomes[0][1];

			if (rand == 0) {
				if (model.getTabObj(i - 1, j) == "" || model.getTabObj(i - 1, j) == "gum" || model.getTabObj(i - 1, j) == "Sgum") {
					model.setDirectionYellow("h");
					mouvementSet = true;
				}
			}
			else if (rand == 1) {
				if (model.getTabObj(i + 1, j) == "" || model.getTabObj(i + 1, j) == "gum" || model.getTabObj(i + 1, j) == "Sgum") {
					model.setDirectionYellow("b");
					mouvementSet = true;
				}
			}
			else if (rand == 2) {
				if (model.getTabObj(i, j + 1) == "" || model.getTabObj(i, j + 1) == "gum" || model.getTabObj(i, j + 1) == "Sgum") {
					model.setDirectionYellow("d");
					mouvementSet = true;
				}
			}
			else if (rand == 3) {
				if (model.getTabObj(i, j - 1) == "" || model.getTabObj(i, j - 1) == "gum" || model.getTabObj(i, j - 1) == "Sgum") {
					model.setDirectionYellow("g");
					mouvementSet = true;
				}
			}
		} while (!mouvementSet);
	}

	// donne une direction aléatoire au fantome rose s'il n'y a pas de mur
	protected void directionPink(){
		boolean mouvementSet = false;

		do {
			int rand = (int)(Math.random() * 4);
			int i = model.tabCoordFantomes[1][0];
			int j = model.tabCoordFantomes[1][1];

			if (rand == 0) {
				if (model.getTabObj(i - 1, j) == "" || model.getTabObj(i - 1, j) == "gum" || model.getTabObj(i - 1, j) == "Sgum") {
					model.setDirectionPink("h");
					mouvementSet = true;
				}
			}
			else if (rand == 1) {
				if (model.getTabObj(i + 1, j) == "" || model.getTabObj(i + 1, j) == "gum" || model.getTabObj(i + 1, j) == "Sgum") {
					model.setDirectionPink("b");
					mouvementSet = true;
				}
			}
			else if (rand == 2) {
				if (model.getTabObj(i, j + 1) == "" || model.getTabObj(i, j + 1) == "gum" || model.getTabObj(i, j + 1) == "Sgum") {
					model.setDirectionPink("d");
					mouvementSet = true;
				}
			}
			else if (rand == 3) {
				if (model.getTabObj(i, j - 1) == "" || model.getTabObj(i, j - 1) == "gum" || model.getTabObj(i, j - 1) == "Sgum") {
					model.setDirectionPink("g");
					mouvementSet = true;
				}
			}
		} while (!mouvementSet);
	}

	// donne une direction aléatoire au fantome rouge s'il n'y a pas de mur
	protected void directionRed(){
		boolean mouvementSet = false;

		do {
			int rand = (int)(Math.random() * 4);
			int i = model.tabCoordFantomes[2][0];
			int j = model.tabCoordFantomes[2][1];

			if (rand == 0) {
				if (model.getTabObj(i - 1, j) == "" || model.getTabObj(i - 1, j) == "gum" || model.getTabObj(i - 1, j) == "Sgum") {
					model.setDirectionRed("h");
					mouvementSet = true;
				}
			}
			else if (rand == 1) {
				if (model.getTabObj(i + 1, j) == "" || model.getTabObj(i + 1, j) == "gum" || model.getTabObj(i + 1, j) == "Sgum") {
					model.setDirectionRed("b");
					mouvementSet = true;
				}
			}
			else if (rand == 2) {
				if (model.getTabObj(i, j + 1) == "" || model.getTabObj(i, j + 1) == "gum" || model.getTabObj(i, j + 1) == "Sgum") {
					model.setDirectionRed("d");
					mouvementSet = true;
				}
			}
			else if (rand == 3) {
				if (model.getTabObj(i, j - 1) == "" || model.getTabObj(i, j - 1) == "gum" || model.getTabObj(i, j - 1) == "Sgum") {
					model.setDirectionRed("g");
					mouvementSet = true;
				}
			}
		} while (!mouvementSet);
	}

	// donne une direction aléatoire au fantome bleu s'il n'y a pas de mur
	protected void directionBlue(){
		boolean mouvementSet = false;

		do {
			int rand = (int)(Math.random() * 4);
			int i = model.tabCoordFantomes[3][0];
			int j = model.tabCoordFantomes[3][1];

			if (rand == 0) {
				if (model.getTabObj(i - 1, j) == "" || model.getTabObj(i - 1, j) == "gum" || model.getTabObj(i - 1, j) == "Sgum") {
					model.setDirectionBlue("h");
					mouvementSet = true;
				}
			}
			else if (rand == 1) {
				if (model.getTabObj(i + 1, j) == "" || model.getTabObj(i + 1, j) == "gum" || model.getTabObj(i + 1, j) == "Sgum") {
					model.setDirectionBlue("b");
					mouvementSet = true;
				}
			}
			else if (rand == 2) {
				if (model.getTabObj(i, j + 1) == "" || model.getTabObj(i, j + 1) == "gum" || model.getTabObj(i, j + 1) == "Sgum") {
					model.setDirectionBlue("d");
					mouvementSet = true;
				}
			}
			else if (rand == 3) {
				if (model.getTabObj(i, j - 1) == "" || model.getTabObj(i, j - 1) == "gum" || model.getTabObj(i, j - 1) == "Sgum") {
					model.setDirectionBlue("g");
					mouvementSet = true;
				}
			}
		} while (!mouvementSet);
	}

	// verifie que le fantome jaune est sur un noeud
	protected boolean verifNoeudYellow() {
		for(int i = 0; i < 64; i++) {
			for(int j = 0; j < 2; j++) {
				if(model.tabCoordFantomes[0][0] == model.TAB_NOEUD[i][0] && model.tabCoordFantomes[0][1] == model.TAB_NOEUD[i][1]){
					return true;
				}
			}
		}
		return false;
	}

	// verifie que le fantome rose est sur un noeud
	protected boolean verifNoeudPink() {
		for(int i = 0; i < 64; i++) {
			for(int j = 0; j < 2; j++) {
				if(model.tabCoordFantomes[1][0] == model.TAB_NOEUD[i][0] && model.tabCoordFantomes[1][1] == model.TAB_NOEUD[i][1]){
					return true;
				}
			}
		}
		return false;
	}

	// verifie que le fantome rouge est sur un noeud
	protected boolean verifNoeudRed() {
		for(int i = 0; i < 64; i++) {
			for(int j = 0; j < 2; j++) {
				if(model.tabCoordFantomes[2][0] == model.TAB_NOEUD[i][0] && model.tabCoordFantomes[2][1] == model.TAB_NOEUD[i][1]){
					return true;
				}
			}
		}
		return false;
	}

	// verifie que le fantome bleu est sur un noeud
	protected boolean verifNoeudBlue() {
		for(int i = 0; i < 64; i++) {
			for(int j = 0; j < 2; j++) {
				if(model.tabCoordFantomes[3][0] == model.TAB_NOEUD[i][0] && model.tabCoordFantomes[3][1] == model.TAB_NOEUD[i][1]){
					return true;
				}
			}
		}
		return false;
	}

	protected void sGum(){
		model.setSGumActive(true);
		model.setSGumTurn(50);

		model.setPathFantomeRed(model.getPathFantomeWeak());
		model.setPathFantomeBlue(model.getPathFantomeWeak());
		model.setPathFantomePink(model.getPathFantomeWeak());
		model.setPathFantomeYellow(model.getPathFantomeWeak());
	}

	protected void endSGum(){
		model.setSGumActive(false);
		model.setGhostEat(1);

		model.setPathFantomeRed(model.getPATH_RED());
		model.setPathFantomeBlue(model.getPATH_BLUE());
		model.setPathFantomePink(model.getPATH_PINK());
		model.setPathFantomeYellow(model.getPATH_YELLOW());
	}

	protected void colision() {
		for (int i = 0; i < 4; i ++) {
			if(model.tabCoordFantomes[i][0] == model.oldCoords[0] && model.tabCoordFantomes[i][1] == model.oldCoords[1]){
				if(model.isSGumActive()){
					switch (i) {
						case 0:
							model.setDepartYellow(true);
							model.incrementScore(200 * model.getGhostEat());
							model.setGhostEat(model.getGhostEat() + 1);
							model.setDirectionYellow("d");
							break;
						case 1:
						 	model.setDepartPink(true);
							model.incrementScore(200 * model.getGhostEat());
							model.setGhostEat(model.getGhostEat() + 1);
							model.setDirectionPink("g");
							break;
						case 2:
							model.setDepartRed(true);
							model.incrementScore(200 * model.getGhostEat());
							model.setGhostEat(model.getGhostEat() + 1);
							model.setDirectionRed("d");
							break;
						case 3:
							model.setDepartBlue(true);
							model.incrementScore(200 * model.getGhostEat());
							model.setGhostEat(model.getGhostEat() + 1);
							model.setDirectionBlue("g");
							break;
					}
				}

				else defaiteJoueur();
			}
		}
	}

	protected void defaiteJoueur() {
		if (model.getLaunchGame()){
			f.creerDialog("Aïe, dure défaite pas vrai ? \nVous avez réalisé un score de : "+ model.getScore());
			model.finishGame();
			newGame();
		}
	}
}
