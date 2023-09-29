//Donna Thakadipuram
//5-1-2023
//Honors Project Link  (all classes in this file)




package com.example.game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Model model;
    GameView view;
    GameController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        model = new Model();
        view = new GameView(this, model);
        controller = new GameController(model, view);
        setContentView(view);
        //System.out.println("hello world");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        controller.resume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        controller.pause();
    }

    abstract class Sprite{
        int x, y, width, height;

        boolean broken = false;
        abstract boolean update();
        abstract void draw(int scroll_x, int scroll_y);

        boolean isLink(){
            return false;
        }

        boolean isTile(){
            return false;
        }

        boolean isBoomerang(){
            return false;
        }

        boolean isPot(){
            return false;
        }

    }

    public class Link extends Sprite{
        int prev_x;
        int prev_y;

        double speed = 15;

        static final int DOWN = 0;
        static final int UP = 3;
        static final int LEFT = 1;
        static final int RIGHT = 2;

        int direction = 0; //down
        int current_image_index = 0;

       //Bitmap[] link_images;



        Link(int x, int y){
            this.x = x;
            this.y = y;

            width = 150;
            height = 150;
        }


        boolean update(){

            return true;

        }
        void draw(int scroll_x, int scroll_y){
            int a = current_image_index  + direction * 13;
            GameView.canvas.drawBitmap(view.link_images[a], x - scroll_x, y - scroll_y, null);
            //System.out.println(a )
            //g.drawImage(link_images[a], x-scroll_x, y-scroll_y, null);

        }

        @Override
        public String toString()
        {
            return "Link (x,y) = (" + x + ", " + y + "), x = " + x + ", y = " + y;
        }

        public void updateDirection(int direction){
            //goes to next image in the array
            this.direction = direction;
            current_image_index++;

            if (direction == UP){
                if(current_image_index >= 11){
                    current_image_index = 0;
                }
            }
            else if (current_image_index >= 13) {
                current_image_index = 0;
            }
        }

        @Override
        boolean isLink(){
            return true;
        }


        public void setPrevious(int x, int y){
            prev_x = x;
            prev_y = y;
        }

        public void getOutOfTile(Sprite spr){
            if((this.x + this.width >= spr.x) && this.prev_x + this.width <= spr.x){
                this.x = spr.x - this.height;

            }

            if((this.x <= spr.x + spr.width) && (this.prev_x >= spr.x + spr.width)){
                this.x = spr.x + spr.width;


            }

            if((this.y + this.height >= spr.y) && (this.prev_y + this.height <= spr.y)){
                this.y = spr.y - this.height;

            }

            if((this.y <= spr.y + spr.height) && (this.prev_y >= spr.y + spr.height)){
                this.y = spr.y + spr.height;

            }
        }



    }

    public class Tile extends Sprite{

        //public int width = 90;
        //public int height = 90;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;

            width = 90;
            height = 90;

        }

        boolean update(){
            return true;
        }
        void draw(int scroll_x, int scroll_y){
            GameView.canvas.drawBitmap(view.tile_image, x - scroll_x , y - scroll_y, null);

        }

        @Override
        boolean isTile(){
            return true;
        }

        @Override
        public String toString()
        {
            return "Tile (x,y) = (" + x + ", " + y + "), x = " + x + ", y = " + y;
        }

    }

    public class Boomerang extends Sprite{

        int current_image_index = 0;
        int x_dir, y_dir;
        int speed = 15;


        Boomerang(int x, int y){
            this.x = x;
            this.y = y;

            width = 60;
            height = 60;


        }

        boolean update(){
            x += x_dir * speed;
//            System.out.println("boomerangmoving x_dir: " + x_dir);
//            System.out.println("boomerangmoving x: " + x );
//            System.out.println("boomerangmoving speed: " + speed);
            y += y_dir * speed;

            return true;
        }

        @Override
        public String toString()
        {
            return "Boomerang (x,y) = (" + x + ", " + y + "), x = " + x + ", y = " + y;
        }

        void draw(int scroll_x, int scroll_y){
            GameView.canvas.drawBitmap(view.boomerang_images[current_image_index % 4], x-scroll_x, y-scroll_y, null);
            //System.out.println("drawn boomerang");
            if (current_image_index == 3){
                current_image_index = 0;
            }
            current_image_index ++;
        }

        public void move(int linkDirection){
            //System.out.println("LINK DIRECTION IS: " + linkDirection);
            if (linkDirection == 0){
                y_dir = 1;
                x_dir = 0;

            }
            else if(linkDirection == 1){
                y_dir = 0;
                x_dir = -1;

            }
            else if(linkDirection == 2){
                y_dir = 0;
                x_dir = 1;

            }
            else if(linkDirection == 3){
                y_dir = -1;
                x_dir = 0;

            }

        }

        @Override
        boolean isBoomerang(){
            return true;
        }
    }


    public class Pot extends Sprite{

        int x_dir, y_dir;
        int speed = 10;
        //boolean broken = false;
        int countdown = 34;

        Pot(int x, int y){
            this.x = x;
            this.y = y;

            width = 50;
            height = 50;



        }

        @Override
        public String toString()
        {
            return "Pot (x,y) = (" + x + ", " + y + "), x = " + x + ", y = " + y;
        }
        public boolean update(){
            if (broken){

                x_dir = 0;
                y_dir = 0;
                speed = 0;

                countdown--;
                if (countdown <= 0){
                    return false;
                }
            }
            x += x_dir * speed;
            y += y_dir * speed;

            return true;

        }

        void draw(int scroll_x, int scroll_y){
            if(broken){
                GameView.canvas.drawBitmap(view.pot_broken, x - scroll_x, y -scroll_y, null);
            }
            else{
                GameView.canvas.drawBitmap(view.pot_image, x - scroll_x , y - scroll_y, null);
            }

        }

        @Override
        boolean isPot(){
            return true;
        }

        public void move(int linkDirection){
            if (linkDirection == 0){
                y_dir = 1;
                x_dir = 0;

            }
            else if(linkDirection == 1){
                y_dir = 0;
                x_dir = -1;

            }
            else if(linkDirection == 2){
                y_dir = 0;
                x_dir = 1;

            }
            else if(linkDirection == 3){
                y_dir = -1;
                x_dir = 0;

            }

        }
    }



    class Model
    {
        ArrayList <Sprite> sprites;
        Link link;
        Tile tile;

        Pot pot;
//        boolean flap = false;
//        boolean flapped_recently = false;
//        int x = 150;
//        int y = 200;
//        float yVelocity = -8.0f;
        Model(){
            sprites = new ArrayList<Sprite>();

//            tile = new Tile(0,0);
//            sprites.add(tile);
//
//            tile = new Tile(75,0);
//            sprites.add(tile);

            for(int i = 0; i < 2160; i += 90){
                tile = new Tile(i, 0);
                sprites.add(tile);
            }

            for(int i = 90; i < 4680; i += 90){
                tile = new Tile(0, i);
                sprites.add(tile);
            }

            for(int i = 90; i < 360; i += 90){

                tile = new Tile(i, 2340 - 90 - 90 - 90);
                sprites.add(tile);
            }
            for(int i = 720; i < 2160 - 90 - 90 - 90 - 90; i+= 90){
                tile = new Tile(i, 2340 - 90 - 90 - 90);
                sprites.add(tile);
            }

            for(int i = 90; i < 360; i += 90){

                tile = new Tile(i, 2340);
                sprites.add(tile);
            }
            for(int i = 720; i <2160- 90 - 90 - 90 - 90; i+= 90){
                tile = new Tile(i, 2340);
                sprites.add(tile);
            }

            for(int i = 90; i < 4680; i += 90){
                tile = new Tile(2160-90, i);
                sprites.add(tile);
            }


            for(int i = 90; i < 900; i += 90){
                tile = new Tile(1080-90, i);
                sprites.add(tile);
            }

            for(int i = 90; i < 900; i += 90){
                tile = new Tile(1080, i);
                sprites.add(tile);
            }

            for(int i = 2340; i < 2340 + 1080 + 90*4; i += 90){
                tile = new Tile(1080-90, i);
                sprites.add(tile);
            }

            for(int i = 2340; i < 2340 + 1080 + 90* 4; i += 90){
                tile = new Tile(1080, i);
                sprites.add(tile);
            }



            for(int i = 90; i < 1080* 2; i += 90){
                tile = new Tile(i, 2340*2 - 90 - 90 - 90);
                sprites.add(tile);
            }

//            for(int i = 810; i < 4680; i += 90){
//                tile = new Tile(2160-90, i);
//                sprites.add(tile);
//            }

            tile = new Tile(500, 1000 + 1000 + 1000);
            sprites.add(tile);

            tile = new Tile(500 + 1000, 1000);
            sprites.add(tile);

            tile = new Tile(500, 1000);
            sprites.add(tile);

            tile = new Tile(500 + 1000, 1000 + 1000 + 1000);
            sprites.add(tile);




            link = new Link(500,500);
            sprites.add(link);

            //System.out.println("creating a link");

            pot = new Pot(400, 400);
            sprites.add(pot);

            pot = new Pot(400 + 1080, 400);
            sprites.add(pot);

            pot = new Pot(400, 400 + 2340);
            sprites.add(pot);

            pot = new Pot(400 + 1080, 400 + 2340 + 500);
            sprites.add(pot);

//            pot = new Pot(400, 400);
//            sprites.add(pot);



        }

        void update()
        {
            for(int i = 0; i < sprites.size(); i++){
                if(collision(sprites.get(i)) && !sprites.get(i).isLink() && !sprites.get(i).isBoomerang()){

                    link.getOutOfTile(sprites.get(i));
                    if(sprites.get(i).isPot()){
                        ((Pot) sprites.get(i)).move(link.direction);
                    }
                }
                sprites.get(i).update();

            }

            for(int i = 0; i < sprites.size(); i++){
                for(int j = 0; j < sprites.size(); j++){
                    if(collision(sprites.get(i), sprites.get(j))){
                        //boomerang and tile collisions
                        if((sprites.get(i).isBoomerang() && sprites.get(j).isTile())){
                            sprites.remove(i);
                        }
                        else if(sprites.get(i).isTile() && sprites.get(j).isBoomerang()){
                            sprites.remove(j);
                        }


                        //boomerang and pot collisions
                        else if((sprites.get(i).isBoomerang() && sprites.get(j).isPot())){
                            //change the pot image to the broken pot image and remove the boomerang
                            sprites.remove(i);
                            sprites.get(j).broken = true;
                            //wait 2 seconds and remove the pot image
                            sprites.remove(j);
                            sprites.get(j).update();
                            //System.out.println("removed pot");



                        }
                        //pot and boomerang collisions
                        else if(sprites.get(i).isPot() && sprites.get(j).isBoomerang()){
                            //change the pot image to the broken pot image and remove the boomerang
                            sprites.remove(j);
                            sprites.get(i).broken = true;

                        }

                        //link and pot collisions
                        else if((sprites.get(i).isTile() && sprites.get(j).isPot())){
                            sprites.get(j).broken = true;

                        }
                        // else if(sprites.get(i).isPot() && sprites.get(j).isTile()){
                        // 	sprites.get(i).broken = true;

                        // }

                    }
                    //sprites.get(j).update();
                }
                if(!sprites.get(i).update()){
                    sprites.remove(i);
                    continue;
                }

            }
        }



        public void addBoomerang(){
            Sprite boomer = new Boomerang(link.x, link.y);
            sprites.add(boomer);


            int direction = link.direction;

            ((Boomerang)boomer).move(direction);
            boomer.update();

        }


        public boolean collision(Sprite spr){
            if(this.link.x + 150 < spr.x || this.link.x > spr.x + spr.width){
                return false;
            }
            if(this.link.y + 150 < spr.y || this.link.y > spr.y + spr.height){
                return false;
            }

            return true;

        }

        public boolean collision(Sprite s, Sprite d){
            if(s.x + s.width <= d.x)
                return false;
            if(s.x >= d.x + d.width) // + s.width
                return false;
            if(s.y + s.height <= d.y) // assumes bigger is downward
                return false;
            if(s.y >= d.y + d.height) // + s.height
                return false;
            return true;
        }

    }




    static class GameView extends SurfaceView
    {
        SurfaceHolder ourHolder;
        static Canvas canvas;
        Paint paint;

        //Link link;
        Model model;
        GameController controller;
        Bitmap bird1;
        Bitmap bird2;

        Bitmap link_images[];

        Bitmap boomerang_images[];

        Bitmap pot_broken;

        Bitmap pot_image;

        Bitmap tile_image;

        int scroll_x = 0;
        int scroll_y = 0;


        public GameView(Context context, Model m)
        {
            super(context);
            model = m;

            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            // Load the images
//            bird1 = BitmapFactory.decodeResource(this.getResources(),
//                    R.drawable.link01);
//            bird2 = BitmapFactory.decodeResource(this.getResources(),
//                    R.drawable.link02);

            link_images = new Bitmap[52];
            boomerang_images = new Bitmap[4];

            link_images[0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link01);
            link_images[1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link02);
            link_images[2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link03);
            link_images[3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link04);
            link_images[4] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link05);
            link_images[5] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link06);
            link_images[6] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link07);
            link_images[7] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link08);
            link_images[8] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link09);
            link_images[9] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link10);
            link_images[10] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link11);
            link_images[11] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link12);
            link_images[12] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link13);
            link_images[13] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link14);
            link_images[14] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link15);
            link_images[15] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link16);
            link_images[16] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link17);
            link_images[17] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link18);
            link_images[18] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link19);
            link_images[19] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link20);
            link_images[20] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link21);
            link_images[21] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link22);
            link_images[22] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link23);
            link_images[23] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link24);
            link_images[24] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link25);
            link_images[25] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link26);
            link_images[26] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link27);
            link_images[27] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link28);
            link_images[28] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link29);
            link_images[29] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link30);
            link_images[30] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link31);
            link_images[31] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link32);
            link_images[32] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link33);
            link_images[33] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link34);
            link_images[34] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link35);
            link_images[35] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link36);
            link_images[36] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link37);
            link_images[37] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link38);
            link_images[38] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link39);
            link_images[39] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link40);
            link_images[40] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link41);
            link_images[41] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link42);
            link_images[42] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link43);
            link_images[43] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link44);
            link_images[44] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link45);
            link_images[45] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link46);
            link_images[46] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link47);
            link_images[47] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link48);
            link_images[48] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link49);
            link_images[49] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link50);
            link_images[50] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link51);
            link_images[51] = BitmapFactory.decodeResource(this.getResources(), R.drawable.link52);

            boomerang_images[0] = BitmapFactory.decodeResource(this.getResources(), R.drawable.boomerang1);
            boomerang_images[1] = BitmapFactory.decodeResource(this.getResources(), R.drawable.boomerang2);
            boomerang_images[2] = BitmapFactory.decodeResource(this.getResources(), R.drawable.boomerang3);
            boomerang_images[3] = BitmapFactory.decodeResource(this.getResources(), R.drawable.boomerang4);

            pot_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.pot);
            pot_broken = BitmapFactory.decodeResource(this.getResources(), R.drawable.pot_broken);

            tile_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.tile);

            tile_image = Bitmap.createScaledBitmap(tile_image, 90, 90, true);
            boomerang_images[0] = Bitmap.createScaledBitmap(boomerang_images[0], 60, 60, true);
            boomerang_images[1] = Bitmap.createScaledBitmap(boomerang_images[1], 60, 60, true);
            boomerang_images[2] = Bitmap.createScaledBitmap(boomerang_images[2], 60, 60, true);
            boomerang_images[3] = Bitmap.createScaledBitmap(boomerang_images[3], 60, 60, true);

            for(int i = 0; i < 52; i++){
                link_images[i] = Bitmap.createScaledBitmap(link_images[i], 150, 150, true);
            }

            pot_image = Bitmap.createScaledBitmap(pot_image, 100, 100, true);
            pot_broken = Bitmap.createScaledBitmap(pot_broken, 100, 100, true);


        }

        public void paintComponent(){

            
        }

        void setController(GameController c)
        {
            controller = c;
        }

        public void update()
        {
            if (!ourHolder.getSurface().isValid())
                return;
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 128, 200, 200));


            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("right", 750, 1600, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("left", 150, 1600, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("up", 450, 1400, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("down", 450, 1900, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("boom", 450, 1650, paint);

            for(int i = 0; i < model.sprites.size(); i++){
                model.sprites.get(i).draw(scroll_x, scroll_y);

            }


            ourHolder.unlockCanvasAndPost(canvas);
        }


        // The SurfaceView class (which GameView extends) already
        // implements onTouchListener, so we override this method
        // and pass the event to the controller.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent)
        {
            controller.onTouchEvent(motionEvent);
            return true;
        }
    }




    static class GameController implements Runnable
    {
        volatile boolean playing;
        Thread gameThread = null;
        Model model;
        GameView view;

        boolean toRight = false;
        boolean toLeft = false;
        boolean toUp = false;
        boolean toDown = false;

        GameController(Model m, GameView v)
        {
            model = m;
            view = v;
            view.setController(this);
            playing = true;
        }


        @Override
        public void run()
        {
            while(playing)
            {
                //long time = System.currentTimeMillis();
                this.update();
                model.update();
                view.update();

                try {
                    Thread.sleep(20);
                } catch(Exception e) {
                    Log.e("Error:", "sleeping");
                    System.exit(1);
                }
            }
        }

        void onTouchEvent(MotionEvent motionEvent)
        {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // Player touched the screen
                    if (motionEvent.getX() > 750 && (motionEvent.getY() > 1550) && (motionEvent.getY() < 1700)) { // right
                        toRight = true;

                    }

                    if (motionEvent.getX() < 250 && (motionEvent.getY() > 1550) && (motionEvent.getY() < 1700)) { // left
                        toLeft = true;

                    }

                    if ((motionEvent.getX() > 400) && (motionEvent.getX() < 900) && motionEvent.getY() <= 1500) { // up
                        toUp = true;
                    }

                    if ((motionEvent.getX() > 400) && (motionEvent.getX() < 550) && motionEvent.getY() > 1850) { // down
                        toDown = true;
                    }

                    if(motionEvent.getX() > 500 && motionEvent.getX() < 550 && motionEvent.getY() > 1550 && motionEvent.getY() < 1700){
                        //boomerang
                        model.addBoomerang();
                    }



                    //System.out.println("LINK X INCREASING: " + model.link.x );
                    //model.x++;
                    //model.flap = true;
                    //model.flapped_recently = true;
                    break;

                case MotionEvent.ACTION_UP: // Player withdrew finger
                    if(toUp){
                        model.link.updateDirection(Link.UP);
                        toUp = false;
                    }
                    if(toDown){
                        model.link.updateDirection(Link.DOWN);
                        toDown = false;
                    }
                    if(toRight){
                        model.link.updateDirection(Link.RIGHT);
                        toRight = false;
                    }
                    if(toLeft){
                        model.link.updateDirection(Link.LEFT);
                        toLeft = false;
                    }
                    //model.flapped_recently = false;
                    break;
            }
        }

        void update()
        {

            model.link.setPrevious(model.link.x, model.link.y);
            if(toRight){
                model.link.x += 10;
                model.link.updateDirection(Link.RIGHT);

                if(this.model.link.x > 1080 && this.view.scroll_x != 1080){
                    view.scroll_x = view.scroll_x + 1080;
                }
            }
            if(toLeft){
                model.link.x -= 10;
                model.link.updateDirection(Link.LEFT);

                if(this.model.link.x < 1080 && this.view.scroll_x != 0){
                    view.scroll_x = view.scroll_x - 1080;
                }
            }
            if(toUp){

                model.link.y -= 10;
                model.link.updateDirection(Link.UP);

                if(this.model.link.y < 2340 && this.view.scroll_y != 0){
                    view.scroll_y = view.scroll_y - 2340;
                }
            }
            if(toDown){

                model.link.y += 10;
                model.link.updateDirection(Link.DOWN);

                if(this.model.link.y > 2340 && this.view.scroll_y != 2340){
                    view.scroll_y = view.scroll_y + 2340;
                }
            }
        }

        // Shut down the game thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
                System.exit(1);
            }

        }

        // Restart the game thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
           // System.out.println("running game ");
        }
    }
}