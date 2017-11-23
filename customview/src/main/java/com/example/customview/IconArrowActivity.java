package com.example.customview;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.customview.utils.BitmapUtil;
import com.example.customview.utils.PermissionUtil;
import com.example.customview.utils.UIUtils;

import static com.example.customview.utils.PermissionUtil.PERMISSION_STORAGE;

public class IconArrowActivity extends Activity implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private IconViewArrow icon;
    private BackgroundView backgroundView;
    private EditText txt_width;
    private EditText txt_height;
    private EditText txt_lineColor;
    private EditText txt_iconStrokeWidth;
    private EditText txt_iconPadding;
    private EditText txt_circleColor;
    private EditText txt_circlePadding;
    private EditText txt_circleStrokeColor;
    private EditText txt_circleStrokeWidth;
    private RadioGroup radioGroup;
    private CheckBox checkbox;

    private ViewGroup.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow);
        icon = findViewById(R.id.iconView);
        backgroundView = findViewById(R.id.backgroundView);
        layoutParams = icon.getLayoutParams();

        txt_width = findViewById(R.id.txt_width);
        txt_height = findViewById(R.id.txt_height);
        txt_lineColor = findViewById(R.id.txt_lineColor);
        txt_iconStrokeWidth = findViewById(R.id.txt_iconStrokeWidth);
        txt_iconPadding = findViewById(R.id.txt_iconPadding);
        txt_circleColor = findViewById(R.id.txt_circleColor);
        txt_circlePadding = findViewById(R.id.txt_circlePadding);
        txt_circleStrokeColor = findViewById(R.id.txt_circleStrokeColor);
        txt_circleStrokeWidth = findViewById(R.id.txt_circleStrokeWidth);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_left:
                        icon.setArrowDirection(IconViewArrow.LEFT);
                        break;
                    case R.id.radio_up:
                        icon.setArrowDirection(IconViewArrow.UP);
                        break;
                    case R.id.radio_right:
                        icon.setArrowDirection(IconViewArrow.RIGHT);
                        break;
                    case R.id.radio_down:
                        icon.setArrowDirection(IconViewArrow.DOWN);
                        break;
                }
            }
        });

        checkbox = findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(this);

        setEditTextListener(txt_width);
        setEditTextListener(txt_height);
        setEditTextListener(txt_lineColor);
        setEditTextListener(txt_iconStrokeWidth);
        setEditTextListener(txt_iconPadding);
        setEditTextListener(txt_circleColor);
        setEditTextListener(txt_circlePadding);
        setEditTextListener(txt_circleStrokeColor);
        setEditTextListener(txt_circleStrokeWidth);
    }

    private void setEditTextListener(EditText editText) {
        editText.setFilters(new InputFilter[]{lengthFilter});
        editText.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d("###", v.toString());
        if (hasFocus || !(v instanceof EditText)) {
            return;
        }

        try {
            EditText txt = (EditText) v;
            String value = txt.getText().toString();
            switch (txt.getId()) {
                case R.id.txt_width:
                    layoutParams.width = toPx(value);
                    icon.requestLayout();
                    icon.rePaint();
                    backgroundView.setLayoutParams(layoutParams);
                    break;
                case R.id.txt_height:
                    layoutParams.height = toPx(value);
                    icon.requestLayout();
                    icon.rePaint();
                    break;
                case R.id.txt_lineColor:
                    icon.setLineColor(toColor(value));
                    break;
                case R.id.txt_circleColor:
                    icon.setCircleColor(toColor(value));
                    break;
                case R.id.txt_circleStrokeColor:
                    icon.setCircleStrokeColor(toColor(value));
                    break;
                case R.id.txt_iconPadding:
                    icon.setIconPadding(toPx(value));
                    break;
                case R.id.txt_circlePadding:
                    icon.setCirclePadding(toPx(value));
                    break;
                case R.id.txt_circleStrokeWidth:
                    icon.setCircleStrokeWidth(toPx(value));
                    break;
                case R.id.txt_iconStrokeWidth:
                    icon.setStrokeWidth(toPx(value));
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "输入格式有误", Toast.LENGTH_SHORT).show();
        }
    }

    private int toPx(String value) {
        if (value.endsWith("dp")) {
            value = value.substring(0, value.indexOf("dp"));
        }
        return UIUtils.dip2px(Float.valueOf(value));
    }

    private int toColor(String value) {
        int color = (int) Long.parseLong(value, 16);
        if ((0xFF000000 & color) == 0) {
            return 0xFF000000 | color;
        } else {
            return color;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        findViewById(R.id.line1).setVisibility(isChecked ? View.VISIBLE : View.GONE);
        icon.setCircleBg(isChecked);
    }

    public void exportXml(View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("<org.qiyi.basecore.widget.IconViewClose").append("\n");
        sb.append("android:layout_width=\"").append(txt_width.getText().toString()).append("dp\"").append("\n");
        sb.append("android:layout_height=\"").append(txt_height.getText().toString()).append("dp\"").append("\n");
        sb.append("app:circleBg=\"").append(checkbox.isChecked()).append("\"").append("\n");
        sb.append("app:iconPadding=\"").append(txt_iconPadding.getText().toString()).append("dp\"").append("\n");
        sb.append("app:circlePadding=\"").append(txt_circlePadding.getText().toString()).append("dp\"").append("\n");
        sb.append("app:iconStrokeWidth=\"").append(txt_iconStrokeWidth.getText().toString()).append("dp\"").append("\n");
        sb.append("app:circleStrokeWidth=\"").append(txt_circleStrokeWidth.getText().toString()).append("dp\"").append("\n");
        sb.append("app:lineColor=\"#").append(txt_lineColor.getText().toString()).append("\"").append("\n");
        sb.append("app:circleColor=\"#").append(txt_circleColor.getText().toString()).append("\"").append("\n");
        sb.append("app:circleStrokeColor=\"#").append(txt_circleStrokeColor.getText().toString()).append("\"").append("\n");
        sb.append(" />");
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, sb.toString()));
            Toast.makeText(this, "XML定义已复制到剪切板", Toast.LENGTH_SHORT).show();
        }
    }

    public void exportPng(View view) {
        if (PermissionUtil.hasSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            saveIcon();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }
    }

    private void saveIcon() {
        icon.setDrawingCacheEnabled(true);
        if (BitmapUtil.saveBitmapToSdCard(icon.getDrawingCache(), "iconView")) {
            Toast.makeText(this, "图片已经输出到SD卡根目录:iconView.png", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length <= 0 || grantResults.length <= 0) {
            return;
        }
        if (requestCode == PERMISSION_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveIcon();
        }
    }

    /**
     * 设置小数位数控制
     */
    private InputFilter lengthFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            if (dest.length() >= 8) {
                return "";
            }
            try {
                long number = Long.parseLong(dValue + source, 16);
                Log.d("###", ": " + number);
                return null; // 返回null代表使用原始输入
            } catch (Exception e) {
                e.printStackTrace();
                return ""; // 空字符串代替输入
            }
        }
    };

    public void clickIcon(View view) {
        Toast.makeText(this, "icon click", Toast.LENGTH_SHORT).show();
    }
}
