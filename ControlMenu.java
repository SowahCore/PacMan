import java.awt.event.*;
import javax.swing.*;

public class ControlMenu extends Controller implements ActionListener{

    public ControlMenu(Model model, Vue f) {
		super(model, f);
		f.setMenuControler(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == f.getNouvellePartie()) { super.newGame(); }
		if (e.getSource() == f.getAPropos()) { f.creerDialog("PacMan realiser par : \n\n- Maxime Dronnier (chef de projet)\n- Noah Sochandamandon (manoeuvre de haute performance) \n (5 ou 6 étudiants : he he he, quoi ?)"); }
		if (e.getSource() == f.getPacmanM()) { super.selectPerso("pacman"); }
		if (e.getSource() == f.getPikachuM()) { super.selectPerso("pikachu"); }
		if (e.getSource() == f.getSonicM()) { super.selectPerso("sonic"); }
		if (e.getSource() == f.getVoltaliM()) { super.selectPerso("voltali"); }
		if (e.getSource() == f.getMegamanM()) { super.selectPerso("megaman"); }
    }
}
