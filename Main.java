import javax.swing.UIManager;
//this project was developed by a team of 5 people inclusing myself
public class Main {

	public static void main(String[] args) {

		// this code is just so it looks the same on a mac as windows
		try {
			UIManager.setLookAndFeel( 
				UIManager.getCrossPlatformLookAndFeelClassName() 
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new ProgramController();
	}

}
