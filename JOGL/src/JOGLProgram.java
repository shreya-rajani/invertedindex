import javax.media.opengl.*;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.media.opengl.awt.GLJPanel;

public class JOGLProgram extends GLJPanel implements GLEventListener {

    public static void main(String[] args) {
        JFrame window = new JFrame("JOGL Program");
        JOGLProgram panel = new JOGLProgram();
        window.setContentPane(panel);
        window.pack();
        window.setLocation(50,50);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public JOGLProgram() {
        setPreferredSize( new Dimension(500,500) );
        addGLEventListener(this);
    }
    
    // ---------------  Methods of the GLEventListener interface -----------

    public void init(GLAutoDrawable drawable) {
            // called when the panel is created
        GL2 gl = drawable.getGL().getGL2();
        // Add initialization code here!
   }

    public void display(GLAutoDrawable drawable) {    
            // called when the panel needs to be drawn
        GL2 gl = drawable.getGL().getGL2();
        // Add drawing code here!
        //glBegin();
        
    }

    public void reshape(GLAutoDrawable drawable,
                              int x, int y, int width, int height) {
        // called when user resizes the window
    }

    public void dispose(GLAutoDrawable drawable) {
        // called when the panel is being disposed
    }

}