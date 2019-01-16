public class Position {
    private int X;
    private int Y;

    public Position() {

    }

    public Position(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public void incrX() {
        this.X++;
    }

    public void decrX() {
        this.X--;
    }

    public void incrY() {
        this.Y++;
    }

    public void decrY() {
        this.Y--;
    }
}