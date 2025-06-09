package test.drag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


@SuppressWarnings("serial")
public class _RP extends JPanel {

    private boolean drag = false;
    private boolean top =false;
    private boolean bottom =false;
    private boolean left=false;
    private boolean right =false;
    private Point dragLocation  = new Point();
    public  _RP() {
       // setMinimumSize(new Dimension(200, 200));
    	add(table());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                evaluateMousePress(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                drag = false;
                setCursor(Cursor.getDefaultCursor());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                evaluateMouseDrag(e.getPoint());
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                evaluateMouseHover(e.getPoint());
            }
        });
        
        JFrame frame= new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(this);
        JPanel panel= new JPanel();
        panel.add(new JButton("button"));
        panel.add(new JButton("button"));
        panel.add(new JButton("button"));
        panel.add(new JButton("button"));
        panel.add(new JButton("button"));
        panel.setBackground(Color.black);
        panel.setPreferredSize(new Dimension(800,100));
        frame.add(panel,BorderLayout.SOUTH);
        JPanel pane= new JPanel();
        pane.setBackground(Color.black);
        pane.setPreferredSize(new Dimension(800,100));
        frame.add(pane,BorderLayout.NORTH);
        frame.setSize(new Dimension(800,400));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }
    @Override
    public void setBounds(int x, int y, int width, int height) {
        Rectangle oldBounds=getBounds();
        Rectangle newBounds = new Rectangle(x, y, width, height);
        super.setBounds(x, y, width, height);
        resizeSingleComponent(this,oldBounds, newBounds);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke stroke = g2.getStroke();
        g2.setStroke(new BasicStroke(5));
        g.drawRect(0, 0, getBounds().width, getBounds().height);
        g2.setStroke(stroke);
    }
    private void resizeSingleComponent(Component component , Rectangle oldBounds , Rectangle newBounds) {
        
        if(component instanceof Container) {
            if(!component.equals(this)) {
                Rectangle componentBounds = component.getBounds();
                double x = getNew(componentBounds.getX(), oldBounds.getWidth(), newBounds.getWidth());
                double y = getNew(componentBounds.getY(), oldBounds.getHeight(), newBounds.getHeight());
                double width = getNew(componentBounds.getWidth(), oldBounds.getWidth(), newBounds.getWidth());
                double height = getNew(componentBounds.getHeight(), oldBounds.getHeight(), newBounds.getHeight());
                componentBounds.width = (int) Math.round(width);
                componentBounds.height = (int) Math.round(height);
                componentBounds.x = (int) Math.round(x);
                componentBounds.y = (int) Math.round(y);
                component.setBounds(componentBounds);
            }
            for(Component child:((Container) component).getComponents())
                resizeSingleComponent(child, oldBounds, newBounds);
        }
        else {
            Rectangle componentBounds = component.getBounds();
            double x = getNew(componentBounds.getX(), oldBounds.getWidth(), newBounds.getWidth());
            double y = getNew(componentBounds.getY(), oldBounds.getHeight(), newBounds.getHeight());
            double width = getNew(componentBounds.getWidth(), oldBounds.getWidth(), newBounds.getWidth());
            double height = getNew(componentBounds.getHeight(), oldBounds.getHeight(), newBounds.getHeight());
            componentBounds.width = (int) Math.round(width);
            componentBounds.height = (int) Math.round(height);
            componentBounds.x = (int) Math.round(x);
            componentBounds.y = (int) Math.round(y);
            component.setBounds(componentBounds);
        }
    }
    
    
    private void evaluateMouseDrag(Point point) {
        if (drag) {
            if (top || left || right || bottom) {
                int heightIncrement=0;
                int widthIncrement=0;
                int posX = getBounds().x;
                int posY = getBounds().y;
                if(top || bottom) 
                    heightIncrement = (int) (point.getY()-dragLocation.getY());
                if(left || right)
                    widthIncrement = (int) (point.getX()-dragLocation.getX());
                if(top) {
                    posY = posY+heightIncrement;
                    heightIncrement=-heightIncrement;
                }
                if(left) {
                    posX=posX+widthIncrement;
                    widthIncrement=-widthIncrement;
                }
                if(right)
                    dragLocation.x=(int) point.getX();
                if(bottom)
                    dragLocation.y=(int) point.getY();
                    
                Rectangle bounds = new Rectangle(posX, posY, getWidth()+ widthIncrement,getHeight()+ heightIncrement);    
                justifyCollision(bounds,point);
            }
        }
    }
    private void evaluateMousePress(Point point) {
         drag = true;
         dragLocation = point;
         top=dragLocation.getY() <=5;
         left=dragLocation.getX() <=5;
         bottom = dragLocation.getY() >=getHeight()-5;
         right = dragLocation.getX() >=getWidth()-5;
         setResizeCursor(point);
    }
    private void evaluateMouseHover(Point point) {
        if(!drag) {
            top=point.getY() <=5;
            left=point.getX() <=5;
            bottom = point.getY() >=getHeight()-5;
            right = point.getX() >=getWidth()-5;
            setResizeCursor(point);
        }
   }
    
    private void justifyCollision(Rectangle bounds, Point point) {
        //justify undersizing
        if(bounds.width<getMinimumSize().width) {
            if(left)
                bounds.x= getBounds().x;
            bounds.width=getWidth();
        }
        if(bounds.height<getMinimumSize().height) {
            if(top)
                bounds.y=getBounds().y;
            bounds.height=getHeight();
        }
        Container parent = getParent();
        //justify parent container bounds
        if(bounds.x<0 ||bounds.width+bounds.x>parent.getWidth()) {
            bounds.width = getWidth();
            if(right &&!(bounds.width+bounds.x>parent.getWidth()))
                dragLocation.x=(int) point.getX();
            if(bounds.x<0)
                bounds.x=0;
        }
        if(bounds.y<0 ||bounds.height+bounds.y>parent.getHeight()) {
            bounds.height = getHeight();
            if(bottom &&!(bounds.height+bounds.y>parent.getHeight()))
                dragLocation.y=(int) point.getY();
            if(bounds.y<0)
                bounds.y=0;
        }
        //justify any other component in it's way
        for(Component c:parent.getComponents()) {
            if(!c.equals(this) &&c.getBounds().intersects(bounds)) {
                evaluateOverlaps(bounds, c,point);
            }
        }
        setBounds(bounds);
    }
    private Rectangle evaluateOverlaps(Rectangle bounds , Component component,Point point) {
        if(bounds.intersects(component.getBounds())) {
            if(component instanceof _RP) {
                _RP panel=(_RP) component;
                Rectangle panelBound=panel.getBounds();
                if(top || bottom) {
                    panelBound.height = panelBound.height - bounds.intersection(panelBound).height;
                    if(panelBound.height<panel.getMinimumSize().height) {
                        panelBound.height=panel.getMinimumSize().height;
                        bounds.height = getHeight();
                        if(top) {
                            bounds.y = component.getBounds().y+component.getHeight();
                        }
                    }
                    else if(bottom) {
                        panelBound.y = panelBound.y + bounds.intersection(panelBound).height;
                    }
                }
                else if(left || right) {
                    panelBound.width = panelBound.width - bounds.intersection(panelBound).width;
                    if(panelBound.width<panel.getMinimumSize().width) {
                        panelBound.width=panel.getMinimumSize().width;
                        bounds.width = getWidth();
                        if(left) {
                            bounds.x = component.getBounds().x+component.getWidth();
                        }
                    }
                    else if(right) {
                        panelBound.x = panelBound.x + bounds.intersection(panelBound).width;
                    }
                }
                panel.setBounds(panelBound);
                return bounds;
            }
            
            if(left || right) {
                bounds.width = getWidth();
                if(left) {
                    bounds.x = component.getBounds().x+component.getWidth();
                }
            }
            if(top || bottom) {
                bounds.height = getHeight();
                if(top) {
                    bounds.y = component.getBounds().y+component.getHeight();
                }
            }
        }
        return bounds;
    }
    private void setResizeCursor(Point point) {
        if(top) { 
            if(left) 
                setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            else if(right)  
                setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
            else 
                setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        }
        else if(bottom) { 
            if(left)  
                setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
            else if(right) 
                setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
            else 
                setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        }
        else if (left) 
            setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        else if (right)
            setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        else
            setCursor(Cursor.getDefaultCursor());
    }
    
    private long getNew(double size,double oldMax,double newMax) {
        double value = ((size/oldMax)*newMax);
        return Math.round(value);
    }
    public static void main(String[]args) {
   new _RP();
    }
    
    JScrollPane table() {
        // Creer un JTable
        JTable table=new JTable(34,8);
        // AJouter le JTable dans le JFrame
       return new JScrollPane(table);
    }

}
