package app.invision.mapping.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hp sleek book on 6/27/2015.
 */
public class Utils {


    //Show a custom alert dialog
    public static void showAlertDialogAndGoToLoginActivity(final Context c, String title, String message) {
        new AlertDialog.Builder(c).setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Go to Login Activity
                Activity a = (Activity) c;
                //a.startActivity(new Intent(c, SplashActivity.class));
                a.finish();
            }
        }).show();
    }

    public static void showAlertDialogWithoutCancel(final Context c, String title, String message) {
        new AlertDialog.Builder(c).setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, null).show();
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static SpannableStringBuilder getMultiStyleSignUpText(String text, int textColor, int startIndext, int endIndex, boolean isBold, boolean isItalic) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        // Span to set text color to some RGB value
        ForegroundColorSpan fcs = new ForegroundColorSpan(textColor);
        StyleSpan bss;
        if (isBold && isItalic) {
            // Span to make text bold and italic
            bss = new StyleSpan(Typeface.BOLD_ITALIC);
        } else if (isBold) {
            // Span to make text bold
            bss = new StyleSpan(Typeface.BOLD);
        } else if (isItalic) {
            // Span to make text bold
            bss = new StyleSpan(Typeface.ITALIC);
        } else {
            bss = new StyleSpan(Typeface.BOLD);
        }
        // Set the text color for first 4 characters
        sb.setSpan(fcs, startIndext, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        // make them also bold
        sb.setSpan(bss, startIndext, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    /**
     * get datetime
     */
    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    /**
     * get datetime
     */
    public static String getDateDDMMYYYYYFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * get datetime
     */
    public static String getDateInTFormatWithDayName(String d) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(
                new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).parse(d)
        );
    }

    /**
     * get datetime
     */
    public static String getDateInTFormat(String d) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = dateFormat.parse(d);
        String TPatternDate = dateFormat.format(date).replace(" ","T");
        return TPatternDate;
    }


    /**
     * get datetime
     */
    public static String getDateFromTFormat(String d) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = dateFormat.parse(d);
        String TPatternDate = dateFormat.format(date).replace(" ","T");
        return TPatternDate;
    }

    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static Bitmap blobToBitmap(byte[] blob) {
        return (blob != null) ? BitmapFactory.decodeByteArray(blob, 0, blob.length) : null;
    }

    public static byte[] bitmapToBlob(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            return bos.toByteArray();
        }
        return null;
    }

    public static String capitalizeFirstLetter(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    public static void hideStatusBar(Context c) {
        // remove title
        Activity a = (Activity) c;
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //Check if network is online
    public static boolean isNetworkOnline(Context c) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    //Show a custom alert dialog
    public static void showAlertDialog(final Context c, String title, String message) {
        new AlertDialog.Builder(c).setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity a = (Activity) c;
//                a.startActivity(new Intent(c, MenuActivity.class));
                a.finish();
            }
        }).show();
    }

    public static String removeLineBreaksAndTrim(String text){
        text = text.replaceAll("\\r|\\n", "");
        text = text.trim();
        return text;
    }

    public static void appendLog(String path, String text) {
        File dir = new File(path);
        if(!dir.exists()) dir.mkdirs();

        File logFile = new File(dir,"mapping-app-log.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.newLine();
            buf.append(Calendar.getInstance().getTime().toString() + " -> " + text);
            buf.newLine();
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap drawTextToBitmap(Context gContext,
                                          int gResId,
                                          String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, gResId);

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(224, 224, 224));
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    public static float poundsToKilograms(float pounds) {
        return (float) (pounds * 0.453592);
    }

    public static float kilogramsToPounds(float kilograms) {
        return (float) (kilograms * 2.20462);
    }

    public static float footToCentimeter(float foot) {
        return (float) (foot * 30.48);
    }

    public static float centimeterToFoot(float centimeters) {
        return (float) (centimeters * 0.0328084);
    }

    public static String readFromRawTextFile(Context c, int id) {
        InputStream inputStream = c.getResources().openRawResource(id);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}
