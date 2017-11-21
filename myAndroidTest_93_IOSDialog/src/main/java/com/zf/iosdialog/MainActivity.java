//package com.zf.iosdialog;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.zf.iosdialog.widget.ActionSheetDialog;
//import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
//import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;
//import com.zf.iosdialog.widget.AlertDialog;
//
//public class MainActivity extends Activity implements OnClickListener {
//	private Button btn1;
//	private Button btn2;
//	private Button btn3;
//	private Button btn4;
//	private Button btn5;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		initView();
//	}
//
//	private void initView() {
//		btn1 = (Button) findViewById(R.id.btn1);
//		btn1.setOnClickListener(this);
//		btn2 = (Button) findViewById(R.id.btn2);
//		btn2.setOnClickListener(this);
//		btn3 = (Button) findViewById(R.id.btn3);
//		btn3.setOnClickListener(this);
//		btn4 = (Button) findViewById(R.id.btn4);
//		btn4.setOnClickListener(this);
//		btn5 = (Button) findViewById(R.id.btn5);
//		btn5.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn1:
//			new ActionSheetDialog(MainActivity.this)
//					.builder()
//					.setTitle("�����Ϣ�б�������¼��Ȼ������ȷ��Ҫ�����Ϣ�б�")
//					.setCancelable(false)
//					.setCanceledOnTouchOutside(false)
//					.addSheetItem("�����Ϣ�б�", SheetItemColor.Red,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//
//								}
//							}).show();
//			break;
//		case R.id.btn2:
//			new ActionSheetDialog(MainActivity.this)
//					.builder()
//					.setCancelable(false)
//					.setCanceledOnTouchOutside(false)
//					.addSheetItem("���͸�����", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//
//								}
//							})
//					.addSheetItem("ת�ص��ռ����", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//
//								}
//							})
//					.addSheetItem("�ϴ���Ⱥ���", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//
//								}
//							})
//					.addSheetItem("���浽�ֻ�", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//
//								}
//							})
//					.addSheetItem("�ղ�", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//
//								}
//							})
//					.addSheetItem("�鿴����ͼƬ", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//
//								}
//							}).show();
//			break;
//		case R.id.btn3:
//			new ActionSheetDialog(MainActivity.this)
//					.builder()
//					.setTitle("��ѡ�����")
//					.setCancelable(false)
//					.setCanceledOnTouchOutside(false)
//					.addSheetItem("��Ŀһ", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀ��", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							})
//					.addSheetItem("��Ŀʮ", SheetItemColor.Blue,
//							new OnSheetItemClickListener() {
//								@Override
//								public void onClick(int which) {
//									Toast.makeText(MainActivity.this,
//											"item" + which, Toast.LENGTH_SHORT)
//											.show();
//								}
//							}).show();
//			break;
//		case R.id.btn4:
//			new AlertDialog(MainActivity.this).builder().setTitle("�˳���ǰ�˺�")
//					.setMsg("��������½15�죬�Ϳɱ���ΪQQ���ˡ��˳�QQ���ܻ�ʹ�����м�¼���㣬ȷ���˳���")
//					.setPositiveButton("ȷ���˳�", new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//
//						}
//					}).setNegativeButton("ȡ��", new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//
//						}
//					}).show();
//			break;
//		case R.id.btn5:
//			new AlertDialog(MainActivity.this).builder()
//					.setMsg("�������޷����յ�����Ϣ���ѡ��뵽ϵͳ-����-֪ͨ�п�����Ϣ����")
//					.setNegativeButton("ȷ��", new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//
//						}
//					}).show();
//			break;
//		default:
//			break;
//		}
//	}
//}
