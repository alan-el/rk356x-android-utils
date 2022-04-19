package com.xiaotu.usbswitch;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.xiaotu.usbswitch.databinding.ActivityUsbSwitchBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class USBModeSwitchActivity extends AppCompatActivity {

    private static final String TAG = "USBModeSwitch";
    private static final String FILE_PATHNAME = "/sys/devices/platform/fe8a0000.usb2-phy/otg_mode";
    private static final String OTG_MODE = "otg";
    private static final String HOST_MODE = "host";
    private static final String DEVICE_MODE = "peripheral";
    private int btnIDHost, btnIDDevice;

    private ActivityUsbSwitchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUsbSwitchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnIDHost = binding.buttonHostMode.getId();
        btnIDDevice = binding.buttonDeviceMode.getId();

//        getCurrentUSBMode();
    }

    public void switchUSBMode(View view)
    {
        int viewID = view.getId();

        try (PrintStream ps = new PrintStream(FILE_PATHNAME)){
            if(viewID == btnIDHost)
                ps.println(HOST_MODE);
            else if(viewID == btnIDDevice)
                ps.println(DEVICE_MODE);

            ps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* Update current USB mode. */
        getCurrentUSBMode(null);
    }

    public void getCurrentUSBMode(View view)
    {
        File f_otg_mode = new File(FILE_PATHNAME);

        if((f_otg_mode.exists()) && f_otg_mode.canRead())
        {
            try {
                FileInputStream fin = new FileInputStream(f_otg_mode);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                CharSequence cur_mode = reader.readLine();
                fin.close();
                binding.textViewCurUSBMode.setText(cur_mode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}