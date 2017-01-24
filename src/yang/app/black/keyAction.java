package yang.app.black;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

public class keyAction {
	public static void addKeyAction(blackTextArea bta){
		//System.out.println(b.blackTextArea == null);
		bta.addKeyAction(new checkKey(SWT.CONTROL|'3') {
			@Override
			public void action() {
				// TODO Auto-generated method stub
				if(bta.black.ba.posForTypeMode == 2){
					bta.black.ba.posForTypeMode = 4;
					bta.black.ba.getMessageBox("���ֻ���", "�ѽ��༭���й̶�����Ļ1/4λ��");

				}else if(bta.black.ba.posForTypeMode == 4){
					bta.black.ba.posForTypeMode = 0;
					bta.black.ba.getMessageBox("���ֻ���", "�ѹرմ��ֻ���ģʽ");
				}else{
					bta.black.ba.posForTypeMode = 2;
					bta.black.ba.getMessageBox("���ֻ���", "�ѽ��༭���й̶�����Ļ����");
				}
				
			}
		});
//		bta.addKeyAction(new checkKey(SWT.CONTROL|'4') {
//			
//			@Override
//			public void action() {
//				// TODO Auto-generated method stub
//				//bta.black.ba.getBMessageBox("", "");
//				bta.black.ba.getFileInfo();
//			}
//		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|'5') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				if(bta.black.wv != null && !bta.black.wv.isDisposed() && bta.black.ba.isFullScreenWritingView()){
					int alpha = bta.black.ba.fullscreen.getAlpha();
					if(alpha == 150)
						bta.black.ba.fullscreen.setAlpha(200);
					else if(alpha == 200)
						bta.black.ba.fullscreen.setAlpha(0);
					else if(alpha == 0){
						bta.black.ba.fullscreen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
						bta.black.ba.fullscreen.setAlpha(255);
					}
					else if(alpha == 255){
						bta.black.ba.fullscreen.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
						bta.black.ba.fullscreen.setAlpha(150);
					}
				}
			}
		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|'6') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				if(bta.black.ba.isFullScreenWritingView()){
					bta.black.ba.setWritingView();
					bta.black.ba.getMessageBox("д����ͼ����", "��ȡ��ȫ��Ļд����ͼ");
				}else{
					bta.black.ba.setWritingView();
					bta.black.ba.getMessageBox("д����ͼ����", "������ȫ��Ļд����ͼ");
				}
			}
		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|'`') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				int offset = bta.st.getCaretOffset();
				if(offset != bta.st.getCharCount()){
					String text = bta.st.getText(offset, offset);
					if(text.equals("��")){
						bta.st.replaceTextRange(offset, 1, "��");
						bta.st.setSelection(offset, offset+1);
					}
				}
				
				//System.out.println(text);
			}
		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|SWT.SHIFT|'1') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.replaceMode = !bta.replaceMode;
				if(bta.replaceMode) bta.black.ba.getMessageBox("�����滻���ģʽ", "�����滻���ģʽ�ѿ���");
				else bta.black.ba.getMessageBox("�����滻���ģʽ", "�����滻���ģʽ�ѹر�");
			}
		});
//		bta.addKeyAction(new checkKey(SWT.CONTROL|'0') {
//			
//			@Override
//			public void action() {
//				// TODO Auto-generated method stub
//				bta.black.ba.toAnalysis("nr",false);
//				System.gc();
//			}
//		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|'9') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.black.ba.fastRepalce();
				//bta.black.ba.toAnalysis("",true);
			}
		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|'8') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
				for(int i=0; i<bta.black.text.getLineCount(); i++){
					String line = bta.black.text.getLine(i);
					if(cheakDocument.cheakString(line)) sb.append(line+"\n");
				}
				bMessageBox bes = new bMessageBox(bta.black,SWT.None,false) {
					
					@Override
					public void saveAction() {
						// TODO Auto-generated method stub
						
					}
				};
				bes.setTextFontinfo(12, SWT.ITALIC);
				bes.setText(sb.toString());
				bes.setTitle("�����б�");
				bes.text.addCaretListener(new CaretListener() {
					
					@Override
					public void caretMoved(CaretEvent arg0) {
						// TODO Auto-generated method stub
						StyledText st = bes.text;
						int index = st.getLineAtOffset(st.getCaretOffset());
						String str = st.getLine(index);
						String text = bta.st.getText();
						int indexoftext = text.indexOf(str);
						bta.st.setTopIndex(bta.st.getLineAtOffset(indexoftext));
					}
				});	
				bes.open();

			}
		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|'\\') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				String showname = bta.black.ba.getShowNameByRealName(bta.black.currentEditFile.getName());
				if(showname.indexOf("_�滻�б�") != -1){
					bta.black.ba.findErrorOnEditor(bta.st);
				}
			}
		});
		bta.addKeyAction(new checkKey(SWT.SHIFT|SWT.CONTROL|'`') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				if(bta.black.ba.logsmessage.length() == 0)
					bta.black.log.appendLog("", null, false);
				else bta.black.log.appendLog(bta.black.ba.logsmessage.toString(),null, false);
				bta.black.log.open();
				
			}
		});
		bta.addKeyAction(new checkKey(SWT.CONTROL|'`') {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.black.ba.getChineseName();
			}
		});
		bta.addKeyAction(new checkKey(SWT.F2) {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.black.ba.getItalinaName('m');
			}
		});
		bta.addKeyAction(new checkKey(SWT.F3) {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.black.ba.getItalinaName('f');
			}
		});
		bta.addKeyAction(new checkKey(SWT.F4) {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.black.ba.getEnglishName('m');
			}
		});
		bta.addKeyAction(new checkKey(SWT.F5) {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.black.ba.getEnglishName('f');
			}
		});
		bta.addKeyAction(new checkKey(SWT.F7) {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				bta.black.ba.getChineseNames(20);
			}
		});
	}
	
}
