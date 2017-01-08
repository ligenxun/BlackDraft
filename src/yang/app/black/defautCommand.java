package yang.app.black;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.PackageElement;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.allPurpose.debug;
import yang.demo.allPurpose.time;

public class defautCommand implements Serializable {
	static final long serialVersionUID = 42L;

	static String NOONCURRENT = "�������ڵ�ǰ����������";

	public static void throwNo(black black, StyledText st) {
		black.ba.insertText(NOONCURRENT, st);
	}

	public static void defaultCommand(final black black, final StyledText st, blackTextArea bta) {
		bta.addCommand(new command("ʱ��") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.insertText(time.getCurrentTime(), st);
			}
		});
		bta.addCommand(new command("����") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.insertText(time.getCurrentDate("-"), st);
			}
		});
		bta.addCommand(new command("������") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.resetCaret(SWT.HORIZONTAL);
			}
		});
		bta.addCommand(new command("������") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.resetCaret(SWT.VERTICAL);
			}
		});
		bta.addCommand(new command("����", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (command_tr.text != null)
					black.ba.findinAllText(command_tr.text);
			}
		});
		bta.addCommand(new command("�о�", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (command_tr.text.matches("[0-9]{2}"))
					black.setEditerLineSpace(Integer.valueOf(command_tr.text));
			}
		});
		bta.addCommand(new command("�����ô�ã�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.getMessageBox("����������",
						"���ڱ༭���ڲ���������\n�������ĳЩ����ֻ��ͨ�����������ģ�\n�������ĸ�ʽ������:\n1. ��ֵ�ֻ࣬�����롰@��+���������ʹ�û���ɫֻ�����롰@����ɫ��"
								+ "\n2. ��ֵ�࣬�����롰@��+�����+��ֵ��+���ո񡱣���Ҫ�ı�༭�����о�Ϊ20�������롰@�о�20 ��");
			}
		});
		bta.addCommand(new command("��ʾ��������Ŀ���") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (black.wv == null || black.wv.isDisposed()) {
					black.tree.setVisible(!black.tree.getVisible());
					black.resetLayoutData();

				} else
					throwNo(black, st);
			}
		});
		bta.addCommand(new command("��ʾ�����صױ���", "������ʱ��ʾ�����صױ����������ý��ڳ����˳�ʱʧЧ") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (black.wv == null || black.wv.isDisposed()) {
					black.composite.setVisible(!black.composite.getVisible());
					black.resetLayoutData();
				} else
					throwNo(black, st);
			}
		});

		bta.addCommand(new command("д����ͼ") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.openWritingView();
			}
		});

		bta.addCommand(new command("�˳�����") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.dispose();
			}
		});

		 bta.addCommand(new command("��PDF��ʽ�����ǰ���༭���ļ�") {
		
		 @Override
		 public void action(TextRegion command_tr) {
		 // TODO Auto-generated method stub
		 black.ba.saveCurrentFileAsPDF();
		 }
		 });
		bta.addCommand(new command("��DOCX��ʽ�����ǰ���༭���ļ�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				FileDialog fd;
				if (black.wv == null || black.wv.isDisposed())
					fd = black.ba.getFileDialog("����Ϊdocx�ļ�","", black, SWT.SAVE, new String[] { "*.docx" });
				else
					fd = black.ba.getFileDialog("����Ϊdocx�ļ�","", black.wv, SWT.SAVE, new String[] { "*.docx" });
				if (fd.getFileNames().length == 1) {
					File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
					ioThread io = new ioThread(black);
					io.writeDocxFile(f, black.text, false);
					black.ba.getMessageBox("", "����ɹ�");
				}
			}
		});
		bta.addCommand(new command("��TXT��ʽ�����ǰ���༭���ļ�", "����׷���ַ����룬�粻׷�ӽ�ʹ��ƽ̨��Ĭ�ϱ���", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub

				FileDialog fd;
				if (black.wv == null || black.wv.isDisposed())
					fd = black.ba.getFileDialog("����ΪTXT�ļ�","", black, SWT.SAVE, new String[] { "*.txt" });
				else
					fd = black.ba.getFileDialog("����ΪTXT�ļ�", "",black.wv, SWT.SAVE, new String[] { "*.txt" });
				if (fd.getFileNames().length == 1) {
					File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
					if (black.ba.saveCurrentFileAsTXT(f, command_tr.text))
						black.ba.getMessageBox("", "����ɹ�");
					else
						black.ba.getMessageBox("", "����ʧ��");
				}

			}
		});
		bta.addCommand(new command("��TXT��ʽ���ȫ���ļ�", "����׷���ַ�����", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				FileDialog fd = null;
				if (black.wv == null || black.wv.isDisposed())
					fd = black.ba.getFileDialog("����ΪTXT�ļ�", "",black, SWT.SAVE, new String[] { "*.txt" });
				else
					fd = black.ba.getFileDialog("����ΪTXT�ļ�", "",black.wv, SWT.SAVE, new String[] { "*.txt" });
				if (fd.getFileNames().length == 1) {
					File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
					black.saveCurrentFile(false, false);
					findinfo_[] text = black.ba.getAllTextFromProject(true, false);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < text.length; i++) {
						if (text[i].stringbuilder != null)
							sb.append(text[i].stringbuilder.toString());
					}
					ioThread io = new ioThread(black);
					if (!command_tr.text.equals("")) {
						if (Charset.isSupported(command_tr.text)) {
							if (io.writeTextFile(f, sb.toString(), command_tr.text))
								black.ba.getMessageBox("", "����ɹ�");
							else
								black.ba.getMessageBox("", "����ʧ��");
						} else
							black.ba.getMessageBox("", "����֧�ֵ��ַ�����");
					} else {
						if (io.writeTextFile(f, sb.toString(), Charset.defaultCharset().displayName()))
							black.ba.getMessageBox("", "����ɹ�");
						else
							black.ba.getMessageBox("", "����ʧ��");
					}
				}

			}
		});
		bta.addCommand(new command("�������ļ���") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.getMessageBox("", "����������" + black.ba.getRecycleFileCount() + "���ļ�");
			}
		});
		bta.addCommand(new command("������������") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.restartBlack();
			}
		});
		bta.addCommand(new command("�����ļ�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				new exportFiles(black, SWT.None).open();
			}
		});
		bta.addCommand(new command("�����ļ�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				new importFiles(black, SWT.None).open();
			}
		});
		bta.addCommand(new command("��Ŀ����") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				new projectInfo(black, SWT.None).open();
			}
		});

		bta.addCommand(new command("���ټ���ʱͬʱ�����������ڵ��ļ�", "�ظ����ÿɹرջ���") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.includeRecycle = !black.ba.includeRecycle;
			}
		});
		bta.addCommand(new command("���ټ���ʱ�Ƿ�ͬʱ�����������ڵ��ļ���") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.insertText(String.valueOf(black.ba.includeRecycle), st);
			}
		});
		bta.addCommand(new command("����͸����", "���������ڵ�͸���ȣ�ֵ����0��255֮��", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (command_tr.text.matches("[0-9]{3}")) {
					int alpha = Integer.valueOf(command_tr.text);
					if (alpha <= 255) {
						if (black.wv == null || black.wv.isDisposed())
							black.setAlpha(alpha);
						else
							black.wv.setAlpha(alpha);
					}
				}
			}
		});

		bta.addCommand(new command("ɾ����ǰ�ĵ���ÿ���ı��п�ͷ�Լ�ĩβ������Ŀո�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < black.text.getLineCount(); i++) {
					String line = black.text.getLine(i);
					if (line.length() > 1) {
						// ɾ���п�ͷ�Ŀո�
						if (line.charAt(0) == ' ') {
							line = line.substring(1, line.length());
						}
						// ɾ����ĩβ�Ŀո�
						char lastSecChar = line.charAt(line.length() - 2);
						if (cheakDocument.cheak(lastSecChar)) {
							char lastChar = line.charAt(line.length() - 1);
							if (lastChar == ' ') {
								line = line.substring(0, line.length() - 1);
							}
						}
					}
					sb.append(line + "\n");

				}
				black.text.setText(sb.toString());
			}

		});

		bta.addCommand(new command("�г���ǰ�ĵ��ڵ����б��⣨ĩβȱ�������ı��м���Ϊ���⣩") {
			public void action(TextRegion command_tr) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < black.text.getLineCount(); i++) {
					String line = black.text.getLine(i);
					if (cheakDocument.cheakString(line))
						sb.append(line + "\n");
				}
				black.ba.getBMessageBox("�����б�", sb.toString());
			}
		});

		bta.addCommand(new command("��ϵͳ��Դ����������ʾ��ǰ���༭���ļ�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.showFileInExplorer();
			}

		});
		bta.addCommand(new command("���ñ���Ŀ¼") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.setBackupDir();
			}
		});
		bta.addCommand(new command("�򿪱���Ŀ¼") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				String dir = black.ba.getBackupDir();
				if (dir != null)
					black.ba.showinExplorer(dir, false);
				else
					black.ba.getMessageBox("������Ϣ", "��δ���ñ���Ŀ¼��");
			}
		});
		bta.addCommand(new command("��ʼ����") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.startBackup();
			}
		});
		bta.addCommand(new command("�༭Ԥ�����ļ�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.editMarkFile();
			}
		});
		// bta.addCommand(new command("�۽��ı���") {
		//
		// @Override
		// public void action(TextRegion command_tr) {
		// // TODO Auto-generated method stub
		// black.blackTextArea.test();
		// }
		// });
		bta.addCommand(new command("У����Ŀ�ڵ��ļ�") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.getFileInfo();
			}
		});
		bta.addCommand(new command("��Ԥ�����ļ����ݸĶ�д�����") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.writeToMarkFile();
			}
		});
		bta.addCommand(new command("���¶�ȡԤ�����ļ�����������") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.readMarkFile();
				black.ba.getMarkFileText();
			}
		});
		bta.addCommand(new command("����һ���������������������","����������������Ա𣨡��С���Ů����", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.getItalinaName(command_tr.text.charAt(0));
			}
		});
		bta.addCommand(new command("����һ��Ӣ����������������","����������������Ա𣨡��С���Ů����", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.getEnglishName(command_tr.text.charAt(0));
			}
		});
		bta.addCommand(new command("����������������������", "�������������Ҫ����������",true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				boolean haserror = false;
				int count = 0;
				try {
					count = Integer.valueOf(command_tr.text);
				} catch (NumberFormatException e) {
					haserror = true;
				}
				if (!haserror) {
					bta.black.ba.getChineseNames(count);
				}

			}
		});
		bta.addCommand(new command("����20����������") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (bta.black.namecreator == null) {
					bta.black.namecreator = new nameCreator(bta.black, new File("./nameCreator"));
				}
				if(bta.black.namecreator.isHasChineseNameData()){
					String[] names = bta.black.namecreator.getChineseNames(20);
					StringBuilder sb = new StringBuilder();
					for (String s : names) {
						sb.append(s + "\n");
					}
					bta.black.ba.getBMessageBox("������20����������", sb.toString());
				}else bta.black.ba.getMessageBox("������������", "�����������������ݣ�");
			}
		});
		bta.addCommand(new command("���¶�ȡ��������") {
			
			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (bta.black.namecreator == null) {
					bta.black.namecreator = new nameCreator(bta.black, new File("./nameCreator"));
				}
				bta.black.namecreator.getallfiles();
			}
		});
		bta.addCommand(new command("���ص���ģʽ") {
			
			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.debugMode(!bta.black.ba.isDebugMode(),false);
			}
		});
		bta.addCommand(new command("�Ƿ�Ϊ����ģʽ") {
			
			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.getMessageBox("������Ϣ", bta.black.ba.isDebugMode()+"");
			}
		});
//		bta.addCommand(new command("test") {
//			
//			@Override
//			public void action(TextRegion command_tr) {
//				// TODO Auto-generated method stub
//				bta.black.ba.saveAllAsText();
//			}
//		});
	}
}
