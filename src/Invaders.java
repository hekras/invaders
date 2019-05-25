
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author oldhandmixer
 */
public class Invaders extends Application {

    final static int STARTLEVEL = 2;
    int levels[][] = {
        // Level 0
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        // Level 1
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 0},
        {1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
        {1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
        {1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
        {0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0},
        // Level 2
        {12, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},};

    final double height = 1080;
    final double width = 1920;
    AnimationTimer timer;
    final Canvas canvas = new Canvas(width, height);
    PGoodBoi player = new PGoodBoi(width / 2, height - 80);
    PVector center;
    List<BadBoiClass> baddies = new ArrayList<>();
    List<PDrops> missiles = new ArrayList<>();
    List<PBomb> bombs = new ArrayList<>();
    List<PBackgroundSprite> pbs = new ArrayList<>();
    List<PBoom> boom = new ArrayList<>();
    double mousex, mousey;
    boolean kdown = false;
    boolean kup = false;
    boolean kleft = false;
    boolean krigth = false;

    @Override

    public void start(Stage stage) {
        Group root = new Group();
        Scene s = new Scene(root, width, height, Color.BLACK);
        center = new PVector(width / 2, height / 2);
        player.loc = new PVector(center.x, center.y);

        s.setOnMouseMoved((MouseEvent e) -> {
            mousex = e.getSceneX();
            mousey = e.getSceneY();
        });

        s.setOnMousePressed((MouseEvent e) -> {
            PDrops p = new PDrops(player.loc, player.d, 50);
            missiles.add(p);
        });

        s.setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case A:
                    kleft = true;
                    break;
                case D:
                    krigth = true;
                    break;
                case W:
                    kup = true;
                    break;
                case S:
                    kdown = true;
                    break;
            }
        });

        s.setOnKeyReleased((KeyEvent e) -> {
            switch (e.getCode()) {
                case A:
                    kleft = false;
                    break;
                case D:
                    krigth = false;
                    break;
                case W:
                    kup = false;
                    break;
                case S:
                    kdown = false;
                    break;
            }
        });

