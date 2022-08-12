package NumberClassifier.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

/**
 * Panel where user can draw an image.
 */
public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private double[] image;
    private int imageWidth, imageHeight;
    private Runnable onChanged;

    /**
     * Constructs a new DrawPanel.
     * @param width Width of the image in pixels.
     * @param height Height of the image in pixels.
     */
    public DrawPanel( int width, int height ) {
        this.imageWidth = width;
        this.imageHeight = height;
        this.image = new double[height*width];

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Get the drawn image.
     * @return The image data. The array has height*width elements and is stored in row-major order, top row first.
     */
    public double[] getImage() {
        return image;
    }

    /**
     * Set callback to be called when the image is changed.
     * @param onChanged
     */
    public void setOnChanged( Runnable onChanged ) {
        this.onChanged = onChanged;
    }

    /**
     * Implements MouseListener interface. Doesn't do anything.
     */
    public void mouseClicked(MouseEvent event) {
    }

    /**
     * Implements MouseListener interface. Draws to the image.
     */
    public void mousePressed(MouseEvent event) {
        draw( event.getX(), event.getY() );
    }

    /**
     * Implements MouseListener interface. Doesn't do anything.
     */
    public void mouseReleased(MouseEvent event) {    
    }

    private int imageToPanelX(int imageX) {      
        int x = (int)(imageX * getWidth() / imageWidth);
        return x;        
    }

    private int imageToPanelY(int imageY) {
        int y = (int)(imageY * getHeight() / imageHeight);
        return y;
    }

    private int panelToImageX( int x ) {
        int imageX = (int)(x * imageWidth / getWidth());
        if (imageX < 0) {
            imageX = 0;
        } else if (imageX >= imageWidth) {
            imageX = imageWidth - 1;
        }
        return imageX;        
    }

    private int panelToImageY( int y ) {
        int imageY = (int)(y * imageHeight / getHeight());
        if (imageY < 0) {
            imageY = 0;
        } else if (imageY >= imageHeight) {
            imageY = imageHeight - 1;
        }
        return imageY;
    }

    private void draw(int x, int y) {
        int x_index = panelToImageX(x);
        int y_index = panelToImageY(y);

        image[y_index * imageWidth + x_index] = 1;

        System.out.println(x_index + " " + y_index);

        repaint();  

        if (onChanged != null) {
            onChanged.run();
        }
    }

    /**
     * Implements MouseMotionListener interface. Doesn't do anything.
     */
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Implements MouseMotionListener interface. Doesn't do anything.
     */
    public void mouseExited(MouseEvent event) {
    }

    /**
     * Implements MouseMotionListener interface. Draws to the image.
     */
    public void mouseDragged(MouseEvent event) {
        draw( event.getX(), event.getY() );
    }

    /**
     * Implements MouseMotionListener interface. Doesn't do anything.
     */
    public void mouseMoved(MouseEvent event) {
    }

    /**
     * Render the image.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                int x = imageToPanelX(j);
                int y = imageToPanelY(i);

                int rectWidth = imageToPanelX(j + 1) - x;
                int rectHeight = imageToPanelY(i + 1) - y;

                float pixel = 1.0f - (float)image[i * imageWidth + j];
                g.setColor(new Color(pixel, pixel, pixel));
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
    }


    
}
