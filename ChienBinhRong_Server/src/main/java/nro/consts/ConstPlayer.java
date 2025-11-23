package nro.consts;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class ConstPlayer {

    public static final int[] HEADMONKEY = {192, 195, 196, 199, 197, 200, 198};
    // AURA BIẾN HÌNH Ở ĐÂY
    public static final byte[][] AURABIENHINH = {
        // LẦN LƯỢT TỪ LB 1-5
        {20, 20, 21, 27, 29},   //td
        {24, 22, 23, 24, 30},  //nm
        {20, 20, 21, 23, 25}   //xd
    };
    // SỬA NGOẠI HÌNH TỪ LV 1-5 Ở ĐÂY
    public static final short[][] HEADBIENHINH = {
        {1906,1912, 1910, 1909, 1911}, // head TD 
        {1900,1913, 1914, 1915, 1917},// haed NM
        {1903,1918, 1919, 1920, 1921}, // 5 head XD
    };
    // THÂN NGOẠI HÌNH LV 1-5
    public static final short[] BODYBIENHINH = {1907, 1901, 1904}; // TD /NM/ XD
    public static final short[] LEGBIENHINH = {1908, 1902, 1905}; // TD /NM/ XD
    
    public static final byte TRAI_DAT = 0;
    public static final byte NAMEC = 1;
    public static final byte XAYDA = 2;

   
    public static final short[][] FLAGBIENHINH = {
        {128, 128, 129, 130, 131}, // 5 flagbag TD 
        {133, 134, 135, 136, 137},// 5 flagbag NM
        {128, 128, 129, 130, 132}, // 5 flagbag XD
    };
    //type pk
    public static final byte NON_PK = 0;
    public static final byte PK_PVP = 3;
    public static final byte PK_ALL = 5;

    //type fushion
    public static final byte NON_FUSION = 0;
    public static final byte LUONG_LONG_NHAT_THE = 4;
    public static final byte HOP_THE_PORATA = 6;
    public static final byte HOP_THE_PORATA2 = 8;
    public static final byte HOP_THE_PORATA3 = 10;
}
