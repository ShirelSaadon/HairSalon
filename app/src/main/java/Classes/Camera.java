package Classes;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class Camera {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    private Activity activity;
    public void Camera(Activity activity){
        this.activity=activity;
    }
//
//    public void checkPermission(String permission, int requestCode) {
//
//        // Checking if permission is not granted
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (getFromPref(this, ALLOW_KEY)) {
//
//                showSettingsAlert();
//
//            } else if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.CAMERA)) {
//                    showAlert();
//                } else {
//                    // No explanation needed, we can request the permission.
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//                }
//            }
//        } else {
//            openCamera();
//        }
//
//
//        if (ContextCompat.checkSelfPermission(activity, permission)  != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(activity, new String[]{permission} ,requestCode);
//        else
//            Toast.makeText(activity, "Permission already granted",Toast.LENGTH_SHORT).show();
//
//    }

    public void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                });
        alertDialog.show();
    }

  //  private void openCamera() {
   //     Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    //    startActivity(intent);
    //}


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    // permission was granted,
//                    openCamera();
//                else {
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//
//                if (ActivityCompat.shouldShowRequestPermissionRationale
//                        (activity, Manifest.permission.CAMERA)) {
//
//                    showAlert();
//
//                } else {
//
//                }
//            }
//            return;
//        }

        // other 'case' lines to check for other
        // permissions this app might request

    }
//}


//}