package yang.app.black;


import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
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
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.notes.Note;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.TrackingRefUpdate;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

import yang.demo.allPurpose.fileTool;
import yang.demo.allPurpose.gitTool;
import yang.demo.allPurpose.time;
import yang.demo.allPurpose.yangIO;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JGit API����
 */
public class test {

    public static String remotePath = "https://piiiiq@bitbucket.org/piiiiq/black-project-nov.git";//Զ�̿�·��
    public static String localPath = "D:\\test\\project\\";//�������вֿ⵽����·��
    public static String initPath = "D:\\test\\testgit\\";//����·���½�
    public static String username = "piiiiq";
    public static String password = "nihaoma,.+";
    
    public static void main(String args[]) throws IOException, JGitInternalException, GitAPIException, URISyntaxException{
       String in = "������³˹��³˹������˹����˹��³˹������˹�Ǹ�����";
       String str = "��³˹������˹";
    	int lastIndexOf = blackAction.lastIndexOf(str, in);
    	System.out.println(lastIndexOf);
//       String path = "d://test//git//test01";
//       String path2 = "C://Users//Administrator//Documents//blacktest//2017.01.133";
//       String[] s = new String[]{"refs/heads/nov","refs/heads/master"};
//       
//       ArrayList<RevCommit> commits = gitTool.getCommitsFromBranch(path, s);
//       for(RevCommit r:commits){
//    	   System.out.println(r.getFullMessage());
//       }
//       
//      
//   
    }
    public static Ref getBranch(String repositoryPath,ObjectId id) throws IOException, GitAPIException{
        Git git = new Git(new FileRepository(repositoryPath+"/.git"));
        ListBranchCommand list = git.branchList().setListMode(ListMode.ALL);
        List<Ref> call = list.call();
        Ref ref = null;
        for(Ref r:call){
        	if(r.getObjectId().equals(id)){
        		ref = r;
        	}
        }
        return ref;
    }
    public void cloneAll(String host) throws InvalidRemoteException, TransportException, GitAPIException{
    	Git git = Git.cloneRepository().setURI(host).setNoCheckout(true).setCloneAllBranches(true).call();
    	List<Ref> branches = git.branchList().setListMode( ListMode.REMOTE ).call();
    	for(Ref r:branches){
    		System.out.println(r.getName());
    	}
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