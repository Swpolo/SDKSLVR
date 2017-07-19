package paul.sdkslvr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "SDKSLVR_mainActivity";

    public static Grid sdkGrid;
    SdkView     sdkView;
    SdkSolver   sdkSolver;
    int         solving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detect();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sdkView = (SdkView) findViewById(R.id.sdkView);

        sdkGrid = new Grid();
        solving = 0;

        sdkView.setBackgroundColor(Color.WHITE);

        sdkListener();
    }

    /***********************************************************************************************
     * SDK SOLVER
     */

    public void solve(View v) {
        // TODO : make more robust. Function cannot detect if impossible to solve
        sdkView.setCellUserSet();
        sdkSolver = new SdkSolver();
        sdkGrid.setGrid(sdkView.getGrid());
        while (sdkSolver.sdkSolve() == 0) ;
        sdkView.setGrid(sdkGrid.getGrid());
        sdkView.invalidate();
    }

    public void keyboard(View v) {
        Button b = (Button) findViewById(v.getId());
        sdkView.setCell(Integer.parseInt((b.getText().toString())));
        sdkView.invalidate();
    }

    private void reset(){
        sdkGrid.reset();
        sdkView.reset();
        solving = 0;
        sdkView.invalidate();
    }

    private void save() {
        // TODO : Allow player to save different files
        String fileContent = "";

        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                fileContent = fileContent + sdkView.getCell(col, row);
            }
        }
        Log.d("STATE", fileContent);

        String filename = "lastSDK.txt";

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(fileContent.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        // TODO : Allow player to load different files
        // TODO : Allow player to generate random sdk
        String filename = "lastSDK.txt";
        String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        int[][] newGrid = new int[9][9];
        if(file.exists()) {
            try {
                FileInputStream fis = openFileInput(filename);
                int col = 0;
                int row = 0;
                while(fis.available() != 0) {
                    newGrid[col][row] = fis.read() - '0';

                    row++;
                    if (row == 9) {
                        row = 0;
                        col++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            sdkView.setGrid(newGrid);
            sdkView.setCellUserSet();
            sdkView.invalidate();

        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "No saved SDK", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void sdkListener(){
        sdkView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float screenX = event.getX();
                float screenY = event.getY();
                sdkView.focusedCell((int) screenX, (int) screenY);
                sdkView.invalidate();

                return true;
            }
        });
    }

    /***********************************************************************************************
     * SDK DETECTION
     */

    static final int DETECT_SDK = 1;
    private void detect(){
        int hasPermissionCamera = cameraPermission();
        int hasPermissionWrite = writeExternalStoragePermission();
        if(hasPermissionCamera == PackageManager.PERMISSION_GRANTED && hasPermissionWrite == PackageManager.PERMISSION_GRANTED){
            Intent sdkFinderIntent = new Intent(this, SdkFinderActivity.class);
            //Intent sdkFinderIntent = new Intent(MainActivity.this, thirdActivity.class);
            startActivityForResult(sdkFinderIntent, DETECT_SDK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Toast toast = Toast.makeText(getApplicationContext(), "SDK FOUND", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "SDK NOT FOUND", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    /***********************************************************************************************
     * DRAWER
     */

    public void openDrawer(View v){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT, true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            reset();
        } else if (id == R.id.nav_save) {
            save();
        } else if (id == R.id.nav_load) {
            load();
        } else if (id == R.id.nav_camera) {
            detect();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_help ) {

        } else if (id == R.id.nav_about ) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    /***********************************************************************************************
     * PERMISSIONS
     */

    private final int CAMERA_PERMISSION = 1;
    private final int WRITEEXTERNAL_PERMISSION = 2;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    detect();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You must allow camera for this feature", Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }

            case WRITEEXTERNAL_PERMISSION : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    detect();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You must allow write external for this feature", Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
        }
    }

    private int cameraPermission(){
        int hasPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {

                // TODO : Ajouter un dialogue

            }
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
            hasPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CAMERA);
        }

        return hasPermission;
    }

    private int writeExternalStoragePermission(){
        int hasPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {

            if(!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // TODO : Ajouter un dialogue

            }

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

            hasPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return hasPermission;
    }
}
