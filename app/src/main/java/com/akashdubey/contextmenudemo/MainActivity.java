package com.akashdubey.contextmenudemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String [] name={"Bhagat Singh","Sukhdev","Rajguru","Kumar Vishwas","Anna Hazare"};
    String [] phone={"1111111111","2222222222","3333333333","4444444444","5555555555"};
    static String temp_phone;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView=(ListView)findViewById(R.id.listview1);
        CustomAdapter adapter=new CustomAdapter();
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.context,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        Toast.makeText(this, "Context will come here ", Toast.LENGTH_SHORT).show();

        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position=info.position;
        if (item.getItemId()==R.id.call ){
            Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+phone[position]));

            if(Build.VERSION.SDK_INT>=23) {
                if(checkSelfPermission(Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED){
                    startActivity(intent);
                }else{
                    this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
                }
            }
            startActivity(intent);

        }else if(item.getItemId()==R.id.sms){


            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this);
            Uri uri= Uri.parse("sms:"+phone[position]);
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                intent.putExtra("sms_body","This is my sms message text");
                intent.setData(uri);



                if(Build.VERSION.SDK_INT>=23){
                    if(checkSelfPermission(Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
                        startActivity(intent);
                    }else{
                        this.requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 100);
                    }
                }
                intent.setPackage(defaultSmsPackageName);
                startActivity(intent);
                // how to handle sms app in kitkat, very important, but i cant get it working..
                //https://android-developers.googleblog.com/2013/10/getting-your-sms-apps-ready-for-kitkat.html
            }



        return super.onContextItemSelected(item);

    }

    public class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.custom,null);
            TextView nameView=(TextView)view.findViewById(R.id.nameTV);
            TextView phoneView=(TextView)view.findViewById(R.id.phoneTV);

            nameView.setText(name[i]);
            phoneView.setText(phone[i]);

            return view;
        }
    }
}
