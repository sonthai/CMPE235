package sjsu.cmpe235.smartstreet.user;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sjsu.cmpe235.smartstreet.user.Constant.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private TextView formatText, contentText;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_fragment, container, false);
        formatText = (TextView) v.findViewById(R.id.scan_format);//instantiate variable using ID values
        contentText = (TextView)v.findViewById(R.id.scan_content);
        return v;
    }

    /*
           method which start the scanner by passing SCAN string in intent
            */
    public void scanCode(View v) {
        try {
            Intent i = new Intent(Constants.SCAN);
            //putExtra pass extra data to the intent takes key and value
            i.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(i, 0);
        } catch (ActivityNotFoundException e) {
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        //check if there is valid data
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT"); // store scan result
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                formatText.setText("FORMAT: " + format); // display result in textview
                contentText.setText("CONTENT: " + contents);


            }
        }

    }

}
