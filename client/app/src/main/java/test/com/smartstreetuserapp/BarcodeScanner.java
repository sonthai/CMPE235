package test.com.smartstreetuserapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class BarcodeScanner extends AppCompatActivity {

    static final String SCAN = "com.google.zxing.client.android.SCAN";
    private TextView formatText, contentText;
    // text view to show scan result


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        setContentView(R.layout.activity_barcode_scanner); // setting barcode scanner as view
        formatText = (TextView)findViewById(R.id.scan_format);//instantiate variable using ID values
        contentText = (TextView)findViewById(R.id.scan_content);
    }

            /*
            method which start the scanner by passing SCAN string in intent
             */
            public void scanCode(View v) {

                try {
                    Intent i = new Intent(SCAN);
                    //putExtra pass extra data to the intent takes key and value
                    i.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(i, 0);
                } catch (ActivityNotFoundException e) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(this);
                    ad.setTitle("Download a scanner");

                }

            }

            private Dialog ShowDialog(final Activity act, CharSequence title, CharSequence message, CharSequence Yes, CharSequence No) {
                AlertDialog.Builder download = new AlertDialog.Builder(act);

                download.setTitle(title);
                download.setMessage(message);
                download.setPositiveButton(Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // TODO Auto-generated method stub
                        Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            act.startActivity(intent);
                        } catch (ActivityNotFoundException e) {

                        }
                    }
                });
                download.setNegativeButton(No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // TODO Auto-generated method stub
                    }
                });
                return download.show();
            }

            // onActivityResult method is called when user is done with activity and returns , passes three argument requestCode, resultCode and Intent
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
                // TODO Auto-generated method stub
                //check if there is valid data
                if (requestCode == 0) {
                    if (resultCode == RESULT_OK) {
                        String contents = intent.getStringExtra("SCAN_RESULT"); // store scan result
                        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                        formatText.setText("FORMAT: " + format); // display result in textview
                        contentText.setText("CONTENT: " + contents);


                    }
                }

            }


        }


