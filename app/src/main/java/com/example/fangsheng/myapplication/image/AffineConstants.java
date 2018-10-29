package com.example.fangsheng.myapplication.image;

import android.graphics.Matrix;

public class AffineConstants {

    public static String url1 = "http://gd1.alicdn.com/imgextra/TB2ztNEsFuWBuNjSszbXXcS7FXa_!!2104668892.jpg";
    public static String url2 = "http://gd1.alicdn.com/imgextra/TB24CLkpXXXXXboXpXXXXXXXXXX_!!1656456252.jpg";
    public static String url3 = "http://gd1.alicdn.com/imgextra/TB2YmBjechmZKJjSZFPXXc5_XXa_!!412129187.jpg";
    public static String url4 = "http://gd1.alicdn.com/imgextra/TB21qPZcbsTMeJjy1zeXXcOCVXa_!!1667651824.jpg";

    public static float[] src1 = new float[] {17.325089207923966f, 341.21154648854747f,
        913.9977027769474f, 357.4035054512717f,
        949.8566083468365f, 1063.6320551601257f,
        53.1839947778131f, 1047.4400961974015f};
    public static float[] src2 = new float[] {-10.862517223047911f, 763.739895416358f,
        697.309920360876f, 796.3063205050023f,
        693.3187681993774f, 1076.9228729875279f,
        -14.853669384546574f, 1044.3564478988835f};
    public static float[] src3 = new float[] {137.08307752426333f, 892.6226063918705f,
        653.1207397708162f, 891.4785912987811f,
        651.7147181463226f, 1101.6941639343354f,
        135.6770558997699f, 1102.8381790274248f};
    public static float[] src4 = new float[] {84.0763590720559f, 133.08737434755284f,
        692.792263934111f, 144.23548912706758f,
        735.4795790295639f, 719.340145021456f,
        126.76367416750881f, 708.1920302419412f};

    public static Matrix matrix1 = new Matrix() {
        {
            postScale(0.8371932935777749f, 0.7568230834538107f);
            postSkew(-0.013666580336997441f, -0.04250866843393139f);
            postTranslate(83.2882343850423f, 11.347132455153467f);
        }
    };
    public static Matrix matrix2 = new Matrix() {
        {
            postScale(1.0583718563092737f, 1.0363244471509945f);
            postSkew(-0.047657012168980536f, 0.015053007688280096f);
            postTranslate(-0.723693021193434f, 35.77648715652045f);
        }
    };
    public static Matrix matrix3 = new Matrix() {
        {
            postScale(1.4534038422098305f, 1.260629391579311f);
            postSkew(0.00972105541777204f, 0.0027947166578508845f);
            postTranslate(-207.91430539960385f, -242.64940156598917f);
        }
    };
    public static Matrix matrix4 = new Matrix() {
        {
            setValues(new float[] {1.233779028593615f, -0.09157761741614047f, -91.54382397269022f,
                -0.020663585159934043f, 1.128285202215236f, 86.57680392759917f,
                0, 0, 1});
            //preSkew(-0.09157761741614047f, -0.020663585159934043f);
            //postScale(1.233779028593615f, 1.128285202215236f);
            //
            //preTranslate(-91.54382397269022f, 86.57680392759917f);
        }
    };

    public static String skcAttr3
        = "{\"url\": \"TB2YmBjechmZKJjSZFPXXc5_XXa_!!412129187.jpg\",\"disp_info\": {\"left_color\": [255, 255, 255],\"affine_matrix\": [[1.4534038422098305, 0.00972105541777204, -207.91430539960385],[0.0027947166578508845, 1.260629391579311, -242.64940156598917]],\"origin_corners\": [[137.08307752426333, 892.6226063918705],[653.1207397708162, 891.4785912987811],[651.7147181463226, 1101.6941639343354],[135.6770558997699, 1102.8381790274248]],\"present_ltwh\": [0, 883, 750, 1148],\"right_color\": [255, 255, 255],\"rect\": \"TB1OmR9d9zqK1RjSZFpXXakSXXa\"}}";
    public static String skcAttr4
        = "{\"url\": \"TB21qPZcbsTMeJjy1zeXXcOCVXa_!!1667651824.jpg\",\"disp_info\": {\"left_color\": [255, 255, 255],\"affine_matrix\": [[1.233779028593615, -0.09157761741614047, -91.54382397269022],[-0.020663585159934043, 1.128285202215236, 86.57680392759917]],\"origin_corners\": [[84.0763590720559, 133.08737434755284],[692.792263934111, 144.23548912706758],[735.4795790295639, 719.340145021456],[126.76367416750881, 708.1920302419412]],\"present_ltwh\": [0, 235, 750, 883],\"right_color\": [255, 255, 255],\"rect\": \"TB1Pm49d3HqK1RjSZFEXXcGMXXa\"}}";

    public static String skcAttr5 = "{\"url\": \"TB2yhoKadmgF1Jjy1XaXXcSEFXa-420567757.jpg\",\"disp_info\": {\"left_color\": [228, 227, 223],\"affine_matrix\": [[0.6248902825410545, 0.011790544689608683, 150.16490338787077],[-0.023505177144235417, 0.5857671423038328, 88.5676884435391]],\"origin_corners\": [[-247.4445208483844, 378.3347822974998],[951.858149466766, 426.4594032935514],[941.2043530691983, 991.1028541494197],[-258.09831724595216, 942.9782331533681]],\"present_ltwh\": [0, 316, 750, 647],\"right_color\": [231, 230, 226],\"rect\": \"TB1A_X9d4TpK1RjSZR0XXbEwXXa\"}}";
    public static String skcAttr6 = "{\"url\": \"TB2YmBjechmZKJjSZFPXXc5_XXa_!!412129187.jpg\",\"disp_info\": {\"left_color\": [255, 255, 255],\"affine_matrix\": [[1.4534038422098305, 0.00972105541777204, -207.91430539960385],[0.0027947166578508845, 1.260629391579311, -242.64940156598917]],\"origin_corners\": [[138.3352326313594, 705.4117567994523],[654.3728948779121, 704.2677417063629],[651.7147181463226, 1101.6941639343354],[135.6770558997699, 1102.8381790274248]],\"present_ltwh\": [0, 647, 750, 1148],\"right_color\": [255, 255, 255],\"rect\": \"TB17JR_dVzqK1RjSZFzXXXjrpXa\"}}";

}