        root.getChildren().add(canvas);
        s.setCursor(Cursor.NONE);
        stage.setScene(s);
        stage.show();
        initInvaders(STARTLEVEL);
        initBackground();
        gameloop();
    }

    public void initInvaders(int level) {
        int p1 = level * 8;
        baddies.clear();
        for (int i = 0; i < 8; i++) {
            int[] m = levels[p1 + i];
            int y = 2 + i * 18 * 5;
            for (int j = 0; j < m.length; j++) {
                int x = 4 + j * 18 * 5;
                BadBoiClass p = switch (m[j]) {
                    case 1->
                        new PBadBoiAlien(x, y, Color.RED);
                    case 2->
                        new PBadBoiAlien(x, y, Color.GREEN);
                    case 3->
                        new PBadBoiAlien(x, y, Color.BLUE);
                    case 4->
                        new PBadBoiAlien(x, y, Color.YELLOW);
                    case 5->
                        new PBadBoiAlien(x, y, Color.AQUAMARINE);
                    case 6->
                        new PBadBoiAlien(x, y, Color.BROWN);
                    case 7->
                        new PBadBoiPolyLines(x, y, Color.YELLOW, Color.RED, 4);
                    case 8->
                        new PBadBoiPolyLines(x, y, Color.YELLOW, Color.RED, 5);
                    case 9->
                        new PBadBoiPolyLines(x, y, Color.YELLOW, Color.RED, 6);
                    case 10->
                        new PBadBoiPolyVector(x, y, Color.YELLOW, Color.RED, 4, 5);
                    case 11->
                        new PBadBoiPolyVector(x, y, Color.YELLOW, Color.RED, 10, 11);
                    case 12->
                        new PBadBoiPolyVector(x, y, Color.YELLOW, Color.RED, 8, 9);
                    case 666->
                        new PBadBoiMegaAlien(x, y, Color.BROWN);
                    default->
                        null;
                };
                    if (p != null) {
                        baddies.add(p);
                    }
            }
        }
    }

    /**
     * distributes stars on the background
     */
    public void initBackground() {
        for (int i = 0; i < 2000; i++) {
            pbs.add(new PBackgroundSprite(new PVector(Math.random() * 19200, Math.random() * 10800), Color.DARKSLATEGRAY));
        }
    }

    public void gameloop() {

        timer = new AnimationTimer() {
            double tick = 0;
            int state = 0; // 0:rigth -> 1: down -> 2:left -> 3: down
            double baddiespeed = 1;
            int downcount = 0;

            @Override
            public void handle(long l) {
                double xxx = 0;
                double yyy = 0;
                GraphicsContext gc = canvas.getGraphicsContext2D();
                PVector mloc = new PVector(mousex, mousey);
                gc.clearRect(0, 0, width, height);

                // update the good guy
                player.d = PVector.sub(mloc, player.loc);
                player.d.normalize();
                player.update();
                player.checkEdges();

                // move the baddies
                switch (state) {
                    case 0: // move baddies to the rigth
                        boolean hitTheEdge = false;
                        PVector vel = new PVector(1, 0);
                        vel.mult(baddiespeed);
                        for (int i = 0; i < baddies.size(); i++) {
                            baddies.get(i).loc.add(vel);
                            if (baddies.get(i).loc.x > 1850) {
                                hitTheEdge = true;
                            }
                        }
                        if (hitTheEdge) {
                            state++; // next frame will be state 1
                            downcount = 10; // next state will be repeated <downcount> times
                        }
                        break;
                    case 1: // move baddies down at the rigth side of the screen
                        vel = new PVector(0, 1);
                        vel.mult(baddiespeed);
                        for (int i = 0; i < baddies.size(); i++) {
                            baddies.get(i).loc.add(vel);
                        }
                        if (downcount-- == 0) {
                            state++; // next frame will be state 2
                        }
                        break;
                    case 2: // move baddies to the left
                        hitTheEdge = false;
                        vel = new PVector(-1, 0);
                        vel.mult(baddiespeed);
                        for (int i = 0; i < baddies.size(); i++) {
                            baddies.get(i).loc.add(vel);
                            if (baddies.get(i).loc.x < 20) {
                                hitTheEdge = true;
                            }
                        }
                        if (hitTheEdge) {
                            state++; // next frame will be state 3
                            downcount = 10; // next state will be repeated <downcount> times
                        }
                        break;
                    case 3: // move baddies down at the left side of the screen
                        vel = new PVector(0, 1);
                        vel.mult(baddiespeed);
                        for (int i = 0; i < baddies.size(); i++) {
                            baddies.get(i).loc.add(vel);
                        }
                        if (downcount-- == 0) {
                            state = 0; // next frame will be state 0
                        }
                        break;
                }

                // collission detect missiles
                for (int i = 0; i < missiles.size(); i++) {
                    missiles.get(i).update();
                    if (!missiles.get(i).checkEdges()) {
                        missiles.remove(i--);
                    } else {
                        // collision detect missiles against baddies
                        boolean hit = false;
                        for (int j = 0; j < baddies.size(); j++) {
                            hit = missiles.get(i).checkCollision(baddies.get(j));
                            if (hit) {
                                missiles.remove(i--);
                                baddies.remove(j);
                                break;
                            }
                        }
                        if (!hit) {
                            missiles.get(i).display(gc);
                        }
                    }
                }

                // render the background sprites
                pbs.forEach((p) -> {
                    p.display(gc);
                });

                // render the bad guys
                tick += Math.PI / 60;
                tick = (tick > Math.PI * 2) ? tick - 2 * Math.PI : tick;
                baddies.forEach((p) -> {
                    p.display(tick, gc);
                });

                //Do the bombing
                if (baddies.size() > 0) {
                    if (Math.random() < 0.025) {
                        int i = (int) (Math.random() * baddies.size());
                        bombs.add(new PBomb(baddies.get(i).loc));
                    }
                }

                // collision detect goodboi against bombs
                for (int i = 0; i < bombs.size(); i++) {
                    bombs.get(i).update();
                    if (!bombs.get(i).checkEdges()) {
                        bombs.remove(i--);
                    } else {
                        double hit = bombs.get(i).t.dist(player.loc);
                        if (hit < 23) {
                            bombs.remove(i--);
                            for (int ii = 0; ii < 500; ii++) {
                                boom.add(new PBoom(center));
                            }
                        } else {
                            bombs.get(i).display(gc);
                        }
                    }
                }

                // animate boom if it is active
                boom.forEach((p) -> {
                    p.display(gc);
                    p.update();
                });

                // remove boom sprites that are not visible
                for (Iterator<PBoom> iter = boom.listIterator(); iter.hasNext();) {
                    PBoom p = iter.next();
                    if (p.radius < 0.8) {
                        iter.remove();
                        if (boom.size() == 0) {
                            System.exit(0);
                        }
                    }
                }

                // render crosshair
                gc.setFill(Color.YELLOW);
                double s = 3;
                gc.fillRect(mloc.x - 8 * s, mloc.y - 1 * s, 6 * s, 2 * s);
                gc.fillRect(mloc.x + 2 * s, mloc.y - 1 * s, 6 * s, 2 * s);
                gc.fillRect(mloc.x - 1 * s, mloc.y - 8 * s, 2 * s, 6 * s);
                gc.fillRect(mloc.x - 1 * s, mloc.y + 2 * s, 2 * s, 6 * s);

                // render good gal
                player.display(gc);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Bombs dropped by invaders
     *
     */
    class PBomb {

        PVector t; // translated location vector
        PVector d;
        PVector loc;
        PVector vel;
        private static final int s = 30;
        private static final int w = 5;

        public PBomb(PVector l) {
            loc = new PVector(l.x + 30, l.y);
            t = new PVector(loc.x - player.t.x, loc.y - player.t.y);
            d = new PVector(0, 1);
            vel = new PVector(d.x, d.y);
            vel.normalize();
            vel.mult(2 + 3 * Math.random());
        }

        void update() {
            loc.add(vel);
            t.x = loc.x - player.t.x;
            t.y = loc.y - player.t.y;
        }

        // returns false if drop is outside edges
        boolean checkEdges() {
            boolean ret = true;
            if (loc.x > width) {
                ret = false;
            } else if (loc.x < 0) {
                ret = false;
            }

            if (loc.y > height) {
                ret = false;
            } else if (loc.y < 0) {
                ret = false;
            }
            return ret;
        }

        // returns true if there is a collision
        boolean checkCollision(PGoodBoi p) {
            boolean ret = false;

            if ((loc.x > p.loc.x) && (loc.x < p.loc.x + 50)
                    && (loc.y > p.loc.y) && (loc.y < p.loc.y + 65)) {
                ret = true;
            }

            return ret;
        }

        void display(GraphicsContext gc) {
            gc.setFill(Color.YELLOW);
            gc.fillOval(loc.x - 5 - player.t.x, loc.y - 5 - player.t.y, 10, 10);
        }
    }

    /**
     * Explosion particles
     *
     */
    class PBoom {

        PVector loc;
        PVector vel;
        double radius = 5 + 30 * Math.random();
        double factor = 0.985;
        private static final int s = 30;
        private static final int w = 5;

        /**
         *
         * @param l start location
         */
        public PBoom(PVector l) {
            loc = new PVector(l.x, l.y);
            double a = 2 * Math.PI * Math.random();
            vel = new PVector(Math.sin(a), Math.cos(a));
            vel.mult(3 * Math.random());
        }

        void update() {
            loc.add(vel);
            radius *= factor;
        }

        // returns false if drop is outside edges
        boolean checkEdges() {
            boolean ret = true;
            if (loc.x > width) {
                ret = false;
            } else if (loc.x < 0) {
                ret = false;
            }

            if (loc.y > height) {
                ret = false;
            } else if (loc.y < 0) {
                ret = false;
            }
            return ret;
        }

        void display(GraphicsContext gc) {
            gc.setFill(Color.YELLOW);
            gc.fillOval(loc.x - 5, loc.y - 5, radius, radius);
        }
    }

    /**
     * Backgrund sprites
     *
     */
    class PBackgroundSprite {

        PVector t; // translated location vector
        PVector loc;
        private Color col;

        public PBackgroundSprite(PVector l, Color col) {
            this.col = col;
            loc = new PVector(l.x + 30, l.y);
            t = new PVector(loc.x - player.t.x, loc.y - player.t.y);
        }

        void display(GraphicsContext gc) {
            gc.setFill(this.col);
            gc.fillOval(loc.x - 2.5 - player.t.x, loc.y - 2.5 - player.t.y, 5, 5);
        }
    }

    /**
     * Container for missiles fired by PGoodBoi
     *
     */
    class PDrops {

        PVector d;
        PVector loc;
        PVector vel;
        private static final int s = 30;
        private static final int w = 5;

        public PDrops(PVector l, PVector d_, double s_) {
            loc = new PVector(l.x, l.y);
            d = new PVector(d_.x, d_.y);
            vel = new PVector(d.x, d.y);
            vel.normalize();
            vel.mult(s_);
        }

        void update() {
            loc.add(vel);
        }

        // returns false if drop is outside edges
        boolean checkEdges() {
            boolean ret = true;
            if (loc.x > width) {
                ret = false;
            } else if (loc.x < 0) {
                ret = false;
            }

            if (loc.y > height) {
                ret = false;
            } else if (loc.y < 0) {
                ret = false;
            }
            return ret;
        }

        // returns true if there is a collision
        boolean checkCollision(BadBoiClass p) {
            boolean ret = p.contains(loc.x, loc.y);
            return ret;
        }

        void display(GraphicsContext gc) {
            gc.beginPath();
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(w);
            gc.moveTo(loc.x, loc.y);
            gc.lineTo(loc.x + s * d.x, loc.y + s * d.y);
            gc.stroke();
            gc.closePath();
//            gc.fillRect(loc.x, loc.y, 20, 20);
        }
    }

    /**
     * Player 1
     *
     */
    class PGoodBoi {

        PVector t; // translation vector
        PVector loc; // location in space
        PVector vel; // vector for velocity
        PVector acc; // vector for accelleration
        PVector d; // nomalized vector for direction
        double topspeed = 10;
        private static final int s = 3; // scale of good gal

        public PGoodBoi(double x, double y) {
            loc = new PVector(x, y);
            acc = new PVector(0, 0);
            vel = new PVector(0, 0);
            t = new PVector(0, 0);
        }

        /**
         * handles keypress. Called by AnimationTimer handler at every frame
         */
        void update() {
            if (kup) {
                acc.x = d.x;
                acc.y = d.y;
                acc.mult(0.05);
                vel.add(acc);
                if (vel.mag() > topspeed) {
                    vel.normalize();
                    vel.mult(topspeed);
                }
            } else if (kdown) {
                acc.x = -d.x;
                acc.y = -d.y;
                acc.mult(0.05);
                vel.add(acc);
                if (vel.mag() > topspeed) {
                    vel.normalize();
                    vel.mult(topspeed);
                }
            } else if (kleft) {
                acc.x = d.y;
                acc.y = -d.x;
                acc.mult(0.05);
                vel.add(acc);
                if (vel.mag() > topspeed) {
                    vel.normalize();
                    vel.mult(topspeed);
                }
            } else if (krigth) {
                acc.x = -d.y;
                acc.y = d.x;
                acc.mult(0.05);
                vel.add(acc);
                if (vel.mag() > topspeed) {
                    vel.normalize();
                    vel.mult(topspeed);
                }
            }
            t.add(vel);
//            loc.add(vel); // move the player sprite
        }

        void checkEdges() {
            if ((loc.x < 5) || (loc.x > 1880)) {
                vel.x *= -1;
            }
            if ((loc.y < 5) || (loc.y > 1070)) {
                vel.y *= -1;
            }
        }

        /**
         * Renders the player
         *
         * @param gc graphicsContext of the game
         */
        void display(GraphicsContext gc) {
            double angle = (Math.asin(d.y) < 0) ? Math.PI / 2 - Math.acos(d.x) : Math.PI / 2 + Math.acos(d.x);
            angle = 360 * angle / (2 * Math.PI);
            gc.translate(player.loc.x, player.loc.y);
            gc.rotate(angle);
            double xxx = - 9 * s;
            double yyy = - 3 * s;
            gc.setFill(Color.YELLOW);
            gc.fillRect(xxx + 8 * s, yyy, 3 * s, 3 * s);
            gc.fillRect(xxx + 7 * s, yyy + 3 * s, 5 * s, 1 * s);
            gc.fillRect(xxx + 1 * s, yyy + 4 * s, 16 * s, 1 * s);
            gc.fillRect(xxx, yyy + 5 * s, 18 * s, 5 * s);
            gc.rotate(-angle);
            gc.translate(-player.loc.x, -player.loc.y);

            // debug dot
//            gc.setStroke(Color.BLUEVIOLET);
//            gc.strokeRect(t.x, t.y, 3, 3);
        }
    }

    /**
     * Parent class for all baddies
     *
     */
    class BadBoiClass {

        static final int S = 5;  // scale size of the baddie
        PVector loc;

        public BadBoiClass(double x, double y) {
            loc = new PVector(x, y);
        }

        public void display(double tick, GraphicsContext gc) {
        }

        public boolean contains(double x, double y) {
            return false;
        }

    }

    /**
     * renders an alien type baddie
     *
     */
    class PBadBoiAlien extends BadBoiClass {

        private double r1x1, r1y1, r1x2, r1y2; // for the hitbox
        private Color col;

        /**
         * instansiates the baddie
         *
         * @param x coordinate
         * @param y coordinate
         * @param col color
         */
        public PBadBoiAlien(double x, double y, Color col) {
            super(x, y);
            this.col = col;
        }

        @Override
        public void display(double tick, GraphicsContext gc) {
            double tx = loc.x - player.t.x; // translate location
            double ty = loc.y - player.t.y;
            gc.setFill(col);
            gc.fillRect(tx + 1 * S, ty, 8 * S, 1 * S);
            gc.fillRect(tx, ty + 1 * S, 10 * S, 9 * S);
            double xx = tx + 3 * Math.sin(tick * 2) * S;
            gc.fillRect(xx, ty + 8 * S, 4 * S, 5 * S);
            gc.fillRect(xx + 6 * S, ty + 8 * S, 4 * S, 5 * S);
            gc.setFill(Color.WHITE); // hej
            gc.fillRect(tx + 2 * S, ty + 2 * S, 2 * S, 4 * S);
            gc.fillRect(tx + 6 * S, ty + 2 * S, 2 * S, 4 * S);
            gc.setFill(Color.BLACK);
            gc.fillRect(tx + 2 * S, ty + 4 * S, 1 * S, 2 * S);
            gc.fillRect(tx + 6 * S, ty + 4 * S, 1 * S, 2 * S);

            // calculate hitbox
            r1x1 = tx;
            r1y1 = ty;
            r1x2 = r1x1 + 10 * S;
            r1y2 = r1y1 + 13 * S;
        }

        public boolean contains(double x, double y) {
            if ((x >= r1x1) && (x <= r1x2) && (y >= r1y1) && (y <= r1y2)) {
                return true;
            }
            return false;
        }

    }

    /**
     * renders an alien type baddie
     *
     */
    class PBadBoiMegaAlien extends BadBoiClass {

        private Color col;
        private int s = 30; // the size
        private double r1x1, r1y1, r1x2, r1y2; // for the hitbox
        private double r2x1, r2y1, r2x2, r2y2;
        private double r3x1, r3y1, r3x2, r3y2;

        public PBadBoiMegaAlien(double x, double y, Color col) {
            super(x, y);
            this.col = col;
        }

        @Override
        public void display(double tick, GraphicsContext gc) {
            double tx = loc.x - player.t.x; // translate location
            double ty = loc.y - player.t.y;
            gc.setFill(col);
            gc.fillRect(tx + 1 * s, ty, 8 * s, 1 * s);
            gc.fillRect(tx, ty + 1 * s, 10 * s, 9 * s);
            double xx = tx + 3 * Math.sin(tick) * s;
            gc.fillRect(xx, ty + 8 * s, 4 * s, 5 * s);
            gc.fillRect(xx + 6 * s, ty + 8 * s, 4 * s, 5 * s);
            gc.setFill(Color.WHITE); // hej
            gc.fillRect(tx + 2 * s, ty + 2 * s, 2 * s, 4 * s);
            gc.fillRect(tx + 6 * s, ty + 2 * s, 2 * s, 4 * s);
            gc.setFill(Color.BLACK);
            gc.fillRect(tx + 2 * s, ty + 4 * s, 1 * s, 2 * s);
            gc.fillRect(tx + 6 * s, ty + 4 * s, 1 * s, 2 * s);

            // calculate hitbox
            r1x1 = tx;
            r1y1 = ty;
            r1x2 = tx + 10 * s;
            r1y2 = ty + 10 * s;

            r2x1 = xx;
            r2y1 = ty + 8 * s;
            r2x2 = r2x1 + 4 * s;
            r2y2 = r2y1 + 5 * s;

            r3x1 = xx + 6 * s;
            r3y1 = r2y1;
            r3x2 = r3x1 + 4 * s;
            r3y2 = r2y2;
        }

        public boolean contains(double x, double y) {
            if ((x >= r1x1) && (x <= r1x2) && (y >= r1y1) && (y <= r1y2)) {
                return true;
            }
            if ((x >= r2x1) && (x <= r2x2) && (y >= r2y1) && (y <= r2y2)) {
                return true;
            }
            if ((x >= r3x1) && (x <= r3x2) && (y >= r3y1) && (y <= r3y2)) {
                return true;
            }
            return false;
        }

    }

    /**
     * Renders a polygon vector-type alien
     *
     */
    class PBadBoiPolyVector extends BadBoiClass {

        private double r1x1, r1y1, r1x2, r1y2; // for the hitbox
        private double n = 10; // corners
        private double a = Math.PI;
        private Color col1, col2;

        /**
         *
         * @param x coordinate
         * @param y coordinate
         * @param col1 color 1
         * @param col2 color 2
         * @param f factor for PI
         * @param n number of edges
         */
        public PBadBoiPolyVector(double x, double y, Color col1, Color col2, double f, double n) {
            super(x, y);
            this.n = n;
            this.a = f * Math.PI / n;
            this.col1 = col1;
            this.col2 = col2;
        }

        @Override
        public void display(double tick, GraphicsContext gc) {
            double tx = loc.x - player.t.x; // translate location
            double ty = loc.y - player.t.y;
            double cx = tx - S / 2 + S * 6;
            double cy = ty - S / 2 + S * 6;
            double r = 8;

            gc.setStroke(col1);
            gc.setLineWidth(S / 2);
            gc.strokeOval(tx - S / 2, ty - S / 2, 12 * S, 12 * S);
            double x = cx + (r * S * Math.sin(tick));
            double y = cy + (r * S * Math.cos(tick));
            gc.beginPath();
            gc.moveTo(x, y);
            for (double i = 1; i < n; i += 1.0) {
                x = cx + (r * S * Math.sin(tick + a * i));
                y = cy + (r * S * Math.cos(tick + a * i));
                gc.lineTo(x, y);
            }
            gc.closePath();
            gc.setStroke(col2);
            gc.setLineWidth(S);
            gc.stroke();

            // calculate hitbox
            r1x1 = tx;
            r1y1 = ty;
            r1x2 = r1x1 + 2 * r * S;
            r1y2 = r1y1 + 2 * r * S;
        }

        public boolean contains(double x, double y) {
            if ((x >= r1x1) && (x <= r1x2) && (y >= r1y1) && (y <= r1y2)) {
                return true;
            }
            return false;
        }
    }

    /**
     * Renders a centered vector-type alien
     *
     */
    class PBadBoiPolyLines extends BadBoiClass {

        private double r1x1, r1y1, r1x2, r1y2; // for the hitbox
        private double n = 3; // number of lines from center
        private Color col1, col2;

        /**
         *
         * @param x coordinate
         * @param y coordinate
         * @param col1 color 1
         * @param col2 color 2
         * @param n number of edges
         */
        public PBadBoiPolyLines(double x, double y, Color col1, Color col2, double n) {
            super(x, y);
            this.n = n;
            this.col1 = col1;
            this.col2 = col2;
        }

        @Override
        public void display(double tick, GraphicsContext gc) {
            double tx = loc.x - player.t.x; // translate location
            double ty = loc.y - player.t.y;
            double cx = tx - S / 2 + S * 6;
            double cy = ty - S / 2 + S * 6;
            double r = 8;

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(S / 2);
            gc.strokeOval(tx - S / 2, ty - S / 2, 12 * S, 12 * S);
            double a = 2 * Math.PI / n;
            double x;
            double y;
            gc.beginPath();
            for (double i = 0; i < n; i += 1.0) {
                gc.moveTo(cx, cy);
                x = cx + (r * S * Math.sin(tick + a * i));
                y = cy + (r * S * Math.cos(tick + a * i));
                gc.lineTo(x, y);
            }
            gc.closePath();
            gc.setStroke(Color.RED);
            gc.setLineWidth(S);
            gc.stroke();

            // calculate hitbox
            r1x1 = tx;
            r1y1 = ty;
            r1x2 = r1x1 + 2 * r * S;
            r1y2 = r1y1 + 2 * r * S;
        }

        public boolean contains(double x, double y) {
            if ((x >= r1x1) && (x <= r1x2) && (y >= r1y1) && (y <= r1y2)) {
                return true;
            }
            return false;
        }
    }

}

/**
 * Vector class to help making the particles simpler
 *
 */
class PVector {

    double x;
    double y;

    PVector(double x_, double y_) {
        x = x_;
        y = y_;
    }

    void add(PVector v) {
        x += v.x;
        y += v.y;
    }

    static PVector add(PVector v, PVector u) {
        return (new PVector(v.x + u.x, v.y + u.y));
    }

    void sub(PVector v) {
        x -= v.x;
        y -= v.y;
    }

    static PVector sub(PVector v, PVector u) {
        return (new PVector(v.x - u.x, v.y - u.y));
    }

    static PVector random2D() {
        double angle = 2 * Math.PI * Math.random();
        return (new PVector(Math.cos(angle), Math.sin(angle)));
    }

    void mult(double n) {
        x *= n;
        y *= n;
    }

    void div(double n) {
        x /= n;
        y /= n;
    }

    double mag() {
        return (double) Math.sqrt(x * x + y * y);
    }

    double dist(PVector p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return (double) Math.sqrt(dx * dx + dy * dy);
    }

    void normalize() {
        double m = mag();
        if (m != 0) {
            div(m);
        }
    }

    void limit(double max) {
        if (mag() > max) {
            normalize();
            mult(max);
        }
    }
}
