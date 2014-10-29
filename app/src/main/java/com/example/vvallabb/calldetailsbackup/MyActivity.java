package com.example.vvallabb.calldetailsbackup;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vvallabb.calldetailsbackup.pojo.CallDetailsRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by vasanthnaib on 29/10/2014.
 */
public class MyActivity extends Activity {

    private List<CallDetailsRecord> callDetailsRecordList;
    private static final String CSV_SEPARATOR = ",";
    private static final String TAG = MyActivity.class.getSimpleName();
    private TextView mResultTV;
    private Button mBackUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        init();
    }

    Cursor c;

    @Override
    protected void onResume() {
        super.onResume();
        retrieveCallDetails();
    }

    private void init() {
        callDetailsRecordList = new ArrayList<CallDetailsRecord>();
        mResultTV = (TextView) findViewById(R.id.outputId);
        mBackUpBtn = (Button) findViewById(R.id.backupButton);
        mBackUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToSDCard();

            }
        });
    }

    //Ref: http://stackoverflow.com/a/3666052/927258
    private void persistCallDetails(String path) {
        //CSV separator because "," is their digit separator

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/callRecords.csv"), "UTF-8"));
            for (CallDetailsRecord cdr : callDetailsRecordList) {
                StringBuffer oneLine = new StringBuffer();

                oneLine.append(cdr.getNumber().trim());
                oneLine.append(CSV_SEPARATOR);

                if (cdr.getName() != null) {
                    oneLine.append(cdr.getName().trim());
                } else {
                    oneLine.append("NEW NUMBER");
                }

                oneLine.append(CSV_SEPARATOR);

                oneLine.append(cdr.getDuration());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append("\n");

                String callType = "";
                switch (cdr.getType()) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "missed";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "outgoing";
                        break;

                }
                oneLine.append(callType);
                oneLine.append(CSV_SEPARATOR);
                java.util.Date time = new java.util.Date(Long.parseLong(cdr.getTimestamp(), 10) * 1000);
                oneLine.append(time + "");
                oneLine.append("; \n");
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        mResultTV.setText("Your call details back up file is saved as " + path + "callRecords.csv");
    }

    protected void retrieveCallDetails() {
        Uri allCalls = Uri.parse("content://call_log/calls");
        c = managedQuery(allCalls, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                CallDetailsRecord cdr = new CallDetailsRecord(c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)),
                        c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)), c.getString(c.getColumnIndex(CallLog.Calls.DURATION)),
                        Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE))),
                        c.getString(c.getColumnIndex(CallLog.Calls.DATE)));
                callDetailsRecordList.add(cdr);

            } while (c.moveToNext());
        }
        c.close();

    }

    public void saveToSDCard() {
        File databaseFile = new File("callRecords.csv");
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            File folder = new File(sd + "/backup");
            if (folder.exists() || folder.mkdirs()) {
                persistCallDetails(sd + "/backup");
            }
        } else {
            t("Could not write to sd card so database was not backed up.");
        }
    }

    public void t(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (c != null) {
            c.close();
        }
    }
}
