package yang.app.black;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class autoSaveTimer implements Serializable{
	static final long serialVersionUID = 42L;

	private Timer timer;
	private TimerTask task;
	private black black;
	private long timeForAutoSave;
	
	public autoSaveTimer(black black)
	{
		this.black = black;
		timer = new Timer();
	}
	public void stop()
	{
		if(timer != null)
			timer.cancel();
	}
	/**
	 * �����Զ������ʱ�䣬����Ϊ��λ
	 * @param sec ���������Ϊ-1��ֹͣ�Զ�����
	 */
	public void setTimeForAutoSave(int sec)
	{
		if(sec != -1){
			if(sec > 0)
				timeForAutoSave = sec*1000;
		}else{
			if(timer != null)
				timer.cancel();
		}
	}
	public void start()
	{
		task = new TimerTask(){
			@Override
			public void run(){
				if(!black.isDisposed()){
					black.getDisplay().asyncExec(new Runnable(){
						@Override
						public void run()
						{
							if(!black.isDisposed())
							{
								if(black.getCurrentEditFile() != null && black.getEditer() != null && black.getFileIsSave() == 0)
									//black.getIO().save(black.getCurrentEditFile());
									black.saveCurrentFile(false, false);
							}
							else
								stop();
						}
					});	
				}else{
					stop();
				}
			
		}};
		timer.scheduleAtFixedRate(task, timeForAutoSave, timeForAutoSave);
		
	}
}
