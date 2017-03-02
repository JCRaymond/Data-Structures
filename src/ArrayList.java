import java.util.Arrays;

public class ArrayList<E> {
    private static final int MINCAP = 8;

    private E[] data;
    private int size;
    private int cap;

    private Thread t;
    private E[] preLoad;
    private boolean preLoaded = false;

    public ArrayList(){
        size = 0;
        cap = MINCAP;
    }

    public ArrayList(int initCapacity){
        this.size = 0;
        this.cap = initCapacity;
    }

    private E[] getData(){
        if (data == null){
            data = (E[])(new Object[cap]);
        }
        return data;
    }

    public int size(){
        return size;
    }

    public void add(E elem){
        resize();
        data[size] = elem;
        size++;
    }

    private void resize(){
        if(size/(double)cap > 0.99){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (preLoaded){
            data = preLoad;
            cap = data.length;
            t = null;
            preLoaded = false;
        }

        if (t != null){
            final double capmod;
            if (size/(double)cap > 0.75){
                capmod = 1.5;
                t = new Thread(() -> preLoadArray(capmod));
                t.start();
            }
            else if(size/(double)cap < 0.25 && cap/2 > MINCAP){
                capmod = 0.5;
                t = new Thread(() -> preLoadArray(capmod));
                t.start();
            }
        }
    }


    private void preLoadArray(double capMod){
        preLoad = Arrays.copyOf(data, (int) (cap*capMod));
        preLoaded = true;
    }

}
