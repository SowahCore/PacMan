import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class ControlKey extends Controller implements KeyListener {

	public ControlKey(Model model, Vue f) {
	    super(model, f);
	    f.setKeyController(this);
		super.newGame();
	}

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				model.setDirection2("g");
				if (!model.getLaunchGame()) {
					model.lancerLaGame();
					super.play();
				}
				break;

			case KeyEvent.VK_RIGHT:
				model.setDirection2("d");
				if (!model.getLaunchGame()) {
					model.lancerLaGame();
					super.play();
				}
				break;

			case KeyEvent.VK_UP:
				model.setDirection2("h");
				if (!model.getLaunchGame()) {
					model.lancerLaGame();
					super.play();
				}
				break;

			case KeyEvent.VK_DOWN:
				model.setDirection2("b");
				if (!model.getLaunchGame()) {
					model.lancerLaGame();
					super.play();
				}
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}
}
