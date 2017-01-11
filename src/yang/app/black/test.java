package yang.app.black;


import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

import yang.demo.allPurpose.fileTool;
import yang.demo.allPurpose.gitTool;
import yang.demo.allPurpose.time;
import yang.demo.allPurpose.yangIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JGit API����
 */
public class test {

    public static String remotePath = "https://piiiiq@bitbucket.org/piiiiq/black-project-nov.git";//Զ�̿�·��
    public static String localPath = "D:\\test\\project\\";//�������вֿ⵽����·��
    public static String initPath = "D:\\test\\testgit\\";//����·���½�
    public static String username = "piiiiq";
    public static String password = "nihaoma,.+";
    
    public static void main(String args[]) throws IOException, JGitInternalException, GitAPIException{
       
       String path = "d://test//git//testgit";
        
    	gitTool.createGitRepository(path);
  	// gitTool.changeRemoteBranch(path, "testttblack");
      // gitTool.createGitRepository(path);
    	try {
			gitTool.commit(path,time.getCurrentTime()+"����");
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			System.out.println("û���ļ�ƥ��");
		}
        
    	
//    	 git.cloneRepository().setURI(remotePath).setBranch("testttblack")
//    	 .setDirectory(new File(path)).call();
//    	 Ref mas = git.getRepository().findRef("testttblack");
    	// gitTool.CloneFromRemote(path, remotePath, "testttblack", username, password);
//         git.commit().setAll(true)
//         	.setMessage("���")
//         .call();
       // gitTool.createNewBranch("testttblack", path);
    	
    	//new test().testPush();
    	//if(mas == null) return;
    	try {
			gitTool.pushToRemote(path, remotePath, username, password,true);
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	System.out.println("ok");
    	//t.testPush();
//    	t.testCreate();
//    	t.testClone();
    
    }
   
    //---------------
    

    /**
    * �����½��ֿ�
    */
    @Test
    public void testCreate() throws IOException {
        //�����½��ֿ��ַ
        Repository newRepo = FileRepositoryBuilder.create(new File(initPath + "/.git"));
        newRepo.create();
    }

    /**
    * ���زֿ������ļ�
    */
    @Test
    public void testAdd() throws IOException, GitAPIException {
        File myfile = new File(localPath + "/myfile.txt");
        myfile.createNewFile();
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git"));

        //����ļ�
        git.add().addFilepattern("myfile").call();
    }

    /**
    * �����ύ����
    */
    @Test
    public void testCommit() throws IOException, GitAPIException,
            JGitInternalException {
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git"));
        //�ύ����
        git.commit().setMessage("������ļ�")
        .setAuthor("������", "yangisboy@msn.com")
        //.setAll(true)
        .call();
//        git.commit().setMessage("д�����ļ�")
//        .call();
    }


    /**
    * ��ȡԶ�ֿ̲����ݵ�����
    */
    @Test
    public void testPull() throws IOException, GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new
                UsernamePasswordCredentialsProvider(username,password);
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git"));
        git.pull().setRemoteBranchName("master").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
    }

    /**
    * push���ش��뵽Զ�ֿ̲��ַ
    */
    @Test
    public void testPush() throws IOException, JGitInternalException,
            GitAPIException {

        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider(username,password);
        //git�ֿ��ַ
        Git git = new Git(new FileRepository(localPath+"/.git")); 
        
        git.push().setRemote(remotePath).setCredentialsProvider(usernamePasswordCredentialsProvider)
        .setPushAll()
        .call();
    }
}