package yang.app.black;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.allPurpose.debug;
import yang.demo.allPurpose.time;
import yang.demo.swt.windowLocation;

public class blackTextArea implements Serializable {
	static final long serialVersionUID = 42L;
	static int addSpaceAfterParagraph = 0;// unuse
	black black;
	int wordIndex, wordLength;// ���ҵ�����ֵ
	private StyleRange[] oldsr;// ���ַ�����ǰ�ķ��
	StyledText st;
	KeyListener readingModeKey, defaultKeyListener;
	KeyListener typeModeListener;
	ExtendedModifyListener commandListener;
	Color oldColor;
	int oldLineIndex;
	MouseListener forStyledPanel;
	List<StyleRange[]> styles;
	TextStyle defaultStyle;
	ArrayList<Integer> parspace = new ArrayList<>();
	ArrayList<checkKey> cheakkey = new ArrayList<checkKey>();
	ArrayList<command> commands = new ArrayList<command>();
	mycaret caret;
	String linestr;
	checkKey keyenter;
	int lastcaretOffset, caretOffset, keycode;
	int currentEditLine;
	CaretListener cl = null;
	ScrollBar scrollbar;
	checkKey ck_enter;
	boolean replaceMode;

	public blackTextArea(final StyledText st, final black black) {
		this.black = black;
		this.st = st;
		dropText();
		st.addExtendedModifyListener(new ExtendedModifyListener() {

			@Override
			public void modifyText(ExtendedModifyEvent arg0) {
				// TODO Auto-generated method stub
				scrollForTypeMode();
			}
		});

		restoreSettings();
		addDefaultKeyListener();
		addMenu(st);
		addModifyListener(st);

		// st.getVerticalBar().addSelectionListener(new SelectionListener() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent arg0) {
		// // TODO Auto-generated method stub
		// if(st.getTopPixel() == 0 && st.getTopMargin() != 100){
		// st.setTopMargin(100);
		// }else if(st.getTopPixel() != 0 && st.getTopMargin() !=
		// 0)st.setTopMargin(0);
		// }
		//
		// @Override
		// public void widgetDefaultSelected(SelectionEvent arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		st.setKeyBinding(SWT.TAB, -1);
		st.setKeyBinding(SWT.ESC, -1);
		st.setKeyBinding(SWT.CTRL | 'm', -1);
		st.setKeyBinding(SWT.HOME, ST.TEXT_START);
		st.setKeyBinding(SWT.END, -1);
		st.setKeyBinding(127, -1);
		st.setKeyBinding(13, -1);
		st.setKeyBinding(16777225, -1);// ���θ�дģʽ����
//		st.getVerticalBar().getDisplay().addFilter(SWT.Paint, new Listener() {
//			
//			@Override
//			public void handleEvent(Event arg0) {
//				// TODO Auto-generated method stub
//				if(!st.isDisposed()){
//					st.getVerticalBar().setMaximum(st.getVerticalBar().getMaximum()+500);
//				}
//			}
//		});
//		 st.addKeyListener(new KeyListener() {
//		
//		 @Override
//		 public void keyReleased(KeyEvent arg0) {
//		 // TODO Auto-generated method stub
//			 
//		 }
//		
//		 @Override
//		 public void keyPressed(KeyEvent arg0) {
//		 // TODO Auto-generated method stub
//			 System.out.println(arg0.keyCode);
//		 }
//		 });

		ck_enter = new checkKey(13) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				if (st.getSelectionText().equals("")) {
					st.setRedraw(false);
					black.ba.insertText("\n", st);
					scrollForTypeMode();
					st.setRedraw(true);
				} else {
					if (!black.getCurrentEditFile().equals(black.ba.getRealFile("Ԥ����")))
						black.ba.addTextToMarkFile(black.text.getSelectionText());
				}

			}
		};
		addKeyAction(ck_enter);

		st.setKeyBinding(8, -1);
		addKeyAction(new checkKey(8) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				st.setRedraw(false);
				st.invokeAction(ST.DELETE_PREVIOUS);
				scrollForTypeMode();
				st.setRedraw(true);
			}
		});

		scrollbar = st.getVerticalBar();
		addKeyAction(new checkKey(SWT.TAB) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				scrollForTypeMode();
				if (black.ba.findi == null)
					black.ba.findInfo();
			}
		});

		keyAction.addKeyAction(this);
		replaceMode();

		st.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				// TODO Auto-generated method stub
				if (st.getSelectionCount() != 0)
					black.label_4
							.setText(black.ba.getCharsCount(st.getSelectionText()) + "/" + black.ba.getCharsCount());
				else {
					if (black.label_4.getText().indexOf("/") != -1)
						black.label_4.setText(String.valueOf(black.ba.getCharsCount()));
				}
			}
		});
	}

	public void replaceMode() {
		st.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.stateMask == SWT.CONTROL)
					black.ba.replaceMode(st, new Point(arg0.x, arg0.y));
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		st.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (replaceMode)
					if (arg0.stateMask == SWT.CONTROL)
						black.ba.replaceMode(st, new Point(arg0.x, arg0.y));
			}

			@Override
			public void mouseExit(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEnter(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void re() {
		int h = st.getClientArea().height / 2;
		scrollbar.setMaximum(scrollbar.getMaximum() + h);

	}

	public void test() {
		if (cl == null) {
			st.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
			cl = new CaretListener() {

				@Override
				public void caretMoved(CaretEvent arg0) {

					// TODO Auto-generated method stub

					int line = st.getLineAtOffset(st.getCaretOffset());
					Point p = black.ba.currentLineOffest();
					// �����ǰ�༭���з����˸ı�
					if (line != currentEditLine) {
						int startoffest = st.getOffsetAtLine(currentEditLine);
						st.setStyleRange(new StyleRange(startoffest, st.getLine(currentEditLine).length(),
								SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY), null));

						StyleRange sr = new StyleRange(p.x, p.y - p.x, SWTResourceManager.getColor(SWT.COLOR_BLACK),
								null);
						st.setStyleRange(sr);
						currentEditLine = line;
					} else {// �����껹��ͬһ����
						int start = 0;
						if (p.x > 0)
							start = p.x - 1;
						else
							start = p.x;

						StyleRange sr = new StyleRange(start, p.y - p.x, SWTResourceManager.getColor(SWT.COLOR_BLACK),
								null);
						st.setStyleRange(sr);
					}

				}
			};
			st.addCaretListener(cl);
		} else {
			st.removeCaretListener(cl);
			cl = null;
			st.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		}
	}

	void dropText() {
		String DRAG_START_DATA = "DRAG_START_DATA";
		final DragSource source = new DragSource(st, DND.DROP_COPY | DND.DROP_MOVE);

		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new DragSourceAdapter() {
			Point selection;

			public void dragStart(DragSourceEvent event) {
				selection = st.getSelection();
				event.doit = selection.x != selection.y;
				st.setData(DRAG_START_DATA, selection);
			}

			public void dragSetData(DragSourceEvent e) {
				e.data = st.getText(selection.x, selection.y - 1);
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_MOVE) {
					Point newSelection = st.getSelection();
					int length = selection.y - selection.x;
					int delta = 0;
					if (newSelection.x < selection.x)
						delta = length;
					st.replaceTextRange(selection.x + delta, length, "");
				}
				selection = null;
				st.setData(DRAG_START_DATA, null);
			}
		});

		DropTarget target = new DropTarget(st, DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
		target.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		target.addDropListener(new DropTargetAdapter() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if (st.getData(DRAG_START_DATA) == null)
						event.detail = DND.DROP_COPY;
					else
						event.detail = DND.DROP_MOVE;
				}
			}

			public void dragOperationChanged(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if (st.getData(DRAG_START_DATA) == null)
						event.detail = DND.DROP_COPY;
					else
						event.detail = DND.DROP_MOVE;
				}
			}

			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
			}

			public void drop(DropTargetEvent event) {
				if (event.detail != DND.DROP_NONE) {
					Point selection = (Point) st.getData(DRAG_START_DATA);
					int insertPos = st.getCaretOffset();
					if (event.detail == DND.DROP_MOVE && selection != null && selection.x <= insertPos
							&& insertPos <= selection.y
							|| event.detail == DND.DROP_COPY && selection != null && selection.x < insertPos
									&& insertPos < selection.y) {
						st.setSelection(selection);
						event.detail = DND.DROP_COPY; // prevent source from
														// deleting selection
					} else {
						String string = (String) event.data;
						st.insert(string);
						if (selection != null)
							st.setSelectionRange(insertPos, string.length());
					}
				}
			}
		});
	}

	public void restoreSettings() {
		st.setIndent(black.getEditerIndent());
		addCaret();
		st.setCaret(caret);
		addCommandListener();
		addDefaultCommand();
		normalMode();

	}

	public void addCaret() {
		if (Boolean.valueOf(black.appProperties.getProperty("CaretV", "true")))
			caret = new mycaret(st, SWT.VERTICAL);
		else
			caret = new mycaret(st, SWT.HORIZONTAL);
	}

	public void addModifyListener(final StyledText st) {
		st.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				black.ba.setFileChanged();
			}
		});

		// Ϊ��������ַ�������ʽ
		st.addExtendedModifyListener(new ExtendedModifyListener() {

			public void modifyText(ExtendedModifyEvent event) {

				if (!st.isDisposed()) {
					if (event.start > 0 && event.start <= st.getCharCount()) {
						int alignment = st.getLineAlignment(st.getLineAtOffset(event.start - 1));
						st.setLineAlignment(st.getLineAtOffset(event.start), 1, alignment);

						StyleRange s = st.getStyleRangeAtOffset(event.start - 1);
						if (s != null && s.start + s.length < st.getCharCount()) {
							s.start = event.start;
							s.length = 1;

							st.setStyleRange(s);
						}
					}
					if (event.start + event.length == st.getCharCount() && defaultStyle != null) {
						StyleRange sr = new StyleRange(defaultStyle);
						sr.start = event.start;
						sr.length = event.length;
						st.setStyleRange(sr);
						defaultStyle = null;
					}
				}
			}
		});
	}

	public void addCommandListener() {
		if (commandListener == null) {
			commandListener = new ExtendedModifyListener() {

				@Override
				public void modifyText(ExtendedModifyEvent event) {
					// TODO Auto-generated method stub
					linestr = st.getLine(st.getLineAtOffset(st.getCaretOffset()));
					String command_str = "@";
					TextRegion tr_command_str = cheakDocument.find(linestr, command_str);
					// ����⵽�û�����@ʱ
					if (st.getCaretOffset() > 0) {
						String currentStr = st.getText(st.getCaretOffset() - 1, st.getCaretOffset() - 1);
						if (currentStr.equals(command_str)) {
							TextRegion tr_current = new TextRegion(currentStr, st.getCaretOffset() - 1,
									st.getCaretOffset());
							if (tr_current != null) {
								if (black.ba.findi == null) {
									if (black.wv == null || black.wv.isDisposed())
										black.ba.findi = new findinfo(black, black, SWT.None);
									else
										black.ba.findi = new findinfo(black.wv, black, SWT.None);

									for (int i = 0; i < commands.size(); i++) {
										command com = commands.get(i);
										TreeItem ti = new TreeItem(black.ba.findi.tree, SWT.None);
										ti.setData("index", i);
										ti.setData("textregion", new TextRegion(com.command, tr_current.end,
												tr_current.end + com.command.length()));
										if (com.describe != null)
											ti.setText(com.command + "(" + com.describe + ")");
										else
											ti.setText(com.command);
									}
									if (black.ba.findi.tree.getItemCount() > 0)
										black.ba.findi.tree.setSelection(black.ba.findi.tree.getItem(0));
									black.ba.findi.setVisible(true);
								}
							}
						} else {
							// ̽���û��ڵ�ǰ����������ַ�ǰ���Ƿ����@����
							if (tr_command_str != null) {
								if (tr_command_str.start + getLineOffset() < st.getCaretOffset()) {
									String str = linestr.substring(tr_command_str.start + 1,
											st.getCaretOffset() - getLineOffset());
									if (black.ba.findi == null)
										black.ba.findi = new findinfo(black, black, SWT.None);
									else
										black.ba.findi.tree.removeAll();
									int a = 0;
									for (int i = 0; i < commands.size(); i++) {
										command com = commands.get(i);
										int index = com.command.indexOf(str);
										if (index != -1) {
											TreeItem ti = new TreeItem(black.ba.findi.tree, SWT.None);
											ti.setData("index", a);
											ti.setData("textregion",
													new TextRegion(com.command, tr_command_str.start + getLineOffset(),
															tr_command_str.end + getLineOffset() + str.length()));
											if (com.describe != null)
												ti.setText(com.command + "(" + com.describe + ")");
											else
												ti.setText(com.command);
											a++;
										}
									}
									black.ba.findi.setVisible(true);
								}
							}
						}
					}

					if (tr_command_str != null)
						for (int i = 0; i < commands.size(); i++) {
							command com = commands.get(i);

							TextRegion command_tr = cheakDocument.find(linestr, command_str + com.command);
							if (command_tr != null) {
								if (!com.needmore) {
									st.replaceTextRange(getLineOffset() + command_tr.start,
											command_tr.end - command_tr.start, "");
									com.action(command_tr);

								} else {
									TextRegion tr = cheakDocument.subString(linestr,
											command_tr.start + command_tr.text.length(), " ");
									if (tr != null) {
										st.replaceTextRange(getLineOffset() + command_tr.start,
												command_tr.text.length() + tr.text.length() + 1, "");
										com.action(tr);
									}
								}
								break;
							}
						}
				}
			};
			st.addExtendedModifyListener(commandListener);
		}
	}

	public void addCommand(command com) {
		if (com != null)
			commands.add(com);
		else
			throw new NullPointerException();
	}

	public void removeAllCommand() {
		commands.removeAll(new ArrayList<command>());
	}

	public void removeCommand(command com) {
		if (com != null)
			commands.remove(com);
		else
			throw new NullPointerException();
	}

	public void addDefaultCommand() {
		defautCommand.defaultCommand(black, st, this);
	}

	public int getLineOffset() {
		return st.getOffsetAtLine(st.getLineAtOffset(st.getCaretOffset()));
	}

	// public void insertForCommandListener(String text, int start, int end) {
	// st.replaceTextRange(start, end - start, text);
	// st.setCaretOffset(start + text.length());
	// }

	public void showMark() {
		int lineindex = st.getLineAtOffset(st.getCaretOffset());
		int lineoffset = st.getOffsetAtLine(lineindex);
		List<TextRegion> lis = new cheakDocument().splitString(st.getLine(st.getLineAtOffset(st.getCaretOffset())), ' ',
				false, null);
		Iterator<TextRegion> it = lis.iterator();
		StyleRange sr = new StyleRange();
		while (it.hasNext()) {
			TextRegion tr = it.next();
			sr.background = SWTResourceManager.getColor(SWT.COLOR_GREEN);
			sr.start = tr.start + lineoffset;
			sr.length = tr.end - tr.start;
			st.setStyleRange(sr);

		}
	}

	public void writingView() {
		// st.setBackground(SWTResourceManager.getColor(175, 205, 133));
		st.getVerticalBar().setVisible(false);
		st.setFocus();
		MenuItem git = black.ba.getMenuItem(st.getMenu(), "�ύ���ĵ�GitԶ�ֿ̲�", SWT.NONE);
		git.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				String[] gitInfo = black.ba.getGitInfo();
				if(gitInfo != null)
					black.ba.gitWorking(gitInfo[0],gitInfo[1],gitInfo[2]);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		black.ba.getMenuItem(st.getMenu(), "", SWT.SEPARATOR);
		black.ba.getMenuItem(st.getMenu(), "�˳�д����ͼ	ESC", SWT.None).addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				black.wv.exit();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		MenuItem mi1 = black.ba.getMenuItem(st.getMenu(), "", SWT.SEPARATOR);
		MenuItem mi2 = black.ba.getMenuItem(st.getMenu(), "�ַ���	" + black.ba.getCharsCount() + "����", SWT.None);

		mi2.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				black.ba.wordCountStat(black.wv);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		st.getMenu().addMenuListener(new MenuListener() {

			@Override
			public void menuShown(MenuEvent arg0) {
				// TODO Auto-generated method stub
				if (st.getSelectionCount() == 0)
					mi2.setText("�ַ���	" + black.ba.getCharsCount() + "����");
				else {
					int count = black.ba.getCharsCount(st.getSelectionText());
					mi2.setText("�ַ���	" + count + "/" + black.ba.getCharsCount());
				}
				if(black.ba.gitSetUp()) git.setEnabled(true);
				else git.setEnabled(false);
			}

			@Override
			public void menuHidden(MenuEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * ��ͨģʽ
	 */
	public void normalMode() {
		st.setFont(new Font(st.getDisplay(), black.getEditorDefaultFontFromCFG(),
				black.getZoomedFontSize(black.ba.getEditerDefaultFontSize()), 0));
		st.setLineSpacing(black.getEditerLineSpace());
		st.setFocus();
		addKeyActionForNormal();

		// ��ʾ�༭�����
		if (forStyledPanel == null)
			forStyledPanel = new MouseListener() {

				public void mouseUp(MouseEvent e) {
					if (e.button == 3)
						if (black.wv == null || black.wv.isDisposed())
							new editerStyledPanel(black, new Point(e.x, e.y)).open();
				}

				@Override
				public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
					// TODO Auto-generated method stub

				}

			};

		st.addMouseListener(forStyledPanel);
	}

	/**
	 * ��������ı�ʱ���õĸ������
	 */
	public void clearSelection() {
		if (oldsr != null && black.getEditer() != null && !black.getEditer().isDisposed()) {
			st.setStyleRanges(oldsr);
		}
		if (styles != null && black.getEditer() != null && !black.getEditer().isDisposed()) {
			Iterator<StyleRange[]> it = styles.iterator();
			while (it.hasNext()) {
				black.getEditer().setStyleRanges(it.next());
			}
		}
	}

	public void clearFindHistory() {
		wordIndex = wordLength = 0;
	}

	/**
	 * ����
	 * 
	 * @param word
	 */
	public void findWord(IDocument doc, String word, boolean forwardSearch, boolean caseSensitive, boolean wholeWord,
			boolean showAll, boolean regularExpressions) {
		clearSelection();
		FindReplaceDocumentAdapter frda = new FindReplaceDocumentAdapter(doc);
		if (forwardSearch == true)
			wordIndex += wordLength;
		else
			wordIndex -= 1;
		// �Ƿ���ʾȫ��
		if (!showAll) {
			IRegion ir = null;
			try {
				ir = frda.find(wordIndex, word, forwardSearch, caseSensitive, wholeWord, regularExpressions);

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (ir != null) {
				wordIndex = ir.getOffset();
				int line = st.getContent().getLineAtOffset(wordIndex);
				st.setTopIndex(line);
				wordLength = ir.getLength();
				st.setSelection(wordIndex, wordIndex + wordLength);
				// StyleRange sr = new StyleRange(wordIndex, wordLength,
				// black.color_textBackground,
				// org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
				// // ������ַ�����ǰ�ķ��
				// oldsr = st.getStyleRanges(wordIndex, wordLength);
				// st.setStyleRange(sr);

			} else {
				if (wordIndex == 0)
					black.ba.getMessageBox("����/�滻��Ϣ", "δ���ҵ����");
				else {
					if (forwardSearch)
						black.ba.getMessageBox("����/�滻��Ϣ", "�Ѳ������ĵ�ĩβ���´ν����ĵ���ʼ���²���");
					else
						black.ba.getMessageBox("����/�滻��Ϣ", "�Ѳ������ĵ����ˣ��´ν����ĵ�ĩβ���²���");
				}
				wordIndex = 0;
				wordLength = 0;
			}
		} else {// ��ʾȫ����
			IRegion ir;
			int i = 0;
			try {
				while ((ir = frda.find(wordIndex, word, forwardSearch, caseSensitive, wholeWord,
						regularExpressions)) != null) {
					styles = new ArrayList<StyleRange[]>();
					wordIndex = ir.getOffset();
					wordLength = ir.getLength();
					StyleRange sr = new StyleRange(wordIndex, wordLength, black.color_textBackground,
							org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
					styles.add(black.getEditer().getStyleRanges(wordIndex, wordLength));
					black.getEditer().setStyleRange(sr);
					wordIndex += wordLength;
					++i;
				}
				black.ba.getMessageBox("����/�滻��Ϣ", "���ҵ�" + i + "��");
				wordIndex = 0;
				wordLength = 0;
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * �滻�ı�
	 * 
	 * @param word1
	 *            Ҫ�滻���ַ���
	 * @param word2
	 *            �滻Ϊ
	 */
	public int replace(IDocument doc, String word1, String word2, boolean forwardSearch, boolean caseSensitive,
			boolean wholeWord, boolean showmessage, boolean regularExpressions) {
		int i = 0;
		try {
			FindReplaceDocumentAdapter frda = new FindReplaceDocumentAdapter(doc);
			IRegion ir = new IRegion() {

				@Override
				public int getOffset() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int getLength() {
					// TODO Auto-generated method stub
					return 0;
				}
			};
			while ((ir = frda.find(ir.getOffset() + 1, word1, true, caseSensitive, wholeWord,
					regularExpressions)) != null) {
				frda.replace(word2, regularExpressions);
				i++;
			}
			if (showmessage)
				black.ba.getMessageBox("����/�滻��Ϣ", "�滻��" + i + "��");
			black.ba.setFileChanged();

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	public void scrollForTypeMode() {
		if (!st.isDisposed() && black.ba.posForTypeMode != 0) {
			int toppixel;
			if (st.getCaret().getLocation().y > (st.getClientArea().height) / black.ba.posForTypeMode)
				toppixel = st.getTopPixel()
						+ (st.getCaret().getLocation().y - st.getClientArea().height / black.ba.posForTypeMode);
			else
				toppixel = st.getTopPixel()
						- ((st.getClientArea().height / black.ba.posForTypeMode) - st.getCaret().getLocation().y);

			// ������ǰ�༭�Ķ���ĸ߶��Ƿ񳬹��˱༭���߶ȵ�һ��
			Point p = black.ba.currentLineOffest();
			int y_start = st.getLocationAtOffset(p.x).y;
			int y_end = st.getLocationAtOffset(p.y).y;
			int h = st.getClientArea().height / black.ba.posForTypeMode;
			int height = y_end - y_start;

			// ֻ�ڶ������겻��ȣ��Ҷ���ĸ߶Ȳ������༭���߶ȵ�һ��ʱ����
			if (toppixel != st.getTopPixel() && height < h) {
				st.setTopPixel(toppixel);
			}
		}
	}

	/**
	 * ���ֻ���
	 * 
	 * @param b
	 */
	public void typeMode(boolean b) {
		if (b) {
			if (typeModeListener == null) {
				typeModeListener = new KeyListener() {

					@Override
					public void keyReleased(KeyEvent arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void keyPressed(KeyEvent arg0) {
						// TODO Auto-generated method stub
						scrollForTypeMode();
					}
				};
				st.addKeyListener(typeModeListener);
			} else {
				if (typeModeListener != null) {
					st.removeKeyListener(typeModeListener);
				}
			}
		}
	}

	public void addMenu(final StyledText st) {
		Menu menu = new Menu(st);
		st.setMenu(menu);

		MenuItem mntmNewItem_ = new MenuItem(menu, SWT.None);
		mntmNewItem_.setText("���뵱ǰ����	Alt+D");
		mntmNewItem_.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.ba.insertText(time.getCurrentDate("-"), st);
			}
		});
		MenuItem mntmNewItem_1 = new MenuItem(menu, SWT.None);
		mntmNewItem_1.setText("���뵱ǰʱ��	Alt+T");
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.ba.insertText(time.getCurrentTime(), st);
			}
		});

		MenuItem sep = new MenuItem(menu, SWT.SEPARATOR);

		MenuItem mntmNewItem = new MenuItem(menu, SWT.None);
		mntmNewItem.setText("����	Ctrl+C");
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.ba.copyText(st);
			}
		});
		MenuItem copy = black.ba.getMenuItem(menu, "���ƣ����������⣩", SWT.NONE);
		copy.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				black.ba.copyWithoutDocumentTitle();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem menupaset = new MenuItem(menu, SWT.CONTROL);
		menupaset.setText("ճ��Ϊ���ı�	Ctrl+V");
		menupaset.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				black.ba.pasteText(st);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem menucut = new MenuItem(menu, SWT.None);
		menucut.setText("����	Ctrl+X");
		menucut.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				black.ba.cutText(st);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem menuSelectAll = new MenuItem(menu, SWT.None);
		menuSelectAll.setText("ȫѡ	Ctrl+A");
		menuSelectAll.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				black.ba.selectAllText(st);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		new MenuItem(menu, SWT.SEPARATOR);
		MenuItem menu0 = new MenuItem(menu, SWT.None);
		menu0.setText("��ѡ����ı���Ϊ�ĵ�����");
		menu0.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				black.ba.setDocumentTitleBySelectionText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		MenuItem menu01 = new MenuItem(menu, SWT.None);
		menu01.setText("����ѡ�ı�����Ԥ�����б�");
		menu01.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if (!black.getCurrentEditFile().equals(black.ba.getRealFile("Ԥ����")))
					black.ba.addTextToMarkFile(black.text.getSelectionText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		MenuItem menu02 = black.ba.getMenuItem(menu, SWT.NONE);
		menu02.setText("Ϊ��ǰ�༭���ĵ���������");
		menu02.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				black.ba.asCopy();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		black.ba.getMenuItem(st.getMenu(), "", SWT.SEPARATOR);
		MenuItem menuitem = black.ba.getMenuItem(st.getMenu(), "�������ļ�", SWT.CASCADE);
		Menu menu1 = new Menu(menuitem);
		menuitem.setMenu(menu1);
		MenuItem mi1 = black.ba.getMenuItem(menu1, "�ݸ�", SWT.CASCADE);
		Menu menu_ = new Menu(mi1);
		mi1.setMenu(menu_);

		MenuItem mire = black.ba.getMenuItem(menu1, "������", SWT.CASCADE);
		Menu menu_1 = new Menu(mire);
		mire.setMenu(menu_1);
		if (black.fileindex != null) {
			Iterator<String> it = black.fileindex.iterator();
			while (it.hasNext()) {
				final String name = it.next();
				if (black.recycle.getProperty(name) == null) {
					black.ba.getMenuItem(menu_, black.fileInfo.getProperty(name, name), SWT.None)
							.addSelectionListener(new SelectionListener() {

								@Override
								public void widgetSelected(SelectionEvent e) {
									// TODO Auto-generated method stub
									File f = new File(black.projectFile.getParent() + "\\Files\\" + name);
									black.ba.openFile(f, true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent e) {
									// TODO Auto-generated method stub

								}
							});

				} else {
					black.ba.getMenuItem(menu_1, black.fileInfo.getProperty(name, name), SWT.None)
							.addSelectionListener(new SelectionListener() {

								@Override
								public void widgetSelected(SelectionEvent e) {
									// TODO Auto-generated method stub
									black.ba.openFile(new File(black.projectFile.getParent() + "\\Files\\" + name),
											true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent e) {
									// TODO Auto-generated method stub

								}
							});
				}
			}
		}
	}

	public void addKeyAction(checkKey ck) {
		if (ck != null)
			cheakkey.add(ck);
		// else throw new NullPointerException();
	}

	public void removeKeyAction(checkKey ck) {
		if (cheakkey != null) {
			if (ck != null)
				cheakkey.remove(ck);
			// else throw new NullPointerException();
		} // else throw new NullPointerException();
	}

	public void removeAllKeyAction() {
		cheakkey = new ArrayList<>();
	}

	void addKeyActionForNormal() {
		addKeyAction(new checkKey(SWT.CONTROL | 'n') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.openNewProjectDialog();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | 'i') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				new importFiles(black, SWT.None).open();

			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | 'e') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				new exportFiles(black, SWT.None).open();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | 'f') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.findReplaceWord();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | SWT.SHIFT | '=') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.addFileToProject(null);
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | SWT.SHIFT | '-') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.moveFileToRecycle();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | SWT.SHIFT | 'c') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.clearRecycle();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | SWT.SHIFT | 'p') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				new projectInfo(black, SWT.None).open();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | SWT.SHIFT | 'r') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.restoreFile();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | 'g') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				if (black.text != null)
					new editerStyledPanel(black, null).open();
			}
		});
		addKeyAction(new checkKey(SWT.ALT | 'd') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.insertText(time.getCurrentDate("-"), st);
			}
		});
		addKeyAction(new checkKey(SWT.ALT | 't') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.insertText(time.getCurrentTime(), st);
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | 'k') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.wordCountStat(black);
			}
		});
		addKeyAction(new checkKey(SWT.F1) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.showSettings();
			}
		});

		addKeyAction(new checkKey(SWT.F11) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.openWritingView();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | SWT.SHIFT | 'a') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.aboutBlack();
			}
		});

		addKeyAction(new checkKey(SWT.CONTROL | SWT.ALT | SWT.SHIFT | 's') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.getSystemInfo();
				// black.ba.getMessageBox("ϵͳ��Ϣ",
				// "ʣ�������������"+black.getsafe().getTryedDays());
			}
		});
		addKeyAction(new checkKey(SWT.ALT | '/') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				if (black.ba.findi == null)
					black.ba.findInfo();
			}
		});
		addKeyAction(new checkKey(SWT.END) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				st.setRedraw(false);
				st.setSelection(black.ba.getEndOffest(st));
				scrollForTypeMode();
				st.setRedraw(true);

			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | '1') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				if (black.wv != null && !black.wv.isDisposed()) {
					if (black.wv.getAlpha() == 255)
						black.wv.setAlpha(200);
					else if(black.wv.getAlpha() == 200)
						black.wv.setAlpha(150);
					else if(black.wv.getAlpha() == 150)
						black.wv.setAlpha(100);
					else if(black.wv.getAlpha() == 100)
						black.wv.setAlpha(255);
				}
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | '2') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				if (black.wv != null && !black.wv.isDisposed()) {
					if (black.wv.getBackground().equals(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND)))
						black.ba.changeColor(black.color_black, black.color_white);
					else if (black.wv.getBackground().equals(black.color_white))
						black.ba.changeColor(black.color_black, black.color_eye);
					else if (black.wv.getBackground().equals(black.color_eye))
						black.ba.changeColor(black.color_white, black.color_black);
					else if (black.wv.getBackground().equals(black.color_black))
						black.ba.changeColor(black.color_black,
								SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				}
			}
		});

	}

	void addDefaultKeyAction() {
		addKeyAction(new checkKey(SWT.CONTROL | 'a') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.selectAllText(st);
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | 's') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.saveFile(false, false);
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | 'z') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.Undo();
			}
		});
		addKeyAction(new checkKey(SWT.ALT | 'z') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.Redo();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | '=') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.zoomOut();
			}
		});
		addKeyAction(new checkKey(SWT.CONTROL | '-') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.zoomIn();
			}
		});
		addKeyAction(new checkKey(SWT.ALT | 'j') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.insertText("��", st);
			}
		});
		addKeyAction(new checkKey(SWT.ALT | 'k') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.insertText("��", st);
			}
		});
		addKeyAction(new checkKey(SWT.ESC) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				if (black.ba.findi != null && !black.ba.findi.isDisposed()) {
					black.ba.findi.dispose();
				} else if (black.wv != null && !black.wv.isDisposed()) {
					black.wv.exit();
				}
			}
		});
		addKeyAction(new checkKey(SWT.CTRL | 'm') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.editMarkFile();
			}
		});
		addKeyAction(new checkKey(SWT.CTRL | 'q') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				black.ba.fastOpenFile();
			}
		});
		addKeyAction(new checkKey(SWT.CTRL | SWT.SHIFT | '/') {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				for (int i = 0; i < 20; i++)
					st.append("\n");
			}
		});

	}

	void addDefaultKeyListener() {
		if (defaultKeyListener == null) {
			defaultKeyListener = new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {

					if (cheakkey != null) {
						for (int i = 0; i < cheakkey.size(); i++) {
							checkKey chkey = cheakkey.get(i);
							if ((e.stateMask | e.keyCode) == chkey.key) {
								chkey.action();
							}
						}
					}
				}
			};
			st.addKeyListener(defaultKeyListener);
			addDefaultKeyAction();
		}
	}
}

abstract class command implements Serializable {
	static final long serialVersionUID = 42L;
	String command, describe;
	boolean needmore;

	public command(String command) {
		this.command = command;
	}

	public command(String command, String describe) {
		this.command = command;
		this.describe = describe;
	}

	public command(String command, String describe, boolean needmore) {
		this.command = command;
		this.describe = describe;
		this.needmore = needmore;
	}

	public command(String command, boolean needmore) {
		this.command = command;
		this.needmore = needmore;
	}

	public abstract void action(TextRegion command_tr);
}

abstract class checkKey implements Serializable {
	static final long serialVersionUID = 42L;
	KeyEvent e;
	int key;

	/**
	 * �󶨼�
	 * 
	 * @param key��ϼ�����Ctrl|Alt|Shift|'t'
	 */
	public checkKey(int key) {
		this.key = key;
	}

	void setKeyEvent(KeyEvent e) {
		this.e = e;
	}

	/**
	 * �����ϼ��󶨵Ķ���
	 */
	public abstract void action();
}
