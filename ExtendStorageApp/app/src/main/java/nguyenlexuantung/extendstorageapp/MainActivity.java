package nguyenlexuantung.extendstorageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ArrayList<Contact> arrContact;
    TextView textView;
    ListView listView;
    Button btnInsert, btnSearch, btnDelete;
    EditText etName, etPhone, etAddress;
    MyArrayAdapter myArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.et_Name);
        etPhone = (EditText) findViewById(R.id.et_Phone);
        etAddress = (EditText) findViewById(R.id.et_Address);

        textView = (TextView) findViewById(R.id.tvShow);
        listView = (ListView) findViewById(R.id.listview);
        btnSearch = (Button) findViewById(R.id.button_search);
        btnInsert = (Button) findViewById(R.id.button_insert);
        btnDelete = (Button) findViewById(R.id.button_delete);
        loadData();

        myArrayAdapter = new MyArrayAdapter(this, R.layout.my_contact, arrContact);
        listView.setAdapter(myArrayAdapter);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDataByName();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact pickedContact = (Contact) adapterView.getItemAtPosition(i);
                etName.setText(pickedContact.getName());
                etPhone.setText(pickedContact.getPhone());
                etAddress.setText(pickedContact.getAddress());
            }
        });
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("CONTACT", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contact list", null);

        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();

        arrContact = gson.fromJson(json, type);

        if (arrContact == null) {
            arrContact = new ArrayList<>();
        }

    }

    private void saveData() {
        arrContact.add(new Contact(
                etAddress.getText().toString(),
                etName.getText().toString(),
                etPhone.getText().toString()
        ));
        myArrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = getSharedPreferences("CONTACT", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(arrContact);

        json.replace("&quot;", "'");
        editor.putString("contact list", json);
        editor.apply();

        etAddress.setText("");
        etName.setText("");
        etPhone.setText("");
    }

    /// Search by Name and Phone
    private void search() {
        SharedPreferences sharedPreferences = getSharedPreferences("CONTACT", MODE_PRIVATE);
        String json = sharedPreferences.getString("contact list", null);

//        try {
//            JSONArray contacts = new JSONArray(json);
//            for (int i = 0; i < contacts.length(); i++) {
//                JSONObject c = contacts.getJSONObject(i);
//                etName.setText(c.getString("Name"));
//                etAddress.setText(c.getString("Address"));
//                etPhone.setText(c.getString("Phone"));
//
//            }
//        } catch (Throwable t) {
//            Toast.makeText(this, "Could not parse malformed JSON " + t.toString(), Toast.LENGTH_LONG).show();
////            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
//        }

        if (!etName.getText().toString().isEmpty() || !etPhone.getText().toString().isEmpty()) {
            try {

                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);

                    if (c.getString("Name").equals(etName.getText().toString()) ||
                            c.getString("Phone").equals(etPhone.getText().toString())) {
                        etName.setText(c.getString("Name"));
                        etAddress.setText(c.getString("Address"));
                        etPhone.setText(c.getString("Phone"));
                        break;
                    } else if (c.getString("Name").equals(etName.getText().toString()) &&
                            !c.getString("Phone").equals(etPhone.getText().toString())) {

                        textView.setText("Name and Phone is not form same object");
                    }
                }
            } catch (Throwable t) {
                Toast.makeText(this, "Could not parse malformed JSON " + t.toString(), Toast.LENGTH_LONG).show();
            }
        } else if (etName.getText().toString().isEmpty() && etPhone.getText().toString().isEmpty()) {
            textView.setText("At least Name or Phone");
        }
    }

    private void deleteDataByName() {
        SharedPreferences sharedPreferences = getSharedPreferences("CONTACT", MODE_PRIVATE);
        String json = sharedPreferences.getString("contact list", null);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        if (!etName.getText().toString().isEmpty()) {
            try {
                arrContact.clear();
                JSONArray contacts = new JSONArray(json);
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    /// Convert JSON object to Java object
                    Gson gson = new Gson();
                    Contact convertedJson = gson.fromJson(c.toString(), Contact.class);
                    arrContact.add(convertedJson);

                    if (c.getString("Name").equals(etName.getText().toString())) {
                        arrContact.remove(i);
                        myArrayAdapter.notifyDataSetChanged();

                        prefsEditor.remove("contact list");
                        String postJson = gson.toJson(arrContact);
                        json.replace("&quot;", "'");
                        prefsEditor.putString("contact list", postJson);
                        prefsEditor.apply();

                        etAddress.setText("");
                        etName.setText("");
                        etPhone.setText("");

                        Toast.makeText(this, "DELETED", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            } catch (Throwable t) {
                Toast.makeText(this, "Could not parse malformed JSON " + t.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "NAME field is required", Toast.LENGTH_LONG).show();
        }
    }
}