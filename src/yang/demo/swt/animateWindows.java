package yang.demo.swt;

import org.eclipse.swt.widgets.Shell;

public class animateWindows {
	private static int animateSpeed = 5;//��Сֵ1�����ֵ255
	private static int animateSpeedForMenuDialogAndFileListDialog = 5;//��Сֵ1�����ֵ255
	/*
	 * ��Բ˵��Ի�����ļ��б�Ի���Ķ���Ч��
	 */
	
	
	public static void showWindow(Shell parent,Shell shell)
	{
		windowLocation.dialogLocation(parent, shell);
		shell.setVisible(true);
	}
	public static void closeBlack(Shell shell)
	{
		for(int i=255; i>=0; i--)
		{
			if(i%animateSpeedForMenuDialogAndFileListDialog == 0)
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			shell.setAlpha(i);
		}
		//stopAutoSaveTimer();
		shell.dispose();
	}
	public static void ����(Shell shell)
	{
		for(int i=255; i>=100; i--)
		{
			if(i%animateSpeedForMenuDialogAndFileListDialog == 0)
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			shell.setAlpha(i);
		}
	}
	public static void ����(Shell shell)
	{
		for(int i=100; i<=255; i++)
		{
			if(i%animateSpeedForMenuDialogAndFileListDialog == 0)
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			shell.setAlpha(i);
		}
	}
	public static void closeWindow(Shell shell)
	{		
		shell.close();
	}
	public static void showOpenDialogAndFileListDialog(Shell parent,Shell shell)
	{
		
		shell.setLocation(parent.getDisplay().getCursorLocation().x, parent.getDisplay().getCursorLocation().y);
		����(shell);
	}
	public static void hideOpenDialogAndFileListDialog(Shell shell)
	{
		����(shell);
		shell.close();
	}
	
	public static void windowSwitch(Shell currentShell, Shell toShell)
	{
		toShell.setVisible(true);
		//toShell.setFullScreen(true);
		toShell.setAlpha(0);
		for(int i=255; i>=0; i--)
		{
			if(i%animateSpeed == 0)
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//currentShell.setAlpha(i);
			toShell.setAlpha(255-i);
		}
		
		currentShell.dispose();
	}
}
