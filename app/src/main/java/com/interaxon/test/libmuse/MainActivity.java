/**
 * Example of using libmuse library on android.
 * Interaxon, Inc. 2015
 */

package com.interaxon.test.libmuse;

import java.io.File;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.interaxon.libmuse.Accelerometer;
import com.interaxon.libmuse.AnnotationData;
import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Eeg;
import com.interaxon.libmuse.LibMuseVersion;
import com.interaxon.libmuse.MessageType;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConfiguration;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseFileFactory;
import com.interaxon.libmuse.MuseFileReader;
import com.interaxon.libmuse.MuseFileWriter;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;
import com.interaxon.libmuse.MuseVersion;


/**
 * In this simple example MainActivity implements 2 MuseHeadband listeners
 * and updates UI when data from Muse is received. Similarly you can implement
 * listers for other data or register same listener to listen for different type
 * of data.
 * For simplicity we create Listeners as inner classes of MainActivity. We pass
 * reference to MainActivity as we want listeners to update UI thread in this
 * example app.
 * You can also connect multiple muses to the same phone and register same
 * listener to listen for data from different muses. In this case you will
 * have to provide synchronization for data members you are using inside
 * your listener.
 *
 * Usage instructions:
 * 1. Enable bluetooth on your device
 * 2. Pair your device with muse
 * 3. Run this project
 * 4. Press Refresh. It should display all paired Muses in Spinner
 * 5. Make sure Muse headband is waiting for connection and press connect.
 * It may take up to 10 sec in some cases.
 * 6. You should see EEG and accelerometer data as well as connection status,
 * Version information and MuseElements (alpha, beta, theta, delta, gamma waves)
 * on the screen.
 */
public class MainActivity extends Activity {
    /**
     * Connection listener updates UI with new connection status and logs it.
     */
    Signal TP9;
    Signal FP1;
    Signal FP2;
    Signal TP10;

    LinearLayout baseLayout;
    RelativeLayout circle_container;
    TextView state_text;
    TextView tp9;

    public String MUSE_FOLDER_NAME = "Muse";
    public String TP9_FILENAME = "tp9_";
    public String TP10_FILENAME = "tp10_";
    public String FP1_FILENAME = "fp1_";
    public String FP2_FILENAME = "fp2_";


    State patientState;

    class ConnectionListener extends MuseConnectionListener {

        final WeakReference<Activity> activityRef;

