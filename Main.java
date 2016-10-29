import javax.swing.UIManager;

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
