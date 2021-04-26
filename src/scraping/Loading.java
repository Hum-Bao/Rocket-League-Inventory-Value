package scraping;
import java.lang.Thread;
import javax.swing.SwingWorker;
import java.util.List;

public class Loading  {
	private static byte anim;
	protected static String animText = "";
	protected static void animate() {
		
		 switch (anim) {
         case 1:
        	 animText = "[ \\ ]";
             break;
         case 2:
        	 animText = "[ | ]";
             break;
         case 3:
        	 animText = "[ / ]";
             break;
         default:
             anim = 0;
             animText = "[ - ]";
     }
     anim++;
 }
	protected static void worker () {
	SwingWorker<Void, String> loadingThread = new SwingWorker<Void, String>() {
		@Override
		protected Void doInBackground() throws Exception {
			while (!Scraper.printValue) {
				animate();
				publish(animText);
				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {
					System.out.println("Thread 2 interrupted");
				}
			}
			return null;
		}
		@Override
		protected void process(List<String> chunks) {
			String animTextOut = chunks.get(chunks.size()-1);
			GUI.output.setText(animTextOut);
		}
		@Override
		protected void done() {

		}
	};
	loadingThread.execute();
	}
	
	
}
