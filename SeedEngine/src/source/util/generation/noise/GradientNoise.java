package source.util.generation.noise;

/**
 * A noise generation class
 * Contributions from Ken Perlin, Ian Parberry, and Zach Harris
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class GradientNoise extends OctaveNoise {

    protected float frequency;
    protected float amplitude;
    protected float lacunarity;
    protected float gain;
    
    private static final int B = 256;
    private static final int BM = B - 1;

    //Should be between 1.0 and 1.1637
    private static final double g_fMu = 1.002;

    private int p[] = new int[B*2], permutation[] = { 151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };

    private static double[] gradX = new double[B], gradY = new double[B];
    static {
        for (int i = 0; i < B; i++) {
            gradX[i] = Math.cos(2.0 * Math.PI * i / B);
            gradY[i] = Math.sin(2.0 * Math.PI * i / B);
        }
    }
    
    private static double[] m = new double[B];
    static {
        double s = 1.0; //current magnitude
        for (int i = 0; i < B; i++){
            m[i] = s;
            s /= g_fMu;
        }

    }

    public GradientNoise(long seed) {
        this(seed, 1);
    }

    public GradientNoise(long seed, int octaves) {
        super(seed, octaves);
        rnd.nextInt();

        this.frequency = 5;
        this.amplitude = 1;
        this.lacunarity = 2;
        this.gain = 0.5f;

        this.perOctaveCurve = (t) -> t;
        this.perPointCurve = (t) -> t;
        for (int i = 0; i < B; i++) {
            int change = i + rnd.nextInt(B - i);
            int helper = permutation[i];
            permutation[i] = permutation[change];
            permutation[change] = helper;

        }
        for (int i = 0; i < B; i++) {
            p[B+i] = p[i] = permutation[i];
        }
    }

    public long getSeed() {
        return seed;
    }

    //region Getters and Setters
    
    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency > 0 ? frequency : this.frequency;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude > 0 ? amplitude : this.amplitude;
    }

    public float getLacunarity() {
        return lacunarity;
    }

    public void setLacunarity(float lacunarity) {
        this.lacunarity = lacunarity > 0 ? lacunarity : this.lacunarity;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain > 0 ? gain : this.gain;
    }

    
    //endregion

    /**
     * Generates a 2D gradient noise field
     * @param ptX the x-coordinate of the noise
     * @param ptY the y-coordinate of the noise
     * @return 2D gradient noise field
     */
    public double[] generate(int ptX, int ptY) {
        double[] data = new double[RESOLUTION * RESOLUTION];

        long xPt = ptX - MIN_BOUND;
        long yPt = ptY - MIN_BOUND;

        double f = frequency * RANGE / (double) RESOLUTION;
        double amp = amplitude;

        double total = 0.0f;
        double max = 0.0f;

        double X, Y;
        double range = RANGE;

        for (int x = RESOLUTION - 1; x >= 0; x--) {
            for (int y = RESOLUTION - 1; y >= 0; y--) {
                X = (xPt + x) / range;
                Y = (yPt + y) / range;

                for (int o = 0; o < octaves; o++) {
                    this.currentOctave = o;
                    total += amp * perOctaveCurve.evaluate(gradient2(X * f, Y * f));
                    max += amp;
                    amp *= gain;
                    f *= lacunarity;
                }
                this.currentOctave = 0;
                //Assign point
                data[(y * RESOLUTION) + x] = perPointCurve.evaluate(total/max);
                //Reset variables
                total = 0.0f;
                max = 0.0f;
                f = frequency * RANGE / (double) RESOLUTION;
                amp = amplitude;
            }
        }
        return data;
    }

    /**
     * Computes the value ([-1, 1]) of a gradient noise field at point (x, y)
     * @param x x-coordinate
     * @param y y-coordinate
     * @return gradient value
     */
    private double gradient2(double x, double y) {
        double result = 0;
        int cellX = (int) Math.floor(x);
        int cellY = (int) Math.floor(y);

        for (int v = cellY - 1; v <= cellY + 2; v++)
        {
            for (int u = cellX - 1; u <= cellX + 2; u++)
            {
                int hash = p[(p[u & BM] + v) & BM];
                result += surflet2(x - u, y - v, gradX[hash], gradY[hash]);
            }
        }
        return result;
    }

    /**
     * A fade function to generate surflet values
     * @param t point
     * @return
     */
    private static double fade(double t) {
        //return t * t * t * (t * (t * 6 - 15) + 10);
        //t = Math.abs(t);
        //return t >= 1.0f ? 0.0f : 1.0f - ( 3.0f - 2.0f * t ) * t * t;
        t = 1.0 - 0.25 * t * t;
        return (t < 0.0) ? 0.0 : (4.0 * t - 3.0) * t * t * t * t;
    }

    /**
     * Generates a 2-Dimensional surflet
     * @param x x-coordinate to evaluate
     * @param y y-coordinate to evaluate
     * @param xGrad x-gradient to evaluate
     * @param yGrad y-gradient to evaluate
     * @return 2D surflet
     */
    private static double surflet2(double x, double y, double xGrad, double yGrad) {
        return fade(x) * fade(y) * dot2(xGrad, yGrad, x, y);
    }

    /**
     * Computes the 2-Dimensional dot product of 2 vectors
     * @param x1 Vector A.x
     * @param y1 Vector A.y
     * @param x2 Vector B.x
     * @param y2 Vector B.y
     * @return A.x * B.x + A.y * B.y
     */
    private static double dot2(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }
    
}
