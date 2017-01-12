package yang.app.black;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.swt.windowLocation;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;

public class gitBranchInfo extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table table;
	black b;
	Menu menu;
	private Button button;
	private Group group;
	Collection<Ref> allBranchFromRemote = null;


	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public gitBranchInfo(Shell parent, int style) {
		super(parent, style);
		b = (black)parent;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		setTable();
		setTableMenu();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(665, 481);
		shell.setText("\u64CD\u4F5C\u4ED3\u5E93\u5206\u652F");
		windowLocation.dialogLocation(getParent(), shell);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton.setBounds(574, 417, 75, 25);
		btnNewButton.setText("\u786E\u5B9A");
		
		group = new Group(shell, SWT.NONE);
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group.setText("\u4ED3\u5E93\u5206\u652F");
		group.setBounds(10, 10, 639, 387);
		
		table = new Table(group, SWT.FULL_SELECTION|SWT.MULTI);
		table.setBounds(10, 28, 619, 338);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		menu = new Menu(table);
		table.setMenu(menu);
		
		button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reLoadData();
			}
		});
		button.setBounds(10, 417, 101, 25);
		button.setText("\u66F4\u65B0\u6570\u636E");

	}
	public void setTable(){
		b.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showProgress showProgress = new showProgress(b) {
					
					@Override
					void actionWhenOKButtonSelected() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					void actionInOtherThread() {
						// TODO Auto-generated method stub
						table.removeAll();
						group.setText("�ֿ��֧("+allBranchFromRemote.size()+")");
						for(Ref r:allBranchFromRemote){
							TableItem tableItem= new TableItem(table, SWT.NONE);
							tableItem.setText(0, r.getName());
						}
					}
				};
				showProgress.setTitle("��Զ�ֿ̲��ȡ����");
				showProgress.open();
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				b.ba.progressMessage = "��Զ�ֿ̲��ȡ����...";
				b.ba.ProgressValue = 20;
				allBranchFromRemote = b.ba.getAllBranchFromRemote();
				b.ba.progressMessage = "��ɣ�";
				b.ba.ProgressValue = 100;
			}
		}).start();
		
	}
	public void setTableMenu(){
		MenuItem remove = b.ba.getMenuItem(menu, "��Զ�ֿ̲���ɾ��", SWT.NONE);
		remove.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() == 0) return;
				
				TableItem[] selection = table.getSelection();
				ArrayList<String> al = new ArrayList<>();
				for(TableItem ti:selection){
					al.add(ti.getText(0));
				}
				
				
				if(selection != null){
					b.getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress showProgress = new showProgress(b) {
								@Override
								void actionInOtherThread() {
									// TODO Auto-generated method stub
									reLoadData();

								}

								@Override
								void actionWhenOKButtonSelected() {
									// TODO Auto-generated method stub
									
								}
							};
							showProgress.setTitle("��Զ�ֿ̲�ɾ��");
							showProgress.open();
						}
					});
					
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int progr = 0;
							if(al.size() > 0)
								progr = 100/al.size();
							for(String str:al){
								b.ba.deleteBranchFromRemote(str,false);
								b.ba.progressMessage = "���ڴ�Զ�˲ֿ�ɾ��"+str;
								b.ba.ProgressValue += progr;
								b.ba.addlogs("ɾ��"+str);
							}
							b.ba.progressMessage = "��ɣ�";
							b.ba.ProgressValue = 100;
						}
					}).start();
					
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem remove_ = b.ba.getMenuItem(menu, "��Զ�ֿ̲�ͱ��زֿ���ɾ��", SWT.NONE);
		remove_.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() == 0) return;
				
				TableItem[] selection = table.getSelection();
				ArrayList<String> al = new ArrayList<>();
				for(TableItem ti:selection){
					al.add(ti.getText(0));
				}
				
				
				if(selection != null){
					b.getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress showProgress = new showProgress(b) {
								@Override
								void actionInOtherThread() {
									// TODO Auto-generated method stub
									reLoadData();

								}

								@Override
								void actionWhenOKButtonSelected() {
									// TODO Auto-generated method stub
									
								}
							};
							showProgress.setTitle("��Զ�̺ͱ��زֿ���ɾ��");
							showProgress.open();
						}
					});
					
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int progr = 0;
							if(al.size() > 0)
								progr = 100/al.size();
							for(String str:al){
								b.ba.deleteBranchFromRemote(str,true);
								b.ba.progressMessage = "���ڴ�Զ�˲ֿ�ɾ��"+str;
								b.ba.ProgressValue += progr;
								b.ba.addlogs("ɾ��"+str);
							}
							b.ba.progressMessage = "��ɣ�";
							b.ba.ProgressValue = 100;
						}
					}).start();
					
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem copy = b.ba.getMenuItem(menu, "������ѡ�ķ�֧������", SWT.NONE);
		copy.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() == 0)return;
				DirectoryDialog dd = new DirectoryDialog(b);
				String dir = dd.open();
				if(dir == null) return;
				ArrayList<String> coll = new ArrayList<>();
				TableItem[] selection = table.getSelection();
				for(TableItem t:selection){
					coll.add(t.getText(0));
				}
				b.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress showProgress = new showProgress(b) {
							
							@Override
							void actionWhenOKButtonSelected() {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							void actionInOtherThread() {
								// TODO Auto-generated method stub
								
							}
						};
						showProgress.setTitle("��Զ�ֿ̲�����");
						showProgress.open();
					}
				});
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						b.ba.progressMessage = "���ڴ�Զ�ֿ̲�����";
						b.ba.ProgressValue = 20;
						b.ba.copyBranchFromeRemote(dir, null, coll, false);
						b.ba.progressMessage = "�������";
						b.ba.ProgressValue = 100;
					}
				}).start();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem copyall = b.ba.getMenuItem(menu, "�������еķ�֧������", SWT.NONE);
		copyall.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getItemCount() == 0)return;
				DirectoryDialog dd = new DirectoryDialog(b);
				String dir = dd.open();
				if(dir == null) return;
				
				b.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress showProgress = new showProgress(b) {
							
							@Override
							void actionWhenOKButtonSelected() {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							void actionInOtherThread() {
								// TODO Auto-generated method stub
								
							}
						};
						showProgress.setTitle("��Զ�ֿ̲�����");
						showProgress.open();
					}
				});
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						b.ba.progressMessage = "��ʼ��Զ�ֿ̲�����";
						b.ba.ProgressValue = 20;
						b.ba.copyBranchFromeRemote(dir, null, null, true);
						b.ba.progressMessage = "��ɣ�";
						b.ba.ProgressValue = 100;
					}
				}).start();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem saveto = b.ba.getMenuItem(menu, "����ǰ��Ŀ���ص���ѡ��֧", SWT.NONE);
		MenuItem crate = b.ba.getMenuItem(menu, "������֧", SWT.NONE);
		crate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				rename re = new rename(b, SWT.NONE);
				re.setTitle("������֧");
				String name = re.open();
				if(name != null){
					b.getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress showProgress = new showProgress(b) {
								
								@Override
								void actionWhenOKButtonSelected() {
									// TODO Auto-generated method stub
									reLoadData();
								}
								
								@Override
								void actionInOtherThread() {
									// TODO Auto-generated method stub
									
								}
							};
							showProgress.setTitle("�½���֧�����浽Զ�ֿ̲�");
							showProgress.open();
						}
					});
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							b.ba.progressMessage = "���ڽ������ϴ���Զ�ֿ̲�";
							b.ba.ProgressValue = 20;
							b.ba.createBranch(name);
							b.ba.progressMessage = "��ɣ�";
							b.ba.ProgressValue = 100;
						}
					}).start();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

	}
	public void reLoadData(){
		setTable();
	}
}
