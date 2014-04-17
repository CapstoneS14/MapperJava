import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JFileChooser;

import netscape.javascript.JSObject;

public class LauncherApplet extends Applet{
  
	private static final long serialVersionUID = 2467897672237407225L;
	private static File dataFile; 
	
	public void init(){
		try{
			//System.setSecurityManager(null);
			//System.setProperty("java.security.policy", "mapper.policy"); 
			//Policy.getPolicy().refresh();
			//System.setSecurityManager(new SecurityManager());
			System.out.println("Applet getting initialized..");
			Object[] args = { this };
			JSObject.getWindow(this).call("appletLoaded", args);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
/*	public void start(){
		try{
		//	SecurityManager sm = new MySecurityManager();
		//	System.setSecurityManager(sm);
			System.out.println("Applet started..");
		}catch(Exception e){
			e.printStackTrace();
		}				
	}*/
	
	public void readFile( String id ) throws IOException
	{
		JSObject window = JSObject.getWindow(this);
		//String filePath = (String) window.eval( "document.getElementById( '" + id + "' )" );
		System.out.println("filePath ==== " + id);
		//File file = new File(id);
		//		Object[] args = { this };
//		JSObject.getWindow(this).call("readFileCalled", args);
/*		JSObject.getWindow(this).eval( "document.getElementById( '" + id + "' ).appendChild( document.createTextNode( '" 
				+ HELLO_WORLD + " (via applet.sendMsgUsingEval())' ) );" );
		JSObject.getWindow(this).eval( "document.getElementById( '" + id + "' ).appendChild( "
				+ "document.createElement( 'br' ) );" );*/
	}
	
	public void chooseFile( ) {
		try{
			JSObject window = JSObject.getWindow(this);
			System.out.println("Hi there");
			
			
			AccessController.doPrivileged(new PrivilegedAction<File>() {
			    
	            public File run() {
	            	JFileChooser chooser = new JFileChooser();
	    		    chooser.setMultiSelectionEnabled(false);
	    		    int option = chooser.showOpenDialog(chooser);
	    		    if (option == JFileChooser.APPROVE_OPTION) {
	    		      File fileChosen = chooser.getSelectedFile();
	    		      System.out.println("File Path="+fileChosen.getAbsolutePath());       
	    		    }
	    		    return null; // nothing to return
	            }
	        });
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*private class MySecurityManager extends SecurityManager {
	    @Override
	    public void checkPermission(Permission perm) {
	        return;
	    }
	}*/
}	