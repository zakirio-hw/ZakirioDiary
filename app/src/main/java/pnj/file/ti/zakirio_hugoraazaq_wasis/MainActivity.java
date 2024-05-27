package pnj.file.ti.zakirio_hugoraazaq_wasis;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button listBtn, createBtn, openBtn;
    ArrayList<String> files;
    String title;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.create_item) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Buat File");
            View layout = getLayoutInflater().inflate(R.layout.file_create_layout, null, false);
            alert.setView(layout);
            EditText fileName = layout.findViewById(R.id.file_name);
            EditText fileContent = layout.findViewById(R.id.file_content);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
            String formattedDateTime = now.format(formatter);
            fileName.setText(formattedDateTime + ".txt");
            alert.setPositiveButton("Buat", ((dialog, which) -> {
                addFile(fileName.getText().toString(), fileContent.getText().toString());
            })).setNegativeButton("Tutup", ((dialog, which) -> dialog.dismiss())).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_items, menu) ;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar ab = getSupportActionBar();

        listView = findViewById(R.id.fileList);
        // listBtn = findViewById(R.id.listBtn);
        // createBtn = findViewById(R.id.createBtn);
        // openBtn = findViewById(R.id.openBtn);
        files = new ArrayList<>();

        /* createBtn.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Buat File");
            View layout = getLayoutInflater().inflate(R.layout.file_create_layout, null, false);
            alert.setView(layout);
            EditText fileName = layout.findViewById(R.id.file_name);
            EditText fileContent = layout.findViewById(R.id.file_content);
            long tsLong = System.currentTimeMillis()/1000;
            fileName.setText(tsLong + ".txt");
            alert.setPositiveButton("Buat", ((dialog, which) -> {
                addFile(fileName.getText().toString(), fileContent.getText().toString());
            })).setNegativeButton("Tutup", ((dialog, which) -> dialog.dismiss())).show();
        }); */
    }

    private void addFile(String name, String content) {
        try {
            FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            loadFiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFiles() {
        File currentDir = getFilesDir();
        files.clear();
        files.addAll(Arrays.asList(currentDir.list()));
        FileAdapter adapter = new FileAdapter(files, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            showFileContent(files.get(position));
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(files.get(position));
            return true;
        });
    }

    private void showDeleteConfirmationDialog(String fileName) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus file ini?")
                .setPositiveButton("Ya", (dialog, which) -> removeFile(fileName))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void removeFile(String s) {
        File f = new File(getFilesDir(), s);
        f.delete();
        loadFiles();
    }

    private void showFileContent(String s) {
        StringBuilder sb = new StringBuilder();
        File f = new File(getFilesDir(), s);
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit File");
        View layout = getLayoutInflater().inflate(R.layout.file_create_layout, null, false);
        alert.setView(layout);
        EditText fileName = layout.findViewById(R.id.file_name);
        EditText fileContent = layout.findViewById(R.id.file_content);
        fileName.setText(s);
        fileContent.setText(sb.toString());
        alert.setPositiveButton("Edit", ((dialog, which) -> {
            updateFile(fileName.getText().toString(), fileContent.getText().toString());
        })).setNegativeButton("Tutup", ((dialog, which) -> dialog.dismiss())).show();
    }

    private void updateFile(String name, String content) {
        try {
            FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
            loadFiles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}