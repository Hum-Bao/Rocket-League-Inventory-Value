package scraping;
import java.lang.Thread;
import javax.swing.SwingWorker;
import java.util.List;

public class Loading  {
	private static byte anim;
	private static String animText = "";
	public static void animate() {
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
	public static void worker () {
	SwingWorker<Void, String> loadingThread = new SwingWorker<Void, String>() {
		@Override
		public Void doInBackground() throws Exception {
			while (!Scraper.getPrintValue()) {
				animate();
				publish(animText);
				try {Thread.sleep(900);} catch (InterruptedException e) {System.out.println("Thread 2 interrupted");}
			}
			return null;
		}
		@Override
		public void process(List<String> chunks) {
			String animTextOut = chunks.get(chunks.size()-1);
			GUI.updateOutput(animTextOut);
		}
	};
	loadingThread.execute();
	}
	
	
}
