package com.example.vcam;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity {
    private AlertDialog mRequestPermissionDialog;
    private Switch force_show_switch;
    private Switch disable_switch;
    private Switch play_sound_switch;
    private Switch force_private_dir;
    private Switch disable_toast_switch;
    private Switch mSwitchMuteMic;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this, R.string.permission_lack_warn, Toast.LENGTH_SHORT).show();
            } else {
                File cameraDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/");
                if (!cameraDir.exists()) {
                    cameraDir.mkdir();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncSwitchesStatus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button repo_button = findViewById(R.id.button);
        force_show_switch = findViewById(R.id.switch1);
        disable_switch = findViewById(R.id.switch2);
        play_sound_switch = findViewById(R.id.switch3);
        force_private_dir = findViewById(R.id.switch4);
        disable_toast_switch = findViewById(R.id.switch5);
        mSwitchMuteMic = findViewById(R.id.switch_mute_mic);

        syncSwitchesStatus();

        repo_button.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/w2016561536/android_virtual_cam");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        Button repo_button_chinamainland = findViewById(R.id.button2);
        repo_button_chinamainland.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://gitee.com/w2016561536/android_virtual_cam");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        disable_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                if (missPermission()) {
                    request_permission();
                } else {
                    File disable_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/disable.jpg");
                    if (disable_file.exists() != b) {
                        if (b) {
                            try {
                                disable_file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            disable_file.delete();
                        }
                    }
                }
                syncSwitchesStatus();
            }
        });

        force_show_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                if (missPermission()) {
                    request_permission();
                } else {
                    File force_show_switch = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/force_show.jpg");
                    if (force_show_switch.exists() != b) {
                        if (b) {
                            try {
                                force_show_switch.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            force_show_switch.delete();
                        }
                    }
                }
                syncSwitchesStatus();
            }
        });

        play_sound_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                if (missPermission()) {
                    request_permission();
                } else {
                    File play_sound_switch = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/no-silent.jpg");
                    if (play_sound_switch.exists() != b) {
                        if (b) {
                            try {
                                play_sound_switch.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            play_sound_switch.delete();
                        }
                    }
                }
                syncSwitchesStatus();
            }
        });

        force_private_dir.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                if (missPermission()) {
                    request_permission();
                } else {
                    File force_private_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/private_dir.jpg");
                    if (force_private_dir.exists() != b) {
                        if (b) {
                            try {
                                force_private_dir.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            force_private_dir.delete();
                        }
                    }
                }
                syncSwitchesStatus();
            }
        });


        disable_toast_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                if (missPermission()) {
                    request_permission();
                } else {
                    File disable_toast_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/no_toast.jpg");
                    if (disable_toast_file.exists() != b) {
                        if (b) {
                            try {
                                disable_toast_file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            disable_toast_file.delete();
                        }
                    }
                }
                syncSwitchesStatus();
            }
        });

        mSwitchMuteMic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (missPermission()) {
                    request_permission();
                } else {
                    File muteMicFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/mute_mic.jpg");
                    if (muteMicFile.exists() != isChecked) {
                        if (isChecked) {
                            try {
                                muteMicFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            muteMicFile.delete();
                        }
                    }
                }
                syncSwitchesStatus();
            }
        });
    }

    private void request_permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.permission_lack_warn)
                        .setMessage(R.string.permission_description)
                        .setNegativeButton(R.string.negative, (dialogInterface, i) ->
                                Toast.makeText(this, R.string.permission_lack_warn, Toast.LENGTH_SHORT).show()
                        )
                        .setPositiveButton(R.string.positive, (dialogInterface, i) ->
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1));
                if (mRequestPermissionDialog != null) {
                    mRequestPermissionDialog.dismiss();
                }
                mRequestPermissionDialog = builder.show();
            }
        }
    }

    private boolean missPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
        }
        return false;
    }


    private void syncSwitchesStatus() {
        Log.d(this.getApplication().getPackageName(), "【VCAM】[sync]同步开关状态");

        if (missPermission()) {
            request_permission();
        } else {
            File camera_dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1");
            if (!camera_dir.exists()) {
                camera_dir.mkdir();
            }
        }

        File disable_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/disable.jpg");
        disable_switch.setChecked(disable_file.exists());

        File force_show_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/force_show.jpg");
        force_show_switch.setChecked(force_show_file.exists());

        File play_sound_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/no-silent.jpg");
        play_sound_switch.setChecked(play_sound_file.exists());

        File force_private_dir_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/private_dir.jpg");
        force_private_dir.setChecked(force_private_dir_file.exists());

        File disable_toast_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/no_toast.jpg");
        disable_toast_switch.setChecked(disable_toast_file.exists());

        File muteMic = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera1/mute_mic.jpg");
        mSwitchMuteMic.setChecked(muteMic.exists());
    }
}



