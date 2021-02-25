public class ControlGroup{

	private Model model;
  	private Vue f;
  	public ControlKey controlKey;
	public ControlMenu controlMenu;

	public ControlGroup(Model model) {
		this.model = model;
		this.f = new Vue(model);

		controlKey = new ControlKey(model, f);
		controlMenu = new ControlMenu(model, f);
	}
}