        ConnectionListener(final WeakReference<Activity> activityRef) {
            this.activityRef = activityRef;
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket p) {
            final ConnectionState current = p.getCurrentConnectionState();
            final String status = p.getPreviousConnectionState().toString() +
                         " -> " + current;
            final String full = "Muse " + p.getSource().getMacAddress() +
                                " " + status;
            Log.i("Muse Headband", full);
            Activity activity = activityRef.get();
            // UI thread is used here only because we need to update
            // TextView values. You don't have to use another thread, unless
            // you want to run disconnect() or connect() from connection packet
            // handler. In this case creating another thread is required.
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView statusText =
                                (TextView) findViewById(R.id.con_status);
                        statusText.setText(status);
                        if (current == ConnectionState.CONNECTED) {
                            MuseVersion museVersion = muse.getMuseVersion();
                            String version = museVersion.getFirmwareType() +
                                 " - " + museVersion.getFirmwareVersion() +
                                 " - " + Integer.toString(
                                    museVersion.getProtocolVersion());
                        } else {
                        }
                    }
                });
            }
        }
    }

    /**
     * Data listener will be registered to listen for: Accelerometer,
     * Eeg and Relative Alpha bandpower packets. In all cases we will
     * update UI with new values.
     * We also will log message if Artifact packets contains "blink" flag.
     * DataListener methods will be called from execution thread. If you are
     * implementing "serious" processing algorithms inside those listeners,
     * consider to create another thread.
     */
    class DataListener extends MuseDataListener {

        final WeakReference<Activity> activityRef;
        private  MuseFileWriter fileWriter;

        DataListener(final WeakReference<Activity> activityRef) {
            this.activityRef = activityRef;
        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket p) {
            switch (p.getPacketType()) {
                case EEG:
                    updateEeg(p.getValues());
                    break;
                case BATTERY:
                    fileWriter.addDataPacket(1, p);
                    // It's library client responsibility to flush the buffer,
                    // otherwise you may get memory overflow. 
                    if (fileWriter.getBufferedMessagesSize() > 8096)
                        fileWriter.flush();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket p) {
            if (p.getHeadbandOn() && p.getBlink()) {
                Log.i("Artifacts", "blink");
            }
        }


        private void updateEeg(final ArrayList<Double> data) {
            //Update EEG, analyze signal when needed , update UI
            //TODO: async analysis
            Activity activity = activityRef.get();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         tp9 = (TextView) findViewById(R.id.eeg_tp9);
                         //TextView fp1 = (TextView) findViewById(R.id.eeg_fp1);
                         //TextView fp2 = (TextView) findViewById(R.id.eeg_fp2);
                        // TextView tp10 = (TextView) findViewById(R.id.eeg_tp10);
                        Double value =Math.max(data.get(Eeg.TP10.ordinal()), Math.max(data.get(Eeg.FP2.ordinal()), Math.max(data.get(Eeg.TP9.ordinal()), data.get(Eeg.FP1.ordinal()))));
                         tp9.setText(String.format(
                            "%6.2f", value ));
                        /* fp1.setText(String.format(
                            "%6.2f", data.get(Eeg.FP1.ordinal())));
                         fp2.setText(String.format(
                            "%6.2f", data.get(Eeg.FP2.ordinal())));
                         tp10.setText(String.format(
                            "%6.2f", data.get(Eeg.TP10.ordinal())));
*/
                         TP9.update(data.get(Eeg.TP9.ordinal()));
                         FP1.update(data.get(Eeg.FP1.ordinal()));
                         FP2.update(data.get(Eeg.FP2.ordinal()));
                         TP10.update(data.get(Eeg.TP10.ordinal()));
                         if (patientState != null) {
                             patientState.updateState(TP9);
                             updateBackgroundColor(patientState.getCurrentStateColor());
                             updateTextColor(patientState.getCurrentStateColor());
                             updateTextMessage(patientState.getCurrentStateName());
                         }
                        if (circle_container != null) {
                            //Update radius according to EEG data
                            Double size;
                            size =  CircleConstants.MAX_SIZE * data.get(Eeg.TP9.ordinal())/10000;
                           // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size.intValue(), size.intValue());

                          //  circle_container.setLayoutParams(params);
                            //circle_container.getBackground().set
                        }

                    }
                });
            }
        }

        public void setFileWriter(MuseFileWriter fileWriter) {
            this.fileWriter  = fileWriter;
        }
    }

    private void updateTextMessage(String currentStateName) {
        state_text.setText(currentStateName);
    }

    private void updateTextColor(int currentStateColor) {
        if (state_text != null) {
            state_text.setTextColor(currentStateColor);
        }
        if (tp9 != null) {
            tp9.setTextColor(currentStateColor);
        }
    }

    private void updateBackgroundColor(int color) {
        if (baseLayout != null) {
            baseLayout.setBackgroundColor(color);
        }
    }

    private Muse muse = null;
    private ConnectionListener connectionListener = null;
    private DataListener dataListener = null;
    private boolean dataTransmission = true;
    private MuseFileWriter fileWriter = null;
    private Context context;
    private Integer log_counter;
    private FileWriter fWriter;
    File fileTP9;
    File fileTP10;
    File fileFP1;
    File fileFP2;
    File MuseDir;
    Spinner musesSpinner;

    public MainActivity() {
        // Create listeners and pass reference to activity to them
        WeakReference<Activity> weakActivity =
                                new WeakReference<Activity>(this);

        connectionListener = new ConnectionListener(weakActivity);
        dataListener = new DataListener(weakActivity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseLayout = (LinearLayout) findViewById(R.id.base_layout);
        state_text = (TextView) findViewById(R.id.status_text);
        circle_container = (RelativeLayout) findViewById(R.id.circle_container);
        TP9 = new Signal();
        FP2 = new Signal();
        FP1 = new Signal();
        TP10 = new Signal();
        patientState = new State(this);
        log_counter = 0;
        musesSpinner = (Spinner) findViewById(R.id.muses_spinner);
        MuseDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MUSE_FOLDER_NAME);
        fileTP9 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MUSE_FOLDER_NAME + File.separator + TP9_FILENAME +log_counter.toString()+ ".txt");
        fileTP10 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MUSE_FOLDER_NAME + File.separator + TP10_FILENAME +log_counter.toString()+ ".txt");
        fileFP1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MUSE_FOLDER_NAME + File.separator + FP1_FILENAME +log_counter.toString()+ ".txt");
        fileFP2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MUSE_FOLDER_NAME+ File.separator + FP2_FILENAME +log_counter.toString()+ ".txt");

        if (!MuseDir.isDirectory()) {
            MuseDir.mkdirs();
        }
        // // Uncommet to test Muse File Reader
        //
        // // file can be big, read it in a separate thread
        // Thread thread = new Thread(new Runnable() {
        //     public void run() {
        //         playMuseFile("testfile.muse");
        //     }
        // });
        // thread.start();

        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        fileWriter = MuseFileFactory.getMuseFileWriter(
                     new File(dir, "new_muse_file.muse"));
        Log.i("Muse Headband", "libmuse version=" + LibMuseVersion.SDK_VERSION);
        fileWriter.addAnnotationString(1, "MainActivity onCreate");
        dataListener.setFileWriter(fileWriter);
    }


    /*
     * Simple example of getting data from the "*.muse" file
     */
    private void playMuseFile(String name) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, name);
        final String tag = "Muse File Reader";
        if (!file.exists()) {
            Log.w(tag, "file doesn't exist");
            return;
        }
        MuseFileReader fileReader = MuseFileFactory.getMuseFileReader(file);
        while (fileReader.gotoNextMessage()) {
            MessageType type = fileReader.getMessageType();
            int id = fileReader.getMessageId();
            long timestamp = fileReader.getMessageTimestamp();
            Log.i(tag, "type: " + type.toString() +
                  " id: " + Integer.toString(id) +
                  " timestamp: " + String.valueOf(timestamp));
            switch(type) {
                case EEG: case BATTERY: case ACCELEROMETER: case QUANTIZATION:
                    MuseDataPacket packet = fileReader.getDataPacket();
                    Log.i(tag, "data packet: " + packet.getPacketType().toString());
                    break;
                case VERSION:
                    MuseVersion version = fileReader.getVersion();
                    Log.i(tag, "version" + version.getFirmwareType());
                    break;
                case CONFIGURATION:
                    MuseConfiguration config = fileReader.getConfiguration();
                    Log.i(tag, "config" + config.getBluetoothMac());
                    break;
                case ANNOTATION:
                    AnnotationData annotation = fileReader.getAnnotation();
                    Log.i(tag, "annotation" + annotation.getData());
                    break;
                default:
                    break;
            }
        }
    }


    private void configureLibrary() {
        muse.registerConnectionListener(connectionListener);
        muse.registerDataListener(dataListener,
                                  MuseDataPacketType.ACCELEROMETER);
        muse.registerDataListener(dataListener,
                                  MuseDataPacketType.EEG);
        muse.registerDataListener(dataListener,
                                  MuseDataPacketType.ALPHA_RELATIVE);
        muse.registerDataListener(dataListener,
                                  MuseDataPacketType.ARTIFACTS);
        muse.registerDataListener(dataListener,
                                  MuseDataPacketType.BATTERY);
        muse.setPreset(MusePreset.PRESET_14);
        muse.enableDataTransmission(dataTransmission);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add) {
            return true;
        }
        if (id == R.id.action_connect_muse) {
            List<Muse> pairedMuses = MuseManager.getPairedMuses();
            if (pairedMuses.size() < 1 ||
                    musesSpinner.getAdapter().getCount() < 1) {
                Log.w("Muse Headband", "There is nothing to connect to");
            }
            else {
                muse = pairedMuses.get(musesSpinner.getSelectedItemPosition());
                ConnectionState state = muse.getConnectionState();
                if (state == ConnectionState.CONNECTED ||
                        state == ConnectionState.CONNECTING) {
                    Log.w("Muse Headband",
                            "doesn't make sense to connect second time to the same muse");
                    return true;
                }
                configureLibrary();
                fileWriter.open();
                fileWriter.addAnnotationString(1, "Connect clicked");
                /**
                 * In most cases libmuse native library takes care about
                 * exceptions and recovery mechanism, but native code still
                 * may throw in some unexpected situations (like bad bluetooth
                 * connection). Print all exceptions here.
                 */
                try {
                    muse.runAsynchronously();
                } catch (Exception e) {
                    Log.e("Muse Headband", e.toString());
                }
            }
        }
        if (id == R.id.action_disconnect_muse) {
            if (muse != null) {
                /**
                 * true flag will force libmuse to unregister all listeners,
                 * BUT AFTER disconnecting and sending disconnection event.
                 * If you don't want to receive disconnection event (for ex.
                 * you call disconnect when application is closed), then
                 * unregister listeners first and then call disconnect:
                 * muse.unregisterAllListeners();
                 * muse.disconnect(false);
                 */
                muse.disconnect(true);
                fileWriter.addAnnotationString(1, "Disconnect clicked");
                fileWriter.flush();
                fileWriter.close();
            }
        }
        if (id == R.id.action_refresh_muse) {
            MuseManager.refreshPairedMuses();
            List<Muse> pairedMuses = MuseManager.getPairedMuses();
            List<String> spinnerItems = new ArrayList<String>();
            for (Muse m: pairedMuses) {
                String dev_id = m.getName() + "-" + m.getMacAddress();
                Log.i("Muse Headband", dev_id);
                spinnerItems.add(dev_id);
            }
            ArrayAdapter<String> adapterArray = new ArrayAdapter<String> (
                    this, android.R.layout.simple_spinner_item, spinnerItems);
            musesSpinner.setAdapter(adapterArray);
        }
        if (id == R.id.action_pause_muse) {
            dataTransmission = !dataTransmission;
            if (muse != null) {
                muse.enableDataTransmission(dataTransmission);
            }
        }
        if (id == R.id.action_log_muse) {
            try{
                fWriter = new FileWriter(fileTP9, true);
                fWriter.write(TP9.getString());
                fWriter.flush();
                fWriter.close();

                fWriter = new FileWriter(fileTP10, true);
                fWriter.write(TP10.getString());
                fWriter.flush();
                fWriter.close();

                fWriter = new FileWriter(fileFP1, true);
                fWriter.write(FP1.getString());
                fWriter.flush();
                fWriter.close();

                fWriter = new FileWriter(fileFP2, true);
                fWriter.write(FP2.getString());
                fWriter.flush();
                fWriter.close();
            } catch(Exception e) {
                e.printStackTrace();
            }

            Log.d("FileSaving ", "Saving Data On "+Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MUSE_FOLDER_NAME + File.separator);
        }
        return super.onOptionsItemSelected(item);
    }
}
