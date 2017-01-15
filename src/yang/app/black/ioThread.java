package yang.app.black;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.HWPFOldDocument;
import org.apache.poi.hwpf.model.TextPieceTable;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.custom.StyledText;


public class ioThread implements Runnable,Serializable{
	static final long serialVersionUID = 42L;

	private black black;
	private File file;
	private int writeOrRead;
	IDocument doc;
	TextViewer tv;
	Properties styles;
	Object o;
	/**
	 * 
	 * @param black
	 * @param file
	 * @param writeOrRead 1Ϊд�룬����ֵ���ʾ��ȡ
	 */
	public ioThread(black black, File file, int writeOrRead, IDocument doc, TextViewer tv, Properties styles) {
		this.black = black;
		this.file = file;
		this.writeOrRead = writeOrRead;
		this.doc = doc;
		this.tv = tv;
		this.styles = styles;
	}
	public ioThread(black black, Object o, File file, int writeorread){
		this.o = o;
		this.file = file;
		this.black = black;
		this.writeOrRead = writeorread;
	}
	public ioThread(black black){
		this.black = black;
	}

	@Override
	public void run() {

		if ((file.getName().matches(".*.black"))) {
			blackFileIO(writeOrRead, file);
		} else if(file.getName().matches(".*.blaobj")){
			if(writeOrRead == 1) writeObjFile(file, o);
			else o = readObjFile(file);
		}

	}

	public void blackFileIO(int writeOrRead, File file)// 1 is write
	{
		if(writeOrRead == 1){
			if(doc != null && styles != null)
			writeBlackFile(file, doc, styles);
		}else{
			if(tv == null)
				doc = readBlackFile(file, null);
			else doc = readBlackFile(file, tv);
		}
	}
	public boolean writeTextFile(File file, String text, String fileencode){
		OutputStreamWriter osr = null;
		try {
			osr = new OutputStreamWriter(new FileOutputStream(file),fileencode);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			BufferedWriter out = new BufferedWriter(osr);
			String newtext = text.replaceAll("\n", System.getProperty("line.separator","\n"));
			out.write(newtext);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public String readTextFile(File file,String fileencode){
		StringBuilder strBuilder = new StringBuilder();

		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(file), fileencode);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			BufferedReader in = new BufferedReader(isr, 10000);
			String str;

			while ((str = in.readLine()) != null) {
				strBuilder.append(str + "\n");
			}
			in.close();
			
		} catch (IOException e) {
		}
		return strBuilder.toString();
	}
	
	
	public void writeDocxFile(File file,StyledText text, boolean onlytext){
		try {
			FileOutputStream fos = new FileOutputStream(file);
			XWPFDocument document = new XWPFDocument();
			black.ba.saveStylesToDocxFile(text,document);
			try {
				document.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * ��ȡDocx�ļ�
	 * @param file docx�ļ�
	 * @param text Ҫ��ʾdocx�ļ����ݵ�StyledText���
	 * @param tv ��һ��text������������
	 * 
	 */
	public void readDocxFile(File file,StyledText text, TextViewer tv){
		try {
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument docx;
			try {
				docx = new XWPFDocument(fis);
				//XWPFWordExtractor xwe = new XWPFWordExtractor(docx);
				black.ba.getContentFromDocxFile(docx,text,tv);
				//black.tv.setDocument(new Document(xwe.getText()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String readDocFile(File file){
		FileInputStream fis = null;
		String text = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			HWPFDocument hwpf = new HWPFDocument(fis);
			text = hwpf.getText().toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	/**
	 * ����docx�ļ������ʽ���ԣ�ֻ��ȡ����Ĵ��ı�����
	 * @param file
	 * @return
	 */
	public String readDocxFile(File file){
		String text = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument docx;
			try {
				docx = new XWPFDocument(fis);
				XWPFWordExtractor xwe = new XWPFWordExtractor(docx);
				text = xwe.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	/**
	 * ����.black�ļ���д����
	 * @param file Ҫд�����ݵ�black�ļ�
	 * @param doc �ĵ�������Դ
	 * @param styles �ĵ����ݷ����ʽ��Դ������Ϊnull
	 */
	public void writeBlackFile(File file,IDocument doc, Properties styles){
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.setLevel(9);
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(out, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(osw);
		try {
			ZipEntry word = new ZipEntry("black_word");
			out.putNextEntry(word);
			bw.write(doc.get());
			bw.flush();
			if(styles != null){
				ZipEntry styles_entry = new ZipEntry("styles"); 
				out.putNextEntry(styles_entry);
				styles.storeToXML(out, null);
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡblack�ļ�
	 * @param file Ҫ��ȡ��black�ļ������black�ļ�������styles�����ļ�����ֻ��ȡ���ı�����
	 * @param textviewer Ҫ��ʾblack�ļ����ݵ�StyledText���
	 * ���textviewerΪ�գ�������black�ļ�����ʽ����ֻ��ȡ���ı�����
	 * @return ����black�ļ����ı����ݵ�Document����
	 */
	public IDocument readBlackFile(File file, TextViewer textviewer){
		StringBuilder strBuilder = new StringBuilder();
		ZipInputStream in = null;
		String fileName = null;
		Document doc = null;
		try {
			in = new ZipInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileName = in.getNextEntry().getName();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(isr);
		String str;

		try {
			if(fileName.equals("black_word")){
				while ((str = br.readLine()) != null) {
					strBuilder.append(str + "\n");
				}
				String text = null;
				if(strBuilder.length() > 0) text = strBuilder.substring(0, strBuilder.length()-1);
				else text = "";
				doc = new Document(text);
				if(textviewer != null){
					textviewer.setDocument(doc);
					ZipEntry entry = in.getNextEntry();
					if(entry != null){
						fileName = entry.getName();
						if(fileName.equals("styles")){
							Properties styles_ = new Properties();
							styles_.loadFromXML(in);
							black.ba.getStylesFromProperties(styles_,textviewer.getTextWidget());
						}
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	/**
	 * ��ȡ��������л��ļ�
	 * @param file
	 * @return
	 */
	public Object readObjFile(File file){
		FileInputStream fis = null;
		Object oo = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				oo = ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				fis.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			black.ba.getMessageBox("���ļ���ԭ����ʱ����", "���ļ���ԭ����ʱ����!\n"+"�ļ�·����"+file.getPath()+"\n"
					+"��ϸ��Ϣ��"+e.toString());
			if(file.delete())black.ba.getMessageBox("����ɾ��������ļ�", "��ɾ�������ļ���\n"+file.toString());
			else {
				black.ba.getMessageBox("����ɾ��������ļ�", "�ļ�ɾ��ʧ�ܣ����ڳ����˳����ٴγ���ɾ��");
				file.deleteOnExit();
			}
			
		}
		return oo;
	}

	public void writeObjFile(File file, Object o) {
		if (o != null) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				// BufferedOutputStream bos = new BufferedOutputStream(fos);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(o);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * ����һ����������ֽ������������ָ���ļ�file
	 * @param file
	 * @return
	 */
	public BufferedOutputStream getBufferedFileOutputStream(File file){
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bos = new BufferedOutputStream(fos);
		return bos;
	}
	/**
	 * ����һ����������ַ������������ָ���ļ�file
	 * @param file
	 * @return
	 */
	public BufferedWriter getBufferedFileWriter(File file){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		return bw;
	}
}
