import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Vue extends JFrame {

    protected Model model;

    // variable pour le controle du menu
    // protected ControlMenu controlMen;

    // tableau de boutons
    protected JLabel[][] tabLabels;

    // composant du Menu
    protected JMenuBar barMenu;
    protected JMenu menu;
	protected JMenu subMenu;
    protected JMenuItem mNouvellePartie;
    protected JMenuItem mAPropos;
	protected JMenuItem pacman;
	protected JMenuItem pikachu;
	protected JMenuItem sonic;
	protected JMenuItem voltali;
	protected JMenuItem megaman;

    public Vue(Model model) {

        this.model = model;

        initAttribut();
        // creerMenu();
        creerVue();
		initMenu();

        setTitle("PacManMusic");
        setVisible(true);
        setResizable(false);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // initialise tout les attribue de la classe Vue pour l'affichage
    public void initAttribut() {

        // initialisation du menu et des sous-menu associés.
        barMenu = new JMenuBar();
        menu = new JMenu("Options");
		subMenu = new JMenu("Skin");
		pacman = new JMenuItem("pacman");
		pikachu = new JMenuItem("pikachu");
		sonic = new JMenuItem("sonic");
		voltali = new JMenuItem("voltali");
		megaman = new JMenuItem("megaman");
        mNouvellePartie = new JMenuItem("Nouvelle partie");
        mAPropos = new JMenuItem("A Propos");

        // initialisation des boutons à une taille prédéfinie.
        tabLabels = new JLabel[31][28];

        // remplissage du tableau par des JLabel sans bordure, de taille 32x32 pixels avec un fond noir.
        for(int i = 0; i < 31; i++) {
            for(int j = 0; j < 28; j++) {
                tabLabels[i][j] = new JLabel();
                tabLabels[i][j].setBorder(BorderFactory.createEmptyBorder());
                tabLabels[i][j].setPreferredSize(new Dimension(32, 32));
                tabLabels[i][j].setOpaque(true);
                tabLabels[i][j].setBackground(Color.BLACK);
            }
        }
    }

    // création des panels pour l'affichage
    public void creerVue() {

        // création d'un panel avec un GridLayout pour que les boutons remplissent tout l'espace disponible
        // création d'un panel général qui contient les autres panels
        JPanel grid = new JPanel(new GridLayout(31,28));
        JPanel panAll = new JPanel();

        // ajout de chaque JLabel dans le panel avec le GridLayout
        for(int i = 0; i < 31; i++) {
            for(int j = 0; j < 28; j++) {
                grid.add(tabLabels[i][j]);
            }
        }

        // ajout du panel grid dans le panel général
        panAll.add(grid);

        setContentPane(panAll);
    }

    public JLabel getLabel(int i, int j){
        return tabLabels[i][j];
    }

    public void setKeyController(ControlKey listener) {
        addKeyListener(listener);
    }

  // ajout de tout les composants du menu pour l'affichage
	public void initMenu() {
		menu.add(mNouvellePartie);
		menu.add(subMenu);
		subMenu.add(pacman);
		subMenu.add(pikachu);
		subMenu.add(sonic);
		subMenu.add(voltali);
		subMenu.add(megaman);
	    menu.add(mAPropos);
		barMenu.add(menu);
		setJMenuBar(barMenu);
	}

	public void setMenuControler(ActionListener listener){
		mNouvellePartie.addActionListener(listener);
		mAPropos.addActionListener(listener);
		pacman.addActionListener(listener);
		pikachu.addActionListener(listener);
		sonic.addActionListener(listener);
		voltali.addActionListener(listener);
		megaman.addActionListener(listener);
	}

	public JMenuItem getNouvellePartie() {
		return mNouvellePartie;
	}
	public JMenuItem getAPropos() {
		return mAPropos;
	}
	public JMenuItem getPacmanM() {
		return pacman;
	}
	public JMenuItem getPikachuM() {
		return pikachu;
	}
	public JMenuItem getSonicM() {
		return sonic;
	}
	public JMenuItem getVoltaliM() {
		return voltali;
	}
	public JMenuItem getMegamanM() {
		return megaman;
	}

	public void creerDialog(String msg){
		JOptionPane dialog = new JOptionPane();
		dialog.showMessageDialog(this, msg);
		JDialog fenDialog = dialog.createDialog(this, msg);
	}

  // retourne la position d'un boutons du tableau
	public JLabel getButton(int i, int j) { return tabLabels[i][j]; }
}
