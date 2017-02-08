package yang.demo.swt;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class windowLocation {
	private final static int leftX = 100;
	private static int mouseDown = 0;
	private static int oldMouseX, oldMouseY;
	/**
	 * ��ȡwindows �������Ĵ�С
	 * @return�����������߶����ȵ�point����
	 */
	public static Point getTaskBarSize(){
		 Rectangle screenRec = Display.getDefault().getBounds();
		 Rectangle screenNoTaskbar = Display.getDefault().getClientArea();
		 int taskbarHeight = screenRec.height - screenNoTaskbar.height; 
		 Point p = new Point(screenRec.width,taskbarHeight);
		 return p;
	 }
	/**
	 * ����Ļ�м����һ������
	 * 
	 * @param shell
	 */
	public static void showWindowOnScreenCenter(Shell shell) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - shell.getSize().x) / 2;
		int y = (screenSize.height - shell.getSize().y) / 2;
		shell.setLocation(x, y);
	}
	/**
	 * ��ֹ���ڳ�����Ļ
	 * �˷����������ձ��ԣ�����black�����ĳЩShell�������
	 * @param location ����λ�õ�
	 * @param shell ��ҪУ׼��Shell����
	 * @param addvalue �ڻ���λ�õ�location֮�ϵ����긽��ֵ������Ϊnull
	 * 
	 */
	public static Point windowAlwaysInScreen(Point location,Shell parent, Shell shell,Caret caret){
		int newX = 0,newY = 0;
		int width = 0, height = 0;
		int lineheightatcaretoffset = (int)caret.getData();
		int caretsizeX = 0,caretsizeY =0;
		if(caret != null){
			caretsizeX = caret.getSize().x;
			caretsizeY = caret.getSize().y;
		}
		if(parent != null){
			width = returnScreenWidth();
			//width = parent.getSize().x;
			height = parent.getSize().y;
		}else{
			width = returnScreenWidth();
			height = returnScreenHeight();
		}
		int locationX = location.x;
		int locationY = location.y;
		int sizeX = shell.getSize().x;
		int sizeY = shell.getSize().y;
		
		if(locationX <= 0){
			newX = 0;
		}
		if(locationX+sizeX+caretsizeX >= width){
			newX = width-sizeX;
		}
		if(locationY <= 0){
			newY = 0;
		}
		if(locationY+sizeY+caretsizeY >= height){
			if(caret.getStyle() == SWT.VERTICAL)
				newY = locationY-sizeY;
			else newY = locationY-sizeY-lineheightatcaretoffset;
		}
		if(newX == 0) newX = locationX;
		if(newY == 0) {
			if(caret.getStyle() == SWT.VERTICAL)
				newY = locationY+caretsizeY;
			else newY = locationY+caret.getSize().y;
		}
		shell.setLocation(newX, newY);
		return new Point(newX,newY);
	}
	/**
	 * �ڸ����ڵ����½ǳ����Ӵ���
	 * @param parent
	 * @param shell
	 */
	public static void dialogLocationOnLeftBottom(Shell parent, Shell shell){
		shell.setLocation(parent.getSize().x-shell.getSize().x+parent.getLocation().x
				,parent.getSize().y-shell.getSize().y+parent.getLocation().y);
	}
	/**
	 * ����Ļ���½ǳ��ִ���
	 * @param shell
	 */
	public static void onLeftBottomInScreen(Shell shell){
		Rectangle rect = shell.getDisplay().getClientArea();
		int x = rect.width-shell.getSize().x;
		int y = rect.height-shell.getSize().y;
		shell.setLocation(x,y);
	}
	/**
	 * ͨ�������ĸ������������Ӵ��ڳ��ֵ�λ��
	 * 
	 * @param parent
	 * @param shell
	 */
	public static void dialogLocation(Shell parent, Shell shell) {
		int x = ((parent.getSize().x - shell.getSize().x) / 2)
				+ parent.getLocation().x;
		int y = ((parent.getSize().y - shell.getSize().y) / 2)
				+ parent.getLocation().y;
		shell.setLocation(x, y);

	}

	/**
	 * ����Ļ��߳���һ���Ի���
	 * 
	 * @param parent
	 * @param shell
	 */
	public static void dialogLeftLocation(Shell parent, Shell shell) {
		int y = ((parent.getSize().y - shell.getSize().y) / 2)
				+ parent.getLocation().y;
		shell.setLocation(leftX, y);
	}

	/**
	 * �ڸ������·�ƫ��һ���ľ�������Ӵ���
	 * 
	 * @param oldWindow
	 * @param newWindow
	 */
	public static void newWindow(Shell oldWindow, Shell newWindow) {
		newWindow.setLocation(oldWindow.getLocation().x + 10,
				oldWindow.getLocation().y + 10);
	}

	/**
	 * ������Ļ���
	 * 
	 * @return
	 */
	public static int returnScreenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}

	/**
	 * ������Ļ�߶�
	 * 
	 * @return
	 */
	public static int returnScreenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}

	/**
	 * ������϶�����
	 * 
	 * @param shell
	 */
	public static void reMoveWindow(final Shell shell) {

		shell.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseDown = 0;
			}

			@Override
			public void mouseDown(MouseEvent e) {

				mouseDown = 1;
				oldMouseX = e.x;
				oldMouseY = e.y;
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				

			}
		});

		shell.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {

				if (mouseDown == 1) {
					if (shell.getDisplay().getCursorLocation().y == 0)
						shell.setMaximized(true);
					else {
						int x = shell.getLocation().x + (e.x - oldMouseX);
						int y = shell.getLocation().y + (e.y - oldMouseY);
						shell.setLocation(x, y);
					}

				}

			}
		});

	}
}
