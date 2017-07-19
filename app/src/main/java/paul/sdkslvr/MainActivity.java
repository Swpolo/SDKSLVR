package paul.sdkslvr;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
                // TODO AJOUTER FONCTION DETECTION GRILLE
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

    public void solve(View v) {
        sdkView.setCellUserSet();
        sdkSolver = new SdkSolver();
        sdkGrid.setGrid(sdkView.getGrid());
        while (sdkSolver.sdkSolve() == 0) ;
        sdkView.setGrid(sdkGrid.getGrid());
        sdkView.invalidate();
    }

    public void reset(View v){
        sdkGrid.reset();
        sdkView.reset();
        solving = 0;
        sdkView.invalidate();
    }

    public void keyboard(View v) {
        Button b = (Button) findViewById(v.getId());
        sdkView.setCell(Integer.parseInt((b.getText().toString())));
        sdkView.invalidate();
    }

    public void save(View v) {
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

    public void load(View v) {
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

    public void sdkListener(){
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

        } else if (id == R.id.nav_save) {

        } else if (id == R.id.nav_load) {

        } else if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_help ) {

        } else if (id == R.id.nav_about ) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
