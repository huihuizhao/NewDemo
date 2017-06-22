package com.example.newdemo;

import com.example.newdemo.util.MyToast;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginActivity extends Activity implements OnClickListener {
	
	private EditText et_phone;
	private EditText et_pwd;
	private ImageButton ib_delete_phone;
	private CheckBox cb_eye;
	private TextView tv_forget_pwd;
	private Button bt_login;
	private Button bt_register;
	
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private String userName, password;
	private CheckBox remeber_pswd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		ib_delete_phone = (ImageButton) findViewById(R.id.ib_delete_phone);
		cb_eye = (CheckBox) findViewById(R.id.cb_eye);
		tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
		bt_login = (Button) findViewById(R.id.bt_login);
		bt_register = (Button) findViewById(R.id.bt_register);
		
		
		bt_login.setOnClickListener(this);
		bt_register.setOnClickListener(this);
		tv_forget_pwd.setOnClickListener(this);
		ib_delete_phone.setOnClickListener(this);
		
//		remeber_pswd = (CheckBox)findViewById(R.id.remeber_pswd);
//		boolean isRemeber = pref.getBoolean("remeberPswd", false);
		
		//密码显示隐藏
		cb_eye.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {//选中隐藏
					et_pwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else {
					et_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
			}
		});
		
		initModle();
	}

	
	private void initModle() {
		pref = PreferenceManager.getDefaultSharedPreferences(this);
	}
	

	@Override
	public void onClick(View v) {
//		Intent intent = new Intent();
		switch (v.getId()) {
		//登录
		case R.id.bt_login:
			String telephone = et_phone.getText().toString();
			String password = et_pwd.getText().toString();
			if (telephone.equals("18888888888") && password.equals("1")) {
//				editor = pref.edit();
//				if (remeber_pswd.isChecked()) {
//					editor.putBoolean("remeberPswd", true);
//					editor.putString("userName", telephone);
//					editor.putString("password", password);
//				} else {
//					editor.clear();
//				}
//				editor.commit();
//				Intent intent = new Intent(LoginActivity.this, MapActivity.class);
//				startActivity(intent);
				
				Intent intent = new Intent(LoginActivity.this, UploadActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			} else {
				MyToast.showToast(LoginActivity.this, "请输入正确的手机号或密码");
			}
			break;
		//注册
		case R.id.bt_register:
//			intent.setClass(this, LoginRegisterActivity.class);
//			startActivity(intent);
			break;
		//忘记密码
		case R.id.tv_forget_pwd:
//			intent.setClass(this, ForgetPwdActivity.class);
//			startActivity(intent);
			break;
		//删除手机号
		case R.id.ib_delete_phone:
			et_phone.setText("");
			break;
		default:
			break;
		}
	}
	
}
