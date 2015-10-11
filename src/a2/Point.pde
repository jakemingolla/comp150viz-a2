public class Point {
    private int x,y;

    Point() {
        x = 0;
        y = 0;
    }
    
    Point(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int _x) {
        x = _x;
    }

    public void setX(int _y) {
        y = _y;
    }
}
